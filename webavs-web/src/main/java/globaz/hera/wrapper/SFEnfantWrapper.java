/*
 * Cr�� le 14 ao�t 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFEnfant;

/**
 * @author scr
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SFEnfantWrapper implements ISFEnfant {

    private String dateAdoption = "";
    private boolean isRecueilli = false;
    private String noAvsMere = "";
    private String noAvsPere = "";
    private String nomMere = "";
    private String nomPere = "";
    private String nss = "";
    private String prenomMere = "";
    private String prenomPere = "";
    private String dateNaissance = "";
    private String dateNaissanceMere = "";
    private String dateNaissancePere = "";

    /**
     * @return
     */
    @Override
    public String getDateAdoption() {
        return dateAdoption;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsMere() {
        return noAvsMere;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsPere() {
        return noAvsPere;
    }

    /**
     * @return
     */
    @Override
    public String getNomMere() {
        return nomMere;
    }

    /**
     * @return
     */
    @Override
    public String getNomPere() {
        return nomPere;
    }

    /**
     * @return
     */
    @Override
    public String getNss() {
        return nss;
    }

    /**
     * @return
     */
    @Override
    public String getPrenomMere() {
        return prenomMere;
    }

    /**
     * @return
     */
    @Override
    public String getPrenomPere() {
        return prenomPere;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDateNaissance(){return dateNaissance;}

    /**
     *
     * @return
     */
    @Override
    public String getDateNaissancePere(){return dateNaissancePere;}

    /**
     *
     * @return
     */
    @Override
    public String getDateNaissanceMere(){return dateNaissanceMere;}

    /**
     * @return
     */
    @Override
    public boolean isRecueilli() {
        return isRecueilli;
    }

    /**
     * @param string
     */
    public void setDateAdoption(String string) {
        dateAdoption = string;
    }

    /**
     * @param string
     */
    public void setNoAvsMere(String string) {
        noAvsMere = string;
    }

    /**
     * @param string
     */
    public void setNoAvsPere(String string) {
        noAvsPere = string;
    }

    /**
     * @param string
     */
    public void setNomMere(String string) {
        nomMere = string;
    }

    /**
     * @param string
     */
    public void setNomPere(String string) {
        nomPere = string;
    }

    /**
     * @param string
     */
    public void setNss(String string) {
        nss = string;
    }

    /**
     * @param string
     */
    public void setPrenomMere(String string) {
        prenomMere = string;
    }

    /**
     * @param string
     */
    public void setPrenomPere(String string) {
        prenomPere = string;
    }

    /**
     * @param b
     */
    public void setRecueilli(boolean b) {
        isRecueilli = b;
    }

    public void setDateNaissance(String date){dateNaissance = date;}
    public void setDateNaissanceMere(String date){dateNaissanceMere = date;}
    public void setDateNaissancePere(String date){dateNaissancePere = date;}
}
