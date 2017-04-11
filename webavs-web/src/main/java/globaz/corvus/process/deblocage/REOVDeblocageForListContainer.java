package globaz.corvus.process.deblocage;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverterToDouble.class })
public class REOVDeblocageForListContainer {

    private String desciption;
    private String adressePaiement;
    private Montant montant;

    @Column(name = "LISTE_DEBLOCAGE_OV_DESCRIPTION", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getDesciption() {
        return desciption;
    }

    @Column(name = "LISTE_DEBLOCAGE_OV_ADRESSE_PAEIMENT", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getAdressePaiement() {
        return adressePaiement;
    }

    @Column(name = "LISTE_DEBLOCAGE_OV_MONTANT", order = 3)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00", width = "10%")
    public Montant getMontant() {
        return montant;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

}
