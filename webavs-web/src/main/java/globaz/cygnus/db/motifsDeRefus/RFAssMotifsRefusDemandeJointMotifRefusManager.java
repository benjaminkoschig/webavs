/*
 * Créé le 26 janvier 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFAssMotifsRefusDemandeJointMotifRefusManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String forIdDemande = "";
    private transient String forIdMotifDeRefus = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssMotifsRefusDemandeManager.
     */
    public RFAssMotifsRefusDemandeJointMotifRefusManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_ID_ASS).append(",");
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE).append(",");
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS).append(",");
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_IDS_SOIN).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_HAS_MONTANT).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_FR).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_IT).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_FR).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_IT).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_DE).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_IS_MOTIF_SYSTEME).append(",");
        fields.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFAssMotifsRefusDemandeJointMotifRefus.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande)) {

            sqlWhere.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDemande));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdMotifDeRefus)) {

            sqlWhere.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdMotifDeRefus));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFAssMotifsRefusDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAssMotifsRefusDemandeJointMotifRefus();
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdMotifDeRefus() {
        return forIdMotifDeRefus;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdMotifDeRefus(String forIdMotifDeRefus) {
        this.forIdMotifDeRefus = forIdMotifDeRefus;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
