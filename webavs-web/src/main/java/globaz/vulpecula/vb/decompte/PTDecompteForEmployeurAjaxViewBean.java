package globaz.vulpecula.vb.decompte;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTDecompteForEmployeurAjaxViewBean extends JadeAbstractAjaxFindForDomain<Decompte> {
    private static final long serialVersionUID = 6945360872073577082L;

    private String idEmployeur;
    private String idDecompte;
    private String numeroDecompte;
    private String type;
    private TaxationOffice taxationOfficeModel;

    public List<Decompte> getDecomptes() {
        return getList();
    }

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaxationOffice getTaxationOfficeModel() {
        return taxationOfficeModel;
    }

    @Override
    public Decompte getEntity() {
        return new Decompte();
    }

    @Override
    public Repository<Decompte> getRepository() {
        return VulpeculaRepositoryLocator.getDecompteRepository();
    }

    @Override
    public List<Decompte> findByRepository() {
        List<Decompte> listeDecomptesRetour = new ArrayList<Decompte>();
        List<Decompte> listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository().findByIdEmployeur(
                idEmployeur, idDecompte, numeroDecompte, type);

        for (Decompte decompte : listeDecomptes) {
            if (decompte.isTaxationOffice()) {
                taxationOfficeModel = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByIdDecompte(
                        decompte.getId());
                decompte.setTaxationOfficeModel(taxationOfficeModel);

                // POBMS-132
                if (!EtatTaxation.ANNULE.equals(decompte.getEtatTaxationOffice())) {
                    listeDecomptesRetour.add(decompte);
                }
            } else {
                listeDecomptesRetour.add(decompte);
            }
        }

        return listeDecomptesRetour;
    }

}
