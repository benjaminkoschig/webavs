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
 * Contexte de prestation utilis� pour la g�n�ration de prestations. Il contient les informations li�es au dossier
 * 
 * @author jts
 */
public class ContextDossier {

    /**
     * Retourne une instance de <code>ContextDossier</code>
     * 
     * @param dossier Mod�le du dossier
     * @param debutPeriode d�but de la p�riode � traiter (format MM.AAAA)
     * @param finPeriode fin de la p�riode � traiter (format MM.AAAA)
     * @param montantForce Montant forc�
     * @param contextAffilie context d'affili� auquel est li� le context de dossier
     * @param bonification Indique s'il s'agit d'une demande de restitution
     * @param nbUnites nombre d'unit�. Utilis� pour les dossiers � l'heure ou au jour
     * @param typeGen Type de g�n�ration ex�cut� pour le dossier
     * 
     * @return une nouvelle instance de <code>ContextDossier</code>
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * R�sultat du calcul
     */
    private List<CalculBusinessModel> calcul = null;

    /**
     * Contexte d'affili� auquel est li� le contexte de dossier
     */
    private ContextAffilie contextAffilie = null;
    /**
     * P�riode (mois) actuellement en cours de traitement
     */
    private String currentPeriode = null;
    /**
     * D�but de la p�riode
     */
    private String debutPeriode = null;
    /**
     * Mod�le du dossier
     */
    private DossierComplexModel dossier = null;
    /**
     * Fin de la p�riode
     */
    private String finPeriode = null;
    /**
     * Indique si des prestations ont �t� g�n�r�e pendant le traitement en cours
     */
    private boolean hasPrestations = false;
    /**
     * Permet de d�finir l'id d'un droit � g�n�rer. Si cette valeur n'est pas d�finie ou vaut 0, tous les droits du
     * dossier seront g�n�r�s
     */
    private String idDroit;
    /**
     * Montant forc�. Utilis� dans le cas d'une g�n�ration unitaire (dossier) avec un montant forc� au moment de la
     * g�n�ration
     */
    private String montantForce;
    /**
     * Nombre de jour de d�but ou de fin
     */
    private String nbJourDebutOuFin;
    /**
     * Nombre d'unit� � g�n�rer dans le cas d'un dossier � l'heure ou au jour
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL
     */
    private String nbUnites;

    /**
     * Contexte de prestation utilis� pour les prestations en paiement direct (genre ind�pendant)
     */
    private ContextPrestation prestDirectIndep = null;

    /**
     * Contexte de prestation utilis� pour les prestations en paiement direct (genre paritaire)
     */
    private ContextPrestation prestDirectPar = null;
    /**
     * Contexte de prestation utilis� pour les prestations en paiement indirect (genre ind�pendant)
     */
    private ContextPrestation prestIndirectIndep = null;
    /**
     * Contexte de prestation utilis� pour les prestations en paiement indirect (genre paritaire)
     */
    private ContextPrestation prestIndirectPar = null;
    /**
     * Contexte de prestation utilis� pour les prestations de naissance en paiement direct (genre ind�pendant)
     */
    private ContextPrestation prestNaisDirectIndep = null;

    /**
     * Contexte de prestation utilis� pour les prestations de naissance en paiement direct (genre paritaire)
     */
    private ContextPrestation prestNaisDirectPar = null;
    /**
     * Contexte de prestation utilis� pour les prestations de naissance en paiement indirect (genre ind�pendant)
     */
    private ContextPrestation prestNaisIndirectIndep = null;
    /**
     * Contexte de prestation utilis� pour les prestations de naissance en paiement indirect (genre paritaire)
     */
    private ContextPrestation prestNaisIndirectPar = null;
    /**
     * Total du calcul pr�c�dent
     */
    private Double totalPrecedent = null;
    /**
     * Type de g�n�ration en cours. Elle permet d'identifier la classe GenPrestation* qui a �t� utilis�e
     */
    private TypeGeneration typeGenPrestation = null;

    /**
     * Unit� � utiliser pour le calcul de la p�riode courante
     */
    private String unite;

    /**
     * Constructeur priv�. Utiliser la m�thode <code>getContextDossier</code> pour r�cup�rer une instance de context
     * 
     * @see ContextDossier#getContextDossier(DossierComplexModel dossier, String debutPeriode, String finPeriode, String montantForce, ContextAffilie contextAffilie, Bonification bonification, String nbUnites, TypeGeneration typeGen)
     */
    private ContextDossier() {
        // Utiliser la m�thode getContextDossier
    }

