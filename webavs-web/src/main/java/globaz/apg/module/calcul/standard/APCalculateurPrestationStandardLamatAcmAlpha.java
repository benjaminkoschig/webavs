package globaz.apg.module.calcul.standard;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import ch.globaz.common.properties.CommonPropertiesUtils;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.application.APApplication;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.acm.alfa.APCalculateurAcmAlpha;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APDroitLAPGJoinPrestationManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.helpers.droits.APDroitLAPGJointDemandeHelper;
import globaz.apg.helpers.prestation.APPrestationExtensionSplitter;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCalculACORException;
import globaz.apg.module.calcul.APCalculException;
import globaz.apg.module.calcul.APCalculateurFactory;
import globaz.apg.module.calcul.APModuleRepartitionPaiements;
import globaz.apg.module.calcul.APPrestationCalculee;
import globaz.apg.module.calcul.APPrestationStandardLamatAcmAlphaData;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.interfaces.IAPCalculateur;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.lamat.LAMatCalculateur;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapperComparator;
import globaz.apg.properties.APProperties;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.apg.utils.APCalculAcorUtil;
import globaz.apg.vb.droits.APDroitLAPGJointDemandeViewBean;
import globaz.apg.vb.droits.APEnfantMatListViewBean;
import globaz.apg.vb.droits.APEnfantMatViewBean;
import globaz.apg.vb.prestation.APPrestationListViewBean;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Description Calcul de la prestation APG
 *
 *
 *    Cas de figure :
 *                     Définition : [ Prestation   + montant CHF de la prestation ]    [] == date début/date fin de la prestation
 *                                  Rest. == Prestation de restitution
 *                                  PB == période de base
 *                                  PGPC == plus grande période commune
 *                                  PGPCE == plus grande période commune étendue, pour restitutions
 *
 *    Droit id - id parent  (description)          :  Prestations
 *    ------------------------------------------------------------------------------------------------
 *
 *     Droit 1 -   null                            : [    P1         35.-       ][    P2    45.-  ]
 *     Droit 2 -    1      (correction Droit 1)    : [    P3         +5.-       ]
 *
 *     Droit 3 -    1      (+ 1 enfant)            : [    P4      Rest. 40.-    ]
 *                                                 : [  P5 40.-   ][ P6   60.-  ][   P7   +20.-   ]
 *
 *     Droit 4 -    1      (correction)            : [ P8 +5.-    ]              [   P9  + 10.-   ]
 *
 *
 *     Droit 5 -    1      (correction, dans la)   : [Rest.  45.- ][Rest.   60.-]                              -- A3
 *                         (date de naiss. de  )   : ¦  PGPCE  1  ¦¦ PGPCE  2   ¦
 *                         (l'enfant           )
 *                                                 : [    P10  45.-       ][ 60.- ]                            -- A1
 *
 *     Définition pour traitement cas 5            : ¦       PB  1        ¦¦PB 2  ¦                            -- A2
 *                                                 : ¦   PGPC 1   ¦¦PGPC 2¦
 *                                                 :
 *
 *
 *     Algorithme :
 *
 *     Cas droit 5
 *
 *     1) Calcul des prestations sans prises en compte des evt. prestations précédentes (A1)
 *     2) Récupérer les plus grandes périodes communes (pgpc) pour une période donnée (PB (A2))
 *     3) Pour chacune de ces PGPC (PGPC1)
 *         3a) Récupérer toutes les prestations contenues (P1, P3, P4, P5, P8)
 *         3b) Sommer montant brut [MB] (total = 45.-)
 *         3c) Si période de base PB1 != PGPC (PGPC1)
 *             3c1) si MB > 0
 *                     Restituer prestation avec période étendue -- Créer physiquement la prestation en DB à ce moment là.(PGPCE  1) (A3)
 *             3c2) créer obj prestation (P10) avec période = période de base (PB 1) et montant égal au montant calcul au pt. 1)
 *         3d) Sinon (période de base== PGPC)
 *             3d1) Créer obj. prestation avec montant == différence entre MB et montant calculé au point 1)
 *                  La période de cette prestation == à la période de base
 *     4) Parcourir tous les obj. prestations précédemment créé (en mémoire) ordonné par date de début
 *         4a) Si montant prestation courante == montant de la prestation suivante && périodes sont constigües (unité == au jour)
 *             fusionner les 2 prestations
 *     5)Pour chaque object prestation, créer la prestation.
 * </pre>
 *
 * @author SCR
 */
