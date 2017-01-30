package ch.globaz.amal.businessimpl.services.sedexCO.listes;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.common.listoutput.converterImplemented.PeriodeConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ PeriodeConverter.class, MontantConverterToDouble.class })
public class SimpleOutputList_Decompte_5234_401_1 {
    private String nssDebiteur = null;
    private String nomPrenomDebiteur = null;
    private String typeActe = null;
    private Montant interets = null;
    private Montant frais = null;
    private Montant total = null;
    private String nssAssure = null;
    private String nomPrenomAssure = null;
    private Periode primePeriode = null;
    private Montant primeMontant = null;
    private Periode sharingPeriode = null;
    private Montant sharingMontant = null;
    private String message = null;

    @Column(name = "NSS débiteur", order = 0)
    public String getNssDebiteur() {
        return nssDebiteur;
    }

    @Column(name = "Débiteur", order = 1)
    public String getNomPrenomDebiteur() {
        return nomPrenomDebiteur;
    }

    @Column(name = "Acte", order = 2)
    @ColumnValueConverter(CodeSystemeConverter.class)
    public String getTypeActe() {
        return typeActe;
    }

    @Column(name = "NSS Assuré", order = 3)
    public String getNssAssure() {
        return nssAssure;
    }

    @Column(name = "Assuré", order = 4)
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    @Column(name = "Prime période", order = 5)
    public Periode getPrimePeriode() {
        return primePeriode;
    }

    @Column(name = "Prime montant", order = 6)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getPrimeMontant() {
        return primeMontant;
    }

    @Column(name = "Participation période", order = 7)
    public Periode getSharingPeriode() {
        return sharingPeriode;
    }

    @Column(name = "Participation montant", order = 8)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getSharingMontant() {
        return sharingMontant;
    }

    @Column(name = "Intérêts", order = 9)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getInterets() {
        return interets;
    }

    @Column(name = "Frais", order = 10)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFrais() {
        return frais;
    }

    @Column(name = "Total", order = 11)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getTotal() {
        return total;
    }

    @Column(name = "Message", order = 12)
    public String getMessage() {
        return message;
    }

    public void setNssDebiteur(String nssDebiteur) {
        this.nssDebiteur = nssDebiteur;
    }

    public void setNomPrenomDebiteur(String nomPrenomDebiteur) {
        this.nomPrenomDebiteur = nomPrenomDebiteur;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public void setInterets(Montant interets) {
        this.interets = interets;
    }

    public void setFrais(Montant frais) {
        this.frais = frais;
    }

    public void setTotal(Montant total) {
        this.total = total;
    }

    public void setNssAssure(String nssAssure) {
        this.nssAssure = nssAssure;
    }

    public void setNomPrenomAssure(String nomPrenomAssure) {
        this.nomPrenomAssure = nomPrenomAssure;
    }

    public void setPrimePeriode(Periode primePeriode) {
        this.primePeriode = primePeriode;
    }

    public void setPrimeMontant(Montant primeMontant) {
        this.primeMontant = primeMontant;
    }

    public void setSharingPeriode(Periode sharingPeriode) {
        this.sharingPeriode = sharingPeriode;
    }

    public void setSharingMontant(Montant sharingMontant) {
        this.sharingMontant = sharingMontant;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}