package globaz.corvus.db.rentesverseesatort.wrapper;

import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.RELigneDetailCalculRenteVerseeATort;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public class RECalculRentesVerseesATortConverterTest {

    private void checkContientRenteVerseeATort(Collection<RERenteVerseeATort> result,
            RERenteVerseeATort renteVerseeATortVoulue) {
        StringBuilder message = new StringBuilder();
        boolean contientLaRente = false;

        message.append("Rente versée à tort voulue : ").append(getDescription(renteVerseeATortVoulue));

        message.append("\nRentes versées à tort présentes : ");
        for (RERenteVerseeATort unResultat : result) {
            message.append(getDescription(unResultat));

            if (unResultat.equals(renteVerseeATortVoulue)) {

                if (((renteVerseeATortVoulue.getIdRenteAccordeeNouveauDroit() == null) && (unResultat
                        .getIdRenteAccordeeNouveauDroit() != null))
                        || !renteVerseeATortVoulue.getIdRenteAccordeeNouveauDroit().equals(
                                unResultat.getIdRenteAccordeeNouveauDroit())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getIdRenteAccordeeAncienDroit().equals(
                        unResultat.getIdRenteAccordeeAncienDroit())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getIdDemandeRente().equals(unResultat.getIdDemandeRente())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getMontant().equals(unResultat.getMontant())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getTypeRenteVerseeATort().equals(unResultat.getTypeRenteVerseeATort())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getDateDebut().equals(unResultat.getDateDebut())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getDateFin().equals(unResultat.getDateFin())) {
                    continue;
                }
                if (!renteVerseeATortVoulue.getIdTiers().equals(unResultat.getIdTiers())) {
                    continue;
                }
                contientLaRente = true;
            }
        }

        if (!contientLaRente) {
            Assert.fail(message.toString());
        }
    }

    private RECalculRentesVerseesATortEntity createEntity(String idTiers, String idRenteNouveauDroit,
            String idRenteAncienDroit, String idPrestationDue) {

        RECalculRentesVerseesATortEntity newEntity = new RECalculRentesVerseesATortEntity();
        newEntity.setIdRenteAccordeeNouveauDroit(idRenteNouveauDroit);
        newEntity.setIdRenteAccordeeAncienDroit(idRenteAncienDroit);
        newEntity.setIdPrestationDueAncienDroit(idPrestationDue);
        newEntity.setCodePrestationRenteAccordeeAncienDroit("10");
        newEntity.setCodePrestationRenteAccordeeNouveauDroit("10");
        newEntity.setIdTiersBeneficiaireRenteAccordeeAncienDroit(idTiers);
        newEntity.setIdTiersBeneficiaireRenteAccordeeNouveauDroit(idTiers);
        newEntity.setDateDebutPaiementPrestationVerseeDueAncienDroit("01.2010");
        newEntity.setDateFinPaiementPrestationVerseeDueAncienDroit("11.2010");
        newEntity.setMontantPrestationVerseeDueAncienDroit("10.0");
        newEntity.setNomTiersBeneficiaire("");
        newEntity.setPrenomTiersBeneficiaire("");
        newEntity.setNssTiersBeneficiaire("756.1234.5678.97");

        return newEntity;
    }

    private RERenteVerseeATort creerRenteVerseeATort(Long idRenteVerseeATort, Long idDemandeRente, Long idTiers,
            Long idRenteAccordeeAncienDroit, Long idRenteAccordeeNouveauDroit, BigDecimal montant,
            TypeRenteVerseeATort typeRenteVerseeATort, String dateDebut, String dateFin) {
        RERenteVerseeATort renteVerseeATort = new RERenteVerseeATort();
        renteVerseeATort.setIdRenteVerseeATort(idRenteVerseeATort);
        renteVerseeATort.setIdDemandeRente(idDemandeRente);
        renteVerseeATort.setIdTiers(idTiers);
        renteVerseeATort.setIdRenteAccordeeAncienDroit(idRenteAccordeeAncienDroit);
        renteVerseeATort.setIdRenteAccordeeNouveauDroit(idRenteAccordeeNouveauDroit);
        renteVerseeATort.setMontant(montant);
        renteVerseeATort.setTypeRenteVerseeATort(typeRenteVerseeATort);
        renteVerseeATort.setDateDebut(dateDebut);
        renteVerseeATort.setDateFin(dateFin);
        return renteVerseeATort;
    }

    private String getDescription(RERenteVerseeATort unRenteVerseeATort) {
        StringBuilder message = new StringBuilder();
        message.append("{\nID rente versée à tort : ").append(unRenteVerseeATort.getIdRenteVerseeATort());
        message.append("\nID rente ancien droit : ").append(unRenteVerseeATort.getIdRenteAccordeeAncienDroit());
        message.append("\nID rente nouveau droit : ").append(unRenteVerseeATort.getIdRenteAccordeeNouveauDroit());
        message.append("\nID demande rente : ").append(unRenteVerseeATort.getIdDemandeRente());
        message.append("\nID tiers : ").append(unRenteVerseeATort.getIdTiers());
        message.append("\nmontant : ").append(unRenteVerseeATort.getMontant());
        message.append("\ndate début : ").append(unRenteVerseeATort.getDateDebut());
        message.append("\ndate fin : ").append(unRenteVerseeATort.getDateFin());
        message.append("\n}");

        return message.toString();
    }

    private boolean listContainsOnceRenteWithId(Long idRente, Collection<RERentesPourCalculRenteVerseeATort> list) {
        int count = 0;
        for (RERentesPourCalculRenteVerseeATort uneRente : list) {
            if ((idRente != null) && idRente.equals(uneRente.getIdRenteAccordee())) {
                count++;
            }
        }
        return count == 1;
    }

    private boolean renteHasPrestationsDuesWithIds(RERentesPourCalculRenteVerseeATort rente,
            Collection<Long> idsPrestationDues) {
        if (rente.getPrestationsDues() == null) {
            return false;
        }
        for (REPrestationDuePourCalculRenteVerseeATort unePrestationDue : rente.getPrestationsDues()) {
            if ((unePrestationDue.getIdPrestationDue() != null)
                    && idsPrestationDues.contains(unePrestationDue.getIdPrestationDue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * <pre>
     * Période            [01.2013 -> 06.2013][07.2013 -> 08.2013][09.2013 -> 10.2013]
     * Montant                 10.0 / mois          3.0 / mois           7.0 / mois
     * -----------------------------------------------------------------
     * Résultat attendu   une rente versée à tort avec comme périodes [01.2013 -> 10.2013] et un montant de
     * 6 x 10.0 : 60.-
     * 2 x 3.0  :  6.-
     * 2 x 7.0  : 14.-
     * montant  : 80.-
     * </pre>
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testConvertToEntity() throws Exception {
        List<REDetailCalculRenteVerseeATort> details = new ArrayList<REDetailCalculRenteVerseeATort>();

        details.add(new REDetailCalculRenteVerseeATort(null, 1l, 2l, 3l, Arrays.asList(
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal("10.00"), new Periode("01.2013", "06.2013")),
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal("3.00"), new Periode("07.2013", "08.2013")),
                new RELigneDetailCalculRenteVerseeATort(new BigDecimal("7.00"), new Periode("09.2013", "10.2013"))),
                4l, new NumeroSecuriteSociale("756.1234.5678.97"), "test", "test", "", "", "", "",
                TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT));

        Collection<RERenteVerseeATort> result = RECalculRentesVerseesATortConverter.convertToEntity(details);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        checkContientRenteVerseeATort(
                result,
                creerRenteVerseeATort(null, 3l, 4l, 2l, 1l, new BigDecimal("80.00"),
                        TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT, "01.2013", "10.2013"));
    }

    /**
     * <p>
     * Test de la conversion avec une structure de type deux rente de nouveau droit, 3 rente de l'ancien droit avec au
     * total 6 prestation due répartie entre les anciennes rentes.
     * </p>
     * <p>
     * Lors de la remonté de la requête SQL, les données seront présentées sous cette forme :
     * 
     * <pre>
     * Rente nouveau droit 1 ------ Rente ancien droit 3 ------ Prestation due 6
     * Rente nouveau droit 1 ------ Rente ancien droit 3 ------ Prestation due 7
     * Rente nouveau droit 1 ------ Rente ancien droit 3 ------ Prestation due 8
     * Rente nouveau droit 1 ------ Rente ancien droit 4 ------ Prestation due 9
     * Rente nouveau droit 1 ------ Rente ancien droit 4 ------ Prestation due 10
     * Rente nouveau droit 1 ------ Rente ancien droit 5 ------ Prestation due 11
     * Rente nouveau droit 2 ------ Rente ancien droit 3 ------ Prestation due 6
     * Rente nouveau droit 2 ------ Rente ancien droit 3 ------ Prestation due 7
     * Rente nouveau droit 2 ------ Rente ancien droit 3 ------ Prestation due 8
     * Rente nouveau droit 2 ------ Rente ancien droit 4 ------ Prestation due 9
     * Rente nouveau droit 2 ------ Rente ancien droit 4 ------ Prestation due 10
     * Rente nouveau droit 2 ------ Rente ancien droit 5 ------ Prestation due 11
     * </pre>
     * 
     * Ce qui devrait donner la structure suivante :
     * 
     * <pre>
     * {@link RECalculRentesVerseesATortWrapper}
     *    liste des rentes de l'ancien droit
     *       |-- Rente ancien droit 3 ------ Prestation due 6
     *       |                           |-- Prestation due 7
     *       |                           |-- Prestation due 8
     *       |-- Rente ancien droit 4 ------ Prestation due 9
     *       |                           |-- Prestation due 10
     *       |-- Rente ancien droit 5 ------ Prestation due 11
     *    liste des rentes du nouveau droit
     *       |-- Rente nouveau droit 1
     *       |-- Rente nouveau droit 2
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testConvertToWrapper() throws Exception {

        List<RECalculRentesVerseesATortEntity> entities = new ArrayList<RECalculRentesVerseesATortEntity>();
        RECalculRentesVerseesATortEntity newEntity = createEntity("123456", "1", "3", "6");
        newEntity.setIdDemandeRenteNouveauDroit("1");
        entities.add(newEntity);
        entities.add(createEntity("123456", "1", "3", "7"));
        entities.add(createEntity("123456", "1", "3", "8"));
        entities.add(createEntity("123456", "1", "4", "9"));
        entities.add(createEntity("123456", "1", "4", "10"));
        entities.add(createEntity("123456", "1", "5", "11"));
        entities.add(createEntity("123456", "2", "3", "6"));
        entities.add(createEntity("123456", "2", "3", "7"));
        entities.add(createEntity("123456", "2", "3", "8"));
        entities.add(createEntity("123456", "2", "4", "9"));
        entities.add(createEntity("123546", "2", "4", "10"));
        entities.add(createEntity("123456", "2", "5", "11"));
        entities.add(createEntity("234567", "6", "7", "12"));
        entities.add(createEntity("234567", "6", "7", "13"));
        entities.add(createEntity("234567", "6", "7", "14"));

        RECalculRentesVerseesATortWrapper result = RECalculRentesVerseesATortConverter.convertToWrapper(entities);
        RETiersPourCalculRenteVerseeATortWrapper tiers1 = result.getTiers(123456l);

        Assert.assertNotNull(tiers1);
        Assert.assertEquals(Integer.valueOf(1), result.getIdDemandeRente());

        Assert.assertTrue(listContainsOnceRenteWithId(1l, tiers1.getRentesNouveauDroit()));
        Assert.assertTrue(listContainsOnceRenteWithId(2l, tiers1.getRentesNouveauDroit()));

        Assert.assertTrue(listContainsOnceRenteWithId(3l, tiers1.getRentesAncienDroit()));
        Assert.assertTrue(listContainsOnceRenteWithId(4l, tiers1.getRentesAncienDroit()));
        Assert.assertTrue(listContainsOnceRenteWithId(5l, tiers1.getRentesAncienDroit()));

        Assert.assertTrue(renteHasPrestationsDuesWithIds(tiers1.getRenteAncienDroitSelonId(3l),
                Arrays.asList(6l, 7l, 8l)));
        Assert.assertTrue(renteHasPrestationsDuesWithIds(tiers1.getRenteAncienDroitSelonId(4l), Arrays.asList(9l, 10l)));
        Assert.assertTrue(renteHasPrestationsDuesWithIds(tiers1.getRenteAncienDroitSelonId(5l), Arrays.asList(11l)));
    }
}
