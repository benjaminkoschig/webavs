package globaz.orion.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEtapesSuivantes;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.orion.businessimpl.services.partnerWeb.ContactEbusinessAffilie;
import ch.globaz.orion.businessimpl.services.partnerWeb.ContactSalaire;
import ch.globaz.orion.businessimpl.services.partnerWeb.PartnerWebServiceImpl;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import com.google.common.base.Throwables;

public class EBGenererListSalaire extends EBAbstractJadeJob {

    private String email;
    private Boolean generateEtapeRappel = false;
    private String forDateReference;
    private Boolean forIsSimulation = false;
    private String forDateImpression;

    public String getForDateImpression() {
        return forDateImpression;
    }

    public void setForDateImpression(String forDateImpression) {
        this.forDateImpression = forDateImpression;
    }

    public String getForDateReference() {
        return forDateReference;
    }

    public void setForDateReference(String forDateReference) {
        this.forDateReference = forDateReference;
    }

    public Boolean getForIsSimulation() {
        return forIsSimulation;
    }

    public void setForIsSimulation(Boolean forIsSimulation) {
        this.forIsSimulation = forIsSimulation;
    }

    public Boolean getGenerateEtapeRappel() {
        return generateEtapeRappel;
    }

    public void setGenerateEtapeRappel(Boolean generateEtapeRappel) {
        this.generateEtapeRappel = generateEtapeRappel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PROCESS_LISTE_DES_SALAIRES_DESCRIPTION");
    }

    @Override
    public String getName() {
        return getSession().getLabel("PROCESS_LISTE_DES_SALAIRES");
    }

    String generateFileName(String numeroInforom) {
        return Jade.getInstance().getPersistenceDir() + numeroInforom + "_"
                + getName().replaceAll(" ", "_").toLowerCase() + "_" + JadeUUIDGenerator.createStringUUID();
    }

    @Override
    protected void process() throws Exception {
        try {

            Set<ContactEbusinessAffilie> list = PartnerWebServiceImpl.searchSuiviDeclarationContact();
            List<ContactEbusinessAffilie> listFiltrePourEnvoie = new ArrayList<ContactEbusinessAffilie>();
            for (ContactEbusinessAffilie contactEbusinessAffilie : list) {
                // On n'envois le mail qu'aux administrateur et pas à une fiduciaire
                if (contactEbusinessAffilie.isAdministrateur() && !contactEbusinessAffilie.isMandataire()) {
                    listFiltrePourEnvoie.add(contactEbusinessAffilie);
                }
            }

            if (generateEtapeRappel && !listFiltrePourEnvoie.isEmpty()) {
                Map<String, String> mapEmailByNumAffilie = new HashMap<String, String>();
                for (ContactEbusinessAffilie contactEbusinessAffilie : listFiltrePourEnvoie) {
                    mapEmailByNumAffilie.put(contactEbusinessAffilie.getNumeroAffilie(),
                            contactEbusinessAffilie.getEmail());
                }

                LEGenererEtapesSuivantes etapes = new LEGenererEtapesSuivantes();
                etapes.setSession(BSessionUtil.getSessionFromThreadContext());

                etapes.setSendMailToAffiliee(true);
                etapes.setEMailAddress(email);
                etapes.setForNumerosAffilie(new ArrayList<String>(mapEmailByNumAffilie.keySet()));

                etapes.setDatePriseEnCompte(forDateReference);
                etapes.setMapNumerosAffilieMail(mapEmailByNumAffilie);
                ArrayList<String> b = new ArrayList<String>();

                b.add(ILEConstantes.CS_DEF_FORMULE_RAPPEL_DS);
                b.add(ILEConstantes.CS_CATEGORIE_SUIVI_DS_LTN);
                etapes.setCsFormule(b);
                etapes.setDateImpression(forDateImpression);
                etapes.setCategorie(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
                etapes.setSimulation(forIsSimulation);
                etapes.setCommentaire("Génération par e-business");
                etapes.executeProcess();

                for (Iterator<JadePublishDocument> iter = etapes.getAttachedDocuments().iterator(); iter.hasNext();) {
                    JadePublishDocument document = iter.next();
                    JadeJobInfo attachedJob = JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(
                            document));
                }

                for (ContactEbusinessAffilie contactEbusinessAffilie : listFiltrePourEnvoie) {
                    if (etapes.getListEmailEtNumAffDesRappelsEnvoyes().contains(
                            contactEbusinessAffilie.getNumeroAffilie() + ":" + contactEbusinessAffilie.getEmail())) {
                        contactEbusinessAffilie.setIsEnvoye(true);
                    }
                }
            }
            String body = "";
            if (generateEtapeRappel) {
                body = body + getSession().getLabel("PROCESS_LISTE_DES_SALAIRES_SECOND_MAILE") + "\n\n";
            }

            String nomDoc = generateFileName(ContactSalaire.NUMERO_INFOROM);
            Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());

            SimpleOutputListBuilder<ContactSalaire> builder = new SimpleOutputListBuilder<ContactSalaire>();

            File file = builder.local(locale).classValue(ContactSalaire.class).asXls().addList(list).outputName(nomDoc)
                    .build();

            sendCompletionMail(email, file.getAbsolutePath(), body);

        } catch (Exception e) {
            JadeSmtpClient.getInstance().sendMail(email, getName() + " - Error", Throwables.getStackTraceAsString(e),
                    null);
        }
    }

    void sendCompletionMail(String email, String path, String body) throws Exception {
        if (email == null) {
            throw new NullPointerException("cannot send completion mail: dest list is null");
        }
        if (email.isEmpty()) {
            return;
        }
        BSession theSession = getSession();
        if (theSession == null) {
            throw new IllegalStateException("cannot send completion mail: user session is null.");
        }
        JadeBusinessLogSession logs = getLogSession();
        JadeBusinessMessage[] l = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.INFO);
        if (l != null) {
            for (JadeBusinessMessage jadeBusinessMessage : l) {
                logs.addMessage(jadeBusinessMessage);
            }
        }

        String subject = getSession().getLabel("PROCESS_OBJECT_SUCCES") + ": " + getName();
        if (logs.getMaxLevel() == JadeBusinessMessageLevels.ERROR) {
            subject = getSession().getLabel("PROCESS_OBJECT_ERROR") + " :" + getName();
        } else if (logs.getMaxLevel() == JadeBusinessMessageLevels.WARN) {
            subject = getSession().getLabel("PROCESS_OBJECT_WARNING") + ": " + getName();
        }

        String bodyError = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(logs.getMessages(), getSession().getIdLangueISO());

        // body = bodyError + body + "\n\n" + loaderOuter.getTime().toString();
        JadeSmtpClient.getInstance().sendMail(email, subject, body, new String[] { path });
    }
}
