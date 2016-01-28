package globaz.helios.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGAnalyseBudgetaireViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.servlet.CGActionAnalyseBudgetaire;
import globaz.jade.log.JadeLogger;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 17 mai 04
 * 
 * @author scr
 * 
 */
public class CGAnalyseBudgetaireHelper extends FWHelper {

    /**
     * Constructor for CGAnalyseBudgetaireHelper.
     */
    public CGAnalyseBudgetaireHelper() {
        super();
    }

    private CGSolde createSolde(BSession session, CGAnalyseBudgetaireViewBean viewBean, boolean isForPeriode,
            String idPeriodeComptable) throws Exception {

        // création du solde
        CGSolde solde = new CGSolde();
        solde.setSession(session);
        solde.setIdExerComptable(viewBean.getIdExerciceComptable());
        solde.setIdMandat(viewBean.getIdMandat());
        solde.setIdCompte(viewBean.getIdCompte());
        solde.setEstPeriode(new Boolean(isForPeriode));
        if (isForPeriode) {
            solde.setIdPeriodeComptable(idPeriodeComptable);
        }

        solde.add();
        return solde;
    }

    /**
     * Method doAfficherAnalyseBudgetaire.
     * 
     * Met a jours le viewBean avec les périodes comptables existante et le budget définit si existant. Création du
     * solde associé à la période si inexistant !!!
     * 
     * @param viewBean
     * @param session
     * @return CGAnalyseBudgetaireViewBean
     */
    private CGAnalyseBudgetaireViewBean doAfficherAnalyseBudgetaire(CGAnalyseBudgetaireViewBean viewBean,
            BISession session) throws Exception {

        // Récupération des périodes comptables existante :

        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setForEstPeriode(new Boolean(false));

        soldeManager.setForIdCompte(viewBean.getIdCompte());
        soldeManager.setForIdExerComptable(viewBean.getIdExerciceComptable());
        soldeManager.setForIdMandat(viewBean.getIdMandat());
        soldeManager.setSession((BSession) session);
        soldeManager.find();

        if (soldeManager.size() > 1) {
            throw new Exception(this.getClass().getName() + " Plusieurs soldes trouvé pour le compte id = "
                    + viewBean.getIdCompte() + " exercice id = " + viewBean.getIdExerciceComptable());
        } else if (soldeManager.size() == 0) {
            // création du solde pour l'exercice
            createSolde((BSession) session, viewBean, false, null);
        } else {
            viewBean.setMontantAnalyseBudgetaireAnnuelle(((CGSolde) soldeManager.getEntity(0)).getBudget());
        }

        CGPeriodeComptableManager mgr = new CGPeriodeComptableManager();
        mgr.setSession((BSession) session);
        mgr.setForIdExerciceComptable(viewBean.getIdExerciceComptable());
        mgr.find();

        for (int i = 0; i < mgr.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) mgr.getEntity(i);
            CGSolde solde = getSoldePeriode(viewBean, periode.getIdPeriodeComptable(), session);
            if (solde == null) {
                // création du solde pour la période
                solde = createSolde((BSession) session, viewBean, true, periode.getIdPeriodeComptable());
            }
            viewBean.addBudgetPeriode(solde.getIdSolde(), periode.getCode(), periode.getIdTypePeriode(),
                    solde.getBudget());
        }

