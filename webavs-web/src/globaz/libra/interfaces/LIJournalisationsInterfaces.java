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
 * [1] > Création de journalisation simple liée à un tiers et au dossier si souhaité [2] > Création de journalisation
 * simple liée à un tiers et au dossier si souhaité avec création dossier si inexistant [3] > Création de journalisation
 * avec remarque liée à un tiers et au dossier si souhaité [4] > Création de journalisation avec remarque liée à un
 * tiers et au dossier si souhaité avec création dossier si inexistant
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
     * [1] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
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

            // Si dossier clos, réouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // création groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Création de l'ensemble des entités :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // Défini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // création complementJournal fmt système
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // création ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // création ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

    /**
     * [3] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
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

            // Si dossier clos, réouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // création groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Création de l'ensemble des entités :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // Défini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // création complementJournal fmt système
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // création ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // création ReferenceProvenance "tiers"
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
     * [4] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
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

            // Si dossier inexistant, le créer
            if (dossier.isNew()) {
                LibraServiceLocator.getDossierService().createDossier(idTiers, csDomaine, idExterne);
                // BZ 7333
                dossier.retrieve();
            }

            // Si dossier clos, réouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // création groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Création de l'ensemble des entités :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // Défini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // création complementJournal fmt système
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // création ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // création ReferenceProvenance "tiers"
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

            // création groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction, dateRappel);

            // Création de l'ensemble des entités :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);
            JOCreationJournalisationHandler rappel = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // Défini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    "", "", "", rappel.getIdJournalisation());
            // création complementJournal fmt envoi simple
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_AVS_FMT_ENVOI_SIMPLE);
            // création ReferenceProvenance "dossier"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, idDossier);
            // création ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, idTiers);
            // création ReferenceDestianation "formule"
            handler.registerReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS, idFormule);

            // Défini les valeurs pour la journalisation
            rappel.setJournalisationValues(JOConstantes.CS_JO_RAPPEL, idUtilisateurRappel, "",
                    handler.getIdJournalisation(), handler.getIdJournalisation(), "");
            // création complementJournal fmt rappel
            rappel.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_AVS_FMT_RAPPEL);
            // création ReferenceProvenance "dossier"
            rappel.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, idDossier);
            // création ReferenceProvenance "tiers"
            rappel.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, idTiers);
            // création ReferenceDestianation "formule"
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
     * [2] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
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

            // Si dossier inexistant, le créer
            if (dossier.isNew()) {
                LibraServiceLocator.getDossierService().createDossier(idTiers, csDomaine, idExterne);
                // BZ 7333
                dossier.retrieve();
            }

            // Si dossier clos, réouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }

            JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

            // création groupeJournal
            JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction);

            // Création de l'ensemble des entités :
            JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                    .getCreationJournalisationHandler(transaction, groupeJournal);

            // Défini les valeurs pour la journalisation
            handler.setJournalisationValues(JOConstantes.CS_JO_JOURNALISATION, transaction.getSession().getUserId(),
                    libelle);
            // création complementJournal fmt système
            handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, JOConstantes.CS_JO_FMT_AUTOMATIQUE);

            if (isDossier) {
                // création ReferenceProvenance "dossier"
                handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
            }

            // création ReferenceProvenance "tiers"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

            // Lance les processus
            handler.processJournalisation();

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

}
