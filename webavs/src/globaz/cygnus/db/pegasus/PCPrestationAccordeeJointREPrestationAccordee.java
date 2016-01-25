/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.pegasus;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author fha
 * @revision JJE -> date échéance
 */
public class PCPrestationAccordeeJointREPrestationAccordee extends RFPrestationAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String egal = "=";
    public static final String FIELDNAME_DATEDECES = "HPDDEC";

    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";
    private static final String on = " ON ";
    private static final String point = ".";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des prestations et la table des prestations des rentes
        PCPrestationAccordeeJointREPrestationAccordee.innerJoinTable(schema, fromClauseBuffer,
                RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE, RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE,
                REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES,
                REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        return fromClauseBuffer.toString();
    }

    /**
     * ajoute une jointure INNER sur une nouvelle table. <br>
     * Il se construit sur le modèle "INNER JOIN tableDest ON tableSrc.field = tableDest.field"
     * 
     * @param schema
     *            schema
     * @param fromClauseBuffer
     *            tampon
     * @param tableJoinSrc
     *            table de reference de la jointure
     * @param fieldJoinSrc
     *            champ de liaison pour la table de reference de la jointure
     * @param tableJoinDest
     *            table à ajouter
     * @param fieldJoinDest
     *            champ de liaison pour la table à ajouter
     */
    private static void innerJoinTable(String schema, StringBuffer fromClauseBuffer, String tableJoinSrc,
            String fieldJoinSrc, String tableJoinDest, String fieldJoinDest) {
        PCPrestationAccordeeJointREPrestationAccordee.joinTable(PCDemandeJointQdJointDossierJointTiers.innerJoin,
                schema, fromClauseBuffer, tableJoinSrc, fieldJoinSrc,
                PCPrestationAccordeeJointREPrestationAccordee.egal, tableJoinDest, fieldJoinDest);
    }

    private static void joinTable(String typeJoin, String schema, StringBuffer fromClauseBuffer, String tableJoinSrc,
            String fieldJoinSrc, final String operator, String tableJoinDest, String fieldJoinDest) {
        fromClauseBuffer.append(typeJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinDest);
        fromClauseBuffer.append(PCPrestationAccordeeJointREPrestationAccordee.on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinSrc);
        fromClauseBuffer.append(PCPrestationAccordeeJointREPrestationAccordee.point);
        fromClauseBuffer.append(fieldJoinSrc);
        fromClauseBuffer.append(operator);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(tableJoinDest);
        fromClauseBuffer.append(PCPrestationAccordeeJointREPrestationAccordee.point);
        fromClauseBuffer.append(fieldJoinDest);
    }

    private String csGenrePrestationAccordee = "";
    private String dateDebutDroit = "";
    private String dateEcheance = "";
    private String dateFinDroit = "";
    private String dateValidationDecision = "";
    private transient String fromClause = null;
    private String idInfoCompta = "";
    private String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String montantPrestation = "";

    private String referencePaiement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    PCPrestationAccordeeJointREPrestationAccordee.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idTiers = statement.dbReadString(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        referencePaiement = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);

        csGenrePrestationAccordee = statement.dbReadString(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));

        dateEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));

        idInfoCompta = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);

    }

    public String getCsGenrePrestationAccordee() {
        return csGenrePrestationAccordee;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setCsGenrePrestationAccordee(String csGenrePrestationAccordee) {
        this.csGenrePrestationAccordee = csGenrePrestationAccordee;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }
}
