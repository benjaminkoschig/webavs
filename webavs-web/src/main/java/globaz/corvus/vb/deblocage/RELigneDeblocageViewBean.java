/*
 * Globaz SA.
 */
package globaz.corvus.vb.deblocage;

import java.io.Serializable;

public class RELigneDeblocageViewBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idLigneDeblocage;
    private long idApplicationAdressePaiement;
    private long idRentePrestation;
    private long idRoleDetteEnCompta;
    private long idSectionDetteEnCompta;
    private long idTiersAdressePaiement;
    private long idTiersCreancier;
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

    public long getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public void setIdApplicationAdressePaiement(long idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    public long getIdRentePrestation() {
        return idRentePrestation;
    }

    public void setIdRentePrestation(long idRentePrestation) {
        this.idRentePrestation = idRentePrestation;
    }

    public long getIdRoleDetteEnCompta() {
        return idRoleDetteEnCompta;
    }

    public void setIdRoleDetteEnCompta(long idRoleDetteEnCompta) {
        this.idRoleDetteEnCompta = idRoleDetteEnCompta;
    }

    public long getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public void setIdSectionDetteEnCompta(long idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public long getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(long idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public long getIdTiersCreancier() {
        return idTiersCreancier;
    }

    public void setIdTiersCreancier(long idTiersCreancier) {
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
