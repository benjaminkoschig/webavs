/*
 * Cr�� le 16 avr. 07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Vector;

/**
 * @author BSC
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class APEnfantAPG_2ViewBean extends APEnfantAPGViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String getCsSexe() {
        PRTiersWrapper tiers;
        try {
            tiers = PRTiersHelper.getTiers(getSession(), getNss());
            return tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE);
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * M�thode qui retourne le NNSS format� sans le pr�fixe (756.) ou alors le NSS normal
     * 
     * @return NNSS format� sans pr�fixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * getter pour l'attribut ti pays.
     * 
     * @return la valeur courante de l'attribut ti pays
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    /**
     * M�thode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }
}
