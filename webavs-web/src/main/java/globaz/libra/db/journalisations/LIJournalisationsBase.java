/**
 * 
 */
package globaz.libra.db.journalisations;

import globaz.globall.db.BStatement;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceDestinationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.IJOComplementJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOGroupeJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceDestinationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.JOJournalisation;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * @author HPE
 * 
 */
public abstract class LIJournalisationsBase extends JOJournalisation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String csTypeCodeSysteme = new String();

    // TABLE GroupeJournal (JOJPGJO)
    public String dateRappel = new String();
    public String dateReception = new String();
    private transient String fromClause = null;

    // TABLE ComplementJournal (JOJPCJO)
    public String idComplementJournal = new String();

    // // TABLE ReferenceProvenance (JOJPREP)
    // public String idReferenceProvenance = new String();
    // public String idCleReferenceProvenance = new String();
    // public String typeReferenceProvenance = new String();

    // TABLE ReferenceDestination (JOJPRED)
    public String idReferenceDestination = new String();
    // public String idCleReferenceDestination = new String();
    // public String typeReferenceDestination = new String();
    public Boolean isOnTraitement = Boolean.FALSE;
    public String valeurCodeSysteme = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        dateRappel = statement.dbReadDateAMJ(IJOCommonGroupeJournalDefTable.DATE_RAPPEL);
        dateReception = statement.dbReadDateAMJ(IJOCommonGroupeJournalDefTable.DATE_RECEPTION);
        isOnTraitement = statement.dbReadBoolean(IJOCommonGroupeJournalDefTable.ON_TRAITEMENT);

        idReferenceDestination = statement.dbReadNumeric(IJOCommonReferenceDestinationDefTable.IDREFERENCEDESTINATION);
        // idCleReferenceDestination =
        // statement.dbReadNumeric(IJOReferenceDestinationDefTable.IDCLEREFERENCEDESTINATION);
        // typeReferenceDestination =
        // statement.dbReadString(IJOReferenceDestinationDefTable.TYPEREFERENCEDESTINATION);

        idComplementJournal = statement.dbReadNumeric(IJOCommonComplementJournalDefTable.IDCOMPLEMENTJOURNAL);
        csTypeCodeSysteme = statement.dbReadNumeric(IJOCommonComplementJournalDefTable.CSTYPECODESYSTEME);
        valeurCodeSysteme = statement.dbReadNumeric(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);

    }

    /**
     * Génération de la clause from pour la requête > Jointure depuis la table JOJPJOU (journalisations) sur :
     * 
     * > JOJPGJO (GroupeJournal) > JOJPRED (ReferenceDestination) > JOJPREP (ReferenceProvenance) > JOJPCJO
     * (ComplementJournal)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT OUTER JOIN ";
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOJournalisationDefTable.TABLE_NAME);

        // Jointure entre table des journalisations et des groupeJournal
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOGroupeJournalDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOJournalisationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonJournalisationDefTable.IDGROUPEJOURNAL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOGroupeJournalDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonGroupeJournalDefTable.IDGROUPEJOURNAL);

        // Jointure entre table des journalisations et des referenceProvenance
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOJournalisationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonJournalisationDefTable.IDJOURNALISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceProvenanceDefTable.IDJOURNALISATION);

        // Jointure entre table des journalisations et des referenceDestination
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceDestinationDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOJournalisationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonJournalisationDefTable.IDJOURNALISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceDestinationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceDestinationDefTable.IDJOURNALISATION);
        fromClauseBuffer.append(" AND ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceDestinationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceDestinationDefTable.TYPEREFERENCEDESTINATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append("'" + ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS + "'");

        // Jointure entre table des journalisations et des complementJournal
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOComplementJournalDefTable.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOJournalisationDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonJournalisationDefTable.IDJOURNALISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOComplementJournalDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonComplementJournalDefTable.IDJOURNALISATION);

        return fromClauseBuffer.toString();
    }

    public String getCsTypeCodeSysteme() {
        return csTypeCodeSysteme;
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    @Override
    public String getDateRappel() {
        return dateRappel;
    }

    @Override
    public String getDateReception() {
        return dateReception;
    }

    public String getIdComplementJournal() {
        return idComplementJournal;
    }

    public String getIdReferenceDestination() {
        return idReferenceDestination;
    }

    // public String getIdCleReferenceDestination() {
    // return idCleReferenceDestination;
    // }
    //
    // public String getTypeReferenceDestination() {
    // return typeReferenceDestination;
    // }

    // public String getIdReferenceProvenance() {
    // return idReferenceProvenance;
    // }
    //
    // public String getIdCleReferenceProvenance() {
    // return idCleReferenceProvenance;
    // }
    //
    // public String getTypeReferenceProvenance() {
    // return typeReferenceProvenance;
    // }

    public Boolean getIsOnTraitement() {
        return isOnTraitement;
    }

    public String getValeurCodeSysteme() {
        return valeurCodeSysteme;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsTypeCodeSysteme(String csTypeCodeSysteme) {
        this.csTypeCodeSysteme = csTypeCodeSysteme;
    }

    // public void setIdCleReferenceDestination(String
    // idCleReferenceDestination) {
    // this.idCleReferenceDestination = idCleReferenceDestination;
    // }
    //
    // public void setTypeReferenceDestination(String typeReferenceDestination)
    // {
    // this.typeReferenceDestination = typeReferenceDestination;
    // }

    // public void setIdReferenceProvenance(String idReferenceProvenance) {
    // this.idReferenceProvenance = idReferenceProvenance;
    // }
    //
    // public void setIdCleReferenceProvenance(String idCleReferenceProvenance)
    // {
    // this.idCleReferenceProvenance = idCleReferenceProvenance;
    // }
    //
    // public void setTypeReferenceProvenance(String typeReferenceProvenance) {
    // this.typeReferenceProvenance = typeReferenceProvenance;
    // }

    @Override
    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    @Override
    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdComplementJournal(String idComplementJournal) {
        this.idComplementJournal = idComplementJournal;
    }

    public void setIdReferenceDestination(String idReferenceDestination) {
        this.idReferenceDestination = idReferenceDestination;
    }

    public void setIsOnTraitement(Boolean isOnTraitement) {
        this.isOnTraitement = isOnTraitement;
    }

    public void setValeurCodeSysteme(String valeurCodeSysteme) {
        this.valeurCodeSysteme = valeurCodeSysteme;
    }

}
