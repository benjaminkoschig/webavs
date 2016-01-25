package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;

public class DetailGroupeLocaliteComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 7995355337732632415L;

    private DetailGroupeLocaliteSimpleModel detailGroupeLocaliteSimpleModel;
    private GroupeLocaliteSimpleModel groupeLocaliteSimpleModel;

    public DetailGroupeLocaliteComplexModel() {
        detailGroupeLocaliteSimpleModel = new DetailGroupeLocaliteSimpleModel();
        groupeLocaliteSimpleModel = new GroupeLocaliteSimpleModel();
    }

    @Override
    public String getId() {
        return detailGroupeLocaliteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return detailGroupeLocaliteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        detailGroupeLocaliteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        detailGroupeLocaliteSimpleModel.setSpy(spy);
    }

    public DetailGroupeLocaliteSimpleModel getDetailGroupeLocaliteSimpleModel() {
        return detailGroupeLocaliteSimpleModel;
    }

    public void setDetailGroupeLocaliteSimpleModel(DetailGroupeLocaliteSimpleModel detailGroupeLocaliteSimpleModel) {
        this.detailGroupeLocaliteSimpleModel = detailGroupeLocaliteSimpleModel;
    }

    public GroupeLocaliteSimpleModel getGroupeLocaliteSimpleModel() {
        return groupeLocaliteSimpleModel;
    }

    public void setGroupeLocaliteSimpleModel(GroupeLocaliteSimpleModel groupeLocaliteSimpleModel) {
        this.groupeLocaliteSimpleModel = groupeLocaliteSimpleModel;
    }

}
