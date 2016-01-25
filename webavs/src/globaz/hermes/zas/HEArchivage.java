package globaz.hermes.zas;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HENbreARCDansLot;
import globaz.hermes.db.gestion.HEAnnoncesListViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author ald Programme permettant d'archiver les arcs
 */
public class HEArchivage extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    private static int FACTEUR_MULTIPLICATIF_TEMPS_ARC_EN_COURS = 2;
    private static int FACTEUR_MULTIPLICATIF_TEMPS_BASE = 1;

    public static void main(String[] args) {
        boolean profile = false;
        try {
            if (args.length < 2) {
                System.out.println(DateUtils.getTimeStamp()
                        + "java globaz.hermes.zas.HEArchivage <uid> <pwd> [date|lot] [idLot1 idLot2 idLotn]|[date]");
                System.exit(CODE_RETOUR_ERREUR);
            }
            // créer la session
            BSession session = new BSession("HERMES");
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES").newSession(args[0], args[1]);
            HEArchivage archivageProcess = new HEArchivage(args);
            archivageProcess.setSession(session);
            archivageProcess.setEMailAddress(session.getApplication().getProperty("adh.user.email"));
            archivageProcess.executeProcess();
            if (!archivageProcess.getMemoryLog().isOnErrorLevel()) {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process archivage executed successfully !");
                System.exit(CODE_RETOUR_OK);
            } else {
                // il y a eu une erreur dans l'exécution du processus, je
                // retourne le code de retour erreur
                System.out.println("Process archivage has error(s) !");
                System.exit(CODE_RETOUR_ERREUR);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(CODE_RETOUR_ERREUR);
        }
    }

    private BPreparedStatement archiverAttentesPrepared = null;
    private BPreparedStatement archiverCiPrepared = null;
    private BPreparedStatement archiverSerieTermineePrepared = null;
    private String[] args = null;
    private String dateToDoArchivage = "";
    private HashSet listeIdLots = null;
    private Vector lotTouche = new Vector();
    private HashSet memListe95 = new HashSet();
    private List motifsAArchiver;
    private String pwd = "";
    private BPreparedStatement supprimerAnnoncesPrepared = null;
    private BPreparedStatement supprimerAttentesPrepared = null;
    private BPreparedStatement supprimerCiPrepared = null;
    private BPreparedStatement supprimerOrdresPrepared = null;
    // private BSession session;
    private BTransaction transaction = null;

    private String userID = "";

    public HEArchivage(String[] args) throws Exception {
        super();
        //
        setUserID(args[0]);
        setPwd(args[1]);
        this.args = args;
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        if (args.length > 2) {
            if ("lot".equals(args[2])) {
                HashSet listeIdLots = new HashSet();
                for (int i = 3; i < args.length; i++) {
                    try {
                        listeIdLots.add(Integer.valueOf(args[i]));
                    } catch (Exception e) {
                        System.out.println(DateUtils.getTimeStamp()
                                + "Erreur : l'identifieur d'un lot donnée n'est pas un nombre !");
                    }
                }
                setListeIdLots(listeIdLots);
            } else {
                if ("date".equals(args[2]) && !StringUtils.isStringEmpty(args[3])) {
                    dateToDoArchivage = args[3];
                } else {
                    System.out
                            .println(DateUtils.getTimeStamp()
                                    + "java globaz.hermes.zas.HEArchivage <uid> <pwd> [date|lot] [idLot1 idLot2 idLotn]|[date]");
                    System.exit(CODE_RETOUR_ERREUR);
                }
            }
        }
        try {
            if (getSession().getApplication().getProperty("adh.log").equals("true")) {
                String logOut =
                // getSession().getApplication().getProperty("adh.home.dir")
                // + "/"
                // + getSession().getApplication().getProperty("adh.log.dir")
                // + "/"
                Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEArchivage/out/"
                        + getSession().getApplication().getProperty("adh.log.out") + DateUtils.getLocaleDateAndTime()
                        + ".log";
                String logErr =
                // getSession().getApplication().getProperty("adh.home.dir")
                // + "/"
                // + getSession().getApplication().getProperty("adh.log.dir")
                // + "/"
                Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEArchivage/err/"
                        + getSession().getApplication().getProperty("adh.log.err") + DateUtils.getLocaleDateAndTime()
                        + ".log";
                StringUtils.createDirectory(logOut);
                StringUtils.createDirectory(logErr);
                PrintStream streamOut = new PrintStream(new FileOutputStream(logOut));
                System.setOut(streamOut);
                PrintStream streamErr = new PrintStream(new FileOutputStream(logErr));
                System.setErr(streamErr);
            }
            loadMotifsAArchiver(getSession());
            // /////////////////////////////
            // Traitement archivage //////
            // /////////////////////////////
            archiver();
            // *****************************
            return true;
        } catch (Exception e) {
            System.err.println(DateUtils.getTimeStamp() + "ARCHIVAGE EN ERREUR");
            System.err.println(getTransaction().getErrors());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Batch HEArchivage");
            JadeLogger.info("Batch HEArchivage", e);
            _addError(getTransaction(), e.getMessage());
            return false;
        } catch (Throwable er) {
            System.err.println(DateUtils.getTimeStamp() + "ARCHIVAGE EN ERREUR");
            System.err.println(getTransaction().getErrors());
            getMemoryLog().logMessage(er.getMessage(), FWMessage.ERREUR, "Batch HEArchivage");
            JadeLogger.info("Batch HEArchivage", er);
            _addError(getTransaction(), er.getMessage());
            return false;
        }
    }

    /**
     * Method archiver.
     */
    private void archiver() throws Exception {
        HELotListViewBean listeLots = new HELotListViewBean();
        if (getListeIdLots() == null) {
            // aucun lot spécifié par l'utilisateur
            String dateYYYYMMJJ = "";
            if (!StringUtils.isStringEmpty(dateToDoArchivage)) {
                dateYYYYMMJJ = dateToDoArchivage;
            } else {
                // on considère tous les lots plus vieux que 1 fois le temps
                // limite
                dateYYYYMMJJ = CalculerDateLimite(FACTEUR_MULTIPLICATIF_TEMPS_BASE);
            }
            System.out.println(DateUtils.getTimeStamp() + "l'archivage considère tous les lots plus vieux que "
                    + dateYYYYMMJJ);
            // rechercher tous les lots vieux d'un mois de type envoi
            listeLots.setSession(getSession());
            listeLots.setUntilDateEnvoi(dateYYYYMMJJ);
            listeLots.setForType(HELotViewBean.CS_TYPE_ENVOI);
            listeLots.find(getTransaction(), BManager.SIZE_NOLIMIT);
            for (int i = 0; i < listeLots.size(); i++) {
                HELotViewBean lotCrt = (HELotViewBean) listeLots.getEntity(i);
                archiverLot(lotCrt);
                lotTouche.add(lotCrt.getIdLot());
                System.out.println(DateUtils.getTimeStamp() + "lot " + lotCrt.getIdLot() + " archivé !");
            }
            // supprimer également tous les ordres que l'on reçoit
            listeLots.setForType(HELotViewBean.CS_TYPE_RECEPTION);
            listeLots.find(getTransaction(), BManager.SIZE_NOLIMIT);
            supprimerOrdreReception(listeLots);
            // supprimer les annonce orphelines et les annonces en erreurs
            supprimerOrphelineErreurLot(dateYYYYMMJJ, null);
        } else {
            // traiter tous les lots spécifiés pas l'utilisateur
            for (Iterator idLotItr = listeIdLots.iterator(); idLotItr.hasNext();) {
                String idLotCrt = ((Integer) idLotItr.next()).toString();
                HELotViewBean lotCrt = new HELotViewBean();
                lotCrt.setSession(getSession());
                lotCrt.setIdLot(idLotCrt);
                lotCrt.retrieve(getTransaction());
                testTransaction();
                if (lotCrt.getType().equals(HELotViewBean.CS_TYPE_ENVOI)) {
                    archiverLot(lotCrt);
                    System.out.println(DateUtils.getTimeStamp() + "lot " + lotCrt.getIdLot() + " archivé !");
                } else {
                    System.err.println(DateUtils.getTimeStamp() + "le lot " + lotCrt.getIdLot()
                            + " n'est pas un lot de type envoi --> non archivé");
                }
                // supprimer les annonce orphelines et les annonces en erreurs
                supprimerOrphelineErreurLot(null, idLotCrt);
            }
        }
        controlerLot();
    }

    /**
     * @param lotCrt
     */
    private void archiverARCEnCours(HELotViewBean lotCrt) throws Exception {
        // est-ce que ce lot a été créé il y a deux fois le temps d'attente ?
        String dateRefYYYYMMJJ = CalculerDateLimite(FACTEUR_MULTIPLICATIF_TEMPS_ARC_EN_COURS);
        System.out.println(DateUtils.getTimeStamp() + "Date de référence pour l'archivage des annonces en cours :"
                + dateRefYYYYMMJJ);
        if (Integer.parseInt(lotCrt.getDateCentrale()) < Integer.parseInt(dateRefYYYYMMJJ)) {
            System.out.println(DateUtils.getTimeStamp() + "Annonces en cours du lot " + lotCrt.getIdLot() + "("
                    + lotCrt.getDateCentrale() + ")en cours d'archivage...");
            // lister tous les 1101 du lot courant de motif <> 95 qui sont
            // terminés
            HEAnnoncesListViewBean listeOnzes = new HEAnnoncesListViewBean();
            listeOnzes.setSession(getSession());
            listeOnzes.setForIdLot(lotCrt.getIdLot());
            listeOnzes.setLikeEnregistrement("1101");
            listeOnzes.setForStatut(IHEAnnoncesViewBean.CS_A_TRAITER);
            listeOnzes.find(getTransaction(), BManager.SIZE_NOLIMIT);
            System.out.println(DateUtils.getTimeStamp() + listeOnzes.size() + " annnonce(s) 1101 en cours trouvées...");
            long nbreAnnoncesArch = 0;
            long nbreAnnoncesSupp = 0;
            HEAnnoncesViewBean annonceCrt;
            String reference;
            String motif;
            for (int i = 0; i < listeOnzes.size(); i++) {
                // pour chaque annonce trouvée, archiver ses attentes et la
                // série associée au 1101
                annonceCrt = (HEAnnoncesViewBean) listeOnzes.getEntity(i);
                reference = annonceCrt.getRefUnique();
                motif = annonceCrt.getMotif();
                if (isMotifAArchiver(motif)) {
                    archiverAttentes(annonceCrt, reference, motif);
                    archiverSerieOnze(annonceCrt, reference, motif);
                    deleteSerieOnze(annonceCrt, reference, motif, annonceCrt.getStatut());
                    nbreAnnoncesArch++;
                    nbreAnnoncesSupp++;
                } else {
                    deleteSerieOnze(annonceCrt, reference, motif, annonceCrt.getStatut());
                    nbreAnnoncesSupp++;
                }
            }
            System.out.println(DateUtils.getTimeStamp() + nbreAnnoncesArch + " series archivées");
            System.out.println(DateUtils.getTimeStamp() + nbreAnnoncesSupp + " series supprimées");
        }
    }

    /**
     * Method archiverAttentes.
     * 
     * @param reference
     * @param motif
     */
    private void archiverAttentes(HEAnnoncesViewBean annonceCrt, String reference, String motif) throws Exception {
        if (archiverAttentesPrepared == null) {
            archiverAttentesPrepared = new BPreparedStatement(getTransaction());
            archiverAttentesPrepared.prepareStatement(annonceCrt.getSqlForCopyRetour());
        }
        archiverAttentesPrepared.setString(1, reference);
        archiverAttentesPrepared.setString(2, motif);
        archiverAttentesPrepared.execute();
        archiverAttentesPrepared.closeStatement();
    }

    /**
     * @param lotCrt
     */
    private void archiverCI(HELotViewBean lotCrt) throws Exception {
        HEAnnoncesListViewBean listeOrdres = new HEAnnoncesListViewBean();
        listeOrdres.setSession(getSession());
        listeOrdres.setForIdLot(lotCrt.getIdLot());
        listeOrdres.setLikeEnregistrement("39001");
        listeOrdres.setLikeEnregistrementOr("2301");
        listeOrdres.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        listeOrdres.find(getTransaction(), BManager.SIZE_NOLIMIT);
        System.out.println(DateUtils.getTimeStamp() + listeOrdres.size() + " ci(s) reçus terminés, trouvés...");
        HEAnnoncesViewBean crtVb;
        for (int i = 0; i < listeOrdres.size(); i++) {
            crtVb = (HEAnnoncesViewBean) listeOrdres.getEntity(i);
            if (crtVb.getIdAnnonce().equals(crtVb.getRefUnique())) {
                // si l'id de l'arc est identique à sa référence unique,
                // on est sûr d'avoir affaire à un ci isolés reçus
                archiverCiRecu(crtVb);
                supprimerCiRecu(crtVb);
            }
        }
        System.out.println(DateUtils.getTimeStamp() + "archivage et suppression des " + listeOrdres.size()
                + " ci(s) reçus terminés dans le lot : " + lotCrt.getIdLot() + "!");
    }

    /**
     * @param crtVb
     */
    private void archiverCiRecu(HEAnnoncesViewBean crtVb) throws Exception {
        // archiver la série
        if (archiverCiPrepared == null) {
            archiverCiPrepared = new BPreparedStatement(getTransaction());
            archiverCiPrepared.prepareStatement(crtVb.getSqlForCopyCi());
        }
        archiverCiPrepared.setString(1, crtVb.getRefUnique());
        archiverCiPrepared.setString(2, crtVb.getMotif());
        archiverCiPrepared.setInt(3, Integer.parseInt(crtVb.getIdLot()));
        archiverCiPrepared.execute();
        archiverCiPrepared.closeStatement();
    }

    /**
     * Method archiverLesOnzes.
     * 
     * @param lotCrt
     * @return long
     */
    private void archiverLesOnzes(HELotViewBean lotCrt) throws Exception {
        // lister tous les 1101 du lot courant de motif <> 95 qui sont terminés
        HEAnnoncesListViewBean listeOnzes = new HEAnnoncesListViewBean();
        listeOnzes.setSession(getSession());
        listeOnzes.setForIdLot(lotCrt.getIdLot());
        listeOnzes.setLikeEnregistrement("1101");
        // on archive aussi les 95
        // listeOnzes.setForNotMotif("95");
        listeOnzes.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        listeOnzes.find(getTransaction(), BManager.SIZE_NOLIMIT);
        System.out.println(DateUtils.getTimeStamp() + listeOnzes.size() + " annnonce(s) 1101 terminées trouvées...");
        long nbreAnnoncesArch = 0;
        long nbreAnnoncesSupp = 0;
        HEAnnoncesViewBean annonceCrt;
        String reference;
        String motif;
        for (int i = 0; i < listeOnzes.size(); i++) {
            // pour chaque annonce trouvée, archiver ses attentes et la série
            // associée au 1101
            annonceCrt = (HEAnnoncesViewBean) listeOnzes.getEntity(i);
            reference = annonceCrt.getRefUnique();
            motif = annonceCrt.getMotif();
            if (isMotifAArchiver(motif)) {
                if ("95".equals(motif)) {
                    System.out.println(DateUtils.getTimeStamp() + "un motif 95 terminé --> à archiver ?");
                    // archiver les nonantes cinq
                    if (isConjointTermine(annonceCrt)) {
                        // add to memListe95 to remove
                        System.out.println(DateUtils.getTimeStamp()
                                + "un motif 95 terminé --> oui, son conjoint est terminé ou introuvable");
                        if (!memListe95.contains(annonceCrt)) {
                            System.out.println("Ajout du 95 avec ref unique :" + annonceCrt.getRefUnique()
                                    + " et motif" + annonceCrt.getMotif());
                            memListe95.add(annonceCrt);
                            nbreAnnoncesArch++;
                            nbreAnnoncesSupp++;
                        }
                    } else {
                        System.out.println(DateUtils.getTimeStamp()
                                + "un motif 95 terminé --> non, son conjoint n'est pas terminé");
                    }
                } else {
                    {
                        // checkRefLot(lotCrt);
                        archiverAttentes(annonceCrt, reference, motif);
                        archiverSerieOnze(annonceCrt, reference, motif);
                        deleteSerieOnze(annonceCrt, reference, motif, annonceCrt.getStatut());
                        nbreAnnoncesArch++;
                        nbreAnnoncesSupp++;
                    }
                }
            } else {
                deleteSerieOnze(annonceCrt, reference, motif, annonceCrt.getStatut());
                nbreAnnoncesSupp++;
            }
        }
        // de plus archiver les annonces 95devant être archivées en parcourant
        // la boucle
        archiverNonanteCinqTerminees();
        System.out.println(DateUtils.getTimeStamp() + nbreAnnoncesArch + " series archivées");
        System.out.println(DateUtils.getTimeStamp() + nbreAnnoncesSupp + " series supprimées");
    }

    private void archiverLot(HELotViewBean lotCrt) throws Exception {
        // pour chaque lot trouvé, extraire les 3% pour les supprimer
        System.out.println(DateUtils.getTimeStamp() + "Suppression des annonces autres que 11 du lot "
                + lotCrt.getIdLot());
        supprimerLesTrentes(lotCrt);
        // pour chaque lot trouvé, archiver les 1101 suivant le filtre configuré
        // dans les properties
        // + tout ce qui est lié
        // c'est-à-dire les attentes (HEAREAP) et ses annonces en retour
        archiverLesOnzes(lotCrt);
        archiverARCEnCours(lotCrt);
    }

    /**
     * Method archiverNonanteCinq.
     * 
     * @param hashSet
     */
    private void archiverNonanteCinqTerminees() throws Exception {
        System.out.println(DateUtils.getTimeStamp() + "Archiver les " + memListe95.size() + " annonces 95 terminées");
        HEAnnoncesViewBean crtArc = null;
        // pour toutes les annonces 95 présentes dans le hashSet
        for (Iterator crtItrArc = memListe95.iterator(); crtItrArc.hasNext();) {
            crtArc = (HEAnnoncesViewBean) crtItrArc.next();
            archiverAttentes(crtArc, crtArc.getRefUnique(), crtArc.getMotif());
            archiverSerieOnze(crtArc, crtArc.getRefUnique(), crtArc.getMotif());
            deleteSerieOnze(crtArc, crtArc.getRefUnique(), crtArc.getMotif(), crtArc.getStatut());
        }
        // ******************************************************
        memListe95.clear();
    }

    /**
     * Method archiverSerieOnze.
     * 
     * @param reference
     * @param motif
     */
    private void archiverSerieOnze(HEAnnoncesViewBean annonceCrt, String reference, String motif) throws Exception {
        System.out.println("Archiver serie onze avec refunique :" + reference + " et motif " + motif);
        HEAnnoncesListViewBean lotsSerie = new HEAnnoncesListViewBean();
        lotsSerie.setSession(getSession());
        lotsSerie.setForMotif(motif);
        lotsSerie.setForRefUnique(reference);
        lotsSerie.setOrder("");
        lotsSerie.setGroupBy(" RMILOT ");
        lotsSerie.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; i < lotsSerie.size(); i++) {
            HEAnnoncesViewBean lotUtilise = (HEAnnoncesViewBean) lotsSerie.getEntity(i);
            if (!lotTouche.contains(lotUtilise.getIdLot())) {
                lotTouche.add(lotUtilise.getIdLot());
            }
        }
        if (archiverSerieTermineePrepared == null) {
            archiverSerieTermineePrepared = new BPreparedStatement(getTransaction());
            archiverSerieTermineePrepared.prepareStatement(annonceCrt.getSqlForCopyAnnonce());
        }
        archiverSerieTermineePrepared.setString(1, reference);
        archiverSerieTermineePrepared.setString(2, motif);
        archiverSerieTermineePrepared.execute();
        archiverSerieTermineePrepared.closeStatement();
    }

    private String CalculerDateLimite(int facteurMult) throws Exception {
        String dateYYYYMMJJ;
        // calculer la date jusqu'à laquelle on veut archiver
        // date = date courante - joursConserv jours
        Calendar cal = Calendar.getInstance();
        int joursConserv = facteurMult * Integer.parseInt(getSession().getApplication().getProperty("adh.days"));
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - joursConserv);
        dateYYYYMMJJ = DateUtils.getDateFromCalendar(cal);
        return dateYYYYMMJJ;
    }

    /**
     * Method checkRefLot.
     * 
     * @param annonceCrt
     */
    private HELotViewBean checkRefLot(HELotViewBean l) throws Exception {
        HELotViewBean lot = new HELotViewBean();
        lot.setSession(getSession());
        lot.setArchivage(true);
        lot.setIdLot(l.getIdLot());
        lot.retrieve(getTransaction());
        testTransaction();
        if (lot.isNew()) {
            lot.setArchivage(false);
            lot.retrieve(getTransaction());
            testTransaction();
            if (!lot.isNew()) {
                lot.setArchivage(true);
                lot.add(getTransaction());
                testTransaction();
            } else {
                throw new Exception("Hermes archivage : impossible de trouver le lot : " + lot.getIdLot()
                        + " dans les données en cours");
            }
        }
        return lot;
    }

    private String chercherNumAVSConjoint(HEAnnoncesViewBean annonceCrt) throws Exception {
        HEOutputAnnonceListViewBean leOnzeZeroCinq = new HEOutputAnnonceListViewBean();
        leOnzeZeroCinq.setSession(getSession());
        leOnzeZeroCinq.setLikeEnregistrement("1105");
        leOnzeZeroCinq.setForIdLot(annonceCrt.getIdLot());
        leOnzeZeroCinq.setForStatut(annonceCrt.getStatut());
        leOnzeZeroCinq.setForRefUnique(annonceCrt.getRefUnique());
        leOnzeZeroCinq.setForMotif(annonceCrt.getMotif());
        leOnzeZeroCinq.setForNumAVS(annonceCrt.getNumeroAVS());
        leOnzeZeroCinq.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if (leOnzeZeroCinq.size() == 0 || leOnzeZeroCinq.size() > 1) {
            System.err.println("Impossible d'archiver l'arc 95 n° " + annonceCrt.getIdAnnonce());
            System.err
                    .println("¦----> le nombre d'arc 1105 associé n'est pas égal à 1 mais à " + leOnzeZeroCinq.size());
            return "";
        } // on a trouvé qu'une seule annonce 1105, ce qui est correcte
        return ((HEOutputAnnonceViewBean) leOnzeZeroCinq.getFirstEntity())
                .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE);
    }

    /**
     * Method controlerLot.
     * 
     * @param lotCrt
     */
    private void controlerLot() throws Exception {
        System.out.println(DateUtils.getTimeStamp() + "Contrôle l'integrite des lots...");
        for (int i = 0; i < lotTouche.size(); i++) {
            // vérifier qu'il existe dans l'archivage
            // si non l'ajouter
            HELotViewBean lotCrt = new HELotViewBean();
            lotCrt.setIdLot(lotTouche.get(i).toString());
            checkRefLot(lotCrt);
            // obtenir les nombre d'arc faisant référence à ce lot
            controleSiVideSupp(lotTouche.get(i).toString());
        }
        System.out.println(DateUtils.getTimeStamp() + "Archivage ok");
    }

    private void controleSiVideSupp(String idLot) throws Exception {
        HENbreARCDansLot nbRef = new HENbreARCDansLot();
        nbRef.setNumeroLot(idLot);
        nbRef.retrieve(getTransaction());
        testTransaction();
        if (nbRef.getNombreARC() == 0) { // il n'y a plus aucune arc faisant
            // référence à ce lot -> delete
            HELotViewBean lot = new HELotViewBean();
            lot.setSession(getSession());
            lot.setArchivage(false);
            lot.setIdLot(idLot);
            lot.retrieve(getTransaction());
            testTransaction();
            if (!lot.isNew()) {
                lot.delete(getTransaction());
                testTransaction();
            }
        }
    }

    /**
     * Method creerLot.
     * 
     * @param lotCrt
     */
    private void creerLot(HELotViewBean lotCrt) throws Exception {
        lotCrt.setArchivage(true);
        lotCrt.retrieve(getTransaction());
        testTransaction();
        if (lotCrt.isNew()) {
            lotCrt.add(getTransaction());
            testTransaction();
        }
    }

    /**
     * Method deleteSerieOnze.
     * 
     * @param reference
     * @param motif
     */
    private void deleteSerieOnze(HEAnnoncesViewBean annonceCrt, String reference, String motif, String statut)
            throws Exception {
        // supprimer les attentes retours
        if (supprimerAttentesPrepared == null) {
            supprimerAttentesPrepared = new BPreparedStatement(getTransaction());
            supprimerAttentesPrepared.prepareStatement(annonceCrt.getSqlForDeleteAttentesSerie());
        }
        supprimerAttentesPrepared.setString(1, reference);
        supprimerAttentesPrepared.setString(2, motif);
        supprimerAttentesPrepared.execute();
        supprimerAttentesPrepared.closeStatement();
        // supprimer ensuite les annonces elles-mêmes
        if (supprimerAnnoncesPrepared == null) {
            supprimerAnnoncesPrepared = new BPreparedStatement(getTransaction());
            supprimerAnnoncesPrepared.prepareStatement(annonceCrt.getSqlForDeleteAnnoncesSerie());
        }
        supprimerAnnoncesPrepared.setString(1, reference);
        supprimerAnnoncesPrepared.setString(2, motif);
        supprimerAnnoncesPrepared.setInt(3, Integer.parseInt(statut));
        supprimerAnnoncesPrepared.execute();
        supprimerAnnoncesPrepared.closeStatement();
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("HERMES_10020");// "L'archivage est terminé";
    }

    /**
     * Returns the listeIdLots.
     * 
     * @return HashSet
     */
    public HashSet getListeIdLots() {
        return listeIdLots;
    }

    /**
     * Returns the pwd.
     * 
     * @return String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Returns the userID.
     * 
     * @return String
     */
    public String getUserID() {
        return userID;
    }

    private boolean isConjointTermine(HEAnnoncesViewBean annonceCrt) throws Exception {
        // chercher les 1105 associés au 1101 de l'annonce courante
        String numAVSConjoint = chercherNumAVSConjoint(annonceCrt);
        // si on ne trouve pas le numAVS du conjoint
        if (StringUtils.isStringEmpty(numAVSConjoint)) {
            return true;
        }
        // voir si tous les 95 du conjoint sont terminés avec comme numéro AVS
        // celui du conjoint
        HEAnnoncesListViewBean liste95 = new HEAnnoncesListViewBean();
        liste95.setSession(getSession());
        liste95.setLikeEnregistrement("1101");
        liste95.setForNumAVS(numAVSConjoint);
        liste95.setForMotif("95");
        liste95.wantCallMethodAfter(false);
        liste95.wantCallMethodAfterFind(false);
        liste95.wantCallMethodBefore(false);
        liste95.wantCallMethodBeforeFind(false);
        liste95.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if (liste95.size() == 0) {
            // add to memListe95 to remove
            // il n'y a pas d'arc 95 pour le conjoint
            // donc on considère que le conjoint est terminé
            return true;
        } else {
            // Contrôler que le 95 trouvé correspond bien
            HEAnnoncesViewBean NonanteCinqCourant = null;
            for (int i = 0; i < liste95.size(); i++) {
                NonanteCinqCourant = (HEAnnoncesViewBean) liste95.getEntity(i);
                if (chercherNumAVSConjoint(NonanteCinqCourant).equals(annonceCrt.getNumeroAVS())) {
                    if (IHEAnnoncesViewBean.CS_A_TRAITER.equals(NonanteCinqCourant.getStatut())) {
                        // on en a trouvé au moins un qui n'est pas terminé -->
                        // donc on ne supprime pas
                        return false;
                    }
                }
            }
            // on a trouvé aucun 95 du conjoint qui n'est pas terminé --> donc
            // on peut archiver le 95
            return true;
        }
    }

    private boolean isMotifAArchiver(String motif) {
        return motifsAArchiver.contains(motif);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void loadMotifsAArchiver(BSession session) throws Exception {
        String motifs = ((HEApplication) session.getApplication()).getProperty("adh.archivage.motifs");
        if (motifs == null || JAUtil.isStringEmpty(motifs)) {
            throw new Exception(DateUtils.getTimeStamp()
                    + "Erreur : la propriété adh.archivage.motifs est obligatoire pour l'archivage Hermes !");
        }
        StringTokenizer st = new StringTokenizer(motifs, ",");
        String listM[] = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            listM[st.countTokens() - 1] = st.nextToken();
        }
        motifsAArchiver = Arrays.asList(listM);
    }

    /**
     * Sets the listeIdLots.
     * 
     * @param listeIdLots
     *            The listeIdLots to set
     */
    public void setListeIdLots(HashSet listeIdLots) {
        this.listeIdLots = listeIdLots;
    }

    /**
     * Sets the pwd.
     * 
     * @param pwd
     *            The pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    /**
     * Returns the transaction.
     * 
     * @return BTransaction
     */
    /**
     * Sets the transaction.
     * 
     * @param transaction
     *            The transaction to set
     */
    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Sets the userID.
     * 
     * @param userID
     *            The userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @param crtVb
     */
    private void supprimerCiRecu(HEAnnoncesViewBean crtVb) throws Exception {
        // archiver la série
        if (supprimerCiPrepared == null) {
            supprimerCiPrepared = new BPreparedStatement(getTransaction());
            supprimerCiPrepared.prepareStatement(crtVb.getSqlForDeleteCi());
        }
        supprimerCiPrepared.setString(1, crtVb.getRefUnique());
        supprimerCiPrepared.setString(2, crtVb.getMotif());
        supprimerCiPrepared.setInt(3, Integer.parseInt(crtVb.getStatut()));
        supprimerCiPrepared.setInt(4, Integer.parseInt(crtVb.getIdLot()));
        supprimerCiPrepared.execute();
        supprimerCiPrepared.closeStatement();
    }

    /**
     * Method archiverLesTrentes.
     * 
     * @param lotCrt
     * @return long
     */
    private void supprimerLesTrentes(HELotViewBean lotCrt) throws Exception {
        // vérifier que le lot soit bien en envoi
        if (!HELotViewBean.CS_TYPE_ENVOI.equals(lotCrt.getType())) {
            throw new Exception(
                    "Erreur fatale: impossible de supprimer les annonces qui ne sont des 11 dans le lot numero "
                            + lotCrt.getIdLot() + " qui n'est pas de type envoi !!");
        }
        // suppression des arcs envoi dont on a plus besoin
        BPreparedStatement gardeSltOnze = new BPreparedStatement(getTransaction());
        gardeSltOnze.prepareStatement(lotCrt.getSqlForDeletePasArc());
        gardeSltOnze.setInt(1, Integer.parseInt(lotCrt.getIdLot()));
        gardeSltOnze.execute();
        gardeSltOnze.closeStatement();
    }

    /**
     * @param listeLots
     *            la liste des lots à traiter
     */
    private void supprimerOrdreReception(HELotListViewBean listeLots) throws Exception {
        System.out.println(DateUtils.getTimeStamp() + listeLots.size() + " lot(s) en réception plus vieux que "
                + listeLots.getUntilDateEnvoi());
        for (int i = 0; i < listeLots.size(); i++) {
            HELotViewBean lotCrt = (HELotViewBean) listeLots.getEntity(i);
            supprimerOrdres(lotCrt);
            archiverCI(lotCrt);
            lotTouche.add(lotCrt.getIdLot());
            System.out.println(DateUtils.getTimeStamp() + "lot " + lotCrt.getIdLot() + "vidé des ordres reçus !");
        }
    }

    /**
     * @param lotCrt
     */
    private void supprimerOrdres(HELotViewBean lotCrt) throws Exception {
        HEAnnoncesListViewBean listeOrdres = new HEAnnoncesListViewBean();
        listeOrdres.setSession(getSession());
        listeOrdres.setForIdLot(lotCrt.getIdLot());
        listeOrdres.setLikeEnregistrement("2201");
        listeOrdres.setLikeEnregistrementOr("2901");
        listeOrdres.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        listeOrdres.find(getTransaction(), BManager.SIZE_NOLIMIT);
        System.out.println(DateUtils.getTimeStamp() + listeOrdres.size() + " ordre(s) reçus terminés, trouvés...");
        HEAnnoncesViewBean crtVb;
        for (int i = 0; i < listeOrdres.size(); i++) {
            crtVb = (HEAnnoncesViewBean) listeOrdres.getEntity(i);
            if (crtVb.getIdAnnonce().equals(crtVb.getRefUnique())) {
                // si l'id de l'arc est identique à sa référence unique,
                // on est sûr d'avoir affaire à un ordre d'une autre caisse
                supprimerOrdresRecu(crtVb);
            }
        }
        System.out.println(DateUtils.getTimeStamp() + "supression des " + listeOrdres.size()
                + " ordre(s) reçus terminés !");
    }

    /**
     * @param crtVb
     */
    private void supprimerOrdresRecu(HEAnnoncesViewBean crtVb) throws Exception {
        // supprimer ensuite les annonces elles-mêmes
        if (supprimerOrdresPrepared == null) {
            supprimerOrdresPrepared = new BPreparedStatement(getTransaction());
            supprimerOrdresPrepared.prepareStatement(crtVb.getSqlForDeleteOrdresSerie());
        }
        supprimerOrdresPrepared.setString(1, crtVb.getRefUnique());
        supprimerOrdresPrepared.setString(2, crtVb.getMotif());
        supprimerOrdresPrepared.setInt(3, Integer.parseInt(crtVb.getStatut()));
        supprimerOrdresPrepared.setInt(4, Integer.parseInt(crtVb.getIdLot()));
        supprimerOrdresPrepared.execute();
        supprimerOrdresPrepared.closeStatement();
    }

    /**
     * Method supprimerArcEnErreur.
     * 
     * @param lotCrt
     */
    private void supprimerOrphelineErreurLot(String date, String idLot) throws Exception {
        Vector lotTouche = new Vector();
        // lister tous les 1101 et les 2501 qui sont ne pas dans le statut a
        // traiter, en attente ou termine
        HEAnnoncesListViewBean listeOnzes = new HEAnnoncesListViewBean();
        listeOnzes.setSession(getSession());
        if (!StringUtils.isStringEmpty(date)) {
            System.out.println(DateUtils.getTimeStamp()
                    + "Suppression de toutes les annonces orphelines ou en erreur plus vieilles que " + date);
            listeOnzes.setUntilDate(DateUtils.getDateWithDayIndex(date, DateUtils.AAAAMMJJ, 1));
        }
        if (!StringUtils.isStringEmpty(idLot)) {
            System.out.println(DateUtils.getTimeStamp()
                    + "Suppression de toutes les annonces orphelines ou en erreur dans le lot " + idLot);
            listeOnzes.setForIdLot(idLot);
        }
        listeOnzes.setForNotStatut(IHEAnnoncesViewBean.CS_A_TRAITER);
        listeOnzes.setForNotStatut2(IHEAnnoncesViewBean.CS_TERMINE);
        listeOnzes.setForNotStatut3(IHEAnnoncesViewBean.CS_EN_ATTENTE);
        listeOnzes.find(getTransaction(), BManager.SIZE_NOLIMIT);
        long nbreAnnonces = 0;
        HEAnnoncesViewBean annonceCrt;
        String reference;
        String motif;
        System.out.println(DateUtils.getTimeStamp() + listeOnzes.size()
                + " annonces dans un statut autre que en cours, a traiter ou termine à supprimer");
        for (int i = 0; i < listeOnzes.size(); i++) {
            // pour chaque annonce trouvée, archiver ses attentes et la série
            // associée au 1101
            annonceCrt = (HEAnnoncesViewBean) listeOnzes.getEntity(i);
            // on supprime la serie seuelement pour les 01
            if (annonceCrt.getChampEnregistrement().substring(2).startsWith("01")
                    || annonceCrt.getChampEnregistrement().substring(2).startsWith("001")) {
                reference = annonceCrt.getRefUnique();
                motif = annonceCrt.getMotif();
                deleteSerieOnze(annonceCrt, reference, motif, annonceCrt.getStatut());
                if (!lotTouche.contains(annonceCrt.getIdLot())) {
                    lotTouche.add(annonceCrt.getIdLot());
                }
            }
        }
        // // vérifier que les lots ne soit pas vide sinon on supprime
        // for (int i = 0; i < lotTouche.size(); i++) {
        // controleSiVideSupp(lotTouche.get(i).toString());
        // }
    }

    private void testTransaction() throws Exception {
        if (getTransaction().hasErrors()) {
            System.err.println(DateUtils.getTimeStamp() + "ARCHIVAGE EN ERREUR");
            System.err.println(getTransaction().getErrors());
            getTransaction().rollback();
            System.exit(CODE_RETOUR_ERREUR);
        }
    }
}
