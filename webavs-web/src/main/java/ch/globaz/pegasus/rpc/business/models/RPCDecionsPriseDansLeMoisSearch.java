package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;

public class RPCDecionsPriseDansLeMoisSearch extends JadeSearchComplexModel {
    private String forDateFinMoisFutur;
    private String forDateDecisionMin;
    private String forDateDecisionMax;
    private String forDateDecisionMinMoins1;
    private String forDateDecisionMaxMoins1;
    private String forDateDecisionMoisAnneMoins1;
    private String forDebutDecision;
    private String forCsEtatPca;
    private String forCsEtatDemande;
    private String forCsEtatDroit;
    private String forDateFinPca;
    private String forIdDemande;
    private String forIdVersionDroitSup;
    private Collection<String> forCsMotifNotIn = new ArrayList<String>();
    private Collection<String> forCsEtatDemandeMoisFutur = new ArrayList<String>();
    private Collection<String> forIdsVersionDroit = new ArrayList<String>();
    private Collection<String> forIdsVersionDroitNotIn = new ArrayList<String>();
    private Collection<String> forIdsDecsion = new ArrayList<String>();
    private Collection<String> inCsType = new ArrayList<String>();
    private Collection<String> forNss = new ArrayList<String>();

    public Collection<String> getForCsMotifNotIn() {
        return forCsMotifNotIn;
    }

    public void setForCsMotifNotIn(Collection<String> forCsMotifNotIn) {
        this.forCsMotifNotIn = forCsMotifNotIn;
    }

    public String getForDateFinMoisFutur() {
        return forDateFinMoisFutur;
    }

    public void setForDateFinMoisFutur(String forDateFinMoisFutur) {
        this.forDateFinMoisFutur = forDateFinMoisFutur;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
    }

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public String getForDateDecisionMinMoins1() {
        return forDateDecisionMinMoins1;
    }

    public void setForDateDecisionMinMoins1(String forDateDecisionMinMoins1) {
        this.forDateDecisionMinMoins1 = forDateDecisionMinMoins1;
    }

    public String getForDateDecisionMaxMoins1() {
        return forDateDecisionMaxMoins1;
    }

    public void setForDateDecisionMaxMoins1(String forDateDecisionMaxMoins1) {
        this.forDateDecisionMaxMoins1 = forDateDecisionMaxMoins1;
    }

    public String getForDateDecisionMoisAnneMoins1() {
        return forDateDecisionMoisAnneMoins1;
    }

    public void setForDateDecisionMoisAnneMoins1(String forDateDecisionMoisAnneMoins1) {
        this.forDateDecisionMoisAnneMoins1 = forDateDecisionMoisAnneMoins1;
    }

    public Collection<String> getForIdsDecsion() {
        return forIdsDecsion;
    }

    public void setForIdsDecsion(Collection<String> forIdsDecsion) {
        this.forIdsDecsion = forIdsDecsion;
    }

    public void setForIdDecsion(String forIdDecsion) {
        forIdsDecsion.add(forIdDecsion);
    }

    public Collection<String> getInCsType() {
        return inCsType;
    }

    public void setInCsType(Collection<String> inCsType) {
        this.inCsType = inCsType;
    }

    public String getForCsEtatPca() {
        return forCsEtatPca;
    }

    public void setForCsEtatPca(String forCsEtatPca) {
        this.forCsEtatPca = forCsEtatPca;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public String getForCsEtatDroit() {
        return forCsEtatDroit;
    }

    public void setForCsEtatDroit(String forCsEtatDroit) {
        this.forCsEtatDroit = forCsEtatDroit;
    }

    public String getForDateFinPca() {
        return forDateFinPca;
    }

    public void setForDateFinPca(String forDateFinPca) {
        this.forDateFinPca = forDateFinPca;
    }

    public Collection<String> getForIdsVersionDroit() {
        return forIdsVersionDroit;
    }

    public void setForIdsVersionDroit(Collection<String> forIdsVersionDroit) {
        this.forIdsVersionDroit = forIdsVersionDroit;
    }

    public Collection<String> getForIdsVersionDroitNotIn() {
        return forIdsVersionDroitNotIn;
    }

    public void setForIdsVersionDroitNotIn(Collection<String> forIdsVersionDroitNotIn) {
        this.forIdsVersionDroitNotIn = forIdsVersionDroitNotIn;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public Collection<String> getForCsEtatDemandeMoisFutur() {
        return forCsEtatDemandeMoisFutur;
    }

    public void setForCsEtatDemandeMoisFutur(Collection<String> forCsEtatDemandeMoisFutur) {
        this.forCsEtatDemandeMoisFutur = forCsEtatDemandeMoisFutur;
    }

    public String getForDebutDecision() {
        return forDebutDecision;
    }

    public void setForDebutDecision(String forDebutDecision) {
        this.forDebutDecision = forDebutDecision;
    }

    @Override
    public Class<RPCDecionsPriseDansLeMois> whichModelClass() {
        return RPCDecionsPriseDansLeMois.class;
    }

    public Collection<String> getForNss() {
        return forNss;
    }

    public void setForNss(Collection<String> forNss) {
        this.forNss = forNss;
    }

    public String getForIdVersionDroitSup() {
        return forIdVersionDroitSup;
    }

    public void setForIdVersionDroitSup(String forIdVersionDroitSup) {
        this.forIdVersionDroitSup = forIdVersionDroitSup;
    }

}
