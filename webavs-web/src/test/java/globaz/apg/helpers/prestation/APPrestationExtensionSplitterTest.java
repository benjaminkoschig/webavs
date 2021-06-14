package globaz.apg.helpers.prestation;

import ch.globaz.common.util.Dates;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapperComparator;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class APPrestationExtensionSplitterTest {

    @Test
    public void periodeExtensionSpliter_sans_jourSupplementaire_renvoiLaMemeListe() {
        List<APBaseCalcul> basesCalcul = new ArrayList<>();
        SortedSet<APPrestationWrapper> pw = new TreeSet<>(new APPrestationWrapperComparator());
        Stream.of("", "0", null).forEachOrdered(jourSupp -> {
            assertThat(APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalcul, pw)).isSameAs(pw);
        });
    }

    @Test
    public void periodeExtensionSpliter_avecMemePeriode_renvoiLaMemeListe() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "31.01.2021"));

        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));

        assertThat(APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet)).isSameAs(pwSet);

    }

    @Test
    public void periodeExtensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWrapper() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "31.01.2021"));
        basesCalculList.add(createBaseCalcul("01.02.2021", "23.02.2021"));
        basesCalculList.add(createBaseCalcul("24.02.2021", "28.02.2021").setExtension(true));

        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));


        List<APPrestationWrapper> apPrestationWrappers = new ArrayList<>(APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList,
                                                                                                                               pwSet));
        assertThat(apPrestationWrappers).hasSize(3);
        assertThat(apPrestationWrappers.get(1).getPeriodeBaseCalcul().getDateDebut()).hasToString("01022021");
        assertThat(apPrestationWrappers.get(1).getPeriodeBaseCalcul().getDateFin()).hasToString("23022021");
        assertThat(apPrestationWrappers.get(1).getPrestationBase().getNombreJoursSoldes()).isEqualTo(23);

        assertThat(apPrestationWrappers.get(2).getPeriodeBaseCalcul().getDateDebut()).hasToString("24022021");
        assertThat(apPrestationWrappers.get(2).getPeriodeBaseCalcul().getDateFin()).hasToString("28022021");
        assertThat(apPrestationWrappers.get(2).getPrestationBase().getNombreJoursSoldes()).isEqualTo(5);
    }

    @Test
    public void periodeExtensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWdrapper() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "28.02.2021"));
        basesCalculList.add(createBaseCalcul("01.03.2021", "03.04.2021"));
        basesCalculList.add(createBaseCalcul("04.04.2021", "25.04.2021").setExtension(true));
        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));
        pwSet.add(createApPrestationWrapper("01.03.2021", "31.03.2021"));
        pwSet.add(createApPrestationWrapper("01.04.2021", "25.04.2021"));

        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet);
        assertThat(apPrestationWrappers).hasSize(5);
        assertThat(apPrestationWrappers).containsExactly(
                createApPrestationWrapper("01.01.2021", "31.01.2021"),
                createApPrestationWrapper("01.02.2021", "28.02.2021"),
                createApPrestationWrapper("01.03.2021", "31.03.2021"),
                createApPrestationWrapper("01.04.2021", "03.04.2021"),
                createApPrestationWrapper("04.04.2021", "25.04.2021"));

    }

    @Test
    public void periodeExtensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWdrapper2() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "28.02.2021"));
        basesCalculList.add(createBaseCalcul("01.03.2021", "03.04.2021"));
        basesCalculList.add(createBaseCalcul("04.04.2021", "25.05.2021").setExtension(true));
        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));
        pwSet.add(createApPrestationWrapper("01.03.2021", "31.03.2021"));
        pwSet.add(createApPrestationWrapper("01.04.2021", "30.04.2021"));
        pwSet.add(createApPrestationWrapper("01.05.2021", "25.05.2021"));

        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet);
        assertThat(apPrestationWrappers).containsExactly(
                createApPrestationWrapper("01.01.2021", "31.01.2021"),
                createApPrestationWrapper("01.02.2021", "28.02.2021"),
                createApPrestationWrapper("01.03.2021", "31.03.2021"),
                createApPrestationWrapper("01.04.2021", "03.04.2021"),
                createApPrestationWrapper("04.04.2021", "30.04.2021"),
                createApPrestationWrapper("01.05.2021", "25.05.2021"));
        List<APPrestationWrapper> wrapperList = new ArrayList<>(apPrestationWrappers);
        assertThat(wrapperList.get(3).getPrestationBase().getNombreJoursSoldes()).isEqualTo(3);
        assertThat(wrapperList.get(4).getPrestationBase().getNombreJoursSoldes()).isEqualTo(27);
        assertThat(wrapperList.get(4).getPeriodeBaseCalcul().getDateDebut()).hasToString("04042021");
        assertThat(wrapperList.get(4).getPeriodeBaseCalcul().getDateFin()).hasToString("30042021");

        assertThat(wrapperList.get(5).getPrestationBase().getNombreJoursSoldes()).isEqualTo(25);
    }

    @Test
    public void copyResultatCalcul_avecBonneValeur_ok() throws JAException {
        APPrestationWrapper prestationWrapper = createApPrestationWrapper("01.01.2021", "12.01.2021");
        APBaseCalcul apBaseCalcul = createBaseCalcul("01.01.2021", "12.01.2021");
        apBaseCalcul.setNombreJoursSoldes(12);
        APResultatCalculSituationProfessionnel apResultatCalculSituationProfessionnel = new APResultatCalculSituationProfessionnel();
        apResultatCalculSituationProfessionnel.setSalaireJournalierNonArrondi(new FWCurrency(1));
        prestationWrapper.getPrestationBase().addResultatCalculSitProfessionnelle(apResultatCalculSituationProfessionnel);
        APResultatCalcul apResultatCalcul = APPrestationExtensionSplitter.copyResultatCalcul(prestationWrapper, apBaseCalcul
                .getDateDebut(), apBaseCalcul.getDateFin(), apBaseCalcul.getNombreJoursSoldes());
        assertThat(apResultatCalcul.getResultatsCalculsSitProfessionnelle().get(0).getMontant(BigDecimal.ZERO)).isEqualTo(new BigDecimal("12.00"));
    }


    @Test
    public void periodeExtensionSpliter_avecMemeNombreDePeriode_ajoutUnePeriodePresationWdrapper() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "15.02.2021"));
        basesCalculList.add(createBaseCalcul("16.02.2021", "28.02.2021").setExtension(true));
        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));

        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter
                .periodeExtentionSpliter(basesCalculList, pwSet);
        assertThat(apPrestationWrappers).containsExactly(
                createApPrestationWrapper("01.01.2021", "31.01.2021"),
                createApPrestationWrapper("01.02.2021", "15.02.2021"),
                createApPrestationWrapper("16.02.2021", "28.02.2021")
        );
    }

    private APPrestationWrapper createApPrestationWrapper(final String dateDebut, final String dateFin) throws JAException {
        APPrestationWrapper prestationWrapper = new APPrestationWrapper();
        APPeriodeWrapper apPeriodeWrapper = new APPeriodeWrapper();
        apPeriodeWrapper.setDateDebut(new JADate(dateDebut));
        apPeriodeWrapper.setDateFin(new JADate(dateFin));
        prestationWrapper.setPeriodeBaseCalcul(apPeriodeWrapper);
        APResultatCalcul apResultatCalcul = new APResultatCalcul();
        prestationWrapper.setPrestationBase(apResultatCalcul);

        prestationWrapper.getPrestationBase()
                         .setNombreJoursSoldes((int) Dates.daysBetween(dateDebut, dateFin));
        prestationWrapper.setFraisGarde(new FWCurrency(10));
        return prestationWrapper;
    }

    private APBaseCalcul createBaseCalcul(String dateDebut, String dateFin) throws JAException {
        APBaseCalcul apBaseCalcul = new APBaseCalcul();
        apBaseCalcul.setDateDebut(new JADate(dateDebut));
        apBaseCalcul.setDateFin(new JADate(dateFin));
        apBaseCalcul.setNombreJoursSoldes((int) Dates.daysBetween(dateDebut, dateFin));
        APBaseCalculSituationProfessionnel apBaseCalculSituationProfessionnel = new APBaseCalculSituationProfessionnel();
        apBaseCalcul.addBaseCalculSituationProfessionnel(apBaseCalculSituationProfessionnel);
        return apBaseCalcul;
    }
}
