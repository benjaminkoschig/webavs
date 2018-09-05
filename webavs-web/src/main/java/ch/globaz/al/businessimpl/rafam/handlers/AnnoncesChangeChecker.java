package ch.globaz.al.businessimpl.rafam.handlers;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyStatus;
import ch.globaz.al.business.constantes.enumerations.RafamLegalBasis;
import ch.globaz.al.business.constantes.enumerations.RafamOccupationStatus;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.rafam.InitAnnoncesRafamService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;

public class AnnoncesChangeChecker {

    /**
     * Vérifie s'il y a eu des modifications depuis l'envoi de la dernière annonce.
     *
     * @param annonce
     *            Nouvelle annonce correspondant à l'état actuel du droit/dossier
     *
     * @return <code>true</code> si des modifications ont été identifiées
     *
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static boolean hasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce,
            DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {

        if (lastAnnonce.isNew()) {
            return false;
        }

        if (statutDossierHasChanged(lastAnnonce, dossier)) {
            throw new ALRafamException(
                    "AnnonceHandlerAbstract#hasChanged : le statut du dossier a été modifiée après l'envoi d'une annonce RAFam. Dans ces cas de figure il est nécessaire de créer un nouveau dossier (n° "
                            + dossier.getId() + ")");
        }

        if (baseLegalHasChanged(lastAnnonce, dossier, droit)) {
            throw new ALRafamException(
                    "AnnonceHandlerAbstract#hasChanged : l'activité de l'allocataire a été modifiée après l'envoi d'une annonce RAFam. Dans ces cas de figure il est nécessaire de créer un nouveau dossier (n° "
                            + dossier.getId() + ")");
        }

        if (debutDroitHasChanged(annonce, lastAnnonce, droit)) {
            return true;
        }

        if (echeanceDroitHasChanged(annonce, lastAnnonce, droit)) {
            return true;
        }

        if (statutFamilialHasChanged(lastAnnonce, droit)) {
            return true;
        }

        if (typeActiviteHasChanged(lastAnnonce, dossier)) {
            return true;
        }

        if (nssAllocataireHasChanged(annonce, lastAnnonce)) {
            return true;
        }

        if (nssEnfantHasChanged(annonce, lastAnnonce)) {
            return true;
        }

        if (officeHasChanged(annonce, lastAnnonce)) {
            return true;
        }
        // Vérification du pays de résidence de l'enfant seulement avec la nouvelle version des schéma xsd
        if ("true".equals(JadePropertiesService.getInstance().getProperty(ALConstRafam.VERSION_ANNONCES_XSD_4_1))
                && paysResidenceEnfantChanged(annonce, lastAnnonce)) {
            return true;
        }

        return false;

    }

    private static boolean paysResidenceEnfantChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce) {
        return !lastAnnonce.getCodeCentralePaysEnfant().equals(annonce.getCodeCentralePaysEnfant());
    }

    private static boolean nssEnfantHasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce) {
        return !lastAnnonce.getNssEnfant().equals(annonce.getNssEnfant());
    }

    private static boolean nssAllocataireHasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce) {
        return !lastAnnonce.getNssAllocataire().equals(annonce.getNssAllocataire());
    }

    private static boolean typeActiviteHasChanged(AnnonceRafamModel lastAnnonce, DossierComplexModel dossier)
            throws JadeApplicationException {
        return !RafamOccupationStatus.getOccupationStatus(lastAnnonce.getCodeTypeActivite()).equals(
                RafamOccupationStatus.getOccupationStatusCS(dossier.getDossierModel().getActiviteAllocataire()));
    }

    private static boolean statutFamilialHasChanged(AnnonceRafamModel lastAnnonce, DroitComplexModel droit)
            throws JadeApplicationException {
        return !RafamFamilyStatus.getFamilyStatus(lastAnnonce.getCodeStatutFamilial())
                .equals(RafamFamilyStatus.getFamilyStatusCS(droit.getDroitModel().getStatutFamilial()));
    }

    private static boolean echeanceDroitHasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce,
            DroitComplexModel droit) {
        return !lastAnnonce.getEcheanceDroit()
                .equals((annonce == null ? droit.getDroitModel().getFinDroitForcee() : annonce.getEcheanceDroit()));
    }

    private static boolean debutDroitHasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce,
            DroitComplexModel droit) {
        return !lastAnnonce.getDebutDroit()
                .equals((annonce == null ? droit.getDroitModel().getDebutDroit() : annonce.getDebutDroit()));
    }

    private static boolean baseLegalHasChanged(AnnonceRafamModel lastAnnonce, DossierComplexModel dossier,
            DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {

        InitAnnoncesRafamService initAnnonceService = ALImplServiceLocator.getInitAnnoncesRafamService();

        return !RafamLegalBasis.getLegalBasis(lastAnnonce.getBaseLegale())
                .equals(initAnnonceService.getBaseLegale(dossier, droit));
    }

    private static boolean officeHasChanged(AnnonceRafamModel annonce, AnnonceRafamModel lastAnnonce)
            throws JadeApplicationException, JadePersistenceException {

        boolean legalOfficeHasChanged = !lastAnnonce.getLegalOffice().equals(annonce.getLegalOffice());
        boolean officeBranchHasChanged = !lastAnnonce.getOfficeBranch().equals(annonce.getOfficeBranch());
        boolean officeIdentifierHasChanged = !lastAnnonce.getOfficeIdentifier().equals(annonce.getOfficeIdentifier());

        return legalOfficeHasChanged || officeBranchHasChanged || officeIdentifierHasChanged;
    }

    /**
     * Vérifie si le statut du dossier en cours de traitement à changé depuis l'enregistrement de la dernière annonce.
     *
     * @return <code>true</code> si le statut à changé, <code>false</code> sinon
     *
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static boolean statutDossierHasChanged(AnnonceRafamModel lastAnnonce, DossierComplexModel dossier)
            throws JadeApplicationException, JadePersistenceException {

        if (lastAnnonce.isNew()) {
            return false;
        } else {

            String currentStatus = dossier.getDossierModel().getStatut();

            switch (RafamFamilyAllowanceType.getFamilyAllowanceType(lastAnnonce.getGenrePrestation())) {
                case ADI:
                    return !ALCSDossier.STATUT_IS.equals(currentStatus);

                case ADC:
                case DIFFERENCE_ADOPTION:
                case DIFFERENCE_NAISSANCE:
                    return !ALCSDossier.STATUT_CS.equals(currentStatus);

                default:
                    return (ALCSDossier.STATUT_IS.equals(currentStatus) || ALCSDossier.STATUT_CS.equals(currentStatus));
            }
        }
    }
}
