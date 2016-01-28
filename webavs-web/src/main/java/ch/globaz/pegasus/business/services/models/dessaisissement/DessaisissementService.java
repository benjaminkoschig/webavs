package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

public interface DessaisissementService extends JadeApplicationService {

    public void copyDessaisissement(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    /**
     * Permet de créer un DessaisissementFortune <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param dessaisissementFortune
     * @return DessaisissementFortune
     * @throws JadePersistenceException
     * @throws DessaisissementFortuneException
     * @throws DonneeFinanciereException
     */
    public DessaisissementFortune createDessaisissementFortune(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException;

    /**
     * Permet de créer un DessaisissementRevenu <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param dessaisissementRevenu
     * @return DessaisissementRevenu
     * @throws JadePersistenceException
     * @throws DessaisissementRevenuException
     * @throws DonneeFinanciereException
     */
    public DessaisissementRevenu createDessaisissementRevenu(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException, DonneeFinanciereException;

    /**
     * Suppression des entitées "Dessaisissement" en fonction d'une version de droit dans les données financière header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

}
