package ch.globaz.al.business.models.prestation.paiement;

/**
 * Mod�le d�taill� pour la compensation sur facture de prestations AF
 * 
 * @author jts
 */
public class CompensationPrestationFullComplexModel extends CompensationPrestationComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Nom de l'affili�
     */
    private String nomAffilie = null;

    /**
     * @return the nomAffilie
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * @param nomAffilie
     *            the nomAffilie to set
     */
    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }
}