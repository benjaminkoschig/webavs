/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.adhesion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.couverture.AFCouvertureListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Le viewBean de l'entité Adhésion.
 * 
 * @author sau
 */
public class AFAdhesionViewBean extends AFAdhesion implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Map cotisationMap = new HashMap();

    Map handleCotisations = null;

    /**
     * Constructeur de AFAdhesionViewBean
     */
    public AFAdhesionViewBean() {
        super();
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);

        if ((handleCotisations != null) && !handleCotisations.isEmpty()) {
            for (Iterator iterator = handleCotisations.keySet().iterator(); iterator.hasNext();) {
                String j = (String) iterator.next();
                AFCotisation coti = (AFCotisation) handleCotisations.get(j);
                coti.setAdhesionId(getAdhesionId());
                coti.add(transaction);
            }
        }

    }

    public AFCotisation createCotisation(String planCaisseId, String assuranceId, String planAffiliationId, String j,
            String affiliationId, String adhesionId, String dateDebut, String periodicite, String motifFin,
            String dateFin) {

        AFCotisation cotisation = new AFCotisation();
        cotisation.setSession(getSession());
        cotisation.setPlanCaisseId(planCaisseId);
        cotisation.setAssuranceId(assuranceId);
        cotisation.setPlanAffiliationId(planAffiliationId);
        cotisation.setAffiliationId(affiliationId);
        cotisation.setDateDebut(dateDebut);
        cotisation.setDateFin(dateFin);
        cotisation.setPeriodicite(periodicite);
        cotisation.setMotifFin(motifFin);

        cotisationMap.put(j, cotisation);

        return cotisation;
    }

    public Map getCotisationMap() {
        return cotisationMap;
    }

    public Map getHandleCotisationMap() {
        return handleCotisations;
    }

    public AFCouvertureListViewBean getListCouverture(String planCaisseId, String date) throws Exception {
        AFCouvertureListViewBean list = new AFCouvertureListViewBean();
        list.setSession(getSession());
        list.setForPlanCaisseId(planCaisseId);
        list.setForDateFinPlusGransQue(date);
        list.find();

        return list;
    }

    public String getNumeroAffilie(String idAffiliation) throws Exception {
        AFAffiliation af = new AFAffiliation();
        af.setSession(getSession());
        af.setAffiliationId(idAffiliation);
        af.retrieve();

        return af.getAffilieNumero();
    }

    public AFPlanAffiliation getPlanAffiliation(String idPlanAffiliation) throws Exception {
        AFPlanAffiliation pa = new AFPlanAffiliation();
        pa.setSession(getSession());
        pa.setId(idPlanAffiliation);
        pa.retrieve();

        return pa;
    }

    public String getPlanCaisseLibelle(String idPlanCaisse) throws Exception {
        AFPlanCaisse plan = new AFPlanCaisse();
        plan.setSession(getSession());
        plan.setId(idPlanCaisse);
        plan.retrieve();

        return plan.getLibelle();

    }

    public AFAffiliation retrieveAffiliation(String idAffiliation) throws Exception {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(getSession());
        aff.setAffiliationId(idAffiliation);
        aff.retrieve();

        return aff;
    }

    public void setCotisationMap(Map cotisationMap) {
        this.cotisationMap = cotisationMap;
    }

    public void setHandleCotisationMap(Map handleCotisations) {
        this.handleCotisations = handleCotisations;
    }
}
