/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMChampformuleViewBean extends BJadePersistentObjectViewBean {

    String idFormuleSearch = null;
    String idSignetSearch = null;
    String libelleFormule = "";
    String nameFormule = "";
    private SignetListModel signetListModel = null;

    /**
	 * 
	 */
    public AMChampformuleViewBean() {
        super();
        signetListModel = new SignetListModel();
    }

    public AMChampformuleViewBean(SignetListModel signetListModel) {
        super();
        this.signetListModel = signetListModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        List<String> arrayIdsSignets = JadeStringUtil.tokenize(signetListModel.getSimpleSignetJointure().getIdSignet(),
                ";");

        for (String idSignet : arrayIdsSignets) {
            if (!JadeStringUtil.isBlankOrZero(idSignet)) {
                SignetListModel signetListModelTmp = signetListModel;
                signetListModelTmp.getSimpleSignetJointure().setIdSignet(idSignet);
                signetListModel = ENServiceLocator.getSignetListModelService().createJointure(signetListModel);
            }
        }

        // this.signetListModel = ENServiceLocator.getSignetListModelService().createJointure(this.signetListModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
        signetListModel = ENServiceLocator.getSignetListModelService().deleteJointure(signetListModel);

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    public String getIdFormule() {
        return signetListModel.getSimpleSignetJointure().getIdFormule();
    }

    public String getIdFormuleSearch() {
        return idFormuleSearch;
    }

    public String getIdSignet() {
        return signetListModel.getSimpleSignetModel().getId();
    }

    public String getLibelleFormule() {
        return libelleFormule;
    }

    public String getNameFormule() {
        return nameFormule;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    public SignetListModel getSignetListModel() {
        return signetListModel;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return new BSpy(signetListModel.getSpy());
    }

    public boolean isNew() {

        if ((signetListModel.getSpy() == null) || (signetListModel.getSpy() == "")) {
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
         * ChampFormuleSearch search = new ChampFormuleSearch(); search.setForIdChampFormule(this.getId());
         * 
         * search = ENServiceLocator.getChampFormuleListService().search(search);
         * 
         * for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) { this.champformule =
         * (ChampFormule) it.next(); }
         */
        SignetListModelSearch signetListModelSearch = new SignetListModelSearch();
        signetListModelSearch.setForIdFormule(idFormuleSearch);
        signetListModelSearch.setForIdSignet(idSignetSearch);
        signetListModelSearch = ENServiceLocator.getSignetListModelService().search(signetListModelSearch);

        if (signetListModelSearch.getSize() > 0) {
            for (Iterator it = Arrays.asList(signetListModelSearch.getSearchResults()).iterator(); it.hasNext();) {
                SignetListModel signetListModel = (SignetListModel) it.next();
                this.signetListModel = signetListModel;
            }
        }
    }

    public void retrieveName() throws Exception {

        FormuleListSearch formuleListSearch = new FormuleListSearch();
        formuleListSearch.setDefinedSearchSize(0);
        formuleListSearch.setForIdFormule(getIdFormuleSearch());

        formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);

        if (formuleListSearch.getSize() > 0) {
            for (Iterator it = Arrays.asList(formuleListSearch.getSearchResults()).iterator(); it.hasNext();) {
                FormuleList formule = (FormuleList) it.next();
                nameFormule = formule.getDefinitionformule().getCsDocument();
                libelleFormule = formule.getFormule().getLibelleDocument();
            }
        }
        /*
         * search.s ChampFormuleSearch search = new ChampFormuleSearch(); search.setForIdChampFormule(this.getId());
         * 
         * search = ENServiceLocator.getChampFormuleListService().search(search);
         * 
         * for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) { this.champformule =
         * (ChampFormule) it.next(); }
         */

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        signetListModel.setId(newId);
    }

    public void setIdFormuleSearch(String idFormuleSearch) {
        this.idFormuleSearch = idFormuleSearch;
    }

    public String setIdSignetSearch(String newId) {
        return idSignetSearch = newId;
    }

    public void setLibelleFormule(String libelleFormule) {
        this.libelleFormule = libelleFormule;
    }

    public void setNameFormule(String nameFormule) {
        this.nameFormule = nameFormule;
    }

    public void setSignetListModel(SignetListModel signetListModel) {
        this.signetListModel = signetListModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        signetListModel = ENServiceLocator.getSignetListModelService().update(signetListModel);
        // this.champformule = AmalServiceLocator..update(this.formule);
        // this.champformule = ENServiceLocator.getChampFormuleListService().update(this.champformule);
    }

}
