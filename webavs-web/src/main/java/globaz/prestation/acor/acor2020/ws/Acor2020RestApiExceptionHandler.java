package globaz.prestation.acor.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import ch.globaz.common.ws.configuration.ExceptionRequestInfo;
import ch.globaz.common.ws.configuration.RequestInfo;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@ExceptionMapper("/acor2020")
@Slf4j
public class Acor2020RestApiExceptionHandler implements ExceptionHandler {

    @Override
    public Response generateResponse(final Exception e, final Response.ResponseBuilder responseBuilder, final HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo(request);
        ExceptionRequestInfo exceptionRequestInfo = new ExceptionRequestInfo(requestInfo, e);
        BSession session = BSessionUtil.getSessionFromThreadContext();
        try {
            if (StringUtils.contains(requestInfo.getPathInfo(), "import")) {
                LOG.error("Une erreur est intervenue lors de l'importation.", e);
                String objet = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), Acor2020StandardErrorUtil.ERROR_ACOR_IMPORT_SUBJECT);
                String label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), Acor2020StandardErrorUtil.ERROR_ACOR_IMPORT);
                sendMailError(session, exceptionRequestInfo, objet);
                return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(label, e, 1, OriginType.TECHNICAL_IMPORT)).build();
            } else if (StringUtils.contains(requestInfo.getPathInfo(), "export")) {
                LOG.error("Une erreur est intervenue lors de l'exportation.", e);
                String objet = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), Acor2020StandardErrorUtil.ERROR_ACOR_EXPORT_SUBJECT);
                String label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), Acor2020StandardErrorUtil.ERROR_ACOR_EXPORT);
                sendMailError(session, exceptionRequestInfo, objet);
                return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(label, e, 1, OriginType.TECHNICAL_EXPORT)).build();
            }
        } catch (Exception exception) {
            LOG.error("Une erreur s'est produite lors de l'envoi du mail.", e);
        }
        LOG.error("Une erreur imprévue s'est produite.", e);
        String label = "Global error";
        if(session!=null) {
             label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), Acor2020StandardErrorUtil.ERROR_ACOR_GLOBAL);
        }
        return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(label, e, 1, OriginType.TECHNICAL_EXPORT)).build();
    }

    private void sendMailError(BSession session, ExceptionRequestInfo exceptionRequestInfo, String object) throws Exception {
        if(session!=null) {
            BTransaction transaction = session.getCurrentThreadTransaction();
            StringBuilder content = new StringBuilder();
            if (session.hasErrors()) {
                content.append("Errors in session : ");
                ajoutSautDeLigne(content);
                content.append(session.getErrors());
                LOG.error("ERROR in session {}", session.getErrors());
            }
            if (transaction.hasErrors()) {
                ajoutSautDeLigne(content);
                content.append("Errors in transaction : ");
                ajoutSautDeLigne(content);
                content.append(transaction.getErrors());
                LOG.error("ERROR in transaction {}", transaction.getErrors());
            }
            if (exceptionRequestInfo != null) {
                ajoutSautDeLigne(content);
                content.append("Exception information : ");
                ajoutSautDeLigne(content);
                content.append(exceptionRequestInfo);
            }
            JadeSmtpClient.getInstance().sendMail(session.getUserEMail(), object, content.toString(), null);
        }
    }

    private void ajoutSautDeLigne(StringBuilder content) {
        content.append(System.getProperty("line.separator"));
        content.append(System.getProperty("line.separator"));
    }

}
