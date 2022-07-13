package globaz.pyxis.web.service;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.web.DTO.PYTiersDTO;

public class PYExecuteService extends BProcess {
    /**
     * Création de tiers
     *
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé
     */
    public PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        //TODO: check that token is valid

        String idTiers;
        try {
            System.out.println(dto.getName());
            idTiers = PRTiersHelper.addTiersPage1(getSession(), dto);
            dto.setId(idTiers);
        }
        catch (Exception e) {
            System.err.println(e);
        }

        return dto;
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return false;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }
}
