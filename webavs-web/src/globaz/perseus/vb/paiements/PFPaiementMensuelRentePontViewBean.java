/**
 * 
 */
package globaz.perseus.vb.paiements;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.rmi.RemoteException;
import java.util.Vector;
import ch.globaz.osiris.businessimpl.services.organes.CAOrganeExecutionImpl;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFPaiementMensuelRentePontViewBean extends BJadePersistentObjectViewBean {

    private String adresseMail = null;
    private String mois = null;
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

    public PFPaiementMensuelRentePontViewBean() {
        super();
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

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the mois
     */
    public String getMois() {
        return mois;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     * @throws Exception
     */
    public Vector<String[]> getOrganesExecution() throws Exception {
        return new CAOrganeExecutionImpl().getAllOrganesExecution((BSession) getISession());
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        mois = PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt();
    }

    /**
     * @param adresseMail the adresseMail to set
     */
    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    /**
     * @param mois the mois to set
     */
    public void setMois(String mois) {
        this.mois = mois;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

    public static String getLabel(String code) throws RemoteException {
        String output = BSessionUtil.getSessionFromThreadContext().getLabel(code);

        return '"' + output + '"';
    }
}
