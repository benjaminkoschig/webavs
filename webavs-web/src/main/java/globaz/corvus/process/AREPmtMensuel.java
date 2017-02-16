package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RECountRentesManager;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.process.paiement.mensuel.Key;
import globaz.corvus.utils.pmt.mensuel.RECumulPrstParRubrique;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASecteurTypeSection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.process.PRAbstractProcess;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.util.prestations.MotifVersementUtil;

/**
 * @author SCR
 */
public abstract class AREPmtMensuel extends PRAbstractProcess {

    private static Logger logger = LoggerFactory.getLogger(AREPmtMensuel.class);

    private static final long serialVersionUID = 1L;

    /**
     * @author SCR
     */
    class PlageIncrement {
        public long max = 0;
        public long min = 0;
    }

    public static final String NO_SECTEUR_RENTE = "200";
    public static final String PARAM_APPLIC = "CORVUS";
    public static final String PARAM_SECTEUR_RENTE_USER_KEY = "SECTEUR";

    // Référence sur la CA, pour le traitement des écritures
    // des RA bloquées / avec retenues
    protected APIGestionComptabiliteExterne comptaExt = null;
    private String dateEcheancePaiement = "";
    private String description = "";
    private String emailObject = "";
    private String idOrganeExecution = "";
    private String moisPaiement = "";
    private String numeroOG = "";
    private BISession sessionOsiris = null;

    // SEPA iso20002
    private String isoCsTypeAvis = "";
    private String isoGestionnaire = "";
    private String isoHighPriority = "";
    private Boolean isIso = null;

    public AREPmtMensuel() {
        super();
    }

    public AREPmtMensuel(boolean initThreadContext, BProcess parent) {
        super(initThreadContext, parent);
    }

    public AREPmtMensuel(boolean initThreadContext, BSession session) {
        super(initThreadContext, session);
    }

    public AREPmtMensuel(BProcess parent) {
        this(false, parent);
    }

    public AREPmtMensuel(BSession session) {
        this(false, session);
    }

    /**
     * Test que la rubrique comptable fournie en paramètres ne soit pas null. Si c'est le cas, une exception sera lancée
     * 
     * @param rubriqueComptable
     *            La rubrique à tester
     * @param prestationsAccordee
     *            La rente utilisée pour la récupération de la rubrique
     * @throws Exception
     *             Dans le cas ou la rubrique comptable est null
     */
    protected void checkRubriqueNotNull(APIRubrique rubriqueComptable, REPaiementRentes prestationsAccordee)
            throws Exception {
        if (rubriqueComptable == null) {
            throw new Exception("Aucune rubrique comptable trouvée pour la rente accordée avec l'id : ["
                    + prestationsAccordee.getIdRenteAccordee() + "], codePrestation : ["
                    + prestationsAccordee.getCodePrestation() + "], sousTypeGenrePrestation : ["
                    + prestationsAccordee.getSousTypeCodePrestation() + "]");
        }
    }

    protected BTransaction commitResetTransaction(BTransaction transaction) throws Exception {

        if (transaction == null) {
            throw new Exception("commitResetTransaction(BTransaction transaction) La transaction est null");
        }
        if (transaction.hasErrors()) {
            throw new Exception("commitResetTransaction(BTransaction transaction) La transaction est en erreur : "
                    + transaction.getErrors().toString());
        } else {
            transaction.commit();
            transaction.closeTransaction();
            transaction.openTransaction();
        }
        return transaction;
    }

