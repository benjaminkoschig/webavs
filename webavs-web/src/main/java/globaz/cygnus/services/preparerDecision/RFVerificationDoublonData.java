/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationDoublonData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String dateFinTraitement = "";
    private Set<String> idsDemandeToIgnore = null;
    private String idTiers = "";
    private String montantFacture = "";
    private String numeroDecompte = "";

    public RFVerificationDoublonData(String idTiers, String codeSousTypeDeSoin, String codeTypeDeSoin,
            Set<String> idsDemandeToIgnore, String dateFacture, String dateDebutTraitement, String dateFinTraitement,
            String montantFacture, String numeroDecompte) {
        this.idTiers = idTiers;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.idsDemandeToIgnore = idsDemandeToIgnore;
        this.dateFacture = dateFacture;
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFinTraitement = dateFinTraitement;
        this.montantFacture = montantFacture;
        this.numeroDecompte = numeroDecompte;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public Set<String> getIdsDemandeToIgnore() {
        return idsDemandeToIgnore;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getNumeroDecompte() {
        return numeroDecompte;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setIdsDemandeToIgnore(Set<String> idsDemandeToIgnore) {
        this.idsDemandeToIgnore = idsDemandeToIgnore;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

}
