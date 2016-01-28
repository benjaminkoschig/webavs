package ch.globaz.amal.business.services.models.uploadfichierreprise;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.businessimpl.services.models.uploadfichierreprise.SimpleUploadValidation;

public interface SimpleUploadFichierRepriseService extends JadeApplicationService {
    public int count(SimpleUploadFichierRepriseSearch search) throws JadePersistenceException,
            AMUploadFichierRepriseException;

    public SimpleUploadFichierReprise create(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public SimpleUploadFichierReprise delete(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public int delete(SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public SimpleUploadFichierReprise read(String idSimpleUploadFichierReprise) throws JadePersistenceException,
            AMUploadFichierRepriseException;

    public SimpleUploadFichierRepriseSearch search(SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public SimpleUploadValidation serializeFile(String path, String fileName, String params)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public SimpleUploadValidation serializeFileSourciers(String path, String fileName, String params)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public SimpleUploadFichierReprise update(SimpleUploadFichierReprise simpleUploadFichierReprise)
            throws JadePersistenceException, AMUploadFichierRepriseException;

    public String updateProgressBar(String value) throws JadePersistenceException, AMUploadFichierRepriseException;
}
