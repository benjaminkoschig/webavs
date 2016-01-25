package globaz.al.process.rafam;

import globaz.jade.context.JadeThreadActivator;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.smtp.JadeSmtpClient;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Permet l'envoi quotidien du protocole de contrôle des annonces RAFam
 * 
 * @author jts
 * @see ALProtocoleRafamProcess
 */
public class ALProtocoleRafam extends AbstractRafamSedex {

    /** destinataire du protocole */
    private String email;

    public String getEmail() {
        return email;
    }

    @Override
    public void run() {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());
            email = JadePropertiesService.getInstance().getProperty(ALConstRafam.RAFAM_CONTACT_EMAIL);
            sendProtocole();

        } catch (Exception e) {
            JadeLogger.error(this, "ALProtocoleRafamHTML#run : Unable to prepare RAFam protocole, " + e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    public void sendProtocole() throws Exception {
        String file = ALServiceLocator.getAnnoncesRafamProtocoleService().createProtocole();
        String description = "Protocole de contrôle des Annonces RAFam";
        JadeSmtpClient.getInstance().sendMail(email, description, description, new String[] { file });
        JadeLogger.info(this, "protocole RAFam sur demande envoyé");
    }

    public void sendProtocoleDelegue(String idEmployeur) throws Exception {
        String file = ALServiceLocator.getAnnoncesRafamDelegueProtocoleService().createProtocole(idEmployeur);
        String description = "Protocole de contrôle des Annonces RAFam délégué " + idEmployeur;
        JadeSmtpClient.getInstance().sendMail(email, description, description, new String[] { file });

        String uriProtocole = JadePropertiesService.getInstance().getProperty(
                "al.rafam.delegue." + idEmployeur + ".protocoleRetour.uri");
        String destinationFinale = uriProtocole.concat(file.substring(file.lastIndexOf("/") + 1));
        JadeFsFacade.copyFile(file, destinationFinale);

        JadeLogger.info(this, "protocole RAFam délégué sur demande envoyé");

    }

    public void setEmail(String email) {
        this.email = email;
    }
}