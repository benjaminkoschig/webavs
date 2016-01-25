/*
 * Créé le 27 oct. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.helpers.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.vb.famille.SFApercuRelationConjointViewBean;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFApercuRelationConjointHelper extends FWHelper {

    // La relation est garder pour faire remonter les erreurs dans le after
    // retrieve
    private SFRelationConjoint relation = null;

    /**
	 * 
	 */
    public SFApercuRelationConjointHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession) Ne supprime que la relation
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        SFRelationConjoint relation = getRelationConjoint(viewBean, session);
        relation.delete();

        // Copie les éventuelles erreurs
        if (relation.hasErrors()) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(relation.getErrors().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession) Ne modifie que la relation
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        SFRelationConjoint relation = getRelationConjoint(viewBean, session);
        relation.update();

        // Copie les éventuelles erreurs
        if (relation.hasErrors()) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(relation.getErrors().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.IFWHelper#afterExecute(globaz.framework.bean .FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    public void afterExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super.afterExecute(viewBean, action, session);
        // fait remonter les erreurs de la relation sur le viewBean
        if (relation != null && relation.hasErrors()) {
            viewBean.setMsgType(relation.getMsgType());
            viewBean.setMessage(relation.getMessage());
        }

    }

    // renvoie la relationConjoint à partir de l'apercu
    private SFRelationConjoint getRelationConjoint(FWViewBeanInterface viewBean, BISession session) throws Exception {
        SFApercuRelationConjointViewBean vb = (SFApercuRelationConjointViewBean) viewBean;
        relation = new SFRelationConjoint();
        relation.setISession(session);
        relation.setIdRelationConjoint(vb.getIdRelationConjoint());
        relation.retrieve();
        relation.setDateDebut(vb.getDateDebut());
        relation.setDateFin(vb.getDateFin());
        relation.setTypeRelation(vb.getTypeRelation());
        return relation;
    }

}
