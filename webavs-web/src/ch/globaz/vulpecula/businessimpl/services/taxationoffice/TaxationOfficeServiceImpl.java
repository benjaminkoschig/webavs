package ch.globaz.vulpecula.businessimpl.services.taxationoffice;

import globaz.globall.db.BProcessLauncher;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.businessimpl.services.decompte.AnnulerDecompteComptabilise;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.documents.rappels.DocumentTaxationOfficePrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.LigneTaxationRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.services.musca.PassageService;

public class TaxationOfficeServiceImpl implements TaxationOfficeService {
    private TaxationOfficeRepository taxationOfficeRepository;
    private LigneTaxationRepository ligneTaxationRepository;
    private PassageService passageService;

    public TaxationOfficeServiceImpl() {
        taxationOfficeRepository = VulpeculaRepositoryLocator.getTaxationOfficeRepository();
        ligneTaxationRepository = VulpeculaRepositoryLocator.getLigneTaxationRepository();
        passageService = VulpeculaServiceLocator.getPassageService();
    }

    public TaxationOfficeServiceImpl(TaxationOfficeRepository taxationOfficeRepository,
            LigneTaxationRepository ligneTaxationRepository, PassageService passageService) {
        this.taxationOfficeRepository = taxationOfficeRepository;
        this.ligneTaxationRepository = ligneTaxationRepository;
        this.passageService = passageService;
    }

    @Override
    public void update(TaxationOffice taxationOffice) {
        // si le passage de facturation est renseigné et que l'état est saisi, il faut passer l'état à valider
        taxationOfficeRepository.update(taxationOffice);
        for (LigneTaxation ligneTaxation : taxationOffice.getLignes()) {
            ligneTaxationRepository.update(ligneTaxation);
        }
    }

    @Override
    public void devalider(String idTaxationOffice) {
        TaxationOffice to = taxationOfficeRepository.findById(idTaxationOffice);
        to.setEtat(EtatTaxation.SAISI);
        taxationOfficeRepository.update(to);
    }

