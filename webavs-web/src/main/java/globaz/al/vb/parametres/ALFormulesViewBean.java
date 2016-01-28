/**
 * 
 */
package globaz.al.vb.parametres;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAVector;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.GenererFormule;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * View Bean d'une entitée envoitemplatecomplexmodel
 * 
 * @author dhi
 * 
 */
public class ALFormulesViewBean extends BJadePersistentObjectViewBean {

    private EnvoiTemplateComplexModel envoiTemplate = null;

    /**
     * Default constructor
     */
    public ALFormulesViewBean() {
        super();
        envoiTemplate = new EnvoiTemplateComplexModel();
    }

    /**
     * Constructor called from list view bean
     */
    public ALFormulesViewBean(EnvoiTemplateComplexModel _envoiTemplate) {
        super();
        setEnvoiTemplate(_envoiTemplate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        envoiTemplate = ALServiceLocator.getEnvoiTemplateComplexService().create(envoiTemplate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        envoiTemplate = ALServiceLocator.getEnvoiTemplateComplexService().delete(envoiTemplate);
    }

    /**
     * Appel du service envoi pour mettre en blob le fichier wordml
     * 
     * @param genenerForm
     * @return
     * @throws Exception
     */
    public GenererFormule downloadFormule(GenererFormule genenerForm) throws Exception {
        return ENServiceLocator.getGenererFormuleService().exporter(genenerForm);
    }

    /**
     * @return the envoiTemplate
     */
    public EnvoiTemplateComplexModel getEnvoiTemplate() {
        return envoiTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        if (envoiTemplate != null) {
            return envoiTemplate.getId();
        } else {
            return null;
        }
    }

    /**
     * Récupération des valeurs des codes systèmes ALENVOIDOC
     * 
     * @return
     */
    public JAVector getListeDocumentsFromCS() {
        BSession currentSession = (BSession) getISession();

        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALENVOIDOC");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return new JAVector();
        }
        JAVector containerCS = cm.getContainer();
        return containerCS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (envoiTemplate != null) {
            return new BSpy(envoiTemplate.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        EnvoiTemplateComplexModelSearch searchModel = new EnvoiTemplateComplexModelSearch();
        searchModel.setForIdFormule(getId());
        searchModel = ALServiceLocator.getEnvoiTemplateComplexService().search(searchModel);
        if (searchModel.getSize() == 1) {
            envoiTemplate = (EnvoiTemplateComplexModel) searchModel.getSearchResults()[0];
        } else {
            envoiTemplate = new EnvoiTemplateComplexModel();
        }
    }

    /**
     * @param envoiTemplate
     *            the envoiTemplate to set
     */
    public void setEnvoiTemplate(EnvoiTemplateComplexModel envoiTemplate) {
        this.envoiTemplate = envoiTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        if (envoiTemplate != null) {
            envoiTemplate.setId(newId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        envoiTemplate = ALServiceLocator.getEnvoiTemplateComplexService().update(envoiTemplate);
    }

    /**
     * Appel du service envoi pour mettre en blob le fichier wordml
     * 
     * @param genenerForm
     * @throws Exception
     */
    public void uploadFormule(GenererFormule genenerForm) throws Exception {
        ENServiceLocator.getGenererFormuleService().importer(genenerForm);
    }

}
