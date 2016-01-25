/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author bsc
 * 
 *         Jointure entre les tables des bases de calcul et des demandes de rentes
 * 
 * @deprecated replaced by RERenteAccJoinTblTiersJoinDemandeRente
 */
@Deprecated
public class RERenteAccordeeJointDemandeRente extends RERenteAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // INNER JOIN WEBAVS.REREACC ON
        // WEBAVS.REBACAL.YIIBCA=WEBAVS.REREACC.YLIBAC
        // jointure entre table des rentes accordées et base de calcul
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // INNER JOIN WEBAVS.REPRACC ON
        // WEBAVS.REREACC.YLIRAC=WEBAVS.REPRACC.ZTIPRA
        // jointure entre table des bases de calculs et table des rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordées et informations
        // comptabilités
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        // jointure entre table des demandes et table des demandes de rentes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String degreInvalidite = "";
    private String idDomaineAdressePmt = "";
    private String idTierAdressePmt = "";
    private String idTierRequerant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // Autres champs nécessaires
    private String noDemandeRente = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return true;
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
        return true;
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
        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        noDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTierAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idDomaineAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        degreInvalidite = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE);
        idTierRequerant = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getIdDomaineAdressePmt() {
        return idDomaineAdressePmt;
    }

    /**
     * @return
     */
    public String getIdTierAdressePmt() {
        return idTierAdressePmt;
    }

    /**
     * @return
     */
    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    /**
     * @return
     */
    public String getNoDemandeRente() {
        return noDemandeRente;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setIdDomaineAdressePmt(String idDomaineAdressePmt) {
        this.idDomaineAdressePmt = idDomaineAdressePmt;
    }

    /**
     * @param string
     */
    public void setIdTierAdressePmt(String string) {
        idTierAdressePmt = string;
    }

    /**
     * @param string
     */
    public void setIdTierRequerant(String string) {
        idTierRequerant = string;
    }

    /**
     * @param string
     */
    public void setNoDemandeRente(String string) {
        noDemandeRente = string;
    }

}
