package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author MMO
 * @date 18.04.2013
 */
public class ComplexEnfantDecisionCapWidget extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CompositionTiersSimpleModel compositionTiers = null;
    private PersonneSimpleModel personne = null;
    private PersonneEtendueSimpleModel personneEtendue = null;
    private TiersSimpleModel tiers = null;

    public ComplexEnfantDecisionCapWidget() {
        super();
        tiers = new TiersSimpleModel();
        personne = new PersonneSimpleModel();
        personneEtendue = new PersonneEtendueSimpleModel();
        compositionTiers = new CompositionTiersSimpleModel();
    }

    public CompositionTiersSimpleModel getCompositionTiers() {
        return compositionTiers;
    }

    @Override
    public String getId() {
        return personneEtendue.getId();
    }

    public PersonneSimpleModel getPersonne() {
        return personne;
    }

    public PersonneEtendueSimpleModel getPersonneEtendue() {
        return personneEtendue;
    }

    @Override
    public String getSpy() {
        return personneEtendue.getSpy();
    }

    public TiersSimpleModel getTiers() {
        return tiers;
    }

    public void setCompositionTiers(CompositionTiersSimpleModel compositionTiers) {
        this.compositionTiers = compositionTiers;
    }

    @Override
    public void setId(String id) {
        personneEtendue.setId(id);

    }

    public void setPersonne(PersonneSimpleModel personne) {
        this.personne = personne;
    }

    public void setPersonneEtendue(PersonneEtendueSimpleModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    @Override
    public void setSpy(String spy) {
        personneEtendue.setSpy(spy);

    }

    public void setTiers(TiersSimpleModel tiers) {
        this.tiers = tiers;
    }

}
