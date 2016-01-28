package globaz.al.helpers.adi;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.adi.ALGenerationDecompteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALGenerationDecompteHelper extends ALAbstractHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (viewBean instanceof ALGenerationDecompteViewBean) {
            try {
                ((ALGenerationDecompteViewBean) viewBean).setDecompteAdiModel(ALServiceLocator
                        .getDecompteAdiModelService().read(
                                ((ALGenerationDecompteViewBean) viewBean).getDecompteAdiModel().getId()));

                // TODO initialiser periodeTraitement
                // numFacture
                // periodicite et bonification

                DossierModel dossierLieDecompte = ALServiceLocator.getDossierModelService().read(
                        ((ALGenerationDecompteViewBean) viewBean).getDecompteAdiModel().getIdDossier());

                ((ALGenerationDecompteViewBean) viewBean).setDossierModel(dossierLieDecompte);

                String periodicite = ALServiceLocator.getAffiliationBusinessService()
                        .getAssuranceInfo(dossierLieDecompte, JadeDateUtil.getGlobazFormattedDate(new Date()))
                        .getPeriodicitieAffiliation();

                String genreAssurance = ALServiceLocator.getDossierBusinessService().isParitaire(
                        dossierLieDecompte.getActiviteAllocataire()) ? ALCSAffilie.GENRE_ASSURANCE_PARITAIRE
                        : ALCSAffilie.GENRE_ASSURANCE_INDEP;

                String bonification = JadeStringUtil.isBlankOrZero(dossierLieDecompte.getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                        : ALCSPrestation.BONI_DIRECT;

                String constTypeCotisation = ALServiceLocator.getDossierBusinessService().getTypeCotisation(
                        dossierLieDecompte);

                ((ALGenerationDecompteViewBean) viewBean).setProcessusSelectableList(ALServiceLocator
                        .getBusinessProcessusService().getUnlockProcessusPaiementForPeriode(bonification,
                                genreAssurance));

                // si on a l'id du dossier, on peut générer la période qu'on
                // veut

                ((ALGenerationDecompteViewBean) viewBean).setPeriodeTraitement(ALServiceLocator
                        .getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(periodicite, bonification,
                                constTypeCotisation));
                // numéro de facture sur la base de la période de traitement// numéro de facture
                ((ALGenerationDecompteViewBean) viewBean).setNoFacture(ALServiceLocator.getNumeroFactureService()
                        .getNumFacture(((ALGenerationDecompteViewBean) viewBean).getPeriodeTraitement(),
                                dossierLieDecompte));

                // on recherche si il y a une récap ouverte pour ce dossier
                RecapitulatifEntrepriseSearchModel searchRecapOuvertes = new RecapitulatifEntrepriseSearchModel();

                searchRecapOuvertes.setForNumeroAffilie(((ALGenerationDecompteViewBean) viewBean).getDossierModel()
                        .getNumeroAffilie());
                searchRecapOuvertes.setForBonification(bonification);
                searchRecapOuvertes.setForEtatRecap(ALCSPrestation.ETAT_SA);

                searchRecapOuvertes = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(
                        searchRecapOuvertes);
                ((ALGenerationDecompteViewBean) viewBean).setSearchRecapsExistantesAffilie(searchRecapOuvertes);
                // on prend la récap qui correspond au n° facture pour lier le processus
                RecapitulatifEntrepriseSearchModel searchRecap = new RecapitulatifEntrepriseSearchModel();
                searchRecap.setForNumeroAffilie(((ALGenerationDecompteViewBean) viewBean).getDossierModel()
                        .getNumeroAffilie());
                searchRecap.setForBonification(bonification);
                searchRecap.setForEtatRecap(ALCSPrestation.ETAT_SA);
                searchRecap.setForNumeroFacture(((ALGenerationDecompteViewBean) viewBean).getNoFacture());
                searchRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(searchRecap);

                RecapitulatifEntrepriseModel recap = null;
                if (searchRecap.getSize() > 0) {
                    recap = (RecapitulatifEntrepriseModel) searchRecap.getSearchResults()[0];
                }
                if (recap != null) {
                    ((ALGenerationDecompteViewBean) viewBean).setNumProcessus(recap.getIdProcessusPeriodique());
                }

            } catch (JadeApplicationException e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } catch (JadePersistenceException e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        }
        super._init(viewBean, action, session);
    }
}
