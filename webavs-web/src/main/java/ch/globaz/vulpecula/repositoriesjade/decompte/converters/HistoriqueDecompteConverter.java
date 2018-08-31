package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * Convertisseur d'objet {@link HistoriqueDecompte} <--> {@link HistoriqueDecompteComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class HistoriqueDecompteConverter implements
        DomaineConverterJade<HistoriqueDecompte, HistoriqueDecompteComplexModel, HistoriqueDecompteSimpleModel> {

    private static final DecompteConverter DECOMPTE_CONVERTER = new DecompteConverter();
    private static final HistoriqueDecompteConverter INSTANCE = new HistoriqueDecompteConverter();

    public static HistoriqueDecompteConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public HistoriqueDecompteSimpleModel convertToPersistence(final HistoriqueDecompte historiqueDecompte) {
        HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel = new HistoriqueDecompteSimpleModel();

        historiqueDecompteSimpleModel.setId(historiqueDecompte.getId());
        if (historiqueDecompte.getDecompte() != null) {
            historiqueDecompteSimpleModel.setIdDecompte(String.valueOf(historiqueDecompte.getDecompte().getId()));
        }
        historiqueDecompteSimpleModel.setDate(historiqueDecompte.getDateAsSwissValue());
        historiqueDecompteSimpleModel.setEtat(historiqueDecompte.getEtat().getValue());
        historiqueDecompteSimpleModel.setSpy(historiqueDecompte.getSpy());

        return historiqueDecompteSimpleModel;
    }

    @Override
    public HistoriqueDecompte convertToDomain(final HistoriqueDecompteComplexModel historiqueDecompteComplexModel) {
        DecompteComplexModel decompteComplexModel = historiqueDecompteComplexModel.getDecompteComplexModel();
        HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel = historiqueDecompteComplexModel
                .getHistoriqueDecompteSimpleModel();

        Decompte decompte = DECOMPTE_CONVERTER.convertToDomain(decompteComplexModel);
        HistoriqueDecompte historiqueDecompte = convertToDomain(historiqueDecompteSimpleModel);
        historiqueDecompte.setDecompte(decompte);

        return historiqueDecompte;
    }

    @Override
    public HistoriqueDecompte convertToDomain(final HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();

        historiqueDecompte.setId(historiqueDecompteSimpleModel.getId());
        historiqueDecompte.setDate(new Date(historiqueDecompteSimpleModel.getDate()));
        if (EtatDecompte.isValid(historiqueDecompteSimpleModel.getEtat())) {
            historiqueDecompte.setEtat(EtatDecompte.fromValue(historiqueDecompteSimpleModel.getEtat()));
        }
        historiqueDecompte.setSpy(historiqueDecompteSimpleModel.getSpy());

        return historiqueDecompte;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new HistoriqueDecompteSearchSimpleModel();
    }
}
