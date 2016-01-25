/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.postetravail;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.comparators.PosteTravailActifsInactifsComparator;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailDecompteSalaireExistantSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailDecompteSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailMultipleEmployeurTravailleurMemePeriodeSpecification;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemAlphaComparator;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.DBUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author JPA
 * 
 */
public class PosteTravailServiceImpl implements PosteTravailService {
    private final PosteTravailRepository repository;
    private final AdhesionCotisationPosteTravailRepository adhesionCotisationRepository;
    private final EmployeurRepository employeurRepository;
    private final AffiliationCaisseMaladieRepository affiliationCaisseMaladieRepository;
    private final CotisationService cotisationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PosteTravailServiceImpl.class);

    public PosteTravailServiceImpl(final PosteTravailRepository repository,
            final AdhesionCotisationPosteTravailRepository adhesionCotisationRepository,
            final CotisationService cotisationService,
            AffiliationCaisseMaladieRepository affiliationCaisseMaladieRepository) {
        this.repository = repository;
        this.adhesionCotisationRepository = adhesionCotisationRepository;
        this.cotisationService = cotisationService;
        this.affiliationCaisseMaladieRepository = affiliationCaisseMaladieRepository;
        employeurRepository = VulpeculaRepositoryLocator.getEmployeurRepository();
    }

    @Override
    public PosteTravail create(final PosteTravail posteTravail) throws UnsatisfiedSpecificationException {
        posteTravail.validate();

        // Création de l'employeur dans sa table si inexistant
        if (!employeurRepository.hasEntryInDB(posteTravail.getEmployeur())) {
            employeurRepository.create(posteTravail.getEmployeur());
        }

        // Recherche des postes de travail appartenant à l'employeur et
        // vérification que le poste n'est pas déjà présent pour le même
        // travailleur sur une période se chevauchant.
        List<PosteTravail> postes = repository.findByIdTravailleur(posteTravail.getTravailleur().getId());

        Specification<PosteTravail> spec = new PosteTravailMultipleEmployeurTravailleurMemePeriodeSpecification(postes);
        // Validation de la spécification
        spec.isSatisfiedBy(posteTravail);

        PosteTravail poste = repository.create(posteTravail);

        VulpeculaServiceLocator.getAffiliationCaisseMaladieService().createForPosteTravail(poste);

        return poste;

    }

    @Override
    public PosteTravail update(final PosteTravail posteTravail) throws UnsatisfiedSpecificationException {
        posteTravail.validate();

        // Recherche des postes de travail appartenant à l'employeur et
        // vérification que le poste n'est pas déjà présent pour le même
        // travailleur sur une période se chevauchant.
        List<PosteTravail> postes = repository.findByIdTravailleur(posteTravail.getTravailleur().getId());

        Specification<PosteTravail> spec = new PosteTravailMultipleEmployeurTravailleurMemePeriodeSpecification(postes);
        // Validation de la spécification
        spec.isSatisfiedBy(posteTravail);

        List<DecompteSalaire> decomptes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdPosteTravail(posteTravail.getId());

        Specification<PosteTravail> specDecompte = new PosteTravailDecompteSpecification(decomptes);
        specDecompte.isSatisfiedBy(posteTravail);

        if (posteTravail.getFinActivite() != null) {
            cloturerCaissesMaladies(posteTravail);
        }

        return repository.update(posteTravail);
    }

    void cloturerCaissesMaladies(PosteTravail posteTravail) {
        List<AffiliationCaisseMaladie> affiliations = affiliationCaisseMaladieRepository
                .findByIdPosteTravail(posteTravail.getId());
        for (AffiliationCaisseMaladie affiliation : affiliations) {
            if (affiliation.getMoisFin() == null) {
                affiliation.setMoisFin(posteTravail.getFinActivite());
                affiliationCaisseMaladieRepository.update(affiliation);
            }
        }
    }

    @Override
    public void delete(final PosteTravail posteTravail) throws UnsatisfiedSpecificationException {
        List<DecompteSalaire> decomptes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdPosteTravail(posteTravail.getId());

        Specification<PosteTravail> specDecompte = new PosteTravailDecompteSalaireExistantSpecification(decomptes);
        specDecompte.isSatisfiedBy(posteTravail);

        repository.delete(posteTravail);
    }

    @Override
    public List<PosteTravail> findPostesActifsByIdAffilie(final String idAffilie) {
        return findPostesActifsByIdAffilie(idAffilie, new Date());
    }

    @Override
    public List<PosteTravail> findPostesActifs(Annee annee) {
        List<PosteTravail> postesTravail = repository.findAll();
        return filterPostesActifs(postesTravail, annee.getFirstDayOfYear());
    }

    @Override
    public List<PosteTravail> findPostesActifsByIdAffilie(final String idAffilie, final Date date) {
        List<PosteTravail> postesTravail = repository.findByIdEmployeur(idAffilie);
        return filterPostesActifsMois(postesTravail, date);
    }

    @Override
    public List<PosteTravail> findPostesActifsByIdAffilie(String idAffilie, Date dateDebut, Date dateFin) {
        List<PosteTravail> postesTravail = repository.findByIdEmployeur(idAffilie);
        return filterPostesActifs(postesTravail, dateDebut, dateFin);
    }

    @Override
    public PosteTravail findPlusAncienPosteActif(String idTravailleur, Date date) {
        List<PosteTravail> postesTravail = repository.findByIdTravailleur(idTravailleur);
        List<PosteTravail> postes = filterPostesActifs(postesTravail, date);
        Collections.sort(postes, new Comparator<PosteTravail>() {
            @Override
            public int compare(PosteTravail p1, PosteTravail p2) {
                return p1.getPeriodeActivite().compareTo(p2.getPeriodeActivite());
            }
        });
        if (postes.size() > 0) {
            return postes.get(0);
        }
        return null;
    }

    private List<PosteTravail> filterPostesActifsMois(List<PosteTravail> postesTravail, final Date date) {
        Collection<PosteTravail> filteredList = Collections2.filter(postesTravail, new Predicate<PosteTravail>() {
            @Override
            public boolean apply(PosteTravail poste) {
                return poste.getPeriodeActivite().isActifMois(date);
            }
        });
        return new ArrayList<PosteTravail>(filteredList);
    }

    /**
     * Filtre une liste de postes de travail entre deux dates.
     * Un poste est considéré actif si la période passée en paramètre chevauche (touche) la période du poste de travail.
     * 
     * @param postesTravail Postes de travail à filtrer
     * @param dateDebut Date de début de période
     * @param dateFin Date de fin de période
     * @return Liste de postes de travails
     */
    private List<PosteTravail> filterPostesActifs(List<PosteTravail> postesTravail, final Date dateDebut,
            final Date dateFin) {
        Collection<PosteTravail> filteredList = Collections2.filter(postesTravail, new Predicate<PosteTravail>() {
            @Override
            public boolean apply(PosteTravail poste) {
                return poste.getPeriodeActivite().chevauche(new Periode(dateDebut, dateFin));
            }
        });
        return new ArrayList<PosteTravail>(filteredList);
    }

    private List<PosteTravail> filterPostesActifs(List<PosteTravail> postesTravail, Date date) {
        List<PosteTravail> postesTravailActifs = new ArrayList<PosteTravail>();

        for (PosteTravail posteTravail : postesTravail) {
            if (posteTravail.isActif(date.getLastDayOfMonth())) {
                postesTravailActifs.add(posteTravail);
            }
        }

        return postesTravailActifs;
    }

    @Override
    public boolean hasDecomptesSalaires(String idPosteTravail) {
        String query = "SELECT COUNT(*) AS NB FROM SCHEMA.PT_DECOMPTE_LIGNES WHERE ID_PT_POSTES_TRAVAIL="
                + idPosteTravail;
        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, getClass());
            return result.size() > 0;
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hasDecomptesSalaires(PosteTravail posteTravail) {
        return hasDecomptesSalaires(posteTravail.getId());
    }

    @Override
    public List<Cotisation> getCotisationsForAJ(PosteTravail posteTravail, Date date) {
        if (posteTravail == null) {
            throw new NullPointerException("Le poste de travail passé en paramètre est null");
        }

        List<Cotisation> cotisationsForAJ = new ArrayList<Cotisation>();
        List<TypeAssurance> typeAssurances = new ArrayList<TypeAssurance>();
        typeAssurances.add(TypeAssurance.COTISATION_AVS_AI);
        typeAssurances.add(TypeAssurance.ASSURANCE_CHOMAGE);
        cotisationsForAJ = getCotisationFor(posteTravail, typeAssurances, date);

        return cotisationsForAJ;
    }

    protected List<Cotisation> getCotisationFor(PosteTravail posteTravail, List<TypeAssurance> typeAssurances, Date date) {
        return getCotisationFor(posteTravail, typeAssurances, date, Beneficiaire.EMPLOYEUR);
    }

    protected List<Cotisation> getCotisationFor(PosteTravail posteTravail, List<TypeAssurance> typeAssurances,
            Date date, Beneficiaire beneficiaire) {
        List<Cotisation> cotisationsFor = new ArrayList<Cotisation>();
        List<AdhesionCotisationPosteTravail> adhesionCotisationPosteTravails = adhesionCotisationRepository
                .findByIdPosteTravail(posteTravail.getId(), date);

        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : adhesionCotisationPosteTravails) {
            Cotisation cotisation = adhesionCotisationPosteTravail.getCotisation();

            TypeAssurance typeAssurance = cotisation.getTypeAssurance();
            if (typeAssurances.contains(typeAssurance)) {
                // On recherche le taux de cotisation de l'employeur et on le set dans la cotisation...
                Taux taux = getTauxSelonBeneficaire(date, beneficiaire, posteTravail.getTypeSalaire(), cotisation);

                // On ajoute pas la cotisation s'il est à 0 (taux null)
                // if (taux != 0) {
                cotisation.setTaux(taux);
                cotisationsFor.add(cotisation);
                // }
            }
        }

        return cotisationsFor;
    }

    /**
     * Retourne le taux applicable (valeur employeur, valeur employé) pour une cotisation en fonction du type de salaire
     * et du bénéficiaire.
     * <table border="1">
     * <tr>
     * <td>TypeSalaire
     * <td>Beneficiaire
     * <td>Taux
     * <tr>
     * <td>Horaire
     * <td>Employeur
     * <td>Employe
     * <tr>
     * <td>Horaire
     * <td>Note de crédit
     * <td>Employe
     * <tr>
     * <td>Horaire
     * <td>Travailleur
     * <td>Employe
     * <tr>
     * <td>Mensuel
     * <td>Employeur
     * <td>Employeur
     * <tr>
     * <td>Mensuel
     * <td>Note de crédit
     * <td>Employeur
     * <tr>
     * <td>Mensuel
     * <td>Travailleur
     * <td>Employe
     * 
     * </table>
     * 
     * @param date Date à laquelle déterminer le taux de la cotisation
     * @param beneficiaire Bénéficiaire de la prestation
     * @param typeSalaire Type de salaire du poste de travail
     * @param cotisation Cotisation sur laquelle déterminer le taux
     * @return
     */
    Taux getTauxSelonBeneficaire(Date date, Beneficiaire beneficiaire, TypeSalaire typeSalaire, Cotisation cotisation) {
        TauxAssuranceSimpleModel tauxAssuranceSimpleModel = cotisationService.findTauxForAssurance(
                cotisation.getAssuranceId(), date);
        Taux taux = new Taux(0);
        if (tauxAssuranceSimpleModel != null) {
            if (typeSalaire.equals(TypeSalaire.HEURES)) {
                taux = new Taux(tauxAssuranceSimpleModel.getValeurEmploye());
            } else if (beneficiaire != null && beneficiaire.equals(Beneficiaire.TRAVAILLEUR)) {
                taux = new Taux(tauxAssuranceSimpleModel.getValeurEmploye());
            } else {
                taux = new Taux(tauxAssuranceSimpleModel.getValeurEmployeur());
            }
        }
        return taux;
    }

    @Override
    public List<Cotisation> getCotisationsForAJ(String idPosteTravail, Date date) {
        PosteTravail posteTravail = repository.findById(idPosteTravail);
        return getCotisationsForAJ(posteTravail, date);
    }

    @Override
    public List<Cotisation> getCotisationsElectrciensForCP(PosteTravail posteTravail, Annee annee,
            Beneficiaire beneficiaire) {
        List<Cotisation> cotisationsForCP = new ArrayList<Cotisation>();
        List<TypeAssurance> typeAssurances = new ArrayList<TypeAssurance>();

        typeAssurances.add(TypeAssurance.COTISATION_AVS_AI);
        typeAssurances.add(TypeAssurance.ASSURANCE_CHOMAGE);
        typeAssurances.add(TypeAssurance.COTISATION_AF);
        typeAssurances.add(TypeAssurance.COTISATION_LPP);
        typeAssurances.add(TypeAssurance.COTISATION_RETAVAL);
        typeAssurances.add(TypeAssurance.ASSURANCE_MALADIE);
        typeAssurances.add(TypeAssurance.COTISATION_FFPP_MASSE);

        cotisationsForCP = getCotisationForCP(posteTravail, typeAssurances, annee, beneficiaire);
        return cotisationsForCP;
    }

    protected List<Cotisation> getCotisationForCP(PosteTravail posteTravail, List<TypeAssurance> typeAssurances,
            Annee annee, Beneficiaire beneficiaire) {
        List<Cotisation> cotisationsForCP = new ArrayList<Cotisation>();
        List<AdhesionCotisationPosteTravail> adhesionCotisationPosteTravails = adhesionCotisationRepository
                .findByIdPosteTravail(posteTravail.getId());
        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : adhesionCotisationPosteTravails) {
            if (!adhesionCotisationPosteTravail.isActifIn(annee)) {
                continue;
            }
            Cotisation cotisation = adhesionCotisationPosteTravail.getCotisation();

            TypeAssurance typeAssurance = cotisation.getTypeAssurance();
            if (typeAssurances.contains(typeAssurance)) {
                // On recherche le taux de cotisation de l'employeur et on le set dans la cotisation...
                Taux taux = getTauxSelonBeneficaire(annee.getFirstDayOfYear(), beneficiaire,
                        posteTravail.getTypeSalaire(), cotisation);

                cotisation.setTaux(taux);
                cotisationsForCP.add(cotisation);
            }
        }

        return cotisationsForCP;
    }

    @Override
    public int getNumeroCaissePrincipale(String idPosteTravail) {
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        Adhesion adh = VulpeculaRepositoryLocator.getAdhesionRepository()
                .findCaisseMetier(poste.getEmployeur().getId());
        if (adh != null) {
            return Integer.valueOf(adh.getPlanCaisse().getAdministration().getCodeAdministration());
        } else {
            return PosteTravail.CAISSE_METIER_INVALIDE;
        }
    }

    @Override
    public int getIdTiersCaissePrincipale(String idPosteTravail) {
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        Adhesion adh = VulpeculaRepositoryLocator.getAdhesionRepository()
                .findCaisseMetier(poste.getEmployeur().getId());
        if (adh != null) {
            return Integer.valueOf(adh.getPlanCaisse().getAdministration().getIdTiers());
        } else {
            return PosteTravail.CAISSE_METIER_INVALIDE;
        }
    }

    @Override
    public boolean hasCaisseMetier(String idPosteTravail) {
        return getNumeroCaissePrincipale(idPosteTravail) != PosteTravail.CAISSE_METIER_INVALIDE;
    }

    @Override
    public double getNombreHeuresParMois(String idPosteTravail, Date date) {
        int noCaisseMetier = getNumeroCaissePrincipale(idPosteTravail);
        double nombreHeuresParMois = TableParametrage.getInstance().getHeuresTravailMois(noCaisseMetier);
        return new Montant(nombreHeuresParMois).multiply(findTauxOccupation(idPosteTravail, date)).decimal(2)
                .doubleValue();
    }

    @Override
    public double getNombreHeuresParMois(String idPosteTravail, Date dateDebut, Date dateFin) {
        PeriodeMensuelle periode = new PeriodeMensuelle(dateDebut, dateFin);
        int noCaisseMetier = getNumeroCaissePrincipale(idPosteTravail);

        double nombreHeuresParMois = TableParametrage.getInstance().getHeuresTravailMois(noCaisseMetier);
        Montant nombreHeures = Montant.ZERO;
        for (Date date : periode.getMois()) {
            nombreHeures = nombreHeures.add(new Montant(nombreHeuresParMois).multiply(findTauxOccupation(
                    idPosteTravail, date)));

        }
        return nombreHeures.decimal(2).doubleValue();
    }

    @Override
    public double getNombreHeuresParJour(String idPosteTravail, Date date) {
        int noCaisseMetier = getNumeroCaissePrincipale(idPosteTravail);
        return TableParametrage.getInstance().getHeuresTravailJour(noCaisseMetier);
    }

    protected Taux findTauxOccupation(String idPosteTravail, Date date) {
        PosteTravail posteTravail = repository.findById(idPosteTravail);
        posteTravail.setOccupations(VulpeculaRepositoryLocator.getOccupationRepository()
                .findOccupationsByIdPosteTravail(idPosteTravail));

        if (posteTravail.getOccupation(date) == null) {
            return Taux.ZERO();
        } else {
            return posteTravail.getOccupation(date).getTaux();
        }
    }

    @Override
    public List<PosteTravail> findAAnnoncer(Date date) {
        Map<String, PosteTravail> postesSansDoublonsTravailleurs = new HashMap<String, PosteTravail>();
        List<PosteTravail> postes = repository.findAAnnoncer(date, false);
        postes = removeNonEligibles(postes, date);

        for (PosteTravail posteTravail : postes) {
            if (!postesSansDoublonsTravailleurs.containsKey(posteTravail.getIdTravailleur())) {
                postesSansDoublonsTravailleurs.put(posteTravail.getIdTravailleur(), posteTravail);
            } else {
                PosteTravail posteExistant = postesSansDoublonsTravailleurs.get(posteTravail.getIdTravailleur());
                if (!posteExistant.getDebutActivite().before(posteTravail.getDebutActivite())) {
                    postesSansDoublonsTravailleurs.put(posteTravail.getIdTravailleur(), posteTravail);
                }
            }
        }

        return new ArrayList<PosteTravail>(postesSansDoublonsTravailleurs.values());
    }

    private List<PosteTravail> removeNonEligibles(List<PosteTravail> postes, Date date) {
        List<PosteTravail> eligibles = new ArrayList<PosteTravail>();
        for (PosteTravail poste : postes) {
            if (poste.hasMoreThanOrEquals18Ans(date) && poste.cotiseAVS(date)) {
                eligibles.add(poste);
            }
        }
        return eligibles;
    }

    @Override
    public List<PosteTravail> getPostesTravailsWithDroitsAJ(String idTravailleur) {
        return getPostesTravailWithDroitsX(idTravailleur, TypePrestation.ABSENCES_JUSTIFIEES);
    }

    @Override
    public List<PosteTravail> getPostesTravailsWithDroitsCP(String idTravailleur) {
        return getPostesTravailWithDroitsX(idTravailleur, TypePrestation.CONGES_PAYES);
    }

    @Override
    public List<PosteTravail> getPostesTravailsWithDroitsSM(String idTravailleur) {
        return getPostesTravailWithDroitsX(idTravailleur, TypePrestation.SERVICES_MILITAIRE);
    }

    private List<PosteTravail> getPostesTravailWithDroitsX(String idTravailleur, TypePrestation typePrestation) {
        List<PosteTravail> postesDroits = new ArrayList<PosteTravail>();
        // Chargement des postes avec dépendances
        List<PosteTravail> postes = repository.findByIdTravailleurWithDependencies(idTravailleur);
        // On enlève les postes qui n'ont pas droit aux AJ
        for (PosteTravail posteTravail : postes) {
            int idCaisseMetier = getNumeroCaissePrincipale(posteTravail.getId());
            boolean hasDroit = false;
            switch (typePrestation) {
                case ABSENCES_JUSTIFIEES:
                    hasDroit = posteTravail.hasDroitAJ(idCaisseMetier);
                    break;
                case CONGES_PAYES:
                    hasDroit = posteTravail.hasDroitCP(idCaisseMetier);
                    break;
                case SERVICES_MILITAIRE:
                    hasDroit = posteTravail.hasDroitSM(idCaisseMetier);
                    break;
            }
            if (hasDroit) {
                postesDroits.add(posteTravail);
            }
        }

        Collections.sort(postesDroits, new PosteTravailActifsInactifsComparator());
        return postesDroits;
    }

    @Override
    public boolean hasAssuranceActiveForX(PosteTravail posteTravail, Date dateDebut, Date dateFin,
            TypePrestation typePrestation) {
        String idPosteTravail = posteTravail.getId();
        int caisseMetier = getNumeroCaissePrincipale(idPosteTravail);
        List<TypeAssurance> obligatoires = findPrestationsObligatoiresFor(caisseMetier, typePrestation);
        return hasAssuranceActiveForX(idPosteTravail, dateDebut, dateFin, obligatoires);
    }

    private boolean hasAssuranceActiveForX(String idPosteTravail, Date dateDebut, Date dateFin,
            List<TypeAssurance> obligatoires) {
        List<AdhesionCotisationPosteTravail> adhesions = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findByIdPosteTravail(idPosteTravail, dateDebut);
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            if (adhesion.getPeriode().contains(dateFin) && obligatoires.contains(adhesion.getTypeAssurance())) {
                return true;
            }
        }
        return false;
    }

    private List<TypeAssurance> findPrestationsObligatoiresFor(int noCaisseMetier, TypePrestation typePrestation) {
        TableParametrage table = TableParametrage.getInstance();
        switch (typePrestation) {
            case ABSENCES_JUSTIFIEES:
                return table.getTypeAssuranceobligatoireForAJ(noCaisseMetier);
            case CONGES_PAYES:
                return table.getTypeAssuranceObligatoireForCP(noCaisseMetier);
            case SERVICES_MILITAIRE:
                return table.getTypeAssuranceobligatoireForSM(noCaisseMetier);
        }
        throw new AssertionError();
    }

    @Override
    public List<CodeSystem> getQualificationForConvention(String idConvention) {
        List<CodeSystem> qualificationsValides = new ArrayList<CodeSystem>();
        if (JadeStringUtil.isEmpty(idConvention)) {
            return new ArrayList<CodeSystem>();
        }

        List<Qualification> qualifications = VulpeculaRepositoryLocator.getQualificationRepository()
                .findByIdConvention(idConvention);
        List<CodeSystem> codes = CodeSystemUtil.getCodesSystemesForFamille("PTQUALIFIC");
        for (CodeSystem codeSystem : codes) {
            if (qualifications.contains(Qualification.fromValue(codeSystem.getId()))) {
                qualificationsValides.add(codeSystem);
            }
        }

        Collections.sort(qualificationsValides, new CodeSystemAlphaComparator());
        return qualificationsValides;
    }
}
