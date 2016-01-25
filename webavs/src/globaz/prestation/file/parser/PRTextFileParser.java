package globaz.prestation.file.parser;

import globaz.prestation.file.parser.exception.PRFieldNotFoundException;
import globaz.prestation.file.parser.exception.PRLineNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 2 mars 04
 * 
 * @author scr
 * 
 */
public class PRTextFileParser extends APRTextFileParser {

    public static void main(String[] args) {

        // programe de test
        // System.out.println("start");
        //
        // IPRFileParser fileParser = null;
        // try {
        // fileParser = PRFileParserFactory.newInstance("paiementEtranger.xml",
        // "MADRID");
        // fileParser.setSource("D:\\test\\Cotisation_Espagne.txt");
        // while (fileParser.hasNext()) {
        // fileParser.goToNextRecord();
        // System.out.print("record : ");
        // System.out.print(" date = " + fileParser.getField("DATE"));
        // System.out.print(" noAvs = " + fileParser.getField("NO_AVS"));
        // System.out.println(" montant = " + fileParser.getField("MONTANT"));
        // }
        // } catch (PRLabelNameException e) {
        // e.printStackTrace();
        // System.out.println("LABEL = " + e.getLabel());
        // System.out.println("msg = " + e.getExtraMessage());
        // } catch (Exception e) {
        // e.printStackTrace();
        // fileParser.close();
        // }
        //
        //
        // System.out.println("fin");
        // System.exit(1);

    }

    private Map fields = new HashMap();

    private String line = null;

    /**
     * @see globaz.osiris.file.paiement.APRTextFileParser#addField(PRTextField)
     */
    @Override
    void addField(PRTextField field) {
        fields.put(field.getName(), field);
    }

    /**
     * @see globaz.osiris.file.paiement.APRTextFileParser#getDumpFileRecord()
     */
    @Override
    public String getDumpFileRecord() {
        return line;
    }

    @Override
    public String getField(String fieldName) throws PRLineNotFoundException, PRFieldNotFoundException {
        PRTextField field = (PRTextField) fields.get(fieldName);

        if (line == null) {
            throw new PRLineNotFoundException();
        }

        if (field == null) {
            throw new PRFieldNotFoundException(fieldName);
        }

        String dummy = line;
        if (field.getEndPos() > line.length() || field.getBeginPos() >= line.length()) {
            return null;
        } else {
            return dummy.substring(field.getBeginPos(), field.getEndPos());
        }
    }

    @Override
    public void goToNextRecord() throws Exception {
        line = next();
    }

}
