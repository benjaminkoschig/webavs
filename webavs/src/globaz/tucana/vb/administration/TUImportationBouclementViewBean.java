package globaz.tucana.vb.administration;

import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Classe viewBean pour lancement de l'importation des bouclements
 * 
 * @author fgo date de création : 10 juil. 06
 * @version : version 1.0
 * 
 */
public class TUImportationBouclementViewBean extends TUAbstractPersitentObject {
    private String annee = "";

    private String csApplication = "";

    private String eMail = "";

    private String mois = "";

    /**
	 * 
	 */
    public TUImportationBouclementViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#delete()
     */
    @Override
    public void delete() throws Exception {

    }

    /**
     * Récupération de l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupération du code système application
     * 
     * @return
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Récupération de l'adresse e-mail
     * 
     * @return
     */
    public String getEMail() {
        return eMail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * Récupération du mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    /**
     * Modification de l'année
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modification de l'application
     * 
     * @param string
     */
    public void setCsApplication(String string) {
        csApplication = string;
    }

    /**
     * Modification de l'adresse e-mail
     * 
     * @param string
     */
    public void setEMail(String string) {
        eMail = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {

    }

    /**
     * Modification du mois
     * 
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#update()
     */
    @Override
    public void update() throws Exception {

    }
}