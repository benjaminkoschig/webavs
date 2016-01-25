package ch.globaz.pegasus.businessimpl.utils.dessaisissement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class DessaisissementUtils {

    public static class Fortune {
        public int annee = 0;
        public Calendar dateDebut = Calendar.getInstance();
        public int mois = 0;
        public float montantInitial = 0f;

        public Fortune(Date dateDebut, float montant) {
            super();
            this.dateDebut.setTime(dateDebut);
            annee = this.dateDebut.get(Calendar.YEAR);
            mois = this.dateDebut.get(Calendar.MONTH);
            montantInitial = montant;
        }
    }

    /**
     * @param dateDebut
     *            date de début de la période concernée
     * @param dateDebutCalcul
     *            date du dessaisissement le plus ancien de la liste
     * @param dessaisissements
     *            liste des données de déssaisissement
     * @param amortissements
     *            liste triée par date des variables metier d'amortissement
     * @return
     */
    public static final float calculeAmortissement(Date dateDebut, Date dateDebutCalcul,
            List<Fortune> dessaisissements, TreeMap<Long, String> amortissements) {

        float somme = 0f;

        // calcul des montants
        Calendar calPeriode = Calendar.getInstance();
        calPeriode.setTime(dateDebut);
        Calendar calFortune = Calendar.getInstance();
        calFortune.setTime(dateDebutCalcul);
        float sommeNonPlafonnable = 0;
        for (int annee = calFortune.get(Calendar.YEAR); annee <= calPeriode.get(Calendar.YEAR); annee++) {
            somme = Math.max(somme - DessaisissementUtils.getVariableFrom(amortissements, annee), 0);
            somme += sommeNonPlafonnable;

            // pour tous les dessaisissements
            sommeNonPlafonnable = 0f;
            for (Fortune fortune : dessaisissements) {
                if (fortune.annee == annee) {
                    sommeNonPlafonnable += fortune.montantInitial;
                }

            }
        }
        somme += sommeNonPlafonnable;

        return somme;
    }

    private static float getVariableFrom(TreeMap<Long, String> amortissements, int annee) {
        if (amortissements.size() == 0) {
            return 0;
        }

        // conversion de l'année en epoch
        Date dateAnnee = new Date(annee, 1, 1);
        final long anneeEpoch = dateAnnee.getTime();

        Long index = amortissements.firstKey();
        String result = null;
        for (Long currentAnnee : amortissements.keySet()) {
            if (anneeEpoch < currentAnnee) {
                result = amortissements.get(index);
                if (result == null) {
                    result = "0";
                }
                break;
            } else {
                index = currentAnnee;
            }
        }
        if (result == null) {
            result = amortissements.get(index);
        }
        if (result == null) {
            result = "0";
        }
        return Float.parseFloat(result);
    }

}
