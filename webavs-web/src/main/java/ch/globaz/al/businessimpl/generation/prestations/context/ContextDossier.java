package ch.globaz.al.businessimpl.generation.prestations.context;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstPrestations.TypeGeneration;
import ch.globaz.al.business.constantes.ALConstPrestations.TypeMontantForce;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.naos.business.data.AssuranceInfo;

/**
 * Contexte de prestation utilisé pour la génération de prestations. Il contient les informations liées au dossier
 * 
 * @author jts
 */
public class ContextDossier {

    /**
     * Retourne une instance de <code>ContextDossier</code>
     * 
     * @param dossier Modèle du dossier
     * @param debutPeriode début de la période à traiter (format MM.AAAA)
     * @param finPeriode fin de la période à traiter (format MM.AAAA)
     * @param montantForce Montant forcé
     * @param contextAffilie context d'affilié auquel est lié le context de dossier
     * @param bonification Indique s'il s'agit d'une demande de restitution
     * @param nbUnites nombre d'unité. Utilisé pour les dossiers à l'heure ou au jour
     * @param typeGen Type de génération exécuté pour le dossier
     * 
     * @return une nouvelle instance de <code>ContextDossier</code>
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public static ContextDossier getContextDossier(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String montantForce, ContextAffilie contextAffilie, Bonification bonification, String nbUnites,
            TypeGeneration typeGen) throws JadeApplicationException {

        if (dossier == null) {
            throw new ALGenerationPrestationsContextException("ContextDossier#getContextDossier : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextDossier#getContextDossier : " + debutPeriode
                    + "is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(finPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextDossier#getContextDossier : " + finPeriode
                    + "is not a valid period");
        }

        if (JadeDateUtil.isDateMonthYearBefore(finPeriode, debutPeriode)) {
            throw new ALGenerationPrestationsContextException("ContextDossier#getContextDossier : " + finPeriode
                    + " must be after (or equal) " + debutPeriode);
        }

        if (!JadeStringUtil.isEmpty(montantForce) && !JadeNumericUtil.isNumeric(montantForce)) {
            throw new ALGenerationException("ContextDossier#getContextDossier : " + montantForce
                    + " is not a numeric value");
        }

        if (JadeStringUtil.isEmpty(nbUnites)
                || (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites))) {
            throw new ALGenerationPrestationsContextException("ContextDossier#getContextDossier : " + nbUnites
                    + " is not an unsigned integer");
        }

        ContextDossier context = new ContextDossier();
        context.dossier = dossier;
        context.contextAffilie = contextAffilie;
        context.debutPeriode = debutPeriode;
        context.finPeriode = finPeriode;
        context.montantForce = montantForce;
        context.bonification = bonification;
        context.unite = dossier.getDossierModel().getUniteCalcul();
        context.nbUnites = nbUnites;
        context.typeGenPrestation = typeGen;

        return context;
    }

    /**
     * Information sur l'assurance
     */
    private AssuranceInfo assuranceInfo = null;

    /**
     * Indique s'il s'agit d'une restitution
     */
    private Bonification bonification;

    /**
     * Résultat du calcul
     */
    private List<CalculBusinessModel> calcul = null;

    /**
     * Contexte d'affilié auquel est lié le contexte de dossier
     */
    private ContextAffilie contextAffilie = null;
    /**
     * Période (mois) actuellement en cours de traitement
     */
    private String currentPeriode = null;
    /**
     * Début de la période
     */
    private String debutPeriode = null;
    /**
     * Modèle du dossier
     */
    private DossierComplexModel dossier = null;
    /**
     * Fin de la période
     */
    private String finPeriode = null;
    /**
     * Indique si des prestations ont été générée pendant le traitement en cours
     */
    private boolean hasPrestations = false;
    /**
     * Permet de définir l'id d'un droit à générer. Si cette valeur n'est pas définie ou vaut 0, tous les droits du
     * dossier seront générés
     */
    private String idDroit;
    /**
     * Montant forcé. Utilisé dans le cas d'une génération unitaire (dossier) avec un montant forcé au moment de la
     * génération
     */
    private String montantForce;
    /**
     * Nombre de jour de début ou de fin
     */
    private String nbJourDebutOuFin;
    /**
     * Nombre d'unité à générer dans le cas d'un dossier à l'heure ou au jour
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL
     */
    private String nbUnites;

