package globaz.libra.db.dossiers;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BStatement;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.utilisateurs.LIUtilisateurs;

public class LIDossiersJointTiers extends LIDossiers {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
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
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME + " AS dossier");

        // Jointure entre table des dossiers et tables des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("dossier");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // Jointure entre table des dossiers et tables des domaines
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("dossier");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_DOMAINE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDomaines.FIELDNAME_ID_DOMAINE);

        // Jointure entre table des dossiers et tables des groupes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("dossier");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_GROUPE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIGroupes.FIELDNAME_ID_GROUPE);

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

        // jointure entre table des dossiers et table des utilisateurs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIUtilisateurs.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("dossier");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIUtilisateurs.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIUtilisateurs.FIELDNAME_ID_UTILISATEUR);

        return fromClauseBuffer.toString();
    }

    private String csDomaine = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private transient String fromClause = null;
    private String idUtilisateur = "";

    private String libelleGroupe = "";
    // Autres champs nécessaires
    private String noAVS = "";

    private String nom = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String visaUtilisateur = "";

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
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(FIELDNAME_SEXE);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        idUtilisateur = statement.dbReadNumeric(LIUtilisateurs.FIELDNAME_ID_UTILISATEUR);
        visaUtilisateur = statement.dbReadString(LIUtilisateurs.FIELDNAME_ID_UTILISATEUR_EXTERNE);
        csNationalite = statement.dbReadNumeric(FIELDNAME_NATIONALITE);
        csDomaine = statement.dbReadNumeric(LIDomaines.FIELDNAME_CS_DOMAINE);
        libelleGroupe = statement.dbReadString(LIGroupes.FIELDNAME_LIBELLE_GROUPE);
    }

    public String getCsDomaine() {
        return csDomaine;
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

    public String getFromClause() {
        return fromClause;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
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

    public String getVisaUtilisateur() {
        return visaUtilisateur;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
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

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
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

    public void setVisaUtilisateur(String visaUtilisateur) {
        this.visaUtilisateur = visaUtilisateur;
    }

}
