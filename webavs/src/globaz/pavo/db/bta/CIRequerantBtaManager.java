package globaz.pavo.db.bta;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CIRequerantBtaManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = new String();
    private String forDateFin = new String();
    private String forDateInscriptionRetroFlag = new String();
    private String forDateNaissanceCadet = new String();
    private Boolean forHasEnfant = new Boolean(false);
    private String forIdConjointRequerant = new String();
    private String forIdDossierBta = new String();
    private String forIdRequerant = new String();
    private String forIdTiersRequerant = new String();
    private Boolean forIsMenageCommun = new Boolean(false);
    private String forLienParente = new String();
    private String fromDateDebut = new String();
    private String untilDateDebut = new String();

    @Override
    protected String _getFrom(BStatement statement) {
        // jointure pour avoir également accès aux informations du tiers
        String from = _getCollection() + "CIBTARP bta";
        from += " inner join " + _getCollection() + "TIPAVSP avs on bta.HTITIE=avs.HTITIE";
        from += " inner join " + _getCollection() + "TITIERP tiers on bta.HTITIE=tiers.HTITIE";
        from += " inner join " + _getCollection() + "TIPERSP pers on bta.HTITIE=pers.HTITIE";

        return from;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdRequerant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDRE=" + _dbWriteNumeric(statement.getTransaction(), getForIdRequerant());
        }
        // traitement du positionnement
        if (getForIdTiersRequerant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTiersRequerant());
        }
        // traitement du positionnement
        if (getForIdDossierBta().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDD=" + _dbWriteNumeric(statement.getTransaction(), getForIdDossierBta());
        }
        // traitement du positionnement
        if (getForDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATD=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }
        // traitement du positionnement
        if (getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATD>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }
        // traitement du positionnement
        if (getUntilDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATD<=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateDebut());
        }
        // traitement du positionnement
        if (getForDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATF=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }
        // traitement du positionnement
        if (getForHasEnfant().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDENFA=" + _dbWriteBoolean(statement.getTransaction(), getForHasEnfant());
        }
        // traitement du positionnement
        if (getForDateNaissanceCadet().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATC=" + _dbWriteNumeric(statement.getTransaction(), getForDateNaissanceCadet());
        }
        // traitement du positionnement
        if (getForLienParente().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDPARE=" + _dbWriteNumeric(statement.getTransaction(), getForLienParente());
        }
        // traitement du positionnement
        if (getForIsMenageCommun().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDMENA=" + _dbWriteBoolean(statement.getTransaction(), getForIsMenageCommun());
        }
        // traitement du positionnement
        if (getForIdConjointRequerant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDCO=" + _dbWriteNumeric(statement.getTransaction(), getForIdConjointRequerant());
        }
        if (getForDateInscriptionRetroFlag().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForDateInscriptionRetroFlag().equals("nullOrZero")) {
                sqlWhere += "(KDREFL IS NULL OR KDREFL=0) ";
            } else {
                sqlWhere += "KDREFL=" + _dbWriteNumeric(statement.getTransaction(), getForDateInscriptionRetroFlag());
            }

        }

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIRequerantBta();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateInscriptionRetroFlag() {
        return forDateInscriptionRetroFlag;
    }

    public String getForDateNaissanceCadet() {
        return forDateNaissanceCadet;
    }

    public Boolean getForHasEnfant() {
        return forHasEnfant;
    }

    public String getForIdConjointRequerant() {
        return forIdConjointRequerant;
    }

    public String getForIdDossierBta() {
        return forIdDossierBta;
    }

    public String getForIdRequerant() {
        return forIdRequerant;
    }

    public String getForIdTiersRequerant() {
        return forIdTiersRequerant;
    }

    public Boolean getForIsMenageCommun() {
        return forIsMenageCommun;
    }

    public String getForLienParente() {
        return forLienParente;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getUntilDateDebut() {
        return untilDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * si la valeur passée en paramètre est la chaine "nullOrZero" on recherche que les requerants non flagés
     * 
     * @param forDateInscriptionRetroFlag
     */
    public void setForDateInscriptionRetroFlag(String forDateInscriptionRetroFlag) {
        this.forDateInscriptionRetroFlag = forDateInscriptionRetroFlag;
    }

    public void setForDateNaissanceCadet(String forDateNaissanceCadet) {
        this.forDateNaissanceCadet = forDateNaissanceCadet;
    }

    public void setForHasEnfant(Boolean forHasEnfant) {
        this.forHasEnfant = forHasEnfant;
    }

    public void setForIdConjointRequerant(String forIdConjointRequerant) {
        this.forIdConjointRequerant = forIdConjointRequerant;
    }

    public void setForIdDossierBta(String forIdDossierBta) {
        this.forIdDossierBta = forIdDossierBta;
    }

    public void setForIdRequerant(String forIdRequerant) {
        this.forIdRequerant = forIdRequerant;
    }

    public void setForIdTiersRequerant(String forIdTiersRequerant) {
        this.forIdTiersRequerant = forIdTiersRequerant;
    }

    public void setForIsMenageCommun(Boolean forIsMenageCommun) {
        this.forIsMenageCommun = forIsMenageCommun;
    }

    public void setForLienParente(String forLienParente) {
        this.forLienParente = forLienParente;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setUntilDateDebut(String untilDateDebut) {
        this.untilDateDebut = untilDateDebut;
    }

}
