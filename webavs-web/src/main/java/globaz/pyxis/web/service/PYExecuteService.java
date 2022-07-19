package globaz.pyxis.web.service;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.web.DTO.PYTiersDTO;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PYExecuteService extends BProcess {
    /**
     * Cr�ation de tiers
     *
     * @param dto JSON mapp� en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers cr��
     */
    public PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        //TODO: check that token is valid

        try {
            String idTiers = PRTiersHelper.addTiersPage1(getSession(), dto);
            dto.setId(idTiers);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de param�tre est survenue lors de la cr�ation du tiers: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la cr�ation du tiers: " + e);
            throw e;
        }
        catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la cr�ation du tiers: " + e);
            throw new PYInternalException(e);
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
