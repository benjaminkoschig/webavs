/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.vb.demandes;

import globaz.cygnus.db.demandes.RFDemandeJointDossierJointTiersManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiersListViewBean extends RFDemandeJointDossierJointTiersManager implements
        FWListViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String MONTANT_ZERO = "0.00";
    private boolean hasPostitField = false;

    public RFDemandeJointDossierJointTiersListViewBean() {
        super(true);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeJointDossierJointTiersViewBean();
    }

    /**
     * TODO à supprimer creation du count pour les postit de l'entity
     * 
     * @return
     */
    // private String createSelectCountPostit(String schema) {
    //
    // StringBuffer query = new StringBuffer();
    // query.append("SELECT COUNT(*) FROM ");
    // query.append(schema);
    // query.append(FWNoteP.TABLE_NAME);
    // query.append(" WHERE ");
    // query.append("NPSRCID");
    // query.append(" = ");
    // query.append(IJPrononce.FIELDNAME_ID_PRONONCE);
    // query.append(" AND ");
    // query.append("NPTBLSRC");
    // query.append(" = '");
    // query.append(IJApplication.KEY_POSTIT_PRONONCES);
    // query.append("'");
    //
    // return query.toString();
    // }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public boolean isPropertieForcerPaiement() {
        try {
            return RFPropertiesUtils.afficherCaseForcerPaiementDemande();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }
}
