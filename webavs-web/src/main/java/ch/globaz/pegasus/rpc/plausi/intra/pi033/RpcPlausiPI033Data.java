package ch.globaz.pegasus.rpc.plausi.intra.pi033;

import java.util.List;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.rpc.domaine.RpcVitalNeedsCategory;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

public class RpcPlausiPI033Data extends RpcPlausiCommonCalculData {

    List<AnnoncePerson> persons;

    public RpcPlausiPI033Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        for (AnnoncePerson person : persons) {
            if ((RpcVitalNeedsCategory.COUPLE.equals(person.getVitalNeedsCategory())
                    || RpcVitalNeedsCategory.CHILD.equals(person.getVitalNeedsCategory()))
                    && PcaGenre.HOME.equals(person.getHousingMode())) {
                return false;
            }
        }
        return true;
    }

}
