package globaz.phenix.vb.communications;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.vb.CPAbstractPersistentObjectViewBean;
import globaz.pyxis.constantes.IConstantes;

public class CPApercuCommunicationFiscaleRetourComViewBean extends CPAbstractPersistentObjectViewBean {

    private String canton = "";
    private ICommunicationRetour communicationRetour = null;

    private String idRetour = "";

    @Override
    public void add() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }

    public String getCanton() {
        return canton;
    }

    public ICommunicationRetour getCommunicationRetour() {
        return communicationRetour;
    }

    public String getIdRetour() {
        return idRetour;
    }

    @Override
    public void retrieve() throws Exception {

        if (JadeStringUtil.isBlank(getCanton())) {
            CPCommunicationFiscaleRetourViewBean vb = new CPCommunicationFiscaleRetourViewBean();
            vb.setSession(getSession());
            vb.setIdRetour(getIdRetour());
            vb.retrieve();

            canton = vb.getCantonJournal();
        }

        if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_JURA)) {
            communicationRetour = new CPCommunicationFiscaleRetourJUViewBean();
        } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
            communicationRetour = new CPCommunicationFiscaleRetourNEViewBean();
        } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_GENEVE)) {
            communicationRetour = new CPCommunicationFiscaleRetourGEViewBean();
        } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
            communicationRetour = new CPCommunicationFiscaleRetourVDViewBean();
        } else if (canton.equalsIgnoreCase(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
            communicationRetour = new CPCommunicationFiscaleRetourVSViewBean();
        } else if (canton.equalsIgnoreCase(CPJournalRetour.CS_CANTON_SEDEX)) {
            communicationRetour = new CPCommunicationFiscaleRetourSEDEXViewBean();
            communicationRetour.setWantDonneeBase(true);
        }
        communicationRetour.setWantAfterRetrieve(true);
        communicationRetour.setSession(getSession());
        communicationRetour.setIdRetour(getIdRetour());
        communicationRetour.retrieve();
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCommunicationRetour(ICommunicationRetour communicationRetour) {
        this.communicationRetour = communicationRetour;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    @Override
    public void update() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }
}
