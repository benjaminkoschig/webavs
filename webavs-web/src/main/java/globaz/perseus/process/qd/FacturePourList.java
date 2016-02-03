package globaz.perseus.process.qd;

import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.MontantStringConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "LIST_PF_VALIDATION_FACTURE")
public class FacturePourList {

    private String nom;
    private String prenom;
    private String nss;
    private String dateNaissance;
    private String etat;
    private String montant;
    private String dateFacture;
    private String cstypeSousType;
    private String rembourse;
    private String gestionaire;
    private String idFacture;
    private String errorMessage;

    @Column(name = "nss", order = 1)
    public String getNss() {
        return nss;
    }

    @Column(name = "Nom", order = 2)
    public String getNom() {
        return nom;
    }

    @Column(name = "Prenom", order = 3)
    public String getPrenom() {
        return prenom;
    }

    @Column(name = "DateNaissance", order = 4)
    public String getDateNaissance() {
        return dateNaissance;
    }

    @Column(name = "DateFacture", order = 5)
    public String getDateFacture() {
        return dateFacture;
    }

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "TypeSousType", order = 6)
    public String getCstypeSousType() {
        return cstypeSousType;
    }

    @ColumnValueConverter(MontantStringConverter.class)
    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "Montant", order = 7)
    public String getMontant() {
        return montant;
    }

    @ColumnValueConverter(MontantStringConverter.class)
    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "MontantRembourser", order = 8)
    public String getRembourse() {
        return rembourse;
    }

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "Etat", order = 9)
    public String getEtat() {
        return etat;
    }

    @Column(name = "Gestionaire", order = 10)
    public String getGestionaire() {
        return gestionaire;
    }

    @Column(name = "NoFacture", order = 11)
    public String getIdFacture() {
        return idFacture;
    }

    @Column(name = "Erreur", order = 12)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setCstypeSousType(String cstypeSousType) {
        this.cstypeSousType = cstypeSousType;
    }

    public void setRembourse(String rembourse) {
        this.rembourse = rembourse;
    }

    public void setGestionaire(String gestionaire) {
        this.gestionaire = gestionaire;
    }

    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
