package ch.globaz.vulpecula.domain.models.parametrage;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.absencejustifiee.LienParente;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.parametrage.Deces.Parente;
import ch.globaz.vulpecula.domain.models.parametrage.Naissances.Naissance;
import ch.globaz.vulpecula.domain.models.parametrage.Predicats.Predicat;
import ch.globaz.vulpecula.domain.models.parametrage.ServiceMilitaire.Genre;
import ch.globaz.vulpecula.domain.models.parametrage.Vacances.Vacance;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;

/**
 * Table param�trage pour les prestations accord�es (Absences justifi�es, Cong�s pay�s, Service militaire)
 */
public class TableParametrage {

    private static final String PRESTATIONS_XML_PATH = "ch/globaz/vulpecula/parametrage/prestations.xml";
    private static final String FIELD_AGE = "age";
    private static final String FIELD_ANNEE_SERVICE = "anneeService";

    private static final String TYPE_LESS = "less";
    private static final String TYPE_GREATER = "greater";
    private static final String TYPE_EQUALS = "equals";
    private static final String TYPE_GREATER_OR_EQUALS = "greaterOrEquals";
    private static final String TYPE_LESS_OR_EQUALS = "lessOrEquals";

    private static final String LIEN_PARENTE_AUTRES = "autres";

    private ParametragePrestation parametragePrestation;

    private static final Logger LOGGER = LoggerFactory.getLogger(TableParametrage.class);
    private static TableParametrage tableParametrage = null;

