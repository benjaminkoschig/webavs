/*
 * Créé le 17 janvier 2012
 */
package globaz.cygnus.db.recapitulation;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * @author MBO
 */
public class RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_SOMME_MONTANT = "SOMME_MONTANT";

    private String datePeriode = "";
    private String datePeriodeAAAAMM = "";
    private transient String fromClause = "";
    private Boolean isAdaptation = false;
    private Boolean isAugmentation = false;
    private Boolean isAugmentationAdaptation = false;
    private Boolean isCourant = false;
    private Boolean isDiminution = false;
    private Boolean isFuturMensuel = false;
    private Boolean isRetro = false;
    private Boolean isSum = false;

    private String montantTotal = "";
    private ArrayList<String> typePrestation = new ArrayList();

    public RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        if (isSum) {
            fields.append("SUM(");
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(") ");
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager.ALIAS_SOMME_MONTANT);
        } else {
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(" ")
                    .append(RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager.ALIAS_SOMME_MONTANT)
                    .append(" ,");
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(",");
            fields.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(",");
            fields.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(",");
            fields.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION).append(",");
            fields.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(",");
            fields.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);

        }

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (JadeStringUtil.isBlank(fromClause)) {
            StringBuffer from = new StringBuffer(
                    RFPrestationAccordeeJointREPrestationAccordeeRecapitulation.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append("(");
        sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sqlWhere.append(" = ");
        sqlWhere.append(IREPrestationAccordee.CS_ETAT_VALIDE);
        sqlWhere.append(")");

        if (isAugmentation) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), datePeriode));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sqlWhere.append(" >= ");
            sqlWhere.append(datePeriodeAAAAMM);
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }
        }

        if (isAugmentationAdaptation) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), datePeriode));
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }
        }

        if (isDiminution) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            if (isAdaptation) {

                sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), false, BConstants.DB_TYPE_BOOLEAN_CHAR));
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), datePeriode));
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }

        }

        if (isRetro) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" <= ");
            sqlWhere.append(datePeriodeAAAAMM);
            sqlWhere.append(" AND ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" <> ");
            sqlWhere.append("0");
            sqlWhere.append(" AND ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" IS NOT NULL ");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), datePeriode));
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }

        }

        if (isFuturMensuel) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), ("01." + datePeriode)));
            // JadeDateUtil.addMonths("01." + this.datePeriode, 1)));
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sqlWhere.append(" >= ");
            sqlWhere.append(datePeriodeAAAAMM);
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }

        }

        if (isCourant) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sqlWhere.append(" < ");
            sqlWhere.append(datePeriodeAAAAMM);
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" = ");
            sqlWhere.append("0");
            sqlWhere.append(" OR ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" > ");
            sqlWhere.append(datePeriodeAAAAMM);
            sqlWhere.append(" OR ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), datePeriode));
            sqlWhere.append(")");

            // Clause par type de prestation
            if ((null != typePrestation) && (typePrestation.size() > 0)) {
                sqlWhere.append(" AND ");

                sqlWhere.append("(");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
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
            }
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrestationAccordeeJointREPrestationAccordeeRecapitulation();
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public String getDatePeriodeAAAAMM() {
        return datePeriodeAAAAMM;
    }

    public String getFromClause() {
        return fromClause;
    }

    public Boolean getIsAdaptation() {
        return isAdaptation;
    }

    public Boolean getIsAugmentation() {
        return isAugmentation;
    }

    public Boolean getIsCourant() {
        return isCourant;
    }

    public Boolean getIsDiminution() {
        return isDiminution;
    }

    public Boolean getIsFuturMensuel() {
        return isFuturMensuel;
    }

    public Boolean getIsRetro() {
        return isRetro;
    }

    public Boolean getIsSum() {
        return isSum;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public ArrayList<String> getTypePrestation() {
        return typePrestation;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void setDatePeriodeAAAAMM(String datePeriodeAAAAMM) {
        this.datePeriodeAAAAMM = datePeriodeAAAAMM;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIsAdaptation(Boolean isAdaptation) {
        this.isAdaptation = isAdaptation;
    }

    public void setIsAugmentation(Boolean isAugmentation) {
        this.isAugmentation = isAugmentation;
    }

    public void setIsAugmentationAdaptation(Boolean isAugmentationAdaptation) {
        this.isAugmentationAdaptation = isAugmentationAdaptation;
    }

    public void setIsCourant(Boolean isCourant) {
        this.isCourant = isCourant;
    }

    public void setIsDiminution(Boolean isDiminution) {
        this.isDiminution = isDiminution;
    }

    public void setIsFuturMensuel(Boolean isFuturMensuel) {
        this.isFuturMensuel = isFuturMensuel;
    }

    public void setIsRetro(Boolean isRetro) {
        this.isRetro = isRetro;
    }

    public void setIsSum(Boolean isSum) {
        this.isSum = isSum;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setTypePrestation(ArrayList<String> typePrestation) {
        this.typePrestation = typePrestation;
    }

}
