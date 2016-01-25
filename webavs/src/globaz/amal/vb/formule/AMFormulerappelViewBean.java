/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleDefinitionFormule;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleFormule;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleRappel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleRappelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMFormulerappelViewBean extends BJadePersistentObjectViewBean {
    private SimpleDefinitionFormule definitionformuleRappeler = null;
    private FormuleList formule = null;
    private SimpleFormule formuleRappeler = null;

    private String idFormule = null;
    private String idRappel = null;
    private boolean isWithRappel = false;
    private SimpleRappel rappel = null;

    /**
	 * 
	 */
    public AMFormulerappelViewBean() {
        super();
        formule = new FormuleList();
        rappel = new SimpleRappel();
        definitionformuleRappeler = new SimpleDefinitionFormule();
        formuleRappeler = new SimpleFormule();

    }

    public AMFormulerappelViewBean(FormuleList simpleFormule) {
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
        // this.formule = AmalServiceLocator.getFormuleListService().create(this.formule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {

        ENServiceLocator.getFormuleRappelService().delete(rappel);
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

    public SimpleDefinitionFormule getDefinitionformuleRappeler() {
        return definitionformuleRappeler;
    }

    public FormuleList getFormule() {
        return formule;
    }

    public SimpleFormule getFormuleRappeler() {
        return formuleRappeler;
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

    public String getIdFormule() {
        return idFormule;
    }

    public String getIdRappel() {
        return idRappel;
    }

    public String getLibelleDocument() {
        return formule.getLibelle();
    }

    public String getLibelleSearch() {
        if (JadeCodesSystemsUtil.getCodeLibelle(getDefinitionformuleRappeler().getCsDocument()).equals("")) {
            return "";
        } else {
            return getFormuleRappeler().getLibelleDocument() + ","
                    + JadeCodesSystemsUtil.getCodeLibelle(getDefinitionformuleRappeler().getCsDocument());
        }
    }

    public SimpleRappel getRappel() {
        return rappel;
    }

    /**
     * @return the listeContribuable
     */

    // public List getListeFormule() { return this.listeFormule; }

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

    public boolean isWithRappel() {
        return isWithRappel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        SimpleRappelSearch search = new SimpleRappelSearch();
        search.setForIdFormule(getId());

        search = ENServiceLocator.getFormuleRappelService().search(search);

        if (search.getSize() > 0) {
            isWithRappel = true;
            for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
                rappel = (SimpleRappel) it.next();
                // this.formule = (FormuleList) it.next();
            }

            // search FormuleDefinition
            /*
             * SimpleDefinitionFormuleSearch searchdef = new SimpleDefinitionFormuleSearch();
             * searchdef.setForIdDefinitionFormule(this.rappel.getIdDefinitionFormule()); searchdef =
             * ENServiceLocator.getSimpleDefinitionFormuleService().search(searchdef);
             * 
             * this.definitionformuleRappeler = (SimpleDefinitionFormule) searchdef.getSearchResults()[0];
             */

            FormuleListSearch searchfor = new FormuleListSearch();
            searchfor.setForIdDefinitionFormule(rappel.getIdDefinitionFormule());
            searchfor = ENServiceLocator.getFormuleListService().search(searchfor);
            formuleRappeler = ((FormuleList) searchfor.getSearchResults()[0]).getFormule();
            definitionformuleRappeler = ((FormuleList) searchfor.getSearchResults()[0]).getDefinitionformule();

        }

        // search FormuleList
        FormuleListSearch searchformule = new FormuleListSearch();
        searchformule.setForIdFormule(getId());

        searchformule = ENServiceLocator.getFormuleListService().search(searchformule);

        for (Iterator it = Arrays.asList(searchformule.getSearchResults()).iterator(); it.hasNext();) {
            formule = (FormuleList) it.next();

        }

    }

    public void setDefinitionformuleRappeler(SimpleDefinitionFormule definitionformuleRappeler) {
        this.definitionformuleRappeler = definitionformuleRappeler;
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

    public void setFormuleRappeler(SimpleFormule formuleRappeler) {
        this.formuleRappeler = formuleRappeler;
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

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setIdRappel(String idRappel) {
        this.idRappel = idRappel;
    }

    public void setRappel(SimpleRappel rappel) {
        this.rappel = rappel;
    }

    public void setWithRappel(boolean isWithRappel) {
        this.isWithRappel = isWithRappel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        FormuleListSearch searchformule = new FormuleListSearch();
        searchformule.setForlibelle(rappel.getIdDefinitionFormule());

        searchformule = ENServiceLocator.getFormuleListService().search(searchformule);
        FormuleList temp = new FormuleList();
        for (Iterator it = Arrays.asList(searchformule.getSearchResults()).iterator(); it.hasNext();) {
            temp = (FormuleList) it.next();
        }
        rappel.setIdDefinitionFormule(temp.getDefinitionformule().getIdDefinitionFormule());
        rappel.setIdFormule(formule.getId());
        rappel.setIdRappel(formule.getId());
        definitionformuleRappeler = temp.getDefinitionformule();

        SimpleRappelSearch simpleRappelSearch = new SimpleRappelSearch();
        simpleRappelSearch.setForIdFormule(formule.getId());
        simpleRappelSearch = ENServiceLocator.getFormuleRappelService().search(simpleRappelSearch);
        if (simpleRappelSearch.getSize() == 0) {
            rappel = ENServiceLocator.getFormuleRappelService().create(rappel);
        } else {
            rappel = ENServiceLocator.getFormuleRappelService().update(rappel);
        }
    }
}
