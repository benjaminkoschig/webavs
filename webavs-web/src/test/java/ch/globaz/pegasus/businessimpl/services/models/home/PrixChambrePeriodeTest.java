package ch.globaz.pegasus.businessimpl.services.models.home;

import static org.fest.assertions.api.Assertions.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.home.MontantPeriodePrixChambre.TypeMontant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PrixChambrePeriodeTest {

    final String _011999 = "01.1999";
    final String _012013 = "01.2013";
    final String _012014 = "01.2014";
    final String _012015 = "01.2015";

    @Test
    public void testWithMontantPlafondFirst() {

        List<MontantPeriodePrixChambre> variablesMetiers = Lists.newArrayList();
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("200", "01.1998", "12.2012"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("204", "01.2013", "12.2013"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("206", "01.2014", "12.2014"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("208", "01.2015", ""));

        List<MontantPeriodePrixChambre> prixHomes = Lists.newArrayList();
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("193", "01.1999", "12.2013"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("198", "01.2014", "04.2014"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("208", "05.2014", "12.2014"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("218", "01.2015", ""));

        // fusion des périodes
        Map<String, List<MontantPeriodePrixChambre>> periodes = fusionneAndMap(prixHomes, variablesMetiers);
        // 8 périodes fusionnées, 012014 et 01.2015 avec deux montants
        assertThat(periodes).isNotEmpty().hasSize(6);
        // 2014 et 2015 avec deux éléments
        assertThat(periodes.get(_012014)).isNotEmpty().hasSize(2);
        assertThat(periodes.get(_012015)).isNotEmpty().hasSize(2);

        printMapContent(periodes);

        // fusion des périodes avec montants reprise pour chaque périodes
        List<MontantsPeriode> periodesWithMontantsFusionnes = fusionneAndFinaliseDates(periodes);
        assertThat(periodesWithMontantsFusionnes).isNotEmpty().hasSize(6);

        // check que toutes les spériodes, sauf la premiere on deux valeurs

        for (int cpt = 0; cpt < periodesWithMontantsFusionnes.size(); cpt++) {
            MontantsPeriode montants = periodesWithMontantsFusionnes.get(cpt);

            if (cpt != 0) {
                assertThat(montants.getMontantPlafond()).isNotNull();
                assertThat(montants.getPrixChambre()).isNotNull();

            }
        }

        printListContent(periodesWithMontantsFusionnes);

        // finalisation périodes de fin
        List<MontantsPeriode> periodesFinalises = finaliseFirstPeriod(periodesWithMontantsFusionnes);

        assertThat(periodesFinalises).hasSize(5); // supression d'une période dans le case d'une variable smétiers en
                                                  // début de période
        assertThat(periodesFinalises.get(0).getDateDebut().equals(_011999));

        printListContent(periodesFinalises);

    }

    @Test
    public void testWithTaxesJournalieresEquals() {

        List<MontantPeriodePrixChambre> variablesMetiers = Lists.newArrayList();
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("200", "01.1999", "12.2012"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("204", "01.2013", "12.2013"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("206", "01.2014", "12.2014"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("208", "01.2015", ""));

        List<MontantPeriodePrixChambre> prixHomes = Lists.newArrayList();
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("193", "01.1999", "12.2013"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("198", "01.2014", "04.2014"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("208", "05.2014", "12.2014"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixJournalier("218", "01.2015", ""));

        // fusion des périodes
        Map<String, List<MontantPeriodePrixChambre>> periodes = fusionneAndMap(prixHomes, variablesMetiers);
        // 5 périodes fusionnées, 01.1999, 012014 et 01.2015 avec deux montants
        assertThat(periodes).isNotEmpty().hasSize(5);
        // 2014 et 2015 avec deux éléments
        assertThat(periodes.get(_011999)).isNotEmpty().hasSize(2);
        assertThat(periodes.get(_012014)).isNotEmpty().hasSize(2);
        assertThat(periodes.get(_012015)).isNotEmpty().hasSize(2);

        printMapContent(periodes);

        // fusion des périodes avec montants reprise pour chaque périodes
        List<MontantsPeriode> periodesWithMontantsFusionnes = fusionneAndFinaliseDates(periodes);
        assertThat(periodesWithMontantsFusionnes).isNotEmpty().hasSize(5);

        // check que toutes les spériodes, sauf la premiere on deux valeurs

        for (int cpt = 0; cpt < periodesWithMontantsFusionnes.size(); cpt++) {
            MontantsPeriode montants = periodesWithMontantsFusionnes.get(cpt);

            if (cpt != 0) {
                assertThat(montants.getMontantPlafond()).isNotNull();
                assertThat(montants.getPrixChambre()).isNotNull();

            }
        }

        printListContent(periodesWithMontantsFusionnes);

        // finalisation périodes de fin
        List<MontantsPeriode> periodesFinalises = finaliseFirstPeriod(periodesWithMontantsFusionnes);

        assertThat(periodesFinalises).hasSize(5);
        assertThat(periodesFinalises.get(0).getDateDebut().equals(_011999));

        printListContent(periodesFinalises);

    }

    @Test
    public void testCasTestStandard() {

        List<MontantPeriodePrixChambre> variablesMetiers = Lists.newArrayList();
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixJournalier("118", "01.2013", "12.2014"));
        variablesMetiers.add(MontantPeriodePrixChambre.forPrixJournalier("125", "01.2015", ""));

        List<MontantPeriodePrixChambre> prixHomes = Lists.newArrayList();
        prixHomes.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("116", "01.2013", "12.2014"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("120", "01.2015", "12.2015"));
        prixHomes.add(MontantPeriodePrixChambre.forPrixPlafondAdmis("128", "01.2016", ""));

        // fusion des périodes
        Map<String, List<MontantPeriodePrixChambre>> periodes = fusionneAndMap(prixHomes, variablesMetiers);
        // 1 périodes fusionnées, 01.2013
        assertThat(periodes).isNotEmpty().hasSize(3);
        // 2013 et 2015 avec deux éléments
        assertThat(periodes.get(_012013)).isNotEmpty().hasSize(2);
        assertThat(periodes.get(_012015)).isNotEmpty().hasSize(2);

        printMapContent(periodes);

        // fusion des périodes avec montants reprise pour chaque périodes
        List<MontantsPeriode> periodesWithMontantsFusionnes = fusionneAndFinaliseDates(periodes);
        assertThat(periodesWithMontantsFusionnes).isNotEmpty().hasSize(3);

        // check que toutes les spériodes, sauf la premiere on deux valeurs

        for (int cpt = 0; cpt < periodesWithMontantsFusionnes.size(); cpt++) {
            MontantsPeriode montants = periodesWithMontantsFusionnes.get(cpt);

            if (cpt != 0) {
                assertThat(montants.getMontantPlafond()).isNotNull();
                assertThat(montants.getPrixChambre()).isNotNull();

            }
        }

        printListContent(periodesWithMontantsFusionnes);

        // finalisation périodes de fin
        List<MontantsPeriode> periodesFinalises = finaliseFirstPeriod(periodesWithMontantsFusionnes);

        assertThat(periodesFinalises).hasSize(3);
        assertThat(periodesFinalises.get(0).getDateDebut().equals(_012013));

        printListContent(periodesFinalises);

    }

    private List<MontantsPeriode> finaliseFirstPeriod(List<MontantsPeriode> periodesFinalises) {

        // récupération de la premiere periode
        MontantsPeriode montantsFirstPeriodes = periodesFinalises.get(0);

        MontantPeriodePrixChambre montantPlafond = montantsFirstPeriodes.getMontantPlafond();
        MontantPeriodePrixChambre montantPrixChambre = montantsFirstPeriodes.getPrixChambre();

        // si le prix chambres null, on le supprime d ela liste
        if (null == montantPrixChambre) {
            periodesFinalises.remove(0);
        } else if (null == montantPlafond) {
            // on doit setter la valeur montantPlafond
            MontantsPeriode next = periodesFinalises.get(1);

            montantsFirstPeriodes.addMontant(next.getMontantPlafond());
        }

        return periodesFinalises;

    }

    private void printMapContent(Map<String, List<MontantPeriodePrixChambre>> periodes) {
        System.out.println("----------------------------------------------------------------");
        for (String date : periodes.keySet()) {
            // liste des montant pour chaque date
            List<MontantPeriodePrixChambre> montantForDate = periodes.get(date);

            System.out.println(date + " : ");
            for (MontantPeriodePrixChambre montant : montantForDate) {
                System.out.println("DebutPeriode: " + montant.getDateDebutPeriode() + ",type: " + montant.getType()
                        + ", montant: " + montant.getMontant());
            }
            System.out.println("...");
        }
    }

    private void printListContent(List<MontantsPeriode> periodes) {
        System.out.println("----------------------------------------------------------------");
        for (MontantsPeriode montantPeriode : periodes) {
            System.out.println(montantPeriode);

        }
    }

    private Map<String, List<MontantPeriodePrixChambre>> fusionneAndMap(List<MontantPeriodePrixChambre> prixChambres,
            List<MontantPeriodePrixChambre> montantPlafondAdmis) {

        List<MontantPeriodePrixChambre> fusion = Lists.newArrayList(prixChambres);
        fusion.addAll(montantPlafondAdmis);

        assertThat(fusion).isNotEmpty().hasSize(prixChambres.size() + montantPlafondAdmis.size());

        Comparator<String> comparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                // les années
                String year1 = o1.split("\\.")[1];
                String year2 = o2.split("\\.")[1];

                int res = year1.compareTo(year2);

                if (res == 0) {
                    String jour1 = o1.split("\\.")[0];
                    String jour2 = o2.split("\\.")[0];

                    return jour1.compareTo(jour2);
                } else {
                    return res;
                }
            }
        };

        Map<String, List<MontantPeriodePrixChambre>> periodes = Maps.newTreeMap(comparator);

        for (MontantPeriodePrixChambre montant : fusion) {
            // Si clé présente ajout à la liste
            if (periodes.containsKey(montant.getDateDebutPeriode())) {
                periodes.get(montant.getDateDebutPeriode()).add(montant);
            } else {
                // sinion in instancie une nouvelle liste pour la clé
                List<MontantPeriodePrixChambre> listForKey = Lists.newArrayList();
                listForKey.add(montant);
                periodes.put(montant.getDateDebutPeriode(), listForKey);
            }
        }

        // Map<String, List<MontantPeriodePrixChambre>> periodesApresFinalisationPeriodes =
        // fusionneAndFinaliseDates(periodes);

        return periodes;
    }

    private List<MontantsPeriode> fusionneAndFinaliseDates(
            Map<String, List<MontantPeriodePrixChambre>> montantsPeriodesPrixChambres) {
        // plaffonement de la date de début à retourner, cela doit être celle du premier prix de la liste

        List<MontantsPeriode> periodesFinalises = Lists.newArrayList();
        // List<MontantPeriodePrixChambre> montantsPourPeriodeFusionnes = Lists.newArrayList();
        // MontantsPeriode montantsPeriode = new MontantsPeriode();
        // periodes

        // serniere valeur inséré
        MontantPeriodePrixChambre lastMontantPrixChambre = null;
        MontantPeriodePrixChambre lastMontantPlafonAdmis = null;

        for (String dateDebutPeriode : montantsPeriodesPrixChambres.keySet()) {

            // liste des montants pour la périodes
            List<MontantPeriodePrixChambre> montantsPourPeriode = montantsPeriodesPrixChambres.get(dateDebutPeriode);

            // Si un élément pour la période
            if (montantsPourPeriode.size() == 1) {

                // recuperation de l'élélment
                MontantPeriodePrixChambre montant = montantsPourPeriode.get(0);
                MontantPeriodePrixChambre montantAutreTypePeriode = null;

                // Check derneir élément inséré d'un type différent
                if (montant.getType() == TypeMontant.MONTANT_PLAFOND_ADMIS) {
                    montantAutreTypePeriode = lastMontantPrixChambre;
                    lastMontantPlafonAdmis = montant;
                } else {
                    montantAutreTypePeriode = lastMontantPlafonAdmis;
                    lastMontantPrixChambre = montant;
                }

                // ajout des montants
                MontantsPeriode montantsPeriode = new MontantsPeriode();
                montantsPeriode.addMontant(montant, montant.getDateDebutPeriode(), montant.getDateFinPeriode());
                if (null != montantAutreTypePeriode) {
                    montantsPeriode.addMontant(montantAutreTypePeriode);
                }
                periodesFinalises.add(montantsPeriode);

            } else if (montantsPourPeriode.size() == 2) {
                MontantsPeriode montantsPeriode = new MontantsPeriode();

                MontantPeriodePrixChambre montant = montantsPourPeriode.get(0);

                switch (montant.getType()) {
                    case MONTANT_PLAFOND_ADMIS:
                        lastMontantPlafonAdmis = montant;
                        break;

                    case PRIX_JOURNALIER:
                        lastMontantPrixChambre = montant;
                        break;

                }

                montantsPeriode.addMontant(montant, montant.getDateDebutPeriode(), montant.getDateFinPeriode());

                MontantPeriodePrixChambre montant2 = montantsPourPeriode.get(1);

                switch (montant2.getType()) {
                    case MONTANT_PLAFOND_ADMIS:
                        lastMontantPlafonAdmis = montant2;
                        break;

                    case PRIX_JOURNALIER:
                        lastMontantPrixChambre = montant2;
                        break;

                }

                montantsPeriode.addMontant(montant2);
                periodesFinalises.add(montantsPeriode);

            }
        }
        return periodesFinalises;

    }
}
