/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.op.common.model.document.Document;
import java.util.HashMap;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.GenererFormule;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMGenererformuleViewBean extends BJadePersistentObjectViewBean {

    public final static String AVEC_GENERATION_VISA_DOUBLE_CENTRALISE = "4";
    public final static String AVEC_GENERATION_VISA_DOUBLE_PRODUCTION = "5";
    public final static String AVEC_GENERATION_VISA_SIMPLE_CENTRALISE = "2";
    public final static String AVEC_GENERATION_VISA_SIMPLE_PRODUCTION = "3";
    public final static String SANS_GENERATION_VISA = "1";

    private String fileName = "";
    private GenererFormule genenerFormule = null;
    private String idFormule = "";
    private String modeGeneration = AMGenererformuleViewBean.SANS_GENERATION_VISA;
    private ParametreModelComplex parametreModelComplex = null;

    /**
	 * 
	 */
    public AMGenererformuleViewBean() {
        super();

    }

    public AMGenererformuleViewBean(FormuleList simpleFormule) {
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

    public ParametreModelComplex getParametreModelComplex() {
        return parametreModelComplex;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void importer(GenererFormule genenerForm) throws Exception {
        genenerFormule = ENServiceLocator.getGenererFormuleService().importer(genenerForm);
    }

    public Boolean isBatch() {
        if ((parametreModelComplex.getFormuleList().getFormule() != null)
                && (parametreModelComplex.getFormuleList().getFormule().getCsSequenceImpression() != null)) {
            if (parametreModelComplex.getFormuleList().getFormule().getCsSequenceImpression()
                    .equalsIgnoreCase("42001001")) {
                return true;
            }
        }

        return false;
    }

    public boolean isNew() {

        /*
         * if ((this.formule.getSpy() == null) || (this.formule.getSpy() == "")) { return true; } else { return false; }
         */
        return false;

    }

    public Document merge(Object ob) throws Exception {

        /*
         * SimpleSignetSearch simpleSignetSearch = new SimpleSignetSearch(); simpleSignetSearch.setForClass("");
         * 
         * simpleSignetSearch = ENServiceLocator.getSimpleSignetService().search(simpleSignetSearch);
         * 
         * HashMap hash = AmalServiceLocator.getDocumentAmalMergeService().create(cont); EnvoiContainer container = new
         * EnvoiContainer(hash);
         * 
         * ENWordmlTemplateManager manager = new ENWordmlTemplateManager();
         * manager.setSession(BSessionUtil.getSessionFromThreadContext()); manager.setForIdFormule("1000");
         * manager.find();
         * 
         * ENWordmlTemplate wtvb = new ENWordmlTemplate(); wtvb.setSession(BSessionUtil.getSessionFromThreadContext());
         * WordmlDocument docToMerge = null; if (manager.size() > 0) {
         * wtvb.setIdWordTemplate(manager.getEntity(0).getId()); wtvb.retrieve(); } // if docToMerge =
         * wtvb.getWordmlDocument();
         * 
         * docToMerge.mergeDocument(container); return docToMerge;
         */

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("idDetailFamille", "3020");
        map.put("idContribuable", "34605");
        // return AmalServiceLocator.getDocumentAmalMergeService().merge(map, "42000083");

        // AmalServiceLocator.getDocumentAmalBatchService().processJobEnvoi("20");
        // AmalServiceLocator.getDocumentAmalBatchService().loadContainer("1011");

        // return AmalServiceLocator.getDocumentAmalMergeService().merge(map, "42000021");
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
        // this.formule = formule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // this.formule.setId(newId);
    }

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setModeGeneration(String modeGeneration) {
        this.modeGeneration = modeGeneration;
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
        // this.formule = ENServiceLocator.getFormuleListService().update(this.formule);
        // this.formule = ENServiceLocator.getFormuleListService().update(this.formule);
    }

}
