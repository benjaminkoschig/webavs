/*
 * Créé le 13 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;

/**
 * @author hna
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPDecisionAffiliationCotisationManager extends CPDecisionAffiliationManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    private java.lang.String forGenreCotisation = "";
    private String forIdAffiliation = "";

    private String notInIdAffiliation = "";

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
                + _getCollection() + "CPDECIP.IAACTI IAACTI," + _getCollection() + "CPDECIP.IABIMP IATETA, "
                + _getCollection() + "CPDECIP.IATSPE IATSPE, " + _getCollection() + "CPCOTIP.ISICOT ISICOT, "
                + _getCollection() + "CPCOTIP.ISTGCO ISTGCO, " + _getCollection() + "CPCOTIP.MEICOT MEICOT, "
                + _getCollection() + "CPCOTIP.ISMCME ISMCME, " + _getCollection() + "CPCOTIP.ISMCTR ISMCTR, "
                + _getCollection() + "CPCOTIP.ISMCSE ISMCSE, " + _getCollection() + "CPCOTIP.ISMCAN ISMCAN, "
                + _getCollection() + "CPCOTIP.ISMTAU ISMTAU, " + _getCollection() + "CPCOTIP.ISDDCO ISDDCO, "
                + _getCollection() + "CPCOTIP.ISDFCO ISDFCO, " + _getCollection() + "CPCOTIP.ISTPER ISTPER, "
                + _getCollection() + "CPCOTIP.ISMCFA ISMCFA, " + _getCollection() + "CPCOTIP.ISBZER ISBZER, "
                + _getCollection() + "CPCOTIP.ISBMIN ISBMIN";
        return fields;

    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String cotisation = _getCollection() + "CPCOTIP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + cotisation + " ON (" + cotisation + ".IAIDEC=" + _getCollection() + "CPDECIP.IAIDEC)";

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

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (getNotInIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.MAIAFF not in (" + getNotInIdAffiliation() + ")";
        }

        // Pour une données calculées
        if (getForGenreCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPCOTIP.ISTGCO="
                    + _dbWriteNumeric(statement.getTransaction(), getForGenreCotisation());
        }
        // Avec les décisions actives
        if (Boolean.TRUE.equals(getIsActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAACTI = "
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // traitement du positionnement
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA=" + _dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.MAIAFF="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        // Pour un etat
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE=" + _dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
        }
        // Pour un etat
        if (getInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA IN (" + getInEtat() + ")";
        }
        // Avec les décisions actives ou radiées
        if (Boolean.TRUE.equals(getIsActiveOrRadie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (IAACTI = '1'or (IAACTI='2' and (IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB))";
        }
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionAffiliationCotisation();
    }

    /**
     * @return
     */
    public java.lang.String getForGenreCotisation() {
        return forGenreCotisation;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    @Override
    public String getNotInIdAffiliation() {
        return notInIdAffiliation;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getOrder() {
        return order;
    }

    public void oderByIdCotisationDesc() {
        order = _getCollection() + "CPCOTIP.ISICOT DESC";
    }

    /**
     * @param string
     */
    public void setForGenreCotisation(java.lang.String string) {
        forGenreCotisation = string;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    @Override
    public void setNotInIdAffiliation(String notInIdAffiliation) {
        this.notInIdAffiliation = notInIdAffiliation;
    }

    /**
     * @param string
     */
    @Override
    public void setOrder(java.lang.String string) {
        order = string;
    }

}