    /**
     * Ajoute le d�tail de prestation <code>detail</code> au contexte de prestation appropri�
     * 
     * @param detail Le d�tail de prestation � ajouter
     * @param droit Droit auquel est li� le d�tail
     * 
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Cr�e une nouvelle liste calcul pour les cas de g�n�ration pour un seul droit. Seul les r�sultats concernant le
     * droit en cours de g�n�ration sont ajout�s � la liste.
     * 
     * @throws JadeApplicationException Exception lev�e si le droit n'a pas pu �tre trouv�
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
     * V�rifie si le dossier � un montant forc�. Si c'est le cas, un message d'avertissement est ajout� dans le logger
     * 
     * @throws JadeApplicationException Exception lev�e si le message n'a pas pu �tre ajout�
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
     * Retourne une instance de <code>AssuranceInfo</code> correspondant � l'affili�
     * 
     * @return l'assuranceInfo r�cup�r�e
     * @throws JadeApplicationException Exception lev�e si l'un des param�tre n'est pas correct ou si le code du type de
     *             g�n�ration n'a pas pu �tre contr�l�
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     */
    public AssuranceInfo getAssuranceInfo() throws JadePersistenceException, JadeApplicationException {

        if (assuranceInfo == null) {

            assuranceInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                    dossier.getDossierModel(), "01." + getFinPeriode());

            // FIXME: voir pour activer contr�le � chaque p�riode dans une version majeure
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
     * Ex�cute le calcul pour la prochaine p�riode � traiter {@link ContextDossier#getNextPeriode()}.
     * 
     * si une prestation existe d�j� et qu'on est dans le cas d'une g�n�ration globale/affili�, on loggue un
     * avertissement qui sera affich� sur le protocole. Pour le cas d'une g�n�ration de dossier, la v�rification se fait
     * au niveau de la classe de g�n�ration (extourne en cas de prestation existante) Sinon, le calcul pour la p�riode.
     * Si le montant total a chang� par rapport � la p�riode pr�c�dente, les prestations d�j� g�n�r�es sont
     * enregistr�es. Finalement le r�sultat du calcul est retourn�
     * 
     * @return Le r�sultat du calcul, <code>null</code> si toutes les p�riodes du contexte ont �t� ex�cut�e, une
     *         <code>ArrayList</code> vide si une prestation existe d�j�
     * 
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
     */
    @SuppressWarnings("unchecked")
    public List<CalculBusinessModel> getCalcul() throws JadeApplicationException, JadePersistenceException {

        String periode = getNextPeriode();

        if (periode == null) {
            return null;
        } else {

            // si une prestation existe d�j� et qu'on est dans le cas d'une
            // g�n�ration globale/affili�, on loggue un avertissement qui sera
            // affich� sur le protocole.
            // Pour le cas d'une g�n�ration de dossier, la v�rification se fait
            // au niveau de la classe de g�n�ration (extourne en cas de
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

                // si le dossier n'est pas valide pour la p�riode � g�n�rer
            } else if (JadeDateUtil.isDateBefore(dossier.getDossierModel().getFinValidite(), "01." + periode)
                    || JadeDateUtil.isDateBefore(ALDateUtils.getDateFinMoisPourPeriode(periode), dossier
                            .getDossierModel().getDebutActivite())) {

                calcul = new ArrayList<>();
                return calcul;

                // sinon, ex�cution du calcul pour la p�riode. Si le montant
                // total a chang� par rapport � la p�riode pr�c�dente, les
                // prestations d�j� g�n�r�e sont enregistr�es.
                // Finalement le r�sultat du calcul est retourn�
            } else {
                // On calcule le nombre de jour uniquement si l'unit� du dossier n'est pas en heure
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
     * Retourne le contexte d'affili� auquel est li� le dossier
     * 
     * @return contexte d'affili�
     */
    public ContextAffilie getContextAffilie() {
        return contextAffilie;
    }

    /**
     * Retourne le nombre de de jour de d�but ou de fin actuellement d�fini
     * 
     * @return nombre de jour
     * @see ContextDossier#getNbJourDebutOuFin()
     */
    public String getCurrentNbJourDebutOuFin() {
        return nbJourDebutOuFin;
    }

    /**
     * Retourne la p�riode (mois) actuellement en cours de traitement.
     * 
     * @return the currentPeriode
     */
    public String getCurrentPeriode() {
        return currentPeriode;
    }

    /**
     * Retourne le d�but de la p�riode � traiter
     * 
     * @return the debutPeriode
     */
    public String getDebutPeriode() {
        return debutPeriode;
    }

    /**
     * Retourne le mod�le du dossier
     * 
     * @return mod�le de dossier
     */
    public DossierComplexModel getDossier() {
        return dossier;
    }

    /**
     * @return Fin de la p�riode � g�n�rer
     */
    public String getFinPeriode() {
        return finPeriode;
    }

    /**
     * Permet de retourner l'id du droit � g�n�rer.
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * Retourne le montant forc� au moment de la g�n�ration ou, s'il n'est pas d�fini, celui du dossier. Si aucun
     * montant n'est forc�, retourne 0.
     * 
     * @return le montant forc�
     */
    public String getMontantForce() {
        return !JadeStringUtil.isBlankOrZero(montantForce) ? montantForce : dossier.getDossierModel().getMontantForce();
    }

    /**
     * V�rifie si la p�riode en cours de traitement a un nombre de jours de d�but ou de fin. Cette m�thode est utilis�
     * pour traiter les cas de dossiers mensuels dont la prestation de d�but ou de fin de validit� n'est pas due en
     * totalit� (la validit� du dossier ne commence pas le premier jour du mois ou ne se termine pas le dernier jour du
     * mois).
     * 
     * @return Nombre de jours si l'une de ces valeurs est d�finie et que la p�riode en cours de traitement est
     *         concern�e (premier ou dernier mois de validit� du dossier). Si ce n'est pas le cas, retourne
     *         <code>null</code>
     */
    public String getNbJourDebutOuFin() {

        String debVal = (JadeStringUtil.isEmpty(dossier.getDossierModel().getDebutValidite()) ? "" : dossier
                .getDossierModel().getDebutValidite().substring(3));
        String finVal = (JadeStringUtil.isEmpty(dossier.getDossierModel().getFinValidite()) ? "" : dossier
                .getDossierModel().getFinValidite().substring(3));

        // d�but de p�riode
        if ((debVal.equals(currentPeriode) && !JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel()
                .getNbJoursDebut()))) {
            return dossier.getDossierModel().getNbJoursDebut();
            // fin de p�riode
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
     * Retourne la prochaine p�riode (mois) � traiter. Si <code>currentPeriode</code> n'est pas d�fini elle est
     * initialis� avec <code>debutPeriode</code> et est retourn�. Sinon la p�riode est incr�ment�e avant d'�tre
     * retourn�e. Si tous les mois ont �t� trait� <code>currentPeriode</code> est d�fini � <code>null</code> et est
     * retourn�
     * 
     * @return p�riode sous la forme MM.AAAA
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Identifie le type de montant forc�.
     * 
     * @return Type de montant forc�
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
     * V�rifie si des prestations sont en attente d'�tre enregistr�es.
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
     * @return <code>true</code> si l'unit� du dossier correspond � {@link ALCSDossier#UNITE_CALCUL_HEURE}
     */
    private boolean hasUniteHeure() {
        return ALCSDossier.UNITE_CALCUL_HEURE.equals(dossier.getDossierModel().getUniteCalcul());
    }

    /**
     * Initialise les contextes de prestation.
     * 
     * @param context contexte de dossier auquel les contextes de prestation doivent �tre li�s
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Indique si la g�n�ration en cours est une demande d'extourne
     * 
     * @return <code>true</code> en cas d'extourne
     */
    public boolean isExtourne() {
        return Bonification.EXTOURNE.equals(bonification);
    }

    /**
     * Indique si la g�n�ration en cours est une demande de restitution
     * 
     * @return <code>true</code> en cas de restitution
     */
    public boolean isRestitution() {
        return Bonification.RESTITUTION.equals(bonification);
    }

    /**
     * V�rifie si une prestation existe d�j� pour la prestation en cours
     * 
     * @return <code>true</code> si une prestation existe d�j�, false sinon
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�
     * @see ContextDossier#currentPeriode
     */
    public boolean prestationExistsForCurrentPeriod() throws JadePersistenceException, JadeApplicationException {
        return prestationExistsForPeriod(currentPeriode);
    }

    /**
     * V�rifie si une prestation (en-t�te) existe d�j� pour ce dossier � la <code>periode</code> indiqu�e
     * 
     * @param periode P�riode � contr�ler
     * @return <code>true</code> si une prestation (en-t�te) existe d�j� pour ce dossier � la p�riode indiqu�e,
     *         <code>false</code> sinon.
     * 
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Traite les messages d'erreur du log du thread courant et les place dans le logger global de la g�n�ration de
     * prestations
     * 
     * @throws JadeApplicationException Exception lev�e si des messages ne peuvent �tre trait�s
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
     * Enregistre puis lib�re les contextes de prestations
     * 
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Effectue un rollback de la session (dossier et prestations) puis r�initialise les contextes de prestations
     * 
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Enregistre les prestations puis r�initialise les contextes des prestation
     * 
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de
     *             persistence n'a pu se faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration
     *             souhait�e
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
     * Permet de d�finir l'id d'un droit � g�n�rer. Si cette valeur n'est pas d�finie ou vaut 0, tous les droits du
     * dossier seront g�n�r�s
     * 
     * @param idDroit id du droit � g�n�rer
     * @throws JadeApplicationException Exception lev�e si le param�tre n'est pas une valeur num�rique
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
     * D�fini l'unit� pour le calcul et le nombre de jour de d�but ou de fin
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