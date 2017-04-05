package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.common.Jade;
import java.io.File;
import ch.globaz.amal.business.services.sedexCO.AnnoncesCOService;

public class AnnoncesCOServiceImpl extends AnnoncesCOReceptionMessage5232_000202_1 implements AnnoncesCOService {

    @Override
    public String printListComparaisonFull(String annee) throws Exception {
        // SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch = new
        // SimplePersonneANePasPoursuivreSearch();
        // simplePersonneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
        // .search(simplePersonneANePasPoursuivreSearch);
        //
        // List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison = new
        // ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();
        // ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge = new
        // ComparaisonAnnonceCreancePriseEnCharge();
        // for (JadeAbstractModel modelAbstract : simplePersonneANePasPoursuivreSearch.getSearchResults()) {
        // SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre = (SimplePersonneANePasPoursuivre)
        // modelAbstract;
        // comparaisonAnnonceCreancePriseEnCharge = getLigneComparaison(simplePersonneANePasPoursuivre);
        // listeComparaison.add(comparaisonAnnonceCreancePriseEnCharge);
        // }
        // generationListeDifferences(listeComparaison);

        File fileAnnoncesRecues = createAndPrintList("2016", "263301");

        String file = Jade.getInstance().getPersistenceDir() + fileAnnoncesRecues.getName();

        return file;
    }

}
