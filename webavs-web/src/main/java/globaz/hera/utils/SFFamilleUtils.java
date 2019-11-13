package globaz.hera.utils;

import ch.globaz.common.domaine.Adresse;
import ch.globaz.orion.ws.service.adresse.AdresseLoaderManager;
import ch.globaz.pegasus.businessimpl.services.adresse.TIAdresseLoaderManager;
import globaz.corvus.application.REApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFEnfantJoinMembreFamille;
import globaz.hera.db.famille.SFEnfantJoinMembreFamilleManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAdresseData;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ch.globaz.al.businessimpl.rafam.handlers.AnnoncesChangeChecker.isDateFinDroitExpire;

/**
 * Utilitaire pour récupérer des info multiples sur les familles ou relations familiales (tel que le conjoint actuel, ou
 * les ex-conjoints)
 *
 * @author PBA
 */
public class SFFamilleUtils {

    /**
     * Retourne la conjoint actuel (état de la relation marié ou séparé de fait) du tiers passé en paramètre.<br/>
     *
     * @param session   La session utilisateur
     * @param csDomaine le domaine d'application dans lequel on veut chercher la relation. Si aucune relation n'est trouvée
     *                  dans ce domaine, une recherche dans le domaine standard sera effectuée
     * @param idTiers   l'ID du tiers duquel on recherche le conjoint
     * @return Le conjoint du tiers (si marié ou séparé de fait) ou <code>null</code> si le tiers n'est pas dans une
     * relation actuellement. Retourne aussi <code>null</code> si le tiers est veuf
     * @throws Exception dans les cas suivants : <li>Si la session est <code>null</code></li><li>Si l'ID Tiers est vide ou
     *                   <code>null</code></li><li>
     *                   Si un erreur survient lors de la récupération des relations du tiers</li>
     */
    public static PRTiersWrapper getConjointActuel(BSession session, String csDomaine, String idTiers) throws Exception {
        if (session == null) {
            throw new NullPointerException("La session ne doit pas être null");
        }
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new Exception("L'id tiers doit être renseigné");
        }

        ISFRelationFamiliale[] relations = SFFamilleUtils.getRelationsPourTiers(session, csDomaine, idTiers);

        if (relations == null) {
            return null;
        }

        ISFRelationFamiliale lastRelation = null;

        // on parcourt la liste des relations du tiers en cherchant celle ayant la date de début
        // la plus récente (donc l'actuelle)
        for (ISFRelationFamiliale relation : relations) {

            if (lastRelation == null) {
                lastRelation = relation;
                continue;
            }

            if (JadeDateUtil.isDateBefore(lastRelation.getDateDebutRelation(), relation.getDateDebutRelation())) {
                lastRelation = relation;
            }
        }

        if (lastRelation == null) {
            return null;
        }

