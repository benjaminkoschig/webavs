package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;

public class RETraitementExportAnnonces {
    private final FWMemoryLog _log;
    private final BSession _session;
    private final BITransaction _transaction;
    private boolean isCIADD;
    private REArrayListPourAnnonce liste;

    public RETraitementExportAnnonces(BITransaction transaction, BSession session, FWMemoryLog log) {
        _transaction = transaction;
        _log = log;
        _session = session;
    }

    public List<REArrayListPourAnnonce> execute(IHEOutputAnnonce[] annonces) throws REDownloaderException, Exception {
        isCIADD = false;
        liste = new REArrayListPourAnnonce();

        List<REArrayListPourAnnonce> output = new ArrayList<REArrayListPourAnnonce>();

        // On parcourt les annonces de Hermes dans le sens inverse pour
        // avoir les 39 avant les 38
        // le déplacement du curseur (i) se fait dans les blocs de
        // traitement
        for (int i = annonces.length - 1; i >= 0; i--) {

            IHEOutputAnnonce annonce = annonces[i];

            // 1ère étape, on trie toutes les annonces pour les regroupées

            // Définition du code application
            String codeApplication = JadeStringUtil.substring(annonce.getChampEnregistrement(), 0, 2);

            // En premier, toujours une annonce 39
            if (codeApplication.equals("39")) {

                traiteAnnonce39(annonce, output);

                /*
                 * Pour la suite, il peut y avoir une annonce 38, mais pas dans tous les cas !
                 * 
                 * --> Si CI-ADD : Il y a forcément une annonce 38 --> Sinon, s'il n'y en pas, on passe directement à
                 * l'annonce 11
                 */
            } else if (codeApplication.equals("38")) {

                traiteAnnonce38(i, annonce, output);

                /*
                 * si ce n'est pas un CI-ADD, il y a peut y avoir des annonces 25 on les traites pour avoir l'historique
                 * des no AVS et NSS de l'assuré
                 * 
                 * 
                 * si c'etait un CI-ADD et que l'on arrive dans une annonce 25, c'est que l'on commence le traitement
                 * d'un nouveau rassemblement CI
                 * 
                 * 
                 * On ne garde pas les 25, elles ne nous intéressent plus une fois l'historique fait
                 */
            } else if (codeApplication.equals("25")) {

                traiteAnnonce25(annonce, output);

                /*
                 * revocation du RCI ou de l'ordre de splitting
                 * 
                 * si c'etait un CI-ADD et que l'on arrive dans une annonce 29, c'est que l'on a fini le traitement de
                 * ce CI-ADD(ici on admet qu'un 29 arrive toujours tout seul!)
                 * 
                 * on traite les motifs 99 et on ne fait rien pour les 96 (splitting)
                 * 
                 * On ne garde pas les 29, elles ne nous intéressent plus une fois la révocation faite
                 */
            } else if (codeApplication.equals("29")) {

                traiteAnnonce29(annonce, output);

                /*
                 * Pour la suite, il doit y avoir une annonce 11 si ce n'est pas un CI-ADD, donc il faut mettre ce 11
                 * dans la map et changer la clé pour changer de groupe.
                 */
            } else if (codeApplication.equals("11")) {

                traiteAnnonce11(annonce, output);

            } else {
                throw new REDownloaderException("Code application incorrect! code application=" + codeApplication);
            }
        }

        return output;
    }

    private void logMessage(String text) {
        _log.logMessage(text, FWMessage.INFORMATION, this.getClass().getName());
    }

    private void traiteAnnonce11(IHEOutputAnnonce annonce, List<REArrayListPourAnnonce> output) {
        /*
         * Pour les annonces 11, on ne traite que le code enregistrement 01 et 04 qui contient la date de clôture
         */
        String codeEnregistrement = JadeStringUtil.substring(annonce.getChampEnregistrement(), 2, 2);

        if (codeEnregistrement.equals("01")) {

            liste.add(new REAnnoncesHermesMap(annonce, "11001"));

            output.add(liste);

            liste = new REArrayListPourAnnonce();

        } else if (codeEnregistrement.equals("04")) {

            liste.add(new REAnnoncesHermesMap(annonce, "11004"));
            liste.setHasAnnonce1104(true);
        }
    }

