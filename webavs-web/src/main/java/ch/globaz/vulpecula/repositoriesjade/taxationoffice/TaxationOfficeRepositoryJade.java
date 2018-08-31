package ch.globaz.vulpecula.repositoriesjade.taxationoffice;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSearchComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.taxationoffice.converters.TaxationOfficeConverter;

public class TaxationOfficeRepositoryJade extends
        RepositoryJade<TaxationOffice, TaxationOfficeComplexModel, TaxationOfficeSimpleModel> implements
        TaxationOfficeRepository {

    @Override
    public TaxationOffice findById(String id) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<TaxationOffice> findByIdIn(Collection<String> ids) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIds(ids);
        searchModel.setOrderKey(TaxationOfficeSearchComplexModel.NO_AFFILIE_ASC);
        return searchAndFetch(searchModel);
    }

    @Override
    public TaxationOffice findByIdDecompte(String idDecompte) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public DomaineConverterJade<TaxationOffice, TaxationOfficeComplexModel, TaxationOfficeSimpleModel> getConverter() {
        return new TaxationOfficeConverter();
    }

    @Override
    public List<TaxationOffice> getTaxationForFacturation(String idPassage) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdPassageFacturation(idPassage);
        // BMS-2110 l'état "saisi" ne doit pas être pris en compte par la facturation.
        searchModel.setForEtatIn(EtatTaxation.VALIDE, EtatTaxation.FACTURATION);
        List<TaxationOffice> taxationOffices = searchAndFetch(searchModel);
        loadDependencies(taxationOffices);
        return taxationOffices;
    }

    @Override
    public List<TaxationOffice> findBy(String idPassage, String numeroAffilie, EtatTaxation etat) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdPassageFacturation(idPassage);
        searchModel.setLikeNoAffilie("%" + numeroAffilie);
        if (etat != null) {
            searchModel.setForEtatIn(etat);
        }
        return searchAndFetch(searchModel);
    }

    @Override
    public List<TaxationOffice> findByIdEmployeur(String idEmployeur) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForEtatNotIn(EtatTaxation.ANNULE);
        return searchAndFetch(searchModel);
    }

    @Override
    public int findNbTOActives(Employeur employeur, Date dateDebut) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForIdEmployeur(employeur.getId());
        searchModel.setForEtatNotIn(EtatTaxation.ANNULE);
        // BMS-2404 Décompte: Impossibilité de saisir un décompte s'il y a une TO active sur une CP
        searchModel.setForTypesNotIn(TypeDecompte.COMPLEMENTAIRE);
        searchModel.setForDateContenueDansPeriode(dateDebut);
        return count(searchModel);
    }

    private void loadDependencies(List<TaxationOffice> tos) {
        for (TaxationOffice taxationOffice : tos) {
            taxationOffice.setLignes(VulpeculaRepositoryLocator.getLigneTaxationRepository().findByIdTaxationOffice(
                    taxationOffice.getId()));
        }
    }

    @Override
    public List<TaxationOffice> findByEtat(EtatTaxation etat) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForEtatIn(etat);
        List<TaxationOffice> tos = searchAndFetch(searchModel);
        loadDependencies(tos);
        return tos;
    }

    @Override
    public List<TaxationOffice> findByEtatIn(EtatTaxation... etats) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForEtatIn(etats);
        List<TaxationOffice> tos = searchAndFetch(searchModel);
        loadDependencies(tos);
        return tos;
    }

    @Override
    public List<TaxationOffice> findAnnuleForDate(Date date) {
        TaxationOfficeSearchComplexModel searchModel = new TaxationOfficeSearchComplexModel();
        searchModel.setForDateAnnulation(date);
        searchModel.setForEtatIn(EtatTaxation.ANNULE);
        List<TaxationOffice> tos = searchAndFetch(searchModel);
        List<TaxationOffice> tosComptabilisee = new ArrayList<TaxationOffice>();
        for (TaxationOffice taxationOffice : tos) {
            if (!JadeStringUtil.isEmpty(taxationOffice.getIdSection())) {
                tosComptabilisee.add(taxationOffice);
            }
        }
        loadDependencies(tosComptabilisee);
        return tosComptabilisee;

    }
}
