package ch.globaz.pegasus.business.models.mutation;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Collection;

public class MutationPcaSearch extends JadeAbstractSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FOR_OLD_CURENT_PCA = "forOldCurentVersioned";
    public final static String FOR_PCA_VALIDEE = "forPCAValidee";

    private String forDateDebutPca = null;
    private String forDateDecisionMax;
    private String forDateDecisionMin;
    private boolean forIsPlanRetenu = false;
    private boolean forIsSupprime = false;

    private Collection<String> inEtatDroitCourant = null;
    private Collection<String> inEtatDroitPrecedant = null;
    private Collection<String> inEtatPca = null;
    private Collection<String> inEtatPcaPrecedante = null;
    private Collection<String> inIdDroit = null;
    private Collection<String> inTypeDecision = null;

    public String getForDateDebutPca() {
        return forDateDebutPca;
    }

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
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

    public Collection<String> getInIdDroit() {
        return inIdDroit;
    }

    public Collection<String> getInTypeDecision() {
        return inTypeDecision;
    }

    public void setForDateDebutPca(String forDateDebutPca) {
        this.forDateDebutPca = forDateDebutPca;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
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

    public void setInIdDroit(Collection<String> inIdDroit) {
        this.inIdDroit = inIdDroit;
    }

    public void setInTypeDecision(Collection<String> inTypeDecision) {
        this.inTypeDecision = inTypeDecision;
    }

    @Override
    public Class<MutationPca> whichModelClass() {
        return MutationPca.class;
    }

}
