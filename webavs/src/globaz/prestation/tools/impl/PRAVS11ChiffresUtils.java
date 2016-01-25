package globaz.prestation.tools.impl;

import globaz.globall.util.JAUtil;
import globaz.prestation.tools.PRAVSUtils;
import globaz.prestation.tools.PRStringUtils;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Extension de PRAVSUtils pour les numéros AVS à 11 chiffres. Veuillez passer par la classe parente pour obtenir une
 * instance de cette classe.
 * </p>
 * 
 * @see globaz.prestation.tools.PRAVSUtils#getInstance(String)
 * @author vre
 */
public class PRAVS11ChiffresUtils extends PRAVSUtils {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static class NoAVSIterator implements Iterator {

        // ~ Static fields/initializers
        // ---------------------------------------------------------------------------------

        private static int[] ponderations = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private int id;

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
            StringBuffer retValue = new StringBuffer();
            int modulo = 0;

            retValue.append(++id);

            for (int idChar = 0; idChar < 10; ++idChar) {
                if (idChar < retValue.length()) {
                    modulo = (retValue.charAt(idChar) - '0') * ponderations[idChar];
                } else {
                    retValue.append('0');
                }
            }

            retValue.append(11 - (modulo % 11));

            return JAUtil.formatAvs(retValue.toString());
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

    /**
     * @see globaz.prestation.tools.PRAVSUtils#getInstance(String)
     */
    public PRAVS11ChiffresUtils() {
    }

    /**
     * @see globaz.prestation.tools.PRAVSUtils#isSuisse(String)
     */
    @Override
    public boolean isSuisse(String noAVS) {
        return (PRStringUtils.extraireDigits(noAVS).charAt(9) < '5');
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
        return "111.11.111.113";
    }
}
