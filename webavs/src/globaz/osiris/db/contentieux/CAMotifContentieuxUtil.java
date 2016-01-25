package globaz.osiris.db.contentieux;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.exceptions.CATechnicalException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CAMotifContentieuxUtil {

    public static boolean hasMotifContentieux(BSession session, String idCompteAnnexe, String idSection,
            String idMotifBlocage) throws CATechnicalException {
        if (JadeStringUtil.isIntegerEmpty(idMotifBlocage)) {
            throw new CATechnicalException("[idMotifBlocage] mustn't be null or empty");
        }
        return CAMotifContentieuxUtil.hasMotifsContentieuxForYear(session, idCompteAnnexe, idSection,
                new HashSet<String>(Arrays.asList(idMotifBlocage)), null);
    }

    public static boolean hasMotifContentieuxForYear(BSession session, String idCompteAnnexe, String idSection,
            String idMotifBlocage, String year) throws CATechnicalException {
        if (JadeStringUtil.isIntegerEmpty(idMotifBlocage)) {
            throw new CATechnicalException("[idMotifBlocage] mustn't be null or empty");
        }
        return CAMotifContentieuxUtil.hasMotifsContentieuxForYear(session, idCompteAnnexe, idSection,
                new HashSet<String>(Arrays.asList(idMotifBlocage)), year);
    }

    public static boolean hasMotifsContentieuxForYear(BSession session, String idCompteAnnexe, String idSection,
            Set<String> idsMotifBlocage, String year) throws CATechnicalException {

        if ((session == null) || !session.isConnected()) {
            throw new CATechnicalException("A valid and connected session is needed");
        }

        if (JadeStringUtil.isIntegerEmpty(idSection) && JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            throw new CATechnicalException("At least one of [idSection,idCommpteAnnexe] must be given");
        }

        if ((idsMotifBlocage == null) || idsMotifBlocage.isEmpty()) {
            throw new CATechnicalException("[idsMotifBlocage] mustn't be null or empty");
        }

        CAMotifContentieuxManager manager = new CAMotifContentieuxManager();
        manager.setSession(session);

        manager.setForIdMotifBlocageIn(idsMotifBlocage);

        if (!JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            manager.setForIdCompteAnnexe(idCompteAnnexe);
        }

        if (!JadeStringUtil.isIntegerEmpty(idSection)) {
            manager.setForIdSection(idSection);
        }

        if (!JadeStringUtil.isBlank(year)) {
            manager.setForDateInYear(year);
        }

        try {
            manager.find();
        } catch (Exception ex) {
            throw new CATechnicalException("error while retrieving [CAMotifContentieux]", ex);
        }

        return !manager.isEmpty();
    }
}
