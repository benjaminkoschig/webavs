package globaz.libra.db.journalisations;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BStatement;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.groupes.LIGroupes;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIJournalisationsJointDossiers extends LIJournalisationsBase {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
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
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";

    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDomaine = "";

    private String csEtat = new String();
    private String csGroupe = new String();
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";

    private String dateNaissance = "";
    private transient String fromClause = null;
    // TABLE Dossiers (LIDOSSI)
    private String idDossier = new String();
    private String idExterne = new String();
    private String idGestionnaire = new String();
    private String idTiers = new String();
    private String libelleGroupe = "";
    // Autres champs nécessaires
    private String noAVS = "";
    private String nom = "";

    private String prenom = "";
    private String visaGestionnaire = "";

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

        idDossier = statement.dbReadNumeric(LIDossiers.FIELDNAME_ID_DOSSIER);
        idExterne = statement.dbReadNumeric(LIDossiers.FIELDNAME_ID_EXTERNE);
        idTiers = statement.dbReadNumeric(LIDossiers.FIELDNAME_ID_TIERS);
        csEtat = statement.dbReadNumeric(LIDossiers.FIELDNAME_CS_ETAT);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(LIJournalisationsJointDossiers.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(LIJournalisationsJointDossiers.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(LIJournalisationsJointDossiers.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(LIJournalisationsJointDossiers.FIELDNAME_SEXE);
        nom = statement.dbReadString(LIJournalisationsJointDossiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(LIJournalisationsJointDossiers.FIELDNAME_PRENOM);
        csNationalite = statement.dbReadNumeric(LIJournalisationsJointDossiers.FIELDNAME_NATIONALITE);
        csDomaine = statement.dbReadNumeric(LIDomaines.FIELDNAME_CS_DOMAINE);
        libelleGroupe = statement.dbReadString(LIGroupes.FIELDNAME_LIBELLE_GROUPE);

    }

    /**
     * Génération de la clause from pour la requête > Jointure depuis la table JOJPREP (ReferenceProvenance) sur :
     * 
     * > LIDOSSI (Dossiers)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    @Override
    public final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(super.createFromClause(schema));

        // Jointure entre table des ReferenceProvnance et des dossiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append("replace(ltrim(replace(digits(");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append("), '0', ' ')), ' ', '0')");
        fromClauseBuffer.append(and);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append("'" + ILIConstantesExternes.REF_PRO_DOSSIER + "'");

        // Jointure entre table des dossiers et tables des domaines
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_DOMAINE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDomaines.FIELDNAME_ID_DOMAINE);

        // Jointure entre table des domaines et tables des groupes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDomaines.FIELDNAME_ID_DOMAINE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIGroupes.FIELDNAME_ID_DOMAINE);

        // Jointure entre table des dossier et tables des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIJournalisationsJointDossiers.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGroupe() {
        return csGroupe;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idExterne
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGroupe(String csGroupe) {
        this.csGroupe = csGroupe;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idExterne
     *            the idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelleGroupe(String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }
}
