/*
 * mmu Cr�� le 19 oct. 05
 */
package globaz.hera.external;

import globaz.jade.client.util.JadeStringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <H1>Permet de g�rer l'url pass�e en param�tre</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author mmu
 * 
 *         <p>
 *         19 oct. 05
 *         </p>
 */
public class ISFUrlEncode {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * D�code l'url de retour.
     * 
     * @param url
     *            � d�coder
     * 
     * @return l'url d�cod�e
     */
    public static String decodeUrl(String url) {

        String result = url;
        ;
        try {
            result = URLDecoder.decode(result, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            try {
                result = URLDecoder.decode(result, "UTF-8");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        result = JadeStringUtil.change(result, "HERVEREVIENT", "userAction");
        return result;

    }

    /**
     * Encode l'Url de retour afin qu'elle puisse �tre pass�e par une application ext�rieure.
     * 
     * @param url
     *            : l'url de retour � encoder
     * 
     * @return l'url encod�e
     */
    public static String encodeUrl(String url) {

        String result = JadeStringUtil.change(url, "userAction", "HERVEREVIENT");

        try {
            result = URLEncoder.encode(result, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            try {
                result = URLEncoder.encode(result, "UTF-8");
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

        return result;
    }
}