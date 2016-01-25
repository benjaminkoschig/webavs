package ch.globaz.vulpecula.repositoriesjade.is.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.is.HistoriqueProcessusAfSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.HistoriqueProcessusAfSimpleModel;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * Converter HistoriqueProcessusAf
 * 
 * @author JPA
 * 
 */
public final class HistoriqueProcessusAfConverter implements
        DomaineConverterJade<HistoriqueProcessusAf, JadeComplexModel, HistoriqueProcessusAfSimpleModel> {

    private static final HistoriqueProcessusAfConverter INSTANCE = new HistoriqueProcessusAfConverter();

    public static HistoriqueProcessusAfConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public HistoriqueProcessusAfSimpleModel convertToPersistence(HistoriqueProcessusAf historique) {
        HistoriqueProcessusAfSimpleModel simpleModel = new HistoriqueProcessusAfSimpleModel();
        simpleModel.setId(historique.getId());
        simpleModel.setIdProcessus(historique.getIdProcessus());
        simpleModel.setSpy(historique.getSpy());
        return simpleModel;
    }

    @Override
    public HistoriqueProcessusAf convertToDomain(HistoriqueProcessusAfSimpleModel simpleModel) {
        HistoriqueProcessusAf historique = new HistoriqueProcessusAf();
        historique.setId(simpleModel.getId());
        historique.setIdProcessus(simpleModel.getIdProcessus());
        historique.setSpy(simpleModel.getSpy());
        return historique;
    }

    @Override
    public HistoriqueProcessusAf convertToDomain(JadeComplexModel model) {
        return null;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new HistoriqueProcessusAfSearchSimpleModel();
    }

}
