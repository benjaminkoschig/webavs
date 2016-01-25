package ch.globaz.corvus.businessimpl.services.models.demande;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRentePourServiceDomaine;
import globaz.corvus.db.demandes.REDemandeRentePourServiceDomaineManager;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteCrudService;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.corvus.domaine.BaseCalcul;
import ch.globaz.corvus.domaine.Creance;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteAPI;
import ch.globaz.corvus.domaine.DemandeRenteInvalidite;
import ch.globaz.corvus.domaine.DemandeRenteSurvivant;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.InteretMoratoire;
import ch.globaz.corvus.domaine.PrestationDue;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.RepartitionCreance;
import ch.globaz.corvus.domaine.constantes.Anticipation;
import ch.globaz.corvus.domaine.constantes.Atteinte;
import ch.globaz.corvus.domaine.constantes.Infirmite;
import ch.globaz.corvus.domaine.constantes.OfficeAI;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.prestation.domaine.DossierPrestation;
import ch.globaz.prestation.domaine.EnTeteBlocage;
import ch.globaz.prestation.domaine.InformationsComplementaires;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * Implémentation utilisant le framework Jade d'un service de persistance pour les demandes de rentes
 */
public class DemandeRenteCrudServiceJadeImpl extends CanevasCrudService<DemandeRente> implements
        DemandeRenteCrudService {

    private final JadeCodeSystemeService codeSystemeService;
    private final RenteAccordeeCrudService renteAccordeeCrudService;

    public DemandeRenteCrudServiceJadeImpl(final JadeCodeSystemeService codeSystemeService) {
        super();

        Checkers.checkNotNull(codeSystemeService, "codeSystemeService");

        this.codeSystemeService = codeSystemeService;
        renteAccordeeCrudService = CorvusCrudServiceLocator.getRenteAccordeeCrudService();
    }

    @Override
    public DemandeRente create(final DemandeRente demande) {

        Checkers.checkNotNull(demande, "demande");
        Checkers.checkDoesntHaveID(demande, "demande");
        Checkers.checkHasID(demande.getDossier(), "demande.dossier");
        if (!demande.getRentesAccordees().isEmpty()) {
            for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
                Checkers.checkHasID(uneRenteDeLaDemande.getBaseCalcul(), "demande.uneRente.baseCalcul");
            }
        }

        try {

            REDemandeRente demandeRenteEntity = null;

            switch (demande.getTypeDemandeRente()) {
                case DEMANDE_API:
                    DemandeRenteAPI demandeAPI = (DemandeRenteAPI) demande;

                    REDemandeRenteAPI demandeRenteAPIEntity = new REDemandeRenteAPI();
                    demandeRenteAPIEntity.setCodeOfficeAI("" + demandeAPI.getOfficeAI().getCodeOfficeAI());
                    demandeRenteAPIEntity.setCsAtteinte(demandeAPI.getAtteinte().getCodeSysteme().toString());
                    demandeRenteAPIEntity
                            .setCsGenrePrononceAI(demandeAPI.isAvecMotivation() ? IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION
                                    : IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_SANS_MOTIVATION);
                    demandeRenteAPIEntity.setCsInfirmite(demandeAPI.getInfirmite().getCodeSysteme().toString());
                    demandeRenteAPIEntity.setDateSuvenanceEvenementAssure(demandeAPI.getDateSuvenanceEvenementAssure());

                    demandeRenteEntity = demandeRenteAPIEntity;
                    break;

                case DEMANDE_INVALIDITE:
                    DemandeRenteInvalidite demandeInvalidite = (DemandeRenteInvalidite) demande;

                    REDemandeRenteInvalidite demandeRenteInvaliditeEntity = new REDemandeRenteInvalidite();
                    demandeRenteInvaliditeEntity
                            .setCodeOfficeAI("" + demandeInvalidite.getOfficeAI().getCodeOfficeAI());
                    demandeRenteInvaliditeEntity.setCsAtteinte(demandeInvalidite.getAtteinte().getCodeSysteme()
                            .toString());
                    demandeRenteInvaliditeEntity
                            .setCsGenrePrononceAI(demandeInvalidite.isAvecMotivation() ? IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION
                                    : IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_SANS_MOTIVATION);
                    demandeRenteInvaliditeEntity.setCsInfirmite(demandeInvalidite.getInfirmite().getCodeSysteme()
                            .toString());
                    demandeRenteInvaliditeEntity.setDateDebutRedNonCollaboration(demandeInvalidite
                            .getMoisDebutReductionPourNonCollaboration());
                    demandeRenteInvaliditeEntity.setDateFinRedNonCollaboration(demandeInvalidite
                            .getMoisFinReductionPourNonCollaboration());
                    demandeRenteInvaliditeEntity.setDateSuvenanceEvenementAssure(demandeInvalidite
                            .getDateSuvenanceEvenementAssure());
                    demandeRenteInvaliditeEntity.setNbPageMotivation("" + demandeInvalidite.getNombrePagesMotivation());
                    demandeRenteInvaliditeEntity.setPourcentageReduction(""
                            + demandeInvalidite.getPourcentageReduction().doubleValue());
                    demandeRenteInvaliditeEntity.setPourcentRedFauteGrave(""
                            + demandeInvalidite.getPourcentageReductionPourFauteGrave().doubleValue());
                    demandeRenteInvaliditeEntity.setPourcentRedNonCollaboration(""
                            + demandeInvalidite.getPourcentageReductionPourNonCollaboration().doubleValue());

                    demandeRenteEntity = demandeRenteInvaliditeEntity;
                    break;

                case DEMANDE_SURVIVANT:
                    DemandeRenteSurvivant demandeSurvivant = (DemandeRenteSurvivant) demande;

                    REDemandeRenteSurvivant demandeRenteSurvivantEntity = new REDemandeRenteSurvivant();
                    demandeRenteSurvivantEntity.setPourcentageReduction(""
                            + demandeSurvivant.getPourcentageReduction().doubleValue());

                    demandeRenteEntity = demandeRenteSurvivantEntity;
                    break;

                case DEMANDE_VIEILLESSE:
                    DemandeRenteVieillesse demandeVieillesse = (DemandeRenteVieillesse) demande;

                    REDemandeRenteVieillesse demandeRenteVieillesseEntity = new REDemandeRenteVieillesse();

                    demandeRenteVieillesseEntity.setCsAnneeAnticipation(demandeVieillesse.getAnticipation()
                            .getCodeSysteme().toString());
                    demandeRenteVieillesseEntity.setDateRevocationRequerant(demandeVieillesse
                            .getDateRevocationAjournement());
                    demandeRenteVieillesseEntity.setIsAjournementRequerant(demandeVieillesse.avecAjournement());

                    demandeRenteEntity = demandeRenteVieillesseEntity;
                    break;

                default:
                    throw new IllegalArgumentException("[demande.type] is invalid");

            }

            demandeRenteEntity.setCsEtat(demande.getEtat().getCodeSysteme().toString());
            demandeRenteEntity.setCsTypeCalcul(demande.getTypeCalcul().getCodeSysteme().toString());
            demandeRenteEntity.setCsTypeDemandeRente(demande.getTypeDemandeRente().getCodeSysteme().toString());
            demandeRenteEntity.setDateDebut(demande.getDateDebutDuDroitInitial());
            demandeRenteEntity.setDateDepot(demande.getDateDepot());
            demandeRenteEntity.setDateFin(demande.getDateFinDuDroitInitial());
            demandeRenteEntity.setDateReception(demande.getDateReception());
            demandeRenteEntity.setDateTraitement(demande.getDateTraitement());
            demandeRenteEntity.setIdDemandePrestation(demande.getDossier().getId().toString());
            demandeRenteEntity.setIdGestionnaire(demande.getGestionnaire());
            demandeRenteEntity.add();

            demande.setId(Long.parseLong(demandeRenteEntity.getIdDemandeRente()));

            if (!demande.getRentesAccordees().isEmpty()) {
                RERenteCalculee renteCalculee = new RERenteCalculee();
                renteCalculee.add();

                demandeRenteEntity.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                demandeRenteEntity.update();

                for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
                    REBasesCalcul baseCalculEntity = new REBasesCalcul();
                    baseCalculEntity.setIdBasesCalcul(uneRenteDeLaDemande.getBaseCalcul().getId().toString());
                    baseCalculEntity.retrieve();

                    baseCalculEntity.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                    baseCalculEntity.update();

                    RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
                    renteVerseeATortManager.setForIdRenteNouveauDroit(uneRenteDeLaDemande.getId());
                    renteVerseeATortManager.find(BManager.SIZE_NOLIMIT);

                    for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {
                        uneRenteVerseeATort.retrieve();
                        uneRenteVerseeATort.setIdDemandeRente(Long.parseLong(demandeRenteEntity.getIdDemandeRente()));
                        uneRenteVerseeATort.update();
                    }
                }

                CorvusServiceLocator.getDemandeRenteService().mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(
                        demande);
            }

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return demande;
    }

    @Override
    public boolean delete(final Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public DemandeRente read(final Long id) {

        Checkers.checkNotNull(id, "demande.id");
        DemandeRente demande = null;

        try {
            REDemandeRentePourServiceDomaineManager manager = new REDemandeRentePourServiceDomaineManager();
            manager.setSession(BSessionUtil.getSessionFromThreadContext());
            manager.setForIdDemandeRente(id);
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                throw new RETechnicalException(BSessionUtil.getSessionFromThreadContext().getLabel(
                        "ERREUR_DONNEE_MANQUANTE_DANS_DEMANDE_OU_REQUERANT"));
            }

            Set<RenteAccordee> rentesAccordeesDeLaDemande = new HashSet<RenteAccordee>();
            Map<RenteAccordee, Set<PrestationDue>> prestationsDuesParRente = new HashMap<RenteAccordee, Set<PrestationDue>>();
            Map<RenteAccordee, Set<RepartitionCreance>> repartitionDeCreanceParRente = new HashMap<RenteAccordee, Set<RepartitionCreance>>();
            Map<RenteAccordee, EnTeteBlocage> enTeteBlocageParRente = new HashMap<RenteAccordee, EnTeteBlocage>();
            Map<Integer, Creance> creancesParId = new HashMap<Integer, Creance>();
            PersonneAVS requerant = new PersonneAVS();
            DossierPrestation dossier = new DossierPrestation();
            InformationsComplementaires informationComplementaire = new InformationsComplementaires();

            boolean premierPassage = true;

            for (REDemandeRentePourServiceDomaine uneEntree : manager.getContainerAsList()) {
                if (uneEntree.getIdBaseCalcul() != null) {
                    RenteAccordee uneRenteAccordee = new RenteAccordee();
                    uneRenteAccordee.setId(uneEntree.getIdRenteAccordee());

                    if (!rentesAccordeesDeLaDemande.contains(uneRenteAccordee)) {
                        uneRenteAccordee.setId(uneEntree.getIdRenteAccordee());
                        uneRenteAccordee.setCodePrestation(uneEntree.getCodePrestationRenteAccordee());
                        uneRenteAccordee.setBloquee(uneEntree.laPrestationAccordeeEstBloquee());
                        uneRenteAccordee.setEtat(uneEntree.getEtatPrestationAccordee());
                        uneRenteAccordee.setMoisDebut(uneEntree.getMoisDebutDroitPrestationAccordee());
                        uneRenteAccordee.setMoisFin(uneEntree.getMoisFinDroitPrestationAccordee());
                        uneRenteAccordee.setMontant(uneEntree.getMontantPrestationAccordee());
                        uneRenteAccordee.setCodesCasSpeciaux(uneEntree.getCodesCasSpeciaux());
                        if (uneEntree.getIdInteretMoratoire() != null) {
                            InteretMoratoire interetMoratoire = new InteretMoratoire();
                            interetMoratoire.setId(uneEntree.getIdInteretMoratoire());
                            uneRenteAccordee.setInteretMoratoire(interetMoratoire);
                        }

                        PersonneAVS beneficiaire = new PersonneAVS();
                        beneficiaire.setId(uneEntree.getIdTiersBeneficiairePrestationAccordee());
                        beneficiaire.setNss(uneEntree.getNssTiersBeneficiaire());
                        beneficiaire.setNom(uneEntree.getNomTiersBeneficiaire());
                        beneficiaire.setPrenom(uneEntree.getPrenomTiersBeneficiaire());
                        beneficiaire.setDateDeces(uneEntree.getDateDecesTiersBeneficiaire());
                        beneficiaire.setDateNaissance(uneEntree.getDateNaissanceTiersBeneficiaire());
                        beneficiaire.setSexe(uneEntree.getSexeTiersBeneficiaire());

                        if (uneEntree.getIdPaysTiersBeneficiaire() != null) {
                            Pays paysBeneficiaire = new Pays();
                            paysBeneficiaire.setId(uneEntree.getIdPaysTiersBeneficiaire());
                            paysBeneficiaire.setCodeIso(uneEntree.getCodeIsoPaysTiersBeneficiaire());
                            paysBeneficiaire.setTraductionParLangue(uneEntree.getTraductionsPaysTiersBeneficiaire());
                            beneficiaire.setPays(paysBeneficiaire);
                        }

                        if ((uneEntree.getCsTitreTiersBeneficiaire() != null)
                                && (uneEntree.getCsTitreTiersBeneficiaire().intValue() != 0)) {
                            JadeCodeSysteme csTitreTiers = codeSystemeService.getCodeSysteme(uneEntree
                                    .getCsTitreTiersBeneficiaire().toString());
                            Map<Langues, String> titreTiers = new HashMap<Langues, String>();
                            for (Langues uneLangue : Langues.values()) {
                                if (csTitreTiers.getTraduction(uneLangue) != null) {
                                    titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                                }
                            }
                            beneficiaire.setTitreParLangue(titreTiers);
                        } else {
                            throw new REBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                                    "ERREUR_TITRE_MANQUANT_POUR_TIERS")
                                    + " : " + beneficiaire);
                        }

                        uneRenteAccordee.setBeneficiaire(beneficiaire);

                        BaseCalcul baseCalcul = new BaseCalcul();
                        baseCalcul.setId(uneEntree.getIdBaseCalcul());

                        PersonneAVS donneurDeDroit = new PersonneAVS();
                        donneurDeDroit.setId(uneEntree.getIdTiersBaseCalcul());
                        beneficiaire.setNss(uneEntree.getNssTiersBaseCalcul());
                        donneurDeDroit.setNom(uneEntree.getNomTiersBaseCalcul());
                        donneurDeDroit.setPrenom(uneEntree.getPrenomTiersBaseCalcul());
                        donneurDeDroit.setDateDeces(uneEntree.getDateDecesTiersBaseCalcul());
                        donneurDeDroit.setDateNaissance(uneEntree.getDateNaissanceTiersBaseCalcul());
                        donneurDeDroit.setSexe(uneEntree.getSexeTiersBaseCalcul());

                        if (uneEntree.getIdPaysTiersBaseCalcul() != null) {
                            Pays paysBaseCalcul = new Pays();
                            paysBaseCalcul.setId(uneEntree.getIdPaysTiersBaseCalcul());
                            paysBaseCalcul.setCodeIso(uneEntree.getCodeIsoPaysTiersBaseCalcul());
                            paysBaseCalcul.setTraductionParLangue(uneEntree.getTraductionsPaysTiersBaseCalcul());
                            donneurDeDroit.setPays(paysBaseCalcul);
                        }
                        baseCalcul.setDonneurDeDroit(donneurDeDroit);

                        uneRenteAccordee.setBaseCalcul(baseCalcul);

                        rentesAccordeesDeLaDemande.add(uneRenteAccordee);
                    }

                    if (uneEntree.getIdRepartitionCreance() != null) {
                        RepartitionCreance uneRepartitionCreance = new RepartitionCreance();
                        uneRepartitionCreance.setId(uneEntree.getIdRepartitionCreance());
                        uneRepartitionCreance.setMontantReparti(uneEntree.getMontantRepartitionCreance());

                        Creance creance = creancesParId.get(uneEntree.getIdCreance());
                        if (creance == null) {
                            creance = new Creance();
                            creance.setId(uneEntree.getIdCreance());
                            creance.setMontant(uneEntree.getMontantCreance());

                            if (uneEntree.getIdTiersCreancier() != null) {
                                Tiers creancier = new Tiers();
                                creancier.setId(uneEntree.getIdTiersCreancier());
                                creancier.setDesignation1(uneEntree.getNomTiersCreancier());
                                creancier.setDesignation2(uneEntree.getPrenomTiersCreancier());

                                if ((uneEntree.getCsTitreTiersCreancier() != null)
                                        && (uneEntree.getCsTitreTiersCreancier().intValue() != 0)) {
                                    JadeCodeSysteme csTitreTiers = codeSystemeService.getCodeSysteme(uneEntree
                                            .getCsTitreTiersCreancier().toString());
                                    Map<Langues, String> titreTiers = new HashMap<Langues, String>();
                                    for (Langues uneLangue : Langues.values()) {
                                        if (csTitreTiers.getTraduction(uneLangue) != null) {
                                            titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                                        }
                                    }
                                    creancier.setTitreParLangue(titreTiers);
                                }

                                if (uneEntree.getIdPaysTiersCreancier() != null) {
                                    Pays paysCreancier = new Pays();
                                    paysCreancier.setId(uneEntree.getIdPaysTiersCreancier());
                                    paysCreancier.setCodeIso(uneEntree.getCodeIsoPaysTiersCreancier());
                                    paysCreancier.setTraductionParLangue(uneEntree.getTraductionsPaysTiersCreancier());
                                    creancier.setPays(paysCreancier);
                                }

                                creance.setCreancier(creancier);
                            }
                        }
                        uneRepartitionCreance.setCreance(creance);

                        if (repartitionDeCreanceParRente.containsKey(uneRenteAccordee)) {
                            repartitionDeCreanceParRente.get(uneRenteAccordee).add(uneRepartitionCreance);
                        } else {
                            repartitionDeCreanceParRente.put(uneRenteAccordee,
                                    new HashSet<RepartitionCreance>(Arrays.asList(uneRepartitionCreance)));
                        }
                    }

                    if (uneEntree.getIdPrestationDue() != null) {
                        PrestationDue prestationDue = new PrestationDue();
                        prestationDue.setId(uneEntree.getIdPrestationDue());
                        prestationDue.setMontant(uneEntree.getMontantPrestationDue());
                        prestationDue.setPeriode(new Periode(uneEntree.getMoisDebutPrestationDue(), uneEntree
                                .getMoisFinPrestationDue()));
                        prestationDue.setType(uneEntree.getTypePrestationDue());

                        if (prestationsDuesParRente.containsKey(uneRenteAccordee)) {
                            prestationsDuesParRente.get(uneRenteAccordee).add(prestationDue);
                        } else {
                            prestationsDuesParRente.put(uneRenteAccordee,
                                    new HashSet<PrestationDue>(Arrays.asList(prestationDue)));
                        }
                    }

                    if (uneEntree.getIdEnTeteBlocage() != null) {
                        EnTeteBlocage enTeteBlocage;

                        if (!enTeteBlocageParRente.containsKey(uneRenteAccordee)) {
                            enTeteBlocage = new EnTeteBlocage();
                            enTeteBlocage.setId(uneEntree.getIdEnTeteBlocage());
                            enTeteBlocage.setMontantDebloque(uneEntree.getMontantDebloque());

                            enTeteBlocageParRente.put(uneRenteAccordee, enTeteBlocage);
                        } else {
                            enTeteBlocage = enTeteBlocageParRente.get(uneRenteAccordee);
                        }

                        if ((uneEntree.getIdBlocage() != null)
                                && !enTeteBlocage.contientUnBlocagePourLeMois(uneEntree.getMoisBlocage())) {
                            enTeteBlocage.ajouterUnBlocagePourLeMois(uneEntree.getMoisBlocage(),
                                    uneEntree.getMontantBlocage());
                        }
                    }
                }

                if (premierPassage) {

                    switch (uneEntree.getTypeDemandeRente()) {
                        case DEMANDE_API:
                            DemandeRenteAPI demandeAPI = new DemandeRenteAPI();

                            REDemandeRenteAPI demandeAPIEntity = new REDemandeRenteAPI();
                            demandeAPIEntity.setSession(BSessionUtil.getSessionFromThreadContext());
                            demandeAPIEntity.setIdDemandeRente(id.toString());
                            demandeAPIEntity.retrieve();

                            demandeAPI.setAtteinte(Atteinte.parse(demandeAPIEntity.getCsAtteinte()));
                            demandeAPI.setAvecMotivation(IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION
                                    .equals(demandeAPIEntity.getCsGenrePrononceAI()));
                            demandeAPI.setDateSuvenanceEvenementAssure(demandeAPIEntity
                                    .getDateSuvenanceEvenementAssure());
                            demandeAPI.setInfirmite(Infirmite.parse(demandeAPIEntity.getCsInfirmite()));
                            demandeAPI.setOfficeAI(OfficeAI.getOfficeAIPourCode(Integer.parseInt(demandeAPIEntity
                                    .getCodeOfficeAI())));

                            demande = demandeAPI;
                            break;
                        case DEMANDE_INVALIDITE:
                            DemandeRenteInvalidite demandeInvalidite = new DemandeRenteInvalidite();

                            REDemandeRenteInvalidite demandeInvaliditeEntity = new REDemandeRenteInvalidite();
                            demandeInvaliditeEntity.setSession(BSessionUtil.getSessionFromThreadContext());
                            demandeInvaliditeEntity.setIdDemandeRente(id.toString());
                            demandeInvaliditeEntity.retrieve();

                            demandeInvalidite.setAtteinte(Atteinte.parse(demandeInvaliditeEntity.getCsAtteinte()));
                            demandeInvalidite
                                    .setAvecMotivation(IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION
                                            .equals(demandeInvaliditeEntity.getCsGenrePrononceAI()));
                            demandeInvalidite.setDateSuvenanceEvenementAssure(demandeInvaliditeEntity
                                    .getDateSuvenanceEvenementAssure());
                            demandeInvalidite.setInfirmite(Infirmite.parse(demandeInvaliditeEntity.getCsInfirmite()));
                            demandeInvalidite.setMoisDebutReductionPourNonCollaboration(demandeInvaliditeEntity
                                    .getDateDebutRedNonCollaboration());
                            demandeInvalidite.setMoisFinReductionPourNonCollaboration(demandeInvaliditeEntity
                                    .getDateFinRedNonCollaboration());
                            demandeInvalidite.setNombrePagesMotivation(Integer.parseInt(demandeInvaliditeEntity
                                    .getNbPageMotivation()));
                            demandeInvalidite.setOfficeAI(OfficeAI.getOfficeAIPourCode(Integer
                                    .parseInt(demandeInvaliditeEntity.getCodeOfficeAI())));
                            demandeInvalidite.setPourcentageReduction(new Pourcentage(Double
                                    .parseDouble(demandeInvaliditeEntity.getPourcentageReduction())));
                            demandeInvalidite.setPourcentageReductionPourFauteGrave(new Pourcentage(Double
                                    .parseDouble(demandeInvaliditeEntity.getPourcentRedFauteGrave())));
                            demandeInvalidite.setPourcentageReductionPourNonCollaboration(new Pourcentage(Double
                                    .parseDouble(demandeInvaliditeEntity.getPourcentRedNonCollaboration())));

                            demande = demandeInvalidite;
                            break;
                        case DEMANDE_SURVIVANT:
                            DemandeRenteSurvivant demandeSurvivant = new DemandeRenteSurvivant();

                            REDemandeRenteSurvivant demandeSurvivantEntity = new REDemandeRenteSurvivant();
                            demandeSurvivantEntity.setSession(BSessionUtil.getSessionFromThreadContext());
                            demandeSurvivantEntity.setIdDemandeRente(id.toString());
                            demandeSurvivantEntity.retrieve();

                            demandeSurvivant.setPourcentageReduction(new Pourcentage(Double
                                    .parseDouble(demandeSurvivantEntity.getPourcentageReduction())));

                            demande = demandeSurvivant;
                            break;
                        case DEMANDE_VIEILLESSE:
                            DemandeRenteVieillesse demandeVieillesse = new DemandeRenteVieillesse();

                            REDemandeRenteVieillesse demandeVieillesseEntity = new REDemandeRenteVieillesse();
                            demandeVieillesseEntity.setSession(BSessionUtil.getSessionFromThreadContext());
                            demandeVieillesseEntity.setIdDemandeRente(id.toString());
                            demandeVieillesseEntity.retrieve();

                            if (!JadeStringUtil.isBlankOrZero(demandeVieillesseEntity.getCsAnneeAnticipation())) {
                                demandeVieillesse.setAnticipation(Anticipation.parse(demandeVieillesseEntity
                                        .getCsAnneeAnticipation()));
                            }
                            demandeVieillesse.setAvecAjournement(demandeVieillesseEntity.getIsAjournementRequerant());
                            demandeVieillesse.setDateRevocationAjournement(demandeVieillesseEntity
                                    .getDateRevocationRequerant());

                            demande = demandeVieillesse;
                            break;
                        default:
                            throw new IllegalArgumentException("[typeDemandeRente] is invalid");
                    }

                    demande.setId(id);

                    requerant.setId(uneEntree.getIdTiersRequerant());
                    requerant.setNom(uneEntree.getNomTiersRequerant());
                    requerant.setPrenom(uneEntree.getPrenomTiersRequerant());
                    requerant.setNss(uneEntree.getNssTiersRequerant());
                    requerant.setDateNaissance(uneEntree.getDateNaissanceTiersRequerant());
                    requerant.setDateDeces(uneEntree.getDateDecesTiersRequerant());
                    requerant.setSexe(uneEntree.getSexeTiersRequerant());

                    if ((uneEntree.getCsTitreTiersRequerant() != null)
                            && (uneEntree.getCsTitreTiersRequerant().intValue() != 0)) {
                        JadeCodeSysteme csTitreTiers = codeSystemeService.getCodeSysteme(uneEntree
                                .getCsTitreTiersRequerant().toString());
                        Map<Langues, String> titreTiers = new HashMap<Langues, String>();
                        for (Langues uneLangue : Langues.values()) {
                            if (csTitreTiers.getTraduction(uneLangue) != null) {
                                titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                            }
                        }
                        requerant.setTitreParLangue(titreTiers);
                    } else {
                        throw new REBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                                "ERREUR_TITRE_MANQUANT_POUR_TIERS")
                                + " : " + requerant);
                    }

                    Pays nationaliteRequerant = new Pays();
                    nationaliteRequerant.setId(uneEntree.getIdPaysTiersRequerant());
                    nationaliteRequerant.setCodeIso(uneEntree.getCodeIsoPaysTiersRequerant());
                    nationaliteRequerant.setTraductionParLangue(uneEntree.getTraductionsPaysTiersRequerant());
                    requerant.setPays(nationaliteRequerant);

                    dossier.setId(uneEntree.getIdDemandePrestation());
                    dossier.setRequerant(requerant);

                    demande.setDateDepot(uneEntree.getDateDepotDemandeRente());
                    demande.setDateDebutDuDroitInitial(uneEntree.getDateDebutDroitDemandeRente());
                    demande.setDateFinDuDroitInitial(uneEntree.getDateFinDroitDemandeRente());
                    demande.setDateReception(uneEntree.getDateReceptionDemandeRente());
                    demande.setDateTraitement(uneEntree.getDateTraitementDemandeRente());
                    demande.setEtat(uneEntree.getEtatDemandeRente());
                    demande.setGestionnaire(uneEntree.getGestionnaireDemandeRente());
                    demande.setTypeCalcul(uneEntree.getTypeCalculDemandeRente());
                    demande.setTypeDemandeRente(uneEntree.getTypeDemandeRente());

                    if (!uneEntree.getIdInfoComplementaire().equals(0)) {
                        informationComplementaire.setId(uneEntree.getIdInfoComplementaire());
                    }

                    premierPassage = false;
                }
            }

            for (RenteAccordee uneRenteAccorde : rentesAccordeesDeLaDemande) {

                Set<RepartitionCreance> repartitionsCreancePourLaRente = repartitionDeCreanceParRente
                        .get(uneRenteAccorde);
                if (repartitionsCreancePourLaRente != null) {
                    uneRenteAccorde.setRepartitionCreance(repartitionsCreancePourLaRente);
                }

                Set<PrestationDue> prestationsDuesPourLaRente = prestationsDuesParRente.get(uneRenteAccorde);
                if (prestationsDuesPourLaRente != null) {
                    uneRenteAccorde.setPrestationsDues(prestationsDuesPourLaRente);
                }

                EnTeteBlocage enTeteBlocage = enTeteBlocageParRente.get(uneRenteAccorde);
                if (enTeteBlocage != null) {
                    uneRenteAccorde.setEnTeteBlocage(enTeteBlocage);
                }
            }

            demande.setDossier(dossier);
            demande.setInformationsComplementaires(informationComplementaire);
            demande.setRentesAccordees(rentesAccordeesDeLaDemande);
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return demande;
    }

    @Override
    public DemandeRente update(final DemandeRente objetDeDomaine) {

        try {

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setIdDemandeRente(objetDeDomaine.getId().toString());
            demandeRente.retrieve();

            if (demandeRente.isNew()) {
                throw new IllegalArgumentException("Entity not found in DB : " + objetDeDomaine.toString());
            }

            demandeRente.setCsEtat(objetDeDomaine.getEtat().getCodeSysteme().toString());
            demandeRente.update();

            RERenteCalculee renteCalculee = new RERenteCalculee();
            renteCalculee.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
            renteCalculee.retrieve();

            if (renteCalculee.isNew()) {
                renteCalculee.add();
                demandeRente.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                demandeRente.update();
            }

            List<String> idsRAtoString = new ArrayList<String>();
            for (RenteAccordee uneRenteAccordeeDeLaDemande : objetDeDomaine.getRentesAccordees()) {
                BaseCalcul baseCalcul = uneRenteAccordeeDeLaDemande.getBaseCalcul();

                REBasesCalcul baseCalculEntity = new REBasesCalcul();
                baseCalculEntity.setIdBasesCalcul(baseCalcul.getId().toString());
                baseCalculEntity.retrieve();

                baseCalculEntity.setIdRenteCalculee(renteCalculee.getIdRenteCalculee());
                baseCalculEntity.update();

                renteAccordeeCrudService.update(uneRenteAccordeeDeLaDemande);

                idsRAtoString.add(uneRenteAccordeeDeLaDemande.getId().toString());
            }

            CorvusServiceLocator.getDemandeRenteService().mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(
                    objetDeDomaine);
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
        return objetDeDomaine;
    }
}
