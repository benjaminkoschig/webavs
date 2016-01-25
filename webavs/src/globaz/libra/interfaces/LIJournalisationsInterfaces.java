package globaz.libra.interfaces;

import globaz.globall.db.BTransaction;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;
import globaz.journalisation.implementation.adapter.journalisation.JOCreationJournalisationHandler;
import globaz.journalisation.implementation.service.JOServiceLocator;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.utils.LIUtilsEntity;
import ch.globaz.libra.api.ILIJournalisationsInterfaces;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Fait :
 * 
 * [1] > Cr�ation de journalisation simple li�e � un tiers et au dossier si souhait� [2] > Cr�ation de journalisation
 * simple li�e � un tiers et au dossier si souhait� avec cr�ation dossier si inexistant [3] > Cr�ation de journalisation
 * avec remarque li�e � un tiers et au dossier si souhait� [4] > Cr�ation de journalisation avec remarque li�e � un
 * tiers et au dossier si souhait� avec cr�ation dossier si inexistant
 * 
 * [X] > Exemple VKU
 * 
 * Attente besoin pour autres interfaces
 */
public class LIJournalisationsInterfaces implements ILIJournalisationsInterfaces {

    // ~Constructor------------------------------------------------------------------------------------------------------

    public LIJournalisationsInterfaces() {
        super();
    }

    // ~Methods----------------------------------------------------------------------------------------------------------

    /**
     * [1] Cr�ation de journalisation simple
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait�
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    @Override
    public void createJournalisation(BTransaction transaction, String idExterne, String libelle, boolean isDossier) {

        try {

            LIDossiers dossier = new LIDossiers();
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            // Si dossier inexistant, erreur
            if (dossier.isNew()) {
                throw new Exception("Dossier inexistant dans Web@Journalisations");
            }

            // Si dossier clos, r�ouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // cr�ation groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Cr�ation de l'ensemble des entit�s :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // D�fini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // cr�ation complementJournal fmt syst�me
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // cr�ation ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // cr�ation ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

    /**
     * [3] Cr�ation de journalisation avec remarque
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait� -> Cr�ation d'une remarque
     * dans la journalisation
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    @Override
    public void createJournalisationAvecRemarque(BTransaction transaction, String idExterne, String libelle,
            String remarque, boolean isDossier) {

        try {

            LIDossiers dossier = new LIDossiers();
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            // Si dossier inexistant, erreur
            if (dossier.isNew()) {
                throw new Exception("Dossier inexistant dans Web@Journalisations");
            }

            // Si dossier clos, r�ouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // cr�ation groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Cr�ation de l'ensemble des entit�s :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // D�fini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // cr�ation complementJournal fmt syst�me
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // cr�ation ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // cr�ation ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

            // ajout de la remarque
            LIUtilsEntity utils = new LIUtilsEntity();
            utils.setIdJournalisation(handler.getIdJournalisation());
            utils.setRemarque(remarque);
            utils.add(transaction);

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

    /**
     * [4] Cr�ation de journalisation avec remarque
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait� -> Cr�ation d'une remarque
     * dans la journalisation
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    @Override
    public void createJournalisationAvecRemarqueWithTestDossier(BTransaction transaction, String idExterne,
            String libelle, String remarque, String idTiers, String csDomaine, boolean isDossier) {

        try {

            LIDossiers dossier = new LIDossiers();
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            // Si dossier inexistant, le cr�er
            if (dossier.isNew()) {
                LibraServiceLocator.getDossierService().createDossier(idTiers, csDomaine, idExterne);
                // BZ 7333
                dossier.retrieve();
            }

            // Si dossier clos, r�ouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // cr�ation groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Cr�ation de l'ensemble des entit�s :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // D�fini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // cr�ation complementJournal fmt syst�me
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // cr�ation ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // cr�ation ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

            // ajout de la remarque
            LIUtilsEntity utils = new LIUtilsEntity();
            utils.setIdJournalisation(handler.getIdJournalisation());
            utils.setRemarque(remarque);
            utils.add(transaction);

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }
    }

    /**
     * [X] Exemple VKU
     * 
     */
    public BTransaction createJournalisationsForEnvoiFormuleWithRappel(BTransaction transaction, String dateRappel,
            String idDossier, String idTiers, String idUtilisateurRappel, String idFormule, String idFormuleRappel) {

        try {
            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // Exemple de chargement
            // // charger le groupeJournal
            // JOGroupeJournal groupeJournal =
            // serviceLocator.getGroupeJournalService().read(transaction.getSession(),
            // idGroupeJournal);

            // cr�ation groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction, dateRappel);

            // Cr�ation de l'ensemble des entit�s :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);
            JOCreationJournalisationHandler rappel = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // D�fini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    "", "", "", rappel.getIdJournalisation());
            // cr�ation complementJournal fmt envoi simple
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_AVS_FMT_ENVOI_SIMPLE);
            // cr�ation ReferenceProvenance "dossier"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, idDossier);
            // cr�ation ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, idTiers);
            // cr�ation ReferenceDestianation "formule"
            handler.registerReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS, idFormule);

            // D�fini les valeurs pour la journalisation
            rappel.setJournalisationValues(JOConstantes.CS_JO_RAPPEL, idUtilisateurRappel, "",
                    handler.getIdJournalisation(), handler.getIdJournalisation(), "");
            // cr�ation complementJournal fmt rappel
            rappel.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_AVS_FMT_RAPPEL);
            // cr�ation ReferenceProvenance "dossier"
            rappel.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, idDossier);
            // cr�ation ReferenceProvenance "tiers"
            rappel.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, idTiers);
            // cr�ation ReferenceDestianation "formule"
            rappel.registerReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS, idFormuleRappel);

            // Lance les processus
            handler.processJournalisation();
            rappel.processJournalisation();

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

        return transaction;
    }

    /**
     * [2] Cr�ation de journalisation simple
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    @Override
    public void createJournalisationWithTestDossier(BTransaction transaction, String idExterne, String libelle,
            String idTiers, String csDomaine, boolean isDossier) {

        try {

            LIDossiers dossier = new LIDossiers();
            dossier.setSession(transaction.getSession());
            dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
            dossier.setIdExterne(idExterne);
            dossier.retrieve();

            // Si dossier inexistant, le cr�er
            if (dossier.isNew()) {
                LibraServiceLocator.getDossierService().createDossier(idTiers, csDomaine, idExterne);
                // BZ 7333
                dossier.retrieve();
            }

            // Si dossier clos, r�ouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // cr�ation groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Cr�ation de l'ensemble des entit�s :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // D�fini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // cr�ation complementJournal fmt syst�me
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // cr�ation ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // cr�ation ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

}
