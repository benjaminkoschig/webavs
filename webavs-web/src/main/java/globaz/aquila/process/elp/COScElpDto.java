package globaz.aquila.process.elp;

import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.ScType.Outcome.Summon;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COTypeMessageELP;
import globaz.aquila.process.exception.ElpProcessException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class COScElpDto extends COAbstractELP {

    private ScType scType;
    private String idEtapePoursuiteClassee;
    private String idEtapePoursuiteRadiee;

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
    public void setMotifWrongStep(BSession session, String idSequence, String idEtape) throws ElpProcessException {
        if (Objects.isNull(idEtapePoursuiteClassee)) {
            idEtapePoursuiteClassee = getIdEtape(session, idSequence, ICOEtape.CS_POURSUITE_CLASSEE);
        }
        if (Objects.isNull(idEtapePoursuiteRadiee)) {
            idEtapePoursuiteRadiee = getIdEtape(session, idSequence, ICOEtape.CS_POURSUITE_RADIEE);
        }

        if (StringUtils.equals(idEtapePoursuiteClassee, idEtape)) {
            this.setMotif(COMotifMessageELP.POURSUITE_CLASSE);
        } else if (StringUtils.equals(idEtapePoursuiteRadiee, idEtape)) {
            this.setMotif(COMotifMessageELP.POURSUITE_RADIE);
        } else {
            this.setMotif(COMotifMessageELP.CDP_NON_TRAITE);
        }
    }

    private String getIdEtape(BSession session, String idSequence, String csEtape) throws ElpProcessException {
        String result;
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setSession(session);
        etapeManager.setForIdSequence(idSequence);
        etapeManager.setForLibEtape(csEtape);
        try {
            etapeManager.find(session.getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ElpProcessException("Erreur lors de la récupération de l'id étape :" + csEtape, e);
        }
        if (etapeManager.getContainer().size() == 1) {
            result = ((COEtape) etapeManager.getFirstEntity()).getIdEtape();
        } else {
            throw new ElpProcessException("Erreur lors de la récupération de l'id étape :" + csEtape);
        }
        return result;
    }
}
