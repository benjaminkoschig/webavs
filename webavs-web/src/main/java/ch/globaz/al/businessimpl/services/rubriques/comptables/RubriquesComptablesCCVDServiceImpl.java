package ch.globaz.al.businessimpl.services.rubriques.comptables;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCVDService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Impl�mentation du service sp�cifique permettant de r�cup�rer une rubrique comptable pour la CCVD
 * 
 * @author jts
 * 
 */
public class RubriquesComptablesCCVDServiceImpl extends RubriquesComptablesServiceImpl implements
        RubriquesComptablesCCVDService {

    @Override
    protected String getRubriqueADI(DossierModel dossier, EntetePrestationModel entete, DetailPrestationModel detail,
            String date) throws JadeApplicationException, JadePersistenceException {

        if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF_ADI);
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT_ADI);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_ADI);
        }

    }

    @Override
    protected String getRubriqueAgriculteur(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE_ADI);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE);
        }
    }

    @Override
    protected String getRubriqueCollaborateurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE_ADI);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE);
        }
    }

    @Override
    protected String getRubriquePecheur(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {
        return getRubriqueAgriculteur(dossier, entete, detail, date);
    }

    @Override
    protected String getRubriqueRestitution(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
        CategorieTarifComplexModel categorieTarif = getCategorieTarif(detail.getCategorieTarif());
        String rubriqueAffiliation = assurance.getLibelleCourt();

        if (ALCSTarif.LEGISLATION_AGRICOLE.equals(categorieTarif.getLegislationTarifModel().getTypeLegislation())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_LOI_FEDERALE);
        } else if (ALCSCantons.VS.equals(ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                assurance.getCanton()))) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_VS_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_H.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_H_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RA.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RA_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RB.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RB_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RC.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RC_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RD.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RD_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RE.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RE_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RF.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RF_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RL.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RL_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_S.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_S_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_TSE.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_TSE_STANDARD);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_EGLISE.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_RL_STANDARD);
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_RESTITUTION_NON_ACTIF);
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION_INDEPENDANT);
        } else if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION_SALARIE);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_RESTITUTION);
        }
    }

    @Override
    public String getRubriqueForIS(DossierModel dossier, String cantonImposition, String date) throws JadeApplicationException, JadePersistenceException {
        AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
        String rubriqueAffiliation = assurance.getLibelleCourt();
        if (JadeStringUtil.isBlankOrZero("cantonImposition") &&
                ALCSTarif.CATEGORIE_VS.equals(ALImplServiceLocator.getCalculService().getTarifForCanton(
                cantonImposition))) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_VS_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_H.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_H_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RA.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RA_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RB.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RB_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RC.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RC_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RD.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RD_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RE.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RE_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RF.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RF_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RL.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_S.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_S_IS);
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_TSE.equals(rubriqueAffiliation)) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_IS);
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF_IS);
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT_IS);
        } else if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE_IS);
        } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(dossier.getActiviteAllocataire())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_TRAVAILLEUR_AGRICOLE_IS);
        }
        return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_IS);
    }

    @Override
    protected String getRubriqueSalarie(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        AssuranceInfo assurance = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier, date);
        String rubriqueAffiliation = assurance.getLibelleCourt();

        if (ALCSCantons.VS.equals(ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                assurance.getCanton()))) {

            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_VS_NAISSANCE);
            } else {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_VS_STANDARD);
            }
        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_H.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_H_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_H_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RA.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RA_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RA_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RB.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RB_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RB_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RC.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RC_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RC_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RD.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RD_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RD_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RE.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RE_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RE_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RF.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RF_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RF_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_RL.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_S.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_S_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_S_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_TSE.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_EGLISE.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_STANDARD);

        } else if (RubriquesComptablesCCVDService.COTIS_ALLOC_FAM_LAVS.equals(rubriqueAffiliation)) {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_NAISSANCE);
            }
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_STANDARD);

        } else {
            if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
                    || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
                return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_SALARIE_NAISSANCE);
            }

            return super.getRubriqueSalarie(dossier, entete, detail, date);
        }
    }

    @Override
    protected String getRubriqueTravailleurAgricole(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        if (ALCSPrestation.STATUT_ADI.equals(entete.getStatut())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE_ADI);
        } else if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())) {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_CAISSE_TRAVAILLEUR_AGRICOLE_NAISSANCE);
        } else {
            return getRubrique(date, ALConstRubriques.RUBRIQUE_STANDARD_TRAVAILLEUR_AGRICOLE);
        }
    }

    @Override
    public List<String> getAllRubriquesForIS(String caisse, String date) throws JadeApplicationException, JadePersistenceException {
        List<String> rubriques = new ArrayList<>();

        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_VS_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_H_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RA_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RB_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RC_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RD_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RE_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RF_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_RL_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_S_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_SALARIE_TSE_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_STANDARD_NON_ACTIF_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_STANDARD_INDEPENDANT_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_CAISSE_AGRICOLE_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_STANDARD_TRAVAILLEUR_AGRICOLE_IS);
        rubriques.add(ALConstRubriques.RUBRIQUE_STANDARD_IS);

        return rubriques;
    }
}
