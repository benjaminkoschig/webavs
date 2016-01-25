package globaz.lynx.service.helios;

import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;

public class LXHeliosService {

    /**
     * Retrouve le compte de comptabilité générale.
     * 
     * @param session
     * @param idMandat
     * @param forDate
     * @param idCompte
     * @return
     * @throws Exception
     */
    public static CGPlanComptableViewBean getCompte(BSession session, String idMandat, String forDate, String idCompte)
            throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(session);

        manager.setForIdExerciceComptable(getExerciceComptable(session, idMandat, forDate).getIdExerciceComptable());
        manager.setForIdCompte(idCompte);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(session.getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return (CGPlanComptableViewBean) manager.getFirstEntity();
    }

    /**
     * Return l'exercice comptable correspondant pour la date et le mandat sélectionné.
     * 
     * @param session
     * @param forDate
     * @return
     * @throws Exception
     */
    public static CGExerciceComptable getExerciceComptable(BSession session, String idMandat, String forDate)
            throws Exception {
        CGExerciceComptableManager manager = new CGExerciceComptableManager();
        manager.setSession(session);

        manager.setForIdMandat(idMandat);
        manager.setBetweenDateDebutDateFin(forDate);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(session.getLabel("GLOBAL_EXERCICE_INEXISTANT"));
        }

        return (CGExerciceComptable) manager.getFirstEntity();
    }

    /**
     * Return l'id externe d'un compta de comptabilité générale. Nécessaire pour la mise en compte lors de la
     * comptabilisation.
     * 
     * @param session
     * @param idExerciceComptable
     * @param idCompte
     * @return
     * @throws Exception
     */
    public static String getIdExterneCompte(BSession session, String idExerciceComptable, String idCompte)
            throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(session);

        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForIdCompte(idCompte);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(session.getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdExterne();
    }

    /**
     * Return la periode comptable d'une exercice comptable pour la date passée en paramètre.
     * 
     * @param session
     * @param idExerciceComptable
     * @param forDate
     * @return
     * @throws Exception
     */
    public static CGPeriodeComptable getPeriode(BSession session, String idExerciceComptable, String forDate)
            throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(session);

        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForDateInPeriode(forDate);
        manager.setForPeriodeOuverte(true);

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(session.getLabel("GLOBAL_PERIODE_INEXISTANT"));
        }

        return (CGPeriodeComptable) manager.getFirstEntity();
    }

}
