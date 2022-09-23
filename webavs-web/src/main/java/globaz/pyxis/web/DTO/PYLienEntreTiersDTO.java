package globaz.pyxis.web.DTO;

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
    public String etatCivil;
    public String debutRelation;
    public String finRelation;


    public Boolean isValidForCreationLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("TypeLien", this.getTypeLien());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }

    public Boolean isValidForUpdateLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("IdComposition", this.getIdComposition());
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("TypeLien", this.getTypeLien());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }

    public Boolean isValidForDeletionLienEntreTiers() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idComposition", this.getIdComposition());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }

    public Boolean isValidForCreationConjoint() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("EtatCivil", this.getEtatCivil());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }

    public Boolean isValidForUpdateConjoint() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiersPrincipal", this.getIdTiersPrincipal());
        mapForValidator.put("IdTiersSecondaire", this.getIdTiersSecondaire());
        mapForValidator.put("EtatCivil", this.getEtatCivil());
        PYValidateDTO.checkIfEmpty(mapForValidator);
        return true;
    }

}
