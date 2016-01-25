package globaz.aquila.helpers.batch;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.batch.COTransitionEditViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import java.util.Collections;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COTransitionEditHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        /*
         * si l'action supprimer a �t� appell�e depuis l'�cran de l'�tape (GCO0021), on a remplac� le viewBean mais il
         * n'a pas encore �t� charg� depuis la base, on le charge
         */
        COTransitionEditViewBean transitionEditViewBean = (COTransitionEditViewBean) viewBean;

        if (transitionEditViewBean.isNew()) {
            transitionEditViewBean.setISession(session);
            transitionEditViewBean.retrieve();
        }

        super._delete(viewBean, action, session);
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        // charger la liste des �tapes
        chargerEtapes((COTransitionEditViewBean) viewBean, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger la liste des �tapes
        chargerEtapes((COTransitionEditViewBean) viewBean, session);
    }

    private void chargerEtapes(COTransitionEditViewBean transitionEditViewBean, BISession session) throws Exception {
        // charger l'�tape from
        COEtape etapeRetour = transitionEditViewBean.getEtapeRetour();

        if (etapeRetour.isNew()) {
            etapeRetour.setISession(session);
            etapeRetour.setIdEtape(transitionEditViewBean.getIdEtapeRetour());
            etapeRetour.retrieve();
        }

        // charger les �tapes
        COEtapeManager listViewBean = new COEtapeManager();

        listViewBean.setISession(session);
        listViewBean.setForIdSequence(etapeRetour.getIdSequence());
        listViewBean.find();

        transitionEditViewBean.setEtapes(Collections.unmodifiableList(listViewBean.getContainer()));
    }
}
