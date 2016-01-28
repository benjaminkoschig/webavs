/*
 * Créé le 10 juin 05
 */
package globaz.apg.db.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APAnnonceAPGHierarchiqueManager extends PRAbstractManagerHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** Critére de recherche pour un etat envoyé ou erroné */
    public static final String FOR_ETAT_ENVOYE_OU_ERRONE = "envoyeOuErrone";

    /** Critère de recherche pour lister toutes les annonces APG (rev99 et 05) */
    public static final String FOR_TYPE_APG = "forapg";

    /** Critère de recherche pour lister tous les types d'annonces */
    public static final String FOR_TYPE_TOUS = "tous";

    /**
	 * 
	 */
    private static final long serialVersionUID = 2935810824289799292L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forBusinessProcessId = "";
    private String forDateEnvoi = "";
    private String forEtat = "";
    private String forMoisAnneeComptable = "";
    private String forMoisAnneeComptableDifferentDe = "";
    private String forNss = "";
    private String forType = "";
    private String fromMoisAnneeComptable = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sb = new StringBuilder();
        sb.append(APAnnonceAPG.FIELDNAME_IDANNONCE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_CODEAPPLICATION);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_CODEENREGISTREMENT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROCAISSE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROAGENCE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_GENRE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROCOMPTE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROASSURE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROCONTROLE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_CANTONETAT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ETATCIVIL);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_GENREACTIVITE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_REVENUMOYENDETERMINANT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NOMBREENFANTS);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_PERIODEDE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_PERIODEA);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NOMBREJOURSSERVICE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_TAUXJOURNALIER);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_GARANTIEIJ);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONEXPLOITATION);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONFRAISGARDE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONFRAISGARDE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONEXPLOITATION);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_TOTALAPG);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_MODEPAIEMENT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_NUMEROASSUREPEREENFANT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_DATEDEBUTDROIT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_DATEFINDROIT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ETAT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISRECRUE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONPERSONNESEULE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONMENAGE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONASSISTANCE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_SIGNEMONTANTALLOCATION);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONBASE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_TAUXJOURNALIERALLOCATIONBASE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_SIGNEALLOCATIONFRAISGARDE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_ISALLOCATIONISOLEEFRAISGARDE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_DATEENVOI);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_IDPARENT);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_TYPEANNONCE);
        sb.append(",").append(APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID);
        sb.append(",").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // String joinType = " inner join ";
        String joinType = " left outer join ";
        StringBuilder from = new StringBuilder();
        from.append(" ");
        // from.append(this._getDbSchema()).append(".");
        // from.append(APAnnonceAPG.TABLE_NAME);
        // from.append(" as parent left outer join ");
        from.append(_getDbSchema()).append(".");
        from.append(APAnnonceAPG.TABLE_NAME);
        from.append(" as annonce");
        from.append(joinType);

        from.append(_getDbSchema()).append(".");
        from.append(APPrestation.TABLE_NAME);
        from.append(" on ");
        from.append(_getDbSchema()).append(".");
        from.append(APPrestation.TABLE_NAME).append(".");
        from.append(APPrestation.FIELDNAME_IDANNONCE);
        from.append(" = ");
        from.append(" annonce.");
        from.append(APAnnonceAPG.FIELDNAME_IDANNONCE);
        from.append(joinType);
        from.append(_getDbSchema()).append(".");
        from.append(APDroitLAPG.TABLE_NAME_LAPG);
        from.append(" on ");
        from.append(_getDbSchema()).append(".");
        from.append(APDroitLAPG.TABLE_NAME_LAPG).append(".");
        from.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        from.append(" = ");
        from.append(_getDbSchema()).append(".");
        from.append(APPrestation.TABLE_NAME).append(".");
        from.append(APPrestation.FIELDNAME_IDDROIT);

        return from.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String joinType = " left outer join ";
        StringBuilder where = new StringBuilder();

        where.append(" SELECT ");
        where.append(APAnnonceAPG.FIELDNAME_IDANNONCE);
        where.append(" FROM ");
        where.append(_getDbSchema()).append(".");
        where.append(APAnnonceAPG.TABLE_NAME);
        where.append(joinType);

        where.append(_getDbSchema()).append(".");
        where.append(APPrestation.TABLE_NAME);
        where.append(" as prestation on prestation.");
        where.append(APPrestation.FIELDNAME_IDANNONCE);
        where.append(" = ");
        where.append(_getDbSchema()).append(".");
        where.append(APAnnonceAPG.TABLE_NAME).append(".");
        where.append(APAnnonceAPG.FIELDNAME_IDANNONCE);
        where.append(joinType);
        where.append(_getDbSchema()).append(".");
        where.append(APDroitLAPG.TABLE_NAME_LAPG);
        where.append(" as droit on droit.");
        where.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        where.append(" = prestation.");
        where.append(APPrestation.FIELDNAME_IDDROIT);
        where.append(joinType);
        where.append(_getDbSchema()).append(".");
        where.append(PRDemande.TABLE_NAME);
        where.append(" as demande on demande.");
        where.append(PRDemande.FIELDNAME_IDDEMANDE);
        where.append(" = droit.");
        where.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        where.append(joinType);
        where.append(_getDbSchema()).append(".");
        where.append(IPRTiers.TABLE_AVS);
        where.append(" as tiers on tiers.");
        where.append(IPRTiers.FIELD_TI_IDTIERS);
        where.append(" = demande.");
        where.append(PRDemande.FIELDNAME_IDTIERS);

        String whereCondition1 = getWhere(statement, false);
        String whereCondition2 = getWhere(statement, true);

        StringBuilder whereGlobal = new StringBuilder();
        whereGlobal.append("annonce.");
        whereGlobal.append(APAnnonceAPG.FIELDNAME_IDPARENT);
        whereGlobal.append(" IN (");
        whereGlobal.append(where.toString());
        if (!JadeStringUtil.isEmpty(whereCondition1)) {
            whereGlobal.append(" WHERE ");
            whereGlobal.append(whereCondition1);
        }
        whereGlobal.append(")");

        whereGlobal.append(" OR ");

        whereGlobal.append("annonce.");
        whereGlobal.append(APAnnonceAPG.FIELDNAME_IDANNONCE);
        whereGlobal.append(" IN (");
        whereGlobal.append(where.toString());
        if (!JadeStringUtil.isEmpty(whereCondition1)) {
            whereGlobal.append(" WHERE ");
            whereGlobal.append(whereCondition1);
        }
        whereGlobal.append(")");
        return whereGlobal.toString();
    }

    private String getWhere(BStatement statement, boolean isannonce) {
        String sqlWhere = "";
        String prefix = null;
        if (isannonce) {
            prefix = "annonce.";
        } else {
            prefix = _getDbSchema() + "." + APAnnonceAPG.TABLE_NAME + ".";
        }

        try {
            if (!JAUtil.isDateEmpty(fromMoisAnneeComptable)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += prefix
                        + APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE
                        + ">="
                        + this._dbWriteNumeric(statement.getTransaction(),
                                JACalendar.format(new JADate(fromMoisAnneeComptable), JACalendar.FORMAT_YYYYMM));
            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORECT"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtat)
                || forEtat.equals(APAnnonceAPGHierarchiqueManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (forEtat.equals(APAnnonceAPGHierarchiqueManager.FOR_ETAT_ENVOYE_OU_ERRONE)) {
                sqlWhere += "(" + prefix + APAnnonceAPG.FIELDNAME_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ENVOYE) + " OR " + prefix
                        + APAnnonceAPG.FIELDNAME_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), IAPAnnonce.CS_ERRONE) + ")";
            } else if (forEtat.equals(IAPAnnonce.ETATS_NON_ENVOYE)) {
                sqlWhere += " (" + prefix + APAnnonceAPG.FIELDNAME_ETAT + " <> " + IAPAnnonce.CS_ENVOYE + " or("
                        + prefix + APAnnonceAPG.FIELDNAME_ETAT + " = " + IAPAnnonce.CS_ENVOYE + " and " + prefix
                        + APAnnonceAPG.FIELDNAME_ETAT + " = " + IAPAnnonce.CS_ERRONE + ")) ";
            } else if (forEtat.equals(IAPAnnonce.ETATS_TOUS)) {
                sqlWhere += "";
            } else {
                sqlWhere += prefix + APAnnonceAPG.FIELDNAME_ETAT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forEtat);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forType) && !forType.equals(APAnnonceAPGHierarchiqueManager.FOR_TYPE_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (forType.equals(APAnnonceAPGHierarchiqueManager.FOR_TYPE_APG)) {
                sqlWhere += prefix + APAnnonceAPG.FIELDNAME_GENRE + "<> '90'";
            } else {
                sqlWhere += prefix + APAnnonceAPG.FIELDNAME_GENRE + " = '90'";
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forMoisAnneeComptable)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            try {
                sqlWhere += prefix
                        + APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE
                        + "="
                        + this._dbWriteNumeric(statement.getTransaction(),
                                JACalendar.format(new JADate(forMoisAnneeComptable), JACalendar.FORMAT_YYYYMM));
            } catch (JAException e1) {
                _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
            }
        }

        if (!isannonce && !JadeStringUtil.isEmpty(forNss)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "tiers." + IPRTiers.FIELD_NUMERO_AVS + " LIKE '" + forNss + "%'";
        }

        if (!JadeStringUtil.isEmpty(forBusinessProcessId)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += prefix + APAnnonceAPG.FIELDNAME_BUSINESS_PROCESS_ID + "="
                    + this._dbWriteString(statement.getTransaction(), forBusinessProcessId);
        }

        if (!JAUtil.isDateEmpty(forDateEnvoi)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += prefix + APAnnonceAPG.FIELDNAME_DATEENVOI + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateEnvoi);
        }

        try {
            if (!JAUtil.isDateEmpty(forMoisAnneeComptableDifferentDe)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += prefix
                        + APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE
                        + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), JACalendar.format(new JADate(
                                forMoisAnneeComptableDifferentDe), JACalendar.FORMAT_YYYYMM));

            }
        } catch (JAException e) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMAT_DATEMOISANNEECOMPTABLE_INCORRECT"));
        }

        return sqlWhere;
    }

    @Override
    public String getOrderBy() {
        if (!JadeStringUtil.isEmpty(super.getOrderBy())) {
            if (super.getOrderBy().equals(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE)) {
                return super.getOrderBy() + " DESC";
            } else if (super.getOrderBy().equals(APAnnonceAPG.FIELDNAME_NUMEROASSURE)) {
                return super.getOrderBy() + " DESC";
            } else if (super.getOrderBy().equals(APAnnonceAPG.FIELDNAME_IDANNONCE)) {
                return super.getOrderBy() + " DESC";
            }
        }
        return super.getOrderBy();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APAnnonceAPG();
    }

    /**
     * @return the forBusinessProcessId
     */
    public String getForBusinessProcessId() {
        return forBusinessProcessId;
    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for etat
     * 
     * @return la valeur courante de l'attribut for etat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    /**
     * getter pour l'attribut for mois annee comptable different de
     * 
     * @return la valeur courante de l'attribut for mois annee comptable different de
     */
    public String getForMoisAnneeComptableDifferentDe() {
        return forMoisAnneeComptableDifferentDe;
    }

    /**
     * @return the forNss
     */
    public String getForNss() {
        return forNss;
    }

    /**
     * getter pour l'attribut for type
     * 
     * @return la valeur courante de l'attribut for type
     */
    public String getForType() {
        return forType;
    }

    /**
     * getter pour l'attribut from mois annee comptable
     * 
     * @return la valeur courante de l'attribut from mois annee comptable
     */
    public String getFromMoisAnneeComptable() {
        return fromMoisAnneeComptable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRAbstractManagerHierarchique#getHierarchicalOrderBy ()
     */
    @Override
    public String getHierarchicalOrderBy() {
        String result = "";
        result = " CASE WHEN annonce." + APAnnonceAPG.FIELDNAME_IDPARENT + "=0" + " THEN " + " char(annonce."
                + APAnnonceAPG.FIELDNAME_IDANNONCE + ") " + " ELSE " + " char(annonce."
                + APAnnonceAPG.FIELDNAME_IDPARENT + ")" + " END " + "|| '-' || char(annonce."
                + APAnnonceAPG.FIELDNAME_IDANNONCE + ") ";

        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APAnnonceAPG.FIELDNAME_IDANNONCE;
    }

    /**
     * @param forBusinessProcessId
     *            the forBusinessProcessId to set
     */
    public void setForBusinessProcessId(String forBusinessProcessId) {
        this.forBusinessProcessId = forBusinessProcessId;
    }

    /**
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable different de
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptableDifferentDe(String string) {
        forMoisAnneeComptableDifferentDe = string;
    }

    /**
     * @param forNss
     *            the forNss to set
     */
    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    /**
     * setter pour l'attribut for type
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForType(String string) {
        forType = string;
    }

    /**
     * setter pour l'attribut from mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromMoisAnneeComptable(String string) {
        fromMoisAnneeComptable = string;
    }

}
