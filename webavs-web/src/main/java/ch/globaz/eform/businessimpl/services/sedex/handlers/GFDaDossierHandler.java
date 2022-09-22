package ch.globaz.eform.businessimpl.services.sedex.handlers;

import ch.globaz.common.exceptions.NotFoundException;
import ch.globaz.common.util.Dates;
import ch.globaz.common.util.ZipUtils;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.businessimpl.services.sedex.ZipFile;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import globaz.eform.translation.CodeSystem;
import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class GFDaDossierHandler implements GFSedexhandler {
    protected BSession session;
    protected GFDaDossierModel model;
    protected Object message;
    protected ZipFile zipFile;

    protected abstract String getMessageId();
    protected abstract String getYourBusinessReferenceId();
    protected abstract String getOurBusinessReferenceId();
    protected abstract LocalDate getMessageDate();
    protected abstract String getSenderId();
    protected abstract NaturalPersonsOASIDIType getInsuredPerson();

    protected void initSollicitationModel() {
        model = new GFDaDossierModel();

        model.setMessageId(getMessageId());
        model.setOurBusinessRefId(UUID.randomUUID().toString());
        model.setYourBusinessRefId(getOurBusinessReferenceId());
        model.setType(GFTypeDADossier.SOLICITATION.getCodeSystem());
        model.setStatus(GFStatusDADossier.TO_SEND.getCodeSystem());
        model.setCreationSpy(Dates.formatSwiss(getMessageDate()));

        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForSedexId(getSenderId());
        search.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);

        try {
            search = TIBusinessServiceLocator.getAdministrationService().find(search);
        } catch (JadePersistenceException | JadeApplicationException e) {
            throw new RuntimeException(e);
        }

        if (search.getSize() > 0) {
            AdministrationComplexModel complexModel = (AdministrationComplexModel) search.getSearchResults()[0];
            model.setCodeCaisse(complexModel.getAdmin().getCodeAdministration());
        } else {
            model.setCodeCaisse("000000");
        }
        if(getInsuredPerson() != null) {
            model.setNssAffilier(getInsuredPerson().getVn().toString());
        }
    }

    protected  void initReceptionModel() {
        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setByOurBusinessRefId(getYourBusinessReferenceId());
        search.setWhereKey("ourBusinessRefId");

        try {
            GFEFormServiceLocator.getGFDaDossierDBService().search(search);

            model = Arrays.stream(search.getSearchResults())
                    .map(o -> (GFDaDossierModel) o)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Sollicitation non trouvé pour le message Id : " + getMessageId()));
        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setData(Map<String, Object> externData) throws RuntimeException {
        zipFile = (ZipFile) externData.get("zipFile");
    }

    @Override
    public void create(ValidationResult result) throws RuntimeException {
        try {
            initSollicitationModel();
            GFEFormServiceLocator.getGFDaDossierDBService().create(model, result);
        } catch (Exception e) {
            LOG.error("GFDaDossierHandler#create - Erreur lors de l'ajout de la demande en DB : {}", model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        }
    }

    @Override
    public void update(ValidationResult result) {
        initReceptionModel();

        model.setStatus(GFStatusDADossier.TREAT.getCodeSystem());

        String sftpPath = GFFileUtils.generateDaDossierFilePath(model);

        EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_HOST_FILE_SERVER);

        try {
            // Valide la mise sur le sftp des fichiers avant la persistance des informations
            //Décompression du zip
            Path zipTmpPath = Paths.get(Jade.getInstance().getPersistenceDir() + File.separator + UUID.randomUUID());
            Path zipFilepath = Paths.get(zipFile.getPath());

            Files.createDirectories(zipTmpPath);
            ZipUtils.unZip(zipFilepath, zipTmpPath);

            //Préparation du dossier de destination sur le hostFile
            fileService.createFolder(sftpPath);

            //Envoie des fichiers dans le dossier sftp à plat
            sendDirectory(zipTmpPath, sftpPath, fileService);

            //persistence des informations en base
            GFEFormServiceLocator.getGFDaDossierDBService().update(model, result);

        } catch (Exception e) {
            LOG.error("GFDaDossierHandler#update - Erreur lors de la mise à jour de la demande en DB  : {}", model.getMessageId(), e);
            //Si le dossier sur le hostFile a ét créé alors on le purge
            if (fileService.exist(sftpPath)) {
                fileService.remove(sftpPath);
            }

            throw new JadeApplicationRuntimeException(e);
        }
    }

    private void sendDirectory(Path zipTmpPath, String sftpPath, EFormFileService fileService) throws Exception{
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(zipTmpPath)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    sendDirectory(path, sftpPath, fileService);
                } else {
                    fileService.send(path.toAbsolutePath(), sftpPath);
                }
            }
        }
    }
    public void setMessage(Object message){
        this.message = message;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
