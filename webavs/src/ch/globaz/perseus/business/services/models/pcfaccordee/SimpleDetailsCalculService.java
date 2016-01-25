package ch.globaz.perseus.business.services.models.pcfaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalculSearchModel;

public interface SimpleDetailsCalculService extends JadeApplicationService {

    public int count(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel) throws JadePersistenceException,
            PCFAccordeeException;

    public SimpleDetailsCalcul create(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException;

    public SimpleDetailsCalcul delete(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException;

    public int delete(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel) throws JadePersistenceException,
            PCFAccordeeException;

    public SimpleDetailsCalcul read(String idSimpleDetailsCalcul) throws JadePersistenceException, PCFAccordeeException;

    public SimpleDetailsCalculSearchModel search(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel)
            throws JadePersistenceException, PCFAccordeeException;

    public SimpleDetailsCalcul update(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException;

}
