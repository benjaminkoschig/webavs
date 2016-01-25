/*
 * Créé le Nov 7, 2005, dda
 */
package globaz.helios.process.modele;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.helios.api.ICGJournal;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGEcritureDoubleViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.db.modeles.CGEnteteModeleEcritureViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcritureViewBean;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dda
 * 
 */
public class CGProcessEcritureModele extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_EXECUTION_DU_MODELE_ERROR = "EXECUTION_DU_MODELE_ERROR";
    private static final String LABEL_EXECUTION_DU_MODELE_OK = "EXECUTION_DU_MODELE_OK";
    private static final String LABEL_MODEL_LISTE_ECRITURES_DOUBLES_NON_VALIDE = "MODEL_LISTE_ECRITURES_DOUBLES_NON_VALIDE";

    private List attachedEcrituresDoubles = new ArrayList();
    private String date;
    private List ecrituresCollectives = new ArrayList();

    private List ecrituresDoubles = new ArrayList();
    private String idExerciceComptable;

    private String idJournal;
    private String idMandat;

    private String interMandat;
    private String libelleJournal;

    private String piece;
    private String saisieEcran = "false";

    /**
     * Constructor for CGEcritureModele.
     */
    public CGProcessEcritureModele() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGEcritureModele.
     * 
     * @param parent
     */
    public CGProcessEcritureModele(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGEcritureModele.
     * 
     * @param session
     * @throws Exception
     */
    public CGProcessEcritureModele(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            executeEcrituresDouble();
            executeEcrituresCollective();
        } catch (Exception e) {
            setSendCompletionMail(true);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        if (!JadeStringUtil.isBlank(getInterMandat()) && "true".equals(getInterMandat())) {
            setSendCompletionMail(true);
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(false);
        setSendMailOnError(true);

        if (!validateEcrituresDoubles()) {
            this._addError(getTransaction(), CGProcessEcritureModele.LABEL_MODEL_LISTE_ECRITURES_DOUBLES_NON_VALIDE);
        }
    }

    /**
     * Ajout des écritures collectives du modèle prérempli par l'utilisateur. <BR/>
     * Créer les entêtes et écritures collectives du journal.
     * 
     * @throws Exception
     */
    private void executeEcrituresCollective() throws Exception {
        String lastIdEntete = new String();
        String userLibelle = new String();
        CGExerciceComptable idExerciceComptableForDate = null;
        String idMandat = new String();

        CGGestionEcritureViewBean ecritures = null;
        ArrayList ecrituresList = new ArrayList();

        for (int i = 0; i < getEcrituresCollectives().size(); i++) {
            CGLigneModeleEcritureViewBean ligneViewBean = (CGLigneModeleEcritureViewBean) getEcrituresCollectives()
                    .get(i);

            String userMontant = ligneViewBean.getMontant();
            String userMontantMonnaie = ligneViewBean.getMontantMonnaie();
            String userCours = ligneViewBean.getCoursMonnaie();
            String userIdCentreCharge = ligneViewBean.getIdCentreCharge();

            if (!JadeStringUtil.isBlank(ligneViewBean.getLibelle())) {
                userLibelle = ligneViewBean.getLibelle();
            }

            ligneViewBean.setSession(getSession());
            ligneViewBean.retrieve(getTransaction());

            if (!ligneViewBean.isNew()) {
                if (!ligneViewBean.getIdEnteteModeleEcriture().equals(lastIdEntete)) {
                    ecritures = new CGGestionEcritureViewBean();
                    ecritures.setSession(getSession());
                    if ("true".equalsIgnoreCase(getSaisieEcran())) {
                        ecritures.setSaisieEcran(getSaisieEcran());
                    }

                    CGEnteteModeleEcritureViewBean enteteModele = ligneViewBean.getEnteteViewBean();
                    if (JadeStringUtil.isIntegerEmpty(this.getIdJournal())) {
                        idMandat = enteteModele.getIdMandat();
                        idExerciceComptableForDate = enteteModele.getExerciceForDate(getDate());
                    } else {
                        idMandat = getIdMandat();
                        idExerciceComptableForDate = getExerciceComptable();
                    }

                    ecritures.setDateValeur(getDate());
                    ecritures.setIdJournal(this.getIdJournal(idMandat,
                            idExerciceComptableForDate.getIdExerciceComptable()));
                    ecritures.setPiece(getPiece());
                }

                CGEcritureViewBean ecriture = new CGEcritureViewBean();
                ecriture.setSession(getSession());
                ecriture.setIdExerciceComptable(idExerciceComptableForDate.getIdExerciceComptable());
                ecriture.setIdMandat(idMandat);
                ecriture.setIdJournal(this.getIdJournal(idMandat, idExerciceComptableForDate.getIdExerciceComptable()));
                ecriture.setDate(getDate());
                ecriture.setLibelle(userLibelle);
                ecriture.setPiece(getPiece());
                ecriture.setCodeDebitCredit(ligneViewBean.getCodeDebitCredit());
                ecriture.setIdCompte(ligneViewBean.getIdCompte());
                ecriture.setIdCentreCharge(userIdCentreCharge);

                ecriture.setIdExterneCompte(ligneViewBean.getIdExterne(idExerciceComptableForDate
                        .getIdExerciceComptable()));

                if (!JadeStringUtil.isDecimalEmpty(userMontant)) {
                    ecriture.setMontant(userMontant);
                }

                if (!JadeStringUtil.isDecimalEmpty(userMontantMonnaie)) {
                    ecriture.setMontantMonnaie(userMontantMonnaie);
                }

                if (!JadeStringUtil.isDecimalEmpty(userCours)) {
                    ecriture.setCoursMonnaie(userCours);
                }

                ecrituresList.add(ecriture);

                CGLigneModeleEcritureViewBean nextLigne = null;
                if (i < (getEcrituresCollectives().size() - 1)) {
                    nextLigne = new CGLigneModeleEcritureViewBean();
                    nextLigne.setIdLigneModeleEcriture(((CGLigneModeleEcritureViewBean) getEcrituresCollectives().get(
                            i + 1)).getIdLigneModeleEcriture());
                    nextLigne.setSession(getSession());
                    nextLigne.retrieve(getTransaction());
                }

                if ((i == (getEcrituresCollectives().size() - 1))
                        || ((nextLigne != null) && !ligneViewBean.getIdEnteteModeleEcriture().equals(
                                nextLigne.getIdEnteteModeleEcriture()))) {
                    ecritures.setEcritures(ecrituresList);

                    CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);

                    ecrituresList = new ArrayList();
                }

                lastIdEntete = ligneViewBean.getIdEnteteModeleEcriture();
            }

        }
    }

    /**
     * Ajout des écritures doubles du modèle prérempli par l'utilisateur. <BR/>
     * Créer les entêtes et écritures doubles du journal.
     * 
     * @throws Exception
     */
    private void executeEcrituresDouble() throws Exception {
        if (!validateEcrituresDoubles()) {
            throw new Exception(CGProcessEcritureModele.LABEL_MODEL_LISTE_ECRITURES_DOUBLES_NON_VALIDE);
        }

        for (int i = 0; i < getEcrituresDoubles().size(); i++) {
            CGLigneModeleEcritureViewBean ligneViewBean = (CGLigneModeleEcritureViewBean) getEcrituresDoubles().get(i);

            String userMontant = ligneViewBean.getMontant();
            String userMontantMonnaie = ligneViewBean.getMontantMonnaie();
            String userCours = ligneViewBean.getCoursMonnaie();
            String userLibelle = ligneViewBean.getLibelle();
            String userIdCentreCharge = ligneViewBean.getIdCentreCharge();

            ligneViewBean.setSession(getSession());
            ligneViewBean.retrieve(getTransaction());

            CGEnteteModeleEcritureViewBean entete = ligneViewBean.getEnteteViewBean();

            String idMandat;
            CGExerciceComptable idExerciceComptableForDate;
            if (JadeStringUtil.isIntegerEmpty(this.getIdJournal())) {
                idMandat = entete.getIdMandat();
                idExerciceComptableForDate = entete.getExerciceForDate(getDate());
            } else {
                idMandat = getIdMandat();
                idExerciceComptableForDate = getExerciceComptable();
            }

            if (!ligneViewBean.isNew()) {
                CGLigneModeleEcritureViewBean attachedLigneViewBean = (CGLigneModeleEcritureViewBean) getAttachedEcrituresDoubles()
                        .get(i);

                String userAttachedIdCentreCharge = attachedLigneViewBean.getIdCentreCharge();

                attachedLigneViewBean.setSession(getSession());
                attachedLigneViewBean.retrieve(getTransaction());

                if (!attachedLigneViewBean.isNew()) {
                    CGEcritureDoubleViewBean ecriture = new CGEcritureDoubleViewBean();
                    if ("true".equalsIgnoreCase(getSaisieEcran())) {
                        ecriture.setSaisieEcran(getSaisieEcran());
                    }
                    ecriture.setSession(getSession());
                    ecriture.setIdJournal(this.getIdJournal(idMandat,
                            idExerciceComptableForDate.getIdExerciceComptable()));
                    ecriture.setDate(getDate());
                    ecriture.setLibelle(userLibelle);
                    ecriture.setPiece(getPiece());

                    if (!JadeStringUtil.isDecimalEmpty(userMontant)) {
                        FWCurrency montant = new FWCurrency(userMontant);
                        if (montant.isPositive()) {
                            ecriture.setMontant(montant.toString());

                            ecriture.setIdCompteDebite(ligneViewBean.getIdCompte());
                            ecriture.setIdCompteCredite(attachedLigneViewBean.getIdCompte());

                            ecriture.setIdExterneCompteDebite(ligneViewBean.getIdExterne(idExerciceComptableForDate
                                    .getIdExerciceComptable()));
                            ecriture.setIdExterneCompteCredite(attachedLigneViewBean
                                    .getIdExterne(idExerciceComptableForDate.getIdExerciceComptable()));

                            ecriture.setIdCentreChargeDebite(userIdCentreCharge);
                            ecriture.setIdCentreChargeCredite(userAttachedIdCentreCharge);
                        } else {
                            montant.negate();
                            ecriture.setMontant(montant.toString());

                            ecriture.setIdCompteCredite(ligneViewBean.getIdCompte());
                            ecriture.setIdCompteDebite(attachedLigneViewBean.getIdCompte());

                            ecriture.setIdExterneCompteDebite(attachedLigneViewBean
                                    .getIdExterne(idExerciceComptableForDate.getIdExerciceComptable()));
                            ecriture.setIdExterneCompteCredite(ligneViewBean.getIdExterne(idExerciceComptableForDate
                                    .getIdExerciceComptable()));

                            ecriture.setIdCentreChargeCredite(userIdCentreCharge);
                            ecriture.setIdCentreChargeDebite(userAttachedIdCentreCharge);
                        }
                    }

                    if (!JadeStringUtil.isDecimalEmpty(userMontantMonnaie)) {
                        FWCurrency montantMonnaie = new FWCurrency(userMontantMonnaie);
                        if (montantMonnaie.isPositive()) {
                            ecriture.setMontantMonnaie(montantMonnaie.toString());

                            ecriture.setIdCompteDebite(ligneViewBean.getIdCompte());
                            ecriture.setIdCompteCredite(attachedLigneViewBean.getIdCompte());

                            ecriture.setIdExterneCompteDebite(ligneViewBean.getIdExterne(idExerciceComptableForDate
                                    .getIdExerciceComptable()));
                            ecriture.setIdExterneCompteCredite(attachedLigneViewBean
                                    .getIdExterne(idExerciceComptableForDate.getIdExerciceComptable()));

                            ecriture.setIdCentreChargeDebite(userIdCentreCharge);
                            ecriture.setIdCentreChargeCredite(userAttachedIdCentreCharge);
                        } else {
                            montantMonnaie.negate();
                            ecriture.setMontantMonnaie(montantMonnaie.toString());

                            ecriture.setIdCompteCredite(ligneViewBean.getIdCompte());
                            ecriture.setIdCompteDebite(attachedLigneViewBean.getIdCompte());

                            ecriture.setIdExterneCompteDebite(attachedLigneViewBean
                                    .getIdExterne(idExerciceComptableForDate.getIdExerciceComptable()));
                            ecriture.setIdExterneCompteCredite(ligneViewBean.getIdExterne(idExerciceComptableForDate
                                    .getIdExerciceComptable()));

                            ecriture.setIdCentreChargeCredite(userIdCentreCharge);
                            ecriture.setIdCentreChargeDebite(userAttachedIdCentreCharge);
                        }
                    }

                    if (!JadeStringUtil.isDecimalEmpty(userCours)) {
                        ecriture.setCours(userCours);
                    }

                    ecriture.addEcriture(getTransaction());
                }
            }

        }
    }

    /**
     * @return
     */
    public List getAttachedEcrituresDoubles() {
        return attachedEcrituresDoubles;
    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * @return
     */
    public List getEcrituresCollectives() {
        return ecrituresCollectives;
    }

    /**
     * @return
     */
    public List getEcrituresDoubles() {
        return ecrituresDoubles;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel(CGProcessEcritureModele.LABEL_EXECUTION_DU_MODELE_ERROR);
        } else {
            return getSession().getLabel(CGProcessEcritureModele.LABEL_EXECUTION_DU_MODELE_OK);
        }
    }

    /**
     * Retourne l'exercice comptable en cours.
     * 
     * @return
     * @throws Exception
     */
    private CGExerciceComptable getExerciceComptable() throws Exception {
        CGExerciceComptable exercice = new CGExerciceComptable();
        exercice.setSession(getSession());

        exercice.setIdExerciceComptable(getIdExerciceComptable());

        exercice.retrieve(getTransaction());

        return exercice;
    }

    /**
     * @return
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * @return
     */
    public String getIdJournal() {
        return idJournal;
    }

    public String getIdJournal(String idMandat, String idExerciceComptable) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(this.getIdJournal())) {
            return this.getIdJournal();
        }

        CGJournalManager manager = new CGJournalManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForDateValeur(getDate());
        manager.setForDate(JACalendar.today().toString());

        manager.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
        manager.setForIdTypeJournal(CGJournal.CS_TYPE_ECRITURES_MODELES);
        manager.setForProprietaire(getSession().getUserId());

        manager.find(getTransaction());

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (!manager.isEmpty()) {
            return ((CGJournal) manager.getFirstEntity()).getIdJournal();
        } else {
            CGJournal journal = new CGJournal();
            journal.setSession(getSession());

            journal.setLibelle(getLibelleJournal());
            journal.setDateValeur(getDate());
            journal.setDate(JACalendar.today().toString());
            journal.setIdExerciceComptable(idExerciceComptable);

            journal.setIdPeriodeComptable(getIdPeriodeComptable(getDate(), idExerciceComptable));
            journal.setEstPublic(new Boolean(false));
            journal.setEstConfidentiel(new Boolean(false));
            journal.setIdEtat(ICGJournal.CS_ETAT_OUVERT);
            journal.setIdTypeJournal(CGJournal.CS_TYPE_ECRITURES_MODELES);
            journal.setProprietaire(getSession().getUserId());

            journal.add(getTransaction());

            if (journal.hasErrors() || journal.isNew()) {
                throw new Exception(journal.getErrors().toString());
            }

            return journal.getIdJournal();
        }
    }

    /**
     * @return
     */
    public String getIdMandat() {
        return idMandat;
    }

    private String getIdPeriodeComptable(String dateValeur, String idExerciceComptable) throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForPeriodeOuverte(true);
        manager.setForDateInPeriode(dateValeur);

        manager.find(getTransaction());

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception("No periode found !");
        }

        return ((CGPeriodeComptable) manager.getFirstEntity()).getIdPeriodeComptable();
    }

    public String getInterMandat() {
        return interMandat;
    }

    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @return
     */
    public String getPiece() {
        return piece;
    }

    public String getSaisieEcran() {
        return saisieEcran;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param list
     */
    public void setAttachedEcrituresDoubles(List list) {
        attachedEcrituresDoubles = list;
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param list
     */
    public void setEcrituresCollectives(List list) {
        ecrituresCollectives = list;
    }

    /**
     * @param list
     */
    public void setEcrituresDoubles(List list) {
        ecrituresDoubles = list;
    }

    /**
     * @param string
     */
    public void setIdExerciceComptable(String string) {
        idExerciceComptable = string;
    }

    /**
     * @param string
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }

    /**
     * @param string
     */
    public void setIdMandat(String string) {
        idMandat = string;
    }

    public void setInterMandat(String interMandat) {
        this.interMandat = interMandat;
    }

    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    /**
     * @param string
     */
    public void setPiece(String string) {
        piece = string;
    }

    public void setSaisieEcran(String newSaisieEcran) {
        saisieEcran = newSaisieEcran;
    }

    private boolean validateEcrituresDoubles() {
        if (getEcrituresDoubles().size() != getAttachedEcrituresDoubles().size()) {
            return false;
        } else {
            return true;
        }
    }

}
