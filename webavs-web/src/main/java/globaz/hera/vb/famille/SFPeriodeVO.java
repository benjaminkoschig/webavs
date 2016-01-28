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
     * Retourne le type énuméré correspondant au code système 'TypeDeDetenteur'
     * 
     * @see TypeDeDetenteur
     * @return le énuméré correspondant au code système 'TypeDeDetenteur' ou null si 'csTypeDeDetenteur' n'est pas
     *         renseigné
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
     * Retourne le code système du type de détenteur
     * 
     * @return le code système du type de détenteur
     */
    public String getCsTypeDeDetenteur() {
        return csTypeDeDetenteur;
    }

    /**
     * Définit le code système du type de détenteur
     * 
     * @param csTypeDeDetenteur
     */
    public void setCsTypeDeDetenteur(String csTypeDeDetenteur) {
        this.csTypeDeDetenteur = csTypeDeDetenteur;
    }

}
