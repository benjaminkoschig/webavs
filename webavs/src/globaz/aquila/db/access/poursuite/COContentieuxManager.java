package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.List;

/**
 * Représente un container de type Contentieux
 * 
 * @author Pascal Lovy, 15-oct-2004
 */
public class COContentieuxManager extends COBManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int TRI_CA_IDEXTERNEROLE_IDROLE = 3;
    public static final int TRI_CA_IDROLE_IDEXTERNEROLE = 2;
    public static final int TRI_DESCRIPTION_IDCOMPTEANNEXE = 1;

    private String beforeNumAffilie = "";
    private String beforeProchaineDateDeclenchement = "";
    private boolean forBulletinNeutre = false;
    private String forDateDeclenchement = "";
    private String forDateExecution = "";
    private String forDateOuverture = "";
    private String forIdCategorie;
    private String forIdCompteAnnexe = "";
    private String forIdContentieux = "";
    private String forIdEtape = "";
    private String forIdGenreCompte;
    private String forIdSection = "";
    private String forIdSequence = "";
    private List<String> forIdsEtapes;
    private List<String> forIdsRoles; // String forSelectionRole
    private String forIdTypeSection = "";
    private String forMontantInitial = "";
    private String forMontantInitialOperator = "=";
    private String forMotif = "";
    private String forNotModeCompensation;
    private String forNumAffilie = "";
    private String forNumPoursuite = "";
    private String forNumSection = "";
    private String forProchaineDateDeclenchement = "";
    private String forRemarque = "";

    private String forSelectionRole = "";
    private String forSolde = "";
    private String forSoldeOperator = "=";
    private String forTriListeCA = "";
    private String forTriListeSection = "";
    private List<String> forTypesSections;
    private String forUser = "";
    private String fromDateDeclenchement = "";
    private String fromDateExecution = "";
    private String fromDateOuverture = "";
    private String fromIdCompteAnnexe = "";
    private String fromIdContentieux = "";
    private String fromIdEtape = "";
    private String fromIdSection = "";
    private String fromIdSequence = "";

    private String fromMotif = "";
    private String fromNumAffilie = "";
    private String fromNumPoursuite = "";
    private String fromNumSection = "";

    private String fromProchaineDateDeclenchement = "";
    private String fromRemarque = "";
    private String fromUser = "";

    private String likeNumSection = "";
    private String toDateDeclenchement = "";
    private String toDateExecution = "";
    private String toDateOuverture = "";

    /**
     * Initialise le manager
     */
    public COContentieuxManager() {
        /*
         * Les méthodes _beforeFind et _afterFind sont utilisées pour compléter les recherches sur des propriétés
         * d'objets liés.
         */
        wantCallMethodBeforeFind(true);
        wantCallMethodAfterFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer("");
        addField(sqlFields, ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT);
        addField(sqlFields, ICOContentieuxConstante.FNAME_DATE_EXECUTION);
        addField(sqlFields, ICOContentieuxConstante.FNAME_DATE_OUVERTURE);
        addField(sqlFields, ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE);
        addField(sqlFields, _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        addField(sqlFields, _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_ETAPE);
        addField(sqlFields, ICOContentieuxConstante.FNAME_ID_SECTION);
        addField(sqlFields, _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_SEQUENCE);
        addField(sqlFields, ICOContentieuxConstante.FNAME_MONTANT_INITIAL);
        addField(sqlFields, ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL);
        addField(sqlFields, ICOContentieuxConstante.FNAME_REMARQUE);
        addField(sqlFields, ICOContentieuxConstante.FNAME_USERNAME);
        addField(sqlFields, _getTableName() + "." + BSpy.FIELDNAME);
        addField(sqlFields, ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE_PRINCIPAL);

        // Pour le tri par ORDER BY
        // Traitement du tri du compte annexe de la liste et des documents
        if ((getForTriListeCA() != null) && (getForTriListeCA().length() != 0)) {
            switch (Integer.parseInt(getForTriListeCA())) {
                case 1:
                    addField(sqlFields, CACompteAnnexe.FIELD_DESCRIPTION);
                    addField(sqlFields, _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                            + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
                    break;
                case 2:
                    addField(sqlFields, CACompteAnnexe.FIELD_IDROLE);
                    addField(sqlFields, CACompteAnnexe.FIELD_IDEXTERNEROLE);
                    break;
                default:
                    break;
            }
        }

        // Traitement du tri de la section de la liste et des documents
        if ((getForTriListeSection() != null) && (getForTriListeSection().length() != 0)) {
            switch (Integer.parseInt(getForTriListeSection())) {
                case 1:
                    addField(sqlFields, CASection.FIELD_IDEXTERNE);
                    break;
                case 2:
                    addField(sqlFields, CASection.FIELD_DATESECTION);
                    break;
                default:
                    break;
            }
        }

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS);
        addJointures(from);
        return from.toString();
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer _order = new StringBuffer();

        // Traitement du tri du compte annexe de la liste et des documents
        if (getForTriListeCA().length() != 0) {
            if (JadeStringUtil.isBlank(getForNumPoursuite())) {
                switch (Integer.parseInt(getForTriListeCA())) {
                    case TRI_DESCRIPTION_IDCOMPTEANNEXE:
                        _order.append(CACompteAnnexe.FIELD_DESCRIPTION);
                        _order.append(", ");
                        _order.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
                        break;
                    case TRI_CA_IDROLE_IDEXTERNEROLE:
                        _order.append(CACompteAnnexe.FIELD_IDROLE);
                        _order.append(", ");
                        _order.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
                        break;
                    case TRI_CA_IDEXTERNEROLE_IDROLE:
                        _order.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
                        _order.append(", ");
                        _order.append(CACompteAnnexe.FIELD_IDROLE);
                    default:
                        break;
                }
            }
        }

        // Traitement du tri de la section de la liste et des documents
        if (getForTriListeSection().length() != 0) {
            if (_order.length() >= 1) {
                _order.append(", ");
            }
            switch (java.lang.Integer.parseInt(getForTriListeSection())) {
                case 1:
                    _order.append(CASection.FIELD_IDEXTERNE);
                    break;
                case 2:
                    _order.append(CASection.FIELD_DATESECTION);
                    break;
                default:
                    break;
            }
        }

        return _order.toString();
    }

    /**
     * @return Le nom de la table principale
     */
    protected String _getTableName() {
        return _getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdContentieux())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_CONTENTIEUX + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdContentieux()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdSection())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_SECTION + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateOuverture())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_OUVERTURE + COBManager.EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateOuverture()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateDeclenchement())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT + COBManager.EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDeclenchement()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForDateExecution())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_EXECUTION + COBManager.EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateExecution()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForProchaineDateDeclenchement())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL + COBManager.EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForProchaineDateDeclenchement()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForMontantInitial()) && !JadeStringUtil.isBlank(getForMontantInitialOperator())) {
            addCondition(sqlWhere, ICOContentieuxConstante.FNAME_MONTANT_INITIAL + getForMontantInitialOperator()
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontantInitial()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForUser())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_USERNAME + COBManager.EGAL
                            + this._dbWriteString(statement.getTransaction(), getForUser()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdEtape())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_ETAPE + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdEtape()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdSequence())) {
            addCondition(sqlWhere, _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_SEQUENCE + COBManager.EGAL
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSequence()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForMotif())) {
            addCondition(sqlWhere,
                    "motif" + COBManager.EGAL + this._dbWriteString(statement.getTransaction(), getForMotif()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForRemarque())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_REMARQUE + COBManager.EGAL
                            + this._dbWriteString(statement.getTransaction(), getForRemarque()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdContentieux())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_CONTENTIEUX + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdContentieux()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdCompteAnnexe())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdCompteAnnexe()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdSection())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_SECTION + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdSection()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromDateOuverture())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_OUVERTURE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateOuverture()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromDateDeclenchement())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDeclenchement()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getToDateDeclenchement())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_DECLENCHEMENT + COBManager.PLUS_PETIT_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getToDateDeclenchement()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromDateExecution())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_EXECUTION + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateExecution()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getToDateExecution())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_EXECUTION + COBManager.PLUS_PETIT_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), getToDateExecution()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromProchaineDateDeclenchement())) {
            addCondition(sqlWhere, ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL + COBManager.PLUS_GRAND_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromProchaineDateDeclenchement()));
        }

        if (!JadeStringUtil.isBlank(getBeforeProchaineDateDeclenchement())) {
            addCondition(sqlWhere, ICOContentieuxConstante.FNAME_PROCHAINE_DATE_DECL + COBManager.PLUS_PETIT_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getBeforeProchaineDateDeclenchement()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromUser())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_USERNAME + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromUser()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdEtape())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_ETAPE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtape()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromIdSequence())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_ID_SEQUENCE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromIdSequence()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromMotif())) {
            addCondition(
                    sqlWhere,
                    "motif" + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromMotif()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getFromRemarque())) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_REMARQUE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromRemarque()));
        }

        /*
         * Recherche sur les colonnes liées
         */
        if (!JadeStringUtil.isBlank(getForSolde()) && !JadeStringUtil.isBlank(getForSoldeOperator())) {
            addCondition(sqlWhere, _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE
                    + getForSoldeOperator() + this._dbWriteNumeric(statement.getTransaction(), getForSolde()));
        }

        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            // addCondition(sqlWhere, CACompteAnnexe.FIELD_IDCATEGORIE + EGAL +
            // _dbWriteNumeric(statement.getTransaction(), getForIdCategorie()));
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            if (getForIdCategorie().indexOf(',') != -1) {
                String[] categories = JadeStringUtil.split(getForIdCategorie(), ',', Integer.MAX_VALUE);

                sqlWhere.append(CACompteAnnexe.FIELD_IDCATEGORIE + " IN (");

                for (int idCategorie = 0; idCategorie < categories.length; ++idCategorie) {
                    if (idCategorie > 0) {
                        sqlWhere.append(",");
                    }
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), categories[idCategorie]));
                }
                sqlWhere.append(")");
            } else {
                sqlWhere.append(CACompteAnnexe.FIELD_IDCATEGORIE + COBManager.EGAL
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie()));
            }
        }

        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            addCondition(
                    sqlWhere,
                    CACompteAnnexe.FIELD_IDGENRECOMPTE + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte()));
        }

        if (!JadeStringUtil.isBlank(getForNumAffilie())) {
            addCondition(
                    sqlWhere,
                    CACompteAnnexe.FIELD_IDEXTERNEROLE + COBManager.LIKE
                            + this._dbWriteString(statement.getTransaction(), getForNumAffilie() + "%"));
        }

        if (!JadeStringUtil.isBlank(getForNumSection())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_IDEXTERNE + COBManager.LIKE
                            + this._dbWriteString(statement.getTransaction(), "%" + getForNumSection() + "%"));
        }

        if (!JadeStringUtil.isBlank(getForIdTypeSection()) && jointureSection()) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_IDTYPESECTION + COBManager.EGAL
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection()));
        }

        if (!JadeStringUtil.isBlank(getLikeNumSection())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_IDEXTERNE + COBManager.LIKE
                            + this._dbWriteString(statement.getTransaction(), getLikeNumSection() + "%"));
        }

        if (!JadeStringUtil.isBlank(getFromNumAffilie())) {
            addCondition(
                    sqlWhere,
                    CACompteAnnexe.FIELD_IDEXTERNEROLE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getBeforeNumAffilie())) {
            addCondition(
                    sqlWhere,
                    CACompteAnnexe.FIELD_IDEXTERNEROLE + COBManager.PLUS_PETIT_EGAL
                            + this._dbWriteString(statement.getTransaction(), getBeforeNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getFromNumSection())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_IDEXTERNE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromNumSection()));
        }

        if (!JadeStringUtil.isBlank(getFromNumPoursuite())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_NOPOURSUITE + COBManager.PLUS_GRAND_EGAL
                            + this._dbWriteString(statement.getTransaction(), getFromNumPoursuite()));
        }

        if (!JadeStringUtil.isIntegerEmpty(toDateOuverture)) {
            addCondition(
                    sqlWhere,
                    ICOContentieuxConstante.FNAME_DATE_OUVERTURE + COBManager.PLUS_PETIT_EGAL
                            + this._dbWriteDateAMJ(statement.getTransaction(), toDateOuverture));
        }

        if (!JadeStringUtil.isEmpty(getForNotModeCompensation())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_IDMODECOMPENSATION + COBManager.DIFFERENT
                            + this._dbWriteNumeric(statement.getTransaction(), getForNotModeCompensation()));
        }

        // Traitement du positionnement pour une sélection du rôle
        if (!JadeStringUtil.isBlank(getForSelectionRole()) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere.append(CACompteAnnexe.FIELD_IDROLE + " IN (");

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere.append(",");
                    }
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), roles[idRole]));
                }
                sqlWhere.append(")");
            } else {
                sqlWhere.append(CACompteAnnexe.FIELD_IDROLE + COBManager.EGAL
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole()));
            }
        }

        // traitement de la liste de roles
        if (getForIdsRoles() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(CACompteAnnexe.FIELD_IDROLE + " IN (");
            for (int i = 0; i < getForIdsRoles().size(); i++) {
                sqlWhere.append(getForIdsRoles().get(i));
                if (i != getForIdsRoles().size() - 1) {
                    sqlWhere.append(", ");
                }
            }
            sqlWhere.append(")");
        }

        // Permet de gèrer l'idTypeSection
        if (getForTypesSections() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            sqlWhere.append(CASection.FIELD_IDTYPESECTION + " IN (");
            for (int i = 0; i < getForTypesSections().size(); i++) {
                sqlWhere.append(getForTypesSections().get(i));
                if (i != getForTypesSections().size() - 1) {
                    sqlWhere.append(", ");
                }
            }
            sqlWhere.append(")");
        }

        // Etapes suivantes
        if (getForIdsEtapes() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }

            /*
             * <code> SELECT ODIETA FROM WEBAVSCIAM.COTRANP WHERE OGBAUT='1' AND OGIESU IN (1,43,31) ORDER BY ODIETA
             * </code>
             */
            sqlWhere.append(ICOContentieuxConstante.FNAME_ID_ETAPE + " IN (");
            sqlWhere.append(COBManager.SELECT);
            sqlWhere.append(COTransition.FNAME_ID_ETAPE);
            sqlWhere.append(COBManager.FROM);
            sqlWhere.append(_getCollection() + COTransition.TABLE_NAME);
            sqlWhere.append(COBManager.WHERE);
            sqlWhere.append(COTransition.FNAME_AUTO + COBManager.EGAL + BConstants.DB_BOOLEAN_TRUE_DELIMITED);
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COTransition.FNAME_ID_ETAPE_SUIVANTE);
            sqlWhere.append(COBManager.IN + "(");

            for (int i = 0; i < getForIdsEtapes().size(); i++) {
                sqlWhere.append(getForIdsEtapes().get(i));
                if (i != getForIdsEtapes().size() - 1) {
                    sqlWhere.append(", ");
                }
            }
            sqlWhere.append(")");
            sqlWhere.append(")");
        }

        if (getForBulletinNeutre()) {
            addCondition(sqlWhere, CASection.FIELD_PMTCMP + COBManager.EGAL + "0");
            addCondition(sqlWhere, CASection.FIELD_STATUTBN + COBManager.DIFFERENT + APISection.STATUTBN_ANNULE);
            addCondition(sqlWhere, CASection.FIELD_STATUTBN + COBManager.DIFFERENT + APISection.STATUTBN_DECOMPTE_FINAL);
        }

        // Doit-etre à la fin
        if (!JadeStringUtil.isBlank(getForNumPoursuite())) {
            addCondition(
                    sqlWhere,
                    CASection.FIELD_NOPOURSUITE + COBManager.EGAL
                            + this._dbWriteString(statement.getTransaction(), getForNumPoursuite()));
            // Recherche du numéro de poursuite dans l'historique
            addSearchInHistorique(statement, sqlWhere);
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COContentieux();
    }

    /**
     * @param fields
     * @param columnName
     * @return
     */
    private String addField(StringBuffer fields, String columnName) {
        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(columnName);

        return fields.toString();
    }

    /**
     * @param buffer
     */
    protected void addJointures(StringBuffer buffer) {
        if (jointureSection()) {
            buffer.append(COBManager.INNER_JOIN);
            buffer.append(_getCollection());
            buffer.append(CASection.TABLE_CASECTP);
            buffer.append(COBManager.ON);
            buffer.append(ICOContentieuxConstante.FNAME_ID_SECTION);
            buffer.append("=");
            buffer.append(CASection.FIELD_IDSECTION);
        }
        if (jointureCA()) {
            buffer.append(COBManager.INNER_JOIN);
            buffer.append(_getCollection());
            buffer.append(CACompteAnnexe.TABLE_CACPTAP);
            buffer.append(COBManager.ON);
            buffer.append(ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE);
            buffer.append("=");
            buffer.append(_getCollection());
            buffer.append(CACompteAnnexe.TABLE_CACPTAP);
            buffer.append(".");
            buffer.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        }
    }

    /**
     * Recherche du numéro de poursuite dans l'historique. <br>
     * Ajout l'UNION et la requete nécessaire.
     * 
     * @param statement
     * @param sqlWhere
     */
    private void addSearchInHistorique(BStatement statement, StringBuffer sqlWhere) {
        sqlWhere.append(COBManager.UNION);
        sqlWhere.append(COBManager.SELECT);
        sqlWhere.append(_getFields(statement));
        sqlWhere.append(COBManager.FROM);
        sqlWhere.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP);
        sqlWhere.append(COBManager.INNER_JOIN);
        sqlWhere.append("(");
        sqlWhere.append(_getTableName());
        sqlWhere.append(COBManager.INNER_JOIN);
        sqlWhere.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME);
        sqlWhere.append(COBManager.ON);
        sqlWhere.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        sqlWhere.append(COBManager.EGAL);
        sqlWhere.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        sqlWhere.append(")");
        sqlWhere.append(COBManager.ON + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + ".IDCOMPTEANNEXE");
        sqlWhere.append(COBManager.EGAL + _getTableName() + "." + ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE);
        sqlWhere.append(COBManager.AND);
        sqlWhere.append(ICOHistoriqueConstante.FNAME_NUMERO_POURSUITE);
        sqlWhere.append(COBManager.EGAL);
        sqlWhere.append(this._dbWriteString(statement.getTransaction(), getForNumPoursuite()));
        sqlWhere.append(COBManager.AND);
        sqlWhere.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                + ICOHistoriqueConstante.FNAME_EST_ANNULE + " = " + BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        sqlWhere.append(COBManager.LEFT_OUTER_JOIN);
        sqlWhere.append(_getCollection() + CASection.TABLE_CASECTP);
        sqlWhere.append(COBManager.ON);
        sqlWhere.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_ID_SECTION + COBManager.EGAL
                + _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
    }

    /**
     * @return the beforeNumAffilie
     */
    public String getBeforeNumAffilie() {
        return beforeNumAffilie;
    }

    public String getBeforeProchaineDateDeclenchement() {
        return beforeProchaineDateDeclenchement;
    }

    /**
     * @return the forSectionPmtCmpIsZero
     */
    public boolean getForBulletinNeutre() {
        return forBulletinNeutre;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateDeclenchement() {
        return forDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateExecution() {
        return forDateExecution;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateOuverture() {
        return forDateOuverture;
    }

    /**
     * @return the forIdCategorie
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdContentieux() {
        return forIdContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdEtape() {
        return forIdEtape;
    }

    /**
     * @return the forIdGenreCompte
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * @return the forIdsEtapes
     */
    public List<String> getForIdsEtapes() {
        return forIdsEtapes;
    }

    /**
     * @return the forIdsRoles
     */
    public List<String> getForIdsRoles() {
        return forIdsRoles;
    }

    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForMontantInitial() {
        return forMontantInitial;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForMontantInitialOperator() {
        return forMontantInitialOperator;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForMotif() {
        return forMotif;
    }

    /**
     * @return the forNotModeCompensation
     */
    public String getForNotModeCompensation() {
        return forNotModeCompensation;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForNumPoursuite() {
        return forNumPoursuite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForNumSection() {
        return forNumSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForProchaineDateDeclenchement() {
        return forProchaineDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForRemarque() {
        return forRemarque;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForSolde() {
        return forSolde;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForSoldeOperator() {
        return forSoldeOperator;
    }

    /**
     * @return the forTriListeCA
     */
    public String getForTriListeCA() {
        return forTriListeCA;
    }

    /**
     * @return the forTriListeSection
     */
    public String getForTriListeSection() {
        return forTriListeSection;
    }

    /**
     * @return the forTypesSections
     */
    public List<String> getForTypesSections() {
        return forTypesSections;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForUser() {
        return forUser;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateDeclenchement() {
        return fromDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateExecution() {
        return fromDateExecution;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateOuverture() {
        return fromDateOuverture;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdCompteAnnexe() {
        return fromIdCompteAnnexe;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdContentieux() {
        return fromIdContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdEtape() {
        return fromIdEtape;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdSection() {
        return fromIdSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdSequence() {
        return fromIdSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromMotif() {
        return fromMotif;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromNumPoursuite() {
        return fromNumPoursuite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromNumSection() {
        return fromNumSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromProchaineDateDeclenchement() {
        return fromProchaineDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromRemarque() {
        return fromRemarque;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromUser() {
        return fromUser;
    }

    public String getLikeNumSection() {
        return likeNumSection;
    }

    public String getToDateDeclenchement() {
        return toDateDeclenchement;
    }

    public String getToDateExecution() {
        return toDateExecution;
    }

    public String getToDateOuverture() {
        return toDateOuverture;
    }

    /**
     * @return
     */
    protected boolean jointureCA() {
        return !JadeStringUtil.isEmpty(getForNumAffilie()) || !JadeStringUtil.isEmpty(getFromNumAffilie())
                || !JadeStringUtil.isEmpty(getBeforeNumAffilie()) || !JadeStringUtil.isEmpty(getForTriListeCA());
    }

    /**
     * @return
     */
    protected boolean jointureSection() {
        return !JadeStringUtil.isEmpty(getForSolde()) || !JadeStringUtil.isEmpty(getForNumSection())
                || !JadeStringUtil.isEmpty(getForNumPoursuite()) || !JadeStringUtil.isEmpty(getForNumSection())
                || !JadeStringUtil.isEmpty(getFromNumPoursuite()) || !JadeStringUtil.isEmpty(getForTriListeSection());
    }

    /**
     * @param beforeNumAffilie
     */
    public void setBeforeNumAffilie(String beforeNumAffilie) {
        this.beforeNumAffilie = beforeNumAffilie;
    }

    public void setBeforeProchaineDateDeclenchement(String string) {
        beforeProchaineDateDeclenchement = string;
    }

    /**
     * @param forSectionPmtCmpIsZero
     *            the forSectionPmtCmpIsZero to set
     */
    public void setForBulletinNeutre(boolean forSectionPmtCmpIsZero) {
        forBulletinNeutre = forSectionPmtCmpIsZero;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateDeclenchement(String string) {
        forDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateExecution(String string) {
        forDateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateOuverture(String string) {
        forDateOuverture = string;
    }

    /**
     * @param forIdCategorie
     *            the forIdCategorie to set
     */
    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdCompteAnnexe(String string) {
        forIdCompteAnnexe = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdContentieux(String string) {
        forIdContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdEtape(String string) {
        forIdEtape = string;
    }

    /**
     * @param forIdGenreCompte
     *            the forIdGenreCompte to set
     */
    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdSection(String string) {
        forIdSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdSequence(String string) {
        forIdSequence = string;
    }

    /**
     * @param forIdsEtapes
     *            the forIdsEtapes to set
     */
    public void setForIdsEtapes(List<String> forIdsEtapes) {
        this.forIdsEtapes = forIdsEtapes;
    }

    /**
     * @param forIdsRoles
     *            the forIdsRoles to set
     */
    public void setForIdsRoles(List<String> forIdsRoles) {
        this.forIdsRoles = forIdsRoles;
    }

    public void setForIdTypeSection(String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForMontantInitial(String string) {
        forMontantInitial = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForMontantInitialOperator(String string) {
        forMontantInitialOperator = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForMotif(String string) {
        forMotif = string;
    }

    /**
     * @param forNotModeCompensation
     *            the forNotModeCompensation to set
     */
    public void setForNotModeCompensation(String forNotModeCompensation) {
        this.forNotModeCompensation = forNotModeCompensation;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForNumAffilie(String string) {
        forNumAffilie = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForNumPoursuite(String string) {
        forNumPoursuite = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForNumSection(String string) {
        forNumSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForProchaineDateDeclenchement(String string) {
        forProchaineDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForRemarque(String string) {
        forRemarque = string;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForSolde(String string) {
        forSolde = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForSoldeOperator(String string) {
        forSoldeOperator = string;
    }

    /**
     * @param forTriListeCA
     *            the forTriListeCA to set
     */
    public void setForTriListeCA(String forTriListeCA) {
        this.forTriListeCA = forTriListeCA;
    }

    /**
     * @param forTriListeSection
     *            the forTriListeSection to set
     */
    public void setForTriListeSection(String forTriListeSection) {
        this.forTriListeSection = forTriListeSection;
    }

    /**
     * @param forTypesSections
     *            the forTypesSections to set
     */
    public void setForTypesSections(List<String> forTypesSections) {
        this.forTypesSections = forTypesSections;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForUser(String string) {
        forUser = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateDeclenchement(String string) {
        fromDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateExecution(String string) {
        fromDateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateOuverture(String string) {
        fromDateOuverture = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdCompteAnnexe(String string) {
        fromIdCompteAnnexe = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdContentieux(String string) {
        fromIdContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdEtape(String string) {
        fromIdEtape = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdSection(String string) {
        fromIdSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdSequence(String string) {
        fromIdSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromMotif(String string) {
        fromMotif = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromNumAffilie(String string) {
        fromNumAffilie = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromNumPoursuite(String string) {
        fromNumPoursuite = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromNumSection(String string) {
        fromNumSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromProchaineDateDeclenchement(String string) {
        fromProchaineDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromRemarque(String string) {
        fromRemarque = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromUser(String string) {
        fromUser = string;
    }

    public void setLikeNumSection(String newLikeNumSection) {
        likeNumSection = newLikeNumSection;
    }

    public void setToDateDeclenchement(String newToDateDeclenchement) {
        toDateDeclenchement = newToDateDeclenchement;
    }

    public void setToDateExecution(String newToDateExecution) {
        toDateExecution = newToDateExecution;
    }

    public void setToDateOuverture(String string) {
        toDateOuverture = string;
    }
}
