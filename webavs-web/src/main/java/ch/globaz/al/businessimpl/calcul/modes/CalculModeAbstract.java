package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.tarif.PrestationTarifModel;
import ch.globaz.al.business.models.tarif.TarifComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.DroitComplexModelService;
import ch.globaz.al.business.services.models.tarif.TarifComplexModelService;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Classe mère de tous les modes de calcul.
 * 
 * Elle fournit les méthodes communes à tous les modes de calcul
 * 
 * @author jts
 * 
 */
public abstract class CalculModeAbstract implements CalculMode {

    /**
     * Context du calcul
     */
    protected ContextCalcul context = null;
    /**
     * Liste contenant les droits calculé
     */
    protected List<CalculBusinessModel> droitsCalcules = new ArrayList<>();
    /**
     * Mode de calcul
     */
    protected String modeCalcul = null;

    /**
     * Nombre de droit actuellement calculé. Cette valeur est incrémenté au cours du traitement
     */
    protected int nombre = 0;

    protected TauxImpositions tauxGroupByCanton;

    protected TauxImpositionRepository tauxImpositionRepository = ALRepositoryLocator
            .getTauxImpositionRepository();
    /**
     * Il n'est pas possible de savoir si un critère de nombre doit être utilisé pour déterminer un montant avant que le
     * calcul ne soit effectivement exécuté. Ce flag permet de demander au calcul de s'exécuter une seconde fois si un
     * critère de nombre est détecté pendant le traitement
     * 
     * @see CalculModeAbstract#checkPotetialNumberCriterium(TarifComplexSearchModel)
     * @see CalculModeAbstract#processDroits(DossierComplexModelRoot, String)
     */
    protected boolean potetialNumberCriterion = false;
    /**
     * Rang du droit actuellement en cours de traitement. Cette valeur est incrémenté au cours du traitement
     */
    protected int rang = 0;
    /**
     * Variable stockant le revenu de l'allocataire
     */
    private String revenu = null;
    /**
     * Tarif correspondant au mode de calcul
     */
    protected String usedTarif = null;

