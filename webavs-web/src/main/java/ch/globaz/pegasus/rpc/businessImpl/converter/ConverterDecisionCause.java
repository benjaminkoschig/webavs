package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.math.BigInteger;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;

public class ConverterDecisionCause {
    public static BigInteger convert(VersionDroit versionDroit, MotifDecision motif) {
        if (versionDroit.getNumero() == 1) {
            return toBigInt(1);
        } else if (versionDroit.getMotif() != null) {
            switch (versionDroit.getMotif()) {
                case ARRIVEE_ETRANGER:
                case NOUVEAU_DROIT:
                case TRANSFERT_HORS_CANTON:
                    return toBigInt(1);

                case CORRECTION_REPRISE:
                case DIVORCE:
                case MARRIAGE:
                case MODIFICATIONS_DIVERSES:
                case MODIFICATION_RETOUR_REGISTRE:
                case REPRISE:
                    if(MotifDecision.DECES.equals(motif)) {
                        return toBigInt(4);
                    } else {
                        return toBigInt(2);
                    }
                case ADAPTATION:
                case ADAPTATION_HOME:
                    return toBigInt(3);
                case DECES:
                case VEUVAGE:
                    return toBigInt(4);

                case REVISION_QUADRIENNALE:
                    return toBigInt(5);

                case REPRISE_ADAPTATION_ERRONE:
                default:
                    return toBigInt(6);
            }
        }
        throw new RpcBusinessException("pegasus.rpc.converter.decisionCause.introuvable", versionDroit);
    }
    
    private static BigInteger toBigInt(int nb) {
        return BigInteger.valueOf(nb);
    }

}
