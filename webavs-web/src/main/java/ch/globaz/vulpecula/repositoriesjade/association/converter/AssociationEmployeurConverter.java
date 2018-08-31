package ch.globaz.vulpecula.repositoriesjade.association.converter;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationEmployeurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.AssociationEmployeurSimpleModel;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AssociationEmployeurConverter implements
        DomaineConverterJade<AssociationEmployeur, JadeComplexModel, AssociationEmployeurSimpleModel> {
    private static final AssociationEmployeurConverter INSTANCE = new AssociationEmployeurConverter();

    public static AssociationEmployeurConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public AssociationEmployeurSimpleModel convertToPersistence(AssociationEmployeur associationEmployeur) {
        AssociationEmployeurSimpleModel associationEmployeurSimpleModel = new AssociationEmployeurSimpleModel();
        associationEmployeurSimpleModel.setId(associationEmployeur.getId());
        associationEmployeurSimpleModel.setIdEmployeur(associationEmployeur.getIdEmployeur());
        associationEmployeurSimpleModel.setIdAssociation(associationEmployeur.getIdAssociation());
        associationEmployeurSimpleModel.setMasseAssociation(associationEmployeur.getMasseAssociation().getValue());
        return associationEmployeurSimpleModel;
    }

    @Override
    public AssociationEmployeur convertToDomain(AssociationEmployeurSimpleModel associationEmployeurSimpleModel) {
        AssociationEmployeur associationEmployeur = new AssociationEmployeur();
        associationEmployeur.setId(associationEmployeurSimpleModel.getId());
        associationEmployeur.setIdEmployeur(associationEmployeurSimpleModel.getIdEmployeur());
        associationEmployeur.setIdAssociation(associationEmployeurSimpleModel.getIdAssociation());

        if (!JadeStringUtil.isEmpty(associationEmployeurSimpleModel.getMasseAssociation())) {
            associationEmployeur
                    .setMasseAssociation(new Montant(associationEmployeurSimpleModel.getMasseAssociation()));
        }
        associationEmployeur.setSpy(associationEmployeurSimpleModel.getSpy());

        return associationEmployeur;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new AssociationEmployeurSearchSimpleModel();
    }

    @Override
    public AssociationEmployeur convertToDomain(JadeComplexModel model) {
        return null;
    }

}
