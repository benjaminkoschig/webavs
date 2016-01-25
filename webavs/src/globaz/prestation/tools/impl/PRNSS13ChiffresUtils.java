package globaz.prestation.tools.impl;

import globaz.commons.nss.NSUtil;
import globaz.prestation.tools.PRAVSUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Extension de PRAVSUtils pour les numéros AVS à 11 chiffres. Veuillez passer par la classe parente pour obtenir une
 * instance de cette classe.
 * </p>
 * 
 * @see globaz.prestation.tools.PRAVSUtils#getInstance(String)
 * @author scr
 */

public class PRNSS13ChiffresUtils extends PRAVSUtils {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static class NoAVSIterator implements Iterator {

        // ~ Static fields/initializers
        // ---------------------------------------------------------------------------------

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean hasNext() {
            return true;
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public Object next() {
            String retValue = PRNSS13ChiffresUtils.getNSSErrone(0);
            return NSUtil.formatAVSUnknown(retValue);
        }

        /**
         * @throws UnsupportedOperationException
         *             DOCUMENT ME!
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("cette opération n'est pas permise");
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static String getNSSErrone(int i) {

        List nssErrones = new ArrayList();

        nssErrones.add("100.0000.0000.01");
        nssErrones.add("101.0000.0000.03");
        nssErrones.add("102.0000.0000.04");
        nssErrones.add("103.0000.0000.05");
        nssErrones.add("104.0000.0000.06");
        nssErrones.add("105.0000.0000.07");
        nssErrones.add("106.0000.0000.08");
        nssErrones.add("107.0000.0000.09");
        nssErrones.add("108.0000.0000.10");
        nssErrones.add("109.0000.0000.11");
        nssErrones.add("110.0000.0000.12");
        nssErrones.add("111.0000.0000.13");
        nssErrones.add("112.0000.0000.14");
        nssErrones.add("113.0000.0000.15");
        nssErrones.add("114.0000.0000.16");
        nssErrones.add("115.0000.0000.17");
        nssErrones.add("116.0000.0000.18");
        nssErrones.add("117.0000.0000.20");
        nssErrones.add("118.0000.0000.21");
        nssErrones.add("119.0000.0000.22");
        nssErrones.add("120.0000.0000.23");
        nssErrones.add("121.0000.0000.24");
        nssErrones.add("122.0000.0000.25");
        nssErrones.add("123.0000.0000.27");
        nssErrones.add("124.0000.0000.28");
        nssErrones.add("125.0000.0000.29");

        return (String) nssErrones.get(i);

    }

    /**
     * @see globaz.prestation.tools.PRAVSUtils#getInstance(String)
     */
    public PRNSS13ChiffresUtils() {
    }

    /**
     * @see globaz.prestation.tools.PRAVSUtils#isSuisse(String)
     */
    @Override
    public boolean isSuisse(String noAVS) {
        return noAVS.startsWith("756");
    }

    /**
     * @see PRAVSUtils#iteratorNoAVSBidon()
     */
    @Override
    public Iterator iteratorNoAVSBidon() {
        return new NoAVSIterator();
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @see PRAVSUtils#noAVSBidon()
     */
    @Override
    public String noAVSBidon() {
        return "756.0000.0000.01";
    }
}
