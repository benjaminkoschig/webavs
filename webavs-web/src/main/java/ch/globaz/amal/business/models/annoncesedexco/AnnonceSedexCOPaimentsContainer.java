/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

/**
 * @author LBE
 * 
 */
public class AnnonceSedexCOPaimentsContainer {
    private String idType;
    private String typeLibelle;
    private String montant;

    public AnnonceSedexCOPaimentsContainer() {
        idType = "";
        typeLibelle = "";
        montant = "";
    }

    /**
     * @return the idType
     */
    public String getIdType() {
        return idType;
    }

    /**
     * @param idType the idType to set
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }

    /**
     * @return the typeLibelle
     */
    public String getTypeLibelle() {
        return typeLibelle;
    }

    /**
     * @param typeLibelle the typeLibelle to set
     */
    public void setTypeLibelle(String typeLibelle) {
        this.typeLibelle = typeLibelle;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param montant the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }
}
