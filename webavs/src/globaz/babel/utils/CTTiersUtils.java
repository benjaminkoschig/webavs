/*
 * Créé le 10 mai 05
 * 
 * Description :
 */
package globaz.babel.utils;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITITiers;
import java.util.Hashtable;

/**
 * DOCUMENT ME!
 * 
 * @author scr Descpription
 */
public class CTTiersUtils {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut langue d'un tiers donne par son id
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers par id
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String getLangueTiersParIdTiers(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return "";
        }

        ITITiers tiers = (ITITiers) session.getAPIFor(ITITiers.class);

        Hashtable criteres = new Hashtable();
        criteres.put(ITITiers.FIND_FOR_IDTIERS, idTiers);

        tiers.setISession(session);

        ITITiers[] result = tiers.findTiers(criteres);

        if ((result == null) || (result.length == 0)) {
            return "";
        } else {
            return result[0].getLangue();
        }
    }

    /**
     * getter pour les attributs prenom et nom d'un tiers donne par son id
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut tiers par id
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String getPrenomNomTiersParIdTiers(BISession session, String idTiers) throws Exception {
        // fail fast
        if (JadeStringUtil.isEmpty(idTiers)) {
            return "";
        }

        ITITiers tiers = (ITITiers) session.getAPIFor(ITITiers.class);

        Hashtable criteres = new Hashtable();
        criteres.put(ITITiers.FIND_FOR_IDTIERS, idTiers);

        tiers.setISession(session);

        ITITiers[] result = tiers.findTiers(criteres);

        if ((result == null) || (result.length == 0)) {
            return "";
        } else {
            return result[0].getNom();
        }
    }

}
