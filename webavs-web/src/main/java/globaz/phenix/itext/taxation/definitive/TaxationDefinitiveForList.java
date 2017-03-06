package globaz.phenix.itext.taxation.definitive;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.common.listoutput.converterImplemented.PourcentConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverter.class, PourcentConverter.class })
public abstract class TaxationDefinitiveForList {
    private String nss;
    private String numAffillie;
    private String designation;
    private Integer anneeTaxation;
    private String dateDebut;
    private String dateFin;
    private String codeService;
    private Integer nbJoursIndamnisation;
    private Montant revenuDefinitif;
    private Montant apgSurLeRevenu;
    private String type;
    private double ecart;

    @ColumnStyle(width = "10%", align = Align.LEFT)
    @Column(name = "LISTE_TAX_DEF_NSS", order = 0)
    public String getNss() {
        return nss;
    }

    @ColumnStyle(width = "8%", align = Align.LEFT)
    @Column(name = "LISTE_TAX_DEF_NAFF", order = 1)
    public String getNumAffillie() {
        return numAffillie;
    }

    @Column(name = "LISTE_TAX_DEF_DESIGNATION", order = 2)
    public String getDesignation() {
        return designation;
    }

    @ColumnStyle(width = "7%", align = Align.CENTER)
    @Column(name = "LISTE_TAX_DEF_ANNEE_TAXATION", order = 3)
    public Integer getAnneeTaxation() {
        return anneeTaxation;
    }

    @ColumnStyle(width = "6.5%", align = Align.CENTER)
    @Column(name = "LISTE_TAX_DEF_DATE_DEBUT", order = 4)
    public String getDateDebut() {
        return dateDebut;
    }

    @ColumnStyle(width = "6.5%", align = Align.CENTER)
    @Column(name = "LISTE_TAX_DEF_DATE_FIN", order = 5)
    public String getDateFin() {
        return dateFin;
    }

    @ColumnStyle(width = "5.5%", align = Align.RIGHT)
    @Column(name = "LISTE_TAX_DEF_CODE_SERVICE", order = 6)
    public String getCodeService() {
        return codeService;
    }

    @Column(name = "LISTE_TAX_DEF_NB_JOUR_INDEMNISATION", order = 7)
    public Integer getNbJoursIndamnisation() {
        return nbJoursIndamnisation;
    }

    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "LISTE_TAX_DEF_REVENU_DETERMINANT", order = 8)
    public Montant getRevenuDefinitif() {
        return revenuDefinitif;
    }

    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "LISTE_TAX_DEF_REVENU_INDEPENDANT", order = 9)
    public Montant getApgSurLeRevenu() {
        return apgSurLeRevenu;
    }

    @ColumnStyle(width = "5%")
    @Column(name = "LISTE_TAX_DEF_TYPE", order = 10)
    public String getType() {
        return type;
    }

    @ColumnStyle(width = "7%", align = Align.RIGHT)
    @Column(name = "LISTE_TAX_DEF_ECART", order = 11)
    public double getEcart() {
        return ecart;
    }

    public void setNumAffillie(String numAffillie) {
        this.numAffillie = numAffillie;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setAnneeTaxation(Integer anneeTaxation) {
        this.anneeTaxation = anneeTaxation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setCodeService(String codeService) {
        this.codeService = codeService;
    }

    public void setNbJoursIndamnisation(Integer nbJoursIndamnisation) {
        this.nbJoursIndamnisation = nbJoursIndamnisation;
    }

    public void setRevenuDefinitif(Montant revenuDefinitif) {
        this.revenuDefinitif = revenuDefinitif;
    }

    public void setApgSurLeRevenu(Montant apgSurLeRevenu) {
        this.apgSurLeRevenu = apgSurLeRevenu;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEcart(double ecart) {
        this.ecart = ecart;
    }
}
