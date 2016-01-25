package globaz.perseus.vb.informationfacture;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.rmi.RemoteException;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFInformationFactureViewBean extends BJadePersistentObjectViewBean {

    private InformationFacture informationfacture;

    public PFInformationFactureViewBean() {
        informationfacture = new InformationFacture();
    }

    public PFInformationFactureViewBean(InformationFacture infoFact) {
        informationfacture = infoFact;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getInformationFactureService().create(informationfacture);
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getInformationFactureService().delete(informationfacture);
    }

    public InformationFacture getInformationFacture() {
        return informationfacture;
    }

    @Override
    public String getId() {
        return informationfacture.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(informationfacture.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        informationfacture = PerseusServiceLocator.getInformationFactureService().read(getId());
    }

    public void setInformationFacture(InformationFacture informationfacture) {
        this.informationfacture = informationfacture;
    }

    @Override
    public void setId(String arg0) {
        informationfacture.setId(arg0);
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getInformationFactureService().update(informationfacture);
    }

    public static String getLabel(String code) throws RemoteException {
        String output = BSessionUtil.getSessionFromThreadContext().getLabel(code);

        return '"' + output + '"';
    }

}
