package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSortedByDate {
    final static SimpleDateFormat dateFormatMMsyyyy = new SimpleDateFormat("MM.yyyy");

    public interface DatesStringToCompare<T> {
        public String[] getDates(T val1, T val2);
    }

    private static int compareDate(String[] dates) {
        try {
            return dateFormatMMsyyyy.parse(dates[0]).compareTo(dateFormatMMsyyyy.parse(dates[1]));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date non parsable. date1: " + dates[0] + " date2: " + dates[1]);
        }
    }

    public static <T> void sortDateMmsyyyyAsc(List<T> list, final DatesStringToCompare<T> datesStringToCompare) {

        Comparator<T> comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return compareDate(datesStringToCompare.getDates(o1, o2));
            }
        };

        Collections.sort(list, comparator);
    }

    public static <T> void sortDateMmsyyyyDesc(List<T> list, final DatesStringToCompare<T> datesStringToCompare) {

        Comparator<T> comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return -1 * compareDate(datesStringToCompare.getDates(o1, o2));
            }
        };

        Collections.sort(list, comparator);
    }
}
