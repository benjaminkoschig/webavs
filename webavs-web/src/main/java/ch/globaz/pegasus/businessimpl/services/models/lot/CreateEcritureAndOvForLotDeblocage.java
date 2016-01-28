package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class CreateEcritureAndOvForLotDeblocage extends PCLotBusinessAbstract {

    @Override
    public void comptabiliserLot(SimpleLot lot, String idOrganeExecution, String numeroOG, String libelleJournal,
            String dateValeur, String dateEchance) throws ComptabiliserLotException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

}
