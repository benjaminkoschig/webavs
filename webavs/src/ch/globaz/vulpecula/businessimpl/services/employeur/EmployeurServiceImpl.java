package ch.globaz.vulpecula.businessimpl.services.employeur;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ch.globaz.naos.business.model.ParticulariteSearchSimpleModel;
import ch.globaz.naos.business.model.ParticulariteSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurParticulariteComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurParticulariteSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.ParticulariteConverter;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.EmployeurConverter;
import ch.globaz.vulpecula.util.DBUtil;

/**
 * @author Arnaud Geiser (AGE) | Créé le 14 mars 2014
 * 
 */
public class EmployeurServiceImpl implements EmployeurService {
    private EmployeurRepository employeurRepository;
    private PosteTravailService posteTravailService;

    public EmployeurServiceImpl(final EmployeurRepository employeurRepository,
            final PosteTravailService posteTravailService) {
        this.employeurRepository = employeurRepository;
        this.posteTravailService = posteTravailService;
    }

    @Override
    public List<Employeur> findByIdConvention(final String idConvention, final Date dateDebut, final Date dateFin,
            final Collection<String> inPeriodicite) {
        return employeurRepository.findByIdConvention(idConvention, dateDebut, dateFin, inPeriodicite);
    }

    @Override
    public List<Employeur> findByIdConventionInAnnee(String idConvention, final Date dateDebut, final Date dateFin,
            Collection<String> inPeriodicite) {
        List<Employeur> employeurs = employeurRepository.findByIdConvention(idConvention, inPeriodicite);
        return filterEmployeursActifs(employeurs, dateDebut, dateFin);
    }

    @Override
    public List<Employeur> findByIdAffilie(final String idAffilie, final Date dateDebut, final Date dateFin,
            final Collection<String> inPeriodicite) {
        return employeurRepository.findByIdAffilie(idAffilie, dateDebut, dateFin, inPeriodicite);
    }

    @Override
    public List<Employeur> findEmployeurActif(final Date periodeDebut, final Date periodeFin) {
        // Passe 2x la périodeFin à la fonction findByIdAffilie afin que tous les tests pour
        // récupérer les employeur actif soient corrects
        List<Employeur> employeurs = findByIdAffilie(null, periodeFin, periodeFin, null);
        return filterEmployeursActifs(employeurs, periodeDebut, periodeFin);
    }

    private List<Employeur> filterEmployeursActifs(List<Employeur> employeurs, Date dateDebut, Date dateFin) {
        List<Employeur> employeurActifs = new ArrayList<Employeur>();

        Periode periodeDemande = new Periode(dateDebut, dateFin);

        // Tri la liste retourné par la fonction findByIdAffilie afin qu'il ne reste que
        // les employeurs qui ont une période d'activité dans la période demandé
        for (Employeur empl : employeurs) {
            if (empl.isActif(periodeDemande)) {
                employeurActifs.add(empl);
            }
        }

        return employeurActifs;
    }

    @Override
    public boolean hasPosteTravailActifs(final Employeur employeur, final Date dateActivite) {
        return posteTravailService.findPostesActifsByIdAffilie(employeur.getId(), dateActivite).size() > 0;
    }

    @Override
    public List<Employeur> getEmployeursActifsAvecPostes(final List<Employeur> employeurs, final Date dateDebut,
            final Date dateFin) {
        List<Employeur> employeursActifs = new ArrayList<Employeur>();

        List<String> ids = new ArrayList<String>();
        for (Employeur employeur : employeurs) {
            ids.add(employeur.getId());
        }

        String sql = "SELECT DISTINCT aff.MAIAFF "
                + "FROM SCHEMA.AFAFFIP aff "
                + "JOIN SCHEMA.PT_POSTES_TRAVAILS postes ON aff.MAIAFF=postes.ID_AFAFFIP "
                + "WHERE (postes.DATE_DEBUT_ACTIVITE<=:dateDebut "
                + "AND (postes.DATE_FIN_ACTIVITE>:dateDebut OR postes.DATE_FIN_ACTIVITE=0) "
                + "AND aff.MADDEB<=:dateDebut "
                + "AND (aff.MADFIN>:dateDebut OR aff.MADFIN=0) OR postes.DATE_DEBUT_ACTIVITE BETWEEN :dateDebut AND :dateFin)"
                + "AND aff.MAIAFF IN (:inClause)";
        sql = sql.replace(":dateFin", dateFin.getValue());
        sql = sql.replace(":dateDebut", dateDebut.getValue());

        List<String> validIds = DBUtil.executeQuery(ids, sql, getClass());

        for (Employeur employeur : employeurs) {
            if (validIds.contains(employeur.getId())) {
                employeursActifs.add(employeur);
            }
        }

        return employeursActifs;
    }

