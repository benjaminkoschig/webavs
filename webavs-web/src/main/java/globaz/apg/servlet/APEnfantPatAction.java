/*
 * Créé le 20 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APEnfantPatViewBean;
import globaz.apg.vb.droits.APTypePresationDemandeResolver;
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
public class APEnfantPatAction extends APAbstractDroitDTOAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnfantPatAction.
     *
     * @param servlet
     */
    public APEnfantPatAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see APAbstractDroitDTOAction#getViewBeanClass()
     */
    @Override
    protected Class getViewBeanClass() {
        return APEnfantPatViewBean.class;
    }

    /**
     * @see APAbstractDroitDTOAction#initViewBean(FWViewBeanInterface,
     *      APDroitDTO, HttpSession)
     */
    @Override
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {
        ((APEnfantPatViewBean) viewBean).setDroitDTO(droitDTO);
        ((APEnfantPatViewBean) viewBean).setTypeDemande(APTypePresationDemandeResolver.resolveEnumTypePrestation(session));

        return viewBean;
    }
}
