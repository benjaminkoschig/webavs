/*
 * Créé le 5 avril 2011
 */
package globaz.cygnus.db.adaptationsJournalieres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author jje
 */
public class RFDateTraitementAdaptationJournaliereManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDateTraitementAdaptationJournaliere.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private boolean forDerniereDateDeTraitement = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossierManager.
     */
    public RFDateTraitementAdaptationJournaliereManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        // TODO Auto-generated method stub
        if (forDerniereDateDeTraitement) {
            return "MAX(" + RFDateTraitementAdaptationJournaliere.FIELDNAME_DATE_TRAITEMENT + ") as "
                    + RFDateTraitementAdaptationJournaliere.FIELDNAME_DATE_TRAITEMENT;
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFDateTraitementAdaptationJournaliereManager.createFromClause(_getCollection()));
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

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDateTraitementAdaptationJournaliere();
    }

    public String getFromClause() {
        return fromClause;
    }

    public boolean isForDerniereDateDeTraitement() {
        return forDerniereDateDeTraitement;
    }

    public void setForDerniereDateDeTraitement(boolean forDerniereDateDeTraitement) {
        this.forDerniereDateDeTraitement = forDerniereDateDeTraitement;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