    private void traiteAnnonce25(IHEOutputAnnonce annonce, List<REArrayListPourAnnonce> output) {
        // si c'etait un CI-ADD, changer la clé pour ce ci qui
        // est un Nouveau CI
        if (isCIADD) {
            output.add(liste);

            liste = new REArrayListPourAnnonce();
            isCIADD = false;
        }

        String codeEnregistrement = JadeStringUtil.substring(annonce.getChampEnregistrement(), 2, 2);

        // les no d'assuré ne sont pas au même endroit dans le 01 et
        // les 02-99
        if (codeEnregistrement.equals("01")) {

            String nssAssure = JadeStringUtil.substring(annonce.getChampEnregistrement(), 10, 11);
            if (nssAssure.startsWith("-")) {
                nssAssure = JadeStringUtil.change(nssAssure, "-", "756");
            }
            liste.setNssForRassemblement(nssAssure);
        } else {

            String nssAssure = JadeStringUtil.substring(annonce.getChampEnregistrement(), 4, 11);
            if (nssAssure.startsWith("-")) {
                nssAssure = JadeStringUtil.change(nssAssure, "-", "756");
            }
            liste.getHistNoAvsAssure().add(nssAssure);
        }
    }

    private void traiteAnnonce29(IHEOutputAnnonce annonce, List<REArrayListPourAnnonce> output) throws Exception {
        // si c'etait un CI-ADD, changer la clé pour ce ci qui
        // est un Nouveau CI
        if (isCIADD) {
            output.add(liste);

            liste = new REArrayListPourAnnonce();
            isCIADD = false;
        }

        String motif = JadeStringUtil.substring(annonce.getChampEnregistrement(), 72, 2);

        if ("99".equals(motif)) {

            // on cherche l'assure dans Pyxis
            String nssAssureAyantDroit = JadeStringUtil.substring(annonce.getChampEnregistrement(), 10, 11);

            if (nssAssureAyantDroit.startsWith("-")) {
                nssAssureAyantDroit = JadeStringUtil.change(nssAssureAyantDroit, "-", "756");
            } else {
                // on cherche dans la table de concordance pour
                // trouver le NSS
                String nssConcordance = NSUtil.returnNNSS(_session, nssAssureAyantDroit);
                if (nssConcordance != null) {
                    nssAssureAyantDroit = nssConcordance;
                }
            }

            System.out.println("Révocation du RCI pour: " + nssAssureAyantDroit);

            String information = FWMessageFormat.format(_session.getLabel("INFO_REVOCATION_RCI_POUR"),
                    NSUtil.formatAVSUnknown(nssAssureAyantDroit));

            logMessage(information);

            // premiere recherche dans les NSS/N AVS courant
            PRTiersWrapper assureAyantDroit = REDownloaderInscriptionsCI.getTiersFromNss(_session, _transaction,
                    nssAssureAyantDroit);

            // le tiers existe dans Pyxis
            if (assureAyantDroit != null) {

                // Recherche du dernier RCI non cloture de l'assure, pour un motif entre 71 et 91
                RERassemblementCIManager rciMgr = new RERassemblementCIManager();
                rciMgr.setSession(_session);
                rciMgr.setForIdTiers(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                rciMgr.setForMotifBetween(" 71 AND 91 ");
                // pas un RCI daja revoque
                rciMgr.setForCsEtatDiffentDe(IRERassemblementCI.CS_ETAT_REVOQUE);
                // pas un rci enfant
                rciMgr.setForRassWithoutParent(Boolean.TRUE);
                rciMgr.setOrderBy(RERassemblementCI.FIELDNAME_ID_RCI + " DESC");
                rciMgr.find(_transaction, 1);

                if (rciMgr.getSize() > 0) {
                    // le dernier rassemblement CI
                    RERassemblementCI rci = (RERassemblementCI) rciMgr.getFirstEntity();
                    rci.retrieve(_transaction);

                    rci.setCsEtat(IRERassemblementCI.CS_ETAT_REVOQUE);
                    rci.setDateRevocation(JACalendar.todayJJsMMsAAAA());
                    // la date de cloture est vide, on la met a jour avec la date de cloture donnee dans le
                    // 29
                    if (JadeStringUtil.isBlankOrZero(rci.getDateCloture())) {
                        try {
                            String dateCloture29 = JadeStringUtil.substring(annonce.getChampEnregistrement(), 74, 4);
                            dateCloture29 = PRDateFormater.convertDate_MMAA_to_MMxAAAA(dateCloture29);
                            rci.setDateCloture(dateCloture29);
                        } catch (Exception e) {
                            // la date de cloture ne peut pas etre recuperee
                        }
                    }

                    rci.update(_transaction);
                } else {
                    String information2 = FWMessageFormat.format(_session.getLabel("INFO_REVOCATION_PAS_DE_RCI"),
                            NSUtil.formatAVSUnknown(nssAssureAyantDroit));

                    logMessage(information2);
                }

            } else {
                String information3 = FWMessageFormat.format(
                        _session.getLabel("INFO_NSS_NAVS_PAS_DANS_TIERS_POUR_REVOCATION_RCI"),
                        NSUtil.formatAVSUnknown(nssAssureAyantDroit));

                logMessage(information3);
            }
        }
    }

    private void traiteAnnonce38(int i, IHEOutputAnnonce annonce, List<REArrayListPourAnnonce> output)
            throws REDownloaderException {
        String code = JadeStringUtil.substring(annonce.getChampEnregistrement(), 71, 1);

        /*
         * Il y a toujours un code 1 pour une annonce 38, mais il peut y avoir aussi un code 2 qui est lié à ce code 1
         * (idem enregistrement 001 et 002 pour annonces 39)
         */

        // si code 2 (partie information)
        if (code.equals("2")) {
            liste.add(new REAnnoncesHermesMap(annonce, "38001"));
            // pour la suite : s'il y a une code 2, il y a forcément
            // une code 1

            // si code 1 (partie CI)
        } else if (code.equals("1")) {

            liste.add(new REAnnoncesHermesMap(annonce, "38002"));

            // on est dans le cas ou les dernier element est une 38
            // d'un CI-ADD
            if (i == 0) {
                output.add(liste);

                liste = new REArrayListPourAnnonce();
                isCIADD = false;
            }

        } else {
            throw new REDownloaderException("Code incorrect! code=" + code);
        }
    }

    private void traiteAnnonce39(IHEOutputAnnonce annonce, List<REArrayListPourAnnonce> output)
            throws REDownloaderException {
        // Définition du code enregistrement
        String code = JadeStringUtil.substring(annonce.getChampEnregistrement(), 2, 3);

        /*
         * Il y a toujours une annonce 39 au début (fin) du groupe, cette annonce 39 comporte toujours un enregistrement
         * 001 et il peut aussi y avoir un enregistrement 002
         */

        // si enregistrement 002
        if (code.equals("002")) {

            // si c'est deja un CI-ADD, changer la clé pour ce ci
            // qui
            // est un Nouveau CI-ADD
            if (isCIADD) {
                output.add(liste);

                liste = new REArrayListPourAnnonce();
                isCIADD = false;
            }

            liste.add(new REAnnoncesHermesMap(annonce, "39002"));
            // pour la suite : s'il y a une 002, il y a forcément
            // une 001

        } else if (code.equals("001")) {

            liste.add(new REAnnoncesHermesMap(annonce, "39001"));
            // pour la suite : si c'est un CI-ADD, en prendre note,
            // car il n'y aura pas d'annonces 11
            // sinon, continuer pour chercher l'annonce 38

            String CIAdditionnel = JadeStringUtil.substring(annonce.getChampEnregistrement(), 91, 1);

            // definir si CI-ADD ou CI-Normal
            isCIADD = "1".equals(CIAdditionnel);
        } else {
            throw new REDownloaderException("Code enregistrement de l'annonce 39 incorrect! code enregistrement="
                    + code);
        }
    }
}
