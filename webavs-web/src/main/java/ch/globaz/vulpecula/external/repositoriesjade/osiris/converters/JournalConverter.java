package ch.globaz.vulpecula.external.repositoriesjade.osiris.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.osiris.Journal;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class JournalConverter implements DomaineConverterJade<Journal, JadeComplexModel, JournalSimpleModel> {
    private static final JournalConverter INSTANCE = new JournalConverter();

    public static JournalConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Journal convertToDomain(JadeComplexModel model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JournalSimpleModel convertToPersistence(Journal journal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Journal convertToDomain(JournalSimpleModel journalSimpleModel) {
        Journal journal = new Journal();
        journal.setId(journalSimpleModel.getId());
        if (!JadeStringUtil.isEmpty(journalSimpleModel.getDateValeurCG())) {
            journal.setDateComptabilisation(new Date(journalSimpleModel.getDateValeurCG()));
        }
        journal.setSpy(journalSimpleModel.getSpy());
        return journal;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        throw new UnsupportedOperationException();
    }

}
