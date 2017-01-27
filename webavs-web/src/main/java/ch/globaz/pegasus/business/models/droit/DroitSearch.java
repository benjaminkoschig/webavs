/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ECO
 * 
 */
public class DroitSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CURRENT_VERSION = "currentVersion";
    public static final String CURRENT_VERSION_WITH_DATE_FIN_DEMANDE_NULL = "currentVersionWithDateFinDemandeNull";
    public static final String OLD_DEMANDE = "withOldDemande";

    private String forCsEtatDemande = null;
    private String forCsEtatDroit = null;
    private List<String> forCsEtatDroitIn = null;
    private List<String> forIdsDemandeIn = new ArrayList<String>();
    private String forCsSexe = null;
    private String forDateDebutDemande = null;
    private String forDateFinDemande = null;
    private String forDateNaissance = null;
    private String forIdDemandePc = null;
    private String forIdDroit = null;
    private String forIdTiers = null;
    private String forIdVersionDroit = null;
    private String forNss = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    /**
     * @return the forCsEtatDroit
     */
    public String getForCsEtatDroit() {
        return forCsEtatDroit;
    }

    /**
     * @return the forCsEtatDroitIn
     */
    public List<String> getForCsEtatDroitIn() {
        return forCsEtatDroitIn;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDebutDemande() {
        return forDateDebutDemande;
    }

    public String getForDateFinDemande() {
        return forDateFinDemande;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemandePc() {
        return forIdDemandePc;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forIdVersionDroit
     */
    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public String getForNss() {
        return forNss;
    }

    /**
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return the likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    /**
     * @param forCsEtatDroit
     *            the forCsEtatDroit to set
     */
    public void setForCsEtatDroit(String forCsEtatDroit) {
        this.forCsEtatDroit = forCsEtatDroit;
    }

    /**
     * @param forCsEtatDroitIn
     *            the forCsEtatDroitIn to set
     */
    public void setForCsEtatDroitIn(List<String> forCsEtatDroitIn) {
        this.forCsEtatDroitIn = forCsEtatDroitIn;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDebutDemande(String forDateDebutDemande) {
        this.forDateDebutDemande = forDateDebutDemande;
    }

    public void setForDateFinDemande(String forDateFinDemande) {
        this.forDateFinDemande = forDateFinDemande;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemandePc(String forIdDemande) {
        forIdDemandePc = forIdDemande;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forIdVersionDroit
     *            the forIdVersionDroit to set
     */
    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    /**
     * @param likeNom
     *            the likeNom to set
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    /**
     * @param likeNss
     *            the likeNss to set
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * @param likePrenom
     *            the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    public List<String> getForIdsDemandeIn() {
        return forIdsDemandeIn;
    }

    public void setForIdsDemandeIn(List<String> forIdsDemandeIn) {
        this.forIdsDemandeIn = forIdsDemandeIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<Droit> whichModelClass() {
        return Droit.class;
    }

}
