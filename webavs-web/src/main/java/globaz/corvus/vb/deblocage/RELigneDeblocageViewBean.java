/*
 * Globaz SA.
 */
package globaz.corvus.vb.deblocage;

import java.io.Serializable;

public class RELigneDeblocageViewBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idLigneDeblocage;
    private long idApplicationAdressePaiement;
    private long idRenteAccordee;
    private long idRoleSection;
    private long idSectionCompensee;
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

    public long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(long idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public long getIdRoleSection() {
        return idRoleSection;
    }

    public void setIdRoleSection(long idRoleSection) {
        this.idRoleSection = idRoleSection;
    }

    public long getIdSectionCompensee() {
        return idSectionCompensee;
    }

    public void setIdSectionCompensee(long idSectionCompensee) {
        this.idSectionCompensee = idSectionCompensee;
    }

}
