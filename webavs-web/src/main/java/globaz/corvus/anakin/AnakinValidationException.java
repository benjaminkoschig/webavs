package globaz.corvus.anakin;

import java.util.Enumeration;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

public class AnakinValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public AnakinValidationException(String message) {
        super(message);
    }

    public static AnakinValidationException create(Enumeration<AnnonceErreur> erreurs) {
        StringBuilder message = new StringBuilder();
        while ((erreurs != null) && erreurs.hasMoreElements()) {
            AnnonceErreur erreur = erreurs.nextElement();
            message.append(erreur.getMessage()).append("\n");
        }
        return new AnakinValidationException(message.toString());
    }
}
