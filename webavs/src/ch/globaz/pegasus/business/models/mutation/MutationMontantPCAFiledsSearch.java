package ch.globaz.pegasus.business.models.mutation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class MutationMontantPCAFiledsSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDecisionMax;
    private String forDateDecisionMin;
    private String forDateMonthFutur = null;
    private String forIdPcaParent = null;
    private boolean forIsPlanRetenu = false;
    private boolean forIsSupprime = false;

    private Collection<String> inEtatDroitCourant = null;
    private Collection<String> inEtatDroitPrecedant = null;
    private Collection<String> inEtatPca = null;
    private Collection<String> inEtatPcaPrecedante = null;
    private Collection<String> inTypeDecision = null;

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
    }

    public String getForDateMonthFutur() {
        return forDateMonthFutur;
    }

    public String getForIdPcaParent() {
        return forIdPcaParent;
    }

    public boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public boolean getForIsSupprime() {
        return forIsSupprime;
    }

    public Collection<String> getInEtatDroitCourant() {
        return inEtatDroitCourant;
    }

    public Collection<String> getInEtatDroitPrecedant() {
        return inEtatDroitPrecedant;
    }

    public Collection<String> getInEtatPca() {
        return inEtatPca;
    }

    public Collection<String> getInEtatPcaPrecedante() {
        return inEtatPcaPrecedante;
    }

    public Collection<String> getInTypeDecision() {
        return inTypeDecision;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
    }

    public void setForDateMonthFutur(String forDateMonthFutur) {
        this.forDateMonthFutur = forDateMonthFutur;
    }

    public void setForIdPcaParent(String forIdPcaParent) {
        this.forIdPcaParent = forIdPcaParent;
    }

    public void setForIsPlanRetenu(boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    public void setForIsSupprime(boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    public void setInEtatDroitCourant(Collection<String> inEtatDroitCourant) {
        this.inEtatDroitCourant = inEtatDroitCourant;
    }

    public void setInEtatDroitPrecedant(Collection<String> inEtatDroitPrecedant) {
        this.inEtatDroitPrecedant = inEtatDroitPrecedant;
    }

    public void setInEtatPca(Collection<String> inEtatPca) {
        this.inEtatPca = inEtatPca;
    }

    public void setInEtatPcaPrecedante(Collection<String> inEtatPcaPrecedante) {
        this.inEtatPcaPrecedante = inEtatPcaPrecedante;
    }

    public void setInTypeDecision(Collection<String> inTypeDecision) {
        this.inTypeDecision = inTypeDecision;
    }

    @Override
    public Class<MutationMontantPCAFileds> whichModelClass() {
        return MutationMontantPCAFileds.class;
    }
}
