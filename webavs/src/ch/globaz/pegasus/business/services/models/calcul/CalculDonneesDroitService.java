package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDernierePCASearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroit;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch;

public interface CalculDonneesDroitService extends JadeApplicationService {

    public CalculDernierePCASearch calculDernierePCASearch(CalculDernierePCASearch calculDernierePCASearch)
            throws JadePersistenceException, CalculException;

    public int count(CalculDonneesDroitSearch search) throws CalculException, JadePersistenceException;

    public CalculDonneesDroit read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException;

    public CalculDonneesDroitSearch search(CalculDonneesDroitSearch calculDonneesDroitSearch)
            throws JadePersistenceException, CalculException;

}
