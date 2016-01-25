package globaz.tucana.vb.administration;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Classe g�n�rique pour lancer les process bouclement
 * 
 * @author fgo date de cr�ation : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUPassageSuppressionViewBean extends TUAbstractPersitentObject {
    private String csApplication = "";
    private String csSuppressionReferenceAF = "";
    private String eMail = "";
    private String noPassage = "";

    /**
     * Constructeur
     */
    public TUPassageSuppressionViewBean() {
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
     * R�cup�ration du code syst�me application
     * 
     * @return
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Retourne le fait de supprimer les r�f�rences AF
     * 
     * @return
     */
    public String getCsSuppressionReferenceAF() {
        return csSuppressionReferenceAF;
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
     * R�cup�re le num�ro de passage
     * 
     * @return
     */
    public String getNoPassage() {
        return noPassage;
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
     * Modification de l'application
     * 
     * @param string
     */
    public void setCsApplication(String string) {
        csApplication = string;
    }

    /**
     * Modifie le fait de supprimer ou non les r�f�rences AF
     * 
     * @param newCsSuppressionReferenceAF
     */
    public void setCsSuppressionReferenceAF(String newCsSuppressionReferenceAF) {
        csSuppressionReferenceAF = newCsSuppressionReferenceAF;
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
     * Modifie le num�ro de passage
     * 
     * @param newNoPassage
     */
    public void setNoPassage(String newNoPassage) {
        noPassage = newNoPassage;
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
