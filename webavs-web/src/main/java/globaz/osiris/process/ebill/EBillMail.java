package globaz.osiris.process.ebill;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.smtp.JadeSmtpClient;

public class EBillMail {

    public static final String REGEX_MAIL= "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String CONFIRMATION_TITLE = "ebill.email.confirmation.titre";
    private static final String CONFIRMATION_MESSAGE = "ebill.email.confirmation.message";

    public static void sendMailConfirmation(String email, String codeIso) throws PropertiesException, EBillMailException {
        String from = CAProperties.EBILL_EMAIL_CONFIRMATION.getValue();
        if(from.isEmpty()) {
            throw new EBillMailException("L'adresse mail de la propriété "+CAProperties.EBILL_EMAIL_CONFIRMATION.getPropertyName()+" est vide");
        } else if(!checkMail(from)) {
            throw new EBillMailException("L'adresse mail de la propriété "+CAProperties.EBILL_EMAIL_CONFIRMATION.getPropertyName()+" n'est pas valide : "+from+"\n("+REGEX_MAIL+")");
        }
        String titre = JadeI18n.getInstance().getMessage(codeIso, CONFIRMATION_TITLE);
        String description = JadeI18n.getInstance().getMessage(codeIso, CONFIRMATION_MESSAGE);

        try {
            JadeSmtpClient.getInstance().sendMail(from, email, titre, description, null);
        } catch (Exception e){
            throw new EBillMailException(e.getMessage(), e);
        }
    }

    public static boolean checkMail(String mailAdress) {
        return mailAdress.matches(REGEX_MAIL);
    }

    public static class EBillMailException extends JadeApplicationException {
        private static final long serialVersionUID = 1;
        public EBillMailException(String m) {
            super(m);
        }
        public EBillMailException(String m,Throwable t) {
            super(m, t);
        }
    }
}
