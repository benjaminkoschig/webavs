package globaz.pyxis.web.DTO;

import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

import java.util.Arrays;
import java.util.List;

public class PYValidateDTO {

    // TODO: Implement "validators" for other fields in PYTiersDTO
    public static final List<String> validLanguage = Arrays.asList(CodeLangue.FR.getCodeIsoLangue(), CodeLangue.DE.getCodeIsoLangue(), CodeLangue.IT.getCodeIsoLangue(), CodeLangue.RM.getCodeIsoLangue(), CodeLangue.EN.getCodeIsoLangue());

    public static Boolean isValid(String language){
        // TODO: Implement validation on PYTiersDTO's fields
        return PYValidateDTO.validLanguage.contains(language.toLowerCase());
    }
}
