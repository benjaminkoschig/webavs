package ch.globaz.vulpecula.repositoriesjade.caissemaladie.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieSearchSimpleModel;
import ch.globaz.vulpecula.business.models.caissemaladie.SuiviCaisseMaladieSimpleModel;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.models.caissemaladie.TypeDocumentCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.TravailleurConverter;

public class SuiviCaisseMaladieConverter implements
        DomaineConverterJade<SuiviCaisseMaladie, SuiviCaisseMaladieComplexModel, SuiviCaisseMaladieSimpleModel> {

    @Override
    public SuiviCaisseMaladie convertToDomain(SuiviCaisseMaladieComplexModel model) {
        Travailleur travailleur = TravailleurConverter.getInstance()
                .convertToDomain(model.getTravailleurComplexModel());
        Administration caisseMaladie = AdministrationConverter.convertToDomain(model.getAdministrationComplexModel());
        SuiviCaisseMaladie suivi = new SuiviCaisseMaladie();
        suivi.setTravailleur(travailleur);
        suivi.setCaisseMaladie(caisseMaladie);
        suivi.setDateEnvoi(new Date(model.getSuiviCaisseMaladieSimpleModel().getDateEnvoi()));
        suivi.setEnvoye(model.getSuiviCaisseMaladieSimpleModel().getIsEnvoye());
        suivi.setId(model.getSuiviCaisseMaladieSimpleModel().getId());
        suivi.setSpy(model.getSpy());
        suivi.setTypeDocument(TypeDocumentCaisseMaladie.fromValue(model.getSuiviCaisseMaladieSimpleModel()
                .getTypeDocument()));

        return suivi;
    }

    @Override
    public SuiviCaisseMaladieSimpleModel convertToPersistence(SuiviCaisseMaladie entity) {
        SuiviCaisseMaladieSimpleModel simpleModel = new SuiviCaisseMaladieSimpleModel();
        simpleModel.setId(entity.getId());
        simpleModel.setIdTravailleur(entity.getIdTravailleur());
        simpleModel.setIdCaisseMaladie(entity.getCaisseMaladie().getId());
        simpleModel.setIsEnvoye(entity.isEnvoye());
        simpleModel.setTypeDocument(entity.getTypeDocument().getValue());
        if (entity.getDateEnvoi() != null) {
            simpleModel.setDateEnvoi(entity.getDateEnvoi().getSwissValue());
        }
        simpleModel.setSpy(entity.getSpy());
        return simpleModel;
    }

    @Override
    public SuiviCaisseMaladie convertToDomain(SuiviCaisseMaladieSimpleModel simpleModel) {
        SuiviCaisseMaladie entity = new SuiviCaisseMaladie();
        entity.setId(simpleModel.getId());

        entity.setDateEnvoi(new Date(simpleModel.getDateEnvoi()));
        // entity.setCaisseMaladie(caisseMaladie);
        // entity.setTravailleur(travailleur);
        entity.setEnvoye(simpleModel.getIsEnvoye());
        entity.setTypeDocument(TypeDocumentCaisseMaladie.fromValue(simpleModel.getTypeDocument()));

        entity.setSpy(simpleModel.getSpy());
        return entity;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new SuiviCaisseMaladieSearchSimpleModel();
    }

}
