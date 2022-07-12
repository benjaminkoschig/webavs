package globaz.corvus.utils;

import ch.globaz.common.util.Dates;
import ch.globaz.hera.business.constantes.ISFPeriode;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;

import java.time.LocalDate;
import java.util.Arrays;

public class REAnnonceUtils {

    private REAnnonceUtils(){
    }

    public static boolean enfantRecueilli(BSession session, RERenteAccordee ra, String idMembreFamille) throws Exception {
        SFPeriodeManager periodeMgr = new SFPeriodeManager();
        periodeMgr.setSession(session);
        periodeMgr.setForIdMembreFamille(idMembreFamille);
        periodeMgr.setForCsTypePeriodeIn(Arrays.asList(ISFPeriode.CS_TYPE_PERIODE_ENFANT_CONJOINT));
        periodeMgr.find(BManager.SIZE_NOLIMIT);
        return periodeMgr.<SFPeriode>toList()
                .stream()
                .anyMatch(periode -> periodesSeChevauchent(periode, ra));
    }
    private static boolean periodesSeChevauchent(SFPeriode periode, RERenteAccordee ra) {
        LocalDate start1 = Dates.toDate(periode.getDateDebut());
        LocalDate end1 = Dates.toDate(periode.getDateFin());
        LocalDate start2 = Dates.firstDayOfMonth(ra.getDateDebutDroit());
        LocalDate end2 = Dates.lastDayOfMonth(ra.getDateFinDroit());
        return (start1 == null || end2 == null || start1.isBefore(end2))
                && (start2 == null || end1 == null || start2.isBefore(end1));
    }
}
