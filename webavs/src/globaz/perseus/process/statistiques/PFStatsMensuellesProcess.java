package globaz.perseus.process.statistiques;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.StatsMensuelles;

public class PFStatsMensuellesProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String mailAd = null;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMailAd() {
        return mailAd;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void process() throws Exception {
        try {
            StatsMensuelles stats = new StatsMensuelles();
            String body = getSession().getLabel("JSP_PF_STATS_DEMANDES_PAR_JOUR_D_JOURFIN_TITRE_BODY").replace(
                    "{dateDebut}", dateDebut);
            body = body.replace("{dateFin}", dateFin);
            JadeSmtpClient.getInstance().sendMail(getMailAd(),
                    getSession().getLabel("JSP_PF_STATS_DEMANDES_PAR_JOUR_D_JOURFIN_TITRE_MAIL"), body,
                    new String[] { stats.createDocAndSave(dateDebut, dateFin) });
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.getClass());
            e.printStackTrace();
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
        if (getLogSession().hasMessages()) {
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }

    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

}
