package globaz.apg.helpers.prestation;

import globaz.apg.db.droits.APDroitLAPG;
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
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class APPrestationExtensionSplitterTest {

    @Test
    public void periodeExtensionSpliter_sans_jourSupplementaire_renvoiLaMemeListe() {
        APDroitLAPG droit = new APDroitLAPG();
        List<APBaseCalcul> basesCalcul = new ArrayList<>();
        SortedSet<APPrestationWrapper> pw = new TreeSet<>(new APPrestationWrapperComparator());
        Stream.of("", "0", null).forEachOrdered(jourSupp -> {
            droit.setJoursSupplementaires(jourSupp);
            assertThat(APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalcul, pw, droit)).isSameAs(pw);
        });
        droit.setJoursSupplementaires("0");
    }

    @Test
    public void periodeExtensionSpliter_avecMemePeriode_renvoiLaMemeListe() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "31.01.2021"));

        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        List<APPrestationWrapper> pw = new ArrayList<>();
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));

        APDroitLAPG droit = new APDroitLAPG();
        droit.setJoursSupplementaires("1");

        assertThat(APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet, droit)).isSameAs(pwSet);

        droit.setJoursSupplementaires("0");
    }

   // @Test
    public void periodeExtensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWrapper() throws JAException {
        APPrestationHelper apPrestationHelper = new APPrestationHelper();
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "31.01.2021"));
        basesCalculList.add(createBaseCalcul("01.02.2021", "23.02.2021"));
        basesCalculList.add(createBaseCalcul("24.02.2021", "28.02.2021"));

        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));

        APDroitLAPG droit = new APDroitLAPG();
        droit.setJoursSupplementaires("5");
        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet, droit);
        assertThat(apPrestationWrappers).hasSize(3);

        droit.setJoursSupplementaires("0");
    }


    //@Test
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


        APDroitLAPG droit = new APDroitLAPG();
        droit.setJoursSupplementaires("2");
        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet, droit);
        assertThat(apPrestationWrappers).hasSize(5);

    }


   // @Test
    public void periodeExtensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWdrapper2() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();
        basesCalculList.add(createBaseCalcul("01.01.2021", "28.02.2021"));
        basesCalculList.add(createBaseCalcul("01.03.2021", "03.04.2021"));
        basesCalculList.add(createBaseCalcul("04.04.2021", "25.05.2021"));
        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.01.2021"));
        pwSet.add(createApPrestationWrapper("01.02.2021", "28.02.2021"));
        pwSet.add(createApPrestationWrapper("01.03.2021", "31.03.2021"));
        pwSet.add(createApPrestationWrapper("01.04.2021", "30.04.2021"));
        pwSet.add(createApPrestationWrapper("01.05.2021", "25.05.2021"));


        pwSet.add(createApPrestationWrapper("01.04.2021", "03.04.2021")); // standard
        pwSet.add(createApPrestationWrapper("04.04.2021", "30.04.2021")); // extension
        pwSet.add(createApPrestationWrapper("01.05.2021", "25.05.2021"));

        APDroitLAPG droit = new APDroitLAPG();
        droit.setJoursSupplementaires("2");
        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet, droit);
        assertThat(apPrestationWrappers).hasSize(5);

        droit.setJoursSupplementaires("0");
    }

   // @Test
    public void copyResultatCalcul_avecBonneValeur_ok() throws JAException {
        APPrestationWrapper prestationWrapper = createApPrestationWrapper("01.01.2021", "12.01.2021");
        APBaseCalcul apBaseCalcul = createBaseCalcul("01.01.2021", "12.01.2021");
        apBaseCalcul.setNombreJoursSoldes(12);
        APResultatCalculSituationProfessionnel apResultatCalculSituationProfessionnel = new APResultatCalculSituationProfessionnel();
        apResultatCalculSituationProfessionnel.setSalaireJournalierNonArrondi(new FWCurrency(1));
        prestationWrapper.getPrestationBase().addResultatCalculSitProfessionnelle(apResultatCalculSituationProfessionnel);
        APResultatCalcul apResultatCalcul = APPrestationExtensionSplitter.copyResultatCalcul(prestationWrapper, apBaseCalcul);
        assertThat(apResultatCalcul.getResultatsCalculsSitProfessionnelle().get(0).getMontant(BigDecimal.ZERO)).isEqualTo(new BigDecimal("12.00"));

    }

    @Test
    public void periodeExensionSpliter_avecPeriodeEnPlus_ajoutUnePeriodePresationWrappers() throws JAException {
        List<APBaseCalcul> basesCalculList = new ArrayList<>();

        basesCalculList.add(createBaseCalcul("01.01.2021", "28.02.2021"));
        basesCalculList.add(createBaseCalcul("01.03.2021", "31.03.2021")); // extension

        SortedSet<APPrestationWrapper> pwSet = new TreeSet<>(new APPrestationWrapperComparator());
        pwSet.add(createApPrestationWrapper("01.01.2021", "31.03.2021"));

        APDroitLAPG droit = new APDroitLAPG();
        droit.setJoursSupplementaires("5");
        Collection<APPrestationWrapper> apPrestationWrappers = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalculList, pwSet, droit);
        assertThat(apPrestationWrappers).hasSize(2);

        Iterator<APPrestationWrapper> iterator = apPrestationWrappers.iterator();

        APPeriodeWrapper periodeBaseCalcul = iterator.next().getPeriodeBaseCalcul();
        assertThat(periodeBaseCalcul.getDateDebut()).hasToString("01012021");
        assertThat(periodeBaseCalcul.getDateFin()).hasToString("28022021");

        periodeBaseCalcul = iterator.next().getPeriodeBaseCalcul();
        assertThat(periodeBaseCalcul.getDateDebut()).hasToString("01032021");
        assertThat(periodeBaseCalcul.getDateFin()).hasToString("31032021");

        droit.setJoursSupplementaires("0");
    }

    private APPrestationWrapper createApPrestationWrapper(final String dateDebut, final String dateFin) throws JAException {
        APPrestationWrapper prestationWrapper = new APPrestationWrapper();
        APPeriodeWrapper apPeriodeWrapper = new APPeriodeWrapper();
        apPeriodeWrapper.setDateDebut(new JADate(dateDebut));
        apPeriodeWrapper.setDateFin(new JADate(dateFin));
        prestationWrapper.setPeriodeBaseCalcul(apPeriodeWrapper);
        APResultatCalcul apResultatCalcul = new APResultatCalcul();
        prestationWrapper.setPrestationBase(apResultatCalcul);
        prestationWrapper.setFraisGarde(new FWCurrency(10));
        return prestationWrapper;
    }

    private APBaseCalcul createBaseCalcul(String dateDebut, String dateFin) throws JAException {
        APBaseCalcul apBaseCalcul = new APBaseCalcul();
        apBaseCalcul.setDateDebut(new JADate(dateDebut));
        apBaseCalcul.setDateFin(new JADate(dateFin));
        APBaseCalculSituationProfessionnel apBaseCalculSituationProfessionnel = new APBaseCalculSituationProfessionnel();
        apBaseCalcul.addBaseCalculSituationProfessionnel(apBaseCalculSituationProfessionnel);
        return apBaseCalcul;
    }
}
