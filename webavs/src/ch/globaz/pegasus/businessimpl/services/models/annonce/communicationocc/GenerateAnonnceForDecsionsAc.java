package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;

class GenerateAnonnceForDecsionsAc {

    private List<PeriodeOcc> generatePeriodeToSave(List<PeriodeOcc> periodesNouvelle, List<PeriodeOcc> periodesAncienne)
            throws DecisionException {
        PeriodeMerger spliter = new PeriodeMerger();
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = spliter.splitPeriode(periodesNouvelle);
        List<PeriodeOcc> periodeToFilter = new ArrayList<PeriodeOcc>(mapPeriode.keySet());
        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodeToFilter, periodesAncienne);
        return periodes;
    }

    private List<PeriodeOcc> generatePeriodeToSave(List<PeriodeOcc> periodesNouvelle) throws DecisionException {

        PeriodeMerger spliter = new PeriodeMerger();
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = spliter.splitPeriode(periodesNouvelle);
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        for (PeriodeOcc periode : mapPeriode.keySet()) {
            if (!periode.isRefus()) {
                periodes.add(periode);
            }
        }
        return periodes;
    }

    private List<SimpleCommunicationOCC> generateAnonnceOcc(List<PeriodeOcc> periodes) {

        List<SimpleCommunicationOCC> communiations = new ArrayList<SimpleCommunicationOCC>();
        for (PeriodeOcc periodeOcc : periodes) {
            String idDonneesPersonnellesRequerant = MembreFamilleLoader.resolveIdDonneePersonnelRequerant(periodeOcc
                    .getMembresFamille());
            for (PlanDeCalculWitMembreFamille membreFamille : periodeOcc.getMembresFamille()) {
                communiations.add(this.genereCommunicationOCCValidation(periodeOcc, membreFamille,
                        idDonneesPersonnellesRequerant));
            }
        }
        return communiations;
    }

    public List<SimpleCommunicationOCC> genereCommunicationOCCValidation(List<PeriodeOcc> periodesNouvelle,
            List<PeriodeOcc> periodesAncienne) throws DecisionException {

        List<PeriodeOcc> periodesToSave = null;
        if (periodesAncienne.isEmpty()) {
            periodesToSave = generatePeriodeToSave(periodesNouvelle);
        } else {
            periodesToSave = generatePeriodeToSave(periodesNouvelle, periodesAncienne);
        }

        List<SimpleCommunicationOCC> communications = generateAnonnceOcc(periodesToSave);
        return communications;
    }

    private SimpleCommunicationOCC genereCommunicationOCCValidation(PeriodeOcc periodeOcc,
            PlanDeCalculWitMembreFamille membreFamille, String idDonneesPersonnellesRequerant) {

        SimpleCommunicationOCC anonnceOcc = new SimpleCommunicationOCC();

        anonnceOcc.setIdVersionDroit(periodeOcc.getIdVersionDroit());
        anonnceOcc.setDateRapport(JadeDateUtil.getFormattedDate(new Date()));
        anonnceOcc.setDateEffet(JadeDateUtil.convertDateMonthYear(periodeOcc.getDateDebut()));
        anonnceOcc.setDateFinEffet(JadeDateUtil.convertDateMonthYear(periodeOcc.getDateFin()));

        anonnceOcc.setIdTiers(membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers()
                .getIdTiers());
        anonnceOcc.setIdTiersRequerant(periodeOcc.getIdTiersRequerant());

        anonnceOcc.setMotif(periodeOcc.getIntroduction());

        anonnceOcc.setIdLocalite(membreFamille.getSimpleDonneesPersonnelles().getIdDernierDomicileLegale());
        anonnceOcc.setIdDonneesPersonnelles(membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille()
                .getIdDonneesPersonnelles());
        anonnceOcc.setIdDonneesPersonnellesRequerant(idDonneesPersonnellesRequerant);

        anonnceOcc.setEtatPC(periodeOcc.getStatusPca().getValue());

        return anonnceOcc;

    }
}
