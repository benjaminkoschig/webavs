package globaz.cygnus.process;

import globaz.cygnus.db.contributions.RFContributionsAssistanceAIManager;
import globaz.cygnus.db.contributions.RFContributionsJointTiers;
import globaz.cygnus.db.contributions.RFContributionsJointTiersManager;
import globaz.cygnus.helpers.process.RFListeExcelContributionsAssistanceAI;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;

public class RFListeContributionsAssistanceAIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut;
    private String dateFin;
    private String enCoursLe;

    public RFListeContributionsAssistanceAIProcess() {
        super();
        setSendCompletionMail(false);

        dateDebut = null;
        dateFin = null;
        enCoursLe = null;
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        RFContributionsJointTiersManager manager = new RFContributionsJointTiersManager();
        manager.setSession(getSession());

        if (!JadeStringUtil.isBlank(enCoursLe)) {
            manager.setForContributionsEnCoursAu(enCoursLe);
        } else if (!JadeStringUtil.isBlankOrZero(dateDebut) && !JadeStringUtil.isBlankOrZero(dateFin)) {
            JadePeriodWrapper periode = new JadePeriodWrapper(dateDebut, dateFin);
            manager.setForContributionsEnCoursDurant(periode);
        }
        manager.setOrdreDeTri(RFContributionsAssistanceAIManager.OrdreDeTri.NomPrenomDateDebutCAAI);
        manager.find(BManager.SIZE_NOLIMIT);

        List<RFContributionsJointTiers> contributions = new ArrayList<RFContributionsJointTiers>();
        for (int i = 0; i < manager.size(); i++) {
            RFContributionsJointTiers uneContribution = (RFContributionsJointTiers) manager.get(i);
            contributions.add(uneContribution);
        }

        if (contributions.isEmpty()) {
            JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getSession().getLabel("PROCESS_LISTE_CAAI"),
                    getSession().getLabel("PROCESS_LISTE_CAAI_AUCUNE_CONTRIBUTION"), null);
        } else {
            RFListeContributionsAssistanceAIExcelJob excelJob = new RFListeContributionsAssistanceAIExcelJob(
                    getSession(), new RFListeExcelContributionsAssistanceAI(getSession(), contributions),
                    getEMailAddress());
            excelJob.run();
        }
        return true;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    public String getEnCoursLe() {
        return enCoursLe;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEnCoursLe(String enCoursLe) {
        this.enCoursLe = enCoursLe;
    }
}
