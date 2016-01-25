package globaz.hera.tools.impl;

import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.hera.application.SFApplication;
import globaz.hera.tools.SFAVSUtils;
import globaz.prestation.tools.PRAVSUtils;
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
 * @author scr
 */

// TODO les noavs bidon retournée par cette classe sont des ancien no AVS.
// Changer lorsque les nouvelles
// règles seront définies.

public class SFNSS13ChiffresUtils extends SFAVSUtils {

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
            try {
                throw new UnsupportedOperationException(
                        (new BSession(SFApplication.DEFAULT_APPLICATION_SF)).getLabel("ERROR_OPERATION_NON_PERMISE"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.prestation.tools.PRAVSUtils#getInstance(String)
     */
    public SFNSS13ChiffresUtils() {
    }

    /**
     * @see globaz.prestation.tools.PRAVSUtils#getSexe(String)
     */
    @Override
    public String getSexe(String noAVS) {
        return null;
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
        return "111.11.111.113";
    }
}
