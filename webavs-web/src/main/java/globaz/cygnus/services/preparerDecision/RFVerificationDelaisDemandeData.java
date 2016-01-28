/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationDelaisDemandeData {

    private String dateDeces = "";
    private String dateFacture = "";
    private String dateReception = "";
    private String idTiers = "";
    private String montantAccepte = "";

    public RFVerificationDelaisDemandeData(String dateFacture, String dateReception, String idTiers,
            String montantAccepte, String dateDeces) {
        super();
        this.dateFacture = dateFacture;
        this.dateReception = dateReception;
        this.idTiers = idTiers;
        this.montantAccepte = montantAccepte;
        this.dateDeces = dateDeces;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantAccepte() {
        return montantAccepte;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantAccepte(String montantAccepte) {
        this.montantAccepte = montantAccepte;
    }

}
