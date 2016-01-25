/**
 * 
 */
package globaz.amal.vb.controleurRappel;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.amal.business.echeances.AMControleurRappelDetail;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author DHI
 * 
 */
public class AMControleurRappelViewBean extends BJadePersistentObjectViewBean {

    private HashMap<String, ArrayList<AMControleurRappelDetail>> rappelOuverts = null;
    private String selectedLibra = null;
    private String selectedRappel = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        if ((selectedLibra != null) && (selectedLibra.length() > 0)) {
            String[] itemsToRemove = selectedLibra.split(",");
            for (int iItem = 0; iItem < itemsToRemove.length; iItem++) {
                String currentLibra = itemsToRemove[iItem];
                AmalServiceLocator.getControleurRappelService().deleteRappelLibra(currentLibra);
            }
        } else if ((selectedRappel != null) && (selectedRappel.length() > 0)) {
            AmalServiceLocator.getControleurRappelService().deleteRappel(selectedRappel);
        }
    }

    public void generateRappel() throws Exception {
        if ((selectedLibra != null) && (selectedLibra.length() > 0)) {
            String[] itemsToChange = selectedLibra.split(",");
            for (int iItem = 0; iItem < itemsToChange.length; iItem++) {
                String currentLibra = itemsToChange[iItem];
                AmalServiceLocator.getControleurRappelService().generateRappelLibra(currentLibra);
            }
        } else if ((selectedRappel != null) && (selectedRappel.length() > 0)) {
            AmalServiceLocator.getControleurRappelService().generateRappel(selectedRappel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the rappelOuverts
     */
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getRappelOuverts() {
        return rappelOuverts;
    }

    /**
     * @return the selectedLibra
     */
    public String getSelectedLibra() {
        return selectedLibra;
    }

    /**
     * @return the selectedRappel
     */
    public String getSelectedRappel() {
        return selectedRappel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /**
     * Retrive all rappels
     * 
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void retrieveRappels() throws JadeApplicationServiceNotAvailableException {
        JadeLogger.info(null, "Retrieving AMAL rappels ...");
        Date dateToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String csDateToday = sdf.format(dateToday);
        csDateToday = "";
        HashMap<String, ArrayList<AMControleurRappelDetail>> results = AmalServiceLocator.getControleurRappelService()
                .getLIBRARappels(csDateToday);
        if (results != null) {
            setRappelOuverts(results);
        } else {
            rappelOuverts = new HashMap<String, ArrayList<AMControleurRappelDetail>>();
        }
        JadeLogger.info(null, "END Retrieving AMAL rappels ...");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param rappelOuverts
     *            the rappelOuverts to set
     */
    public void setRappelOuverts(HashMap<String, ArrayList<AMControleurRappelDetail>> rappelOuverts) {
        this.rappelOuverts = rappelOuverts;
    }

    /**
     * @param selectedLibra
     *            the selectedLibra to set
     */
    public void setSelectedLibra(String selectedLibra) {
        this.selectedLibra = selectedLibra;
    }

    /**
     * @param selectedRappel
     *            the selectedRappel to set
     */
    public void setSelectedRappel(String selectedRappel) {
        this.selectedRappel = selectedRappel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
