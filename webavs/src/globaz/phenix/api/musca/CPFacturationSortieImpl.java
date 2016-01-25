package globaz.phenix.api.musca;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.process.CPProcessFacturation;
import globaz.phenix.process.CPProcessValidationFacturation;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class CPFacturationSortieImpl extends CPFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public CPFacturationSortieImpl() {
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
        if (!procFacturation.isAborted() && !context.getTransaction().hasErrors()) {
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
        procFacturation.setIdModuleFacturation(idModuleFacturation);
        procFacturation.setIdPassage(passage.getIdPassage());
        procFacturation.setSendCompletionMail(false);
        procFacturation.setSendMailOnError(false);
        procFacturation.executeProcess();
        context.getMemoryLog().logMessage(procFacturation.getMemoryLog());
        // contrôler si le process a fonctionné
        if (!procFacturation.isAborted() && !context.getTransaction().hasErrors()) {
            // Créer un nouveau passage automatiquement si défini dans fwparp
            String libPassageSortie = "";
            try {
                libPassageSortie = FWFindParameter.findParameter(context.getTransaction(), "10500150", "CPLIBSORTI",
                        JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), "", 2);

            } catch (Exception e) {
                libPassageSortie = "";
                ;
            }
            if (!JadeStringUtil.isEmpty(libPassageSortie)) {
                openNewPassage(passage, context, libPassageSortie);
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
    public boolean openNewPassage(IFAPassage passage, BProcess process, String libPassage) {
        // Créer un nouveau passage s'il n'y a pas d'autres passage ouvert
        boolean finalValue;
        IFAPassage existsOpenPassage = ServicesFacturation.getProchainPassageFacturation(process.getTransaction()
                .getSession(), process.getTransaction(),
                globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_ETUDIANT);
        // S'il y a déjà un passage ouvert, ne pas en ouvrir automatiquement
        if ((existsOpenPassage != null) && FAPassage.CS_ETAT_OUVERT.equals(existsOpenPassage.getStatus())
                && !passage.getIdPassage().equalsIgnoreCase(existsOpenPassage.getIdPassage())) {
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

        String libelleAuto = "Facturation sortie cot. pers";

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
            libelleAuto = myPassage.getIdPassage() + " " + libPassage + " "
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
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

}
