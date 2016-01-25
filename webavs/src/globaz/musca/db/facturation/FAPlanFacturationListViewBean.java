package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FAPlanFacturationListViewBean extends FAPlanFacturationManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "FAPLFAP.IDPLANFACTURATION, FAPLFAP.LIBELLEFR, FAPLFAP.LIBELLEDE, FAPLFAP.LIBELLEIT, FAPLFAP.IDTYPEFACTURATION";
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getIdPlanFacturation(int pos) {
        return ((FAPlanFacturation) getEntity(pos)).getIdPlanFacturation();
    }

    public String getLibellePlan(int pos) {
        return ((FAPlanFacturation) getEntity(pos)).getLibelle();
    }

    public String getLibelleType(int pos) {
        try {
            // return ((FAPlanFacturation) getEntity(pos)).getLibelleType();
            return globaz.musca.translation.CodeSystem.getLibelle(getSession(),
                    ((FAPlanFacturation) getEntity(pos)).getIdTypeFacturation());
        } catch (Exception e) {
            return "";
        }
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
