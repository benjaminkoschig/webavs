/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.cat;

import globaz.babel.db.cat.CTTexteManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTTexteManager
 */
public class CTTexteListViewBean extends CTTexteManager implements FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String borneInferieure = "0";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTTexteViewBean();
    }

    /**
     * getter pour l'attribut borne inferieure
     * 
     * @return la valeur courante de l'attribut borne inferieure
     */
    public String getBorneInferieure() {
        return borneInferieure;
    }

    /**
     * setter pour l'attribut borne inferieure
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setBorneInferieure(String string) {
        borneInferieure = string;
    }
}
