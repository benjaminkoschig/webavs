package ch.globaz.vulpecula.external.repositoriesjade.musca.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class PassageConverter implements DomaineConverterJade<Passage, JadeComplexModel, PassageModel> {
    private static final PassageConverter INSTANCE = new PassageConverter();

    public static PassageConverter getInstance() {
        return INSTANCE;
    }

    private PassageConverter() {

    }

    @Override
    public Passage convertToDomain(JadeComplexModel complexModel) {
        return null;
    }

    @Override
    public PassageModel convertToPersistence(Passage passage) {
        return null;
    }

    @Override
    public Passage convertToDomain(PassageModel passageModel) {
        Passage passage = new Passage();
        passage.setId(passageModel.getId());
        passage.setIdTypeFacturation(passageModel.getTypeFacturation());
        passage.setLibelle(passageModel.getLibellePassage());
        passage.setDateFacturation(passageModel.getDateFacturation());
        passage.setStatus(passageModel.getStatus());
        return passage;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return null;
    }

}
