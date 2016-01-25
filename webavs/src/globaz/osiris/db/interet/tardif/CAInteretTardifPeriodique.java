package globaz.osiris.db.interet.tardif;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.osiris.utils.CADateUtil;

public class CAInteretTardifPeriodique extends CAInteretTardif {

    /**
     * @see super.getDateCalculDebutInteret(session, transaction, ecriture)
     */
    @Override
    public JADate getDateCalculDebutInteret(BSession session, BTransaction transaction) throws Exception {
        JADate sectionDate = new JADate(getSection(session, transaction).getDateFinPeriode());
        sectionDate = session.getApplication().getCalendar().addMonths(sectionDate, 1);
        sectionDate.setDay(1);

        return sectionDate;
    }

    /**
     * @see super.isTardif(session, transaction, ecriture)
     */
    @Override
    public boolean isTardif(BSession session, BTransaction transaction, String dateToTest) throws Exception {
        JADate dateDebutSoumis = new JADate(getSection(session, transaction).getDateFinPeriode());

        JACalendar cal = session.getApplication().getCalendar();
        dateDebutSoumis = cal.addMonths(dateDebutSoumis, 1);
        dateDebutSoumis.setDay(1);
        dateDebutSoumis = cal.addDays(dateDebutSoumis, 30 - 1);

        dateDebutSoumis = CADateUtil.getDateOuvrable(dateDebutSoumis);

        return (cal.compare(new JADate(dateToTest), dateDebutSoumis) == JACalendar.COMPARE_FIRSTUPPER);
    }

}
