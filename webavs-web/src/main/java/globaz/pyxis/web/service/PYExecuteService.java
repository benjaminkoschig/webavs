package globaz.pyxis.web.service;

import globaz.pyxis.web.DTO.PYTiersDTO;

public class PYExecuteService {
    /**
     * Cr�ation de tiers
     *
     * @param dto JSON mapp� en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers cr��
     */
    public PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        //TODO: Implement "tiers" creation

        System.out.println(dto.getName());

        //TODO: Return created "tiers"'s ID (HTITIE) instead of the whole dto
        return dto;
    }
}
