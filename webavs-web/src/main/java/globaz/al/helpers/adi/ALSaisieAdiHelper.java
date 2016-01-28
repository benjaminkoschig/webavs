package globaz.al.helpers.adi;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.adi.ALSaisieAdiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper d�di� au viewBean ALSaisieViewBean
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

            // on charge le d�compte pour le saisie, avec l'id d�finie depuis
            // ALActionAdi#beforeNouveau
            ((ALSaisieAdiViewBean) viewBean)
                    .setDecompteModel(ALServiceLocator.getDecompteAdiModelService().read(
                            ((ALSaisieAdiViewBean) viewBean).getAdiSaisieComplexModel().getAdiSaisieModel()
                                    .getIdDecompteAdi()));

            // on charge les d�tails de prestations de travail relatifs au
            // d�compte
            ((ALSaisieAdiViewBean) viewBean).setPrestationComplexSearchModel(ALServiceLocator
                    .getDecompteAdiBusinessService().getPrestationsTravailDossier(
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getIdDossier(),
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getPeriodeDebut(),
                            ((ALSaisieAdiViewBean) viewBean).getDecompteModel().getPeriodeFin()));

            AdiSaisieComplexSearchModel adiSaisieComplexSearchModel = new AdiSaisieComplexSearchModel();
            adiSaisieComplexSearchModel
                    .setForIdDecompteAdi(((ALSaisieAdiViewBean) viewBean).getDecompteModel().getId());
            // lancement de la recherche des saisie adi existantes pour ce
            // d�compte
            ((ALSaisieAdiViewBean) viewBean).setSaisieComplexSearchModel(ALServiceLocator
                    .getAdiSaisieComplexModelService().search(adiSaisieComplexSearchModel));
            // on d�finit dans le viewBean la liste (Hashmap) r�sultante du
            // contr�le de saisie du d�compte
            ((ALSaisieAdiViewBean) viewBean).setListeASaisir(ALServiceLocator.getDecompteAdiBusinessService()
                    .controleSaisieDecompte(((ALSaisieAdiViewBean) viewBean).getDecompteModel(),
                            ((ALSaisieAdiViewBean) viewBean).getPrestationComplexSearchModel(),
                            ((ALSaisieAdiViewBean) viewBean).getSaisieComplexSearchModel()));
            // On initialise la nouvelle saisie en fonction des saisies d�j�
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
