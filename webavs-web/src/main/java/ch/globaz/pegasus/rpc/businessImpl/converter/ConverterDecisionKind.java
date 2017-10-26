package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;

public class ConverterDecisionKind {

    private static Set<MotifDecision> motif5 = new HashSet<MotifDecision>();
    private static Set<MotifDecision> motif1 = new HashSet<MotifDecision>();

    static {
        motif5.add(MotifDecision.RENCONTRE_IMPOSSIBLE_INITIAL);
        motif5.add(MotifDecision.PARTIS_SANS_ADRESSE_INITIAL);
        motif5.add(MotifDecision.JUSTIFICATIFS_DEMANDES_INITIAL);

        motif1.add(MotifDecision.AUCUN_DROIT_DOMICILLE);
        motif1.add(MotifDecision.AUCUN_DROIT_EMS);
        motif1.add(MotifDecision.DELAI_CARENCE);
        motif1.add(MotifDecision.DOMICILLE_PAR_RECONNU);
        motif1.add(MotifDecision.DROIT_INDEMNITE_AI);
        motif1.add(MotifDecision.ENFANT_PARENT_RENTIER);
        motif1.add(MotifDecision.DROIT_ENTRETIEN);
    }

    public static BigInteger convert(TypeDecision typeDecision, MotifDecision motifDecision) {

        if (typeDecision.isRefusSansCalcul()) {
            if (MotifDecision.RENONCIATION.equals(motifDecision)) {
                return toBigInt(4);
            } else if (motif5.contains(motifDecision)) {
                return toBigInt(5);
            } else if (motif1.contains(motifDecision)) {
                return toBigInt(1);
            }
        } else {
            if (typeDecision.isOctroiOrPartiel() || typeDecision.isAdaptation()) {
                return toBigInt(6);
            } else if (typeDecision.isRefusApresCalcul()) {
                return toBigInt(2);
            } else if (typeDecision.isSuppression()) {
                return toBigInt(3);
            }
        }

        throw new RpcBusinessException("pegasus.rpc.converter.decisionKind.introuvable", typeDecision, motifDecision);
    }

    private static BigInteger toBigInt(int nb) {
        return BigInteger.valueOf(nb);
    }

}
