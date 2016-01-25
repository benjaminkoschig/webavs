package globaz.vulpecula.vb.decomptereception;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.exceptions.NotImplementedException;
import ch.globaz.vulpecula.web.views.decompte.DecompteViewService;

/**
 * @author Arnaud Geiser (AGE) | Créé le 10 avr. 2014
 * 
 */
public class PTDecomptereceptionViewBean extends BJadePersistentObjectViewBean {

    @Override
    public void add() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(final String newId) {
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getDecompteService() {
        return DecompteViewService.class.getName();
    }

    public String getDecompteMiseAJourMessage() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DECOMPTE_MISE_A_JOUR");
    }

    public String getDecompteNonExistant() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DECOMPTE_NON_EXISTANT");
    }

    public String getDecompteDejaReceptionne() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DECOMPTE_DEJA_RECEPTIONNE");
    }
}
