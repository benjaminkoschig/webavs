package globaz.pyxis.web.service;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.web.DTO.PYTiersDTO;
import globaz.pyxis.web.DTO.PYTiersUpdateDTO;
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
        String idAddress = null;
        try {
            PRTiersHelper.addTiersPage1(getSession(), dto);

            // Only add a mail address if the DTO contains an address
            if (dto.getAddresses().size() != 0) {
                idAddress = PRTiersHelper.addTiersAddress(getSession(), dto);
            }
            // TODO: This is kinda wrong, we probably shouldn't be relying on mail address creation for payment address creation. Better check for payment info's fields.
            //  NO, the link between payment address and domicile/courrier address is mandatory. Define which address should be linked.
            //  So, check for address AND payment infos
            if (idAddress != null) {
                PRTiersHelper.addTiersPaymentAddress(getSession(), idAddress, null, true, dto);
            }
            PRTiersHelper.addTiersPage2(getSession(), dto);
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

    /**
     * Modification de tiers
     *
     * @param dto JSON mapp� en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers cr�� et la date de mise � jour
     */
    public PYTiersDTO updateTiers(PYTiersUpdateDTO dto, String token) {
        // TODO: upgrade the updating. If at least one field is present then update. Faire un vecteur de champs pour les update comme pour la cr�ation et isValid.
        try {
            PRTiersHelper.updateTiersPage1(getSession(), dto);
            if (!dto.getPaymentAddress().isEmpty())
                PRTiersHelper.updateTiersPaymentAddress(getSession(), dto);
            if (!dto.getContacts().isEmpty())
                PRTiersHelper.updateTiersPage2(getSession(), dto);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de param�tre est survenue lors de la modification du tiers: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du tiers: " + e);
            throw e;
        }
        catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du tiers: " + e);
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
