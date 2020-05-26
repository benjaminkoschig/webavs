package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.exception.CantonImpositionNotFoundException;
import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class CalculImpotSource {

    private CalculImpotSource() {
        throw new UnsupportedOperationException();
    }

    public static void computeIS(List<CalculBusinessModel> droitsCalcules, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository,
                                 DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException {
        AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier.getDossierModel(), dateCalcul);
        if (dossier.getDossierModel().getRetenueImpot()
                && !ALCSDossier.PAIEMENT_INDIRECT.equals(getPaiementMode((DossierComplexModel) dossier, dateCalcul))) {
                    String cantonImposition = getCantonImposition((DossierComplexModel) dossier, infos.getCanton());
            for (CalculBusinessModel droitCalcule : droitsCalcules) {
                if (droitCalcule.getDroit().equals(droit)) {
                    computeISforDroit(droitCalcule, droitCalcule.getCalculResultMontantBase(), tauxGroupByCanton, tauxImpositionRepository, cantonImposition, dateCalcul);
                }
            }
        }
    }

    public static void computeISforDroit(CalculBusinessModel droitCalcule, String montant, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository, String cantonImposition, String dateCalcul)
            throws JadeApplicationException {
        Taux tauxApplicable;
        try {
            tauxApplicable = findTauxApplicable(tauxGroupByCanton, tauxImpositionRepository,
                    cantonImposition, dateCalcul);
        } catch (TauxImpositionNotFoundException e) {
            throw new ALCalculException(e.getMessage());
        }
        Montant montantPrestation = new Montant(montant);
        Montant impots = montantPrestation.multiply(tauxApplicable).normalize();
        droitCalcule.setCalculResultMontantIS(impots.getValue());
    }

    /**
     * Permet de récupérer le canton d'imposition suivant les règles suivantes :
     *  - Si résident Suisse : canton de résidence de l'allocataire.
     *  - Si résident hors Suisse et tarif forcé : canton imposition relatif au tarif forcé.
     *  - Si résident hors Suisse sans tarif forcé : canton de l'affilié.
     *
     * @param dossierComplexModel
     * @param cantonAffiliation
     * @return le canton d'imposition
     */
    public static String getCantonImposition(DossierComplexModel dossierComplexModel, String cantonAffiliation) throws JadeApplicationException {
        String cantonImposition;
        AllocataireModel allocataireModel = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel();
        if (StringUtils.equals(ALCSPays.PAYS_SUISSE, allocataireModel.getIdPaysResidence())) {
            cantonImposition = allocataireModel.getCantonResidence();
        } else {
            DossierModel dossierModel = dossierComplexModel.getDossierModel();
            if (StringUtils.isNotEmpty(dossierModel.getTarifForce()) && !StringUtils.equals("0", dossierModel.getTarifForce())) {
                String cantonImpositionForce = dossierModel.getCantonImposition();
                if (StringUtils.isEmpty(cantonImpositionForce) || StringUtils.equals("0", cantonImpositionForce)) {
                    throw new CantonImpositionNotFoundException("Le canton d'imposition forcé n'a pas été trouvé.");
                }
                cantonImposition = dossierModel.getCantonImposition();
            } else {
                cantonImposition = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(cantonAffiliation);
            }
        }
        return cantonImposition;
    }

    private static Taux findTauxApplicable(TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository,
                                           String cantonImposition, String date)
            throws TauxImpositionNotFoundException {
        if (tauxGroupByCanton == null) {
            tauxGroupByCanton = tauxImpositionRepository.findAll(TypeImposition.IMPOT_SOURCE);
        }
        return tauxGroupByCanton.getTauxImpotSource(cantonImposition,
                new Date(date));
    }

    public static String getPaiementMode(DossierComplexModel dossier, String date) {

        String idTiersBeneficiaire = dossier.getDossierModel().getIdTiersBeneficiaire();
        String idTiersAllocataire = dossier.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire();

        if (dossier.isNew()) {

            try {
                String paiementdirect = (ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.MODE_PAIEMENT_DIRECT, date))
                        .getValeurAlphaParametre();

                if ("true".equals(paiementdirect)) {
                    return ALCSDossier.PAIEMENT_DIRECT;

                } else {
                    return ALCSDossier.PAIEMENT_INDIRECT;
                }
            } catch (Exception e) {
                return null;
            }

        }

        if (idTiersBeneficiaire.equals(idTiersAllocataire)) {
            return ALCSDossier.PAIEMENT_DIRECT;
        } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
            return ALCSDossier.PAIEMENT_INDIRECT;
        } else {
            return ALCSDossier.PAIEMENT_TIERS;
        }

    }
}
