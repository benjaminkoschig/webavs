package globaz.aquila.print.list.elp;

import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.ScType.Outcome.Summon;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Optional;

public class COResultScELP extends COAbstractResultELP {

    ScType scType;

    public COResultScELP(ScType scType) {
        this.scType = scType;
    }

    @Override
    public COTypeMessageELP getTypemessage() {
        return COTypeMessageELP.SC;
    }

    @Override
    public String getRemarque() {
        return Optional.ofNullable(scType.getStatusInfo().getDetails()).orElse("");
    }

    public XMLGregorianCalendar getDateNotification() {
        return Optional.ofNullable(scType.getOutcome().getSummon())
                .map(summon -> summon.getDelivery())
                .map(delivery -> delivery.getDeliveryDate())
                .orElse(null);
    }

    public String getNoPoursuite() {
        return scType.getCaseNumber();
    }

    public XMLGregorianCalendar getOpposition() {
        Optional<Summon> op = Optional.ofNullable(scType.getOutcome().getSummon());
        return op.map(sum -> sum.getObjection().getFull()).map(full -> full.getObjectionDate()) // date du Full
                .orElse(op.map(sum -> sum.getObjection().getPartial()).map(partial -> partial.getObjectionDate()) // ou date du Partial
                        .orElse(null));
    }

}
