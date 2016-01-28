package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJSituationProfessionnelleManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrononce = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer retValue = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (retValue.length() > 0) {
                retValue.append(" AND ");
            }

            retValue.append(IJSituationProfessionnelle.FIELDNAME_ID_GRANDE_IJ);
            retValue.append("=");
            retValue.append(forIdPrononce);
        }

        return retValue.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJSituationProfessionnelle();
    }

    /**
     * getter pour l'attribut for id prononce
     * 
     * @return la valeur courante de l'attribut for id prononce
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJSituationProfessionnelle.FIELDNAME_ID_SITUATION_PROFESSIONNELLE;
    }

    /**
     * setter pour l'attribut for id prononce
     * 
     * @param forIdPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
    }
}
