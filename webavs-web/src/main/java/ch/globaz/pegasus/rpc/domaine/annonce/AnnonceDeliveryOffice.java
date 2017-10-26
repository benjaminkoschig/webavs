package ch.globaz.pegasus.rpc.domaine.annonce;

import java.math.BigInteger;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class AnnonceDeliveryOffice {

    protected BigInteger elOffice;
    protected Integer elAgency;

    public AnnonceDeliveryOffice(InfoCaisse infoCaisse) {
        elOffice = BigInteger.valueOf(infoCaisse.getNumeroCaisse());
        if (infoCaisse.hasAgency()) {
            elAgency = infoCaisse.getNumeroAgence();
        }
    }

    public BigInteger getElOffice() {
        return elOffice;
    }

    public Integer getElAgency() {
        return elAgency;
    }

}
