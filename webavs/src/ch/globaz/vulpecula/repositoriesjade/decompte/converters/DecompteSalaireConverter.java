package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSalaireAAnnoncerComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
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
        if (ligneDecompte.getTauxContribuableForCaissesSocialesAsValue() != null) {
            ligneDecompteSimpleModel.setTauxContribuable(String.valueOf(ligneDecompte
                    .getTauxContribuableForCaissesSocialesAsValue()));
        }
        if (ligneDecompte.getDateAnnonceLPP() != null) {
            ligneDecompteSimpleModel.setDateAnnonceLPP(ligneDecompte.getDateAnnonceLPP().getSwissValue());
        }
        ligneDecompteSimpleModel.setSpy(ligneDecompte.getSpy());

        return ligneDecompteSimpleModel;
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
        decompteSalaire.setId(decompteSalaireComplexModel.getId());
        decompteSalaire.setPeriode(new Periode(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getPeriodeDebut(), decompteSalaireComplexModel.getLigneDecompteSimpleModel().getPeriodeFin()));
        decompteSalaire.setSalaireTotal(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                .getSalaireTotal()));
        decompteSalaire.setSpy(decompteSalaireComplexModel.getSpy());
        decompteSalaire.setPosteTravail(posteTravail);
        decompteSalaire.setDecompte(decompte);
        if (decompteSalaireComplexModel.getLigneDecompteSimpleModel() != null
                && !JadeStringUtil.isEmpty(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                        .getMontantFranchise())) {
            decompteSalaire.setMontantFranchise(new Montant(decompteSalaireComplexModel.getLigneDecompteSimpleModel()
                    .getMontantFranchise()));
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

        Decompte decompte = new Decompte();
        decompte.setId(ligneDecompteSimpleModel.getIdDecompte());
        ligneDecompte.setDecompte(decompte);

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId(ligneDecompteSimpleModel.getIdPosteTravail());
        ligneDecompte.setPosteTravail(posteTravail);

        return ligneDecompte;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new LigneDecompteSearchSimpleModel();
    }
}
