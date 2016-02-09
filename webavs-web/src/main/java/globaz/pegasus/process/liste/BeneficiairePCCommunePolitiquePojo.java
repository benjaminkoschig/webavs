package globaz.pegasus.process.liste;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter(MontantConverter.class)
@Translater(value = LabelTranslater.class, identifier = "PEGASUS_LISTE_EXCEL_CP")
public class BeneficiairePCCommunePolitiquePojo implements Comparable<BeneficiairePCCommunePolitiquePojo> {

    private String communePolitique = "";
    private String idTiers = "";
    private String nss = "";
    private String nom = "";
    private String prenom = "";
    private String codePrestation = "";
    private String typePrestation = "";
    private Montant montant = Montant.ZERO;

    @Column(name = "commune", order = 1)
    @ColumnStyle(align = Align.CENTER)
    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Column(name = "Nss", order = 2)
    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Column(name = "Nom", order = 3)
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Column(name = "Prenom", order = 4)
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Column(name = "codePrestation", order = 5)
    public String getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    @Column(name = "Montant", order = 6)
    @ColumnStyle(align = Align.RIGHT)
    @Aggregate(AggregateFunction.SUM)
    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    @Override
    public int compareTo(BeneficiairePCCommunePolitiquePojo o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }

}