    private TableParametrage() {
        try {
            loadTable();
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
    }

    public static TableParametrage getInstance() {
        if (tableParametrage == null) {
            tableParametrage = new TableParametrage();
        }
        return tableParametrage;
    }

    private void loadTable() throws JAXBException, URISyntaxException {
        JAXBContext jc = JAXBContext.newInstance(ParametragePrestation.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream configurationFile = getClass().getClassLoader().getResourceAsStream(PRESTATIONS_XML_PATH);
        parametragePrestation = (ParametragePrestation) unmarshaller.unmarshal(configurationFile);
    }

    /**
     * Retourne le nombre de jours accord�s lors d'une absence justifi�e de type mariage pour la caisse m�tier dont l'id
     * est pass� en param�tre.
     * Cette m�thode retournera le nombre de jours standard accord� � un mariage
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours accord� pour une absence de type mariage
     */
    public int getJoursMariage(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getMariage().standard.intValue();
    }

    /**
     * Retourne le nombre de jours accord�s lors d'une absence justifi�e de type mariage des enfants pour la caisse
     * m�tier dont l'id est pass� en param�tre.
     * Cette m�thode retournera 0 si l'attribut n'est pas d�fini par une caisse m�tier.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours accord� pour une absence de type mariage pour les enfants d'un travailleur
     */
    public int getJoursMariageEnfants(int noCaisseMetier) {
        BigInteger nbJoursEnfants = getProfession(noCaisseMetier).getMariage().enfants;
        if (nbJoursEnfants != null) {
            return nbJoursEnfants.intValue();
        } else {
            return 0;
        }
    }

    /**
     * Retourne le nombre de jours accord�s lors d'une absence justifi�e de type inspection pour la caisse m�tier dont
     * l'id est pass� en param�tre.
     * Cette m�thode retournera 0 si l'attribut n'est pas d�fini par une caisse m�tier.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     */
    public double getJoursInspection(int noCaisseMetier) {
        Double nbJours = getProfession(noCaisseMetier).getInspection();
        if (nbJours != null) {
            return nbJours;
        } else {
            return 0;
        }
    }

    /**
     * Retourne le nombre de jours accord�s lors d'une absence justifi�e de type d�c�s dont l'id de la caisse m�tier
     * est pass� en param�tre.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @param lienParenteARechercher Le lien de parent� avec la personne d�c�d�e
     * @return Nombre de jours accord�s
     */
    public int getJoursDeces(int noCaisseMetier, LienParente lienParenteARechercher) {
        List<Parente> parentes = getParentes(noCaisseMetier);
        for (Parente parente : parentes) {
            if (parente.lien.equals(LIEN_PARENTE_AUTRES)) {
                return Integer.valueOf(parente.content);
            }
            LienParente lienParente = LienParente.fromValue(parente.lien);
            if (lienParente.equals(lienParenteARechercher)) {
                return Integer.valueOf(parente.content);
            }
        }
        return 0;
    }

    /**
     * Retourne le nombre de jours accord�s lors d'une absence justifi�e de type naissance pour la caisse m�tier i est
     * pass�e en param�tre.
     * Lorsque l'on acc�de � cette m�thode, il est n�cessaire que SEULE une entr�e soit pr�sente dans le fichier de
     * param�trage. Celle-ci sera prise peu importe la condition d�finie.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours accord� pour une allocation de type naissance
     * @throws IllegalArgumentException lorsqu'il existe plusieurs options possibles et que le nombre de jours ne peut
     *             �tre d�termin�
     */
    public int getJoursNaissance(int noCaisseMetier) {
        List<Naissance> naissances = getNaissances(noCaisseMetier);
        if (naissances.size() == 1) {
            return naissances.get(0).nbjours.intValue();
        } else {
            LOGGER.error("On retrouve plusieurs r�gles concernant la caisse {} pour des naissances", noCaisseMetier);
            throw new IllegalArgumentException("On retrouve plusieurs r�gles concernant la caisse " + noCaisseMetier
                    + " pour des naissances");
        }
    }

    /**
     * Retourne le nombre de jours accord�es lors d'une absence justifi�e de type naissance pour la caisse m�tier pass�e
     * en param�tre.
     * Cette m�thode calcul le nombre de jours en fonction du nombre d'ann�es de services effectu�es par le travailleur.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @param nbAnneesService Nombre de jours de service effectu�es
     * @return Nombre de jours d'absences accord�es
     */
    public int getJoursNaissance(int noCaisseMetier, int nbAnneesService) {
        int nbJours = 0;
        List<Naissance> naissances = getNaissances(noCaisseMetier);
        for (Naissance naissance : naissances) {
            Predicats pred = naissance.getPredicats();

            if (pred == null) {
                int nouveauNombreJours = naissance.nbjours.intValue();
                nbJours = getMax(nbJours, nouveauNombreJours);
            } else {
                List<Predicat> predicats = pred.getPredicat();
                boolean status = checkPredicats(predicats, 0, nbAnneesService);

                if (status) {
                    int nouveauNombreJours = naissance.getNbjours().intValue();
                    nbJours = getMax(nbJours, nouveauNombreJours);
                }
            }
        }
        return nbJours;
    }

    /**
     * Retourne le nombre de jours accord� lors d'une journ�e de recrutement ou d'information.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours accord�
     */
    public double getJoursRecrutementInfo(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getRecrutementInfo();
    }

    /**
     * Retourne le nombre de jours accord� lors d'une journ�e de "liber".
     * 
     * @return Nombre de jours accord�
     */
    public double getJoursLiber(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getLiber();
    }

    /**
     * Retourne le nombre de jours accord� lors d'un d�m�nagement.
     * On prend en compte la propri�t� "pay�" de la balise &lt;demenagement&gt; : Si == "non" alors le nombre de jour
     * sera
     * forc� � 0
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours accord� ou "0" si pay� == non
     */
    public int getJoursDemenagement(int noCaisseMetier) {
        String s_nbJours = getProfession(noCaisseMetier).getDemenagement().content;

        // Si le jour n'est pas pay� par l'employeur (propri�t� "pay�" � "non") alors on force le nombre de jours � 0
        if ("non".equals(getProfession(noCaisseMetier).getDemenagement().getPaye())) {
            s_nbJours = "0";
        }

        return Integer.parseInt(s_nbJours);
    }

    /**
     * Retourne le nombre de jours f�ri�s accord�.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours f�ri�s
     */
    public int getJoursFeries(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getJoursFeries().intValue();
    }

    /**
     * Retourne le nombre de jours de formation accord�.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return Nombre de jours de formation
     */
    public int getJoursFormation(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getFormationProf().intValue();
    }

    /**
     * Retourne le nombre d'heure de travail � effectuer par jour pour la caisse m�tier dont l'id est pass� en
     * param�tre.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return double repr�sentant le nombre d'heures de travail par jour
     */
    public double getHeuresTravailJour(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getHoraire().jour;
    }

    /**
     * Retourne le nombre d'heure de travail � effectuer par semaine pour la caisse m�tier dont l'id est pass� en
     * param�tre.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return double repr�sentant le nombre d'heures de travail par semaine
     */
    public double getHeuresTravailSemaine(int noCaisseMetier) {
        try {
            return getProfession(noCaisseMetier).getHoraire().semaine;
        } catch (IllegalArgumentException ex) {
            return 0;
        }
    }

    /**
     * Retourne le nombre d'heure de travail � effectuer par semaine pour la caisse m�tier dont l'id est pass� en
     * param�tre.
     * 
     * @param noCaisseMetier Integer repr�sentant l'id de la caisse m�tier
     * @return double repr�sentant le nombre d'heures de travail par mois
     */
    public double getHeuresTravailMois(int noCaisseMetier) {
        try {
            return getProfession(noCaisseMetier).getHoraire().mois;
        } catch (IllegalArgumentException ex) {
            return 0;
        }
    }

    /**
     * Retourne le nombre de jours de vacances accord�es par rapport � la caisse m�tier et l'�ge de la personne.
     * Le nombre d'ann�e de service consid�r� sera de 0.
     * 
     * @param noCaisseMetier No� de caisse m�tier
     * @param age Age de la personne
     * @return Nombre de jours accord�
     */
    public int getJoursVacances(int noCaisseMetier, int age) {
        return getJoursVacances(noCaisseMetier, age, 0);
    }

    /**
     * Retourne le nombre de jours de vacances accord�es par rapport � la caisse m�tier, l'�ge et le nombre d'ann�es de
     * service d'un employ�.
     * 
     * @param noCaisseMetier No� de caisse m�tier
     * @param age Age de la personne
     * @param anneeService Nombre d'ann�e de services
     * @return Nombre de jours accord�
     */
    private int getJoursVacances(int noCaisseMetier, int age, int anneeService) {
        return getVacance(noCaisseMetier, age, anneeService).nbjours.intValue();
    }

    /**
     * Retourne le couple nbJours et taux pour les vacances pour la caisse m�tier, l'�ge et le nombre d'ann�e de service
     * pass�s en param�tres.
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param age Age de la personne
     * @param anneeService Nombre d'ann�es de service
     * @return NbJours et taux
     */
    public NbJoursTaux getNbJoursTaux(int noCaisseMetier, int age) {
        Vacance vacance = getVacance(noCaisseMetier, age, 0);
        return new NbJoursTaux(vacance.nbjours.intValue(), vacance.taux);
    }

    /**
     * Retourne le couple nbJours et taux pour les vacances pour la caisse m�tier, l'�ge et le nombre d'ann�e de service
     * pass�s en param�tres.
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param age Age de la personne
     * @param anneeService Nombre d'ann�es de service
     * @return NbJours et taux
     */
    public NbJoursTaux getNbJoursTaux(int noCaisseMetier, int age, int anneeService) {
        Vacance vacance = getVacance(noCaisseMetier, age, anneeService);
        return new NbJoursTaux(vacance.nbjours.intValue(), vacance.taux);
    }

    /**
     * Retourne la gratification que la caisse m�tier octroie dans le cas de service militaire.
     * 
     * @return double repr�sentant un taux de gratification
     */
    public double getGratification(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getGratification();
    }

    /**
     * Retourne le taux de couverture APG que la caisse m�tier octroie.
     * 
     * @param noCaisseMetier Num�ro de la caisse m�tier
     * @param genre Genre de prestation service militaire
     * @return Taux de couverture octroy�
     */
    public Taux getCouvertureAPG(int noCaisseMetier, GenreSM genre) {
        return new Taux(getGenreAPG(noCaisseMetier, genre).couvertureAPG.intValue());
    }

    public ConfigurationSM getConfigurationSM(int noCaisseMetier, GenreSM genreToSearch, int age) {
        return getConfigurationSM(noCaisseMetier, genreToSearch, age, 0);
    }

    private ConfigurationSM getConfigurationSM(int noCaisseMetier, GenreSM genreToSearch, int age, int anneesService) {
        double tauxCP = getTauxVacances(noCaisseMetier, getJoursVacances(noCaisseMetier, age, anneesService));
        double gratification = getGratification(noCaisseMetier);
        boolean isCPForce0 = isTauxCPForce0(noCaisseMetier);
        Genre config = getGenreAPG(noCaisseMetier, genreToSearch);

        int minJours = 0;
        if (config.getMinJours() != null) {
            minJours = config.getMinJours().intValue();
        }

        int maxJours = 0;
        if (config.getMaxJours() != null) {
            maxJours = config.getMaxJours().intValue();
        }

        int couvertureAPG = 0;
        if (config.getCouvertureAPG() != null) {
            couvertureAPG = config.getCouvertureAPG().intValue();
        }

        if (isCPForce0) {
            tauxCP = 0;
        }

        return new ConfigurationSM(minJours, maxJours, couvertureAPG, tauxCP, gratification, isCPForce0);
    }

    /**
     * Retourne la configuration relative � un genre de service militaire pour une caisse m�tier.
     * 
     * @param noCaisseMetier Num�ro de la caisse m�tier
     * @param genreToSearch Enum�ration repr�sentant la liste des codes syst�mes relatifs aux genres de service
     *            militaire
     * @return Genre de service militaire
     */
    public Genre getGenreAPG(int noCaisseMetier, GenreSM genreToSearch) {
        ServiceMilitaire serviceMilitaire = tableParametrage.getProfession(noCaisseMetier).getServiceMilitaire();
        for (Genre genre : serviceMilitaire.genre) {
            String codeSysteme = String.valueOf(genre.getCodeSysteme());
            if (genreToSearch.getValue().equals(codeSysteme)) {
                return genre;
            }
        }
        throw new IllegalStateException("Pas de configuration pour ce genre de prestation");
    }

    /**
     * Retourne la "balise" vacance remplissant les conditions caisseMetier, age et anneeDeService.
     * 
     * @param noCaisseMetier No� de caisse m�tier
     * @param age Age de la personne
     * @param anneeService Nombre d'ann�es de service
     * @return La balise "Vacance" remplissant les conditions, ou null si aucun r�sultat n'est trouv�
     */
    private Vacance getVacance(int noCaisseMetier, int age, int anneeService) {
        Vacance vacanceToApply = null;
        List<Vacance> vacances = getVacances(noCaisseMetier);
        for (Vacance vacance : vacances) {
            if (vacance.getPredicats() == null) {
                vacanceToApply = getGreaterVacance(vacanceToApply, vacance);
            } else {
                List<Predicat> predicats = vacance.getPredicats().getPredicat();
                boolean status = checkPredicats(predicats, age, anneeService);

                if (status) {
                    vacanceToApply = getGreaterVacance(vacanceToApply, vacance);
                }
            }
        }
        return vacanceToApply;
    }

    /**
     * Retourne l'objet vacance dont le nombre de jours est le plus �lev�.
     * 
     * @param vacance1 Vacance de base
     * @param vacance2 Vacance � comparer
     * @return L'objet vacance dont le nombre de jours est le plus �lev�. Si vacance1 est null, retourne vacance2.
     */
    private Vacance getGreaterVacance(Vacance vacance1, Vacance vacance2) {
        if (vacance1 == null) {
            return vacance2;
        }

        if (vacance1.nbjours.compareTo(vacance2.nbjours) == 1) {
            return vacance1;
        } else {
            return vacance2;
        }
    }

    /**
     * Retourne le taux de vacances par rapport � un nombre de jours et une caisse sociale.
     * 
     * @param noCaisseMetier No� de la caisse sociale
     * @param nbJours Nombre de jours de vacances
     * @return double repr�sentant le taux pour ce nombre de jours de vacances
     */
    public double getTauxVacances(int noCaisseMetier, int nbJours) {
        List<Vacance> vacances = getVacances(noCaisseMetier);
        for (Vacance vacance : vacances) {
            if (nbJours == vacance.getNbjours().intValue()) {
                return vacance.getTaux();
            }
        }
        LOGGER.error("Il n'existe pas de taux pour le nombre {} de vacances pour la caisse {}", nbJours, noCaisseMetier);
        throw new IllegalArgumentException("Il n'existe pas de taux pour le nombre " + nbJours
                + " de vacances pour la caisse " + noCaisseMetier);
    }

    /**
     * D�termine si la caisse m�tier dispose d'un calcul de cong� pay� par rapport � toutes les cotisations du poste.
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @return true si cotisation requises
     */
    public boolean hasCotisationsCongesPays(int noCaisseMetier) {
        return getProfession(noCaisseMetier).hasCotisationsCongesPayes;
    }

    public boolean hasCotisationsCongesPays(String noCaisseMetier) {
        return hasCotisationsCongesPays(Integer.valueOf(noCaisseMetier));
    }

    /**
     * Retourne si le taux CP a �t� forc� � 0.
     * 
     * @return true si force � 0
     */
    public boolean isTauxCPForce0(int noCaisseMetier) {
        ServiceMilitaire serviceMilitaire = getProfession(noCaisseMetier).getServiceMilitaire();
        if (serviceMilitaire.isTauxCP0() == null) {
            return false;
        }
        return serviceMilitaire.tauxCP0;
    }

    /**
     * Retourne le nombre de jours accord�s dans le cas d'un type de prestation AJ.
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param type Type d'absence justifi�e
     * @param lien Lien de parent� dans le cas d'un deuil
     * @param nbAnneesService Nombre d'ann�es de service au sein de l'entreprise
     * @return Nombre de jours
     */
    public double getNombreJoursPrestationAJ(int noCaisseMetier, TypeAbsenceJustifiee type, LienParente lien,
            int nbAnneesService) {
        switch (type) {
            case DEUIL:
                return getJoursDeces(noCaisseMetier, lien);
            case DEMENAGEMENT:
                return getJoursDemenagement(noCaisseMetier);
            case INFO_RECRUTEMENT:
                return getJoursRecrutementInfo(noCaisseMetier);
            case MARIAGE:
                return getJoursMariage(noCaisseMetier);
            case NAISSANCE:
                return getJoursNaissance(noCaisseMetier, nbAnneesService);
            case LIBERATION:
                return getJoursLiber(noCaisseMetier);
            case MARIAGE_ENFANT:
                return getJoursMariageEnfants(noCaisseMetier);
            case INSPECTION:
                return getJoursInspection(noCaisseMetier);
            default:
                return 0;
        }

    }

    /**
     * Retourne une liste des assurances obligatoires afin de pouvoir disposer d'une prestation absence justifi�e.
     * Il suffit de disposer de l'UNE DE CES ASSURANCES pour avoir droit aux prestations absences justifi�es.
     * 
     * @param noCaisseMetier caisse m�tier sur lequel retrouver les assurances obligatoires
     * @return Liste des assurances
     */
    public List<TypeAssurance> getTypeAssuranceobligatoireForAJ(int noCaisseMetier) {
        List<TypeAssurance> types = new ArrayList<TypeAssurance>();
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return types;
        }
        if (getProfession(noCaisseMetier).hasDroitAJ.typeAssurance != null) {
            for (String type : getProfession(noCaisseMetier).hasDroitAJ.typeAssurance) {
                types.add(TypeAssurance.fromValue(type));
            }
        }

        return types;
    }

    /**
     * Retourne une liste des assurances obligatoires afin de pouvoir disposer d'une prestation cong� pay�.
     * Il suffit de disposer de l'UNE DE CES ASSURANCES pour avoir droit au cong� pay�.
     * 
     * @param noCaisseMetier caisse m�tier sur lequel retrouver les assurances obligatoires
     * @return Liste des assurances
     */
    public List<TypeAssurance> getTypeAssuranceObligatoireForCP(int noCaisseMetier) {
        List<TypeAssurance> types = new ArrayList<TypeAssurance>();
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return types;
        }

        if (getProfession(noCaisseMetier).hasDroitCP.typeAssurance != null) {
            for (String type : getProfession(noCaisseMetier).hasDroitCP.typeAssurance) {
                types.add(TypeAssurance.fromValue(type));
            }
        }

        return types;
    }

