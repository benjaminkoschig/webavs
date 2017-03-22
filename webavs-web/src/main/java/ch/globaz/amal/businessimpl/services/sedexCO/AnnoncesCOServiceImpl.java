package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.common.Jade;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;
import ch.globaz.amal.business.services.sedexCO.AnnoncesCOService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.ComparaisonAnnonceCreancePriseEnCharge;

public class AnnoncesCOServiceImpl extends AnnoncesCOReceptionMessage5232_000202_1 implements AnnoncesCOService {

    @Override
    public String printListComparaisonFull(String annee) throws Exception {
        SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
        simplePersonneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
                .search(simplePersonneANePasPoursuivreSearch);

        List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison = new ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();
        ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge = new ComparaisonAnnonceCreancePriseEnCharge();
        for (JadeAbstractModel modelAbstract : simplePersonneANePasPoursuivreSearch.getSearchResults()) {
            SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre = (SimplePersonneANePasPoursuivre) modelAbstract;
            comparaisonAnnonceCreancePriseEnCharge = getLigneComparaison(simplePersonneANePasPoursuivre);
            listeComparaison.add(comparaisonAnnonceCreancePriseEnCharge);
        }
        generationListeDifferences(listeComparaison);

        String file = Jade.getInstance().getPersistenceDir() + fileAnnoncesRecues.getName();

        return file;
    }

}