    @Override
    public void imprimer(String idTaxationOffice) throws Exception {
        TaxationOffice to = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findById(idTaxationOffice);
        to.setLignes(VulpeculaRepositoryLocator.getLigneTaxationRepository().findByIdTaxationOffice(idTaxationOffice));
        to.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                to.getIdTiers()));
        List<TaxationOffice> liste = new ArrayList<TaxationOffice>();
        liste.add(to);
        DocumentTaxationOfficePrinter docs = new DocumentTaxationOfficePrinter(DocumentPrinter.getIds(liste));
        BProcessLauncher.start(docs);
    }

    @Override
    public void genererTaxationsOffice(List<Decompte> decomptes) {
        Passage passage = passageService.findOrCreatePassageTO();

        for (Decompte decompte : decomptes) {
            TaxationOffice taxationOffice = new TaxationOffice();
            taxationOffice.setDecompte(decompte);
            taxationOffice.setEtat(EtatTaxation.VALIDE);
            taxationOffice.setIdPassageFacturation(passage.getId());
            VulpeculaRepositoryLocator.getTaxationOfficeRepository().create(taxationOffice);
            addTaxationOfficeToHistorique(decompte);
            suppressionDateDeRappelAndSetEtatToTO(decompte);
            genererTaxationOffice(taxationOffice);
        }
    }

    /**
     * Ajoute un historique "Taxation d'Office" à l'historique du décompte.
     */
    private void addTaxationOfficeToHistorique(Decompte decompte) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(Date.now());
        historiqueDecompte.setEtat(EtatDecompte.TAXATION_DOFFICE);
        historiqueDecompte.setDecompte(decompte);
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historiqueDecompte);
    }

    private void suppressionDateDeRappelAndSetEtatToTO(Decompte decompte) {
        decompte.setDateRappel(null);
        decompte.setEtat(EtatDecompte.TAXATION_DOFFICE);
        VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);
    }

    private void genererTaxationOffice(TaxationOffice taxationOffice) {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(
                taxationOffice.getDecompte().getId());
        Decompte decomptePrecedent = VulpeculaServiceLocator.getDecompteService().findDecomptePrecedent(decompte);

        // Cas de reprise
        if (decomptePrecedent != null) {
            if (decomptePrecedent.getIdPassageFacturation().equals("0")) {
                genererLignesTaxationReprise(taxationOffice, decompte, decomptePrecedent);
            } else {
                genererLignesTaxation(taxationOffice, decomptePrecedent);
            }
        } else {
            genererLignesTaxationNouveauDecompte(taxationOffice, decompte);
        }

    }

    private void genererLignesTaxation(TaxationOffice taxationOffice, Decompte decompte) {
        List<CotisationCalculee> cotisationCalculees = decompte.getTableCotisationsCalculees();

        for (CotisationCalculee cotisationCalculee : cotisationCalculees) {
            // On majore de 20%
            Montant montant = cotisationCalculee.getMontant();
            Montant pourcentage = montant.multiply(0.2);
            Montant masse = montant.add(pourcentage);
            Taux taux = cotisationCalculee.getTaux();
            Montant montantCoti = masse.multiply(taux);

            LigneTaxation ligne = new LigneTaxation();
            ligne.setTaxationOffice(taxationOffice);
            ligne.setMasse(new Montant(masse.getValueNormalisee()));
            ligne.setTaux(cotisationCalculee.getTaux());
            ligne.setMontant(new Montant(montantCoti.getValueNormalisee()));
            ligne.setCotisation(cotisationCalculee.getCotisation());
            VulpeculaRepositoryLocator.getLigneTaxationRepository().create(ligne);
        }
    }

    private void genererLignesTaxationNouveauDecompte(TaxationOffice taxationOffice, Decompte decompte) {
        List<CotisationCalculee> cotisationCalculees = decompte.getTableCotisationsCalculees();

        for (CotisationCalculee cotisationCalculee : cotisationCalculees) {

            Montant montant = new Montant(VulpeculaServiceLocator.getPropertiesService().getMontantBaseTOSansDecompte());

            Montant masse = montant;
            Taux taux = cotisationCalculee.getTaux();
            Montant montantCoti = masse.multiply(taux);

            LigneTaxation ligne = new LigneTaxation();
            ligne.setTaxationOffice(taxationOffice);
            ligne.setMasse(new Montant(masse.getValueNormalisee()));
            ligne.setTaux(cotisationCalculee.getTaux());
            ligne.setMontant(new Montant(montantCoti.getValueNormalisee()));
            ligne.setCotisation(cotisationCalculee.getCotisation());
            VulpeculaRepositoryLocator.getLigneTaxationRepository().create(ligne);
        }
    }

    private void genererLignesTaxationReprise(TaxationOffice taxationOffice, Decompte decompte,
            Decompte decomptePrecedent) {
        List<CotisationCalculee> cotisationCalculees = decompte.getTableCotisationsCalculees();
        Montant montant = calculMontantReprise(decomptePrecedent);
        for (CotisationCalculee cotisationCalculee : cotisationCalculees) {
            // On majore de 20%
            Montant pourcentage = montant.multiply(0.2);
            Montant masse = montant.add(pourcentage);
            Taux taux = cotisationCalculee.getTaux();
            Montant montantCoti = masse.multiply(taux);

            LigneTaxation ligne = new LigneTaxation();
            ligne.setTaxationOffice(taxationOffice);
            ligne.setMasse(new Montant(masse.getValueNormalisee()));
            ligne.setTaux(cotisationCalculee.getTaux());
            ligne.setMontant(new Montant(montantCoti.getValueNormalisee()));
            ligne.setCotisation(cotisationCalculee.getCotisation());
            VulpeculaRepositoryLocator.getLigneTaxationRepository().create(ligne);
        }
    }

    private Montant calculMontantReprise(Decompte decomptePrecedent) {
        List<DecompteSalaire> decompteSalaires = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdDecompte(decomptePrecedent.getId());
        Montant montantTotal = new Montant(0);
        for (DecompteSalaire decompteSalaire : decompteSalaires) {
            montantTotal = montantTotal.add(decompteSalaire.getSalaireTotal());
        }
        return montantTotal;
    }

    @Override
    public void annuler(String idTaxation) {
        TaxationOffice taxationOffice = taxationOfficeRepository.findById(idTaxation);
        if (taxationOffice.isComptabilisee()) {
            AnnulerDecompteComptabilise annulerTaxationOfficeComptabilise = new AnnulerDecompteComptabilise(
                    taxationOffice);
            annulerTaxationOfficeComptabilise.annulerSection("Annulation T.O " + taxationOffice.getIdDecompte());
        }
        taxationOffice.setEtat(EtatTaxation.ANNULE);
        taxationOfficeRepository.update(taxationOffice);
    }

    @Override
    public boolean hasTO(Employeur employeur, Date dateDebut) {
        return taxationOfficeRepository.findNbTOActives(employeur, dateDebut) > 0;
    }
}
