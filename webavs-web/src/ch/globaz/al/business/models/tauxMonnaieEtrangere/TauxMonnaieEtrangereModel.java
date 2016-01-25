/**
 * 
 */
package ch.globaz.al.business.models.tauxMonnaieEtrangere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * modèle pour le taux de monnaie étrangère
 * 
 * @author PTA
 * 
 */
public class TauxMonnaieEtrangereModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * date de début la validité du taux de la monnaie étangère
     */
    private String debutTaux = null;
    /**
     * date de fin de la validité du taux de la monnaie étrangère
     */
    private String finTaux = null;
    /**
     * identifiant du taux de la monnaie etrangère
     */
    private String idTauxMonnaieEtrangere = null;

    /**
     * taux de la monnaie étrangère
     */
    private String tauxMonnaie = null;

    /**
     * type de la monnaie étrangère, code système
     */
    private String typeMonnaie = null;

    /**
     * @return the debutTaux
     */
    public String getDebutTaux() {
        return debutTaux;
    }

    /**
     * @return the finTaux
     */
    public String getFinTaux() {
        return finTaux;
    }

    /**
     * retourne l'identifiant du taux de monnaie étrangère
     */
    @Override
    public String getId() {

        return idTauxMonnaieEtrangere;
    }

    /**
     * @return the idTauxMonnaieEtrangere
     */
    public String getIdTauxMonnaieEtrangere() {
        return idTauxMonnaieEtrangere;
    }

    /**
     * @return the tauxMonnaie
     */
    public String getTauxMonnaie() {
        return tauxMonnaie;
    }

    /**
     * @return the typeMonnaie
     */
    public String getTypeMonnaie() {
        return typeMonnaie;
    }

    /**
     * @param debutTaux
     *            the debutTaux to set
     */
    public void setDebutTaux(String debutTaux) {
        this.debutTaux = debutTaux;
    }

    /**
     * @param finTaux
     *            the finTaux to set
     */
    public void setFinTaux(String finTaux) {
        this.finTaux = finTaux;
    }

    /**
     * @param idTauxMonnaieEtrangere
     *            the idTauxMonnaieEtrangere to set
     */
    @Override
    public void setId(String idTauxMonnaieEtrangere) {
        this.idTauxMonnaieEtrangere = idTauxMonnaieEtrangere;

    }

    /**
     * @param idTauxMonnaieEtrangere
     *            the idTauxMonnaieEtrangere to set
     */
    public void setIdTauxMonnaieEtrangere(String idTauxMonnaieEtrangere) {
        this.idTauxMonnaieEtrangere = idTauxMonnaieEtrangere;
    }

    /**
     * @param tauxMonnaie
     *            the tauxMonnaie to set
     */
    public void setTauxMonnaie(String tauxMonnaie) {
        this.tauxMonnaie = tauxMonnaie;
    }

    /**
     * @param typeMonnaie
     *            the typeMonnaie to set
     */
    public void setTypeMonnaie(String typeMonnaie) {
        this.typeMonnaie = typeMonnaie;
    }

}
