package globaz.pegasus.vb.avance;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class PCListeAvancesViewBean extends BJadePersistentObjectViewBean {

    String idDemande = null;
    String idTiers = null;
    String idTiersConjoint = null;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
        System.out.println();
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
        System.out.println();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        System.out.println();
        return null;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        System.out.println();
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        System.out.println();
    }

    /**
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     */
    public String returnIdTiersForInDemande() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        // on recherche le conjoint pour avoir son id
        searchConjoint();

        if (idTiersConjoint == null) {
            return idTiers;
        } else {
            return idTiers + "," + idTiersConjoint;
        }

    }

    /**
     * Recherche si un conjoitn existe pour cette demande
     * Si il n'y pas de droit, il n'existe pas
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     */
    private void searchConjoint() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // chargement du droit
        SimpleDroitSearch droitSearch = new SimpleDroitSearch();
        droitSearch.setForIdDemandePC(idDemande);
        droitSearch = PegasusImplServiceLocator.getSimpleDroitService().search(droitSearch);

        // Si on a des résultats
        if (droitSearch.getSearchResults().length > 0) {

            // on recupère le 1er id droit
            String idDroit = ((SimpleDroit) droitSearch.getSearchResults()[0]).getIdDroit();

            // on recherche si conjoint il y a
            ArrayList<String> csConjoint = new ArrayList<String>();
            csConjoint.add(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);

            // load personnes
            DroitMembreFamilleSearch membreSearch = new DroitMembreFamilleSearch();
            membreSearch.setForCsRoletMembreFamilleIn(csConjoint);
            membreSearch.setForIdDroit(idDroit);
            membreSearch = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(membreSearch);

            for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext();) {
                // on a bien un conjoint
                DroitMembreFamille membre = (DroitMembreFamille) it.next();
                idTiersConjoint = membre.getMembreFamille().getPersonneEtendue().getTiers().getIdTiers();
            }
        }

    }

    @Override
    public void setId(String arg0) {
        // TODO Auto-generated method stub
        System.out.println();
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
        System.out.println();
    }

}
