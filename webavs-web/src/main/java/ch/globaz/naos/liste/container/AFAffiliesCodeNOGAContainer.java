/**
 * 
 */
package ch.globaz.naos.liste.container;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

/**
 * Classe représentant une entité de la liste des affiliés par code NOGA
 * 
 * @author est
 */

@ColumnValueConverter({ MontantConverterToDouble.class })
public class AFAffiliesCodeNOGAContainer {
    private String numIde;
    private String numAffilie;
    private String raisonSociale;
    private String codeCategorie;
    private String txtCategorie;
    private String codeNoga;
    private String txtNoga;
    private String genreAffi;
    private Montant masseSalariale;
    private Integer nbAssures;

    // Getters

    @Column(name = "LISTE_CODE_NOGA_NUM_IDE", order = 1)
    public String getNumIde() {
        return numIde;
    }

    @Column(name = "LISTE_CODE_NOGA_NUM_AFFILIE", order = 2)
    public String getNumAffilie() {
        return numAffilie;
    }

    @Column(name = "LISTE_CODE_NOGA_RAISON_SOC", order = 3)
    public String getRaisonSociale() {
        return raisonSociale;
    }

    @Column(name = "LISTE_CODE_NOGA_CODE_CAT", order = 5)
    public String getCodeCategorie() {
        return codeCategorie;
    }

    @Column(name = "LISTE_CODE_NOGA_CODE_CAT_TXT", order = 6)
    public String getTxtCategorie() {
        return txtCategorie;
    }

    @Column(name = "LISTE_CODE_NOGA_CODE", order = 4)
    public String getCodeNoga() {
        return codeNoga;
    }

    @Column(name = "LISTE_CODE_NOGA_CODE_TXT", order = 7)
    public String getTxtNoga() {
        return txtNoga;
    }

    @Column(name = "LISTE_CODE_NOGA_GENRE_AFFILIE", order = 8)
    public String getGenreAffi() {
        return genreAffi;
    }

    @Column(name = "LISTE_CODE_NOGA_MASSE_SALARIALE", order = 9)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getMasseSalariale() {
        return masseSalariale;
    }

    @Column(name = "LISTE_CODE_NOGA_NB_ASSURES", order = 10)
    public Integer getNbAssures() {
        return nbAssures;
    }

    // Setters

    public void setNumIde(String numIde) {
        this.numIde = numIde;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setCodeCategorie(String codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

    public void setTxtCategorie(String txtCategorie) {
        this.txtCategorie = txtCategorie;
    }

    public void setCodeNoga(String codeNoga) {
        this.codeNoga = codeNoga;
    }

    public void setTxtNoga(String txtNoga) {
        this.txtNoga = txtNoga;
    }

    public void setGenreAffi(String genreAffi) {
        this.genreAffi = genreAffi;
    }

    public void setMasseSalariale(Montant masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public void setNbAssures(Integer nbAssures) {
        this.nbAssures = nbAssures;
    }
}
