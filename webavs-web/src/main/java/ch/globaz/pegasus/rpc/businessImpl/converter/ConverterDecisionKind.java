package ch.globaz.pegasus.rpc.businessImpl.converter;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import globaz.jade.client.util.JadeDateUtil;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class ConverterDecisionKind {

    private static Set<MotifDecision> motif5 = new HashSet<MotifDecision>();
    private static Set<MotifDecision> motif1 = new HashSet<MotifDecision>();
    private static Set<MotifDecision> motif7 = new HashSet<>();

    static {
        motif5.add(MotifDecision.RENCONTRE_IMPOSSIBLE_INITIAL);
        motif5.add(MotifDecision.PARTIS_SANS_ADRESSE_INITIAL);
        motif5.add(MotifDecision.JUSTIFICATIFS_DEMANDES_INITIAL);

        motif1.add(MotifDecision.AUCUN_DROIT_DOMICILLE);
        motif1.add(MotifDecision.AUCUN_DROIT_EMS);
        motif1.add(MotifDecision.AUCUN_DROIT_EMS2);
        motif1.add(MotifDecision.DELAI_CARENCE);
        motif1.add(MotifDecision.DOMICILLE_PAR_RECONNU);
        motif1.add(MotifDecision.DROIT_INDEMNITE_AI);
        motif1.add(MotifDecision.ENFANT_PARENT_RENTIER);
        motif1.add(MotifDecision.DROIT_ENTRETIEN);

        motif7.add(MotifDecision.SEUIL_FORTUNE_DEPASSE);
    }

    public static BigInteger convert(TypeDecision typeDecision, MotifDecision motifDecision,
            PcaEtatCalcul etatCalculFederal) {

        if (typeDecision.isRefusSansCalcul()) {
            if (MotifDecision.RENONCIATION.equals(motifDecision)) {
                return toBigInt(4);
            } else if (motif5.contains(motifDecision)) {
                return toBigInt(5);
            } else if (motif1.contains(motifDecision)) {
                return toBigInt(1);
            } else if (motif7.contains(motifDecision)) {
                return toBigInt(7);
            }
        } else {
            if ((etatCalculFederal != null && etatCalculFederal.isRefus()) || typeDecision.isRefusApresCalcul()) {
                return toBigInt(2);
            } else if (typeDecision.isOctroiOrPartiel() || typeDecision.isAdaptation()) {
                return toBigInt(6);
            } else if (typeDecision.isSuppression()) {
                return toBigInt(3);
            }
        }

        throw new RpcBusinessException("pegasus.rpc.converter.decisionKind.introuvable", typeDecision, motifDecision);
    }

    private static BigInteger toBigInt(int nb) {
        return BigInteger.valueOf(nb);
    }

    public static boolean isMotifReform(Decision decision) {
        String dateReforme = null;
        try {
            dateReforme = EPCProperties.DATE_REFORME_PC.getValue();
        } catch (PropertiesException e) {
            throw new RpcBusinessException("Unbale to obtain properties for reforme pc", e);
        }

        return motif7.contains(decision.getMotif()) || !JadeDateUtil.isDateBefore(decision.getDateDecision().getSwissValue(), dateReforme);
    }

}
