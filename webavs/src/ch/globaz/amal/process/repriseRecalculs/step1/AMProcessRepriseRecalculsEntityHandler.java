package ch.globaz.amal.process.repriseRecalculs.step1;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMDocumentModeles;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTaxationType;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

public class AMProcessRepriseRecalculsEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {
    private String dateAvisTaxation = null;
    private String idContribuable = null;
    private String idDetailFamille = null;
    private SimpleDetailFamille simpleDetailFamille = null;

    private Integer _calculRevenuDeterminant(String idTaxation, Integer anneeHistoriqueTaxation)
            throws RevenuException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idTaxation)) {
            throw new RevenuException("idTaxation can't be null or empty !");
        }

        if (anneeHistoriqueTaxation == 0) {
            throw new RevenuException("Année historique can't be zero !");
        }

        RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex(idTaxation);
        RevenuHistoriqueComplex revenuHistorique = new RevenuHistoriqueComplex();
        revenuHistorique.setRevenuFullComplex(revenuFullComplex);

        revenuHistorique.getSimpleRevenuHistorique().setAnneeHistorique(anneeHistoriqueTaxation.toString());
        revenuHistorique.setRevenuFullComplex(revenuHistorique.getRevenuFullComplex());
        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
        try {
            revenuHistorique = calculsRevenuFormules.doCalcul(revenuHistorique);
        } catch (Exception ex) {
            throw new RevenuException("Erreur pendant le calcul du revenu déterminant ==> " + ex.getMessage());
        }
        if (revenuHistorique == null) {
            throw new RevenuException("Pas de calcul du revenu possible !;");
        }
        Integer revenuDeterminant = new Integer(revenuHistorique.getSimpleRevenuDeterminant()
                .getRevenuDeterminantCalcul());

        return revenuDeterminant;

    }

    /**
     * Récupère l'ENUM qui correspond au modèle du subside
     * 
     * @return
     * @throws JadeNoBusinessLogSessionError
     */
    private AMDocumentModeles _getModelACREPEnum() throws Exception {
        String noModele = simpleDetailFamille.getNoModeles();
        AMDocumentModeles modelACREP = null;
        if (AMDocumentModeles.ACREP10.getValue().equals(noModele)) {
            modelACREP = AMDocumentModeles.ACREP10;
        } else if (AMDocumentModeles.ACREP11.getValue().equals(noModele)) {
            modelACREP = AMDocumentModeles.ACREP11;
        } else if (AMDocumentModeles.ACREP12.getValue().equals(noModele)) {
            modelACREP = AMDocumentModeles.ACREP12;
        } else if (AMDocumentModeles.ACREP13.getValue().equals(noModele)) {
            modelACREP = AMDocumentModeles.ACREP13;
        } else {
            throw new Exception("Model must be ACREP10+ !");
        }
        return modelACREP;
    }

    private RevenuHistoriqueComplex _getRevenuActuel(Integer anneeRecalcul) throws Exception {
        RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = new RevenuHistoriqueComplexSearch();
        revenuHistoriqueComplexSearch.setForAnneeHistorique(anneeRecalcul.toString());
        revenuHistoriqueComplexSearch.setForIdContribuable(idContribuable);
        revenuHistoriqueComplexSearch.setForRevenuActif(true);
        revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService().search(revenuHistoriqueComplexSearch);

        RevenuHistoriqueComplex revenuHistoriqueComplex = null;
        if (revenuHistoriqueComplexSearch.getSize() == 1) {
            revenuHistoriqueComplex = (RevenuHistoriqueComplex) revenuHistoriqueComplexSearch.getSearchResults()[0];
            return revenuHistoriqueComplex;
            // String revDet = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();
            // revDet = JANumberFormatter.fmt(revDet, false, false, false, 0);
            // return Integer.parseInt(revDet);
        } else if (revenuHistoriqueComplexSearch.getSize() > 1) {
            throw new Exception("Plusieurs revenus trouvés pour l'année " + anneeRecalcul);
        } else {
            throw new Exception("Revenu non trouvée pour l'année " + anneeRecalcul);
        }
    }

    private Revenu _getRevenuForRecalcul(Integer anneeRecalcul, String idTaxationUtilisee) throws Exception {
        RevenuSearch revenuSearch = new RevenuSearch();
        revenuSearch.setForAnneeTaxation(anneeRecalcul.toString());
        revenuSearch.setForIdContribuable(idContribuable);
        // Attention : Le forIdRevenu est un NOT EQUALS avec la whereKey "repriseRecalculs"
        revenuSearch.setForIdRevenu(idTaxationUtilisee);
        ArrayList<String> inTypeTaxationArray = new ArrayList<String>();
        inTypeTaxationArray.add(AMTaxationType.ORDINAIRE.getValue());
        revenuSearch.setInTypeTaxation(inTypeTaxationArray);
        revenuSearch.setWhereKey("repriseRecalculs");
        revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);

        if (revenuSearch.getSize() > 0) {
            return (Revenu) revenuSearch.getSearchResults()[0];
        } else {
            return null;
        }
    }

    private Integer _retrieveMontantSubside(String year, Integer revenuDeterminant) throws Exception {
        if (JadeStringUtil.isBlankOrZero(year)) {
            throw new Exception("Year can't be null or empty !");
        }

        if (revenuDeterminant > 99999) {
            revenuDeterminant = 99999;
        }

        SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
        simpleSubsideAnneeSearch.setForAnneeSubside(year);
        simpleSubsideAnneeSearch.setForLimiteRevenuGOE(revenuDeterminant.toString());
        simpleSubsideAnneeSearch.setOrderKey("limiteRevenuAsc");

        simpleSubsideAnneeSearch = AmalServiceLocator.getSimpleSubsideAnneeService().search(simpleSubsideAnneeSearch);

        if (simpleSubsideAnneeSearch.getSize() > 0) {
            SimpleSubsideAnnee subsideAnnee = (SimpleSubsideAnnee) simpleSubsideAnneeSearch.getSearchResults()[0];
            String s_subside = JANumberFormatter.fmt(subsideAnnee.getSubsideAdulte(), false, false, false, 0);
            return Integer.parseInt(s_subside);
        } else {
            throw new Exception("Aucun subside trouvé pour l'année " + year + " et le revenu déterminant "
                    + revenuDeterminant + "!");
        }

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        try {
            // Tout d'abord, vérifier que nous avons bien un subside
            if ((simpleDetailFamille == null) || simpleDetailFamille.isNew()) {
                JadeThread.logError(null, "SimpleDetailFamille can't be null !");
                return;
            }

            Integer anneeSubside = Integer.parseInt(simpleDetailFamille.getAnneeHistorique());
            Integer anneeRecalcul = 0;
            boolean dejaEuDroit = false;

            // Vérifier de quel ACREP il s'agit
            AMDocumentModeles modelACREP = _getModelACREPEnum();

            switch (modelACREP) {
                case ACREP10:
                    // Demande recalcul deja eu droit. N-1
                    anneeRecalcul = anneeSubside - 1;
                    dejaEuDroit = true;
                    break;
                case ACREP11:
                    // Demande recalcul deja eu droit. N-2
                    anneeRecalcul = anneeSubside - 2;
                    dejaEuDroit = true;
                    break;
                case ACREP12:
                    // Demande recalcul pas eu droit. N-1
                    anneeRecalcul = anneeSubside - 1;
                    break;
                case ACREP13:
                    // Demande recalcul pas eu droit. N-2
                    anneeRecalcul = anneeSubside - 2;
                    break;
            }

            RevenuHistoriqueComplex revenuActuel = _getRevenuActuel(anneeSubside);
            String idTaxationUtilisee = revenuActuel.getRevenuFullComplex().getSimpleRevenu().getIdRevenu();

            // Si c'est un recalcul année - 1, on se fout de la taxation utilisée puisqu'on prend celle d'une autre
            // année
            if (AMDocumentModeles.ACREP10.equals(modelACREP) || AMDocumentModeles.ACREP12.equals(modelACREP)) {
                idTaxationUtilisee = null;
            }

            Revenu taxationPourRecalcul = _getRevenuForRecalcul(anneeRecalcul, idTaxationUtilisee);

            if (taxationPourRecalcul == null) {
                JadeThread.logWarn(null, "Nouvelle taxation absente!");
                return;
            }

            String idTaxationRecalcul = taxationPourRecalcul.getIdRevenu();

            Integer montantSubsideActuel = 0;
            if (dejaEuDroit) {
                String s_montantSubsideActuel = JANumberFormatter.fmt(
                        simpleDetailFamille.getMontantContributionAvecSupplExtra(), false, false, false, 0);
                montantSubsideActuel = Integer.parseInt(s_montantSubsideActuel);
            }

            // Calcul du revenu déterminant avec la nouvelle taxation
            Integer revenuDeterminantRecalcul = _calculRevenuDeterminant(taxationPourRecalcul.getIdRevenu(),
                    anneeSubside);
            Integer montantSubsideRecalcul = _retrieveMontantSubside(anneeSubside.toString(), revenuDeterminantRecalcul);

            String idTaxation = idTaxationRecalcul;
            AMDocumentModeles modelDECMSTRecalcul = AMDocumentModeles.DECMST5;
            switch (modelACREP) {
                case ACREP10:
                    // Demande recalcul deja eu droit. N-1
                    if (montantSubsideActuel >= montantSubsideRecalcul) {
                        modelDECMSTRecalcul = AMDocumentModeles.DECMST10;
                        JadeThread.logInfo("Attribution", "DECMST10");
                    }
                    break;
                case ACREP11:
                    // Demande recalcul deja eu droit. N-2
                    if (montantSubsideActuel >= montantSubsideRecalcul) {
                        modelDECMSTRecalcul = AMDocumentModeles.DECMST11;
                        JadeThread.logInfo("Attribution", "DECMST11");
                    }
                    break;
                case ACREP12:
                    // Demande recalcul pas eu droit. N-1
                    if (montantSubsideRecalcul == 0) {
                        modelDECMSTRecalcul = AMDocumentModeles.DECMST12;
                        JadeThread.logInfo("Attribution", "DECMST12");
                    }
                    break;
                case ACREP13:
                    // Demande recalcul pas eu droit. N-2
                    if (montantSubsideRecalcul == 0) {
                        modelDECMSTRecalcul = AMDocumentModeles.DECMST13;
                        JadeThread.logInfo("Attribution", "DECMST13");
                    }
                    break;
            }

            if (modelDECMSTRecalcul == AMDocumentModeles.DECMST5) {
                JadeThread.logInfo("Attribution", "DECMST5");
            }

            CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable,
                    anneeSubside.toString(), IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue(), idTaxation, true);

            for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
                SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);

                // Si c'est un DECMST10+ et que l'on n'est pas sur le subside du membre qui a eu l'ACREP10+ on sort.
                if ((modelDECMSTRecalcul != AMDocumentModeles.DECMST5)
                        && !currentSubside.getIdFamille().equals(simpleDetailFamille.getIdFamille())) {
                    currentCalculs.getSubsides().remove(iSubside);
                    iSubside--;
                    continue;
                }

                currentSubside.setRefus(false);
                currentSubside.setNoModeles(modelDECMSTRecalcul.getValue());
                currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue());

                // Si on a un DECMST10+, il faut conserver les montants du subside
                if (modelDECMSTRecalcul != AMDocumentModeles.DECMST5) {
                    currentSubside.setMontantContribAnnuelle(simpleDetailFamille.getMontantContribAnnuelle());
                    currentSubside.setMontantContribSansSuppl(simpleDetailFamille.getMontantContribSansSuppl());
                    currentSubside.setMontantContribution(simpleDetailFamille.getMontantContribution());
                    currentSubside.setMontantExact(simpleDetailFamille.getMontantExact());
                    currentSubside.setMontantSupplement(simpleDetailFamille.getMontantSupplement());
                    currentSubside.setTypeDemande(simpleDetailFamille.getTypeDemande());
                    currentSubside.setCodeActif(simpleDetailFamille.getCodeActif());
                    currentSubside.setAnnonceCaisseMaladie(simpleDetailFamille.getAnnonceCaisseMaladie());
                }

                currentCalculs.getSubsides().set(iSubside, currentSubside);
            }

            boolean isRecalculUnfavorable = false;
            if (modelDECMSTRecalcul != AMDocumentModeles.DECMST5) {
                isRecalculUnfavorable = true;
            }

            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue(), isRecalculUnfavorable);

        } catch (Exception ex) {
            JadeThread.logError("Recalcul", ex.getMessage());
        } finally {
            // JadeThread.logError("ROLLBACK", "ROLLBACK");
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        idContribuable = entity.getIdRef();
        idDetailFamille = entity.getValue1();

        try {
            simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
        } catch (Exception e) {
            JadeThread.logError("AMProcessRepriseRecalculsEntityHandler.setCurrentEntity", e.getMessage());
        }
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {

    }

}