    /**
     * Retourne une liste des assurances obligatoires afin de pouvoir disposer d'une prestation service militaire.
     * Il suffit de disposer de l'UNE DE CES ASSURANCES pour avoir droit aux prestations service militaire.
     * 
     * @param noCaisseMetier caisse m�tier sur lequel retrouver les assurances obligatoires
     * @return Liste des assurances
     */
    public List<TypeAssurance> getTypeAssuranceobligatoireForSM(int noCaisseMetier) {
        List<TypeAssurance> types = new ArrayList<TypeAssurance>();
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return types;
        }

        if (getProfession(noCaisseMetier).hasDroitSM.typeAssurance != null) {
            for (String type : getProfession(noCaisseMetier).hasDroitSM.typeAssurance) {
                types.add(TypeAssurance.fromValue(type));
            }
        }

        return types;
    }

    /**
     * Retourne si la caisse m�tier dispose des droits AJ si ces types d'assurance sont saisis
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param types Type d'assurance
     * @return true si dispose des droits AJ
     */
    public boolean hasDroitAJ(int noCaisseMetier, TypeAssurance... types) {
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return false;
        }

        Profession profession = getProfession(noCaisseMetier);
        return hasDroitX(noCaisseMetier, profession.hasDroitAJ.typeAssurance, types);
    }

    /**
     * Retourne si la caisse m�tier dispose des droits CP si ces types d'assurance sont saisis
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param types Type d'assurance
     * @return true si dispose des droits CP
     */
    public boolean hasDroitCP(int noCaisseMetier, TypeAssurance... types) {
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return false;
        }

        Profession profession = getProfession(noCaisseMetier);
        if (profession.hasDroitCP.typeAssurance == null) {
            return false;
        }
        return hasDroitX(noCaisseMetier, profession.hasDroitCP.typeAssurance, types);
    }

    /**
     * Retourne si la caisse m�tier dispose des droits SM si ces types d'assurance sont saisis
     * 
     * @param noCaisseMetier No de la caisse m�tier
     * @param types Type d'assurance
     * @return true si dispose des droits SM
     */
    public boolean hasDroitSM(int noCaisseMetier, TypeAssurance... types) {
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return false;
        }

        Profession profession = getProfession(noCaisseMetier);
        return hasDroitX(noCaisseMetier, profession.hasDroitSM.typeAssurance, types);
    }

    private boolean hasDroitX(int noCaisseMetier, List<String> typesFromParametrages, TypeAssurance... types) {
        if (!isCaisseMetierExist(noCaisseMetier)) {
            return false;
        }

        return containsOne(typesFromParametrages, Arrays.asList(types));
    }

    private boolean containsOne(List<String> values, List<TypeAssurance> typesAssurances) {
        List<String> typesToFind = new ArrayList<String>();
        for (TypeAssurance typeAssurance : typesAssurances) {
            typesToFind.add(typeAssurance.getValue());
        }

        for (String type : values) {
            if (typesToFind.contains(type)) {
                return true;
            }
        }
        return false;
    }

    protected double getNombreJoursPrestationAJ(int noCaisseMetier, TypeAbsenceJustifiee type) {
        return getNombreJoursPrestationAJ(noCaisseMetier, type, null, 0);
    }

    protected double getNombreJoursPrestationAJ(int noCaisseMetier, TypeAbsenceJustifiee type, LienParente lien) {
        return getNombreJoursPrestationAJ(noCaisseMetier, type, lien, 0);
    }

    protected double getNombreJoursPrestationAJ(int noCaisseMetier, TypeAbsenceJustifiee type, int nbAnneesService) {
        return getNombreJoursPrestationAJ(noCaisseMetier, type, null, nbAnneesService);
    }

    /**
     * Retourne la valeur maximum par rapport aux deux param�tres.
     * 
     * @param nombre1 Un nombre
     * @param nombre2 Un nombre
     * @return Le nombre le plus �lev� par rapport aux deux param�tres
     */
    private int getMax(int nombre1, int nombre2) {
        if (nombre1 > nombre2) {
            return nombre1;
        } else {
            return nombre2;
        }
    }

    /**
     * Retourne si le pr�dicat v�rifie le nombre d'ann�es de service.
     * 
     * @param predicat Pr�dicat � contr�ler
     * @return true si le champ de contr�le est "anneeService"
     */
    private boolean isAnneeServicePredicat(Predicat predicat) {
        return FIELD_ANNEE_SERVICE.equals(predicat.field);
    }

    /**
     * Retourne si le pr�dicat v�rifie l'�ge.
     * 
     * @param predicat Pr�dicat � contr�ler
     * @return true si le champ de contr�le est "age"
     */
    private boolean isAgePredicat(Predicat predicat) {
        return FIELD_AGE.equals(predicat.field);
    }

    private boolean checkPredicats(List<Predicat> predicats, int age, int anneeService) {
        for (Predicat predicat : predicats) {
            boolean status = checkPredicat(predicat, age, anneeService);
            if (!status) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPredicat(Predicat predicat, int age, int anneeService) {
        boolean status = true;
        if (isAgePredicat(predicat)) {
            if (!checkPredicat(predicat, age)) {
                status = false;
            }
        } else if (isAnneeServicePredicat(predicat)) {
            if (!checkPredicat(predicat, anneeService)) {
                status = false;
            }
        }
        return status;
    }

    private boolean checkPredicat(Predicat predicat, int valueToCheck) {
        String type = predicat.type;
        int value = Integer.valueOf(predicat.content);
        if (TYPE_GREATER_OR_EQUALS.equals(type)) {
            return valueToCheck >= value;
        }
        if (TYPE_EQUALS.equals(type)) {
            return valueToCheck == value;
        }
        if (TYPE_LESS.equals(type)) {
            return valueToCheck < value;
        }
        if (TYPE_LESS_OR_EQUALS.equals(type)) {
            return valueToCheck <= value;
        }
        if (TYPE_GREATER.equals(type)) {
            return valueToCheck > value;
        }
        LOGGER.error("Il existe des pr�dicats invalides dans le fichier XML");
        throw new IllegalStateException("Il existe des pr�dicats invalides dans le fichier XML");
    }

    private List<Naissance> getNaissances(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getNaissances().getNaissance();
    }

    private List<Parente> getParentes(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getDeces().getParente();
    }

    private List<Vacance> getVacances(int noCaisseMetier) {
        return getProfession(noCaisseMetier).getVacances().getVacance();
    }

    private Profession getProfession(int noCaisseMetierToFind) {
        List<Profession> professions = parametragePrestation.getProfession();
        for (Profession profession : professions) {
            for (BigInteger noCaisseMetier : profession.getCaissesMetiers().getCaisseMetier()) {
                if (noCaisseMetier.equals(BigInteger.valueOf(noCaisseMetierToFind))) {
                    return profession;
                }
            }
        }
        throw new IllegalArgumentException("Le no� de caisse m�tier " + noCaisseMetierToFind + " est invalide");
    }

    private boolean isCaisseMetierExist(int noCaisseMetierToFind) {
        try {
            getProfession(noCaisseMetierToFind);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }
}
