package globaz.corvus.api.arc.downloader.domaine;

import globaz.pavo.db.compte.CICompteIndividuel;
import java.util.ArrayList;
import java.util.List;

public class RECILiesWrapper {

    CICompteIndividuel compteIndividuel = null;
    List<String> nssLiesTrouves = new ArrayList<String>();

    public void addNssLie(String nss) {
        nssLiesTrouves.add(nss);
    }

    public CICompteIndividuel getCompteIndividuel() {
        return compteIndividuel;
    }

    public void setCompteIndividuel(CICompteIndividuel compteIndividuel) {
        this.compteIndividuel = compteIndividuel;
    }

    public List<String> getNssLiesTrouves() {
        return new ArrayList<String>(nssLiesTrouves);
    }
}