    /**
     * Vérifie s'il est possible qu'il y ait un critère de nombre dont il faut tenir compte. Si c'est le cas, le champ
     * <code>potetialNumberCriterium</code> est définit à <code>true</code>
     * 
     * @param tarifs
     *            Tarifs qui ont été trouvés pour un droit
     * @throws ALCalculException
     *             Exception levée si <code>tarifs<code> est null
     */
    private void checkPotetialNumberCriterium(TarifComplexSearchModel tarifs) throws ALCalculException {

        if (tarifs == null) {
            throw new ALCalculException("CalculModeAbstract#checkPotetialNumberCriterium : tarifs is null");
        }

        // Si plusieurs résultats ont été retournés par la recherche des tarifs
        // on effectue une comparaisons pour déterminer si plusieurs résultat
        // appartiennent à la même catégorie
        if (tarifs.getSize() > 1) {

            // récupération du premier tarif de la liste, celui qui sera
            // effectivement placé dans les résultats du calcul
            PrestationTarifModel tarif = ((TarifComplexModel) tarifs.getSearchResults()[0]).getPrestationTarifModel();

            if (!potetialNumberCriterion) {

                // on compare chaque tarif avec le premier
                for (int i = 1; i < tarifs.getSize(); i++) {

                    PrestationTarifModel tmp = ((TarifComplexModel) tarifs.getSearchResults()[i])
                            .getPrestationTarifModel();

                    // Si deux tarifs appartenant à la même catégorie ont été
                    // récupérés c'est qu'il y en a probablement deux
                    // "identiques" mais pour un critère NBR différent, il
                    // faudra donc réexécuter le calcul en tenant compte de ce
                    // critère
                    if (tarif.getIdCategorieTarif().equals(tmp.getIdCategorieTarif())) {
                        potetialNumberCriterion = true;
                        break;
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculMode#compute(ch.globaz.al
     * .business.models.dossier.DossierComplexModelAbstract, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALCalculException("CalculModeAbstract#compute : context is null");
        }

        initCalculMode(context);

        processDroits(context.getDossier(), context.getDateCalcul());
        Collections.sort(droitsCalcules);
        return droitsCalcules;
    }

    /**
     * Exécute le calcul pour un droit quel que soit son type
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droit
     *            Le droit à calculer
     * @return <code>true</code> si le droit est actif, <code>false</code> sinon
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see CalculModeAbstract#computeEnfant(DossierComplexModelRoot, DroitComplexModel, String, String)
     * @see CalculModeAbstract#computeMenage(DossierComplexModelRoot, DroitComplexModel, String, String)
     * @see CalculModeAbstract#computeNaissance(DossierComplexModelRoot, DroitComplexModel, String, String)
     */
    protected boolean computeDroit(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#computeDroit : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#computeDroit : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#computeDroit : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#computeDroit : typeResident is null or empty");
        }

        // si droit actif => calcul, sinon montant à 0
        if (ALServiceLocator.getDroitBusinessService().isDroitActif(droit.getDroitModel(), dateCalcul)) {
            // ménage
            if (ALServiceLocator.getDroitBusinessService().isTypeMenage(droit.getDroitModel())) {
                computeMenage(dossier, droit, dateCalcul, typeResident);
                // ENF ou FORM
            } else {
                computeEnfant(dossier, droit, dateCalcul, typeResident);
            }

            usedTarif = null;
            return true;
        } else {
            droitsCalcules = ALImplServiceLocator.getCalculMontantsService().addDroitCalculeInactif(droit,
                    droitsCalcules, dateCalcul);

            usedTarif = null;
            return false;
        }
    }

    /**
     * Exécute le calcul pour un droit de type enfant ou formation
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droit
     *            Le droit à calculer
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see CalculModeAbstract#computeMenage(DossierComplexModelRoot, DroitComplexModel, String, String)
     * @see CalculModeAbstract#computeNaissance(DossierComplexModelRoot, DroitComplexModel, String, String)
     */
    private void computeEnfant(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#computeEnfant : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#computeEnfant : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#computeEnfant : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#computeEnfant : typeResident is null or empty");
        }

        TarifComplexSearchModel tarifs = getTarifComplexSearchModel(dossier, droit, dateCalcul, typeResident, droit
                .getDroitModel().getTypeDroit());

        // si l'enfant à droit à une prestation, ajout du montant au droit...
        if (setMontantsForDroit(dossier, droit, tarifs)) {
            checkPotetialNumberCriterium(tarifs);
            computeNaissance(dossier, droit, dateCalcul, typeResident);
            rang++;
            nombre++;
        } else if (ALCSDroit.ETAT_G.equals(droit.getDroitModel().getEtatDroit())) {
            rang++;
            nombre++;
        }
    }

    /**
     * Exécute le calcul pour un droit de type ménage
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droit
     *            Le droit à calculer
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see CalculModeAbstract#computeEnfant(DossierComplexModelRoot, DroitComplexModel, String, String)
     * @see CalculModeAbstract#computeNaissance(DossierComplexModelRoot, DroitComplexModel, String, String)
     */
    protected void computeMenage(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#computeMenage : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#computeMenage : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#computeMenage : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#computeMenage : typeResident is null or empty");
        }

        TarifComplexSearchModel tarifs = getTarifComplexSearchModel(dossier, droit, dateCalcul, typeResident, droit
                .getDroitModel().getTypeDroit());

        setMontantsForDroit(dossier, droit, tarifs);
    }

    /**
     * Détermine si une prestation de naissance doit être accordée et la calul si c'est le cas
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droit
     *            Le droit à calculer
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see CalculModeAbstract#computeEnfant(DossierComplexModelRoot, DroitComplexModel, String, String)
     * @see CalculModeAbstract#computeMenage(DossierComplexModelRoot, DroitComplexModel, String, String)
     */
    protected void computeNaissance(DossierComplexModelRoot dossier, DroitComplexModel droit, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#computeNaissance : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#computeNaissance : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#computeNaissance : " + dateCalcul + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#computeNaissance : typeResident is null or empty");
        }

        String type = droit.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance();

