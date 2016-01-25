package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.utils.compta.PRRubriqueComptableResolver;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

public class HandlerRubrique {

    /**
     * Permet de trouver le code système de la rubrique standard à utiliser.
     * 
     * @param ov
     * @return
     * @throws JadeApplicationException
     */
    private static String determineCsNormal(OrdreVersement ov) throws JadeApplicationException {

        // DetermineSousCodePreation sousCode = DetermineSousCodePreation.factory();
        // String rubrique = sousCode.determineCsNormal(ov);
        String rubrique;
        try {

            if (IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL.equals(ov.getCsType())) {
                if (ov.getCsTypeDomaine().equals(IPCOrdresVersements.CS_DOMAINE_AI)) {
                    return APIReferenceRubrique.PC_AI_ALLOCATIONS_DE_NOEL;
                } else if (ov.getCsTypeDomaine().equals(IPCOrdresVersements.CS_DOMAINE_AVS)) {
                    return APIReferenceRubrique.PC_AVS_ALLOCATIONS_DE_NOEL;
                } else {
                    throw new ComptabiliserLotException(
                            "Unable to find the rubrique whit this domaine for the allocation de noel: "
                                    + ov.getCsTypeDomaine());
                }
            } else {
                String genrePresation = HandlerRubrique.resolveGenrePrestaion(ov.getCsTypeDomaine());

                rubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(genrePresation,
                        ov.getSousTypeGenrePrestation());
            }
        } catch (Exception e) {
            throw new ComptabiliserLotException("Unable to find the rubrique whit this soustypeGrenrePresation: "
                    + ov.getSousTypeGenrePrestation(), e);
        }
        if (rubrique == null) {
            throw new ComptabiliserLotException("Unable to map the csRubriqueRestitution with this value("
                    + ov.getSousTypeGenrePrestation() + ")");
        }

        return rubrique;
    }

    /**
     * Permet de trouver le code système de la rubrique de restitution à utiliser.
     * 
     * @param ovRestitution
     * @return code system qui dÃ©finit la rubrique
     * @throws JadeApplicationException
     */
    private static String determineCsRestitution(OrdreVersement ov) throws JadeApplicationException {
        String genrePresation = HandlerRubrique.resolveGenrePrestaion(ov.getCsTypeDomaine());
        String rubrique;

        try {
            rubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMrestitution(genrePresation,
                    ov.getSousTypeGenrePrestation());
        } catch (Exception e) {
            throw new ComptabiliserLotException("Unable to find the rubrique whit this soustypeGrenrePresation: "
                    + ov.getSousTypeGenrePrestation(), e);
        }

        if (rubrique == null) {
            throw new ComptabiliserLotException("Unable to map the csRubriqueRestitution with this value("
                    + ov.getSousTypeGenrePrestation() + ")");
        }
        return rubrique;
    }

    /**
     * Permet de retrouver en partie le genre de prestation avec le domaine de l'ordre ce versement
     * 
     * @param ov
     * @return
     * @throws ComptabiliserLotException
     */
    private static String resolveGenrePrestaion(String csTypeDomaine) throws ComptabiliserLotException {
        String genrePresation = null;
        // On ne peut pas complètement résoudre les genre de prestation car on mixte les code 110 et 113 dans le
        // domaine AI
        if (IPCOrdresVersements.CS_DOMAINE_AI.equals(csTypeDomaine)) {
            genrePresation = PRCodePrestationPC._150.getCodePrestationAsString();
        } else if (IPCOrdresVersements.CS_DOMAINE_AVS.equals(csTypeDomaine)) {
            genrePresation = PRCodePrestationPC._110.getCodePrestationAsString();
        } else {
            throw new ComptabiliserLotException("Unable to resolve the genre presations with thi csDomaine: "
                    + csTypeDomaine);
        }
        return genrePresation;
    }

    /**
     * Permet de retrouve l'idrefRubrique pour un domaine et en fonction du type d'ov
     * 
     * @param ov
     * @return
     * @throws JadeApplicationException
     */
    public static String resolveIdRefRubrique(OrdreVersement ov) throws JadeApplicationException {
        if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ov.getCsType())
                || IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL.equals(ov.getCsType())) {
            return HandlerRubrique.determineCsNormal(ov);
        } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(ov.getCsType())) {
            return HandlerRubrique.determineCsRestitution(ov);
        } else {
            throw new ComptabiliserLotException("Unable to resolve the genre presations with this csType ov: "
                    + ov.getCsType() + ".For this ov: " + ov.toString());
        }
    }
}
