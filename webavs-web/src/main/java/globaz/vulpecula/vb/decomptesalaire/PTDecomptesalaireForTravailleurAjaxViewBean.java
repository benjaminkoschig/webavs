package globaz.vulpecula.vb.decomptesalaire;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTDecomptesalaireForTravailleurAjaxViewBean extends JadeAbstractAjaxFindForDomain<DecompteSalaire> {
    private static final long serialVersionUID = 2617152575851579057L;

    private String idTravailleur;
    private String idDecompte;
    private String raisonSociale;
    private String numeroDecompte;
    private String type;

    public List<DecompteSalaire> getSalaires() {
        return getList();
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public void setIdTravailleur(final String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public DecompteSalaire getEntity() {
        return new DecompteSalaire();
    }

    @Override
    public Repository<DecompteSalaire> getRepository() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository();
    }

    @Override
    public List<DecompteSalaire> findByRepository() {
    	List<DecompteSalaire> listRetour = new ArrayList<DecompteSalaire>();
    	
    	for (DecompteSalaire decompteSalaire : VulpeculaRepositoryLocator.getDecompteSalaireRepository().findLignesDecomptesSansCotisationsByIdTravailleur(
                idTravailleur, idDecompte, raisonSociale, numeroDecompte, type)) {
    	
    		Decompte decompte = decompteSalaire.getDecompte();

    		// POBMS-132
    		if (decompte != null && EtatDecompte.ANNULE.equals(decompte.getEtat())) {
    			continue;
    		}
    		
    		if (decompte != null && decompte.isTaxationOffice()) {
    			// POBMS-132
    			if(!EtatTaxation.ANNULE.equals(decompte.getEtatTaxationOffice())) {
    				listRetour.add(decompteSalaire);
                }
    		} else {
    			listRetour.add(decompteSalaire);
    		}
    	}
    	
        return listRetour;
    }
}
