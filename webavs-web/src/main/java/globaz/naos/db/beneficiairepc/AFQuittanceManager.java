package globaz.naos.db.beneficiairepc;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Quittance.
 * 
 * @author jpa
 */
public class AFQuittanceManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private Boolean forCiNonTraite = Boolean.FALSE;
    private String forDateValeur = "";

    private String forEtat = "";

    private String forEtatQuittances = "";

    private String forIdJournalQuittance = "";

    private String forIdLocalite = "";

    private String forIdQuittance = "";
    private String forIdTiersAideMenagere = "";
    private String forIdTiersBeneficiaire = "";
    private String forLocalite = "";
    private String forNom = "";
    private String forNomAide = "";

    private String forNombreHeures = "";

    private String ForNumAffilie = "";
    private String forNumAvsAideMenage = "";
    private String forPeriodeDebut = "";
    private String forPeriodeFin = "";
    private String forPrenom = "";
    private String forPrenomAide = "";
    private String forPrixHeure = "";

    private String forTotalVerse = "";
    private String inEtat = "";
    private String likeNumAffilie = "";
    private String likeNumAVS = "";
    private String notInEtat = "";
    private String order;
    private boolean selectionBeneficiairePC = false;

    @Override
    protected String _getFields(BStatement statement) {
        if (isSelectionBeneficiairePC()) {
            return "MAQTIB, MAQANN";
        } else {
            return "*";
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + "AFQUIPC";
        if (!isSelectionBeneficiairePC()) {
            // Jointure sur les journaux de quittance
            fromClause += " LEFT JOIN " + _getCollection() + "AFJRNQU ON " + _getCollection() + "AFQUIPC.MAQIJR="
                    + _getCollection() + "AFJRNQU.MAJQID";
            // Jointure sur l'affiliation
            fromClause += " LEFT JOIN " + _getCollection() + "AFAFFIP ON " + _getCollection() + "AFQUIPC.MAQTIB="
                    + _getCollection() + "AFAFFIP.MAIAFF";
            // Jointure sur les tiers
            fromClause += " LEFT JOIN " + _getCollection() + "TITIERP ON " + _getCollection() + "AFAFFIP.HTITIE="
                    + _getCollection() + "TITIERP.HTITIE";
        } else {
            // fromClause += _getGroupBy(statement);
        }
        return fromClause;
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        return " GROUP BY MAQTIB, MAQANN";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (!JadeStringUtil.isEmpty(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFJRNQU.MAJQET =" + getForEtat();
        }
        if (!JadeStringUtil.isEmpty(getForNomAide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQNAI like \'" + getForNomAide() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getForPrenomAide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQPAI like \'" + getForPrenomAide() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getForNumAvsAideMenage())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQTAM like \'" + getForNumAvsAideMenage() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getForNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TITIERP.HTLDE1 like \'" + getForNom() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getForPrenom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TITIERP.HTLDE2 like \'" + getForPrenom() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getLikeNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF like \'" + getLikeNumAffilie() + "%\'";
        }
        if (!JadeStringUtil.isEmpty(getForIdQuittance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQIDQ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdQuittance());
        }
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQANN="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (!JadeStringUtil.isEmpty(getForIdTiersBeneficiaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQTIB="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiersBeneficiaire());
        }
        if (!JadeStringUtil.isEmpty(getForIdTiersAideMenagere())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQTAM="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiersAideMenagere());
        }
        if (!JadeStringUtil.isEmpty(getForIdJournalQuittance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQIJR="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalQuittance());
        }
        if (!JadeStringUtil.isEmpty(getForPeriodeDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQPDE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForPeriodeDebut());
        }
        if (!JadeStringUtil.isEmpty(getForPeriodeFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQPFI="
                    + this._dbWriteNumeric(statement.getTransaction(), getForPeriodeFin());
        }
        if (!JadeStringUtil.isEmpty(getForNombreHeures())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQNHE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForNombreHeures());
        }
        if (!JadeStringUtil.isEmpty(getForPrixHeure())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQPHE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForPrixHeure());
        }
        if (!JadeStringUtil.isEmpty(getForTotalVerse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQTVE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForTotalVerse());
        }
        if (!JadeStringUtil.isEmpty(getForDateValeur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQDVA="
                    + this._dbWriteNumeric(statement.getTransaction(), getForDateValeur());
        }
        if (!JadeStringUtil.isEmpty(getForIdLocalite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFQUIPC.MAQILO="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdLocalite());
        }
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }

        if (!JadeStringUtil.isEmpty(getForEtatQuittances())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAQTET = " + this._dbWriteString(statement.getTransaction(), getForEtatQuittances());
        }

        // Avec les décisions actives
        if (Boolean.TRUE.equals(getForCiNonTraite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (MABCIT = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR)
                    + " AND MABCIM = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR) + ")";
        }

        if (getInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAQTET in (" + getInEtat() + ")";
        }

        if (getNotInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAQTET not in (" + getNotInEtat() + ")";
        }
        if (isSelectionBeneficiairePC()) {
            sqlWhere += _getGroupBy(statement);
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFQuittance();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public Boolean getForCiNonTraite() {
        return forCiNonTraite;
    }

    public String getForDateValeur() {
        return forDateValeur;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForEtatQuittances() {
        return forEtatQuittances;
    }

    public String getForIdJournalQuittance() {
        return forIdJournalQuittance;
    }

    public String getForIdLocalite() {
        return forIdLocalite;
    }

    public String getForIdQuittance() {
        return forIdQuittance;
    }

    public String getForIdTiersAideMenagere() {
        return forIdTiersAideMenagere;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getForLocalite() {
        return forLocalite;
    }

    public String getForNom() {
        return forNom;
    }

    public String getForNomAide() {
        return forNomAide;
    }

    public String getForNombreHeures() {
        return forNombreHeures;
    }

    public String getForNumAffilie() {
        return ForNumAffilie;
    }

    public String getForNumAvsAideMenage() {
        return forNumAvsAideMenage;
    }

    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    public String getForPrenom() {
        return forPrenom;
    }

    public String getForPrenomAide() {
        return forPrenomAide;
    }

    public String getForPrixHeure() {
        return forPrixHeure;
    }

    public String getForTotalVerse() {
        return forTotalVerse;
    }

    public String getInEtat() {
        return inEtat;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getLikeNumAVS() {
        return likeNumAVS;
    }

    public String getNotInEtat() {
        return notInEtat;
    }

    public String getOrder() {
        return order;
    }

    public boolean isSelectionBeneficiairePC() {
        return selectionBeneficiairePC;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCiNonTraite(Boolean forCiTraite) {
        forCiNonTraite = forCiTraite;
    }

    public void setForDateValeur(String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForEtatQuittances(String forEtatQuittances) {
        this.forEtatQuittances = forEtatQuittances;
    }

    public void setForIdJournalQuittance(String forIdJournalQuittance) {
        this.forIdJournalQuittance = forIdJournalQuittance;
    }

    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

    public void setForIdQuittance(String forIdQuittance) {
        this.forIdQuittance = forIdQuittance;
    }

    public void setForIdTiersAideMenagere(String forIdTiersAideMenagere) {
        this.forIdTiersAideMenagere = forIdTiersAideMenagere;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForLocalite(String forLocalite) {
        this.forLocalite = forLocalite;
    }

    public void setForNom(String forNom) {
        this.forNom = forNom;
    }

    public void setForNomAide(String forNomAide) {
        this.forNomAide = forNomAide;
    }

    public void setForNombreHeures(String forNombreHeures) {
        this.forNombreHeures = forNombreHeures;
    }

    public void setForNumAffilie(String forNumAffilie) {
        ForNumAffilie = forNumAffilie;
    }

    public void setForNumAvsAideMenage(String forNumAvsAideMenage) {
        this.forNumAvsAideMenage = forNumAvsAideMenage;
    }

    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    public void setForPrenom(String forPrenom) {
        this.forPrenom = forPrenom;
    }

    public void setForPrenomAide(String forPrenomAide) {
        this.forPrenomAide = forPrenomAide;
    }

    public void setForPrixHeure(String forPrixHeure) {
        this.forPrixHeure = forPrixHeure;
    }

    public void setForTotalVerse(String forTotalVerse) {
        this.forTotalVerse = forTotalVerse;
    }

    public void setInEtat(String forInEtat) {
        inEtat = forInEtat;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikeNumAVS(String likeNumAVS) {
        this.likeNumAVS = likeNumAVS;
    }

    public void setNotInEtat(String notInEtat) {
        this.notInEtat = notInEtat;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setSelectionBeneficiairePC(boolean selectionBeneficiairePC) {
        this.selectionBeneficiairePC = selectionBeneficiairePC;
    }

}
