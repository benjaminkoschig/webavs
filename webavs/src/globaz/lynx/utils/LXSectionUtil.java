package globaz.lynx.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.section.LXSection;

public class LXSectionUtil {

    /**
     * Return la section.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    public static LXSection getSection(BSession session, BTransaction transaction, String idSection) throws Exception {
        LXSection section = new LXSection();
        section.setSession(session);
        section.setIdSection(idSection);

        section.retrieve(transaction);

        if (transaction != null && transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (section.hasErrors()) {
            throw new Exception(section.getErrors().toString());
        }

        if (section.isNew()) {
            throw new Exception(session.getLabel("SECTION_NON_RESOLUE"));
        }

        return section;
    }

    /**
     * Constructeur
     */
    protected LXSectionUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
