package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType;
import ch.eahv_iv.xmlns.eahv_iv_2469_common._1.DeliveryOfficeType;
import ch.eahv_iv.xmlns.eahv_iv_2469_common._1.ObjectFactory;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.rpc.domaine.RpcData;

public class Converter2469_301 implements Converter<RpcData, ContentType> {
    private static final Logger LOG = LoggerFactory.getLogger(Converter2469_301.class);
    private InfoCaisse infoCaisse;

    /**
     * @param infoCaisse
     */
    public Converter2469_301(InfoCaisse infoCaisse) {
        this.infoCaisse = infoCaisse;
    }

    @Override
    public ContentType convert(RpcData businessData) {
        PcaDecision pcaDecision = businessData.resolveDecisionsRequerant().list().get(0);
        Decision decision = pcaDecision.getDecision();
        ObjectFactory factory = new ObjectFactory();
        ContentType masterData = new ContentType();
        masterData.setDecisionId(decision.getId());
        DeliveryOfficeType dot = factory.createDeliveryOfficeType();
        dot.setElOffice(BigInteger.valueOf(infoCaisse.getNumeroCaisse()));
        if (infoCaisse.hasAgency()) {
            dot.setElAgency(infoCaisse.getNumeroAgence());
        }
        masterData.setDeliveryOffice(dot);
        masterData.setBusinessCaseIdRPC(businessData.getDossier().getId());
        // actionKind de type 1 ne sont pas encore géré
        masterData.setActionKind(new BigInteger("0"));
        return masterData;
    }

}
