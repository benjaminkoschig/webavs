package globaz.corvus.db.adaptation;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BStatement;

/**
 * 
 * @author HPE
 * 
 *         Manager spécifique pour la circulaire aux rentiers et la liste des erreurs
 * 
 */
public class REPrestAccJointInfoComptaJointTiers extends RERenteAccordee {

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

    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et table rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et table bases calcul
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        // jointure entre table des prestations accordées et table info compta
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

        // jointure entre table des prestations accordées et table des nss
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des nss et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestAccJointInfoComptaJointTiers.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csNationalite = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String droitApplique = "";
    private transient String fromClause = null;
    private String idTiersAdressePaiement = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String remarquesAdaptation = ""; // NON STOCKE EN DB

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = REPrestAccJointInfoComptaJointTiers.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idTiersAdressePaiement = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        nss = NSUtil.formatAVSUnknown(statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_PRENOM);
        droitApplique = statement.dbReadString(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE);
        dateNaissance = statement.dbReadDateAMJ(REPrestAccJointInfoComptaJointTiers.FIELDNAME_DATENAISSANCE);
        csSexe = statement.dbReadNumeric(REPrestAccJointInfoComptaJointTiers.FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NATIONALITE);
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDroitApplique() {
        return droitApplique;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRemarquesAdaptation() {
        return remarquesAdaptation;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDroitApplique(String droitApplique) {
        this.droitApplique = droitApplique;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRemarquesAdaptation(String remarquesAdaptation) {
        this.remarquesAdaptation = remarquesAdaptation;
    }

}
