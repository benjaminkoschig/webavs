package globaz.perseus.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author DDE
 * 
 */
public class PFNssHelper {

    public static String getNssLike(BSession session) {
        String nssLike = (String) session.getAttribute("likeNss");
        nssLike = (JadeStringUtil.isEmpty(nssLike)) ? "" : nssLike.substring(4);
        return nssLike;
    }

}
