package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RECountRentesManager;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.module.compta.AREModuleComptable;
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
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASecteurTypeSection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.process.PRAbstractProcess;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author SCR
 */
public abstract class AREPmtMensuel extends PRAbstractProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final class Key implements Comparable<Key> {

        public String idCompteAnnexe = "";
        public String refPmt = "";

        public Key(Key k) {
            idCompteAnnexe = k.idCompteAnnexe;
            refPmt = k.refPmt;
        }

        public Key(String idCompteAnnexe, String refPmt) {
            this.idCompteAnnexe = idCompteAnnexe;
            this.refPmt = refPmt;
        }

        @Override
        public int compareTo(Key key) {
            if (idCompteAnnexe.compareTo(key.idCompteAnnexe) != 0) {
                return idCompteAnnexe.compareTo(key.idCompteAnnexe);
            } else if (refPmt.compareTo(key.refPmt) != 0) {
                return refPmt.compareTo(key.refPmt);
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }

            Key key = (Key) obj;

            return ((key.idCompteAnnexe.equals(idCompteAnnexe)) && (key.refPmt.equals(refPmt)));
        }

        @Override
        public int hashCode() {
            return (idCompteAnnexe + refPmt).hashCode();
        }
    }

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
    protected String emailObject = "";
    private String idOrganeExecution = "";
    private String moisPaiement = "";
    private String numeroOG = "";
    private BISession sessionOsiris = null;

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
    protected void checkAPIRubrique(APIRubrique rubriqueComptable, REPaiementRentes prestationsAccordee)
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
            throw new Exception("Cannot reset transaction. Transaction is null");
        }
        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        } else {
            transaction.commit();
            transaction.closeTransaction();
            transaction.openTransaction();
        }
        return transaction;
    }

    protected void doMiseEnErreurRA(BSession session, String idRA, String errorMsg) throws Exception {
        // On flag la RenteAccordee en erreur...
        BITransaction transaction = session.newTransaction();
        try {

            transaction.openTransaction();

            getMemoryLog().logMessage("Mise en erreur de la PA no " + idRA, FWMessage.AVERTISSEMENT,
                    this.getClass().toString());
            getMemoryLog().logMessage("\t" + errorMsg, FWMessage.AVERTISSEMENT, this.getClass().toString());

            // bz-6124
            REPrestationsAccordees pa = new REPrestationsAccordees();
            pa.setSession(session);
            pa.setIdPrestationAccordee(idRA);
            pa.retrieve(transaction);
            PRAssert.notIsNew(pa, "Traitement des erreurs, PA " + idRA + " non trouvée !!!");
            pa.setIsErreur(Boolean.TRUE);
            pa.update(transaction);

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(FWViewBeanInterface.ERROR, transaction.getErrors().toString(),
                        this.getClass().toString());
                throw new Exception();
            }
            transaction.commit();

        } catch (Exception e) {
            getMemoryLog().logMessage(FWViewBeanInterface.ERROR, e.toString(), this.getClass().toString());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
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

    protected Key getKey(REPaiementRentes rente) throws Exception {
        Key key = null;
        if (JadeStringUtil.isBlankOrZero(rente.getIdCompteAnnexe())) {
            throw new Exception("Incohérance dans les données. Pas de compte annexe référence pour la RA # "
                    + rente.getIdRenteAccordee());
        }
        key = new Key(rente.getIdCompteAnnexe(), rente.getIdTiersAdressePmt() + "-" + rente.getReferencePmt());
        return key;
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
            String genrePrestation) {

        try {
            String motifVersement = nss;
            motifVersement += " " + datePmt;
            motifVersement += " " + nom + " " + prenom;
            motifVersement += " " + AREModuleComptable.getLibelleRubrique(getSession(), genrePrestation);
            motifVersement += " " + refPmt;

            if ((motifVersement != null) && (motifVersement.length() > 140)) {
                String mot1 = nss;
                mot1 += " " + datePmt;

                String mot2 = " " + nom + " " + prenom;
                if ((mot2 != null) && (mot2.length() > 35)) {
                    mot2 = mot2.substring(0, 34);
                }
                String mot3 = " " + AREModuleComptable.getLibelleRubrique(getSession(), genrePrestation);

                String mot4 = " " + refPmt;
                if ((mot4 != null) && (mot4.length() > 35)) {
                    mot4 = mot4.substring(0, 34);
                }
                return mot1 + mot2 + mot3 + mot4;
            } else {
                return motifVersement;
            }
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

            if ((mgr != null) && !mgr.isEmpty()) {
                FWParameters p = (FWParameters) mgr.getFirstEntity();
                return p.getValeurAlpha();
            } else {
                return AREPmtMensuel.NO_SECTEUR_RENTE;
            }
        } catch (Exception e) {
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
    synchronized protected PlageIncrement reserverPlageIncrementsOperationsCA(BSession session, long nombreRentes)
            throws Exception {
        boolean hasError = false;
        int retryNumber = 0;

        PlageIncrement plageIncr = new PlageIncrement();

        BTransaction transaction2 = (BTransaction) getSession().newTransaction();
        try {
            transaction2.openTransaction();
            do {
                hasError = false;

                // MAJ de l'increment de la CA
                FWIncrementation increment = new FWIncrementation();
                increment.setSession(session);
                increment.setIdIncrement(CAOperation.TABLE_CAOPERP);
                increment.setIdCodeSysteme("");
                increment.setAnneeIncrement("");
                increment.retrieve(transaction2);
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
                increment.setValeurIncrement(new Long(plageIncr.max).toString());

                try {
                    // la reservation de l'increment de fait pas partie de la
                    // transaction
                    increment.update(transaction2);
                    transaction2.commit();
                    if (transaction2.hasErrors()) {
                        transaction2.rollback();
                        transaction2.clearErrorBuffer();
                        throw new Exception("Error while updating increment for table : " + CAOperation.TABLE_CAOPERP);
                    }
                } catch (Exception e) {
                    hasError = true;
                    retryNumber++;

                    Thread.sleep(2000);
                } finally {
                    transaction2.closeTransaction();
                }
            } while (hasError && (retryNumber <= 3));
        } finally {
            if (transaction2 != null) {
                transaction2.closeTransaction();
            }
        }
        if (hasError) {
            return null;
        } else {
            return plageIncr;
        }
    }

    synchronized protected PlageIncrement reserverPlageIncrementsSectionsCA(BSession session, long nombreRentes)
            throws Exception {
        boolean hasError = false;
        int retryNumber = 0;

        PlageIncrement plageIncr = new PlageIncrement();

        BTransaction transaction2 = (BTransaction) getSession().newTransaction();
        try {
            transaction2.openTransaction();
            do {
                hasError = false;

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

                // mise a jours de l'increment
                increment.setValeurIncrement(new Long(plageIncr.max).toString());

                try {
                    // la reservation de l'increment de fait pas partie de la
                    // transaction
                    increment.update(transaction2);
                    transaction2.commit();
                    if (transaction2.hasErrors()) {
                        transaction2.rollback();
                        transaction2.clearErrorBuffer();
                        throw new Exception("Error while updating increment for table : " + CAOperation.TABLE_CAOPERP);
                    }
                } catch (Exception e) {
                    hasError = true;
                    retryNumber++;

                    Thread.sleep(2000);
                } finally {
                    transaction2.closeTransaction();
                }
            } while (hasError && (retryNumber <= 3));
        } finally {
            if (transaction2 != null) {
                transaction2.closeTransaction();
            }
        }
        if (hasError) {
            return null;
        } else {
            return plageIncr;
        }
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

    protected void validationPmt(BSession session, APIGestionRentesExterne comptaFast,
            APIGestionComptabiliteExterne comptaExt, Map<Integer, RECumulPrstParRubrique> cumulParGenreRente)
            throws Exception {
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
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "");
            try {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.rollback();
                }
            } catch (Exception e2) {
                getMemoryLog().logMessage(e2.toString(), FWMessage.ERREUR, "");
            }

            // Une erreur est survenue, annulation de toutes les écritures des
            // journaux !!!
            // TODO
            // comptaFast.rollback();
            // comptaExt.getJournal().annuler();
            throw new Exception("Contrôle d'intégrité en erreur. " + e.toString());
        } finally {
            try {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
