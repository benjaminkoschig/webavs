/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import ch.globaz.amal.business.models.famille.SimpleFamille;

/**
 * @author CBU
 * 
 */
public class ContribuableFamilleRCListe extends ContribuableRCListe {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleFamille famille = null;
    private String idContribuableFamille = null;

    public ContribuableFamilleRCListe() {
        super();
        famille = new SimpleFamille();
    }

    public ContribuableFamilleRCListe(SimpleFamille famille) {
        super();
        this.famille = famille;
    }

    @Override
    public SimpleFamille getFamille() {
        return famille;
    }

    @Override
    public String getIdContribuableFamille() {
        return idContribuableFamille;
    }

    @Override
    public void setFamille(SimpleFamille famille) {
        this.famille = famille;
    }

    @Override
    public void setIdContribuableFamille(String idContribuableFamille) {
        this.idContribuableFamille = idContribuableFamille;
    }

}
