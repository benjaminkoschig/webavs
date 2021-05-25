package globaz.apg.properties;

import globaz.globall.db.BSession;

import java.time.LocalDate;

public interface Parameter {

    <T> T findValue(String date, BSession session);

    LocalDate findDateDebutValidite(String date, BSession session);

    <T> T findValueOrWithDateNow(String date, BSession session);
}
