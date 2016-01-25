package globaz.lyra.vb.process;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Iterator;
import ch.globaz.lyra.business.exceptions.LYTechnicalException;

public class LYProcessAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail;
    private String moisTraiement;
    private String processPath;

    public LYProcessAjaxViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        throw new LYTechnicalException("Méthode non supportée");
    }

    @Override
    public void delete() throws Exception {
        throw new LYTechnicalException("Méthode non supportée");
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    public String getMoisTraiement() {
        return moisTraiement;
    }

    public String getProcessPath() {
        return processPath;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        throw new LYTechnicalException("Méthode non supportée");
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    public void setMoisTraiement(String moisTraiement) {
        this.moisTraiement = moisTraiement;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    @Override
    public void update() throws Exception {
        throw new LYTechnicalException("Méthode non supportée");
    }
}
