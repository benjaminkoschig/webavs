package globaz.aquila.process.elp;

import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.ScType.Outcome.Summon;
import aquila.ch.eschkg.StatusInfoType;
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
    private String opposition;

    public COScElpDto(ScType scType) {
        this.scType = scType;
        this.numeroStatut = Objects.nonNull(scType.getStatusInfo()) && Objects.nonNull(scType.getStatusInfo().getStatus()) ?
                                    scType.getStatusInfo().getStatus() :
                                    StringUtils.EMPTY;
        initialiseParam();
    }

    private void initialiseParam() {
        opposition = StringUtils.equals(ElpStatut.CDP_AVEC_OPPOSITION, numeroStatut) ? "on" : StringUtils.EMPTY;
    }

    @Override
    public COTypeMessageELP getTypemessage() {
        return COTypeMessageELP.SC;
    }

    @Override
    public String getRemarque() {
            return Objects.nonNull(scType.getStatusInfo()) ?
                        Optional.ofNullable(scType.getStatusInfo().getDetails()).orElse("") :
                        StringUtils.EMPTY;
    }

    public String getDateNotification() {
            return Objects.nonNull(scType.getOutcome()) ?
                    getDate(Optional.ofNullable(scType.getOutcome().getSummon())
                                    .map(Summon::getDelivery)
                                    .map(Summon.Delivery::getDeliveryDate)
                                    .orElse(null)) :
                    StringUtils.EMPTY;
    }

    public String getNoPoursuite() {
        return Objects.nonNull(scType.getCaseNumber()) ? scType.getCaseNumber() : StringUtils.EMPTY;
    }

    public String getOpposition() {
        return opposition;
    }

    /**
     * Permet de renseigner le bon message du motif lorsque la transition n'existe pas : le contentieux est dans une mauvaise �tape.
     *
     * @param idEtape : id de l'�tape actuel du contentieux.
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
            throw new ElpProcessException("Erreur lors de la r�cup�ration de l'id �tape :" + csEtape, e);
        }
        if (etapeManager.getContainer().size() == 1) {
            result = ((COEtape) etapeManager.getFirstEntity()).getIdEtape();
        } else {
            throw new ElpProcessException("L'id �tape du code syst�me " + csEtape + " ne peut pas �tre r�cup�r�.");
        }
        return result;
    }
}
