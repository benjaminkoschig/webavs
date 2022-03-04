package ch.globaz.eform.business.models;

import ch.globaz.common.util.Strings;
import ch.globaz.common.validation.ValidationError;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.constant.GFStatusEForm;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = true)
public class GFEFormModel extends JadeSimpleModel {
    private final String typePattern = "^25(\\d{2})";
    private final String nssPattern = "^756\\.\\d{4}\\.\\d{4}\\.\\d{2}$";
    private final String datePattern = "^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.]((?:19|20)\\d\\d)$";

    private String id;
    private String messageId;
    private String type;
    private String subject;
    private String date;
    private String status;
    private String beneficiaireNss;
    private String beneficiaireNom;
    private String beneficiairePrenom;
    private String beneficiaireDateNaissance;
    private String userGestionnaire;
    private byte[] attachement;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public ValidationResult validating() {
        ValidationResult result = new ValidationResult();

        if (JadeStringUtil.isBlank(id)) {
            result.addError("id", ValidationError.MANDATORY);
        }

        if (JadeStringUtil.isBlank(type)) {
            result.addError("type", ValidationError.MANDATORY);
        } else if (!Strings.match(type, typePattern)) {
            result.addError("type", ValidationError.MALFORMED);
        }

        if (JadeStringUtil.isBlank(date)) {
            result.addError("date", ValidationError.MANDATORY);
        } else if(!Strings.match(date, datePattern)) {
            result.addError("date", ValidationError.MALFORMED);
        }

        if (JadeStringUtil.isBlank(status)) {
            result.addError("status", ValidationError.MANDATORY);
        } else if (Arrays.stream(GFStatusEForm.values()).map(GFStatusEForm::toString).noneMatch(e -> e.equals(status))) {
            result.addError("status", ValidationError.ILLEGAL_VALUE);
        }

        if (JadeStringUtil.isBlank(beneficiaireNss)) {
            result.addError("beneficiaireNss", ValidationError.MANDATORY);
        } else if (!Strings.match(beneficiaireNss, nssPattern)) {
            result.addError("beneficiaireNss", ValidationError.MALFORMED);
        }

        if (JadeStringUtil.isBlank(beneficiaireDateNaissance)) {
            result.addError("beneficiaireDateNaissance", ValidationError.MANDATORY);
        } else if (!Strings.match(beneficiaireDateNaissance, datePattern)) {
            result.addError("beneficiaireDateNaissance", ValidationError.MALFORMED);
        }

        if (JadeStringUtil.isBlank(userGestionnaire)) {
            result.addError("userGestionnaire", ValidationError.MANDATORY);
        }

        return result;
    }
}
