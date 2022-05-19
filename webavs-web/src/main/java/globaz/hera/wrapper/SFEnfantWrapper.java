/*
 * Créé le 14 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFEnfant;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFEnfantWrapper implements ISFEnfant {

    private String dateAdoption = "";
    private boolean isRecueilli = false;
    private String noAvsParent2 = "";
    private String noAvsParent1 = "";
    private String nomParent2 = "";
    private String nomParent1 = "";
    private String nss = "";
    private String prenomParent2 = "";
    private String prenomParent1 = "";
    private String dateNaissance = "";
    private String dateNaissanceParent2 = "";
    private String dateNaissanceParent1 = "";
    private String csSexeParent2 = "";
    private String csSexeParent1 = "";

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
    public String getNoAvsParent2() {
        return noAvsParent2;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsParent1() {
        return noAvsParent1;
    }

    /**
     * @return
     */
    @Override
    public String getNomParent2() {
        return nomParent2;
    }

    /**
     * @return
     */
    @Override
    public String getNomParent1() {
        return nomParent1;
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
    public String getPrenomParent2() {
        return prenomParent2;
    }

    /**
     * @return
     */
    @Override
    public String getPrenomParent1() {
        return prenomParent1;
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
    public String getDateNaissanceParent1(){return dateNaissanceParent1;}

    /**
     *
     * @return
     */
    @Override
    public String getDateNaissanceParent2(){return dateNaissanceParent2;}

    @Override
    public String getSexeParent1() {
        return csSexeParent1;
    }

    @Override
    public String getSexeParent2() {
        return csSexeParent2;
    }

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
    public void setNoAvsParent2(String string) {
        noAvsParent2 = string;
    }

    /**
     * @param string
     */
    public void setNoAvsParent1(String string) {
        noAvsParent1 = string;
    }

    /**
     * @param string
     */
    public void setNomParent2(String string) {
        nomParent2 = string;
    }

    /**
     * @param string
     */
    public void setNomParent1(String string) {
        nomParent1 = string;
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
    public void setPrenomParent2(String string) {
        prenomParent2 = string;
    }

    /**
     * @param string
     */
    public void setPrenomParent1(String string) {
        prenomParent1 = string;
    }

    /**
     * @param b
     */
    public void setRecueilli(boolean b) {
        isRecueilli = b;
    }

    public void setDateNaissance(String date){dateNaissance = date;}

    public void setDateNaissanceParent2(String date){
        dateNaissanceParent2 = date;}

    public void setDateNaissanceParent1(String date){
        dateNaissanceParent1 = date;}

    public void setCsSexeParent2(String csSexeParent2) {
        this.csSexeParent2 = csSexeParent2;
    }

    public void setCsSexeParent1(String csSexeParent1) {
        this.csSexeParent1 = csSexeParent1;
    }
}
