/*
 * Cr�� le 4 mars 2011
 */
package globaz.cygnus.db.adaptationsJournalieres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author jje
 */
public class RFDateTraitementAdaptationJournaliere extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_TRAITEMENT = "FVDTRA";

    public static final String TABLE_NAME = "RFADJOD";

    private String dateTraitement = "";

    /**
     * Cr�e une nouvelle instance de la classe RFDateTraitementAdaptationJournaliere.
     */
    public RFDateTraitementAdaptationJournaliere() {
        super();
    }

    /**
     * M�thode avant l'ajout l'incr�mentation de la cl� primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /**
     * getter pour le nom de la table des dates des adaptations journali�res
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDateTraitementAdaptationJournaliere.TABLE_NAME;
    }

    /**
     * Lecture des propri�t�s dans les champs de la table des dates des adaptations journali�res
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateTraitement = statement.dbReadDateAMJ(RFDateTraitementAdaptationJournaliere.FIELDNAME_DATE_TRAITEMENT);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * D�finition de la cl� primaire de la table des dates des adaptations journali�res
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * M�thode d'�criture des champs dans la table des dates des adaptations journali�res
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFDateTraitementAdaptationJournaliere.FIELDNAME_DATE_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateTraitement, "dateTraitement"));

    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

}
