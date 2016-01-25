/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APRepartitionJointPrestationViewBean extends APRepartitionJointPrestation implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient APDroitLAPG droit = null;
    private transient APDroitDTO droitDTO = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        if (droitDTO != null) {
            return droitDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroit());

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

            return PRNSSUtil.formatDetailRequerantDetail(
                    getNoAVSDroit(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut droit DTO
     * 
     * @return la valeur courante de l'attribut droit DTO
     */
    public APDroitDTO getDroitDTO() {
        return droitDTO;
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    @Override
    public String getGenreService() {
        if (droitDTO != null) {
            return droitDTO.getGenreService();
        } else {
            return "";
        }
    }

    /**
     * @see globaz.apg.db.prestation.APRepartitionJointPrestation#getMontantBrutPrestation()
     */
    @Override
    public String getMontantBrut() {
        return JANumberFormatter.fmt(super.getMontantBrut(), true, true, true, 2);
    }

    /**
     * getter pour l'attribut no AVSDroit
     * 
     * @return la valeur courante de l'attribut no AVSDroit
     */
    public String getNoAVSDroit() {
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
     * getter pour l'attribut prenom nom droit
     * 
     * @return la valeur courante de l'attribut prenom nom droit
     */
    public String getPrenomNomDroit() {
        if (droitDTO != null) {
            return droitDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APDroitLAPG loadDroit() throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(super.getIdDroit())) {
            droit = APGUtils.loadDroit(getSession(), super.getIdDroit(), super.getGenreService());
        }

        return droit;
    }

    /**
     * setter pour l'attribut droit DTO
     * 
     * @param droitDTO
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitDTO(APDroitDTO droitDTO) {
        this.droitDTO = droitDTO;
    }
}
