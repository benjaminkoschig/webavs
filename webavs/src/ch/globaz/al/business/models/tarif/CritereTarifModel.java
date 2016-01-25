package ch.globaz.al.business.models.tarif;

/**
 * class repréentant le critère de tarif
 * 
 * @author PTA
 * 
 */
public class CritereTarifModel extends CritereTarifFkModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de sélection de tarif
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CRITERE
     */
    private String critereTarif = null;
    /**
     * Valeur de début de la plage
     */
    private String debutCritere = null;
    /**
     * Valeur de fin de la plage
     */
    private String finCritere = null;

    /**
     * @return the critereTarif
     */
    public String getCritereTarif() {
        return critereTarif;
    }

    /**
     * @return the debutCritere
     */
    public String getDebutCritere() {
        return debutCritere;
    }

    /**
     * @return the finCritere
     */
    public String getFinCritere() {
        return finCritere;
    }

    /**
     * @param critereTarif
     *            the critereTarif to set
     */
    public void setCritereTarif(String critereTarif) {
        this.critereTarif = critereTarif;
    }

    /**
     * @param debutCritere
     *            the debutCritere to set
     */
    public void setDebutCritere(String debutCritere) {
        this.debutCritere = debutCritere;
    }

    /**
     * @param finCritere
     *            the finCritere to set
     */
    public void setFinCritere(String finCritere) {
        this.finCritere = finCritere;
    }
}