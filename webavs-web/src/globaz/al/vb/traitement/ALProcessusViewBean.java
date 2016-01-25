package globaz.al.vb.traitement;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeNumericUtil;
import java.util.HashSet;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.musca.business.constantes.FACSModule;
import ch.globaz.musca.business.constantes.FACSPassage;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.business.models.PassageModuleComplexSearchModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;

public class ALProcessusViewBean extends BJadePersistentObjectViewBean {

    private RecapitulatifEntrepriseSearchModel listRecaps = null;

    private PassageModuleComplexModel passageModuleComplexModel = null;

    private PassageModuleComplexSearchModel searchPassageModuleComplexModel = null;

    public PassageModuleComplexSearchModel getSearchPassageModuleComplexModel() {
        return searchPassageModuleComplexModel;
    }

    public void setSearchPassageModuleComplexModel(PassageModuleComplexSearchModel searchPassageModuleComplexModel) {
        this.searchPassageModuleComplexModel = searchPassageModuleComplexModel;
    }

    private TemplateTraitementListComplexModel templateTraitementComplexModel = null;

    public ALProcessusViewBean() {
        super();
        listRecaps = new RecapitulatifEntrepriseSearchModel();

        passageModuleComplexModel = new PassageModuleComplexModel();
        searchPassageModuleComplexModel = new PassageModuleComplexSearchModel();
        templateTraitementComplexModel = new TemplateTraitementListComplexModel();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        return templateTraitementComplexModel.getProcessusPeriodiqueModel().getIdProcessusPeriodique();
    }

    public RecapitulatifEntrepriseSearchModel getListRecaps() {
        return listRecaps;
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (templateTraitementComplexModel.getProcessusPeriodiqueModel() != null)
                && !templateTraitementComplexModel.getProcessusPeriodiqueModel().isNew() ? new BSpy(
                templateTraitementComplexModel.getProcessusPeriodiqueModel().getSpy()) : new BSpy(getSession());
    }

    public TemplateTraitementListComplexModel getTemplateTraitementComplexModel() {
        return templateTraitementComplexModel;
    }

    @Override
    public void retrieve() throws Exception {

        TemplateTraitementListComplexSearchModel searchTemplateTraitement = new TemplateTraitementListComplexSearchModel();
        searchTemplateTraitement.setForIdProcessusPeriodique(getId());
        searchTemplateTraitement = ALServiceLocator.getTemplateTraitementListComplexModelService().search(
                searchTemplateTraitement);

        templateTraitementComplexModel = (TemplateTraitementListComplexModel) searchTemplateTraitement
                .getSearchResults()[0];

        if (!JadeNumericUtil.isEmptyOrZero(templateTraitementComplexModel.getProcessusPeriodiqueModel()
                .getIdPassageFactu())) {
            setPassageModuleComplexModel(FABusinessServiceLocator.getPassageModuleComplexModelService().read(
                    templateTraitementComplexModel.getProcessusPeriodiqueModel().getIdPassageFactu()));

        }

        searchPassageModuleComplexModel.setForNotTypeFacturation(FACSPassage.TYPE_FACTU_PERIODIQUE);
        searchPassageModuleComplexModel.setForStatus(FACSPassage.STATUS_OUVERT);

        HashSet<String> setTypeModuleAF = new HashSet<String>();
        setTypeModuleAF.add(FACSModule.TYPE_MODULE_PRESTATIONS_AF);
        setTypeModuleAF.add(FACSModule.TYPE_MODULE_PRESTATIONS_AF_PARITAIRE);
        setTypeModuleAF.add(FACSModule.TYPE_MODULE_PRESTATIONS_AF_PERSONNEL);
        searchPassageModuleComplexModel.setInPassageTypeModule(setTypeModuleAF);

        searchPassageModuleComplexModel = FABusinessServiceLocator.getPassageModuleComplexModelService().search(
                searchPassageModuleComplexModel);
    }

    @Override
    public void setId(String newId) {
        templateTraitementComplexModel.getProcessusPeriodiqueModel().setId(newId);

    }

    public void setListRecaps(RecapitulatifEntrepriseSearchModel listRecaps) {
        this.listRecaps = listRecaps;
    }

    public void setTemplateTraitementComplexModel(TemplateTraitementListComplexModel templateTraitementComplexModel) {
        this.templateTraitementComplexModel = templateTraitementComplexModel;
    }

    @Override
    public void update() throws Exception {

        ALServiceLocator.getProcessusPeriodiqueModelService().update(
                templateTraitementComplexModel.getProcessusPeriodiqueModel());
    }

    public PassageModuleComplexModel getPassageModuleComplexModel() {
        return passageModuleComplexModel;
    }

    public void setPassageModuleComplexModel(PassageModuleComplexModel passageModuleComplexModel) {
        this.passageModuleComplexModel = passageModuleComplexModel;
    }

}
