package globaz.phenix.api;

import globaz.globall.db.BSession;
import java.math.BigDecimal;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public interface ICPDecisionBeanRecap {
    public void add(ICPDecisionBean bean);

    /**
     * Returns the m_nbrCas.
     * 
     * @return String
     */
    public int get_nbrCas();

    /**
     * Returns the fortuneDet.
     * 
     * @return String
     */
    public BigDecimal getFortuneDetGroup();

    public ICPDecisionBeanRecap getNewInstance();

    /**
     * Returns the revenuCI.
     * 
     * @return String
     */
    public BigDecimal getRevenuCIGroup();

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession();

    /**
     * Sets the m_fortuneDet.
     * 
     * @param m_fortuneDet
     *            The m_fortuneDet to set
     */
    public void setFortuneDetGroup(BigDecimal fortuneDet);

    /**
     * Sets the m_revenuCI.
     * 
     * @param m_revenuCI
     *            The m_revenuCI to set
     */
    public void setRevenuCIGroup(BigDecimal revenuCI);

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session);

}
