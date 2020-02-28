package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
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

import java.util.List;

public class CalculImpotSource {

    private CalculImpotSource() {
        throw new UnsupportedOperationException();
    }

    public static void computeIS(List<CalculBusinessModel> droitsCalcules, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository,
                                 DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException {
        AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier.getDossierModel(), dateCalcul);
        if(dossier.getDossierModel().getRetenueImpot()
                && !ALCSDossier.PAIEMENT_INDIRECT.equals(getPaiementMode((DossierComplexModel)dossier, dateCalcul))) {
            for (CalculBusinessModel droitCalcule : droitsCalcules) {
                if (droitCalcule.getDroit().equals(droit)) {
                    computeISforDroit(droitCalcule, droitCalcule.getCalculResultMontantBase(), tauxGroupByCanton, tauxImpositionRepository, infos.getCanton(), dateCalcul);
                }
            }
        }
    }

    public static void computeISforDroit(CalculBusinessModel droitCalcule, String montant, TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository,
                                 String canton, String dateCalcul)
            throws JadeApplicationException {
                    Taux tauxApplicable;
                    try {
                        tauxApplicable = findTauxApplicable(tauxGroupByCanton, tauxImpositionRepository,
                                canton, dateCalcul);
                    } catch (TauxImpositionNotFoundException e) {
                        throw new ALCalculException(e.getMessage());
                    }
                    Montant montantPrestation = new Montant(montant);
                    Montant impots = montantPrestation.multiply(tauxApplicable).normalize();
                    droitCalcule.setCalculResultMontantIS(impots.getValue());
        }

    private static Taux findTauxApplicable(TauxImpositions tauxGroupByCanton, TauxImpositionRepository tauxImpositionRepository,
                                           String cantonResidence, String date)
            throws TauxImpositionNotFoundException {
        if(tauxGroupByCanton == null) {
            tauxGroupByCanton = tauxImpositionRepository.findAll(TypeImposition.IMPOT_SOURCE);
        }
        return tauxGroupByCanton.getTauxImpotSource(cantonResidence,
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
