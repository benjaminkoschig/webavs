package db;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseListViewBean;
import globaz.pyxis.util.TINSSFormater;

public class LAFichierCentralListViewBean extends TIPersonneAvsAdresseListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forFormeJuridique = "";
    private String forNumAffilieFC = "";
    private String forNumCaisseAF = "";
    private String forNumCaisseAVS = "";

    private boolean includeLibelleExterne = false;

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        /*
         * Ajouter le type d'affiliation
         */

        String fields = super._getFields(statement) + ",";

        fields += _getCollection() + "AFAFFIP.MATTAF  MATTAF, ";
        fields += _getCollection() + "AFSUAFP.MYDDEB  MYDDEB, ";
        fields += _getCollection() + "AFSUAFP.MYDFIN  MYDFIN, ";
        fields += _getCollection() + "AFSUAFP.MYTGEN  MYTGEN, ";
        fields += _getCollection() + "TIADMIP.HBCADM  HBCADM, ";
        fields += _getCollection() + "AFAFFIP.MAIAFF  MAIAFF, ";
        fields += _getCollection() + "AFSUAFP.MYTNAF  MYTNAF, ";
        fields += _getCollection() + "AFAFFIP.MADDEB  MADDEB, ";
        fields += _getCollection() + "AFAFFIP.MADFIN  MADFIN  ";
        return fields;
    }

    @Override
    protected String _getFromJoins(globaz.globall.db.BStatement statement) {
        String suiviCaisse = _getCollection() + "AFSUAFP";
        String affiliation = _getCollection() + "AFAFFIP";
        String tiersAdmin = _getCollection() + "TIADMIP";

        String from = super._getFromJoins(statement);

        from += " LEFT OUTER JOIN " + suiviCaisse + " ON (" + suiviCaisse + ".MAIAFF=" + affiliation + ".MAIAFF AND "
                + suiviCaisse + ".MYTGEN IN(" + CodeSystem.GENRE_CAISSE_AVS + "," + CodeSystem.GENRE_CAISSE_AF + "))";
        from += " LEFT OUTER JOIN " + tiersAdmin + " ON (" + tiersAdmin + ".HTITIE=" + suiviCaisse + ".HTITIE)";
        return from;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";// super._getWhere(statement);

        if (!JadeStringUtil.isEmpty(getForNumAffilieFC())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MYTNAF like " + _dbWriteString(statement.getTransaction(), getForNumAffilieFC() + "%");
        }

        if (!JadeStringUtil.isEmpty(getForNumCaisseAVS())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " HBCADM = " + _dbWriteString(statement.getTransaction(), getForNumCaisseAVS());
            sqlWhere += " AND MYTGEN = " + CodeSystem.GENRE_CAISSE_AVS;
        }

        if (!JadeStringUtil.isEmpty(getForNumCaisseAF())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " HBCADM = " + _dbWriteString(statement.getTransaction(), getForNumCaisseAF());
            sqlWhere += " AND MYTGEN = " + CodeSystem.GENRE_CAISSE_AF;
        }

        if (!JadeStringUtil.isEmpty(getForFormeJuridique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MATJUR = " + getForFormeJuridique();
        }
        // ////////////////////////////// VENANT DES ANCIENS GETWHERE
        // /////////////////////////////////////////
        TIApplication pyxisApp = null;
        try {
            if (getSession() instanceof BSession) {
                pyxisApp = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            } else {
                pyxisApp = new TIApplication();
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Unable to get application for LACERTA.");
        }

        // avec historique
        if (getForNumAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // TODO : AVSMOD
            // les num avs sur 8 ont un '.' ajouté en serrnière position par le
            // tag nnss
            // je commence par supprimer tout les points sinon les num comme
            // 254.73.143 ne pouront
            // pas être retrouvé (car le tag ajoute un '.' -> 254.73.143.)
            // Par la suite, il faudrait év. modifier le comportement du tag
            // pour qu'il n'ajoute pas ce '.'
            String numAvs = JadeStringUtil.removeChar(getForNumAvs(), '.');
            try {
                if (TINSSFormater.TYPE_EAN13.equals(getTypeRechercheAvs())) {
                    numAvs = TINSSFormater.format(numAvs, TINSSFormater.TYPE_EAN13);
                } else if (TINSSFormater.TYPE_NAVS.equals(getTypeRechercheAvs())) {
                    numAvs = TINSSFormater.format(numAvs, TINSSFormater.TYPE_NAVS);
                } else {
                    // pour compatibilité
                    IFormatData avsFormater = pyxisApp.getAvsFormater();
                    if (avsFormater != null) {
                        numAvs = avsFormater.format(numAvs);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sqlWhere += "(HVNAVS like " + _dbWriteString(statement.getTransaction(), numAvs + "%") + " OR "
                    + " HXNAVS like " + _dbWriteString(statement.getTransaction(), numAvs + "%") + ")";
        }

        if (getForDesignationUpper1Like().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if ((includeLibelleExterne == false) || (TIApplication.getAffMapper().getLibelleFieldName() == null)) {

                sqlWhere += "HTLDU1 like "
                        + JAUtil.removeAccents(_dbWriteString(statement.getTransaction(),
                                getForDesignationUpper1Like() + "%").toUpperCase());
            } else {
                // on inclut aussi le libelle externe dans la recherche, par
                // exemple, la raison social d'une affiliation.
                String libelleField = _getCollection() + TIApplication.getAffMapper().getLibelleFieldName();
                sqlWhere += "(HTLDU1 like "
                        + JAUtil.removeAccents(_dbWriteString(statement.getTransaction(),
                                getForDesignationUpper1Like() + "%").toUpperCase());
                sqlWhere += " or " + libelleField + " like "
                        + _dbWriteString(statement.getTransaction(), getForDesignationUpper1Like() + "%").toUpperCase();
                sqlWhere += ") ";

            }

        }

        if (getForDesignationUpper2Like().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "HTLDU2 like "
                    + JAUtil.removeAccents(_dbWriteString(statement.getTransaction(),
                            getForDesignationUpper2Like() + "%").toUpperCase());

        }

        if (getForAlias().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            /*
             * Changement suite au point ouvert po-AVS-GLOB-PROD-01805 -> passe de for à like...
             */
            sqlWhere += "HZLALI LIKE " + _dbWriteString(statement.getTransaction(), getForAlias() + "%");
            // sqlWhere += "HZLALI=" +
            // _dbWriteString(statement.getTransaction(), getForAlias());
        }

        if (getForNpaOrLocaliteLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (HJNPA like" + _dbWriteString(statement.getTransaction(), getForNpaOrLocaliteLike() + "%")
                    + "OR upper(HJLOCA) like"
                    + _dbWriteString(statement.getTransaction(), getForNpaOrLocaliteLike().toUpperCase() + "%") + ") ";
        }

        if (getForRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYTROL=" + _dbWriteNumeric(statement.getTransaction(), getForRole());
        }

        if (getForDateNaissance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String date = _dbWriteDateAMJ(statement.getTransaction(), getForDateNaissance());
            sqlWhere += "HPDNAI = " + date;
        }

        if (getForSexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HPTSEX = " + _dbWriteNumeric(statement.getTransaction(), getForSexe());
        }

        if (getForActiviteEntreDebutEtFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String dateDebutField = _getCollection() + TIApplication.getAffMapper().getDateDebutAffiliationFieldName();
            String dateFinField = _getCollection() + TIApplication.getAffMapper().getDateFinAffiliationFieldName();

            String date = _dbWriteDateAMJ(statement.getTransaction(), getForActiviteEntreDebutEtFin());
            sqlWhere += " ((" + date + " between " + dateDebutField + " and " + dateFinField + ") or (" + dateFinField
                    + "=0 and " + dateDebutField + "<= " + date + " ))";
        }

        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new LAFichierCentralViewBean();
    }

    public String getForFormeJuridique() {
        return forFormeJuridique;
    }

    public String getForNumAffilieFC() {
        return forNumAffilieFC;
    }

    public String getForNumCaisseAF() {
        return forNumCaisseAF;
    }

    public String getForNumCaisseAVS() {
        return forNumCaisseAVS;
    }

    @Override
    public boolean isIncludeLibelleExterne() {
        return includeLibelleExterne;
    }

    public void setForFormeJuridique(String forFormeJuridique) {
        this.forFormeJuridique = forFormeJuridique;
    }

    public void setForNumAffilieFC(String forNumAffilieFC) {
        this.forNumAffilieFC = forNumAffilieFC;
    }

    public void setForNumCaisseAF(String forNumCaisseAF) {
        this.forNumCaisseAF = forNumCaisseAF;
    }

    public void setForNumCaisseAVS(String forNumCaisseAVS) {
        this.forNumCaisseAVS = forNumCaisseAVS;
    }

    @Override
    public void setIncludeLibelleExterne(boolean includeLibelleExterne) {
        this.includeLibelleExterne = includeLibelleExterne;
    }
}
