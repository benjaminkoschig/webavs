package ch.globaz.al.liste.container;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

/**
 * Classe représentant un objet reprenant les informations utile à la liste excel
 * pour les indépendants avec AF avec un revenu minimal non atteint
 * 
 * @author est
 * 
 */
@ColumnValueConverter({ MontantConverterToDouble.class })
public class ALIndeRevenuMinNonAtteintContainer {

    private String numAffilie;
    private String nomAffilie;
    private BigDecimal debutPeriode;
    private BigDecimal finPeriode;
    private Montant revenuDeterminant;
    private Montant revenuMinimal;
    private BigDecimal idDossierAf;

    // Getters
    @Column(name = "LISTE_INDE_NUMERO_AFFILIE", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getNumAffilie() {
        return numAffilie;
    }

    @Column(name = "LISTE_INDE_NOM_AFFILIE", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getNomAffilie() {
        return nomAffilie;
    }

    @Column(name = "LISTE_INDE_DEBUT_PERIODE", order = 3)
    @ColumnStyle(align = Align.RIGHT)
    public String getDebutPeriode() throws JAException {
        JADate dateDebutPeriode = new JADate(debutPeriode);
        return dateDebutPeriode.toStr(".");
    }

    @Column(name = "LISTE_INDE_FIN_PERIODE", order = 4)
    @ColumnStyle(align = Align.RIGHT)
    public String getFinPeriode() throws JAException {
        JADate dateFinPeriode = new JADate(finPeriode);
        return dateFinPeriode.toStr(".");
    }

    @Column(name = "LISTE_INDE_REVENU_DETERMINANT", order = 5)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuDeterminant() {
        return revenuDeterminant;
    }

    @Column(name = "LISTE_INDE_REVENU_MINIMAL", order = 6)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuMinimal() {
        return revenuMinimal;
    }

    @Column(name = "LISTE_INDE_ID_DOSSIER_AF", order = 7)
    @ColumnStyle(align = Align.RIGHT)
    public BigDecimal getIdDossierAf() {
        return idDossierAf;
    }

    // Setters

    public void setIdDossierAf(BigDecimal idDossierAf) {
        this.idDossierAf = idDossierAf;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setDebutPeriode(BigDecimal debutPeriode) {
        this.debutPeriode = debutPeriode;
    }

    public void setFinPeriode(BigDecimal finPeriode) {
        this.finPeriode = finPeriode;
    }

    public void setRevenuDeterminant(Montant revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    public void setRevenuMinimal(Montant revenuMinimal) {
        this.revenuMinimal = revenuMinimal;
    }

    @Override
    public String toString() {
        return "ALIndeRevenuMinNonAtteintPojo [numAffilie=" + numAffilie + ", nomAffilie=" + nomAffilie
                + ", debutPeriode=" + debutPeriode + ", finPeriode=" + finPeriode + ", revenuDeterminant="
                + revenuDeterminant + ", revenuMinimal=" + revenuMinimal + ", idDossierAf=" + idDossierAf + "]";
    }

}