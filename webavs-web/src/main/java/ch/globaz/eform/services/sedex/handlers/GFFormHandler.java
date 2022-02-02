package ch.globaz.eform.services.sedex.handlers;

import ch.eahv_iv.xmlns.eahv_iv_common._4.AttachmentFileType;
import ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.eform.business.models.GFAttachementModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.services.sedex.model.GFSedexModel;
import eform.eahv_iv.afv_common.AttachmentType;
import globaz.common.util.CommonBlobUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public abstract class GFFormHandler {

    protected GFSedexModel model;

    public abstract boolean setDataFromFile(SimpleSedexMessage currentSimpleMessage, Object sedexObject, String currentSedexFolder);

    protected void setAttachements(Map<String, String> attachments) {
        ArrayList<String> attachementFiles = new ArrayList<>();
        Set set=attachments.entrySet();//Converting to Set so that we can traverse
        Iterator itr=set.iterator();
        while(itr.hasNext()){
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry entry=(Map.Entry)itr.next();
            File file = new File(entry.getKey().toString());
                if (file.exists() && file.isFile()) {
                    attachementFiles.add(file.getPath());
                }

        }
        model.setAttachementFile(attachementFiles);
    }

    protected void setBeneficiaireData(NaturalPersonsOASIDIType person) {
        if(person != null) {
            model.setNssBeneficiaire(person.getVn().toString());
            model.setNomBeneficiaire(person.getOfficialName());
            model.setPrenomBenefiaicaire(person.getFirstName());
            model.setNaissanceBeneficiaire(person.getDateOfBirth().getYearMonthDay().toGregorianCalendar().toZonedDateTime().toLocalDate());
        }
    }

    public boolean saveDataInDb(BSession bSession) throws Exception {
        boolean saveSucceed;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) bSession.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            if (transaction.hasErrors()) {
                LOG.error("Des erreurs ont été trouvés dans la transaction. : {}", transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            setFormulaireData(bSession);
            for (String attachement:
                 model.getAttachementFile()) {
                setAttachmentData(bSession, attachement);
            }
            if (hasError(bSession, transaction)) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
            saveSucceed = true;
        } catch (FileNotFoundException e) {
            LOG.error("Fichier non trouvé : " + model.getMessageId(), e);
            saveSucceed = false;
        } catch (Exception e) {
            LOG.error("Erreur lors de l'ajout du formulaire en DB  : {}", model.getMessageId(), e);
            if (transaction != null) {
                transaction.rollback();
            }
            saveSucceed = false;
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (IOException e) {
                LOG.error("Impossible de cloture la transaction", e);
                saveSucceed = false;
            }
        }
        return saveSucceed;
    }

    private void setFormulaireData(BSession bSession) throws Exception {
            GFFormulaireModel dbModel = new GFFormulaireModel();
            dbModel.setSession(bSession);
            dbModel.setFormulaireId(model.getMessageId());
            dbModel.setFormulaireSubject(model.getMessageSubject());
            dbModel.setFormulaireDate(model.getMessageDate());
            dbModel.setNssBeneficiaire(model.getNssBeneficiaire());
            dbModel.setNomBeneficiaire(model.getNomBeneficiaire());
            dbModel.setPrenomBeneficiaire(model.getPrenomBenefiaicaire());
            dbModel.setDateNaissanceBeneficiaire(model.getNaissanceBeneficiaire());
            if (model.getZipFile() != null) {
                InputStream target = new ByteArrayInputStream(CommonBlobUtils.fileToByteArray(model.getZipFile()));
                dbModel.setFichierZip(target);
            }
            dbModel.add();
    }

    private void setAttachmentData(BSession bSession, String attachement) throws Exception {

            GFAttachementModel attachementModel = new GFAttachementModel();
            attachementModel.setSession(bSession);
            attachementModel.setFormulaireId(model.getMessageId());
            //attachementModel.setNom(attachement.getName());
            if(attachement != null) {
                InputStream target = new ByteArrayInputStream(CommonBlobUtils.fileToByteArray(attachement));
                attachementModel.setFichier(target);
            }
            attachementModel.add();

    }

    protected String getZip(String fileLocation) {
        File f = new File(fileLocation);
        for (File file : f.listFiles()) {
            if (file.isFile() && file.getPath().endsWith(".zip")) {
                return file.getPath();
            }
        }
        return null;
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
}
