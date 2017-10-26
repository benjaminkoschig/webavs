package ch.globaz.pegasus.rpc.plausi.intra.pi013;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI013Data extends RpcPlausiHeader {
    String idPca;
    List<PersonDataWrapper> personsElement;

    public RpcPlausiPI013Data(RpcPlausi<RpcPlausiPI013Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        for (PersonDataWrapper person : personsElement) {
            if (Collections.frequency(personsElement, person.nss) > 1) {
                return false;
            }
        }
        return true;
    }

    public void loadDatas(List<PersonElementsCalcul> personsElements) {
        personsElement = new ArrayList<PersonDataWrapper>();
        for (PersonElementsCalcul person : personsElements) {
            PersonDataWrapper personWrapper = new PersonDataWrapper();
            personWrapper.nss = person.getMembreFamille().getPersonne().getNss().toString();
        }
    }

    class PersonDataWrapper {
        String nss;
    }

}
