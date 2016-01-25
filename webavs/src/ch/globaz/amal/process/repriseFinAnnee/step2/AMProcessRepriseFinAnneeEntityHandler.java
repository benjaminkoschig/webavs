/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee.step2;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.process.repriseFinAnnee.AMProcessRepriseFinAnneeEnum;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneeEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private JadeProcessEntity currentEntity = null;
    private Map<Enum<?>, String> data = null;
    private String idContribuable = "";
    private List<String> idsDetailFamille = new ArrayList<String>();
    List<SimpleFamille> listParent = new ArrayList<SimpleFamille>();

    /**
     * Renvoi vrai si un changement d'�tat civil est intervenu entre 2 taxation (-2 et -3)
     * 
     * @param anneeHistorique
     * @return
     */
    private boolean checkChangementEtatCivil(String anneeHistorique) {
        // --------------------------------------------------------------------
        // R�cup�ration �tat civil taxation anneeHistorique -2
        // --------------------------------------------------------------------
        String anneeTaxation2 = "" + (Integer.parseInt(anneeHistorique) - 2);
        String etatCivilTaxation2 = "";
        String lastDateTaxation2 = "";
        RevenuSearch taxationSearch2 = new RevenuSearch();
        taxationSearch2.setForAnneeTaxation(anneeTaxation2);
        taxationSearch2.setForIdContribuable(idContribuable);
        try {
            taxationSearch2 = AmalServiceLocator.getRevenuService().search(taxationSearch2);
            for (int iTaxation = 0; iTaxation < taxationSearch2.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch2.getSearchResults()[iTaxation];
                if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                    if (currentTaxation.getTypeTaxation()
                            .equals(IAMCodeSysteme.AMTaxationType.OFFICE_TOTAUX.getValue())
                            || currentTaxation.getTypeTaxation().equals(
                                    IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                        if (JadeStringUtil.isEmpty(etatCivilTaxation2)) {
                            lastDateTaxation2 = currentTaxation.getDateAvisTaxation();
                            etatCivilTaxation2 = currentTaxation.getEtatCivil();
                        } else {
                            if (JadeDateUtil.isDateAfter(currentTaxation.getDateAvisTaxation(), lastDateTaxation2)) {
                                lastDateTaxation2 = currentTaxation.getDateAvisTaxation();
                                etatCivilTaxation2 = currentTaxation.getEtatCivil();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
        // --------------------------------------------------------------------
        // R�cup�ration �tat civil taxation anneeHistorique -3
        // --------------------------------------------------------------------
        String anneeTaxation3 = "" + (Integer.parseInt(anneeHistorique) - 3);
        String etatCivilTaxation3 = "";
        String lastDateTaxation3 = "";
        RevenuSearch taxationSearch3 = new RevenuSearch();
        taxationSearch3.setForAnneeTaxation(anneeTaxation3);
        taxationSearch3.setForIdContribuable(idContribuable);
        try {
            taxationSearch3 = AmalServiceLocator.getRevenuService().search(taxationSearch3);
            for (int iTaxation = 0; iTaxation < taxationSearch3.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch3.getSearchResults()[iTaxation];
                if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                    if (currentTaxation.getTypeTaxation()
                            .equals(IAMCodeSysteme.AMTaxationType.OFFICE_TOTAUX.getValue())
                            || currentTaxation.getTypeTaxation().equals(
                                    IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                        if (JadeStringUtil.isEmpty(etatCivilTaxation3)) {
                            lastDateTaxation3 = currentTaxation.getDateAvisTaxation();
                            etatCivilTaxation3 = currentTaxation.getEtatCivil();
                        } else {
                            if (JadeDateUtil.isDateAfter(currentTaxation.getDateAvisTaxation(), lastDateTaxation3)) {
                                lastDateTaxation3 = currentTaxation.getDateAvisTaxation();
                                etatCivilTaxation3 = currentTaxation.getEtatCivil();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
        // --------------------------------------------------------------------
        // Comparaison uniquement si nous trouvons les informations n�cessaires
        // --------------------------------------------------------------------
        if ((!JadeStringUtil.isEmpty(etatCivilTaxation3)) && (!JadeStringUtil.isEmpty(etatCivilTaxation2))) {
            // --------------------------------------------------------------------
            // Si l'EC du revenu pr�c�dent est concubin, l'etat civil ne change que si le nouveau est
            // "Mari�"
            // --------------------------------------------------------------------
            if (etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_CONCUBIN)) {
                if (etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE)
                        || etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_VEUF)
                        || etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_SEPARE)
                        || etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_DIVORCE)
                        || etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_HOIRIE)) {
                    return false;
                } else if (etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_MARIED)) {
                    return true;
                } else {
                    return false;
                }
            } else if (etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_CONCUBIN)) {
                if (etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE)
                        || etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_VEUF)
                        || etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_SEPARE)
                        || etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_DIVORCE)
                        || etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_HOIRIE)) {
                    return false;
                } else if (etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_MARIED)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // --------------------------------------------------------------------
                // On ne consid�re pas un passage "S�par�" --> "Divorc�" comme changement d'EC
                // --------------------------------------------------------------------
                if ((etatCivilTaxation3.equals(IAMCodeSysteme.CS_ETAT_CIVIL_SEPARE) || etatCivilTaxation3
                        .equals(IAMCodeSysteme.CS_ETAT_CIVIL_DIVORCE))
                        && (etatCivilTaxation2.equals(IAMCodeSysteme.CS_ETAT_CIVIL_SEPARE) || etatCivilTaxation2
                                .equals(IAMCodeSysteme.CS_ETAT_CIVIL_DIVORCE))) {
                    return false;
                } else {
                    if (!etatCivilTaxation3.equals(etatCivilTaxation2)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Contr�le que le contribuable est toujours connu par les PCs
     * 
     * @param allIdFamille
     * @return
     */
    private HashMap<String, List<String>> checkIfKnownByPC(List<String> allIdFamille) {

        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 1);

        List<String> idKnowAsPC = new ArrayList<String>();
        List<String> idUnknownAsPC = new ArrayList<String>();

        for (String currentIdFamille : allIdFamille) {
            // -----------------------------------------------------------------
            // Lecture du membre de famille connu pour PC Amal
            // -----------------------------------------------------------------
            SimpleFamille currentFamille = null;
            PCAccordeeSearch currentSearch = null;
            try {
                currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(currentIdFamille);

            } catch (Exception ex) {
                ex.printStackTrace();
                currentFamille = null;
            }
            if (currentFamille != null) {
                try {
                    if (currentFamille.isMere() || currentFamille.isPere()) {
                        listParent.add(currentFamille);
                    }

                    // -----------------------------------------------------------------
                    // Recherche de la prestation PC correspondante
                    // -----------------------------------------------------------------
                    currentSearch = new PCAccordeeSearch();
                    currentSearch.setForIdTiers(currentFamille.getIdTier());
                    currentSearch = PegasusImplServiceLocator.getPCAccordeeService().search(currentSearch);
                    boolean bLineAdded = false;
                    for (int iCurrentPC = 0; iCurrentPC < currentSearch.getSize(); iCurrentPC++) {
                        PCAccordee currentPC = (PCAccordee) currentSearch.getSearchResults()[iCurrentPC];
                        // Ann�e de taxation uniquement
                        if (currentPC.getSimplePrestationsAccordees().getDateDebutDroit().indexOf(anneeTaxation) < 0) {
                            continue;
                        }
                        // droit non termin�
                        if (!JadeStringUtil.isBlankOrZero(currentPC.getSimplePrestationsAccordees().getDateFinDroit())) {
                            continue;
                        }
                        // ----------------------------------------------
                        // A ce stade, droit trouv� sous recherche propre
                        // ----------------------------------------------
                        bLineAdded = true;
                        idKnowAsPC.add(currentIdFamille);
                    }
                    if ((currentSearch.getSize() <= 0) || (bLineAdded == false)) {
                        // -----------------------------------------------------------------
                        // Si pas trouv� et r�le diff�rent de contribuable principal
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
                                // Ann�e de taxation uniquement
                                if (currentPC.getSimplePrestationsAccordees().getDateDebutDroit()
                                        .indexOf(anneeTaxation) < 0) {
                                    continue;
                                }
                                // droit non termin�
                                if (!JadeStringUtil.isBlankOrZero(currentPC.getSimplePrestationsAccordees()
                                        .getDateFinDroit())) {
                                    continue;
                                }
                                bLineAdded = true;
                                // -----------------------------------------------------------------
                                // A ce stade, droit trouv� sous recherche contribuable
                                // -----------------------------------------------------------------
                                idKnowAsPC.add(currentIdFamille);
                            }
                        }
                    }
                    if ((currentSearch.getSize() <= 0) || (bLineAdded == false)) {
                        // -----------------------------------------------------------------
                        // A ce stade, droit non trouv�
                        // -----------------------------------------------------------------
                        idUnknownAsPC.add(currentIdFamille);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // -----------------------------------------------
        // Organisation du r�sultat dans la map de retour
        // PC, liste_droit_pc_connu
        // F, liste_droit_pc_inconnu
        // ----------------------------------------------
        HashMap<String, List<String>> returnMap = new HashMap<String, List<String>>();
        returnMap.put(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue(), idKnowAsPC);
        returnMap.put(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue(), idUnknownAsPC);
        return returnMap;
    }

    private void generateRevenuHistoriquePCA() {

        try {
            String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
            String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 2);

            // ----------------------------------------------------------------
            // Recherche de la derni�re taxation
            // ----------------------------------------------------------------
            RevenuSearch taxationSearch = new RevenuSearch();
            taxationSearch.setForAnneeTaxation(anneeTaxation);
            taxationSearch.setForIdContribuable(idContribuable);
            taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
            String lastIdTaxation = "";
            String lastDateTaxation = "";
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(currentTaxation.getTypeRevenu())) {
                    continue;
                }
                if (JadeStringUtil.isEmpty(lastIdTaxation)) {
                    lastIdTaxation = currentTaxation.getIdRevenu();
                    lastDateTaxation = currentTaxation.getDateAvisTaxation();
                } else {
                    if (JadeDateUtil.isDateAfter(currentTaxation.getDateAvisTaxation(), lastDateTaxation)) {
                        lastIdTaxation = currentTaxation.getIdRevenu();
                        lastDateTaxation = currentTaxation.getDateAvisTaxation();
                    }
                }
            }
            // R�cup�ration de la taxation (check idtaxation)
            // ----------------------------------------------------------------
            if (JadeStringUtil.isEmpty(lastIdTaxation)) {
                return;
            }
            RevenuFullComplex taxation = AmalServiceLocator.getRevenuService().readFullComplex(lastIdTaxation);

            // Recherche du revenu historique actif pour l'ann�e de calcul
            // ----------------------------------------------------------------
            RevenuHistoriqueComplexSearch revenuSearch = new RevenuHistoriqueComplexSearch();
            revenuSearch.setForIdContribuable(idContribuable);
            revenuSearch.setForAnneeHistorique(anneeHistorique);
            revenuSearch.setForRevenuActif(true);
            revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);
            if (revenuSearch.getSize() == 0) {
                // Cr�ation du revenu historique si aucun disponible
                // ----------------------------------------------------------------
                RevenuHistoriqueComplex revenuHistorique = new RevenuHistoriqueComplex();
                revenuHistorique.setRevenuFullComplex(taxation);
                revenuHistorique.getSimpleRevenuHistorique().setAnneeHistorique(anneeHistorique);
                revenuHistorique.getSimpleRevenuHistorique().setIdContribuable(idContribuable);
                revenuHistorique.getSimpleRevenuHistorique().setCodeActif(true);
                revenuHistorique.getSimpleRevenuHistorique().setIdRevenu(taxation.getId());
                revenuHistorique = AmalServiceLocator.getRevenuService().create(revenuHistorique);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateSubsideAssiste(List<String> allIdFamille) {
        // -------------------------------------------------------------------------
        // 0) Cr�ation du revenu historique si possible
        // -------------------------------------------------------------------------
        generateRevenuHistoriquePCA();

        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);

        // -------------------------------------------------------------------------
        // 1) Simulation du calcul
        // -------------------------------------------------------------------------
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue(), "", true);

        // -------------------------------------------------------------------------
        // 2) Traitement du calcul >> g�n�ration DECMASM
        // -------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            // A prendre en compte uniquement si membre de famille concern� par la s�lection
            if (allIdFamille.contains(currentSubside.getIdFamille())) {
                currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue());
                currentSubside.setRefus(false);
                currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMASM.getValue());
                currentSubside.setMontantContribution(currentSubside.getMontantContributionAssiste());
                currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                newSubsides.add(currentSubside);
            }
        }
        if (newSubsides.size() <= 0) {
            return;
        }
        // Set les nouveaux subsides � appliquer
        currentCalculs.setSubsides(newSubsides);
        // -------------------------------------------------------------------------
        // 3) Application du calcul
        // -------------------------------------------------------------------------
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utiliser pour l'attribution continue (ann�e -1 d�j� attribu�)
     * 
     * @param currentTypeKey
     * @param allIdFamille
     */
    private void generateSubsideByType(String currentTypeKey, List<String> allIdFamille) {
        if (currentTypeKey.equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())) {
            generateSubsideAssiste(allIdFamille);
        } else if (currentTypeKey.equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
            HashMap<String, List<String>> droitPCChecked = checkIfKnownByPC(allIdFamille);
            List<String> idFamillePC = droitPCChecked.get(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue());
            List<String> idFamilleNonPC = droitPCChecked.get(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue());
            setPcOnConjoint(idFamillePC, idFamilleNonPC);
            if (idFamillePC.size() > 0) {
                generateSubsidePC(idFamillePC);
            }
            if (idFamilleNonPC.size() > 0) {
                generateSubsideStandard(idFamilleNonPC);
            }
        } else if (currentTypeKey.equals(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue())) {
            generateSubsideSourciers(allIdFamille);
        } else {
            generateSubsideStandard(allIdFamille);
        }
    }

    /**
     * Utiliser pour l'attribution nouvelle (pas d'attribution en ann�e -1)
     */
    private void generateSubsideForContribuable() {
        // -------------------------------------------------------------------------
        // 1) R�cup�ration de l'idTaxation
        // -------------------------------------------------------------------------
        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 2);
        String idTaxation = "";
        String dateTaxation = "";
        RevenuSearch taxationSearch = new RevenuSearch();
        taxationSearch.setForAnneeTaxation(anneeTaxation);
        taxationSearch.setForIdContribuable(idContribuable);
        try {
            taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                    if (currentTaxation.getTypeTaxation().equals(IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                        if (JadeStringUtil.isEmpty(idTaxation)) {
                            idTaxation = currentTaxation.getIdRevenu();
                            dateTaxation = currentTaxation.getDateAvisTaxation();
                            continue;
                        }
                        // Si date plus r�cente, remplacement idTaxation
                        if (JadeDateUtil.isDateAfter(currentTaxation.getDateAvisTaxation(), dateTaxation)) {
                            idTaxation = currentTaxation.getIdRevenu();
                            dateTaxation = currentTaxation.getDateAvisTaxation();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        if (JadeStringUtil.isEmpty(idTaxation)) {
            return;
        }
        // -------------------------------------------------------------------------
        // 2) Simulation du calcul
        // -------------------------------------------------------------------------
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue(), idTaxation, true);

        // Si tous les montants � 0 >> tous � refus
        // Si 1 montant � 0 et les autres >0 >> document famille modeste (ATENF8)
        // Si tous les montants > 0 >> document famille (ATSUBS1/2/3)
        int iNbZero = 0;
        int iNbNotZero = 0;
        int iNbEnfant = 0;
        int iNbAdulte = 0;
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            // Type de membres concern�s par le calcul
            try {
                SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        currentSubside.getIdFamille());
                if (currentFamille.getPereMereEnfant().equals(IAMCodeSysteme.CS_TYPE_ENFANT)) {
                    if (JadeStringUtil.isBlankOrZero(currentFamille.getFinDefinitive())) {
                        iNbEnfant++;
                    }
                } else {
                    if (JadeStringUtil.isBlankOrZero(currentFamille.getFinDefinitive())) {
                        iNbAdulte++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        // -------------------------------------------------------------------------
        // 3) Traitement du calcul
        // -------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue());
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
            if (iNbZero == 0) {
                // si il n'y a pas de montant � 0, ATSUBS1/2/3
                if (iNbEnfant == 0) {
                    if (iNbAdulte == 1) {
                        // Adulte seul > 25 ans
                        if (!isUnder26(currentSubside.getIdFamille(), anneeHistorique)) {
                            currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS1.getValue());
                            currentSubside.setRefus(false);
                            currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                            newSubsides.add(currentSubside);
                        }
                    } else {
                        // Couple sans enfant
                        try {
                            SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                                    currentSubside.getIdFamille());
                            if (currentFamille.getIsContribuable()) {
                                currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS3.getValue());
                                currentSubside.setRefus(false);
                                currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                                newSubsides.add(currentSubside);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // famille avec enfant
                    try {
                        SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                                currentSubside.getIdFamille());
                        if (currentFamille.getIsContribuable()) {
                            currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS2.getValue());
                            currentSubside.setRefus(false);
                            currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                            newSubsides.add(currentSubside);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (iNbNotZero == 0) {
                    // si il n'y a que des montants � 0, rien
                    // pas d'ajout � newSubsides
                } else {
                    // si il y a des montants � 0 et >0, ATENF8 sauf pour les parents � 0
                    if (subsideValue > 0.0) {
                        currentSubside.setRefus(false);
                        currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATENF8.getValue());
                        currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                        newSubsides.add(currentSubside);
                    }
                }
            }
        }
        // G�n�ration du revenu historique dans tous les cas
        // if (newSubsides.size() <= 0) {
        // return;
        // }
        // Set les nouveaux subsides � appliquer
        currentCalculs.setSubsides(newSubsides);
        // -------------------------------------------------------------------------
        // 4) Application du calcul
        // -------------------------------------------------------------------------
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateSubsidePC(List<String> allIdFamille) {
        // -------------------------------------------------------------------------
        // 0) Cr�ation du revenu historique si possible
        // -------------------------------------------------------------------------
        generateRevenuHistoriquePCA();

        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);

        // -------------------------------------------------------------------------
        // 1) Simulation du calcul
        // -------------------------------------------------------------------------
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue(), "", true);

        // -------------------------------------------------------------------------
        // 2) Traitement du calcul >> g�n�ration ATENF1 ou DECMPCM
        // -------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue());
            // A prendre en compte uniquement si membre de famille concern� par la s�lection
            if (allIdFamille.contains(currentSubside.getIdFamille())) {
                boolean bAtenf = needAtenf(currentSubside.getIdFamille(), anneeHistorique);
                if (bAtenf) {
                    currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATENF1.getValue());
                } else {
                    currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMPCM.getValue());
                }
                currentSubside.setRefus(false);
                currentSubside.setMontantContribution(currentSubside.getMontantContributionPC());
                currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                newSubsides.add(currentSubside);
            }
        }
        if (newSubsides.size() <= 0) {
            return;
        }
        // Set les nouveaux subsides � appliquer
        currentCalculs.setSubsides(newSubsides);
        // -------------------------------------------------------------------------
        // 3) Application du calcul
        // -------------------------------------------------------------------------
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateSubsideSourciers(List<String> allIdFamille) {
        // -------------------------------------------------------------------------
        // 1) R�cup�ration de l'idTaxation
        // -------------------------------------------------------------------------
        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 1);
        String idTaxation = "";
        // RevenuSearch taxationSearch = new RevenuSearch();
        // taxationSearch.setForAnneeTaxation(anneeTaxation);
        // taxationSearch.setForIdContribuable(this.idContribuable);
        // try {
        // taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
        // for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
        // Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
        // if (currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
        // idTaxation = currentTaxation.getIdRevenu();
        // }
        // }
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // return;
        // }
        // if (JadeStringUtil.isEmpty(idTaxation)) {
        try {
            // Cr�ation de la taxation avec valeurs vide (seront remplies lors de la r�ception ATMIS)
            RevenuFullComplex currentTaxation = new RevenuFullComplex();
            currentTaxation.getSimpleRevenu().setTypeRevenu(IAMCodeSysteme.CS_TYPE_SOURCIER);
            currentTaxation.getSimpleRevenu().setAnneeTaxation(anneeTaxation);
            currentTaxation.getSimpleRevenu().setIdContribuable(idContribuable);
            currentTaxation.getSimpleRevenu().setTypeSource(IAMCodeSysteme.AMTypeSourceTaxation.AUTO_FISC.getValue());
            currentTaxation.getSimpleRevenu().setIsSourcier(true);
            currentTaxation.getSimpleRevenu().setRevDetUniqueOuiNon(false);
            currentTaxation = AmalServiceLocator.getRevenuService().create(currentTaxation);
            idTaxation = currentTaxation.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        // }
        // -------------------------------------------------------------------------
        // 2) Simulation du calcul
        // -------------------------------------------------------------------------
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue(), idTaxation, true);

        // -------------------------------------------------------------------------
        // 3) Traitement du calcul >> g�n�ration ATMIS
        // -------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue());
            // A prendre en compte uniquement si membre de famille est le contribuable principal !!!
            try {
                SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        currentSubside.getIdFamille());
                if (currentFamille.getIsContribuable()) {
                    currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATMIS1.getValue());
                    currentSubside.setRefus(false);
                    currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                    newSubsides.add(currentSubside);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Set les nouveaux subsides � appliquer
        currentCalculs.setSubsides(newSubsides);
        // -------------------------------------------------------------------------
        // 4) Application du calcul
        // -------------------------------------------------------------------------
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateSubsideStandard(List<String> allIdFamille) {
        // -------------------------------------------------------------------------
        // 1) R�cup�ration de l'idTaxation
        // -------------------------------------------------------------------------
        String anneeHistorique = data.get(AMProcessRepriseFinAnneeEnum.ANNEE);
        String anneeTaxation = "" + (Integer.parseInt(anneeHistorique) - 2);
        String idTaxation = "";
        String dateTaxation = "";
        RevenuSearch taxationSearch = new RevenuSearch();
        taxationSearch.setForAnneeTaxation(anneeTaxation);
        taxationSearch.setForIdContribuable(idContribuable);
        try {
            taxationSearch = AmalServiceLocator.getRevenuService().search(taxationSearch);
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                Revenu currentTaxation = (Revenu) taxationSearch.getSearchResults()[iTaxation];
                if (!currentTaxation.getTypeRevenu().equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                    if (currentTaxation.getTypeTaxation().equals(IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue())) {
                        if (JadeStringUtil.isEmpty(idTaxation)) {
                            idTaxation = currentTaxation.getIdRevenu();
                            dateTaxation = currentTaxation.getDateAvisTaxation();
                            continue;
                        }
                        // Si date plus r�cente, remplacement idTaxation
                        if (JadeDateUtil.isDateAfter(currentTaxation.getDateAvisTaxation(), dateTaxation)) {
                            idTaxation = currentTaxation.getIdRevenu();
                            dateTaxation = currentTaxation.getDateAvisTaxation();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        if (JadeStringUtil.isEmpty(idTaxation)) {
            return;
        }
        // -------------------------------------------------------------------------
        // 2) Simulation du calcul
        // -------------------------------------------------------------------------
        CalculsSubsidesContainer currentCalculs = new CalculsSubsidesContainer(idContribuable, anneeHistorique,
                IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue(), idTaxation, true);
        int iNbZero = 0;
        int iNbNotZero = 0;
        int iNbEnfant = 0;
        int iNbAdulte = 0;
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue());
            // Type de membres concern�s par le calcul
            try {
                SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        currentSubside.getIdFamille());
                if (currentFamille.getPereMereEnfant().equals(IAMCodeSysteme.CS_TYPE_ENFANT)) {
                    if (JadeStringUtil.isBlankOrZero(currentFamille.getFinDefinitive())) {
                        iNbEnfant++;
                    }
                } else {
                    if (JadeStringUtil.isBlankOrZero(currentFamille.getFinDefinitive())) {
                        iNbAdulte++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

        // -------------------------------------------------------------------------
        // 3) Traitement du calcul
        // <33000 (inbzero==0)
        // 16 � 25 ans >> ATENF1
        // pas de changement �tat civil >> DECMST1
        // changement �tat civil >> ATSUBS1/2/3
        // >33000<40000 (inbzero>0 && ibnnotzero>0)
        // 16 � 25 ans >> ATENF8
        // <16ans >> DECMST8
        // -------------------------------------------------------------------------
        ArrayList<SimpleDetailFamille> newSubsides = new ArrayList<SimpleDetailFamille>();
        for (int iSubside = 0; iSubside < currentCalculs.getSubsides().size(); iSubside++) {
            SimpleDetailFamille currentSubside = currentCalculs.getSubsides().get(iSubside);
            if (allIdFamille.contains(currentSubside.getIdFamille())) {
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
                boolean bAtenf = needAtenf(currentSubside.getIdFamille(), anneeHistorique);
                if (iNbZero == 0) {
                    if (bAtenf) {
                        currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATENF1.getValue());
                    } else {
                        boolean bCivilChange = checkChangementEtatCivil(anneeHistorique);
                        if (bCivilChange) {
                            // si il n'y a pas de montant � 0, ATSUBS1/2/3
                            if (iNbEnfant == 0) {
                                if (iNbAdulte == 1) {
                                    // Adulte seul
                                    currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS1.getValue());
                                } else {
                                    // Couple sans enfant
                                    try {
                                        SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService()
                                                .read(currentSubside.getIdFamille());
                                        if (currentFamille.getIsContribuable()) {
                                            currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS3
                                                    .getValue());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                // famille avec enfant
                                try {
                                    SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService()
                                            .read(currentSubside.getIdFamille());
                                    if (currentFamille.getIsContribuable()) {
                                        currentSubside
                                                .setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATSUBS2.getValue());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (iNbAdulte == 0) {
                                currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMST8.getValue());
                            } else {
                                currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMST1.getValue());
                            }
                        }
                    }
                    currentSubside.setRefus(false);
                    if (JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribution());
                    } else {
                        currentSubside.setMontantContribution(currentSubside.getMontantContribSansSuppl());
                    }
                    currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                    newSubsides.add(currentSubside);
                } else {
                    if ((iNbNotZero > 0) && (subsideValue > 0.0)) {
                        if (bAtenf) {
                            currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.ATENF8.getValue());
                        } else {
                            currentSubside.setNoModeles(IAMCodeSysteme.AMDocumentModeles.DECMST8.getValue());
                        }
                        currentSubside.setRefus(false);
                        if (JadeStringUtil.isBlankOrZero(currentSubside.getMontantContribSansSuppl())) {
                            currentSubside.setMontantContribution(currentSubside.getMontantContribution());
                        } else {
                            currentSubside.setMontantContribution(currentSubside.getMontantContribSansSuppl());
                        }
                        currentSubside.setSupplExtra(currentSubside.getMontantSupplement());
                        newSubsides.add(currentSubside);
                    }
                }
            }
        }
        // G�n�ration dans tous les cas du revenu historique

        // if (newSubsides.size() <= 0) {
        // return;
        // }
        // Set les nouveaux subsides � appliquer
        currentCalculs.setSubsides(newSubsides);
        // -------------------------------------------------------------------------
        // 4) Application du calcul
        // -------------------------------------------------------------------------
        try {
            AmalServiceLocator.getDetailFamilleService().generateSubside(currentCalculs,
                    IAMCodeSysteme.AMJobType.JOBPROCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * R�cup�ration des subsides par type de demande
     * 
     * @return
     */
    private HashMap<String, List<String>> groupSubsides() {
        HashMap<String, List<String>> returnHashMap = new HashMap<String, List<String>>();

        for (int iSubside = 0; iSubside < idsDetailFamille.size(); iSubside++) {
            String currentSubsideId = idsDetailFamille.get(iSubside);
            try {
                SimpleDetailFamille currentSubside = AmalServiceLocator.getDetailFamilleService()
                        .read(currentSubsideId);
                if (!JadeStringUtil.isEmpty(currentSubside.getIdFamille())) {
                    // Les anciens subsides 'D' doivent �tre pris en compte comme subside 'F'
                    if (currentSubside.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue())) {
                        currentSubside.setTypeDemande(IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue());
                    }
                    if (returnHashMap.get(currentSubside.getTypeDemande()) != null) {
                        List<String> allIdFamille = returnHashMap.get(currentSubside.getTypeDemande());
                        allIdFamille.add(currentSubside.getIdFamille());
                        returnHashMap.remove(currentSubside.getTypeDemande());
                        returnHashMap.put(currentSubside.getTypeDemande(), allIdFamille);
                    } else {
                        List<String> allIdFamille = new ArrayList<String>();
                        allIdFamille.add(currentSubside.getIdFamille());
                        returnHashMap.put(currentSubside.getTypeDemande(), allIdFamille);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnHashMap;
    }

    private boolean isUnder26(String idFamille, String anneeHistorique) {
        boolean bIsUnder26 = true;
        // R�cup�ration du r�le et de l'�ge de l'enfant
        try {
            FamilleContribuable currentFamille = AmalServiceLocator.getFamilleContribuableService().read(idFamille);
            SimpleFamille currentSimpleFamille = currentFamille.getSimpleFamille();
            if (!currentSimpleFamille.getPereMereEnfant().equals(IAMCodeSysteme.CS_TYPE_ENFANT)) {
                if (!JadeStringUtil.isBlankOrZero(currentSimpleFamille.getFinDefinitive())) {
                    bIsUnder26 = true;
                } else {
                    // Adulte
                    String dateNaissance = "";
                    if (JadeStringUtil.isBlankOrZero(currentSimpleFamille.getIdTier())) {
                        dateNaissance = currentSimpleFamille.getDateNaissance();
                    } else {
                        if (JadeStringUtil.isBlankOrZero(currentFamille.getPersonneEtendue().getPersonne()
                                .getDateNaissance())) {
                            dateNaissance = currentSimpleFamille.getDateNaissance();
                        } else {
                            dateNaissance = currentFamille.getPersonneEtendue().getPersonne().getDateNaissance();
                        }
                    }
                    if (!JadeStringUtil.isBlank(dateNaissance)) {
                        int ageMembre = Integer.valueOf(anneeHistorique)
                                - Integer.valueOf(JACalendar.getYear(dateNaissance));
                        if (ageMembre <= 25) {
                            bIsUnder26 = true;
                        } else {
                            bIsUnder26 = false;
                        }
                    } else {
                        bIsUnder26 = true;
                    }
                }
            } else {
                // Pas un adulte
                bIsUnder26 = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bIsUnder26;
    }

    private boolean needAtenf(String idFamille, String anneeHistorique) {
        boolean bAtenf = true;
        // R�cup�ration du r�le et de l'�ge de l'enfant
        try {
            FamilleContribuable currentFamille = AmalServiceLocator.getFamilleContribuableService().read(idFamille);
            SimpleFamille currentSimpleFamille = currentFamille.getSimpleFamille();
            if (currentSimpleFamille.getPereMereEnfant().equals(IAMCodeSysteme.CS_TYPE_ENFANT)) {

                if (!JadeStringUtil.isBlankOrZero(currentSimpleFamille.getFinDefinitive())) {
                    bAtenf = false;
                } else {
                    // Enfant
                    String dateNaissance = "";
                    if (JadeStringUtil.isBlankOrZero(currentSimpleFamille.getIdTier())) {
                        dateNaissance = currentSimpleFamille.getDateNaissance();
                    } else {
                        if (JadeStringUtil.isBlankOrZero(currentFamille.getPersonneEtendue().getPersonne()
                                .getDateNaissance())) {
                            dateNaissance = currentSimpleFamille.getDateNaissance();
                        } else {
                            dateNaissance = currentFamille.getPersonneEtendue().getPersonne().getDateNaissance();
                        }
                    }
                    if (!JadeStringUtil.isBlank(dateNaissance)) {
                        int ageMembre = Integer.valueOf(anneeHistorique)
                                - Integer.valueOf(JACalendar.getYear(dateNaissance));
                        if ((ageMembre >= 17) && (ageMembre <= 25)) {
                            bAtenf = true;
                        } else {
                            bAtenf = false;
                        }
                    } else {
                        bAtenf = true;
                    }
                }
            } else {
                // Pas enfant
                bAtenf = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bAtenf;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#run()
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        JadeThread.logClear();
        listParent = new ArrayList<SimpleFamille>();
        if (idsDetailFamille.size() > 0) {
            // ------------------------------------------------------------
            // ATTRIBUTION A DEJA EU LIEU EN ANNEE - 1
            // ------------------------------------------------------------

            // TRAITEMENT PAR LOT NECESSAIRE POUR GROUPAGE DE DOCUMENT
            // R�cup�ration des idfamilles par type de demande
            HashMap<String, List<String>> subsidesByType = groupSubsides();
            Iterator<String> keyIterator = subsidesByType.keySet().iterator();
            while (keyIterator.hasNext()) {
                String currentTypeKey = keyIterator.next();
                List<String> allIdFamille = subsidesByType.get(currentTypeKey);
                generateSubsideByType(currentTypeKey, allIdFamille);
            }

        } else {
            // -------------------------------------------------------------
            // ATTRIBUTION N'A PAS EU LIEU EN ANNEE - 1 >> CONTRIBUABLE ONLY
            // -------------------------------------------------------------
            generateSubsideForContribuable();
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
        currentEntity = entity;
        idContribuable = entity.getIdRef();
        String idsDetailFamilleFromEntity = entity.getValue1();
        if (!JadeStringUtil.isEmpty(idsDetailFamilleFromEntity)) {
            if (idsDetailFamilleFromEntity.indexOf(";") >= 0) {
                String[] allIds = idsDetailFamilleFromEntity.split(";");
                for (int iCurrentId = 0; iCurrentId < allIds.length; iCurrentId++) {
                    idsDetailFamille.add(allIds[iCurrentId]);
                }
            } else {
                idsDetailFamille.add(idsDetailFamilleFromEntity);
            }
        }
    }

    private void setPcOnConjoint(List<String> idFamillePC, List<String> idFamilleNonPC) {

        int parentWithPc = 0;
        int parentWithoutPc = 0;
        boolean ecMaried = false;
        String idToAdd = "";

        for (SimpleFamille parent : listParent) {
            if (idFamillePC.contains(parent.getIdFamille())) {
                parentWithPc++;
            }

            if (idFamilleNonPC.contains(parent.getIdFamille())) {
                parentWithoutPc++;
                PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
                try {
                    if (!JadeStringUtil.isBlankOrZero(parent.getIdTier())) {
                        personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().read(
                                parent.getIdTier());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TITiersViewBean.CS_MARIE.equals(personneEtendueComplexModel.getPersonne().getEtatCivil())) {
                    ecMaried = true;
                    idToAdd = parent.getIdFamille();
                }
            }
        }

        if ((parentWithPc == listParent.size()) || (parentWithPc == 0)) {
            // Tout les parents ont une PC ou n'ont rien du tout, on ne fait rien
        } else {
            // 1 parent a une PC et l'autre pas
            // seulement si ec == mari�
            if (ecMaried) {
                // Parcours des non pc pour rechercher le cas du parent sans PC
                for (int i = 0; i < idFamilleNonPC.size(); i++) {
                    String idFamNonPC = idFamilleNonPC.get(i);
                    if (idFamNonPC.equals(idToAdd)) {
                        // On supprime le cas des "Sans PC"...
                        idFamilleNonPC.remove(i);
                    }
                }

                // ... et on l'ajoute dans la liste des membres qui doivent recevoir une PC
                idFamillePC.add(idToAdd);
            }
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
