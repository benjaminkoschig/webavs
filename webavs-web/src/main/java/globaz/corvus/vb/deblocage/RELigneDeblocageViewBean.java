package globaz.corvus.vb.deblocage;

import java.io.Serializable;

public class RELigneDeblocageViewBean implements Serializable {

    private String idLigneDeblocage;
    private int idApplicationAdressePaiement;
    private int idRentePrestation;
    private int idRoleDetteEnCompta;
    private int idSectionDetteEnCompta;
    private int idTiersAdressePaiement;
    private int idTiersCreancier;
    private String etatDeblocage;
    private String typeDeblocage;
    private String montant;
    private String refPaiement;
    private String spy;

    public String getSpy() {
        return spy;
    }

    public void setSpy(String spy) {
        this.spy = spy;
    }

    public int getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public void setIdApplicationAdressePaiement(int idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    public int getIdRentePrestation() {
        return idRentePrestation;
    }

    public void setIdRentePrestation(int idRentePrestation) {
        this.idRentePrestation = idRentePrestation;
    }

    public int getIdRoleDetteEnCompta() {
        return idRoleDetteEnCompta;
    }

    public void setIdRoleDetteEnCompta(int idRoleDetteEnCompta) {
        this.idRoleDetteEnCompta = idRoleDetteEnCompta;
    }

    public int getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public void setIdSectionDetteEnCompta(int idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public int getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(int idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public int getIdTiersCreancier() {
        return idTiersCreancier;
    }

    public void setIdTiersCreancier(int idTiersCreancier) {
        this.idTiersCreancier = idTiersCreancier;
    }

    public String getEtatDeblocage() {
        return etatDeblocage;
    }

    public void setEtatDeblocage(String etatDeblocage) {
        this.etatDeblocage = etatDeblocage;
    }

    public String getTypeDeblocage() {
        return typeDeblocage;
    }

    public void setTypeDeblocage(String typeDeblocage) {
        this.typeDeblocage = typeDeblocage;
    }

    public String getIdLigneDeblocage() {
        return idLigneDeblocage;
    }

    public void setIdLigneDeblocage(String idLigneDeblocage) {
        this.idLigneDeblocage = idLigneDeblocage;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

}
