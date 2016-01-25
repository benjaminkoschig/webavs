/*
 * Créé le 17 janvier 2012
 */
package globaz.cygnus.db.recapitulation;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.paiement.RFLot;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;

/**
 * @author MBO
 * @revision JJE
 */
public class RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_SOMME_MONTANT_OV = "SOMME_MONTANT_OV";

    private String datePeriode = "";
    private transient String fromClause = null;
    private String groupeTypesSoins = null;
    private boolean isAvancesSas = false;
    private boolean isFinancementSoins = false;
    private boolean isMoyenAuxiliaire = false;
    private boolean isMutations = false;
    private boolean isPonctuel = false;
    private String sousTypeSoinsComplementaire = null;
    private ArrayList<String> typePrestation = new ArrayList();
    private String typeSoinsComplementaire = null;

    public RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        if (!isMutations) {

            fields.append("SUM(");
            fields.append(RFOrdresVersements.FIELDNAME_MONTANT);
            fields.append(" + ");
            fields.append(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD);
            fields.append(") ");
            fields.append(RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager.ALIAS_SOMME_MONTANT_OV);

        } else {

            fields.append(RFOrdresVersements.FIELDNAME_MONTANT);
            fields.append(" + ");
            fields.append(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD);
            fields.append(" ");
            fields.append(RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager.ALIAS_SOMME_MONTANT_OV);
            fields.append(",");
            fields.append(RFOrdresVersements.FIELDNAME_ID_TIERS);

        }

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation
                            .createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        sqlWhere.append(" IN ");
        sqlWhere.append("(");
        sqlWhere.append(getFromImbrique(_getCollection()));

        sqlWhere.append(" WHERE ");

        if (isPonctuel()) {

            sqlWhere.append("(");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(
                    this._dbWriteDateAMJ(statement.getTransaction(), JadeDateUtil.getFirstDateOfMonth(datePeriode)))
                    .append(" AND ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            JadeDateUtil.getLastDateOfMonth(datePeriode)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append("(");
                sqlWhere.append(RFPrestation.FIELDNAME_TYPE_PRESTATION);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : this.typePrestation) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < this.typePrestation.size()) {
                        sqlWhere.append(", ");
                    }
                }
                sqlWhere.append("))");

                sqlWhere.append(" AND ");
            }

            // Clause des dates de début du retroactif à NULL
            sqlWhere.append("(");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DEBUT_RETRO);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DEBUT_RETRO);
            sqlWhere.append(" = 0");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause des dates de fin du retroactif à NULL
            sqlWhere.append("(");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_FIN_RETRO);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_FIN_RETRO);
            sqlWhere.append(" = 0");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" NOT IN ");
            sqlWhere.append("(");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03);
            sqlWhere.append(", ");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05);
            sqlWhere.append(", ");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11);
            sqlWhere.append(", ");
            sqlWhere.append(IRFTypesDeSoins.CS_FINANCEMENT_DES_SOINS_20);
            sqlWhere.append(", ");
            sqlWhere.append(IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02);
            sqlWhere.append(")");

            sqlWhere.append(" AND ");
            sqlWhere.append("(");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" <> ");
            sqlWhere.append(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES);
            sqlWhere.append(" OR ");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(")");
        }

        // Si recherche des moyens auxiliaires
        if (isMoyenAuxiliaire()) {

            // Clause par date souhaitée
            sqlWhere.append("((");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
            sqlWhere.append(") BETWEEN ");
            sqlWhere.append(
                    this._dbWriteDateAMJ(statement.getTransaction(), JadeDateUtil.getFirstDateOfMonth(datePeriode)))
                    .append(" AND ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            JadeDateUtil.getLastDateOfMonth(datePeriode)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append("(");
                sqlWhere.append(RFPrestation.FIELDNAME_TYPE_PRESTATION);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : this.typePrestation) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < this.typePrestation.size()) {
                        sqlWhere.append(", ");
                    }
                }
                sqlWhere.append("))");

                sqlWhere.append(" AND ");
            }

            // Clause des types de soins égal aux moyens auxiliaires
            sqlWhere.append("(");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03);
            sqlWhere.append(" OR ");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05);
            sqlWhere.append(" OR ");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11);
            sqlWhere.append(")");

        }

        // Si recherche des financements des soins
        if (isFinancementSoins()) {

            // Clause par date souhaitée
            sqlWhere.append("((");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
            sqlWhere.append(") BETWEEN ");
            sqlWhere.append(
                    this._dbWriteDateAMJ(statement.getTransaction(), JadeDateUtil.getFirstDateOfMonth(datePeriode)))
                    .append(" AND ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            JadeDateUtil.getLastDateOfMonth(datePeriode)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append("(");
                sqlWhere.append(RFPrestation.FIELDNAME_TYPE_PRESTATION);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : this.typePrestation) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < this.typePrestation.size()) {
                        sqlWhere.append(", ");
                    }
                }
                sqlWhere.append("))");

                sqlWhere.append(" AND ");
            }

            // Clause des types de soins égal aux financement des soins
            sqlWhere.append("((");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(") = ");
            sqlWhere.append(IRFTypesDeSoins.CS_FINANCEMENT_DES_SOINS_20);
            sqlWhere.append(") ");

        }

        // Si recherche des avances SAS
        if (isAvancesSas) {

            // Clause par date souhaitée
            sqlWhere.append("((");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
            sqlWhere.append(") BETWEEN ");
            sqlWhere.append(
                    this._dbWriteDateAMJ(statement.getTransaction(), JadeDateUtil.getFirstDateOfMonth(datePeriode)))
                    .append(" AND ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            JadeDateUtil.getLastDateOfMonth(datePeriode)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append("(");
                sqlWhere.append(RFPrestation.FIELDNAME_TYPE_PRESTATION);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : this.typePrestation) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < this.typePrestation.size()) {
                        sqlWhere.append(", ");
                    }
                }
                sqlWhere.append("))");

                sqlWhere.append(" AND ");
            }

            // Clause des types de soins égal aux avances SAS
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13);
            sqlWhere.append(" AND ");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES);
            sqlWhere.append(" ");

        }

        // Si recherche des types de soins complémentaires
        if (typeSoinsComplementaire != null) {

            // Clause par date souhaitée
            sqlWhere.append("((");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);
            sqlWhere.append(") BETWEEN ");
            sqlWhere.append(
                    this._dbWriteDateAMJ(statement.getTransaction(), JadeDateUtil.getFirstDateOfMonth(datePeriode)))
                    .append(" AND ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(),
                            JadeDateUtil.getLastDateOfMonth(datePeriode)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append("(");
                sqlWhere.append(RFPrestation.FIELDNAME_TYPE_PRESTATION);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : this.typePrestation) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < this.typePrestation.size()) {
                        sqlWhere.append(", ");
                    }
                }
                sqlWhere.append("))");

                sqlWhere.append(" AND ");
            }

            // Clause des types de soins égal à la liste en paramètre
            sqlWhere.append("(");
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
            sqlWhere.append(" IN (");
            sqlWhere.append(typeSoinsComplementaire);
            sqlWhere.append(")) ");

            // TODO : pas de gestion des sous-types de soins. Attendre création branche 1-11-00 CCVD
            // Exception pour le type et sous-type 13.5 (Soins par la famille)
            // if(this.typeSoinsComplementaire.equals(IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13) &&
            // this.sousTypeSoinsComplementaire.equals(null)){
            // sqlWhere.append(" AND ");
            // RFOrdresVersements.field
            // }

        }

        sqlWhere.append(" AND ");

        // Clause obligeant un état de prestation à VALIDER
        sqlWhere.append("(");
        sqlWhere.append(RFPrestation.FIELDNAME_ETAT);
        sqlWhere.append(" = ");
        sqlWhere.append(IRFPrestations.CS_ETAT_PRESTATION_VALIDE);
        sqlWhere.append(")");

        // TODO BZ_7829 : RECHERCHE DE PRESTATIONS DANS LOTS COMPTABILISES
        // Clause obligeant un état de lot à VALIDER
        // sqlWhere.append(" AND ");

        // sqlWhere.append("(");
        // sqlWhere.append(RELot.FIELDNAME_ETAT);
        // sqlWhere.append(" = ");
        // sqlWhere.append(IRELot.CS_ETAT_LOT_VALIDE);
        // sqlWhere.append(")");

        sqlWhere.append(")");
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation();
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getFromImbrique(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append("SELECT ");
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        fromClauseBuffer.append(" FROM ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        // jointure entre la table des décisions et la table des prestations
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // Jointure de la table des prestations et des ordres de versements
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_PRESTATION);

        // Jointure de la table des prestations avec la table des lots RFM
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_LOT);

        // Jointure entre la table des lots RFM et la table des lots RENTE
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFLot.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFLot.FIELDNAME_ID_LOT_RFM);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RELot.FIELDNAME_ID_LOT);

        return fromClauseBuffer.toString();
    }

    public final String getGroupeTypesSoins() {
        return groupeTypesSoins;
    }

    public final String getSousTypeSoinsComplementaire() {
        return sousTypeSoinsComplementaire;
    }

    public ArrayList<String> getTypePrestation() {
        return typePrestation;
    }

    public final String getTypeSoinsComplementaire() {
        return typeSoinsComplementaire;
    }

    public boolean isAvancesSas() {
        return isAvancesSas;
    }

    public boolean isFinancementSoins() {
        return isFinancementSoins;
    }

    public boolean isMoyenAuxiliaire() {
        return isMoyenAuxiliaire;
    }

    public boolean isMutations() {
        return isMutations;
    }

    public boolean isPonctuel() {
        return isPonctuel;
    }

    public void setAvancesSas(boolean isAvancesSas) {
        this.isAvancesSas = isAvancesSas;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void setFinancementSoins(boolean isFinancementSoins) {
        this.isFinancementSoins = isFinancementSoins;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public final void setGroupeTypesSoins(String groupeTypesSoins) {
        this.groupeTypesSoins = groupeTypesSoins;
    }

    public void setMoyenAuxiliaire(boolean isMoyenAuxiliaire) {
        this.isMoyenAuxiliaire = isMoyenAuxiliaire;
    }

    public void setMutations(boolean isMutations) {
        this.isMutations = isMutations;
    }

    public void setPonctuel(boolean isPonctuel) {
        this.isPonctuel = isPonctuel;
    }

    public final void setSousTypeSoinsComplementaire(String sousTypeSoinsComplementaire) {
        this.sousTypeSoinsComplementaire = sousTypeSoinsComplementaire;
    }

    public void setTypePrestation(ArrayList<String> typePrestation) {
        this.typePrestation = typePrestation;
    }

    public final void setTypeSoinsComplementaire(String typeSoinsComplementaire) {
        this.typeSoinsComplementaire = typeSoinsComplementaire;
    }

}
