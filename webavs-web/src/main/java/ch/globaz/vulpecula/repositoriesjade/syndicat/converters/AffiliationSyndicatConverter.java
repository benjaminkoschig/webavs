package ch.globaz.vulpecula.repositoriesjade.syndicat.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatSearchSimpleModel;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.TravailleurConverter;

public class AffiliationSyndicatConverter implements
        DomaineConverterJade<AffiliationSyndicat, AffiliationSyndicatComplexModel, AffiliationSyndicatSimpleModel> {

    private static final AffiliationSyndicatConverter INSTANCE = new AffiliationSyndicatConverter();

    public static AffiliationSyndicatConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public AffiliationSyndicat convertToDomain(AffiliationSyndicatComplexModel affiliationSyndicatComplexModel) {
        AffiliationSyndicatSimpleModel affiliationSyndicatSimpleModel = affiliationSyndicatComplexModel
                .getAffiliationSyndicatSimpleModel();
        TravailleurComplexModel travailleurComplexModel = affiliationSyndicatComplexModel.getTravailleurComplexModel();
        AdministrationComplexModel administrationComplexModel = affiliationSyndicatComplexModel
                .getAdministrationComplexModel();

        AffiliationSyndicat affiliationSyndicat = convertToDomain(affiliationSyndicatSimpleModel);
        affiliationSyndicat.setTravailleur(TravailleurConverter.getInstance().convertToDomain(travailleurComplexModel));
        affiliationSyndicat.setSyndicat(AdministrationConverter.convertToDomain(administrationComplexModel));
        return affiliationSyndicat;
    }

    @Override
    public AffiliationSyndicatSimpleModel convertToPersistence(AffiliationSyndicat affiliationSyndicat) {
        AffiliationSyndicatSimpleModel affiliationSyndicatSimpleModel = new AffiliationSyndicatSimpleModel();
        affiliationSyndicatSimpleModel.setId(affiliationSyndicat.getId());
        affiliationSyndicatSimpleModel.setDateDebut(affiliationSyndicat.getDateDebutAsSwissValue());
        affiliationSyndicatSimpleModel.setDateFin(affiliationSyndicat.getDateFinAsSwissValue());
        affiliationSyndicatSimpleModel.setIdTravailleur(affiliationSyndicat.getIdTravailleur());
        affiliationSyndicatSimpleModel.setIdSyndicat(affiliationSyndicat.getIdSyndicat());
        affiliationSyndicatSimpleModel.setSpy(affiliationSyndicat.getSpy());
        return affiliationSyndicatSimpleModel;
    }

    @Override
    public AffiliationSyndicat convertToDomain(AffiliationSyndicatSimpleModel affiliationSyndicatSimpleModel) {
        AffiliationSyndicat affiliationSyndicat = new AffiliationSyndicat();
        affiliationSyndicat.setId(affiliationSyndicatSimpleModel.getId());
        Date dateDebut = new Date(affiliationSyndicatSimpleModel.getDateDebut());
        Date dateFin = null;
        String dateFinAsString = affiliationSyndicatSimpleModel.getDateFin();
        if (!JadeNumericUtil.isEmptyOrZero(dateFinAsString)) {
            dateFin = new Date(dateFinAsString);
        }
        affiliationSyndicat.setPeriode(new Periode(dateDebut, dateFin));
        affiliationSyndicat.setSpy(affiliationSyndicatSimpleModel.getSpy());
        return affiliationSyndicat;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new AffiliationSyndicatSearchSimpleModel();
    }

}
