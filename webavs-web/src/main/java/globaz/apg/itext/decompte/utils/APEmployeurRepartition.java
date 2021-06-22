package globaz.apg.itext.decompte.utils;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.globall.db.BSession;
import globaz.prestation.api.IPRDemande;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Value(staticConstructor="of")
public final class APEmployeurRepartition {
    private final Optional<APSituationProfessionnelle> employeurRepartition;
    private final Optional<List<APSituationProfessionnelle>> employeursAssureRepartition;
    private final String typeLot;

    public static final APEmployeurRepartition of(String idSituationProfessionnelle, String typeLot, BSession session) throws Exception {
        final APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
        situationProfessionnelle.setIdSituationProf(idSituationProfessionnelle);
        situationProfessionnelle.setSession(session);
        situationProfessionnelle.retrieve();

        if (situationProfessionnelle != null && !situationProfessionnelle.isNew() && isCalculProrata(situationProfessionnelle, typeLot)) {
            return of(Optional.of(situationProfessionnelle), Optional.empty(), typeLot);
        }

        return of(Optional.empty(), Optional.empty(), typeLot);
    }

    public static final APEmployeurRepartition of(List<APSituationProfessionnelle> listSituationsProfessionnelles, String typeLot) throws Exception {
        return of(Optional.empty(), Optional.of(listSituationsProfessionnelles.stream()
                .filter(sit -> !sit.getIsVersementEmployeur())
                .filter(sit -> APEmployeurRepartition.isCalculProrata(sit, typeLot))
                .collect(Collectors.toList())), typeLot);
    }

    public Double getRevenuTotalEmployeur() {
        return APSituationProfessionnelleManager.getRevenuJournalier(employeurRepartition.get()).doubleValue();
    }

    public boolean hasEmployeurRepartition() {
        return employeurRepartition.isPresent();
    }

    public static boolean isCalculProrata(APSituationProfessionnelle sitProf, String typeLot) {
        return sitProf.getNbJourIndemnise() > 0
                && !sitProf.getIsJoursIdentiques().booleanValue()
                && IPRDemande.CS_TYPE_PROCHE_AIDANT.equals(typeLot);
    }

    public boolean isCalculProrataForAssure() {
        return employeursAssureRepartition.isPresent() && !employeursAssureRepartition.get().isEmpty();
    }

    public static boolean isCalculProrataForAssure(List<APSituationProfessionnelle> sitProfs, String typeLot){
        return sitProfs.stream().anyMatch(sit -> !sit.getIsVersementEmployeur() && isCalculProrata(sit, typeLot));
    }

    public Double calculRevenuJournalierTotalEmployeurAssure() {
        if(employeursAssureRepartition.isPresent()) {
            return employeursAssureRepartition.get().stream()
                    .map(empRep -> APSituationProfessionnelleManager.getRevenuJournalier(empRep).doubleValue())
                    .reduce(0D,Double::sum);
        }
        return null;
    }

    public BigDecimal calculRevenuAnnuelTotalEmployeurAssure() {
        if(employeursAssureRepartition.isPresent()) {
            return BigDecimal.valueOf(calculRevenuJournalierTotalEmployeurAssure()).multiply(new BigDecimal(360));
        }
        return null;
    }
}
