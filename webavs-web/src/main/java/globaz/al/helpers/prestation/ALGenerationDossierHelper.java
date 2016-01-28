package globaz.al.helpers.prestation;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.prestation.ALGenerationDossierViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Helper dédié au viewBean ALGenerationDossierViewBean
 * 
 * @author GMO
 * 
 */
public class ALGenerationDossierHelper extends ALAbstractHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (viewBean instanceof ALGenerationDossierViewBean) {
            try {
                String paramWarnRetroBefore = ParamServiceLocator
                        .getParameterModelService()
                        .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.WARN_RETRO_BEFORE,
                                JadeDateUtil.getGlobazFormattedDate(new Date())).getValeurAlphaParametre();
                ((ALGenerationDossierViewBean) viewBean)
                        .setParamWarnRetroBefore(Integer.parseInt(paramWarnRetroBefore));
                // chargement du droit
                if (!JadeNumericUtil.isEmptyOrZero(((ALGenerationDossierViewBean) viewBean).getDroitComplexModel()
                        .getId())) {
                    ((ALGenerationDossierViewBean) viewBean).setDroitComplexModel(ALServiceLocator
                            .getDroitComplexModelService().read(
                                    ((ALGenerationDossierViewBean) viewBean).getDroitComplexModel().getId()));
                }

                // si on a pas l'id du dossier, on utilise l'id récap
                if (JadeNumericUtil.isEmptyOrZero(((ALGenerationDossierViewBean) viewBean).getDossierComplexModel()
                        .getId())) {

                    // on trouve la 1ère prestation à compléter
                    EntetePrestationModel nextPrestationToComplete = ALServiceLocator
                            .getRecapitulatifEntrepriseBusinessService().getNextPrestationASaisir(
                                    ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap());

                    RecapitulatifEntrepriseModel recap = ALServiceLocator.getRecapitulatifEntrepriseModelService()
                            .read(((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap());

                    ((ALGenerationDossierViewBean) viewBean).setPeriodeTraitement(recap.getPeriodeA());
                    ((ALGenerationDossierViewBean) viewBean).setNoFacture(recap.getNumeroFacture());

                    // on vérifie combien de prestations il reste à compléter,
                    // celle-ci incluse
                    ((ALGenerationDossierViewBean) viewBean).setNbPrestationsASaisir(ALServiceLocator
                            .getRecapitulatifEntrepriseBusinessService().getNbPrestationsASaisir(
                                    ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().getIdRecap()));

                    // on charge le dossier
                    ((ALGenerationDossierViewBean) viewBean).setDossierComplexModel(ALServiceLocator
                            .getDossierComplexModelService().read(nextPrestationToComplete.getIdDossier()));

                    ((ALGenerationDossierViewBean) viewBean).setEntetePrestationModel(nextPrestationToComplete);

                } else {
                    // On charge le dossier lié pour lequel on va générer une
                    // prestation
                    ((ALGenerationDossierViewBean) viewBean).setDossierComplexModel(ALServiceLocator
                            .getDossierComplexModelService().read(
                                    ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel().getId()));

                    String constTypeCotisation = ALServiceLocator.getDossierBusinessService().getTypeCotisation(
                            ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel().getDossierModel());

                    String genreAssurance = ALServiceLocator.getDossierBusinessService().isParitaire(
                            ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel().getDossierModel()
                                    .getActiviteAllocataire()) ? ALCSAffilie.GENRE_ASSURANCE_PARITAIRE
                            : ALCSAffilie.GENRE_ASSURANCE_INDEP;
                    ((ALGenerationDossierViewBean) viewBean).setProcessusSelectableList(ALServiceLocator
                            .getBusinessProcessusService().getUnlockProcessusPaiementForPeriode(
                                    ((ALGenerationDossierViewBean) viewBean).getBonification(), genreAssurance));

                    // si on a l'id du dossier, on peut générer la période qu'on
                    // veut

                    ((ALGenerationDossierViewBean) viewBean).setPeriodeTraitement(ALServiceLocator
                            .getPeriodeAFBusinessService().getPeriodeToGenerateForDossier(
                                    ((ALGenerationDossierViewBean) viewBean).getPeriodicite(),
                                    ((ALGenerationDossierViewBean) viewBean).getBonification(), constTypeCotisation));
                    // numéro de facture sur la base de la période de traitement// numéro de facture
                    ((ALGenerationDossierViewBean) viewBean)
                            .setNoFacture(ALServiceLocator.getNumeroFactureService()
                                    .getNumFacture(
                                            ((ALGenerationDossierViewBean) viewBean).getPeriodeTraitement(),
                                            ((ALGenerationDossierViewBean) viewBean).getDossierComplexModel()
                                                    .getDossierModel()));

                    ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().setPeriodeDe("");
                    ((ALGenerationDossierViewBean) viewBean).getEntetePrestationModel().setPeriodeA("");

                    // on recherche si il y a une récap ouverte pour ce dossier
                    RecapitulatifEntrepriseSearchModel searchRecapOuvertes = new RecapitulatifEntrepriseSearchModel();

                    searchRecapOuvertes.setForNumeroAffilie(((ALGenerationDossierViewBean) viewBean)
                            .getDossierComplexModel().getDossierModel().getNumeroAffilie());
                    searchRecapOuvertes.setForBonification(((ALGenerationDossierViewBean) viewBean).getBonification());
                    searchRecapOuvertes.setForEtatRecap(ALCSPrestation.ETAT_SA);

                    searchRecapOuvertes = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(
                            searchRecapOuvertes);
                    ((ALGenerationDossierViewBean) viewBean).setSearchRecapsExistantesAffilie(searchRecapOuvertes);
                    // on prend la récap qui correspond au n° facture pour lier le processus
                    RecapitulatifEntrepriseSearchModel searchRecap = new RecapitulatifEntrepriseSearchModel();
                    searchRecap.setForNumeroAffilie(((ALGenerationDossierViewBean) viewBean).getDossierComplexModel()
                            .getDossierModel().getNumeroAffilie());
                    searchRecap.setForBonification(((ALGenerationDossierViewBean) viewBean).getBonification());
                    searchRecap.setForEtatRecap(ALCSPrestation.ETAT_SA);
                    searchRecap.setForNumeroFacture(((ALGenerationDossierViewBean) viewBean).getNoFacture());
                    searchRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(searchRecap);

                    RecapitulatifEntrepriseModel recap = null;
                    if (searchRecap.getSize() > 0) {
                        recap = (RecapitulatifEntrepriseModel) searchRecap.getSearchResults()[0];
                    }
                    if (recap != null) {
                        ((ALGenerationDossierViewBean) viewBean).setNumProcessus(recap.getIdProcessusPeriodique());
                    }

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
