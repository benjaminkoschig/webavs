package globaz.osiris.db.interet.tardif;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;

public class CAInteretTardifDefinitifCotPers extends CAInteretTardif {

    /**
     * @see super.getDateCalculDebutInteret(session, transaction, ecriture)
     */
    @Override
    public JADate getDateCalculDebutInteret(BSession session, BTransaction transaction) throws Exception {
        JADate sectionDate = new JADate(getSection(session, transaction).getDateSection());
        sectionDate = session.getApplication().getCalendar().addDays(sectionDate, 1);

        return sectionDate;
    }

    /**
     * @see super.isTardif(session, transaction, ecriture)
     */
    @Override
    public boolean isTardif(BSession session, BTransaction transaction, String dateToTest) throws Exception {
        return true;
    }

}
