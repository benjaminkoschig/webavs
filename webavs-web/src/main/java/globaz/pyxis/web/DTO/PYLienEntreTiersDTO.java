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
public class PYLienEntreTiersDTO {
    public String idComposition;
    public String idTiersPrincipal;
    public String idTiersSecondaire;
    public String typeLien;
    public String debutRelation;
    public String finRelation;
    @JsonIgnore


    public Boolean isValidForCreationLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("TypeLien", this.getTypeLien());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        if(!JadeDateUtil.isGlobazDate(this.getDebutRelation())){
            throw new PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_DEB_ERRONEE"));
        }
        if(!JadeDateUtil.isGlobazDate(this.getFinRelation())){
            throw new  PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_FIN_ERRONEE"));
        }
        return true;
    }
    @JsonIgnore
    public Boolean isValidForUpdateLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("IdComposition", this.getIdComposition());
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("TypeLien", this.getTypeLien());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        if( !JadeDateUtil.isGlobazDate(this.getDebutRelation())){
            throw new  PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_DEB_ERRONEE"));
        }
        if( !JadeDateUtil.isGlobazDate(this.getFinRelation())){
            throw new  PYInternalException(BSessionUtil.getSessionFromThreadContext().getLabel("DATE_FIN_ERRONEE"));
        }
        return true;
    }
    @JsonIgnore
    public Boolean isValidForDeletionLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idComposition", this.getIdComposition());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }



}
