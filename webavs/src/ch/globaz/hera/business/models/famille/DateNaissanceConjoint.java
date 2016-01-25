package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeComplexModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class DateNaissanceConjoint extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "DateNaissanceConjoint [idSimpleMembreFamille=" + simpleMembreFamille.getIdMembreFamille() + ", NSS="
                + personneEtendue.getPersonneEtendue().getNumAvsActuel() + "]";
    }

    private SimpleMembreFamille simpleMembreFamille = null;
    private PersonneEtendueComplexModel personneEtendue = null;

    public DateNaissanceConjoint() {
        super();
        simpleMembreFamille = new SimpleMembreFamille();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    @Override
    public String getId() {
        return simpleMembreFamille.getId();
    }

    @Override
    public String getSpy() {
        throw new NotImplementedException();
    }

    @Override
    public void setId(String id) {
        simpleMembreFamille.setId(id);

    }

    public SimpleMembreFamille getSimpleMembreFamille() {
        return simpleMembreFamille;
    }

    public void setSimpleMembreFamille(SimpleMembreFamille simpleMembreFamille) {
        this.simpleMembreFamille = simpleMembreFamille;
    }

    @Override
    public void setSpy(String spy) {
        throw new NotImplementedException();

    }

}
