/*
 * Créé le 10 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author hpe
 * 
 */

public class REDemandeRenteJointDemandeListViewBean extends REDemandeRenteJointDemandeManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private boolean hasPostitField = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        if (hasPostitField) {
            return super._getFields(statement) + ", (" + createSelectCountPostit(_getCollection()) + ") AS "
                    + FIELDNAME_COUNT_POSTIT + " ";
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteJointDemandeViewBean();
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
        query.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(REApplication.KEY_POSTIT_RENTES);
        query.append("'");

        return query.toString();
    }

    public String getDateDernierPaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

}