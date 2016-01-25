package globaz.apg.services;

import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APRechercherTypeAcmServiceException;
import globaz.apg.properties.APProperties;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAssurance;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;

/**
 * Service d�terminant le type de prestation ACM (NE,ALPHA,NONE) et son association (FNE,MECP,PP) selon le plan
 * d'affiliation.
 * 
 * @author JJE
 */
public class APRechercherTypeAcmService {

    private static final String NONE_ACM = "NONE";

    private APRechercherTypeAcmServiceData getAcmNeAssuranceAssociation(final String idDroit,
            final String idAffilieEmployeur) throws APRechercherTypeAcmServiceException, Exception {

        // On recherche les assurances
        final List<IAFAssurance> assurancesList = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                idAffilieEmployeur);

        final List<IAFAssurance> assurancesAcmNeList = new ArrayList<IAFAssurance>();

        if (assurancesList.size() > 0) {

            for (final IAFAssurance assuranceCourante : assurancesList) {
                if (!(getAssuranceTypeAssociation(assuranceCourante).equals(APAssuranceTypeAssociation.NONE
                        .getCodesystemToString()))) {
                    assurancesAcmNeList.add(assuranceCourante);
                }
            }

            if (assurancesAcmNeList.size() == 1) {
                APAssuranceTypeAssociation association = getAssuranceTypeAssociation(assurancesAcmNeList.get(0));
                return new APRechercherTypeAcmServiceData(association.getCodesystemToString(),
                        association.getNomTypeAssociation(), APTypeDePrestation.ACM_NE);
            } else {
                if (assurancesAcmNeList.size() == 0) {
                    return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(),
                            "", APTypeDePrestation.STANDARD);
                } else {
                    throw new APRechercherTypeAcmServiceException(
                            "APRechercherTypeAcmService.getAcmNeAssuranceAssociation(): Les assurances (FNE,MECP,PP) ne sont pas cumulables");
                }
            }

        } else {
            return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(), "",
                    APTypeDePrestation.STANDARD);
        }

    }

    /**
     * Retourne l'association de l'assuance en fonction des properties
     * 
     * @param assuranceCourante
     * @return
     * @throws APRechercherTypeAcmServiceException
     */
    private APAssuranceTypeAssociation getAssuranceTypeAssociation(final IAFAssurance assuranceCourante)
            throws APRechercherTypeAcmServiceException {

        final String[] idAssurances = getIdAssuranceFneMecpPp();

        final String idAssuranceFne = idAssurances[0];
        final String idAssuranceMecp = idAssurances[1];
        final String idAssurancePp = idAssurances[2];

        // Assurance FNE
        if (idAssuranceFne.equals(assuranceCourante.getAssuranceId())) {
            return APAssuranceTypeAssociation.FNE;
        }// Assurance MECP
        else if (idAssuranceMecp.equals(assuranceCourante.getAssuranceId())) {
            return APAssuranceTypeAssociation.MECP;
        }// Assurance PP
        else if (idAssurancePp.equals(assuranceCourante.getAssuranceId())) {
            return APAssuranceTypeAssociation.PP;
        } else {
            return APAssuranceTypeAssociation.NONE;
        }
    }

    private String[] getIdAssuranceFneMecpPp() throws APRechercherTypeAcmServiceException {

        String idAssuranceFne = "";
        String idAssuranceMecp = "";
        String idAssurancePp = "";

        try {

            idAssuranceFne = APProperties.ASSURANCE_FNE_ID.getValue();
            idAssuranceMecp = APProperties.ASSURANCE_MECP_ID.getValue();
            idAssurancePp = APProperties.ASSURANCE_PP_ID.getValue();

            if (JadeStringUtil.isBlankOrZero(idAssuranceFne) || JadeStringUtil.isBlankOrZero(idAssuranceMecp)
                    || JadeStringUtil.isBlankOrZero(idAssurancePp)) {
                throw new APRechercherTypeAcmServiceException(
                        "APRechercherTypeAcmService.getIdAssuranceFneMecpPp(): Les propri�t�s suivantes doivent avoir une valeur diff�rente de 0 ou null:"
                                + APProperties.ASSURANCE_FNE_ID.getPropertyName()
                                + ", "
                                + APProperties.ASSURANCE_MECP_ID.getPropertyName()
                                + ", "
                                + APProperties.ASSURANCE_PP_ID.getPropertyName());
            }

        } catch (final PropertiesException e) {
            throw new APRechercherTypeAcmServiceException(
                    "APRechercherTypeAcmService.getIdAssuranceFneMecpPp(): Les propri�t�s suivantes doivent exister :"
                            + APProperties.ASSURANCE_FNE_ID.getPropertyName() + ", "
                            + APProperties.ASSURANCE_MECP_ID.getPropertyName() + ", "
                            + APProperties.ASSURANCE_PP_ID.getPropertyName());
        }

        return new String[] { idAssuranceFne, idAssuranceMecp, idAssurancePp };
    }

    /**
     * vrais si l'affilie cotise a une assurance acm alpha
     * 
     * @return Boolean
     * @throws APRechercherTypeAcmServiceException
     */
    private Boolean hasCotisationAssuranceAcmAlpha(final String idAffilieEmployeur, final String idDroit)
            throws APRechercherTypeAcmServiceException {

        if (!JadeStringUtil.isEmpty(idAffilieEmployeur)) {

            try {

                final List<IAFAssurance> assurancesList = APRechercherAssuranceFromDroitCotisationService.rechercher(
                        idDroit, idAffilieEmployeur);

                for (final IAFAssurance assuranceCourante : assurancesList) {
                    if (IAFAssurance.TYPE_ASS_COTISATION_AF.equals(assuranceCourante.getTypeAssurance())) {
                        return Boolean.TRUE;
                    }
                }

            } catch (final Exception e) {
                throw new APRechercherTypeAcmServiceException(
                        "APRechercherTypeAcmService.hasCotisationAssuranceAcmAlpha(): " + e.getMessage());
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Est-ce que la caisse propose des prestation ACM_ALFA
     * 
     * @return
     * @throws PropertiesException
     */
    private boolean hasPrestationAcmAlfa() throws PropertiesException {
        // R�cup�ration de la valeur de la property. Exception si pas d�clar�e
        final String propertyValue = APProperties.TYPE_DE_PRESTATION_ACM.getValue();
        // validation en fonction de son domaine de valeur. Exception si valeur ne fait pas partie du domaine
        CommonPropertiesUtils.validatePropertyValue(APProperties.TYPE_DE_PRESTATION_ACM, propertyValue,
                APPropertyTypeDePrestationAcmValues.propertyValues());

        return APPropertyTypeDePrestationAcmValues.ACM_ALFA.getPropertyValue().equals(propertyValue);
    }

    /**
     * M�thode d�terminant selon les properties le type de prestation ACM � rechercher
     * 
     * @return APRechercherTypeAcmServiceData
     */
    public APRechercherTypeAcmServiceData rechercher(final boolean isPaiementAssure,
            final boolean isPaiementIndependant, final String idDroit, final String idAffilieEmployeur,
            final String method) throws APRechercherTypeAcmServiceException, Exception {

        validerParametres(idDroit, idAffilieEmployeur, method);

        // On choisit la fa�on de d�terminer le type de prestation ACM selon le type d'ACM g�r�e par la caisse (voir
        // properties)
        if (APProperties.TYPE_DE_PRESTATION_ACM.isEqualToValue(APTypeDePrestation.ACM_NE.getNomTypePrestation())) {
            APRechercherTypeAcmServiceData result = rechercherAcmNe(isPaiementAssure, isPaiementIndependant, idDroit,
                    idAffilieEmployeur);

            return result;

        } else if (APProperties.TYPE_DE_PRESTATION_ACM.isEqualToValue(APTypeDePrestation.ACM_ALFA
                .getNomTypePrestation())) {

            return rechercherAcmAlpha(idAffilieEmployeur, idDroit);

        } else if (APProperties.TYPE_DE_PRESTATION_ACM.isEqualToValue(APRechercherTypeAcmService.NONE_ACM)) {

            return rechercherPrestationNonAcm();

        } else {
            throw new APRechercherTypeAcmServiceException(
                    "APRechercherTypeAcmService.rechercher(): Valeur non ger�e, properties: "
                            + APProperties.TYPE_DE_PRESTATION_ACM.getPropertyName());
        }
    }

    private APRechercherTypeAcmServiceData rechercherAcmAlpha(final String idAffilieEmployeur, final String idDroit)
            throws Exception {

        if (hasPrestationAcmAlfa() && hasCotisationAssuranceAcmAlpha(idAffilieEmployeur, idDroit)) {
            return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(), "",
                    APTypeDePrestation.ACM_ALFA);
        } else {
            return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(), "",
                    APTypeDePrestation.STANDARD);
        }
    }

    private APRechercherTypeAcmServiceData rechercherAcmNe(final boolean isPaiementAssure,
            final boolean isPaiementIndependant, final String idDroit, final String idAffilieEmployeur)
            throws APRechercherTypeAcmServiceException, Exception {

        // Le calcul ACM est impossible si le paiement est effectu� � l'assur� ou pay�e � l'ind�pendant
        if (isPaiementAssure || isPaiementIndependant) {
            return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(), "",
                    APTypeDePrestation.STANDARD);
        } else {
            return getAcmNeAssuranceAssociation(idDroit, idAffilieEmployeur);
        }
    }

    private APRechercherTypeAcmServiceData rechercherPrestationNonAcm() {
        return new APRechercherTypeAcmServiceData(APAssuranceTypeAssociation.NONE.getCodesystemToString(), "",
                APTypeDePrestation.STANDARD);
    }

    private void validerParametres(final String idDroit, final String idAffilieEmployeur, final String method)
            throws APRechercherTypeAcmServiceException {

        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("APRechercherTypeAcmService.validerParametres() : idDroit");
        }

        if (JadeStringUtil.isBlankOrZero(idAffilieEmployeur)) {
            throw new IllegalArgumentException("APRechercherTypeAcmService.validerParametres() : idAffilieEmployeur");
        }

        if (JadeStringUtil.isBlankOrZero(method)) {
            throw new IllegalArgumentException("APRechercherTypeAcmService.validerParametres() : method");
        }

    }

}
