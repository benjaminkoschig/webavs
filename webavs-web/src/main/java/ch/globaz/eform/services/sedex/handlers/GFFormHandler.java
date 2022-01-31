package ch.globaz.eform.services.sedex.handlers;

import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.services.sedex.model.GFSedexModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public abstract class GFFormHandler {

    protected GFSedexModel model;

    public abstract boolean getDataFromFile(Object sedexObject, String zipFileLocation) throws IOException;

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

            GFFormulaireModel dbModel = new GFFormulaireModel();
            dbModel.setSession(bSession);
            dbModel.setFormulaireId(model.getMessageId());
            dbModel.setFormulaireSubject(model.getMessageSubject());
            dbModel.setFormulaireDate(model.getMessageDate());
            dbModel.setNssBeneficiaire(model.getNssBeneficiaire());
            dbModel.setNomBeneficiaire(model.getNomBeneficiaire());
            dbModel.setPrenomBeneficiaire(model.getPrenomBenefiaicaire());
            dbModel.setDateNaissanceBeneficiaire(model.getNaissanceBeneficiaire());
            // TODO : Récupérer le fichier zip
            dbModel.add();
            if (hasError(bSession, transaction)) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
            bSession.getCurrentThreadTransaction().commit();
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

    protected void zipEntryFile(String fileLocation, ZipOutputStream out) throws IOException {
        final int bufferSize = 2048;
        FileInputStream fi = new FileInputStream(fileLocation);
        try(BufferedInputStream origin = new BufferedInputStream(fi, bufferSize)) {
            byte data[] = new byte[bufferSize];
            ZipEntry entry = new ZipEntry(fileLocation);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, bufferSize)) != -1) {
                out.write(data, 0, count);
            }
        }catch(IllegalArgumentException e){
            LOG.error("");
        }
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
