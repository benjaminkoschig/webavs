package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.CPListeExcelConcordanceCotPersCIProcess;

public class CPListeConcordanceCotPersCIViewBean extends CPListeExcelConcordanceCotPersCIProcess implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
        if (JadeStringUtil.isEmpty(getForAnnee())) {
            this._addError(getSession().getLabel("CP_MSG_0126"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        BTransaction trans = new BTransaction(getSession());
        try {
            trans.openTransaction();
            setFromDiffAdmise(FWFindParameter.findParameter(trans, "10500010", "DIFFMINCI", "", "", 0));
            setToDiffAdmise(FWFindParameter.findParameter(trans, "10500010", "DIFFMAXCI", "", "", 0));
        } catch (Exception e) {
            setFromDiffAdmise("0");
            setToDiffAdmise("0");
        } finally {
            trans.closeTransaction();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        id = newId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
