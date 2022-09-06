package ch.globaz.eform.business.models;

import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.validation.ValidationError;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GFDaDossierModel extends JadeSimpleModel {
    private String id;
    private String messageId;
    private String nssAffilier;
    private String codeCaisse;
    private String type;
    private String status;
    private String userGestionnaire;
    private String yourBusinessRefId;
    private String ourBusinessRefId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public ValidationResult validating() {
        return validating(new ValidationResult());
    }

    public ValidationResult validating(ValidationResult result) {
        if (!isNew() && JadeStringUtil.isBlank(messageId)) {
            result.addError("messageId", ValidationError.EMPTY);
        }

        if (JadeStringUtil.isBlank(nssAffilier)) {
            result.addError("nssAffilier", ValidationError.MANDATORY);
        } else if (!NSSUtils.checkNSS(nssAffilier)) {
            result.addError("nssAffilier", ValidationError.MALFORMED);
        }

        if (JadeStringUtil.isBlank(codeCaisse)) {
            result.addError("codeCaisse", ValidationError.MANDATORY);
        }

        if (JadeStringUtil.isBlank(type)) {
            result.addError("type", ValidationError.MANDATORY);
        } else {
            FWParametersSystemCodeManager gfDaDossierType = new FWParametersSystemCodeManager();
            gfDaDossierType.setForActif(true);
            gfDaDossierType.setForCodeUtilisateur(GFTypeDADossier.getTypeByCodeSystem(type).getCode());
            gfDaDossierType.setForIdGroupe("GFDATYPE");
            gfDaDossierType.setSession(BSessionUtil.getSessionFromThreadContext());

            try {
                gfDaDossierType.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (gfDaDossierType.isEmpty()) {
                result.addError("type", ValidationError.ILLEGAL_VALUE);
            }
        }

        if (JadeStringUtil.isBlank(status)) {
            result.addError("status", ValidationError.MANDATORY);
        } else {
            FWParametersSystemCodeManager gfDaDossierStatus = new FWParametersSystemCodeManager();
            gfDaDossierStatus.setForActif(true);
            gfDaDossierStatus.setForCodeUtilisateur(GFStatusDADossier.getStatusByCodeSystem(status).getCode());
            gfDaDossierStatus.setForIdGroupe("GFDASTATUS");
            gfDaDossierStatus.setSession(BSessionUtil.getSessionFromThreadContext());

            try {
                gfDaDossierStatus.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(gfDaDossierStatus.isEmpty()) {
                result.addError("status", ValidationError.ILLEGAL_VALUE);
            }
        }

        if (JadeStringUtil.isBlank(ourBusinessRefId)) {
            result.addError("ourBusinessRefId", ValidationError.MANDATORY);
        }

        return result;
    }
}
