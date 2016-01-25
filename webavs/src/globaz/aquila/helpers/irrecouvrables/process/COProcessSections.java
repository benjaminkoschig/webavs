/**
 *
 */
package globaz.aquila.helpers.irrecouvrables.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.ICOTaxeProducer;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import java.util.List;

/**
 * @author SEL
 * 
 */
public class COProcessSections extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private COContentieux contentieux = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        passerIrrecouvrable();
        return false;
    }

    /**
     * @return the contentieux
     */
    public COContentieux getContentieux() {
        return contentieux;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * @param httpSession
     * @param request
     * @param response
     * @param dispatcher
     * @throws Exception
     */
    private void passerIrrecouvrable() throws Exception {
        if (getContentieux() != null) {
            // retrouver l'étape irrécouvrable
            COEtapeManager etapeManager = new COEtapeManager();
            etapeManager.setSession(getSession());
            etapeManager.setForLibEtape(ICOEtape.CS_IRRECOUVRABLE);
            etapeManager.setForIdSequence(getContentieux().getIdSequence());
            etapeManager.find(getTransaction());

            // étape irrécouvrable
            COEtape etapeIrrecouvrable = (COEtape) etapeManager.getFirstEntity();

            // exécuter l'action irrécouvrable
            COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(getSession(),
                    getTransaction(), getContentieux().getIdEtape(), etapeIrrecouvrable.getIdEtape(), null, null);

            // calculer les taxes
            ICOTaxeProducer producer = COServiceLocator.getTaxeService().getTaxeProducer(etapeIrrecouvrable);
            List taxes = producer.getListeTaxes(getSession(), getContentieux(), etapeIrrecouvrable);

            action.setTaxes(taxes);
            action.canExecute(getContentieux(), getTransaction());
            COServiceLocator.getTransitionService().executerTransition(getSession(), getTransaction(),
                    getContentieux(), action);
        } else {
            throw new Exception(getSession().getLabel("CONTENTIEUX_NULL"));
        }
    }

    /**
     * @param contentieux
     *            the contentieux to set
     */
    public void setContentieux(COContentieux contentieux) {
        this.contentieux = contentieux;
    }

}
