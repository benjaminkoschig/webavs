package ch.globaz.vulpecula.domain.models.holidays;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Jours fériés paramétrés dans le fichier holidays.xml
 * 
 */
public class JoursFeries {
    private static final String HOLIDAYS_XML_PATH = "ch/globaz/vulpecula/holidays/holidays.xml";
    private static JoursFeries joursFeries = null;
    private Holidays holidays = null;

    private JoursFeries() throws JAXBException, URISyntaxException {
        loadJoursFeries();
    }

    /**
     * Retourne l'instance singleton de JoursFeries.
     * 
     * @return l'instance
     * @throws GlobazTechnicalException si une instance ne peut pas être créée
     */
    public static JoursFeries getInstance() {
        if (joursFeries == null) {
            try {
                joursFeries = new JoursFeries();
            } catch (JAXBException e) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            } catch (URISyntaxException e) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            }
        }
        return joursFeries;
    }

    /**
     * Retourne <b>true</b> si le jour est un jour férié (déclaré dans holidays.xml)
     * 
     * @param year l'année
     * @param month le mois
     * @param day le jour
     * @return <b>true</b> si c'est un jour férié, sinon <b>false</b>
     */
    public boolean isJourFerie(int year, int month, int day) {
        return getDay(year, month, day) != null;
    }

    private void loadJoursFeries() throws JAXBException, URISyntaxException {
        JAXBContext jc = JAXBContext.newInstance(Holidays.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream holidaysFile = getClass().getClassLoader().getResourceAsStream(HOLIDAYS_XML_PATH);
        holidays = (Holidays) unmarshaller.unmarshal(holidaysFile);
    }

    private Year getYear(int annee) {
        List<Year> years = holidays.getYear();
        for (Year year : years) {
            if (year.getValue() == annee) {
                return year;
            }
        }
        return null;
    }

    private Month getMonth(int annee, int mois) {
        Year year = getYear(annee);
        if (year == null) {
            return null;
        }

        List<Month> months = year.getMonth();
        for (Month month : months) {
            if (month.getValue() == mois) {
                return month;
            }
        }
        return null;
    }

    private Day getDay(int annee, int mois, int jour) {
        Month month = getMonth(annee, mois);
        if (month == null) {
            return null;
        }

        List<Day> days = month.getDay();
        for (Day day : days) {
            if (day.getValue() == jour) {
                return day;
            }
        }
        return null;
    }
}
