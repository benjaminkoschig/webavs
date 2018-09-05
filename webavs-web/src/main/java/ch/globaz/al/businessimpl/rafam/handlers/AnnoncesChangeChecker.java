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
     * V�rifie s'il y a eu des modifications depuis l'envoi de la derni�re annonce.
     *
     * @param annonce
     *            Nouvelle annonce correspondant � l'�tat actuel du droit/dossier
     *
     * @return <code>true</code> si des modifications ont �t� identifi�es
     *
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
                    "AnnonceHandlerAbstract#hasChanged : le statut du dossier a �t� modifi�e apr�s l'envoi d'une annonce RAFam. Dans ces cas de figure il est n�cessaire de cr�er un nouveau dossier (n� "
                            + dossier.getId() + ")");
        }

        if (baseLegalHasChanged(lastAnnonce, dossier, droit)) {
            throw new ALRafamException(
                    "AnnonceHandlerAbstract#hasChanged : l'activit� de l'allocataire a �t� modifi�e apr�s l'envoi d'une annonce RAFam. Dans ces cas de figure il est n�cessaire de cr�er un nouveau dossier (n� "
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
        // V�rification du pays de r�sidence de l'enfant seulement avec la nouvelle version des sch�ma xsd
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
     * V�rifie si le statut du dossier en cours de traitement � chang� depuis l'enregistrement de la derni�re annonce.
     *
     * @return <code>true</code> si le statut � chang�, <code>false</code> sinon
     *
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
