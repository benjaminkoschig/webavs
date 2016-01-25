package ch.globaz.perseus.business.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.Loyer;
import ch.globaz.perseus.business.models.parametres.LoyerSearchModel;

public interface LoyerService extends JadeApplicationService {

    public int count(LoyerSearchModel search) throws ParametresException, JadePersistenceException;

    public Loyer create(Loyer loyer) throws JadePersistenceException, ParametresException;

    public Loyer delete(Loyer loyer) throws JadePersistenceException, ParametresException;

    public Loyer read(String idLoyer) throws JadePersistenceException, ParametresException;

    public LoyerSearchModel search(LoyerSearchModel searchModel) throws JadePersistenceException, ParametresException;

    /**
     * Recherche d'un plafond de loyer pour une localité à une date donnée
     * 
     * @param idLocalite
     *            Id de la localité dont on veut la plafond de loyer
     * @param nbPersonnes
     *            Nombre de personnes dans le logement
     * @param date
     *            Date à laquelle on veut le plafond
     * @return Le plafond de loyer
     * @throws JadePersistenceException
     * @throws ParametresException
     */
    public Loyer searchForLocalite(String idLocalite, Integer nbPersonnes, String date)
            throws JadePersistenceException, ParametresException;

    public Loyer update(Loyer Loyer) throws JadePersistenceException, ParametresException;

}
