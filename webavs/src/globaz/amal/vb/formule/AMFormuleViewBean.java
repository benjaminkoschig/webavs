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
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleDefinitionFormule;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleFormule;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMFormuleViewBean extends BJadePersistentObjectViewBean {
    private List listeFormule = new ArrayList();

    // Models specifiques
    private ParametreModelComplex parametreModelComplex = null;
    private ParametreModelComplex parametreModelComplextemp = null;

    /**
	 * 
	 */
    public AMFormuleViewBean() {
        super();
        parametreModelComplex = new ParametreModelComplex();
    }

    public AMFormuleViewBean(ParametreModelComplex simpleFormule) {
        super();
        parametreModelComplex = simpleFormule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().create(parametreModelComplex);
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
        parametreModelComplex = AmalServiceLocator.getParametreModelService().delete(parametreModelComplex);
    }

    /**
     * @return the contribuable
     */
    public ParametreModelComplex getContribuable() {
        return parametreModelComplex;
    }

    public SimpleDefinitionFormule getDefinitionFormule() {
        return parametreModelComplex.getFormuleList().getDefinitionformule();
    }

    public SimpleFormule getFormule() {
        return parametreModelComplex.getFormuleList().getFormule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return parametreModelComplex.getId();
    }

    public String getLibelleDocument() {
        return parametreModelComplex.getFormuleList().getLibelle();
    }

    /**
     * @return the listeContribuable
     */
    public List getListeFormule() {
        return listeFormule;
    }

    public ParametreModelComplex getParametreModelComplex() {
        return parametreModelComplex;
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
        return new BSpy(parametreModelComplex.getSpy());
    }

    public String getTest() {
        return "loic";
    }

    public String isBatch() {
        if ((parametreModelComplex.getFormuleList().getFormule() != null)
                && (parametreModelComplex.getFormuleList().getFormule().getCsSequenceImpression() != null)) {
            if (parametreModelComplex.getFormuleList().getFormule().getCsSequenceImpression()
                    .equalsIgnoreCase("42001001")) {
                return new String("1");
            }
        }

        return new String("0");
    }

    public boolean isNew() {

        if ((parametreModelComplex.getSpy() == null) || (parametreModelComplex.getSpy() == "")) {
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
        /*
         * FormuleListSearch search = new FormuleListSearch(); search.setForIdFormule(this.getId());
         * 
         * search = ENServiceLocator.getFormuleListService().search(search);
         * 
         * for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) { this.formuletemp =
         * (FormuleList) it.next(); this.formule = this.formuletemp; this.listeFormule.add(this.formuletemp);
         * 
         * }
         */
        ParametreModelComplexSearch search = new ParametreModelComplexSearch();
        search.setForIdFormule(getId());
        search.setWhereKey("basic");

        search = AmalServiceLocator.getParametreModelService().search(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            parametreModelComplextemp = (ParametreModelComplex) it.next();
            parametreModelComplex = parametreModelComplextemp;
            listeFormule.add(parametreModelComplextemp);

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        getFormule().setId(newId);
    }

    public void setParametreModelComplex(ParametreModelComplex parametreModelComplex) {
        this.parametreModelComplex = parametreModelComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        parametreModelComplex = AmalServiceLocator.getParametreModelService().update(parametreModelComplex);
    }
}
