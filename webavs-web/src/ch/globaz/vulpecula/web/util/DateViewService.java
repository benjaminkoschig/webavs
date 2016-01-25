package ch.globaz.vulpecula.web.util;

import ch.globaz.vulpecula.domain.models.common.Date;

public class DateViewService {
    public static Integer nombreJoursOuvrablesEntreDeuxDates(String dateDebut, String dateFin) {
        Date startDate = new Date(dateDebut);
        Date endDate = new Date(dateFin);
        int nb = Date.getNbWorkingDaysBetweenDatesWithFeries(startDate, endDate);
        return nb;
    }
}
