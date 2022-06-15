package globaz.apg.vb.prestation;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.Slashs;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1>
 *
 * @author vre
 */
@Slf4j
public abstract class APAbstractCalculACORViewBean extends PRAbstractViewBeanSupport { // TODO WS ACOR APG SE BASER SUR REAbstractCalculACORViewBean et IJAbstractCalculACORViewBean donc peut-�tre remonter plus de code dans l'abstract

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * M�thode permettant de r�cup�rer le navigateur � utiliser pour ACOR.
     *
     * @param session
     * @return
     */
    public String getStartNavigateurAcor(BSession session) {
        try {
            return PRACORConst.navigateurACOR(session);
        } catch (PRACORException e) {
            LOG.warn("Impossible de r�cup�rer le navigateur ACOR.", e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * M�thode permettant de r�cup�rer l'URL ACOR.
     *
     * @param askAction
     * @param token
     * @return
     */
    public String getAdresseWebACOR(String askAction, String token) {
        try {
            return Slashs.addLastSlash(CommonProperties.ACOR_ADRESSE_WEB.getValue()) + askAction + "/"+ token;
        } catch (PropertiesException e) {
            LOG.warn("La propri�t� n'existe pas ou n'est pas renseign� :", e);
            return "";
        }
    }

}