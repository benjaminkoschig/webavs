package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Mod�le regroupant une cat�gorie de tarif � la l�gislation � laquelle elle appartient
 * 
 * @author jts
 */
public class CategorieTarifComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Mod�le de la cat�gorie de tarif (root model)
     */
    private CategorieTarifModel categorieTarifModel = null;
    /**
     * Mod�le de la l�gislation � laquelle appartient la cat�gorie
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
