/**
 *
 */
package globaz.osiris.helpers.recouvrement.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean;
import globaz.osiris.utils.CASursisPaiementEcheancier;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author SEL
 */
public class CAProcessSursis extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forComputeEcheance;
    private boolean forDeleteEcheance;
    private String idEcheance = "";
    private String idPlanRecouvrement = "";

    /**
	 *
	 */
    public CAProcessSursis() {
        super();
        forDeleteEcheance = false;
        forComputeEcheance = false;
    }

    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            if (isForComputeEcheance()) {
                computeEcheanceDown(getIdEcheance(), getIdPlanRecouvrement());
            }
            if (isForDeleteEcheance()) {
                deleteEcheanceDown(getIdEcheance(), getIdPlanRecouvrement());
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.toString());
        }

        return false;
    }

    /**
     * Ajoute une échéance au plan.
     * 
     * @param session
     * @param montant
     * @param dateExigibilite
     * @param plan
     * @param count
     * @throws JAException
     * @throws Exception
     */
    private void addEcheance(BSession session, BigDecimal montant, JADate dateExigibilite,
            CAPlanRecouvrementViewBean plan, int count) throws JAException, Exception {
        CAEcheancePlan echeance = new CAEcheancePlan();
        echeance.setSession(session);
        echeance.setDateExigibilite(dateExigibilite.toStr("."));
        echeance.setIdPlanRecouvrement(plan.getIdPlanRecouvrement());
        echeance.setMontant(montant.toString());
        echeance.add(getTransaction());
    }

    /**
     * Recalcule les échéances en-dessous de l'échéance selectionnée en se basant sur l'échéance sélectionnée.
     * 
     * @param session
     * @param idEcheance
     * @param idPlan
     * @throws Exception
     */
    private void computeEcheanceDown(String idEcheance, String idPlan) throws Exception {
        BigDecimal soldeRestant = new BigDecimal("0");
        BigDecimal nouvelAcompte = new BigDecimal("0");

        try {
            CAEcheancePlanManager mgrEcheance = new CAEcheancePlanManager();
            mgrEcheance.setSession(getSession());
            mgrEcheance.setForIdPlanRecouvrement(idPlan);
            mgrEcheance.setFromIdEcheancePlan(idEcheance);
            mgrEcheance.setOrder(CAEcheancePlan.FIELD_IDECHEANCEPLAN);
            mgrEcheance.find(getTransaction(), BManager.SIZE_NOLIMIT);

            Iterator it = mgrEcheance.iterator();
            CAEcheancePlan echeanceModel = (CAEcheancePlan) it.next();
            nouvelAcompte = new BigDecimal(echeanceModel.getMontant());
            JADate dateExigibilite = new JADate(echeanceModel.getDateExigibilite());

            CAPlanRecouvrementViewBean plan = getPlan(getSession(), idPlan);

            // Calcul du solde de base pour les échéances à modifier/créer
            if (!JadeStringUtil.isDecimalEmpty(plan.getPlafond())) {
                soldeRestant = soldeRestant.add(new BigDecimal(plan.getPlafond()));
            } else {
                soldeRestant = soldeRestant.add(new BigDecimal(plan.getSoldeResiduelPlan()));
            }
            soldeRestant = soldeRestant.subtract(getSumMontantEcheancesRestantes(getSession(), idPlan,
                    dateExigibilite.toStr(".")));

            if (soldeRestant.floatValue() > 0) {
                int count = 1;
                while (it.hasNext() || (soldeRestant.compareTo(nouvelAcompte) >= 0)) {
                    if ((soldeRestant.compareTo(nouvelAcompte) >= 0) && it.hasNext()) {
                        dateExigibilite = getNextDateExigibilite(getSession(), dateExigibilite, plan, count++);
                        CAEcheancePlan echeance = (CAEcheancePlan) it.next();
                        echeance.setSession(getSession());
                        echeance.setMontant(nouvelAcompte.toString());
                        echeance.setDateExigibilite(dateExigibilite.toStr("."));
                        echeance.update(getTransaction());
                        soldeRestant = soldeRestant.subtract(nouvelAcompte);
                    } else if ((soldeRestant.compareTo(nouvelAcompte) >= 0) && !it.hasNext()) {
                        dateExigibilite = getNextDateExigibilite(getSession(), dateExigibilite, plan, count++);
                        // Créer les échéances manquantes
                        addEcheance(getSession(), nouvelAcompte, dateExigibilite, plan, count++);
                        soldeRestant = soldeRestant.subtract(nouvelAcompte);
                    } else if ((soldeRestant.compareTo(nouvelAcompte) < 0) && it.hasNext()) {
                        // Supprimer les échéances suivantes
                        CAEcheancePlan echeance = (CAEcheancePlan) it.next();
                        echeance.setSession(getSession());
                        echeance.delete(getTransaction());
                    }
                    if (!CAPlanRecouvrement.CS_ECHEANCE_HEBDOMADAIRE.equals(plan.getIdTypeEcheance())
                            && !CAPlanRecouvrement.CS_ECHEANCE_QUINZAINE.equals(plan.getIdTypeEcheance())) {
                        dateExigibilite.setDay(new JADate(echeanceModel.getDateExigibilite()).getDay());
                    }
                }

                // Solde restant
                if (soldeRestant.floatValue() > 0) {
                    addEcheance(getSession(), soldeRestant,
                            getNextDateExigibilite(getSession(), dateExigibilite, plan, count++), plan, count++);
                }
            }
        } catch (Exception e) {
            throw new Exception(getSession().getLabel("COMPUTEECHEANCEDOWN") + " - " + e.toString());
        }
    }

    /**
     * Supprime les échéances en-dessous de l'échéance selectionnée ainsi que l'échéance sélectionnée.
     * 
     * @param dispatcher
     * @param idEcheance
     * @param idPlan
     * @throws Exception
     */
    private void deleteEcheanceDown(String idEcheance, String idPlan) throws Exception {
        try {
            CAEcheancePlanManager mgrEcheance = new CAEcheancePlanManager();
            mgrEcheance.setSession(getSession());
            mgrEcheance.setForIdPlanRecouvrement(idPlan);
            mgrEcheance.setFromIdEcheancePlan(idEcheance);
            mgrEcheance.find(getTransaction(), BManager.SIZE_NOLIMIT);

            Iterator it = mgrEcheance.iterator();
            while (it.hasNext()) {
                CAEcheancePlan echeance = (CAEcheancePlan) it.next();
                echeance.setSession(getSession());
                if ((echeance != null) && !echeance.isNew()) {
                    echeance.delete(getTransaction());
                }
            }
        } catch (Exception e) {
            throw new Exception(getSession().getLabel("DELETEECHEANCEDOWN") + " - " + e.toString());
        }
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return the idEcheance
     */
    public String getIdEcheance() {
        return idEcheance;
    }

    /**
     * @return the idPlanRecouvrement
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @param sessionOsiris
     * @param dateExigibilite
     * @param plan
     * @param count
     * @return la prochaine date d'échéance
     * @throws JAException
     * @throws Exception
     */
    private JADate getNextDateExigibilite(BSession sessionOsiris, JADate dateExigibilite,
            CAPlanRecouvrementViewBean plan, int count) throws JAException, Exception {
        JADate date = new JADate(dateExigibilite.toStr("."));
        date = CASursisPaiementEcheancier.getNextDateExigibilite(sessionOsiris, date, count, plan);
        return date;
    }

    /**
     * @param sessionOsiris
     * @param idPlan
     * @return le plan correspondant à l'id
     * @throws Exception
     */
    private CAPlanRecouvrementViewBean getPlan(BSession sessionOsiris, String idPlan) throws Exception {
        CAPlanRecouvrementViewBean plan = new CAPlanRecouvrementViewBean();
        plan.setSession(sessionOsiris);
        plan.setIdPlanRecouvrement(idPlan);
        try {
            plan.retrieve(getTransaction());
        } catch (Exception e) {
            throw new Exception(e);
        }
        return plan;
    }

    /**
     * @param session
     * @param idPlan
     * @param toDate
     * @return la somme des échéances jusqu'à la date toDate
     * @throws Exception
     */
    private BigDecimal getSumMontantEcheancesRestantes(BSession session, String idPlan, String toDate) throws Exception {
        BigDecimal totalEcheancesRestantes = new BigDecimal("0");
        CAEcheancePlanManager mgrEcheanceRestante = new CAEcheancePlanManager();
        mgrEcheanceRestante.setSession(session);
        mgrEcheanceRestante.setForIdPlanRecouvrement(idPlan);
        mgrEcheanceRestante.setToDateExigibilite(toDate);

        try {
            totalEcheancesRestantes = mgrEcheanceRestante.getSum(CAEcheancePlan.FIELD_MONTANT, getTransaction());
        } catch (Exception e) {
            throw new Exception(e);
        }
        return totalEcheancesRestantes;
    }

    /**
     * @return the forComputeEcheance
     */
    public boolean isForComputeEcheance() {
        return forComputeEcheance;
    }

    /**
     * @return the forDeleteEcheance
     */
    public boolean isForDeleteEcheance() {
        return forDeleteEcheance;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * @param forComputeEcheance
     *            the forComputeEcheance to set
     */
    public void setForComputeEcheance(boolean forComputeEcheance) {
        this.forComputeEcheance = forComputeEcheance;
    }

    /**
     * @param forDeleteEcheance
     *            the forDeleteEcheance to set
     */
    public void setForDeleteEcheance(boolean forDeleteEcheance) {
        this.forDeleteEcheance = forDeleteEcheance;
    }

    /**
     * @param idEcheance
     *            the idEcheance to set
     */
    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    /**
     * @param idPlanRecouvrement
     *            the idPlanRecouvrement to set
     */
    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

}
