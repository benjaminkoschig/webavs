/*
 * Créé le 13 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdminManager;

/**
 * @author hna
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPDecisionAgenceCommunaleManager extends TIPAvsAdrLienAdminManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forAgenceCommunale = "";

    private java.lang.String forGenreAffilie = "";

    private java.lang.String forIdDecision = "";

    private java.lang.String forIdPassage = "";

    private Boolean forImpression = Boolean.FALSE;

    private Boolean forImpressionMontantIdentique = Boolean.FALSE;

    private Boolean forNotImpression = Boolean.FALSE;

    private java.lang.String forTypeDecision = "";

    private java.lang.String fromAffilieDebut = "";

    private java.lang.String order = "";
    private java.lang.String untilAffilie = "";

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        String fields = super._getFields(statement);
        fields += ", " + _getCollection() + "CPDECIP.IAIDEC IAIDEC," + _getCollection() + "CPDECIP.MAIAFF MAIAFF,"
                + _getCollection() + "AFAFFIP.MALNAF MALNAF," + _getCollection() + "AFAFFIP.MATPER MATPER,"
                + _getCollection() + "CPDECIP.ICIIFD ICIIFD," + _getCollection() + "CPDECIP.ICIIFP ICIIFP,"
                + _getCollection() + "CPDECIP.EBIPAS EBIPAS," + _getCollection() + "CPDECIP.IATTDE IATTDE,"
                + _getCollection() + "CPDECIP.HTICJT HTICJT," + _getCollection() + "CPDECIP.IBIDCF IBIDCF,"
                + _getCollection() + "CPDECIP.IATGAF IATGAF," + _getCollection() + "CPDECIP.IADINF IADINF,"
                + _getCollection() + "CPDECIP.IAANNE IAANNE," + _getCollection() + "CPDECIP.IADDEB IADDEB,"
                + _getCollection() + "CPDECIP.IADFIN IADFIN," + _getCollection() + "CPDECIP.IARESP IARESP,"
                + _getCollection() + "CPDECIP.IATSPE  IATSPE," + _getCollection() + "CPDECIP.IABSIG IABSIG,"
                + _getCollection() + "CPDECIP.IAACTI IAACTI," + _getCollection() + "CPDECIP.IABDAC IABDAC,"
                + _getCollection() + "CPDECIP.IABCOM IABCOM," + _getCollection() + "CPDECIP.IAOPPO IAOPPO,"
                + _getCollection() + "CPDECIP.IAAPRI IAAPRI, " + _getCollection() + "CPDECIP.IABIMP IABIMP, "
                + _getCollection() + "CPDECIP.IATETA IATETA, " + _getCollection() + "CPDECIP.IADFAC IADFAC, "
                + _getCollection() + "CPDECIP.IABCMP IABCMP, " + _getCollection() + "CPDECIP.IAICOM IAICOM, "
                + _getCollection() + "CPDECIP.IABPRO IABPRO ";
        return fields;
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String decision = _getCollection() + "CPDECIP";
        String affiliation = _getCollection() + "AFAFFIP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + decision + " ON (" + decision + ".HTITIE=tiers.HTITIE)";
        // Tests si il faut ajouter l'affiliation car elle peut
        // déjà être implentée dans le manager hérité des tiers
        String affiliationMapperClassName = "";
        try {
            TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            affiliationMapperClassName = app.getProperty("affiliation_mapper_class");
        } catch (Exception e) {
            affiliationMapperClassName = "";
        }
        if (JadeStringUtil.isEmpty(affiliationMapperClassName)) {
            from += " INNER JOIN " + affiliation + " ON (" + affiliation + ".HTITIE=tiers.HTITIE)";
        }
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
        String sqlWhere = "";
        sqlWhere += super._getWhere(statement);
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += _getCollection() + "CPDECIP.MAIAFF=" + _getCollection() + "AFAFFIP.MAIAFF";
        // Pour un passage
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.EBIPAS="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }
        // Pour un type de décision
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IATTDE="
                    + _dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
        }
        // Pour un genre
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IATGAF="
                    + _dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // Depuis un affilié
        if (getFromAffilieDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF>="
                    + _dbWriteString(statement.getTransaction(), getFromAffilieDebut());
        }
        // Jusqu'à un affilié
        if (getUntilAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF <="
                    + _dbWriteString(statement.getTransaction(), getUntilAffilie());
        }
        if (getForAgenceCommunale().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TIADMIP.HBCADM ="
                    + _dbWriteString(statement.getTransaction(), getForAgenceCommunale());
        }
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IAIDEC ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        // Prendre uniquement celles que l'on veut imprimer
        if ((Boolean.TRUE.equals(getForImpression())) && (Boolean.FALSE.equals(getForImpressionMontantIdentique()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABIMP = "
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Prendre uniquement celles qui ont le meme montant entre la provisoire
        // et la communication fiscale
        if (Boolean.TRUE.equals(getForImpressionMontantIdentique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABIMP = '2' AND IAFACT = '2' ";
        }
        // Prendre uniquement celles que l'on ne veut pas imprimer
        if (Boolean.TRUE.equals(getForNotImpression())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABIMP = "
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionAgenceCommunale();
    }

    /**
     * @return
     */
    public java.lang.String getForAgenceCommunale() {
        return forAgenceCommunale;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * @return
     */
    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public Boolean getForImpression() {
        return forImpression;
    }

    public Boolean getForImpressionMontantIdentique() {
        return forImpressionMontantIdentique;
    }

    public Boolean getForNotImpression() {
        return forNotImpression;
    }

    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    /**
     * @return
     */
    public java.lang.String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * @return
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * @return
     */
    public java.lang.String getUntilAffilie() {
        return untilAffilie;
    }

    /**
     * Tri pas agence communale Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAgence() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("HBCADM ASC");
        } else {
            setOrder(getOrder() + ", HBCADM ASC");
        }
    }

    /**
     * Tri par année Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnnee() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IAANNE");
        } else {
            setOrder(getOrder() + ", IAANNE ASC");
        }
    }

    /**
     * Tri par date de début de décision Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDebutDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IADDEB");
        } else {
            setOrder(getOrder() + ", IADDEB ASC");
        }
    }

    /**
     * Tri par numéro d'affilié Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNoAffilie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("MALNAF ASC");
        } else {
            setOrder(getOrder() + ", MALNAF ASC");
        }
    }

    /**
     * Tri par utilisateur Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByUser() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IARESP");
        } else {
            setOrder(getOrder() + ", IARESP ASC");
        }
    }

    /**
     * @param string
     */
    public void setForAgenceCommunale(java.lang.String string) {
        forAgenceCommunale = string;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    /**
     * @param string
     */
    public void setForIdDecision(java.lang.String string) {
        forIdDecision = string;
    }

    /**
     * @param string
     */
    public void setForIdPassage(java.lang.String string) {
        forIdPassage = string;
    }

    public void setForImpression(Boolean forImpression) {
        this.forImpression = forImpression;
    }

    public void setForImpressionMontantIdentique(Boolean forImpressionMontantIdentique) {
        this.forImpressionMontantIdentique = forImpressionMontantIdentique;
    }

    public void setForNotImpression(Boolean forNotImpression) {
        this.forNotImpression = forNotImpression;
    }

    public void setForTypeDecision(java.lang.String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    /**
     * @param string
     */
    public void setFromAffilieDebut(java.lang.String string) {
        fromAffilieDebut = string;
    }

    /**
     * @param string
     */
    public void setOrder(java.lang.String string) {
        order = string;
    }

    /**
     * @param string
     */
    public void setUntilAffilie(java.lang.String string) {
        untilAffilie = string;
    }

}
