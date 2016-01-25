/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers.step2;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuFullComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
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

        String anneeHistorique = data.get(AMProcessRepriseSourciersEnum.ANNEE);
        String anneeTaxation = data.get(AMProcessRepriseSourciersEnum.ANNEE_TAXATION);

        // ---------------------------------------------------------------------------------------------
        // 0 - CHECK VALIDITE DE L'ENTITE CSV et du contribuable
        // ---------------------------------------------------------------------------------------------
        if (currentCsvEntity == null) {
            JadeThread.logError("CSV Entity not loaded", "CSV Entity not loaded");
            return;
        }
        if (JadeStringUtil.isEmpty(idContribuable)) {
            JadeThread.logError("Id Contribuable manquant", "Id Contribuable manquant ;" + currentCsvEntity.toString());
            return;
        }
        // si historique, transfert puis création de la taxation
        // ---------------------------------------------------------------------------------------------
        if (bHistorique) {
            // TODO : create tiers + dossier
            JadeThread.logWarn("Contribuable historique", "Contribuable historique ;" + currentCsvEntity.toString());
            return;
        }

        // ------------------------------------------------------------------------------------
        // Check si taxation déjà présente
        // année de taxation à anneeTaxation
        // check si revenu sourcier >> il s'agit du revenu sourcier à contrôler (même revenu ?)
        // ------------------------------------------------------------------------------------
        RevenuFullComplexSearch taxationSearch = new RevenuFullComplexSearch();
        taxationSearch.setForAnneeTaxation(anneeTaxation);
        taxationSearch.setForIdContribuable(idContribuable);
        boolean bFoundRevenuSourcier = false;
        SimpleRevenu revenuSourcierFound = null;
        try {
            taxationSearch = (RevenuFullComplexSearch) AmalServiceLocator.getRevenuService().search(taxationSearch);
            for (int iTaxation = 0; iTaxation < taxationSearch.getSize(); iTaxation++) {
                RevenuFullComplex currentTaxation = (RevenuFullComplex) taxationSearch.getSearchResults()[iTaxation];
                if (currentTaxation.getSimpleRevenu().isSourcier()) {
                    // Contrôle du revenu sourcier, déjà réceptionné automatiquement ou reprise
                    if (currentTaxation.getSimpleRevenu().getAnneeTaxation().equals(anneeTaxation)) {
                        String existingRevenuConjoint = currentTaxation.getSimpleRevenuSourcier()
                                .getRevenuEpouseAnnuel();
                        String existingRevenuSourcier = currentTaxation.getSimpleRevenuSourcier()
                                .getRevenuEpouxAnnuel();
                        if (JadeStringUtil.isBlankOrZero(existingRevenuConjoint)) {
                            existingRevenuConjoint = "0.0";
                        }
                        if (JadeStringUtil.isBlankOrZero(existingRevenuSourcier)) {
                            existingRevenuSourcier = "0.0";
                        }
                        String newRevenuConjoint = currentCsvEntity.getRevenuConjoint();
                        if (JadeStringUtil.isBlankOrZero(newRevenuConjoint)) {
                            newRevenuConjoint = "0.0";
                        }
                        String newRevenuSourcier = currentCsvEntity.getRevenuSourcier();
                        if (JadeStringUtil.isBlankOrZero(newRevenuSourcier)) {
                            newRevenuSourcier = "0.0";
                        }
                        if (!currentTaxation.getSimpleRevenu().getTypeSource()
                                .equals(IAMCodeSysteme.AMTypeSourceTaxation.MANUELLE.getValue())) {
                            // Contrôle des valeurs, nous nous trouvons dans le cas
                            // ou un revenu sourcier est existant pour les années introduites
                            // Si une taxation pour une même année de taxation est trouvée,
                            // comparer les montants si <> 0
                            try {
                                float fExistingRevenuConjoint = Float.parseFloat(existingRevenuConjoint);
                                float fExistingRevenuSourcier = Float.parseFloat(existingRevenuSourcier);
                                if ((fExistingRevenuConjoint + fExistingRevenuSourcier) == 0) {
                                    JadeThread.logInfo("INFO REVENU SOURCIER",
                                            "REVENU SOURCIER AUTO FISC DEJA EXISTANT MAIS REVENU A ZERO"
                                                    + anneeTaxation + ";" + currentCsvEntity.toString());
                                } else {
                                    float fNewRevenuConjoint = Float.parseFloat(newRevenuConjoint);
                                    float fNewRevenuSourcier = Float.parseFloat(newRevenuSourcier);
                                    if ((fExistingRevenuConjoint + fExistingRevenuSourcier) != (fNewRevenuConjoint + fNewRevenuSourcier)) {
                                        JadeThread.logWarn("WARNING REVENU SOURCIER",
                                                "ERROR COMPARING REVENUS SOURCIERS AUTO FISC " + anneeTaxation
                                                        + " Total Existant : "
                                                        + (fExistingRevenuConjoint + fExistingRevenuSourcier)
                                                        + " - Total fichier : "
                                                        + (fNewRevenuConjoint + fNewRevenuSourcier) + ";"
                                                        + currentCsvEntity.toString());
                                    } else {
                                        JadeThread.logInfo("INFO REVENU SOURCIER",
                                                "REVENU SOURCIER AUTO FISC DEJA EXISTANT REVENUS SOURCIERS "
                                                        + anneeTaxation + ";" + currentCsvEntity.toString());
                                        bFoundRevenuSourcier = true;
                                    }
                                }
                            } catch (Exception x) {
                                JadeThread.logWarn("ERREUR REVENU SOURCIER",
                                        "ERROR COMPARING AUTO FISC REVENUS SOURCIERS " + anneeTaxation + ";"
                                                + currentCsvEntity.toString() + x.getMessage());
                            }
                        } else {
                            // Taxation sourcier manuelle trouvée !
                            // si le montant == 0, on l'utilise pour la suite
                            // on récupère l'id du simple revenu et du revenu sourcier
                            try {
                                float fExistingRevenuConjoint = Float.parseFloat(existingRevenuConjoint);
                                float fExistingRevenuSourcier = Float.parseFloat(existingRevenuSourcier);
                                if ((fExistingRevenuConjoint + fExistingRevenuSourcier) == 0) {
                                    JadeThread.logInfo("INFO REVENU SOURCIER",
                                            "REVENU SOURCIER MANUAL DEJA EXISTANT MAIS REVENU A ZERO" + anneeTaxation
                                                    + ";" + currentCsvEntity.toString());
                                    revenuSourcierFound = currentTaxation.getSimpleRevenu();
                                } else {
                                    float fNewRevenuConjoint = Float.parseFloat(newRevenuConjoint);
                                    float fNewRevenuSourcier = Float.parseFloat(newRevenuSourcier);
                                    if ((fExistingRevenuConjoint + fExistingRevenuSourcier) != (fNewRevenuConjoint + fNewRevenuSourcier)) {
                                        JadeThread.logWarn("WARNING REVENU SOURCIER",
                                                "ERROR COMPARING REVENUS SOURCIERS MANUAL CCJU " + anneeTaxation
                                                        + " Total Existant : "
                                                        + (fExistingRevenuConjoint + fExistingRevenuSourcier)
                                                        + " - Total fichier : "
                                                        + (fNewRevenuConjoint + fNewRevenuSourcier) + ";"
                                                        + currentCsvEntity.toString());
                                    } else {
                                        JadeThread.logInfo("INFO REVENU SOURCIER",
                                                "REVENU SOURCIER MANUAL CCJU DEJA EXISTANT REVENUS SOURCIERS "
                                                        + anneeTaxation + ";" + currentCsvEntity.toString());
                                        bFoundRevenuSourcier = true;
                                    }
                                }
                            } catch (Exception xx) {
                                JadeThread.logWarn("ERREUR REVENU SOURCIER",
                                        "ERROR COMPARING MANUAL CCJU REVENUS SOURCIERS " + anneeTaxation + ";"
                                                + currentCsvEntity.toString() + xx.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JadeThread.logWarn("ERREUR REVENU SOURCIER", "ERREUR RECHERCHE REVENU " + anneeTaxation + ";"
                    + currentCsvEntity.toString());
        }
        // Si un revenu sourcier a déjà été trouvé, sortie
        if (bFoundRevenuSourcier) {
            JadeThread.logInfo("INFO REVENU SOURCIER", "REVENU SOURCIER DEJA EXISTANT " + anneeTaxation + ";"
                    + currentCsvEntity.toString());
            return;
        }

        // ------------------------------------------------------------------------------------
        // Cas actuel depuis AS/400
        // Recherche d'un revenu avec anneeHistorique
        // annee de taxation à 0
        // afin de récupérer l'éventuelle date de traitement (qui correspond à la date
        // de réception de l'ATMIS en retour
        // ------------------------------------------------------------------------------------
        RevenuHistoriqueComplexSearch revenuSearch = new RevenuHistoriqueComplexSearch();
        revenuSearch.setForAnneeHistorique(anneeHistorique);
        revenuSearch.setForIdContribuable(idContribuable);
        revenuSearch.setForRevenuActif(true);
        String dateTraitement = "";
        String dateAvisTaxation = "";
        String etatCivil = "";
        String nbEnfants = "0";
        try {
            revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);
            if (revenuSearch.getSize() == 1) {
                RevenuHistoriqueComplex currentRevenuHistorique = (RevenuHistoriqueComplex) revenuSearch
                        .getSearchResults()[0];
                if (anneeHistorique.equals("2012")) {
                    if (currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation().equals("0")) {
                        dateTraitement = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getDateTraitement();
                        dateAvisTaxation = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getDateAvisTaxation();
                        etatCivil = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().getEtatCivil();
                        if (!JadeStringUtil.isEmpty(currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getNbEnfants())) {
                            nbEnfants = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().getNbEnfants();
                        }
                    }
                } else {
                    if (currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
                        dateTraitement = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getDateTraitement();
                        dateAvisTaxation = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getDateAvisTaxation();
                        etatCivil = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().getEtatCivil();
                        if (!JadeStringUtil.isEmpty(currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu()
                                .getNbEnfants())) {
                            nbEnfants = currentRevenuHistorique.getRevenuFullComplex().getSimpleRevenu().getNbEnfants();
                        }
                    }
                }
            } else if (revenuSearch.getSize() > 1) {
                JadeThread.logError("ERREUR REVENU SOURCIER", "PLUSIEURS REVENUS ACTIFS POUR ANNEE " + anneeHistorique
                        + ";" + currentCsvEntity.toString());
                return;
            }
        } catch (Exception ex) {
            JadeThread.logError("ERREUR REVENU SOURCIER", "ERREUR RECHERCHE REVENU " + anneeHistorique
                    + " RECUPERATION DATE DE TRAITEMENT;" + currentCsvEntity.toString());
            return;
        }

        // ---------------------------------------------------------------------------------------------
        // 1 - INSCRIPTION DU REVENU (TAXATION)
        // ---------------------------------------------------------------------------------------------
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());
        RevenuFullComplex revenuToCreate = new RevenuFullComplex();

        // Simple Revenu
        // ---------------------------------------------------------------------------------------------
        if (revenuSourcierFound != null) {
            if (JadeStringUtil.isEmpty(dateTraitement)) {
                dateTraitement = revenuSourcierFound.getDateTraitement();
            }
            if (JadeStringUtil.isEmpty(dateAvisTaxation)) {
                dateAvisTaxation = revenuSourcierFound.getDateAvisTaxation();
            }
            if (JadeStringUtil.isBlankOrZero(etatCivil)) {
                etatCivil = revenuSourcierFound.getEtatCivil();
            }
            // BZ8381 : Problème reprise nb enfant depuis historique (CBU / 24.05.2013)
            // Le test faisait un isEmpty sur le nbEnfant. Or le nb enfant n'était jamais empty, mais valait 0 si il n'y
            // avait rien, on entrait donc pas dans le test. Désormais on compare le nbEnfant du revenu historique
            // (nbEnfants) à celui de la taxation manuelle trouvée (revenuSourcierFound) et si le nombre est différent,
            // on prend celui de la taxation manuelle
            if (!revenuSourcierFound.getNbEnfants().equals(nbEnfants)) {
                if (!JadeStringUtil.isEmpty(revenuSourcierFound.getNbEnfants())) {
                    nbEnfants = revenuSourcierFound.getNbEnfants();
                }
            }
        }
        revenuToCreate.getSimpleRevenu().setAnneeTaxation(data.get(AMProcessRepriseSourciersEnum.ANNEE_TAXATION));
        if (!JadeStringUtil.isEmpty(dateAvisTaxation)) {
            revenuToCreate.getSimpleRevenu().setDateAvisTaxation(dateAvisTaxation);
        }
        if (!JadeStringUtil.isEmpty(dateTraitement)) {
            revenuToCreate.getSimpleRevenu().setDateTraitement(dateTraitement);
        }
        revenuToCreate.getSimpleRevenu().setDateSaisie(dateToday);
        revenuToCreate.getSimpleRevenu().setCodeSuspendu("2");
        revenuToCreate.getSimpleRevenu().setIsSourcier(true);
        if (!nbEnfants.equals("0")) {
            revenuToCreate.getSimpleRevenu().setNbEnfants(nbEnfants);
            if (!nbEnfants.equals(currentCsvEntity.getNombreEnfants())) {
                JadeThread.logWarn("WARNING REVENU ",
                        "NB ENFANTS DIFFERENTS, VALEUR FICHIER : " + currentCsvEntity.getNombreEnfants() + ";"
                                + currentCsvEntity.toString());
            }
        } else {
            if (!JadeStringUtil.isEmpty(currentCsvEntity.getNombreEnfants())) {
                revenuToCreate.getSimpleRevenu().setNbEnfants(currentCsvEntity.getNombreEnfants());
            }
        }
        if (!JadeStringUtil.isEmpty(etatCivil)) {
            revenuToCreate.getSimpleRevenu().setEtatCivil(etatCivil);
        }
        revenuToCreate.getSimpleRevenu().setTypeRevenu(IAMCodeSysteme.CS_TYPE_SOURCIER);
        revenuToCreate.getSimpleRevenu().setIdContribuable(idContribuable);
        revenuToCreate.getSimpleRevenu().setTypeSource(IAMCodeSysteme.AMTypeSourceTaxation.AUTO_FISC.getValue());
        revenuToCreate.getSimpleRevenu().setRevDetUnique("0.0");
        revenuToCreate.getSimpleRevenu().setRevDetUniqueOuiNon(false);
        // Revenu Sourcier et conjoint
        // ---------------------------------------------------------------------------------------------
        if (JadeStringUtil.isEmpty(currentCsvEntity.getRevenuConjoint())) {
            revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouseAnnuel("0.0");
        } else {
            revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouseAnnuel(currentCsvEntity.getRevenuConjoint());
        }
        if (JadeStringUtil.isEmpty(currentCsvEntity.getRevenuSourcier())) {
            revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouxAnnuel("0.0");
        } else {
            revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouxAnnuel(currentCsvEntity.getRevenuSourcier());
        }
        revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouseMensuel("0.0");
        revenuToCreate.getSimpleRevenuSourcier().setRevenuEpouxMensuel("0.0");
        revenuToCreate.getSimpleRevenuSourcier().setNombreMois("0");

        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
        try {
            revenuToCreate = calculsRevenuFormules.doCalculSourcier(revenuToCreate);
        } catch (Exception ex) {
            JadeThread.logError("ERREUR REVENU IMPOSABLE",
                    "ERREUR CALCUL REVENU IMPOSABLE ;" + currentCsvEntity.toString());
            return;
        }
        try {
            AmalServiceLocator.getRevenuService().create(revenuToCreate);
        } catch (Exception ex) {
            JadeThread.logError("ERREUR REVENU SOURCIER",
                    "ERREUR ENREGISTREMENT REVENU SOURCIER;" + currentCsvEntity.toString());
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
