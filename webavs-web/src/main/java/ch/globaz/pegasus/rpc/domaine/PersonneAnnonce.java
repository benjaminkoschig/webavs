package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.pyxis.domaine.PersonneAVS;

public class PersonneAnnonce implements PersonneAnnonceRpc {
    private AnnonceRpc annonce = new Annonce();
    private PersonneAVS personneAVS = new PersonneAVS();

    public PersonneAnnonce() {
    }

    public AnnonceRpc getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceRpc annonce) {
        this.annonce = annonce;
    }

    public void setPersonneAVS(PersonneAVS personneAVS) {
        this.personneAVS = personneAVS;
    }

    @Override
    public AnnonceRpc getAnnonceRpc() {
        return annonce;
    }

    @Override
    public PersonneAVS getPersonne() {
        return personneAVS;
    }

    @Override
    public String getId() {
        return annonce.getId();
    }

    @Override
    public void setId(String id) {
        annonce.setId(id);
    }

    @Override
    public String getSpy() {
        return annonce.getSpy();
    }

    @Override
    public void setSpy(String spy) {
        annonce.setSpy(spy);
    }

}
