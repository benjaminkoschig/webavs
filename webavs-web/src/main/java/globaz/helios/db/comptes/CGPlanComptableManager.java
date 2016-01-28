package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CGPlanComptableManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String beginWithIdExterne = "";
    private ArrayList beginWithIdExterneListIn = null;
    private String endWithIdExterne = "";
    private Boolean forAffillie = null;
    private boolean forAnnoncesOfas = false;
    private Boolean forEstAReouvrir = null;
    private Boolean forEstPeriode = new Boolean(false);
    private String forIdCentreCharge = "";
    // criteres de recherche

    private String forIdCompte = "";
    private String forIdExerciceComptable = "";
    private String forIdExterne = new String();
    private String forIdExterneLike;
    private String forIdMandat = "";
    private String forIdNature = "";
    private String forIdPeriodeComptable = "";

    private String forIdSecteurAVS = "";

    private String forLibelleDeLike;
    private String forLibelleFrLike;
    private String forLibelleItLike;
    private String forSecteur = "";
    private boolean forSoldeOpen = false;
    private String fromIdExterne = "";
    private String fromLibelleDe = "";
    private String fromLibelleFr = "";
    private String fromLibelleIt = "";
    private String fromSecteur = "";
    private Boolean inclurePeriodesPrec = null;
    private String orderBy = "";
    private String reqComptabilite = "";
    private String reqCritere = "";
    private String reqDomaine = "";

    private String reqDomaineNot = "";

    private String reqForListPeriodesComptable = "";

    private String reqGenreCompte = "";
    private String reqLibelle = "";
    private String reqMontant = "";

    private String reqPeriodeComptable = "";
    private String untilSecteur = "";

    /**
     * Commentaire relatif au constructeur CGPlanComptableManager.
     */
    public CGPlanComptableManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Critères de recherche
     * 
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

        // Gestion des criteres de recherche
        if (!JadeStringUtil.isBlank(getReqMontant())) {
            if (CGEcritureViewBean.CS_MONTANT_MONNAIE_ETRANGERE.equals(getReqMontant())) {
                setForIdNature(CGCompte.CS_MONNAIE_ETRANGERE);
            } else {
                setForIdNature(null);
            }
        }

        // libelle selon critere
        if (!JadeStringUtil.isBlank(getReqCritere())) {
            if (CodeSystem.CS_TRI_LIBELLE.equals(getReqCritere())) {
                // libelle
                String langue = getSession().getUserInfo().getLanguage();
                if (langue.equalsIgnoreCase("IT")) {
                    setFromLibelleIt(getReqLibelle());
                    setOrderBy(CGPlanComptableViewBean.FIELD_LIBELLE_IT);
                } else if (langue.equalsIgnoreCase("DE")) {
                    setFromLibelleDe(getReqLibelle());
                    setOrderBy(CGPlanComptableViewBean.FIELD_LIBELLE_DE);
                } else {
                    setFromLibelleFr(getReqLibelle());
                    setOrderBy(CGPlanComptableViewBean.FIELD_LIBELLE_FR);
                }
            } else if (CodeSystem.CS_TRI_NUMERO_COMPTE.equals(getReqCritere())) {
                // numero
                setFromIdExterne(getReqLibelle());
                setOrderBy(CGPlanComptableViewBean.FIELD_IDEXTERNE);
            }
        }

        // comptablite : rien, standard

        // genre de compte :
        if (!JadeStringUtil.isBlank(getReqGenreCompte())) {
            if (CGCompte.CS_GENRE_TOUS.equals(getReqGenreCompte())) {
                setReqGenreCompte("");
            }
        }

        // domaine :
        if (!JadeStringUtil.isBlank(getReqDomaine())) {
            if (CGCompte.CS_COMPTE_TOUS.equals(getReqDomaine())) {
                setReqDomaine("");
            }

        }

        if (!JadeStringUtil.isBlank(getReqPeriodeComptable())) {

            if ("0".equals(getReqPeriodeComptable()) || getReqPeriodeComptable() == null
                    || getReqPeriodeComptable().trim().length() == 0) {
                // si on a "0" ,c'est que l'on veut avoir le solde de l'exerice
                setForEstPeriode(new Boolean(false));
            } else {
                // sinon on veut avoir le solde de la periode
                setForEstPeriode(new Boolean(true));
            }
            setForIdPeriodeComptable(getReqPeriodeComptable());
        } else if (!JadeStringUtil.isBlank(getReqForListPeriodesComptable())) {
            String listIdPeriodesComptable = "";
            try {
                if ("0".equals(getReqForListPeriodesComptable())) {
                    // si on a "0" ,c'est que l'on veut avoir le solde de
                    // l'exerice
                    setForEstPeriode(new Boolean(false));
                } else {
                    // sinon on veut avoir le solde de la periode (et des
                    // périodes précédentes)
                    setForEstPeriode(new Boolean(true));

                    // Récupération de la période courante
                    CGPeriodeComptable periode = new CGPeriodeComptable();
                    periode.setSession(getSession());
                    periode.setIdPeriodeComptable(getReqForListPeriodesComptable());
                    periode.retrieve(transaction);
                    if (periode != null && !periode.isNew()) {

                        if (isInclurePeriodesPrec() != null && isInclurePeriodesPrec().booleanValue()) {
                            listIdPeriodesComptable += periode.getIdPeriodeComptable() + ",";

                            List idPeriodesComptables = CGSolde.getListIdPreviousPeriodeComptables(getSession(),
                                    periode.getIdExerciceComptable(), periode, null);
                            Iterator iter = idPeriodesComptables.iterator();
                            while (iter.hasNext()) {
                                String element = (String) iter.next();
                                listIdPeriodesComptable += element + ",";
                            }

                            listIdPeriodesComptable = listIdPeriodesComptable.substring(0,
                                    listIdPeriodesComptable.length() - 1);
                        }
                        // on inclut pas les periodes précédentes
                        else {
                            listIdPeriodesComptable += periode.getIdPeriodeComptable();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                setReqForListPeriodesComptable(null);
            }
            setReqForListPeriodesComptable(listIdPeriodesComptable);
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGPLANP";
        String table2 = _getCollection() + "CGCOMTP";
        String table3 = _getCollection() + "CGSOLDP";
        String from = " " + table1;
        from += " INNER JOIN " + table2 + " ON (" + table1 + ".IDCOMPTE=" + table2 + ".IDCOMPTE)";
        from += " LEFT OUTER JOIN " + table3 + " ON (" + table1 + ".IDEXERCOMPTABLE = " + table3
                + ".IDEXERCOMPTABLE and " + table3 + ".ESTPERIODE="
                + _dbWriteBoolean(statement.getTransaction(), getForEstPeriode(), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + " AND " + table1 + ".IDCOMPTE=" + table3 + ".IDCOMPTE) ";
        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrderBy();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdExterne() != null && getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE=" + _dbWriteString(statement.getTransaction(), getForIdExterne());
        }
        if (getForIdMandat() != null && getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCOMTP.IDMANDAT="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (getForIdCompte() != null && getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.IDCOMPTE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        if (getForIdSecteurAVS() != null && getForIdSecteurAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS=" + _dbWriteNumeric(statement.getTransaction(), getForIdSecteurAVS());
        }

        // traitement du positionnement
        if (getFromIdExterne() != null && getFromIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE>=" + _dbWriteString(statement.getTransaction(), getFromIdExterne());
        }

        // traitement du positionnement
        if (getForIdExerciceComptable() != null && getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.IDEXERCOMPTABLE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        // traitement du positionnement
        if (getForEstAReouvrir() != null && getForEstAReouvrir().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection()
                    + "CGPLANP.AREOUVRIR="
                    + _dbWriteBoolean(statement.getTransaction(), getForEstAReouvrir(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (getFromLibelleFr() != null && getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEFR>=" + _dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        // traitement du positionnement
        if (getFromLibelleDe() != null && getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEDE>=" + _dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        // traitement du positionnement
        if (getFromLibelleIt() != null && getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEIT>=" + _dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        if (!JadeStringUtil.isBlank(getForLibelleFrLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEFR like "
                    + _dbWriteString(statement.getTransaction(), "%" + getForLibelleFrLike() + "%");
        }

        if (!JadeStringUtil.isBlank(getForLibelleDeLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEDE like "
                    + _dbWriteString(statement.getTransaction(), "%" + getForLibelleDeLike() + "%");
        }

        if (!JadeStringUtil.isBlank(getForLibelleItLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEIT like "
                    + _dbWriteString(statement.getTransaction(), "%" + getForLibelleItLike() + "%");
        }

        // traitement du positionnement pour ReqGenreCompte
        if (getReqGenreCompte() != null && getReqGenreCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDGENRE=" + _dbWriteNumeric(statement.getTransaction(), getReqGenreCompte());
        }

        // traitement du positionnement pour ReqDomaine
        if (getReqDomaine() != null && getReqDomaine().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDDOMAINE=" + _dbWriteNumeric(statement.getTransaction(), getReqDomaine());
        }

        if (!JadeStringUtil.isIntegerEmpty(getReqDomaineNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDDOMAINE <> " + _dbWriteNumeric(statement.getTransaction(), getReqDomaineNot());
        }

        // traitement du positionnement pour PeriodeComptable
        if (getForIdPeriodeComptable() != null && getForIdPeriodeComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPERIODECOMPTABLE=" + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
        }

        // traitement du positionnement pour PeriodeComptable et periode
        // comptable précédente
        if (getReqForListPeriodesComptable() != null && getReqForListPeriodesComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPERIODECOMPTABLE IN (" + getReqForListPeriodesComptable() + ")";
        }

        // traitement du positionnement pour IdExterne
        if (getBeginWithIdExterne() != null && getBeginWithIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE LIKE '" + getBeginWithIdExterne() + "%'";
        }

        if (beginWithIdExterneListIn != null) {
            String tmp = new String();

            Iterator iter = getBeginWithIdExterneListIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += " OR ";
                }

                tmp += "IDEXTERNE LIKE '" + element + "%'";
            }

            if (!JadeStringUtil.isBlank(tmp)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "(" + tmp + ")";
            }

        }

        if (!JadeStringUtil.isBlank(getForIdExterneLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE LIKE '" + getForIdExterneLike() + "'";
        }

        // traitement du positionnement pour IdExterne
        if (getEndWithIdExterne() != null && getEndWithIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SUBSTR(IDEXTERNE,14,1)="
                    + _dbWriteString(statement.getTransaction(), getEndWithIdExterne().substring(0, 1));
        }

        if (getFromSecteur() != null && getFromSecteur().length() != 0 && getUntilSecteur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CAST (SUBSTR(IDEXTERNE,1,4) AS INTEGER) BETWEEN "
                    + _dbWriteNumeric(statement.getTransaction(), getFromSecteur()) + " AND "
                    + _dbWriteNumeric(statement.getTransaction(), getUntilSecteur());
        } else {
            if (getFromSecteur() != null && getFromSecteur().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "CAST (SUBSTR(IDEXTERNE,1,4) AS INTEGER) >= "
                        + _dbWriteNumeric(statement.getTransaction(), getFromSecteur());
            }
            if (getUntilSecteur() != null && getUntilSecteur().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "CAST (SUBSTR(IDEXTERNE,1,4) AS INTEGER) <= "
                        + _dbWriteNumeric(statement.getTransaction(), getUntilSecteur());
            }
        }

        if (getForSecteur() != null && getForSecteur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CAST (SUBSTR(IDEXTERNE,1,4) AS INTEGER) = "
                    + _dbWriteNumeric(statement.getTransaction(), getForSecteur());
        }

        if (getForAffillie() != null && getForAffillie().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SUBSTR(IDEXTERNE,6,3) = '110'";
        }

        // traitement du positionnement
        if (getForIdNature() != null && getForIdNature().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDNATURE=" + _dbWriteNumeric(statement.getTransaction(), getForIdNature());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreCharge())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGSOLDP.IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCentreCharge());
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( " + _getCollection() + "CGSOLDP.IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), "0") + " OR " + _getCollection()
                    + "CGSOLDP.IDCENTRECHARGE is null ) ";
        }

        /*
         * //traitement du positionnement pour ReqComptabilite if(getReqComptabilite.length() != 0) { if
         * (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere += "?=" + _dbWrite?(statement.getTransaction(),
         * getReqComptabilite()); }
         */

        if (isForSoldeOpen()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ( " + _getCollection() + "CGSOLDP.SOLDE <> "
                    + _dbWriteNumeric(statement.getTransaction(), "0") + " OR " + _getCollection()
                    + "CGSOLDP.SOLDEMONNAIE <> " + _dbWriteNumeric(statement.getTransaction(), "0");
            sqlWhere += " OR " + _getCollection() + "CGSOLDP.SOLDEPROVISOIRE <> "
                    + _dbWriteNumeric(statement.getTransaction(), "0") + " OR " + _getCollection()
                    + "CGSOLDP.SOLDEPROVIMONNAIE <> " + _dbWriteNumeric(statement.getTransaction(), "0") + " )";
        }

        if (isForAnnoncesOfas()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (SUBSTR(IDEXTERNE,1,1) IN ('9'))";
            sqlWhere += " AND (IDDOMAINE = " + CGCompteOfas.CS_DOMAINE_BILAN + " OR " + "IDDOMAINE = "
                    + CGCompteOfas.CS_DOMAINE_ADMINISTRATION + ") ";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGPlanComptableViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 13:56:53)
     * 
     * @return String
     */
    public String getBeginWithIdExterne() {
        return beginWithIdExterne;
    }

    public ArrayList getBeginWithIdExterneListIn() {
        return beginWithIdExterneListIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 11:39:41)
     * 
     * @return String
     */
    public String getEndWithIdExterne() {
        return endWithIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 13:20:39)
     * 
     * @return Boolean
     */
    public Boolean getForAffillie() {
        return forAffillie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 10:45:22)
     * 
     * @return Boolean
     */
    public Boolean getForEstAReouvrir() {
        return forEstAReouvrir;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 16:49:30)
     * 
     * @return Boolean
     */
    public Boolean getForEstPeriode() {
        return forEstPeriode;
    }

    /**
     * Returns the forIdCentreCharge.
     * 
     * @return String
     */
    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:08:06)
     * 
     * @return String
     */
    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 11:22:55)
     * 
     * @return String
     */
    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Getter
     */
    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdExterneLike() {
        return forIdExterneLike;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 14:36:33)
     * 
     * @return String
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdNature.
     * 
     * @return String
     */
    public String getForIdNature() {
        return forIdNature;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:06:43)
     * 
     * @return String
     */
    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 12:09:14)
     * 
     * @return String
     */
    public String getForIdSecteurAVS() {
        return forIdSecteurAVS;
    }

    public String getForLibelleDeLike() {
        return forLibelleDeLike;
    }

    public String getForLibelleFrLike() {
        return forLibelleFrLike;
    }

    public String getForLibelleItLike() {
        return forLibelleItLike;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2003 10:06:05)
     * 
     * @return String
     */
    public String getForSecteur() {
        return forSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:55:32)
     * 
     * @return String
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:37)
     * 
     * @return String
     */
    public String getFromLibelleDe() {
        return fromLibelleDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:22)
     * 
     * @return String
     */
    public String getFromLibelleFr() {
        return fromLibelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:55)
     * 
     * @return String
     */
    public String getFromLibelleIt() {
        return fromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:25:59)
     * 
     * @return String
     */
    public String getFromSecteur() {
        return fromSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:54:28)
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    public String getReqComptabilite() {
        return reqComptabilite;
    }

    public String getReqCritere() {
        return reqCritere;
    }

    public String getReqDomaine() {
        return reqDomaine;
    }

    /**
     * @return
     */
    public String getReqDomaineNot() {
        return reqDomaineNot;
    }

    /**
     * Returns the reqForListPeriodesComptable.
     * 
     * @return String
     */
    public String getReqForListPeriodesComptable() {
        return reqForListPeriodesComptable;
    }

    public String getReqGenreCompte() {
        return reqGenreCompte;
    }

    public String getReqLibelle() {
        return reqLibelle;
    }

    /**
     * Returns the reqMontant.
     * 
     * @return String
     */
    public String getReqMontant() {
        return reqMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:04:47)
     * 
     * @return String
     */
    public String getReqPeriodeComptable() {
        return reqPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:26:33)
     * 
     * @return String
     */
    public String getUntilSecteur() {
        return untilSecteur;
    }

    public boolean isForAnnoncesOfas() {
        return forAnnoncesOfas;
    }

    /**
     * @return
     */
    public boolean isForSoldeOpen() {
        return forSoldeOpen;
    }

    /**
     * Returns the inclurePeriodesPrec.
     * 
     * @return Boolean
     */
    public Boolean isInclurePeriodesPrec() {
        return inclurePeriodesPrec;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2003 13:56:53)
     * 
     * @param newBeginWithIdExterne
     *            String
     */
    public void setBeginWithIdExterne(String newBeginWithIdExterne) {
        beginWithIdExterne = newBeginWithIdExterne;
    }

    public void setBeginWithIdExterneListIn(ArrayList beginWithIdExterneList) {
        beginWithIdExterneListIn = beginWithIdExterneList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 11:39:41)
     * 
     * @param newEndWithIdExterne
     *            String
     */
    public void setEndWithIdExterne(String newEndWithIdExterne) {
        endWithIdExterne = newEndWithIdExterne;
    }

    public void setForAnnoncesOfas(boolean forAnnoncesOfas) {
        this.forAnnoncesOfas = forAnnoncesOfas;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 10:45:22)
     * 
     * @param newForEstAReouvrir
     *            Boolean
     */
    public void setForEstAReouvrir(Boolean newForEstAReouvrir) {
        forEstAReouvrir = newForEstAReouvrir;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 16:49:30)
     * 
     * @param newForEstPeriode
     *            Boolean
     */
    public void setForEstPeriode(Boolean newForEstPeriode) {
        forEstPeriode = newForEstPeriode;
    }

    /**
     * Sets the forIdCentreCharge.
     * 
     * @param forIdCentreCharge
     *            The forIdCentreCharge to set
     */
    public void setForIdCentreCharge(String forIdCentreCharge) {
        this.forIdCentreCharge = forIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:08:06)
     * 
     * @param newForIdCompte
     *            String
     */
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 11:22:55)
     * 
     * @param newForIdExerciceComptable
     *            String
     */
    public void setForIdExerciceComptable(String newForIdExerciceComptable) {
        forIdExerciceComptable = newForIdExerciceComptable;
    }

    /**
     * Setter
     */
    public void setForIdExterne(String newForIdExterne) {
        forIdExterne = newForIdExterne;
    }

    public void setForIdExterneLike(String forIdExterneLike) {
        this.forIdExterneLike = forIdExterneLike;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 14:36:33)
     * 
     * @param newForIdMandat
     *            String
     */
    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Sets the forIdNature.
     * 
     * @param forIdNature
     *            The forIdNature to set
     */
    public void setForIdNature(String forIdNature) {
        this.forIdNature = forIdNature;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:06:43)
     * 
     * @param newForIdPeriodeComptable
     *            String
     */
    public void setForIdPeriodeComptable(String newForIdPeriodeComptable) {
        forIdPeriodeComptable = newForIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 12:09:14)
     * 
     * @param newForIdSecteurAVS
     *            String
     */
    public void setForIdSecteurAVS(String newForIdSecteurAVS) {
        forIdSecteurAVS = newForIdSecteurAVS;
    }

    public void setForLibelleDeLike(String forLibelleDeLike) {
        this.forLibelleDeLike = forLibelleDeLike;
    }

    public void setForLibelleFrLike(String forLibelleFrLike) {
        this.forLibelleFrLike = forLibelleFrLike;
    }

    public void setForLibelleItLike(String forLibelleItLike) {
        this.forLibelleItLike = forLibelleItLike;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2003 10:06:05)
     * 
     * @param newForSecteur
     *            String
     */
    public void setForSecteur(String newForSecteur) {
        forSecteur = newForSecteur;
    }

    /**
     * @param b
     */
    public void setForSoldeOpen(boolean b) {
        forSoldeOpen = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:55:32)
     * 
     * @param newFromIdExterne
     *            String
     */
    public void setFromIdExterne(String newFromIdExterne) {
        fromIdExterne = newFromIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:37)
     * 
     * @param newFromLibelleDe
     *            String
     */
    public void setFromLibelleDe(String newFromLibelleDe) {
        fromLibelleDe = newFromLibelleDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:22)
     * 
     * @param newFromLibelleFr
     *            String
     */
    public void setFromLibelleFr(String newFromLibelleFr) {
        fromLibelleFr = newFromLibelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:53:55)
     * 
     * @param newFromLibelleIt
     *            String
     */
    public void setFromLibelleIt(String newFromLibelleIt) {
        fromLibelleIt = newFromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:25:59)
     * 
     * @param newFromSecteur
     *            String
     */
    public void setFromSecteur(String newFromSecteur) {
        fromSecteur = newFromSecteur;
    }

    /**
     * Sets the inclurePeriodesPrec.
     * 
     * @param inclurePeriodesPrec
     *            The inclurePeriodesPrec to set
     */
    public void setInclurePeriodesPrec(Boolean inclurePeriodesPrec) {
        this.inclurePeriodesPrec = inclurePeriodesPrec;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 11:54:28)
     * 
     * @param newOrderBy
     *            String
     */
    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    public String setReqComptabilite(String newValue) {
        return reqComptabilite = newValue;
    }

    public String setReqCritere(String newValue) {
        return reqCritere = newValue;
    }

    public String setReqDomaine(String newValue) {
        return reqDomaine = newValue;
    }

    /**
     * @param string
     */
    public void setReqDomaineNot(String string) {
        reqDomaineNot = string;
    }

    /**
     * Sets the reqForListPeriodesComptable.
     * 
     * @param reqForListPeriodesComptable
     *            The reqForListPeriodesComptable to set
     */
    public void setReqForListPeriodesComptable(String reqListPeriodesComptable) {
        reqForListPeriodesComptable = reqListPeriodesComptable;
    }

    public String setReqGenreCompte(String newValue) {
        return reqGenreCompte = newValue;
    }

    public String setReqLibelle(String newValue) {
        return reqLibelle = newValue;
    }

    /**
     * Sets the reqMontant.
     * 
     * @param reqMontant
     *            The reqMontant to set
     */
    public void setReqMontant(String reqMontant) {
        this.reqMontant = reqMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:04:47)
     * 
     * @param newReqPeriodeComptable
     *            String
     */
    public void setReqPeriodeComptable(String newReqPeriodeComptable) {
        reqPeriodeComptable = newReqPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:26:33)
     * 
     * @param newUntilSecteur
     *            String
     */
    public void setUntilSecteur(String newUntilSecteur) {
        untilSecteur = newUntilSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 13:20:39)
     * 
     * @param newForAffillie
     *            Boolean
     */
    public void wantForAffillie(Boolean newForAffillie) {
        forAffillie = newForAffillie;
    }

}
