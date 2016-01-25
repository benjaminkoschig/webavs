/*
 * Créé le 13 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.principale;

/**
 * @author hna
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPDecisionAffiliationCalculManager extends CPDecisionAffiliationManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forIdDonneesCalcul = "";

    private java.lang.String order = "";

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        // String fields = super._getFields(statement);
        String fields = "";
        fields += _getCollection() + "CPDECIP.IAIDEC IAIDEC," + _getCollection() + "CPDECIP.HTITIE HTITIE,"
                + _getCollection() + "CPDECIP.MAIAFF MAIAFF," + _getCollection() + "AFAFFIP.MALNAF MALNAF,"
                + _getCollection() + "AFAFFIP.MADDEB MADDEB," + _getCollection() + "AFAFFIP.MADFIN MADFIN,"
                + _getCollection() + "CPDECIP.EBIPAS EBIPAS," + _getCollection() + "CPDECIP.IATTDE IATTDE,"
                + _getCollection() + "CPDECIP.IATGAF IATGAF," + _getCollection() + "CPDECIP.IAANNE IAANNE,"
                + _getCollection() + "CPDECIP.IADDEB IADDEB," + _getCollection() + "CPDECIP.IADFIN IADFIN,"
                + _getCollection() + "CPDECIP.IADFIN IADFAC," + _getCollection() + "CPDECIP.IAACTI IAACTI,"
                + _getCollection() + "CPDECIP.IATETA IATETA, " + _getCollection() + "CPDOCAP.IHMDCA IHMDCA, "
                + _getCollection() + "CPDOCAP.IHIDCA IHIDCA ";
        return fields;
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String calcul = _getCollection() + "CPDOCAP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + calcul + " ON (" + calcul + ".IAIDEC=" + _getCollection() + "CPDECIP.IAIDEC)";

        String tableDecision = _getCollection() + "CPDECIP";
        String tableAffiliation = _getCollection() + "AFAFFIP";
        String fromTable = "FROM " + tableDecision + " INNER JOIN " + tableAffiliation + " ON ( " + tableDecision
                + ".MAIAFF=" + tableAffiliation + ".MAIAFF) "
                + "WHERE (IAACTI = '1'or (IAACTI='2' and (IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB))";
        // Inclusion de la sélection dans le sous select...
        if (getFromAnneeDecision().length() != 0) {
            fromTable += " AND IAANNE>=" + _dbWriteNumeric(statement.getTransaction(), getFromAnneeDecision());
        }
        // traitement du positionnement
        if (getTillAnneeDecision().length() != 0) {
            fromTable += " AND IAANNE<=" + _dbWriteNumeric(statement.getTransaction(), getTillAnneeDecision());
        }
        // traitement du positionnement
        if (getFromPeriodeFacturation().length() != 0) {
            fromTable += " AND IADFAC>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromPeriodeFacturation());
        }
        // traitement du positionnement
        if (getTillPeriodeFacturation().length() != 0) {
            fromTable += " AND IADFAC<=" + _dbWriteDateAMJ(statement.getTransaction(), getTillPeriodeFacturation());
        }
        // traitement du positionnement
        if (getForIdPassage().length() != 0) {
            fromTable += " AND EBIPAS=" + _dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }
        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            fromTable += " AND IATGAF=" + _dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // traitement du positionnement
        if (getFromNoAffilie().length() != 0) {
            fromTable += " AND MALNAF>=" + _dbWriteString(statement.getTransaction(), getFromNoAffilie());
        }
        // traitement du positionnement
        if (getTillNoAffilie().length() != 0) {
            fromTable += " AND MALNAF<=" + _dbWriteString(statement.getTransaction(), getTillNoAffilie());
        }
        fromTable += " group by " + tableDecision + ".HTITIE," + tableDecision + ".IAANNE) temp";
        from += "INNER JOIN (SELECT " + tableDecision + ".IAANNE ANNEE, " + tableDecision + ".HTITIE TIERS "
                + fromTable;
        from += " ON (" + tableDecision + ".IAANNE=temp.ANNEE AND " + tableDecision + ".HTITIE=temp.TIERS)";

        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        // Pour une données calculées
        if (getForIdDonneesCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDOCAP.IHIDCA="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDonneesCalcul());
        }
        // // Avec les décisions actives
        // if (Boolean.TRUE.equals(getIsActive())) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += "IAACTI = "
        // + _dbWriteBoolean(statement.getTransaction(), new Boolean(true),
        // BConstants.DB_TYPE_BOOLEAN_CHAR);
        // }
        // // traitement du positionnement
        // if(getForEtat().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += "IATETA=" + _dbWriteNumeric(statement.getTransaction(),
        // getForEtat());
        // }
        // // Pour un etat
        // if (getInEtat().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += "IATETA IN ("+ getInEtat()+")";
        // }
        // // Pour un etat
        // if (getForTypeDecision().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += "IATETA IN ("+ getInEtat()+")";
        // }
        // Avec les décisions actives ou radiées
        // if (Boolean.TRUE.equals(getIsActiveOrRadie())) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere +=
        // " (IAACTI = '1'or (IAACTI='2' and (IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB))";
        // }
        // Différent de la spécification
        // if (getForExceptSpecification().length() != 0) {
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += "IATSPE <> "
        // + _dbWriteNumeric(statement.getTransaction(),
        // getForExceptSpecification());
        // }
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionAffiliationCalcul();
    }

    /**
     * @return
     */
    public java.lang.String getForIdDonneesCalcul() {
        return forIdDonneesCalcul;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * @param string
     */
    public void setForIdDonneesCalcul(java.lang.String string) {
        forIdDonneesCalcul = string;
    }

    /**
     * @param string
     */
    @Override
    public void setOrder(java.lang.String string) {
        order = string;
    }

}
