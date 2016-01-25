package globaz.naos.db.affiliation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdminManager;

public class AFAffiliationForDSManager extends TIPAvsAdrLienAdminManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAssuranceSeule = "";
    private String forDateFin = "";
    private String forFinCotisation = "";
    private String forTillDateDebut = "";
    private String forTypeAffiliation = "";
    private String forTypeDeclaration = "";
    private String forTypeFacturation = "";
    private String fromAffilieNumero = "";
    private String fromDateFin = "";
    private String toAffilieNumero = "";
    private boolean wantAssuranceSeule = false;
    private boolean wantProvisoire = true;

    // Si on ne veut pas le tri par agence communale, on ne fait pas toutes les
    // jointures pour des raison de perf.
    private boolean wantTriAgenceCommunale = false;

    public AFAffiliationForDSManager() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getFields(BStatement statement) {
        String fields = "";
        /*
         * if(wantTriAgenceCommunale){ fields = super._getFields(statement); fields += ", "; }
         */
        fields += "*";
        return fields;
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = "";
        if (wantTriAgenceCommunale) {
            from = super._getFrom(statement);
        } else {
            from += _getCollection() + "AFAFFIP";
        }
        if (wantAssuranceSeule && !JadeStringUtil.isBlankOrZero(forAssuranceSeule)) {
            from += " JOIN "
                    + _getCollection()
                    + "AFPLAFP PLA ON (PLA.MAIAFF = "
                    + _getCollection()
                    + "AFAFFIP.MAIAFF AND PLA.MUBINA="
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR) + ")";
            from += " JOIN " + _getCollection() + "AFCOTIP COTI2 on COTI2.MUIPLA = PLA.MUIPLA ";
            from += " JOIN " + _getCollection() + "AFASSUP ASSU ON ASSU.MBIASS = COTI2.MBIASS ";
            from += " WHERE (COTI2.MEDFIN = 0 OR COTI2.MEDFIN > "
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateFin)
                    + " ) AND MADDEB <> MADFIN AND MATTAF IN (804002,804005) AND ASSU.MBTTYP = " + forAssuranceSeule
                    + " AND ASSU.MBTGEN = " + CodeSystem.GENRE_ASS_PARITAIRE
                    + " AND MEBMER = 2 AND (MADFIN = 0 OR MADFIN > "
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateFin) + " ) " + " AND (MADDEB < "
                    + this._dbWriteDateAMJ(statement.getTransaction(), forTillDateDebut) + " ) ";
            if (!JadeStringUtil.isBlankOrZero(getForTypeDeclaration())) {
                from += " AND " + _getCollection() + "AFAFFIP.MATDEC=" + forTypeDeclaration;
            }
            if (!JadeStringUtil.isEmpty(getFromAffilieNumero())) {
                if (from.length() != 0) {
                    from += " AND ";
                }

                from += "MALNAF >= '" + getFromAffilieNumero() + "'";
            }
            if (!JadeStringUtil.isEmpty(getToAffilieNumero())) {
                if (from.length() != 0) {
                    from += " AND ";
                }

                from += "MALNAF <= '" + getToAffilieNumero() + "'";
            }
            if (getForTypeLien().length() != 0) {
                if (from.length() != 0) {
                    from += " AND ";
                }
                from += "HGTTLI=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeLien());
            }

            from += " AND " + _getCollection() + "AFAFFIP.MAIAFF ";
            from += " NOT IN (SELECT AFFIL.MAIAFF FROM " + _getCollection() + "AFCOTIP AF ";
            from += " JOIN " + _getCollection() + "AFASSUP ASS ON AF.MBIASS = ASS.MBIASS ";
            from += " JOIN " + _getCollection() + "AFPLAFP PLAN1 ON (PLAN1.MUIPLA = AF.MUIPLA) ";
            from += " JOIN " + _getCollection() + "AFAFFIP AFFIL ON (PLAN1.MAIAFF = AFFIL.MAIAFF) ";
            from += " JOIN " + _getCollection() + "AFPLAFP PLAN2 ON (PLAN2.MAIAFF = AFFIL.MAIAFF) ";
            from += " JOIN " + _getCollection() + "AFCOTIP AF2 ON (AF2.MUIPLA = PLAN2.MUIPLA) ";
            from += " JOIN " + _getCollection() + "AFASSUP ASS2 ON ASS2.MBIASS = AF2.MBIASS ";
            from += " WHERE AF.MEICOT <> AF2.MEICOT AND ASS.MBTTYP = " + forAssuranceSeule + " AND ASS2.MBTTYP = "
                    + CodeSystem.TYPE_ASS_COTISATION_AVS_AI;
            from += " AND ASS.MBTGEN = " + CodeSystem.GENRE_ASS_PARITAIRE + " AND ASS2.MBTGEN = "
                    + CodeSystem.GENRE_ASS_PARITAIRE;
            from += " AND (AF.MEDFIN = 0 OR AF.MEDFIN > "
                    + this._dbWriteDateAMJ(statement.getTransaction(), forFinCotisation)
                    + ") AND (AF2.MEDFIN = 0 OR AF2.MEDFIN >"
                    + this._dbWriteDateAMJ(statement.getTransaction(), forFinCotisation) + " ) ";
            from += " AND (MADFIN = 0 OR MADFIN > "
                    + this._dbWriteDateAMJ(statement.getTransaction(), forFinCotisation) + " ) ";
            from += " AND (MADDEB < " + this._dbWriteDateAMJ(statement.getTransaction(), forTillDateDebut) + " ) ";
            from += " AND (AF.MEBMER = 1 OR AF2.MEBMER = 2) ";
            from += " AND MATTAF IN (804002,804005))";

        }
        return from;
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if ("".equals(getOrderBy())) {
            return "MALNAF, MADDEB";
        } else {
            return getOrderBy();
        }
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (!wantAssuranceSeule) {
            if (wantTriAgenceCommunale) {
                sqlWhere = super._getWhere(statement);
            }
            // PO 3585: Ne pas prendre les affiliations annulées.
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MADFIN<>" + _getCollection() + "AFAFFIP.MADDEB";

            if (forDateFin.length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "AFAFFIP.MADFIN="
                        + this._dbWriteDateAMJ(statement.getTransaction(), forDateFin);
            }
            if (getForTypeDeclaration().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "AFAFFIP.MATDEC="
                        + this._dbWriteNumeric(statement.getTransaction(), getForTypeDeclaration());
            }
            if (!JadeStringUtil.isEmpty(getFromAffilieNumero())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "MALNAF >= '" + getFromAffilieNumero() + "'";
            }
            if (!JadeStringUtil.isEmpty(getToAffilieNumero())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "MALNAF <= '" + getToAffilieNumero() + "'";
            }
            if (!JadeStringUtil.isEmpty(getForTillDateDebut())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " MADDEB < " + this._dbWriteDateAMJ(statement.getTransaction(), getForTillDateDebut());
            }
            if (!JadeStringUtil.isEmpty(getFromDateFin())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "(MADFIN > " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFin())
                        + " OR MADFIN=0)";
            }
            if (!JadeStringUtil.isEmpty(getForTypeAffiliation())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "MATTAF = " + getForTypeAffiliation();
            }
            if (!JadeStringUtil.isEmpty(getForTypeFacturation())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                if (getForTypeFacturation().equals(AFAffiliationManager.PARITAIRE)) {
                    sqlWhere += "MATTAF in(804002,804005,804010,804012)";
                } else {
                    sqlWhere += "MATTAF in(804001,804004) AND MATPER not in(802005)";
                }
            }
            if (!wantProvisoire) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "MABTRA ="
                        + this._dbWriteBoolean(statement.getTransaction(), wantProvisoire, "is provisoire");
                ;

            }

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
        return new AFAffiliation();
    }

    public String getForAssuranceSeule() {
        return forAssuranceSeule;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForFinCotisation() {
        return forFinCotisation;
    }

    public String getForTillDateDebut() {
        return forTillDateDebut;
    }

    public String getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    public String getForTypeDeclaration() {
        return forTypeDeclaration;
    }

    public String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public String getFromAffilieNumero() {
        return fromAffilieNumero;
    }

    public String getFromDateFin() {
        return fromDateFin;
    }

    public String getToAffilieNumero() {
        return toAffilieNumero;
    }

    public boolean isWantAssuranceSeule() {
        return wantAssuranceSeule;
    }

    public boolean isWantProvisoire() {
        return wantProvisoire;
    }

    public boolean isWantTriAgenceCommunale() {
        return wantTriAgenceCommunale;
    }

    /**
     * Tri pas agence communale Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAgencePuisNumAff() {
        setOrderBy("HBCADM ASC, MALNAF ASC, MADDEB ASC ");

    }

    public void setForAssuranceSeule(String forAssuranceSeule) {
        this.forAssuranceSeule = forAssuranceSeule;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForFinCotisation(String forFinCotisation) {
        this.forFinCotisation = forFinCotisation;
    }

    public void setForTillDateDebut(String forTillDateDebut) {
        this.forTillDateDebut = forTillDateDebut;
    }

    public void setForTypeAffiliation(String forTypeAffiliation) {
        this.forTypeAffiliation = forTypeAffiliation;
    }

    public void setForTypeDeclaration(String forTypeDeclaration) {
        this.forTypeDeclaration = forTypeDeclaration;
    }

    public void setForTypeFacturation(String forTypeFacturation) {
        this.forTypeFacturation = forTypeFacturation;
    }

    public void setFromAffilieNumero(String fromAffilieNumero) {
        this.fromAffilieNumero = fromAffilieNumero;
    }

    public void setFromDateFin(String fromDateFin) {
        this.fromDateFin = fromDateFin;
    }

    public void setToAffilieNumero(String toAffilieNumero) {
        this.toAffilieNumero = toAffilieNumero;
    }

    public void setWantAssuranceSeule(boolean wantAssuranceSeule) {
        this.wantAssuranceSeule = wantAssuranceSeule;
    }

    public void setWantProvisoire(boolean wantProvisoire) {
        this.wantProvisoire = wantProvisoire;
    }

    public void setWantTriAgenceCommunale(boolean wantTriAgenceCommunale) {
        this.wantTriAgenceCommunale = wantTriAgenceCommunale;
    }
}
