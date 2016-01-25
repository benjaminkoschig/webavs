/*
 * Créé le 20 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APEnfantMatViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APEnfantMatAction extends APAbstractDroitDTOAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnfantMatAction.
     * 
     * @param servlet
     */
    public APEnfantMatAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.apg.servlet.APAbstractDroitDTOAction#getViewBeanClass()
     */
    @Override
    protected Class getViewBeanClass() {
        return APEnfantMatViewBean.class;
    }

    /**
     * @see globaz.apg.servlet.APAbstractDroitDTOAction#initViewBean(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.apg.vb.droits.APDroitDTO, javax.servlet.http.HttpSession)
     */
    @Override
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {
        ((APEnfantMatViewBean) viewBean).setDroitDTO(droitDTO);

        return viewBean;
    }
}
