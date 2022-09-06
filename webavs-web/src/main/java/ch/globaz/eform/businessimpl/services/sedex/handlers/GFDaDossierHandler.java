package ch.globaz.eform.businessimpl.services.sedex.handlers;

import ch.globaz.common.util.Dates;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import globaz.globall.db.BSession;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class GFDaDossierHandler implements GFSedexhandler {
    protected BSession session;
    protected GFDaDossierModel model;
    protected Object message;

    @Override
    public void setData(Map<String, Object> externData) throws RuntimeException {
    }

    protected abstract void extractData() throws RuntimeException;

    protected void initModel(String messageId, String yourBusinessReferenceId, LocalDate messageDate) {
        model = new GFDaDossierModel();

        model.setMessageId(messageId);
        model.setOurBusinessRefId(UUID.randomUUID().toString());
        model.setYourBusinessRefId(yourBusinessReferenceId);
        model.setType(GFTypeDADossier.SOLICITATION.getCodeSystem());
        model.setStatus(GFStatusDADossier.TO_SEND.getCodeSystem());
        model.setCreationSpy(Dates.formatSwiss(messageDate));
    }

    protected void initCaisseData(String senderId) {
        if(senderId != null) {
            //todo recherche de la caisse pas son id Sedex modification

            model.setCodeCaisse("000000");
        }
    }

    protected void setBeneficiaireData(NaturalPersonsOASIDIType person) {
        if(person != null) {
            model.setNssAffilier(person.getVn().toString());
        }
    }

    @Override
    public void save(ValidationResult result) throws RuntimeException {
        try {
            extractData();
            GFEFormServiceLocator.getGFDaDossierDBService().create(model, result);
        } catch (Exception e) {
            LOG.error("GFFormHandler#saveDataInDb - Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        }
    }

    public void setMessage(Object message){
        this.message = message;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
