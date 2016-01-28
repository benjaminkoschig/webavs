package globaz.ij.helpers.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prononces.IJMesureJointAgentExecution;
import globaz.ij.db.prononces.IJMesureJointAgentExecutionManager;
import globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFormulaireIndemnisationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        chargerAgentsExecution((IJFormulaireIndemnisationViewBean) viewBean, (BSession) session);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        chargerAgentsExecution((IJFormulaireIndemnisationViewBean) viewBean, (BSession) session);
    }

    private void chargerAgentsExecution(IJFormulaireIndemnisationViewBean viewBean, BSession session) throws Exception {
        IJBaseIndemnisation base = viewBean.loadBaseIndemnisation();

        // charger
        IJMesureJointAgentExecutionManager agents = new IJMesureJointAgentExecutionManager();

        agents.setForIdPrononce(base.getIdPrononce());
        agents.setSession(session);
        agents.find();

        // renseigner le viewBean
        Vector nomsAgents = new Vector(agents.size());

        for (int idAgent = 0; idAgent < agents.size(); ++idAgent) {
            IJMesureJointAgentExecution agent = (IJMesureJointAgentExecution) agents.get(idAgent);

            nomsAgents.add(new String[] { agent.getIdAgentExecution(), agent.loadNom() });
        }

        viewBean.setNomsAgents(nomsAgents);
    }
}
