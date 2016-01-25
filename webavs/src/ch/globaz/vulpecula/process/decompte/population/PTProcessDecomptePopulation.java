package ch.globaz.vulpecula.process.decompte.population;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationCheckable;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.domain.constants.Constants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteInfo;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteProperty;

/**
 * @author jpa
 * 
 *         <pre>
 * 		   Cette classe est charg�e de remplir la population du process. A savoir aller rechercher les affili�s
 *         correspondants aux crit�res de recherches du processus. 2 cas de figure sont possible, d�compte de type
 *         p�riodique ou sp�cial. 
 *         
 *         Dans le cas d'un d�compte sp�cial, on va rechercher sp�cifiquement un et un seul
 *         affili�, afin de cr�� un d�compte, dans ce cas les param�tres sont les suivants : 
 *         -Date du d�compte : date qui va figurer sur le d�compte 
 *         -Num. affili� : num�ro de l'affili� concern� 
 *         -P�riode salaire : mois concern�es par le d�compte
 *         
 *         Dans le cas d'un d�compte p�riodique, les param�tres sont les suivants :
 *         -Date du d�compte : date qui va figurer sur le d�compte
 *         -Conventions : il est possible de prendre les affili�s concern�s que par une convention
 *         -Num. affili� : il est �galement possible de cr�er un d�compte pour un seul affili�
 *         -P�riode salaire : mois concern�es par le d�compte
 *         
 *         D'apr�s ces crit�res le programme va rechercher les affili�s concern�s.
 *         Le processus se d�coupe ensuite par tranches de 1 mois.
 *         Pour chaque mois, on regarde les cotisations concern�es et donc la p�ridodicit�, 
 *         afin de voir si l'affili� est compris pour ce d�compte
 *         
 *         Pour janvier,f�vrier,avril,mai,juillet,ao�t,octobre, novembre que les mensuels.
 *         Pour mars, juin, septembre et d�cembre, on regarde en plus les trimestriels
 *         Pour d�cembre il faut �galement tenir compte des annuels
 * </pre>
 */
