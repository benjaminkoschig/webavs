package globaz.al.process.rafam;

import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.services.ALServiceLocator;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.sedex.JadeSedexService;

/**
 * Classe gérant l'appel du service d'envoi des annonces RAFam
 *
 * @author jts
 *
 */
public class ExportAnnoncesRafamSedex extends AbstractRafamSedex {

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (JadeSedexService.getInstance().isServiceStarted()) {
                if ("true".equals(
                        JadePropertiesService.getInstance().getProperty(ALConstRafam.VERSION_ANNONCES_XSD_4_1))) {
                    ALServiceLocator.getExportAnnoncesNewXSDVersionRafamService().sendMessageDelegue();
                    ALServiceLocator.getExportAnnoncesNewXSDVersionRafamService().sendMessage();
                } else {
                    ALServiceLocator.getExportAnnoncesRafamService().sendMessageDelegue();
                    ALServiceLocator.getExportAnnoncesRafamService().sendMessage();
                }
            } else {
                JadeLogger.info(this, "Impossible d'envoyer un message au RAFam si le service sedex est inactif");
            }
        } catch (Exception e) {
            JadeLogger.warn(this, "ExportAnnoncesRafamSedex#run : Unable to prepare RAFam message, " + e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

}