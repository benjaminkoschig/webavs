package ch.globaz.eform.businessimpl.services.sedex.handlers;

import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.common.util.Dates;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.sedex.GFSedexModel;
import ch.globaz.eform.constant.GFStatusEForm;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.time.LocalDate;

@Slf4j
public abstract class GFFormHandler {
    protected BSession session;
    protected GFSedexModel model;
    protected Object message;

    public void setDataFromFile(String userGestionnaire, String zipName, byte[] zipByte) throws RuntimeException {
        if(message != null){
            try {
                extractData();
                model.setUserGestionnaire(userGestionnaire);
                model.setAttachementName(zipName);
                model.setZipFile(zipByte);
            }catch(ClassCastException e){
                LOG.error("GFFormHandler#setDataFromFile - Erreur de type de message.", e);
                throw new JadeApplicationRuntimeException(e);
            }
        }
    }

    protected abstract void extractData() throws RuntimeException;

    protected void initModel(String messageId, String type, String subject, LocalDate messageDate) {
        model = new GFSedexModel();

        model.setMessageId(messageId);
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

    public void saveDataInDb(ValidationResult result) throws RuntimeException {
        try {
            setFormulaireData(result);
        } catch (FileNotFoundException e) {
            LOG.error("GFFormHandler#saveDataInDb - Fichier non trouvé : " + model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        } catch (Exception e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        }
    }

    private void setFormulaireData(ValidationResult result) throws Exception {
            GFFormulaireModel dbModel = new GFFormulaireModel();
            dbModel.setMessageId(model.getMessageId());
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
            dbModel.setAttachement(model.getZipFile());

            GFEFormServiceLocator.getGFEFormService().create(dbModel, result);
    }

    /**
     *
     * @param session: la session en cours
     * @param transaction: la transaction en cours
     * @return True si le traitement à une erreur.
     */
    private boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }

    public void setMessage(Object message){
        this.message = message;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
