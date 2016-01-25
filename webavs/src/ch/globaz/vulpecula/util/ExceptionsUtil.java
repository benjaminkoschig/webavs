package ch.globaz.vulpecula.util;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;

public final class ExceptionsUtil {
    private ExceptionsUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Conversion de l'exception en un message compréhensible pour l'utilisateur en la transformant en une
     * UncheckedException.
     * 
     * @param ex Exception à convertir
     */
    public static void translateAndThrowUncheckedException(TauxImpositionNotFoundException ex) {
        throw new IllegalStateException(translateException(ex));
    }

    /**
     * Traduction de l'exception en un message compréhensible pour l'utilisateur.
     */
    public static String translateException(TauxImpositionNotFoundException ex, EntetePrestationComplexModel entete) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return I18NUtil.getMessageFromResource("vulpecula.is.erreur_parametrage_avec_id_dossier",
                session.getCodeLibelle(ex.getCanton()), ex.getDate().getSwissValue(),
                session.getCodeLibelle(ex.getType()), entete.getIdDossier());
    }

    public static String translateException(TauxImpositionNotFoundException ex) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return I18NUtil.getMessageFromResource("vulpecula.is.erreur_parametrage",
                session.getCodeLibelle(ex.getCanton()), ex.getDate().getSwissValue(),
                session.getCodeLibelle(ex.getType()));
    }
}
