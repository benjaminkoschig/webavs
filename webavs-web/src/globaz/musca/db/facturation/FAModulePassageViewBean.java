package globaz.musca.db.facturation;

import globaz.globall.db.BTransaction;

public class FAModulePassageViewBean extends FAModulePassage implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String saveIdModule = "";

    /*
     * Traitement apr�s lecture
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        setSaveIdModule(getIdModuleFacturation());
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Si l'idModuleFacturation �tait enseign� avant la modification, il ne
        // peut �tre mis � blanc
        if ((!globaz.jade.client.util.JadeStringUtil.isBlank(saveIdModule))
                && (globaz.jade.client.util.JadeStringUtil.isBlank(getIdModuleFacturation()))) {
            _addError(statement.getTransaction(), "L'action ne peut �tre mise � blanc. ");
        }
        super._validate(statement);
    }

    public java.lang.String getAction() {
        return action;
    }

    public java.lang.String getSaveIdModule() {
        return saveIdModule;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public void setSaveIdModule(java.lang.String newSaveIdModule) {
        saveIdModule = newSaveIdModule;
    }
}
