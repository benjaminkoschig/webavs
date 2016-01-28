package globaz.ij.acor.parser.rev3;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.file.parser.PRFileParserFactory;
import globaz.prestation.file.parser.PRTextField;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public abstract class IJACORAbstractFlatFileParser {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static final String CODE_ANNONCE_REV3 = "85";

    protected static final String CODE_ANNONCE_REV4_01 = "8G01";
    protected static final String CODE_ANNONCE_REV4_02 = "8G02";
    protected static final String CODE_BASE_CALCUL = "$b";
    protected static final String CODE_DECOMPTE = "$d";
    protected static final String CODE_PRESTATION = "$s";
    private static HashMap CONFIGURATIONS;

    private static final String FILE_PARSER = "fileParserIJ.xml";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static void addConfiguration(String code, List configuration) {
        HashMap map = new HashMap();

        for (Iterator fields = configuration.iterator(); fields.hasNext();) {
            PRTextField field = (PRTextField) fields.next();

            map.put(field.getName(), field);
        }

        if (CONFIGURATIONS == null) {
            CONFIGURATIONS = new HashMap();
        }

        CONFIGURATIONS.put(code, map);
    }

    /**
     * retourne une configuration pour le code.
     * 
     * @param code
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut configuration
     */
    protected static HashMap getConfiguration(String code) {
        return (HashMap) CONFIGURATIONS.get(code);
    }

    /**
     * idem que getField mais retourne une chaine au format jj.mm.aaaa
     * 
     * @param line
     *            DOCUMENT ME!
     * @param fields
     *            DOCUMENT ME!
     * @param fieldName
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut date
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static String getDate(String line, Map fields, String fieldName) throws PRACORException {
        try {
            return new JADate(getField(line, fields, fieldName)).toStr(".");
        } catch (JAException e) {
            throw new PRACORException("impossible de transformer la date", e);
        }
    }

    /**
     * recherche dans la map la definition du champ fieldname et l'utilise pour extraire la valeur correspondante dans
     * la ligne de texte.
     * 
     * @param line
     *            DOCUMENT ME!
     * @param fields
     *            DOCUMENT ME!
     * @param fieldName
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut field
     */
    protected static String getField(String line, Map fields, String fieldName) {
        PRTextField field = (PRTextField) fields.get(fieldName);

        return line.substring(field.getBeginPos(), field.getEndPos()).trim();
    }

    /**
     * charge toutes les configurations du fichier fileparserIJ.xml
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static void loadConfigurations() throws PRACORException {
        try {
            addConfiguration(CODE_BASE_CALCUL, PRFileParserFactory.loadConfiguration(FILE_PARSER, "BASE_CALCUL"));
            addConfiguration(CODE_PRESTATION, PRFileParserFactory.loadConfiguration(FILE_PARSER, "PRESTATION"));
            addConfiguration(CODE_DECOMPTE, PRFileParserFactory.loadConfiguration(FILE_PARSER, "DECOMPTE"));
            addConfiguration(CODE_ANNONCE_REV3, PRFileParserFactory.loadConfiguration(FILE_PARSER, "ANNONCE_REV3"));
            addConfiguration(CODE_ANNONCE_REV4_01,
                    PRFileParserFactory.loadConfiguration(FILE_PARSER, "ANNONCE_REV4_01"));
            addConfiguration(CODE_ANNONCE_REV4_02,
                    PRFileParserFactory.loadConfiguration(FILE_PARSER, "ANNONCE_REV4_02"));
        } catch (Exception e) {
            throw new PRACORException("impossible de charger le fichier de configuration pour parser", e);
        }
    }
}
