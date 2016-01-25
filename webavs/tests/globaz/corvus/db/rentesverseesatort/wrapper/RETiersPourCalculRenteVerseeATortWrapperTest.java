package globaz.corvus.db.rentesverseesatort.wrapper;

import globaz.jade.client.util.JadeDateUtil;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;

public class RETiersPourCalculRenteVerseeATortWrapperTest {

    private void checkContientPeriode(Collection<Periode> listePeriodes, String dateDebut, String dateFin) {

        Assert.assertNotNull("Liste null", listePeriodes);

        boolean trouvee = false;
        for (Periode unePeriode : listePeriodes) {
            if (JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()).equals(dateDebut)
                    && JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()).equals(dateFin)) {
                trouvee = true;
            }
        }
        StringBuilder message = new StringBuilder();
        message.append("\nPériode attendue : [ ").append(dateDebut).append(" - ").append(dateFin).append(" ]\n");
        message.append(getDescriptionListePeriode(listePeriodes));
        Assert.assertTrue(message.toString(), trouvee);
    }

    private void checkNombrePeriode(Collection<Periode> listePeriodes, int nombreAttendu) {

        Assert.assertNotNull("Liste null", listePeriodes);

        StringBuilder message = new StringBuilder();
        message.append("\n\nNombre de période invalide\n");
        message.append("Atendu : ").append(nombreAttendu).append("\n");
        message.append("Trouvé : ").append(listePeriodes.size()).append("\n");
        message.append(getDescriptionListePeriode(listePeriodes)).append("\n");
        Assert.assertEquals(message.toString(), nombreAttendu, listePeriodes.size());
    }

    private String getDescriptionListePeriode(Collection<Periode> listePeriodes) {
        StringBuilder description = new StringBuilder();
        description.append("Périodes présentes : ");
        for (Periode unePeriode : listePeriodes) {
            description.append("[ ").append(unePeriode.getDateDebut()).append(" - ").append(unePeriode.getDateFin())
                    .append(" ]");
        }
        description.append("\n");
        return description.toString();
    }

    @Test
    public void testGetPeriodeDuDroit() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();
        Assert.assertNull(wrapper.getPeriodeDuDroit(null));

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "01.2013", "06.2013", null, 1l);
        Collection<Periode> result = wrapper.getPeriodeDuDroit(Arrays.asList(rente1));
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "01.2013", "06.2013");

        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(2l,
                "07.2013", "12.2013", null, 1l);
        result = wrapper.getPeriodeDuDroit(Arrays.asList(rente1, rente2));
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "01.2013", "12.2013");

        rente2.setDateDebutDroit("08.2013");
        result = wrapper.getPeriodeDuDroit(Arrays.asList(rente1, rente2));
        checkNombrePeriode(result, 2);
        checkContientPeriode(result, "01.2013", "06.2013");
        checkContientPeriode(result, "08.2013", "12.2013");

        RERentesPourCalculRenteVerseeATort rente3 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(3l,
                "01.2014", "03.2014", null, 1l);
        result = wrapper.getPeriodeDuDroit(Arrays.asList(rente1, rente2, rente3));
        checkNombrePeriode(result, 2);
        checkContientPeriode(result, "01.2013", "06.2013");
        checkContientPeriode(result, "08.2013", "03.2014");
    }

    /**
     * 1) Périodes se chevauchant partiellement <br/>
     * 
     * <pre>
     * 1.1)
     * Nouveau droit                       [------------------]
     * Ancien droit       [-----------------------]
     * Résultat                            [xxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 1.2)
     * Nouveau droit      [-----------------------]
     * Ancien droit                        [---------------------]
     * Résultat                            [xxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * 2) Période englobée dans une autre
     * 
     * <pre>
     * 2.1)
     * Nouveau droit      [--------------------------]
     * Ancien droit            [---------]
     * Résultat                [xxxxxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 2.2)
     * Nouveau droit           [---------]
     * Ancien droit       [----------------------]
     * Résultat                [xxxxxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * 3) Trou dans les périodes des droits
     * 
     * <pre>
     * 3.1)
     * Nouveau droit      [---------]         [-----]
     * Ancien droit           [-----------------------]
     * Résultat               [xxxxx][xxxxxxx][xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 3.2) Spécialité ici : on ne veut pas de période de chevauchement
     *      lorsque l'ancien droit n'est pas présent
     * Nouveau droit          [-----------------------]
     * Ancien droit       [---------]         [-----]
     * Résultat               [xxxxx]         [xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 3.3)
     * Nouveau droit      [---------]         [-----]
     * Ancien droit           [---------][-------------]
     * Résultat               [xxxxx][xx][xxx][xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * 
     * @throws Exception
     */
    @Test
    public void testGetPeriodesChevauchementDesDroits() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        /**
         * préparation test 1.1)
         * 
         * <pre>
         * Nouveau droit                   [06.2013              10.2013]
         * Ancien droit       [01.2013                08.2013]
         * Résultat                        [06.2013 - 08.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        RERentesPourCalculRenteVerseeATort renteNouveauDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(1l, "06.2013", "10.2013", null, 1l);
        RERentesPourCalculRenteVerseeATort renteAncienDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(2l, "01.2013", "08.2013", null, 1l, null);
        wrapper.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1));
        wrapper.setRentesAncienDroit(Arrays.asList(renteAncienDroit1));

        // test 1.1)
        Collection<Periode> result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "06.2013", "08.2013");

        /**
         * préparation test 1.2)
         * 
         * <pre>
         * Nouveau droit      [01.2013                08.2013]
         * Ancien droit                    [06.2013              10.2013]
         * Résultat                        [06.2013 - 08.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        renteNouveauDroit1.setDateDebutDroit("01.2013");
        renteNouveauDroit1.setDateFinDroit("08.2013");

        renteAncienDroit1.setDateDebutDroit("06.2013");
        renteAncienDroit1.setDateFinDroit("10.2013");

        // test 1.2
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "06.2013", "08.2013");

        /**
         * préparation test 2.1)
         * 
         * <pre>
         * Nouveau droit      [01.2013                             12.2013]
         * Ancien droit               [03.2013             10.2013]
         * Résultat                   [03.2013             10.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        renteNouveauDroit1.setDateDebutDroit("01.2013");
        renteNouveauDroit1.setDateFinDroit("12.2013");

        renteAncienDroit1.setDateDebutDroit("03.2013");
        renteAncienDroit1.setDateFinDroit("10.2013");

        // test 2.1
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "03.2013", "10.2013");

        /**
         * préparation test 2.2)
         * 
         * <pre>
         * Nouveau droit              [03.2013             10.2013]
         * Ancien droit       [01.2013                             12.2013]
         * Résultat                   [03.2013             10.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        renteNouveauDroit1.setDateDebutDroit("03.2013");
        renteNouveauDroit1.setDateFinDroit("10.2013");

        renteAncienDroit1.setDateDebutDroit("01.2013");
        renteAncienDroit1.setDateFinDroit("12.2013");

        // test 2.2
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "03.2013", "10.2013");

        /**
         * préparation test 3.1)
         * 
         * <pre>
         * Nouveau droit      [01.2013       05.2013]                   [08.2013   10.2013]
         * Ancien droit           [02.2013                                                      12.2013]
         * Résultat               [02.2013   05.2013][06.2013   07.2013][08.2013   10.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        renteNouveauDroit1.setDateDebutDroit("01.2013");
        renteNouveauDroit1.setDateFinDroit("05.2013");
        RERentesPourCalculRenteVerseeATort renteNouveauDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(3l, "08.2013", "10.2013", null, 1l);
        wrapper.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1, renteNouveauDroit2));

        renteAncienDroit1.setDateDebutDroit("02.2013");
        renteAncienDroit1.setDateFinDroit("12.2013");

        // test 3.1
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 3);
        checkContientPeriode(result, "02.2013", "05.2013");
        checkContientPeriode(result, "06.2013", "07.2013");
        checkContientPeriode(result, "08.2013", "10.2013");

        /**
         * 
         * <pre>
         * 3.2) Spécialité ici : on ne veut pas de période de chevauchement
         *      lorsque l'ancien droit n'est pas présent
         * Nouveau droit          [03.2013                                      12.2013]
         * Ancien droit       [02.2013       05.2013]         [08.2013   10.2013]
         * Résultat               [03.2013   05.2013]         [08.2013   10.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */
        // préparation test 3.2)
        renteNouveauDroit1.setDateDebutDroit("03.2013");
        renteNouveauDroit1.setDateFinDroit("12.2013");
        wrapper.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1));

        renteAncienDroit1.setDateDebutDroit("02.2013");
        renteAncienDroit1.setDateFinDroit("05.2013");
        RERentesPourCalculRenteVerseeATort renteAncienDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(3l, "08.2013", "10.2013", null, 1l);
        wrapper.setRentesAncienDroit(Arrays.asList(renteAncienDroit1, renteAncienDroit2));

        // test 3.2
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 2);
        checkContientPeriode(result, "03.2013", "05.2013");
        checkContientPeriode(result, "08.2013", "10.2013");

        /**
         * <pre>
         * 3.3)
         * Nouveau droit      [03.2013      06.2013]                          [10.2013     12.2013]
         * Ancien droit          [04.2013            07.2013][08.2013          11.2013]
         * Résultat              [04.2013   06.2013][07.2013][08.2013 09.2013][10.2013 11.2013]
         *                    -----------------------------------------> temps
         * </pre>
         */

        // préparation test 3.3)
        renteNouveauDroit1.setDateDebutDroit("03.2013");
        renteNouveauDroit1.setDateFinDroit("06.2013");
        renteNouveauDroit2.setDateDebutDroit("10.2013");
        renteNouveauDroit2.setDateFinDroit("12.2013");
        wrapper.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1, renteNouveauDroit2));

        renteAncienDroit1.setDateDebutDroit("04.2013");
        renteAncienDroit1.setDateFinDroit("07.2013");
        renteAncienDroit2.setDateDebutDroit("08.2013");
        renteAncienDroit2.setDateFinDroit("11.2013");
        wrapper.setRentesAncienDroit(Arrays.asList(renteAncienDroit1, renteAncienDroit2));

        // test 3.3
        result = wrapper.getPeriodesChevauchementDesDroits();
        Assert.assertNotNull(result);
        checkNombrePeriode(result, 4);
        checkContientPeriode(result, "04.2013", "06.2013");
        checkContientPeriode(result, "07.2013", "07.2013");
        checkContientPeriode(result, "08.2013", "09.2013");
        checkContientPeriode(result, "10.2013", "11.2013");

    }

    @Test
    public void testGetPeriodeTrous() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        /**
         * <pre>
         * 		périodes : [01.2013 - 06.2013][07.2013 - 12.2013]
         * 		résultat : rien (null)
         * </pre>
         */
        Periode periode1 = new Periode("01.2013", "06.2013");
        Periode periode2 = new Periode("07.2013", "12.2013");

        Assert.assertNull(wrapper.getPeriodeTrous(Arrays.asList(periode1, periode2)));

        /**
         * <pre>
         * 		périodes : [01.2013 - 06.2013]           [08.2013 - 12.2013]
         * 		résultat :                    [ 07.2013 ]
         * </pre>
         */
        periode2 = new Periode("08.2013", "12.2013");

        Collection<Periode> result = wrapper.getPeriodeTrous(Arrays.asList(periode1, periode2));
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "07.2013", "07.2013");

        /**
         * <pre>
         * 		périodes : [01.2013 - 06.2013]           [08.2013 - 12.2013]                   [05.2014 - 07.2014]
         * 		résultat :                    [ 07.2013 ]                   [01.2014 - 04.2014]
         * </pre>
         */
        Periode periode3 = new Periode("05.2014", "07.2014");

        result = wrapper.getPeriodeTrous(Arrays.asList(periode1, periode2, periode3));
        checkNombrePeriode(result, 2);
        checkContientPeriode(result, "07.2013", "07.2013");
        checkContientPeriode(result, "01.2014", "04.2014");

        /**
         * <pre>
         * 		périodes : [01.2013 - 06.2013][07.2013 - 12.2013]                   [05.2014 - 07.2014]
         * 		résultat :                                       [01.2014 - 04.2014]
         * </pre>
         */
        periode2 = new Periode("07.2013", "12.2013");

        result = wrapper.getPeriodeTrous(Arrays.asList(periode1, periode2, periode3));
        checkNombrePeriode(result, 1);
        checkContientPeriode(result, "01.2014", "04.2014");

    }

    @Test
    public void testGetRenteAncienDroitSelonId() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        Assert.assertNull(wrapper.getRenteAncienDroitSelonId(null));

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(null, "",
                "", null, null, null);
        wrapper.setRentesAncienDroit(Arrays.asList(rente1));
        Assert.assertNull(wrapper.getRenteAncienDroitSelonId(null));

        rente1.setIdRenteAccordee(1l);
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "",
                "", null, null, null);
        RERentesPourCalculRenteVerseeATort rente3 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(3l, "",
                "", null, null, null);
        wrapper.setRentesAncienDroit(Arrays.asList(rente1, rente2, rente3));

        Assert.assertEquals(rente1, wrapper.getRenteAncienDroitSelonId(1l));
        Assert.assertEquals(rente2, wrapper.getRenteAncienDroitSelonId(2l));
        Assert.assertEquals(rente3, wrapper.getRenteAncienDroitSelonId(3l));
    }

    @Test
    public void testGetRentesNouveauDroitDansPeriode() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        Assert.assertNotNull(wrapper.getRentesNouveauDroitDansPeriode(null));
        Assert.assertTrue(wrapper.getRentesNouveauDroitDansPeriode(null).isEmpty());

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "01.2013", "06.2013", null, 1l);
        wrapper.setRentesNouveauDroit(Arrays.asList(rente1));

        Periode periode = new Periode("01.2012", "12.2012");
        Assert.assertNotNull(wrapper.getRentesNouveauDroitDansPeriode(periode));
        Assert.assertTrue(wrapper.getRentesNouveauDroitDansPeriode(periode).isEmpty());

        periode = new Periode("01.2012", "01.2013");
        Assert.assertNotNull(wrapper.getRentesNouveauDroitDansPeriode(periode));
        Assert.assertFalse(wrapper.getRentesNouveauDroitDansPeriode(periode).isEmpty());
        Assert.assertTrue(wrapper.getRentesNouveauDroitDansPeriode(periode).contains(rente1));

        periode = new Periode("06.2012", "12.2013");
        Assert.assertNotNull(wrapper.getRentesNouveauDroitDansPeriode(periode));
        Assert.assertFalse(wrapper.getRentesNouveauDroitDansPeriode(periode).isEmpty());
        Assert.assertTrue(wrapper.getRentesNouveauDroitDansPeriode(periode).contains(rente1));

        periode = new Periode("07.2013", "12.2013");
        Assert.assertNotNull(wrapper.getRentesNouveauDroitDansPeriode(periode));
        Assert.assertTrue(wrapper.getRentesNouveauDroitDansPeriode(periode).isEmpty());
    }

    @Test
    public void testHasTrouDansLesPeriodesDeAncienDroit() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDeAncienDroit());

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(1l,
                "01.2013", "06.2013", null, 1l, null);
        wrapper.setRentesAncienDroit(Arrays.asList(rente1));
        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDeAncienDroit());

        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "07.2013", "12.2013", null, 1l, null);
        wrapper.setRentesAncienDroit(Arrays.asList(rente1, rente2));
        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDeAncienDroit());

        rente2.setDateDebutDroit("08.2013");
        Assert.assertTrue(wrapper.hasTrouDansLesPeriodesDeAncienDroit());
    }

    @Test
    public void testHasTrouDansLesPeriodesDuNouveauDroit() throws Exception {
        RETiersPourCalculRenteVerseeATortWrapper wrapper = new RETiersPourCalculRenteVerseeATortWrapper();

        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDuNouveauDroit());

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "01.2013", "06.2013", null, 1l);
        wrapper.setRentesNouveauDroit(Arrays.asList(rente1));
        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDuNouveauDroit());

        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(2l,
                "07.2013", "12.2013", null, 1l);
        wrapper.setRentesNouveauDroit(Arrays.asList(rente1, rente2));
        Assert.assertFalse(wrapper.hasTrouDansLesPeriodesDuNouveauDroit());

        rente2.setDateDebutDroit("08.2013");
        Assert.assertTrue(wrapper.hasTrouDansLesPeriodesDuNouveauDroit());
    }

    @Test
    public void testUnePeriodeEnglobeUneAutre() throws Exception {

        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(null, null));

        Periode periode1 = new Periode("01.2013", "06.2013");

        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, null));
        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(null, periode1));

        Periode periode2 = new Periode("07.2013", "12.2013");
        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode2 = new Periode("02.2013", "07.2013");
        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode2 = new Periode("12.2012", "07.2013");
        Assert.assertTrue(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode2 = new Periode("12.2012", "06.2013");
        Assert.assertTrue(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode2 = new Periode("01.2013", "06.2013");
        Assert.assertTrue(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode2 = new Periode("12.2013", "05.2013");
        Assert.assertFalse(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));

        periode1 = new Periode("12.2011", "05.2013");
        periode2 = new Periode("12.2011", "");
        Assert.assertTrue(RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(periode1, periode2));
    }

}
