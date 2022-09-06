package ch.globaz.eform.businessimpl.services.sedex.handlers;

import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.common.sftp.exception.SFtpOperationException;
import ch.globaz.common.util.Dates;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.sedex.GFSedexModel;
import ch.globaz.eform.businessimpl.services.sedex.ZipFile;
import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import globaz.globall.db.BSession;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
public abstract class GFEFormHandler implements GFSedexhandler {
    protected BSession session;
    protected GFSedexModel model;
    protected Object message;
    private ZipFile zipFile;

    @Override
    public void setData(Map<String, Object> externData) throws RuntimeException {
        extractData();
        zipFile = (ZipFile) externData.get("zipFile");

        model.setUserGestionnaire((String) externData.get("userGestionnaire"));
        model.setAttachementName(zipFile.getName());
    }

    protected abstract void extractData() throws RuntimeException;

    protected void initModel(String messageId, String businessProcessId, String type, String subject, LocalDate messageDate) {
        model = new GFSedexModel();

        model.setMessageId(messageId);
        model.setBusinessProcessId(businessProcessId);
        model.setFormulaireType(type);
        model.setMessageSubject(subject);
        model.setMessageDate(messageDate);
    }

    protected void setBeneficiaireData(NaturalPersonsOASIDIType person) {
        if(person != null) {
            model.setNssBeneficiaire(person.getVn().toString());
            model.setNomBeneficiaire(person.getOfficialName());
            model.setPrenomBenefiaicaire(person.getFirstName());
            model.setNaissanceBeneficiaire(person.getDateOfBirth().getYearMonthDay().toGregorianCalendar().toZonedDateTime().toLocalDate());
        }
    }

    @Override
    public void save(ValidationResult result) throws RuntimeException {
        EFormFileService fileService = EFormFileService.instance();
        try {
            fileService.send(zipFile.getFile().getAbsolutePath(), GFFileUtils.generateFilePath(model));
            setFormulaireData(result);
        } catch (SFtpOperationException e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'envoie du fichier sur le server FTP : {}", model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        } catch (Exception e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);

            //Nétoyage du fichier si l'enregistrement en db c'est mal passé.
            try {
                fileService.remove(GFFileUtils.generateFilePath(model) + File.separator + zipFile.getName());
            }catch (Exception ex) {
                LOG.error("GFFormHandler#saveDataInDb - Le nétoyage du fichier a échoué", ex);
            }

            throw new JadeApplicationRuntimeException(e);
        }
    }

    private void setFormulaireData(ValidationResult result) throws Exception {
            GFFormulaireModel dbModel = new GFFormulaireModel();
            dbModel.setMessageId(model.getMessageId());
            dbModel.setBusinessProcessId(model.getBusinessProcessId());
            dbModel.setType(model.getFormulaireType());
            dbModel.setSubject(model.getMessageSubject());
            dbModel.setStatus(GFStatusEForm.RECEIVE.getCodeSystem());
            dbModel.setDate(Dates.formatSwiss(model.getMessageDate()));
            dbModel.setBeneficiaireNss(model.getNssBeneficiaire());
            dbModel.setBeneficiaireNom(model.getNomBeneficiaire());
            dbModel.setBeneficiairePrenom(model.getPrenomBenefiaicaire());
            dbModel.setBeneficiaireDateNaissance(Dates.formatSwiss(model.getNaissanceBeneficiaire()));
            dbModel.setUserGestionnaire(model.getUserGestionnaire());
            dbModel.setAttachementName(model.getAttachementName());

            GFEFormServiceLocator.getGFEFormDBService().create(dbModel, result);
    }

    public void setMessage(Object message){
        this.message = message;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
