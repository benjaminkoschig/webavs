package globaz.pegasus.process.liste;

import globaz.prestation.interfaces.tiers.CommunePolitiqueBean;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter(MontantConverter.class)
public class ContainerByTiers {

    private String idTiers;
    private Montant montantRestitutionPC;
    private Montant montantPaiementPC;
    private Montant montantRestitutionRFM;
    private Montant montantPaiementRFM;
    private CommunePolitiqueBean communePolitique;

    public ContainerByTiers(String idTiers) {
        this.idTiers = idTiers;
        montantRestitutionPC = Montant.ZERO;
        montantPaiementPC = Montant.ZERO;
        montantRestitutionRFM = Montant.ZERO;
        montantPaiementRFM = Montant.ZERO;
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

    public void addMontantRestitutionPC(Montant montant) {
        montantRestitutionPC = montantRestitutionPC.add(montant);
    }

    public void addMontantPaiementPC(Montant montant) {
        montantPaiementPC = montantPaiementPC.add(montant);
    }

    public void addMontantRestitutionRFM(Montant montant) {
        montantRestitutionRFM = montantRestitutionRFM.add(montant);
    }

    public void addMontantPaiementRFM(Montant montant) {
        montantPaiementRFM = montantPaiementRFM.add(montant);
    }

    public CommunePolitiqueBean getCommunePolitique() {
        return communePolitique;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public Montant getMontantRestitutionPC() {
        return montantRestitutionPC;
    }

    public Montant getMontantPaiementPC() {
        return montantPaiementPC;
    }

    public Montant getMontantRestitutionRFM() {
        return montantRestitutionRFM;
    }

    public Montant getMontantPaiementRFM() {
        return montantPaiementRFM;
    }
}