public class APCalculateurPrestationStandardLamatAcmAlpha implements IAPPrestationCalculateur<Object, Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(APCalculateurPrestationStandardLamatAcmAlpha.class);

    // en cas de service d'avancement le salaire est considéré a 100% pendant ce nombre de jours
    private static final int NB_JOURS_APG_PLEINE_SERVICE_AVANCEMENT = 30;

    public static final String PRESTATION_APG = "APG";
    public static final String PRESTATION_MATERNITE = "MATERNITE";
    public static final String PRESTATION_PATERNITE = "PATERNITE";
    public static final String PRESTATION_PANDEMIE = "PANDEMIE";
    public static final String PRESTATION_PROCHE_AIDANT = "PROCHE_AIDANT";
    private static final String REFERENCE_FILE_APG = "calculAPGReferenceData.xml";

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

    private APPrestationViewBean viewBean;

    public APCalculateurPrestationStandardLamatAcmAlpha() {
        super();
    }

    private JADate addDays(final JADate date, final int nombreJours) {
        final JACalendar calendar = new JACalendarGregorian();

        return calendar.addDays(date, nombreJours);
    }

    /**
     * Ajoute une prestation de type Restitution complète dans la DB
     */
    private APPrestation addRestitution(final BSession session, final BTransaction transaction, final String idDroit,
            final APPeriodeWrapper periodeRestitution, final FWCurrency montantJournalier,
            final FWCurrency montantFraisGarde, final FWCurrency montantAllocExploitation,
            final String nombreJoursSoldes, final String noRevision, final String genre) throws Exception {
        final APPrestation restitution = new APPrestation();
        restitution.setSession(session);
        restitution.setContenuAnnonce(IAPAnnonce.CS_RESTITUTION);
        restitution.setDateDebut(periodeRestitution.getDateDebut().toStr("."));
        restitution.setDateFin(periodeRestitution.getDateFin().toStr("."));
        restitution.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
        restitution.setIdDroit(idDroit);
        restitution.setIdRestitution(null);
        restitution.setMontantJournalier(montantJournalier.toString());
        restitution.setNombreJoursSoldes(nombreJoursSoldes);

        BigDecimal mb = montantJournalier.getBigDecimalValue();
        mb = mb.multiply(new BigDecimal(restitution.getNombreJoursSoldes()));
        restitution.setMontantBrut(mb.toString());
        restitution.setFraisGarde(montantFraisGarde.toString());
        restitution.setMontantAllocationExploitation(montantAllocExploitation.toString());
        restitution.setDroitAcquis(null);
        restitution.setType(IAPPrestation.CS_TYPE_ANNULATION);
        restitution.setGenre(genre);
        restitution.setNoRevision(noRevision);
        restitution.setSession(session);
        restitution.add(transaction);
        return restitution;
    }

    /**
     * Ajout une prestation (calculée) à la liste des prestations à créer
     */
    private List<APPrestationCalculee> ajouterPrestationACreer(
            final List<APPrestationCalculee> listDesPrestationsACreer, final APPrestationWrapper prestationWrapper,
            final APPeriodeWrapper pgpc, final APDroitLAPG droit) throws Exception {
        // Ajouter prestation à la liste des prestations à créer
        final APPrestationCalculee prestCalculee = new APPrestationCalculee();
        prestCalculee.setResultatCalcul(prestationWrapper.getPrestationBase());
        prestCalculee.setDateDebut(pgpc.getDateDebut());
        prestCalculee.setDateFin(pgpc.getDateFin());
        prestCalculee.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
        // Code défensif -> impact moindre vu que seule la FERCIAB utilise les jours isolés
        //               -> pour le nodule Pandémie
        if ("true".equals(JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB))
                || droit instanceof APDroitPandemie) {
            prestCalculee.setCsGenrePrestation(prestationWrapper.getPrestationBase().getCsGenrePrestion());
        }

        // gestion du duplicata pour les APG militaire
        if (!IAPDroitMaternite.CS_REVISION_MATERNITE_2005.equals(prestationWrapper.getPrestationBase().getRevision())) {
            final APDroitAPG droitApg = new APDroitAPG();
            droitApg.setSession(droit.getSession());
            droitApg.setIdDroit(droit.getIdDroit());
            droitApg.retrieve();

            prestCalculee.setContenuAnnonce(droitApg.getDuplicata().booleanValue() ? IAPAnnonce.CS_DUPLICATA
                    : IAPAnnonce.CS_DEMANDE_ALLOCATION);
        } else {
            prestCalculee.setContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);
        }

        prestCalculee.setMontantJournalier(prestationWrapper.getPrestationBase().getMontantJournalier());
        prestCalculee.setBasicDailyAmount(prestationWrapper.getPrestationBase().getBasicDailyAmount());
        prestCalculee.setTypePrestation(IAPPrestation.CS_TYPE_NORMAL);
        prestCalculee.setRevenuDeterminantMoyen(new FWCurrency(
                prestationWrapper.getPrestationBase().revenuDeterminantMoyen.getBigDecimalValue().toString(), 3));

        // Lors de la restitution de prestation maternité calculée normalement
        // lors de la creation, mais rétroactivement
        // lors de la correction. Le nombre de jour de la période de base est plus grand que le nombre de jour de la
        // prestation.
        // Dans ce cas le nombre de jour de la période de base est remplace par le nombre de jours de la prestation.
        // !! Nous n'obtenons pas nouvelle une prestations rétroactive regroupée, mais une prestation par période comme
        // lors d'un calcul normal.
        final JACalendar cal = new JACalendarGregorian();
        final long nbJoursPrestation = cal.daysBetween(pgpc.getDateDebut(), pgpc.getDateFin()) + 1;
        final int nbJoursPeriodeBase = prestationWrapper.getPrestationBase().getNombreJoursSoldes();
        if (nbJoursPeriodeBase > nbJoursPrestation) {
            prestCalculee.setNombreJoursSoldes(String.valueOf(nbJoursPrestation));
        } else {
            prestCalculee.setNombreJoursSoldes(String.valueOf(nbJoursPeriodeBase));
        }

        prestCalculee.setIdDroit(droit.getIdDroit());

        // BZ6749 : Inforom 462
        if (IAPDroitAPG.CS_IJ_ASSURANCE_CHOMAGE.equals(droit.getCsProvenanceDroitAcquis())) {
            BigDecimal droitAcquis = new BigDecimal(droit.getDroitAcquis());

            // droitAquis * 21.7 / 30.0 arrondi aux 5 centimes supérieurs
            droitAcquis = droitAcquis.multiply(new BigDecimal("21.7")).divide(new BigDecimal("30.0"), 2,
                    BigDecimal.ROUND_HALF_UP);

            final FWCurrency droitAcquisArrondiAuCinqCentimes = new FWCurrency(droitAcquis.doubleValue());
            droitAcquisArrondiAuCinqCentimes.round(FWCurrency.ROUND_5CT);

            prestCalculee.setDroitAcquis(droitAcquisArrondiAuCinqCentimes);
        } else {
            prestCalculee.setDroitAcquis(new FWCurrency(droit.getDroitAcquis()));
        }

        // Frais de garde
        // Contrôler que le montant des frais de garde journalier max n'exède pas le montant de l'allocation journalière
        // max (BKmax). !!!
        // Si nombre de jours = zero, on set le montant journalier a zero
        BigDecimal montantJournalierFG = prestationWrapper.getFraisGarde().getBigDecimalValue();
        if (prestationWrapper.getPrestationBase().getNombreJoursSoldes() != 0) {
            montantJournalierFG = montantJournalierFG.divide(
                    new BigDecimal(prestationWrapper.getPrestationBase().getNombreJoursSoldes()), 5,
                    BigDecimal.ROUND_HALF_EVEN);
        } else {
            montantJournalierFG = new BigDecimal("0");
        }

        // montantJournalierFG > BKmax
        if (montantJournalierFG.compareTo(prestationWrapper.getPrestationBase().getAllocationJournaliereMaxFraisGarde()
                .getBigDecimalValue()) == 1) {
            // TE = BKmax
            BigDecimal montantFraisGarde = prestationWrapper.getPrestationBase().getAllocationJournaliereMaxFraisGarde()
                    .getBigDecimalValue();
            montantFraisGarde = montantFraisGarde
                    .multiply(new BigDecimal(prestationWrapper.getPrestationBase().getNombreJoursSoldes()));
            prestationWrapper.setFraisGarde(new FWCurrency(montantFraisGarde.toString()));
        }

        prestCalculee.setFraisGarde(prestationWrapper.getFraisGarde());
        listDesPrestationsACreer.add(prestCalculee);

        return listDesPrestationsACreer;
    }

    /**
     * Ajoute des droits rétroactifs pour les ACM alpha.
     */
    public void calculerAcmAlpha(final BSession session, final APDroitLAPG droit, final List basesCalcul)
            throws Exception {
        BTransaction transaction = null;

        try {
            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(droit.getEtat())) {
                throw new APCalculException(session.getLabel("MODULE_CALCUL_ETAT_DROIT_INVALIDE"));
            }

            final APBaseCalcul bc = (APBaseCalcul) basesCalcul.get(0);

            if (bc != null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();

                // Calcul des prestations courantes, pour chaque base de calculs données.
                // Toutes les prestations sont ajoutées dans la liste prestationCourantes
                traiterPrestationsAcmALpha(session, transaction, droit,
                        APTypeDePrestation.ACM_ALFA.getCodesystemString());
            }
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        System.out.println(transaction.getErrors());
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    public void calculerDroitMatCantonale(final BSession session, final APDroitMaternite droit, final List basesCalcul)
            throws Exception {
        BTransaction transaction = null;

        try {
            // le droit doit être dans l'état en attente
            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(droit.getEtat())) {
                throw new APCalculException(session.getLabel("MODULE_CALCUL_ETAT_DROIT_INVALIDE"));
            }

            final APBaseCalcul bc = (APBaseCalcul) basesCalcul.get(0);

            if (bc != null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();

                // Calcul des prestations courantes, pour chaque base de calculs données.
                // Toutes les prestations sont ajoutées dans la liste prestationCourantes
                traiterPrestationsMaternite(session, transaction, droit, APTypeDePrestation.LAMAT.getCodesystemString(),
                        isAllocationMax(session, transaction, droit.getIdDroit()));
            }
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        System.out.println(transaction.getErrors());
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * Récupère les plus grandes périodes communes (PGPC) des périodes données (PD)
     *
     * @return Collection d'objects de type {@link APPrestationWrapper}
     */
    private Collection calculerPGPCs(final BSession session, final BTransaction transaction, final String idDroitPere,
                                     final String idDroit, final Collection prestations) throws Exception {
        // Itérer chaque prestationsCourantes
        final Collection listPGPCs = new TreeSet(new APPrestationWrapperComparator());

        if (prestations != null) {
            APPrestationWrapper ppw = null;

            // Récupération des plus grandes périodes communes.
            for (final Iterator iter = prestations.iterator(); iter.hasNext();) {
                ppw = (APPrestationWrapper) iter.next();
                ppw.setIdDroit(idDroit);

                // Mise à jours des données de la prestation dans la liste.
                ppw = loadPGPC(session, transaction, idDroitPere, ppw);

                // Si date de fin de la PGPC < date de fin de la prestation de
                // base donnée
                // Créer la PGPC manquante (PGPC2) par appel récursif à la
                // présente méthode
                // Example :
                // [ P1 ][ P2 ]
                // [ prest. base ]
                // [ PGPC ][ PGPCP2 ]

                // Example 2:
                // [ P1 ][ P2 ][ P3 ]
                // [ PB ]
                // [ PGPC ][ PGPC2 ][ PGPC3 ]

                // Calcul de PGPC2
                while (BSessionUtil.compareDateFirstLower(session,
                        ppw.getLastPeriodePGPCAdded().getDateFin().toStr("."),
                        ppw.getPeriodeBaseCalcul().getDateFin().toStr("."))) {
                    final APPeriodeWrapper pw = new APPeriodeWrapper();

                    // Date début = date fin PGPC + 1 jour
                    pw.setDateDebut(addDays(ppw.getLastPeriodePGPCAdded().getDateFin(), 1));

                    // Date fin = date fin de la période donnée
                    pw.setDateFin(ppw.getPeriodeBaseCalcul().getDateFin());
                    ppw.setPeriodeBaseCalcul(pw);

                    // Mise à jours des données de la prestation dans la liste.
                    ppw = loadPGPC(session, transaction, idDroitPere, ppw);
                }

                if (ppw != null) {
                    listPGPCs.add(ppw);
                }
            }
        }

        return listPGPCs;
    }

    /**
     * WARNING : cette méthode n'est pas implémentée, il faut une mise en conformité du calculateur avant de pouvoir
     * découpler la persistence du calculateur
     */
    @Override
    public List<Object> calculerPrestation(final List<Object> donneesDomainCalcul) throws Exception {
        throw new RuntimeException("Not implemented yet... ");
    }

    public List<Object> calculerPrestation(final APPrestationStandardLamatAcmAlphaData data, final BSession session,
                                           final BTransaction transaction) throws Exception {
        APPrestationViewBean viewBean = data.getViewBean();

        if (data.getDroit().validateBeforeCalcul(transaction)) {

            genererPrestations(session, data.getDroit(), data.getFraisDeGarde(), data.getBasesCalcul());

            viewBean = calculPrestationAMAT_ACM(session, transaction, data.getDroit(), data.getAction());

        } else {
            // Les données ne sont pas valides
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(transaction.getErrors().toString());
        }

        return null;
    }

    /**
     * Calcul de la prestation
     *
     * @param dateRevision
     *                         Date utilisée pour rechercher la bonne révision. Si null, l'on prendra le no de révision
     *                         qui doit être
     *                         stocké dans la base de calcul.
     * @return Une Collection contenant les prestations calculées {@link APPrestationWrapper}. La liste retournée n'est
     *         jamais null, mais peut être vide. La collection retournée est ordonnée par date de début des prestations
     */
    private Collection calculerPrestationsCourantes(final BSession session, final APBaseCalcul baseCalcul,
                                                    final String typePrestationLAPG, final JADate dateRevision) throws Exception {
        final APReferenceDataParser obj = new APReferenceDataParser();

        // Génération de la liste des périodes de prestations courantes
        final Collection listPrestationWrapper = new TreeSet(new APPrestationWrapperComparator());

        // Si aucun jours soldé, on retourne la collection vide. Droit à aucune prestations.
        if (baseCalcul.getNombreJoursSoldes() <= 0) {
            return listPrestationWrapper;
        }

        if (baseCalcul.getNombreJoursSoldes() <= 0) {
            throw new APCalculException(
                    this.getClass().getName() + session.getLabel("MODULE_CALCUL_NBR_JOURS_SOLDE_OBLIGATOIRE"));
        }

        // Recherche des prestations à partir d'une période
        if (JadeStringUtil.isIntegerEmpty(baseCalcul.getNoRevision())
                || IAPDroitAPG.CS_REVISION_STANDARD.equals(baseCalcul.getNoRevision())) {
            final List refData = obj.loadReferencesData(session,
                    APCalculateurPrestationStandardLamatAcmAlpha.REFERENCE_FILE_APG, typePrestationLAPG,
                    baseCalcul.getDateDebut(), baseCalcul.getDateFin(), dateRevision);
            final Iterator iter = refData.iterator();

            final JACalendar cal = new JACalendarGregorian();
            final long nombreJoursSoldes = baseCalcul.getNombreJoursSoldes();
            final long nombreJoursTotal = cal.daysBetween(baseCalcul.getDateDebut(), baseCalcul.getDateFin()) + 1;
            long soldeJoursRestants = nombreJoursSoldes;

            while (iter.hasNext()) {
                final IAPReferenceDataPrestation element = (IAPReferenceDataPrestation) iter.next();

                final IAPCalculateur calculateur = APCalculateurFactory.getInstance().getCalculateur(element);
                final APResultatCalcul resultatCalcul = calculateur.calculerPrestation(baseCalcul, session);

                resultatCalcul.setTypeAllocation(baseCalcul.getTypeAllocation());
                resultatCalcul.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
                resultatCalcul.setIdTauxImposition(baseCalcul.getIdTauxImposition());
                resultatCalcul.setTauxImposition(baseCalcul.getTauxImposition());

                // On applique une règle de trois pour calculer le nombre de jours soldés de la période en cours
                final long nombreJoursPeriode = cal.daysBetween(element.getDateDebut(), element.getDateFin()) + 1;
                long nombreJoursSoldesPeriode = nombreJoursSoldes * nombreJoursPeriode;
                nombreJoursSoldesPeriode = nombreJoursSoldesPeriode / nombreJoursTotal;

                if (iter.hasNext()) {
                    soldeJoursRestants = soldeJoursRestants - nombreJoursSoldesPeriode;
                    resultatCalcul.setNombreJoursSoldes((new BigDecimal(nombreJoursSoldesPeriode)).intValue());
                }
                // Dernier enregistrement, le nombre de jours soldés égal au solde des jours restants
                else {
                    resultatCalcul.setNombreJoursSoldes((new BigDecimal(soldeJoursRestants)).intValue());
                }

                resultatCalcul.setDateDebut(element.getDateDebut());
                resultatCalcul.setDateFin(element.getDateFin());
                resultatCalcul.setRevision(element.getNoRevision());

                final APPrestationWrapper prestationWrapper = new APPrestationWrapper();
                prestationWrapper.setPrestationBase(resultatCalcul);
                prestationWrapper
                        .setPeriodeBaseCalcul(new APPeriodeWrapper(baseCalcul.getDateDebut(), baseCalcul.getDateFin()));

                listPrestationWrapper.add(prestationWrapper);
            }
        } else {
            final IAPReferenceDataPrestation element = obj.loadReferencesData(session,
                    APCalculateurPrestationStandardLamatAcmAlpha.REFERENCE_FILE_APG, typePrestationLAPG,
                    baseCalcul.getNoRevision());

            final IAPCalculateur calculateur = APCalculateurFactory.getInstance().getCalculateur(element);
            final APResultatCalcul resultatCalcul = calculateur.calculerPrestation(baseCalcul, session);
            resultatCalcul.setTypeAllocation(baseCalcul.getTypeAllocation());

            resultatCalcul.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
            resultatCalcul.setIdTauxImposition(baseCalcul.getIdTauxImposition());
            resultatCalcul.setTauxImposition(baseCalcul.getTauxImposition());

            resultatCalcul.setDateDebut(element.getDateDebut());
            resultatCalcul.setDateFin(element.getDateFin());
            resultatCalcul.setNombreJoursSoldes(baseCalcul.getNombreJoursSoldes());
            resultatCalcul.setRevision(element.getNoRevision());

            final APPrestationWrapper prestationWrapper = new APPrestationWrapper();
            prestationWrapper.setPrestationBase(resultatCalcul);
            prestationWrapper
                    .setPeriodeBaseCalcul(new APPeriodeWrapper(baseCalcul.getDateDebut(), baseCalcul.getDateFin()));

            listPrestationWrapper.add(prestationWrapper);
        }

        return listPrestationWrapper;
    }

    /**
     * Calcul des prestations courantes, pour chaque base de calculs données. Toutes les prestations sont ajoutées dans
     * la liste prestationCourantes
     *
     * @return Collection contenant les prestations courantes ({@link APPrestationWrapper}). La collection retournée est
     *         ordonnée par date de début.
     * @throws Exception
     *                       en cas d'erreurs
     */

    private Collection calculerPrestationsCourantes(final BSession session, final List basesCalcul,
                                                    final String apgOuMaternite) throws Exception {
        final Collection prestationsCourantes = new TreeSet(new APPrestationWrapperComparator());

        // Les bases de calculs sont ordonnées dans le temps, on les parcours dans le sens inverse, cad de la plus
        // vielles à la plus jeunes.

        // Dans le temps...., car parcours de la liste en sens inverse
        // [ bc[i] ][ bcSuivante[i+1] ] ---------> [t]

        JADate dateRevision = null;
        APBaseCalcul bcSuivante = (APBaseCalcul) basesCalcul.get(basesCalcul.size() - 1);

        dateRevision = bcSuivante.getDateFin();
        // Ajout des prestations courantes
        prestationsCourantes.addAll(this.calculerPrestationsCourantes(session, bcSuivante, apgOuMaternite, dateRevision));

        // Pour toutes les périodes de bases de calcul contiguës, on calcul chacune des ces bases de calculs avec comme
        // date de révision la date la plus ancienne.
        for (int i = basesCalcul.size() - 2; i >= 0; i--) {
            final APBaseCalcul bc = (APBaseCalcul) basesCalcul.get(i);

            if (!isDatesContigues(bc.getDateFin(), bcSuivante.getDateDebut())) {
                dateRevision = bc.getDateFin();
            }

            prestationsCourantes.addAll(this.calculerPrestationsCourantes(session, bc, apgOuMaternite, bc.getDateFin()));
            bcSuivante = bc;
        }

        return prestationsCourantes;
    }

    public APPrestationViewBean calculPrestationAMAT_ACM(final BSession session, final BTransaction transaction,
                                                         final APDroitLAPG droit, final FWAction action) throws Exception {
        APPrestationViewBean pViewBean;
        // charger la première prestation générée pour rediriger vers les
        // répartitions
        // et pour calculer LAMat et ACM en se basant sur les standard
        final APPrestationListViewBean mgr = new APPrestationListViewBean();
        mgr.setSession(session);
        mgr.changeManagerSize(1);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(transaction);

        if (mgr.isEmpty()) {
            // erreur il n'y a eu aucune prestation générée pour ce droit
            pViewBean = new APPrestationViewBean();
            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
            pViewBean.setMessage(session.getLabel(APPrestationHelper.ERREUR_AUCUNE_PRESTATION_GENEREE));
            pViewBean.setSession(session);
        } else {
            pViewBean = (APPrestationViewBean) mgr.get(0);
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // si calcul de toutes les prestations => ajoute les prestations
        // LAMat et ACM au besoin
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // si une IJAA perçue, pas de calcul de prestation LAMAt
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // si le calcul doit etre refait en utilisant ACOR (hasErrors et
        // isCalculACOR sont true), on
        // ne peut pas calculer les LAMat et les ACM
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!(pViewBean.hasErrors() && pViewBean.isCalculACOR())) {

            final APDroitLAPGJointDemandeHelper helper = new APDroitLAPGJointDemandeHelper();
            final APDroitLAPGJointDemandeViewBean vbDroit = new APDroitLAPGJointDemandeViewBean();
            vbDroit.setGenreService(pViewBean.getGenreService());
            vbDroit.setIdDroit(pViewBean.getIdDroit());
            vbDroit.setEtatDroit(droit.getEtat());

            final PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(droit.getIdDemande());
            try {
                demande.retrieve(transaction);
            } catch (final Exception e2) {
                session.addError("Erreur retrieve demande" + " " + e2.toString());
                pViewBean.setMessage(e2.toString());
                pViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.setRollbackOnly();
            }

            boolean isDroitAdoption = false;
            if (droit instanceof APDroitMaternite) {
                isDroitAdoption = retrieveIsAdoption(droit, session);
            }
            final boolean hasDroitAcquis = Float.parseFloat(droit.getDroitAcquis()) != 0;
            try {

                // Si la caisse donne une maternite cantonale => calculer la
                // LAMat
                if ((droit instanceof APDroitMaternite) && "true".equals(PRAbstractApplication
                        .getApplication(APApplication.DEFAULT_APPLICATION_APG).getProperty("isDroitMaterniteCantonale"))
                        && !hasDroitAcquis) {

                    // si c'est une adoption, avant de calculer les
                    // nouvelles prestations LAMat,
                    // on modifie les prestations standard (taux journalier
                    // = 0 CHF) qui n'existe pas pour
                    // une adoption. Puis, on cree les prestations LAMat
                    // normalement et on efface les
                    // prestations standard pour ce droit.
                    // Dans les autres cas, on annule, puis on efface que
                    // les prestation anterieur au 01.07.2005.
                    if (isDroitAdoption) {
                        try {
                            // on modifie le taux journalier (0 CHF)
                            this.updateTauxJournalierPrestation(droit, session, transaction);

                            // creation des prestations LAMat
                            if (APPrestationHelper.hasLAMatFalgInSitPro(session, droit)) {
                                helper.calculerDroitMaterniteCantonale(vbDroit, action, session);
                            }

                            // effacement des prestations standard
                            this.deletePrestationStandard(droit, session, transaction);
                        } catch (final Exception e) {
                            session.addError(session.getLabel("ERROR_CALCUL_LAMAT") + " " + e.toString());
                            pViewBean.setMessage(e.toString());
                            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
                            if (transaction != null) {
                                transaction.setRollbackOnly();
                            }
                        }
                    } else {
                        try {

                            // on modifie le taux journalier à (0 CHF) pour
                            // les prestation avant le 01.07.2005
                            this.updateTauxJournalierPrestation(droit, session, transaction, "01.07.2005");

                            // creation des prestations LAMat
                            if (APPrestationHelper.hasLAMatFalgInSitPro(session, droit)) {
                                helper.calculerDroitMaterniteCantonale(vbDroit, action, session);
                            }

                            // effacement des prestations standard d'avant
                            // 01.07.2005
                            this.deletePrestationStandard(droit, session, transaction, "01.07.2005");
                        } catch (final Exception e) {
                            session.addError(
                                    session.getLabel("ERROR_CALCUL_LAMAT_AVANT_01072005") + " " + e.toString());
                            pViewBean.setMessage(e.toString());
                            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
                            transaction.setRollbackOnly();
                        }
                    }
                }

                // Récupération de la valeur de la property. Exception si pas déclarée
                final String propertyValue = APProperties.TYPE_DE_PRESTATION_ACM.getValue();
                // validation en fonction de son domaine de valeur. Exception si valeur ne fait pas partie du domaine
                CommonPropertiesUtils.validatePropertyValue(APProperties.TYPE_DE_PRESTATION_ACM, propertyValue,
                        APPropertyTypeDePrestationAcmValues.propertyValues());

                final boolean hasPrestationACMALFA = APPropertyTypeDePrestationAcmValues.ACM_ALFA.getPropertyValue()
                        .equals(propertyValue);
                // Si la caisse est une caisse horlogere => calculer les ACM
                if (hasPrestationACMALFA && APPrestationHelper.hasAcmFalgInSitPro(session, droit) && !isDroitAdoption) {
                    helper.calculerACM(vbDroit, action, session);
                }
            } catch (final RemoteException e1) {
                session.addError(session.getLabel("ERROR_CALCUL_LAMAT_ACM") + " " + e1.toString());
                pViewBean.setMessage(e1.toString());
                pViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.setRollbackOnly();
            } catch (final Exception e1) {
                session.addError(session.getLabel("ERROR_CALCUL_LAMAT_ACM") + " " + e1.toString());
                pViewBean.setMessage(e1.toString());
                pViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.setRollbackOnly();
            }
        }
        return pViewBean;
    }

    /**
     * Plausi contrôlant que la somme des montants bruts des répartitions de paiement soit bien égale à la la somme
     * total de la prestations
     */
    private void controleRepartitions(final BSession session, final BTransaction transaction, final String idPrestation)
            throws Exception {

        final FWCurrency montantBrutPrestation = new FWCurrency();
        final FWCurrency montantBrutRepartitions = new FWCurrency();

        // 1ère étape, reprendre le montant brut de la prestation
        final APPrestation prestation = new APPrestation();
        prestation.setSession(session);
        prestation.setIdPrestationApg(idPrestation);
        prestation.retrieve();

        montantBrutPrestation.add(prestation.getMontantBrut());
        // On rajoute le montant des frais de garde au total pour la comparaison !!!
        if (!JadeStringUtil.isIntegerEmpty(prestation.getFraisGarde())) {
            montantBrutPrestation.add(prestation.getFraisGarde());
        }

        // 2ème étape, reprendre le montant brut de toutes les répartitions
        final APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();
        repartitions.setSession(session);
        repartitions.setParentOnly(true);
        repartitions.setForIdPrestation(idPrestation);
        repartitions.find();

        for (final Iterator iterator = repartitions.iterator(); iterator.hasNext();) {
            final APRepartitionPaiements rep = (APRepartitionPaiements) iterator.next();
            montantBrutRepartitions.add(rep.getMontantBrut());
        }

        // Dernière étape, comparaison des deux montants pour savoir s'ils sont identiques...
        if (!montantBrutPrestation.equals(montantBrutRepartitions)) {
            transaction.addErrors(
                    "Le montant brut des répartitions de paiement n'est pas égal au montant brut de la prestation avec l'id ["
                            + idPrestation + "]");
        }
    }

    /**
     * Pour les jours isolées si un emplyeur ne cotise pas il faut retirer le montant du montant
     * total de la prestations
     */
    private void ajusteMontantPrestationPourJourIsolee(final BSession session, final BTransaction transaction, final APPrestation entity)
            throws Exception {

        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        if(!"true".equals(isFerciab) || !APTypeDePrestation.JOUR_ISOLE.getCodesystemString().equals(entity.getGenre()) ) {
            return ;
        }

        final FWCurrency montantBrutPrestation = new FWCurrency();
        final FWCurrency montantBrutRepartitions = new FWCurrency();

        montantBrutPrestation.add(entity.getMontantBrut());
        // On rajoute le montant des frais de garde au total pour la comparaison !!!
        if (!JadeStringUtil.isIntegerEmpty(entity.getFraisGarde())) {
            montantBrutPrestation.add(entity.getFraisGarde());
        }

        // reprendre le montant brut de toutes les répartitions
        final APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();
        repartitions.setSession(session);
        repartitions.setParentOnly(true);
        repartitions.setForIdPrestation(entity.getIdPrestation());
        repartitions.find();

        for (final Iterator iterator = repartitions.iterator(); iterator.hasNext();) {
            final APRepartitionPaiements rep = (APRepartitionPaiements) iterator.next();
            montantBrutRepartitions.add(rep.getMontantBrut());
        }

        if (!montantBrutPrestation.equals(montantBrutRepartitions)) {
            montantBrutRepartitions.sub(entity.getFraisGarde());
            entity.setMontantBrut(montantBrutRepartitions.toString());
            BigDecimal montantJournalier = new BigDecimal(montantBrutRepartitions.doubleValue()).divide(new BigDecimal(entity.getNombreJoursSoldes()), 10, BigDecimal.ROUND_HALF_UP);
            entity.setMontantJournalier(montantJournalier.toString());
            entity.update();
        }

    }

    /**
     * WARNING : cette méthode n'est pas implémentée, il faut une mise en conformité du calculateur avant de pouvoir
     * découpler la persistence du calculateur
     */
    @Override
    public List<Object> persistenceToDomain(final Object data) throws Exception {
        throw new RuntimeException("Not implemented yet... ");
    }

    /**
     * Création de la prestation
     */
    public void creerPrestations(final BSession session, final BTransaction transaction, final APDroitLAPG droit,
                                  final List prestations) throws Exception {
        final APModuleRepartitionPaiements repartitionPaiements = new APModuleRepartitionPaiements();
        final Iterator iter = prestations.iterator();

        while (iter.hasNext()) {
            final APPrestationCalculee prestationACreer = (APPrestationCalculee) iter.next();

            final APPrestation entity = new APPrestation();
            entity.setSession(session);
            entity.setContenuAnnonce(prestationACreer.getContenuAnnonce());

            entity.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            entity.setDateDebut(prestationACreer.getDateDebut().toStr("."));
            entity.setDateFin(prestationACreer.getDateFin().toStr("."));
            entity.setNoRevision(prestationACreer.getResultatCalcul().getRevision());
            entity.setEtat(prestationACreer.getEtat());
            entity.setIdDroit(prestationACreer.getIdDroit());
            // Code défensif -> impact moindre vu que seule la FERCIAB utilise les jours isolés
            //               -> pour le module PANDEMIE
            if ("true".equals(JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB))
                    || droit instanceof APDroitPandemie) {
                entity.setGenre(prestationACreer.getCsGenrePrestation());
            }

            // On prend le maximum entre le montant journalier et le droit acquis
            BigDecimal mb = null;

            if (prestationACreer.getMontantJournalier().getBigDecimalValue()
                    .compareTo(prestationACreer.getDroitAcquis().getBigDecimalValue()) < 0) {
                mb = prestationACreer.getDroitAcquis().getBigDecimalValue();
            } else {
                mb = prestationACreer.getMontantJournalier().getBigDecimalValue();
            }

            // Récupération du basicDailyAmount
            // Si il est pas présent dans la prestationAcreer on prend le montant journalier (calcul maison)
            if ((prestationACreer.getBasicDailyAmount() == null)
                    || JadeStringUtil.isEmpty(prestationACreer.getBasicDailyAmount().toString())) {
                entity.setBasicDailyAmount(prestationACreer.getMontantJournalier().toString());
            } else {
                entity.setBasicDailyAmount(prestationACreer.getBasicDailyAmount().toString());
            }

            // on met a jour le montant journalier de la prestation à créer pour calculer juste les repartition
            // on fait cela avant l'ajout des frais de garde car ils sont payés dans une repartition séparée
            prestationACreer.setMontantJournalier(new FWCurrency(mb.toString()));

            // Cumul du montant journalier avec le montant journalier de l'allocation d'exploitation
            if ((prestationACreer.getResultatCalcul().getAllocationJournaliereExploitation() != null)
                    && !prestationACreer.getResultatCalcul().getAllocationJournaliereExploitation().isZero()) {
                entity.setMontantAllocationExploitation(
                        prestationACreer.getResultatCalcul().getAllocationJournaliereExploitation().toString());

                mb = mb.add(prestationACreer.getResultatCalcul().getAllocationJournaliereExploitation()
                        .getBigDecimalValue());
            }

            entity.setMontantJournalier(mb.toString());
            mb = mb.multiply(new BigDecimal(prestationACreer.getNombreJoursSoldes()));
            entity.setMontantBrut(mb.toString());

            // Bz 7642 : Correction pour la plausibilté 307 lorsque le résultat du calcul vient de ACOR et le montant
            // est plafonné
            final BigDecimal basicDailyAmout = new BigDecimal(entity.getBasicDailyAmount());
            if (basicDailyAmout.multiply(new BigDecimal(prestationACreer.getNombreJoursSoldes())).floatValue() < mb
                    .floatValue()) {
                entity.setBasicDailyAmount(prestationACreer.getMontantJournalier().toString());
            }

            entity.setNombreJoursSoldes(prestationACreer.getNombreJoursSoldes());
            entity.setRevenuMoyenDeterminant(
                    prestationACreer.getRevenuDeterminantMoyen().getBigDecimalValue().toString());
            entity.setType(prestationACreer.getTypePrestation());
            entity.setDroitAcquis(prestationACreer.getDroitAcquis().toString());

            if (prestationACreer.getFraisGarde() == null) {
                entity.setFraisGarde("0");
            } else {
                entity.setFraisGarde(prestationACreer.getFraisGarde().toString());
            }

            entity.add(transaction);

            repartitionPaiements.repartirPaiements(session, transaction, droit.loadDemande().getIdTiers(),
                    prestationACreer, entity.getIdPrestationApg());

            ajusteMontantPrestationPourJourIsolee(session, transaction, entity);

            controleRepartitions(session, transaction, entity.getIdPrestationApg());

        }
    }

    /**
     * Création des prestations des droits spécifiques, prends en compte les anciennes prestations pour le calcul des
     * montants rétroactifs.
     */
    private void creerPrestationsACM(final BSession session, final BTransaction transaction,
                                     final APPrestation lastPrestation, final APDroitLAPG droit, final FWCurrency sommeMontantsJournalier,
                                     final FWCurrency revenuMoyenDeterminant, final String genrePrestation, final String dateDebut,
                                     final String dateFin, final String nombreJoursSoldes, final String noRevision) throws Exception {

        this.creerPrestationsACM(session, transaction, lastPrestation, droit, sommeMontantsJournalier,
                revenuMoyenDeterminant, genrePrestation, dateDebut, dateFin, nombreJoursSoldes, noRevision, false);
    }

    /**
     * Création des prestations des droits spécifiques, prends en compte les anciennes prestations pour le calcul des
     * montants rétroactifs.
     *
     * @param plusDeTrenteJours
     *                              pour les APG de service d'avancement, pour savoir si il faut calculer avec 50% ou
     *                              80% du revenu
     */
    private void creerPrestationsACM(final BSession session, final BTransaction transaction,
                                     final APPrestation lastPrestation, final APDroitLAPG droit, final FWCurrency sommeMontantsJournalier,
                                     final FWCurrency revenuMoyenDeterminant, final String genrePrestation, final String dateDebut,
                                     final String dateFin, final String nombreJoursSoldes, final String noRevision,
                                     final boolean plusDeTrenteJours) throws Exception {
        final APCalculateurAcmAlpha acmCalculateur = new APCalculateurAcmAlpha();
        final HashMap montants = acmCalculateur.calculerMontantACM(session, transaction, droit.getGenreService(),
                droit.getIdDroit(), dateDebut, dateFin, plusDeTrenteJours);

        // on prend le montant total pour setter le montant de la prestation
        final BigDecimal montantACM = (BigDecimal) montants.get(APCalculateurAcmAlpha.KEY_TOTAL_JOURNALIER_ACM);
        montants.remove(APCalculateurAcmAlpha.KEY_TOTAL_JOURNALIER_ACM);

        // Aucune prestation ACM à créer
        if (montantACM.compareTo(new BigDecimal(0)) == 0) {
            return;
        } else {
            // Creation de la prestation
            final APPrestation prestation = new APPrestation();
            prestation.setSession(session);

            // Les prestations ACM ne doivent pas être annoncées
            prestation.setContenuAnnonce(null);

            prestation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            prestation.setDateDebut(dateDebut);
            prestation.setDateFin(dateFin);
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestation.setIdDroit(droit.getIdDroit());
            prestation.setNombreJoursSoldes(nombreJoursSoldes);
            prestation.setNoRevision(noRevision);
            prestation.setRevenuMoyenDeterminant(revenuMoyenDeterminant.getBigDecimalValue().toString());
            prestation.setType(IAPPrestation.CS_TYPE_NORMAL);
            prestation.setGenre(genrePrestation);

            prestation.setMontantJournalier(montantACM.toString());

            BigDecimal mb = new BigDecimal(montantACM.toString());
            mb = mb.multiply(new BigDecimal(nombreJoursSoldes));
            prestation.setMontantBrut(mb.toString());
            prestation.add(transaction);

            final APModuleRepartitionPaiements repartitionPaiements = new APModuleRepartitionPaiements();
            repartitionPaiements.repartirPaiementsACM(session, transaction, lastPrestation, prestation, montants);
            controleRepartitions(session, transaction, prestation.getIdPrestationApg());
        }
    }

    /**
     * Création les prestations des droits LAMat, prends en compte les anciennes prestations (AMat) pour le calcul des
     * montants rétroactifs.
     */
    private void creerPrestationsLAMat(final BSession session, final BTransaction transaction,
            final APPrestation lastPrestation, final APDroitLAPG droit, final FWCurrency sommeMontantsJournalier,
            final FWCurrency revenuMoyenDeterminant, final String genrePrestation, final String dateDebut,
            final String dateFin, final String nombreJoursSoldes, final String noRevision,
            final boolean isAllocationMax) throws Exception {

        // si la prestation LAMat des 14 jours supplémentaires comprend le 01.01.2008
        // on coupe cette prestation en 2 pour pouvoir profiter du nouveau barème valable dès le 01.01.2008
        if (BSessionUtil.compareDateFirstLower(session, dateDebut, "01.01.2008")
                && BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFin, "01.01.2008")) {

            final JACalendar calendar = new JACalendarGregorian();
            final long jourFirstPeriod = calendar.daysBetween(dateDebut, "31.12.2007");
            final long jourSecondPeriod = calendar.daysBetween("01.01.2008", dateFin);

            creerPrestationsLAMat(session, transaction, lastPrestation, droit, sommeMontantsJournalier,
                    revenuMoyenDeterminant, genrePrestation, dateDebut, "31.12.2007",
                    Long.toString(jourFirstPeriod + 1), noRevision, isAllocationMax);

            creerPrestationsLAMat(session, transaction, lastPrestation, droit, sommeMontantsJournalier,
                    revenuMoyenDeterminant, genrePrestation, "01.01.2008", dateFin, Long.toString(jourSecondPeriod + 1),
                    noRevision, isAllocationMax);

        } else {
            // le cas normal
            final LAMatCalculateur lamatCalculateur = new LAMatCalculateur();

            final BigDecimal montantLAMat = lamatCalculateur.calculerMontantLAMat(session, transaction,
                    droit.getGenreService(), droit.getIdDroit(), revenuMoyenDeterminant.toString(),
                    sommeMontantsJournalier.toString(), dateDebut, dateFin, isAllocationMax);

            // Aucune prestation LAMat à créer
            if (montantLAMat.compareTo(new BigDecimal(0)) == 0) {
                return;
            } else {
                // Creation de la prestation
                final APPrestation prestation = new APPrestation();
                prestation.setSession(session);

                // Les prestations LAMat ne doivent pas être annoncées
                prestation.setContenuAnnonce(null);

                prestation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
                prestation.setDateDebut(dateDebut);
                prestation.setDateFin(dateFin);
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
                prestation.setIdDroit(droit.getIdDroit());
                prestation.setNombreJoursSoldes(nombreJoursSoldes);
                prestation.setNoRevision(noRevision);
                prestation.setRevenuMoyenDeterminant(revenuMoyenDeterminant.getBigDecimalValue().toString());
                prestation.setType(IAPPrestation.CS_TYPE_NORMAL);
                prestation.setGenre(genrePrestation);

                prestation.setMontantJournalier(montantLAMat.toString());

                BigDecimal mb = new BigDecimal(montantLAMat.toString());
                mb = mb.multiply(new BigDecimal(nombreJoursSoldes));
                prestation.setMontantBrut(mb.toString());
                prestation.add(transaction);

                final APModuleRepartitionPaiements repartitionPaiements = new APModuleRepartitionPaiements();
                repartitionPaiements.repartirPaiementsLAMat(session, transaction, lastPrestation, prestation);
                controleRepartitions(session, transaction, prestation.getIdPrestationApg());
            }
        }
    }

    private void creerRestitutions(final BSession session, final BTransaction transaction, final String idDroit,
                                   final APPeriodeWrapper pgpcRestitution, final APPrestation[] prestationsARestituer) throws Exception {

        final List prestations = new LinkedList();

        for (int i = 0; i < prestationsARestituer.length; i++) {

            if (APTypeDePrestation.STANDARD.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        //ESVE MATERNITE RESTITUTION!!!!!
        // Traitement des prestations ACM ALFA
        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }

        // Traitement des prestations ACM NE
        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        // Traitement des prestations ACM 2
        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        // Traitement des prestations MATCIAB1
        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        // Traitement des prestations MATCIAB2
        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.LAMAT.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);

        for (int i = 0; i < prestationsARestituer.length; i++) {
            if (APTypeDePrestation.PANDEMIE.isCodeSystemEqual(prestationsARestituer[i].getGenre())
                    && IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationsARestituer[i].getEtat())) {
                prestations.add(prestationsARestituer[i]);
            }
        }
        doRestitutionAndClear(session, transaction, idDroit, pgpcRestitution, prestations);
    }

    private void doRestitutionAndClear(final BSession session, final BTransaction transaction, final String idDroit,
                                       final APPeriodeWrapper pgpcRestitution, final List prestations) throws Exception {
        if (!prestations.isEmpty()) {
            doRestitution(session, transaction, idDroit, pgpcRestitution,
                    (APPrestation[]) prestations.toArray(new APPrestation[prestations.size()]));
        }

        prestations.clear();
    }

    /**
     * WARNING : cette méthode n'est pas implémentée, il faut une mise en conformité du calculateur avant de pouvoir
     * découpler la persistence du calculateur
     */
    @Override
    public List<APPrestationCalculeeAPersister> domainToPersistence(final List<Object> resultat) throws Exception {
        throw new RuntimeException("Not implemented yet... ");
    }

    public void deletePrestationsStandardsWhenAmatIsExcluded(final BSession session, final BTransaction transaction,
                                                             final APDroitLAPG droit) throws Exception {
        APSituationProfessionnelleManager si = new APSituationProfessionnelleManager();
        String iddroi = droit.getIdDroit();
        si.setForIdDroit(iddroi);
        si.setSession(session);
        si.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < si.size(); i++) {
            APSituationProfessionnelle sipro = (APSituationProfessionnelle) si.getEntity(i);
            if (sipro.getIsAMATFExcluded()) {
                deletePrestationStandard(droit, session, transaction);
            }
        }
    }

    private void deletePrestationStandard(final APDroitLAPG droit, final BISession session,
                                          final BITransaction transaction) throws Exception {
        this.deletePrestationStandard(droit, session, transaction, "31.12.2999");
    }

    /**
     * Efface toutes les prestation Standard du droit donne
     *
     * @param droit
     * @param session
     */
    private void deletePrestationStandard(final APDroitLAPG droit, final BISession session,
                                          final BITransaction transaction, final String dateFin) throws Exception {

        final APPrestationListViewBean prestations = new APPrestationListViewBean();

        prestations.setSession((BSession) session);
        prestations.setForIdDroit(droit.getIdDroit());
        prestations.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
        prestations.setToDateFin(dateFin);
        prestations.find(transaction);

        BStatement statement = null;
        statement = prestations.cursorOpen((BTransaction) transaction);

        APPrestationViewBean prestation = null;
        while (null != (prestation = (APPrestationViewBean) prestations.cursorReadNext(statement))) {
            prestation.delete(transaction);
        }
    }

    private void doRestitution(final BSession session, final BTransaction transaction, final String idDroit,
            final APPeriodeWrapper pgpcRestitution, final APPrestation[] prestationsARestituer) throws Exception {

        // Sommer les montants journaliers des prestations retournées
        final FWCurrency sommeMontantJournalier = new FWCurrency(0);

        // Sommer les frais de garde alloué des prestations retournées
        final FWCurrency sommeFraisGarde = new FWCurrency(0);

        // Sommer les montants alloc. exploitations
        final FWCurrency sommeMontantAllocExploitation = new FWCurrency(0);

        // Restitution par genre de prestation

        String noRevision = "";
        String nbrJoursSolde = "";

        boolean isPrestationARestituer = false;
        for (int i = 0; i < prestationsARestituer.length; i++) {
            sommeMontantJournalier.add(prestationsARestituer[i].getMontantJournalier());
            sommeFraisGarde.add(prestationsARestituer[i].getFraisGarde());
            sommeMontantAllocExploitation.add(prestationsARestituer[i].getMontantAllocationExploitation());

            nbrJoursSolde = prestationsARestituer[i].getNombreJoursSoldes();
            noRevision = prestationsARestituer[i].getNoRevision();
            isPrestationARestituer = true;
        }

        if (isPrestationARestituer) {

            sommeMontantJournalier.negate();
            sommeFraisGarde.negate();

            final APPrestation restitution = addRestitution(session, transaction, idDroit, pgpcRestitution,
                    sommeMontantJournalier, sommeFraisGarde, sommeMontantAllocExploitation, nbrJoursSolde, noRevision,
                    prestationsARestituer[0].getGenre());

            final APModuleRepartitionPaiements repartitionPaiements = new APModuleRepartitionPaiements();
            repartitionPaiements.restituerPaiements(session, transaction, restitution, prestationsARestituer);

            // Mise à jours des prestations qui vont être annulées par cette restitution complète
            updatePrestationsRestituees(transaction, restitution.getIdPrestationApg(), prestationsARestituer);
        }
    }

    /**
     * On fusionne les prestations contiguës quand cela est possible.
     */
    private List fusionnerPrestationsACreer(final BSession session, final List prestations) throws Exception {
        final List result = new ArrayList();
        final Iterator iter = prestations.iterator();

        APPrestationCalculee elementPrecedent = null;

        if (iter.hasNext()) {
            elementPrecedent = (APPrestationCalculee) iter.next();
        }

        // 1 seul element
        if (!iter.hasNext()) {
            if (!isMontantPrestationEmpty(elementPrecedent)) {
                result.add(elementPrecedent);
            }

            return result;
        }

        // Prestation de type maternité, paternité ou proche-aidant
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(elementPrecedent.getResultatCalcul().getTypeAllocation())
                || IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(elementPrecedent.getResultatCalcul().getTypeAllocation())
                || IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(elementPrecedent.getResultatCalcul().getTypeAllocation())) {
            BigDecimal montantJournalierPrestationMaternite = elementPrecedent.getResultatCalcul()
                    .getMontantJournalier().getBigDecimalValue();

            // 27.11.2008 / HPE / BZ 2157
            // Lors de la fusion des prestations maternité, si montant total < 200.-, prendre en compte le droit acquis
            // si existant pour savoir si fusion ou pas.
            final APDroitLAPG droitAPG = new APDroitLAPG();
            droitAPG.setIdDroit(elementPrecedent.getIdDroit());
            droitAPG.setSession(session);
            droitAPG.retrieve();

            if (!JadeStringUtil.isIntegerEmpty(droitAPG.getDroitAcquis())) {
                final FWCurrency montantDroitAcquis = new FWCurrency(droitAPG.getDroitAcquis());
                montantJournalierPrestationMaternite = montantJournalierPrestationMaternite
                        .add(montantDroitAcquis.getBigDecimalValue());
            }

            // Fusion de toutes les prestations si pas de changement au niveau du versement des employeurs
            // Et montant max de la prestation <= 200.- CHF
            // Pour les prestations de type maternité, fusion si montant total des prestations < 200.-
            // c'est a dire montant journalier < 200/98
            // Par contre, si changement dans les versement par employeur, on ne peut pas fusionner.

            BigDecimal montantJournalierMinMaternite = new BigDecimal(
                    session.getApplication().getProperty(APApplication.PROPERTY_MONTANT_MIN_MATERNITE));
            montantJournalierMinMaternite = montantJournalierMinMaternite.divide(
                    new BigDecimal(session.getApplication().getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS)),
                    8, BigDecimal.ROUND_HALF_EVEN);

            if (montantJournalierPrestationMaternite.compareTo(montantJournalierMinMaternite) <= 0) {
                HashMap sitProfElementPrecedent = getSituationsProfesionnelRef(elementPrecedent);

                while (iter.hasNext()) {
                    final APPrestationCalculee element = (APPrestationCalculee) iter.next();
                    final HashMap sitProfElementCourant = getSituationsProfesionnelRef(element);

                    if (isSameSituationsProfessionnelles(sitProfElementCourant, sitProfElementPrecedent)) {
                        elementPrecedent.setDateFin(element.getDateFin());

                        BigDecimal nbrJours = new BigDecimal(elementPrecedent.getNombreJoursSoldes());

                        nbrJours = nbrJours.add(new BigDecimal(element.getNombreJoursSoldes()));
                        elementPrecedent.setNombreJoursSoldes(nbrJours.toString());
                    } else {
                        if (!isMontantPrestationEmpty(elementPrecedent)) {
                            result.add(elementPrecedent);
                        }

                        elementPrecedent = element;
                        sitProfElementPrecedent = getSituationsProfesionnelRef(elementPrecedent);
                    }
                }

                if (!isMontantPrestationEmpty(elementPrecedent)) {
                    result.add(elementPrecedent);
                }
            } else {
                if (!isMontantPrestationEmpty(elementPrecedent)) {
                    result.add(elementPrecedent);
                }

                while (iter.hasNext()) {
                    final APPrestationCalculee element = (APPrestationCalculee) iter.next();

                    if (!isMontantPrestationEmpty(element)) {
                        result.add(element);
                    }
                }
            }
        }

        // Prestation APG
        else {
            while (iter.hasNext()) {
                final APPrestationCalculee element = (APPrestationCalculee) iter.next();

                String typeAllocation = element.getResultatCalcul().getTypeAllocation();

                if(session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(typeAllocation) || IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(typeAllocation)){
                    result.add(elementPrecedent);
                    elementPrecedent = element;
                } else {

                    // Fusion possible uniquement si la base de calcul est la même.
                    // Survient lorsque les PGPC sont des sous-ensembles de la période de base et ont été splitté en deux
                    // suite au calcul d'une restitution.
                    if (elementPrecedent.getResultatCalcul().equals(element.getResultatCalcul())) {
                        elementPrecedent.setDateFin(element.getDateFin());
                    } else {
                        if (!isMontantPrestationEmpty(elementPrecedent)) {
                            result.add(elementPrecedent);
                        }

                        elementPrecedent = element;
                    }
                }
            }

            if (!isMontantPrestationEmpty(elementPrecedent)) {
                result.add(elementPrecedent);
            }
        }

        return result;
    }

    /**
     * Génération des prestations Cette méthode génère toutes les prestations pour un droit donné.
     *
     * @param session
     * @param droit
     *                        Le droit pour lequel doivent être générées les prestations
     * @param fraisGarde
     *                        frais de garde
     * @param basesCalcul
     *                        La base de calcul, référence utilisée pour le calcul des prestations. Les bases de calculs
     *                        doivent
     *                        être ordonnée dans le temps.
     * @throws Exception
     *                       en cas d'erreurs. Dans ce cas, toute la transaction est rollbackée, aucune prestation n'est
     *                       créée.
     */
    public void genererPrestations(final BSession session, final APDroitLAPG droit, final FWCurrency fraisGarde,
                                   final List basesCalcul) throws Exception {
        BTransaction transaction = null;

        try {
            String apgOuMaternite = null;

            if (droit instanceof APDroitMaternite) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_MATERNITE;
            } else if (droit instanceof APDroitPaternite) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PATERNITE;
            } else if (droit instanceof APDroitProcheAidant) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PROCHE_AIDANT;
            } else if(droit instanceof APDroitPandemie) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PANDEMIE;
            } else {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_APG;
            }

            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(droit.getEtat())) {
                throw new APCalculException(session.getLabel("MODULE_CALCUL_ETAT_DROIT_INVALIDE"));
            }

            // -------------------------------------------------------------------
            // -- Condition stipulant que le calcul doit s'effectuer avec ACOR
            // -------------------------------------------------------------------
            if (APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_MATERNITE.equals(apgOuMaternite)) {
                if (APCalculAcorUtil.grantCalulAcorMaternite(session, droit)) {
                    final JACalendarGregorian calendar = new JACalendarGregorian();
                    // si une partie du droit se situe entre le 01.07.2001 et le 01.07.2005, le calcul doit se faire
                    // à la main.
                    // On crée des prestations avec des repartitions a zero.
                    if (!((calendar.compare(droit.getDateDebutDroit(), "01.07.2005") == JACalendar.COMPARE_FIRSTLOWER)
                            && (calendar.compare(droit.getDateFinDroit(),
                            "01.07.2001") == JACalendar.COMPARE_FIRSTUPPER)
                            && "true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty("isDroitMaterniteCantonale")))) {
                        throw new APCalculACORException(session.getLabel("MODULE_CALCUL_ACOR_ERROR"));
                    }
                }
            } else if (!APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PANDEMIE.equals(apgOuMaternite)
                    && !APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PATERNITE.equals(apgOuMaternite)
                    && !APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PROCHE_AIDANT.equals(apgOuMaternite)){
                if (APCalculAcorUtil.grantCalulAcorAPG(session, (APDroitAPG) droit)) {
                    throw new APCalculACORException(session.getLabel("MODULE_CALCUL_ACOR_ERROR"));
                }
            }

            if (!basesCalcul.isEmpty()) {
            final APBaseCalcul bc = (APBaseCalcul) basesCalcul.get(0);
            if (bc != null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();

                // Il faut supprimer toutes les prestations du droit au préalable, avant de pouvoir les recalculer.
                // Sinon, elles risqueraient d'etre prise en compte pour le calcul des nouvelles prestations et
                // provoquerait des incohérences.
                APPrestationHelper.removePrestationsDroitByType(session, transaction, droit, null);

                // Calcul des prestations courantes, pour chaque base de calculs données.
                // Toutes les prestations sont ajoutées dans la liste prestationCourantes
                traiterPrestationsCourantes(session, transaction,
                                            this.calculerPrestationsCourantes(session, basesCalcul, apgOuMaternite), droit, fraisGarde,null);

                }
            }
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        System.out.println(transaction.getErrors());
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * L'id droit du père est l'id permettant de récupérer tous les droits groupés entre eux, cad l'id du droit de base.
     * S'il s'agit du droit parent, idDroitGroupe = droit.getIdDroit S'ils'agit d'un droit enfant, l'idDroitGroupe =
     * droit.getIdDroitParent; 1 seul niveau hiérarchique.
     */
    private String getIdDroitParent(final APDroitLAPG droit) {
        String idDroitPere = droit.getIdDroit();

        if (!JadeStringUtil.isIntegerEmpty(droit.getIdDroitParent())) {
            idDroitPere = droit.getIdDroitParent();
        }

        return idDroitPere;
    }

    /**
     * Retourne un tableau de prestation dont les dates de début/fin chevauchent la période PGPC.
     *
     * @return APPrestation[] Le tableau de prestation
     */
    private APPrestation[] getPrestationsContenueDansPeriodes(final BSession session, final BTransaction transaction,
            final APPeriodeWrapper pgpc, final String idDroitPere) throws Exception {
        final APDroitLAPGJoinPrestationManager prestationMgr = new APDroitLAPGJoinPrestationManager();
        prestationMgr.setSession(session);
        prestationMgr.setForIdDroitGroupe(idDroitPere);

        prestationMgr.setFromDateDebut(pgpc.getDateDebut().toStr("."));
        prestationMgr.setToDateDebut(pgpc.getDateFin().toStr("."));
        prestationMgr.setForTypeDifferentDe(IAPPrestation.CS_TYPE_ANNULATION);
        prestationMgr.setOrderBy(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        prestationMgr.find(transaction);

        final APPrestation[] result = new APPrestation[prestationMgr.getSize()];

        for (int i = 0; i < prestationMgr.getSize(); i++) {
            final APPrestation element = (APPrestation) prestationMgr.getEntity(i);
            result[i] = new APPrestation(element);
        }

        return result;
    }

    private HashMap getSituationsProfesionnelRef(final APPrestationCalculee prestation) {
        final Iterator sitProfIteraror = prestation.getResultatCalcul().getResultatsCalculsSitProfessionnelle()
                .iterator();
        final HashMap map = new HashMap();

        while (sitProfIteraror.hasNext()) {
            final APResultatCalculSituationProfessionnel sitProf = (APResultatCalculSituationProfessionnel) sitProfIteraror
                    .next();
            map.put(sitProf.getIdAffilie() + "-" + sitProf.getIdTiers(),
                    sitProf.getSalaireJournalierVerse().toString());
        }

        return map;
    }

    public APPrestationViewBean getViewBean() {
        return viewBean;
    }

    private Collection initPrestationFraisGarde(final Collection prestationsCourantes, final FWCurrency fraisGarde) {
        int nombreJoursSoldesTotal = 0;

        for (final Iterator iter = prestationsCourantes.iterator(); iter.hasNext();) {
            final APPrestationWrapper pw = (APPrestationWrapper) iter.next();
            nombreJoursSoldesTotal += pw.getPrestationBase().getNombreJoursSoldes();
        }

        final FWCurrency fraisGardeAlloue = new FWCurrency(0);

        for (final Iterator iter = prestationsCourantes.iterator(); iter.hasNext();) {
            final APPrestationWrapper pw = (APPrestationWrapper) iter.next();

            // Pas de frais de garde
            if ((fraisGarde == null) || fraisGarde.isZero()) {
                pw.setFraisGarde(new FWCurrency(0));
            } else {
                // Calcul des frais de garde au pro-rata du nombre de jours.
                BigDecimal fg = fraisGarde.getBigDecimalValue();
                fg = fg.multiply(new BigDecimal(pw.getPrestationBase().getNombreJoursSoldes()));
                fg = fg.divide(new BigDecimal(nombreJoursSoldesTotal), BigDecimal.ROUND_HALF_EVEN);

                // Dernier enregistrement .... pour éviter les erreurs d'arrondi
                if (!iter.hasNext()) {
                    fg = fraisGarde.getBigDecimalValue();
                    fg = fg.subtract(fraisGardeAlloue.getBigDecimalValue());
                }

                // Arrondis
                fg = JANumberFormatter.formatBigD(fg);

                fraisGardeAlloue.add(fg.toString());
                pw.setFraisGarde(new FWCurrency(fg.toString()));
            }
        }

        return prestationsCourantes;
    }

    /**
     * True si le tiers possède une situation prof avec le flag allocation max. = true
     */
    public boolean isAllocationMax(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception {

        // on cherche les situations prof.
        final APSituationProfessionnelleManager situProfMananger = new APSituationProfessionnelleManager();
        situProfMananger.setSession(session);
        situProfMananger.setForIdDroit(idDroit);
        situProfMananger.find();
        APSituationProfessionnelle situationProf = null;

        for (final Iterator iter = situProfMananger.iterator(); iter.hasNext();) {
            situationProf = (APSituationProfessionnelle) iter.next();
            if (situationProf.getIsAllocationMax().booleanValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDatesContigues(final JADate date1, final JADate date2) {
        final JACalendarGregorian cal = new JACalendarGregorian();

        return cal.addDays(date1, 1).equals(date2);
    }

    private boolean isMontantPrestationEmpty(final APPrestationCalculee prestation) {
        if (((prestation.getMontantJournalier() == null) || prestation.getMontantJournalier().equals(new FWCurrency(0)))
                && ((prestation.getFraisGarde() == null) || prestation.getFraisGarde().equals(new FWCurrency(0)))
                && ((prestation.getResultatCalcul().getAllocationJournaliereExploitation() == null) || prestation
                .getResultatCalcul().getAllocationJournaliereExploitation().equals(new FWCurrency(0)))
                && ((prestation.getDroitAcquis() == null) || prestation.getDroitAcquis().equals(new FWCurrency(0)))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSameSituationsProfessionnelles(final Map situationProf1, final Map situationProf2) {
        return (situationProf1.equals(situationProf2)) && (situationProf2.equals(situationProf1));
    }

    /**
     * Charge la plus grande période commune (pgpc) pour une période de base donnée (pd) parmi toutes les prestations
     * incluses dans cette périodes de base. La période de base est : pc.getDateDebut() - pc.getDateFin()
     *
     * <pre>
     * Example 1 [   P1         ][   P2      ]
     *                     [  pd  ]
     *
     *        pgpc = pd
     *
     *        Example 2 [   P1   ][   P2      ] [      P3   ]
     *                     [  pd         ]
     *
     *        pgpc = P1
     *
     *        Example 3 [   P1        ][   P2           ]
     *                     [          pd          ]
     *                     [pgpc1        ][ pgpc2 ]
     * </pre>
     *
     * @param session
     * @param transaction
     * @param idDroitPere
     *                        Identifiant du père du droit;
     * @param ppw
     *                        la prestation pour laquelle l'on veut calculer toutes les pgpc
     * @return APPrestationWrapper, la prestation mise à jour.
     * @throws Exception
     */
    /*
     * Exemple de cas : [ P1 ][ P2 annule][ P3 ]
     *
     * Retourne [ ] [ P3 ]
     *
     * La méthode loadPGPC retourne la période complète (une seule)
     *
     * La correction du po #00619 à un effet de bord non désiré pour le calcul des prestations de restitutions. Cette
     * méthode annule la modification du po 00619 faite dans loadPGPC().
     */
    private APPrestationWrapper loadPGPC(final BSession session, final BTransaction transaction,
            final String idDroitPere, final APPrestationWrapper ppw) throws Exception {
        final APPeriodeWrapper pgpc = new APPeriodeWrapper();
        JADate dateDebutPrestationLaPlusRecente = new JADate();
        boolean dateDebutChange = false;
        pgpc.setDateDebut(ppw.getPeriodeBaseCalcul().getDateDebut());
        pgpc.setDateFin(ppw.getPeriodeBaseCalcul().getDateFin());

        final APDroitLAPGJoinPrestationManager prestationMgr = new APDroitLAPGJoinPrestationManager();
        prestationMgr.setSession(session);

        if (JadeStringUtil.isBlankOrZero(idDroitPere)) {
            prestationMgr.setForIdDroit(ppw.getIdDroit());
        } else {
            prestationMgr.setForIdDroitGroupe(idDroitPere);
        }

        prestationMgr.setFromDateDebut(ppw.getPeriodeBaseCalcul().getDateDebut().toStr("."));
        // si la période est la premiere période du droit (dateDebutDroit = dateDebutPeriodeBaseCalcul) et qu'il y a un
        // droit parent et que la date de début du droit parent est < date début du droit
        // Alors, on prend comme ref. la date de début du droit parent.
        if (!JadeStringUtil.isIntegerEmpty(idDroitPere)) {
            final APDroitLAPG droitParent = new APDroitLAPG();
            droitParent.setSession(session);
            droitParent.setIdDroit(idDroitPere);
            droitParent.retrieve();

            final APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(session);
            droit.setIdDroit(ppw.getIdDroit());
            droit.retrieve();

            if (BSessionUtil.compareDateEqual(session, ppw.getPeriodeBaseCalcul().getDateDebut().toStr("."),
                    droit.getDateDebutDroit())) {

                if (BSessionUtil.compareDateFirstLower(session, droitParent.getDateDebutDroit(),
                        droit.getDateDebutDroit()) == true) {
                    prestationMgr.setFromDateDebut(droitParent.getDateDebutDroit());
                    dateDebutPrestationLaPlusRecente = new JADate(droitParent.getDateDebutDroit());
                    dateDebutChange = true;
                    // si la date de début du droit parent > début du nouveau droit, la date de début de la prestation
                    // la plus récente vaut la date de début du nouveau droit + flag pour empêcher changement
                } else if (BSessionUtil.compareDateFirstGreater(session, droitParent.getDateDebutDroit(),
                        droit.getDateDebutDroit()) == true) {
                    dateDebutPrestationLaPlusRecente = new JADate(droit.getDateDebutDroit());
                    dateDebutChange = true;
                }
            }
        }

        prestationMgr.setToDateFin(ppw.getPeriodeBaseCalcul().getDateFin().toStr("."));
        prestationMgr.setForTypeDifferentDe(IAPPrestation.CS_TYPE_ANNULATION);
        prestationMgr.find(transaction);

        if ((prestationMgr == null) || (prestationMgr.size() == 0)) {
            dateDebutPrestationLaPlusRecente = new JADate(ppw.getPeriodeBaseCalcul().getDateDebut().toStr("."));
        } else {
            if (!dateDebutChange) {
                dateDebutPrestationLaPlusRecente = new JADate("31.12.2999");
            }
        }

        for (final Iterator iter = prestationMgr.iterator(); iter.hasNext();) {
            final APPrestation prestation = (APPrestation) iter.next();

            // point ouvert 00619 changer compareDateFirstLower en compareDateFirstGreater
            if (BSessionUtil.compareDateFirstLower(session, prestation.getDateFin(),
                    pgpc.getDateFin().toStr(".")) == true) {
                pgpc.setDateFin(new JADate(prestation.getDateFin()));
            }

            if (BSessionUtil.compareDateFirstLower(session, prestation.getDateDebut(),
                    dateDebutPrestationLaPlusRecente.toStr(".")) == true) {
                dateDebutPrestationLaPlusRecente = new JADate(prestation.getDateDebut());
            }
        }
        // Si la date de début de la prestation la plus récente est > que la date de début de la période
        // Ce cas arrive lorsque les périodes du droit ne sont pas contiguës
        // Droit [ ]
        // Période du droit [ ] [ ]
        // Prestations [ P1 ] [ P2 ]
        // PGPC [pgpc1] [ pgpc2 ]

        if (BSessionUtil.compareDateFirstGreater(session, dateDebutPrestationLaPlusRecente.toStr("."),
                ppw.getPeriodeBaseCalcul().getDateDebut().toStr(".")) == true) {
            pgpc.setDateDebut(dateDebutPrestationLaPlusRecente);
        }

        ppw.addPeriodePGPC(pgpc);

        return ppw;
    }



    public void reprendreDepuisACOR(final BSession session, final Collection prestationsCourantes,
                                    final APDroitLAPG droit, final FWCurrency fraisGarde, final List<APBaseCalcul> basesCalcul) throws Exception {
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            // Il faut supprimer toutes les prestations du droit au préalable, avant de pouvoir les recalculer. Sinon,
            // elles risqueraient d'etre prise en compte pour le calcul des nouvelles prestations et provoquerait des
            // incohérences.
            APPrestationHelper.removePrestationsDroitByType(session, transaction, droit, null);

            // Calcul des prestations courantes, pour chaque base de calculs données.
            // Toutes les prestations sont ajoutées dans la liste prestationCourantes
            traiterPrestationsCourantes(session, transaction, prestationsCourantes, droit, fraisGarde,basesCalcul);
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        System.out.println(transaction.getErrors());
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    public void restituerPrestations(final BSession session, final APDroitLAPG droit) throws Exception {
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            this.restituerPrestations(session, transaction, droit);

        } catch (final Exception e) {
            transaction.addErrors(e.getMessage());
            throw e;
        } finally {
            try {
                if (transaction.hasErrors()) {
                    System.out.println(transaction.getErrors());
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    public void restituerPrestations(final BSession session, final BTransaction transaction, final APDroitLAPG droit)
            throws Exception {

        // L'id droit du père est l'id permettant de récupérer tous les droits groupés entre eux, cad l'id du droit de
        // base.
        // S'il s'agit du droit parent, idDroitGroupe = droit.getIdDroit
        // S'ils'agit d'un droit enfant, l'idDroitGroupe = droit.getIdDroitParent;
        // 1 seul niveau hiérarchique.
        final String idDroitPere = getIdDroitParent(droit);

        // On récupère les date début et fin de droit couvrant la plus grande plage pour toutes les prestations liées au
        // même parent.
        final APDroitLAPGJoinPrestationManager prestationMgr = new APDroitLAPGJoinPrestationManager();
        prestationMgr.setSession(session);
        prestationMgr.setForIdDroitGroupe(idDroitPere);
        prestationMgr.setForTypeDifferentDe(IAPPrestation.CS_TYPE_ANNULATION);
        prestationMgr.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        prestationMgr.find(transaction, BManager.SIZE_NOLIMIT);

        String dateDebut = "31.12.9999";
        String dateFin = "01.01.1960";

        for (int i = 0; i < prestationMgr.size(); i++) {
            final APPrestation entity = (APPrestation) prestationMgr.getEntity(i);
            if (BSessionUtil.compareDateFirstGreater(session, dateDebut, entity.getDateDebut())) {
                dateDebut = entity.getDateDebut();
            }

            if (BSessionUtil.compareDateFirstLower(session, dateFin, entity.getDateFin())) {
                dateFin = entity.getDateFin();
            }
        }

        if (prestationMgr.size() > 0) {

            // Rechercher prestations actuelles
            final APPeriodeWrapper periodeDroit = new APPeriodeWrapper();
            periodeDroit.setDateDebut(new JADate(dateDebut));
            periodeDroit.setDateFin(new JADate(dateFin));

            // Calcul des PGPC pour la période de droit.
            Collection prestationsCourantesFictive = new TreeSet(new APPrestationWrapperComparator());
            final APPrestationWrapper prestationFictive = new APPrestationWrapper();
            prestationFictive.setPeriodeBaseCalcul(periodeDroit);
            prestationsCourantesFictive.add(prestationFictive);

            prestationsCourantesFictive = calculerPGPCs(session, transaction, idDroitPere, droit.getIdDroit(),
                    prestationsCourantesFictive);

            // Pour chacune des périodes communes (PGPC), rechercher la liste de toutes les prestations contenues, et
            // sommer les montants journaliers.
            final APPrestationWrapper prestationWrapper = (APPrestationWrapper) prestationsCourantesFictive.iterator()
                    .next();
            final Collection colPGPCParPrestation = prestationWrapper.getPeriodesPGPC();

            for (final Iterator iterator = colPGPCParPrestation.iterator(); iterator.hasNext();) {
                final APPeriodeWrapper pgpc = (APPeriodeWrapper) iterator.next();

                // Recherche des prestations contenus dans la PGPC
                final APPrestation[] prestations = getPrestationsContenueDansPeriodes(session, transaction, pgpc,
                        idDroitPere);

                // Représente la période de restitution complète, s'il y a lieu.
                // Égal à la plus petite période commune de toutes les prestations retournées.
                final APPeriodeWrapper pgpcRestitution = new APPeriodeWrapper();
                pgpcRestitution.setDateDebut(pgpc.getDateDebut());
                pgpcRestitution.setDateFin(pgpc.getDateFin());

                if (prestations.length != 0) {
                    creerRestitutions(session, transaction, droit.getIdDroit(), pgpcRestitution, prestations);
                }
            }
        }
    }

    /**
     * Recherche si tous les enfants liés au droit ont ete adopte et retourne true dans ce cas.
     *
     * @param droit
     * @return
     */
    private boolean retrieveIsAdoption(final APDroitLAPG droit, final BISession session) {

        boolean result = true;
        final APEnfantMatListViewBean enfantsMat = new APEnfantMatListViewBean();

        enfantsMat.setSession((BSession) session);
        enfantsMat.setForIdDroitMaternite(droit.getIdDroit());
        BITransaction transaction = null;
        BStatement statement = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            enfantsMat.find(transaction);

            statement = enfantsMat.cursorOpen((BTransaction) transaction);

            APEnfantMatViewBean enfantMat = null;

            while (null != (enfantMat = (APEnfantMatViewBean) enfantsMat.cursorReadNext(statement))) {
                if (!enfantMat.getIsAdoption().booleanValue()) {
                    result = false;
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    try {
                        enfantsMat.cursorClose(statement);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            statement.closeStatement();
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Recherche le nombre de jours effectue de manière contiguë, par le même personne pour un service d'avancement
     */
    private int retrieveNbJoursServiceAvancementEffectue(final BSession session, final BTransaction transaction,
                                                         final APDroitLAPG droit) throws Exception {

        int nbJours = 0;
        String dateDebutDroitCourant = droit.getDateDebutDroit();
        String dateFinDroitPrecedant = "";
        boolean droitPrecedentContiguTrouve = true;

        while (droitPrecedentContiguTrouve) {

            droitPrecedentContiguTrouve = false;

            // calcul de le date de fin du droit precedent
            final JADate dateDebut = new JADate(dateDebutDroitCourant);
            final JACalendarGregorian cal = new JACalendarGregorian();
            final JADate dateFin = cal.addDays(dateDebut, -1);
            dateFinDroitPrecedant = dateFin.toStr(".");

            final APDroitLAPGJointDemandeManager droitDemandeManager = new APDroitLAPGJointDemandeManager();
            droitDemandeManager.setSession(session);
            droitDemandeManager.setForIdTiers(droit.loadDemande().getIdTiers());
            droitDemandeManager.setForGenreService(IAPDroitLAPG.CS_SERVICE_AVANCEMENT);
            final BStatement statement = droitDemandeManager.cursorOpen(transaction);
            APDroitLAPGJointDemande droitDemande = null;

            while (null != (droitDemande = (APDroitLAPGJointDemande) droitDemandeManager.cursorReadNext(statement))) {
                // le droit est contigu au droit courant
                if (droitDemande.getDateFinDroit().equals(dateFinDroitPrecedant)) {

                    final APDroitAPG droitAPG = new APDroitAPG();
                    droitAPG.setSession(session);
                    droitAPG.setIdDroit(droitDemande.getIdDroit());
                    droitAPG.retrieve(transaction);

                    nbJours += Integer.parseInt(droitAPG.getNbrJourSoldes());
                    dateDebutDroitCourant = droitDemande.getDateDebutDroit();
                    droitPrecedentContiguTrouve = true;
                    break;
                } else {
                    droitPrecedentContiguTrouve = false;
                    continue;
                }
            }
        }

        return nbJours;
    }

    public void setViewBean(final APPrestationViewBean viewBean) {
        this.viewBean = viewBean;
    }

    /**
     * Traite les cas spécifiques des prestations ACM.
     */
    private void traiterPrestationsAcmALpha(final BSession session, final BTransaction transaction,
                                            final APDroitLAPG droit, final String genrePrestation) throws Exception {

        final APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        boolean isAllocationMax = false;
        boolean isIndependant = false;
        boolean isVersementAssure = false;
        String dateFinDeContrat = "";

        for (final Iterator iter = mgr.iterator(); iter.hasNext();) {
            final APSituationProfessionnelle element = (APSituationProfessionnelle) iter.next();

            if (!isAllocationMax) {
                isAllocationMax = element.getIsAllocationMax().booleanValue();
            }

            if (!isIndependant) {
                isIndependant = element.getIsIndependant().booleanValue();
            }

            if (!isVersementAssure) {
                isVersementAssure = !element.getIsVersementEmployeur().booleanValue();
            }

            // Mémorise la date de fin de contrat la plus petite
            if (!JadeStringUtil.isEmpty(element.getDateFinContrat())) {

                if (JadeStringUtil.isEmpty(dateFinDeContrat)) {
                    dateFinDeContrat = element.getDateFinContrat();
                } else {
                    if (BSessionUtil.compareDateFirstLower(session, element.getDateFinContrat(), dateFinDeContrat)) {
                        dateFinDeContrat = element.getDateFinContrat();
                    }
                }
            }
        }

        // L'id droit du père est l'id permettant de récupérer tous les droits groupés entre eux, cad l'id du droit de
        // base.
        // S'il s'agit du droit parent, idDroitGroupe = droit.getIdDroit
        // S'ils'agit d'un droit enfant, l'idDroitGroupe = droit.getIdDroitParent;
        // 1 seul niveau hiérarchique.
        final String idDroitPere = getIdDroitParent(droit);

        // Calcul des PGPC pour la période de droit.
        // prestationFictive va contenir les PGPC des prestations en fonctions des prestations actuelle (non annulée) du
        // droit.

        Collection prestationsCourantesFictive = new TreeSet(new APPrestationWrapperComparator());
        final APPrestationWrapper prestationFictive = new APPrestationWrapper();
        final APPeriodeWrapper periodeDroit = new APPeriodeWrapper();
        periodeDroit.setDateDebut(new JADate(droit.getDateDebutDroit()));
        periodeDroit.setDateFin(new JADate(droit.getDateFinDroit()));

        String dateRepriseActivite = null;

        if ((droit instanceof APDroitMaternite)
                && !JadeStringUtil.isBlankOrZero(((APDroitMaternite) droit).getDateRepriseActiv())) {
            dateRepriseActivite = ((APDroitMaternite) droit).getDateRepriseActiv();

            if (BSessionUtil.compareDateFirstLower(session, dateRepriseActivite, droit.getDateFinDroit())) {
                periodeDroit.setDateFin(new JADate(dateRepriseActivite));
            }
        }

        prestationFictive.setPeriodeBaseCalcul(periodeDroit);
        prestationsCourantesFictive.add(prestationFictive);

        prestationsCourantesFictive = calculerPGPCs(session, transaction, null, droit.getIdDroit(),
                prestationsCourantesFictive);

        // Pour chacune des périodes communes (PGPC), rechercher la liste de toutes les prestations contenues, et sommer
        // les montants journaliers.
        final APPrestationWrapper prestationWrapper = (APPrestationWrapper) prestationsCourantesFictive.iterator()
                .next();
        final Collection colPGPCParPrestation = prestationWrapper.getPeriodesPGPC();

        FWCurrency lastRMD = null;
        FWCurrency lastMJ = null;
        String lastNoRevision = null;
        APPrestation lastPrestation = null;
        FWCurrency sommeMontantJournalier = null;

        int nbJoursDepuisDebutDroitAvantPeriode = 0;
        int nbJoursDepuisDebutDroitApresPeriode = 0;

        // on cherche si le tiers a effectue un service d'avancement lors de droits contigus précédant
        if (!(droit instanceof APDroitMaternite)) {
            nbJoursDepuisDebutDroitAvantPeriode = retrieveNbJoursServiceAvancementEffectue(session, transaction, droit);
            nbJoursDepuisDebutDroitApresPeriode = nbJoursDepuisDebutDroitAvantPeriode;
        }

        boolean lastCalculerACM = false;
        int nombreJourSupp = Integer.parseInt(droit.getJoursSupplementaires());
        String dateDeFinAcm = computeDateFinAcm((TreeSet<APPeriodeWrapper>) colPGPCParPrestation, nombreJourSupp);
        for (final Iterator iterator = colPGPCParPrestation.iterator(); iterator.hasNext();) {
            final APPeriodeWrapper pgpc = (APPeriodeWrapper) iterator.next();
            nbJoursDepuisDebutDroitAvantPeriode = nbJoursDepuisDebutDroitApresPeriode;

            // Recherche des prestations contenus dans la PGPC non annulée.
            final APPrestation[] prestations = getPrestationsContenueDansPeriodes(session, transaction, pgpc,
                    idDroitPere);

            // Sommer les montants journaliers des prestations retournées qui ne sont pas annulée ou qui ne vont pas
            // être annulée lors de la génération de la décision.
            sommeMontantJournalier = new FWCurrency(0);

            boolean calculerACM = false;
            lastCalculerACM = calculerACM;
            for (int i = 0; i < prestations.length; i++) {
                // Le droit traité est en attente -> si des prestation de type restitution on été créée pour ce droit,
                // elles ne sont par retournée car elles sont de type annulation.
                // Par contre, les prestations qu'elles annules peuvent être retournée car étant donné que le droit en
                // cours n'est pas 'définitif', les prestations qui vont être annulée au moment de la génération de la
                // décision ne sont pas encore 'annule'. Et il ne faut pas les prendre en compte pour le calcul du
                // montant journalier.

                if (JadeStringUtil.isIntegerEmpty(prestations[i].getIdRestitution())) {

                    // si la prestation n'est pas dans le droit traite et qu'elle n'a pas d'id lot => c'est une
                    // prestation non payée du droit corrige
                    if (!(!prestations[i].getIdDroit().equals(droit.getIdDroit()))
                            && JadeStringUtil.isIntegerEmpty(prestations[i].getIdLot())) {

                        if (IAPPrestation.CS_TYPE_NORMAL.equals(prestations[i].getType())) {

                            final APRepartitionPaiementsManager reManager = new APRepartitionPaiementsManager();
                            reManager.setSession(session);
                            reManager.setForIdPrestation(prestations[i].getIdPrestation());
                            reManager.setForTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);

                            if (reManager.getCount() > 0) {
                                calculerACM = true;
                            }
                        }

                        sommeMontantJournalier.add(prestations[i].getMontantJournalier());
                        nbJoursDepuisDebutDroitApresPeriode = nbJoursDepuisDebutDroitAvantPeriode
                                + Integer.parseInt(prestations[i].getNombreJoursSoldes());
                    }
                }
            }

            lastCalculerACM = calculerACM;
            if (!calculerACM) {
                continue;
            }

            if (prestations.length == 0) {
                continue;
            } else {
                // Crée les prestations de type ACM ou MATERNITE_CANTONALE avec montant 'rétroactif'
                lastRMD = new FWCurrency(prestations[prestations.length - 1].getRevenuMoyenDeterminant());
                lastMJ = new FWCurrency(prestations[prestations.length - 1].getRevenuMoyenDeterminant());
                lastNoRevision = prestations[prestations.length - 1].getNoRevision();
                lastPrestation = prestations[prestations.length - 1];

                // pour les APG du genre service d'avancement
                if (IAPDroitLAPG.CS_SERVICE_AVANCEMENT.equals(droit.getGenreService())
                        && !(droit instanceof APDroitMaternite)) {

                    // avant le 31em jour de service d'avancement
                    if (nbJoursDepuisDebutDroitApresPeriode < 31) {
                        this.creerPrestationsACM(session, transaction, lastPrestation, droit, sommeMontantJournalier,
                                lastMJ, genrePrestation, prestations[prestations.length - 1].getDateDebut(),
                                prestations[prestations.length - 1].getDateFin(),
                                prestations[prestations.length - 1].getNombreJoursSoldes(),
                                prestations[prestations.length - 1].getNoRevision());
                    }
                    // Après le 31em jour de service d'avancement
                    else if (nbJoursDepuisDebutDroitAvantPeriode > 30) {
                        this.creerPrestationsACM(session, transaction, lastPrestation, droit, sommeMontantJournalier,
                                lastMJ, genrePrestation, prestations[prestations.length - 1].getDateDebut(),
                                prestations[prestations.length - 1].getDateFin(),
                                prestations[prestations.length - 1].getNombreJoursSoldes(),
                                prestations[prestations.length - 1].getNoRevision(), true);

                    }
                    // le 31em jour de service d'avancement tombe durant la période, on coupe la période en 2 sur le
                    // 30em jour
                    else {
                        // Calcul du nombre de jours des deux périodes et des dates de début et de fin des périodes
                        final int nbJoursPremierePeriode = APCalculateurPrestationStandardLamatAcmAlpha.NB_JOURS_APG_PLEINE_SERVICE_AVANCEMENT
                                - nbJoursDepuisDebutDroitAvantPeriode;
                        final int nbJoursDeuxiemePeriode = Integer.parseInt(
                                prestations[prestations.length - 1].getNombreJoursSoldes()) - nbJoursPremierePeriode;

                        final JADate dateDebutPremierePeriode = new JADate(
                                prestations[prestations.length - 1].getDateDebut());
                        final JACalendarGregorian cal = new JACalendarGregorian();
                        final JADate dateFinPrmierePeriode = cal.addDays(dateDebutPremierePeriode,
                                nbJoursPremierePeriode - 1);
                        final JADate dateDebutDeuxiemePeriode = cal.addDays(dateFinPrmierePeriode, 1);

                        // la 1er prestation
                        if (nbJoursPremierePeriode > 0) {
                            this.creerPrestationsACM(session, transaction, lastPrestation, droit,
                                    sommeMontantJournalier, lastMJ, genrePrestation,
                                    prestations[prestations.length - 1].getDateDebut(),
                                    dateFinPrmierePeriode.toStr("."), Integer.toString(nbJoursPremierePeriode),
                                    prestations[prestations.length - 1].getNoRevision());
                        }

                        // la 2em prestation
                        if (nbJoursDeuxiemePeriode > 0) {
                            this.creerPrestationsACM(session, transaction, lastPrestation, droit,
                                    sommeMontantJournalier, lastMJ, genrePrestation,
                                    dateDebutDeuxiemePeriode.toStr("."),
                                    prestations[prestations.length - 1].getDateFin(),
                                    Integer.toString(nbJoursDeuxiemePeriode),
                                    prestations[prestations.length - 1].getNoRevision(), true);
                        }

                    }

                }
                // pour tous les cas Mat et les APG qui ne sont pas un service d'avancement
                else {
                    if (droit instanceof APDroitMaternite) {
                        if (isLastACM(pgpc, dateDeFinAcm)) {
                            createLastACM(session, transaction, lastPrestation, droit, sommeMontantJournalier, lastMJ, genrePrestation, prestations[prestations.length - 1], dateDeFinAcm);
                        } else if (isACMToCreate(pgpc, dateDeFinAcm)) {
                            createCurrentACM(session, transaction, lastPrestation, droit, sommeMontantJournalier, lastMJ, genrePrestation, prestations[prestations.length - 1]);
                        }
                    } else {
                        createCurrentACM(session, transaction, lastPrestation, droit, sommeMontantJournalier, lastMJ, genrePrestation, prestations[prestations.length - 1]);
                    }
                }
            }
        }

        // ACM pour allocation de maternité, supplément de 14 jours
        if (droit instanceof APDroitMaternite) {
            final String dureeACM = session.getApplication()
                    .getProperty(APApplication.PROPERTY_DROIT_ACM_MAT_DUREE_JOURS);
            // Les ACM sont toujours calculé sur la durée de maternité de base et non pas sur la durée effective qui pourrait avoir été étendue
            final String dureeMatFederale = session.getApplication()
                    .getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS);
            final int diff = Integer.parseInt(dureeACM) - Integer.parseInt(dureeMatFederale);

            final JACalendar jaCal = new JACalendarGregorian();

            final JADate date = new JADate(droit.getDateFinDroit());

            // dateDebut = dateFinDroit + 1 jour;
            final JADate dateDebut = jaCal.addDays(date, 1);

            // dateFin = dateDebut + (diff - 1);
            final JADate dateFin = jaCal.addDays(dateDebut, diff - 1);
            int nombreJoursSoldes = diff;

            // si on ne calcul pas de prestations LAMat, la somme des montants journalier vaut zero.
            // si on calcul des prestations LAMat.
            // OK SI DUREE ACM = DUREE LAMat
            FWCurrency mj = null;
            if ("false"
                    .equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty(APApplication.PROPERTY_IS_DROIT_MAT_CANTONALE))
                    || !APPrestationHelper.hasLAMatFalgInSitPro(session, droit)) {
                mj = new FWCurrency(0);
            } else {
                mj = sommeMontantJournalier;
            }
            if (lastCalculerACM) {

                // contrôler si il n'y a pas une date de reprise du travail ou une date de fin de contrat dans ce cas
                // plus de prestations ACM
                if (!JadeStringUtil.isEmpty(((APDroitMaternite) droit).getDateRepriseActiv())
                        || !JadeStringUtil.isEmpty(dateFinDeContrat)) {

                    JADate dateFinDePrestation = new JADate();

                    if (!JadeStringUtil.isEmpty(((APDroitMaternite) droit).getDateRepriseActiv())) {
                        dateFinDePrestation = new JADate(((APDroitMaternite) droit).getDateRepriseActiv());
                    }

                    if (!JadeStringUtil.isEmpty(dateFinDeContrat)) {
                        if (JadeStringUtil.isEmpty(((APDroitMaternite) droit).getDateRepriseActiv())) {
                            dateFinDePrestation = new JADate(dateFinDeContrat);
                        } else {
                            if (BSessionUtil.compareDateFirstLower(session,
                                    ((APDroitMaternite) droit).getDateRepriseActiv(), dateFinDeContrat)) {
                                dateFinDePrestation = new JADate(((APDroitMaternite) droit).getDateRepriseActiv());
                            } else {
                                dateFinDePrestation = new JADate(dateFinDeContrat);
                            }
                        }
                    }

                    // mise a jours de du nombre de jours soldes
                    nombreJoursSoldes = new Long(jaCal.daysBetween(dateDebut, dateFinDePrestation)).intValue();

                    if (BSessionUtil.compareDateFirstLower(session, dateFinDePrestation.toStr("."),
                            dateDebut.toStr("."))) {
                        // reprise du travail avant le doit au 14 jours supp.
                    } else {
                        // si la date de reprise est pendant les 14 jours supp.
                        // on set la date de fin de prestation avec la date de reprise
                        if (BSessionUtil.compareDateBetweenOrEqual(session, dateDebut.toStr("."), dateFin.toStr("."),
                                dateFinDePrestation.toStr("."))) {

                            this.creerPrestationsACM(session, transaction, lastPrestation, droit, mj, lastRMD,
                                    genrePrestation, dateDebut.toStr("."),
                                    jaCal.addDays(dateFinDePrestation, -1).toStr("."),
                                    String.valueOf(nombreJoursSoldes), lastNoRevision, isAllocationMax);
                        } else {
                            this.creerPrestationsACM(session, transaction, lastPrestation, droit, mj, lastRMD,
                                    genrePrestation, dateDebut.toStr("."), dateFin.toStr("."),
                                    String.valueOf(nombreJoursSoldes), lastNoRevision, isAllocationMax);
                        }
                    }
                } else {
                    this.creerPrestationsACM(session, transaction, lastPrestation, droit, mj, lastRMD, genrePrestation,
                            dateDebut.toStr("."), dateFin.toStr("."), String.valueOf(nombreJoursSoldes), lastNoRevision,
                            isAllocationMax);
                }
            }
        }
    }

    private boolean isACMToCreate(APPeriodeWrapper periode, String dateDeFinAcm) {
        if (dateDeFinAcm == null) {
            return true;
        } else {
            LocalDate dateDeFinACMDate = parseStringToDateTime(dateDeFinAcm, format);
            LocalDate dateDebutPrestation = parseStringToDateTime(periode.getDateDebut().toString(), formatter);
            LocalDate dateFinPrestation = parseStringToDateTime(periode.getDateFin().toString(), formatter);
            return (dateDeFinACMDate != null && dateDeFinACMDate.isAfter(dateDebutPrestation) && (dateDeFinACMDate.isAfter(dateFinPrestation) || dateDeFinACMDate.equals(dateFinPrestation)));
        }

    }

    private boolean isLastACM(APPeriodeWrapper periode, String dateDeFinAcm) {
        if (dateDeFinAcm == null) {
            return false;
        } else {
            LocalDate dateDeFinACMDate = parseStringToDateTime(dateDeFinAcm, format);
            LocalDate dateDebutPrestation = parseStringToDateTime(periode.getDateDebut().toString(), formatter);
            LocalDate dateFinPrestation = parseStringToDateTime(periode.getDateFin().toString(), formatter);

            return (dateDeFinACMDate != null && dateDeFinACMDate.isAfter(dateDebutPrestation) && (dateDeFinACMDate.isBefore(dateFinPrestation) || dateDeFinACMDate.equals(dateFinPrestation)));
        }
    }

    private String computeDateFinAcm(TreeSet<APPeriodeWrapper> colPGPCParPrestation, int nombreJourSupp) {
        // On recherche la dernière période de presta
        APPeriodeWrapper lastPrestation = colPGPCParPrestation.last();
        String dateDeFinPrestationACM;

        LocalDate dateDeFinPrestDate = parseStringToDateTime(lastPrestation.getDateFin().toString(), formatter);
        if (dateDeFinPrestDate != null) {
            dateDeFinPrestDate = dateDeFinPrestDate.minusDays(nombreJourSupp);
            dateDeFinPrestationACM = dateDeFinPrestDate.format(format);
            return dateDeFinPrestationACM;
        }
        return null;
    }

    /**
     *
     * @param session
     * @param transaction
     * @param lastPrestation
     * @param droit
     * @param sommeMontantJournalier
     * @param lastMJ
     * @param genrePrestation
     * @param prestation
     * @throws Exception
     */
    private void createCurrentACM(BSession session, BTransaction transaction, APPrestation lastPrestation, APDroitLAPG droit, FWCurrency sommeMontantJournalier, FWCurrency lastMJ, String genrePrestation, APPrestation prestation) throws Exception {
        this.creerPrestationsACM(session, transaction, lastPrestation, droit, sommeMontantJournalier,
                lastMJ, genrePrestation, prestation.getDateDebut(),
                prestation.getDateFin(),
                prestation.getNombreJoursSoldes(),
                prestation.getNoRevision());
    }

    /**
     *
     *
     *
     * @param session
     * @param transaction
     * @param lastPrestation
     * @param droit
     * @param sommeMontantJournalier
     * @param lastMJ
     * @param genrePrestation
     * @param prestation
     * @param dateDeFinAcm
     * @throws Exception
     */
    private void createLastACM(BSession session, BTransaction transaction, APPrestation lastPrestation, APDroitLAPG droit, FWCurrency sommeMontantJournalier, FWCurrency lastMJ, String genrePrestation, APPrestation prestation, String dateDeFinAcm) throws Exception {

        LocalDate dateDeFinACMDate = parseStringToDateTime(dateDeFinAcm, format);
        LocalDate dateDeDebutPrestDate = parseStringToDateTime(prestation.getDateDebut(), format);
        if (dateDeFinACMDate != null && dateDeDebutPrestDate != null) {
            String nombreJourSoldes = String.valueOf(ChronoUnit.DAYS.between(dateDeDebutPrestDate, dateDeFinACMDate));
            this.creerPrestationsACM(session, transaction, lastPrestation, droit, sommeMontantJournalier,
                    lastMJ, genrePrestation, prestation.getDateDebut(),
                    prestation.getDateFin(),
                    nombreJourSoldes,
                    prestation.getNoRevision());
        }

    }

    /**
     *
     *
     * @param date
     * @param formatter
     * @return
     */
    private LocalDate parseStringToDateTime(String date, DateTimeFormatter formatter) {
        if (!JadeStringUtil.isBlankOrZero(date)) {
            return LocalDate.parse(date, formatter);
        }
        return null;
    }

    //ESVE MATERNITE CALCULATEUR STANDARD
    private void traiterPrestationsCourantes(final BSession session, final BTransaction transaction,
                                             Collection prestationsCourantes, final APDroitLAPG droit, final FWCurrency fraisGarde, final List<APBaseCalcul> basesCalcul) throws Exception {

        // Màj des frais de garde au prorata en fonction du nombre de prestation à créer
        prestationsCourantes = initPrestationFraisGarde(prestationsCourantes, fraisGarde);

        // Génération des plus grandes périodes communes pour des périodes de base et un droit donnée.
        // Mise à jours des prestations courantes en leurs ajoutant les PGPC.
        prestationsCourantes = calculerPGPCs(session, transaction, null, droit.getIdDroit(), prestationsCourantes);

        // Restitution des prestations
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        this.restituerPrestations(session, transaction, droit);

        if(droit instanceof APDroitMaternite && basesCalcul!=null) {
            prestationsCourantes = APPrestationExtensionSplitter.periodeExtentionSpliter(basesCalcul, (SortedSet<APPrestationWrapper>)prestationsCourantes, droit);
        }

        // List des prestations à créer.
        List listDesPrestationsACreer = new ArrayList();

        // Pour chacune des périodes communes (PGPC), rechercher la liste de toutes les prestations contenues, et sommer
        // les montants journaliers.
        for (final Iterator iter = prestationsCourantes.iterator(); iter.hasNext();) {
            final APPrestationWrapper prestationWrapper = (APPrestationWrapper) iter.next();

            final Collection colPGPCParPrestation = prestationWrapper.getPeriodesPGPC();

            for (final Iterator iterator = colPGPCParPrestation.iterator(); iterator.hasNext();) {
                final APPeriodeWrapper pgpc = (APPeriodeWrapper) iterator.next();

                // Ajoute la prestation à créer
                listDesPrestationsACreer = ajouterPrestationACreer(listDesPrestationsACreer, prestationWrapper, pgpc,
                        droit);

            }
        }
        if (!listDesPrestationsACreer.isEmpty()) {
            // On fusionne les prestations contiguës, avec même montant d'allocation journalière
            listDesPrestationsACreer = fusionnerPrestationsACreer(session, listDesPrestationsACreer);

            // Créer les prestations
            creerPrestations(session, transaction, droit, listDesPrestationsACreer);
        }
    }

    /**
     * Traite les cas spécifiques des prestations maternité cantonale.
     */
    private void traiterPrestationsMaternite(final BSession session, final BTransaction transaction,
            final APDroitLAPG droit, final String genrePrestation, final boolean isAllocationMax) throws Exception {

        // prestation du genre LAMAT (Droit genevois)
        if (APTypeDePrestation.LAMAT.isCodeSystemEqual(genrePrestation)) {

            // L'id droit du père est l'id permettant de récupérer tous les droits groupés entre eux, car c'est l'id du
            // droit de base.
            // S'il s'agit du droit parent, idDroitGroupe = droit.getIdDroit
            // S'ils'agit d'un droit enfant, l'idDroitGroupe = droit.getIdDroitParent;
            // 1 seul niveau hiérarchique.
            final String idDroitPere = getIdDroitParent(droit);

            // Calcul des PGPC pour la période de droit.
            // prestationFictive va contenir les PGPC des prestations en fonctions des prestations actuelle (non
            // annulée) du droit.
            Collection prestationsCourantesFictive = new TreeSet(new APPrestationWrapperComparator());
            final APPrestationWrapper prestationFictive = new APPrestationWrapper();
            final APPeriodeWrapper periodeDroit = new APPeriodeWrapper();

            periodeDroit.setDateDebut(new JADate(droit.getDateDebutDroit()));
            periodeDroit.setDateFin(new JADate(droit.getDateFinDroit()));

            String dateRepriseActivite = null;

            if ((droit instanceof APDroitMaternite)
                    && !JadeStringUtil.isBlankOrZero(((APDroitMaternite) droit).getDateRepriseActiv())) {
                dateRepriseActivite = ((APDroitMaternite) droit).getDateRepriseActiv();

                if (BSessionUtil.compareDateFirstLower(session, dateRepriseActivite, droit.getDateFinDroit())) {
                    periodeDroit.setDateFin(new JADate(dateRepriseActivite));
                }
            }

            // on cherche si il y a un date de fin de contract
            String dateFinDeContrat = "";
            final APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
            mgr.setSession(session);
            mgr.setForIdDroit(droit.getIdDroit());
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            final Iterator iterSP = mgr.iterator();
            while (iterSP.hasNext()) {
                final APSituationProfessionnelle sp = (APSituationProfessionnelle) iterSP.next();

                // mémorise la date de fin de contrat la plus petite
                if (!JadeStringUtil.isEmpty(sp.getDateFinContrat())) {

                    if (JadeStringUtil.isEmpty(dateFinDeContrat)) {
                        dateFinDeContrat = sp.getDateFinContrat();
                    } else {
                        if (BSessionUtil.compareDateFirstLower(session, sp.getDateFinContrat(), dateFinDeContrat)) {
                            dateFinDeContrat = sp.getDateFinContrat();
                        }
                    }
                }
            }

            prestationFictive.setPeriodeBaseCalcul(periodeDroit);
            prestationsCourantesFictive.add(prestationFictive);

            prestationsCourantesFictive = calculerPGPCs(session, transaction, null, droit.getIdDroit(),
                    prestationsCourantesFictive);

            // Pour chacune des périodes communes (PGPC), rechercher la liste de toutes les prestations contenues, et
            // sommer les montants journaliers.
            final APPrestationWrapper prestationWrapper = (APPrestationWrapper) prestationsCourantesFictive.iterator()
                    .next();
            final Collection colPGPCParPrestation = prestationWrapper.getPeriodesPGPC();

            FWCurrency lastRMD = null;
            String lastNoRevision = null;
            APPrestation lastPrestation = null;

            for (final Iterator iterator = colPGPCParPrestation.iterator(); iterator.hasNext();) {
                final APPeriodeWrapper pgpc = (APPeriodeWrapper) iterator.next();

                // Recherche des prestations contenus dans la PGPC non annulée.
                final APPrestation[] prestations = getPrestationsContenueDansPeriodes(session, transaction, pgpc,
                        idDroitPere);

                // Sommer les montants journaliers des prestations retournées qui ne sont pas annulée ou qui ne vont pas
                // être annulée lors de la génération de la décision.
                final FWCurrency sommeMontantJournalier = new FWCurrency(0);

                for (int i = 0; i < prestations.length; i++) {
                    // Le droit traité est en attente -> si des prestation de type restitution on été créée pour ce
                    // droit, elles ne sont par retournée car elles sont de type annulation.
                    // Par contre, les prestations qu'elles annules peuvent être retournée car étant donné que le droit
                    // en cours n'est pas 'définitif', les prestations qui vont être annulée au moment de la génération
                    // de la décision ne sont pas encore 'annule'. Et il ne faut pas les prendre en compte pour le
                    // calcul du montant journalier.
                    if (JadeStringUtil.isIntegerEmpty(prestations[i].getIdRestitution())) {

                        // si la prestation n'est pas dans le droit traite et qu'elle n'a pas d'id lot => c'est une
                        // prestation non payée du droit corrigé
                        if (!(!prestations[i].getIdDroit().equals(droit.getIdDroit()))
                                && JadeStringUtil.isIntegerEmpty(prestations[i].getIdLot())) {
                            sommeMontantJournalier.add(prestations[i].getMontantJournalier());
                        }
                    }
                }

                if (prestations.length == 0) {
                    continue;
                } else {
                    // Crée les prestations de type LAMat avec montant 'rétroactif'
                    lastRMD = new FWCurrency(prestations[prestations.length - 1].getRevenuMoyenDeterminant());
                    lastNoRevision = prestations[prestations.length - 1].getNoRevision();
                    lastPrestation = prestations[prestations.length - 1];

                    creerPrestationsLAMat(session, transaction, lastPrestation, droit, sommeMontantJournalier,
                            new FWCurrency(prestations[prestations.length - 1].getRevenuMoyenDeterminant()),
                            genrePrestation, prestations[prestations.length - 1].getDateDebut(),
                            prestations[prestations.length - 1].getDateFin(),
                            prestations[prestations.length - 1].getNombreJoursSoldes(),
                            prestations[prestations.length - 1].getNoRevision(), isAllocationMax);
                }
            }

            // LAMat, supplément de 14 jours du droit fédéral
            if (droit instanceof APDroitMaternite) {
                final String dureeLAMat = session.getApplication()
                        .getProperty(APProperties.DROIT_ACM_MAT_DUREE_JOURS.getPropertyName());
                final String dureeMatFederale = session.getApplication()
                        .getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS);
                final int diff = Integer.parseInt(dureeLAMat) - Integer.parseInt(dureeMatFederale);

                final JACalendar jaCal = new JACalendarGregorian();

                final JADate date = new JADate(droit.getDateFinDroit());

                // dateDebut = dateFinDroit + 1 jour;
                final JADate dateDebut = jaCal.addDays(date, 1);

                // dateFin = dateDebut + (diff-1);
                final JADate dateFin = jaCal.addDays(dateDebut, diff - 1);
                int nombreJoursSoldes = diff;

                // contrôler si il n'y a pas une date de reprise du travail dans ce cas plus de prestations LAMat
                if (!JadeStringUtil.isEmpty(((APDroitMaternite) droit).getDateRepriseActiv())) {

                    final JADate dateReprise = new JADate(((APDroitMaternite) droit).getDateRepriseActiv());

                    if (BSessionUtil.compareDateFirstLower(session, dateReprise.toStr("."), dateDebut.toStr("."))) {
                        // reprise du travail avant le doit au 14 jours supp.
                    } else {

                        String dateFinPeriode = dateFin.toStr(".");

                        // si la date de reprise est pendant les 14 jours supp. on set la date de fin de prestation avec
                        // la date de reprise
                        if (BSessionUtil.compareDateBetweenOrEqual(session, dateDebut.toStr("."), dateFin.toStr("."),
                                dateReprise.toStr("."))) {

                            // mise a jours de du nombre de jours soldes
                            nombreJoursSoldes = new Long(jaCal.daysBetween(dateDebut, dateReprise)).intValue();
                            // mise a jours de la date de fin
                            dateFinPeriode = jaCal.addDays(dateReprise, -1).toStr(".");
                        }

                        if (JadeStringUtil.isEmpty(dateFinDeContrat) || BSessionUtil.compareDateFirstLower(session,
                                dateFinDeContrat, dateDebut.toStr("."))) {
                            creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0),
                                    lastRMD, genrePrestation, dateDebut.toStr("."), dateFinPeriode,
                                    String.valueOf(nombreJoursSoldes), lastNoRevision, isAllocationMax);
                        } else {

                            creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0),
                                    lastRMD, genrePrestation, dateDebut.toStr("."), jaCal.addDays(dateFinDeContrat, -1),
                                    String.valueOf(new Long(jaCal.daysBetween(dateDebut,
                                            new JADate(jaCal.addDays(dateFinDeContrat, -1)))).intValue() + 1),
                                    lastNoRevision, isAllocationMax);

                            creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0),
                                    lastRMD, genrePrestation, dateFinDeContrat, dateFinPeriode,
                                    String.valueOf(new Long(
                                            jaCal.daysBetween(new JADate(dateFinDeContrat), new JADate(dateFinPeriode)))
                                            .intValue()
                                            + 1),
                                    lastNoRevision, isAllocationMax);

                        }

                    }
                } else {
                    if (JadeStringUtil.isEmpty(dateFinDeContrat)
                            || BSessionUtil.compareDateFirstLower(session, dateFinDeContrat, dateDebut.toStr("."))) {
                        creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0), lastRMD,
                                genrePrestation, dateDebut.toStr("."), dateFin.toStr("."),
                                String.valueOf(nombreJoursSoldes), lastNoRevision, isAllocationMax);
                    } else {
                        creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0), lastRMD,
                                genrePrestation, dateDebut.toStr("."), jaCal.addDays(dateFinDeContrat, -1),
                                String.valueOf(new Long(
                                        jaCal.daysBetween(dateDebut, new JADate(jaCal.addDays(dateFinDeContrat, -1))))
                                        .intValue()
                                        + 1),
                                lastNoRevision, isAllocationMax);

                        creerPrestationsLAMat(session, transaction, lastPrestation, droit, new FWCurrency(0), lastRMD,
                                genrePrestation, dateFinDeContrat, dateFin.toStr("."),
                                String.valueOf(
                                        new Long(jaCal.daysBetween(new JADate(dateFinDeContrat), dateFin)).intValue()
                                                + 1),
                                lastNoRevision, isAllocationMax);
                    }
                }
            }
        }
    }

    private BigDecimal[] getMontantMaxMinLamat(BSession session) throws Exception {
        BigDecimal montantMax = new BigDecimal(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "0",
                "M_MAXGROS", "01.03.2015", "", 2));
        BigDecimal montantMin = new BigDecimal(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "0",
                "M_MAXGROS", "01.03.2015", "", 2));

        return new BigDecimal[] { montantMax, montantMin };

    }

    /**
     * Mets à jours toutes les prestations liées à la restitution de la prestation courante. Mets à jours le champ
     * idRestitution.
     */
    private void updatePrestationsRestituees(final BTransaction transaction, final String idPrestationDeRestitution,
                                             final APPrestation[] prestations) throws Exception {
        // Mise à jours des prestations qui vont être annulées par cette restitution complète
        for (int i = 0; i < prestations.length; i++) {
            prestations[i].retrieve(transaction);
            prestations[i].setIdRestitution(idPrestationDeRestitution);
            prestations[i].update(transaction);
        }
    }

    private void updateTauxJournalierPrestation(final APDroitLAPG droit, final BISession session,
                                                final BITransaction transaction) throws Exception {
        this.updateTauxJournalierPrestation(droit, session, transaction, "31.12.2999");
    }

    /**
     * Modifie à 0 CHF le taux journalier des prestations du Droit donne
     *
     * @param droit
     * @param session
     */
    private void updateTauxJournalierPrestation(final APDroitLAPG droit, final BISession session,
                                                final BITransaction transaction, final String dateFin) throws Exception {

        final APPrestationListViewBean prestations = new APPrestationListViewBean();

        prestations.setSession((BSession) session);
        prestations.setForIdDroit(droit.getIdDroit());
        prestations.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
        prestations.setToDateFin(dateFin);

        prestations.find(transaction, BManager.SIZE_NOLIMIT);

        BStatement statement = null;
        statement = prestations.cursorOpen((BTransaction) transaction);

        APPrestationViewBean prestation = null;

        while (null != (prestation = (APPrestationViewBean) prestations.cursorReadNext(statement))) {
            prestation.setMontantJournalier("0");
            prestation.update(transaction);
        }
    }

}
