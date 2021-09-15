package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Représente un container de type PlanRecouvrement.
 * 
 * @author Arnaud Dostes, 29-mar-2005
 */
public class CAPlanRecouvrementManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private String forAcompte = "";
    private String forCollaborateur = "";
    private String forDate = "";
    private String forDateEchance = "";
    private String forIdAdressePaiement = "";
    private String forIdCompteAnnexe = "";
    private String forIdEtat = "";
    private String forIdExterneRoleLike;
    private String forIdModeRecouvrement = "";
    private List forIdModeRecouvrementIn = new ArrayList();
    private List forIdModeRecouvrementNotIn = new ArrayList();
    private String forIdModeVentilation = "";
    private String forIdPlanRecouvrement = "";
    private String forIdRubrique = "";
    private String forIdSection = "";
    private String forIdTypeEcheance = "";
    private String forLibelle = "";
    private String forPlafond = "";

    private String forPourcentage = "";
    private String forPremierAcompte = "";
    private String forSelectionRole = "";
    private String fromAcompte = "";
    private String fromCollaborateur = "";
    private String fromDate = "";
    private String fromDateEchance = "";
    private String fromIdAdressePaiement = "";
    private String fromIdCompteAnnexe = "";
    private String fromIdEtat = "";
    private String fromIdExternalRole;
    private String fromIdModeRecouvrement = "";
    private String fromIdModeVentilation = "";
    private String fromIdPlanRecouvrement = "";
    private String fromIdRubrique = "";
    private String fromIdSection = "";
    private String fromIdTypeEcheance = "";
    private String fromLibelle = "";

    private String fromPlafond = "";
    private String fromPourcentage = "";
    private String fromPremierAcompte = "";
    private String orderBy = "";

    private String untilIdExternalRole;

    public CAPlanRecouvrementManager() {
        super();

        ArrayList idModeRecouvrementNotIn = new ArrayList();
        idModeRecouvrementNotIn.add(CAPlanRecouvrement.CS_AVANCE_APG);
        idModeRecouvrementNotIn.add(CAPlanRecouvrement.CS_AVANCE_RENTES);
        idModeRecouvrementNotIn.add(CAPlanRecouvrement.CS_AVANCE_IJAI);
        idModeRecouvrementNotIn.add(CAPlanRecouvrement.CS_AVANCE_PTRA);
        setForIdModeRecouvrementNotIn(idModeRecouvrementNotIn);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP;

        if (!JadeStringUtil.isBlank(getForIdExterneRoleLike()) || !JadeStringUtil.isBlank(getForSelectionRole())
                || !JadeStringUtil.isBlank(getFromIdExternalRole())
                || !JadeStringUtil.isBlank(getUntilIdExternalRole())) {
            from += CAPlanRecouvrementManager.INNER_JOIN + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ON "
                    + _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDCOMPTEANNEXE + " = " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP
                    + "." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
        }

        return from;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdPlanRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDPLANRECOUVREMENT + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanRecouvrement());
        }

        // traitement du positionnement
        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_DATE
                    + " = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        // traitement du positionnement
        if (getForLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_LIBELLE
                    + " = " + this._dbWriteString(statement.getTransaction(), getForLibelle());
        }

        // traitement du recouvrement
        if (getForIdModeRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            if (getForIdModeRecouvrement().indexOf(',') != -1) {
                String[] modes = JadeStringUtil.split(getForIdModeRecouvrement(), ',', Integer.MAX_VALUE);

                sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                        + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT + " IN (";

                for (int id = 0; id < modes.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), modes[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                        + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT + " = "
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdModeRecouvrement());
            }
        }

        // traitement du recouvrement
        if ((getForIdModeRecouvrementIn() != null) && (getForIdModeRecouvrementIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT + " IN (";
            Iterator iter = getForIdModeRecouvrementIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere += element + ",";
            }
            sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
            sqlWhere += ")";
        }

        // traitement du recouvrement not in
        if ((getForIdModeRecouvrementNotIn() != null) && (getForIdModeRecouvrementNotIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT + " NOT IN (";
            Iterator iter = getForIdModeRecouvrementNotIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere += element + ",";
            }
            sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
            sqlWhere += ")";
        }

        // traitement du positionnement
        if (getForIdModeVentilation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDMODEVENTILATION + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModeVentilation());
        }

        // traitement du positionnement
        if (getForIdTypeEcheance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDTYPEECHEANCE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeEcheance());
        }

        // traitement du positionnement
        if (getForDateEchance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_DATEECHEANCE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForDateEchance());
        }

        // traitement du positionnement
        if (getForAcompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_ACOMPTE
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForAcompte());
        }

        // traitement du positionnement
        if (getForPremierAcompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_PREMIERACOMPTE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForPremierAcompte());
        }

        // traitement du positionnement
        if (getForPlafond().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_PLAFOND
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForPlafond());
        }

        // traitement du positionnement
        if (getForPourcentage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_POURCENTAGE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForPourcentage());
        }

        // traitement du positionnement
        if (getForIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDETAT
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForIdEtat());
        }

        // traitement du positionnement
        if (getForCollaborateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_COLLABORATEUR + " = "
                    + this._dbWriteString(statement.getTransaction(), getForCollaborateur());
        }

        // traitement du positionnement
        if (getForIdCompteAnnexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDCOMPTEANNEXE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        // traitement du positionnement
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDSECTION
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        // //traitement du positionnement
        // if (getForIdAdressePaiement().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
        // + CAPlanRecouvrement.FIELD_IDADRESSEPAIEMENT +" = " +
        // _dbWriteNumeric(statement.getTransaction(),
        // getForIdAdressePaiement());
        // }

        // traitement du positionnement
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDRUBRIQUE
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // traitement du positionnement
        if (getFromIdPlanRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDPLANRECOUVREMENT + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdPlanRecouvrement());
        }

        if (!JadeStringUtil.isEmpty(getFromIdExternalRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                    + " >= " + this._dbWriteString(statement.getTransaction(), getFromIdExternalRole());
        }
        if (!JadeStringUtil.isEmpty(getUntilIdExternalRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                    + " <= " + this._dbWriteString(statement.getTransaction(), getUntilIdExternalRole());
        }

        // traitement du positionnement
        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_DATE
                    + " >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }

        // traitement du positionnement
        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_LIBELLE
                    + " >= " + this._dbWriteString(statement.getTransaction(), getFromLibelle());
        }

        // traitement du positionnement
        if (getFromIdModeRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT + ">= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdModeRecouvrement());
        }

        // traitement du positionnement
        if (getFromIdModeVentilation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDMODEVENTILATION + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdModeVentilation());
        }

        // traitement du positionnement
        if (getFromIdTypeEcheance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDTYPEECHEANCE + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdTypeEcheance());
        }

        // traitement du positionnement
        if (getFromDateEchance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_DATEECHEANCE + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromDateEchance());
        }

        // traitement du positionnement
        if (getFromAcompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_ACOMPTE
                    + " >= " + this._dbWriteNumeric(statement.getTransaction(), getFromAcompte());
        }

        // traitement du positionnement
        if (getFromPremierAcompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_PREMIERACOMPTE + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromPremierAcompte());
        }

        // traitement du positionnement
        if (getFromPlafond().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_PLAFOND
                    + " >= " + this._dbWriteNumeric(statement.getTransaction(), getFromPlafond());
        }

        // traitement du positionnement
        if (getFromPourcentage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_POURCENTAGE + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromPourcentage());
        }

        // traitement du positionnement
        if (getFromIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDETAT
                    + " >= " + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtat());
        }

        // traitement du positionnement
        if (getFromCollaborateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_COLLABORATEUR + " >= "
                    + this._dbWriteString(statement.getTransaction(), getFromCollaborateur());
        }

        // traitement du positionnement
        if (getFromIdCompteAnnexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
                    + CAPlanRecouvrement.FIELD_IDCOMPTEANNEXE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdCompteAnnexe());
        }

        // traitement du positionnement
        if (getFromIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDSECTION
                    + " >= " + this._dbWriteNumeric(statement.getTransaction(), getFromIdSection());
        }

        // //traitement du positionnement
        // if (getFromIdAdressePaiement().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "."
        // + CAPlanRecouvrement.FIELD_IDADRESSEPAIEMENT + " >= " +
        // _dbWriteNumeric(statement.getTransaction(),
        // getFromIdAdressePaiement());
        // }

        // traitement du positionnement
        if (getFromIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CAPlanRecouvrement.TABLE_CAPLARP + "." + CAPlanRecouvrement.FIELD_IDRUBRIQUE
                    + ">=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdRubrique());
        }

        if (!JadeStringUtil.isBlank(getForIdExterneRoleLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }
            sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                    + " like " + this._dbWriteString(statement.getTransaction(), getForIdExterneRoleLike() + "%");
        }

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += CAPlanRecouvrementManager.AND;
            }

            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDROLE
                        + " IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDROLE + "="
                        + getForSelectionRole();
            }
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAPlanRecouvrement();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForAcompte() {
        return forAcompte;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForCollaborateur() {
        return forCollaborateur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateEchance() {
        return forDateEchance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdAdressePaiement() {
        return forIdAdressePaiement;
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
    public String getForIdEtat() {
        return forIdEtat;
    }

    /**
     * @return
     */
    public String getForIdExterneRoleLike() {
        return forIdExterneRoleLike;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdModeRecouvrement() {
        return forIdModeRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public List getForIdModeRecouvrementIn() {
        return forIdModeRecouvrementIn;
    }

    /**
     * @return
     */
    public List getForIdModeRecouvrementNotIn() {
        return forIdModeRecouvrementNotIn;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdModeVentilation() {
        return forIdModeVentilation;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdPlanRecouvrement() {
        return forIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdRubrique() {
        return forIdRubrique;
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
    public String getForIdTypeEcheance() {
        return forIdTypeEcheance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForLibelle() {
        return forLibelle;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForPlafond() {
        return forPlafond;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForPourcentage() {
        return forPourcentage;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForPremierAcompte() {
        return forPremierAcompte;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromAcompte() {
        return fromAcompte;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromCollaborateur() {
        return fromCollaborateur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateEchance() {
        return fromDateEchance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdAdressePaiement() {
        return fromIdAdressePaiement;
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
    public String getFromIdEtat() {
        return fromIdEtat;
    }

    public String getFromIdExternalRole() {
        return fromIdExternalRole;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdModeRecouvrement() {
        return fromIdModeRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdModeVentilation() {
        return fromIdModeVentilation;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdPlanRecouvrement() {
        return fromIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdRubrique() {
        return fromIdRubrique;
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
    public String getFromIdTypeEcheance() {
        return fromIdTypeEcheance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromLibelle() {
        return fromLibelle;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromPlafond() {
        return fromPlafond;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromPourcentage() {
        return fromPourcentage;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromPremierAcompte() {
        return fromPremierAcompte;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @return the untilIdExternalRole
     */
    public String getUntilIdExternalRole() {
        return untilIdExternalRole;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForAcompte(String string) {
        forAcompte = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForCollaborateur(String string) {
        forCollaborateur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDate(String string) {
        forDate = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateEchance(String string) {
        forDateEchance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdAdressePaiement(String string) {
        forIdAdressePaiement = string;
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
    public void setForIdEtat(String string) {
        forIdEtat = string;
    }

    /**
     * @param string
     */
    public void setForIdExterneRoleLike(String string) {
        forIdExterneRoleLike = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdModeRecouvrement(String string) {
        forIdModeRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdModeRecouvrementIn(List list) {
        forIdModeRecouvrementIn = list;
    }

    /**
     * @param list
     */
    public void setForIdModeRecouvrementNotIn(List list) {
        forIdModeRecouvrementNotIn = list;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdModeVentilation(String string) {
        forIdModeVentilation = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdPlanRecouvrement(String string) {
        forIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdRubrique(String string) {
        forIdRubrique = string;
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
    public void setForIdTypeEcheance(String string) {
        forIdTypeEcheance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForLibelle(String string) {
        forLibelle = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForPlafond(String string) {
        forPlafond = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForPourcentage(String string) {
        forPourcentage = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForPremierAcompte(String string) {
        forPremierAcompte = string;
    }

    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromAcompte(String string) {
        fromAcompte = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromCollaborateur(String string) {
        fromCollaborateur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDate(String string) {
        fromDate = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateEchance(String string) {
        fromDateEchance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdAdressePaiement(String string) {
        fromIdAdressePaiement = string;
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
    public void setFromIdEtat(String string) {
        fromIdEtat = string;
    }

    public void setFromIdExternalRole(String fromIdExternalRole) {
        this.fromIdExternalRole = fromIdExternalRole;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdModeRecouvrement(String string) {
        fromIdModeRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdModeVentilation(String string) {
        fromIdModeVentilation = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdPlanRecouvrement(String string) {
        fromIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdRubrique(String string) {
        fromIdRubrique = string;
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
    public void setFromIdTypeEcheance(String string) {
        fromIdTypeEcheance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromLibelle(String string) {
        fromLibelle = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromPlafond(String string) {
        fromPlafond = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromPourcentage(String string) {
        fromPourcentage = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromPremierAcompte(String string) {
        fromPremierAcompte = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    /**
     * @param untilIdExternalRole
     *            the untilIdExternalRole to set
     */
    public void setUntilIdExternalRole(String untilIdExternalRole) {
        this.untilIdExternalRole = untilIdExternalRole;
    }

}
