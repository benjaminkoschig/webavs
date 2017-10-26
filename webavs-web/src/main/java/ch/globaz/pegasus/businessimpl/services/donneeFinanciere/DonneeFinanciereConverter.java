package ch.globaz.pegasus.businessimpl.services.donneeFinanciere;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereComplexModel;
import ch.globaz.pegasus.businessimpl.services.converter.DomaineConverterComplexJade;

class DonneeFinanciereConverter implements DomaineConverterComplexJade<DonneeFinanciere, DonneeFinanciereComplexModel> {

    @Override
    public DonneeFinanciere convertToDomain(DonneeFinanciereComplexModel model) {

        RoleMembreFamille role = RoleMembreFamille.fromValue(model.getCsRoleFamille());
        Date debut;
        debut = toDate(model.getDateDebutDonneeFinanciere());
        Date fin = toDate(model.getDateFinDonneeFinanciere());
        return new DonneeFinanciereHeader(role, debut, fin, model.getIdDonneeFinanciereHeader(),
                model.getIdDroitMembreFamille());
    }

    static Date toDate(String date) {
        Date ddate = null;
        if (date != null) {
            if (date.trim().length() > 0) {
                ddate = new Date(date.trim());
            }
        }
        return ddate;
    }
}
