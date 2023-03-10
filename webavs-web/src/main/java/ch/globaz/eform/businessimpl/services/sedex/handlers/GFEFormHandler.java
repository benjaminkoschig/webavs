package ch.globaz.eform.businessimpl.services.sedex.handlers;

import ch.globaz.eform.web.application.GFApplication;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class GFEFormHandler implements GFSedexhandler {
    public static final String TYPE_FORMULAIRE = "^(\\d{3}\\.\\d{3}(?:\\.\\d)?)(?:\\s-\\s.+)?$";

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

    protected void initModel(String messageId, String businessProcessId, String type, String subType, String subject, LocalDate messageDate) {
        model = new GFSedexModel();

        model.setMessageId(messageId);
        model.setBusinessProcessId(businessProcessId);
        model.setFormulaireType(type);
        model.setFormulaireSubType(subType);
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
    public void create(ValidationResult result) throws RuntimeException {
        EFormFileService fileService = new EFormFileService(GFApplication.EFORM_HOST_FILE_SERVER);
        try {
            fileService.send(zipFile.getFile().getAbsolutePath(), GFFileUtils.generateEFormFilePath(model));
            setFormulaireData(result);
        } catch (SFtpOperationException e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'envoie du fichier sur le server FTP : {}", model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        } catch (Exception e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);

            //N?toyage du fichier si l'enregistrement en db c'est mal pass?.
            try {
                fileService.remove(GFFileUtils.generateEFormFilePath(model) + File.separator + zipFile.getName());
            }catch (Exception ex) {
                LOG.error("GFFormHandler#saveDataInDb - Le nettoyage du fichier a ?chou?", ex);
            }

            throw new JadeApplicationRuntimeException(e);
        }
    }

    @Override
    public void update(ValidationResult result) {
    }

    private void setFormulaireData(ValidationResult result) throws Exception {
            GFFormulaireModel dbModel = new GFFormulaireModel();
            dbModel.setMessageId(model.getMessageId());
            dbModel.setBusinessProcessId(model.getBusinessProcessId());
            dbModel.setType(model.getFormulaireType());
            dbModel.setSubType(model.getFormulaireSubType());
            dbModel.setSubject(cleanSubject(model.getMessageSubject()));
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

    private String cleanSubject(String subject) {
        Pattern pattern = Pattern.compile(TYPE_FORMULAIRE);
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public void setMessage(Object message){
        this.message = message;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
