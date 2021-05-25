/*
 * Créé le 13 mai 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APEnfantPat;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.canton.PRCanton;
import globaz.prestation.interfaces.util.nss.canton.PRCantonManager;
import globaz.prestation.tools.nnss.PRNSSUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

/**
 * DOCUMENT ME!
 *
 * @author vre
 */
public class APEnfantPatViewBean extends APEnfantPat implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Getter
    private APDroitDTO droitDTO = null;
    @Getter
    @Setter
    private String numeroDelaiCadre="";

    @Getter
    @Setter
    private Boolean delaiCadreModifie=Boolean.FALSE;

    @Getter
    @Setter
    private boolean copyDroit=false;


    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit pat
     *
     * @return la valeur courante de l'attribut date debut droit pat
     */
    public String getDateDebutDroitPat() {
        if (droitDTO != null) {
            return droitDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }


    /**
     * getter pour l'attribut date naissance
     *
     * <p>
     * Retourne la date de debut du droit si la date de naissance est vide.
     * </p>
     *
     * @return la valeur courante de l'attribut date naissance
     */
    @Override
    public String getDateNaissance() {
        String retValue = super.getDateNaissance();

        if (JAUtil.isDateEmpty(retValue)) {
            retValue = getDateDebutDroitPat();
        }

        return retValue;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     *
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroitPat());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVSDroitPat(), getNomPrenomDroitPat(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne l'attribut idTiers du Tiers
     *
     * @return la valeur courante de l'attribut idTiers du Tiers
     * @throws Exception
     */
    public String getIdTiers() throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroitPat());
        if (tiers != null) {
            return tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        } else {
            return "";
        }
    }

    /**
     * (non javadoc)
     *
     * @return DOCUMENT ME!
     * @see globaz.apg.db.droits.APSituationFamilialePat#getIdDroitPaternite()
     */
    @Override
    public String getIdDroitPaternite() {
        return super.getIdDroitPaternite();
    }

    /**
     * getter pour l'attribut no AVSDroit pat
     *
     * @return la valeur courante de l'attribut no AVSDroit pat
     */
    public String getNoAVSDroitPat() {
        if (droitDTO != null) {
            if (droitDTO.getNoAVS().length() > 12) {
                return droitDTO.getNoAVS();
            } else {
                return "756." + droitDTO.getNoAVS();
            }
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut nom prenom droit pat
     *
     * @return la valeur courante de l'attribut nom prenom droit pat
     */
    public String getNomPrenomDroitPat() {
        if (droitDTO != null) {
            return droitDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut npa droit pat
     *
     * @return la valeur courante de l'attribut nom prenom droit pat
     */
    public String getNpaDroitPat() {
        if (droitDTO != null) {
            return droitDTO.getNpa();
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     *
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNoAVS(), isNNSS().equals("true") ? true : false);
    }

    /**
     * retourne le libellé du type ou (si le type est null) renvoie le type par défaut: type_enfant.
     *
     * @return la valeur courante de l'attribut type
     */
    public String getTypeLibelle() {
        if (JadeStringUtil.isEmpty(type)) {
            return getSession().getCodeLibelle(IAPDroitMaternite.CS_TYPE_ENFANT);
        } else {
            return getSession().getCodeLibelle(type);
        }
    }

    /**
     * getter pour l'attribut modifiable
     *
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        if (droitDTO != null) {
            return droitDTO.isModifiable();
        } else {
            return false;
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     *
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNoAVS())) {
            return "";
        }

        if (getNoAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * setter pour l'attribut droit DTO
     *
     * @param droitDTO une nouvelle valeur pour cet attribut
     */
    public void setDroitDTO(APDroitDTO droitDTO) {
        this.droitDTO = droitDTO;

        if (droitDTO != null) {
            idDroitPaternite = droitDTO.getIdDroit();
        }
    }

    public String getCanton() {
        String canton = droitDTO.getCanton(getSession());
        if (JadeStringUtil.isBlankOrZero(super.getCanton())) {
            return canton;
        } else {
            return super.getCanton();
        }
    }

    public String getNationaliteAsString() {
        if (!JadeStringUtil.isBlankOrZero(nationalite)) {
            try {
                return PRTiersHelper.getPaysLibelle(nationalite, getSession());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getCantonAsString() {
        String langueUtilisateur = JadeStringUtil.toUpperCase(getSession().getIdLangueISO());
        if (JadeStringUtil.isBlankOrZero(canton)) {
            return "";
        } else {
            try {
                PRCantonManager cantonManager = new PRCantonManager();
                cantonManager.setSession(getSession());
                cantonManager.setForCsCanton(canton);
                cantonManager.find();

                if (cantonManager.size() > 0) {
                    PRCanton entity = (PRCanton) cantonManager.getFirstEntity();
                        return entity.getLibelleCanton();
                }
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
}
