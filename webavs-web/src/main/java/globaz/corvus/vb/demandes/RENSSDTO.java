package globaz.corvus.vb.demandes;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.rmi.RemoteException;

/*
 * Créé le 09 avril 08
 */

/**
 * @author jje
 * 
 *         DTO permettant de stocker le NSS, ceci chaque fois que l'utilisateur rentre dans le détail d'une demande ou
 *         sur les options disponibles.
 */
public class RENSSDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String NSS = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     */
    public RENSSDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     * 
     * @param paramPrononceDTO
     */
    public RENSSDTO(RENSSDTO NSSDTO) {
        NSS = NSSDTO.NSS;
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getNSS() {
        return NSS;
    }

    /**
     * Si on affiche la valeur dans un champ NSS et que le NSS commence par le prefixe NSS, on le supprime
     * 
     * @return
     */
    public String getNSSForSearchField() {

        if (isNssSearchField()) {
            if (NSS.startsWith("756")) {
                return NSUtil.formatWithoutPrefixe(NSS, true);
            } else {
                return NSUtil.formatWithoutPrefixe(NSS, false);
            }
        } else {
            return NSUtil.formatAVSNew(NSS, false);
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS est un NSS compelet, false si c'est un no AVS complet, sinon
     * un string vide
     * 
     * @return String (true ou false)
     */
    public String isNSS() {

        if (JadeStringUtil.isBlankOrZero(NSS)) {
            return "";
        } else {

            if (NSS.length() == 13) {
                return "true";
                // } else if (NSS.startsWith("756")){
                //
                // return "true";
            } else if (NSS.length() == 11) {
                return "false";
            } else {
                return "";
            }
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NSS compelet, false si c'est un no
     * AVS complet, sinon la valeur par default des properties
     * 
     * @param noAvs
     * @return boolean (true ou false)
     */
    private boolean isNssSearchField() {

        if (JadeStringUtil.isBlankOrZero(NSS)) {
            // bon, pas trouvé. Donc on prend la valeur par défaut des
            // properties
            try {
                String appValue = GlobazSystem.getApplication(GlobazSystem.APPLICATION_FRAMEWORK).getProperty(
                        "nsstag.defaultdisplay.newnss");
                if ("false".equals(appValue)) {
                    return false;
                } else if ("true".equals(appValue)) {
                    return true;
                }
            } catch (RemoteException e) {
                // TODO: faire qqchose avec ces try catch
            } catch (Exception e) {
                // TODO: faire qqchose avec ces try catch
            }
        } else {

            if (NSS.length() == 13) {
                return true;
            } else if (NSS.length() == 11) {
                return true;
            } else {
                // bon, pas trouvé. Donc on prend la valeur par défaut des
                // properties
                try {
                    String appValue = GlobazSystem.getApplication(GlobazSystem.APPLICATION_FRAMEWORK).getProperty(
                            "nsstag.defaultdisplay.newnss");
                    if ("false".equals(appValue)) {
                        return false;
                    } else if ("true".equals(appValue)) {
                        return true;
                    }
                } catch (RemoteException e) {
                    // TODO: faire qqchose avec ces try catch
                } catch (Exception e) {
                    // TODO: faire qqchose avec ces try catch
                }
            }
        }
        return true;
    }

    /**
     * @param string
     */
    public void setNSS(String string) {
        NSS = JadeStringUtil.removeChar(string, '.');
    }
}
