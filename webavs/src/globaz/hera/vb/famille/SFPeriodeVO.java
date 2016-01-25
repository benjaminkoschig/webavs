package globaz.hera.vb.famille;

import globaz.hera.enums.TypeDeDetenteur;
import globaz.jade.client.util.JadeStringUtil;

public class SFPeriodeVO {

    private String dateDebut = "";
    private String dateFin = "";
    private String detenteurBTE = "";
    private String csTypeDeDetenteur = "";
    private String libellePeriode = "";
    private String pays = "";

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDetenteurBTE() {
        if (!JadeStringUtil.isBlankOrZero(detenteurBTE)) {
            return detenteurBTE;
        } else {
            return "";
        }
    }

    public String getLibellePeriode() {
        return libellePeriode;
    }

    public String getPays() {
        if (!JadeStringUtil.isBlankOrZero(pays)) {
            return pays;
        } else {
            return "";
        }
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDetenteurBTE(String detenteurBTE) {
        this.detenteurBTE = detenteurBTE;
    }

    public void setLibellePeriode(String libellePeriode) {
        this.libellePeriode = libellePeriode;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Retourne le type �num�r� correspondant au code syst�me 'TypeDeDetenteur'
     * 
     * @see TypeDeDetenteur
     * @return le �num�r� correspondant au code syst�me 'TypeDeDetenteur' ou null si 'csTypeDeDetenteur' n'est pas
     *         renseign�
     */
    public TypeDeDetenteur getTypeDeDetenteur() {
        for (TypeDeDetenteur type : TypeDeDetenteur.values()) {
            if (type.getCodeSystemAsString().equals(csTypeDeDetenteur)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Retourne le code syst�me du type de d�tenteur
     * 
     * @return le code syst�me du type de d�tenteur
     */
    public String getCsTypeDeDetenteur() {
        return csTypeDeDetenteur;
    }

    /**
     * D�finit le code syst�me du type de d�tenteur
     * 
     * @param csTypeDeDetenteur
     */
    public void setCsTypeDeDetenteur(String csTypeDeDetenteur) {
        this.csTypeDeDetenteur = csTypeDeDetenteur;
    }

}
