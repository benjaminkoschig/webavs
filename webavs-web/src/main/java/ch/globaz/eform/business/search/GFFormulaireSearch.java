package ch.globaz.eform.business.search;

import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class GFFormulaireSearch extends JadeSearchSimpleModel {
    public static String WHERE_DEFINITION_FORMULAIRE = "formulaire";

    //Id technique
    private String byId = null;

    private String byGestionnaire = null;
    private String byStatus = null;
    private String byType = null;
    private String byDate = null;
    private String byMessageId = null;
    private String byLastName = null;
    private String byFirstName = null;
    private String likeNss = null;

    public GFFormulaireSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    public String getById() {
        return byId;
    }

    public void setById(String byId) {
        this.byId = byId;
    }

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

    public String getByType() {
        return byType;
    }

    public void setByType(String byType) {
        this.byType = byType;
    }

    public String getByDate() {
        return byDate;
    }

    public void setByDate(String byDate) {
        this.byDate = byDate;
    }

    public String getByMessageId() {
        return byMessageId;
    }

    public void setByMessageId(String byMessageId) {
        this.byMessageId = byMessageId;
    }

    public String getByLastName() {
        return byLastName;
    }

    public void setByLastName(String byLastName) {
        this.byLastName = byLastName;
    }

    public String getByFirstName() {
        return byFirstName;
    }

    public void setByFirstName(String byFirstName) {
        this.byFirstName = byFirstName;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
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
