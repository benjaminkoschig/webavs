package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSalaireAAnnoncerComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSalaireAAnnoncerHistoriqueComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;
import ch.globaz.vulpecula.ws.bean.StatusLine;

/**
 * Convertisseur d'objet {@link DecompteSalaire} <--> {@link LigneDecompteComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class DecompteSalaireConverter implements
        DomaineConverterJade<DecompteSalaire, LigneDecompteComplexModel, LigneDecompteSimpleModel> {

    private static final DecompteConverter DECOMPTE_CONVERTER = new DecompteConverter();

    private static final DecompteSalaireConverter INSTANCE = new DecompteSalaireConverter();

    public static DecompteSalaireConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public LigneDecompteSimpleModel convertToPersistence(final DecompteSalaire ligneDecompte) {
        LigneDecompteSimpleModel ligneDecompteSimpleModel = new LigneDecompteSimpleModel();

        ligneDecompteSimpleModel.setId(ligneDecompte.getId());
        if (ligneDecompte.getDecompte() != null) {
            ligneDecompteSimpleModel.setIdDecompte(ligneDecompte.getDecompte().getId());
        }
        if (ligneDecompte.getPosteTravail() != null) {
            ligneDecompteSimpleModel.setIdPosteTravail(ligneDecompte.getPosteTravail().getId());
        } else {
            ligneDecompteSimpleModel.setIdPosteTravail(null);
        }
        ligneDecompteSimpleModel.setNombreHeures(String.valueOf(ligneDecompte.getHeures()));
        ligneDecompteSimpleModel.setSalaireHoraire(String.valueOf(ligneDecompte.getSalaireHoraireAsValue()));
        ligneDecompteSimpleModel.setSalaireTotal(String.valueOf(ligneDecompte.getSalaireTotalAsValue()));
        ligneDecompteSimpleModel.setPeriodeDebut(ligneDecompte.getPeriode().getDateDebutAsSwissValue());
        ligneDecompteSimpleModel.setPeriodeFin(ligneDecompte.getPeriode().getDateFinAsSwissValue());
        ligneDecompteSimpleModel.setSequence(ligneDecompte.getSequence());
        ligneDecompteSimpleModel.setDateAnnonce(ligneDecompte.getDateAnnonce());
        if (ligneDecompte.getMontantFranchise() != null) {
            ligneDecompteSimpleModel.setMontantFranchise(ligneDecompte.getMontantFranchise().getValue());
        }
        if (ligneDecompte.getTauxContribuableForCaissesSocialesAsValue(false) != null) {
            ligneDecompteSimpleModel.setTauxContribuable(String.valueOf(ligneDecompte
                    .getTauxContribuableForCaissesSocialesAsValue(false)));
        }
        if (ligneDecompte.getTauxForEbu() != null && !ligneDecompte.getTauxForEbu().isZero()) {
            ligneDecompteSimpleModel.setTauxContribuable(String.valueOf(ligneDecompte.getTauxForEbu()));
        }
        if (ligneDecompte.getDateAnnonceLPP() != null) {
            ligneDecompteSimpleModel.setDateAnnonceLPP(ligneDecompte.getDateAnnonceLPP().getSwissValue());
        }
        ligneDecompteSimpleModel.setGenreCotisations(ligneDecompte.getGenreCotisations());
        ligneDecompteSimpleModel.setSpy(ligneDecompte.getSpy());
        ligneDecompteSimpleModel.setRemarque(ligneDecompte.getRemarque());
        if (ligneDecompte.getAnneeCotisations() != null) {
            ligneDecompteSimpleModel
                    .setAnneeCotisations(String.valueOf(ligneDecompte.getAnneeCotisations().getValue()));
        }

        if (ligneDecompte.isaTraiter()) {
            ligneDecompteSimpleModel.setToTreat(ligneDecompte.isaTraiter());
        }
        ligneDecompteSimpleModel.setCorrelationId(ligneDecompte.getCorrelationId());
        ligneDecompteSimpleModel.setLineCorrelationId(ligneDecompte.getLineCorrelationId());
        ligneDecompteSimpleModel.setPosteCorrelationId(ligneDecompte.getPosteCorrelationId());
        ligneDecompteSimpleModel.setTauxSaisieEbu(ligneDecompte.getTauxSaisieEbu());

        ligneDecompteSimpleModel.setMntAbsencesJustifiees(ligneDecompte.getAbsencesJustifieesAsValue());
        ligneDecompteSimpleModel.setMntAPGComplementaireSM(ligneDecompte.getApgComplementaireSMAsValue());
        ligneDecompteSimpleModel.setMntGratifications(ligneDecompte.getGratificationsAsValue());
        ligneDecompteSimpleModel.setMntVacancesFeries(ligneDecompte.getVacancesFeriesAsValue());

        if (ligneDecompte.getStatus() != null) {
            ligneDecompteSimpleModel.setStatus(ligneDecompte.getStatus().getValue());
        }

        return ligneDecompteSimpleModel;
    }

    public DecompteSalaire convertToDomain(
            final DecompteSalaireAAnnoncerHistoriqueComplexModel decompteSalaireComplexModel) {
        Employeur employeur = new Employeur();
        employeur.setAffilieNumero(decompteSalaireComplexModel.getAffiliationSimpleModel().getAffilieNumero());
        employeur.setId(decompteSalaireComplexModel.getAffiliationSimpleModel().getId());

        Travailleur travailleur = new Travailleur();
        travailleur.setDesignation1(decompteSalaireComplexModel.getTravailleurTiersSimpleModel().getDesignation1());
        travailleur.setDesignation2(decompteSalaireComplexModel.getTravailleurTiersSimpleModel().getDesignation2());
        travailleur.setNumAvsActuel(decompteSalaireComplexModel.getTravailleurPersonneEtendueSimpleModel()
                .getNumAvsActuel());
        travailleur.setId(decompteSalaireComplexModel.getTravailleurSimpleModel().getId());

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setEmployeur(employeur);
        posteTravail.setTravailleur(travailleur);
        posteTravail.setId(decompteSalaireComplexModel.getPosteTravailSimpleModel().getId());

        Decompte decompte = new Decompte();
        decompte.setId(decompteSalaireComplexModel.getDecompteSimpleModel().getId());

        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setGenreCotisations(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getGenreCotisations());
        decompteSalaire.setId(decompteSalaireComplexModel.getId());
        decompteSalaire.setPeriode(new Periode(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getPeriodeDebut(), decompteSalaireComplexModel.getLigneDecompteSimpleModel().getPeriodeFin()));
        decompteSalaire.setSalaireTotal(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getSalaireTotal()));
        decompteSalaire.setSpy(decompteSalaireComplexModel.getSpy());
        decompteSalaire.setPosteTravail(posteTravail);
        decompteSalaire.setDecompte(decompte);
        if (!JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel().getDateAnnonceLPP())) {
            decompteSalaire.setDateAnnonceLPP(new Date(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getDateAnnonceLPP()));
        }
        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMontantFranchise())) {
            decompteSalaire.setMontantFranchise(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMontantFranchise()));
        }
        decompteSalaire.setLineCorrelationId(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getLineCorrelationId());
        decompteSalaire.setPosteCorrelationId(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getPosteCorrelationId());
        decompteSalaire.setTauxSaisieEbu(decompteSalaireComplexModel.getLigneDecompteSimpleModel().getTauxSaisieEbu());
        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getStatus() != null) {
            decompteSalaire.setStatus(StatusLine.valueOf(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getStatus()));
        }
        return decompteSalaire;
    }

    public DecompteSalaire convertToDomain(final DecompteSalaireAAnnoncerComplexModel decompteSalaireComplexModel) {
        Employeur employeur = new Employeur();
        employeur.setAffilieNumero(decompteSalaireComplexModel.getAffiliationSimpleModel().getAffilieNumero());
        employeur.setId(decompteSalaireComplexModel.getAffiliationSimpleModel().getId());

        Travailleur travailleur = new Travailleur();
        travailleur.setDesignation1(decompteSalaireComplexModel.getTravailleurTiersSimpleModel().getDesignation1());
        travailleur.setDesignation2(decompteSalaireComplexModel.getTravailleurTiersSimpleModel().getDesignation2());
        travailleur.setNumAvsActuel(decompteSalaireComplexModel.getTravailleurPersonneEtendueSimpleModel()
                .getNumAvsActuel());
        travailleur.setId(decompteSalaireComplexModel.getTravailleurSimpleModel().getId());

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setEmployeur(employeur);
        posteTravail.setTravailleur(travailleur);
        posteTravail.setId(decompteSalaireComplexModel.getPosteTravailSimpleModel().getId());

        Decompte decompte = new Decompte();
        decompte.setId(decompteSalaireComplexModel.getDecompteSimpleModel().getId());

        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setGenreCotisations(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getGenreCotisations());
        decompteSalaire.setId(decompteSalaireComplexModel.getId());
        decompteSalaire.setPeriode(new Periode(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getPeriodeDebut(), decompteSalaireComplexModel.getLigneDecompteSimpleModel().getPeriodeFin()));
        decompteSalaire.setSalaireTotal(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getSalaireTotal()));
        decompteSalaire.setSpy(decompteSalaireComplexModel.getSpy());
        if (!JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel().getDateAnnonceLPP())) {
            decompteSalaire.setDateAnnonceLPP(new Date(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getDateAnnonceLPP()));
        }
        decompteSalaire.setPosteTravail(posteTravail);
        decompteSalaire.setDecompte(decompte);
        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMontantFranchise())) {
            decompteSalaire.setMontantFranchise(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMontantFranchise()));
        }
        decompteSalaire.setLineCorrelationId(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getLineCorrelationId());
        decompteSalaire.setPosteCorrelationId(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getPosteCorrelationId());
        decompteSalaire.setTauxSaisieEbu(decompteSalaireComplexModel.getLigneDecompteSimpleModel().getTauxSaisieEbu());

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getMntAbsencesJustifiees() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMntAbsencesJustifiees())) {
            decompteSalaire.setAbsencesJustifiees(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMntAbsencesJustifiees()));
        }

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getMntAPGComplementaireSM() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMntAPGComplementaireSM())) {
            decompteSalaire.setApgComplementaireSM(new Montant(decompteSalaireComplexModel
                    .getLigneDecompteSimpleModel().getMntAPGComplementaireSM()));
        }

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getMntGratifications() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMntGratifications())) {
            decompteSalaire.setGratifications(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMntGratifications()));
        }

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getMntGratifications() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMntGratifications())) {
            decompteSalaire.setGratifications(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMntGratifications()));
        }

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getMntVacancesFeries() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMntVacancesFeries())) {
            decompteSalaire.setVacancesFeries(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMntVacancesFeries()));
        }

        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel().getStatus() != null) {
            decompteSalaire.setStatus(StatusLine.valueOf(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getStatus()));
        }

        return decompteSalaire;
    }

    @Override
    public DecompteSalaire convertToDomain(final LigneDecompteComplexModel ligneDecompteComplexModel) {
        AffiliationTiersComplexModel affiliationTiersComplexModel = new AffiliationTiersComplexModel();
        affiliationTiersComplexModel.setAffiliation(ligneDecompteComplexModel.getAffiliationSimpleModel());
        affiliationTiersComplexModel.setTiersAffiliation(ligneDecompteComplexModel.getEmployeurTiersSimpleModel());

        AdministrationComplexModel administrationComplexModel = new AdministrationComplexModel();
        administrationComplexModel.setTiers(ligneDecompteComplexModel.getConventionTiersSimpleModel());
        administrationComplexModel.setAdmin(ligneDecompteComplexModel.getConventionAdministrationSimpleModel());

        EmployeurComplexModel employeurComplexModel = new EmployeurComplexModel();
        employeurComplexModel.setEmployeurSimpleModel(ligneDecompteComplexModel.getEmployeurSimpleModel());
        employeurComplexModel.setAffiliationTiersComplexModel(affiliationTiersComplexModel);
        employeurComplexModel.setAdministrationComplexModel(administrationComplexModel);

        PersonneSimpleModel personneSimpleModel = ligneDecompteComplexModel.getTravailleurPersonneSimpleModel();
        PersonneEtendueComplexModel personneEtendueComplexModel = new PersonneEtendueComplexModel();
        personneEtendueComplexModel.getPersonne().setCanton(personneSimpleModel.getCanton());
        personneEtendueComplexModel.getPersonne().setCreationSpy(personneSimpleModel.getCreationSpy());
        personneEtendueComplexModel.getPersonne().setDateDeces(personneSimpleModel.getDateDeces());
        personneEtendueComplexModel.getPersonne().setDateNaissance(personneSimpleModel.getDateNaissance());
        personneEtendueComplexModel.getPersonne().setDistrict(personneSimpleModel.getDistrict());
        personneEtendueComplexModel.getPersonne().setEtatCivil(personneSimpleModel.getEtatCivil());
        personneEtendueComplexModel.getPersonne().setId(personneSimpleModel.getId());
        personneEtendueComplexModel.getPersonne()
                .setIdLocaliteDependance(personneSimpleModel.getIdLocaliteDependance());
        personneEtendueComplexModel.getPersonne().setIdTiers(personneSimpleModel.getIdTiers());
        personneEtendueComplexModel.getPersonne().setSexe(personneSimpleModel.getSexe());
        personneEtendueComplexModel.getPersonne().setSpy(personneSimpleModel.getSpy());

        TiersSimpleModel tiersSimpleModel = ligneDecompteComplexModel.getTravailleurTiersSimpleModel();
        personneEtendueComplexModel.getTiers().set_inactif(tiersSimpleModel.get_inactif());
        personneEtendueComplexModel.getTiers().set_personneMorale(tiersSimpleModel.get_personneMorale());
        personneEtendueComplexModel.getTiers().set_personnePhysique(tiersSimpleModel.get_personnePhysique());
        personneEtendueComplexModel.getTiers().setCreationSpy(tiersSimpleModel.getCreationSpy());
        personneEtendueComplexModel.getTiers().setDesignation1(tiersSimpleModel.getDesignation1());
        personneEtendueComplexModel.getTiers().setDesignation2(tiersSimpleModel.getDesignation2());
        personneEtendueComplexModel.getTiers().setDesignation3(tiersSimpleModel.getDesignation3());
        personneEtendueComplexModel.getTiers().setDesignation4(tiersSimpleModel.getDesignation4());
        personneEtendueComplexModel.getTiers().setDesignationCourt(tiersSimpleModel.getDesignationCourt());
        personneEtendueComplexModel.getTiers().setDesignationUpper1(tiersSimpleModel.getDesignationUpper1());
        personneEtendueComplexModel.getTiers().setDesignationUpper2(tiersSimpleModel.getDesignationUpper2());
        personneEtendueComplexModel.getTiers().setId(tiersSimpleModel.getId());
        personneEtendueComplexModel.getTiers().setIdPays(tiersSimpleModel.getIdPays());
        personneEtendueComplexModel.getTiers().setIdTiers(tiersSimpleModel.getIdTiers());
        personneEtendueComplexModel.getTiers().setIdTiersExterne(tiersSimpleModel.getIdTiersExterne());
        personneEtendueComplexModel.getTiers().setInactif(tiersSimpleModel.getInactif());
        personneEtendueComplexModel.getTiers().setLangue(tiersSimpleModel.getLangue());
        personneEtendueComplexModel.getTiers().setPersonneMorale(tiersSimpleModel.getPersonneMorale());
        personneEtendueComplexModel.getTiers().setPersonnePhysique(tiersSimpleModel.getPersonnePhysique());
        personneEtendueComplexModel.getTiers().setPolitesseSpecDe(tiersSimpleModel.getPolitesseSpecDe());
        personneEtendueComplexModel.getTiers().setPolitesseSpecFr(tiersSimpleModel.getPolitesseSpecFr());
        personneEtendueComplexModel.getTiers().setPolitesseSpecIt(tiersSimpleModel.getPolitesseSpecIt());
        personneEtendueComplexModel.getTiers().setSpy(tiersSimpleModel.getSpy());
        personneEtendueComplexModel.getTiers().setTitreTiers(tiersSimpleModel.getTitreTiers());
        personneEtendueComplexModel.getTiers().setTypeTiers(tiersSimpleModel.getTypeTiers());

        personneEtendueComplexModel.getPersonneEtendue().setNumAvsActuel(
                ligneDecompteComplexModel.getTravailleurPersonneEtendueSimpleModel().getNumAvsActuel());

        TravailleurComplexModel travailleurComplexModel = new TravailleurComplexModel();
        travailleurComplexModel.setTravailleurSimpleModel(ligneDecompteComplexModel.getTravailleurSimpleModel());
        travailleurComplexModel.setPersonneEtendueComplexModel(personneEtendueComplexModel);

        PosteTravailComplexModel posteTravailComplexModel = new PosteTravailComplexModel();
        posteTravailComplexModel.setPosteTravailSimpleModel(ligneDecompteComplexModel.getPosteTravailSimpleModel());
        posteTravailComplexModel.setEmployeurComplexModel(employeurComplexModel);
        posteTravailComplexModel.setTravailleurComplexModel(travailleurComplexModel);

        DecompteComplexModel decompteComplexModel = new DecompteComplexModel();
        decompteComplexModel.setEmployeurComplexModel(employeurComplexModel);
        decompteComplexModel.setDecompteSimpleModel(ligneDecompteComplexModel.getDecompteSimpleModel());

        LigneDecompteSimpleModel ligneDecompteSimpleModel = ligneDecompteComplexModel.getLigneDecompteSimpleModel();

        Decompte decompte = DECOMPTE_CONVERTER.convertToDomain(decompteComplexModel);

        DecompteSalaire ligneDecompte = convertToDomain(ligneDecompteSimpleModel);
        ligneDecompte.setPosteTravail(PosteTravailConverter.getInstance().convertToDomain(posteTravailComplexModel));
        if (!JadeStringUtil.isEmpty(ligneDecompteComplexModel.getLigneDecompteSimpleModel().getDateAnnonceLPP())) {
            ligneDecompte.setDateAnnonceLPP(new Date(ligneDecompteComplexModel.getLigneDecompteSimpleModel()
                    .getDateAnnonceLPP()));
        }
        ligneDecompte.setDecompte(decompte);

        return ligneDecompte;
    }

    @Override
    public DecompteSalaire convertToDomain(final LigneDecompteSimpleModel ligneDecompteSimpleModel) {
        DecompteSalaire ligneDecompte = new DecompteSalaire();

        ligneDecompte.setId(ligneDecompteSimpleModel.getId());
        if (ligneDecompteSimpleModel.getNombreHeures() != null) {
            ligneDecompte.setHeures(Double.valueOf(ligneDecompteSimpleModel.getNombreHeures()));
        }
        if (ligneDecompteSimpleModel.getSalaireHoraire() != null) {
            ligneDecompte.setSalaireHoraire(new Montant(ligneDecompteSimpleModel.getSalaireHoraire()));
        }
        if (ligneDecompteSimpleModel.getSalaireTotal() != null) {
            ligneDecompte.setSalaireTotal(new Montant(ligneDecompteSimpleModel.getSalaireTotal()));
        }
        String periodeDebut = ligneDecompteSimpleModel.getPeriodeDebut();
        String periodeFin = ligneDecompteSimpleModel.getPeriodeFin();
        if (periodeDebut != null) {
            ligneDecompte.setPeriode(new Periode(periodeDebut, periodeFin));
        }
        if (!JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getMontantFranchise())) {
            ligneDecompte.setMontantFranchise(new Montant(ligneDecompteSimpleModel.getMontantFranchise()));
        } else {
            ligneDecompte.setMontantFranchise(Montant.ZERO);
        }
        ligneDecompte.setSequence(ligneDecompteSimpleModel.getSequence());
        ligneDecompte.setDateAnnonce(ligneDecompteSimpleModel.getDateAnnonce());
        ligneDecompte.setTauxContribuableAffiche(new Taux(ligneDecompteSimpleModel.getTauxContribuable()));
        ligneDecompte.setSpy(ligneDecompteSimpleModel.getSpy());
        ligneDecompte.setGenreCotisations(ligneDecompteSimpleModel.getGenreCotisations());
        if (!JadeNumericUtil.isEmptyOrZero(ligneDecompteSimpleModel.getAnneeCotisations())) {
            ligneDecompte.setAnneeCotisations(new Annee(ligneDecompteSimpleModel.getAnneeCotisations()));
        }

        if (!JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getDateAnnonceLPP())) {
            ligneDecompte.setDateAnnonceLPP(new Date(ligneDecompteSimpleModel.getDateAnnonceLPP()));
        }

        Decompte decompte = new Decompte();
        decompte.setId(ligneDecompteSimpleModel.getIdDecompte());
        ligneDecompte.setDecompte(decompte);

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId(ligneDecompteSimpleModel.getIdPosteTravail());
        ligneDecompte.setPosteTravail(posteTravail);

        ligneDecompte.setRemarque(ligneDecompteSimpleModel.getRemarque());

        ligneDecompte.setaTraiter(ligneDecompteSimpleModel.getToTreat());
        ligneDecompte.setCorrelationId(ligneDecompteSimpleModel.getCorrelationId());
        ligneDecompte.setLineCorrelationId(ligneDecompteSimpleModel.getLineCorrelationId());
        ligneDecompte.setPosteCorrelationId(ligneDecompteSimpleModel.getPosteCorrelationId());
        ligneDecompte.setTauxSaisieEbu(ligneDecompteSimpleModel.getTauxSaisieEbu());

        if (ligneDecompteSimpleModel.getMntAbsencesJustifiees() != null
                && !JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getMntAbsencesJustifiees())) {
            ligneDecompte.setAbsencesJustifiees(new Montant(ligneDecompteSimpleModel.getMntAbsencesJustifiees()));
        }

        if (ligneDecompteSimpleModel.getMntAPGComplementaireSM() != null
                && !JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getMntAPGComplementaireSM())) {
            ligneDecompte.setApgComplementaireSM(new Montant(ligneDecompteSimpleModel.getMntAPGComplementaireSM()));
        }

        if (ligneDecompteSimpleModel.getMntGratifications() != null
                && !JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getMntGratifications())) {
            ligneDecompte.setGratifications(new Montant(ligneDecompteSimpleModel.getMntGratifications()));
        }

        if (ligneDecompteSimpleModel.getMntVacancesFeries() != null
                && !JadeStringUtil.isEmpty(ligneDecompteSimpleModel.getMntVacancesFeries())) {
            ligneDecompte.setVacancesFeries(new Montant(ligneDecompteSimpleModel.getMntVacancesFeries()));
        }

        if (ligneDecompteSimpleModel.getStatus() != null
                && !JadeStringUtil.isBlankOrZero(ligneDecompteSimpleModel.getStatus())) {
            ligneDecompte.setStatus(StatusLine.fromValue(ligneDecompteSimpleModel.getStatus()));
        }

        return ligneDecompte;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new LigneDecompteSearchSimpleModel();
    }
}
