/*
 * Créé le 14 févr. 07
 */

package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.naos.process.AFListeControlesAEffectuerProcess;

/**
 * @author hpe
 * 
 */

public class AFControlesAEffectuerViewBean extends AFListeControlesAEffectuerProcess implements FWViewBeanInterface,
        BIPersistentObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String annee = new String();

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String genreControle = new String();
    private Boolean isAvecReviseur = null;
    private String masseSalA = new String();
    private String masseSalDe = new String();

    public AFControlesAEffectuerViewBean() throws java.lang.Exception {
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

    /**
     * @return
     */
    @Override
    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    @Override
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    @Override
    public String getMasseSalDe() {
        return masseSalDe;
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
     * @param boolean1
     */
    @Override
    public void setIsAvecReviseur(Boolean boolean1) {
        isAvecReviseur = boolean1;
    }

    /**
     * @param string
     */
    @Override
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    @Override
    public void setMasseSalDe(String string) {
        masseSalDe = string;
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
