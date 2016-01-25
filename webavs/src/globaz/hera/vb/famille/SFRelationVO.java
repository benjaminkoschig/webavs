package globaz.hera.vb.famille;

import globaz.hera.api.ISFSituationFamiliale;

public class SFRelationVO {

    private String csTypeRelation = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String idRelation = "";

    public String getCsTypeRelation() {
        return csTypeRelation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public String getLibelleRelation(long size) {
        if (size < SFVueGlobaleViewBean.MIN_RELATION_SIZE) {
            if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(csTypeRelation)) {
                return "M";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(csTypeRelation)) {
                return "D";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(csTypeRelation)) {
                return "E";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(csTypeRelation)) {
                return "I";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(csTypeRelation)) {
                return "S";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(csTypeRelation)) {
                return "S";
            } else {
                return "?";
            }
        } else {
            if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equals(csTypeRelation)) {
                return "MAR/LPa";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equals(csTypeRelation)) {
                return "DIVORCE";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(csTypeRelation)) {
                return "ENFANT";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(csTypeRelation)) {
                return "INDEFIN";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(csTypeRelation)) {
                return "SEP FAI";
            } else if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equals(csTypeRelation)) {
                return "SEP JUD";
            } else {
                return "?????";
            }
        }

    }

    public void setCsTypeRelation(String csTypeRelation) {
        this.csTypeRelation = csTypeRelation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

}
