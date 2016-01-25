package globaz.al.process.rafam;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;

/**
 * Process permettant l'envoi du protocole de contr�le des annonces RAFam
 * 
 * 
 * @author jts
 * @see ALProtocoleRafam
 */
public class ALProtocoleRafamProcess extends ALAbsrtactProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * D�finit l'id de l'employeur pour lequel imprimer le protocole, si pas d�fini protocole CAF
     */
    private String idEmployeurDelegue = "";

    @Override
    public String getDescription() {
        return "Protocole de contr�le des Annonces RAFam";
    }

    public String getIdEmployeurDelegue() {
        return idEmployeurDelegue;
    }

    @Override
    public String getName() {
        return "Cr�ation protocole RAFam";
    }

    @Override
    protected void process() {

        try {
            ALProtocoleRafam protocole = new ALProtocoleRafam();
            protocole.setEmail(JadeThread.currentUserEmail());
            if (JadeStringUtil.isEmpty(idEmployeurDelegue)) {
                protocole.sendProtocole();
            } else {
                protocole.sendProtocoleDelegue(idEmployeurDelegue);
            }
        } catch (Exception e) {
            JadeThread.logError(
                    this.getClass().getName() + ".process()",
                    "Une erreur s'est produite pendant l'envoi ou la pr�paration du protocole RAFam : "
                            + e.getMessage());
        }

        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        try {
            sendCompletionMail(emails);
        } catch (Exception e) {
            JadeLogger.error(this, "Impossible d'envoyer le mail de r�sultat du traitement. Raison : " + e.getMessage()
                    + ", " + e.getCause());
        }
    }

    public void setIdEmployeurDelegue(String idEmployeurDelegue) {
        this.idEmployeurDelegue = idEmployeurDelegue;
    }
}
