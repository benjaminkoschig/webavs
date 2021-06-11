package globaz.apg.helpers.prestation;

import ch.globaz.common.util.Dates;
import com.google.gson.Gson;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class APPrestationExtensionSplitter {

    public static SortedSet<APPrestationWrapper> periodeExtentionSpliter(final List<APBaseCalcul> basesCalculs,
                                                                         final SortedSet<APPrestationWrapper> pwSet,
                                                                         final APDroitLAPG droit) {

        if (!JadeStringUtil.isBlankOrZero(droit.getJoursSupplementaires()) && basesCalculs.size() != pwSet.size()) {
            Map<APPrestationWrapper, List<APBaseCalcul>> map = mapByPeriodeDate(basesCalculs, pwSet);
            SortedSet<APPrestationWrapper> pwNew = new TreeSet<>(pwSet);
            map.forEach((prestationWrapper, apBaseCalculs) -> {
                if (apBaseCalculs.size() > 1) {
                    pwNew.remove(prestationWrapper);
                    apBaseCalculs.stream()
                                 .sorted(Comparator.comparing(o -> Dates.toDate(o.getDateDebut())))
                                 .forEach(apBaseCalcul -> pwNew.add(copyPrestation(prestationWrapper, apBaseCalcul)));
                }
            });
            return pwNew;
        }

        return pwSet;
    }

    private static Map<APPrestationWrapper, List<APBaseCalcul>> mapByPeriodeDate(final List<APBaseCalcul> basesCalculs, final SortedSet<APPrestationWrapper> pwSet) {
        Map<APPrestationWrapper, List<APBaseCalcul>> map = new HashMap<>();
        pwSet.forEach(apPrestationWrapper -> {
            ArrayList<APBaseCalcul> baseCalculs = new ArrayList<>();
            map.put(apPrestationWrapper, baseCalculs);
            basesCalculs.forEach(apBaseCalcul -> {
                LocalDate dateDebut = Dates.toDate(apBaseCalcul.getDateDebut());
                LocalDate dateFin = Dates.toDate(apBaseCalcul.getDateFin());
                LocalDate dateDebutPrestation = Dates.toDate(apPrestationWrapper.getPeriodeBaseCalcul().getDateDebut());
                LocalDate dateFinPrestation = Dates.toDate(apPrestationWrapper.getPeriodeBaseCalcul().getDateFin());

                if ((dateDebutPrestation.isBefore(dateDebut) || dateDebutPrestation.isEqual(dateDebut))
                        && (dateFinPrestation.isAfter(dateFin) || dateFinPrestation.isEqual(dateFin))) {
                    baseCalculs.add(apBaseCalcul);
                }
            });
        });
        return map;
    }

    private static APPrestationWrapper copyPrestation(final APPrestationWrapper prestationWrapper, final APBaseCalcul apBaseCalcul) {
        APPrestationWrapper prestationWrapperNew = new APPrestationWrapper();
        prestationWrapperNew.setPrestationBase(copyResultatCalcul(prestationWrapper, apBaseCalcul));
        if (!prestationWrapper.getPeriodesPGPC().isEmpty()) {
            APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
            periodeWrapper.setDateDebut(apBaseCalcul.getDateDebut());
            periodeWrapper.setDateFin(apBaseCalcul.getDateFin());
            prestationWrapperNew.getPeriodesPGPC().add(periodeWrapper);
        }
        prestationWrapperNew.setFraisGarde(prestationWrapper.getFraisGarde());
        prestationWrapperNew.setIdDroit(prestationWrapper.getIdDroit());
        prestationWrapperNew.setPeriodeBaseCalcul(new APPeriodeWrapper());
        prestationWrapperNew.getPeriodeBaseCalcul().setDateDebut(apBaseCalcul.getDateDebut());
        prestationWrapperNew.getPeriodeBaseCalcul().setDateFin(apBaseCalcul.getDateFin());
        return prestationWrapperNew;
    }

    static APResultatCalcul copyResultatCalcul(final APPrestationWrapper prestationWrapper, final APBaseCalcul apBaseCalcul) {
        Gson gson = new Gson();
        APResultatCalcul prestationBase = gson.fromJson(gson.toJson(prestationWrapper.getPrestationBase()), APResultatCalcul.class);
        prestationBase.setResultatsCalculsSitProfessionnelle(new ArrayList<>());
        prestationWrapper.getPrestationBase()
                         .getResultatsCalculsSitProfessionnelle().stream()
                         .map(o -> calculerMontantSituationProffessionnelleEtLeSet(apBaseCalcul, o))
                         .forEach(prestationBase::addResultatCalculSitProfessionnelle);
        prestationBase.setDateDebut(apBaseCalcul.getDateDebut());
        prestationBase.setDateFin(apBaseCalcul.getDateFin());
        prestationBase.setNombreJoursSoldes(apBaseCalcul.getNombreJoursSoldes());
        return prestationBase;
    }

    private static APResultatCalculSituationProfessionnel calculerMontantSituationProffessionnelleEtLeSet(final APBaseCalcul apBaseCalcul, final APResultatCalculSituationProfessionnel o) {
        FWCurrency montant = new FWCurrency(o.getSalaireJournalierNonArrondi().getBigDecimalValue()
                                             .multiply(new BigDecimal(apBaseCalcul.getNombreJoursSoldes())).doubleValue());
        o.setMontant(montant);
        return o;
    }
}
