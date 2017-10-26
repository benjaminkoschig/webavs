package ch.globaz.pegasus.rpc.plausi.gz.gz001;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiGZ001Data extends RpcPlausiHeader {

    String idPca;
    List<PersonDataWrapper> personsElement;
    Date dateDecision;
    Date FC6;

    public RpcPlausiGZ001Data(RpcPlausi<RpcPlausiGZ001Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        Boolean isOk = true;
        for (PersonDataWrapper element : personsElement) {
            Date tmpDeces = new Date("01/" + element.dateDeces.getMois() + "/" + element.dateDeces.getYear());

            // Compares date of death with "validTo" date (FC6)
            // if FC6 isn't set compares with Decision date
            if (FC6 != null) {
                if (element.dateDeces.before(FC6)) {
                    Date tmpFC6 = new Date("01/" + FC6.getMois() + "/" + FC6.getYear());
                    isOk = tmpDeces.after(tmpFC6);
                }
            } else {
                if (element.dateDeces.before(dateDecision)) {
                    Date tmpDecision = new Date("01/" + dateDecision.getMois() + "/" + dateDecision.getYear());
                    isOk = tmpDeces.after(tmpDecision);
                }
            }

        }
        return isOk;
    }

    public void loadDatas(List<PersonElementsCalcul> personsElements) {
        personsElement = new ArrayList<PersonDataWrapper>();
        for (PersonElementsCalcul person : personsElements) {
            if (person.hasPension() && person.getMembreFamille().getPersonne().getDateDeces() != null
                    && !person.getMembreFamille().getPersonne().getDateDeces().isEmpty()) {
                PersonDataWrapper personWrapper = new PersonDataWrapper();

                // TODO: FW Date parser must accept YYYY0000, some caisses have partial dates for death date
                if (person.getMembreFamille().getPersonne().getDateDeces().endsWith("0000")) {
                    personWrapper.dateDeces = new Date(person.getMembreFamille().getPersonne().getDateDeces()
                            .substring(0, 3)
                            + "0101");
                } else {
                    personWrapper.dateDeces = new Date(person.getMembreFamille().getPersonne().getDateDeces());
                }
                personWrapper.hasPension = true;
            }
        }
    }

    class PersonDataWrapper {
        Date dateDeces;
        Boolean hasPension;
    }
}
