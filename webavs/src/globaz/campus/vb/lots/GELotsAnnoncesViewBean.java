package globaz.campus.vb.lots;

import globaz.campus.db.lots.GELots;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class GELotsAnnoncesViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_FIELDNAME_ANNEE = "ANNEE";
    public static final String ALIAS_FIELDNAME_DATE_RECEPTION = "DATERECEPTION";
    public static final String ALIAS_FIELDNAME_ETAT_LOT = "ETATLOT";
    // alias champs
    public static final String ALIAS_FIELDNAME_ID_LOT = "IDLOT";
    public static final String ALIAS_FIELDNAME_ID_TIERS_ECOLE = "IDTIERSECOLE";
    public static final String ALIAS_FIELDNAME_LIBELLE_TRAITEMENT = "LIBELLETRAITEMENT ";
    public static final String ALIAS_NB_A_TRAITER = "NBATRAITER";
    public static final String ALIAS_NB_ANNONCES = "NBANNONCES";

    public static final String ALIAS_NB_ERREURS = "NBERREURS";
    public static final String ALIAS_NB_IMPUTATIONS = "NBIMPUTATIONS";
    public static final String ALIAS_PAS_TERMINE = "NBPASTERMINE";
    public static final String ALIAS_TABLE_NAME_ANNONCE = "ANNONCE";
    // Alias table
    public static final String ALIAS_TABLE_NAME_LOT = "LOT";
    public static final String FIELDNAME_ANNEE = ALIAS_TABLE_NAME_LOT + "." + GELots.FIELDNAME_ANNEE;
    public static final String FIELDNAME_DATE_RECEPTION = ALIAS_TABLE_NAME_LOT + "." + GELots.FIELDNAME_DATE_RECEPTION;
    public static final String FIELDNAME_ETAT_LOT = ALIAS_TABLE_NAME_LOT + "." + GELots.FIELDNAME_ETAT_LOT;
    // champs
    public static final String FIELDNAME_ID_LOT = ALIAS_TABLE_NAME_LOT + "." + GELots.FIELDNAME_ID_LOT;
    public static final String FIELDNAME_ID_TIERS_ECOLE = ALIAS_TABLE_NAME_LOT + "." + GELots.FIELDNAME_ID_TIERS_ECOLE;
    public static final String FIELDNAME_LIBELLE_TRAITEMENT = ALIAS_TABLE_NAME_LOT + "."
            + GELots.FIELDNAME_LIBELLE_TRAITEMENT;

    private String annee = "";
    private String csEtatLot = "";
    private String dateReceptionLot = "";
    private String idLot = "";
    private String idTiersEcole = "";
    private String libelleTraitement = "";
    private String nbAnnonces = "";
    private String nbATraiter = "";
    private String nbErreurs = "";
    private String nbImputations = "";
    private String pasTermine = "";

    public GELotsAnnoncesViewBean() {
        super();
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_LOT);
        dateReceptionLot = statement.dbReadDateAMJ(ALIAS_FIELDNAME_DATE_RECEPTION);
        csEtatLot = statement.dbReadNumeric(ALIAS_FIELDNAME_ETAT_LOT);
        idTiersEcole = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_TIERS_ECOLE);
        libelleTraitement = statement.dbReadString(ALIAS_FIELDNAME_LIBELLE_TRAITEMENT);
        annee = statement.dbReadNumeric(ALIAS_FIELDNAME_ANNEE);
        nbAnnonces = statement.dbReadNumeric(ALIAS_NB_ANNONCES);
        nbImputations = statement.dbReadNumeric(ALIAS_NB_IMPUTATIONS);
        nbErreurs = statement.dbReadNumeric(ALIAS_NB_ERREURS);
        nbATraiter = statement.dbReadNumeric(ALIAS_NB_A_TRAITER);
        pasTermine = statement.dbReadNumeric(ALIAS_PAS_TERMINE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getDateReceptionLot() {
        return dateReceptionLot;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdTiersEcole() {
        return idTiersEcole;
    }

    public String getLibelleTraitement() {
        return libelleTraitement;
    }

    public String getNbAnnonces() {
        return nbAnnonces;
    }

    public String getNbATraiter() {
        return nbATraiter;
    }

    public String getNbErreurs() {
        return nbErreurs;
    }

    public String getNbImputations() {
        return nbImputations;
    }

    public String getPasTermine() {
        return pasTermine;
    }
}
