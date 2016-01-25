package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.musca.api.IFAPrintManageDoc;
import java.util.ArrayList;

public class FAAfactImpressionManager extends FAAfactManager implements java.io.Serializable, IFAPrintManageDoc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forNumCaisse = new String();
    private boolean isAfacForControleEmpl = false;
    private boolean isForImpression = false;
    private ArrayList<String> numCaisseListe = new ArrayList<String>();

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (isAfacForBulletinsSoldes()) {
            return " IDTIEDEBCOM,IDROLDEBCOM, IDEXTDEBCOM, IDEXTFACCOM, SUM(MONTANTFACTURE) AS MONTANTFACTURE";
        } else {
            return FAAfact.TABLE_FIELDS
                    + ", TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.IDEXTERNEROLE, FAENTFP.IDEXTERNEFACTURE, "
                    + "PMTRADP.LIBELLE AS LIBELLERUB, CARUBRP.IDEXTERNE, FAREMAP.TEXTE, "
                    + "FAORDIP.EHIDOR, FAORDIP.EHNORD, FAORDIP.EHIDCA, FAORDIP.EHLLIF, FAORDIP.EHLLID, "
                    + "FAORDIP.EHLLII ";
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (isAfacForBulletinsSoldes()) {
            return _getCollection() + "FAAFACP AS FAAFACP ";
        }
        if (isAfacForControleEmpl()) {
            // select * from webavsciam.faafacp afa
            // inner join webavsciam.faordip ord on(afa.NUMCAISSE=ord.EHIDCA or
            // ord.EHIDCA=0)
            // inner join webavsciam.faorlip lien
            // on(afa.idrubrique=lien.idrubrique and ord.ehidor=lien.ehidor)
            // where afa.idpassage=632 and identetefacture=152355
            // order by ord.ehnord, afa.idrubrique, afa.anneecotisation
            return _getCollection()
                    + "FAAFACP AS FAAFACP "
                    + "LEFT JOIN "
                    + _getCollection()
                    + "CARUBRP AS CARUBRP ON (CARUBRP.IDRUBRIQUE=FAAFACP.IDRUBRIQUE) "
                    + "LEFT JOIN "
                    + _getCollection()
                    + "FAORDIP AS FAORDIP ON (FAORLIP.EHIDOR = FAORDIP.EHIDOR) "
                    + "LEFT JOIN "
                    + _getCollection()
                    + "FAORLIP AS FAORLIP ON (FAORLIP.IDRUBRIQUE = FAAFACP.IDRUBRIQUE AND FAORLIP.ehidor=FAORDIP.ehidor) "
                    + "INNER JOIN " + _getCollection()
                    + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION= CARUBRP.IDTRADUCTION "
                    + "AND PMTRADP.codeisolangue="
                    + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase()
                    + ") " + "LEFT JOIN " + _getCollection()
                    + "FAREMAP AS FAREMAP ON (FAAFACP.IDREMARQUE=FAREMAP.IDREMARQUE) " + "LEFT JOIN "
                    + _getCollection() + "FAENTFP AS FAENTFP ON (FAAFACP.IDENTETEFACTURE=FAENTFP.IDENTETEFACTURE) "
                    + "LEFT JOIN " + _getCollection() + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE)";
        } else {
            return _getCollection() + "FAAFACP AS FAAFACP " + "LEFT JOIN " + _getCollection()
                    + "CARUBRP AS CARUBRP ON (CARUBRP.IDRUBRIQUE=FAAFACP.IDRUBRIQUE) " + "LEFT JOIN "
                    + _getCollection() + "FAORLIP AS FAORLIP ON (FAORLIP.IDRUBRIQUE = FAAFACP.IDRUBRIQUE) "
                    + "LEFT JOIN " + _getCollection() + "FAORDIP AS FAORDIP ON (FAORLIP.EHIDOR = FAORDIP.EHIDOR) "
                    + "INNER JOIN " + _getCollection()
                    + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION= CARUBRP.IDTRADUCTION "
                    + "AND PMTRADP.codeisolangue="
                    + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase()
                    + ") " + "LEFT JOIN " + _getCollection()
                    + "FAREMAP AS FAREMAP ON (FAAFACP.IDREMARQUE=FAREMAP.IDREMARQUE) " + "LEFT JOIN "
                    + _getCollection() + "FAENTFP AS FAENTFP ON (FAAFACP.IDENTETEFACTURE=FAENTFP.IDENTETEFACTURE) "
                    + "LEFT JOIN " + _getCollection() + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE)";
        }

    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (super._getOrder(statement).length() > 1) {
            return "EHNORD, IDEXTERNE, EHIDCA, " + super._getOrder(statement);
        } else {
            // Tri par défaut
            return "EHNORD, IDEXTERNE, EHIDCA ";
        }
    }

    @Override
    protected String _getSql(BStatement statement) {
        if (isForImpression()) {
            StringBuffer sql = new StringBuffer();
            // Table temporaire
            sql.append("with tmp as ( ");
            sql.append("select FAORLIP.ehidor,ehncai,idrubrique,ehidca from ");
            sql.append(_getCollection() + "FAORLIP AS FAORLIP ");
            sql.append("INNER JOIN " + _getCollection() + "FAORDIP AS FAORDIP ON (FAORLIP.EHIDOR = FAORDIP.EHIDOR)");
            sql.append(")");
            // SELECT
            sql.append("SELECT ");
            sql.append(FAAfact.TABLE_FIELDS
                    + ", TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.IDEXTERNEROLE, FAENTFP.IDEXTERNEFACTURE, "
                    + "PMTRADP.LIBELLE AS LIBELLERUB, CARUBRP.IDEXTERNE, FAREMAP.TEXTE, "
                    + "FAORDIP.EHIDOR, FAORDIP.EHNORD, FAORDIP.EHIDCA, FAORDIP.EHLLIF, FAORDIP.EHLLID, "
                    + "FAORDIP.EHLLII ");
            // FROM et JOINS
            sql.append("FROM " + _getCollection() + "FAAFACP AS FAAFACP ");
            sql.append("LEFT JOIN " + _getCollection()
                    + "CARUBRP AS CARUBRP ON (CARUBRP.IDRUBRIQUE=FAAFACP.IDRUBRIQUE) ");
            sql.append("LEFT JOIN tmp ON (tmp.IDRUBRIQUE = FAAFACP.IDRUBRIQUE AND ((tmp.ehidca = faafacp.numcaisse) or tmp.ehidca = 0 or tmp.ehidca is null))");
            sql.append("LEFT JOIN " + _getCollection() + "FAORDIP AS FAORDIP ON (FAORDIP.EHIDOR = tmp.EHIDOR) ");
            sql.append("INNER JOIN " + _getCollection()
                    + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION= CARUBRP.IDTRADUCTION "
                    + "AND PMTRADP.codeisolangue="
                    + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase()
                    + ") " + "LEFT JOIN " + _getCollection()
                    + "FAREMAP AS FAREMAP ON (FAAFACP.IDREMARQUE=FAREMAP.IDREMARQUE) " + "LEFT JOIN "
                    + _getCollection() + "FAENTFP AS FAENTFP ON (FAAFACP.IDENTETEFACTURE=FAENTFP.IDENTETEFACTURE) "
                    + "LEFT JOIN " + _getCollection() + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE) ");
            // WHERE
            sql.append("WHERE FAAFACP.IDENTETEFACTURE= " + getForIdEnteteFacture() + " ");
            sql.append("AND FAAFACP.AQUITTANCER ="
                    + this._dbWriteBoolean(statement.getTransaction(), getForAQuittancer(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "forAQuittancer") + " ");
            // ORDER
            sql.append(" ORDER BY EHNORD, IDEXTERNE, EHIDCA ");

            return sql.toString();
        } else {
            return super._getSql(statement);
        }

    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // ACU 18.06.2003 OPtimization
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement
        if (getForNumCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAORDIP.EHIDCA=" + this._dbWriteNumeric(statement.getTransaction(), getForNumCaisse())
                    + " OR FAORDIP.EHIDCA IS NULL OR FAORDIP.EHIDCA=0)";
        }

        // traitement du positionnement
        if (getForNumCaisseListe().size() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAORDIP.EHIDCA in (";
            for (int i = 0; i < numCaisseListe.size(); i++) {
                sqlWhere += numCaisseListe.get(i);
                if (!(i + 1 == numCaisseListe.size())) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += ") OR FAORDIP.EHIDCA IS NULL OR FAORDIP.EHIDCA=0)";
        }

        return sqlWhere;
    }

    /**
     * @return
     */
    public java.lang.String getForNumCaisse() {
        return forNumCaisse;
    }

    public ArrayList<String> getForNumCaisseListe() {
        return numCaisseListe;
    }

    public boolean isAfacForControleEmpl() {
        return isAfacForControleEmpl;
    }

    public boolean isForImpression() {
        return isForImpression;
    }

    public void setAfacForControleEmpl(boolean isAfacForControleEmpl) {
        this.isAfacForControleEmpl = isAfacForControleEmpl;
    }

    public void setForImpression(boolean isForImpression) {
        this.isForImpression = isForImpression;
    }

    /**
     * @param string
     */
    public void setForNumCaisse(java.lang.String string) {
        forNumCaisse = string;
    }

    public void setForNumCaisseListe(ArrayList<String> numCaisseListe) {
        this.numCaisseListe = numCaisseListe;
    }

}
