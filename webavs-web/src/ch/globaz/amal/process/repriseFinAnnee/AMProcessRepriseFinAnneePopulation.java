/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneePopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<AMProcessRepriseFinAnneeEnum> {
    private ContainerParametres containerParametres = null;
    private Map<AMProcessRepriseFinAnneeEnum, String> data = null;

    private boolean checkLastTaxation(String idContribuable) {
        // contrôle si taxation 2010 stockée
        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 2);
        RevenuSearch taxationSearch = new RevenuSearch();
        taxationSearch.setForAnneeTaxation(anneeTaxation);
        taxationSearch.setForIdContribuable(idContribuable);
        try {
            boolean bFoundTaxation = false;
            taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                    if (currentTaxation.getTypeTaxation()
                            .equals(IAMCodeSysteme.AMTaxationType.OFFICE_TOTAUX.getValue())
                            || currentTaxation.getTypeTaxation().equals(
                                    IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                        bFoundTaxation = true;
                        break;
                    }
                }
            }
            return bFoundTaxation;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Type assisté doit être pris en compte dans la population si attribution année-1, et dernier document = DECMAS7,
     * DECMASM
     * 
     * @param subside
     * @return
     */
    private boolean checkSubsideAssiste(SimpleDetailFamille subside) {
        // Si refus, pas de prise en compte
        if (subside.getRefus()) {
            return false;
        }
        // Si fin droit, droit terminé
        if (!JadeStringUtil.isBlankOrZero(subside.getFinDroit())) {
            return false;
        }
        // check document
        if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMAS7.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMASM.getValue())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Type PC doit être pris en compte dans la population si attribution année-1 et dernier document = DECMPC7, DECMPCM
     * 
     * @param subside
     * @return
     */
    private boolean checkSubsidePC(SimpleDetailFamille subside) {
        // Si refus, pas de prise en compte
        if (subside.getRefus()) {
            return false;
        }
        // Si fin droit, droit terminé
        if (!JadeStringUtil.isBlankOrZero(subside.getFinDroit())) {
            return false;
        }
        // check document
        if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMPC7.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMPCM.getValue())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Type sourcier doit être prise en compte dans la population si attribution année - 1 c'est à dire si dernier
     * document = DECMIS1, DECMISA, DECMISC
     * 
     * @param subside
     * @return
     */
    private boolean checkSubsideSourcier(SimpleDetailFamille subside) {
        // Si refus, pas de prise en compte
        if (subside.getRefus()) {
            return false;
        }
        // Si fin droit, droit terminé
        if (!JadeStringUtil.isBlankOrZero(subside.getFinDroit())) {
            return false;
        }
        // check document
        if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMIS1.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMISA.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMISC.getValue())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Selon derniers envois xml du service des contributions, seront dans ce cas :
     * 
     * taxation définitives reçues, mais paramètres pas encore introduits ex 2012, taxation 2010 stockée et revenu
     * déterminant 2010 pas encore calculé
     * 
     * @param subside
     * @return
     */
    private boolean checkSubsideStandard(SimpleDetailFamille subside) {
        // Si refus, pas de prise en compte
        if (subside.getRefus()) {
            return false;
        }
        // Si fin droit, droit terminé
        if (!JadeStringUtil.isBlankOrZero(subside.getFinDroit())) {
            return false;
        }
        // Contrôle des types de documents
        if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST1.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST2.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST3.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST5.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST8.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMPCE.getValue())
                || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMASB.getValue())) {
            // contrôle si taxation 2010 stockée
            String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
            String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 2);
            RevenuSearch taxationSearch = new RevenuSearch();
            taxationSearch.setForAnneeTaxation(anneeTaxation);
            taxationSearch.setForIdContribuable(subside.getIdContribuable());
            try {
                boolean bFoundTaxation = false;
                String idTaxation = "";
                taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
                for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                    Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                    if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                        if (currentTaxation.getTypeTaxation().equals(
                                IAMCodeSysteme.AMTaxationType.OFFICE_TOTAUX.getValue())
                                || currentTaxation.getTypeTaxation().equals(
                                        IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                            bFoundTaxation = true;
                            idTaxation = currentTaxation.getId();
                            break;
                        }
                    }
                }
                // Si nous trouvons une taxation et que le dernier document envoyé était un DECMST8
                // Il s'agit d'une famille moyenne
                // Dans ce cas, contrôler que la nouvelle taxation provoque un RDU de 33'000 à 40'000
                // Si inférieur à 33'000, il faudra envoyer un ATSUB et considérer le dossier comme étant un nouveau
                // dossier
                if (bFoundTaxation) {

                    RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = new RevenuHistoriqueComplexSearch();
                    revenuHistoriqueComplexSearch.setForAnneeHistorique("" + (Integer.parseInt(anneeHistorique) - 1));
                    revenuHistoriqueComplexSearch.setForIdContribuable(subside.getIdContribuable());
                    revenuHistoriqueComplexSearch.setForRevenuActif(true);
                    revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService().search(
                            revenuHistoriqueComplexSearch);

                    Integer revenuDeterminantActuel = 50000;
                    if (revenuHistoriqueComplexSearch.getSize() > 0) {
                        RevenuHistoriqueComplex revenuHistoriqueComplex = (RevenuHistoriqueComplex) revenuHistoriqueComplexSearch
                                .getSearchResults()[0];
                        double revDetTmp = Double.parseDouble(revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                                .getRevenuDeterminantCalcul());

                        revenuDeterminantActuel = (int) revDetTmp;
                    }

                    // récupération des paramètres annuels
                    initParamAnnuels();
                    Integer montantRevenu33000 = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                            .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_REVENU_MIN_SUBSIDE)
                            .getFormatedValueByYear(anneeHistorique, null, 0));
                    Integer montantRevenuMax = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                            .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_REVENU_MAX_SUBSIDE)
                            .getFormatedValueByYear(anneeHistorique, null, 0));

                    if ((revenuDeterminantActuel > montantRevenu33000) && (revenuDeterminantActuel <= montantRevenuMax)) {

                        // if (subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST5.getValue())
                        // || subside.getNoModeles().equals(IAMCodeSysteme.AMDocumentModeles.DECMST8.getValue())) {
                        // Read current taxation
                        RevenuFullComplex currentTaxation = AmalServiceLocator.getRevenuService().readFullComplex(
                                idTaxation);

                        // Initialization d'un revenu full complex et
                        // d'un calcul revenuformule
                        // nécessaires aux calculs
                        RevenuHistoriqueComplex currentRevenuHistorique = new RevenuHistoriqueComplex();
                        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();

                        // Set the minimal values
                        SimpleRevenuHistorique revenuHistorique = new SimpleRevenuHistorique();
                        revenuHistorique.setAnneeHistorique(anneeHistorique);
                        currentRevenuHistorique.setRevenuFullComplex(currentTaxation);
                        currentRevenuHistorique.setSimpleRevenuHistorique(revenuHistorique);
                        currentRevenuHistorique = calculsRevenuFormules.doCalcul(currentRevenuHistorique);

                        Integer currentRevenuDeterminant = Integer.parseInt(currentRevenuHistorique
                                .getSimpleRevenuDeterminant().getRevenuDeterminantCalcul());
                        if (currentRevenuDeterminant < montantRevenu33000) {
                            // Force un enregistrement du dossier complet pour traitement
                            bFoundTaxation = false;
                        }
                    }
                }
                return bFoundTaxation;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Contrôle si le subside doit être pris en compte dans la population
     * 
     * @param demandeType
     * @param subside
     * @return
     */
    private boolean checkSubsideType(String demandeType, SimpleDetailFamille subside) {
        try {
            if (demandeType.equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())) {
                // 1 check assisté >> prise en compte
                return checkSubsideAssiste(subside);
            } else if (demandeType.equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                // 2 check pc
                return checkSubsidePC(subside);
            } else if (demandeType.equals(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue())) {
                // 3 check sourcier
                return checkSubsideSourcier(subside);
            } else {
                // 4 check taxation definitive
                return checkSubsideStandard(subside);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this,
                    "Error checking subside type " + demandeType + " " + subside.getId() + " : " + ex.toString());
            return false;
        }
    }

    private void compareDossiersPC(List<SimpleDetailFamille> currentPCIds) {

        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 1);

        List<String> allRecords = new ArrayList<String>();
        for (SimpleDetailFamille currentSubside : currentPCIds) {
            // -----------------------------------------------------------------
            // Lecture du membre de famille connu pour PC Amal
            // -----------------------------------------------------------------
            SimpleFamille currentFamille = null;
            PCAccordeeSearch currentSearch = null;
            PersonneEtendueComplexModel currentTiers = null;
            try {
                currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(currentSubside.getIdFamille());

            } catch (Exception ex) {
                ex.printStackTrace();
                currentFamille = null;
            }
            try {
                if ((currentFamille != null) && !JadeStringUtil.isEmpty(currentFamille.getIdTier())) {
                    currentTiers = TIBusinessServiceLocator.getPersonneEtendueService()
                            .read(currentFamille.getIdTier());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                currentTiers = null;
            }
            if (currentFamille != null) {
                try {
                    // -----------------------------------------------------------------
                    // Recherche de la prestation PC correspondante
                    // -----------------------------------------------------------------
                    currentSearch = new PCAccordeeSearch();
                    currentSearch.setForIdTiers(currentFamille.getIdTier());
                    currentSearch = PegasusImplServiceLocator.getPCAccordeeService().search(currentSearch);
                    boolean bLineAdded = false;
                    for (int iCurrentPC = 0; iCurrentPC < currentSearch.getSize(); iCurrentPC++) {
                        PCAccordee currentPC = (PCAccordee) currentSearch.getSearchResults()[iCurrentPC];
                        // Année de taxation uniquement
                        if (currentPC.getSimplePrestationsAccordees().getDateDebutDroit().indexOf(anneeTaxation) < 0) {
                            continue;
                        }
                        // droit non terminé
                        if (!JadeStringUtil.isBlankOrZero(currentPC.getSimplePrestationsAccordees().getDateFinDroit())) {
                            continue;
                        }
                        bLineAdded = true;
                        // -----------------------------------------------------------------
                        // Lignes de logs
                        // -----------------------------------------------------------------
                        String currentLine = "";
                        if (currentTiers != null) {
                            currentLine += currentTiers.getPersonneEtendue().getNumAvsActuel() + ";";
                        } else {
                            currentLine += " " + ";";
                        }
                        currentLine += BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                currentFamille.getPereMereEnfant())
                                + ";";
                        currentLine += currentFamille.getNomPrenomUpper() + ";";
                        currentLine += currentFamille.getDateNaissance() + ";";
                        if (currentTiers != null) {
                            currentLine += currentTiers.getPersonne().getDateDeces() + ";";
                        } else {
                            currentLine += " " + ";";
                        }
                        currentLine += "RECHERCHE_PROPRE;";
                        currentLine += currentPC.getSimplePCAccordee().getDateDebut() + ";";
                        currentLine += currentPC.getSimplePCAccordee().getDateFin() + ";";
                        if (IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(currentPC.getSimplePCAccordee().getCsEtatPC())) {
                            currentLine += "VALIDE;";
                        } else if (IPCPCAccordee.CS_ETAT_PCA_HISTORISEE.equals(currentPC.getSimplePCAccordee()
                                .getCsEtatPC())) {
                            currentLine += "HISTORISEE;";
                        } else if (IPCPCAccordee.CS_ETAT_PCA_CALCULE.equals(currentPC.getSimplePCAccordee()
                                .getCsEtatPC())) {
                            currentLine += "CALCULE;";
                        } else {
                            currentLine += "AUTRE;";
                        }
                        allRecords.add(currentLine);
                    }
                    if ((currentSearch.getSize() <= 0) || (bLineAdded == false)) {
                        // -----------------------------------------------------------------
                        // Si pas trouvé et rôle différent de contribuable principal
                        // Recherche du contribuable principal
                        // -----------------------------------------------------------------
                        String idTiersContribuable = "";
                        if (!currentFamille.getIsContribuable()) {
                            try {
                                SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
                                familleSearch.setForIdContribuable(currentFamille.getIdContribuable());
                                familleSearch.setIsContribuable(true);
                                familleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(familleSearch);
                                if (familleSearch.getSize() == 1) {
                                    idTiersContribuable = ((SimpleFamille) (familleSearch.getSearchResults()[0]))
                                            .getIdTier();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (!JadeStringUtil.isBlankOrZero(idTiersContribuable)) {
                            // -----------------------------------------------------------------
                            // Recherche de la prestation PC correspondante pour le contribuable
                            // -----------------------------------------------------------------
                            currentSearch = new PCAccordeeSearch();
                            currentSearch.setForIdTiers(idTiersContribuable);
                            currentSearch = PegasusImplServiceLocator.getPCAccordeeService().search(currentSearch);
                            bLineAdded = false;
                            for (int iCurrentPC = 0; iCurrentPC < currentSearch.getSize(); iCurrentPC++) {
                                PCAccordee currentPC = (PCAccordee) currentSearch.getSearchResults()[iCurrentPC];
                                // Année de taxation uniquement
                                if (currentPC.getSimplePrestationsAccordees().getDateDebutDroit()
                                        .indexOf(anneeTaxation) < 0) {
                                    continue;
                                }
                                // droit non terminé
                                if (!JadeStringUtil.isBlankOrZero(currentPC.getSimplePrestationsAccordees()
                                        .getDateFinDroit())) {
                                    continue;
                                }
                                bLineAdded = true;
                                // -----------------------------------------------------------------
                                // Lignes de logs
                                // -----------------------------------------------------------------
                                String currentLine = "";
                                if (currentTiers != null) {
                                    currentLine += currentTiers.getPersonneEtendue().getNumAvsActuel() + ";";
                                } else {
                                    currentLine += " " + ";";
                                }
                                currentLine += BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                        currentFamille.getPereMereEnfant())
                                        + ";";
                                currentLine += currentFamille.getNomPrenomUpper() + ";";
                                currentLine += currentFamille.getDateNaissance() + ";";
                                if (currentTiers != null) {
                                    currentLine += currentTiers.getPersonne().getDateDeces() + ";";
                                } else {
                                    currentLine += " " + ";";
                                }
                                currentLine += "RECHERCHE_CONTRIBUABLE;";
                                currentLine += currentPC.getSimplePCAccordee().getDateDebut() + ";";
                                currentLine += currentPC.getSimplePCAccordee().getDateFin() + ";";
                                if (IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(currentPC.getSimplePCAccordee()
                                        .getCsEtatPC())) {
                                    currentLine += "VALIDE;";
                                } else if (IPCPCAccordee.CS_ETAT_PCA_HISTORISEE.equals(currentPC.getSimplePCAccordee()
                                        .getCsEtatPC())) {
                                    currentLine += "HISTORISEE;";
                                } else if (IPCPCAccordee.CS_ETAT_PCA_CALCULE.equals(currentPC.getSimplePCAccordee()
                                        .getCsEtatPC())) {
                                    currentLine += "CALCULE;";
                                } else {
                                    currentLine += "AUTRE;";
                                }
                                allRecords.add(currentLine);
                            }
                        }
                    }
                    if ((currentSearch.getSize() <= 0) || (bLineAdded == false)) {
                        // -----------------------------------------------------------------
                        // Lignes de logs
                        // -----------------------------------------------------------------
                        String currentLine = "";
                        if (currentTiers != null) {
                            currentLine += currentTiers.getPersonneEtendue().getNumAvsActuel() + ";";
                        } else {
                            currentLine += " " + ";";
                        }
                        currentLine += BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                currentFamille.getPereMereEnfant())
                                + ";";
                        currentLine += currentFamille.getNomPrenomUpper() + ";";
                        currentLine += currentFamille.getDateNaissance() + ";";
                        if (currentTiers != null) {
                            currentLine += currentTiers.getPersonne().getDateDeces() + ";";
                        } else {
                            currentLine += " " + ";";
                        }
                        currentLine += " ; ; ;INCONNU;";
                        allRecords.add(currentLine);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    currentSearch = null;
                }
            }
        }
        // Ecriture du fichier
        AMProcessRepriseFinAnneePopulationFileHelper currentFile = AMProcessRepriseFinAnneePopulationFileHelper
                .getInstance(AMProcessRepriseFinAnneePopulationFileHelper.FILE_TYPE_CSV);
        currentFile.setShortFileName("Comparaison_PC");
        currentFile.writeFile(allRecords);

        // Envoi du fichier par mail
        try {
            String subject = "Liste concordance PC - Amal";
            String message = "Veuillez trouver en fichier joint la liste susmentionnée";
            String[] fileNames = new String[1];
            fileNames[0] = currentFile.getFullFileName();
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    message, fileNames);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties#getEnumForProperties
     * ()
     */
    @Override
    public Class<AMProcessRepriseFinAnneeEnum> getEnumForProperties() {
        return AMProcessRepriseFinAnneeEnum.class;
    }

    /**
     * Récupération des derniers subsides par type de demande
     * 
     * @param idContribuable
     * @param anneeHistorique
     * @return
     */
    private HashMap<String, List<SimpleDetailFamille>> getLastSubsideType(String idContribuable, String anneeHistorique) {

        HashMap<String, List<SimpleDetailFamille>> returnHashMap = new HashMap<String, List<SimpleDetailFamille>>();

        // Recherche sur l'année qui précède l'année historique
        int iLastAnnee = Integer.parseInt(anneeHistorique) - 1;
        String anneeSearch = "" + iLastAnnee;

        // Recherche des subsides pour l'ensemble de la famille du contribuable
        SimpleDetailFamilleSearch searchSubside = new SimpleDetailFamilleSearch();
        searchSubside.setForIdContribuable(idContribuable);
        searchSubside.setForAnneeHistorique(anneeSearch);
        searchSubside.setForCodeActif(true);
        try {
            searchSubside = AmalServiceLocator.getDetailFamilleService().search(searchSubside);
            HashMap<String, SimpleDetailFamille> subsidesByMember = new HashMap<String, SimpleDetailFamille>();
            for (int iSubside = 0; iSubside < searchSubside.getSize(); iSubside++) {
                SimpleDetailFamille currentSubside = (SimpleDetailFamille) searchSubside.getSearchResults()[iSubside];
                if (!subsidesByMember.containsKey(currentSubside.getIdFamille())) {
                    subsidesByMember.put(currentSubside.getIdFamille(), currentSubside);
                } else {
                    SimpleDetailFamille lastSavedSubside = subsidesByMember.get(currentSubside.getIdFamille());
                    if (JadeDateUtil.isDateMonthYearAfter(currentSubside.getDebutDroit(),
                            lastSavedSubside.getDebutDroit())) {
                        subsidesByMember.remove(currentSubside.getIdFamille());
                        subsidesByMember.put(currentSubside.getIdFamille(), currentSubside);
                    }
                }
            }
            // HASH MAP REMPLIE ACTUELLEMENT AVEC ID FAMILLE ET SUBSIDE LE PLUS RéCENT
            Iterator<String> subsidesMembersIterator = subsidesByMember.keySet().iterator();
            while (subsidesMembersIterator.hasNext()) {
                String currentIdFamille = subsidesMembersIterator.next();
                SimpleDetailFamille subside = subsidesByMember.get(currentIdFamille);
                // Ajout dans hashmap de retour
                if (!returnHashMap.containsKey(subside.getTypeDemande())) {
                    List<SimpleDetailFamille> subsidesByType = new ArrayList<SimpleDetailFamille>();
                    subsidesByType.add(subside);
                    returnHashMap.put(subside.getTypeDemande(), subsidesByType);
                } else {
                    List<SimpleDetailFamille> subsidesByType = returnHashMap.remove(subside.getTypeDemande());
                    subsidesByType.add(subside);
                    returnHashMap.put(subside.getTypeDemande(), subsidesByType);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "Error search detail famille : " + ex.toString());
        }

        return returnHashMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#getParametersForUrl
     * (ch.globaz.jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        return "selectedId=" + entity.getIdRef();
    }

    /**
     * Initialisation des paramètres annuels
     */
    private void initParamAnnuels() {
        if (containerParametres == null) {
            try {
                containerParametres = new ContainerParametres();
                String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
                SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
                simpleParametreAnnuelSearch.setForAnneeParametre(anneeHistorique);
                simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService().search(
                        simpleParametreAnnuelSearch);
                containerParametres.setParametresAnnuelsProvider(new ParametresAnnuelsProvider(
                        simpleParametreAnnuelSearch));
            } catch (Exception e) {
                e.printStackTrace();
                containerParametres = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#searchPopulation()
     */
    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        // -----------------------------------------------------------------------
        // 1) Recherche dans tous les dossiers contribuables
        // -----------------------------------------------------------------------

        SimpleContribuableSearch currentSearch = new SimpleContribuableSearch();
        currentSearch.setDefinedSearchSize(500);
        currentSearch.setForContribuableActif(true);
        // currentSearch.setForIdContribuable("211");
        currentSearch = AmalImplServiceLocator.getSimpleContribuableService().search(currentSearch);

        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);

        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        List<SimpleDetailFamille> allPCSubsides = new ArrayList<SimpleDetailFamille>();
        boolean needTreatment = true;
        int iOffset = 0;
        int iTotal = 0;
        int iCompteur = 0;
        try {
            while (needTreatment) {

                for (JadeAbstractModel model : currentSearch.getSearchResults()) {

                    SimpleContribuable currentSimpleContribuable = (SimpleContribuable) model;
                    String idContribuable = currentSimpleContribuable.getId();

                    ContribuableRCListe contrib = null;
                    ContribuableRCListeSearch contribuableSearch = new ContribuableRCListeSearch();
                    contribuableSearch.setIsContribuable(true);
                    contribuableSearch.setForIdContribuable(idContribuable);
                    contribuableSearch = AmalServiceLocator.getContribuableService().searchRCListe(contribuableSearch);
                    if (contribuableSearch.getSize() == 1) {
                        contrib = (ContribuableRCListe) contribuableSearch.getSearchResults()[0];
                        // Check si pas de date de fin pour le contribuable principal
                        try {
                            SimpleFamille contribuableFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                                    contrib.getIdFamille());
                            if (!JadeStringUtil.isBlankOrZero(contribuableFamille.getFinDefinitive())) {
                                if (JadeDateUtil.isDateMonthYearAfter("01." + anneeHistorique,
                                        contribuableFamille.getFinDefinitive())) {
                                    // Skip dossier, le contribuable principal a une date de fin définitive avant
                                    // 01.2013
                                    continue;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        JadeLogger
                                .error(null, "----------------------------------------------------------------------");
                        JadeLogger.error(null, iCompteur + " - " + "Nombre résultats != 1 : " + idContribuable);
                        JadeLogger
                                .error(null, "----------------------------------------------------------------------");
                        continue;
                    }

                    // récupération des subsides par type de demande
                    HashMap<String, List<SimpleDetailFamille>> lastSubsides = getLastSubsideType(idContribuable,
                            anneeHistorique);
                    List<SimpleDetailFamille> subsidesToKeep = new ArrayList<SimpleDetailFamille>();
                    // itération pour traitement
                    Iterator<String> lastSubsidesIterator = lastSubsides.keySet().iterator();
                    while (lastSubsidesIterator.hasNext()) {
                        String currentType = lastSubsidesIterator.next();
                        List<SimpleDetailFamille> currentSubsides = lastSubsides.get(currentType);
                        for (int iSubside = 0; iSubside < currentSubsides.size(); iSubside++) {
                            SimpleDetailFamille currentSubside = currentSubsides.get(iSubside);
                            boolean fillConditions = checkSubsideType(currentType, currentSubside);
                            // grouper les simpledetailfamille pour les envois groupés ...
                            if (fillConditions) {
                                subsidesToKeep.add(currentSubside);
                            }
                        }
                    }

                    // -------------------------------------------------------------------------------
                    // Recherche des dossiers contribuables sans subsides avec taxation n-2 dispo
                    // -------------------------------------------------------------------------------
                    if (subsidesToKeep.size() <= 0) {
                        boolean keepContribuable = checkLastTaxation(idContribuable);
                        if (keepContribuable) {
                            JadeProcessEntity entite = new JadeProcessEntity();
                            entite.setIdRef(contrib.getIdContribuable());
                            String description = contrib.getDesignation1();
                            description += " " + contrib.getDesignation2();
                            description += " " + contrib.getNumAvsActuel();
                            description += "  - Taxation " + (Integer.parseInt(anneeHistorique) - 2) + " disponible";
                            entite.setDescription(description);
                            entite.setValue1("");
                            for (int iEntite = 0; iEntite < entites.size(); iEntite++) {
                                JadeProcessEntity searchEntite = entites.get(iEntite);
                                if (searchEntite.getIdRef().equals(entite.getIdRef())) {
                                    JadeLogger.error(null,
                                            "----------------------------------------------------------------------");
                                    JadeLogger.error(null, iCompteur + " - "
                                            + "ENTITE DEJA EXISTANTE POUR ID CONTRIBUABLE : " + entite.getIdRef()
                                            + " - " + entite.getDescription());
                                    JadeLogger.error(null,
                                            "----------------------------------------------------------------------");
                                    break;
                                }
                            }
                            entites.add(entite);
                        }
                    }

                    if (subsidesToKeep.size() > 0) {
                        // String value 1
                        String idSubsides = "";
                        String typeDemandeDescription = "";
                        int cpt = 0;
                        for (int iSubside = 0; iSubside < subsidesToKeep.size(); iSubside++) {
                            SimpleDetailFamille currentSubside = subsidesToKeep.get(iSubside);

                            SimpleFamille membre = new SimpleFamille();
                            if (!JadeStringUtil.isBlankOrZero(currentSubside.getIdFamille())) {
                                membre = AmalImplServiceLocator.getSimpleFamilleService().read(
                                        currentSubside.getIdFamille());
                                if (!JadeStringUtil.isBlankOrZero(membre.getFinDefinitive())) {
                                    continue;
                                }
                            }

                            if (JadeStringUtil.isEmpty(typeDemandeDescription)) {
                                if (!JadeStringUtil.isBlankOrZero(currentSubside.getTypeDemande())) {
                                    typeDemandeDescription = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                            currentSubside.getTypeDemande());
                                    if (currentSubside.getTypeDemande().equals(
                                            IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                                        allPCSubsides.add(currentSubside);
                                    }
                                }
                            }
                            if (cpt > 0) {
                                idSubsides += ";";
                            }
                            idSubsides += currentSubside.getIdDetailFamille();
                            cpt++;
                        }
                        JadeProcessEntity entite = new JadeProcessEntity();
                        entite.setIdRef(contrib.getIdContribuable());
                        String description = contrib.getDesignation1();
                        description += " " + contrib.getDesignation2();
                        description += " " + contrib.getNumAvsActuel();
                        description += "  - " + typeDemandeDescription;
                        entite.setDescription(description);
                        entite.setValue1(idSubsides);
                        for (int iEntite = 0; iEntite < entites.size(); iEntite++) {
                            JadeProcessEntity searchEntite = entites.get(iEntite);
                            if (searchEntite.getIdRef().equals(entite.getIdRef())) {
                                JadeLogger.error(null,
                                        "----------------------------------------------------------------------");
                                JadeLogger.error(null, iCompteur + " - "
                                        + "ENTITE DEJA EXISTANTE POUR ID CONTRIBUABLE : " + entite.getIdRef() + " - "
                                        + entite.getDescription());
                                JadeLogger.error(null,
                                        "----------------------------------------------------------------------");
                                break;
                            }
                        }
                        entites.add(entite);
                    }
                    iCompteur++;
                }

                iOffset += currentSearch.getSize();
                iTotal = iOffset;
                currentSearch.setOffset(iOffset);
                JadeLogger.info(null, "--------------------------------------------------");
                JadeLogger.info(null, "Nombre de contribuables parcourus : " + iTotal);
                JadeLogger.info(null, "--------------------------------------------------");

                if (currentSearch.isHasMoreElements()) {
                    needTreatment = true;
                    currentSearch = AmalImplServiceLocator.getSimpleContribuableService().search(currentSearch);
                } else {
                    needTreatment = false;
                }
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "CreateArray Exception --> " + e.getMessage());
        }
        // -----------------------------------------------------------------------
        // 1) Recherche dans les dossiers PC NEW >> tous les dossiers PCs sont dans allPCIds
        // -----------------------------------------------------------------------

        try {
            compareDossiersPC(allPCSubsides);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entites;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties#setProperties
     * (java.util.Map)
     */
    @Override
    public void setProperties(Map<AMProcessRepriseFinAnneeEnum, String> hashMap) {
        data = hashMap;
    }

}
