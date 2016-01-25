/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMFormuleViewBeanStandard extends BJadePersistentObjectViewBean {
    private FormuleList formule = null;
    private FormuleList formuletemp = null;
    private List listeFormule = new ArrayList();

    /**
	 * 
	 */
    public AMFormuleViewBeanStandard() {
        super();
        formule = new FormuleList();
    }

    public AMFormuleViewBeanStandard(FormuleList simpleFormule) {
        super();
        formule = simpleFormule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        formule = ENServiceLocator.getFormuleListService().create(formule);
        // this.formule = ENServiceLocator.getFormuleListService().create(this.formule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
        formule = ENServiceLocator.getFormuleListService().delete(formule);
    }

    /**
     * @return the contribuable
     */
    public FormuleList getContribuable() {
        return formule;
    }

    public FormuleList getDefinitionFormule() {
        return formule;
    }

    public FormuleList getFormule() {
        return formule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return formule.getId();
    }

    public String getLibelleDocument() {
        return formule.getLibelle();
    }

    /**
     * @return the listeContribuable
     */
    public List getListeFormule() {
        return listeFormule;
    }

    public ArrayList getSelectListLangue() {
        ArrayList<String> list = new ArrayList();
        list.add("Français - F");
        list.add("Allemand - D");
        list.add("Italien - I");
        return list;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(formule.getSpy());
    }

    public String getTest() {
        return "loic";
    }

    public boolean isNew() {

        if ((formule.getSpy() == null) || (formule.getSpy() == "")) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        FormuleListSearch search = new FormuleListSearch();
        search.setForIdFormule(getId());

        search = ENServiceLocator.getFormuleListService().search(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            formuletemp = (FormuleList) it.next();
            formule = formuletemp;
            listeFormule.add(formuletemp);

        }

    }

    public void setFormule(FormuleList formule) {
        this.formule = formule;
    }

    /**
     * @param contribuable
     *            the contribuable to set
     */
    public void setFormuleList(FormuleList formule) {
        this.formule = formule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        formule.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // this.formule = ENServiceLocator.getFormuleListService().update(this.formule);
        formule = ENServiceLocator.getFormuleListService().update(formule);
    }
}
