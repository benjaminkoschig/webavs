package globaz.tucana.vb.list;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.tucana.vb.TUAbstractPersitentObject;

/**
 * Viewbean non attaché à un fichier DB et donc pas rattaché à un BEntity
 * 
 * @author fgo date de création : 5 juil. 06
 * @version : version 1.0
 * 
 */
public class TUAffiliationProblemListViewBean extends TUAbstractPersitentObject {
    private String annee = new String();
    private String eMail = new String();
    private String genreAssu = new String();
    private String idRubrique = new String();
    private String mois = new String();

    /**
     * Constructeur
     */
    public TUAffiliationProblemListViewBean() {
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
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupère l'adresse e-mail
     * 
     * @return
     */
    public String getEMail() {
        return eMail;
    }

    public String getGenreAssu() {
        return genreAssu;
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
     * Récupère le numéro de rubrique
     * 
     * @return
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Récupère le mois
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
     * Modifie l'année
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

    public void setGenreAssu(String genreAssu) {
        this.genreAssu = genreAssu;
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
     * Modifie le numéro de rubrique
     * 
     * @param newRubrique
     */
    public void setIdRubrique(String newRubrique) {
        idRubrique = newRubrique;
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
