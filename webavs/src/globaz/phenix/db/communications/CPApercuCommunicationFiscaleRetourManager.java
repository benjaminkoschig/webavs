/*
 * Créé le 30 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.toolbox.CPToolBox;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPApercuCommunicationFiscaleRetourManager extends CPCommunicationFiscaleRetourManager {

    private static final long serialVersionUID = 4584273820502747429L;
    private Boolean estAbandonne = new Boolean(false);
    private Boolean estEnEnquete = new Boolean(false);
    private String forReportType = "";

    private String likeNom = "";

    private String likePrenom = "";
    private String likeSenderId = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = " cpcretp.iwrjou,cpcretp.ikrety,CPCRETP.htitie,cpsecon.seassty , cpsecon.SEOFNAM,cpsecon.SEFINAM,cpcretp.ikiret,malnaf,hxncon,htlde1,htlde2,ikann1,ikrev1,cpcretp.ikcapi,cpcretp.ikfort,iknoma,iknuma,iknumc,iklpre,ikafge,ikcoge,ikcone,ikncju,ikcovd,ikafvd,cpcrvdp.iklnom,iktsta,iktgaf,cpcrgep.iklnom";
        if (!JadeStringUtil.isEmpty(getForIdPlausibilite())) {
            fields += ",ilcrpp, cplcrpp.ibidcf, cplcrpp.ixidpa";
        }
        return fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer("");
        from.append(_getCollection() + "CPCRETP AS CPCRETP" + " LEFT JOIN " + _getCollection() + "CPJOURP AS CPJOURP"
                + " ON (CPJOURP.IWRJOU = CPCRETP.IWRJOU) "
                + " LEFT JOIN "
                + _getCollection()
                + "AFAFFIP AS AFAFFIP"
                + " ON (AFAFFIP.MAIAFF = CPCRETP.MAIAFF) "
                + " LEFT JOIN "
                + _getCollection()
                + "TIPAVSP AS TIPAVSP"
                + " ON (TIPAVSP.HTITIE = CPCRETP.HTITIE) "
                + " LEFT JOIN "
                + _getCollection()
                + "TIPERSP AS TIPERSP"
                + " ON (TIPERSP.HTITIE = CPCRETP.HTITIE) "
                + " LEFT JOIN "
                + _getCollection()
                + "TITIERP AS TITIERP"
                + " ON (TITIERP.HTITIE = CPCRETP.HTITIE) "
                +
                // On rajoute les communications détaillés pour le
                // canton de vd
                " left join "
                + _getCollection()
                + "CPCRVDP as CPCRVDP      on (CPCRVDP.ikiret      = CPCRETP.ikiret) "
                +
                // pour le jura
                " left join "
                + _getCollection()
                + "CPCRJUP as CPCRJUP      on (CPCRJUP.ikiret      = CPCRETP.ikiret) "
                +
                // pour neuchatel
                " left join "
                + _getCollection()
                + "CPCRNEP as CPCRNEP      on (CPCRNEP.ikiret      = CPCRETP.ikiret) "
                +
                // pour Genf
                " left join " + _getCollection()
                + "CPCRGEP as CPCRGEP      on (CPCRGEP.ikiret      = CPCRETP.ikiret) "
                +
                // pour Valais
                " left join " + _getCollection() + "CPCRVSP as CPCRVSP      on (CPCRVSP.ikiret      = CPCRETP.ikiret) "
                +
                // pour Sedex
                " left join " + _getCollection() + "CPSECON as CPSECON      on (CPSECON.ikiret      = CPCRETP.ikiret) ");

        if (!JadeStringUtil.isEmpty(getForIdPlausibilite())) {
            from.append(" LEFT OUTER JOIN " + _getCollection() + "CPLCRPP" + " " + "CPLCRPP" + " ON (" + "CPLCRPP"
                    + ".IBIDCF=" + "CPCRETP" + ".IKIRET )");
        }
        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        String order = super._getOrder(statement);
        if (JadeStringUtil.isBlankOrZero(order)) {
            return " IKRETY DESC ";
        } else {
            return " IKRETY DESC, " + order;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdJournalRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IWRJOU=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalRetour());
        }
        // traitement du positionnement
        if (getForNumAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNAVS=" + this._dbWriteString(statement.getTransaction(), getForNumAvs());
        }
        // traitement du positionnement
        if (getForIdPlausibilite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPLCRPP.IXIDPA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        // traitement du positionnement
        if (getForIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKRET=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication());
        }
        // traitement du positionnement
        if (getForIdIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD=" + this._dbWriteNumeric(statement.getTransaction(), getForIdIfd());
        }
        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        // traitement du positionnement
        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }

        // traitement du positionnement
        if (getFromNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF >=" + this._dbWriteString(statement.getTransaction(), getFromNumAffilie());
        }
        // traitement du positionnement
        if (getLikeNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(AFAFFIP.MALNAF like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            sqlWhere += " OR CPCRVDP.ikafvd like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            sqlWhere += " OR CPCRGEP.IKAFGE like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            sqlWhere += " OR CPCRVSP.IKNOCO like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            sqlWhere += " OR CPSECON.SEYBUID like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            sqlWhere += " OR CPCRVSP.IKNUMA like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%") + ")";
        }
        // traitement du positionnement
        if (getForNumContibuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNCON=" + this._dbWriteString(statement.getTransaction(), getForNumContibuable());
        }
        if (getLikeNumContribuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String varLike = "";
            try {
                varLike = JANumberFormatter.deQuote(CPToolBox.unFormat(getLikeNumContribuable()));
            } catch (Exception e) {
                varLike = " ";
            }
            sqlWhere += "(HXNCON like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
            sqlWhere += " OR CPCRVDP.IKCOVD like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
            sqlWhere += " OR CPCRGEP.IKCOGE like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
            sqlWhere += " OR CPCRVSP.IKNUMC like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
            sqlWhere += " OR CPSECON.SELPEID like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
            // Remplacement like par bornes >= et <= car le champ
            // numContribualbe JU et NE sont numériques
            sqlWhere += " OR CPCRNEP.IKCONE >=" + CPToolBox.formattedString(varLike, 15, '0')
                    + " AND CPCRNEP.IKCONE <=" + CPToolBox.formattedString(varLike, 15, '9');
            sqlWhere += " OR CPCRJUP.IKNCJU >=" + CPToolBox.formattedString(varLike, 11, '0')
                    + " AND CPCRJUP.IKNCJU <=" + CPToolBox.formattedString(varLike, 11, '9') + ")";

        }
        // traitement du positionnement
        if (getFromNumContibuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNCON >=" + this._dbWriteString(statement.getTransaction(), getFromNumContibuable());
        }
        // traitement du positionnement
        if (getTillNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF <=" + this._dbWriteString(statement.getTransaction(), getTillNumAffilie());
        }
        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(IKTGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie())
                    + " OR IKTGCJ=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie()) + ")";
        }
        // traitement du positionnement
        if (getForGenreTaxation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTGTA=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreTaxation());
        }
        // Except l'id retour
        if (getExceptIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptIdRetour());
        }
        // Inférieur l'id retour
        if (getForLtIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET <" + this._dbWriteNumeric(statement.getTransaction(), getForLtIdRetour());
        }
        // traitement du positionnement
        if (getForStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA IN (" + getForStatus() + ")";
        }
        if (getForStatus().length() == 0) {
            // traitement du positionnement
            if (getExceptStatus().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IKTSTA<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptStatus());
            }
            // traitement du positionnement
            if (getInStatus().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IKTSTA in (" + getInStatus() + ")";
            }
        }
        // traitement du positionnement
        if (getNotInStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA not in (" + getNotInStatus() + ")";
        }
        // Affiche tous les status sauf "abandon" par defaut
        if (!getEstAbandonne().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA <> "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
        }
        // Affiche tous les status sauf "enEnquete" par defaut
        if (!getEstEnEnquete().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA <> "
                    + this._dbWriteNumeric(statement.getTransaction(), CPCommunicationFiscaleRetourViewBean.CS_ENQUETE);
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKANN1=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // traitement du positionnement
        if (getForIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRetour());
        }
        // traitement du positionnement
        if (getForIdPlausibilite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPLCRPP.IXIDPA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        if (!JadeStringUtil.isBlank(getLikeNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (upper(rtrim(TITIERP.HTLDE1)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRVDP.IKLNOM)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRGEP.IKLNCJ)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRGEP.IKLNOM)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPSECON.SEOFNAM)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRVSP.IKNOMA)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNom() + "%") + "))" + ")";
        }

        if (!JadeStringUtil.isBlank(getLikePrenom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (upper(rtrim(TITIERP.HTLDE2)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRVDP.IKLNOM)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRGEP.IKLPRE)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRGEP.IKLPCJ)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPSECON.SEFINAM)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))";
            sqlWhere += " OR  upper(rtrim(CPCRVSP.IKNCON)) like upper(rtrim("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikePrenom() + "%") + "))" + ")";
        }

        if (!JadeStringUtil.isBlank(getForReportType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (!getForReportType().equals("16")) {
                sqlWhere += " IKRETY = " + getForReportType();
            } else {
                sqlWhere += " SEASSTY = 4";
            }
        }
        // traitement du positionnement
        if (getLikeSenderId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPSECON.SESENID like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeSenderId() + "%");
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourViewBean();
    }

    /**
     * @return
     */
    public Boolean getEstAbandonne() {
        return estAbandonne;
    }

    /**
     * @return
     */
    public Boolean getEstEnEnquete() {
        return estEnEnquete;
    }

    public String getForReportType() {
        return forReportType;
    }

    /**
     * @return
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    public String getLikeSenderId() {
        return likeSenderId;
    }

    /**
     * @param estAbandonne
     */
    public void setEstAbandonne(Boolean estAbandonne) {
        this.estAbandonne = estAbandonne;
    }

    /**
     * @param estEnEnquete
     */
    public void setEstEnEnquete(Boolean estEnEnquete) {
        this.estEnEnquete = estEnEnquete;
    }

    public void setForReportType(String forReportType) {
        this.forReportType = forReportType;
    }

    /**
     * @param string
     */
    public void setLikeNom(String string) {
        likeNom = string;
    }

    /**
     * @param string
     */
    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    public void setLikeSenderId(String likeSenderId) {
        this.likeSenderId = likeSenderId;
    }

}
