package ch.globaz.pegasus.business.services.models.lot;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.ComptabiliserLotSearch;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.PegasusJournalConteneur;

public interface LotService extends JadeApplicationService {

    /**
     * Permet de comptabiliser tout les ordres de versement se trouvant dans le lot. Le libelleJournal peut être null.
     * Si c'est le cas on prend le libelle du lot
     * 
     * @param idLot
     * @throws JadePersistenceException
     * @throws ComptabiliserLotException
     * @throws JAException
     */
    public ComptabilisationData comptabiliserLot(String idLot, String idOrganeExecution, String numeroOG,
            String libelleJournal, String dateValeur, String dateEcheance) throws JadePersistenceException,
            ComptabiliserLotException, JAException;

    public void deleteLotDeblocageIfEmpty() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PrestationException, LotException;

    public SimpleLot findCurrentDeblocageLotOrCreate() throws JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException;

    public SimpleLot findCurrentDecisionLotOrCreate() throws JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException;

    /**
     * Comptabilise le lot en résolvant la date comptable à maintenant si le dernier paiement et dans le même mois.
     * Sinon le dernier paiement et le mois prochain on définit la date du premier lundi du mois prochain
     * 
     * @param idLot
     * @throws PropertiesException
     * @throws ComptabiliserLotException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void comptabiliserAndResolveDateComptableEcheance(String idLot, String mailProcessCompta)
            throws PropertiesException, ComptabiliserLotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet la création d'un lot avec une description spécifique
     * 
     * @param csTypeLot Le code systeme d'un type de lot
     * @param description La description du lot
     * @return Le lot créé
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleLot createLot(String csTypeLot, String description)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException;

    public PegasusJournalConteneur generateJournalByIdPrestation(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException;

    public ComptabiliserLotSearch searchComptabiliserLot(ComptabiliserLotSearch search)
            throws JadePersistenceException, ComptabiliserLotException;

}
