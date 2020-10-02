package ch.globaz.pegasus.businessimpl.utils.droit;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class DonneeFinanciereUtils {

    public static SimpleDonneeFinanciereHeader copySimpleDonneFianciere(
            SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        SimpleDonneeFinanciereHeader sf = new SimpleDonneeFinanciereHeader();
        sf.setCsTypeDonneeFinanciere(simpleDonneeFinanciereHeader.getCsTypeDonneeFinanciere());
        sf.setDateDebut(simpleDonneeFinanciereHeader.getDateDebut());
        sf.setDateFin(simpleDonneeFinanciereHeader.getDateFin());
        sf.setIsDessaisissementFortune(simpleDonneeFinanciereHeader.getIsDessaisissementFortune());
        sf.setTypeDessaisissementFortune(simpleDonneeFinanciereHeader.getTypeDessaisissementFortune());
        sf.setIsDessaisissementRevenu(simpleDonneeFinanciereHeader.getIsDessaisissementRevenu());
        return sf;
    }

    public static DroitMembreFamille findTheDroitMembreFamilleInTheSarchModle(
            DroitMembreFamilleSearch droitMembreFamilleSearch, String idMembreFamilleSF) {
        DroitMembreFamille droitMembreFamille = null;
        DroitMembreFamille droitMembreFamilleFinded = null;
        for (JadeAbstractModel model : droitMembreFamilleSearch.getSearchResults()) {
            droitMembreFamille = (DroitMembreFamille) model;
            if (droitMembreFamille.getSimpleDroitMembreFamille().getIdMembreFamilleSF().equals(idMembreFamilleSF)) {
                return droitMembreFamille;
            }
        }
        return droitMembreFamilleFinded;
    }
}
