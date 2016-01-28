package ch.globaz.al.business.models.allocataire;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Classe de recherche d'un allocataire
 * 
 * @author JTS/PTA
 * 
 */
public class AllocataireComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * date de naissance
     */
    private String forDateNaissance = null;

    /**
     * identifiant de l'allocataire
     */
    private String forIdAllocataire = null;

    /**
     * Identifiant du tiers
     */
    private String forIdTiers = null;

    /**
     * Nom de l'allocataire (identique)
     */
    private String forNomAllocataire = null;

    /**
     * Numéro NSS (total)
     */
    private String forNumNss = null;
    /**
     * prénom de l'allocataire (identique)
     */
    private String forPrenomAllocataire = null;

    /**
     * Nom de l'allocataire
     */
    private String likeNomAllocataire = null;

    /**
     * Numéro NSS
     */
    private String likeNssAllocataire = null;
    /**
     * Prénom allocataire
     */
    private String likePrenomAllocataire = null;
    
    private String likeNomAllocataireUpper = null;

    private String likePrenomAllocataireUpper = null;

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * 
     * @return forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forNomAllocataire
     */
    public String getForNomAllocataire() {
        return forNomAllocataire;
    }

    /**
     * 
     * @return forNumNss
     */

    public String getForNumNss() {
        return forNumNss;
    }

    /**
     * @return the forPrenomAllocataire
     */
    public String getForPrenomAllocataire() {
        return forPrenomAllocataire;
    }

    /**
     * 
     * @return likeNomAllocataire
     */

    public String getLikeNomAllocataire() {
        return likeNomAllocataire;
    }

    /**
     * 
     * @return likeNssAllocataire
     */

    public String getLikeNssAllocataire() {
        return likeNssAllocataire;
    }

    /**
     * 
     * @return likePrenomAllocataire
     */

    public String getLikePrenomAllocataire() {
        return likePrenomAllocataire;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdAllocataire
     *            the forIdAllocataire to set
     */
    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    /**
     * 
     * Définit l'identifiant du tiers pour la recherche
     * 
     * @param forIdTiers
     *            : the forIdTiers to set
     */

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forNomAllocataire
     *            the forNomAllocataire to set
     */
    public void setForNomAllocataire(String forNomAllocataire) {
        this.forNomAllocataire = forNomAllocataire;
    }

    /**
     * 
     * Définit le numéro NSS pour la rcherche
     * 
     * @param forNumNss
     *            : the forNumNss to set
     */

    public void setForNumNss(String forNumNss) {
        this.forNumNss = forNumNss;
    }

    /**
     * @param forPrenomallocataire
     *            the forPrenomallocataire to set
     */
    public void setForPrenomAllocataire(String forPrenomallocataire) {
        forPrenomAllocataire = forPrenomallocataire;
    }

    /**
     * Définit le nom de l'allocataire pour la recherche
     * 
     * @param likeNomAllocataire
     *            : the likeNomAllocataire to set
     */

    public void setLikeNomAllocataire(String likeNomAllocataire) {
        this.likeNomAllocataire = likeNomAllocataire;
    }

    /**
     * Définit le numéro NSS pour la recherche
     * 
     * @param likeNssAllocataire
     *            : the likeNssAllocataire to set
     */

    public void setLikeNssAllocataire(String likeNssAllocataire) {
        this.likeNssAllocataire = likeNssAllocataire;
    }

    /**
     * Définit le prénom pour la recherche
     * 
     * @param likePrenomAllocataire
     *            : the likePrenomAllocataire to set
     */
    public void setLikePrenomAllocataire(String likePrenomAllocataire) {
        this.likePrenomAllocataire = likePrenomAllocataire;
    }
    
    public String getLikeNomAllocataireUpper() {
        return likeNomAllocataireUpper;
    }

    public void setLikeNomAllocataireUpper(String likeNomAllocataireUpper) {
        this.likeNomAllocataireUpper = JadeStringUtil.toUpperCase(likeNomAllocataireUpper);
    }

    public String getLikePrenomAllocataireUpper() {
        return likePrenomAllocataireUpper;
    }

    public void setLikePrenomAllocataireUpper(String likePrenomAllocataireUpper) {
        this.likePrenomAllocataireUpper = JadeStringUtil.toUpperCase(likePrenomAllocataireUpper);
    }

    @Override
    public Class<AllocataireComplexModel> whichModelClass() {
        return AllocataireComplexModel.class;
    }

}
