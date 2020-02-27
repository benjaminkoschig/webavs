package globaz.aquila.print.list.elp;

import aquila.ch.eschkg.RcType;
import globaz.aquila.process.elp.COAbstractELP;

import java.util.Optional;

public class COResultRcELP extends COAbstractELP {

    RcType rcType;

    public COResultRcELP(RcType rcType) {
        this.rcType = rcType;
    }

    @Override
    public COTypeMessageELP getTypemessage() {
        return COTypeMessageELP.RC;
    }

    @Override
    public String getRemarque() {
        return Optional.ofNullable(rcType.getStatusInfo().getDetails()).orElse("");
    }

    public String getNoAbd() {
        return Optional.ofNullable(rcType.getOutcome().getRealised())
                .map(realised -> realised.getLoss())
                .map(loss -> loss.getLossNumber())
                .orElse("");
    }

    public String getDateEtablissement() {
        return getDate(Optional.ofNullable(rcType.getOutcome().getRealised())
                .map(realised -> realised.getLoss())
                .map(loss -> loss.getDate()).orElse(null));
    }

}
