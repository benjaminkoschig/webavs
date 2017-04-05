package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class FamillePersonneEtendue extends JadeComplexModel {
    private SimpleFamille simpleFamille = null;
    private SimpleContribuable simpleContribuable = null;
    private PersonneEtendueComplexModel personneEtendue = null;

    public FamillePersonneEtendue() {
        super();
        simpleFamille = new SimpleFamille();
        simpleContribuable = new SimpleContribuable();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    public SimpleFamille getSimpleFamille() {
        return simpleFamille;
    }

    public void setSimpleFamille(SimpleFamille simpleFamille) {
        this.simpleFamille = simpleFamille;
    }

    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    @Override
    public String getId() {
        return simpleFamille.getId();
    }

    @Override
    public String getSpy() {
        return simpleFamille.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleFamille.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simpleFamille.setSpy(spy);
    }

    public SimpleContribuable getSimpleContribuable() {
        return simpleContribuable;
    }

    public void setSimpleContribuable(SimpleContribuable simpleContribuable) {
        this.simpleContribuable = simpleContribuable;
    }

}
