package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe utiliser pour la recherche des âges limites pour le calcul des échéances
 * 
 * @author PTA
 * 
 */
public class EcheanceComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * limite inférieure de l'âge pour l'échéance
     */
    private String ageDebut = null;
    /**
     * limite supérieure de l'âge pour l'échéance
     */
    private String ageFin = null;

    /**
     * Constructeur de EcheanceComplexModel - crée les objets modèle contenus dans ce modèle
     */
    public EcheanceComplexModel() {
        super();
    }

    /**
     * @return the ageDebut
     */
    public String getAgeDebut() {
        return ageDebut;
    }

    /**
     * @return the ageFin
     */
    public String getAgeFin() {
        return ageFin;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    /**
     * @param ageDebut
     *            the ageDebut to set
     */
    public void setAgeDebut(String ageDebut) {
        this.ageDebut = ageDebut;
    }

    /**
     * @param ageFin
     *            the ageFin to set
     */
    public void setAgeFin(String ageFin) {
        this.ageFin = ageFin;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }
}
