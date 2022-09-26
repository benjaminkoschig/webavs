package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.web.exceptions.PYInternalException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Data
public class PYConjointDTO extends PYLienEntreTiersDTO{
    public String etatCivil;
    @JsonIgnore
    public Boolean isValidForCreationConjoint() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("EtatCivil", this.getEtatCivil());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        if(!JadeStringUtil.isBlankOrZero(this.getDebutRelation()) && !JadeDateUtil.isGlobazDate(this.getDebutRelation())){
            String msgError = BSessionUtil.getSessionFromThreadContext().getLabel("DATE_DEB_ERRONEE");
            LOG.error(msgError);
            throw new  PYInternalException(msgError);
        }
        if(!JadeStringUtil.isBlankOrZero(this.getFinRelation()) && !JadeDateUtil.isGlobazDate(this.getFinRelation())){
            String msgError = BSessionUtil.getSessionFromThreadContext().getLabel("DATE_FIN_ERRONEE");
            LOG.error(msgError);
            throw new  PYInternalException(msgError);
        }
        return true;
    }
    @JsonIgnore
    public Boolean isValidForUpdateConjoint() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("EtatCivil", this.getEtatCivil());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        if(!JadeStringUtil.isBlankOrZero(this.getDebutRelation()) && !JadeDateUtil.isGlobazDate(this.getDebutRelation())){
            throw new  PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_DEB_ERRONEE"));
        }
        if(!JadeStringUtil.isBlankOrZero(this.getFinRelation()) && !JadeDateUtil.isGlobazDate(this.getFinRelation())){
            throw new  PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_FIN_ERRONEE"));
        }
        return true;
    }
}
