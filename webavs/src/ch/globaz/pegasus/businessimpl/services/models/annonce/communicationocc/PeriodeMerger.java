package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc.ListSortedByDate.DatesStringToCompare;
import ch.globaz.pegasus.businessimpl.utils.RequerantConjoint;

class PeriodeMerger {

    public TreeMap<PeriodeOcc, List<PeriodeOcc>> splitPeriode(List<PeriodeOcc> PeriodesOcc) throws DecisionException {
        ListSortedByDate.sortDateMmsyyyyAsc(PeriodesOcc, new DatesStringToCompare<PeriodeOcc>() {
            @Override
            public String[] getDates(PeriodeOcc val1, PeriodeOcc val2) {
                return new String[] { val1.getDateDebut(), val2.getDateDebut() };
            }
        });

        List<RequerantConjoint<PeriodeOcc>> groupeDecsions = groupePeriodeByDateDebut(PeriodesOcc);
        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = new TreeMap<PeriodeOcc, List<PeriodeOcc>>();
        PeriodeOcc periodePrecedante = null;
        for (RequerantConjoint<PeriodeOcc> periodeRc : groupeDecsions) {
            PeriodeOcc periode = resolveMostFavorable(periodeRc);

            PeriodeOcc periodeKey = periode.clone();

            if (periodePrecedante == null) {
                map.put(periode, new ArrayList<PeriodeOcc>());
            } else if (!periode.hasSameSituation(periodePrecedante)) {
                periodeKey = periodePrecedante.newPeriodeByPrcedante(periode);
                map.put(periodeKey, map.remove(periodePrecedante));
            } else {
                map.put(periodeKey, new ArrayList<PeriodeOcc>());
                periodePrecedante = periodeKey;
            }

            map.get(periodeKey).add(periode);
            periodePrecedante = periodeKey;
        }
        return map;
    }

    PeriodeOcc resolveMostFavorable(RequerantConjoint<PeriodeOcc> periodes) {
        if (periodes.isConjointEmpty()) {
            return periodes.getRequerant();
        } else {
            if (periodes.getRequerant().isOctroi() || periodes.getRequerant().isOctroiPartiel()) {
                return periodes.getRequerant();
            } else if (periodes.getConjoint().isOctroi() || periodes.getConjoint().isOctroiPartiel()) {
                return periodes.getConjoint();
            } else {
                return periodes.getRequerant();
            }
        }
    }

    List<RequerantConjoint<PeriodeOcc>> groupePeriodeByDateDebut(List<PeriodeOcc> periodesOcc) throws DecisionException {
        List<RequerantConjoint<PeriodeOcc>> list = new ArrayList<RequerantConjoint<PeriodeOcc>>();

        Map<String, List<PeriodeOcc>> mapPca = JadeListUtil.groupBy(periodesOcc, new Key<PeriodeOcc>() {
            @Override
            public String exec(PeriodeOcc e) {
                return e.getDateDebut();
            }
        });

        for (Entry<String, List<PeriodeOcc>> entry : mapPca.entrySet()) {
            RequerantConjoint<PeriodeOcc> rc = new RequerantConjoint<PeriodeOcc>();
            List<PeriodeOcc> groupPeriode = entry.getValue();
            if (groupPeriode.size() == 1) {
                rc.setRequerant(groupPeriode.get(0));
            } else if (groupPeriode.size() == 2) {

                RoleMembreFamille rmf1 = groupPeriode.get(0).getRoleMembreFamille();
                RoleMembreFamille rmf2 = groupPeriode.get(1).getRoleMembreFamille();
                // Cas d'un dom2R
                if (rmf1.isRequerant() && rmf2.isRequerant()) {
                    rc.setRequerant(groupPeriode.get(0));
                } else if (rmf1.isRequerant() && rmf2.isConjoint()) {
                    rc.setRequerant(groupPeriode.get(0));
                    rc.setConjoint(groupPeriode.get(1));
                } else if (rmf1.isConjoint() && rmf2.isRequerant()) {
                    rc.setRequerant(groupPeriode.get(1));
                    rc.setConjoint(groupPeriode.get(0));
                } else {
                    throw new DecisionException("Impossible de determiner les membre de famille avec ces periodes: "
                            + groupPeriode.get(0).toString() + ", " + groupPeriode.get(1).toString());
                }

            } else {
                throw new DecisionException("Trop de décisions sont regroupé avec la méme date de début("
                        + entry.getKey() + "). Id version droit AC:" + periodesOcc.get(0).getIdVersionDroit());
            }
            list.add(rc);
        }

        return list;
    }
}
