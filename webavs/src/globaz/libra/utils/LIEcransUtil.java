/*
 * Créé le 22 juillet 2009
 */
package globaz.libra.utils;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author hpe
 * 
 *         Classe regroupant des méthodes utiles pour le module....
 */

public class LIEcransUtil {

    /**
     * Méthode retourant le détail du gestionnaire
     */
    public static final String getDetailGestionnaire(BSession session, String idGestionnaire)
            throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(idGestionnaire)) {
            return "";
        } else {
            JadeUser userName = session.getApplication()._getSecurityManager().getUserForVisa(session, idGestionnaire);
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }

    }

    /**
     * Méthode qui retourne le détail du tiers sur une ligne
     * 
     * @return le détail du tiers sur une ligne
     * @throws Exception
     */
    public static final String getDetailTiersLigne(BSession session, String idTiers) throws Exception {

        PRTiersWrapper tier = PRTiersHelper.getTiersParId(session, idTiers);

        return PRNSSUtil.formatDetailRequerantDetail(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " " + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                getLibelleCourtSexe(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE), session),
                getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session));

    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public static final String getLibelleCourtSexe(String csSexe, BSession session) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public static final String getLibellePays(String csNationalite, BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
        }

    }

    // Méthodes pour affichage dans les écrans
    /**
     * Méthode qui va regarder dans les properties si on veut afficher le bouton imprimer ou pas
     */
    public static final boolean isWantButtonPrint(BSession session) throws Exception {
        if (session.getApplication().getProperty("isWantButtonPrint").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

}
