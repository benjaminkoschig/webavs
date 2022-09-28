package globaz.naos.web.DTO;

import ch.globaz.jade.JadeBusinessServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.exceptions.AFInternalException;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validation des champs du DTO
 */
public class AFValidateDTO {
    public static void checkIfEmpty(Map<String,String> mapForValidator){

        List<String> listeErrors = mapForValidator
                .entrySet()
                .stream()
                .filter(e -> JadeStringUtil.isEmpty(e.getValue().trim()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!listeErrors.isEmpty()) {
            throw new AFBadRequestException("Champs manquant(s) => " + StringUtils.join(listeErrors, ","));
        }
    }

    /**
     * Vérifie que l'idCodeSystem passé en paramètre est bien dans la famille passée en paramètre.
     * Attention : si le code système est désactivé (FWCOSP.PCODFI = 1), le code système ne sera pas considéré
     * comme faisant partie de la famille et on va remonter une exception.
     *
     * @param idCodeSystem
     * @param famille
     */
    public static void verifyCodeSystem(String idCodeSystem, String famille) {
        if (!JadeStringUtil.isEmpty(idCodeSystem)) {
            try {
                //Validation avec MMO et EVID le 23.09.2022 : la méthode getFamilleCodeSystem ne va remonter que les
                // codes actifs ! Ce qui fait que si on passe un code inactif (FWCOSP.PCODFI = 1), il ne sera pas
                // retrouvé et une exception sera remontée.
                JadeBusinessServiceLocator.getCodeSystemeService()
                        .getFamilleCodeSysteme(famille).stream()
                        .filter(cs -> idCodeSystem.equals(cs.getIdCodeSysteme()))
                        .findFirst()
                        .orElseThrow(() -> new AFBadRequestException("Le code \"" + idCodeSystem + "\" ne fait pas partie de la famille \"" + famille + "\""));
            } catch (JadePersistenceException e) {
                throw new AFInternalException("Erreur lors de la vérification du code system \" " + idCodeSystem + " \" => ", e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new AFInternalException("Erreur lors de la vérification du code system \" " + idCodeSystem + " \" => ", e);
            }
        }
    }
}
