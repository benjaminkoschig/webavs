package globaz.corvus.acor.parser.rev09;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.file.parser.PRFileParserFactory;
import globaz.prestation.file.parser.PRTextField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public abstract class REACORAbstractFlatFileParser {

    protected static final String CODE_41_01 = "4101";
    protected static final String CODE_41_02 = "4102";
    protected static final String CODE_44_01 = "4401";
    protected static final String CODE_44_02 = "4402";
    protected static final String CODE_AJOURNEMENT = "$a";
    protected static final String CODE_ALLOC_RENCH_1991 = "$z";
    protected static final String CODE_BASE_CALCUL = "$b";
    protected static final String CODE_BASE_FIN_CALCUL = "$s";
    protected static final String CODE_COTISATION = "$c";
    protected static final String CODE_RENTE_INCHANGEE = "$i";
    protected static final String CODE_MONTANT_MENS_RENTE_AJOURNEES = "$q";
    protected static final String CODE_PRESTATION_DUE_MENSUEL = "$p";
    protected static final String CODE_PRESTATION_DUE_RETROACTIF = "$t";
    protected static final String CODE_RENTE_ACCORDEE = "$r";

    private static Map<String, Map<String, PRTextField>> CONFIGURATIONS;
    private static final String FILE_PARSER = "fileParserRE.xml";

    private static void addConfiguration(String code, List<PRTextField> configuration) {
        Map<String, PRTextField> map = new HashMap<String, PRTextField>();

        for (PRTextField field : configuration) {
            map.put(field.getName(), field);
        }

        if (REACORAbstractFlatFileParser.CONFIGURATIONS == null) {
            REACORAbstractFlatFileParser.CONFIGURATIONS = new HashMap<String, Map<String, PRTextField>>();
        }

        REACORAbstractFlatFileParser.CONFIGURATIONS.put(code, map);
    }

    /**
     * retourne une configuration pour le code.
     * 
     * @param code
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut configuration
     */
    protected static Map<String, PRTextField> getConfiguration(String code) {
        return REACORAbstractFlatFileParser.CONFIGURATIONS.get(code);
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
    protected static String getDate(String line, Map<String, PRTextField> fields, String fieldName)
            throws PRACORException {
        try {
            return new JADate(REACORAbstractFlatFileParser.getField(line, fields, fieldName)).toStr(".");
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
    protected static String getField(String line, Map<String, PRTextField> fields, String fieldName) {
        PRTextField field = fields.get(fieldName);

        return line.substring(field.getBeginPos(), field.getEndPos());
    }

    /**
     * charge toutes les configurations du fichier fileparserIJ.xml
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static void loadConfigurations_NAVS() throws PRACORException {
        try {
            REACORAbstractFlatFileParser
                    .addConfiguration(REACORAbstractFlatFileParser.CODE_BASE_CALCUL, PRFileParserFactory
                            .loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "BASE_CALCUL_NAVS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE,
                    PRFileParserFactory.loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER,
                            "RENTE_ACCORDEE_NAVS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL,
                    PRFileParserFactory.loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER,
                            "MONTANT_VERSE_NAVS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF,
                    PRFileParserFactory.loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER,
                            "MONTANT_VERSE_NAVS"));

        } catch (Exception e) {
            throw new PRACORException("impossible de charger le fichier de configuration pour parser", e);
        }
    }

    /**
     * charge toutes les configurations du fichier fileparserIJ.xml
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static void loadConfigurations_NSS() throws PRACORException {
        try {
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_BASE_CALCUL,
                    PRFileParserFactory.loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "BASE_CALCUL_NSS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_RENTE_ACCORDEE,
                    PRFileParserFactory.loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER,
                            "RENTE_ACCORDEE_NSS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_MENSUEL,
                    PRFileParserFactory
                            .loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "MONTANT_VERSE_NSS"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_PRESTATION_DUE_RETROACTIF,
                    PRFileParserFactory
                            .loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "MONTANT_VERSE_NSS"));

        } catch (Exception e) {
            throw new PRACORException("impossible de charger le fichier de configuration pour parser", e);
        }
    }

    /**
     * charge toutes les configurations du fichier fileparserRE.xml
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static void loadConfigurationsAnnonceRR() throws PRACORException {
        try {
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_41_01, PRFileParserFactory
                    .loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "ANNONCE_41_42_43_01"));
            REACORAbstractFlatFileParser
                    .addConfiguration(REACORAbstractFlatFileParser.CODE_41_02, PRFileParserFactory.loadConfiguration(
                            REACORAbstractFlatFileParser.FILE_PARSER, "ANNONCE_41_43_02"));
            REACORAbstractFlatFileParser.addConfiguration(REACORAbstractFlatFileParser.CODE_44_01, PRFileParserFactory
                    .loadConfiguration(REACORAbstractFlatFileParser.FILE_PARSER, "ANNONCE_44_45_46_01"));
            REACORAbstractFlatFileParser
                    .addConfiguration(REACORAbstractFlatFileParser.CODE_44_02, PRFileParserFactory.loadConfiguration(
                            REACORAbstractFlatFileParser.FILE_PARSER, "ANNONCE_44_46_02"));

        } catch (Exception e) {
            throw new PRACORException("impossible de charger le fichier de configuration pour parser", e);
        }
    }
}
