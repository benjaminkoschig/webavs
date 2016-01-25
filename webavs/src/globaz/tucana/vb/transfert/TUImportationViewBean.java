package globaz.tucana.vb.transfert;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Viewbean non attach� � un fichier DB et donc pas rattach� � un BEntity Permettant l'importation d'un bouclement et de
 * ses d�tails
 * 
 * @author fgo date de cr�ation : 25.08.2006
 * @version : version 1.0
 * 
 */
public class TUImportationViewBean extends TUAbstractPersitentObject {

    private String eMailAdress = new String();

    private String fileName = new String();

    /**
     * Constructeur
     */
    public TUImportationViewBean() {
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
     * R�cup�re l'adresse e-mail
     * 
     * @return
     */
    public String getEMailAdress() {
        return eMailAdress;
    }

    /**
     * R�cup�re la fileName
     * 
     * @return
     */
    public String getFileName() {
        return fileName;
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
     * Modifie l'adresse e-mail
     * 
     * @param newEMail
     */
    public void setEMailAdress(String newEMail) {
        eMailAdress = newEMail;
    }

    /**
     * Modifie la fileName
     * 
     * @param newSource
     */
    public void setFileName(String newSource) {
        fileName = newSource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {

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
