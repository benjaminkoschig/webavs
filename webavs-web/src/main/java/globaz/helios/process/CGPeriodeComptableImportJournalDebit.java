/**
 *
 */
package globaz.helios.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.db.journaldebits.CGExtractionOperationOsiris;
import globaz.helios.db.journaldebits.CGExtractionOperationOsirisManager;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import ch.horizon.jaspe.util.JADate;

/**
 * @author sel
 * 
 */
public class CGPeriodeComptableImportJournalDebit extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String JOURNAL_DEBIT_REFERENCE_EXTERNE = "JDB";
    /**
     * Cache local des exercices
     */
    private HashMap<String, CGExerciceComptable> exercicesMap = null;
    private String idPeriodeComptable = "";
    private CGPeriodeComptable periode = null;
    private String idMandat = "";

    public CGPeriodeComptableImportJournalDebit() {
        super();
        exercicesMap = new HashMap<String, CGExerciceComptable>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        // Annulation des journaux déjà comptabilisés
        if (JadeStringUtil.isBlankOrZero(getIdMandat())) {
        annuleJournalComptabilise();
        } else {
            annuleJournalComptabiliseByMandat(getIdMandat());
        }

        // Vérification des rows obtenues
        CGExtractionOperationOsirisManager ext = new CGExtractionOperationOsirisManager();
        ext.setDateDebut((new JADate(retrievePeriodeComptable().getDateDebut())).toStrAMJ());
        ext.setDateFin((new JADate(retrievePeriodeComptable().getDateFin())).toStrAMJ());
        ext.setIdMandat(getIdMandat());
        ext.setSession(getSession());
        ext.find(getTransaction(), BManager.SIZE_NOLIMIT);

        @SuppressWarnings("unchecked")
        Iterator<CGExtractionOperationOsiris> it2 = ext.iterator();
        while (it2.hasNext()) {
            CGExtractionOperationOsiris extraction = it2.next();

            if (JadeStringUtil.isBlankOrZero(extraction.getCompteCourantDest())
                    || JadeStringUtil.isBlankOrZero(extraction.getContrePartieDest())
                    || JadeStringUtil.isBlankOrZero(extraction.getIdMandat())) {
                this._addError("Error !!" + extraction.getCompteCourantSrc() + " " + extraction.getRubriqueSrc());
                getMemoryLog().logMessage(
                        "Error !!" + extraction.getCompteCourantSrc() + " " + extraction.getRubriqueSrc(),
                        FWMessage.ERREUR, this.getClass().getName());
                return false;
            }

            if (JadeStringUtil.isBlankOrZero(extraction.getMontant())) {
                continue;
            }

            // Créer journal pour mandat
            CGJournal journal = findOrCreateJournal(extraction.getIdMandat());

            if (getTransaction().hasErrors()) {
                this._addError(getTransaction(), "Error : " + getTransaction().getErrors());
                return false;
            }

            // Créer écriture double
            addEcritureDouble(extraction, journal.getIdJournal());

            if (getTransaction().hasErrors()) {
                this._addError(getTransaction(), "Error : " + getTransaction().getErrors());
                return false;
            }
        }

        // Comptabilisation des journaux finaux
        comptabiliserJournaux();

        return true;
    }

    /**
     * @param idMandat
     * @param idJournal
     * @param montant
     * @throws Exception
     */
    private void addEcritureDouble(CGExtractionOperationOsiris extraction, String idJournal) throws Exception {
        CGExerciceComptable exercice = findExerciceComptable(extraction.getIdMandat());

        String annee = getAnnee(retrievePeriodeComptable().getDateDebut());
        String mois = getMois(retrievePeriodeComptable().getDateDebut());
        BigDecimal montant = new BigDecimal(extraction.getMontant());

        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();

        ecritureCrebit.setLibelle(getSession().getLabel("JOURNALDEBIT_LIBELLE_JOURNAL") + " " + mois + " " + annee);
        ecritureDebit.setLibelle(getSession().getLabel("JOURNALDEBIT_LIBELLE_JOURNAL") + " " + mois + " " + annee);
        ecritureCrebit.setIdExerciceComptable(exercice.getIdExerciceComptable());
        ecritureDebit.setIdExerciceComptable(exercice.getIdExerciceComptable());
        ecritureCrebit.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        ecritureDebit.setCodeDebitCredit(CodeSystem.CS_DEBIT);

        if (montant.signum() == 1) {
            ecritureCrebit.setIdExterneCompte(extraction.getContrePartieDest());
            ecritureDebit.setIdExterneCompte(extraction.getCompteCourantDest());
            ecritureCrebit.setMontant(montant.toString());
            ecritureDebit.setMontant(montant.toString());
        } else {
            ecritureCrebit.setIdExterneCompte(extraction.getCompteCourantDest());
            ecritureDebit.setIdExterneCompte(extraction.getContrePartieDest());
            ecritureCrebit.setMontant(montant.abs().toString());
            ecritureDebit.setMontant(montant.abs().toString());
        }

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();
        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);

        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());
        ecritures.setDateValeur(retrievePeriodeComptable().getDateFin());
        ecritures.setIdJournal(idJournal);
        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
    }

    /**
     * Annulation des journaux déjà comptabilisés
     * 
     * @throws Exception
     */
    private void annuleJournalComptabilise() throws Exception {
        String annee = getAnnee(retrievePeriodeComptable().getDateDebut());
        String mois = getMois(retrievePeriodeComptable().getDateDebut());

        CGJournalManager mgr = new CGJournalManager();
        mgr.setSession(getSession());
        mgr.setForReferenceExterne(CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE + annee + mois);
        mgr.setForIdEtat(ICGJournal.CS_ETAT_COMPTABILISE);
        mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

        @SuppressWarnings("unchecked")
        Iterator<CGJournal> it = mgr.iterator();
        while (it.hasNext()) {
            CGJournal journal = it.next();

            journal.annuler(getTransaction());
        }
    }

    /**
     * @throws Exception
     */
    private void annuleJournalComptabiliseByMandat(String idMandat) throws Exception {
        String annee = getAnnee(retrievePeriodeComptable().getDateDebut());
        String mois = getMois(retrievePeriodeComptable().getDateDebut());
        String idExercice = findExerciceComptable(idMandat).getIdExerciceComptable();

        CGJournalManager mgrJournal = new CGJournalManager();
        mgrJournal.setSession(getSession());
        mgrJournal.setForIdExerciceComptable(idExercice);
        mgrJournal.setForReferenceExterne(CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE + annee
                + mois);
        mgrJournal.setForIdEtat(ICGJournal.CS_ETAT_COMPTABILISE);
        mgrJournal.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (mgrJournal.size() == 1) {
            CGJournal journal = (CGJournal) mgrJournal.getFirstEntity();
            journal.annuler(getTransaction());
        } else {
            throw new Exception(
                    "Error in CGPeriodeComptableImportJournalDebit.annuleJournalComptabiliseByMandat : several results ["
                            + idExercice + "-" + CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE
                            + annee + mois + "]");
        }
    }

    /**
     * @throws Exception
     */
    private void comptabiliserJournaux() throws Exception {
        if (JadeStringUtil.isBlankOrZero(getIdMandat())) {
        String annee = getAnnee(retrievePeriodeComptable().getDateDebut());
        String mois = getMois(retrievePeriodeComptable().getDateDebut());
        CGJournalManager mgrJournal = new CGJournalManager();
        mgrJournal.setSession(getSession());
        mgrJournal.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
            mgrJournal.setForReferenceExterne(CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE
                    + annee + mois);
        mgrJournal.find(getTransaction(), BManager.SIZE_NOLIMIT);
        @SuppressWarnings("unchecked")
        Iterator<CGJournal> it = mgrJournal.iterator();
        while (it.hasNext()) {
            CGJournal journal = it.next();
            journal.comptabiliser(this);
        }
        } else {
            CGJournal journal = findOrCreateJournal(getIdMandat());
            journal.comptabiliser(this);
        }
    }

    /**
     * Retourne l'exercice comptable du jour correspondant au mandat.
     * 
     * @param idMandat
     * @return
     * @throws Exception
     */
    protected CGExerciceComptable findExerciceComptable(String idMandat) throws Exception {
        if (!exercicesMap.containsKey(idMandat)) {
            CGExerciceComptableManager mgr = new CGExerciceComptableManager();
            mgr.setSession(getSession());
            mgr.setForIdMandat(idMandat);
            mgr.setBetweenDateDebutDateFin(retrievePeriodeComptable().getDateFin());
            mgr.find(BManager.SIZE_NOLIMIT);

            if (mgr.size() == 0) {
                throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_EXERCICE_NOT_FIND") + " " + idMandat);
            }
            exercicesMap.put(idMandat, (CGExerciceComptable) mgr.getFirstEntity());
        }

        return exercicesMap.get(idMandat);
    }

    /**
     * @param annee
     * @param mois
     * @param mgr
     * @param extraction
     * @return
     * @throws Exception
     */
    private CGJournal findOrCreateJournal(String idMandat) throws Exception {
        String annee = getAnnee(retrievePeriodeComptable().getDateDebut());
        String mois = getMois(retrievePeriodeComptable().getDateDebut());
        String idExercice = findExerciceComptable(idMandat).getIdExerciceComptable();

        CGJournalManager mgrJournal = new CGJournalManager();
        mgrJournal.setSession(getSession());
        mgrJournal.setForIdExerciceComptable(idExercice);
        mgrJournal.setForReferenceExterne(CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE + annee
                + mois);
        mgrJournal.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
        mgrJournal.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if (mgrJournal.size() == 0) {
            CGJournal journal = new CGJournal();
            journal.setSession(getSession());
            journal.setDateValeur(retrievePeriodeComptable().getDateFin());
            journal.setReferenceExterne(CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE + annee
                    + mois);
            journal.setLibelle(getSession().getLabel("JOURNALDEBIT_LIBELLE_JOURNAL") + " " + mois + " " + annee);
            journal.setIdExerciceComptable(idExercice);
            journal.setIdPeriodeComptable(getPeriodeComptableDestination(idExercice,
                    retrievePeriodeComptable().getDateFin()).getIdPeriodeComptable());

            journal.add(getTransaction());

            return journal;
        } else {
            if (mgrJournal.size() > 1) {
                throw new Exception(
                        "Error in CGPeriodeComptableImportJournalDebit.findOrCreateJournal : several results ["
                                + idExercice + "-"
                                + CGPeriodeComptableImportJournalDebit.JOURNAL_DEBIT_REFERENCE_EXTERNE + annee + mois
                                + "]");
            } else {
                return (CGJournal) mgrJournal.getFirstEntity();
            }
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private String getAnnee(String date) throws Exception {
        if (date.length() < 10) {
            return "";
        }
        return date.substring(6, 10);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("JOURNALDEBIT_OBJET_MAIL");
    }

    /**
     * @return the idPeriodeComptable
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * @return
     * @throws Exception
     */
    private String getMois(String date) throws Exception {
        if (date.length() < 10) {
            return "";
        }
        return date.substring(3, 5);
    }

    /**
     * @param idExerciceComptable
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable getPeriodeComptableDestination(String idExerciceComptable, String dateValeur)
            throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForDateInPeriode(dateValeur);
        manager.setForPeriodeOuverte(true);

        manager.find(getTransaction());

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_PERIODE_NOT_FOUND") + " ["
                    + idExerciceComptable + "-" + dateValeur + "]");
        }

        return (CGPeriodeComptable) manager.getFirstEntity();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @return
     * @throws Exception
     */
    public CGPeriodeComptable retrievePeriodeComptable() throws Exception {
        if ((periode == null) || (!periode.getIdPeriodeComptable().equals(getIdPeriodeComptable()))) {
            periode = new CGPeriodeComptable();
            periode.setSession(getSession());
            periode.setIdPeriodeComptable(getIdPeriodeComptable());
            periode.retrieve();
        }

        return periode;
    }

    /**
     * @param idPeriodeComptable
     *            the idPeriodeComptable to set
     */
    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
    }

    /**
     * @return the idMandat
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * @param idMandat the idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

}
