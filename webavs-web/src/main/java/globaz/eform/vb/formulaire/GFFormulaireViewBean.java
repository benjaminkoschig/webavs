package globaz.eform.vb.formulaire;

import ch.globaz.common.exceptions.NotFoundException;
import ch.globaz.common.file.FileUtils;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.constant.GFTypeEForm;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.application.GFApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.CommonNSSFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class GFFormulaireViewBean extends BJadePersistentObjectViewBean {

    private static final Logger LOG = LoggerFactory.getLogger(GFFormulaireViewBean.class);

    private GFFormulaireModel formulaire;

    private File attachement;

    private String byGestionnaire = null;
    
    private String byStatus = null;

    public String getByGestionnaire() {
        return byGestionnaire;
    }

    public void setByGestionnaire(String byGestionnaire) {
        this.byGestionnaire = byGestionnaire;
    }

    public String getByStatus() {
        return byStatus;
    }

    public void setByStatus(String byStatus) {
        this.byStatus = byStatus;
    }

    public String idTiers = "";

    public GFFormulaireViewBean() {
        super();
        formulaire = new GFFormulaireModel();
        attachement = null;
    }

    public GFFormulaireViewBean(GFFormulaireModel formulaire) {
        super();
        this.formulaire = formulaire;
        try {
            idTiers = searchTierByNss(formulaire.getBeneficiaireNss());
        } catch (Exception e) {
            LOG.error("Un problème est survenu lors de la récupération du Tiers "+formulaire.getBeneficiaireNss(), e);
        }
    }

    @Override
    public BSpy getSpy() {
        return (formulaire != null) && !formulaire.isNew() ? new BSpy(formulaire.getSpy()) : new BSpy(getSession());
    }


    @Override
    public void add() throws Exception {
        formulaire = GFEFormServiceLocator.getGFEFormDBService().create(formulaire);
    }

    @Override
    public void delete() throws Exception {
        GFEFormServiceLocator.getGFEFormDBService().delete(formulaire.getId());
    }

    @Override
    public String getId() {
        return formulaire.getId();
    }

    @Override
    public void retrieve() throws Exception {
        formulaire = GFEFormServiceLocator.getGFEFormDBService().read(getId());
        try {
            EFormFileService fileService = new EFormFileService(GFApplication.EFORM_HOST_FILE_SERVER);
            this.attachement = fileService.retrieve(GFFileUtils.generateEFormFilePath(formulaire), formulaire.getAttachementName());
        } catch (Exception e) {
            LOG.error("Un problème est survenu lors du chargement de la pièce jointe " + formulaire.getAttachementName(), e);
        }
        try {
            idTiers = searchTierByNss(formulaire.getBeneficiaireNss());
        } catch (Exception e) {
            LOG.error("Un problème est survenu lors de la récupération du Tiers "+formulaire.getBeneficiaireNss(), e);
        }
    }

    @Override
    public void setId(String newId) {
        formulaire.setId(newId);
    }

    @Override
    public void update() throws Exception {
        formulaire.setStatus(getByStatus());
        formulaire.setUserGestionnaire(getByGestionnaire());
        
        GFEFormServiceLocator.getGFEFormDBService().update(formulaire);
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    public GFFormulaireModel getFormulaire() {
        return formulaire;
    }

    public File getAttachement() {
        return attachement;
    }

    public String getCompleteSubject(BSession session) throws NotFoundException {
        GFTypeEForm type = GFTypeEForm.getGFTypeEForm(formulaire.getSubject());
        return type.getCodeEForm() + " - " + type.getDesignation(session);
    }

    public String getSubject() throws NotFoundException {
        return GFTypeEForm.getGFTypeEForm(formulaire.getSubject())
                .getDesignation(getSession());
    }

    private String searchTierByNss(String nss) throws Exception {
        if(nss == null || nss.isEmpty()){
            return "";
        }
        CommonNSSFormater commonNSSFormater = new CommonNSSFormater();
        String nssFormate = commonNSSFormater.format(nss);
        TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
        personneAvsManager.setSession(getSession());
        personneAvsManager.setForNumAvsActuel(nssFormate);
        personneAvsManager.find(1);
        TITiersViewBean tiers = (TITiersViewBean) personneAvsManager.getFirstEntity();
        return tiers != null ? tiers.getId() : "";
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLenghtAttachement() {
        if (this.attachement == null || !this.attachement.exists()) {
            return "N/A";
        }
        return FileUtils.formatLenght(this.attachement, false);
    }
}
