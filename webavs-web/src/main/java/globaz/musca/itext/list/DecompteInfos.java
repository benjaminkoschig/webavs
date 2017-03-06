package globaz.musca.itext.list;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter({ MontantConverterToDouble.class })
class DecompteInfos {
    private String libelle;
    private Montant montant;

    public DecompteInfos(String libelle, Montant montant) {
        super();
        this.libelle = libelle;
        this.montant = montant;
    }

    @Column(name = "LISDEC_RECAPTYPE", order = 1)
    public String getLibelle() {
        return libelle;
    }

    @Column(name = "MONTANT", order = 2)
    public Montant getMontant() {
        return montant;
    }

}
