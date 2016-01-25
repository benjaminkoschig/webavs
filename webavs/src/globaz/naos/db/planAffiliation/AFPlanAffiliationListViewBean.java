/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planAffiliation;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité PlanAffiliation.
 * 
 * @author sau
 */
public class AFPlanAffiliationListViewBean extends AFPlanAffiliationManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAffiliationId(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getAffiliationId();
    }

    public String getDomaineAdresse(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getDomaineCourrier();
    }

    public String getIdTiersFacturation(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getIdTiersFacturation();
    }

    public String getLibelle(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getLibelle();
    }

    public String getLibelleFacture(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getLibelleFacture();
    }

    public String getPlanAffiliationId(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getPlanAffiliationId();
    }

    public String getTypeAdresse(int index) {

        return ((AFPlanAffiliation) getEntity(index)).getTypeAdresse();
    }

    public Boolean isBlocageEnvoi(int index) {

        return ((AFPlanAffiliation) getEntity(index)).isBlocageEnvoi();
    }

    public Boolean isInactif(int index) {

        return ((AFPlanAffiliation) getEntity(index)).isInactif();
    }
}
