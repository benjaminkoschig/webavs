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
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.generation.prestations.GenPrestationAbstract;
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
import globaz.jade.persistence.model.JadeAbstractModel;
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
            // Si pas de montant prestation pas de montantIS
            if(JadeStringUtil.isBlankOrZero(montant)){
                return;
            }
            // cherche si le droit de ce dossier poss�de une prestation d�j� comptabilis�e � extourner pour la p�riode en cours de traitement,
            DeclarationVersementDetailleComplexModel prestationImpotSourceAExtourner = recherchePrestationAExtourner(dossierModel, droitCalcule, JACalendar.format(dateFinMoisPourPeriode, JACalendar.FORMAT_MMsYYYY));

            String montantIS;
            if (prestationImpotSourceAExtourner != null) {
                if(montant.equals(prestationImpotSourceAExtourner.getMontantDetailPrestation())) {
                    // l'ancien montant correspond au nouveau : on garde le montantIS pr�c�demment calcul�
                    montantIS = prestationImpotSourceAExtourner.getMontantIS();
                } else {
                    Montant montantPrestation = new Montant(montant).add(new Montant(prestationImpotSourceAExtourner.getMontantDetailPrestation()).negate());
                    if(montantPrestation.isPositive()) {
                        // montant � verser sup�rieur : on applique le taux actuel sur la diff�rence et ajoute l'ancien montantIS
                        Montant impotsDiff = rechercheImpot(montantPrestation.getValue(), tauxGroupByCanton, tauxImpositionRepository, cantonImposition, JadeDateUtil.getGlobazFormattedDate(new java.util.Date()));
                        montantIS = impotsDiff.add(new Montant(prestationImpotSourceAExtourner.getMontantIS())).getValue();
                    } else {
                        // montant � verser inf�rieur : on calcule le montantIS avec l'ancien taux
                        montantIS = rechercheImpot(montant, tauxGroupByCanton, tauxImpositionRepository, cantonImposition, dateFinMoisPourPeriode).getValue();
                    }
                }
            } else { // Pas de montant � extourner : on utilise le taux impot source � la date d'aujourd'hui pour recalculer le montantIS
                montantIS = rechercheImpot(montant, tauxGroupByCanton, tauxImpositionRepository, cantonImposition, JadeDateUtil.getGlobazFormattedDate(new java.util.Date())).getValue();
            }
            droitCalcule.setCalculResultMontantIS(montantIS);
        } catch (TauxImpositionNotFoundException e) {
            throw new ALCalculException(e.getMessage());
        }
    }
    private static Montant rechercheImpot(String montant, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository, String cantonImposition, String date) throws TauxImpositionNotFoundException {
        Taux tauxApplicable = findTauxApplicable(tauxGroupByCanton, tauxImpositionRepository, cantonImposition, date);
        return new Montant(montant).multiply(tauxApplicable).normalize();
    }

    public static DeclarationVersementDetailleComplexModel recherchePrestationAExtourner(DossierModel dossierModel, CalculBusinessModel droitCalcule, String dateFinMoisPourPeriode) {
        try {
            DeclarationVersementDetailleSearchComplexModel search = GenPrestationAbstract.searchExistingPrestNssEnfant(dossierModel.getId(), dossierModel.getDateDebutPeriode(), dateFinMoisPourPeriode, droitCalcule.getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());

            ArrayList<String> processed = new ArrayList<>();
            String lastDate = null;
            String lastPeriod = null;

            // si des prestations existent, g�n�ration d'une prestation inverse
            for (JadeAbstractModel abstractModel : search.getSearchResults()) {
                DeclarationVersementDetailleComplexModel oldPrest = (DeclarationVersementDetailleComplexModel) abstractModel;

                if (JadeStringUtil.isBlank(lastPeriod)) {
                    lastPeriod = oldPrest.getPeriode();
                }

                if (JadeStringUtil.isBlank(lastDate)) {
                    lastDate = oldPrest.getDateVersement();
                }

                if (!oldPrest.getPeriode().equals(lastPeriod) || !oldPrest.getDateVersement().equals(lastDate)) {
                    lastDate = oldPrest.getDateVersement();
                    processed.add(lastPeriod);
                    lastPeriod = oldPrest.getPeriode();
                }

                if (!processed.contains(lastPeriod)
                        && ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                        && !JadeStringUtil.isBlankOrZero(oldPrest.getMontantIS())) {
                    return oldPrest;
                }
            }

        } catch (JadeApplicationException | JadePersistenceException e) {
            JadeLogger.error(e, "Une erreur s'est produite pendant la recherche de prestations extournables." + e.getMessage());
        }

        return null;
    }

    /**
     * Permet de r�cup�rer le canton d'imposition suivant les r�gles suivantes :
     *  - Si canton d'imposition forc� renseign� : canton imposition forc�.
     *  - Si r�sident Suisse : canton de r�sidence de l'allocataire.
     *  - Si r�sident hors Suisse : canton de l'affili�.
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
