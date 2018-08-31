package ch.globaz.vulpecula.businessimpl.services.taxationoffice;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.businessimpl.services.decompte.AnnulerDecompteComptabilise;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.documents.rappels.DocumentTaxationOfficePrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.LigneTaxationRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.external.services.musca.PassageService;
import globaz.globall.db.BProcessLauncher;

public class TaxationOfficeServiceImpl implements TaxationOfficeService {
    private final TaxationOfficeRepository taxationOfficeRepository;
    private final LigneTaxationRepository ligneTaxationRepository;
    private final DecompteRepository decompteRepository;
    private final PassageService passageService;
    private final HistoriqueDecompteRepository historiqueDecompteRepository;
    private final PropertiesService propertiesService;
    private final CotisationService cotisationService;

    public TaxationOfficeServiceImpl() {
        taxationOfficeRepository = VulpeculaRepositoryLocator.getTaxationOfficeRepository();
        ligneTaxationRepository = VulpeculaRepositoryLocator.getLigneTaxationRepository();
        passageService = VulpeculaServiceLocator.getPassageService();
        decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
        historiqueDecompteRepository = VulpeculaRepositoryLocator.getHistoriqueDecompteRepository();
        propertiesService = VulpeculaServiceLocator.getPropertiesService();
        cotisationService = VulpeculaServiceLocator.getCotisationService();
    }

    public TaxationOfficeServiceImpl(TaxationOfficeRepository taxationOfficeRepository,
            LigneTaxationRepository ligneTaxationRepository, PassageService passageService,
            DecompteRepository decompteRepository, HistoriqueDecompteRepository historiqueDecompteRepository,
            PropertiesService propertiesService, CotisationService cotisationService) {
        this.taxationOfficeRepository = taxationOfficeRepository;
        this.ligneTaxationRepository = ligneTaxationRepository;
        this.passageService = passageService;
        this.decompteRepository = decompteRepository;
        this.historiqueDecompteRepository = historiqueDecompteRepository;
        this.propertiesService = propertiesService;
        this.cotisationService = cotisationService;
    }

