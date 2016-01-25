/*
 * Créé le 10 oct. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.helpers.dossier;

import globaz.cepheus.db.intervenant.DOIntervenant;
import globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOMetaDossierJointIntervenantsHelper extends PRAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_add(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // recuperation des data
        DOMetaDossierJointIntervenantsViewBean mdiViewBean = (DOMetaDossierJointIntervenantsViewBean) viewBean;

        // creation de l'intervenant
        DOIntervenant intervenant = new DOIntervenant();
        intervenant.setSession((BSession) session);
        intervenant.setIdMetaDossier(mdiViewBean.getIdMetaDossier());
        intervenant.setCsDescription(mdiViewBean.getCsDescription());
        intervenant.setIdTiersIntervenant(mdiViewBean.getIdTiersIntervenant());
        intervenant.setDateDebut(mdiViewBean.getDateDebut());
        intervenant.setDateFin(mdiViewBean.getDateFin());

        intervenant.add();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // recuperation des data
        DOMetaDossierJointIntervenantsViewBean mdiViewBean = (DOMetaDossierJointIntervenantsViewBean) viewBean;

        // mise a jour de l'intervenant
        DOIntervenant intervenant = new DOIntervenant();
        intervenant.setSession((BSession) session);
        intervenant.setIdIntervenant(mdiViewBean.getIdIntervenant());
        intervenant.retrieve();

        intervenant.setCsDescription(mdiViewBean.getCsDescription());
        intervenant.setIdTiersIntervenant(mdiViewBean.getIdTiersIntervenant());
        intervenant.setDateDebut(mdiViewBean.getDateDebut());
        intervenant.setDateFin(mdiViewBean.getDateFin());

        intervenant.update();
    }

}
