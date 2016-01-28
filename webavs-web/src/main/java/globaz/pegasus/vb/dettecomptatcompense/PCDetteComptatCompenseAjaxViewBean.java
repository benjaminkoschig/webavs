package globaz.pegasus.vb.dettecomptatcompense;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;
import com.google.gson.Gson;

public class PCDetteComptatCompenseAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<SimpleDetteComptatCompense, SimpleDetteComptatCompenseSearch> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDetteComptatCompense currentEntity = null;
    private String descriptionSection = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private boolean isCompense = false;
    private Map<String, String> map = null;
    private transient SimpleDetteComptatCompenseSearch search = null;

    public PCDetteComptatCompenseAjaxViewBean() {
        currentEntity = new SimpleDetteComptatCompense();
        initList();
        map = new HashMap<String, String>();
    }

    @Override
    public void add() throws Exception {
        super.add();
        peupleMapDecompt();
    }

    @Override
    public SimpleDetteComptatCompense getCurrentEntity() {
        return currentEntity;
    }

    private String getDescriptionCompteAnnexe(final String idSection) throws Exception {

        OldPersistence<String> per = new OldPersistence<String>() {
            @Override
            public String action() throws Exception {
                SectionSimpleModel section = CABusinessServiceLocator.getSectionService().readSection(idSection);

                CompteAnnexeSimpleModel compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().read(
                        section.getIdCompteAnnexe());

                String role = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(compteAnnexe.getIdRole());

                return compteAnnexe.getDescription() + " (" + role + ")";

            }
        };
        return per.execute();
    }

    public String getDescriptionSection() {
        return descriptionSection;
    }

    public String getDescriptionSection(final String idSection) throws Exception {
        final String compteaAnnexe = getDescriptionCompteAnnexe(idSection);
        OldPersistence<String> per = new OldPersistence<String>() {
            @Override
            public String action() throws Exception {
                return compteaAnnexe + " - " + CABusinessServiceLocator.getSectionService().findDescription(idSection);
            }
        };

        String desc = per.execute();
        return desc;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public boolean getIsCompense() {
        return isCompense;
    }

    public String getJsonInfoDecompt() {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @Override
    public SimpleDetteComptatCompenseSearch getSearchModel() {
        return search;
    }

    @Override
    public JadeCrudService<SimpleDetteComptatCompense, SimpleDetteComptatCompenseSearch> getService()
            throws JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getDetteComptatCompenseService();
    }

    @Override
    public void initList() {
        search = new SimpleDetteComptatCompenseSearch();
    }

    private void peupleMapDecompt() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            DecompteTotalPcVO decompte = PegasusServiceLocator.getDecompteService().getDecompteTotalPCA(idVersionDroit);
            map.put("montantCreance", new FWCurrency(decompte.getCreanciers().getTotal().toString()).toStringFormat());
            map.put("montantDette", new FWCurrency(decompte.getDettesCompta().getTotal().toString()).toStringFormat());
            map.put("montantDeduction", new FWCurrency(decompte.getSousTotalDeduction().toString()).toStringFormat());
            map.put("montantRetro", new FWCurrency(decompte.getSousTotalPCA().toString()).toStringFormat());
            map.put("montantSold", new FWCurrency(decompte.getTotal().toString()).toStringFormat());
        }
    }

    private void read() throws Exception {
        setCurrentEntity(PegasusServiceLocator.getDetteComptatCompenseService().read(
                getCurrentEntity().getId().split("_")[0], getCurrentEntity().getId().split("_")[1],
                getCurrentEntity().getId().split("_")[2]));
        descriptionSection = this.getDescriptionSection(currentEntity.getIdSectionDetteEnCompta());
    }

    @Override
    public void retrieve() throws Exception {
        if (getCurrentEntity().getId().split("_").length == 3) {
            read();
        }
        if (!JadeStringUtil.isBlankOrZero(currentEntity.getId())) {
            isCompense = true;
        }
    }

    @Override
    public void setCurrentEntity(SimpleDetteComptatCompense entite) {
        currentEntity = entite;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsCompense(boolean isCompense) {
        this.isCompense = isCompense;
    }

    @Override
    public void setSearchModel(SimpleDetteComptatCompenseSearch jadeAbstractSearchModel) {
        search = jadeAbstractSearchModel;
    }

    @Override
    public void update() throws Exception {
        if (isCompense) {
            if (JadeStringUtil.isBlankOrZero(currentEntity.getId())) {
                super.add();
            } else {
                super.update();
            }
            currentEntity.setId(currentEntity.getIdSectionDetteEnCompta() + "_" + search.getForIdVersionDroit() + "_"
                    + search.getForIdDroit());
        } else {
            if (!isCompense && !JadeStringUtil.isBlankOrZero(currentEntity.getId())) {
                String idSection = currentEntity.getIdSectionDetteEnCompta();
                // On fait se premier read pour trouver le spy car on peut avoir une erreur qui nous dit que notre
                // entité est new
                currentEntity = PegasusServiceLocator.getDetteComptatCompenseService().read(currentEntity.getId());
                super.delete();
                isCompense = false;
                setCurrentEntity(PegasusServiceLocator.getDetteComptatCompenseService().read(idSection,
                        search.getForIdVersionDroit(), search.getForIdDroit()));
            }
        }
        peupleMapDecompt();
    }
}
