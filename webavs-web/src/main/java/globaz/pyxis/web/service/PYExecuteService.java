package globaz.pyxis.web.service;

import globaz.pyxis.web.DTO.PYTiersDTO;

public class PYExecuteService {
    /**
     * Création de tiers
     *
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé
     */
    public PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        String idTiers = null;

        //TODO: check that token is valid

        //TODO: Call addTiersPage1
        System.out.println(dto.getName());

        //TODO: return dto with tiers' id
        return dto;
    }
}
