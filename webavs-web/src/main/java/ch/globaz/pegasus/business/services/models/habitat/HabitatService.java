package ch.globaz.pegasus.business.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;

public interface HabitatService extends JadeApplicationService {
    public void copyHabitat(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    /**
     * Permet de créer un Loyer <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du droit
     * (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance de
     * cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param loyer
     * @return
     * @throws LoyerException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public Loyer createLoyer(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, Loyer loyer)
            throws LoyerException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une TaxeJournaliereHome <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param taxeJournaliereHome
     * @return TaxeJournaliereHome
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public TaxeJournaliereHome createTaxeJournaliereHome(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un SejourMoisPartielHome <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     *
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param sejourMoisPartielHome
     * @return TaxeJournaliereHome
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    SejourMoisPartielHome createSejourMoisPartielHome(SimpleVersionDroit simpleVersionDroit,
                                                             DroitMembreFamille droitMembreFamille, SejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Suppression des entitées "Habitat" en fonction d'une version de droit dans les données financière header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
