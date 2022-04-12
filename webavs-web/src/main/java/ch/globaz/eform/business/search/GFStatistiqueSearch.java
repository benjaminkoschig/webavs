package ch.globaz.eform.business.search;

import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class GFStatistiqueSearch extends JadeSearchSimpleModel {
    public static String WHERE_DEFINITION_STATISTIQUE_BETWEEN = "statistiqueBetween";
    public static String WHERE_DEFINITION_STATISTIQUE_AFTER = "statistiqueAfter";
    public static String WHERE_DEFINITION_STATISTIQUE_BEFORE = "statistiqueBefore";

    private String byStartDate = null;

    private String byEndDate = null;


    public GFStatistiqueSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    public String getByStartDate() {
        return byStartDate;
    }

    public void setByStartDate(String byStartDate) {
        this.byStartDate = byStartDate;
    }

    public String getByEndDate() {
        return byEndDate;
    }

    public void setByEndDate(String byEndDate) {
        this.byEndDate = byEndDate;
    }

    public String getOrderBy() {
        return getOrderKey();
    }

    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class<GFFormulaireModel> whichModelClass() {
        return GFFormulaireModel.class;
    }
}
