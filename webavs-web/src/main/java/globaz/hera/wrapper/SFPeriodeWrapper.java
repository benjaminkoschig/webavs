/*
 * Cr�� le 14 ao�t 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFPeriode;

/**
 * @author scr
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SFPeriodeWrapper implements ISFPeriode {

    private String dateDebut = "";
    private String dateFin = "";
    private String idDetenteurBTE = "";
    private String noAvs = "";
    private String noAvsDetenteurBTE = "";
    private String csTypeDeDetenteur;
    private String pays = "";
    private String type = "";
    private String idRecueillant = "";

    /**
     * @return
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    @Override
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    @Override
    public String getIdDetenteurBTE() {
        return idDetenteurBTE;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvs() {
        return noAvs;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsDetenteurBTE() {
        return noAvsDetenteurBTE;
    }

    /**
     * @return
     */
    @Override
    public String getPays() {
        return pays;
    }

    /**
     * @return
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdDetenteurBTE(String string) {
        idDetenteurBTE = string;
    }

    /**
     * @param string
     */
    public void setNoAvs(String string) {
        noAvs = string;
    }

    /**
     * @param string
     */
    public void setNoAvsDetenteurBTE(String string) {
        noAvsDetenteurBTE = string;
    }

    /**
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    @Override
    public String getCsTypeDeDetenteur() {
        return csTypeDeDetenteur;
    }

    public void setCsTypeDeDetenteur(String csTypeDeDetenteur) {
        this.csTypeDeDetenteur = csTypeDeDetenteur;
    }

    @Override
    public String getNoAvsRecueillant() {
        return idRecueillant;
    }

    public void setIdRecueillant(String idRecueillant) {
        this.idRecueillant = idRecueillant;
    }
}
