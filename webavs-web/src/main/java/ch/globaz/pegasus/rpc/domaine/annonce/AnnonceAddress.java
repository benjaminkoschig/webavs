package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;

public class AnnonceAddress {

    protected PersonElementsCalcul personData;
    protected RpcAddress address;
    protected String canton;
    protected Integer municipality;

    public AnnonceAddress(PersonElementsCalcul personData, RpcAddress address) {
        this.personData = personData;
        this.address = address;
        canton = address.getCanton().getAbreviation();
        municipality = Integer.valueOf(address.getNumOFS());
    }

    public String getCanton() {
        return canton;
    }

    public Integer getMunicipality() {
        return municipality;
    }

    public RpcAddress getAddress() {
        return address;
    }

}
