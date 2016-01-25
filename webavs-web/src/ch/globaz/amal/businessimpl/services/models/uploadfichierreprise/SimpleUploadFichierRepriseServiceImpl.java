package ch.globaz.amal.businessimpl.services.models.uploadfichierreprise;

import globaz.amal.vb.process.AMUploadFichierRepriseViewBean;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.io.IOException;
import java.util.List;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersCsvEntity;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersCsvFileHelper;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersCsvLine;

public class SimpleUploadFichierRepriseServiceImpl implements SimpleUploadFichierRepriseService {

    @Override
    public int count(SimpleUploadFichierRepriseSearch search) throws JadePersistenceException,
            AMUploadFichierRepriseException {
        if (search == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to count uploadFichierReprise, the search model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleUploadFichierReprise create(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        if (simpleUploadFichierReprise == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to create SimpleUploadFichierReprise, the model passed is null !");
        }
        // Checker...

        return (SimpleUploadFichierReprise) JadePersistenceManager.add(simpleUploadFichierReprise);
    }

    @Override
    public SimpleUploadFichierReprise delete(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        if (simpleUploadFichierReprise == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to delete SimpleUploadFichierReprise, the model passed is null !");
        }
        if (simpleUploadFichierReprise.isNew()) {
            throw new AMUploadFichierRepriseException(
                    "Unable to delete SimpleUploadFichierReprise, the model passed is new !");
        }
        // Checker...

        return (SimpleUploadFichierReprise) JadePersistenceManager.delete(simpleUploadFichierReprise);
    }

    @Override
    public int delete(SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        if (simpleUploadFichierRepriseSearch == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to delete SimpleUploadFichierReprise, the model passed is null !");
        }

        // Checker...
        return JadePersistenceManager.delete(simpleUploadFichierRepriseSearch);
    }

    @Override
    public SimpleUploadFichierReprise read(String idSimpleUploadFichierReprise) throws JadePersistenceException,
            AMUploadFichierRepriseException {
        if (JadeStringUtil.isEmpty(idSimpleUploadFichierReprise)) {
            throw new AMUploadFichierRepriseException(
                    "Unable to read SimpleUploadFichierReprise, the id passed is empty !");
        }
        SimpleUploadFichierReprise simpleUploadFichierReprise = new SimpleUploadFichierReprise();
        simpleUploadFichierReprise.setId(idSimpleUploadFichierReprise);

        return (SimpleUploadFichierReprise) JadePersistenceManager.read(simpleUploadFichierReprise);
    }

    @Override
    public SimpleUploadFichierRepriseSearch search(SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        if (simpleUploadFichierRepriseSearch == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to search SimpleUploadFichierReprise, the search model passed is null");
        }

        return (SimpleUploadFichierRepriseSearch) JadePersistenceManager.search(simpleUploadFichierRepriseSearch);
    }

    @Override
    public SimpleUploadValidation serializeFile(String path, String fileName, String params)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        AMUploadFichierRepriseViewBean reprise = new AMUploadFichierRepriseViewBean();
        return reprise.serializeXmlFile(path, fileName, params);
    }

    @Override
    public SimpleUploadValidation serializeFileSourciers(String path, String fileName, String params)
            throws JadePersistenceException, AMUploadFichierRepriseException {

        SimpleUploadValidation currentValidation = new SimpleUploadValidation();

        try {
            JadeLogger.info(this, "Start reading...");
            // ---------------------------------------------------------------------------------------
            // Lecture du fichier
            // ---------------------------------------------------------------------------------------
            File currentFile = new File(path + File.separator + fileName);
            List<String> fileContents = AMProcessRepriseSourciersCsvFileHelper.readFile(currentFile);
            currentFile = null;

            // ---------------------------------------------------------------------------------------
            // Parsing et validation
            // ---------------------------------------------------------------------------------------
            List<AMProcessRepriseSourciersCsvLine> fileLines = AMProcessRepriseSourciersCsvFileHelper
                    .parseLines(fileContents);
            fileContents = null;
            String validationErrors = "";
            if (params.equals("validationTrue")) {
                validationErrors = AMProcessRepriseSourciersCsvFileHelper.validateLines(fileLines);
            }
            if (validationErrors.length() > 0) {
                JadeLogger.error(null, "ERROR VALIDATING FILES : " + validationErrors);
                currentValidation.setErrorMsg("Error validating file : \n" + validationErrors);
                currentValidation.setReturnCode(1);
            } else {
                // ---------------------------------------------------------------------------------------
                // Suppression des lignes SOURCIER présentent dans la table
                // ---------------------------------------------------------------------------------------
                SimpleUploadFichierRepriseSearch fichierSearch = new SimpleUploadFichierRepriseSearch();
                fichierSearch.setLikeTypeReprise("SOURCIER");
                fichierSearch.setDefinedSearchSize(0);
                try {
                    fichierSearch = AmalServiceLocator.getSimpleUploadFichierService().search(fichierSearch);
                    for (int iLine = 0; iLine < fichierSearch.getSize(); iLine++) {
                        SimpleUploadFichierReprise fichier = (SimpleUploadFichierReprise) fichierSearch
                                .getSearchResults()[iLine];
                        fichier = AmalServiceLocator.getSimpleUploadFichierService().delete(fichier);
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error searching existing item 'SOURCIER'");
                }
                // ---------------------------------------------------------------------------------------
                // Création des entités qui seront utilisées pour la création de la population
                // ---------------------------------------------------------------------------------------
                List<AMProcessRepriseSourciersCsvEntity> fileEntities = AMProcessRepriseSourciersCsvFileHelper
                        .createEntity(fileLines);
                for (int iEntity = 0; iEntity < fileEntities.size(); iEntity++) {
                    AMProcessRepriseSourciersCsvEntity currentEntity = fileEntities.get(iEntity);
                    SimpleUploadFichierReprise simpleUploadFichierReprise = new SimpleUploadFichierReprise();
                    if (!JadeStringUtil.isEmpty(currentEntity.getNoNSSSourcier())) {
                        simpleUploadFichierReprise.setNoContribuable(currentEntity.getNoNSSSourcier());
                    } else if (!JadeStringUtil.isEmpty(currentEntity.getNoAVSSourcier())) {
                        simpleUploadFichierReprise.setNoContribuable(currentEntity.getNoAVSSourcier());
                    }
                    simpleUploadFichierReprise.setXmlLignes(currentEntity.toString());
                    simpleUploadFichierReprise.setTypeReprise("SOURCIER");
                    simpleUploadFichierReprise.setIsValid(true);
                    simpleUploadFichierReprise.setNomPrenom(currentEntity.getNomSourcier() + " "
                            + currentEntity.getPrenomSourcier());
                    try {
                        simpleUploadFichierReprise = AmalServiceLocator.getSimpleUploadFichierService().create(
                                simpleUploadFichierReprise);
                        JadeLogger.info(null, "Create - " + currentEntity.toString());
                    } catch (Exception ex) {
                        JadeLogger.info(null, "Problem creating - " + currentEntity.toString());
                        JadeLogger.info(null, "Exception message : " + ex.getMessage());
                    }
                }
            }

            JadeLogger.info(this, "Stop reading...");
            JadeLogger.info(this, "fileLines.size : " + fileLines.size());
            fileLines = null;

        } catch (IOException e) {
            currentValidation.setErrorMsg("Error reading file : " + e.getMessage());
            currentValidation.setReturnCode(1);
        }

        if (!JadeStringUtil.isEmpty(currentValidation.getErrorMsg())) {
            String to = JadeThread.currentUserEmail();
            String subject = "Importation du fichier BPM (Sourciers) en erreur";
            String body = "Erreurs : " + currentValidation.getErrorMsg();
            try {
                JadeSmtpClient.getInstance().sendMail(to, subject, body, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return currentValidation;
    }

    @Override
    public SimpleUploadFichierReprise update(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException {
        if (simpleUploadFichierReprise == null) {
            throw new AMUploadFichierRepriseException(
                    "Unable to update SimpleUploadFichierReprise, the model passed is null !");
        }
        if (simpleUploadFichierReprise.isNew()) {
            throw new AMUploadFichierRepriseException(
                    "Unable to update SimpleUploadFichierReprise, the model passed is new !");
        }
        // Checker...

        return (SimpleUploadFichierReprise) JadePersistenceManager.update(simpleUploadFichierReprise);
    }

    @Override
    public String updateProgressBar(String value) throws JadePersistenceException, AMUploadFichierRepriseException {
        JadeProgressBarModel progressBar = new JadeProgressBarModel(5213);

        SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
        progressBar.setCurrent(count(simpleUploadFichierRepriseSearch));

        // return progressBar;
        return "";
    }

}
