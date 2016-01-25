package ch.globaz.al.businessimpl.services.rubriques.comptables;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCICIService;

/**
 * Implémentation du service spécifique permettant de récupérer une rubrique comptable pour la CICICAM
 * 
 * @author jts
 * 
 */
public class RubriquesComptablesCICIServiceImpl extends RubriquesComptablesCVCIServiceImpl implements
        RubriquesComptablesCICIService {

    @Override
    protected String getRubriqueADI(DossierModel dossier, EntetePrestationModel entete, DetailPrestationModel detail,
            String date) throws JadeApplicationException, JadePersistenceException {
        return getRubriqueSalarie(dossier, entete, detail, date);
    }
}
