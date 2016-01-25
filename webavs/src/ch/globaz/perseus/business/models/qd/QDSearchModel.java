/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DDE
 * 
 */
public class QDSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forCSTypeQD = null;
    /**
     * Champs non utilité dans la recherche mais utilisé pour une liste ajax de qd (forDateFacture et forDateReception)
     */
    private String forDateFacture = null;
    private String forDateReception = null;
    private String forIdDossier = null;
    private String forIdMembreFamille = null;
    private String forIdQdAnnuelle = null;
    private List<String> inCSTypeQD = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public QDSearchModel() {
        super();
        inCSTypeQD = new ArrayList<String>();
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the forCSTypeQD
     */
    public String getForCSTypeQD() {
        return forCSTypeQD;
    }

    /**
     * @return the forDateFacture
     */
    public String getForDateFacture() {
        return forDateFacture;
    }

    /**
     * @return the forDateReception
     */
    public String getForDateReception() {
        return forDateReception;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the forIdQdAnnuelle
     */
    public String getForIdQdAnnuelle() {
        return forIdQdAnnuelle;
    }

    /**
     * @return the inCSTypeQD
     */
    public List<String> getInCSTypeQD() {
        return inCSTypeQD;
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
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forCSTypeQD
     *            the forCSTypeQD to set
     */
    public void setForCSTypeQD(String forCSTypeQD) {
        this.forCSTypeQD = forCSTypeQD;
    }

    /**
     * @param forDateFacture
     *            the forDateFacture to set
     */
    public void setForDateFacture(String forDateFacture) {
        this.forDateFacture = forDateFacture;
    }

    /**
     * @param forDateReception
     *            the forDateReception to set
     */
    public void setForDateReception(String forDateReception) {
        this.forDateReception = forDateReception;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * @param forIdQdAnnuelle
     *            the forIdQdAnnuelle to set
     */
    public void setForIdQdAnnuelle(String forIdQdAnnuelle) {
        this.forIdQdAnnuelle = forIdQdAnnuelle;
    }

    /**
     * @param inCSTypeQD
     *            the inCSTypeQD to set
     */
    public void setInCSTypeQD(List<String> inCSTypeQD) {
        this.inCSTypeQD = inCSTypeQD;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return QD.class;
    }

}
