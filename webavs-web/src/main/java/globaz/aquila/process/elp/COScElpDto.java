package globaz.aquila.process.elp;

import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.ScType.Outcome.Summon;
import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COTypeMessageELP;

import java.util.Optional;

public class COScElpDto extends COAbstractELP {

    private static final String ID_ETAPE_POURSUITE_CLASSEE = "24";
    private static final String ID_ETAPE_POURSUITE_RADIEE = "25";

    private ScType scType;

    public COScElpDto(ScType scType) {
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

    public String getDateNotification() {
        return getDate(Optional.ofNullable(scType.getOutcome().getSummon())
                .map(summon -> summon.getDelivery())
                .map(delivery -> delivery.getDeliveryDate())
                .orElse(null));
    }

    public String getNoPoursuite() {
        return scType.getCaseNumber();
    }

    public String getOpposition() {
        Optional<Summon> op = Optional.ofNullable(scType.getOutcome().getSummon());
        return getDate(op.map(sum -> sum.getObjection().getFull()).map(full -> full.getObjectionDate()) // date du Full
                .orElse(op.map(sum -> sum.getObjection().getPartial()).map(partial -> partial.getObjectionDate()) // ou date du Partial
                        .orElse(null)));
    }

    /**
     * Permet de renseigner le bon message du motif lorsque la transition n'existe pas : le contentieux est dans une mauvaise étape.
     *
     * @param idEtape : id de l'étape actuel du contentieux.
     */
    public void setMotifWrongStep(String idEtape) {
        switch (idEtape) {
            case ID_ETAPE_POURSUITE_CLASSEE:
                this.setMotif(COMotifMessageELP.POURSUITE_CLASSE);
                break;
            case ID_ETAPE_POURSUITE_RADIEE:
                this.setMotif(COMotifMessageELP.POURSUITE_RADIE);
                break;
            default:
                this.setMotif(COMotifMessageELP.CDP_NON_TRAITE);
                break;
        }
    }
}
