package globaz.pegasus.process.liste;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter(MontantConverter.class)
public class ContainerByTiers {

    private String idTiers;
    private Montant montantRestitution;
    private Montant montantPaiement;
    private String communePolitique;

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public Montant getMontantRestitution() {
        return montantRestitution;
    }

    public Montant getMontantPaiement() {
        return montantPaiement;
    }

    public ContainerByTiers(String idTiers) {
        this.idTiers = idTiers;
        montantRestitution = Montant.ZERO;
        montantPaiement = Montant.ZERO;
    }

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    public void addMontantRestitution(Montant montant) {
        montantRestitution = montantRestitution.add(montant);
    }

    public void addMontantPaiement(Montant montant) {
        montantPaiement = montantPaiement.add(montant);
    }
}