    /**
     * Contexte de prestation utilisé pour les prestations en paiement direct (genre indépendant)
     */
    private ContextPrestation prestDirectIndep = null;

    /**
     * Contexte de prestation utilisé pour les prestations en paiement direct (genre paritaire)
     */
    private ContextPrestation prestDirectPar = null;
    /**
     * Contexte de prestation utilisé pour les prestations en paiement indirect (genre indépendant)
     */
    private ContextPrestation prestIndirectIndep = null;
    /**
     * Contexte de prestation utilisé pour les prestations en paiement indirect (genre paritaire)
     */
    private ContextPrestation prestIndirectPar = null;
    /**
     * Contexte de prestation utilisé pour les prestations de naissance en paiement direct (genre indépendant)
     */
    private ContextPrestation prestNaisDirectIndep = null;

    /**
     * Contexte de prestation utilisé pour les prestations de naissance en paiement direct (genre paritaire)
     */
    private ContextPrestation prestNaisDirectPar = null;
    /**
     * Contexte de prestation utilisé pour les prestations de naissance en paiement indirect (genre indépendant)
     */
    private ContextPrestation prestNaisIndirectIndep = null;
    /**
     * Contexte de prestation utilisé pour les prestations de naissance en paiement indirect (genre paritaire)
     */
    private ContextPrestation prestNaisIndirectPar = null;
    /**
     * Total du calcul précédent
     */
    private Double totalPrecedent = null;
    /**
     * Type de génération en cours. Elle permet d'identifier la classe GenPrestation* qui a été utilisée
     */
    private TypeGeneration typeGenPrestation = null;

    /**
     * Unité à utiliser pour le calcul de la période courante
     */
    private String unite;

    /**
     * Constructeur privé. Utiliser la méthode <code>getContextDossier</code> pour récupérer une instance de context
     * 
     * @see ContextDossier#getContextDossier(DossierComplexModel dossier, String debutPeriode, String finPeriode, String montantForce, ContextAffilie contextAffilie, Bonification bonification, String nbUnites, TypeGeneration typeGen)
     */
    private ContextDossier() {
        // Utiliser la méthode getContextDossier
    }

