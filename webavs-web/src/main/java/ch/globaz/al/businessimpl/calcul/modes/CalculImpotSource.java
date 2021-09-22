package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.generation.prestations.GenPrestationDossier;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
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
                    computeISforDroit(dossier.getDossierModel(), droitCalcule, droitCalcule.getCalculResultMontantBase(), tauxGroupByCanton, tauxImpositionRepository, cantonImposition, dateCalcul);
                }
            }
        }
    }

    public static void computeISforDroit(DossierModel dossierModel, CalculBusinessModel droitCalcule, String montant, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository, String cantonImposition, String dateFinMoisPourPeriode)
            throws JadeApplicationException {
        try {
            // cherche si le droit de ce dossier possède une prestations déjà comptabilisée à extourner pour la période en cours de traitement,
            String prestationImpotSourceAExtourner = hasPrestationAExtourner(dossierModel, droitCalcule, JACalendar.format(dateFinMoisPourPeriode, JACalendar.FORMAT_MMsYYYY));

            // si montantIS de la prestationImpotSourceAExtourner existe on l'utilise tel quel
            if (!JadeStringUtil.isEmpty(prestationImpotSourceAExtourner)) {
                droitCalcule.setCalculResultMontantIS(prestationImpotSourceAExtourner);
            } else { // sinon on utilise le taux impot source à la date d'aujourd'hui pour recalculer le montantIS
                String dateAujourdhuiPourCalculImpotSource = JadeDateUtil.getGlobazFormattedDate(new java.util.Date());
                Taux tauxApplicable = findTauxApplicable(tauxGroupByCanton, tauxImpositionRepository, cantonImposition, dateAujourdhuiPourCalculImpotSource);
                Montant montantPrestation = new Montant(montant);
                Montant impots = montantPrestation.multiply(tauxApplicable).normalize();
                droitCalcule.setCalculResultMontantIS(impots.getValue());
            }
        } catch (TauxImpositionNotFoundException e) {
            throw new ALCalculException(e.getMessage());
        }
    }

    public static String hasPrestationAExtourner(DossierModel dossierModel, CalculBusinessModel droitCalcule, String dateFinMoisPourPeriode) {
        try {
            DetailPrestationGenComplexSearchModel search = GenPrestationDossier.searchExistingPrestSansContextAffilie(dossierModel.getId(), dossierModel.getDateDebutPeriode(), dateFinMoisPourPeriode, droitCalcule.getDroit().getId());

            ArrayList<String> processed = new ArrayList<String>();
            String lastDate = null;
            String lastPeriod = null;

            // si des prestations existent, génération d'une prestation inverse
            if (search.getSize() > 0) {
                for (int i = 0; i < search.getSize(); i++) {
                    DetailPrestationGenComplexModel oldPrest = (DetailPrestationGenComplexModel) search.getSearchResults()[i];

                    if (JadeStringUtil.isBlank(lastPeriod)) {
                        lastPeriod = oldPrest.getPeriodeValidite();
                    }

                    if (JadeStringUtil.isBlank(lastDate)) {
                        lastDate = oldPrest.getDateVersement();
                    }

                    if (!oldPrest.getPeriodeValidite().equals(lastPeriod) || !oldPrest.getDateVersement().equals(lastDate)) {
                        lastDate = oldPrest.getDateVersement();
                        processed.add(lastPeriod);
                        lastPeriod = oldPrest.getPeriodeValidite();
                    }

                    if (!processed.contains(lastPeriod)) {
                        if (ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                                && !JadeStringUtil.isBlankOrZero(oldPrest.getMontantIS())) {
                            return oldPrest.getMontantIS();
                        }
                    }
                }
            }

        } catch (JadeApplicationException | JadePersistenceException e) {
            JadeLogger.error(e, "Une erreur s'est produite pendant la recherche de prestations extournables." + e.getMessage());
        }

        return null;
    }

    /**
     * Permet de récupérer le canton d'imposition suivant les règles suivantes :
     *  - Si canton d'imposition forcé renseigné : canton imposition forcé.
     *  - Si résident Suisse : canton de résidence de l'allocataire.
     *  - Si résident hors Suisse : canton de l'affilié.
     *
     * @param dossierComplexModel
     * @param cantonAffiliation
     * @return le canton d'imposition
     */
    public static String getCantonImposition(DossierComplexModel dossierComplexModel, String cantonAffiliation) throws JadeApplicationException {
        String cantonImposition;
        AllocataireModel allocataireModel = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel();
        String cantonImpositionForce =  dossierComplexModel.getDossierModel().getCantonImposition();
        if (StringUtils.isNotEmpty(cantonImpositionForce) && !StringUtils.equals("0", cantonImpositionForce)) {
            cantonImposition = cantonImpositionForce;
        } else if (StringUtils.equals(ALCSPays.PAYS_SUISSE, allocataireModel.getIdPaysResidence())) {
            cantonImposition = allocataireModel.getCantonResidence();
        } else {
            cantonImposition = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(cantonAffiliation);
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
