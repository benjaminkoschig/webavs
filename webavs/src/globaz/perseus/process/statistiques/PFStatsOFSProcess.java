package globaz.perseus.process.statistiques;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFStatsOFSProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adMail = null;
    private String anneeStat = null;

    public String getAdMail() {
        return adMail;
    }

    public String getAnneeStat() {
        return anneeStat;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void process() throws Exception {
        try {

            String titreMail = getSession().getLabel("PROCESS_PF_GENERATION_XML_STSTS_OFS");
            String path = PerseusServiceLocator.getStatsOFSServie().genererFichierXML(getSession(), getLogSession(),
                    anneeStat);

            if (!JadeStringUtil.isEmpty(path)) {
                JadeSmtpClient.getInstance().sendMail(adMail, titreMail, "", new String[] { path });

            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "Erreur : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }

        logError(getSession().getLabel("PROCESS_ERREUR_MESSAGE"));
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            List<String> email = new ArrayList<String>();
            email.add(getAdMail());
            this.sendCompletionMail(email);
        }
    }

    public void setAdMail(String adMail) {
        this.adMail = adMail;
    }

    public void setAnneeStat(String anneeStat) {
        this.anneeStat = anneeStat;
    }

}
