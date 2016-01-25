package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;

public interface LotService extends JadeApplicationService {

    public int count(LotSearchModel search) throws LotException, JadePersistenceException;

    public Lot create(Lot lot) throws JadePersistenceException, LotException;

    public Lot delete(Lot lot) throws JadePersistenceException, LotException;

    /**
     * Permet de récupérer le lot courant pour y mettre des prestations selon son type (création du lot si il n'y en a
     * pas d'ouvert)
     * 
     * @param type
     *            du lot
     * @return le lot courant
     * @throws JadePersistenceException
     * @throws LotException
     */
    public Lot getLotCourant(CSTypeLot typeLot) throws JadePersistenceException, LotException;

    /**
     * Retourne le lot contenant la demande dont l'identifiant est envoyé en paramètre
     * 
     * @param demandeId
     * @return
     * @throws JadePersistenceException
     * @throws LotException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Lot getLotForDemande(String demandeId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException;

    public Lot getLotForFacture(String factureId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException;

    public Lot getLotForFactureRP(String factureId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException;

    public Lot read(String idLot) throws JadePersistenceException, LotException;

    public LotSearchModel search(LotSearchModel searchModel) throws JadePersistenceException, LotException;

    public Lot update(Lot lot) throws JadePersistenceException, LotException;

}
