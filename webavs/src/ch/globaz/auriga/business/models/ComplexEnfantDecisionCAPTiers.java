package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class ComplexEnfantDecisionCAPTiers extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleEnfantDecisionCAP enfantDecisionCAP;
    private PersonneSimpleModel personne;
    private TiersSimpleModel tiers;

    public ComplexEnfantDecisionCAPTiers() {
        super();
        enfantDecisionCAP = new SimpleEnfantDecisionCAP();
        personne = new PersonneSimpleModel();
        tiers = new TiersSimpleModel();
    }

    public SimpleEnfantDecisionCAP getEnfantDecisionCAP() {
        return enfantDecisionCAP;
    }

    @Override
    public String getId() {
        return enfantDecisionCAP.getIdEnfantDecision();
    }

    public PersonneSimpleModel getPersonne() {
        return personne;
    }

    @Override
    public String getSpy() {
        return enfantDecisionCAP.getSpy();
    }

    public TiersSimpleModel getTiers() {
        return tiers;
    }

    public void setEnfantDecisionCap(SimpleEnfantDecisionCAP enfantDecisionCap) {
        enfantDecisionCAP = enfantDecisionCap;
    }

    @Override
    public void setId(String id) {
        enfantDecisionCAP.setIdEnfantDecision(id);
    }

    public void setPersonne(PersonneSimpleModel personne) {
        this.personne = personne;
    }

    @Override
    public void setSpy(String spy) {
        enfantDecisionCAP.setSpy(spy);
    }

    public void setTiers(TiersSimpleModel tiers) {
        this.tiers = tiers;
    }

}
