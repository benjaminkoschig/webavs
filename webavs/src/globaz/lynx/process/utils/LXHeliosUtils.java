package globaz.lynx.process.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.lynx.service.helios.LXHeliosService;

public class LXHeliosUtils {

    private static LXHeliosUtils instance;

    public static LXHeliosUtils getInstance() {
        if (instance == null) {
            instance = new LXHeliosUtils();
        }
        return instance;
    }

    private CGExerciceComptable exerciceComptable = null;

    /**
     * Utilitaire pour contrôler le type d'un compte. <br/>
     * Doit être réinitialiser si l'exercice comptable change !
     */
    private LXHeliosUtils() {
    }

    /**
     * Le compte de comptabilité générale est-il passif ?
     * 
     * @param session
     * @param transaction
     * @param idMandat
     * @param forDate
     * @param idCompte
     * @return
     * @throws Exception
     */
    public boolean isComptePassif(BSession session, BTransaction transaction, String idMandat, String forDate,
            String idCompte) throws Exception {
        if (exerciceComptable == null) {
            exerciceComptable = LXHeliosService.getExerciceComptable(session, idMandat, forDate);
        }

        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(session);

        manager.setForIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
        manager.setForIdCompte(idCompte);

        manager.find(transaction);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(session.getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdGenre().equals(CGCompte.CS_GENRE_PASSIF);
    }

}
