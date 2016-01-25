package globaz.al.helpers.adi;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.adi.ALSaisieAdiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié au viewBean ALSaisieViewBean
 * 
 * @author GMO
 * 
 */
public class ALSaisieAdiHelper extends ALAbstractHelper {
    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof ALSaisieAdiViewBean) {

            // on charge le décompte pour le saisie, avec l'id définie depuis
            // ALActionAdi#beforeNouveau
            ((ALSaisieAdiViewBean) viewBean)
                    .setDecompteModel(ALServiceLocator.getDecompteAdiModelService().read(
                            ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                                    .getIdDecompteAdi()));

            // on charge les détails de prestations de travail relatifs au
            // décompte
            ((ALSaisieAdiViewBean) viewBean).setPrestationComplexSearchModel(ALServiceLocator
                    .getDecompteAdiBusinessService().getPrestationsTravailDossier(
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getIdDossier(),
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getPeriodeDebut(),
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getPeriodeFin()));

            AdiSaisieComplexSearchModel adiSaisieComplexSearchModel = new AdiSaisieComplexSearchModel();
            adiSaisieComplexSearchModel
                    .setForIdDecompteAdi(((ALSaisieAdiViewBean) viewBean).getDecompteModel().getId());
            // lancement de la recherche des saisie adi existantes pour ce
            // décompte
            ((ALSaisieAdiViewBean) viewBean).setSaisieComplexSearchModel(ALServiceLocator
                    .getAdiSaisieComplexModelService().search(adiSaisieComplexSearchModel));
            // on définit dans le viewBean la liste (Hashmap) résultante du
            // contrôle de saisie du décompte
            ((ALSaisieAdiViewBean) viewBean).setListeASaisir(ALServiceLocator.getDecompteAdiBusinessService()
                    .controleSaisieDecompte(((ALSaisieAdiViewBean) viewBean).getDecompteModel(),
                            ((ALSaisieAdiViewBean) viewBean).getPrestationComplexSearchModel(),
                            ((ALSaisieAdiViewBean) viewBean).getSaisieComplexSearchModel()));
            // On initialise la nouvelle saisie en fonction des saisies déjà
            // faites
            ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                    .setIdDecompteAdi(((ALSaisieAdiViewBean) viewBean).getDecompteModel().getIdDecompteAdi());

            ((ALSaisieAdiViewBean) viewBean).setAdiSaisieComplexModel(ALServiceLocator
                    .getAdiSaisieComplexModelService().initModel(
                            ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel(),
                            ((ALSaisieAdiViewBean) viewBean).getListeASaisir()));

            // Si saisie complete, on peut lancer le calcul
            if (ALServiceLocator.getDecompteAdiBusinessService().isSaisieComplete(
                    ((ALSaisieAdiViewBean) viewBean).getListeASaisir())) {
                ALServiceLocator.getCalculAdiBusinessService().calculForDecompte(
                        ((ALSaisieAdiViewBean) viewBean).getDecompteModel());
            }

        }
        super._init(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("supprimerSaisie".equals(action.getActionPart()) && (viewBean instanceof ALSaisieAdiViewBean)) {
            try {
                if (((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().isNew()) {
                    ((ALSaisieAdiViewBean) viewBean).retrieve();
                }
                ((ALSaisieAdiViewBean) viewBean).delete();

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }

    }
}
