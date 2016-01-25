/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers.step3;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersCsvEntity;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersEnum;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private boolean bHistorique = false;
    private AMProcessRepriseSourciersCsvEntity currentCsvEntity = null;
    private JadeProcessEntity currentProcessEntity = null;
    private SimpleUploadFichierReprise currentUploadEntity = null;
    private Map<Enum<?>, String> data = null;
    private String idContribuable = null;

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#run()
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if (bHistorique) {
            return;
        }

        String anneeHistorique = data.get(AMProcessRepriseSourciersEnum.ANNEE);
        String anneeTaxation = data.get(AMProcessRepriseSourciersEnum.ANNEE_TAXATION);
        String idTaxation = "";

        // -----------------------------------------------------------------
        // 1) Récupération de l'id de taxation
        // -----------------------------------------------------------------
        Revenu lastSourcierTaxation = null;
        Contribuable currentDossier = AmalServiceLocator.getContribuableService().read(idContribuable);
        String idFamille = currentDossier.getFamille().getIdFamille();
        RevenuSearch taxationSearch = new RevenuSearch();
        taxationSearch.setForIdContribuable(idContribuable);
        taxationSearch.setForAnneeTaxation(anneeTaxation);
        taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
        if (taxationSearch.getSize() < 1) {
            JadeThread.logError("ERREUR TAXATIONS SOURCIER", "AUCUNE TAXATION POUR ANNEE " + anneeTaxation + ";"
                    + currentCsvEntity.toString());
            return;
        } else if (taxationSearch.getSize() == 1) {
            Revenu taxation = (Revenu) taxationSearch.getSearchResults()[0];
            idTaxation = taxation.getIdRevenu();
            lastSourcierTaxation = taxation;
        } else if (taxationSearch.getSize() > 1) {
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                if (currentTaxation.isSourcier()) {
                    if (JadeStringUtil.isBlankOrZero(idTaxation)) {
                        idTaxation = currentTaxation.getIdRevenu();
                        lastSourcierTaxation = currentTaxation;
                    } else {
                        try {
                            int idLastTaxation = Integer.parseInt(idTaxation);
                            int idCurrentTaxation = Integer.parseInt(currentTaxation.getIdRevenu());
                            if (idCurrentTaxation > idLastTaxation) {
                                idTaxation = currentTaxation.getIdRevenu();
                                lastSourcierTaxation = currentTaxation;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        if (JadeStringUtil.isBlankOrZero(idTaxation)) {
            JadeThread.logError("ERREUR TAXATIONS SOURCIER", "AUCUNE TAXATION POUR ANNEE " + anneeTaxation + ";"
                    + currentCsvEntity.toString());
            return;
        }

        // Recherche du dernier subside alloué, pour l'année de subside donnée en paramètre
        // Type demande doit être sourcier
        // ATMIS envoyée au contribuable (idFamille)
        // Dossier doit être complet (retour ATMIS)
        String dateFinLastSubsideSourcier = "";
        String dateDebutLastSubsideSourcier = "";
        boolean lastSubsideIsSourcier = false;
        SimpleDetailFamilleSearch subsidesSearch = new SimpleDetailFamilleSearch();
        subsidesSearch.setForAnneeHistorique(anneeHistorique);
        subsidesSearch.setForIdContribuable(idContribuable);
        subsidesSearch.setForCodeActif(true);
        subsidesSearch = AmalServiceLocator.getDetailFamilleService().search(subsidesSearch);
        for (int iSubside = 0; iSubside < subsidesSearch.getSize(); iSubside++) {
            SimpleDetailFamille subside = (SimpleDetailFamille) subsidesSearch.getSearchResults()[iSubside];
            if (subside.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue())) {
                if (subside.getIdFamille().equals(idFamille)) {
                    if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.ATMIS1.getValue())) {
                        // récupération des données de début droit et fin droit
                        if (!JadeStringUtil.isBlankOrZero(subside.getFinDroit())) {
                            dateFinLastSubsideSourcier = subside.getFinDroit();
                        }
                        if (!JadeStringUtil.isBlankOrZero(subside.getDebutDroit())) {
                            dateDebutLastSubsideSourcier = subside.getDebutDroit();
                        }
                        // Contrôle de la date de traitement de la taxation
                        if (lastSourcierTaxation != null) {
                            try {
                                RevenuFullComplex currentRevenu = AmalServiceLocator.getRevenuService()
                                        .readFullComplex(lastSourcierTaxation.getIdRevenu());
                                if (!JadeStringUtil.isEmpty(currentRevenu.getSimpleRevenu().getDateTraitement())) {
                                    lastSubsideIsSourcier = true;
                                    break;
                                } else {
                                    JadeThread.logWarn("WARNING TAXATIONS SOURCIER", "ATMIS NON TRAITEE "
                                            + anneeTaxation + ";" + currentCsvEntity.toString());
                                }
                            } catch (Exception ex) {
                                JadeThread.logError("ERREUR TAXATIONS SOURCIER", "ERREUR LECTURE TAXATION "
                                        + anneeTaxation + ";" + currentCsvEntity.toString() + ex.getMessage());
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (lastSubsideIsSourcier == false) {
            JadeThread.logInfo("INFO SOURCIER", "DERNIERS SUBSIDES DE TYPE NON SOURCIER " + anneeHistorique + ";"
                    + currentCsvEntity.toString());
            return;
        }

        // 1) initialisation calculssubsidescontainer
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue(), idTaxation, true);

        // Si tous les montants à 0 >> tous à refus
        // Si 1 montant à 0 et les autres >0 >> document famille modeste
        // Si tous les montants > 0 >> document famille

        // 2) Travail avec les subsides, set noModeles et supplextra
        int iNbZero = 0;
        int iNbNotZero = 0;
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            double subsideValue = 0.0;
            try {
                if (!JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribution())) {
                    subsideValue += Double.parseDouble(currentSubside.getMontantContribution());
                }
                if (!JadeStringUtil.isBlankOrZero(currentSubside.getMontantSupplement())) {
                    subsideValue += Double.parseDouble(currentSubside.getMontantSupplement());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (subsideValue > 0.0) {
                iNbNotZero++;
            } else {
                iNbZero++;
            }
        }

        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            double subsideValue = 0.0;

            try {
                if (!JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                    subsideValue += Double.parseDouble(currentSubside.getMontantContribSansSuppl());
                    if (!JadeStringUtil.isEmpty(currentSubside.getMontantSupplement())) {
                        subsideValue += Double.parseDouble(currentSubside.getMontantSupplement());
                    }
                } else {
                    if (!JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribution())) {
                        subsideValue += Double.parseDouble(currentSubside.getMontantContribution());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!JadeStringUtil.isBlankOrZero(dateFinLastSubsideSourcier)) {
                currentSubside.setFinDroit(dateFinLastSubsideSourcier);
            }
            if (!currentSubside.getDebutDroit().equals(dateDebutLastSubsideSourcier)) {
                currentSubside.setDebutDroit(dateDebutLastSubsideSourcier);
            }
            // si il n'y a pas de montant à 0, DECMISA
            // si il n'y a que des montants à 0, DECMISB, refus
            // si il y a des montants à 0 et >0, DECMISC, famille moyenne
            if (iNbZero == 0) {
                currentSubside.setRefus(false);
                currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMISA.getValue());
                currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                if (JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                    currentSubside.setMontantContribution(currentSubside.getMontantContribution());
                } else {
                    currentSubside.setMontantContribution(currentSubside.getMontantContribSansSuppl());
                }
                currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue());
                currentCalculs.getSubsides().set(iSubside, currentSubside);
            } else {
                if (iNbNotZero == 0) {
                    currentSubside.setRefus(true);
                    currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMISB.getValue());
                    currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                    if (JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribution());
                    } else {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribSansSuppl());
                    }
                    currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue());
                    currentCalculs.getSubsides().set(iSubside, currentSubside);
                } else {
                    currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                    if (JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribution());
                    } else {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribSansSuppl());
                    }
                    currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue());
                    if (subsideValue == 0.0) {
                        currentSubside.setRefus(true);
                        currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMISB.getValue());
                    } else {
                        currentSubside.setRefus(false);
                        currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMISC.getValue());
                    }
                    currentCalculs.getSubsides().set(iSubside, currentSubside);
                }
            }
        }

        // 3) Appel du service detailfamille.generatesubside
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception ex) {
            JadeThread.logError("ERROR SOURCIER", "CALCULS DES SUBSIDES ERROR " + ex.getMessage() + ";"
                    + currentCsvEntity.toString());
            return;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#setCurrentEntity(ch.globaz
     * .jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        currentProcessEntity = entity;
        String idUpload = entity.getIdRef();
        SimpleUploadFichierReprise fileUploaded = null;
        try {
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(idUpload);
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                currentCsvEntity = new AMProcessRepriseSourciersCsvEntity(fileUploaded.getXmlLignes());
                currentUploadEntity = fileUploaded;
                String nomPrenom = fileUploaded.getNomPrenom();
                if (nomPrenom.indexOf(";") >= 0) {
                    String[] infos = nomPrenom.split(";", 3);
                    idContribuable = infos[0];
                    if (infos[1].equals("H")) {
                        bHistorique = true;
                    } else {
                        bHistorique = false;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties#setProperties(java.util
     * .Map)
     */
    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        data = map;
    }

}
