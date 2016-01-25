package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;

public class CASectionAvecSectionsPoursuiteManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSection = new String();
    private String forIdSeqCon = new String();
    private String forSelectionSections = new String();
    /**
     * Setter
     */
    private String forTypeEtape = new String();

    private String typeEtapeIn = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAEVCTP INNER JOIN " + _getCollection() + "CAPECTP ON " + _getCollection()
                + "CAEVCTP.IDPARAMETREETAPE = " + _getCollection() + "CAPECTP.IDPARAMETREETAPE INNER JOIN "
                + _getCollection() + CASection.TABLE_CASECTP + " ON " + _getCollection() + "CAEVCTP.IDSECTION = "
                + _getCollection() + "CASECTP.IDSECTION";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement pour un idSection
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CASECTP.IDSECTION = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        // traitement du positionnement pour un typeEtape
        if (getForTypeEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAPECTP.IDPARAMETREETAPE = (SELECT IDPARAMETREETAPE FROM "
                    + _getCollection() + "CAETCTP INNER JOIN " + _getCollection() + "CAPECTP ON " + _getCollection()
                    + "CAETCTP.IDETAPE = " + _getCollection() + "CAPECTP.IDETAPE WHERE TYPEETAPE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForTypeEtape()) + " AND " + _getCollection()
                    + "CAPECTP.IDSEQCON = " + this._dbWriteNumeric(statement.getTransaction(), getForIdSeqCon()) + ")";
        }

        // traitement du positionnement pour un typeEtape
        if (getTypeEtapeIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAPECTP.IDPARAMETREETAPE IN (SELECT IDPARAMETREETAPE FROM "
                    + _getCollection() + "CAETCTP INNER JOIN " + _getCollection() + "CAPECTP ON " + _getCollection()
                    + "CAETCTP.IDETAPE = " + _getCollection() + "CAPECTP.IDETAPE WHERE TYPEETAPE IN ("
                    + getTypeEtapeIn() + "))";
        }
        // Traitement du positionnement pour une sélection des sections
        if ((getForSelectionSections().length() != 0) && !getForSelectionSections().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (JadeStringUtil.toIntMIN(getForSelectionSections())) {
                case 1:
                    sqlWhere += _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " = 0";
                    break;
                case 3:
                    sqlWhere += _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " > 0";
                    break;
                case 4:
                    sqlWhere += _getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + " < 0";
                    break;
                default:
                    break;
            }
        }
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += _getCollection() + "CAEVCTP.DATEEXECUTION <> 0";

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEvenementContentieux();
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSeqCon() {
        return forIdSeqCon;
    }

    /**
     * <H1>Permet d'effectuer une sélection du solde de la section</H1>
     * 
     * <Pre>
     * 1 --&gt; solde &lt;&gt; 0
     * 2 --&gt; solde = 0
     * 3 --&gt; solde &gt; 0
     * 4 --&gt; solde &lt; 0
     * </pre>
     * 
     * @return
     */
    public String getForSelectionSections() {
        return forSelectionSections;
    }

    public String getForTypeEtape() {
        return forTypeEtape;
    }

    public String getTypeEtapeIn() {
        return typeEtapeIn;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSeqCon(String forIdSeqCon) {
        this.forIdSeqCon = forIdSeqCon;
    }

    /**
     * <H1>Permet d'effectuer une sélection du solde de la section</H1>
     * 
     * <Pre>
     * 1 --&gt; solde &lt;&gt; 0
     * 2 --&gt; solde = 0
     * 3 --&gt; solde &gt; 0
     * 4 --&gt; solde &lt; 0
     * </pre>
     * 
     * @param newForSelectionSections
     */
    public void setForSelectionSections(String newForSelectionSections) {
        forSelectionSections = newForSelectionSections;
    }

    public void setForTypeEtape(String forTypeEtape) {
        this.forTypeEtape = forTypeEtape;
    }

    public void setTypeEtapeIn(String typeEtapeIn) {
        this.typeEtapeIn = typeEtapeIn;
    }
}