public class PTProcessDecomptePopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PTProcessDecompteProperty>,
        JadeProcessPopulationCheckable<PTProcessDecompteProperty> {

    private static final String PREMIER_JOUR_MOIS = "01.";

    public static final String PERIODICITE_MENSUELLE_TRIMESTRIELLE = "MENSUELLE_TRIMESTRIELLE";
    public static final String PERIODICITE_ANNUELLE = "ANNUELLE";

    private Map<PTProcessDecompteProperty, String> properties = null;
    private PTProcessDecompteInfo decompteInfo;

    private EmployeurService employeurService;
    private TaxationOfficeService taxationOfficeService;

    public PTProcessDecomptePopulation() {
        // On ne peut pas injecter le service car le framework ne g�re qu'un
        // constructeur par d�faut et il n'est pas possible d'appeler un setter
        employeurService = VulpeculaServiceLocator.getEmployeurService();
        taxationOfficeService = VulpeculaServiceLocator.getTaxationOfficeService();
    }

    @Override
    public void checker(final Map<PTProcessDecompteProperty, String> map) throws JadePersistenceException,
            JadeApplicationException {
        if (checkMandatory(map)) {
            checkIntegrity(map, false);
        }
    }

    private void checkIntegrity(final Map<? extends Enum<?>, String> map, final boolean errorPeriode)
            throws JadeNoBusinessLogSessionError {
        if (isMensuelleTrimestrielle(map)) {
            String datePeriodeDe = map.get(PTProcessDecompteProperty.DATE_PERIODE_DE);
            String dateperiodeA = map.get(PTProcessDecompteProperty.DATE_PERIODE_A);
            PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle(datePeriodeDe, dateperiodeA);

            checkPeriodeInferieureOuEgaleA3Mois(periodeMensuelle);
            checkPeriodeMensuelleOuTrimestrielle(periodeMensuelle);
        }
    }

    private boolean checkMandatory(final Map<? extends Enum<?>, String> map) {
        if (JadeStringUtil.isEmpty(map.get(PTProcessDecompteProperty.DATE_DECOMPTE))) {
            JadeThread.logError(getClass().getName(), "vulpecula.decompte.typedecompte.mandatory");
            return false;
        }

        if (JadeStringUtil.isEmpty(map.get(PTProcessDecompteProperty.DATE_DECOMPTE))) {
            JadeThread.logError(getClass().getName(), "vulpecula.decompte.datedecompte.mandatory");
            return false;
        }

        String periodicite = map.get(PTProcessDecompteProperty.PERIODICITE);
        if (JadeStringUtil.isEmpty(periodicite)) {
            JadeThread.logError(getClass().getName(), "vulpecula.decompte.periodicite.mandatory");
            return false;
        }

        if (isMensuelleTrimestrielle(map)) {
            if (JadeStringUtil.isEmpty(map.get(PTProcessDecompteProperty.DATE_PERIODE_DE))) {
                JadeThread.logError(getClass().getName(), "vulpecula.decompte.periodede.mandatory");
                return false;
            }
            if (JadeStringUtil.isEmpty(map.get(PTProcessDecompteProperty.DATE_PERIODE_A))) {
                JadeThread.logError(getClass().getName(), "vulpecula.decompte.periodea.mandatory");
                return false;
            }

            return controleDateFinSuperieureDateDebut(map);
        } else if (isAnnuelle(map)) {
            if (JadeStringUtil.isEmpty(map.get(PTProcessDecompteProperty.ANNEE))) {
                JadeThread.logError(getClass().getName(), "vulpecula.decompte.annee.mandatory");
                return false;
            }
        }

        return true;
    }

    private void checkPeriodeInferieureOuEgaleA3Mois(final PeriodeMensuelle periodeMensuelle)
            throws JadeNoBusinessLogSessionError {
        if (periodeMensuelle.getNombreMois() > 3) {
            JadeThread.logError(getClass().getName(), "vulpecula.decompte.periodeTropLongue");
        }
    }

    private void checkPeriodeMensuelleOuTrimestrielle(final PeriodeMensuelle periodeMensuelle)
            throws JadeNoBusinessLogSessionError {
        if (!periodeMensuelle.isMensuelle() && !periodeMensuelle.isTrimestriel()) {
            JadeThread.logError(this.getClass().getName(), "vulpecula.decompte.periodeNonTrimestriel");
        }
    }

    private boolean isAnnuelle(final Map<? extends Enum<?>, String> map) {
        String periodicite = map.get(PTProcessDecompteProperty.PERIODICITE);
        if (PERIODICITE_ANNUELLE.equals(periodicite)) {
            return true;
        }
        return false;
    }

    private boolean isMensuelleTrimestrielle(final Map<? extends Enum<?>, String> map) {
        String periodicite = map.get(PTProcessDecompteProperty.PERIODICITE);
        if (PERIODICITE_MENSUELLE_TRIMESTRIELLE.equals(periodicite)) {
            return true;
        }
        return false;
    }

    private boolean controleDateFinSuperieureDateDebut(final Map<? extends Enum<?>, String> map) {
        String datePeriodeDeString = PTProcessDecomptePopulation.PREMIER_JOUR_MOIS
                + map.get(PTProcessDecompteProperty.DATE_PERIODE_DE);
        String datePeriodeAString = PTProcessDecomptePopulation.PREMIER_JOUR_MOIS
                + map.get(PTProcessDecompteProperty.DATE_PERIODE_A);

        Date datePeriodeDe = new Date(datePeriodeDeString);
        Date datePeriodeA = new Date(datePeriodeAString);
        if (!(datePeriodeDe.before(datePeriodeA) || datePeriodeDe.equals(datePeriodeA))) {
            JadeThread.logError(getClass().getName(), "vulpecula.decompte.erreurPeriode");
            return false;
        }
        return true;
    }

    /**
     * Cr�ation des entit�s qui seront stock�es en base de donn�es.
     * 
     * @param employeurs
     *            Employeurs � convertir en entit�s
     * @param moisCourant
     *            Mois courant utilis� pour la description
     * @return Liste de {@link JadeProcessEntity}
     */
    private List<JadeProcessEntity> createEntities(final List<Employeur> employeurs) {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        for (Employeur employeur : employeurs) {
            JadeProcessEntity entite = new JadeProcessEntity();
            entite.setValue1(decompteInfo.serialize());
            entite.setIdRef(String.valueOf(employeur.getId()));
            entite.setDescription(employeur.getNomConvention() + " " + employeur.getAffilieNumero() + " "
                    + employeur.getRaisonSociale());
            entites.add(entite);
        }
        return entites;
    }

    @Override
    public String getBusinessKey() {
        // La cl� n'est pas g�r�e, il sera donc possible de relancer un process
        // avec les m�mes propri�t�s
        return null;
    }

    @Override
    public Class<PTProcessDecompteProperty> getEnumForProperties() {
        return PTProcessDecompteProperty.class;
    }

    @Override
    public String getParametersForUrl(final JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        // TODO ajouter l'idPoste de travail, afin d'afficher le poste de
        // travail depuis une entit�
        return null;
    }

    private List<JadeProcessEntity> rechercheAndCreateEntities(final String idConvention, final String idEmployeur,
            final Collection<String> inPeriodicite, final Date dateDebut, final Date dateFin) {
        List<Employeur> employeurs = rechercheCas(idConvention, idEmployeur, inPeriodicite, dateDebut, dateFin);
        // Dans le cas o� aucune entit� est pr�sente, on lance une exception afin d'interrompre le processus
        if (employeurs.isEmpty()) {
            String erreur = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PROCESS_DECOMPTE_AUCUN_EMPLOYEUR");
            throw new java.lang.IllegalArgumentException(erreur);
        }
        employeurs = removeEmployeursSansPostes(employeurs, dateDebut, dateFin);
        employeurs = removeEmployeursAvecParticulariteSansPersonnel(employeurs, dateDebut);

        // On retire les employeurs qui poss�dent des TO actives pour la p�riode
        if (decompteInfo.getType().equals(TypeDecompte.PERIODIQUE)) {
            employeurs = removeEmployeurAvecTO(employeurs, dateDebut);
        }
        if (employeurs.isEmpty()) {
            String erreur = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "JSP_PROCESS_DECOMPTE_AUCUN_POSTE_ACTIF");
            throw new java.lang.IllegalArgumentException(erreur);
        }
        return createEntities(employeurs);
    }

    private List<Employeur> removeEmployeursAvecParticulariteSansPersonnel(List<Employeur> employeurs, Date dateDebut) {
        return employeurService.getEmployeursSansParticularite(employeurs, dateDebut);
    }

    private List<Employeur> removeEmployeursSansPostes(final List<Employeur> employeurs, final Date dateDebut,
            final Date dateFin) {
        return employeurService.getEmployeursActifsAvecPostes(employeurs, dateDebut, dateFin);
    }

    private List<Employeur> removeEmployeurAvecTO(final List<Employeur> employeurs, final Date dateDebut) {
        List<Employeur> employeursSansTO = new ArrayList<Employeur>();
        for (Employeur employeur : employeurs) {
            if (!taxationOfficeService.hasTO(employeur, dateDebut)) {
                employeursSansTO.add(employeur);
            }
        }
        return employeursSansTO;
    }

    /**
     * Appel du repository afin de rechercher les cas correspondant aux crit�res
     * pass�s en param�tre.
     * 
     * @param idConvention
     *            id de la convention
     * @param inPeriodicite
     *            Collection de types de periodicite
     * @param dateDebut
     *            date de d�but � prendre en compte
     * @param dateFin
     *            date de fin � prendre en compte
     * @return Liste d'employeurs
     */
    private List<Employeur> rechercheCas(final String idConvention, final String idEmployeur,
            final Collection<String> inPeriodicite, final Date dateDebut, final Date dateFin) {
        if (idConvention != null) {
            return employeurService.findByIdConventionInAnnee(idConvention, dateDebut, dateFin, inPeriodicite);
        } else {
            return employeurService.findByIdAffilie(idEmployeur, dateDebut, dateFin, inPeriodicite);
        }
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();

        String idEmployeur = getEmployeurNumero();
        // On set les param�tres de la clause where
        String idConvention = getConvention();
        if (JadeStringUtil.isEmpty(idConvention)) {
            idConvention = null;
        }

        Date dateDebut = getDatePeriodeDe();
        Date dateFin = getDatePeriodeA();

        entites.addAll(rechercheAndCreateEntities(idConvention, idEmployeur, getPeriodicites(dateFin), dateDebut,
                dateFin));
        return entites;
    }

    /**
     * Retourne les p�riodicit�s en fonction des param�tres saisis
     * 
     * @return Liste de p�riodicit�s
     */
    List<String> getPeriodicites(Date dateFin) {
        ArrayList<String> periodicites = new ArrayList<String>();

        if (isComplementaire() && isAnnuelle()) {
            periodicites.add(Constants.PERIODICITE_TRIMESTRIELLE);
            periodicites.add(Constants.PERIODICITE_MENSUELLE);
            periodicites.add(Constants.PERIODICITE_ANNUELLE);
        } else if (isPeriodique() && isAnnuelle()) {
            periodicites.add(Constants.PERIODICITE_ANNUELLE);
        } else if (isMensuelleTrimestrielle()) {

            periodicites.add(Constants.PERIODICITE_MENSUELLE);
            if (dateFin.isMoisTrimestriel()) {
                periodicites.add(Constants.PERIODICITE_TRIMESTRIELLE);
            }
        }
        return periodicites;
    }

    private boolean isPeriodique() {
        return getTypeDecompte().isPeriodique();
    }

    private boolean isComplementaire() {
        return getTypeDecompte().isComplementaire();
    }

    @Override
    public void setProperties(final Map<PTProcessDecompteProperty, String> hashMap) {
        properties = hashMap;

        TypeDecompte typeDecompte = getTypeDecompte();
        String periodicite = getPeriodicite();
        int annee = getAnnee();
        Date periodeDebut = getDatePeriodeDe();
        Date periodeFin = getDatePeriodeA();
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle(periodeDebut, periodeFin);
        Date dateEtablissement = getDateDecompte();

        decompteInfo = new PTProcessDecompteInfo(typeDecompte, periodeMensuelle, dateEtablissement, annee, periodicite);
    }

    private String getEmployeurNumero() {
        return properties.get(PTProcessDecompteProperty.employeurNumero);
    }

    private Date getDatePeriodeDe() {
        if (isAnnuelle()) {
            return Date.getFirstDayOfYear(getAnnee());
        }
        return new Date(properties.get(PTProcessDecompteProperty.DATE_PERIODE_DE));
    }

    private Date getDatePeriodeA() {
        if (isAnnuelle()) {
            return Date.getLastDayOfYear(getAnnee());
        }
        return new Date(properties.get(PTProcessDecompteProperty.DATE_PERIODE_A));
    }

    private String getConvention() {
        return properties.get(PTProcessDecompteProperty.CONVENTIONS);
    }

    private TypeDecompte getTypeDecompte() {
        return TypeDecompte.fromValue(properties.get(PTProcessDecompteProperty.TYPE_DECOMPTE));
    }

    private Date getDateDecompte() {
        return new Date(properties.get(PTProcessDecompteProperty.DATE_DECOMPTE));
    }

    private int getAnnee() {
        return Integer.valueOf(properties.get(PTProcessDecompteProperty.ANNEE));
    }

    private String getPeriodicite() {
        return properties.get(PTProcessDecompteProperty.PERIODICITE);
    }

    private boolean isAnnuelle() {
        return PERIODICITE_ANNUELLE.equals(getPeriodicite());
    }

    private boolean isMensuelleTrimestrielle() {
        return PERIODICITE_MENSUELLE_TRIMESTRIELLE.equals(getPeriodicite());
    }
}
