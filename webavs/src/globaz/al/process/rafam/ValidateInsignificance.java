package globaz.al.process.rafam;

import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Classe gérant la validation des erreurs de moindre importance
 * 
 * @author jts
 * 
 */
public class ValidateInsignificance extends AbstractRafamSedex {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            int delayInsignificance = Integer.parseInt(JadePropertiesService.getInstance().getProperty(
                    ALConstRafam.DELAY_INSIGNIFICANCE));

            if (delayInsignificance == 0) {
                throw new ALAnnonceRafamException("ValidateInsignificance#run : delayInsignificance is zero");
            }

            ALServiceLocator.getAnnoncesRafamErrorBusinessService().validateInsignificance(
                    (delayInsignificance > 0) ? delayInsignificance * -1 : delayInsignificance);
        } catch (Exception e) {
            JadeLogger.warn(this, "ValidateInsignificance#run : Unable to execute the process, " + e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

}