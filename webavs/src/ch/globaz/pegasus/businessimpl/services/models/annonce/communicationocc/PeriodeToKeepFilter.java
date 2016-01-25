package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;

public class PeriodeToKeepFilter {

    public static List<PeriodeOcc> filtrePeriodeToKeep(List<PeriodeOcc> periodesNew, List<PeriodeOcc> periodesOld) {
        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = groupPeriode(periodesNew, periodesOld);
        return removePeriode(map);
    }

    static TreeMap<PeriodeOcc, List<PeriodeOcc>> groupPeriode(List<PeriodeOcc> periodesNew, List<PeriodeOcc> periodesOld) {
        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = new TreeMap<PeriodeOcc, List<PeriodeOcc>>();
        for (PeriodeOcc periodeNew : periodesNew) {
            if (!map.containsKey(periodeNew)) {
                map.put(periodeNew, new ArrayList<PeriodeOcc>());
            } else {
                throw new IllegalArgumentException("Periode déjas existante" + periodeNew);
            }
            for (PeriodeOcc periodeOld : periodesOld) {
                ComparaisonDePeriode comparaisonDePeriode = periodeNew.comparerChevauchementMois(periodeOld);
                if (lesPeriodesSeChevauchent(comparaisonDePeriode)) {
                    map.get(periodeNew).add(periodeOld);
                }
            }
        }

        return map;
    }

    static List<PeriodeOcc> removePeriode(TreeMap<PeriodeOcc, List<PeriodeOcc>> map) {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        PeriodeOcc firstPeriodeKeep = null;
        for (Entry<PeriodeOcc, List<PeriodeOcc>> entry : map.entrySet()) {
            PeriodeOcc periode = entry.getKey();
            if (mustKeepPeriode(periode, entry.getValue()) && firstPeriodeKeep == null) {
                firstPeriodeKeep = periode;
                periodes.add(periode);
            } else if (firstPeriodeKeep != null && !periode.isRefusPeriodeClose()) {
                periodes.add(periode);
            }
        }
        return periodes;
    }

    private static boolean mustKeepPeriode(PeriodeOcc periodeToKeep, List<PeriodeOcc> periodesOld) {
        boolean mustKeepPeriode = false;
        for (PeriodeOcc periodeAncienne : periodesOld) {
            mustKeepPeriode = periodeToKeep.mustGeneratePeriode(periodeAncienne);
            if (mustKeepPeriode) {
                break;
            }
        }
        return mustKeepPeriode;
    }

    private static boolean lesPeriodesSeChevauchent(ComparaisonDePeriode comparaisonDePeriode) {
        return comparaisonDePeriode.equals(ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT);
    }
}
