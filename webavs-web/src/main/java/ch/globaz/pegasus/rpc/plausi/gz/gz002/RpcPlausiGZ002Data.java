package ch.globaz.pegasus.rpc.plausi.gz.gz002;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCaissesCompensation;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiGZ002Data extends RpcPlausiHeader {
    String idPca;
    List<PersonDataWrapper> personsElement;

    public RpcPlausiGZ002Data(RpcPlausi<RpcPlausiGZ002Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        boolean isOk = true;
        for (PersonDataWrapper person : personsElement) {
            if (RpcPlausiCaissesCompensation.parseCaisse(person.E1, person.E27) == null) {
                isOk = false;
            }
        }
        return isOk;
    }

    public void loadList(List<PersonElementsCalcul> personsElements) {
        personsElement = new ArrayList<PersonDataWrapper>();
        PersonElementsCalcul dataRequerant = null;
        for (PersonElementsCalcul person : personsElements) {
            if (person.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.REQUERANT) {
                dataRequerant = person;
            }
        }

        for (PersonElementsCalcul person : personsElements) {
            PersonDataWrapper personWrapper = new PersonDataWrapper();
            personWrapper = loadOfficeAndAgency(personWrapper, person, dataRequerant);
            personWrapper.familyRole = person.getMembreFamille().getRoleMembreFamille().toString();
            personWrapper.NSS = person.getMembreFamille().getPersonne().getNss().toString();
            personWrapper.hasPension = !person.isRenteIsSansRente();
            personWrapper.renteAnnuelle = person.getRenteAvsAi();
            personsElement.add(personWrapper);
        }
    }

    private PersonDataWrapper loadOfficeAndAgency(PersonDataWrapper personWrapper, PersonElementsCalcul person,
            PersonElementsCalcul requerant) {
        if (person.getNumeroOffice() == null) {
            if (person.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.REQUERANT) {
                personWrapper.E1 = 0;
            } else if (person.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.ENFANT) {
                if (requerant.getNumeroOffice() == null) {
                    personWrapper.E1 = 0;
                } else {
                    personWrapper.E1 = requerant.getNumeroOffice();
                    if (requerant.getNumeroAgence() != null) {
                        personWrapper.E27 = requerant.getNumeroAgence();
                    } else {
                        personWrapper.E27 = 0;
                    }
                }
            } else {
                personWrapper.E1 = 0;
                personWrapper.E27 = 0;
            }
        } else {
            personWrapper.E1 = person.getNumeroOffice();
            if (person.getNumeroAgence() == null) {
                personWrapper.E27 = 0;
            } else {
                personWrapper.E27 = person.getNumeroAgence();
            }
        }
        return personWrapper;
    }

    class PersonDataWrapper {
        String familyRole;
        String NSS;
        Integer E1;
        Integer E27;
        Boolean hasPension;
        Montant renteAnnuelle;
    }

}
