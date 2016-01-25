/*
 * Créé le 12 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.naos.process.AFListeControlesAttribuesProcess;

/**
 * Ce ViewBean va lancer un process pour imprimer les contrôles attribués (contrôle employeurs)
 * 
 * @author: hpe
 */

public class AFControlesAttribuesViewBean extends AFListeControlesAttribuesProcess implements FWViewBeanInterface,
        BIPersistentObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String annee = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String genreControle = "";
    private String selectionGroupe = "";
    private String visaReviseur = "";

    public AFControlesAttribuesViewBean() throws java.lang.Exception {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {

    }

    /**
     * @return
     */
    @Override
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    @Override
    public String getGenreControle() {
        return genreControle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    public AFReviseurManager getReviseursList() {

        AFReviseurManager reviseurMan = new AFReviseurManager();
        reviseurMan.setSession(getSession());

        try {

            reviseurMan.find(getTransaction());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviseurMan;

    }

    /**
     * @return
     */
    @Override
    public String getSelectionGroupe() {
        return selectionGroupe;
    }

    /**
     * @return
     */
    @Override
    public String getVisaReviseur() {
        return visaReviseur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    /**
     * @param string
     */
    @Override
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    @Override
    public void setGenreControle(String string) {
        genreControle = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {

    }

    /**
     * @param string
     */
    @Override
    public void setSelectionGroupe(String string) {
        selectionGroupe = string;
    }

    /**
     * @param string
     */
    @Override
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

    }

}
