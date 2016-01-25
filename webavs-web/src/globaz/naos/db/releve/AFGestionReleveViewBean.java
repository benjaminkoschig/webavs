/*
 * Créé le 19 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.releve;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;

/**
 * ViewBean permettant de démarrer le process de gestion des relevés
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFGestionReleveViewBean implements BIPersistentObject, FWViewBeanInterface {

    // Renvoie l'adresse e-mail où envoyer les résultats
    private String email = "";
    // champs transmis au process
    // Permet de savoir si les rappels doivent être générés
    private Boolean genererRappel = new Boolean(false);
    // Permet de savoir si les sommations doivent être générées
    private Boolean genererSommation = new Boolean(false);

    // Permet de savoir si les taxations d'office doivent être générées
    private Boolean genererTaxation = new Boolean(false);
    // champs necessaires a l'implementation des interfaces
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    // Renvoie la période à analyser
    private String periode = "";
    private BISession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public AFGestionReleveViewBean() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        try {
            return getSession().getUserEMail();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * @return
     */
    public Boolean getGenererRappel() {
        return genererRappel;
    }

    /**
     * @return
     */
    public Boolean getGenererSommation() {
        return genererSommation;
    }

    /**
     * @return
     */
    public Boolean getGenererTaxation() {
        return genererTaxation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * @return
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * getter pour l'attribut session
     * 
     * @return la valeur courante de l'attribut session
     */
    public BISession getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // TODO Raccord de méthode auto-généré

    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * setter pour l'attribut session
     * 
     * @param session
     *            une nouvelle valeur pour cet attribut
     */

    /**
     * @param boolean1
     */
    public void setGenererRappel(Boolean boolean1) {
        genererRappel = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGenererSommation(Boolean boolean1) {
        genererSommation = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGenererTaxation(Boolean boolean1) {
        genererTaxation = boolean1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // TODO Raccord de méthode auto-généré

    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @param string
     */
    public void setPeriode(String string) {
        periode = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Raccord de méthode auto-généré

    }

}
