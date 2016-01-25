/*
 * Créé le 16 avr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
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
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
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
