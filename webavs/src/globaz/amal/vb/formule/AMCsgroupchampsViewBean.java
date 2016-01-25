/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.envoi.business.models.parametrageEnvoi.CodeSysteme;

/**
 * @author CBU
 * 
 */
public class AMCsgroupchampsViewBean extends BJadePersistentObjectViewBean {
    private CodeSysteme codeSysteme = null;

    /**
	  *
	  */
    public AMCsgroupchampsViewBean() {
        super();
        codeSysteme = new CodeSysteme();
    }

    public AMCsgroupchampsViewBean(CodeSysteme codeSysteme) {
        super();
        this.codeSysteme = codeSysteme;
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public void find() throws Exception {
        // this.formuleListSearch = AmalServiceLocator.getFormuleListService().search(this.formuleListSearch);
        // this.formuleListSearch = ENServiceLocator.getFormuleListService().search(this.formuleListSearch);
    }

    public CodeSysteme getCodeSysteme() {
        return codeSysteme;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return codeSysteme.getId();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setCodeSysteme(CodeSysteme codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
