package globaz.prestation.clone.factory;

import globaz.globall.db.BSession;

/**
 * DOCUMENT ME! Description
 * 
 * @author scr
 * 
 */
public interface IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    int ACTION_CREER_NOUVEAU_DROIT_APG_FILS = 1002;
    int ACTION_CREER_NOUVEAU_DROIT_APG_RESTI_FILS = 1004;
    int ACTION_CREER_NOUVEAU_DROIT_PATERNITE_RESTI_FILS = 1005;

    int ACTION_CREER_NOUVEAU_DROIT_PANDEMIE_RESTI_FILS = 1003;

    /** DOCUMENT ME! */
    int ACTION_CREER_NOUVEAU_DROIT_APG_PARENT = 1001;
    int ACTION_CREER_NOUVEAU_DROIT_PROCHE_AIDANT_RESTI = 1006;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public IPRCloneable duplicate(int action) throws Exception;

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    public String getUniquePrimaryKey();

    /**
     * setter pour l'attribut session
     * 
     * @param session
     *            une nouvelle valeur pour cet attribut
     */
    public void setSession(BSession session);

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    public void setUniquePrimaryKey(String pk);
}
