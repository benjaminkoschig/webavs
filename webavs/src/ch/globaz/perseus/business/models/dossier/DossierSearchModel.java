package ch.globaz.perseus.business.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class DossierSearchModel extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    private String betweenDateRevisionDebut = null;
    private String betweenDateRevisionFin = null;
    // private String forRevision = null;
    private String forCsSexe = null;
    private String forDateNaissance = null;
    private String forIdDossier = null;
    // private String forEnCours = null;
    private String forIdGestionnaire = null;
    private String forIdTiers = null;
    private List<String> inIdDossier = new ArrayList<String>();
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    /**
     * retourne la condition de recherche inférieure pour la date de révision
     * 
     * @return the betweenDateRevisionDebut
     */
    public String getBetweenDateRevisionDebut() {
        return betweenDateRevisionDebut;
    }

    /**
     * retourne la condition de recherche suppérieure pour la date de révision
     * 
     * @return the betweenDateRevisionFin
     */
    public String getBetweenDateRevisionFin() {
        return betweenDateRevisionFin;
    }

    /**
     * retourne la condition de recherche sur le code system du sexe de l'assuré
     * 
     * @return the forSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * retourne la condition de recherche sur la date de naissance de l'assuré
     * 
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * retourne la condition de recherche sur l'id du gestionnaire
     * 
     * @return the forGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return the forIdTier
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the inIdDossier
     */
    public List<String> getInIdDossier() {
        return inIdDossier;
    }

    /**
     * retourne la condition de recherche sur le nom de l'assuré
     * 
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * retourne la condition de recherche sur le no avs de l'assuré
     * 
     * @return the likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * retourne la condition de recherche sur le prénom de l'assuré
     * 
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * définit la condition de recherche inférieure pour la date de révision
     * 
     * @param betweenDateRevisionDebut
     *            Le mois de révision inférieur au format mm.yyyy
     */
    public void setBetweenDateRevisionDebut(String betweenDateRevisionDebut) {
        // Set sur le premier jour du mois inférieur (recherche greater_or_equals)
        this.betweenDateRevisionDebut = (JadeStringUtil.isEmpty(betweenDateRevisionDebut)) ? null : "01."
                + betweenDateRevisionDebut;
    }

    /**
     * définit la condition de recherche suppérieure pour la date de révision
     * 
     * @param betweenDateRevisionFin
     *            Le mois de révision suppérieur au format mm.yyyy
     */
    public void setBetweenDateRevisionFin(String betweenDateRevisionFin) {
        if (!JadeStringUtil.isEmpty(betweenDateRevisionFin)) {
            // Set sur le premier jour du mois suivant (recherche less)
            // TODO : A refaire plus correctement
            Integer mois = Integer.parseInt(betweenDateRevisionFin.substring(0, 2));
            Integer annee = Integer.parseInt(betweenDateRevisionFin.substring(3));
            String moisStr = "";
            if (mois == 12) {
                mois = 1;
                moisStr = "01";
                annee++;
            } else {
                mois++;
                if (mois.toString().length() < 2) {
                    moisStr = "0" + mois.toString();
                } else {
                    moisStr = mois.toString();
                }
            }
            this.betweenDateRevisionFin = "01." + moisStr + "." + annee;
        } else {
            this.betweenDateRevisionFin = null;
        }
    }

    /**
     * définit la condition de recherche sur le sexe de l'assuré
     * 
     * @param forCsSexe
     *            code system du sexe
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    /**
     * définit la condition de recherche sur la date de naissance de l'assuré
     * 
     * @param forDateNaissance
     *            date de naissance au format DDMMAAAA
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * définit la condition de recherche sur le gestionnaire du dossier
     * 
     * @param forIdGestionnaire
     *            id du gestionnaire
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    /**
     * @param forIdTier
     *            the forIdTier to set
     */
    public void setForIdTiers(String forIdTier) {
        forIdTiers = forIdTier;
    }

    /**
     * @param inIdDossier
     *            the inIdDossier to set
     */
    public void setInIdDossier(List<String> inIdDossier) {
        this.inIdDossier = inIdDossier;
    }

    /**
     * définit la condition de recherche sur le nom de l'assuré
     * 
     * @param likeNom
     *            nom de l'assuré
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    /**
     * définit la condition de recherche sur le no AVS de l'assuré
     * 
     * @param likeNss
     *            Numéro AVS de l'assuré
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * définit la condition de recherche sur le prénom de l'assuré
     * 
     * @param likePrenom
     *            prénom de l'assuré
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Dossier.class;
    }

}
