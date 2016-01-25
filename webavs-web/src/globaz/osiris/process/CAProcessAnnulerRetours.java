package globaz.osiris.process;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.retours.CALotsRetours;
import globaz.osiris.db.retours.CARetours;
import globaz.osiris.db.retours.CARetoursJointLotsRetoursManager;
import globaz.osiris.externe.CAGestionComptabiliteExterne;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: bsc
 */
public class CAProcessAnnulerRetours extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CACompteAnnexe compteAnnexe = null;
    private String idRetour = "";
    private CAJournal journal = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerRetours.
     */
    public CAProcessAnnulerRetours() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessAnnulerRetours(BProcess parent) {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        FWMemoryLog comptaMemoryLog = null;

        try {
            // init. de la compta
            CAGestionComptabiliteExterne compta = new CAGestionComptabiliteExterne(this);
            compta.setSession(getSession());
            compta.setLibelle(JadeStringUtil.substring(FWMessageFormat.format(
                    getSession().getLabel("LIBELLE_PROCESS_ANNULER_RETOUR"), getRetourIdExterne()), 0, 50));
            compta.setDateValeur(JACalendar.todayJJsMMsAAAA());
            comptaMemoryLog = new FWMemoryLog();
            compta.setMessageLog(comptaMemoryLog);

            // un journal par annulation de retour
            journal = (CAJournal) compta.createJournal();

            // le retour a annuler
            CARetours retour = new CARetours();
            retour.setSession(getSession());
            retour.setIdRetour(idRetour);
            retour.retrieve(getTransaction());

            // le lot du retour pour l'idRubrique
            CALotsRetours lot = new CALotsRetours();
            lot.setSession(getSession());
            lot.setIdLot(retour.getIdLot());
            lot.retrieve(getTransaction());

            // ecriture d'annulation
            APIEcriture ecritureSectionRetour = compta.createEcriture();
            ecritureSectionRetour.setIdCompteAnnexe(retour.getIdCompteAnnexe());

            // Bugzilla 4992: la section de retour ne doit pas etre retrouvee avec l'annee de la date du
            // jour, mais avec l'annee de la date de retour
            APISection sectionRetour = compta.getSectionByIdExterne(retour.getIdCompteAnnexe(),
                    APISection.ID_TYPE_SECTION_RETOUR, "" + JACalendar.getYear(retour.getDateRetour())
                    /* JACalendar.today().getYear() */+ APISection.CATEGORIE_SECTION_RETOUR + "000");
            ecritureSectionRetour.setIdSection(sectionRetour.getIdSection());
            ecritureSectionRetour.setDate(JACalendar.todayJJsMMsAAAA());
            ecritureSectionRetour.setIdCompte(lot.getIdCompteFinancier());
            ecritureSectionRetour.setMontant(retour.getMontantRetour());
            ecritureSectionRetour.setCodeDebitCredit(APIEcriture.DEBIT);

            // ajout des ecritures
            compta.addOperation(ecritureSectionRetour);

            // on memorise l'id du journal en CA pour pouvoir rediriger sur
            // celui-ci
            retour.setIdJournal(journal.getIdJournal());
            retour.setCsEtatRetour(CARetours.CS_ETAT_RETOUR_ANNULE);
            retour.update(getTransaction());

            // Bugzilla 4561 ajout des retours "En suspens" dans le count
            // si il n'y a plus de retour "ouvert" ou "En suspens" ou "Traité" dans le lot,
            // le lot passe LIQUIDE
            CARetoursJointLotsRetoursManager rjlrManager = new CARetoursJointLotsRetoursManager();
            rjlrManager.setSession(getSession());
            rjlrManager.setForIdLot(lot.getIdLot());
            rjlrManager.setForCsEtatRetourIn(CARetours.CS_ETAT_RETOUR_OUVERT + "," + CARetours.CS_ETAT_RETOUR_SUSPENS
                    + "," + CARetours.CS_ETAT_RETOUR_TRAITE);
            int nbRetourOuvert = rjlrManager.getCount(getTransaction());
            if (nbRetourOuvert == 0) {
                lot.setCsEtatLot(CALotsRetours.CS_ETAT_LOT_LIQUIDE);
                lot.update(getTransaction());
            }

            if (!compta.isJournalEmpty() && !getMemoryLog().hasErrors()) {
                compta.comptabiliser();
            }

        } catch (Exception e) {

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

            return false;
        }
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj;
        if (getMemoryLog().hasErrors()) {
            obj = FWMessageFormat.format(getSession().getLabel("MAIL_PROCESS_ANNULER_RETOUR_KO"),
                    getRetourDescription());
        } else {
            obj = FWMessageFormat.format(getSession().getLabel("MAIL_PROCESS_ANNULER_RETOUR_OK"),
                    getRetourDescription());
        }
        // Restituer l'objet
        return obj;
    }

    public String getIdRetour() {
        return idRetour;
    }

    /**
     * Donne la description du retour (idExterne + compte annexe description)
     * 
     * @return
     */
    public String getRetourDescription() {

        loadCompteAnnexe();

        if (compteAnnexe != null) {
            return compteAnnexe.getIdExterneRole() + " - " + compteAnnexe.getDescription();
        } else {
            return "";
        }
    }

    /**
     * Donne l'idExterne du compte annexe
     * 
     * @return
     */
    public String getRetourIdExterne() {

        loadCompteAnnexe();

        if (compteAnnexe != null) {
            return compteAnnexe.getIdExterneRole();
        } else {
            return "";
        }
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

    /**
     * Cahangement du compte annexe
     */
    private void loadCompteAnnexe() {
        if ((compteAnnexe == null) && !JadeStringUtil.isIntegerEmpty(getIdRetour())) {

            CARetours retour = new CARetours();
            retour.setSession(getSession());
            retour.setIdRetour(getIdRetour());
            try {
                retour.retrieve();

                compteAnnexe = new CACompteAnnexe();
                compteAnnexe.setISession(getSession());
                compteAnnexe.setIdCompteAnnexe(retour.getIdCompteAnnexe());

                compteAnnexe.retrieve();
                if (compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }
}
