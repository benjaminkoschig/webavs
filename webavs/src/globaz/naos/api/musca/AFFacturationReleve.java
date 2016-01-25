/*
 * Créé le 20 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.api.musca;

import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.db.releve.AFApercuReleveMontant;
import globaz.naos.db.releve.AFApercuReleveMontantManager;
import globaz.naos.translation.CodeSystem;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFFacturationReleve extends AFFacturationGenericImpl {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Avant regénérer, il faut effacer les afacts
     * 
     * @return true s'il faut effacer les afacts avant de regénérer, false sinon
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return false;
    }

    /**
     * Passe les releves dans l'etat comptabilise lors de la comptabilisation dans musca.
     * 
     * @param passage
     *            DOCUMENT ME!
     * @param context
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca.api.IFAPassage,
     *      globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        AFApercuReleveManager releves = new AFApercuReleveManager();
        BISession sessionNaos = GlobazSystem.getApplication("NAOS").newSession();

        context.getSession().connectSession(sessionNaos);

        releves.setForJob(passage.getIdPassage());
        releves.setISession(sessionNaos);
        releves.find(BManager.SIZE_NOLIMIT);

        for (int idReleve = 0; idReleve < releves.size(); ++idReleve) {
            AFApercuReleve releve = (AFApercuReleve) releves.get(idReleve);

            releve.setEtat(CodeSystem.ETATS_RELEVE_COMPTABILISER);
            releve.wantCallValidate(false);
            releve.wantCallMethodBefore(false);
            releve.wantCallMethodAfter(false);
            releve.setISession(sessionNaos);
            releve.update();

            // test si des montants existent encore
            AFApercuReleveMontantManager mgr = new AFApercuReleveMontantManager();
            mgr.setISession(sessionNaos);
            mgr.setForIdReleve(releve.getIdReleve());
            releve.setISession(sessionNaos);
            mgr.find();
            for (int i = 0; i < mgr.size(); i++) {
                ((AFApercuReleveMontant) mgr.getEntity(i)).delete();
            }
        }

        return true;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        if (((AFApplication) GlobazSystem.getApplication("NAOS")).isCreationPassageAutomatique()
                && !passage.getIsAuto()) {
            openNewPassage(passage, context);
        }
        return true;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Method openNewPassage. Contrôler s'il ny pas déjà un passage ouvert contenant ce module de facturation. Ouvrir un
     * nouveau passage dès que le passage en cours est fermé.
     * 
     * @param passage
     * @param process
     * @return boolean
     * @author:BTC
     */
    public boolean openNewPassage(IFAPassage passage, BProcess process) {
        // Créer un nouveau passage s'il n'y a pas d'autres passage ouvert
        boolean finalValue;
        IFAPassage existsOpenPassage = ServicesFacturation.getProchainPassageFacturation(process.getTransaction()
                .getSession(), process.getTransaction(),
                globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_RELEVE);
        // S'il y a déjà un passage ouvert, ne pas en ouvrir automatiquement
        if ((existsOpenPassage != null) && FAPassage.CS_ETAT_OUVERT.equals(existsOpenPassage.getStatus())
                && !passage.getIdPassage().equalsIgnoreCase(existsOpenPassage.getIdPassage())) {
            return false;
        }
        if (FAPassage.CS_TYPE_PERIODIQUE.equals(passage.getIdTypeFacturation())) {
            return false;
        }

        JADate newDateFacturation = JACalendar.today();

        int nbrJourAdd;
        try {
            nbrJourAdd = ((AFApplication) GlobazSystem.getApplication("NAOS")).getAddJourDateFacturation();
        } catch (Exception e) {
            nbrJourAdd = 1;
        }

        newDateFacturation = new JACalendarGregorian().addDays(newDateFacturation, nbrJourAdd);
        // Test si la date tombe un week end
        int dayToAdd = 0;
        switch (new JACalendarGregorian().getDayNumber(newDateFacturation)) {
            case 5:
                dayToAdd = 2; // samedi rajouter 2 jours
            case 6:
                dayToAdd = 1; // dimanche rajouter 1 jour
            default:
                dayToAdd = 0;
        }
        newDateFacturation = new JACalendarGregorian().addDays(newDateFacturation, dayToAdd);
        // ouvrir un nouveau passage
        FAPassage myPassage = new FAPassage();
        myPassage.setSession(process.getSession());

        String libelleAuto = "Facturation relevés";

        myPassage.setIdTypeFacturation(FAPassage.CS_TYPE_EXTERNE);
        // recopier le plan de l'ancien passage
        myPassage.setIdPlanFacturation(passage.getIdPlanFacturation());
        myPassage.setDateFacturation(newDateFacturation.toString());

        process.getTransaction().disableSpy();
        try {
            myPassage.add(process.getTransaction());
        } catch (Exception e) {
            process.getTransaction().addErrors(e.getMessage());
        }
        try {
            libelleAuto = myPassage.getIdPassage() + " "
                    + ((AFApplication) GlobazSystem.getApplication("NAOS")).getLibellePassageAutomatique() + " "
                    + JACalendar.format(newDateFacturation, JACalendar.FORMAT_DDsMMsYYYY);
            myPassage.setLibelle(libelleAuto);
            myPassage.update(process.getTransaction());
        } catch (Exception e) {
            process.getTransaction().addErrors(e.getMessage());
        }

        finally {
            process.getTransaction().enableSpy();
            if (!process.getTransaction().hasErrors()) {
                finalValue = true;
            } else {
                finalValue = false;
            }
        }
        return finalValue;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
