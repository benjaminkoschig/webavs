/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.GenererFormule;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMImportformuleViewBean extends BJadePersistentObjectViewBean {

    public final static String AVEC_GENERATION_VISA_DOUBLE_CENTRALISE = "4";
    public final static String AVEC_GENERATION_VISA_DOUBLE_PRODUCTION = "5";
    public final static String AVEC_GENERATION_VISA_SIMPLE_CENTRALISE = "2";
    public final static String AVEC_GENERATION_VISA_SIMPLE_PRODUCTION = "3";
    public final static String SANS_GENERATION_VISA = "1";

    private String fileName = "";
    private FormuleList formuleList = null;
    private GenererFormule genenerFormule = null;
    private String idFormule = "";
    private String modeGeneration = AMImportformuleViewBean.SANS_GENERATION_VISA;

    /**
	 * 
	 */
    public AMImportformuleViewBean() {
        super();
        formuleList = new FormuleList();

    }

    public AMImportformuleViewBean(FormuleList simpleFormule) {
        super();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // this.formule = ENServiceLocator.getFormuleListService().create(this.formule);
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
        // this.formule = ENServiceLocator.getFormuleListService().delete(this.formule);
    }

    public GenererFormule exporter(GenererFormule genenerForm) throws Exception {
        return genenerFormule = ENServiceLocator.getGenererFormuleService().exporter(genenerForm);

    }

    public String getFileName() {
        return fileName;
    }

    public FormuleList getFormuleList() {
        return formuleList;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idFormule;
    }

    public String getIdFormule() {
        return idFormule;
    }

    public String getModeGeneration() {
        return modeGeneration;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void importer(GenererFormule genenerForm) throws Exception {
        genenerFormule = ENServiceLocator.getGenererFormuleService().importer(genenerForm);

    }

    public String isBatch() {
        if ((formuleList.getFormule() != null) && (formuleList.getFormule().getCsSequenceImpression() != null)) {
            if (formuleList.getFormule().getCsSequenceImpression().equalsIgnoreCase("42001001")) {
                return new String("1");
            }
        }

        return new String("0");
    }

    public boolean isNew() {

        /*
         * if ((this.formule.getSpy() == null) || (this.formule.getSpy() == "")) { return true; } else { return false; }
         */
        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        FormuleListSearch formuleListSearch = new FormuleListSearch();
        formuleListSearch.setForIdFormule(getIdFormule());
        formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);

        // FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];

        if (formuleListSearch.getSize() > 0) {
            setFormuleList((FormuleList) formuleListSearch.getSearchResults()[0]);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFormule(FormuleList formule) {
        // this.formule = formule;
    }

    /**
     * @param contribuable
     *            the contribuable to set
     */
    public void setFormuleList(FormuleList formule) {
        formuleList = formule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idFormule = newId;
    }

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setModeGeneration(String modeGeneration) {
        this.modeGeneration = modeGeneration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // this.formule = ENServiceLocator.getFormuleListService().update(this.formule);
        // this.formule = ENServiceLocator.getFormuleListService().update(this.formule);
    }

}
