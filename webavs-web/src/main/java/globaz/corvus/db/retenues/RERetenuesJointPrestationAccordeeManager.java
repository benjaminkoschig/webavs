/*
 * Créé le 23 août. 07
 */

package globaz.corvus.db.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author BSC
 */

public class RERetenuesJointPrestationAccordeeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateFinDroitGreater = "";
    private String forCsEtatRente = "";
    private List<String> forCsEtatRenteList = null;
    private String forDateDernierPaiement = "";
    private String forEnCoursAtDate = "";
    private String forIdRenteAccordee = "";
    private Boolean forIsPrestationBloquee = null;
    private String forRenteEnCoursAtDate = "";
    private String forRetenueEnCoursAtDate = "";
    private String fromDateFinDroit = "";
    private String orderBy = "";
    private List<String> forCsTypseRetenues = new ArrayList<String>();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        return RERetenuesJointPrestationAccordee.createFromClause(_getCollection());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#_getOrder(globaz.globall.db.BStatement )
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForEnCoursAtDate())) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            // afficher les retenues dont la date de debut est plus petite ou
            // egale a la date donnee
            sqlWhere.append(RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE).append(" <= ")
                    .append(getForEnCoursAtDate());

            sqlWhere.append(" AND (").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" = 0");
            sqlWhere.append(" OR ").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" IS NULL ");
            sqlWhere.append(" OR ").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" >= ")
                    .append(getForEnCoursAtDate()).append(")");
            sqlWhere.append(" AND ");

            // afficher toutes les retenues dont le montant déjà retenu n'est
            // pas égal ou plus grand que le montant total à retenir
            sqlWhere.append("((").append(RERetenuesPaiement.FIELDNAME_MONTANT_DEJA_RETENU).append(" <");
            sqlWhere.append(RERetenuesPaiement.FIELDNAME_MONTANT_TOTAL_A_RETENIR).append(")");
            sqlWhere.append(" OR ");
            // ou si pas de date de fin ou si date de fin > date actuelle (IS)
            sqlWhere.append("(").append(RERetenuesPaiement.FIELDNAME_TYPE_RETENU).append(" = ")
                    .append(IRERetenues.CS_TYPE_IMPOT_SOURCE);
            sqlWhere.append(" AND (").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" = 0");
            sqlWhere.append(" OR ").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" IS NULL ");
            sqlWhere.append(" OR ").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(" >= ")
                    .append(getForEnCoursAtDate()).append(")))");
        }
        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatRente())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtatRente()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getDateFinDroitGreater())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">")
                    .append(_dbWriteNumeric(statement.getTransaction(), getDateFinDroitGreater()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getFromDateFinDroit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtatRente()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForRenteEnCoursAtDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("((");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append("=0").append(" OR ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForRenteEnCoursAtDate()));
            sqlWhere.append(") AND (");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append("<=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForRenteEnCoursAtDate()));
            sqlWhere.append("))");
        }
        if (getForCsEtatRenteList() != null && getForCsEtatRenteList().size() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            int cpt = 0;
            sqlWhere.append("(");
            Iterator iter = getForCsEtatRenteList().iterator();
            while (iter.hasNext()) {
                if (cpt > 0) {
                    sqlWhere.append(" OR ");
                }
                sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append("=").append((String) iter.next());
                cpt++;
            }
            sqlWhere.append(")");
        }

        if (getForIsPrestationBloquee() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                    .append("=")
                    .append(_dbWriteBoolean(statement.getTransaction(), getForIsPrestationBloquee(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForIdRenteAccordee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdRenteAccordee()));
        }

        if (forCsTypseRetenues != null && !forCsTypseRetenues.isEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RERetenuesPaiement.FIELDNAME_TYPE_RETENU);
            sqlWhere.append(" in ").append("(").append(StringUtils.join(forCsTypseRetenues, ',')).append(")");
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERetenuesJointPrestationAccordee();
    }

    /**
     * @param forCsEtatRenteList
     */
    public void addCsEtatRenteList(String csEtat) {
        if (forCsEtatRenteList == null) {
            forCsEtatRenteList = new ArrayList();
        }
        forCsEtatRenteList.add(csEtat);
    }

    /**
     * Récupère le teste sur la date de fin de droit plus grand exclusif à .... (format AAAAMM)
     * 
     * @return
     */
    public String getDateFinDroitGreater() {
        return dateFinDroitGreater;
    }

    /**
     * Récupère le code système de l'état des rentes accordées
     * 
     * @return
     */
    public String getForCsEtatRente() {
        return forCsEtatRente;
    }

    public List getForCsEtatRenteList() {
        return forCsEtatRenteList;
    }

    /**
     * @return the forDateDernierPaiement
     */
    public String getForDateDernierPaiement() {
        return forDateDernierPaiement;
    }

    /**
     * @return
     */
    public String getForEnCoursAtDate() {
        return forEnCoursAtDate;
    }

    /**
     * Récupère l'id de la rente accordée
     * 
     * @return the forIdRenteAccordee
     */
    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public Boolean getForIsPrestationBloquee() {
        return forIsPrestationBloquee;
    }

    /**
     * Récuère la date de teste sur la date de fin de droit à la rente (format AAAAMM)
     * 
     * @return
     */
    public String getForRenteEnCoursAtDate() {
        return forRenteEnCoursAtDate;
    }

    /**
     * Récupère la date de test de validité d'une retenue
     * 
     * @return
     */
    public String getForRetenueEnCoursAtDate() {
        return forRetenueEnCoursAtDate;
    }

    /**
     * Récupère la sélectin sur la date de fin de droit (>=...) (format AAAAMM)
     * 
     * @return
     */
    public String getFromDateFinDroit() {
        return fromDateFinDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderBy()
     */
    @Override
    public String getOrderBy() {
        return orderBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RERetenuesJointPrestationAccordee.FIELDNAME_NOM + ", "
                + RERetenuesJointPrestationAccordee.FIELDNAME_PRENOM + ", "
                + REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE;
    }

    /**
     * Modifie le teste sur la date de fin de droit plus grand exclusif à .... (format AAAAMM)
     * 
     * @param dateFinDroitGreater
     */
    public void setDateFinDroitGreater(String dateFinDroitGreater) {
        this.dateFinDroitGreater = dateFinDroitGreater;
    }

    /**
     * Modifie la sélection sur le code système de l'état des rentes accordées
     * 
     * @param forCsEtatRente
     */
    public void setForCsEtatRente(String forCsEtatRente) {
        this.forCsEtatRente = forCsEtatRente;
    }

    /**
     * @param forDateDernierPaiement
     *            the forDateDernierPaiement to set
     */
    public void setForDateDernierPaiement(String forDateDernierPaiement) {
        this.forDateDernierPaiement = forDateDernierPaiement;
    }

    /**
     * @param string
     */
    public void setForEnCoursAtDate(String string) {
        forEnCoursAtDate = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(string);
    }

    /**
     * Modifie l'id de la rente accordée
     * 
     * @param newForIdRenteAccordee
     *            the forIdRenteAccordee to set
     */
    public void setForIdRenteAccordee(String newForIdRenteAccordee) {
        forIdRenteAccordee = newForIdRenteAccordee;
    }

    public void setForIsPrestationBloquee(Boolean forIsPrestationBloquee) {
        this.forIsPrestationBloquee = forIsPrestationBloquee;
    }

    /**
     * Modifie la date de teste sur la date de fin de droit à la rente (format AAAAMM)
     * 
     * @param forRenteEnCoursAtDate
     */
    public void setForRenteEnCoursAtDate(String forRenteEnCoursAtDate) {
        this.forRenteEnCoursAtDate = forRenteEnCoursAtDate;
    }

    /**
     * Modifie la date de test de validité d'une retenue
     * 
     * @param forRetenueEnCoursAtDate
     */
    public void setForRetenueEnCoursAtDate(String forRetenueEnCoursAtDate) {
        this.forRetenueEnCoursAtDate = forRetenueEnCoursAtDate;
    }

    /**
     * Modifie la sélection sur la date de fin de droit (>=...) (format AAAAMM)
     * 
     * @param fromDateFinDroit
     */
    public void setFromDateFinDroit(String fromDateFinDroit) {
        this.fromDateFinDroit = fromDateFinDroit;
    }

    /**
     * Modifie la sélection sur la date de fin de droit (>=...) (format AAAAMM)
     * 
     * @param fromDateFinDroit
     */
    public void setForTypesRetenues(List<String> forCsTypseRetenues) {
        this.forCsTypseRetenues = forCsTypseRetenues;
    }

    /**
     * Définition de l'instruction SQL ORDER By
     * 
     * @param orderBy
     */
    @Override
    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }
}
