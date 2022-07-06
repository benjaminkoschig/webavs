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
        //TODO: Implement "tiers" creation

        System.out.println(dto.getName());

        //TODO: Return created "tiers"'s ID (HTITIE) instead of the whole dto
        return dto;
    }
}
