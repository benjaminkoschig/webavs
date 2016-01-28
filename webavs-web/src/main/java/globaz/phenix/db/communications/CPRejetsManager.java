package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author JPA
 * @since JPA 6 septembre 2011
 */
public class CPRejetsManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forCanton = "";
    private String forEtat = "";

    private String forIdCommunication = "";

    private String forIdRejets = "";

    private String forReasonOfRejection = "";

    private String forReferenceMessageId = "";

    private String likeFirstName = "";
    private String likeMessageId = "";
    private String likeNumContribuable = "";

    private String likeOfficialName = "";

    private String likeOurBusinessReferenceId = "";

    private String likePersonId = "";

    private String likeReferenceMessageId = "";

    private String likeYourBusinessReferenceId = "";

    private Boolean wantAbandonne = true;

    private Boolean wantEnvoye = true;

    @Override
    protected String _getFields(BStatement statement) {
        return "tiers1.HXNCON AS NCON, periode1.ICANDD AS ANDD, demande.IBIDCF, CPSEID, SENDID, SENNOM, SENDEP, SENPHO, SENEMA, RECIID, MESSID, REMEID, OBREID, YBREID, SUBJEC, PERIDC, PERSID, OFNAME, FINAME, PERSEX, DATNAI, ADRLI1, ADRLI2, STREET, LOCALI, TOWNA, MARITA, MESDAT, IMESDA, ACTINU, TESTFL, MESPRI, REJECT, REMARK, STATUS, periode.ICANDD AS ICANDD, LIEN.IBIDCF AS IDCOMF, tiers.HXNCON AS HXNCON, DATENV";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPSEREJ left join " + _getCollection()
                + "CPCOFIP demande ON REMEID = demande.IBMEID left join " + _getCollection()
                + "CPLSECOP lien ON REMEID = lien.IBMEID left join " + _getCollection()
                + "CPCOFIP demande1 ON lien.IBIDCF = demande1.IBIDCF left join " + _getCollection()
                + "CPPEFIP periode ON (periode.ICIIFD = demande.ICIIFD ) left join " + _getCollection()
                + "TIPAVSP tiers ON tiers.HTITIE = demande.HTITIE left join " + _getCollection()
                + "CPPEFIP periode1 ON (periode1.ICIIFD = demande1.ICIIFD ) left join " + _getCollection()
                + "TIPAVSP tiers1 ON (tiers1.HTITIE = demande1.HTITIE )  ";

    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "YBREID";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        // Si le canton est renseigné, on va voir si le sender Id contient VD (2-VD-5)
        if (!JadeStringUtil.isEmpty(getForCanton())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" SENDID = \'2-" + getSession().getCode(getForCanton()) + "-5\'");
        }
        // messageId
        if (!JadeStringUtil.isEmpty(getLikeMessageId())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" MESSID like \'%" + getLikeMessageId() + "%\'");
        }
        // referenceMessageId
        if (!JadeStringUtil.isEmpty(getLikeReferenceMessageId())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" REMEID like \'%" + getLikeReferenceMessageId() + "%\'");
        }
        if (getForReferenceMessageId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("REMEID=" + this._dbWriteString(statement.getTransaction(), getForReferenceMessageId()));
        }
        // ourBusinessReferenceId
        if (!JadeStringUtil.isEmpty(getLikeOurBusinessReferenceId())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" OBREID like \'%" + getLikeOurBusinessReferenceId() + "%\'");
        }
        // yourBusinessReferenceId
        if (!JadeStringUtil.isEmpty(getLikeYourBusinessReferenceId())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" YBREID like \'%" + getLikeYourBusinessReferenceId() + "%\'");
        }

        // yourBusinessReferenceId
        if (!JadeStringUtil.isEmpty(getForIdCommunication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" demande1.IBIDCF=" + getForIdCommunication());
        }

        // personId
        if (!JadeStringUtil.isEmpty(getLikePersonId())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" PERSID like \'%" + getLikePersonId() + "%\'");
        }
        // officialName
        if (!JadeStringUtil.isEmpty(getLikeOfficialName())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (upper(rtrim(OFNAME)) like upper(rtrim('" + getLikeOfficialName() + "%')))");
        }
        // firstName
        if (!JadeStringUtil.isEmpty(getLikeFirstName())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (upper(rtrim(FINAME)) like upper(rtrim('" + getLikeFirstName() + "%')))");
        }
        // reasonOfRejection
        if (!JadeStringUtil.isEmpty(getForReasonOfRejection())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" REJECT = " + getForReasonOfRejection());
        }
        // STATUS
        if (!JadeStringUtil.isEmpty(getForEtat())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" STATUS = " + getForEtat());
        }
        // Annee
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (periode.ICANDD = " + getForAnnee() + " OR periode1.ICANDD=" + getForAnnee() + ")");
        }
        // Num Contribuable
        if (!JadeStringUtil.isEmpty(getLikeNumContribuable())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (tiers.HXNCON like \'%" + getLikeNumContribuable() + "%\' OR tiers1.HXNCON like \'%"
                    + getLikeNumContribuable() + "%\')");
        }
        // IdRejets
        if (!JadeStringUtil.isEmpty(getForIdRejets())) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" CPSEID = " + getForIdRejets());
        }

        if (!getWantEnvoye()) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" STATUS <> " + CPRejets.CS_ETAT_ENVOYE);
        }

        if (!getWantAbandonne()) {
            if (!JadeStringUtil.isEmpty(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" STATUS <> " + CPRejets.CS_ETAT_ABANDONNE);
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPRejets();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCanton() {
        return forCanton;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdCommunication() {
        return forIdCommunication;
    }

    public String getForIdRejets() {
        return forIdRejets;
    }

    public String getForReasonOfRejection() {
        return forReasonOfRejection;
    }

    public String getForReferenceMessageId() {
        return forReferenceMessageId;
    }

    public String getLikeFirstName() {
        return likeFirstName;
    }

    public String getLikeMessageId() {
        return likeMessageId;
    }

    public String getLikeNumContribuable() {
        return likeNumContribuable;
    }

    public String getLikeOfficialName() {
        return likeOfficialName;
    }

    public String getLikeOurBusinessReferenceId() {
        return likeOurBusinessReferenceId;
    }

    public String getLikePersonId() {
        return likePersonId;
    }

    public String getLikeReferenceMessageId() {
        return likeReferenceMessageId;
    }

    public String getLikeYourBusinessReferenceId() {
        return likeYourBusinessReferenceId;
    }

    public String getRejetVisible() {
        return getSession().getLabel("REJET_LIBELLE_" + getForReasonOfRejection());
    }

    public Boolean getWantAbandonne() {
        return wantAbandonne;
    }

    public Boolean getWantEnvoye() {
        return wantEnvoye;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdCommunication(String forIdCommunication) {
        this.forIdCommunication = forIdCommunication;
    }

    public void setForIdRejets(String forIdRejets) {
        this.forIdRejets = forIdRejets;
    }

    public void setForReasonOfRejection(String forReasonOfRejection) {
        this.forReasonOfRejection = forReasonOfRejection;
    }

    public void setForReferenceMessageId(String forReferenceMessageId) {
        this.forReferenceMessageId = forReferenceMessageId;
    }

    public void setLikeFirstName(String likeFirstName) {
        this.likeFirstName = likeFirstName;
    }

    public void setLikeMessageId(String likeMessageId) {
        this.likeMessageId = likeMessageId;
    }

    public void setLikeNumContribuable(String likeNumContribuable) {
        this.likeNumContribuable = likeNumContribuable;
    }

    public void setLikeOfficialName(String likeOfficialName) {
        this.likeOfficialName = likeOfficialName;
    }

    public void setLikeOurBusinessReferenceId(String likeOurBusinessReferenceId) {
        this.likeOurBusinessReferenceId = likeOurBusinessReferenceId;
    }

    public void setLikePersonId(String likePersonId) {
        this.likePersonId = likePersonId;
    }

    public void setLikeReferenceMessageId(String likeReferenceMessageId) {
        this.likeReferenceMessageId = likeReferenceMessageId;
    }

    public void setLikeYourBusinessReferenceId(String likeYourBusinessReferenceId) {
        this.likeYourBusinessReferenceId = likeYourBusinessReferenceId;
    }

    public void setWantAbandonne(Boolean wantAbandonne) {
        this.wantAbandonne = wantAbandonne;
    }

    public void setWantEnvoye(Boolean wantEnvoye) {
        this.wantEnvoye = wantEnvoye;
    }
}
