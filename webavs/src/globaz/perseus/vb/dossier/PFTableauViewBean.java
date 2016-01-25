/**
 * 
 */
package globaz.perseus.vb.dossier;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.perseus.utils.dossier.PFTableauHandler;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFTableauViewBean extends BJadePersistentObjectViewBean {

    private Dossier dossier = null;
    private String idDossier = null;
    private PFTableauHandler pfTableauHandler = null;

    public PFTableauViewBean() {
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
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return idDossier;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the pfTableauHandler
     */
    public PFTableauHandler getPfTableauHandler() {
        return pfTableauHandler;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        dossier = PerseusServiceLocator.getDossierService().read(idDossier);
        pfTableauHandler = new PFTableauHandler(idDossier);
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String newId) {
        idDossier = newId;

    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param pfTableauHandler
     *            the pfTableauHandler to set
     */
    public void setPfTableauHandler(PFTableauHandler pfTableauHandler) {
        this.pfTableauHandler = pfTableauHandler;
    }

    @Override
    public void update() throws Exception {

    }

}
