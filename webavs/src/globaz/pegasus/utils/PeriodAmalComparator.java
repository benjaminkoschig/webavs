package globaz.pegasus.utils;

import java.util.Comparator;

public class PeriodAmalComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Integer yearO1 = Integer.parseInt(((String) o1).split("\\.")[1]);
        Integer yearO2 = Integer.parseInt(((String) o2).split("\\.")[1]);

        if (yearO1 < yearO2) {
            return -1;
        }
        if (yearO1 > yearO2) {
            return 1;
        }
        if (yearO1.equals(yearO2)) {
            Integer monthO1 = Integer.parseInt(((String) o1).split("\\.")[0]);
            Integer monthO2 = Integer.parseInt(((String) o2).split("\\.")[0]);
            if (monthO1 < monthO2) {
                return -1;
            }
            if (monthO1 > monthO2) {
                return 1;
            }
            if (monthO1.equals(monthO2)) {
                return 0;
            }
        }
        return 0;

    }

}
