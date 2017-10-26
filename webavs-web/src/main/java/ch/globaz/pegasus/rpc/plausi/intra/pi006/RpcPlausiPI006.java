package ch.globaz.pegasus.rpc.plausi.intra.pi006;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.pegasus.rpc.domaine.MembresFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcPcaDecisionCalculElementCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI006 implements RpcPlausi<RpcPlausiPI006Data> {
    /**
     * PI006 test integrity between the tyo adress in xml
     * 
     * @deprecated Since P11 (livingAdress) is no more mapped into WebAVS
     */
    @Deprecated
    public RpcPlausiPI006Data buildPlausi(RpcPcaDecisionCalculElementCalcul datas,
            MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere, PcaSituation situation) {
        final RpcPlausiPI006Data dataPlausi = new RpcPlausiPI006Data(this);
        dataPlausi.idPca = datas.getPcaDecision().getPca().getId();
        dataPlausi.personsData = new ArrayList<RpcPlausiPI006AddressContainer>();
        // Load address
        for (PersonElementsCalcul personData : datas.getPersonsElementsCalcul().getPersonsElementsCalcul()) {
            if (personData.isValidLegalAddress() && personData.isValidLivingAddress()) {
                RpcPlausiPI006AddressContainer address = new RpcPlausiPI006AddressContainer();
                address.P11 = Integer.valueOf(personData.getLivingAddress().getNumOFS());
                address.P6 = Integer.valueOf(personData.getLegalAddress().getNumOFS());
                // TODO: get domicile for current person
                address.P12 = datas.getPcaDecision().getPca().getGenre().isDomicile();
                dataPlausi.personsData.add(address);
            }

        }
        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-006";
    }

    @Override
    public String getReferance() {
        return "413";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.INFO;
    }

    @Override
    public List<RpcPlausiApplyToDecision> getApplyTo() {
        return new ArrayList<RpcPlausiApplyToDecision>() {
            {
                add(RpcPlausiApplyToDecision.POSITIVE);
                add(RpcPlausiApplyToDecision.REJECT_FULL);
            }
        };
    }

}
