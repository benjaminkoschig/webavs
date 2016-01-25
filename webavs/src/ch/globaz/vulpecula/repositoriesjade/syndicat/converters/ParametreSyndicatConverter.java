package ch.globaz.vulpecula.repositoriesjade.syndicat.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatSearchSimpleModel;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class ParametreSyndicatConverter implements
        DomaineConverterJade<ParametreSyndicat, ParametreSyndicatComplexModel, ParametreSyndicatSimpleModel> {

    private static final ParametreSyndicatConverter INSTANCE = new ParametreSyndicatConverter();

    public static ParametreSyndicatConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public ParametreSyndicat convertToDomain(ParametreSyndicatComplexModel parametreSyndicatComplexModel) {
        ParametreSyndicatSimpleModel parametreSyndicatSimpleModel = parametreSyndicatComplexModel
                .getParametreSyndicatSimpleModel();
        AdministrationComplexModel administrationSyndicatComplexModel = parametreSyndicatComplexModel
                .getAdministrationSyndicatComplexModel();
        AdministrationComplexModel administrationConventionComplexModel = parametreSyndicatComplexModel
                .getAdministrationCaisseMetierComplexModel();
        Administration syndicat = AdministrationConverter.convertToDomain(administrationSyndicatComplexModel);
        Administration caisseMetier = AdministrationConverter.convertToDomain(administrationConventionComplexModel);

        ParametreSyndicat parametreSyndicat = convertToDomain(parametreSyndicatSimpleModel);
        parametreSyndicat.setSyndicat(syndicat);
        parametreSyndicat.setCaisseMetier(caisseMetier);
        return parametreSyndicat;
    }

    @Override
    public ParametreSyndicatSimpleModel convertToPersistence(ParametreSyndicat parametreSyndicat) {
        ParametreSyndicatSimpleModel parametreSyndicatSimpleModel = new ParametreSyndicatSimpleModel();
        parametreSyndicatSimpleModel.setId(parametreSyndicat.getId());
        parametreSyndicatSimpleModel.setIdSyndicat(parametreSyndicat.getIdSyndicat());
        parametreSyndicatSimpleModel.setIdCaisseMetier(parametreSyndicat.getIdCaisseMetier());
        parametreSyndicatSimpleModel.setPourcentage(parametreSyndicat.getPourcentage().getValue());
        parametreSyndicatSimpleModel.setMontantParTravailleur(parametreSyndicat.getMontantParTravailleur().getValue());
        parametreSyndicatSimpleModel.setDateDebut(parametreSyndicat.getDateDebut().getSwissValue());
        if (parametreSyndicat.getDateFin() != null) {
            parametreSyndicatSimpleModel.setDateFin(parametreSyndicat.getDateFin().getSwissValue());
        }
        parametreSyndicatSimpleModel.setSpy(parametreSyndicat.getSpy());
        return parametreSyndicatSimpleModel;
    }

    @Override
    public ParametreSyndicat convertToDomain(ParametreSyndicatSimpleModel parametreSyndicatSimpleModel) {
        ParametreSyndicat parametreSyndicat = new ParametreSyndicat();
        parametreSyndicat.setId(parametreSyndicatSimpleModel.getId());
        parametreSyndicat.setPourcentage(new Taux(parametreSyndicatSimpleModel.getPourcentage()));
        parametreSyndicat
                .setMontantParTravailleur(new Montant(parametreSyndicatSimpleModel.getMontantParTravailleur()));
        parametreSyndicat.setDateDebut(new Date(parametreSyndicatSimpleModel.getDateDebut()));
        String dateFin = parametreSyndicatSimpleModel.getDateFin();
        if (!JadeNumericUtil.isEmptyOrZero(dateFin)) {
            parametreSyndicat.setDateFin(new Date(parametreSyndicatSimpleModel.getDateFin()));
        }
        parametreSyndicat.setSpy(parametreSyndicatSimpleModel.getSpy());
        return parametreSyndicat;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ParametreSyndicatSearchSimpleModel();
    }

}
