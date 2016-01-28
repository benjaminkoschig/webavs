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
public class CATextFileParser extends ACATextFileParser {

    private Map fields = new HashMap();
    private String line = null;

    /**
     * @see globaz.osiris.file.paiement.ACATextFileParser#addField(TextField)
     */
    @Override
    void addField(TextField field) {
        fields.put(field.getName(), field);
    }

    /**
     * @see globaz.osiris.file.paiement.ACATextFileParser#getDumpFileRecord()
     */
    @Override
    public String getDumpFileRecord() {
        return line;
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

        String dummy = line;
        if ((field.getEndPos() > line.length()) || (field.getBeginPos() >= line.length())) {
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
