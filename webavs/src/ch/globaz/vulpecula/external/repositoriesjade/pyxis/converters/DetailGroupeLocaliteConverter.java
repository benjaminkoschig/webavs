package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.GroupeLocalite;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteComplexModel;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteSearchSimpleModel;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteSimpleModel;
import ch.globaz.vulpecula.external.models.GroupeLocaliteSimpleModel;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class DetailGroupeLocaliteConverter implements
        DomaineConverterJade<DetailGroupeLocalite, DetailGroupeLocaliteComplexModel, DetailGroupeLocaliteSimpleModel> {

    @Override
    public DetailGroupeLocalite convertToDomain(DetailGroupeLocaliteComplexModel model) {
        DetailGroupeLocaliteSimpleModel detailGroupeLocaliteSimpleModel = model.getDetailGroupeLocaliteSimpleModel();
        GroupeLocaliteSimpleModel groupeLocaliteSimpleModel = model.getGroupeLocaliteSimpleModel();

        DetailGroupeLocalite detailGroupeLocalite = convertToDomain(detailGroupeLocaliteSimpleModel);
        GroupeLocalite groupeLocalite = GroupeLocaliteConverter.getInstance()
                .convertToDomain(groupeLocaliteSimpleModel);

        detailGroupeLocalite.setGroupeLocalite(groupeLocalite);

        return detailGroupeLocalite;
    }

    @Override
    public DetailGroupeLocaliteSimpleModel convertToPersistence(DetailGroupeLocalite entity) {
        DetailGroupeLocaliteSimpleModel detailGroupeLocaliteSimpleModel = new DetailGroupeLocaliteSimpleModel();

        detailGroupeLocaliteSimpleModel.setId(entity.getId());
        detailGroupeLocaliteSimpleModel.setIdGroupeLocalite(entity.getGroupeLocalite().getId());
        detailGroupeLocaliteSimpleModel.setIdLocalite(entity.getIdLocalite());
        detailGroupeLocaliteSimpleModel.setSpy(entity.getSpy());

        return detailGroupeLocaliteSimpleModel;
    }

    @Override
    public DetailGroupeLocalite convertToDomain(DetailGroupeLocaliteSimpleModel simpleModel) {
        DetailGroupeLocalite detailGroupeLocalite = new DetailGroupeLocalite();

        GroupeLocalite groupeLocalite = new GroupeLocalite();
        groupeLocalite.setId(simpleModel.getIdGroupeLocalite());
        detailGroupeLocalite.setId(simpleModel.getId());
        detailGroupeLocalite.setGroupeLocalite(groupeLocalite);
        detailGroupeLocalite.setIdLocalite(simpleModel.getIdLocalite());
        detailGroupeLocalite.setSpy(simpleModel.getSpy());

        return detailGroupeLocalite;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new DetailGroupeLocaliteSearchSimpleModel();
    }

}
