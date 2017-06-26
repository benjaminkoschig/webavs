package globaz.hercule.process.controleEmployeur;

import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

public class AffilieSuiviForListe {

    public static final String NUMERO_INFOROM = "0325CCE";

    private String numeroAffilie;
    private String description;
    private String dateDebutAffiliation;
    private String dateFinAffiliation;
    private String anneeSuivi;

    public AffilieSuiviForListe() {
        super();
    }

    @Column(name = "numero_Affilie", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    @Column(name = "description", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getDescription() {
        return description;
    }

    @Column(name = "date_Debut_Affiliation", order = 3)
    @ColumnStyle(align = Align.RIGHT)
    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    @Column(name = "date_Fin_Affiliation", order = 4)
    @ColumnStyle(align = Align.RIGHT)
    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    @Column(name = "annee_Suivi", order = 5)
    @ColumnStyle(align = Align.RIGHT)
    public String getAnneeSuivi() {
        return anneeSuivi;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setAnneeSuivi(String anneeSuivi) {
        this.anneeSuivi = anneeSuivi;
    }

}
