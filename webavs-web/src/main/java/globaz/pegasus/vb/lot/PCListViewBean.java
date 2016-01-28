package globaz.pegasus.vb.lot;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCListViewBean extends BJadePersistentObjectViewBean {

    String dateDernierPaiementt = null;

    public PCListViewBean() throws PmtMensuelException, JadeApplicationServiceNotAvailableException {
        dateDernierPaiementt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDernierPaiementt() {
        return dateDernierPaiementt;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
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
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
