/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.corvus.properties.REProperties;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import java.rmi.RemoteException;
import java.util.Vector;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFComptabiliserLotViewBean extends BJadePersistentObjectViewBean {

    private String adresseMail = null;
    private Lot lot = null;
    private String dateEcheancePaiement = "";
    private String idOrganeExecution = "";
    private String numeroOG = "";

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public PFComptabiliserLotViewBean() throws PropertiesException {
        super();
        lot = new Lot();
        setIdOrganeExecution(getOrganeExecProperty());
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the adresseMail
     */
    public String getAdresseMail() {
        return adresseMail;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     * @throws Exception
     */
    public Vector<String[]> getOrganesExecution() throws Exception {
        Vector<String[]> result = new Vector<String[]>();
        CAOrganeExecution organeExecution = null;
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession((BSession) getISession());
        mgr.setForIdTypeTraitementOG(true);
        mgr.changeManagerSize(0);
        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        for (int i = 0; i < mgr.size(); i++) {
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);

            result.add(new String[] { organeExecution.getIdOrganeExecution(), organeExecution.getNom() });
        }
        return result;

        // return new CAOrganeExecutionImpl().getAllOrganesExecution((BSession) getISession());
    }

    @Override
    public String getId() {
        return lot.getId();
    }

    /**
     * @return the lot
     */
    public Lot getLot() {
        return lot;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        lot = PerseusServiceLocator.getLotService().read(getId());
    }

    /**
     * @param adresseMail
     *            the adresseMail to set
     */
    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    @Override
    public void setId(String newId) {
        lot.setId(newId);
    }

    /**
     * @param lot
     *            the lot to set
     */
    public void setLot(Lot lot) {
        this.lot = lot;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

    public static String getLabel(String code) throws RemoteException {
        String output = BSessionUtil.getSessionFromThreadContext().getLabel(code);

        return '"' + output + '"';
    }

    private String getOrganeExecProperty() throws PropertiesException {
        return REProperties.ORGANE_EXECUTION_PAIEMENT.getValue();
    }

}
