package ch.globaz.pegasus.rpc.plausi.intra.pi082;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

public class RpcPlausiPI082Data extends RpcPlausiHeader {
    XMLGregorianCalendar dateArrivee;
    BigInteger decisionCause;

    public RpcPlausiPI082Data(RpcPlausi<RpcPlausiPI082Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {

        // TODO A modifier pour PS-012
        // Si le code decisionCause < 2 et dateArrivee renseigné
        if (dateArrivee != null && (decisionCause.compareTo(BigInteger.ONE) != 1)) {
            return false;
        }
        return true;
    }
}