    /**
     * Ajoute le détail de prestation <code>detail</code> au contexte de prestation approprié
     * 
     * @param detail Le détail de prestation à ajouter
     * @param droit Droit auquel est lié le détail
     * 
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void addDetailPrestation(DetailPrestationModel detail, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {

        if (detail == null) {
            throw new ALGenerationPrestationsContextException("ContextDossier#addDetailPrestation : detail is null");
        }

        initContextsPrestation(this);

        String genre = ALImplServiceLocator.getDossierBusinessService().getGenreAssurance(
                dossier.getDossierModel().getActiviteAllocataire());

        hasPrestations = true;

        boolean isNAIS = false;
        if ((droit != null)
                && (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation()) || ALCSDroit.TYPE_ACCE.equals(detail
                        .getTypePrestation()))) {
            isNAIS = true;
        }

        if (ALCSAffilie.GENRE_ASSURANCE_PARITAIRE.equals(genre)) {
            // cas de paiement direct
            if (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getIdTiersBeneficiaire())) {
                if (isNAIS) {

                    if (droit == null) {
                        throw new ALGenerationPrestationsContextException(
                                "ContextDossier#addDetailPrestation : droit is null");
                    }

                    prestNaisDirectPar.addDetailNaissance(detail, droit.getEnfantComplexModel().getEnfantModel());
                } else {
                    prestDirectPar.addDetail(detail, droit.getEnfantComplexModel().getEnfantModel());
                }
                // cas de paiement indirect
            } else {
                if (isNAIS) {

                    if (droit == null) {
                        throw new ALGenerationPrestationsContextException(
                                "ContextDossier#addDetailPrestation : droit is null");
                    }

                    prestNaisIndirectPar.addDetailNaissance(detail, droit.getEnfantComplexModel().getEnfantModel());
                } else {
                    prestIndirectPar.addDetail(detail, droit.getEnfantComplexModel().getEnfantModel());
                }
            }
        } else if (ALCSAffilie.GENRE_ASSURANCE_INDEP.equals(genre)) {
            // cas de paiement direct
            if (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getIdTiersBeneficiaire())) {
                if (isNAIS) {

                    if (droit == null) {
                        throw new ALGenerationPrestationsContextException(
                                "ContextDossier#addDetailPrestation : droit is null");
                    }

                    prestNaisDirectIndep.addDetailNaissance(detail, droit.getEnfantComplexModel().getEnfantModel());
                } else {
                    prestDirectIndep.addDetail(detail, droit.getEnfantComplexModel().getEnfantModel());
                }
                // cas de paiement indirect
            } else {
                if (isNAIS) {

                    if (droit == null) {
                        throw new ALGenerationPrestationsContextException(
                                "ContextDossier#addDetailPrestation : droit is null");
                    }

                    prestNaisIndirectIndep.addDetailNaissance(detail, droit.getEnfantComplexModel().getEnfantModel());
                } else {
                    prestIndirectIndep.addDetail(detail, droit.getEnfantComplexModel().getEnfantModel());
                }
            }
        }
    }

    /**
     * Crée une nouvelle liste calcul pour les cas de génération pour un seul droit. Seul les résultats concernant le
     * droit en cours de génération sont ajoutés à la liste.
     * 
     * @throws JadeApplicationException Exception levée si le droit n'a pas pu être trouvé
     */
    private void calculFilter() throws JadeApplicationException {
        if (JadeNumericUtil.isIntegerPositif(idDroit)) {

            List<CalculBusinessModel> calculNew = new ArrayList<>();

            for (CalculBusinessModel item : calcul) {
                if (idDroit.equals(item.getDroit().getId())) {
                    calculNew.add(item);
                }
            }

            calcul = calculNew;

            if (calcul.size() == 0) {
                throw new ALGenerationPrestationsContextException(
                        "ContextDossier#calculFilter : unable to find idDroit '" + idDroit + "' in calcul list");
            }
        }
    }

