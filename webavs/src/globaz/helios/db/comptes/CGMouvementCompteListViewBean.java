package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JAUtil;
import globaz.helios.db.comptes.helper.CGSoldeDataContainer;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Insérez la description du type ici. Date de création : (03.12.2002 11:25:53)
 * 
 * @author: Administrator
 */
public class CGMouvementCompteListViewBean extends CGEcritureListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String TRI_DATE = "DATE";

    public static HashSet getExceptVue(CGExerciceComptable exercice) {
        HashSet liste = new HashSet();

        return liste;
    }

    public static HashSet getExceptVue(CGExerciceComptable exercice, CGCompte compte) {
        HashSet liste = new HashSet();
        if (!CGCompte.CS_MONNAIE_ETRANGERE.equals(compte.getIdNature())) {
            liste.add(CodeSystem.CS_MONNAIE_ETR);
        }
        return liste;
    }

    private java.lang.String forElement = "";
    private java.lang.String forIdPeriodeComptable = "";

    private java.lang.String forListIdPeriodeComptable = "";

    private java.lang.String forMontant = "";

    private java.lang.String forPiece = "";
    private java.lang.String fromDate = "";

    private java.lang.String orderBy = "";
    private java.lang.String reqComptabilite = "";

    private java.lang.String reqPeriodeComptable = "";

    private java.lang.String reqVue = "";

    private java.lang.String soldeANouveau = null;

    /**
     * Commentaire relatif au constructeur CGMouvementCompteListViewBean.
     */
    public CGMouvementCompteListViewBean() {
        super();
        wantCallMethodBeforeFind(true);

    }

    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {
        String idPeriodeComptable = "";

        boolean isSoldeANouveau = false;
        // Si l'on ajoute un filtre dans la recherche (critères de sélection),
        // la notion de solde a nouveau
        // n'a plus de sens
        if ((getBeginWithLibelle() == null || getBeginWithLibelle().trim().length() == 0)
                && (getForMontant() == null || getForMontant().trim().length() == 0)
                && (getForPiece() == null || getForPiece().trim().length() == 0)) {
            isSoldeANouveau = true;
        }

        if (!isSoldeANouveau) {
            setSoldeANouveau(null);
            return;
        }

        if (!JadeStringUtil.isIntegerEmpty(getReqPeriodeComptable()) || !JAUtil.isDateEmpty(getFromDate())) {

            idPeriodeComptable = getReqPeriodeComptable();
            // Calcul du solde a nouveau pour les periodes comptable précédentes
            // !!!
            try {
                boolean isProvisoire = true;

                if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
                    isProvisoire = false;
                }

                FWCurrency soldeANouveau = new FWCurrency(0);
                CGPeriodeComptable periode = new CGPeriodeComptable();
                periode.setSession(getSession());
                periode.setIdPeriodeComptable(idPeriodeComptable);
                periode.retrieve(transaction);

                // Calcul du solde à nouveau jusqu'à la période précédente à
                // celle sélectionnée.
                if (periode != null && !periode.isNew() || !JAUtil.isDateEmpty(getFromDate())) {

                    CGPeriodeComptable periodePrecedente = null;
                    if (periode != null && !periode.isNew()) {
                        periodePrecedente = periode.retrieveLastPeriode(transaction);
                    }

                    if (!JAUtil.isDateEmpty(getFromDate())) {
                        CGSoldeDataContainer soldeDC = CGSolde.computeSoldeCumuleDataAtDate(
                                getForIdExerciceComptable(), periode.getIdPeriodeComptable(), getForIdCompte(),
                                getFromDate(), getForIdCentreCharge(), getSession());

                        if (CodeSystem.CS_MONNAIE_ETR.equals(getReqVue())) {
                            if (isProvisoire) {
                                soldeANouveau = soldeDC.getSoldeProvisoireMonnaie();
                            } else {
                                soldeANouveau = soldeDC.getSoldeMonnaie();
                            }
                        } else {
                            if (isProvisoire) {
                                soldeANouveau = soldeDC.getSoldeProvisoire();
                            } else {
                                soldeANouveau = soldeDC.getSolde();
                            }
                        }
                        setSoldeANouveau(soldeANouveau.toString());
                    } else {
                        if (periodePrecedente == null) {
                            setSoldeANouveau(null);
                        } else {
                            if (CodeSystem.CS_MONNAIE_ETR.equals(getReqVue())) {
                                soldeANouveau = CGSolde.computeSoldeCumuleMonnaie(getForIdExerciceComptable(),
                                        getForIdCompte(), periodePrecedente.getIdPeriodeComptable(),
                                        getForIdCentreCharge(), getSession(), isProvisoire);
                            } else {
                                soldeANouveau = CGSolde.computeSoldeCumule(getForIdExerciceComptable(),
                                        getForIdCompte(), periodePrecedente.getIdPeriodeComptable(),
                                        getForIdCentreCharge(), getSession(), isProvisoire);
                            }
                            setSoldeANouveau(soldeANouveau.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                setSoldeANouveau(null);
            }
        }
        if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
            setForIdPeriodeComptable(idPeriodeComptable);
        } else if (JadeStringUtil.isIntegerEmpty(getForIdPeriodeComptable())) {
            setForIdPeriodeComptable(null);
        }

        if (CodeSystem.CS_PROVISOIRE.equals(getReqComptabilite())) {
            wantForEstProvisoire(null);
        } else {
            wantForEstProvisoire(new Boolean(false));
        }

        if (CodeSystem.CS_CENTRE_CHARGE.equals(getReqVue())) {
            if (!JadeStringUtil.isIntegerEmpty(getForElement())) {
                setForIdCentreCharge(getForElement());
            }
        } else {
            setForPiece(getForElement());
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGJOURP";
        String table2 = _getCollection() + "CGECRIP";
        String table3 = _getCollection() + "CGPERIP";
        String table4 = _getCollection() + "CGEXERP";
        String table5 = _getCollection() + "CGECREP";
        String from = " " + table1;
        from += " INNER JOIN " + table2 + " ON (" + table1 + ".IDJOURNAL=" + table2 + ".IDJOURNAL) ";
        from += " INNER JOIN " + table3 + " ON (" + table1 + ".IDPERIODECOMPTABLE=" + table3 + ".IDPERIODECOMPTABLE ) ";
        from += " INNER JOIN " + table4 + " ON (" + table1 + ".IDEXERCOMPTABLE=" + table4 + ".IDEXERCOMPTABLE ) ";
        from += " INNER JOIN " + table5 + " ON (" + table5 + ".IDENTETEECRITURE=" + table2 + ".IDENTETEECRITURE) ";
        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (orderBy == null || orderBy.trim().length() == 0) {
            return _getCollection() + "CGECREP.DATE, " + _getCollection() + "CGECRIP.IDENTETEECRITURE ";
        } else {
            return orderBy;
        }
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement
        if (getForPiece() != null && getForPiece().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "PIECE="
                    + _dbWriteString(statement.getTransaction(), getForPiece());
        }

        if (getBeginWithLibelle() != null && getBeginWithLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.LIBELLE LIKE '%" + getBeginWithLibelle() + "%'";
        }

        // traitement du positionnement
        // Plus utilisé, car besoin des montants pour le calul du solde à
        // nouveau
        // dans l'écrant des mouvements.
        // La logique d'affichage et déplacée dans la JSP.
        // !!!!!Exception : Dans le cas ou des critères de recherche sont
        // requis,
        // la notions de solde à nouveau n'a plus de sens -> activation
        // de la recherche par date.

        // if(getFromDate()!=null && getFromDate().length() != 0 &&
        // (getForMontant()!=null && getForMontant().length() != 0) ||
        // (getForPiece()!=null && getForPiece().length() != 0) ||
        // (getBeginWithLibelle()!=null &&
        // getBeginWithLibelle().trim().length()!=0)){

        if (getFromDate() != null && getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "DATE>="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }

        if (getForIdPeriodeComptable() != null && getForIdPeriodeComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPERIP.IDPERIODECOMPTABLE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
        }

        if (getReqComptabilite() != null && getReqComptabilite().length() != 0) {
            // Si provisoire, prends tous en comtpe, provisoire + definitif ->
            // on ne traite pas ce cas.
            if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CGECRIP.ESTPROVISOIRE='"
                        + _dbWriteBoolean(statement.getTransaction(), new Boolean(false)) + "' ";
            }
        }

        if (getForMontant() != null && getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ( " + _getCollection() + "CGECRIP.MONTANT="
                    + _dbWriteNumeric(statement.getTransaction(), getForMontant());
            sqlWhere += " OR " + _getCollection() + "CGECRIP.MONTANT="
                    + _dbWriteNumeric(statement.getTransaction(), new BigDecimal(getForMontant()).negate().toString())
                    + " ) ";
        }

        if (getForListIdPeriodeComptable() != null && getForListIdPeriodeComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPERIP.IDPERIODECOMPTABLE IN (" + getForListIdPeriodeComptable() + ") ";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGMouvementCompteViewBean();
    }

    public String getAvoir(int pos) {
        return pos + "";
    }

    public String getCentre(int pos) {
        return pos + "";
    }

    /**
     * Method getContrepartie. Retourne un tableau de String contenant le libelle et l'id externe de la contrepartie.
     * Remplace l'appel successif aux méthodes getContrePartieLibelle & getContrePartieIdExterne
     * 
     * @param pos
     * @return String[]
     */
    public String[] getContrepartie(int pos) {
        String[] result = new String[2];
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        try {
            CGEcritureViewBean contrepartie = entity.retrieveContrepartie();
            if (contrepartie != null) {

                CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
                compte.setSession(getSession());
                compte.setIdCompte(contrepartie.getIdCompte());
                compte.setIdExerciceComptable(contrepartie.getIdExerciceComptable());
                compte.retrieve();
                if (compte.isNew()) {
                    throw (new Exception(getSession().getLabel("AUCUNE")));
                } else {
                    result[0] = compte.getLibelle();
                }
                result[1] = compte.getIdExterne();
            } else {
                result[0] = getSession().getLabel("MULTIPLE");
                result[1] = getSession().getLabel("MULTIPLE");

            }
        } catch (Exception e) {
            result[0] = e.getMessage();
            result[1] = e.getMessage();
        }
        return result;
    }

    public String getContrepartieIdExterne(int pos) {
        String idExterne = "";
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        try {
            CGEcritureViewBean contrepartie = entity.retrieveContrepartie();
            if (contrepartie != null) {

                CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
                compte.setSession(getSession());
                compte.setIdCompte(contrepartie.getIdCompte());
                compte.setIdExerciceComptable(contrepartie.getIdExerciceComptable());
                compte.retrieve();
                if (compte.isNew()) {
                    throw (new Exception(getSession().getLabel("AUCUNE")));
                } else {
                    idExterne = compte.getIdExterne();
                }
            } else {
                idExterne = getSession().getLabel("MULTIPLE");
            }

        } catch (Exception e) {
            idExterne = e.getMessage();
        }
        return idExterne;
    }

    public String getContrepartieLibelle(int pos) {

        String libelle = "";
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        try {
            CGEcritureViewBean contrepartie = entity.retrieveContrepartie();
            if (contrepartie != null) {

                CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
                compte.setSession(getSession());
                compte.setIdCompte(contrepartie.getIdCompte());
                compte.setIdExerciceComptable(contrepartie.getIdExerciceComptable());
                compte.retrieve();
                if (compte.isNew()) {
                    throw (new Exception(getSession().getLabel("AUCUNE")));
                } else {
                    libelle = compte.getLibelle();
                }
            } else {
                libelle = getSession().getLabel("MULTIPLE");
            }

        } catch (Exception e) {
            libelle = e.getMessage();
        }
        return libelle;
    }

    public String getDoit(int pos) {
        return pos + "";
    }

    /**
     * Returns the forElement.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForElement() {
        return forElement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:30:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Returns the forListIdPeriodeComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForListIdPeriodeComptable() {
        return forListIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:48:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMontant() {
        return forMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:25:15)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForPiece() {
        return forPiece;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 19:05:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDate() {
        return fromDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 13:45:14)
     * 
     * @return java.lang.String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 09:49:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqComptabilite() {
        return reqComptabilite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:24:38)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqPeriodeComptable() {
        return reqPeriodeComptable;
    }

    /**
     * Returns the reqVue.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqVue() {
        return reqVue;
    }

    public String getSolde(int pos) {
        return pos + "";
    }

    /**
     * Returns the soldeANouveau.
     * 
     * @return java.lang.String
     */
    public java.lang.String getSoldeANouveau() {
        return soldeANouveau;
    }

    /**
     * Sets the forElement.
     * 
     * @param forElement
     *            The forElement to set
     */
    public void setForElement(java.lang.String forElement) {
        this.forElement = forElement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:30:47)
     * 
     * @param newForIdPeriodeComptable
     *            java.lang.String
     */
    public void setForIdPeriodeComptable(java.lang.String newForIdPeriodeComptable) {
        forIdPeriodeComptable = newForIdPeriodeComptable;
    }

    /**
     * Sets the forListIdPeriodeComptable.
     * 
     * @param forListIdPeriodeComptable
     *            The forListIdPeriodeComptable to set
     */
    public void setForListIdPeriodeComptable(java.lang.String forListIdPeriodeComptable) {
        this.forListIdPeriodeComptable = forListIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:48:18)
     * 
     * @param newForMontant
     *            java.lang.String
     */
    public void setForMontant(java.lang.String newForMontant) {
        forMontant = newForMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:25:15)
     * 
     * @param newForPiece
     *            java.lang.String
     */
    public void setForPiece(java.lang.String newForPiece) {
        forPiece = newForPiece;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 19:05:08)
     * 
     * @param newFromDate
     *            java.lang.String
     */
    public void setFromDate(java.lang.String newFromDate) {
        fromDate = newFromDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 13:45:14)
     * 
     * @param newOrderBy
     *            java.lang.String
     */
    public void setOrderBy(java.lang.String newOrderBy) {
        orderBy = newOrderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 09:49:53)
     * 
     * @param newReqComptabilite
     *            java.lang.String
     */
    public void setReqComptabilite(java.lang.String newReqComptabilite) {
        reqComptabilite = newReqComptabilite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 13:24:38)
     * 
     * @param newReqPeriodeComptable
     *            java.lang.String
     */
    public void setReqPeriodeComptable(java.lang.String newReqPeriodeComptable) {
        reqPeriodeComptable = newReqPeriodeComptable;
    }

    /**
     * Sets the reqVue.
     * 
     * @param reqVue
     *            The reqVue to set
     */
    public void setReqVue(java.lang.String reqVue) {
        this.reqVue = reqVue;
    }

    /**
     * Sets the soldeANouveau.
     * 
     * @param soldeANouveau
     *            The soldeANouveau to set
     */
    public void setSoldeANouveau(java.lang.String soldeANouveau) {
        this.soldeANouveau = soldeANouveau;
    }

}
