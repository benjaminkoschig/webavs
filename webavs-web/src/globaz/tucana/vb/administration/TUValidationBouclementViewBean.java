package globaz.tucana.vb.administration;

import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Classe viewBean pour lancement du process de validation
 * 
 * @author fgo date de création : 14.09.06
 * @version : version 1.0
 * 
 */
public class TUValidationBouclementViewBean extends TUAbstractPersitentObject {
    private String annee = "";
    private String eMail = "";
    private String idBouclement = "";

    private String mois = "";

    /**
	 * 
	 */
    public TUValidationBouclementViewBean() {
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
     * Récupère l'id du bouclement
     * 
     * @return
     */
    public String getIdBouclement() {
        return idBouclement;
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
     * Modifie l'id du bouclement
     * 
     * @param idBouclement
     */
    public void setIdBouclement(String idBouclement) {
        this.idBouclement = idBouclement;
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