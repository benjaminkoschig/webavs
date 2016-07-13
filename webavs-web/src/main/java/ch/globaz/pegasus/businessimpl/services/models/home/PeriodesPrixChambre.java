package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetier;
import ch.globaz.pegasus.businessimpl.services.models.home.MontantPeriodePrixChambre.TypeMontant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PeriodesPrixChambre {

    private List<MontantsPeriode> montantsPeriode = null;
    private boolean withMontantAdmis = Boolean.FALSE;
    private String libelleHome = null;
    private String libelleChambre = null;

    public static PeriodesPrixChambre forPrixChambreOnly(List<PrixChambre> prixChambres) {

        PeriodesPrixChambre p = new PeriodesPrixChambre();
        List<MontantsPeriode> montantPeriodes = new ArrayList<MontantsPeriode>();

        p.libelleChambre = prixChambres.get(0).getTypeChambre().getDesignationTypeChambre();
        p.libelleHome = prixChambres.get(0).getTypeChambre().getHome().getSimpleHome().getNomBatiment();

        for (PrixChambre prixChambre : prixChambres) {
            String dateDebutPeriode = prixChambre.getSimplePrixChambre().getDateDebut();
            String dateFinPeriode = prixChambre.getSimplePrixChambre().getDateFin();

            String prixJounralier = prixChambre.getSimplePrixChambre().getPrixJournalier();
            montantPeriodes.add(new MontantsPeriode(MontantPeriodePrixChambre.forPrixJournalier(prixJounralier,
                    dateDebutPeriode, dateFinPeriode), dateDebutPeriode, dateFinPeriode));
        }

        p.montantsPeriode = montantPeriodes;
        p.withMontantAdmis = Boolean.FALSE;

        return p;
    }

    public static PeriodesPrixChambre forPrixChambrewithMontantPlafond(List<PrixChambre> prixChambres,
            List<VariableMetier> montantsPlafonds) {

        PeriodesPrixChambre p = new PeriodesPrixChambre();
        List<MontantPeriodePrixChambre> montantsPlafondsAdmis = p.mapPlafondsAdmis(montantsPlafonds);
        List<MontantPeriodePrixChambre> montantsJournalier = p.mapPrixChambes(prixChambres);

        Map<String, List<MontantPeriodePrixChambre>> periodesFusionnes = p.fusionneAndMap(montantsJournalier,
                montantsPlafondsAdmis);

        List<MontantsPeriode> listWithMontantAutreType = p.fillMontantEmptyForPeriode(periodesFusionnes);

        List<MontantsPeriode> listFinalise = p.finaliseFirstPeriod(listWithMontantAutreType);

        p.montantsPeriode = listFinalise;

        Collections.reverse(p.montantsPeriode);

        p.libelleChambre = prixChambres.get(0).getTypeChambre().getDesignationTypeChambre();
        p.libelleHome = prixChambres.get(0).getTypeChambre().getHome().getSimpleHome().getNomBatiment();

        p.withMontantAdmis = Boolean.TRUE;

        return p;
    }

    private List<MontantPeriodePrixChambre> mapPlafondsAdmis(List<VariableMetier> montantsPlafonds) {

        List<MontantPeriodePrixChambre> listeEnRetour = new ArrayList<MontantPeriodePrixChambre>();

        for (VariableMetier variable : montantsPlafonds) {
            String dateDebutPeriode = variable.getSimpleVariableMetier().getDateDebut();
            String dateFinPeriode = variable.getSimpleVariableMetier().getDateFin();

            listeEnRetour.add(MontantPeriodePrixChambre.forPrixPlafondAdmis(variable.getSimpleVariableMetier()
                    .getMontant(), dateDebutPeriode, dateFinPeriode));
        }

        return listeEnRetour;
    }

    private List<MontantPeriodePrixChambre> mapPrixChambes(List<PrixChambre> prixChambes) {

        List<MontantPeriodePrixChambre> listeEnRetour = new ArrayList<MontantPeriodePrixChambre>();

        for (PrixChambre prix : prixChambes) {
            String dateDebutPeriode = prix.getSimplePrixChambre().getDateDebut();
            String dateFinPeriode = prix.getSimplePrixChambre().getDateFin();

            listeEnRetour.add(MontantPeriodePrixChambre.forPrixJournalier(prix.getSimplePrixChambre()
                    .getPrixJournalier(), dateDebutPeriode, dateFinPeriode));
        }

        return listeEnRetour;
    }

    private Map<String, List<MontantPeriodePrixChambre>> fusionneAndMap(List<MontantPeriodePrixChambre> prixChambres,
            List<MontantPeriodePrixChambre> montantPlafondAdmis) {

        List<MontantPeriodePrixChambre> fusion = Lists.newArrayList(prixChambres);
        fusion.addAll(montantPlafondAdmis);

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

    private List<MontantsPeriode> fillMontantEmptyForPeriode(
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

            MontantPeriodePrixChambre montantPlafondToPut = next.getMontantPlafond();

            String dateFinEffective = reduitUnMois(montantPlafondToPut.getDateDebutPeriode());
            montantPlafondToPut.setDateFinPeriode(dateFinEffective);

            montantsFirstPeriodes.addMontant(montantPlafondToPut);
            montantsFirstPeriodes.setDateFin(dateFinEffective);
        }

        return periodesFinalises;

    }

    private String reduitUnMois(String dateFinPeriode) {

        return JadeDateUtil.addMonths("01." + dateFinPeriode, -1).substring(3);
    }

}
