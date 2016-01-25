package globaz.musca.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageManager;

/**
 * @author MMO 12.10.2010
 **/

public class FAInfoRom200GenerationComptabilisationPassageProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Constructeur
    public FAInfoRom200GenerationComptabilisationPassageProcess() {
        super();
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        setSendCompletionMail(false);

        FAPassageManager mgrPassage = new FAPassageManager();
        mgrPassage.setSession(getSession());
        mgrPassage.setForStatus(FAPassage.CS_ETAT_OUVERT);
        mgrPassage.setForDateFacturation(String.valueOf(new JACalendarGregorian().addDays(JACalendar.today(), 1)));
        mgrPassage._setForIsAuto(true);
        mgrPassage.setForUserCreateur(getSession().getUserId());
        mgrPassage.find(BManager.SIZE_NOLIMIT);

        for (int i = 1; i <= mgrPassage.size(); i++) {
            try {
                FAPassage passage = (FAPassage) mgrPassage.getEntity(i - 1);

                if (!JadeNumericUtil.isZeroValue(passage.getDelaiJour())) {
                    FAPassage newPassage = new FAPassage();
                    newPassage.copyDataFromEntity(passage);
                    newPassage.setIdPassage(null);

                    newPassage.setDateFacturation(addDaysSkipWeekend(passage.getDateFacturation(),
                            Integer.valueOf(passage.getDelaiJour()).intValue()));
                    newPassage.add();
                }

                FAPassageFacturationProcess passageFactuProcess = new FAPassageFacturationProcess();
                passageFactuProcess.setSession(getSession());
                passageFactuProcess.setEMailAddress(getSession().getUserEMail());
                passageFactuProcess.setActionModulePassage(FAModulePassage.CS_ACTION_GENERE);
                passageFactuProcess.setIdPassage(passage.getIdPassage());
                passageFactuProcess.setAuto(true);
                passageFactuProcess.setEnchainerComptabilisation(true);
                BProcessLauncher.start(passageFactuProcess);

            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        return true;
    }

    /**
     * Méthode temporaire afin de quand même traiter les weekends en attendant la gestion des jours fériés
     */
    private String addDaysSkipWeekend(String theDate, int nbJour) throws Exception {
        JACalendarGregorian calendrier = new JACalendarGregorian();

        while (nbJour > 0) {
            theDate = calendrier.addDays(theDate, 1);
            if (!calendrier.isWeekend(theDate)) {
                nbJour--;
            }
        }

        return theDate;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

}
