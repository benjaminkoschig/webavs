package ch.globaz.al.businessimpl.services.attestationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstAttestattionVersees;
import ch.globaz.al.business.exceptions.declarationVersement.ALAttestationVersementException;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.attestationVersement.AttestationsVersementBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

public class AttestationsVersementBusinessServiceImpl implements AttestationsVersementBusinessService {

    /**
     * méthode qui ajoute lea prestations à une liste
     * 
     * @param dossierAttestation
     * @param searchList
     * @param searchAttestation
     * @throws JadeApplicationException
     */
    private void generListPrestations(ArrayList<DeclarationVersementDetailleComplexModel> searchList,
            DeclarationVersementDetailleSearchComplexModel searchAttestation) throws JadeApplicationException {
        // contrôle des paramètre
        if (searchList == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#generListPrestations: searchList is null");
        }

        if (searchAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#generListPrestations: searchAttestation is null");
        }
        if (searchAttestation.getSize() > 0) {
            for (int j = 0; j < searchAttestation.getSize(); j++) {

                // ArrayList temporaire
                DeclarationVersementDetailleComplexModel detailPresta = (DeclarationVersementDetailleComplexModel) searchAttestation
                        .getSearchResults()[j];

                searchList.add(detailPresta);

            }
        }

    }

    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> returnListPaiement(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#returnListPaiement: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#returnListPaiement: date periodeDe " + periodeDe
                            + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#returnListPaiement: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }
        if (typePrestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#returnListPaiement: typePresta is null");
        }

        if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIRECT, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementDirect(dossierAttestation,
                    periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_INDIRECT, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementIndirect(
                    dossierAttestation, periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_TIERS_BEN, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementTiersBeneficiaire(
                    dossierAttestation, periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIR_INDIR, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementDirectIndirect(
                    dossierAttestation, periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIR_TIERS_BEN, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementDirectTiersBeneficiaire(
                    dossierAttestation, periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_IND_TIERS_BEN, false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService().searchPaiementIndirectTierBeneficaires(
                    dossierAttestation, periodeDe, periodeA);
        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_IND_DIR_TIERS_BEN,
                false)) {
            return ALServiceLocator.getAttestationsVersementBusinessService()
                    .searchPaiementDirectIndirectTiersBeneficaire(dossierAttestation, periodeDe, periodeA);
        } else {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#returnListPaiement: typePresta is " + typePrestation
                            + " doesn't exist");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#searchPaiementdirect(
     * ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String, java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirect: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirect: date periodeDe " + periodeDe
                            + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirect: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_DIRECT);
            typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setForTiers(declarDetail.getTiersAlloc());
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#searchPaiementDirectIndirect
     * (ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirect: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirect: date periodeDe " + periodeDe
                            + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirect: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_INDIRECT);
            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#
     * searchPaiementDirectIndirectTiersBeneficaire
     * (ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectIndirectTiersBeneficaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirectTiersBeneficaire: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirectTiersBeneficaire: date periodeDe "
                            + periodeDe + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectIndirectTiersBeneficaire: date periodeA "
                            + periodeA + "is not a valid globaz Date");
        }

        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();
            ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_INDIRECT);
            typeBoni.add(ALCSPrestation.BONI_DIRECT);
            typeBoni.add(ALCSPrestation.BONI_RESTITUTION);
            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#
     * searchPaiementDirectTiersBeneficiaire
     * (ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementDirectTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectTiersBeneficiaire: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectTiersBeneficiaire: date periodeDe "
                            + periodeDe + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementDirectTiersBeneficiaire: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_DIRECT);
            typeBoni.add(ALCSPrestation.BONI_RESTITUTION);
            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setNotForTiers(declarDetail.getTiersAlloc());
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#searchPaiementIndirect
     * (ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirect(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirect: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirect: date periodeDe " + periodeDe
                            + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirect: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_INDIRECT);

            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#
     * searchPaiementIndirectTierBeneficaires
     * (ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementIndirectTierBeneficaires(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirectTierBeneficaires: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirectTierBeneficaires: date periodeDe "
                            + periodeDe + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementIndirectTierBeneficaires: date periodeA "
                            + periodeA + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_DIRECT);
            typeBoni.add(ALCSPrestation.BONI_RESTITUTION);
            typeBoni.add(ALCSPrestation.BONI_INDIRECT);

            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setNotForTiers(declarDetail.getTiersAlloc());
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.AttestationVersementService.AttestationsVersementService#
     * searchPaiementTiersBeneficiaire(ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> searchPaiementTiersBeneficiaire(
            DossierAttestationVersementComplexSearchModel dossierAttestation, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        if (dossierAttestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementTiersBeneficiaire: dossierAttestaion is null");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementTiersBeneficiaire: date periodeDe " + periodeDe
                            + "is not a valid globaz Date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementServiceImpl#searchPaiementTiersBeneficiaire: date periodeA " + periodeA
                            + "is not a valid globaz Date");
        }

        ArrayList<DeclarationVersementDetailleComplexModel> searchList = new ArrayList<DeclarationVersementDetailleComplexModel>();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listAttestation = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        for (int i = 0; i < dossierAttestation.getSize(); i++) {

            // ArrayList temporaire
            DossierAttestationVersementComplexModel declarDetail = (DossierAttestationVersementComplexModel) dossierAttestation
                    .getSearchResults()[i];

            DeclarationVersementDetailleSearchComplexModel searchAttestation = new DeclarationVersementDetailleSearchComplexModel();

            // type de boni
            HashSet<String> typeBoni = new HashSet<String>();
            typeBoni.add(ALCSPrestation.BONI_RESTITUTION);
            typeBoni.add(ALCSPrestation.BONI_DIRECT);

            // FIXME à voir si problème avec restittiution non actif_indirect (ccju et ccvd)
            // typeBoni.add(ALCSPrestation.BONI_RESTITUTION);

            searchAttestation.setInBonification(typeBoni);
            searchAttestation.setForDateDebut(periodeDe);
            searchAttestation.setForDateFin(periodeA);
            searchAttestation.setNotForTiers(declarDetail.getTiersAlloc());
            searchAttestation.setForIdDossier(declarDetail.getIdDossier());
            searchAttestation.setForEtat(ALCSPrestation.ETAT_CO);
            searchAttestation.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchAttestation.setOrderKey("periode");

            searchAttestation = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                    searchAttestation);

            generListPrestations(searchList, searchAttestation);

            if (searchList.size() > 0) {
                listAttestation.add(searchList);
            }
        }

        return listAttestation;
    }
}
