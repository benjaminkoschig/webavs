package globaz.osiris.file.paiement;

import globaz.osiris.file.paiement.exception.FieldNotFoundException;
import globaz.osiris.file.paiement.exception.LineNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe : type_conteneur Description : Date de création: 2 mars 04
 * 
 * @author scr
 */
public class CASondrioTextFileParser extends ACATextFileParser {

    private Map fields = new HashMap();
    private StringBuffer line = null;

    // package visibility, only the factory is suppose to use it !!!.
    @Override
    void addField(TextField field) {
        fields.put(field.getName(), field);
    }

    /**
     * @see globaz.osiris.file.paiement.ACATextFileParser#getDumpFileRecord()
     */
    @Override
    public String getDumpFileRecord() {
        return line.toString();
    }

    @Override
    public String getField(String fieldName) throws LineNotFoundException, FieldNotFoundException {
        TextField field = (TextField) fields.get(fieldName);

        if (line == null) {
            throw new LineNotFoundException();
        }

        if (field == null) {
            throw new FieldNotFoundException();
        }

        String dummy = line.toString();
        if ((field.getEndPos() > line.length()) || (field.getBeginPos() >= line.length())) {
            return null;
        } else {
            return dummy.substring(field.getBeginPos(), field.getEndPos());
        }
    }

    @Override
    public void goToNextRecord() throws Exception {
        line = new StringBuffer();
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        line.append(next());
        System.out.println("line read : " + line.toString());
    }

}
