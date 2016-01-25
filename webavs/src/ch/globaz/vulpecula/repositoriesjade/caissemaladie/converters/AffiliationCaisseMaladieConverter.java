package ch.globaz.vulpecula.repositoriesjade.caissemaladie.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieComplexModel;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieSearchSimpleModel;
import ch.globaz.vulpecula.business.models.caissemaladie.AffiliationCaisseMaladieSimpleModel;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.TravailleurConverter;

public class AffiliationCaisseMaladieConverter
        implements
        DomaineConverterJade<AffiliationCaisseMaladie, AffiliationCaisseMaladieComplexModel, AffiliationCaisseMaladieSimpleModel> {

    @Override
    public AffiliationCaisseMaladie convertToDomain(AffiliationCaisseMaladieComplexModel model) {
        Travailleur travailleur = TravailleurConverter.getInstance()
                .convertToDomain(model.getTravailleurComplexModel());
        Administration caisseMaladie = AdministrationConverter.convertToDomain(model.getAdministrationComplexModel());
        AffiliationCaisseMaladie affiliationCaisseMaladie = convertToDomain(model
                .getAffiliationCaisseMaladieSimpleModel());
        affiliationCaisseMaladie.setTravailleur(travailleur);
        affiliationCaisseMaladie.setCaisseMaladie(caisseMaladie);

        return affiliationCaisseMaladie;
    }

    @Override
    public AffiliationCaisseMaladieSimpleModel convertToPersistence(AffiliationCaisseMaladie entity) {
        AffiliationCaisseMaladieSimpleModel simpleModel = new AffiliationCaisseMaladieSimpleModel();
        simpleModel.setId(entity.getId());
        simpleModel.setIdTravailleur(entity.getIdTravailleur());
        simpleModel.setMoisDebut(entity.getMoisAnneeDebut());
        simpleModel.setMoisFin(entity.getMoisAnneeFin());
        simpleModel.setIdCaisseMaladie(entity.getCaisseMaladie().getId());
        simpleModel.setIdPosteTravail(entity.getIdPosteTravail());
        if (entity.getDateAnnonceDebut() != null) {
            simpleModel.setDateDebutAnnonce(entity.getDateAnnonceDebut().getSwissValue());
        }
        if (entity.getDateAnnonceFin() != null) {
            simpleModel.setDateFinAnnonce(entity.getDateAnnonceFin().getSwissValue());
        }
        simpleModel.setSpy(entity.getSpy());
        return simpleModel;
    }

    @Override
    public AffiliationCaisseMaladie convertToDomain(AffiliationCaisseMaladieSimpleModel simpleModel) {
        AffiliationCaisseMaladie entity = new AffiliationCaisseMaladie();
        entity.setId(simpleModel.getId());
        entity.setMoisDebut(new Date(simpleModel.getMoisDebut()));
        entity.setIdPosteTravail(simpleModel.getIdPosteTravail());
        if (!JadeNumericUtil.isEmptyOrZero(simpleModel.getMoisFin())) {
            entity.setMoisFin(new Date(simpleModel.getMoisFin()));
        }
        if (!JadeNumericUtil.isEmptyOrZero(simpleModel.getDateDebutAnnonce())) {
            entity.setDateAnnonceDebut(new Date(simpleModel.getDateDebutAnnonce()));
        }
        if (!JadeNumericUtil.isEmptyOrZero(simpleModel.getDateFinAnnonce())) {
            entity.setDateAnnonceFin(new Date(simpleModel.getDateFinAnnonce()));
        }
        entity.setSpy(simpleModel.getSpy());
        return entity;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new AffiliationCaisseMaladieSearchSimpleModel();
    }

}
