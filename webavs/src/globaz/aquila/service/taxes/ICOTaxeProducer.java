/*
 * Créé le 13 mars 06
 */
package globaz.aquila.service.taxes;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Définit l'interface d'une source de taxes.
 * </p>
 * 
 * @author dvh
 */
public interface ICOTaxeProducer {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne la liste des taxes (COTaxe) pour le contentieux donné à l'étape donnée.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param contentieux
     *            DOCUMENT ME!
     * @param etape
     *            DOCUMENT ME!
     * @return une liste jamais nulle, peut-etre vide, d'instances de COTaxe.
     * @throws Exception
     *             DOCUMENT ME!
     */
    List getListeTaxes(BSession session, COContentieux contentieux, COEtape etape) throws Exception;
}