    @Override
    public void update(TaxationOffice taxationOffice) {
        // si le passage de facturation est renseign� et que l'�tat est saisi, il faut passer l'�tat � valider
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
        TaxationOffice to = taxationOfficeRepository.findById(idTaxationOffice);
        to.setLignes(ligneTaxationRepository.findByIdTaxationOffice(idTaxationOffice));
        to.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePrioriteCourrierByIdTiers(to.getIdTiers()));
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
            taxationOfficeRepository.create(taxationOffice);
            addTaxationOfficeToHistorique(decompte);
            updateDecompte(decompte);
            genererTaxationOffice(taxationOffice);
        }
    }

    /**
     * Ajoute un historique "Taxation d'Office" � l'historique du d�compte.
     */
    private void addTaxationOfficeToHistorique(Decompte decompte) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(Date.now());
        historiqueDecompte.setEtat(EtatDecompte.TAXATION_DOFFICE);
        historiqueDecompte.setDecompte(decompte);
        historiqueDecompteRepository.create(historiqueDecompte);
    }

    private void updateDecompte(Decompte decompte) {
        decompte.setDateRappel(null);
        decompte.setEtat(EtatDecompte.TAXATION_DOFFICE);

        NumeroDecompte currentNumDecompte = decompte.getNumeroDecompte();
        if (!String.valueOf(TaxationOffice.OFFSET_FOR_TAXATIONOFFICE).equals(currentNumDecompte.getOffset())) {
            String newOffset = String.valueOf(TaxationOffice.OFFSET_FOR_TAXATIONOFFICE);
            NumeroDecompte newNumDecompte = new NumeroDecompte(currentNumDecompte.getAnnee(),
                    currentNumDecompte.getCode(), newOffset);
            decompte.setNumeroDecompte(newNumDecompte);
        }

        decompteRepository.update(decompte);
    }

    /**
     * G�n�ration des lignes de taxations.
     *
     * La r�gle �tablie est la suivante : on prend le dernier d�compte p�riodique positif comptabilis�. Si aucun n�est
     * trouv�, on applique la masse par d�faut.
     *
     * @param taxationOffice Taxation d'office pour laquelle g�n�rer des lignes
     * @return Taxation d'office avec ses lignes nouvellement cr��es
     */
    TaxationOffice genererTaxationOffice(TaxationOffice taxationOffice) {
        List<LigneTaxation> lignes = new ArrayList<LigneTaxation>();
        Decompte decompte = decompteRepository.findByIdWithDependencies(taxationOffice.getIdDecompte());
        Decompte decomptePrecedent = findDecomptePrecedent(decompte);

        if (decomptePrecedent != null) {
            lignes = genererLignesTaxation(taxationOffice, decompte, decomptePrecedent);
        } else {
            lignes = genererLignesTaxationNouveauDecompte(taxationOffice, decompte);
        }
        taxationOffice.setLignes(lignes);

        return taxationOffice;
    }

    Decompte findDecomptePrecedent(Decompte decompte) {
        return VulpeculaServiceLocator.getDecompteService().findDecomptePrecedent(decompte);
    }

    /**
     * Cr�ation des lignes de taxation et persistence de celles-ci.
     *
     * @param taxationOffice Taxation d'office pour laquelle cr�er des lignes de taxation
     * @param decompte D�compte pour lequel cr�er une taxation d'office
     * @param decomptePrecedent D�compte pr�c�dent sur lequel d�terminer les cotisations � prendre en compte
     * @return Les lignes de taxation g�n�r�es
     */
    private List<LigneTaxation> genererLignesTaxation(TaxationOffice taxationOffice, Decompte decompte,
            Decompte decomptePrecedent) {
        List<LigneTaxation> lignes = creerLignesTaxations(taxationOffice, decompte, decomptePrecedent);
        for (LigneTaxation ligne : lignes) {
            ligneTaxationRepository.create(ligne);
        }
        return lignes;
    }

    /**
     * Cr�ation des lignes de taxation pour un d�compte qui poss�de un d�compte pr�c�dent.
     * Si le d�compte pr�c�dent poss�de des cotisations, on utilise le montant de ces cotisations major�es de 20%
     * Si le d�compte pr�c�dent ne poss�de pas de cotisations, on utilise les cotisations de l'employeur auxquelles on
     * affecte la masse de 20%.
     *
     * @param taxationOffice Taxation d'office pour laquelle cr�er des lignes de taxation
     * @param decompte D�compte pour lequel cr�er une taxation d'office
     * @param decomptePrecedent D�compte pr�c�dent sur lequel d�terminer les cotisations � prendre en compte
     * @return Les lignes de taxation g�n�r�es
     */
    List<LigneTaxation> creerLignesTaxations(TaxationOffice taxationOffice, Decompte decompte,
            Decompte decomptePrecedent) {
        List<LigneTaxation> lignes = new ArrayList<LigneTaxation>();
        List<CotisationCalculee> cotisationCalculees = decomptePrecedent.getTableCotisationsCalculees();

        boolean cotisationsZero = !isCotisationsPositives(cotisationCalculees);
        // boolean cotisationsZero = isCotisationsZero(cotisationCalculees);

        if (cotisationCalculees.isEmpty()) {
            lignes = creerLignesTaxationsFromCotisationsEmployeur(taxationOffice, decompte);
        } else {
            for (CotisationCalculee cotisationCalculee : cotisationCalculees) {
                Montant masse;
                if (cotisationsZero) {
                    masse = getMontantBaseTO();
                } else {
                    // On majore de 20%
                    Montant montant = cotisationCalculee.getMontant();
                    Montant pourcentage = montant.multiply(0.2);
                    masse = montant.add(pourcentage);
                }
                Taux taux = cotisationCalculee.getTaux();
                Montant montantCoti = masse.multiply(taux);

                LigneTaxation ligne = creerLigneTaxation(taxationOffice, masse.normalize(), montantCoti.normalize(),
                        taux, cotisationCalculee.getCotisation());
                lignes.add(ligne);
            }
        }

        return lignes;
    }

    /**
     * G�n�ration des taxations dans le cas o� le d�compte ne poss�de pas de d�compte pr�c�dent.
     * Si le d�compte actuel poss�de des cotisations, on utilise ses cotisations auxquelles on applique la masse par
     * d�faut.
     * Si le d�compte actuel ne poss�de pas de cotisations, on utilise les cotisations de l'affili� auxquelles on
     * applique la masse par d�faut.
     *
     * @param taxationOffice Taxation d'office pour laquelle cr�er les lignes
     * @param decompte D�compte pour lequel la taxation d'office sera cr��
     * @return Les lignes de taxation g�n�r�es pour la taxation d'office
     */
    private List<LigneTaxation> genererLignesTaxationNouveauDecompte(TaxationOffice taxationOffice, Decompte decompte) {
        List<LigneTaxation> lignes = new ArrayList<LigneTaxation>();
        List<CotisationCalculee> cotisationCalculees = decompte.getTableCotisationsCalculees();

        if (cotisationCalculees.isEmpty()) {
            lignes = creerLignesTaxationsFromCotisationsEmployeur(taxationOffice, decompte);
            for (LigneTaxation ligne : lignes) {
                ligneTaxationRepository.create(ligne);
            }
        } else {
            for (CotisationCalculee cotisationCalculee : cotisationCalculees) {
                Montant montant = getMontantBaseTO();

                Montant masse = montant;
                Taux taux = cotisationCalculee.getTaux();
                Montant montantCoti = masse.multiply(taux);

                LigneTaxation ligne = creerLigneTaxation(taxationOffice, masse, montantCoti, taux,
                        cotisationCalculee.getCotisation());
                lignes.add(ligne);
                ligneTaxationRepository.create(ligne);
            }
        }
        return lignes;
    }

    /**
     * Cr�ation des lignes de taxation � partir des cotisations de l'employeur auxquels on affecte la masse par d�faut.
     *
     * @param taxationOffice Taxation d'office pour laquelle cr�er les lignes de taxation
     * @param decompte D�compte pour lequel g�n�rer les lignes
     * @return Liste des lignes de taxation
     */
    private List<LigneTaxation> creerLignesTaxationsFromCotisationsEmployeur(TaxationOffice taxationOffice,
            Decompte decompte) {
        List<LigneTaxation> lignes = new ArrayList<LigneTaxation>();
        List<Cotisation> cotisations = cotisationService.findByIdAffilieForDateWithTaux(decompte.getIdEmployeur(),
                decompte.getPeriodeDebut());
        for (Cotisation cotisation : cotisations) {
            Montant masse = getMontantBaseTO();
            Taux taux = cotisation.getTaux();
            Montant montantCoti = masse.multiply(taux);

            LigneTaxation ligne = creerLigneTaxation(taxationOffice, masse.normalize(), montantCoti.normalize(), taux,
                    cotisation);
            lignes.add(ligne);
        }
        return lignes;
    }

    /**
     * Cr�ation dd'une ligne de taxation d'office
     *
     * @param taxationOffice Taxation d'office pour laquelle cr�er la ligne
     * @param masse Masse de la ligne
     * @param montantCotisation Montant de la cotisation
     * @param taux Taux de la ligne de taxation
     * @param cotisation Cotisation (affiliation) pour laquelle cr�er la ligne
     * @return
     */
    private LigneTaxation creerLigneTaxation(TaxationOffice taxationOffice, Montant masse, Montant montantCotisation,
            Taux taux, Cotisation cotisation) {
        LigneTaxation ligne = new LigneTaxation();
        ligne.setTaxationOffice(taxationOffice);
        ligne.setMasse(masse);
        ligne.setTaux(taux);
        ligne.setMontant(montantCotisation);
        ligne.setCotisation(cotisation);
        return ligne;
    }

    /**
     * @param cotisationCalculees Cotisations � contr�ler
     * @return true si total cotisations positif
     */
    private boolean isCotisationsPositives(List<CotisationCalculee> cotisationCalculees) {
        Montant total = new Montant(0);
        for (CotisationCalculee cc : cotisationCalculees) {
            total = total.add(cc.getMontant());
        }
        return total.isPositive();
    }

    /**
     * Retourne true si toutes les lignes de cotisations sont vides.
     *
     * @param cotisationCalculees Cotisations � contr�ler
     * @return true si cotisations � z�ro
     */
    private boolean isCotisationsZero(List<CotisationCalculee> cotisationCalculees) {
        for (CotisationCalculee cc : cotisationCalculees) {
            if (!cc.getMontant().isZero()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne le montant par d�faut.
     *
     * @return Montant par d�faut
     */
    Montant getMontantBaseTO() {
        return new Montant(propertiesService.getMontantBaseTOSansDecompte());
    }

    @Override
    public void annuler(String idTaxation) {
        TaxationOffice taxationOffice = taxationOfficeRepository.findById(idTaxation);
        annulerTO(taxationOffice);
    }

    @Override
    public void annulerByIdDecompte(String idDecompte) {
        TaxationOffice taxationOffice = taxationOfficeRepository.findByIdDecompte(idDecompte);
        annulerTO(taxationOffice);
    }

    @Override
    public void annulerTO(TaxationOffice taxationOffice) {

        if (taxationOffice.isComptabilisee()) {
            AnnulerDecompteComptabilise annulerTaxationOfficeComptabilise = new AnnulerDecompteComptabilise(
                    taxationOffice);
            String idSection = annulerTaxationOfficeComptabilise
                    .annulerSectionTO("Annulation T.O " + taxationOffice.getIdDecompte());
            taxationOffice.setIdSection(idSection);
        }
        Decompte decompte = taxationOffice.getDecompte();
        decompte.setEtat(EtatDecompte.ANNULE);
        decompteRepository.update(decompte);
        taxationOffice.setEtat(EtatTaxation.ANNULE);
        taxationOffice.setDateAnnulation(Date.now());
        taxationOfficeRepository.update(taxationOffice);
    }

    @Override
    public void annulerForPeriode(List<TaxationOffice> listTO, Periode periode) {
        String employeur = "";
        List<Decompte> listTOComptabilise = new ArrayList<Decompte>();

        for (TaxationOffice taxationOffice : listTO) {
            Periode periodeTO = new Periode(taxationOffice.getPeriodeDebutWithDayAsSwissValue(),
                    taxationOffice.getPeriodeFinWithDayAsSwissValue());

            if (periode.contains(periodeTO) && taxationOffice.getDecompte().isSansSalaire()) {
                if (taxationOffice.isComptabilisee()) {
                    employeur = taxationOffice.getEmployeurAffilieNumero();
                    listTOComptabilise.add(taxationOffice.getDecompte());
                }
                taxationOffice.setEtat(EtatTaxation.ANNULE);
                taxationOfficeRepository.update(taxationOffice);
            }
        }

        AnnulerDecompteComptabilise annulerTaxationOfficeComptabilise = new AnnulerDecompteComptabilise(
                listTOComptabilise);
        annulerTaxationOfficeComptabilise.annulerSection("Annulation des T.O " + employeur);
    }

    @Override
    public boolean hasTO(Employeur employeur, Date dateDebut) {
        return taxationOfficeRepository.findNbTOActives(employeur, dateDebut) > 0;
    }
}
