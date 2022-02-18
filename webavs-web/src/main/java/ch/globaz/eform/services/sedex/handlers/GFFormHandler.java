package ch.globaz.eform.services.sedex.handlers;

import ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.services.sedex.model.GFSedexModel;
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

    public void setDataFromFile(String userGestionnaire, String zipPath) throws RuntimeException {
        if(message != null){
            try {
                extractData();
                model.setUserGestionnaire(userGestionnaire);
                model.setZipFilePath(zipPath);
            }catch(ClassCastException e){
                LOG.error("Erreur de type de message.", e);
                throw new JadeApplicationRuntimeException(e);
            }
        }
    }

    protected abstract void extractData() throws RuntimeException;

    protected void initModel(String messageId, String subject, LocalDate messageDate) {
        model = new GFSedexModel();

        model.setMessageId(messageId);
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

    public void saveDataInDb() throws RuntimeException {
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            if (transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : {}", transaction.getErrors());
                transaction.clearErrorBuffer();
                throw new JadeApplicationRuntimeException("Des erreurs ont été trouvés dans la transaction!");
            }
            setFormulaireData();
            if (hasError(session, transaction)) {
                transaction.rollback();
                LOG.error("Des erreurs ont été trouvés dans la transaction!");
                throw new JadeApplicationRuntimeException("Des erreurs ont été trouvés dans la transaction!");
            } else {
                transaction.commit();
            }
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + model.getMessageId(), e);
            throw new JadeApplicationRuntimeException(e);
        } catch (Exception e) {
            LOG.error("Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    LOG.error("Impossible de rollback la transaction", e1);
                }

            }
            throw new JadeApplicationRuntimeException(e);
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e) {
                LOG.error("Impossible de cloture la transaction", e);
            }
        }
    }

    private void setFormulaireData() throws Exception {
            GFFormulaireModel dbModel = new GFFormulaireModel(session);
            dbModel.setSession(session);
            dbModel.setFormulaireId(model.getMessageId());
            dbModel.setFormulaireSubject(model.getMessageSubject());
            dbModel.setNomFormulaire(session.getLabel(model.getMessageSubject()));
            dbModel.setFormulaireDate(model.getMessageDate());
            dbModel.setNssBeneficiaire(model.getNssBeneficiaire());
            dbModel.setNomBeneficiaire(model.getNomBeneficiaire());
            dbModel.setPrenomBeneficiaire(model.getPrenomBenefiaicaire());
            dbModel.setDateNaissanceBeneficiaire(model.getNaissanceBeneficiaire());
            dbModel.setUserGestionnaire(model.getUserGestionnaire());
            dbModel.addAttachement(model.getZipFilePath());
            dbModel.add();
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
