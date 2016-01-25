/**
 * 
 */
package globaz.perseus.vb.qd;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFQdChercherViewBean implements FWViewBeanInterface {

    private String idDossier = null;
    private List<MembreFamille> listMembreFamille = null;
    private String message = null;
    private String msgType = null;
    private BISession session = null;

    public PFQdChercherViewBean() {
        listMembreFamille = new ArrayList<MembreFamille>();
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @return the listMembreFamille
     */
    public List<MembreFamille> getListMembreFamille() {
        return listMembreFamille;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public void init() throws Exception {
        if (!JadeStringUtil.isEmpty(idDossier)) {
            listMembreFamille = PerseusServiceLocator.getDossierService().getListAllMembresFamille(idDossier);
        }
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * @param listMembreFamille
     *            the listMembreFamille to set
     */
    public void setListMembreFamille(List<MembreFamille> listMembreFamille) {
        this.listMembreFamille = listMembreFamille;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

}