        return viewBean;
    }

    /**
     * Method doModifierAnalyseBudgetaire.
     * 
     * Met a jours le viewBean avec les périodes comptables existante et le budget définit si existant.
     * 
     * @param viewBean
     * @param session
     * @return CGAnalyseBudgetaireViewBean
     */
    private CGAnalyseBudgetaireViewBean doModifierAnalyseBudgetaire(CGAnalyseBudgetaireViewBean viewBean,
            BISession session) throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();
            // Récupération des périodes comptables existante :
            for (int i = 0; i < viewBean.getPeriodeSize(); i++) {
                String idSolde = viewBean.geIdSoldePeriode(i);
                String budget = viewBean.getMontantBudgetePeriode(i);

                // Mise a jours des soldes de chaque période comptable associée
                CGSolde solde = new CGSolde();
                solde.setSession((BSession) session);
                solde.setIdSolde(idSolde);
                solde.retrieve(transaction);
                if (solde != null && !solde.isNew()) {
                    solde.setBudget(budget);
                    solde.update(transaction);
                } else {
                    throw new Exception(this.getClass().getName() + " Impossible de récupérer le solde");
                }
            }

            CGSoldeManager soldeManager = new CGSoldeManager();
            soldeManager.setForEstPeriode(new Boolean(false));

            soldeManager.setForIdCompte(viewBean.getIdCompte());
            soldeManager.setForIdExerComptable(viewBean.getIdExerciceComptable());
            soldeManager.setForIdMandat(viewBean.getIdMandat());
            soldeManager.setSession((BSession) session);
            soldeManager.find(transaction);

            if (soldeManager.size() > 1) {
                throw new Exception(this.getClass().getName() + " Plusieurs soldes trouvé pour le compte id = "
                        + viewBean.getIdCompte() + " exercice id = " + viewBean.getIdExerciceComptable());
            } else if (soldeManager.size() == 0) {
                throw new Exception(this.getClass().getName()
                        + " Impossible de récupérer le solde pour le compte id = " + viewBean.getIdCompte()
                        + " exercice id = " + viewBean.getIdExerciceComptable());
            } else {
                CGSolde solde = (CGSolde) soldeManager.getEntity(0);
                solde.setBudget(viewBean.getMontantAnalyseBudgetaireAnnuelle());
                solde.update(transaction);
            }
        } catch (Exception e) {
            JadeLogger.trace(this, "Error(" + transaction + "," + e.getMessage() + ")");
            transaction.addErrors(e.getMessage());
            throw e;
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                    return viewBean;
                }
            } finally {
                transaction.closeTransaction();
            }
        }
        return null;
    }

    /**
     * Method doSupprimerAnalyseBudgetaire.
     * 
     * On efface rien, on met simplement à jours les budgets avec des montants égal à 0
     * 
     * @param viewBean
     * @param session
     * @return CGAnalyseBudgetaireViewBean
     */
    private CGAnalyseBudgetaireViewBean doSupprimerAnalyseBudgetaire(CGAnalyseBudgetaireViewBean viewBean,
            BISession session) throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();
            // Récupération des périodes comptables existante :
            for (int i = 0; i < viewBean.getPeriodeSize(); i++) {
                String idSolde = viewBean.geIdSoldePeriode(i);

                // Mise a jours des soldes de chaque période comptable associée
                CGSolde solde = new CGSolde();
                solde.setSession((BSession) session);
                solde.setIdSolde(idSolde);
                solde.retrieve(transaction);
                if (solde != null && !solde.isNew()) {
                    solde.setBudget("0.00");
                    solde.update(transaction);
                } else {
                    throw new Exception(this.getClass().getName() + " Impossible de récupérer le solde");
                }
            }

            CGSoldeManager soldeManager = new CGSoldeManager();
            soldeManager.setForEstPeriode(new Boolean(false));

            soldeManager.setForIdCompte(viewBean.getIdCompte());
            soldeManager.setForIdExerComptable(viewBean.getIdExerciceComptable());
            soldeManager.setForIdMandat(viewBean.getIdMandat());
            soldeManager.setSession((BSession) session);
            soldeManager.find(transaction);

            if (soldeManager.size() > 1) {
                throw new Exception(this.getClass().getName() + " Plusieurs soldes trouvé pour le compte id = "
                        + viewBean.getIdCompte() + " exercice id = " + viewBean.getIdExerciceComptable());
            } else if (soldeManager.size() == 0) {
                throw new Exception(this.getClass().getName()
                        + " Impossible de récupérer le solde pour le compte id = " + viewBean.getIdCompte()
                        + " exercice id = " + viewBean.getIdExerciceComptable());
            } else {
                CGSolde solde = (CGSolde) soldeManager.getEntity(0);
                solde.setBudget("0.00");
                solde.update(transaction);
            }
        } catch (Exception e) {
            JadeLogger.trace(this, "Error(" + transaction + "," + e.getMessage() + ")");
            transaction.addErrors(e.getMessage());
            throw e;
        } finally {
            try {
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                    return viewBean;
                }
            } finally {
                transaction.closeTransaction();
            }
        }
        return null;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(FWViewBeanInterface, FWAction, BISession)
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CGAnalyseBudgetaireViewBean vBean = (CGAnalyseBudgetaireViewBean) viewBean;

        try {
            if (CGActionAnalyseBudgetaire.ACTION_AFFICHER_ANALYSE_BUDGETAIRE.equals(action.getActionPart())) {
                return doAfficherAnalyseBudgetaire(vBean, session);
            } else if (CGActionAnalyseBudgetaire.ACTION_MODIFIER_ANALYSE_BUDGETAIRE.equals(action.getActionPart())) {
                return doModifierAnalyseBudgetaire(vBean, session);
            } else if (CGActionAnalyseBudgetaire.ACTION_SUPPRIMER_ANALYSE_BUDGETAIRE.equals(action.getActionPart())) {
                return doSupprimerAnalyseBudgetaire(vBean, session);
            } else {
                throw new Exception(this.getClass().getName() + "Action not supported !!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
            return vBean;
        }
    }

    /**
     * Method getSoldePeriode.
     * 
     * Retourne le solde du compte pour la période donné.
     * 
     * @param viewBean
     * @param idPeriodeComptable
     * @param session
     * @return CGSolde le solde, null si pas de solde.
     * @throws Exception
     */
    private CGSolde getSoldePeriode(CGAnalyseBudgetaireViewBean viewBean, String idPeriodeComptable, BISession session)
            throws Exception {

        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setForEstPeriode(new Boolean(true));

        soldeManager.setForIdCompte(viewBean.getIdCompte());
        soldeManager.setForIdExerComptable(viewBean.getIdExerciceComptable());
        soldeManager.setForIdMandat(viewBean.getIdMandat());
        soldeManager.setForIdPeriodeComptable(idPeriodeComptable);
        soldeManager.setSession((BSession) session);
        soldeManager.find();

        if (soldeManager.size() > 1) {
            throw new Exception(this.getClass().getName() + " Plusieurs soldes trouvé pour le compte id = "
                    + viewBean.getIdCompte() + " période id = " + idPeriodeComptable);
        } else if (soldeManager.size() == 0) {
            return null;
        } else {
            return (CGSolde) soldeManager.getEntity(0);
        }
    }
}
