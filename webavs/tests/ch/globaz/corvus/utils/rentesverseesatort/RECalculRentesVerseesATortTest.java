package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortWrapper;
import globaz.corvus.db.rentesverseesatort.wrapper.REPrestationDuePourCalculRenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.wrapper.RERenteVerseeATortWrapper;
import globaz.corvus.db.rentesverseesatort.wrapper.RERentesPourCalculRenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.wrapper.RETiersPourCalculRenteVerseeATortWrapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public class RECalculRentesVerseesATortTest {

    private RETiersPourCalculRenteVerseeATortWrapper tiers1;
    private RECalculRentesVerseesATortWrapper wrapper;

    private void checkContientRenteVerseeATort(Collection<REDetailCalculRenteVerseeATort> listeRentesVerseesATort,
            Long idRenteAccordeeNouveauDroit, Long idRenteAccordeeAncienDroit, Long idDemandeNouveauDroit,
            BigDecimal montant) {

        Assert.assertNotNull("Liste null", listeRentesVerseesATort);

        boolean trouvee = false;
        for (REDetailCalculRenteVerseeATort unDetailDeCalcul : listeRentesVerseesATort) {
            if (idRenteAccordeeAncienDroit.equals(unDetailDeCalcul.getIdRenteAccordeeAncienDroit())
                    && (((idRenteAccordeeNouveauDroit != null)
                            && (unDetailDeCalcul.getIdRenteAccordeeNouveauDroit() != null) && idRenteAccordeeNouveauDroit
                                .equals(unDetailDeCalcul.getIdRenteAccordeeNouveauDroit())) || ((idRenteAccordeeNouveauDroit == null) && (unDetailDeCalcul
                            .getIdRenteAccordeeNouveauDroit() == null)))
                    && idDemandeNouveauDroit.equals(unDetailDeCalcul.getIdDemandeRente())
                    && montant.equals(unDetailDeCalcul.getMontantTotalVerseeATort())) {
                trouvee = true;
            }
        }
        StringBuilder message = new StringBuilder();
        message.append("\nRente versée à tort attendue: ")
                .append(this.getDescriptionRenteVerseeATort(idRenteAccordeeNouveauDroit, idRenteAccordeeAncienDroit,
                        idDemandeNouveauDroit, montant)).append("\n");
        message.append(getDescriptionListeRentesVerseesATort(listeRentesVerseesATort));
        Assert.assertTrue(message.toString(), trouvee);
    }

    private void checkNombreRentesVerseesATort(Collection<REDetailCalculRenteVerseeATort> listeRentesVerseesATort,
            int nombreAttendu) {

        Assert.assertNotNull("Liste null", listeRentesVerseesATort);

        StringBuilder message = new StringBuilder();
        message.append("\n\nNombre de rentes versées à tort invalide\n");
        message.append("Atendu : ").append(nombreAttendu).append("\n");
        message.append("Trouvé : ").append(listeRentesVerseesATort.size()).append("\n");
        message.append(getDescriptionListeRentesVerseesATort(listeRentesVerseesATort)).append("\n");
        Assert.assertEquals(message.toString(), nombreAttendu, listeRentesVerseesATort.size());
    }

    private String getDescriptionListeRentesVerseesATort(
            Collection<REDetailCalculRenteVerseeATort> listeRentesVerseesATort) {
        StringBuilder description = new StringBuilder();
        description.append("Rentes versées à tort présentes: ");
        for (REDetailCalculRenteVerseeATort uneRenteVerseeATort : listeRentesVerseesATort) {
            description.append(this.getDescriptionRenteVerseeATort(uneRenteVerseeATort));
        }
        description.append("\n");
        return description.toString();
    }

    private String getDescriptionRenteVerseeATort(Long idRenteNouveauDroit, Long idRenteAncienDroit,
            Long idDemandeRente, BigDecimal montant) {
        StringBuilder description = new StringBuilder();
        description.append("{\n   idRenteAccordeeNouveauDroit : ").append(idRenteNouveauDroit)
                .append("\n   idRenteAccordeeAncienDroit : ").append(idRenteAncienDroit)
                .append("\n   idDemandeRente : ").append(idDemandeRente).append("\n   montant : ").append(montant)
                .append("\n}\n");
        return description.toString();
    }

    private String getDescriptionRenteVerseeATort(REDetailCalculRenteVerseeATort renteVerseeATort) {
        return this.getDescriptionRenteVerseeATort(renteVerseeATort.getIdRenteAccordeeNouveauDroit(),
                renteVerseeATort.getIdRenteAccordeeAncienDroit(), renteVerseeATort.getIdDemandeRente(),
                renteVerseeATort.getMontantTotalVerseeATort());
    }

    @Before
    public void setUp() {
        wrapper = new RECalculRentesVerseesATortWrapper();
        wrapper.setIdDemandeRente(1l);

        tiers1 = new RETiersPourCalculRenteVerseeATortWrapper();
        tiers1.setIdTiers(1l);
        tiers1.setNom("nom");
        tiers1.setPrenom("prenom");
        tiers1.setNss(new NumeroSecuriteSociale("756.1234.5678.97"));
        tiers1.setDateNaissance("");
        tiers1.setDateDeces("");
        tiers1.setSexe("");
        tiers1.setNationalite("");

        wrapper.setTiers(Arrays.asList(tiers1));
    }

    @Test
    public void testAvecResultatDuCalculMontantZero() {

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "05.2013", "12.2013", CodePrestation.getCodePrestation(10), 1l);

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "07.2013", BigDecimal.ZERO);
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "01.2013", "07.2013", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        Collection<REDetailCalculRenteVerseeATort> result = RECalculRentesVerseesATort.calculerRentesVerseesATort(
                wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 0);
    }

    @Test
    public void testCalculerRenteVerseeATortAvecTrouDansLeNouveauDroit() {

        /**
         * <pre>
         * Nouveau droit      [01.2013   ->   05.2013]                                 [08.2013     ->            ]
         * Ancien droit           [02.2013     ->          06.2013][07.2013           ->           09.2013]
         * Prestations dues       [02.2013     ->          06.2013][07.2013 -> 07.2013][08.2013 -> 09.2013]
         *                              (1000.- / mois)               (333.- / mois)      (5000.- / mois)
         * -----------------------------------------------------------------
         * Résultat attendu       [02.2013 -> 05.2013][  06.2013  ][     07.2013      ][08.2013 -> 09.2013]
         *                                4000.-          1000.-           333.-              10000.-
         * total = 15333.-
         * </pre>
         */
        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 1l, "02.2013", "06.2013", new BigDecimal("1000.00"));
        RERentesPourCalculRenteVerseeATort renteAncienDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(1l, "02.2013", "06.2013", CodePrestation.getCodePrestation(10), 1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));
        REPrestationDuePourCalculRenteVerseeATort prestationDue2 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(2l, 2l, "07.2013", "07.2013", new BigDecimal("333.00"));
        REPrestationDuePourCalculRenteVerseeATort prestationDue3 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(3l, 2l, "08.2013", "09.2013", new BigDecimal("5000.00"));
        RERentesPourCalculRenteVerseeATort renteAncienDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(
                        2l,
                        "07.2013",
                        "09.2013",
                        CodePrestation.getCodePrestation(10),
                        1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue2,
                                prestationDue3)));
        tiers1.setRentesAncienDroit(Arrays.asList(renteAncienDroit1, renteAncienDroit2));

        RERentesPourCalculRenteVerseeATort renteNouveauDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(3l, "01.2013", "05.2013", CodePrestation.getCodePrestation(10), 1l);
        RERentesPourCalculRenteVerseeATort renteNouveauDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(4l, "08.2013", "", CodePrestation.getCodePrestation(10), 1l);
        tiers1.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1, renteNouveauDroit2));

        Collection<REDetailCalculRenteVerseeATort> result = RECalculRentesVerseesATort.calculerRentesVerseesATort(
                wrapper, "10.2013");
        checkNombreRentesVerseesATort(result, 4);
        checkContientRenteVerseeATort(result, renteNouveauDroit1.getIdRenteAccordee(),
                renteAncienDroit1.getIdRenteAccordee(), wrapper.getIdDemandeRente(), new BigDecimal("4000.00"));
        checkContientRenteVerseeATort(result, null, renteAncienDroit1.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("1000.00"));
        checkContientRenteVerseeATort(result, null, renteAncienDroit2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("333.00"));
        checkContientRenteVerseeATort(result, renteNouveauDroit2.getIdRenteAccordee(),
                renteAncienDroit2.getIdRenteAccordee(), wrapper.getIdDemandeRente(), new BigDecimal("10000.00"));
    }

    @Test
    public void testChevauchementAvecMultiplePrestationesDues() {

        /**
         * préparation test 1
         * 
         * <pre>
         * Nouveau droit                [05.2013             ->             12.2013]
         * Ancien droit       [01.2013           ->           08.2013]
         * Prestations dues   [01.2013 -> 06.2013][07.2013 -> 08.2013]
         *                      (1000.- / mois)     (600.- / mois)
         *                    -----------------------------------------> temps
         * Résultat attendu             [05.2013      ->      08.2013]
         * Montant attendu : 2 mois x 1000.- : 2000.-
         *                   2 mois x 600.-  : 1200.-
         *                   total           : 3200.-
         * </pre>
         */
        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "05.2013", "12.2013", CodePrestation.getCodePrestation(10), 1l);

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "06.2013", new BigDecimal("1000.00"));
        REPrestationDuePourCalculRenteVerseeATort prestationDue2 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(2l, 2l, "07.2013", "08.2013", new BigDecimal("600.00"));
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "01.2013", "08.2013", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1, prestationDue2)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 1
        Collection<REDetailCalculRenteVerseeATort> result = RECalculRentesVerseesATort.calculerRentesVerseesATort(
                wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("3200.00"));

        /**
         * préparation test 2
         * 
         * <pre>
         * Mois paiement : 12.2013
         * 
         * Nouveau droit      [10.2010                         ->
         * Ancien droit       [10.2010                         ->
         * Prestations dues   [10.2010 -> 12.2010][01.2011 -> 12.2012][01.2013 ->
         *                      (1'710.- / mois)    (1'740.- / mois)    (1'755.- / mois)
         *                    --------------------------------------------------------------> temps
         * Résultat attendu   [10.2010                         ->                    12.2013]
         * Montant attendu :  3 mois x 1'710.- :  5'130.-
         *                   24 mois x 1'740.- : 41'760.-
         *                   12 mois x 1'755.- : 21'060.-
         *                   total             : 67'950.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "10.2010", "",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "10.2010", "12.2010",
                new BigDecimal("1710.00"));
        prestationDue2 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(2l, 2l, "01.2011", "12.2012",
                new BigDecimal("1740.00"));
        REPrestationDuePourCalculRenteVerseeATort prestationDue3 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(3l, 2l, "01.2013", "", new BigDecimal("1755.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(
                2l,
                "10.2010",
                "",
                CodePrestation.getCodePrestation(10),
                1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1, prestationDue2,
                        prestationDue3)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 2
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("67950.00"));
    }

    @Test
    public void testChevauchementAvecPriseEnCompteGenreDeRente() {

        /**
         * <pre>
         * Nouveau droit                [05.2013             ->             12.2013]  rente AI
         * Ancien droit R1    [01.2013           ->           08.2013]  rente AI
         * Prestations dues   [01.2013 -> 06.2013][07.2013 -> 08.2013]
         *                      (1000.- / mois)     (600.- / mois)
         * Ancien droit R2    [01.2013           ->           08.2013]  rente API
         * Prestations dues   [01.2013           ->           08.2013]
         *                                (111.- / mois)
         *                    -----------------------------------------> temps
         * Résultat attendu             [05.2013      ->      08.2013]
         * Montant attendu : 2 mois x 1000.- : 2000.-
         *                   2 mois x 600.-  : 1200.-
         *                   total           : 3200.-
         * Les API ne doivent pas être prises dans le calcul car la rente du nouveau droit n'est pas une API
         * </pre>
         */
        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "05.2013", "12.2013", CodePrestation.getCodePrestation(50), 1l);

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "06.2013", new BigDecimal("1000.00"));
        REPrestationDuePourCalculRenteVerseeATort prestationDue2 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(2l, 2l, "07.2013", "08.2013", new BigDecimal("600.00"));
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "01.2013", "08.2013", CodePrestation.getCodePrestation(50), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1, prestationDue2)));

        REPrestationDuePourCalculRenteVerseeATort prestationDue3 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "08.2013", new BigDecimal("111.00"));
        RERentesPourCalculRenteVerseeATort rente3 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(3l,
                "01.2013", "08.2013", CodePrestation.getCodePrestation(88), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue3)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2, rente3));

        Collection<REDetailCalculRenteVerseeATort> result = RECalculRentesVerseesATort.calculerRentesVerseesATort(
                wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("3200.00"));

        /**
         * Mêmes données mais cette fois ci la rente du nouveau droit est une rente API<br/>
         * Montant attendu : 4 mois x 111.- : 444.-
         */
        rente1.setCodePrestation(CodePrestation.getCodePrestation(88));
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente3.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("444.00"));

    }

    /**
     * <p>
     * Avec des périodes finies et une seule zone de chevauchement par cas.
     * </p>
     * <ul>
     * <li>1) Chevauchement partielle</li>
     * <li>2) Période englobante</li>
     * <li>3) Borne commune</li>
     * <li>4) Périodes similaires</li>
     * </ul>
     * 
     */
    @Test
    public void testChevauchementSimple() {

        /**
         * préparation test 1#1
         * 
         * <pre>
         * Nouveau droit                [05.2013      ->      12.2013]
         * Ancien droit       [01.2013      ->      07.2013]
         * Prestations dues   [01.2013      ->      07.2013] (1000.- / mois)
         * Résultat attendu             [05.2013 -> 07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 3 mois x 1000.- : 3000.-
         * </pre>
         */
        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "05.2013", "12.2013", CodePrestation.getCodePrestation(10), 1l);

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "07.2013", new BigDecimal("1000.00"));
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "01.2013", "07.2013", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 1#1
        Collection<REDetailCalculRenteVerseeATort> result = RECalculRentesVerseesATort.calculerRentesVerseesATort(
                wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("3000.00"));

        /**
         * préparation test 1#2)
         * 
         * <pre>
         * Nouveau droit      [01.2013      ->      07.2013]
         * Ancien droit                 [05.2013      ->      12.2013]
         * Prestations dues             [05.2013      ->      12.2013] (1000.- / mois)
         * Résultat attendu             [05.2013 -> 07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 3 mois x 1000.- : 3000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "07.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "05.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "05.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 1#2
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("3000.00"));

        /**
         * préparation test 2#1)
         * 
         * <pre>
         * Nouveau droit      [01.2013             ->            12.2013]
         * Ancien droit            [03.2013      ->      07.2013]
         * Prestations dues        [03.2013      ->      07.2013] (1000.- / mois)
         * Résultat attendu        [03.2013      ->      07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 5 mois x 1000.- : 5000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "03.2013", "07.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "03.2013", "07.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 2#1
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("5000.00"));

        /**
         * préparation test 2#2)
         * 
         * <pre>
         * Nouveau droit           [03.2013      ->      07.2013]
         * Ancien droit       [01.2013             ->            12.2013]
         * Prestations dues   [01.2013             ->            12.2013] (1000.- / mois)
         * Résultat attendu        [03.2013      ->      07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 5 mois x 1000.- : 5000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "03.2013", "07.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "01.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 2#2
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("5000.00"));

        /**
         * préparation test 3#1)
         * 
         * <pre>
         * Nouveau droit      [01.2013      ->      07.2013]
         * Ancien droit       [01.2013             ->            12.2013]
         * Prestations dues   [01.2013             ->            12.2013] (1000.- / mois)
         * Résultat attendu   [01.2013      ->      07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 7 mois x 1000.- : 7000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "07.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "01.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 3#1
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("7000.00"));

        /**
         * préparation test 3#2)
         * 
         * <pre>
         * Nouveau droit      [01.2013             ->            12.2013]
         * Ancien droit       [01.2013      ->      07.2013]
         * Prestations dues   [01.2013      ->      07.2013] (1000.- / mois)
         * Résultat attendu   [01.2013      ->      07.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 7 mois x 1000.- : 7000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "01.2013", "07.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "01.2013", "07.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 3#2
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("7000.00"));

        /**
         * préparation test 3#3)
         * 
         * <pre>
         * Nouveau droit      [01.2013             ->            12.2013]
         * Ancien droit                    [05.2013      ->      12.2013]
         * Prestations dues                [05.2013      ->      12.2013] (1000.- / mois)
         * Résultat attendu                [05.2013      ->      12.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 8 mois x 1000.- : 8000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "05.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "05.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 3#3
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("8000.00"));

        /**
         * préparation test 3#4)
         * 
         * <pre>
         * Nouveau droit                   [05.2013      ->      12.2013]
         * Ancien droit       [01.2013             ->            12.2013]
         * Prestations dues   [01.2013             ->            12.2013] (1000.- / mois)
         * Résultat attendu                [05.2013      ->      12.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 8 mois x 1000.- : 8000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "05.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "01.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 3#4
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("8000.00"));

        /**
         * préparation test 4)
         * 
         * <pre>
         * Nouveau droit      [01.2013             ->            12.2013]
         * Ancien droit       [01.2013             ->            12.2013]
         * Prestations dues   [01.2013             ->            12.2013] (1000.- / mois)
         * Résultat attendu   [01.2013             ->            12.2013]
         *                    -----------------------------------------> temps
         * Montant attendu : 12 mois x 1000.- : 12000.-
         * </pre>
         */
        rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l);

        prestationDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(1l, 2l, "01.2013", "12.2013",
                new BigDecimal("1000.00"));
        rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l, "01.2013", "12.2013",
                CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // test 5
        result = RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013");
        checkNombreRentesVerseesATort(result, 1);
        checkContientRenteVerseeATort(result, rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("12000.00"));

    }

    @Test
    public void testPeriodeNouveauDroitHorsZoneCalcul() {
        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 1l, "01.2012", "12.2013", new BigDecimal("1000.0"));
        RERentesPourCalculRenteVerseeATort renteAncienDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(1l, "01.2013", "", CodePrestation.getCodePrestation(10), 1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        RERentesPourCalculRenteVerseeATort renteNouveauDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(2l, "01.2014", "", CodePrestation.getCodePrestation(10), 1l);

        tiers1.setRentesAncienDroit(Arrays.asList(renteAncienDroit1));
        tiers1.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1));

        Assert.assertTrue(RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013").isEmpty());
    }

    /**
     * <p>
     * Différents tests avec un nouveau droit ne chevauchant pas l'ancien, le calcul doit retourner null :
     * </p>
     * <p>
     * 1) Avec une seule rente dans les deux droits, et une seule prestation due sur l'ancien droit
     * 
     * <pre>
     * Nouveau droit                       [---------------]
     * Ancien droit       [---------------]
     * Prestations dues   [---------------]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * </p>
     * <p>
     * 2) Ancien droit avec un trou
     * 
     * <pre>
     * Nouveau droit                       [---------------]
     * Ancien droit       [---------------]                 [---------------]
     * Prestations dues   [---------------]                 [---------------]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * </p>
     */
    @Test
    public void testSansChevauchementDeDroit() {

        /**
         * préparation test 1)
         * 
         * <pre>
         * Nouveau droit                          [04.2013 -> ...]
         * Ancien droit       [01.2013 -> 03.2013]
         * Prestations dues   [01.2013 -> 03.2013]
         * Résultat attendu   rien (null)
         *                    -----------------------------------------> temps
         * </pre>
         */
        REPrestationDuePourCalculRenteVerseeATort pDue1 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(
                1l, 1l, "01.2013", "03.2013", new BigDecimal("100.00"));
        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(1l,
                "01.2013", "03.2013", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(pDue1)));

        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(2l,
                "04.2013", "", CodePrestation.getCodePrestation(10), 1l);

        tiers1.setRentesAncienDroit(Arrays.asList(rente1));
        tiers1.setRentesNouveauDroit(Arrays.asList(rente2));

        // test 1)
        Assert.assertTrue(RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013").isEmpty());

        /**
         * préparation test 2)
         * 
         * <pre>
         * Nouveau droit                          [04.2013 -> 05.2013]
         * Ancien droit       [01.2013 -> 03.2013]                    [06.2013 -> ...]
         * Prestations dues   [01.2013 -> 03.2013]                    [06.2013 -> ...]
         * Résultat attendu   rien (null)
         *                    -----------------------------------------> temps
         * </pre>
         */
        rente2.setDateFinDroit("05.2013");
        REPrestationDuePourCalculRenteVerseeATort pDue2 = REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(
                2l, 3l, "06.2013", "", new BigDecimal("110.00"));
        RERentesPourCalculRenteVerseeATort rente3 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(3l,
                "06.2013", "", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(pDue2)));
        tiers1.setRentesAncienDroit(Arrays.asList(rente1, rente3));

        // test 2)
        Assert.assertTrue(RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "12.2013").isEmpty());
    }

    @Test
    public void testSimulationRechargementDepuisLaBaseApresDiminutionDesRentesAncienDroit() {

        /**
         * <pre>
         * Nouveau droit                          [05.2013             ->             12.2013]
         * Ancien droit       [01.2013 -> 04.2013] - - - - - - - - - - - - - - - ] <- la rente allait jusqu'ici au moment du 1er calcul
         * Prestations dues   [01.2013       ->       06.2013][07.2013 -> 08.2013]
         *                            (1000.- / mois)            (600.- / mois)
         *                    -----------------------------------------> temps
         * Résultat attendu                       [05.2013       ->       08.2013]
         * Montant attendu : 2 mois x 1000.- : 2000.-
         *                   2 mois x 600.-  : 1200.-
         *                   total           : 3200.-
         * </pre>
         */

        RERentesPourCalculRenteVerseeATort rente1 = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(1l,
                "05.2013", "12.2013", CodePrestation.getCodePrestation(10), 1l);

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 2l, "01.2013", "06.2013", new BigDecimal("1000.00"));
        REPrestationDuePourCalculRenteVerseeATort prestationDue2 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(2l, 2l, "07.2013", "08.2013", new BigDecimal("600.00"));
        RERentesPourCalculRenteVerseeATort rente2 = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(2l,
                "01.2013", "04.2013", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1, prestationDue2)));

        RERenteVerseeATortWrapper renteVerseeATortWrapper = new RERenteVerseeATortWrapper(1l, 1l, 1l, "a", "a",
                new NumeroSecuriteSociale("756.1234.5678.97"), "", "", "", "", rente1, rente2, new Periode("05.2013",
                        "08.2013"), TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);

        tiers1.setRentesNouveauDroit(Arrays.asList(rente1));
        tiers1.setRentesAncienDroit(Arrays.asList(rente2));

        // on simule ici la période contenue dans la table rente versée à tort
        REDetailCalculRenteVerseeATort result = RECalculRentesVerseesATort
                .chargerDetailRenteVerseeATort(renteVerseeATortWrapper);

        Assert.assertNotNull("Le resultat ne doit pas être null", result);
        checkContientRenteVerseeATort(Arrays.asList(result), rente1.getIdRenteAccordee(), rente2.getIdRenteAccordee(),
                wrapper.getIdDemandeRente(), new BigDecimal("3200.00"));
    }

    @Test
    public void testTrouDansPeriodeMaisGenresRenteDifferent() {

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 1l, "01.2013", "12.2013", new BigDecimal("1000.0"));
        RERentesPourCalculRenteVerseeATort renteAncienDroit = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(
                1l, "01.2013", "", CodePrestation.getCodePrestation(10), 1l,
                new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        RERentesPourCalculRenteVerseeATort renteNouveauDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(2l, "10.2013", "11.2013", CodePrestation.getCodePrestation(97), 1l);
        RERentesPourCalculRenteVerseeATort renteNouveauDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteNouveauDroit(3l, "01.2014", "", CodePrestation.getCodePrestation(97), 1l);

        tiers1.setRentesAncienDroit(Arrays.asList(renteAncienDroit));
        tiers1.setRentesNouveauDroit(Arrays.asList(renteNouveauDroit1, renteNouveauDroit2));

        Assert.assertTrue(RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "01.2014").isEmpty());

    }

    @Test
    public void testAvecDeuxRenteComplementairesPourEnfant() {
        /*
         * Lorsqu'il y a deux rentes pour enfant, on ne doit calculer les rentes versées à tort que sur les rentes
         * découlant du même droit que celle de la demande initiale.
         * Si par exemple l'enfant touche une rente complémentaire liée à la rente principale du père, on ne prend en
         * compte que les rentes liées à une rente principale du père pour calculer les rentes versées à tort.
         */

        REPrestationDuePourCalculRenteVerseeATort prestationDue1 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(1l, 1l, "01.2010", "", new BigDecimal(1000.0));
        RERentesPourCalculRenteVerseeATort renteComplementaireAncienDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(1l, "01.2010", "", CodePrestation.CODE_55, 1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue1)));

        REPrestationDuePourCalculRenteVerseeATort prestationDue2 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(2l, 1l, "01.2010", "", new BigDecimal(1001.0));
        RERentesPourCalculRenteVerseeATort renteComplementaireNouveauDroit1 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(2l, "01.2010", "", CodePrestation.CODE_55, 1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue2)));

        REPrestationDuePourCalculRenteVerseeATort prestationDue3 = REPrestationDuePourCalculRenteVerseeATort
                .creerPrestationDue(3l, 1l, "01.2010", "", new BigDecimal(333.0));
        RERentesPourCalculRenteVerseeATort renteComplementaireNouveauDroit2 = RERentesPourCalculRenteVerseeATort
                .creerRenteAncienDroit(3l, "01.2010", "", CodePrestation.CODE_14, 1l,
                        new HashSet<REPrestationDuePourCalculRenteVerseeATort>(Arrays.asList(prestationDue3)));

        tiers1.setRentesAncienDroit(Arrays.asList(renteComplementaireAncienDroit1));
        tiers1.setRentesNouveauDroit(Arrays.asList(renteComplementaireNouveauDroit1, renteComplementaireNouveauDroit2));

        checkNombreRentesVerseesATort(RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "01.2011"), 1);
    }

    @Test
    public void testValeurEntreeNull() {
        try {
            RECalculRentesVerseesATort.calculerRentesVerseesATort(null, "");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
        try {
            wrapper.setIdDemandeRente(null);
            RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, "");
            Assert.fail();
        } catch (Exception ex) {
            // ok
        }
    }
}
