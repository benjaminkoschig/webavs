/*
 * Créé le 9 ovembre 2009
 */
package globaz.pegasus.utils;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.models.home.Home;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author bsc
 */
public class PCHomeHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode qui retourne la description du home
     * 
     * @return description du home
     */
    public static String getHomeDescription(Home home) {

        StringBuffer description = new StringBuffer();

        description.append(home.getAdresse().getTiers().getDesignation1());

        if (!JadeStringUtil.isEmpty(home.getAdresse().getTiers().getDesignation2())) {
            description.append(" " + home.getAdresse().getTiers().getDesignation2().trim());
        }

        if (!JadeStringUtil.isEmpty(home.getSimpleHome().getNomBatiment())) {
            description.append(" - " + home.getSimpleHome().getNomBatiment().trim());
        }

        return description.toString();

    }

}