        // on vérifie que la dernière relation est en cours (marié ou séparé de fait)
        if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equalsIgnoreCase(lastRelation.getTypeRelation())
                || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equalsIgnoreCase(lastRelation.getTypeRelation())) {
            // on récupère le conjoint du tiers (en regardant les numéro AVS de la relation
            // pour définir qui est qui dans la table, homme ou femme)
            PRTiersWrapper conjoint = null;
            if (PRTiersHelper.getTiersParId(session, idTiers).getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                    .equalsIgnoreCase(lastRelation.getNoAvsFemme())) {
                conjoint = PRTiersHelper.getTiers(session, lastRelation.getNoAvsHomme());
            } else {
                conjoint = PRTiersHelper.getTiers(session, lastRelation.getNoAvsFemme());
            }

            if (conjoint == null) {
                return null;
            }

            // si le conjoint n'a pas de date de décès (s'il est vivant) on le retourne
            if (JadeStringUtil.isBlankOrZero(conjoint.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
                return conjoint;
            }
        }

        // si une des conditions n'est pas remplie, on retourne null
        return null;
    }

    /**
     * LGA : Méthode pas encore éprouvé !!!
     * <p>
     * Retourne les enfants découlant des relations conjugales du tiers.<br/>
     * Cela comprend les parents, les frères et soeurs, les (ex-)conjoints et les enfants découlant des relations
     * conjugales du tiers.
     * </p>
     *
     * @param session
     * @param idTiersPrincipal
     * @return
     * @throws Exception
     */
    public static Set<PRTiersWrapper> getEnfantsDuTiers(BSession session, String idTiersPrincipal) throws Exception {
        if (session == null) {
            throw new NullPointerException("session null");
        }

        // si l'ID tiers est vide, retourne null
        if (JadeStringUtil.isBlank(idTiersPrincipal)) {
            throw new Exception("L'id tiers principal doit être renseigné");
        }

        // récupération des ID Tiers des enfants
        Set<String> idEnfants = new HashSet<String>();
        Set<PRTiersWrapper> enfants = new HashSet<PRTiersWrapper>();

        // Via l'id tiers, on recherche l'id famille
        SFMembreFamille membreFamilleLiant = new SFMembreFamille();
        membreFamilleLiant.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        membreFamilleLiant.setIdTiers(idTiersPrincipal);
        membreFamilleLiant.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
        membreFamilleLiant.setSession(session);
        membreFamilleLiant.retrieve();

        if (membreFamilleLiant.isNew()) {
            membreFamilleLiant.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            membreFamilleLiant.retrieve();
        }
        if (!membreFamilleLiant.isNew()) {
            String idMembreFamilleLiant = membreFamilleLiant.getIdMembreFamille();

            ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersPrincipal);

            // récupération des ID des parents, des conjoints et des enfants du tiers
            ISFMembreFamille[] membresFamilleEtendue = situationFamiliale.getMembresFamilleEtendue(
                    idMembreFamilleLiant, true);

            if ((membresFamilleEtendue != null) && (membresFamilleEtendue.length > 0)) {
                for (ISFMembreFamille unMembre : membresFamilleEtendue) {
                    // Juste pour être sur..
                    if (!JadeStringUtil.isBlankOrZero(unMembre.getIdTiers())) {
                        // On récupère que les enfants
                        if ("36003001".equals(unMembre.getRelationAuLiant())) {
                            idEnfants.add(unMembre.getIdTiers());
                        }
                    }
                }
            }

            for (String unId : idEnfants) {
                PRTiersWrapper unEnfant = PRTiersHelper.getTiersParId(session, unId);
                if (unEnfant != null) {
                    enfants.add(unEnfant);
                }
            }

        }
        return enfants;
    }

    /**
     * Retourne les ex-conjoints (vivants) du tiers dont l'ID est passé en paramètre
     *
     * @param session   La session utilisateur
     * @param csDomaine le domaine d'application dans lequel on veut chercher la relation. Si aucune relation n'est trouvée
     *                  dans ce domaine, une recherche dans le domaine standard sera effectuée
     * @param idTiers   l'ID du tiers duquel on recherche les ex-conjoint
     * @return une {@link List}&lt;{@link PRTiersWrapper}&gt; avec les ex-conjoints à l'intérieur, la liste peut être
     * vide si aucune relation antérieure existe.<br/>
     * Un ex-conjoint décédé ne sera pas présent dans la liste.
     * @throws Exception dans les cas suivants : <li>Si la session est <code>null</code></li><li>si l'ID tiers est vide</li>
     *                   <li>Si un erreur survient lors de la récupération des relations du tiers</li>
     */
    public static List<PRTiersWrapper> getExConjointsTiers(BSession session, String csDomaine, String idTiers)
            throws Exception {
        if (session == null) {
            throw new NullPointerException("La session ne doit pas être null");
        }
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new Exception("L'id tiers doit être renseigné");
        }

        return SFFamilleUtils.getRelationsInTypeRelation(session, csDomaine, idTiers,
                new HashSet<String>(Arrays.asList(ISFSituationFamiliale.CS_REL_CONJ_DIVORCE)));
    }

    /**
     * Retourne la liste des tiers qui sont en relation avec le tiers passé en paramètre.<br/>
     * Il est possible de spécifier un filtre pour les relations avec le paramètre <code>csTypeLien</code>.<br/>
     * Si <code>csTypeLien</code> est vide ou <code>null</code>, tous les tiers en liaison (donc conjoint et
     * ex-conjoint, décédé ou non) avec le tiers passé en paramètre seront retournés
     *
     * @param session        La session utilisateur
     * @param csDomaine      le domaine d'application dans lequel on veut chercher la relation. Si aucune relation n'est trouvée
     *                       dans ce domaine, une recherche dans le domaine standard sera effectuée
     * @param idTiers        l'ID du tiers duquel on recherche les relations
     * @param csTypeRelation Les type de relation qu'on veut avoir. Si le {@link Set}&lt;{@link String}&gt; est vide ou
     *                       <code>null</code>, tous les tiers en relations avec celui passé en paramètre seront retournés (donc
     *                       conjoint et ex-conjoint, décédé ou non)
     * @return une {@link List}&lt;{@link PRTiersWrapper}&gt; des relations du tiers selon les filtres définis dans les
     * paramètres
     * @throws Exception dans les cas suivants : <li>Si la session est <code>null</code></li><li>Si l'ID Tiers est vide ou
     *                   <code>null</code></li><li>Si un erreur survient lors du chargement d'un tiers depuis la base de
     *                   données</li><li>Si un erreur survient lors du chargement des relations du tiers, passé en paramètre,
     *                   depuis la base de données</li>
     * @see {@link ISFSituationFamiliale#CS_REL_CONJ_DIVORCE}
     * @see {@link ISFSituationFamiliale#CS_REL_CONJ_MARIE}
     * @see {@link ISFSituationFamiliale#CS_REL_CONJ_SEPARE_DE_FAIT}
     * @see {@link ISFSituationFamiliale#CS_REL_CONJ_SEPARE_JUDICIAIREMENT}
     */
    private static List<PRTiersWrapper> getRelationsInTypeRelation(BSession session, String csDomaine, String idTiers,
                                                                   Set<String> csTypeRelation) throws Exception {
        if (session == null) {
            throw new NullPointerException("La session ne doit pas être null");
        }
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new Exception("L'id tiers doit être renseigné");
        }

        List<PRTiersWrapper> tiersCorrespondants = new ArrayList<PRTiersWrapper>();
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);

        ISFRelationFamiliale[] relationsDuTiers = SFFamilleUtils.getRelationsPourTiers(session, csDomaine, idTiers);

        for (ISFRelationFamiliale relation : relationsDuTiers) {
            if ((csTypeRelation == null) || csTypeRelation.isEmpty()
                    || csTypeRelation.contains(relation.getTypeRelation())) {
                PRTiersWrapper tiersLie = PRTiersHelper
                        .getTiers(
                                session,
                                relation.getNoAvsFemme().equals(
                                        tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)) ? relation
                                        .getNoAvsHomme() : relation.getNoAvsFemme());
                if (tiersLie != null) {
                    tiersCorrespondants.add(tiersLie);
                }
            }
        }

        return tiersCorrespondants;
    }

    /**
     * Recherche toutes les relations conjugales du tiers
     *
     * @param session   La session utilisateur
     * @param csDomaine le domaine d'application dans lequel on veut chercher la relation. Si aucune relation n'est trouvée
     *                  dans ce domaine, une recherche dans le domaine standard sera effectuée
     * @param idTiers   l'ID du tiers duquel on recherche les relations
     * @return un tableau avec les relations conjugales du tiers, peut être <code>null</code> si aucune relation
     * n'existe
     * @throws Exception dans les cas suivants : <li>Si la session est <code>null</code></li><li>Si l'ID Tiers est vide ou
     *                   <code>null</code></li><li>Si un erreur survient lors du chargement de la situation familiale du tiers
     *                   depuis la base de données</li> <li>Si un erreur survient lors de la recherche des relations
     *                   conjugales dans la situation familiale du tiers</li>
     */
    private static ISFRelationFamiliale[] getRelationsPourTiers(BSession session, String csDomaine, String idTiers)
            throws Exception {
        if (session == null) {
            throw new NullPointerException("La session ne doit pas être null");
        }
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new Exception("L'id tiers doit être renseigné");
        }

        // recherche de la situation familiale du tiers en fonction du domaine
        ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(session,
                csDomaine, idTiers);

        // on récupère la liste des relations du tiers
        ISFRelationFamiliale[] relations = situationFamiliale.getRelationsConjoints(idTiers, null);

        return relations;
    }

    /**
     * <p>
     * Retourne les tiers de la famille proche du tiers passé en paramètre.<br/>
     * Cela comprend les parents, les frères et soeurs, les (ex-)conjoints et les enfants découlant des relations
     * conjugales du tiers.
     * </p>
     *
     * @param session une session utilisateur
     * @param idTiers l'ID du tiers dont on aimerait avoir les tiers de la famille proche
     * @return un {@link Set} de {@link PRTiersWrapper} contenant la famille proche du tiers passé en paramètre. Si le
     * tiers n'est pas trouvé dans la situation familiale (domaine rente, puis standard), le {@link Set} sera
     * vide
     * @throws Exception si la session est <code>null</code>, si l'ID tiers n'est pas renseigné ou si un problème survient
     *                   lors de la recherche dans la base de données
     */
    public static Set<PRTiersWrapper> getTiersFamilleProche(BSession session, String idTiers) throws Exception {
        // si la session est null, retourne null
        if (session == null) {
            throw new NullPointerException("session null");
        }

        // si l'ID tiers est vide, retourne null
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new Exception("L'ID tiers doit être renseigné");
        }

        String idTiersLiant = idTiers;
        String idMembreFamilleLiant = null;

        // récupération des ID Tiers des membres de la famille proche
        Set<String> idMembreFamille = new HashSet<String>();

        SFMembreFamille membreFamilleLiant = new SFMembreFamille();
        membreFamilleLiant.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        membreFamilleLiant.setIdTiers(idTiersLiant);
        membreFamilleLiant.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
        membreFamilleLiant.setSession(session);
        membreFamilleLiant.retrieve();

        if (membreFamilleLiant.isNew()) {
            membreFamilleLiant.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            membreFamilleLiant.retrieve();
        }

        if (!membreFamilleLiant.isNew()) {
            idMembreFamilleLiant = membreFamilleLiant.getIdMembreFamille();

            // si le tiers liant est un enfant
            SFEnfant enfant = new SFEnfant();
            enfant.setSession(session);
            enfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
            enfant.setIdMembreFamille(idMembreFamilleLiant);
            enfant.retrieve();

            if (!enfant.isNew()) {
                // on récupère les parents et les frères et soeurs
                SFConjoint relationConjoint = new SFConjoint();
                relationConjoint.setSession(session);
                relationConjoint.setIdConjoints(enfant.getIdConjoint());
                relationConjoint.retrieve();

                if (!relationConjoint.isNew()) {
                    // récupération des ID tiers des frères et soeurs
                    SFEnfantJoinMembreFamilleManager enfantManager = new SFEnfantJoinMembreFamilleManager();
                    enfantManager.setSession(session);
                    enfantManager.setForIdConjoints(relationConjoint.getIdConjoints());
                    enfantManager.find(BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < enfantManager.getSize(); i++) {
                        SFEnfantJoinMembreFamille unFrereEtSoeur = (SFEnfantJoinMembreFamille) enfantManager.get(i);
                        if (!JadeStringUtil.isBlank(unFrereEtSoeur.getIdTiers())
                                && !idTiersLiant.equals(unFrereEtSoeur.getIdTiers())) {
                            idMembreFamille.add(unFrereEtSoeur.getIdTiers());
                        }
                    }
                }
            }

            ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersLiant);

            // récupération des ID des parents, des conjoints et des enfants du tiers
            ISFMembreFamille[] membresFamilleEtendue = situationFamiliale.getMembresFamilleEtendue(
                    idMembreFamilleLiant, true);

            if ((membresFamilleEtendue != null) && (membresFamilleEtendue.length > 0)) {
                for (ISFMembreFamille unMembre : membresFamilleEtendue) {
                    if (!JadeStringUtil.isBlankOrZero(unMembre.getIdTiers())) {
                        idMembreFamille.add(unMembre.getIdTiers());
                    }
                }
            }

            idMembreFamille.add(idTiersLiant);
        }

        Set<PRTiersWrapper> familles = new HashSet<PRTiersWrapper>();
        for (String unId : idMembreFamille) {
            PRTiersWrapper unMembre = PRTiersHelper.getTiersParId(session, unId);
            if (unMembre != null) {
                familles.add(unMembre);
            }
        }

        return familles;
    }


    /**
     * Permet de récupérer le tiers éligible pour le transfert d'une rente.
     * Les règles sont les suivantes :
     *  - Conjoint si non décédé
     *  - Plus jeune enfant si non décédé
     *  - Ex-conjoint si non décédé
     * On contrôle également qu'il existe une adresse à ce tiers pour qu'il soit éligible.
     *
     * @param session
     * @param idTiers
     * @return le tiers éligible pour le transfert
     * @throws Exception
     */
    public static PRTiersWrapper getTiersTransfert(BSession session, String idTiers) throws Exception {
        PRTiersWrapper conjoint = getConjointActuel(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD, idTiers);

        if (Objects.nonNull(conjoint) && isAdresseActive(session, conjoint.getIdTiers())) {
            return conjoint;
        } else {
            Set<PRTiersWrapper> enfants = getEnfantsDuTiers(session, idTiers);
            List<PRTiersWrapper> enfantsVivants = enfants.stream().filter(e -> StringUtils.isEmpty(e.getDateDeces())).sorted(Comparator.comparing(PRTiersWrapper::getDateNaissanceYYYYMMDD).reversed()).collect(Collectors.toList());

            if (!enfantsVivants.isEmpty() && isAdresseActive(session, enfantsVivants.get(0).getIdTiers())) {
                return enfantsVivants.get(0);
            } else {
                List<PRTiersWrapper> exConjoints = getExConjointsTiers(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD, idTiers);
                List<PRTiersWrapper> exConjointsAlive = exConjoints.stream().filter(e -> StringUtils.isEmpty(e.getDateDeces())).collect(Collectors.toList());

                if (!exConjointsAlive.isEmpty() && isAdresseActive(session, exConjointsAlive.get(0).getIdTiers())) {
                    return exConjointsAlive.get(0);
                }
            }
        }
        if (Objects.nonNull(conjoint)) {
            throw new Exception("Pas d'adresse connue pour le tiers - NSS : " + conjoint.getNSS()
                    + " - Nom : " + conjoint.getNom() + " - Prénom : " + conjoint.getPrenom());
        } else {
            throw new Exception("Aucun Tiers éligible à la rente n'a été trouvé.");
        }

    }

    /**
     *
     *  Vérification s'il existe une adresse valide pour un tiers. Il est posisble que l'adresse existe mais qu'elle ne soit plus valable
     *
     * @param session
     * @param idTiers
     * @return
     */
    private static boolean isAdresseActive(BSession session, String idTiers) throws Exception {

        TIAdresseData adresseData = new TIAdresseData();

        if (StringUtils.isNotEmpty(PRTiersHelper.getTiersAdresseParId(session,idTiers).getProperty(PRTiersWrapper.PROPERTY_ID_ADRESSE))) {
            adresseData = findAdresseForIdAdresse(session, PRTiersHelper.getTiersAdresseParId(session,idTiers).getProperty(PRTiersWrapper.PROPERTY_ID_ADRESSE));
            if (StringUtils.isNotEmpty(adresseData.getDateFinRelation())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate dateFinDroit = LocalDate.parse(adresseData.getDateFinRelation(), formatter);
                return !dateFinDroit.isBefore(LocalDate.now());
            }
            return true;

        }
        return false;
    }

    /**
     *
     * Recherche de l'adresse par son Id
     *
     *
     * @param session
     * @param idAdresse
     * @return
     */
    public static TIAdresseData findAdresseForIdAdresse(BSession session, String idAdresse) throws Exception{
        TIAdresseLoaderManager mgrAdresse = new TIAdresseLoaderManager();

        mgrAdresse.setSession(session);
        mgrAdresse.setForIdAdresse(idAdresse);
        try {
            mgrAdresse.find(BManager.SIZE_NOLIMIT);

        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger les adresses avec le manager ->TIAdresseLoaderManager ",
                    e);
        }

        return (TIAdresseData) mgrAdresse.getEntity(0);
    }

    /**
     * Test des méthodes de {@link SFFamilleUtils}
     *
     * @param args
     */
    public static void main(String[] args) {
        BISession session;
        BITransaction transaction = null;

        String noAvsTiers = "756.0384.7999.50";

        try {
            StringBuilder infoTiers = new StringBuilder();
            session = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession("pba", "pba");
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            PRTiersWrapper tiers = PRTiersHelper.getTiers(session, noAvsTiers);

            infoTiers.append("\nTiers");
            infoTiers.append("\nNSS : ").append(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            infoTiers.append("\nNom : ").append(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
            infoTiers.append("\nPrénom : ").append(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            PRTiersWrapper conjoint = SFFamilleUtils.getConjointActuel((BSession) session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            infoTiers.append("\n\nConjoint actuel");
            if (conjoint != null) {
                infoTiers.append("\nNSS : ").append(conjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                infoTiers.append("\nNom : ").append(conjoint.getProperty(PRTiersWrapper.PROPERTY_NOM));
                infoTiers.append("\nPrénom : ").append(conjoint.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            } else {
                infoTiers.append("\nPas de relation en cours pour ce tiers");
            }

            List<PRTiersWrapper> exConjoints = SFFamilleUtils.getExConjointsTiers((BSession) session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            if ((exConjoints != null) && !exConjoints.isEmpty()) {

                infoTiers.append("\n\nEx-conjoints");
                for (PRTiersWrapper unExConjoint : exConjoints) {
                    infoTiers.append("\nNSS : ").append(
                            unExConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    infoTiers.append("\nNom : ").append(unExConjoint.getProperty(PRTiersWrapper.PROPERTY_NOM));
                    infoTiers.append("\nPrénom : ").append(unExConjoint.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                }
            } else {
                infoTiers.append("\nPas d'ex-conjoints pour ce tiers");
            }
            infoTiers.append("\n");

            Set<PRTiersWrapper> familleProche = SFFamilleUtils.getTiersFamilleProche((BSession) session, "167128");
            // tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            if (familleProche != null) {
                infoTiers.append("\nMembres de la famille :\n");
                for (PRTiersWrapper unTiers : familleProche) {
                    infoTiers.append("\n");
                    infoTiers.append("NSS : ").append(unTiers.getNSS()).append("\n");
                    infoTiers.append("Nom : ").append(unTiers.getNom()).append("\n");
                    infoTiers.append("Prénom : ").append(unTiers.getPrenom()).append("\n");
                    infoTiers.append("ID Tiers : ").append(unTiers.getIdTiers()).append("\n");
                }
            }

            System.out.println(infoTiers.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        System.out.println("End...");
    }



}
