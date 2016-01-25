package globaz.osiris.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.remboursementauto.CARemboursementAutomatique;
import globaz.osiris.db.remboursementauto.CARemboursementAutomatiqueManager;

/**
 * Remboursement automatique. <br/>
 * Cette classe permet de rembourser les affiliés qui ont un solde une section des allocations familiales
 * 
 * @author DDA
 */
public class CAProcessRemboursementAutomatique extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_COMPTE_COURANT_NON_RESOLU = "5128";
    private static final String LABEL_LIBELLE_NON_RENSEIGNE = "7131";

    private String forDateEcheance = JACalendarGregorian.todayJJsMMsAAAA();
    private String forIdCompteCourant = new String();

    private String forIdOrganeExecution;

    private String forIdRole;
    private String forMontantLimit = "0.00";

    private String forNatureOrdre;

    private CAJournal journal = null;

    private String libelle = new String();

    /**
     * Remboursement automatique des compte courants par sections. => Ordre de versement pour solder.
     */
    public CAProcessRemboursementAutomatique() {
        super();
    }

    /**
     * Remboursement automatique des compte courants par sections. => Ordre de versement pour solder.
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessRemboursementAutomatique(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
        // Not used here.
    }

    /**
     * Exécution du remboursement automatique.
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        _validate();

        CARemboursementAutomatiqueManager manager = new CARemboursementAutomatiqueManager();
        manager.setSession(getSession());

        if (!JadeStringUtil.isBlankOrZero(getForIdCompteCourant())) {
            manager.setForIdCompteCourant(getForIdCompteCourant());
        }
        manager.setForIdRole(getForIdRole());
        manager.setForMontantLimit(getForMontantLimit());

        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            CARemboursementAutomatique remboursement = (CARemboursementAutomatique) manager.get(i);

            addOrdreVersement(remboursement);
        }

        return false;
    }

    /**
     * Remise à null du journal. Contrôle compte courant et libellé. <br/>
     * Si nécessaire negate du montant.
     */
    @Override
    protected void _validate() throws Exception {
        journal = null;

        // if (JadeStringUtil.isIntegerEmpty(getForIdCompteCourant())) {
        // this._addError(getSession().getLabel(CAProcessRemboursementAutomatique.LABEL_COMPTE_COURANT_NON_RESOLU));
        // throw new Exception(getSession()
        // .getLabel(CAProcessRemboursementAutomatique.LABEL_COMPTE_COURANT_NON_RESOLU));
        // }

        if (JadeStringUtil.isBlank(getLibelle())) {
            this._addError(getSession().getLabel(CAProcessRemboursementAutomatique.LABEL_LIBELLE_NON_RENSEIGNE));
            throw new Exception(getSession().getLabel(CAProcessRemboursementAutomatique.LABEL_LIBELLE_NON_RENSEIGNE));
        }

        setForMontantLimit(JANumberFormatter.deQuote(getForMontantLimit()));

        if (!getForMontantLimitAsCurrency().isZero()) {
            if (getForMontantLimitAsCurrency().isPositive()) {
                FWCurrency tmp = getForMontantLimitAsCurrency();
                tmp.negate();
                setForMontantLimit(tmp.toString());
            }
        }
    }

    /**
     * Ajout d'un ordre de versement si l'adresse de paiement est renseignée.
     * 
     * @param remboursement
     * @throws Exception
     */
    private void addOrdreVersement(CARemboursementAutomatique remboursement) throws Exception {
        CAOperationOrdreVersement versement = new CAOperationOrdreVersement();
        versement.setSession(getSession());

        versement.setIdCompteAnnexe(remboursement.getIdCompteAnnexe());
        versement.setIdSection(remboursement.getIdSection());
        versement.setDate(getForDateEcheance());

        try {
            versement.setDefaultIdAdressePaiement();
        } catch (Exception e) {
            // Do nothing.
        }

        if (!JadeStringUtil.isIntegerEmpty(versement.getIdAdressePaiement())) {
            versement.setIdJournal(getJournal().getIdJournal());

            versement.setMontant(remboursement.getMontantAbs());

            versement.setNatureOrdre(getForNatureOrdre());

            versement.setIdOrganeExecution(getForIdOrganeExecution());

            versement.add(getTransaction());

            if (versement.isNew() || versement.hasErrors()) {
                throw new Exception(getSession().getLabel("5156"));
            }
        }
    }

    /**
     * Set le titre de l'email.
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel("REMBOURSEMENT_AUTOMATIQUE_ERROR");
        } else {
            return getSession().getLabel("REMBOURSEMENT_AUTOMATIQUE_OK");
        }
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public String getForIdOrganeExecution() {
        return forIdOrganeExecution;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public String getForMontantLimit() {
        return forMontantLimit;
    }

    public FWCurrency getForMontantLimitAsCurrency() {
        return new FWCurrency(getForMontantLimit());
    }

    public String getForNatureOrdre() {
        return forNatureOrdre;
    }

    /**
     * Retourne et/ou créé le journal qui recevra les ordres de versements.
     * 
     * @return
     * @throws Exception
     */
    private CAJournal getJournal() throws Exception {
        if (journal == null) {
            journal = new CAJournal();
            journal.setSession(getSession());

            journal.setLibelle(getLibelle());
            journal.setDate(JACalendar.todayJJsMMsAAAA());
            journal.setDateValeurCG(JACalendar.todayJJsMMsAAAA());

            journal.add(getTransaction());

            if (journal.isNew() || journal.hasErrors()) {
                throw new Exception(getSession().getLabel("REMBOURSEMENT_AUTOMATIQUE_ERREUR_CREATION_JOURNAL"));
            }
        }

        return journal;
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    public void setForIdOrganeExecution(String forIdOrganeExecution) {
        this.forIdOrganeExecution = forIdOrganeExecution;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public void setForMontantLimit(String forMontantLimit) {
        this.forMontantLimit = forMontantLimit;
    }

    public void setForNatureOrdre(String forNatureOrdre) {
        this.forNatureOrdre = forNatureOrdre;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
