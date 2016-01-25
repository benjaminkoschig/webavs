package globaz.pegasus.vb.blocage;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;

/**
 * ViewBean pour la liste des entetes de blocage PC --> ECRAN PPC0113
 * 
 * @see
 * @author dma (modification sce --> 1.11.12/1.12.00)
 *         Voir ajaxViewBean pour la liste généré:
 * @see globaz.pegasus.vb.blocage.PCEnteteBlocageAjaxViewBean
 * @version 1.11.12/1.12.00
 * 
 */
public class PCEnteteBlocageViewBean extends BJadePersistentObjectViewBean {

    private String idDemandePc = null;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        return idDemandePc;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getRequerantDetail() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DemandeException {

        if (JadeStringUtil.isBlank(idDemandePc)) {
            return "";
        }

        return PCDroitHandler.getInfoHtmlRequerantForDemande(idDemandePc);
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

    @Override
    public void setId(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