    public boolean hasPostesActifs(Employeur employeur, final Date dateDebut, Date dateFin) {
        return getEmployeursActifsAvecPostes(Arrays.asList(employeur), dateDebut, dateFin).size() > 0;
    }

    /**
     * retourne tous les employeurs qui n'ont pas de date d'affiliation,
     * charge les adresses et les particularités de type sans personnel
     * Attention, comme il n'est pas possible de faire de jointure avec une constante dans Jade, toute les
     * particularités d'affiliation
     * sont chragés, et ensuite traités en JAVA
     */
    @Override
    public List<Employeur> findByIdConventionNonRadieWithParticulariteSansPersonnel(String idConvention)
            throws JadePersistenceException {
        EmployeurParticulariteSearchComplexModel search = new EmployeurParticulariteSearchComplexModel();
        search.setForIdConvention(idConvention);
        search.setForDateFin("0");
        search.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_EMPLOY);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // HACK impossible de faire une jointure avec une constante, du coup, on retourne tout et on trie en java, après
        // search.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
        JadePersistenceManager.search(search);
        List<Employeur> employeurList = new ArrayList<Employeur>();
        String employeurIdPrecedent = "";
        for (int i = 0; i < search.getSearchResults().length; i++) {
            EmployeurParticulariteComplexModel employeurParticulariteComplexModel = (EmployeurParticulariteComplexModel) search
                    .getSearchResults()[i];
            Employeur employeur = EmployeurConverter.getInstance().convertToDomain(employeurParticulariteComplexModel);
            if (!employeurIdPrecedent.equals(employeur.getId())) {
                employeurIdPrecedent = employeur.getId();
                ParticulariteSearchSimpleModel particularite = new ParticulariteSearchSimpleModel();
                particularite.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
                particularite.setForIdAffiliation(employeur.getId());
                JadePersistenceManager.search(particularite);
                List<Particularite> particularitesList = new ArrayList<Particularite>();
                for (int j = 0; j < particularite.getSearchResults().length; j++) {
                    ParticulariteSimpleModel particulariteModel = (ParticulariteSimpleModel) particularite
                            .getSearchResults()[j];
                    Particularite particulariteDomaine = ParticulariteConverter.convertToDomain(particulariteModel);
                    particularitesList.add(particulariteDomaine);
                }
                // On charge les adresses
                Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository()
                        .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers());
                if (adresse != null) {
                    employeur.setAdressePrincipale(adresse);
                }
                employeur.setParticularites(particularitesList);
                employeurList.add(employeur);
            }
        }
        return employeurList;
    }

    /**
     * retourne tous les employeurs qui n'ont pas de date d'affiliation,
     * charge les adresses et les particularités de type sans personnel
     * Attention, comme il n'est pas possible de faire de jointure avec une constante dans Jade, toute les
     * particularités d'affiliation
     * sont chragés, et ensuite traités en JAVA
     */
    @Override
    public List<Employeur> findByIdConventionNonRadieWithParticulariteSansPersonnelEtActif(String idConvention)
            throws JadePersistenceException {
        EmployeurParticulariteSearchComplexModel search = new EmployeurParticulariteSearchComplexModel();
        search.setForIdConvention(idConvention);
        search.setForDateFin("0");
        search.setForDateDebut(Date.now().getSwissValue());
        search.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_EMPLOY);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // HACK impossible de faire une jointure avec une constante, du coup, on retourne tout et on trie en java, après
        // search.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
        JadePersistenceManager.search(search);
        List<Employeur> employeurList = new ArrayList<Employeur>();
        String employeurIdPrecedent = "";
        for (int i = 0; i < search.getSearchResults().length; i++) {
            EmployeurParticulariteComplexModel employeurParticulariteComplexModel = (EmployeurParticulariteComplexModel) search
                    .getSearchResults()[i];
            Employeur employeur = EmployeurConverter.getInstance().convertToDomain(employeurParticulariteComplexModel);
            if (!employeurIdPrecedent.equals(employeur.getId())) {
                employeurIdPrecedent = employeur.getId();
                ParticulariteSearchSimpleModel particularite = new ParticulariteSearchSimpleModel();
                particularite.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
                particularite.setForIdAffiliation(employeur.getId());
                JadePersistenceManager.search(particularite);
                List<Particularite> particularitesList = new ArrayList<Particularite>();
                for (int j = 0; j < particularite.getSearchResults().length; j++) {
                    ParticulariteSimpleModel particulariteModel = (ParticulariteSimpleModel) particularite
                            .getSearchResults()[j];
                    Particularite particulariteDomaine = ParticulariteConverter.convertToDomain(particulariteModel);
                    particularitesList.add(particulariteDomaine);
                }
                // On charge les adresses
                Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository()
                        .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers());
                if (adresse != null) {
                    employeur.setAdressePrincipale(adresse);
                }
                employeur.setParticularites(particularitesList);
                employeurList.add(employeur);
            }
        }
        return employeurList;
    }

    @Override
    public List<Employeur> findEmployeursSansPostesAvecEdition(Date dateDebut, Date dateFin) {
        List<Employeur> employeursInactifsEdition = new ArrayList<Employeur>();
        List<Employeur> employeursEdition = employeurRepository.findEmployeursWithEdition();

        for (Employeur employeur : employeursEdition) {
            Date finAffiliation = null;
            if (!JadeNumericUtil.isEmptyOrZero(employeur.getDateFin())) {
                finAffiliation = new Date(employeur.getDateFin());
            }

            if ((finAffiliation == null || finAffiliation.afterOrEquals(dateFin))
                    && !hasPostesActifs(employeur, dateDebut, dateFin)) {
                employeursInactifsEdition.add(employeur);
            }
        }
        return employeursInactifsEdition;
    }

    @Override
    public String changeTypeFacturation(String idEmployeur, TypeFacturation typeFacturation) {
        if (employeurRepository.hasEntryInDB(idEmployeur)) {
            Employeur employeur = employeurRepository.findById(idEmployeur);
            employeur.setTypeFacturation(typeFacturation);
            employeur = employeurRepository.update(employeur);
            return employeur.getTypeFacturation().getValue();
        } else {
            Employeur employeur = new Employeur();
            employeur.setTypeFacturation(typeFacturation);
            employeur.setId(idEmployeur);
            employeur = employeurRepository.create(employeur);
            return employeur.getTypeFacturation().getValue();
        }
    }

    @Override
    public boolean changeEnvoiBVRSansDecompte(String idEmployeur, boolean activated) {
        if (employeurRepository.hasEntryInDB(idEmployeur)) {
            Employeur employeur = employeurRepository.findById(idEmployeur);
            employeur.setBvr(activated);
            employeur = employeurRepository.update(employeur);
            return employeur.isBvr();
        } else {
            Employeur employeur = new Employeur();
            employeur.setId(idEmployeur);
            employeur.setBvr(activated);
            employeur = employeurRepository.create(employeur);
            return employeur.isBvr();
        }
    }

    @Override
    public boolean changeEditerSansTravailleur(String idEmployeur, boolean activated) {
        if (employeurRepository.hasEntryInDB(idEmployeur)) {
            Employeur employeur = employeurRepository.findById(idEmployeur);
            employeur.setEditerSansTravailleur(activated);
            employeur = employeurRepository.update(employeur);
            return employeur.isEditerSansTravailleur();
        } else {
            Employeur employeur = new Employeur();
            employeur.setId(idEmployeur);
            employeur.setEditerSansTravailleur(activated);
            employeur = employeurRepository.create(employeur);
            return employeur.isEditerSansTravailleur();
        }
    }

    @Override
    public List<Employeur> getEmployeursSansParticularite(List<Employeur> employeurs, Date date) {
        List<Employeur> employeursSansParticularites = new ArrayList<Employeur>();
        for (Employeur e : employeurs) {
            if (!hasParticulariteSansPersonnel(e, date)) {
                employeursSansParticularites.add(e);
            }
        }
        return employeursSansParticularites;
    }

    boolean hasParticulariteSansPersonnel(Employeur employeur, Date date) {
        List<Particularite> particularites = findParticularites(employeur.getId(), date);
        for (Particularite particularite : particularites) {
            Periode periode = new Periode(particularite.getDateDebut(), particularite.getDateFin());
            if (periode.contains(date)) {
                return true;
            }
        }

        return false;
    }

    List<Particularite> findParticularites(String idAffilie, Date date) {
        List<Particularite> particularties = new ArrayList<Particularite>();
        ParticulariteSearchSimpleModel searchModel = new ParticulariteSearchSimpleModel();
        searchModel.setForIdAffiliation(idAffilie);
        searchModel.setForDateDebutLessOrEquals(date.getSwissValue());
        searchModel.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
        RepositoryJade.searchFor(searchModel);

        for (int i = 0; i < searchModel.getSize(); i++) {
            ParticulariteSimpleModel particulariteSimpleModel = (ParticulariteSimpleModel) searchModel
                    .getSearchResults()[i];
            Particularite particularite = new Particularite();
            particularite.setDateDebut(new Date(particulariteSimpleModel.getDateDebut()));
            if (!JadeStringUtil.isEmpty(particulariteSimpleModel.getDateFin())) {
                particularite.setDateFin(new Date(particulariteSimpleModel.getDateFin()));
            }
            particularties.add(particularite);
        }
        return particularties;
    }
}
