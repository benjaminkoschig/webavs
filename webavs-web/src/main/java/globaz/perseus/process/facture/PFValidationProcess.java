/**
 * 
 */
package globaz.perseus.process.facture;

import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.LabelCommonProvider;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.qd.FactureService;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class PFValidationProcess extends PFAbstractJob {

    private String adresseMail = null;
    private List<String> listIdFacture = null;

    /**
     * @return the adresseMail
     */
    public String getAdresseMail() {
        return adresseMail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Valider et comptabiliser les Factures";
    }

    public List<String> getListIdFacture() {
        return listIdFacture;
    }

    public void setListIdFacture(List<String> listIdFacture) {
        this.listIdFacture = listIdFacture;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    @Override
    protected void process() throws Exception {
        try {
            FactureService factureService = PerseusServiceLocator.getFactureService();
            List<FactureWrapper> factures = factureService.validerMultiple(listIdFacture);
            List<FacturePourList> facturesPourXls = new ArrayList<FacturePourList>();
            for (FactureWrapper factureWrapped : factures) {
                Facture facture = factureWrapped.getFacture();
                PersonneEtendueComplexModel personne = facture.getQd().getMembreFamille().getPersonneEtendue();
                FacturePourList facturePourList = new FacturePourList();
                facturePourList.setCstypeSousType(facture.getQd().getSimpleQD().getCsType());
                facturePourList.setDateFacture(facture.getSimpleFacture().getDateFacture());
                facturePourList.setNom(personne.getTiers().getDesignation1());
                facturePourList.setNss(personne.getPersonneEtendue().getNumAvsActuel());
                facturePourList.setPrenom(personne.getTiers().getDesignation2());
                facturePourList.setDateNaissance(personne.getPersonne().getDateNaissance());
                facturePourList.setGestionaire(facture.getSimpleFacture().getIdGestionnaire());
                facturePourList.setIdFacture(facture.getId());
                facturePourList.setMontant(facture.getSimpleFacture().getMontant());
                facturePourList.setRembourse(facture.getSimpleFacture().getMontantRembourse());
                facturePourList.setEtat(facture.getSimpleFacture().getCsEtat());
                facturePourList.setErrorMessage((factureWrapped.getErrorMessage() == null || factureWrapped
                        .getErrorMessage().trim().isEmpty()) ? "" : getSession().getLabel(
                        factureWrapped.getErrorMessage()));
                facturesPourXls.add(facturePourList);
            }

            String path = (Jade.getInstance().getPersistenceDir() + "list_validation_facture" + JadeUUIDGenerator
                    .createStringUUID());
            File xls = SimpleOutputListBuilder.newInstance().addList(facturesPourXls)
                    .classElementList(FacturePourList.class).asXls().outputName(path)
                    .local(new Locale(getSession().getIdLangueISO())).build();
            String[] filenames = new String[1];
            filenames[0] = xls.getAbsolutePath();

            JadeSmtpClient.getInstance().sendMail(
                    adresseMail,
                    "La validation des factures pour les pc famille s'est treminée",
                    "La validation des factures pour les pc famille s'est treminée. Nb facture traitées: "
                            + facturesPourXls.size(), filenames);
        } catch (Exception e) {
            sendMailError(adresseMail, e, this, "");
        }
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeThread.rollbackSession();
        } else {
            JadeThread.commitSession();
        }

    }

    // private void recuperDcoumentEtEnvoiMail(BProcess process, String mailObject) throws Exception {
    // Object[] attachedDocumentArray = process.getAttachedDocuments().toArray();
    // String[] attachedDocumentLocationArray = null;
    // if (attachedDocumentArray != null) {
    // int nbElem = attachedDocumentArray.length;
    // attachedDocumentLocationArray = new String[nbElem];
    // for (int k = 0; k < nbElem; k++) {
    // attachedDocumentLocationArray[k] = ((JadePublishDocument) attachedDocumentArray[k])
    // .getDocumentLocation();
    // }
    // }
    //
    // }

    /**
     * @param adresseMail
     *            the adresseMail to set
     */
    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    private void sendMailError(String mail, Throwable e, AbstractJadeJob proces, String messageInfo,
            Object... objectsToJson) throws Exception {
        String isoLangue = proces.getSession().getIdLangueISO();
        String numAffile = "";

        String body = messageInfo + "\n";
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
            String message = "";
            for (JadeBusinessMessage jadeBusinessMessage : messages) {
                message = message
                        + JadeI18n.getInstance().getMessage(getSession().getIdLangueISO(),
                                jadeBusinessMessage.getMessageId()) + "\n";
            }
            body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + message;
        } else {
            if (getSession() != null) {
                body = body + getSession().getErrors();
            }
            if (getTransaction() != null) {
                body = body + getTransaction().getErrors();
            }
            if (e != null) {
                body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + e.getMessage();
            }
        }

        body = body + "\n\n" + LabelCommonProvider.getLabel("PROCESS_TEXT_MAIL_ERROR", isoLangue);

        body = body + "\n\n\n********************* "
                + LabelCommonProvider.getLabel("PROCESS_INFORMATION_GLOBAZ", isoLangue) + "*********************\n\n";
        String bodyGlobaz = "";
        if (e != null) {
            bodyGlobaz = bodyGlobaz + "Stack: \t " + Throwables.getStackTraceAsString(e) + "\n\n";
        }
        // new GsonBuilder().setPrettyPrinting().create()

        if (objectsToJson != null) {
            for (Object object : objectsToJson) {
                try {
                    bodyGlobaz = bodyGlobaz + "Object:\t " + new Gson().toJson(object) + "\n\n";
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            bodyGlobaz = bodyGlobaz + "Params:\t " + new Gson().toJson(proces) + "\n\n";
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            if (JadeThread.logHasMessagesToLevel(JadeBusinessMessageLevels.ERROR)) {
                bodyGlobaz = bodyGlobaz + "Thread messages: "
                        + new Gson().toJson(JadeThread.logMessagesToLevel(JadeBusinessMessageLevels.ERROR)) + "\n\n";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        body = body + bodyGlobaz;
        JadeSmtpClient.getInstance().sendMail(
                mail,
                proces.getName() + " - " + LabelCommonProvider.getLabel("PROCESS_IN_ERROR", isoLangue) + " "
                        + numAffile, body, null);
        if (e != null) {
            e.printStackTrace();
        }
    }
}
