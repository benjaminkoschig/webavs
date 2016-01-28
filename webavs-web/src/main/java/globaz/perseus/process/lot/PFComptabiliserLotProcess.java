/**
 * 
 */
package globaz.perseus.process.lot;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFComptabiliserLotProcess extends PFAbstractJob {

    private String adresseMail = null;
    private Lot lot = null;

    /**
     * @return the adresseMail
     */
    public String getAdresseMail() {
        return adresseMail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Comptabiliser lot PC Famille";
    }

    /**
     * @return the lot
     */
    public Lot getLot() {
        return lot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    @Override
    protected void process() throws Exception {
        try {
            if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                PerseusServiceLocator.getPmtDecisionService().comptabiliserLot(lot, getLogSession());
            } else if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                PerseusServiceLocator.getPmtFactureService().comptabiliserLot(lot, getLogSession());
            } else if (CSTypeLot.LOT_DECISION_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                PerseusServiceLocator.getPmtDecisionRentePontService().comptabiliserLot(lot, getLogSession());
            } else if (CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                PerseusServiceLocator.getPmtFactureRentePontService().comptabiliserLot(lot, getLogSession());
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Erreur technique : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeThread.rollbackSession();
        } else {
            JadeThread.commitSession();
        }
        List<String> email = new ArrayList<String>();
        email.add(getAdresseMail());
        this.sendCompletionMail(email);
    }

    /**
     * @param adresseMail
     *            the adresseMail to set
     */
    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    /**
     * @param lot
     *            the lot to set
     */
    public void setLot(Lot lot) {
        this.lot = lot;
    }

}
