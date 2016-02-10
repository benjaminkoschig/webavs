package globaz.pegasus.process.liste;

import globaz.prestation.interfaces.tiers.CommunePolitiqueBean;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter(MontantConverter.class)
public class ContainerByTiers {

    private String idTiers;
    private Montant montantRestitution;
    private Montant montantPaiement;
    private CommunePolitiqueBean communePolitique;

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

    public String getCodeCommunePolitique() {
        return communePolitique.getCode();
    }

    public String getNomCommunePolitique() {
        return communePolitique.getNom();
    }

    public void setCommunePolitique(CommunePolitiqueBean communePolitique) {
        this.communePolitique = communePolitique;
    }

    public void addMontantRestitution(Montant montant) {
        montantRestitution = montantRestitution.add(montant);
    }

    public void addMontantPaiement(Montant montant) {
        montantPaiement = montantPaiement.add(montant);
    }

    public CommunePolitiqueBean getCommunePolitique() {
        return communePolitique;
    }

}
