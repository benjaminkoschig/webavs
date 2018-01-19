package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.util.AFIDEUtil;
import java.io.Serializable;
import java.util.List;

public class AFIdeAnnonceManager extends BManager implements Serializable {

    private static final long serialVersionUID = -6424923065533092073L;

    private static final String CREATE_CONDITION_NUMERIC = "CREATE_CONDITION_NUMERIC";
    private static final String CREATE_CONDITION_DATE = "CREATE_CONDITION_DATE";
    private static final String CREATE_CONDITION_STRING = "CREATE_CONDITION_STRING";

    private static final String ALIAS_TABLE_AFFILIATION = "AFF.";
    private static final String ALIAS_TABLE_ANNONCE = "ANN.";
    private static final String ALIAS_TABLE_COTISATION = "COT.";

    private static final String ALIAS_TABLE_AFFILIATION_WITHOUT_POINT = "AFF";
    private static final String ALIAS_TABLE_ANNONCE_WITHOUT_POINT = "ANN";
    private static final String ALIAS_TABLE_COTISATION_WITHOUT_POINT = "COT";

    private boolean wantAnnoncePassive = true;
    private String likeNumeroIde;
    private String likeNumeroAffilie;
    private String likeIdAffiliationLiee;
    private String likeRaisonSociale;
    private String forCategorie;
    private String forNumeroAffilie;
    private String forType;
    private String forIdAffiliation;
    private String forIdTiers;
    private String forEtat;
    private String forStatut;
    private String forDesenregActif;
    private List<String> notInStatutIDEAffiliation;

    private List<String> inIdAnnonce;

    private List<String> inEtat;

    private List<String> inCategorie;

    private List<String> notInCategorie;

    private List<String> inType;

    private List<String> notInType;

    private String fromDateCreation;
    private String untilDateCreation;

    private String fromDateTraitement;
    private String untilDateTraitement;

    private String forNumeroIde;

    public String getForNumeroIde() {
        return forNumeroIde;
    }

    public String getForDesenregActif() {
        return forDesenregActif;
    }

    public String getLikeIdAffiliationLiee() {
        return likeIdAffiliationLiee;
    }

    public void setLikeIdAffiliationLiee(String likeIdAffiliationLiee) {
        this.likeIdAffiliationLiee = likeIdAffiliationLiee;
    }

    public void setForNumeroIde(String forNumeroIde) {

        if (!JadeStringUtil.isBlankOrZero(forNumeroIde)) {
            forNumeroIde = forNumeroIde.replaceAll("[^\\d]", "");
            forNumeroIde = "CHE" + forNumeroIde;
            this.forNumeroIde = forNumeroIde;
        }

    }

