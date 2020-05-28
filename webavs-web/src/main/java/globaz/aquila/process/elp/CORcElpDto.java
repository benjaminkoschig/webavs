package globaz.aquila.process.elp;

import aquila.ch.eschkg.LossType;
import aquila.ch.eschkg.RcType;
import globaz.aquila.print.list.elp.COTypeMessageELP;

import java.util.Objects;
import java.util.Optional;

public class CORcElpDto extends COAbstractELP {

    private RcType rcType;
    private String numAbd;
    private String dateEtablissement;
    private String interest;


    public CORcElpDto(RcType rcType) {
        this.rcType = rcType;
        initialiseParametre();
    }

    private void initialiseParametre() {
        RcType.Outcome.Realised realised = rcType.getOutcome().getRealised();
        if (Objects.nonNull(realised)) {
            LossType loss = realised.getLoss();
            if (Objects.nonNull(loss)) {
                numAbd = loss.getLossNumber();
                dateEtablissement = getDate(loss.getDate());
                if (Objects.nonNull(loss.getInterest())) {
                    interest = loss.getInterest().toString();
                }
            }
        }
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
        return numAbd;
    }

    public String getDateEtablissement() {
        return dateEtablissement;
    }

    public String getInterest() {
        return interest;
    }

}
