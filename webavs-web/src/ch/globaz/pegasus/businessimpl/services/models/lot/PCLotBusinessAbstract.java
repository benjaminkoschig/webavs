package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.corvus.api.lots.IRELot;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

public abstract class PCLotBusinessAbstract {

    public abstract void comptabiliserLot(SimpleLot lot, String idOrganeExecution, String numeroOG,
            String libelleJournal, String dateValeur, String dateEchance) throws ComptabiliserLotException,
            JadePersistenceException;

    protected Boolean isComptabilisable(SimpleLot lot) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtat()) && IRELot.CS_TYP_LOT_DECISION.equals(lot.getCsTypeLot())
                && IRELot.CS_LOT_OWNER_PC.endsWith(lot.getCsProprietaire())) {
            return true;
        }
        return false;
    }
}
