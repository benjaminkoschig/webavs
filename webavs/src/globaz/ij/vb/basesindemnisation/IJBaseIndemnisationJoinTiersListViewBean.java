package globaz.ij.vb.basesindemnisation;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationJointTiersManager;

public class IJBaseIndemnisationJoinTiersListViewBean extends IJBaseIndemnisationJointTiersManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ALL_FIELDS = "XKDDEB, XKDFIN, XKIBIN, XKNJCO, XKNJIN, XKNJEX, "
            + "XKLATT, XKNJOI, XKTMOI, XKLREM, XKTETA, XKIPAR, " + "XKIPAI, XKTCIS, XKMTIS, XKTTIJ, XKICOR, XKTTBA";

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private boolean hasPostitField = false;
    private String idTiers;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField) {
            return "HTITIE, XKIBIN, " + IJBaseIndemnisationJoinTiersListViewBean.ALL_FIELDS + ", ("
                    + createSelectCountPostit(_getCollection()) + ") AS "
                    + IJBaseIndemnisationJoinTiersListViewBean.FIELDNAME_COUNT_POSTIT + " ";
        } else {
            return "HTITIE, XKIBIN, " + super._getFields(statement);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJBaseIndemnisationJoinTiersViewBean();
    }

    /**
     * creation du count pour les postit de l'entity
     * 
     * @return
     */
    private String createSelectCountPostit(String schema) {

        StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(*) FROM ");
        query.append(schema);
        query.append(FWNoteP.TABLE_NAME);
        query.append(" WHERE ");
        query.append("NPSRCID");
        query.append(" = ");
        query.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(IJApplication.KEY_POSTIT_BASES_INDEMNISATION);
        query.append("'");

        return query.toString();
    }

    public final String getIdTiers() {
        return idTiers;
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
