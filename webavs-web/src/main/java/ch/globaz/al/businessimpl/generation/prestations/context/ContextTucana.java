package ch.globaz.al.businessimpl.generation.prestations.context;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Contexte Tucana utilisé pour la génération de prestations
 * 
 * @author jts
 * 
 */
public class ContextTucana {

    private static boolean initialized = false;

    private static Boolean tucanaIsEnabled = null;

    /**
     * Initialise le context. Récupère la valeur du paramètre TucanaIsEnabled
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void initContext(String date) throws JadeApplicationException, JadePersistenceException {

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.TUCANA_IS_ENABLED, date);

        if ("0".equals(param.getValeurAlphaParametre())) {
            ContextTucana.tucanaIsEnabled = false;
        } else if ("1".equals(param.getValeurAlphaParametre())) {
            ContextTucana.tucanaIsEnabled = true;
        } else {
            throw new ALGenerationException("ContextTucana#initContext : Parameter has not a valid value");
        }
    }

    /**
     * Vérifie si Tucana est activé ou non
     * 
     * @return <code>true</code> si Tucana est actif, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static boolean tucanaIsEnabled() throws JadeApplicationException, JadePersistenceException {

        if (!ContextTucana.initialized) {
            ContextTucana.initContext(JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())));
        }

        return ContextTucana.tucanaIsEnabled;
    }
}