    /**
     * Vérifie si le dossier à un montant forcé. Si c'est le cas, un message d'avertissement est ajouté dans le logger
     * 
     * @throws JadeApplicationException Exception levée si le message n'a pas pu être ajouté
     */
    public void checkMontantForce() throws JadeApplicationException {
        if (hasPrestations && JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getMontantForce())) {

            List<String> csvVal = new ArrayList<>();
            csvVal.add(dossier.getDossierModel().getIdDossier());
            csvVal.add(getContextAffilie().getNumAffilie());

            contextAffilie
                    .getLogger()
                    .getWarningsLogger(
                            dossier.getDossierModel().getIdDossier() + "/" + getContextAffilie().getNumAffilie(),
                            dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                    .getDesignation1()
                                    + " "
                                    + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                            .getDesignation2(), csvVal)
                    .addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ContextDossier.class.getName(),
                                    "al.generation.warning.dossierMontantForce"));
        }
    }

    /**
     * Retourne une instance de <code>AssuranceInfo</code> correspondant à l'affilié
     * 
     * @return l'assuranceInfo récupérée
     * @throws JadeApplicationException Exception levée si l'un des paramètre n'est pas correct ou si le code du type de
     *             génération n'a pas pu être contrôlé
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public AssuranceInfo getAssuranceInfo() throws JadePersistenceException, JadeApplicationException {

        if (assuranceInfo == null) {

            assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                    dossier.getDossierModel(), "01." + getFinPeriode());

            // FIXME: voir pour activer contrôle à chaque période dans une version majeure
            // if (this.getCurrentPeriode() == null) {
            // this.assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
            // this.dossier.getDossierModel(), "01." + this.getFinPeriode());
            // } else {
            // this.assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
            // this.dossier.getDossierModel(), "01." + this.getCurrentPeriode());
            // }

        }

        return assuranceInfo;
    }

    /**
     * Exécute le calcul pour la prochaine période à traiter {@link ContextDossier#getNextPeriode()}.
     * 
     * si une prestation existe déjà et qu'on est dans le cas d'une génération globale/affilié, on loggue un
     * avertissement qui sera affiché sur le protocole. Pour le cas d'une génération de dossier, la vérification se fait
     * au niveau de la classe de génération (extourne en cas de prestation existante) Sinon, le calcul pour la période.
     * Si le montant total a changé par rapport à la période précédente, les prestations déjà générées sont
     * enregistrées. Finalement le résultat du calcul est retourné
     * 
     * @return Le résultat du calcul, <code>null</code> si toutes les périodes du contexte ont été exécutée, une
     *         <code>ArrayList</code> vide si une prestation existe déjà
     * 
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    @SuppressWarnings("unchecked")
    public List<CalculBusinessModel> getCalcul() throws JadeApplicationException, JadePersistenceException {

        String periode = getNextPeriode();

        if (periode == null) {
            return null;
        } else {

            // si une prestation existe déjà et qu'on est dans le cas d'une
            // génération globale/affilié, on loggue un avertissement qui sera
            // affiché sur le protocole.
            // Pour le cas d'une génération de dossier, la vérification se fait
            // au niveau de la classe de génération (extourne en cas de
            // prestation existante)
            if (!ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER.equals(contextAffilie.getTypeGeneration())
                    && prestationExistsForPeriod(periode)) {

                List<String> csvVal = new ArrayList<>();
                csvVal.add(dossier.getDossierModel().getIdDossier());
                csvVal.add(getContextAffilie().getNumAffilie());

                contextAffilie
                        .getLogger()
                        .getWarningsLogger(
                                dossier.getDossierModel().getIdDossier() + "/" + getContextAffilie().getNumAffilie(),
                                dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation1()
                                        + " "
                                        + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                                .getTiers().getDesignation2(), csvVal)
                        .addMessage(
                                new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ContextDossier.class.getName(),
                                        "al.generation.warning.prestationExistante", new String[] { periode }));

                calcul = new ArrayList<>();
                return calcul;

                // si le dossier n'est pas valide pour la période à générer
            } else if (JadeDateUtil.isDateBefore(dossier.getDossierModel().getFinValidite(), "01." + periode)
                    || JadeDateUtil.isDateBefore(ALDateUtils.getDateFinMoisPourPeriode(periode), dossier
                            .getDossierModel().getDebutActivite())) {

                calcul = new ArrayList<>();
                return calcul;

                // sinon, exécution du calcul pour la période. Si le montant
                // total a changé par rapport à la période précédente, les
                // prestations déjà générée sont enregistrées.
                // Finalement le résultat du calcul est retourné
            } else {
                // On calcule le nombre de jour uniquement si l'unité du dossier n'est pas en heure
                if (!hasUniteHeure()) {
                    setUniteEtNombreJours();
                }

                calcul = ALImplServiceLocator.getCalculService().getCalcul(dossier,
                        ALDateUtils.getDateFinMoisPourPeriode(periode), getAssuranceInfo());

                calculFilter();

                Map total = ALImplServiceLocator.getCalculMontantsService()
                        .calculerTotalMontant(dossier, calcul, unite,
                                (JadeStringUtil.isEmpty(nbJourDebutOuFin) ? nbUnites : nbJourDebutOuFin), true,
                                "01." + periode);

                calcul = (List) total.get(ALConstCalcul.DROITS_CALCULES);
                Double montantTotal = new Double((String) total.get(ALConstCalcul.TOTAL_EFFECTIF));

                if (totalPrecedent == null) {
                    totalPrecedent = montantTotal;
                } else if (totalPrecedent.compareTo(montantTotal) != 0) {

                    totalPrecedent = montantTotal;

                    if (prestDirectIndep != null) {
                        prestDirectIndep.save();
                    }
                    if (prestIndirectIndep != null) {
                        prestIndirectIndep.save();
                    }
                    prestDirectIndep = null;
                    prestIndirectIndep = null;

                    if (prestDirectPar != null) {
                        prestDirectPar.save();
                    }
                    if (prestIndirectPar != null) {
                        prestIndirectPar.save();
                    }
                    prestDirectPar = null;
                    prestIndirectPar = null;
                }

                return calcul;
            }
        }
    }

    /**
     * Retourne le contexte d'affilié auquel est lié le dossier
     * 
     * @return contexte d'affilié
     */
    public ContextAffilie getContextAffilie() {
        return contextAffilie;
    }

    /**
     * Retourne le nombre de de jour de début ou de fin actuellement défini
     * 
     * @return nombre de jour
     * @see ContextDossier#getNbJourDebutOuFin()
     */
    public String getCurrentNbJourDebutOuFin() {
        return nbJourDebutOuFin;
    }

    /**
     * Retourne la période (mois) actuellement en cours de traitement.
     * 
     * @return the currentPeriode
     */
    public String getCurrentPeriode() {
        return currentPeriode;
    }

    /**
     * Retourne le début de la période à traiter
     * 
     * @return the debutPeriode
     */
    public String getDebutPeriode() {
        return debutPeriode;
    }

    /**
     * Retourne le modèle du dossier
     * 
     * @return modèle de dossier
     */
    public DossierComplexModel getDossier() {
        return dossier;
    }

    /**
     * @return Fin de la période à générer
     */
    public String getFinPeriode() {
        return finPeriode;
    }

    /**
     * Permet de retourner l'id du droit à générer.
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * Retourne le montant forcé au moment de la génération ou, s'il n'est pas défini, celui du dossier. Si aucun
     * montant n'est forcé, retourne 0.
     * 
     * @return le montant forcé
     */
    public String getMontantForce() {
        return !JadeStringUtil.isBlankOrZero(montantForce) ? montantForce : dossier.getDossierModel().getMontantForce();
    }

    /**
     * Vérifie si la période en cours de traitement a un nombre de jours de début ou de fin. Cette méthode est utilisé
     * pour traiter les cas de dossiers mensuels dont la prestation de début ou de fin de validité n'est pas due en
     * totalité (la validité du dossier ne commence pas le premier jour du mois ou ne se termine pas le dernier jour du
     * mois).
     * 
     * @return Nombre de jours si l'une de ces valeurs est définie et que la période en cours de traitement est
     *         concernée (premier ou dernier mois de validité du dossier). Si ce n'est pas le cas, retourne
     *         <code>null</code>
     */
    public String getNbJourDebutOuFin() {

        String debVal = (JadeStringUtil.isEmpty(dossier.getDossierModel().getDebutValidite()) ? "" : dossier
                .getDossierModel().getDebutValidite().substring(3));
        String finVal = (JadeStringUtil.isEmpty(dossier.getDossierModel().getFinValidite()) ? "" : dossier
                .getDossierModel().getFinValidite().substring(3));

        // début de période
        if ((debVal.equals(currentPeriode) && !JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel()
                .getNbJoursDebut()))) {
            return dossier.getDossierModel().getNbJoursDebut();
            // fin de période
        } else if ((finVal.equals(currentPeriode) && !JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel()
                .getNbJoursFin()))) {
            return dossier.getDossierModel().getNbJoursFin();
        }
        return null;
    }

    /**
     * @return the nbUnites
     */
    public String getNbUnites() {
        return nbUnites;
    }

    /**
     * Retourne la prochaine période (mois) à traiter. Si <code>currentPeriode</code> n'est pas défini elle est
     * initialisé avec <code>debutPeriode</code> et est retourné. Sinon la période est incrémentée avant d'être
     * retournée. Si tous les mois ont été traité <code>currentPeriode</code> est défini à <code>null</code> et est
     * retourné
     * 
     * @return période sous la forme MM.AAAA
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public String getNextPeriode() throws JadeApplicationException {

        if (currentPeriode == null) {
            currentPeriode = debutPeriode;
        } else {
            Calendar periode = ALDateUtils.getCalendarDate("01." + currentPeriode);

            int mois = periode.get(Calendar.MONTH) + 2;
            int annee = periode.get(Calendar.YEAR);

            if (mois > 12) {
                currentPeriode = "01." + String.valueOf(++annee);
            } else {
                currentPeriode = String.valueOf(mois) + "." + annee;

                if (currentPeriode.length() < 7) {
                    currentPeriode = "0" + currentPeriode;
                }
            }

            if (JadeDateUtil.isDateMonthYearBefore(finPeriode, currentPeriode)) {
                currentPeriode = null;
            }
        }

        return currentPeriode;
    }

    /**
     * @return the totalPrecedent
     */
    public Double getTotalPrecedent() {
        return totalPrecedent;
    }

    /**
     * @return the typeGenPrestation
     */
    public TypeGeneration getTypeGenPrestation() {
        return typeGenPrestation;
    }

    /**
     * Identifie le type de montant forcé.
     * 
     * @return Type de montant forcé
     */
    public TypeMontantForce getTypeMontantForce() {

        if (!JadeNumericUtil.isEmptyOrZero(montantForce)) {
            return TypeMontantForce.MONTANT_FORCE_GEN;
        } else if (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getMontantForce())) {
            return TypeMontantForce.MONTANT_FORCE_DOSSIER;
        } else {
            return TypeMontantForce.MONTANT_FORCE_AUCUN;
        }

    }

    /**
     * @return <code>true</code> si le context est en erreur, <code>false</code> sinon
     */
    public boolean hasError() {
        return (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.ERROR);
    }

    /**
     * Vérifie si des prestations sont en attente d'être enregistrées.
     * 
     * @return <code>true</code> si une ou plusieurs prestation sont en attente, <code>false</code> sinon.
     */
    public boolean hasPrestations() {
        return (((prestDirectIndep != null) && prestDirectIndep.hasPrestations())
                || ((prestIndirectIndep != null) && prestIndirectIndep.hasPrestations())
                || ((prestNaisDirectIndep != null) && prestNaisDirectIndep.hasPrestations())
                || ((prestNaisIndirectIndep != null) && prestNaisIndirectIndep.hasPrestations())
                || ((prestDirectPar != null) && prestDirectPar.hasPrestations())
                || ((prestIndirectPar != null) && prestIndirectPar.hasPrestations())
                || ((prestNaisDirectPar != null) && prestNaisDirectPar.hasPrestations()) || ((prestNaisIndirectPar != null) && prestNaisIndirectPar
                .hasPrestations()));
    }

    /**
     * 
     * @return <code>true</code> si l'unité du dossier correspond à {@link ALCSDossier#UNITE_CALCUL_HEURE}
     */
    private boolean hasUniteHeure() {
        return ALCSDossier.UNITE_CALCUL_HEURE.equals(dossier.getDossierModel().getUniteCalcul());
    }

    /**
     * Initialise les contextes de prestation.
     * 
     * @param context contexte de dossier auquel les contextes de prestation doivent être liés
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    private void initContextsPrestation(ContextDossier context) throws JadeApplicationException {

        if (context == null) {
            throw new ALGenerationPrestationsContextException("ContextDossier#initContextsPrestation : context is null");
        }

        if (context.prestDirectIndep == null) {
            context.prestDirectIndep = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_DIRECT), unite);
        }

        if (context.prestIndirectIndep == null) {
            context.prestIndirectIndep = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_INDIRECT), unite);
        }

        if (context.prestNaisDirectIndep == null) {
            context.prestNaisDirectIndep = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_DIRECT),
                    ALCSDossier.UNITE_CALCUL_SPECIAL);
        }

        if (context.prestNaisIndirectIndep == null) {
            context.prestNaisIndirectIndep = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_INDIRECT),
                    ALCSDossier.UNITE_CALCUL_SPECIAL);
        }

        if (context.prestDirectPar == null) {
            context.prestDirectPar = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_DIRECT), unite);
        }

        if (context.prestIndirectPar == null) {
            context.prestIndirectPar = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_INDIRECT), unite);
        }

        if (context.prestNaisDirectPar == null) {
            context.prestNaisDirectPar = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_DIRECT),
                    ALCSDossier.UNITE_CALCUL_SPECIAL);
        }

        if (context.prestNaisIndirectPar == null) {
            context.prestNaisIndirectPar = ContextPrestation.getContextPrestation(this,
                    (context.isRestitution() ? ALCSPrestation.BONI_RESTITUTION : ALCSPrestation.BONI_INDIRECT),
                    ALCSDossier.UNITE_CALCUL_SPECIAL);
        }
    }

    /**
     * Indique si la génération en cours est une demande d'extourne
     * 
     * @return <code>true</code> en cas d'extourne
     */
    public boolean isExtourne() {
        return Bonification.EXTOURNE.equals(bonification);
    }

    /**
     * Indique si la génération en cours est une demande de restitution
     * 
     * @return <code>true</code> en cas de restitution
     */
    public boolean isRestitution() {
        return Bonification.RESTITUTION.equals(bonification);
    }

    /**
     * Vérifie si une prestation existe déjà pour la prestation en cours
     * 
     * @return <code>true</code> si une prestation existe déjà, false sinon
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaité
     * @see ContextDossier#currentPeriode
     */
    public boolean prestationExistsForCurrentPeriod() throws JadePersistenceException, JadeApplicationException {
        return prestationExistsForPeriod(currentPeriode);
    }

    /**
     * Vérifie si une prestation (en-tête) existe déjà pour ce dossier à la <code>periode</code> indiquée
     * 
     * @param periode Période à contrôler
     * @return <code>true</code> si une prestation (en-tête) existe déjà pour ce dossier à la période indiquée,
     *         <code>false</code> sinon.
     * 
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    private boolean prestationExistsForPeriod(String periode) throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALGenerationPrestationsContextException("ContextDossier#prestationExistsForPeriod : " + periode
                    + " is not a valid period");
        }

        EntetePrestationSearchModel epsm = new EntetePrestationSearchModel();
        epsm.setWhereKey("prestationExistante");
        epsm.setForIdDossier(dossier.getDossierModel().getIdDossier());
        epsm.setForPeriode(periode);

        return (ALImplServiceLocator.getEntetePrestationModelService().count(epsm) > 0);
    }

    /**
     * Traite les messages d'erreur du log du thread courant et les place dans le logger global de la génération de
     * prestations
     * 
     * @throws JadeApplicationException Exception levée si des messages ne peuvent être traités
     */
    private void processMessages() throws JadeApplicationException {

        checkMontantForce();

        JadeBusinessMessage[] messages = JadeThread.logMessages();

        for (int i = 0; (messages != null) && (i < messages.length); i++) {

            List<String> csvVal = new ArrayList<>();
            csvVal.add(dossier.getDossierModel().getIdDossier());
            csvVal.add(getContextAffilie().getNumAffilie());

            if (JadeBusinessMessageLevels.INFO == messages[i].getLevel()) {

                contextAffilie
                        .getLogger()
                        .getInfosLogger(
                                dossier.getDossierModel().getIdDossier() + "/" + getContextAffilie().getNumAffilie(),
                                dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation1()
                                        + " "
                                        + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                                .getTiers().getDesignation2(), csvVal).addMessage(messages[i]);
            } else if (JadeBusinessMessageLevels.WARN == messages[i].getLevel()) {
                contextAffilie
                        .getLogger()
                        .getWarningsLogger(
                                dossier.getDossierModel().getIdDossier() + "/" + getContextAffilie().getNumAffilie(),
                                dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation1()
                                        + " "
                                        + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                                .getTiers().getDesignation2(), csvVal).addMessage(messages[i]);
            } else if (JadeBusinessMessageLevels.ERROR == messages[i].getLevel()) {
                contextAffilie
                        .getLogger()
                        .getErrorsLogger(
                                dossier.getDossierModel().getIdDossier() + "/" + getContextAffilie().getNumAffilie(),
                                dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation1()
                                        + " "
                                        + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                                .getTiers().getDesignation2(), csvVal).addMessage(messages[i]);
            } else {
                JadeLogger.warn(this, "Unable to render message, no level match!");

            }
        }
    }

    /**
     * Enregistre puis libère les contextes de prestations
     * 
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void releaseDossier() throws JadeApplicationException, JadePersistenceException {

        savePrestations();
        processMessages();

        try {
            if (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.ERROR) {
                JadeThread.rollbackSession();
            } else {
                JadeThread.commitSession();
            }
        } catch (Exception e) {
            throw new ALGenerationPrestationsContextException("ContextDossier#releaseDossier : unable to rollback", e);
        }
    }

    /**
     * Effectue un rollback de la session (dossier et prestations) puis réinitialise les contextes de prestations
     * 
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void rollbackDossier() throws JadeApplicationException {
        try {
            JadeThread.rollbackSession();
        } catch (Exception e) {
            throw new ALGenerationPrestationsContextException("ContextDossier#rollbackDossier : unable to rollback", e);
        } finally {
            prestDirectIndep = null;
            prestIndirectIndep = null;
            prestNaisDirectIndep = null;
            prestNaisIndirectIndep = null;
            prestDirectPar = null;
            prestIndirectPar = null;
            prestNaisDirectPar = null;
            prestNaisIndirectPar = null;
        }
    }

    /**
     * Enregistre les prestations puis réinitialise les contextes des prestation
     * 
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération
     *             souhaitée
     */
    public void savePrestations() throws JadeApplicationException, JadePersistenceException {
        if (prestDirectIndep != null) {
            prestDirectIndep.save();
        }
        if (prestIndirectIndep != null) {
            prestIndirectIndep.save();
        }
        if (prestNaisDirectIndep != null) {
            prestNaisDirectIndep.save();
        }
        if (prestNaisIndirectIndep != null) {
            prestNaisIndirectIndep.save();
        }

        if (prestDirectPar != null) {
            prestDirectPar.save();
        }
        if (prestIndirectPar != null) {
            prestIndirectPar.save();
        }
        if (prestNaisDirectPar != null) {
            prestNaisDirectPar.save();
        }
        if (prestNaisIndirectPar != null) {
            prestNaisIndirectPar.save();
        }

        prestDirectIndep = null;
        prestIndirectIndep = null;
        prestNaisDirectIndep = null;
        prestNaisIndirectIndep = null;
        prestDirectPar = null;
        prestIndirectPar = null;
        prestNaisDirectPar = null;
        prestNaisIndirectPar = null;
    }

    /**
     * Permet de définir l'id d'un droit à générer. Si cette valeur n'est pas définie ou vaut 0, tous les droits du
     * dossier seront générés
     * 
     * @param idDroit id du droit à générer
     * @throws JadeApplicationException Exception levée si le paramètre n'est pas une valeur numérique
     */
    public void setIdDroit(String idDroit) throws JadeApplicationException {

        if (!JadeStringUtil.isBlank(idDroit) && !JadeNumericUtil.isNumeric(idDroit)) {
            throw new ALGenerationPrestationsContextException("ContextDossier#setIdDroit : idDroit is not numeric");
        }

        this.idDroit = idDroit;
    }

    /**
     * @param typeGenPrestation the typeGenPrestation to set
     */
    public void setTypeGenPrestation(TypeGeneration typeGenPrestation) {
        this.typeGenPrestation = typeGenPrestation;
    }

    /**
     * Défini l'unité pour le calcul et le nombre de jour de début ou de fin
     * 
     * @see ContextDossier#getNbJourDebutOuFin()
     * @see ContextDossier#getCalcul()
     */
    private void setUniteEtNombreJours() {
        nbJourDebutOuFin = getNbJourDebutOuFin();

        if (JadeNumericUtil.isEmptyOrZero(nbJourDebutOuFin)) {
            unite = dossier.getDossierModel().getUniteCalcul();
        } else {
            unite = ALCSDossier.UNITE_CALCUL_JOUR;
        }
    }
}