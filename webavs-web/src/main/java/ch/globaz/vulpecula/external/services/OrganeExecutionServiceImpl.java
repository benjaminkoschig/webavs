package ch.globaz.vulpecula.external.services;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.external.models.osiris.OrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecution;

/**
 * @since WebBMS 2.3
 */
public class OrganeExecutionServiceImpl implements OrganeExecutionService {

    @Override
    public OrganeExecution findById(String id) {
        OrganeExecution organeExecution = new OrganeExecution();
        CAOrganeExecution organe = new CAOrganeExecution();
        organe.setId(id);
        try {
            organe.retrieve();
            if (!organe.isNew()) {
                organeExecution.setId(organe.getId());
                organeExecution.setNom(organe.getNom());
                organeExecution.setIdAdressePaiement(organe.getIdAdressePaiement());
                organeExecution.setNoAdherentBVR(organe.getNoAdherentBVR());
                organeExecution.setNoAdherent(organe.getNoAdherent());
                // TODO ...
            }

            return organeExecution;
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

}
