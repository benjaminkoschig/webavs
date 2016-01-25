package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;

class PeriodeOcc extends Periode {

    private String idVersionDroit;
    private PcaStatus statusPca;
    private String introduction;
    private String idTiersRequerant;
    private List<PlanDeCalculWitMembreFamille> membresFamille;
    private RoleMembreFamille roleMembreFamille;
    private static Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
        }
    };

    public PeriodeOcc(String dateDebut, String dateFin, String idVersionDroit, PcaStatus statusPca,
            String introduction, String idTiersRequerant, List<PlanDeCalculWitMembreFamille> membresFamille,
            RoleMembreFamille roleMembreFamille) {
        super(dateDebut, dateFin);

        Checkers.checkNotNull(idVersionDroit, "idVersionDroit");
        Checkers.checkNotNull(statusPca, "statusPca");
        Checkers.checkNotNull(introduction, "introduction");
        Checkers.checkNotNull(idTiersRequerant, "idTiersRequerant");
        Checkers.checkNotNull(membresFamille, "membresFamille");
        Checkers.checkNotEmpty(membresFamille, "membresFamille");

        Checkers.checkNotNull(roleMembreFamille, "roleMembreFamille");

        this.idVersionDroit = idVersionDroit;
        this.statusPca = statusPca;
        this.introduction = introduction;
        this.idTiersRequerant = idTiersRequerant;
        this.membresFamille = membresFamille;
        this.roleMembreFamille = roleMembreFamille;
    }

    public PeriodeOcc(String dateDebut, String dateFin, String idVersionDroit, PcaStatus statusPca,
            List<PlanDeCalculWitMembreFamille> membresFamille, RoleMembreFamille roleMembreFamille) {
        super(dateDebut, dateFin);
        Checkers.checkNotNull(idVersionDroit, "idVersionDroit");
        Checkers.checkNotNull(statusPca, "statusPca");
        Checkers.checkNotNull(membresFamille, "membresFamille");
        Checkers.checkNotEmpty(membresFamille, "membresFamille");
        Checkers.checkNotNull(roleMembreFamille, "roleMembreFamille");
        this.idVersionDroit = idVersionDroit;
        this.statusPca = statusPca;
        this.membresFamille = membresFamille;
        this.roleMembreFamille = roleMembreFamille;
    }

    @Override
    public PeriodeOcc clone() {
        return new PeriodeOcc(getDateDebut(), getDateFin(), idVersionDroit, statusPca, introduction, idTiersRequerant,
                membresFamille, roleMembreFamille);
    }

    public PeriodeOcc newPeriodeByPrcedante(PeriodeOcc periodePrecedante) {
        return new PeriodeOcc(getDateDebut(), periodePrecedante.getDateFin(), idVersionDroit, statusPca, introduction,
                idTiersRequerant, membresFamille, roleMembreFamille);
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public List<PlanDeCalculWitMembreFamille> getMembresFamille() {
        return membresFamille;
    }

    public int getNbPersonneDansCalcul() {
        return membresFamille.size();
    }

    public RoleMembreFamille getRoleMembreFamille() {
        return roleMembreFamille;
    }

    PcaStatus getStatusPca() {
        return statusPca;
    }

    public boolean isRefus() {
        return statusPca.isRefus();
    }

    public boolean isOctroi() {
        return statusPca.isOctroi();
    }

    public boolean isOctroiPartiel() {
        return statusPca.isOctroiPartiel();
    }

    public boolean isStatusChange(final PeriodeOcc periodePrecedante) {
        return isStatusChange(statusPca, periodePrecedante.getStatusPca());
    }

    public boolean isMembreFamilleSame(final PeriodeOcc periodePrecedante) {
        return isMembreFamilleSame(membresFamille, periodePrecedante.getMembresFamille());
    }

    public boolean hasSameSituation(final PeriodeOcc periodePrecedante) {
        return (isStatusChange(periodePrecedante) || !isMembreFamilleSame(periodePrecedante));
    }

    public boolean mustGeneratePeriode(PeriodeOcc periodePrecedante) {
        return (isStatusChange(periodePrecedante) || (!isMembreFamilleSame(periodePrecedante))
                && !(periodePrecedante.isRefus() && isRefus()));
    }

    private static List<String> resolvesIds(List<PlanDeCalculWitMembreFamille> membresFamille) {
        List<String> ids = new ArrayList<String>();
        for (PlanDeCalculWitMembreFamille membreFamille : membresFamille) {
            ids.add(membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille().getIdDroitMembreFamille());
        }
        Collections.sort(ids, comparator);
        return ids;
    }

    static boolean isMembreFamilleSame(List<PlanDeCalculWitMembreFamille> membresFamille1,
            List<PlanDeCalculWitMembreFamille> membresFamille2) {
        List<String> ids1 = resolvesIds(membresFamille1);
        List<String> ids2 = resolvesIds(membresFamille2);
        return ids1.equals(ids2);
    }

    static boolean isStatusChange(final PcaStatus statusCourant, final PcaStatus statuPrecedent) {

        if (!statusCourant.equals(statuPrecedent)) {
            if (statuPrecedent.isRefus()) {
                return statusCourant.isOctroi() ^ statusCourant.isOctroiPartiel();
            } else if (statusCourant.isRefus()) {
                return true;
            }
        }

        return false;
    }

    public boolean isRefusPeriodeClose() {
        return isRefus() && !isDateFinEmpty();
    }

    @Override
    public String toString() {
        return "PeriodeOcc [" + getDateDebut() + " - " + getDateFin() + ", idVersionDroit =" + idVersionDroit
                + ", statusPca=" + statusPca + ", introduction=" + introduction + ", idTiersRequerant="
                + idTiersRequerant + ", roleMembreFamille=" + roleMembreFamille + "]";
    }
}
