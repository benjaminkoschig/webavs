package globaz.tucana.vb.transfert;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Viewbean non attach� � un fichier DB et donc pas rattach� � un BEntity Permettant l'exportation d'un bouclement et de
 * ses d�tails
 * 
 * @author fgo date de cr�ation : 25.08.2006
 * @version : version 1.0
 * 
 */
public class TUExportViewBean extends TUAbstractPersitentObject {
    private String annee = new String();
    private String eMail = new String();
    private String idBouclement = new String();
    private String mois = new String();

    /**
     * Constructeur
     */
    public TUExportViewBean() {
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
     * R�cup�re l'ann�e
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * R�cup�re l'adresse e-mail
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
        return "";
    }

    /**
     * R�cup�ration de l'id de bouclement
     * 
     * @return
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * R�cup�re le mois
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
        return new BSpy((BSession) getISession());
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
     * Modifie l'ann�e
     * 
     * @param newAnnee
     */
    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    /**
     * Modifie l'adresse e-mail
     * 
     * @param newEMail
     */
    public void setEMail(String newEMail) {
        eMail = newEMail;
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
     * Modification de l'id de bouclement
     * 
     * @param idBouclement
     */
    public void setIdBouclement(String idBouclement) {
        this.idBouclement = idBouclement;
    }

    /**
     * Modifie le mois
     * 
     * @param newMois
     */
    public void setMois(String newMois) {
        mois = newMois;
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
