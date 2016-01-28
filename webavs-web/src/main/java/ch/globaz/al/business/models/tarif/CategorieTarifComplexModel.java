package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle regroupant une catégorie de tarif à la législation à laquelle elle appartient
 * 
 * @author jts
 */
public class CategorieTarifComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle de la catégorie de tarif (root model)
     */
    private CategorieTarifModel categorieTarifModel = null;
    /**
     * Modèle de la législation à laquelle appartient la catégorie
     */
    private LegislationTarifModel legislationTarifModel = null;

    /**
     * Constructeur
     */
    public CategorieTarifComplexModel() {
        super();
        categorieTarifModel = new CategorieTarifModel();
        legislationTarifModel = new LegislationTarifModel();
    }

    /**
     * @return the categorieTarifModel
     */
    public CategorieTarifModel getCategorieTarifModel() {
        return categorieTarifModel;
    }

    @Override
    public String getId() {
        return categorieTarifModel.getId();
    }

    /**
     * @return the legislationTarifModel
     */
    public LegislationTarifModel getLegislationTarifModel() {
        return legislationTarifModel;
    }

    @Override
    public String getSpy() {
        return categorieTarifModel.getSpy();
    }

    /**
     * @param categorieTarifModel
     *            the categorieTarifModel to set
     */
    public void setCategorieTarifModel(CategorieTarifModel categorieTarifModel) {
        this.categorieTarifModel = categorieTarifModel;
    }

    @Override
    public void setId(String id) {
        categorieTarifModel.setId(id);
    }

    /**
     * @param legislationTarifModel
     *            the legislationTarifModel to set
     */
    public void setLegislationTarifModel(LegislationTarifModel legislationTarifModel) {
        this.legislationTarifModel = legislationTarifModel;
    }

    @Override
    public void setSpy(String spy) {
        categorieTarifModel.setSpy(spy);
    }
}
