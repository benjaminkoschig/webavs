package globaz.eform.vb.statistique;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.GFStatistiqueModel;
import ch.globaz.eform.business.search.GFStatistiqueSearch;
import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.constant.GFTypeEForm;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GFStatistiqueListViewBean extends BJadePersistentObjectListViewBean {
    private GFStatistiqueSearch statistiqueSearch;
    private List<GFStatistiqueModel> statistiqueList;

    public GFStatistiqueListViewBean() {
        super();
        statistiqueSearch = new GFStatistiqueSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return statistiqueSearch;
    }


    @Override
    public void find() throws Exception {
        if (Objects.isNull(statistiqueSearch.getByStartDate()) && !Objects.isNull(statistiqueSearch.getByEndDate())) {
            statistiqueSearch.setWhereKey(GFStatistiqueSearch.WHERE_DEFINITION_STATISTIQUE_BEFORE);
        } else if (Objects.isNull(statistiqueSearch.getByEndDate()) && !Objects.isNull(statistiqueSearch.getByStartDate())) {
            statistiqueSearch.setWhereKey(GFStatistiqueSearch.WHERE_DEFINITION_STATISTIQUE_AFTER);
        } else if (!(Objects.isNull(statistiqueSearch.getByStartDate()) || Objects.isNull(statistiqueSearch.getByEndDate()))) {
            statistiqueSearch.setWhereKey(GFStatistiqueSearch.WHERE_DEFINITION_STATISTIQUE_BETWEEN);
        }
        statistiqueSearch = GFEFormServiceLocator.getGFEFormDBService().search(statistiqueSearch);
        calculStatistique();
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < statistiqueSearch.getSize() ? new GFStatistiqueViewBean(
                (GFFormulaireModel) statistiqueSearch.getSearchResults()[idx]) : new GFStatistiqueViewBean();
    }

    public String getByStartDate() {
        return statistiqueSearch.getByStartDate();
    }

    public void setByStartDate(String byStartDate) {
        statistiqueSearch.setByStartDate(byStartDate);
    }

    public String getByEndDate() {
        return statistiqueSearch.getByEndDate();
    }

    public void setByEndDate(String byEndDate) {
        statistiqueSearch.setByEndDate(byEndDate);
    }

    public String getOrderBy() {
        return statistiqueSearch.getOrderBy();
    }

    public void setOrderBy(String orderBy) {
        statistiqueSearch.setOrderKey(orderBy);
    }

    public GFStatistiqueModel getStatistiqueEntity(int idx) {
        return statistiqueList.get(idx);
    }

    public int getStatistiqueListSize() {
        return statistiqueList.size();
    }

    private void calculStatistique() {
        if (Objects.isNull(statistiqueList)) {
            statistiqueList = new ArrayList<>();
        } else {
            statistiqueList.clear();
        }

        //Initialisation de la liste de calcul
        Arrays.stream(GFTypeEForm.values()).forEach(type -> {
            GFStatistiqueModel statistiqueModel = new GFStatistiqueModel(type);
            Arrays.stream(GFStatusEForm.values())
                    .filter(status -> !status.equals(GFStatusEForm.BLANK))
                    .forEach(status -> statistiqueModel.getRecensement().put(status, 0));
            statistiqueList.add(statistiqueModel);
        });

        //comptage des formulaires par rapport à son type et son statut
        Arrays.stream(this.statistiqueSearch.getSearchResults())
                .map(o -> (GFFormulaireModel) o)
                .forEach(formulaire -> statistiqueList.stream()
                        //Recherche de la statistique par le type du formulaire
                        .filter(model -> model.getId().equals(formulaire.getSubject()))
                        //Recherche de la statistique par le statut et ajout au compteur du formulaire
                        .forEach(model -> model.addStatus(GFStatusEForm.getStatusByCodeSystem(formulaire.getStatus())))
        );
    }
}
