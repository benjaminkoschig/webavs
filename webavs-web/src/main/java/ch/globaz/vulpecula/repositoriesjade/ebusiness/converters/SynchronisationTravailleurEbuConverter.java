package ch.globaz.vulpecula.repositoriesjade.ebusiness.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class SynchronisationTravailleurEbuConverter
        implements
        DomaineConverterJade<SynchronisationTravailleur, SynchronisationTravailleurEbuComplexModel, SynchronisationTravailleurEbuSimpleModel> {

    @Override
    public SynchronisationTravailleur convertToDomain(SynchronisationTravailleurEbuComplexModel complexModel) {
        SynchronisationTravailleurEbuSimpleModel simpleModel = complexModel
                .getSynchronisationTravailleurEbuSimpleModel();
        return convertToDomain(simpleModel);
    }

    @Override
    public SynchronisationTravailleurEbuSimpleModel convertToPersistence(SynchronisationTravailleur sync) {

        SynchronisationTravailleurEbuSimpleModel simpleModel = new SynchronisationTravailleurEbuSimpleModel();
        simpleModel.setId(sync.getId());
        if (sync.getTravailleur() != null) {
            simpleModel.setIdTravailleur(sync.getTravailleur().getId());
        }
        if (sync.getDateAjout() != null) {
            simpleModel.setDateAjout(sync.getDateAjout().getSwissValue());
        }
        if (sync.getDateSynchronisation() != null) {
            simpleModel.setDateSynchronisation(sync.getDateSynchronisation().getSwissValue());
        }

        if (sync.getCorrelationId() != null) {
            simpleModel.setCorrelationId(sync.getCorrelationId());
        }

        if (sync.getPosteCorrelationId() != null) {
            simpleModel.setPosteCorrelationId(sync.getPosteCorrelationId());
        }

        if (sync.getIdAnnonce() != null) {
            simpleModel.setIdAnnonce(sync.getIdAnnonce());
        }

        simpleModel.setSpy(sync.getSpy());

        return simpleModel;
    }

    @Override
    public SynchronisationTravailleur convertToDomain(SynchronisationTravailleurEbuSimpleModel simpleModel) {
        if (JadeStringUtil.isEmpty(simpleModel.getId())) {
            return null;
        }
        SynchronisationTravailleur synchronisation = new SynchronisationTravailleur();
        synchronisation.setId(simpleModel.getId());

        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                simpleModel.getIdTravailleur());
        synchronisation.setTravailleur(travailleur);
        if (!JadeStringUtil.isEmpty(simpleModel.getDateAjout())) {
            synchronisation.setDateAjout(new Date(simpleModel.getDateAjout()));
        }
        if (!JadeStringUtil.isEmpty(simpleModel.getDateSynchronisation())) {
            synchronisation.setDateSynchronisation(new Date(simpleModel.getDateSynchronisation()));
        }
        if (!JadeStringUtil.isEmpty(simpleModel.getPosteCorrelationId())) {
            synchronisation.setPosteCorrelationId(simpleModel.getPosteCorrelationId());
        }
        if (!JadeStringUtil.isEmpty(simpleModel.getCorrelationId())) {
            synchronisation.setCorrelationId(simpleModel.getCorrelationId());
        }
        if (!JadeStringUtil.isEmpty(simpleModel.getIdAnnonce())) {
            synchronisation.setIdAnnonce(simpleModel.getIdAnnonce());
        }
        synchronisation.setSpy(simpleModel.getSpy());
        return synchronisation;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new SynchronisationTravailleurEbuSearchSimpleModel();
    }

}