        // si une prestation de naissance/accueil doit être versée
        if ((ALCSDroit.NAISSANCE_TYPE_NAIS.equals(type) || ALCSDroit.NAISSANCE_TYPE_ACCE.equals(type))
                && !droit.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee()) {

            String typePrest = (ALCSDroit.NAISSANCE_TYPE_NAIS.equals(type) ? ALCSDroit.TYPE_NAIS : ALCSDroit.TYPE_ACCE);

            TarifComplexSearchModel tarifs = getTarifComplexSearchModel(dossier, droit, dateCalcul, typeResident,
                    typePrest);

            // si l'enfant à droit à une prestation, ajout du montant au droit
            if ((tarifs.getSize() > 0)
                    || !JadeNumericUtil.isEmptyOrZero(droit.getEnfantComplexModel().getEnfantModel()
                            .getMontantAllocationNaissanceFixe())) {
                droitsCalcules = ALImplServiceLocator.getCalculMontantsService().addDroitCalculeNaissance(
                        dossier.getDossierModel(), droit, tarifs, usedTarif, droitsCalcules, String.valueOf(rang + 1),
                        typePrest, tarifs.getForValidite());
            }
        }
    }

    /**
     * Retourne les catégories de tarif dans lesquelles les montants doivent être recherchés
     * 
     * @param dossier
     *            Dossier pour lequel les droits sont calculés
     * @param droitModel
     *            Droit pour lequel le tarif doit être recherché
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * 
     * @return Liste des catégories de tarif à utiliser pour la récupération du montant d'un droit
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected Set<String> getCategoriesList(DossierComplexModelRoot dossier, DroitModel droitModel,
            String dateCalcul) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : dossier is null");
        }

        if (droitModel == null) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : droitModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : " + dateCalcul + " is not a valid date");
        }

        HashSet<String> set = new HashSet<>();

        // ajout de la catégorie forcée si nécessaire
        if (!setCategoriesForceesList(dossier, droitModel, set)) {

            // ajout de la catégorie du mode de calcul si nécessaire
            if (!setCategoriesModesList(set, dateCalcul)) {

                // ajout des catégories standard
                setCategoriesStandardsList(dossier, dateCalcul, set);
            }
        }

        return set;
    }

    /**
     * Retourne la liste des législations dans lesquelles les tarifs doivent être recherchés.
     * 
     * @return liste des législations
     */
    protected Set<String> getLegislationSet() {
        Set<String> set = new HashSet<String>();
        set.add(ALCSTarif.LEGISLATION_CAISSE);
        set.add(ALCSTarif.LEGISLATION_CANTONAL);
        set.add(ALCSTarif.LEGISLATION_FEDERAL);
        set.add(ALCSTarif.LEGISLATION_AGRICOLE);
        return set;
    }

    /**
     * Retourne le nombre d'enfants bénéficiant de prestation. Ne devrait être utilisé qu'à la fin du traitement, cas
     * auquel le nombre retourné correspond au nombre d'enfant bénéficiant d'une prestation
     * 
     * @return nombre d'enfants bénéficiant de prestation
     */
    public int getNombre() {
        return nombre;
    }

    protected String getPermis(DossierComplexModelRoot dossier) {
        if (DossierAgricoleComplexModel.class.equals(dossier.getClass())) {

            return ((DossierAgricoleComplexModel) dossier).getAllocataireAgricoleComplexModel().getAllocataireModel()
                    .getPermis();
        } else {
            return ((DossierComplexModel) dossier).getAllocataireComplexModel().getAllocataireModel().getPermis();
        }
    }

    /**
     * Retourne le revenu de l'allocataire au moment de la date de calcul.
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @return le revenu de l'allocataire du dossier. 0 si aucun revenu n'a été trouvé
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private String getRevenu(DossierComplexModelRoot dossier, String dateCalcul) throws JadePersistenceException,
            JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#getRevenu : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#getRevenu : " + dateCalcul + " is not a valid date");
        }

        if (revenu == null) {
            RevenuModel revenuModel = ALImplServiceLocator.getRevenuModelService().searchDernierRevenu(dateCalcul,
                    dossier.getDossierModel().getIdAllocataire(), false);

            revenu = (revenuModel == null ? "0" : revenuModel.getMontant());
        }

        return revenu;
    }

    /**
     * Recherche les tarifs correspondants aux données passées en paramètre
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param droit
     *            Droit pour lequel le tarif doit être recherché
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param typePrestation
     *            Type de prestation recherché {@link ch.globaz.al.business.constantes.ALCSPrestation}
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private TarifComplexSearchModel getTarifComplexSearchModel(DossierComplexModelRoot dossier,
            DroitComplexModel droit, String dateCalcul, String typeResident, String typePrestation)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#getTarifComplexSearchModel : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#getTarifComplexSearchModel : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#getTarifComplexSearchModel : " + dateCalcul
                    + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#getTarifComplexSearchModel : typeResident is null or empty");
        }

        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALCalculException(
                    "CalculModeAbstract#getTarifComplexSearchModel : typePrestation is null or empty");
        }

        TarifComplexSearchModel tarifs = new TarifComplexSearchModel();

        // législation
        tarifs.setInLegislations(getLegislationSet());
        // catégories de tarif
        tarifs.setInCategoriesTarif(getCategoriesList(dossier, droit.getDroitModel(), dateCalcul));
        // critère revenu
        if (ALCSDossier.ACTIVITE_INDEPENDANT.equals((dossier.getDossierModel().getActiviteAllocataire()))) {
            tarifs.setForCritereRevenuIndependant(getRevenu(dossier, dateCalcul));
        } else if (ALCSDossier.ACTIVITE_NONACTIF.equals((dossier.getDossierModel().getActiviteAllocataire()))) {
            tarifs.setForCritereRevenuNonActif(getRevenu(dossier, dateCalcul));
        }

        // gestion du cas du frontalier pour la naissance (versement du tarif ETR)
        if (!JadeStringUtil.isBlankOrZero(getPermis(dossier)) && ALCSAllocataire.PERMIS_G.equals(getPermis(dossier))
                && (ALCSDroit.TYPE_NAIS.equals(typePrestation) || ALCSDroit.TYPE_ACCE.equals(typePrestation))) {
            typeResident = ALCSTarif.RESIDENT_ETR;
        }

        // type résident
        tarifs.setForCategorieResident(typeResident);
        // type prestation
        tarifs.setForTypePrestation(typePrestation);
        // validité
        tarifs.setForValidite(dateCalcul);

        // ajout des critères supplémentaire pour les types ENF, FORM, NAIS ou
        // ACCE
        if (!ALServiceLocator.getDroitBusinessService().isTypeMenage(droit.getDroitModel())) {

            // NBR
            tarifs.setForCritereNombre(JadeThread.currentContext().getTemporaryAttribute("CRITERE_NBR"));
            // RNG, +1 parce que la valeur est incrémenté à la fin du calcul du
            // droit si une prestation est due
            tarifs.setForCritereRang(String.valueOf(rang + 1));

            // si de type ENF ou FORM ajout des critères propres à ces
            // catégories de prestations
            if (ALCSDroit.TYPE_ENF.equals(typePrestation) || ALCSDroit.TYPE_FORM.equals(typePrestation)) {

                int age = ALImplServiceLocator.getCalculService()
                        .getAgeForCalcul(
                                droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                                        .getDateNaissance(), dateCalcul);
                // AGE
                tarifs.setForCritereAge(String.valueOf(age));

                // capable exercer
                tarifs.setForCapableExercer(droit.getEnfantComplexModel().getEnfantModel().getCapableExercer());

                Calendar date = Calendar.getInstance();
                try {
                    date.setTime(dateCalcul.length() == 10 ? new SimpleDateFormat("dd.MM.yyyy").parse(dateCalcul)
                            : new SimpleDateFormat("dd.MM.yy").parse(dateCalcul));

                    tarifs.setForMoisSeparationMois(String.valueOf(date.get(Calendar.MONTH)));

                    // -1 car dans le cas d'une échéance avec mois de
                    // séparation on doit récupérer le tarif auquel l'enfant
                    // avait droit avant de dépasser la limite
                    tarifs.setForMoisSeparationAge(String.valueOf(age - 1));

                } catch (ParseException e) {
                    throw new ALCalculException("CalculModeAbstract#getTarifComplexSearchModel : Can not parse "
                            + dateCalcul + ", this date is probably not valid");
                }
            }
        }

        TarifComplexModelService tarifsService = ALImplServiceLocator.getTarifComplexModelService();
        return tarifsService.search(tarifs);
    }

    /**
     * Retourne le type de résident en fonction du permis de travail et du pays de résidence. Un allocataire ayant un
     * permis de frontalier est considéré comme Suisse. Dans les autres cas le pays de résidence fait foi
     * 
     * @param dossier
     *            Dossier auquel est lié l'allocataire dont on recherche le type de résident
     * @return type de résident {@link ch.globaz.al.business.constantes.ALCSTarif}
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String getTypeResident(DossierComplexModelRoot dossier) throws JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#getTypeResident : dossier is null");
        }

        AllocataireModel allocataire = null;
        if (DossierAgricoleComplexModel.class.equals(dossier.getClass())) {

            allocataire = ((DossierAgricoleComplexModel) dossier).getAllocataireAgricoleComplexModel()
                    .getAllocataireModel();
        } else {
            allocataire = ((DossierComplexModel) dossier).getAllocataireComplexModel().getAllocataireModel();
        }

        return ALServiceLocator.getAllocataireBusinessService().getTypeResident(allocataire);
    }

    /**
     * Initialise le mode de calcul
     * 
     * @param context
     *            context à utiliser pendant le calcul
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void initCalculMode(ContextCalcul context) throws JadeApplicationException, JadePersistenceException {
        this.context = context;
        modeCalcul = (ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                ALConstParametres.MODE_CALCUL, context.getDateCalcul())).getValeurAlphaParametre();
    }

    /**
     * Charge les droits du dossier <code>idDossier</code>
     * 
     * @param idDossier
     *            Id du dossier pour lequel les droit doivent être chargés
     * 
     * @return Modèle de recherche contenant les droits du dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private DroitComplexSearchModel loadDroits(String idDossier) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new ALCalculException("CalculModeAbstract#loadDroits : idDossier is null or empty");
        }

        DroitComplexModelService droitsService = ALServiceLocator.getDroitComplexModelService();
        DroitComplexSearchModel droits = new DroitComplexSearchModel();
        droits.setForIdDossier(idDossier);
        droits.setOrderKey("calculDroits");
        droitsService.search(droits);

        return droits;
    }

    /**
     * Parcours les droits contenus dans <code>droits</code> et exécute le calcul pour chacun d'eux
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droits
     *            Droits à parcourir
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void loopDroits(DossierComplexModelRoot dossier, DroitComplexSearchModel droits, String dateCalcul,
            String typeResident) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#loopDroits : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#loopDroits : " + dateCalcul + " is not a valid date");
        }

        if (typeResident == null) {
            throw new ALCalculException("CalculModeAbstract#loopDroits : typeResident is null");
        }

        if (droits == null) {
            throw new ALCalculException("CalculModeAbstract#loopDroits : droits is null");
        }

        // parcours des droits
        for (int i = 0; i < droits.getSize(); i++) {
            DroitComplexModel eachDroit = (DroitComplexModel) droits.getSearchResults()[i];
            computeDroit(dossier, eachDroit, dateCalcul, typeResident);
            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()) {
                CalculImpotSource.computeIS(droitsCalcules, tauxGroupByCanton, tauxImpositionRepository, dossier, eachDroit, dateCalcul);
            }
        }
    }

    /**
     * Exécute le calcul pour le <code>dossier</code>. La méthode se charge de récupérer les droits et le type de
     * résident puis d'appeler <code>loopDroits</code>
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculModeAbstract#getTypeResident(DossierComplexModelRoot)
     * @see CalculModeAbstract#loadDroits(String)
     * @see CalculModeAbstract#loopDroits(DossierComplexModelRoot, DroitComplexSearchModel, String, String)
     */
    protected void processDroits(DossierComplexModelRoot dossier, String dateCalcul) throws JadeApplicationException,
            JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#processDroits : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#processDroits : " + dateCalcul + " is not a valid date");
        }

        // récupération du type de résident
        String typeResident = getTypeResident(dossier);

        // chargement des droits
        DroitComplexSearchModel droits = loadDroits(dossier.getId());

        // parcours des droits
        loopDroits(dossier, droits, dateCalcul, typeResident);

        // si un critère NBR potentiel a été détecté, on effectue une seconde
        // fois le calcul pour en tenir compte
        processDroitsForNbr(dossier, dateCalcul, typeResident, droits);
    }

    /**
     * Exécute le calcul pour les <code>droits</code> du <code>dossier</code> en tenant compte du critère NBR. Cette
     * méthode est généralement appelée par la la méthode <code>processDroits</code> si un critère de nombre potentiel a
     * été détecté.
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param typeResident
     *            Type de résident de l'allocataire du dossier {@link ch.globaz.al.business.constantes.ALCSAllocataire}
     * @param droits
     *            Droits à parcourir
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculModeAbstract#processDroits(DossierComplexModelRoot, String)
     */
    protected void processDroitsForNbr(DossierComplexModelRoot dossier, String dateCalcul, String typeResident,
            DroitComplexSearchModel droits) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#processDroitsForNbr : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#processDroitsForNbr : " + dateCalcul
                    + " is not a valid date");
        }

        if (JadeStringUtil.isEmpty(typeResident)) {
            throw new ALCalculException("CalculModeAbstract#processDroitsForNbr : typeResident is null or empty");
        }

        if (droits == null) {
            throw new ALCalculException("CalculModeAbstract#processDroitsForNbr : droits is null");
        }

        if (potetialNumberCriterion) {
            try {

                JadeThread.currentContext().setTemporaryAttribute("CRITERE_NBR", String.valueOf(nombre));

                // on vide la liste des droit calculés dans la 1e boucle
                droitsCalcules = new ArrayList<CalculBusinessModel>();
                nombre = 0;
                rang = 0;

                loopDroits(dossier, droits, dateCalcul, typeResident);
            } finally {
                JadeThread.currentContext().setTemporaryAttribute("CRITERE_NBR", null);
            }
        }
    }

    /**
     * Définit la liste des catégories de tarif si des tarifs forcés sont présents
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param droit
     *            Droit devant être calculé
     * @param set
     *            Liste à laquelle la catégorie forcée est ajoutée
     * 
     * @return <code>true</code> si une catégorie a été définie
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see CalculModeAbstract#getCategoriesList(DossierComplexModelRoot, DroitModel, String)
     */
    protected boolean setCategoriesForceesList(DossierComplexModelRoot dossier, DroitModel droit, Set<String> set)
            throws JadeApplicationException {

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#setCategoriesForceesList : droitModel is null");
        }

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#setCategoriesForceesList : dossier is null");
        }

        if (set == null) {
            throw new ALCalculException("CalculModeAbstract#setCategoriesForceesList : set is null");
        }

        String loiAutreParent = JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT");

        // si on est dans le cas du calcul intercantonal et que c'est le calcul
        // des droits pour l'autre parent qui est en cours
        if (!JadeStringUtil.isNull(loiAutreParent)) {
            set.add(loiAutreParent);
            usedTarif = loiAutreParent;
            return true;
            // si un tarif est forcé au niveau du droit il doit obligatoirement
            // être appliqué
        } else if (JadeNumericUtil.isIntegerPositif(droit.getTarifForce())) {
            set.add(droit.getTarifForce());
            usedTarif = droit.getTarifForce();
            return true;
            // si un tarif est forcé au niveau du dossier il doit
            // obligatoirement être appliqué
        } else if (JadeNumericUtil.isIntegerPositif(dossier.getDossierModel().getTarifForce())) {
            set.add(dossier.getDossierModel().getTarifForce());
            usedTarif = dossier.getDossierModel().getTarifForce();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Définit la liste des catégories de tarif pour des modes particulier de calcul (CAISSEAF et CANTEMPL)
     * 
     * @param set
     *            Liste à laquelle la catégorie forcée est ajoutée
     * @param dateCalcul
     *            Date à laquelle le calcul est effectué
     * 
     * @return <code>true</code> si un tarif a été défini par la méthode, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculModeAbstract#getCategoriesList(DossierComplexModelRoot, DroitModel, String)
     */
    protected boolean setCategoriesModesList(Set<String> set, String dateCalcul) throws JadeApplicationException,
            JadePersistenceException {

        if (set == null) {
            throw new ALCalculException("CalculModeAbstract#setCategoriesModesList : set is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#setCategoriesModesList : " + dateCalcul
                    + " is not a valid date");
        }

        String categorieTarif = null;
        boolean catSet = false;

        if (ALConstParametres.MODE_CALCUL_CAISSEAF.equals(modeCalcul)) {
            categorieTarif = context.getTarifCaisse(dateCalcul);
            set.add(categorieTarif);
            catSet = true;
        } else if (ALConstParametres.MODE_CALCUL_CANTEMPL.equals(modeCalcul)) {
            categorieTarif = context.getTarifCantonAssurance();
            if (JadeStringUtil.isEmpty(categorieTarif)) {
                throw new ALCalculException(
                        "CalculModeAbstract#setCategoriesModesList : unable to get 'canton employeur'");
            }

            set.add(categorieTarif);
            catSet = true;
        }

        if (categorieTarif != null) {
            usedTarif = categorieTarif;
        }

        return catSet;
    }

    /**
     * Définit la liste standard des catégories de tarifs dans lesquelles les montants doivent être recherchés. Cette
     * méthode doit être appelée si aucun autre tarif n'a été défini
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @param set
     *            Liste à laquelle la catégorie forcée est ajoutée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculModeAbstract#getCategoriesList(DossierComplexModelRoot, DroitModel, String)
     * @see CalculModeAbstract#setCategoriesForceesList(DossierComplexModelRoot, DroitModel, Set)
     * @see CalculModeAbstract#setCategoriesModesList(Set, String)
     */
    protected void setCategoriesStandardsList(DossierComplexModelRoot dossier, String dateCalcul, Set<String> set)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : " + dateCalcul + " is not a valid date");
        }

        if (set == null) {
            throw new ALCalculException("CalculModeAbstract#getCategoriesList : set is null");
        }

        String canton = context.getTarifCantonAssurance();

        if (!JadeStringUtil.isEmpty(canton)) {
            set.add(canton);
        }

        set.add(context.getTarifCaisse(dateCalcul));
        set.add(ALCSTarif.CATEGORIE_FED);
    }

    /**
     * Ajoute le droit et le montant à la liste des droits calculé. Si aucun montant n'a été trouvé,
     * <code>setMontantForDroitInactif</code> est appelée pour traiter ce cas
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param droit
     *            Droit ayant été calculé
     * @param tarifs
     *            Résultat de la recherche des tarifs pour <code>droit</code>
     * @return <code>true</code> si un montant est du, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculModeAbstract#droitsCalcules
     */
    protected boolean setMontantsForDroit(DossierComplexModelRoot dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : dossier is null");
        }

        if (droit == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : droit is null");
        }

        if (tarifs == null) {
            throw new ALCalculException("CalculModeAbstract#setMontantsForDroit : tarifs is null");
        }

        if (((tarifs.getSize() > 0) || droit.getDroitModel().getForce())
                && ALCSDroit.ETAT_A.equals(droit.getDroitModel().getEtatDroit())) {

            droitsCalcules = ALImplServiceLocator.getCalculMontantsService().addDroitCalculeActif(
                    dossier.getDossierModel(), droit, tarifs, usedTarif, droitsCalcules, String.valueOf(rang + 1),
                    tarifs.getForValidite());
            return true;

        } else {
            droitsCalcules = ALImplServiceLocator.getCalculMontantsService().addDroitCalculeInactif(droit,
                    droitsCalcules, tarifs.getForValidite());
            return false;
        }
    }
}