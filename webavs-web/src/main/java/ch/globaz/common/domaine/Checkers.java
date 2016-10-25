package ch.globaz.common.domaine;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import java.math.BigDecimal;
import java.util.Collection;
import ch.globaz.common.domaine.Periode.TypePeriode;

/**
 * Regroupement de méthodes statiques permettant de valider des paramètres d'entrée de méthode
 */
public class Checkers {

    public static void checkCantBeNegative(final BigDecimal value, final String nomParametre) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[" + nomParametre + "] can't be a negative amount");
        }
    }

    public static void checkCantBeNegative(final int value, final String nomParametre) {
        if (value < 0) {
            throw new IllegalArgumentException("[" + nomParametre + "] can't be a negative number");
        }
    }

    public static void checkDateMonthYear(final String date, final String nomParametre, final boolean canBeEmpty) {
        Checkers.checkNotNull(date, nomParametre);

        String message = "[" + nomParametre + "] must be a monthYear date (MM.YYYY)";

        if (!canBeEmpty) {
            Checkers.checkNotEmpty(date, nomParametre);
        }

        if (!"".equals(date) && !date.matches("(0[1-9]|1[012])(\\.)(19[0-9]{2}|2[0-9]{3})")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkDoesntHaveID(final EntiteDeDomaine objetDomaine, final String nomParametre) {
        if (objetDomaine.estInitialisee()) {
            throw new IllegalArgumentException("[" + nomParametre + "] mustn't be initialized");
        }
    }

    public static void checkFullDate(final String fullDate, final String nomParametre, final boolean canBeEmpty) {

        if (!canBeEmpty) {
            Checkers.checkNotEmpty(fullDate, nomParametre);
        }

        if (!"".equals(fullDate) && !JadeDateUtil.isGlobazDate(fullDate)) {
            throw new IllegalArgumentException("[" + nomParametre + "] must be a full date (DD.MM.YYYY)");
        }
    }

    public static void checkDateAvs(final String fullDate, final String nomParametre, final boolean canBeEmpty) {

        if (!canBeEmpty) {
            Checkers.checkNotEmpty(fullDate, nomParametre);
        }

        if (!"".equals(fullDate) && !JadeDateUtil.isGlobazDate(fullDate)) {
            // Pour les dates commençants par 00.00. , vérifier que l'année est valide
            if (fullDate.length() == 10 && "00.00".equalsIgnoreCase(fullDate.substring(0, 5))) {
                try {
                    if (JACalendar.getYear(fullDate) < 1000) {
                        throw new IllegalArgumentException("[" + nomParametre + "] is invaldid");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("[" + nomParametre + "] is invaldid");
                }
            } else {
                throw new IllegalArgumentException("[" + nomParametre + "] must be a full date (DD.MM.YYYY)");
            }
        }
    }

    public static void checkHasID(final EntiteDeDomaine objetDomaine, final String nomParametre) {
        if (!objetDomaine.estInitialisee()) {
            throw new IllegalArgumentException("[" + nomParametre + "] must be initialized");
        }
    }

    public static void checkNotEmpty(final Collection<?> collection, final String nomParametre) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("[" + nomParametre + "] can't be empty");
        }
    }

    public static void checkNotEmpty(final String string, final String nomParametre) {
        if ("".equals(string)) {
            throw new IllegalArgumentException("[" + nomParametre + "] can't be empty");
        }
    }

    public static void checkNotEmpty(final Object[] tableau, final String nomParametre) {
        if (tableau.length == 0) {
            throw new IllegalArgumentException("[" + nomParametre + "] can't be empty");
        }
    }

    public static void checkNotNull(final Object object, final String nomParametre) {
        if (object == null) {
            throw new NullPointerException("[" + nomParametre + "] can't be null");
        }
    }

    public static void checkPeriodMonthYear(final Periode periode, final String nomParametre) {
        if (periode.getType() != TypePeriode.MOIS_ANNEE) {
            throw new IllegalArgumentException("[" + nomParametre + "] must be a month year period");
        }
    }

    public static void checkPeriodMonthYear(final String startMonthYear, final String endMonthYear,
            final String nomParametreStartMonthYear, final String nomParametreEndMonthYear) {
        if (!"".equals(startMonthYear) && !"".equals(endMonthYear) && !startMonthYear.equals(endMonthYear)
                && JadeDateUtil.isDateMonthYearAfter(startMonthYear, endMonthYear)) {
            throw new IllegalArgumentException("[" + nomParametreStartMonthYear + "] can't happen after ["
                    + nomParametreEndMonthYear + "]");
        }
    }

    public static void checkIsInteger(final String integer, final String nomParametre) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("[" + nomParametre + "] must be a integer");
        }
    }

}