    public void setForDesenregActif(String forDesenregActif) {

        if (!JadeStringUtil.isBlankOrZero(forDesenregActif)) {
            forDesenregActif = forDesenregActif.replaceAll("[^\\d]", "");
            forDesenregActif = "CHE" + forDesenregActif;
            this.forDesenregActif = forDesenregActif;
        }

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFIdeAnnonce.IDE_ANNONCE_TABLE_NAME + " AS "
                + ALIAS_TABLE_ANNONCE_WITHOUT_POINT);
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection() + AFAffiliation.TABLE_NAME + " AS " + ALIAS_TABLE_AFFILIATION_WITHOUT_POINT);
        sqlFrom.append(" ON(" + ALIAS_TABLE_AFFILIATION + AFAffiliation.FIELDNAME_AFFILIATION_ID + " = "
                + ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION + ")");
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection() + AFCotisation.TABLE_NAME + " AS " + ALIAS_TABLE_COTISATION_WITHOUT_POINT);
        sqlFrom.append(" ON(" + ALIAS_TABLE_COTISATION + AFCotisation.FIELDNAME_COTISATION_ID + " = "
                + ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_COTISATION + ")");
        return sqlFrom.toString();
    }

    /**
     * Renvoie la composante de tri de la requête SQL (sans le mot-clé ORDER BY).
     * 
     * @return la composante ORDER BY
     */
    @Override
    protected java.lang.String _getOrder(BStatement statement) {
        return AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_ANNONCE + " DESC";
    }

    public boolean isWantAnnoncePassive() {
        return wantAnnoncePassive;
    }

    public void setWantAnnoncePassive(boolean wantAnnoncePassive) {
        this.wantAnnoncePassive = wantAnnoncePassive;
    }

    @Override
    protected String _getSqlDelete(BStatement statement) throws Exception {
        StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
        sqlBuffer.append(_getDeleteFrom(statement));
        String sqlWhere = _getDeleteWhere(statement);
        if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
            sqlBuffer.append(" WHERE ");
            sqlBuffer.append(sqlWhere);
            return sqlBuffer.toString();
        } else {
            throw new Exception("Not allowed to empty all the contents of the table: " + _getFrom(statement));
        }
    };

    /**
     * FROM propre au delete (évite le join)
     * 
     * @param statement
     * @return
     */
    private String _getDeleteFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFIdeAnnonce.IDE_ANNONCE_TABLE_NAME);
        sqlFrom.append(" AS " + ALIAS_TABLE_ANNONCE_WITHOUT_POINT);

        return sqlFrom.toString();
    }

    /**
     * si l'on doit deleter une annonce en utilisant un critère numéroIDE de l'affiliation en WHERE
     * 
     * @return
     */
    private boolean isDeleteOnJoinAff() {
        return !JadeStringUtil.isBlankOrZero(forNumeroIde);
    }

    /**
     * pour deleter les cas particulier de désenregistement actif lié a une annonce dont on a supprimé le numéro IDE
     * 
     * @return
     */
    private boolean isDeleteDesenregActif() {
        return !JadeStringUtil.isBlankOrZero(forDesenregActif);
    }

    /**
     * WHERE propre au delete (évite le join)
     * 
     * @param statement
     * @return
     */
    private String _getDeleteWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        BTransaction theTransaction = statement.getTransaction();
        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION, "=", forIdAffiliation);
        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE, "=", forType);
        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ETAT, "IN",
                transformListEnInValues(getInEtat(), CREATE_CONDITION_NUMERIC));
        if (isDeleteDesenregActif()) {
            createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_ANNONCE
                    + AFIdeAnnonce.IDE_ANNONCE_FIELD_NUMERO_IDE_REMPLACEMENT, "=", getForDesenregActif());
        } else if (isDeleteOnJoinAff()) {

            createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_ANNONCE
                    + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION, "IN",
                    ("( SELECT AFF." + AFAffiliation.FIELDNAME_AFFILIATION_ID + " FROM " + _getCollection()
                            + AFAffiliation.TABLE_NAME + " AS " + ALIAS_TABLE_AFFILIATION_WITHOUT_POINT
                            + " WHERE AFF.MALFED =" + _dbWriteString(theTransaction, getForNumeroIde()) + " ) "));
        }
        return sqlWhere.toString();
    }

    private void createCondition(BTransaction transaction, String typeCondition, StringBuffer sqlCondition,
            String column, String operation, String valeur) {

        if (!JadeStringUtil.isBlankOrZero(valeur)) {
            if (sqlCondition.length() != 0) {
                sqlCondition.append(" AND ");
            }

            String theValeur = valeur;

            if (!operation.contains("IN")) {
                if (CREATE_CONDITION_STRING.equalsIgnoreCase(typeCondition)) {

                    if ("LIKE".equalsIgnoreCase(operation) || "LIKE_WITHOUT_CASSE".equalsIgnoreCase(operation)) {
                        valeur = valeur + "%";
                    }

                    theValeur = _dbWriteString(transaction, valeur);
                    if ("LIKE_WITHOUT_CASSE".equalsIgnoreCase(operation)) {
                        operation = "LIKE";
                        theValeur = "UPPER(" + theValeur + ")";
                        column = "UPPER(" + column + ")";
                    }
                } else if (CREATE_CONDITION_NUMERIC.equalsIgnoreCase(typeCondition)) {
                    theValeur = _dbWriteNumeric(transaction, valeur);
                } else if (CREATE_CONDITION_DATE.equalsIgnoreCase(typeCondition)) {
                    theValeur = _dbWriteDateAMJ(transaction, valeur);
                }
            }

            sqlCondition.append(column + " " + operation + " " + theValeur);

        }

    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public List<String> getInEtat() {
        return inEtat;
    }

    public String getLikeNumeroIde() {
        return likeNumeroIde;
    }

    public void setLikeNumeroIde(String likeNumeroIde) {
        this.likeNumeroIde = likeNumeroIde;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public List<String> getNotInCategorie() {
        return notInCategorie;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public List<String> getNotInStatutIDEAffiliation() {
        return notInStatutIDEAffiliation;
    }

    public List<String> getInType() {
        return inType;
    }

    public List<String> getNotInType() {
        return notInType;
    }

    public void setInType(List<String> inType) {
        this.inType = inType;
    }

    public void setNotInType(List<String> notInType) {
        this.notInType = notInType;
    }

    public void setNotInStatutIDEAffiliation(List<String> notInStatutIDEAffiliation) {
        this.notInStatutIDEAffiliation = notInStatutIDEAffiliation;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    public String getLikeRaisonSociale() {
        return likeRaisonSociale;
    }

    public void setLikeRaisonSociale(String likeRaisonSociale) {
        this.likeRaisonSociale = likeRaisonSociale;
    }

    public List<String> getInCategorie() {
        return inCategorie;
    }

    public void setInCategorie(List<String> inCategorie) {
        this.inCategorie = inCategorie;
    }

    public void setNotInCategorie(List<String> notInCategorie) {
        this.notInCategorie = notInCategorie;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        BTransaction theTransaction = statement.getTransaction();

        if (!isWantAnnoncePassive()) {
            setNotInType(AFIDEUtil.getListTypeAnnoncePassive());
        }

        if (!JadeStringUtil.isBlankOrZero(likeNumeroIde)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            String likeNumeroIdeMod = likeNumeroIde.replaceAll("[^\\d]", "");
            likeNumeroIdeMod = "CHE" + likeNumeroIdeMod;

            likeNumeroIdeMod = likeNumeroIdeMod + "%";
            likeNumeroIdeMod = _dbWriteString(theTransaction, likeNumeroIdeMod);

            sqlWhere.append(" ( " + ALIAS_TABLE_AFFILIATION + AFAffiliation.FIELDNAME_NUMERO_IDE + " LIKE "
                    + likeNumeroIdeMod + " OR " + ALIAS_TABLE_ANNONCE
                    + AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_NUMERO_IDE + " LIKE " + likeNumeroIdeMod + " OR "
                    + ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_NUMERO_IDE_REMPLACEMENT + " LIKE "
                    + likeNumeroIdeMod + " ) ");

        }

        if (!JadeStringUtil.isBlankOrZero(likeNumeroAffilie)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            String likeNumeroAffilieMod = "%" + likeNumeroAffilie + "%";
            likeNumeroAffilieMod = _dbWriteString(theTransaction, likeNumeroAffilieMod);

            sqlWhere.append(" ( " + ALIAS_TABLE_AFFILIATION + AFAffiliation.FIELDNAME_NUMERO_AFFILIE + " LIKE "
                    + likeNumeroAffilieMod + " OR " + ALIAS_TABLE_ANNONCE
                    + AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_NUMERO_AFFILIE_LIEE + " LIKE " + likeNumeroAffilieMod + " ) ");

        }

        if (!JadeStringUtil.isBlankOrZero(likeIdAffiliationLiee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            likeIdAffiliationLiee = "%" + likeIdAffiliationLiee + "%";
            likeIdAffiliationLiee = _dbWriteString(theTransaction, likeIdAffiliationLiee);

            sqlWhere.append(ALIAS_TABLE_ANNONCE + AFIdeAnnonce.IDE_ANNONCE_FIELD_LIST_ID_AFFILIATION_LIEE + " LIKE "
                    + likeIdAffiliationLiee);

        }

        createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_RAISON_SOCIALE, "LIKE_WITHOUT_CASSE", likeRaisonSociale);

        createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_HISTORIQUE_STATUT_IDE, "=", forStatut);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_AFFILIATION, "=", forIdAffiliation);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_AFFILIATION
                + AFAffiliation.FIELDNAME_TIER_ID, "=", forIdTiers);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_CATEGORIE, "=", forCategorie);

        createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_AFFILIATION
                + AFAffiliation.FIELDNAME_NUMERO_AFFILIE, "=", getForNumeroAffilie());

        createCondition(theTransaction, CREATE_CONDITION_STRING, sqlWhere, ALIAS_TABLE_AFFILIATION
                + AFAffiliation.FIELDNAME_NUMERO_IDE, "=", getForNumeroIde());

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE, "=", forType);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ETAT, "=", forEtat);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_CATEGORIE, "IN",
                transformListEnInValues(getInCategorie(), CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE, "IN",
                transformListEnInValues(getInType(), CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ETAT, "IN",
                transformListEnInValues(getInEtat(), CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_CATEGORIE, "NOT IN",
                transformListEnInValues(getNotInCategorie(), CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_AFFILIATION
                + AFAffiliation.FIELDNAME_STATUT_IDE, "NOT IN",
                transformListEnInValues(notInStatutIDEAffiliation, CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_TYPE, "NOT IN",
                transformListEnInValues(getNotInType(), CREATE_CONDITION_NUMERIC));

        createCondition(theTransaction, CREATE_CONDITION_DATE, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_CREATION, ">=", fromDateCreation);

        createCondition(theTransaction, CREATE_CONDITION_DATE, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_CREATION, "<=", untilDateCreation);

        createCondition(theTransaction, CREATE_CONDITION_DATE, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_TRAITEMENT, ">=", fromDateTraitement);

        createCondition(theTransaction, CREATE_CONDITION_DATE, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_DATE_TRAITEMENT, "<=", untilDateTraitement);

        createCondition(theTransaction, CREATE_CONDITION_NUMERIC, sqlWhere, ALIAS_TABLE_ANNONCE
                + AFIdeAnnonce.IDE_ANNONCE_FIELD_ID_ANNONCE, "IN",
                transformListEnInValues(getInIdAnnonce(), CREATE_CONDITION_NUMERIC));

        return sqlWhere.toString();
    }

    public List<String> getInIdAnnonce() {
        return inIdAnnonce;
    }

    public void setInIdAnnonce(List<String> inIdAnnonce) {
        this.inIdAnnonce = inIdAnnonce;
    }

    private String transformListEnInValues(List<String> listValues, String typeCondition) {

        if (listValues == null || listValues.size() <= 0) {
            return "";
        }

        StringBuffer inValues = new StringBuffer();

        inValues.append("(");

        for (String aValue : listValues) {

            if (CREATE_CONDITION_STRING.equalsIgnoreCase(typeCondition)) {
                inValues.append("'");
            }

            inValues.append(aValue);

            if (CREATE_CONDITION_STRING.equalsIgnoreCase(typeCondition)) {
                inValues.append("'");
            }

            inValues.append(",");
        }

        inValues = inValues.deleteCharAt(inValues.length() - 1);

        inValues.append(")");

        return inValues.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFIdeAnnonce();
    }

    public String getForCategorie() {
        return forCategorie;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setInEtat(List<String> inEtat) {
        this.inEtat = inEtat;
    }

    public String getForStatut() {
        return forStatut;
    }

    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    public String getFromDateCreation() {
        return fromDateCreation;
    }

    public void setFromDateCreation(String fromDateCreation) {
        this.fromDateCreation = fromDateCreation;
    }

    public String getUntilDateCreation() {
        return untilDateCreation;
    }

    public void setUntilDateCreation(String untilDateCreation) {
        this.untilDateCreation = untilDateCreation;
    }

    public String getFromDateTraitement() {
        return fromDateTraitement;
    }

    public void setFromDateTraitement(String fromDateTraitement) {
        this.fromDateTraitement = fromDateTraitement;
    }

    public String getUntilDateTraitement() {
        return untilDateTraitement;
    }

    public void setUntilDateTraitement(String untilDateTraitement) {
        this.untilDateTraitement = untilDateTraitement;
    }

}