    /**
     * On flag la RenteAccordee en erreur...
     * 
     * @param session
     * @param idRA
     * @param errorMsg
     * @throws Exception
     */
    protected void doMiseEnErreurRA(BSession session, String idRA, String errorMsg) {
        String message = "Mise en erreur de la rente avec l'id [" + idRA + "] pour la raison suivante : " + errorMsg;
        logger.warn(message);
        JadeThread.logClear();
        BITransaction transaction = null;
        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            getMemoryLog().logMessage(message, FWMessage.AVERTISSEMENT, this.getClass().toString());
            getMemoryLog().logMessage("\t" + errorMsg, FWMessage.AVERTISSEMENT, this.getClass().toString());

            // bz-6124
            REPrestationsAccordees pa = new REPrestationsAccordees();
            pa.setSession(session);
            pa.setIdPrestationAccordee(idRA);
            pa.retrieve(transaction);
            PRAssert.notIsNew(pa, "Impossible de récupérer la rente avec l'id [" + idRA + "] pour la flagger en erreur");
            pa.setIsErreur(Boolean.TRUE);
            pa.update(transaction);

            if (transaction.hasErrors()) {
                String message2 = "Impossible de mettre la rente avec l'id [" + idRA
                        + "] en erreur car la transaction contiens des erreurs : " + transaction.getErrors().toString();

                logger.error(message2);
                getMemoryLog().logMessage(FWViewBeanInterface.ERROR, message2, this.getClass().toString());
            }
            transaction.commit();

        } catch (Exception e) {
            String message3 = "Une exception grave est survenue lors de la mise en erreur de la rente avec l'id ["
                    + idRA + "] : " + e.toString()
                    + ". La rente ne sera PAS mise en erreur car la transaction sera rollbackée";

            logger.error(message3, e);
            getMemoryLog().logMessage(FWViewBeanInterface.ERROR, message3, this.getClass().toString());

            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e2) {
                    logger.error(
                            "Une exception critique est appararue lors du rollback de la transaction : "
                                    + e2.toString(), e2);
                }
            }
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e3) {
                    logger.error("Une exception critique est appararue lors du closeTransaction()  : " + e3.toString(),
                            e3);
                }
            }
        }
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    /**
     * En priorité, retourne l'idAdressePmt du domaine des rentes, et si n'existe pas, prend le domaine standard. SAUF
     * SI : le code prestation est 119 ou 159 car ces 2 prestations sont de type PC et représente des allocations de
     * Noël payés par mandat postal. Dans ces 2 cas là, l'adresse de paiement doit être uniquement récupérée dans le
     * domaine des allocation de Noël @see {@link IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL} Il existe
     * également les code prestation 118 et 158 liés aux PC allocation de Noël mais dans ces deux cas là, la recherche
     * de l'adresse de paiement s'effectue de manière standard (Domaine rente / standard)
     * 
     * @param rente
     * @return l'id de l'adresse de pmt. retourne null si aucune adresse.
     */
    protected String getIdAdrPmt(REPaiementRentes rente) {
        String result = null;
        if (!JadeStringUtil.isEmpty(rente.getCodePrestation())) {
            if (rente.getCodePrestation().equals(PRCodePrestationPC._119.getCodePrestationAsString())
                    || rente.getCodePrestation().equals(PRCodePrestationPC._159.getCodePrestationAsString())) {
                result = rente.getIdAdressePmtAllocNoel();
            } else if (!JadeStringUtil.isBlankOrZero(rente.getIdAdressePmtRente())) {
                result = rente.getIdAdressePmtRente();
            } else if (!JadeStringUtil.isBlankOrZero(rente.getIdAdresseStd())) {
                result = rente.getIdAdresseStd();
            }
        }
        return result;
    }

    /**
     * Retourne l'id du compte courant
     * 
     * @param session
     * @param transaction
     * @param idTypeSection
     * @param idSecteur
     * @return
     * @throws Exception
     */
    protected String getIdCompteCourant(BSession session, BTransaction transaction, String idTypeSection,
            String idSecteur) throws Exception {

        CASecteurTypeSection elm = new CASecteurTypeSection();
        elm.setSession(session);
        elm.setIdTypeSection(idTypeSection);
        elm.setIdSecteur(idSecteur);
        elm.retrieve(transaction);
        PRAssert.notIsNew(elm, null);
        return elm.getIdCompteCourant();
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Retourne l'id de la rubrique pour l'ordre de versement
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    protected String getIdRubriquePourVersement(BSession session, BTransaction transaction) throws Exception {
        CAOrganeExecution oe = new CAOrganeExecution();
        oe.setSession(session);
        oe.setIdOrganeExecution(getIdOrganeExecution());
        oe.retrieve(transaction);
        PRAssert.notIsNew(oe, null);
        return oe.getIdRubrique();
    }

    protected Key getKey(REPaiementRentes rente) {
        return new Key(rente.getIdCompteAnnexe(), rente.getIdTiersAdressePmt(), rente.getReferencePmt());
    }

    public String getMoisPaiement() {
        return moisPaiement;
    }

    /**
     * Taille maximum disponible : 4x35 char
     * 
     * @param nss
     * @param datePmt
     * @param nom
     * @param prenom
     * @param refPmt
     * @param genrePrestation
     * @return null si aucun libelle trouvé pour le genre de prestation
     */
    public String getMotifVersement(String nss, String datePmt, String nom, String prenom, String refPmt,
            String genrePrestation, String codeISOLangue) {

        try {
            String nonPrenom = nom + " " + prenom;
            String strGenrePrest = AREModuleComptable.getLibelleRubrique(getSession(), genrePrestation, codeISOLangue);
            return MotifVersementUtil.formatPaiementMensuel(nss, nonPrenom, refPmt, strGenrePrest, datePmt);
        } catch (Exception e) {
            return null;
        }
    }

    protected int getNombreRentes(BSession session, BTransaction transaction, Boolean isErreur,
            Boolean isPrestationsBloquees, Boolean isRetenues) throws Exception {

        RECountRentesManager raManager = new RECountRentesManager();
        raManager.setSession(getSession());
        raManager.setForDatePmt(getMoisPaiement());
        raManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + "," + IREPrestationAccordee.CS_ETAT_VALIDE
                + "," + IREPrestationAccordee.CS_ETAT_DIMINUE);
        raManager.setForIsEnErreur(isErreur);
        raManager.setForIsPrestationBloquee(isPrestationsBloquees);
        raManager.setForIsRetenue(isRetenues);
        return raManager.getCount(transaction);
    }

    protected String getNomCache(REPaiementRentes rente) {
        return rente.getNomTBE() + " " + rente.getPrenomTBE();
    }

    protected String getNoSecteurRente(BSession session) {
        try {
            FWParametersManager mgr = new FWParametersManager();
            mgr.setSession(session);
            mgr.setForApplication(AREPmtMensuel.PARAM_APPLIC);
            mgr.setForIdCle(AREPmtMensuel.PARAM_SECTEUR_RENTE_USER_KEY);
            mgr.find(1);

            if (!mgr.isEmpty()) {
                FWParameters p = (FWParameters) mgr.getFirstEntity();
                return p.getValeurAlpha();
            } else {
                return AREPmtMensuel.NO_SECTEUR_RENTE;
            }
        } catch (Exception e) {
            logger.error("Exception thrown when triing to get numéro de secteur rente : " + e.toString()
                    + " Default number will be returned [" + AREPmtMensuel.NO_SECTEUR_RENTE + "] ", e);
            return AREPmtMensuel.NO_SECTEUR_RENTE;
        }
    }

    /**
     * Retourne le no de la section formatté : aaaa700mm
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public String getNoSection(BSession session, JADate datePmtEnCours, int sectionIncrement) throws Exception {
        // No mois sur 3 positions
        String noMois_XX = "";
        if (datePmtEnCours.getMonth() < 10) {
            noMois_XX = "0" + String.valueOf(datePmtEnCours.getMonth());
        } else if (datePmtEnCours.getMonth() >= 10) {
            noMois_XX = String.valueOf(datePmtEnCours.getMonth());
        }
        return String.valueOf(datePmtEnCours.getYear()) + "70" + noMois_XX + String.valueOf(sectionIncrement);
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public APIGestionComptabiliteExterne initComptaExterne(BTransaction transaction, boolean createJournalIfNotExist)
            throws Exception {

        String libelle = getDescription() + " (suite)";
        if (libelle.length() > 50) {
            libelle = libelle.substring(0, 49);
        }

        if (comptaExt == null) {
            sessionOsiris = initSessionOsiris();
            comptaExt = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
            comptaExt.setDateValeur("01." + getMoisPaiement());
            comptaExt.setEMailAddress(getEMailAddress());
            FWMemoryLog comptaMemoryLog = new FWMemoryLog();
            comptaMemoryLog.setSession((BSession) sessionOsiris);
            comptaExt.setMessageLog(comptaMemoryLog);
            comptaExt.setSendCompletionMail(false);
            comptaExt.setTransaction(transaction);
            comptaExt.setProcess(this);
            comptaExt.setLibelle(libelle);
            comptaExt.createJournal();
        }

        if (createJournalIfNotExist && ((comptaExt.getJournal() == null) || comptaExt.getJournal().isNew())) {
            // comptaExt.setLibelle(getSession().getLabel("PMT_MENSUEL_LIB_CA_JRN_BLOC_RET")
            // +" " + getMoisPaiement());
            comptaExt.setLibelle(libelle);
            comptaExt.createJournal();
        }
        return comptaExt;
    }

    public BISession initSessionOsiris() throws Exception {
        if (sessionOsiris == null) {
            sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");
        }
        return sessionOsiris;
    }

    /**
     * @param session
     * @param nombreRentes
     * @return null si l'incrémentation n'a pas pu se faire, la plage d'incrément autrement
     * @throws Exception
     */
    synchronized protected PlageIncrement reserverPlageIncrementsOperationsCA(BSession session,
            BTransaction transaction, long nombreRentes) {

        PlageIncrement plageIncr = new PlageIncrement();
        try {
            // MAJ de l'increment de la CA
            FWIncrementation increment = new FWIncrementation();
            increment.setSession(session);
            increment.setIdIncrement(CAOperation.TABLE_CAOPERP);
            increment.setIdCodeSysteme("");
            increment.setAnneeIncrement("");
            increment.retrieve(transaction);
            PRAssert.notIsNew(increment, null);

            // calcul de la nouvelle valeur de l'increment CA
            plageIncr.min = Long.parseLong(increment.getValeurIncrement()) + 1;
            plageIncr.max = plageIncr.min + (nombreRentes * 3);

            long diff = plageIncr.max;
            diff -= plageIncr.min;

            diff = diff / 100;
            // Bah, on est jamais trop prudent, réserve de plage de 1% (min
            // : 10, max : 1000)!!!
            // Réserve de sécurité
            if (diff > 1000) {
                diff = 1000;
            } else if (diff < 10) {
                diff = 10;
            }
            plageIncr.max += diff;

            // mise a jours de l'increment
            increment.setValeurIncrement(Long.toString(plageIncr.max));
            increment.update(transaction);
        } catch (Exception e) {
            String message = "Une exception fatale s'est produite lors de la reserveation de la plage d'increment pour les opérations CA";
            logger.error(message, e);
            throw new RETechnicalException(message, e);
        }
        return plageIncr;
    }

    synchronized protected PlageIncrement reserverPlageIncrementsSectionsCA(BSession session, long nombreRentes) {
        PlageIncrement plageIncr = new PlageIncrement();
        BTransaction transaction2 = null;
        try {
            transaction2 = (BTransaction) getSession().newTransaction();

            transaction2.openTransaction();

            // MAJ de l'increment de la CA
            FWIncrementation increment = new FWIncrementation();
            increment.setSession(session);
            increment.setIdIncrement(CASection.TABLE_CASECTP);
            increment.setIdCodeSysteme("");
            increment.setAnneeIncrement("");
            increment.retrieve(transaction2);
            PRAssert.notIsNew(increment, null);

            // calcul de la nouvelle valeur de l'increment CA
            plageIncr.min = Long.parseLong(increment.getValeurIncrement()) + 1;
            plageIncr.max = plageIncr.min + nombreRentes;

            // Bah, on est jamais trop prudent !!!
            // Réserve de sécurité de 1%

            long diff = plageIncr.max;
            diff -= plageIncr.min;

            diff = diff / 100;
            // Bah, on est jamais trop prudent, réserve de plage de 1% (min
            // : 10, max : 1000)!!!
            // Réserve de sécurité
            if (diff > 1000) {
                diff = 1000;
            } else if (diff < 10) {
                diff = 10;
            }
            plageIncr.max += diff;

            increment.setValeurIncrement(new Long(plageIncr.max).toString());
            increment.update(transaction2);

            if (!transaction2.hasErrors()) {
                transaction2.commit();
            } else {
                transaction2.rollback();
                transaction2.clearErrorBuffer();
                throw new RETechnicalException("Error while updating increment for table : "
                        + CAOperation.TABLE_CAOPERP);
            }

        } catch (Exception e) {
            String message = "Une exception s'est produite lors de la réservation de la plage d'incrément des sections CA ["
                    + CAOperation.TABLE_CAOPERP + "] : " + e.toString();
            logger.error(message, e);
        } finally {
            if (transaction2 != null) {
                try {
                    transaction2.closeTransaction();
                } catch (Exception e) {
                    logger.error("A fatal exception has throen when closing transaction !");
                }
            }
        }

        // TODO ajouter des tests sur la valeurs des incréments
        return plageIncr;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setMoisPaiement(String moisPaiement) {
        this.moisPaiement = moisPaiement;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public void setIsoHighPriority(String isoHighPriority) {
        this.isoHighPriority = isoHighPriority;
    }

    protected boolean isIso20022(String idOrganeExecution, BSession session) {
        if (isIso == null) {
            CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
            mgr.setSession(session);
            mgr.setForIdOrganeExecution(idOrganeExecution);
            try {
                mgr.find();
                if (mgr.size() != 1) {
                    throw new Exception();
                }
            } catch (Exception e) {
                getSession().addError("PMT_AVANCE_IDORGANEEXEC_NULL");
            }

            isIso = ((CAOrganeExecution) mgr.getEntity(0)).getIdTypeTraitementOG().equals(
                    APIOrganeExecution.OG_ISO_20022);
        }
        return isIso.booleanValue();
    }

    protected void validationPmt(BSession session, APIGestionRentesExterne comptaFast,
            Map<Integer, RECumulPrstParRubrique> cumulParGenreRente) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();

            HashMap<String, String> mapRentesStd = new HashMap<String, String>();
            HashMap<String, String> mapRentesBlocageRetenue = new HashMap<String, String>();
            Set<Integer> keys = cumulParGenreRente.keySet();

            for (Integer key : keys) {
                RECumulPrstParRubrique cppr = cumulParGenreRente.get(key);

                // On ne prend pas en
                if (RECumulPrstParRubrique.TYPE_STANDARD.equals(cppr.getType())) {
                    mapRentesStd.put(cppr.getIdRubrique(), cppr.getMontant().toString());
                } else {
                    mapRentesBlocageRetenue.put(cppr.getIdRubrique(), cppr.getMontant().toString());
                }
            }
            comptaFast.checkIntegrity(session, transaction, mapRentesStd);
            transaction.commit();
        } catch (Exception e) {
            String message = "Une exception s'est produite lors de la validation du paiement : " + e.toString();
            logger.error(message, e);
            try {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.setRollbackOnly();
                    transaction.rollback();
                }
            } catch (Exception e2) {
                logger.error("Validation du paiement : le rollback de la transaction à échoué : " + e2.toString(), e2);
            }
            throw new Exception(message, e);
        } finally {
            try {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                logger.error("Validation du paiement : la cloture de la transaction à échoué : " + e1.toString(), e1);
            }
        }
    }
}
