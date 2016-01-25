package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.domain.models.common.GroupeLocalite;
import ch.globaz.vulpecula.external.models.GroupeLocaliteSimpleModel;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class GroupeLocaliteConverter implements
        DomaineConverterJade<GroupeLocalite, JadeComplexModel, GroupeLocaliteSimpleModel> {
    private static final GroupeLocaliteConverter INSTANCE = new GroupeLocaliteConverter();

    public static GroupeLocaliteConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public GroupeLocalite convertToDomain(JadeComplexModel model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GroupeLocaliteSimpleModel convertToPersistence(GroupeLocalite entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GroupeLocalite convertToDomain(GroupeLocaliteSimpleModel simpleModel) {
        GroupeLocalite groupeLocalite = new GroupeLocalite();

        groupeLocalite.setId(simpleModel.getId());
        groupeLocalite.setNoGroupe(simpleModel.getNoGroupe());
        groupeLocalite.setNomGroupeFR(simpleModel.getNomGroupeFR());
        groupeLocalite.setNomGroupeDE(simpleModel.getNomGroupeDE());
        groupeLocalite.setNomGroupeIT(simpleModel.getNomGroupeIT());
        groupeLocalite.setTypeGroupe(simpleModel.getTypeGroupe());
        groupeLocalite.setSpy(simpleModel.getSpy());

        return groupeLocalite;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        throw new UnsupportedOperationException();
    }

}
