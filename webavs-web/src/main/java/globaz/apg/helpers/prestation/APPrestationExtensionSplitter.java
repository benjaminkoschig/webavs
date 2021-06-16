package globaz.apg.helpers.prestation;

import ch.globaz.common.util.Dates;
import com.google.gson.Gson;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class APPrestationExtensionSplitter {

    public static SortedSet<APPrestationWrapper> periodeExtentionSpliter(final List<APBaseCalcul> basesCalculs,
                                                                         final SortedSet<APPrestationWrapper> pwSet) {

        List<APBaseCalcul> baseCalculsWithExtension = findBaseCalculeWithExtension(basesCalculs);
        APBaseCalcul baseCalculLast = null;
        if (!baseCalculsWithExtension.isEmpty()) {
            SortedSet<APPrestationWrapper> pwNew = new TreeSet<>(pwSet);
            List<APPrestationWrapper> pwRemoved = new ArrayList<>();
            for (APBaseCalcul baseCalcul : baseCalculsWithExtension) {
                LocalDate dateDebutExtension = Dates.toDate(baseCalcul.getDateDebut());
                LocalDate dateFinExtension = Dates.toDate(baseCalcul.getDateFin());

                for (APPrestationWrapper prestationWrapper : pwSet) {
                    LocalDate dateDebutPrestation = Dates.toDate(prestationWrapper.getPeriodeBaseCalcul().getDateDebut());
                    if (dateDebutExtension.getMonth().equals(dateDebutPrestation.getMonth()) && dateDebutExtension
                            .getDayOfMonth() != dateDebutPrestation.getDayOfMonth()) {

                        JADate dateFinBase = baseCalcul.getDateFin();
                        int nbJourSoldeBase = baseCalcul.getNombreJoursSoldes();

                        if (!estSurLeMemeMois(baseCalculLast, baseCalcul)) {
                            if (!pwRemoved.contains(prestationWrapper)) {
                                // La comparaison ce fait que sur la date de début et pas aussi sur la date de fin
                                // c'est pour cela que l'on s'assure que l'on n'a as déjà supprimé
                                pwNew.remove(prestationWrapper);
                            }
                            pwRemoved.add(prestationWrapper);

                            int nbJourSolde;

                            LocalDate dateFin = dateDebutExtension.minusDays(1);

                            if (dateDebutExtension.getMonth() != dateFinExtension.getMonth()) {
                                nbJourSolde = (int) Dates.daysBetween(Dates.toDate(prestationWrapper.getPeriodeBaseCalcul().getDateDebut()), dateFin);
                                dateFinBase = Dates.toJADate(YearMonth.from(dateDebutExtension).atEndOfMonth());
                                nbJourSoldeBase = prestationWrapper.getPrestationBase().getNombreJoursSoldes() - nbJourSolde;
                            } else {
                                nbJourSolde =  (int) Dates.daysBetween(Dates.toDate(prestationWrapper.getPeriodeBaseCalcul().getDateDebut()), dateFin);
                            }

                            pwNew.add(copyPrestation(
                                    prestationWrapper,
                                    prestationWrapper.getPeriodeBaseCalcul().getDateDebut(),
                                    Dates.toJADate(dateFin), nbJourSolde)
                            );
                        }

                        pwNew.add(copyPrestation(prestationWrapper, baseCalcul.getDateDebut(), dateFinBase, nbJourSoldeBase));
                        baseCalculLast = baseCalcul;
                    }
                }
            }
            return pwNew;
        }

        return pwSet;
    }

    private static boolean estSurLeMemeMois(final APBaseCalcul baseCalculLast, final APBaseCalcul baseCalcul) {
        return baseCalculLast != null && baseCalculLast.getDateDebut().getMonth() == baseCalcul.getDateDebut().getMonth();
    }

    private static List<APBaseCalcul> findBaseCalculeWithExtension(final List<APBaseCalcul> basesCalculs) {
        return basesCalculs.stream()
                           .filter(APBaseCalcul::isExtension)
                           .sorted(Comparator.comparing(o -> Dates.toDate(o.getDateDebut())))
                           .collect(Collectors.toList());
    }

    private static APPrestationWrapper copyPrestation(final APPrestationWrapper prestationWrapper,
                                                      final JADate dateDebut,
                                                      final JADate dateFin,
                                                      final int nombreJoursSoldes) {
        APPrestationWrapper prestationWrapperNew = new APPrestationWrapper();
        prestationWrapperNew.setPrestationBase(copyResultatCalcul(prestationWrapper, dateDebut, dateFin, nombreJoursSoldes));
        if (!prestationWrapper.getPeriodesPGPC().isEmpty()) {
            APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
            periodeWrapper.setDateDebut(dateDebut);
            periodeWrapper.setDateFin(dateFin);
            prestationWrapperNew.getPeriodesPGPC().add(periodeWrapper);
        }
        prestationWrapperNew.setFraisGarde(prestationWrapper.getFraisGarde());
        prestationWrapperNew.setIdDroit(prestationWrapper.getIdDroit());
        prestationWrapperNew.setPeriodeBaseCalcul(new APPeriodeWrapper());
        prestationWrapperNew.getPeriodeBaseCalcul().setDateDebut(dateDebut);
        prestationWrapperNew.getPeriodeBaseCalcul().setDateFin(dateFin);
        return prestationWrapperNew;
    }

    static APResultatCalcul copyResultatCalcul(final APPrestationWrapper prestationWrapper,
                                               final JADate dateDebut,
                                               final JADate dateFin,
                                               final int nombreJoursSoldes) {
        Gson gson = new Gson();
        APResultatCalcul prestationBase = gson.fromJson(gson.toJson(prestationWrapper.getPrestationBase()), APResultatCalcul.class);
        prestationBase.setResultatsCalculsSitProfessionnelle(new ArrayList<>());
        prestationWrapper.getPrestationBase()
                         .getResultatsCalculsSitProfessionnelle().stream()
                         .map(o -> calculerMontantSituationProffessionnelleEtLeSet(o, nombreJoursSoldes))
                         .forEach(prestationBase::addResultatCalculSitProfessionnelle);
        prestationBase.setDateDebut(dateDebut);
        prestationBase.setDateFin(dateFin);
        prestationBase.setNombreJoursSoldes(nombreJoursSoldes);
        return prestationBase;
    }

    private static APResultatCalculSituationProfessionnel calculerMontantSituationProffessionnelleEtLeSet(final APResultatCalculSituationProfessionnel resultatCalculSituationProfessionnel,
                                                                                                          final int nombreJoursSoldes) {
        FWCurrency montant = new FWCurrency(resultatCalculSituationProfessionnel.getSalaireJournalierNonArrondi().getBigDecimalValue()
                                                                                .multiply(new BigDecimal(nombreJoursSoldes)).doubleValue());
        resultatCalculSituationProfessionnel.setMontant(montant);
        return resultatCalculSituationProfessionnel;
    }
}
