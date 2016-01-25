package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Mod�le complexe utiliser pour la recherche des �ges limites pour le calcul des �ch�ances
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
     * limite inf�rieure de l'�ge pour l'�ch�ance
     */
    private String ageDebut = null;
    /**
     * limite sup�rieure de l'�ge pour l'�ch�ance
     */
    private String ageFin = null;

    /**
     * Constructeur de EcheanceComplexModel - cr�e les objets mod�le contenus dans ce mod�le
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
