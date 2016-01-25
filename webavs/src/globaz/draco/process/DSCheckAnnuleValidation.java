package globaz.draco.process;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;

public class DSCheckAnnuleValidation extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDeclarationDistante;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDeclarationDistante)) {
            return false;
        }
        DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
        dsMgr.setSession(getSession());
        dsMgr.setForIdDeclarationDistante(idDeclarationDistante);
        dsMgr.find();
        if (dsMgr.size() > 0) {
            DSDeclarationViewBean decl = (DSDeclarationViewBean) dsMgr.getFirstEntity();

            if (DSDeclarationViewBean.CS_OUVERT.equals(decl.getEtat())) {
                decl.delete();
            } else {
                _addError(getTransaction(), getSession().getLabel("DECLARATION_DISTANTE"));
            }
        }
        return !isOnError();
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDeclarationDistante() {
        return idDeclarationDistante;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdDeclarationDistante(String idDeclarationDistante) {
        this.idDeclarationDistante = idDeclarationDistante;
    }

}
