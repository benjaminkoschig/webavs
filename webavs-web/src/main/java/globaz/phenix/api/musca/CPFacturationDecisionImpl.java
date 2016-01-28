package globaz.phenix.api.musca;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.process.CPProcessFacturation;
import globaz.phenix.process.CPProcessValidationFacturation;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class CPFacturationDecisionImpl extends CPFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public CPFacturationDecisionImpl() {
        super();
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 08:52:52)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CPProcessValidationFacturation procFacturation = new CPProcessValidationFacturation();
        // copier le process parent
        BSession sessionPhenix = new globaz.globall.db.BSession(
                globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        context.getSession().connectSession(sessionPhenix);
        procFacturation.setSession(sessionPhenix);
        procFacturation.setIdPassage(passage.getIdPassage());
        procFacturation.setSendCompletionMail(false);
        procFacturation.setSendMailOnError(false);
        procFacturation.setWantMajCI(true);
        procFacturation.executeProcess();
        context.getMemoryLog().logMessage(procFacturation.getMemoryLog());
        // contrôler si le process a fonctionné
        // Modif facturation journalière if (!procFacturation.isAborted() &&
        // !context.getTransaction().hasErrors()) {
        if (!procFacturation.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        CPProcessFacturation procFacturation = new CPProcessFacturation();

        // copier le process parent
        BSession sessionPhenix = new globaz.globall.db.BSession(
                globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        context.getSession().connectSession(sessionPhenix);
        procFacturation.setSession(sessionPhenix);
        procFacturation.setIdPassage(passage.getIdPassage());
        procFacturation.setIdModuleFacturation(idModuleFacturation);
        procFacturation.setSendCompletionMail(false);
        procFacturation.setSendMailOnError(false);
        procFacturation.executeProcess();
        context.getMemoryLog().logMessage(procFacturation.getMemoryLog());
        // contrôler si le process a fonctionné
        if (!procFacturation.isAborted() && !context.getTransaction().hasErrors()) {
            // Créer un nouveau passage automatiquement si défini dans property
            if (((CPApplication) GlobazSystem.getApplication("PHENIX")).isCreationPassageAutomatique()
                    && !passage.getIsAuto()) {
                // Recherche si séparation indépendant et non-actif - Inforom 314s
                Boolean isSeprationIndNac = false;
                try {
                    isSeprationIndNac = new Boolean(GlobazSystem
                            .getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA).getProperty(
                                    FAApplication.SEPARATION_IND_NA));
                } catch (Exception e) {
                    isSeprationIndNac = Boolean.FALSE;
                }
                if (isSeprationIndNac) {
                    // Recherche du module utiliés si IND ou NAC dans le passage en question
                    FAPassageModuleManager modPass = new FAPassageModuleManager();
                    modPass.setSession(sessionPhenix);
                    modPass.setForIdPassage(passage.getIdPassage());
                    modPass.setInTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", "
                            + FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
                    modPass.find();
                    if (modPass.size() > 0) {
                        String module = ((FAPassageModule) modPass.getFirstEntity()).getIdTypeModule();
                        openNewPassage(passage, context, module);
                    }

                } else {
                    openNewPassage(passage, context, FAModuleFacturation.CS_MODULE_COT_PERS);
                }

            }
            return true;
        } else {
            return false;
        }
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
    public boolean openNewPassage(IFAPassage passage, BProcess process, String module) throws Exception {
        // Créer un nouveau passage s'il n'y a pas d'autres passage ouvert
        boolean finalValue;
        FAPassage existsOpenPassage = null;
        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        modPassManager.setSession(process.getTransaction().getSession());
        modPassManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        modPassManager.setForIdTypeModule(module);
        modPassManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
        modPassManager.orderDesire("DATEFACTURATION, IDPASSAGE");
        modPassManager.find();
        for (int i = 0; i < modPassManager.getSize(); i++) {
            FAPassageModule modPassage = null;
            // Itérer sur tous les modules de facturation qui contiennent ce
            // type de module
            modPassage = (FAPassageModule) modPassManager.getEntity(i);
            if (!modPassage.getIdPassage().equalsIgnoreCase(passage.getIdPassage())) {
                existsOpenPassage = new FAPassage();
                existsOpenPassage.setSession(process.getTransaction().getSession());
                existsOpenPassage.setIdPassage(modPassage.getIdPassage());
                existsOpenPassage.retrieve();
                break;
            }
        }
        // S'il y a déjà un passage ouvert, ne pas en ouvrir automatiquement
        if (existsOpenPassage != null) {
            return false;
        }
        if (FAPassage.CS_TYPE_PERIODIQUE.equals(passage.getIdTypeFacturation())) {
            return false;
        }

        // en cot.pers. la facturation se fait par semaine
        // la prochaine facturation est la semaine suivante
        JADate newDateFacturation;

        int nbrJourAdd = 1;
        try {
            nbrJourAdd = ((CPApplication) GlobazSystem.getApplication("PHENIX")).getAddJourDateFacturation();
        } catch (Exception e) {
            nbrJourAdd = 1;
        }

        newDateFacturation = JACalendar.today();
        newDateFacturation = new JACalendarGregorian().addDays(newDateFacturation, nbrJourAdd);
        // Test si la date tombe un week end
        int dayToAdd = 0;
        switch (new JACalendarGregorian().getDayNumber(newDateFacturation)) {
            case 5:
                dayToAdd = 2;
                break;// samedi rajouter 2 jours
            case 6:
                dayToAdd = 1;
                break;// dimanche rajouter 2 jours
            default:
                dayToAdd = 0;
        }
        newDateFacturation = new JACalendarGregorian().addDays(newDateFacturation, dayToAdd);
        // ouvrir un nouveau passage
        FAPassage myPassage = new FAPassage();
        myPassage.setSession(process.getSession());

        String libelleAuto = "Facturation cot. pers";

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
                    + ((CPApplication) GlobazSystem.getApplication("PHENIX")).getLibellePassageAutomatique(module)
                    + " " + JACalendar.format(newDateFacturation, JACalendar.FORMAT_DDsMMsYYYY);
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
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

}
