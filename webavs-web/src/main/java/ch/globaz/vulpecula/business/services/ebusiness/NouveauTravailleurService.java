package ch.globaz.vulpecula.business.services.ebusiness;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.web.gson.AdresseInfoGSON;

/**
 * Permet différentes opérations métier liées à un travailleur.
 * 
 */
public interface NouveauTravailleurService {
    TravailleurEbuDomain create(TravailleurEbuDomain travailleur) throws GlobazBusinessException;

    void delete(TravailleurEbuDomain travailleur) throws GlobazBusinessException;

    TravailleurEbuDomain update(TravailleurEbuDomain travailleur) throws GlobazBusinessException;

    String findIdTiersBanqueByClearingAndIdLocalite(String clearing, String idLocalite);

    AdresseComplexModel insertAdresse(PersonneEtendueComplexModel personneEtendueComplexModel,
            AdresseInfoGSON adresseInfoGSON) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException;

}
