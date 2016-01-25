/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.helpers.cat;

import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTElementManager;
import globaz.babel.vb.cat.CTTexteSaisieViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTTexteSaisieHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._init(viewBean, action, session);

        CTTexteSaisieViewBean texteSaisieVB = ((CTTexteSaisieViewBean) viewBean);

        // mise a jour des champs isScalable et description de element de meme
        // niveau
        CTElementManager manager = new CTElementManager();
        manager.setSession((BSession) session);
        manager.setForIdDocument(texteSaisieVB.getIdDocument());
        manager.setForNiveau(texteSaisieVB.getNouveauNiveau());
        manager.find(1);

        CTElement e = null;
        if (manager.size() > 0) {
            e = (CTElement) manager.get(0);
            texteSaisieVB.setIsSelectable(e.getIsSelectable());
            texteSaisieVB.setDescription(e.getDescription());
        } else {
            texteSaisieVB.setIsSelectable(Boolean.FALSE);
            texteSaisieVB.setDescription("");
        }

        texteSaisieVB.setNiveau(texteSaisieVB.getNouveauNiveau());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._retrieve(viewBean, action, session);

        CTTexteSaisieViewBean texteSaisieVB = ((CTTexteSaisieViewBean) viewBean);
        // si on change le niveau de l'element
        if (!JadeStringUtil.isEmpty(texteSaisieVB.getNouveauNiveau())
                && !texteSaisieVB.getNouveauNiveau().equals(texteSaisieVB.getNiveau())) {

            // mise a jour des champs isScalable et description de element de
            // meme niveau
            CTElementManager manager = new CTElementManager();
            manager.setSession((BSession) session);
            manager.setForIdDocument(texteSaisieVB.getIdDocument());
            manager.setForNiveau(texteSaisieVB.getNouveauNiveau());
            manager.find(1);

            CTElement e = null;
            if (manager.size() > 0) {
                e = (CTElement) manager.get(0);
                texteSaisieVB.setIsSelectable(e.getIsSelectable());
                texteSaisieVB.setDescription(e.getDescription());
            } else {
                texteSaisieVB.setIsSelectable(Boolean.FALSE);
                texteSaisieVB.setDescription("");
            }

            texteSaisieVB.setNiveau(texteSaisieVB.getNouveauNiveau());
        }
    }

}
