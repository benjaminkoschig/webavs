package globaz.perseus.process.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSTypeListeImpotSource;
import ch.globaz.perseus.business.exceptions.impotsource.ImpotSourceException;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFGenererListeImpotSourceProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adMail = null;
    private String anneeLC = null;

    private String csTypeListe = null;

    private String idPeriode = null;

    private String numeroDebiteur = null;

    public String getAdMail() {
        return adMail;
    }

    public String getAnneeLC() {
        return anneeLC;
    }

    public String getCsTypeListe() {
        return csTypeListe;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumeroDebiteur() {
        return numeroDebiteur;
    }

    @Override
    protected void process() throws Exception {
        try {
            String path = "";
            String titreMail = "";
            if (CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem().equals(csTypeListe)) {
                titreMail = getSession().getLabel("PROCESS_PF_GENERATION_LISTE_RECAPITULATIVE");
                path = PerseusServiceLocator.getImpotSourceService().genererLR(getSession(), idPeriode, numeroDebiteur);

            } else if (CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem().equals(csTypeListe)) {
                titreMail = getSession().getLabel("PROCESS_PF_GENERATION_LISTE_CORRECTIVE");
                path = PerseusServiceLocator.getImpotSourceService().genererLC(getSession(), anneeLC, numeroDebiteur);
            } else {
                throw new ImpotSourceException(getSession().getLabel("PROCESS_PF_GENERATION_LISTE_NON_CORRECT"));
            }

            if (!JadeStringUtil.isEmpty(path)) {
                JadeSmtpClient.getInstance().sendMail(adMail, titreMail, "", new String[] { path });

            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "Erreur : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(getAdMail());
            this.sendCompletionMail(email);
        }

    }

    public void setAdMail(String adMail) {
        this.adMail = adMail;
    }

    public void setAnneeLC(String anneeLC) {
        this.anneeLC = anneeLC;
    }

    public void setCsTypeListe(String csTypeListe) {
        this.csTypeListe = csTypeListe;
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setNumeroDebiteur(String numeroDebiteur) {
        this.numeroDebiteur = numeroDebiteur;
    }

}
