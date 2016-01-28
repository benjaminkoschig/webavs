package globaz.apg.itext;

import globaz.apg.db.annonces.APAbstractListeRecapitulationAnnoncesManager;
import globaz.apg.db.annonces.APRecapitulationAnnonceManagerHermes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;

/**
 * Agrégateur de donnée pour les anciennes annonces (avant septembre 2012)
 * 
 * @author VRE
 * @author LGA
 * @author PBA
 */
public class APRecapitulationAnnonceAdapterHermes extends APAbstractRecapitulationAnnonceAdapter {

    public APRecapitulationAnnonceAdapterHermes(BSession session, String forMoisAnneeComptable) {
        super(session, forMoisAnneeComptable);
    }

    @Override
    protected String getCodeUtilisateurPourCodeSysteme(String codeSysteme) throws Exception {
        try {
            BSessionUtil.initContext(getSession(), this);
            JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
            JadeCodeSysteme codeSystemeWrapper = codeSystemeService.getCodeSysteme(codeSysteme);
            return codeSystemeWrapper.getCodeUtilisateur(Langues.getLangueDepuisCodeIso(getSession().getIdLangueISO()));
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
    }

    @Override
    protected APAbstractListeRecapitulationAnnoncesManager getManager() {
        return new APRecapitulationAnnonceManagerHermes();
    }
}
