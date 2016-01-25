package globaz.orion.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.util.Locale;
import java.util.Set;
import ch.globaz.common.listoutput.LoaderOuter;
import ch.globaz.orion.businessimpl.services.partnerWeb.Contact;
import ch.globaz.orion.businessimpl.services.partnerWeb.ContactEbusinessAffilie;
import ch.globaz.orion.businessimpl.services.partnerWeb.PartnerWebServiceImpl;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import com.google.common.base.Throwables;

public class EBGenererListContact extends EBAbstractJadeJob {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PROCESS_LISTE_DES_CONTACTS_DESCRIPTION");
    }

    @Override
    public String getName() {
        return getSession().getLabel("PROCESS_LISTE_DES_CONTACTS");
    }

    String generateFileName(String numeroInforom) {
        return Jade.getInstance().getPersistenceDir() + numeroInforom + "_"
                + getName().replaceAll(" ", "_").toLowerCase() + "_" + JadeUUIDGenerator.createStringUUID();
    }

    @Override
    protected void process() throws Exception {
        try {
            LoaderOuter<String, Set<ContactEbusinessAffilie>, String> loaderOuter = new LoaderOuter<String, Set<ContactEbusinessAffilie>, String>() {
                @Override
                public String out(Set<ContactEbusinessAffilie> list) {
                    String nomDoc = generateFileName(Contact.NUMERO_INFOROM);

                    Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());

                    SimpleOutputListBuilder<ContactEbusinessAffilie> builder = new SimpleOutputListBuilder<ContactEbusinessAffilie>();

                    File file = builder.local(locale).classValue(ContactEbusinessAffilie.class).asXls()
                            .outputName(nomDoc).build();
                    return file.getAbsolutePath();
                }

                @Override
                public Set<ContactEbusinessAffilie> load(String param) throws JadeApplicationException {
                    Set<ContactEbusinessAffilie> list;
                    list = PartnerWebServiceImpl.searchAllActivContactAffilie();
                    return list;
                }
            };
            loaderOuter.run();
            loaderOuter.getTime().setNombre(loaderOuter.getDataLoaded().size());

            sendCompletionMail(email, loaderOuter);

        } catch (Exception e) {
            JadeSmtpClient.getInstance().sendMail(email, getName() + " - Error", Throwables.getStackTraceAsString(e),
                    null);
        }
    }

    void sendCompletionMail(String email, LoaderOuter<String, Set<ContactEbusinessAffilie>, String> loaderOuter)
            throws Exception {
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

        String body = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(logs.getMessages(), getSession().getIdLangueISO());

        // body = body + "\n" + loaderOuter.getTime().toString();
        JadeSmtpClient.getInstance().sendMail(email, subject, body, new String[] { loaderOuter.getOut() });
    }
}
