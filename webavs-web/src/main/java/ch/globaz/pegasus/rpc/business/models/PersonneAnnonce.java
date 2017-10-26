package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PersonneAnnonce extends JadeComplexModel {
    private SimpleAnnonce simpleAnnonce = new SimpleAnnonce();
    private PersonneEtendueComplexModel personne = new PersonneEtendueComplexModel();
    private SimpleLotAnnonce simpleLotAnnonce = new SimpleLotAnnonce();

    public SimpleLotAnnonce getSimpleLotAnnonce() {
        return simpleLotAnnonce;
    }

    public void setSimpleLotAnnonce(SimpleLotAnnonce simpleLotAnnonce) {
        this.simpleLotAnnonce = simpleLotAnnonce;
    }

    public SimpleAnnonce getSimpleAnnonce() {
        return simpleAnnonce;
    }

    public void setSimpleAnnonce(SimpleAnnonce simpleAnnonce) {
        this.simpleAnnonce = simpleAnnonce;
    }

    public PersonneEtendueComplexModel getPersonne() {
        return personne;
    }

    public void setPersonne(PersonneEtendueComplexModel personne) {
        this.personne = personne;
    }

    @Override
    public String getId() {
        return simpleAnnonce.getId();
    }

    @Override
    public String getSpy() {
        return simpleAnnonce.getSpy();
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setSpy(String spy) {

    }

}
