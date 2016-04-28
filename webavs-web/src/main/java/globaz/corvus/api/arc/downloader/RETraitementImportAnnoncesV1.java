package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.arc.downloader.domaine.NSSAyantDroit;
import globaz.corvus.api.arc.downloader.domaine.NSSAyantDroitV1;
import globaz.corvus.api.arc.downloader.domaine.TypeCI;
import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.RECompteIndividuelManager;
import globaz.corvus.db.ci.REInscriptionCI;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.vb.annonces.REAnnonceInscriptionsCIViewBean;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;

class RETraitementImportAnnoncesV1 {
    private final FWMemoryLog _log;
    private final BSession _session;
    private final BITransaction _transaction;

    RETraitementImportAnnoncesV1(BITransaction transaction, BSession session, FWMemoryLog log) {
        _transaction = transaction;
        _session = session;
        _log = log;
    }

    private REInscriptionCI ajouterCIAdd(IHEOutputAnnonce annonce, REArrayListPourAnnonce regroupementAnnonces,
            String idRci, String idTiersCI, String idCIPourControle) throws Exception {
        // creation de l'annonce
        String idAnnonce = createAnnonce38(annonce, regroupementAnnonces, idTiersCI, idCIPourControle);

        // creation de l'inscription
        REInscriptionCI inscription = new REInscriptionCI();
        inscription.setSession(_session);
        inscription.setIdArc(idAnnonce);
        inscription.setIdRCI(idRci);
        inscription.add(_transaction);

        return inscription;
    }

    /**
     * Créé l'annonce 38. ------------------------------------------------------------------- Enregistrement
     * d'inscription 1 (partie CI) -----+----------+-------------------------------------+------------ champ| position |
     * Contenu | Observation -----+----------+-------------------------------------+------------ 1 | 1- 2 | Code
     * application: 38 | 2 | 3- 5 | Code enregistrement: 001-999 |6 3 | 6- 8 | Numéro de la Caisse commettante |3 4 | 9-
     * 11 | Numéro de l'agence commettante |3 5 | 12- 31 | Référence interne de la Caisse | | | commettante |3 6 | 32-
     * 42 | Numéro d'assuré de l'ayant droit ou | | | du partenaire en cas d'ordre | | | de splitting |3, 12 7 | 43- 44
     * | Motif de l'annonce |3 8 | 45- 48 | Date de clôture: MMAA |2, 3 9 | 49- 54 | Date de l'ordre: JJMMAA |3 10 | 55-
     * 57 | Numéro de la Caisse tenant le CI |1 11 | 58- 60 | Numéro de l'agence tenant le CI |1 12 | 61- 71 | Numéro
     * d'assuré |3 13 | 72 | Code 1 (= partie CI) |11 14 | 73- 79 | Réserve: zéros | 15 | 80- 90 | Numéro d'affilié |5,
     * 14 16 | 91 | Chiffre-clé désignant les extournes |1 17 | 92 | Chiffre-clé du genre de cotisations | 18 | 93 |
     * Chiffre-clé particulier |2, 8 19 | 94- 95 | Part aux bonifications d'assistance |1, 2, 9 20 | 96- 97 | Code
     * spécial |13 21 | 98- 99 | Durée de cotisations (début) |1 22 | 100-101 | Durée de cotisations (fin) |1 23 |
     * 102-103 | Année de cotisations |10 24 | 104-112 | Revenu |1, 4 25 | 113 | Réserve: zéro | 26 | 114 | Code A:
     * amortissement |2 | | Code D: exemption de cotisations |2 | | Code S: sursis au paiement des | | | cotisations
     * (seulement CSC)|2 27 | 115-116 | Branche économique |7 28 | 117-120 | Année de cotisations (AAAA) |
     * -----+----------+-------------------------------------+------------ Les champs inutilisés sont à blanc. 1 = Cadré
     * à droite, les positions inoccupées sont complétées par des zéros. 2 = Si le champ n'est pas utilisé, la position
     * est à blanc. 3 = Selon les données de l'Ordre de clôture et de transmission du CI. 4 = Sans signe préalable. 5 =
     * Les inscriptions des années 1948 à 1968 comportent obligatoirement le chiffre de la branche économique aux deux
     * dernières positions (doc. 318.104.01 Appendice IX). 6 = Pour les CI comportant plus de 999 inscriptions la
     * séquence d'enregistrement est à recommencer à partir de 001. 7 = La branche économique figure aussi,
     * obligatoirement, dans le champ de rechange 27. 8 = Splitting en cas de divorce 1: revenus partagés issus des
     * années de jeunesse 2: revenus partagés qui sont insérés dans une lacune de cotisations d'un conjoint s'étendant
     * sur l'année entière, lacune qui peut être comblée en tenant compte d'une année de jeunesse 3: revenus partagés
     * qui sont insérés dans une lacune de cotisations d'un conjoint s'étendant sur l'année entière, lacune qui peut
     * être comblée en tenant compte d'une année d'appoint 4: revenu annuel moyen déterminant partagé pour les années
     * civiles où l'un des conjoints était bénéficiaire d'une rente d'invalidité 5: revenus partagés déjà pris en compte
     * pour une rente 9 = Part aux bonifications d'assistance par ex.: 01 = bonification d'assistance entière 02 =
     * demi-bonification d'assistance 03 = tiers de bonification d'assistance 10 = Les deux dernières positions. 11 = A
     * la partie CI (enregistrement d'inscription 1) succède la partie Information selon l'enregistrement d'inscription
     * 2. Si cette dernière partie n'est pas utilisée, l'enregistrement d'inscription 2 est sans objet. 12 = En cas de
     * splitting: numéro d'assuré du partenaire (selon champ 3 de l'enregistrement fixe 03 ou du champ 27 de
     * l'enregistrement variable relatif à l'application 22). 13 = Code spécial (si inutilisé, compléter par des zéros).
     * 01 = Cotisation minimale acquittée par la collectivité publique. 02 = Revenus non formateurs de rente des
     * personnes de condition indépendante et des salariés pour qui l'employeur n'est pas tenu de payer des cotisations.
     * 03 = Revenus non formateurs de rente des salariés. 14 = Cadré à gauche, les positions inoccupées sont à blanc.
     * -------------------------------------------------------------------
     * ------------------------------------------------------------------- Enregistrement d'inscription 2 (partie
     * Information) -----+----------+-------------------------------------+------------ champ| position | Contenu |
     * Observation -----+----------+-------------------------------------+------------ 14 | 73-114 | Partie Information
     * |6 15 | 115-120 | Numéro postal de l'employeur |2, 7
     * -----+----------+-------------------------------------+------------ Les champs inutilisés sont à blanc. 2 = Cardé
     * à gauche, les positions inoccupées sont à blanc. 6 = Nom et, le cas échéant, lieu de l'employeur. 7 = Facultatif.
     * Si le numéro postal est indiqué, le lieu correspondant
     * -------------------------------------------------------------------
     * 
     * @param annonce
     * @return id de l'annonce créée
     * @throws Exception
     */
    private String createAnnonce38(IHEOutputAnnonce annonce, REArrayListPourAnnonce regroupementAnnonces,
            String IdTiersCiPourControle, String idCIPourControle) throws Exception {

        REAnnonceInscriptionsCIViewBean vb = new REAnnonceInscriptionsCIViewBean();
        vb.setProvenance("1");

        String enregistrement = annonce.getChampEnregistrement();

        // //////////////////////////////////////////////////////////////////////////
        // RECUPERATION DES VALEUR DE L'ANNONCE RETOURNEE PAR HERMES
        // //////////////////////////////////////////////////////////////////////////

        // 1 | Code application: 38
        vb.setCodeApplication(JadeStringUtil.substring(enregistrement, 0, 2));

        // 2 | Code enregistrement: 001-999
        vb.setCodeEnregistrement01(JadeStringUtil.substring(enregistrement, 2, 3));

        // 3 | Numéro de la Caisse commettante
        vb.setNumeroCaisse(JadeStringUtil.substring(enregistrement, 54, 3));

        // 4 | Numéro de l'agence commettante
        vb.setNumeroAgence(JadeStringUtil.substring(enregistrement, 57, 3));

        // 5 | Référence interne de la Caisse commettante
        vb.setRefInterneCaisse(JadeStringUtil.substring(enregistrement, 11, 20));

        // 6 | Numéro d'assuré de l'ayant droit ou du partenaire en cas d'ordre
        // de splitting
        // Récupération de l'id tiers de l'assure
        String nss = JadeStringUtil.substring(enregistrement, 31, 11);
        if (nss.startsWith("-")) {
            nss = JadeStringUtil.change(nss, "-", "756");
        } else {
            // on cherche dans la table de concordance pour trouver le NSS
            String nssConcordance = NSUtil.returnNNSS(_session, nss);
            if (nssConcordance != null) {
                nss = nssConcordance;
            }
        }

        // premiere recherche dans les NSS/N AVS courant
        PRTiersWrapper twAyantDroit = REDownloaderInscriptionsCI.getTiersFromNss(_session, _transaction, nss);

        if (twAyantDroit != null) {
            String id_tiers = twAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            vb.setIdTiersAyantDroit(id_tiers);
            vb.setIdTiers(id_tiers);
        } else {

            // pour les motifs 71, 81, 75, 85 et 79, il est obligatoire de
            // retrouver le tiers dans Pyxis
            if (Integer.parseInt(JadeStringUtil.substring(enregistrement, 42, 2)) < 91) {
                // Ajouter message dans le mail
                String information = String
                        .format("Le NSS ou no AVS de l'ayant droit suivant n'a pas été trouvé dans les tiers: %s. Pour les motifs 71, 81, 75, 85 et 79, il est obligatoire de retrouver un tiers.",
                                nss);

                System.out.println(information);

                this.logMessage(information);
            }
        }

        // 7 | Motif de l'annonce
        vb.setMotif(JadeStringUtil.substring(enregistrement, 42, 2));

        // 8 | Date de clôture: MMAA
        vb.setDateCloture(JadeStringUtil.substring(enregistrement, 44, 4));

        // 9 | Date de l'ordre: JJMMAA
        vb.setDateOrdre(JadeStringUtil.substring(enregistrement, 48, 6));

        // 10 | Numéro de la Caisse tenant le CI
        vb.setNoAgenceTenantCI(JadeStringUtil.substring(enregistrement, 57, 3));

        // 11 | Numéro de l'agence tenant le CI
        vb.setNoCaisseTenantCI(JadeStringUtil.substring(enregistrement, 54, 3));

        // 12 | Numéro d'assuré
        String nss2 = JadeStringUtil.substring(enregistrement, 60, 11);
        if (nss2.startsWith("-")) {
            nss2 = JadeStringUtil.change(nss2, "-", "756");
        }

        // si le no AVS donné est dans l'historique des no AVS de l'assure, on
        // prend
        // le no AVS avec lequel le rassemblement a ete demande
        if (regroupementAnnonces.getHistNoAvsAssure().contains(nss2)) {
            nss2 = regroupementAnnonces.getNssForRassemblement();
            nss2 = JadeStringUtil.change(nss2, "-", "756");
        } else {
            // on cherche dans la table de concordance pour trouver le NSS
            String nssConcordance = NSUtil.returnNNSS(_session, nss2);
            if (nssConcordance != null) {
                nss2 = nssConcordance;
            }
        }

        // premiere recherche dans les NSS/N AVS courant
        PRTiersWrapper twAssure = REDownloaderInscriptionsCI.getTiersFromNss(_session, _transaction, nss2);

        if (twAssure != null) {
            String id_tiers = twAssure.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            vb.setIdTiers(id_tiers);
        } else {
            // pour les motifs 71, 81, 75, 85 et 79, il est obligatoire de
            // retrouver le tiers dans Pyxis
            if (Integer.parseInt(JadeStringUtil.substring(enregistrement, 42, 2)) < 91) {
                // Ajouter message dans le mail

                String information = String
                        .format("Le NSS ou no AVS de l'assuré suivant n'a pas été trouvé dans les tiers: %s. Pour les motifs 71, 81, 75, 85 et 79, il est obligatoire de retrouver un tiers.",
                                nss2);

                System.out.println(information);

                this.logMessage(information);
            }
        }

        // /////////////////////////////////////////////////////////////////////////////
        // Inscription 1
        // /////////////////////////////////////////////////////////////////////////////

        // 13 | Code 1 (= partie CI)
        // pas utilise

        // 14 | Réserve: zéros
        // pas utilise

        // 15 | Numéro d'affilié
        String noAff = JadeStringUtil.substring(enregistrement, 79, 11);
        // le no aff. peut aussi etre un nss
        noAff = JadeStringUtil.change(noAff, "-", "756");
        vb.setNoAffilie(noAff);

        // 16 | Chiffre-clé désignant les extournes
        vb.setCodeExtourne(JadeStringUtil.substring(enregistrement, 90, 1));

        // 17 | Chiffre-clé du genre de cotisations
        vb.setGenreCotisation(JadeStringUtil.substring(enregistrement, 91, 1));

        // 18 | Chiffre-clé particulier (code particulier)
        vb.setCodeParticulier(JadeStringUtil.substring(enregistrement, 92, 1));

        // 19 | Part aux bonifications d'assistance
        vb.setPartBonifAssist(JadeStringUtil.substring(enregistrement, 93, 2));

        // 20 | Code spécial
        vb.setCodeSpeciale(JadeStringUtil.substring(enregistrement, 95, 2));

        // 21 | Durée de cotisations (début)
        vb.setMoisDebutCotisations(JadeStringUtil.substring(enregistrement, 97, 2));

        // 22 | Durée de cotisations (fin)
        vb.setMoisFinCotisations(JadeStringUtil.substring(enregistrement, 99, 2));

        // 23 | Année de cotisations (AA)
        // Pas utilisé, on utilise le champs 28 (Année cotisations AAAA) a la
        // place

        // 24 | Revenu
        vb.setRevenu(JadeStringUtil.substring(enregistrement, 103, 9));

        // 25 |Réserve: zéro
        // pas utilise

        // 26 | Code A: amortissement
        // | Code D: exemption de cotisations
        // | Code S: sursis au paiement des cotisations (seulement CSC)
        vb.setCodeADS(JadeStringUtil.substring(enregistrement, 113, 1));

        // 27 | Branche économique
        vb.setBrancheEconomique((_session).getSystemCode("VEBRANCHEE", JadeStringUtil.substring(enregistrement, 114, 2)));

        // 28 | Année de cotisations (AAAA)
        vb.setAnneeCotisations(JadeStringUtil.substring(enregistrement, 116, 4));

        // /////////////////////////////////////////////////////////////////////////////
        // Inscription 2
        // /////////////////////////////////////////////////////////////////////////////

        /*
         * On prend pas les 02 !
         */

        // Vérification de l'égalité entre ID tiers du CI et de l'annonce 38 créée - Monitoring du BZ 9375
        String idTiersAnnonce = vb.getIdTiers();

        if (idTiersAnnonce.equals(IdTiersCiPourControle) == false) {
            String message = "Incohérence de tiers entre compte individuel et annonce:\n"
                    + "--------------------------------------------------------\n"
                    + "ID tiers compte individuel (RECI)             :  " + IdTiersCiPourControle + "\n"
                    + "PK compte individuel (RECI)                   :  " + idCIPourControle + "\n"
                    + "ID tiers l'inscription CI (REANICI)           :  " + idTiersAnnonce + "\n"
                    + "ID tiers ayant droit inscription CI (REANICI) :  " + vb.getIdTiersAyantDroit() + "\n"
                    + "PK (colonne RIANN) annonce HERMES (HEANNOP)   :  " + annonce.getIdAnnonce() + "\n"
                    + "Champ enregistrement annonce HERMES (HEANNOP) : \"" + enregistrement + "\"\n\n"
                    + "Le traitement complet a été arrêté et n'a pas été exécuté";

            this.logMessage(message);

            // throw new Exception(message); // TODO Momentanément commenté. Comportement final à établir avec BAN's
        }

        vb.add((BTransaction) _transaction);
        return vb.getIdAnnonce();
    }

    public Map<String, List<REInscriptionCI>> execute(List<REArrayListPourAnnonce> annonceArrayList) throws Exception,
            JadeNoBusinessLogSessionError, REDownloaderException {

        Map<String, List<REInscriptionCI>> inscriptionCIAjoutees = new HashMap<String, List<REInscriptionCI>>();

        groupesAnnonce: for (Iterator<REArrayListPourAnnonce> iterator = annonceArrayList.iterator(); iterator
                .hasNext();) {

            REArrayListPourAnnonce listeAnnonces = iterator.next();

            // tri de la liste pour inverser l'ordre (en final, ça donne le
            // bon ordre 11-38-39 ou 38-39
            Collections.sort(listeAnnonces);

            /*
             * On boucle sur toutes les annonces de la liste pour faire le traitement. Ordre d'arrivée, exemples :
             * Exemple 1 : Exemple 2 : Exemple 3: ----------- ----------- ---------- (CI-ADD) (CI-Normal) (CI-Normal)
             * 38001 11001 11001 39001 38001 38001 39002 38002 38002 39001 39001 38001 39001 39002
             */

            boolean isCI_ADD = determineIfIsCI_ADD(listeAnnonces);
            String refArc = "";
            RERassemblementCI rci = null;
            String idTiersCIPourControle = "";
            String idCIPourControle = "";

            // liste des références uniques dans les ARC pour cette importation
            // utile pour les CI-ADD lorsqu'il y a plusieurs annonces 38
            Set<String> refArcDansCetteImportation = new HashSet<String>();

            // liste des NNS ayant déjà été annoncés dans le mail (pour n'avoir qu'une fois chaque NSS)
            Set<String> nssDejaAnnoncesDansLeMail = new HashSet<String>();

            REAnnoncesHermesMap ann1104 = null;

            // Ensuite, on parcours toute la liste pour créer les annonces
            for (Iterator<REAnnoncesHermesMap> iterator2 = listeAnnonces.iterator(); iterator2.hasNext();) {
                REAnnoncesHermesMap ann = iterator2.next();

                // //////////////////////////////////////////////////////////////////////////////////////
                // a) si c'est un CI-ADD
                // //////////////////////////////////////////////////////////////////////////////////////
                if (isCI_ADD) {

                    /*
                     * Pour les CI Additionnels, on ajoute toutes les annonces dans le rassemblement enfant Il faut
                     * d'abord voir s'il en existe un ou s'il faut en créer un
                     */

                    // On ne traite que les annonces 38 et enregistrement 01
                    // pour les CI-ADD
                    if (isAnnonces38Enregistrement01(ann)) {

                        // Gestion du nss
                        NSSAyantDroitV1 nssAyantDroit = new NSSAyantDroitV1(ann, _session, _log)
                                .forTypeCI(TypeCI.CI_ADDITIONNEL);

                        JadeLogger.info(this, "CI-ADD pour: " + nssAyantDroit);

                        if (!nssDejaAnnoncesDansLeMail.contains(nssAyantDroit.getNss())) {
                            String information = FWMessageFormat.format((_session).getLabel("INFO_CI_ADD_POUR"),
                                    NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                            this.logMessage(information);

                            nssDejaAnnoncesDansLeMail.add(nssAyantDroit.getNss());
                        }

                        // le tiers existe dans Pyxis
                        if (nssAyantDroit.existInPyxs()) {
                            //
                            // Récupération du CI de l'assuré
                            RECompteIndividuel ci = rechercherOuCreerCI(nssAyantDroit.getTiers().getProperty(
                                    PRTiersWrapper.PROPERTY_ID_TIERS));

                            // Récupération de la référence unique dans les ARC (RNREFU de HEANNOP)
                            refArc = JadeStringUtil.stripBlanks(ann.getAnnonce().getRefUnique());

                            // Recherche d'un RCI avec la même ref unique
                            RERassemblementCIManager rciSimilaireMgr = new RERassemblementCIManager();
                            rciSimilaireMgr.setSession(_session);
                            rciSimilaireMgr.setForRefARC(refArc);
                            rciSimilaireMgr.find(_transaction);

                            if (!refArcDansCetteImportation.contains(refArc) && (rciSimilaireMgr.getSize() > 0)) {
                                // Si déjà un RCI avec la même ref unique (ne provenant pas de cette importation),
                                // il y a un possible doublon et on ignore l'importation de ce CI-ADD
                                String information = FWMessageFormat.format((_session).getLabel("INFO_DOUBLON_CI_ADD"),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNss()), refArc);

                                this.logMessage(information);

                                continue groupesAnnonce;
                            }
                            refArcDansCetteImportation.add(refArc);

                            if (rciSimilaireMgr.getSize() > 0) {

                                // le dernier rassemblement CI (pour même ref unique des ARC (RNREFU)
                                rci = (RERassemblementCI) rciSimilaireMgr.getFirstEntity();
                                verifierRCI(rci, nssAyantDroit.getTiers(), ann.getAnnonce());
                            } else {
                                // si pas de RCI avec le même N° d'ARC, creation d'un nouvel RCI
                                // qui va contenir le CI-add

                                RERassemblementCI rciParent = rechercherOuCreerRCIParent(nssAyantDroit.getTiers()
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), JadeStringUtil.substring(ann
                                        .getAnnonce().getChampEnregistrement(), 42, 2),
                                        PRDateFormater.convertDate_MMAA_to_MMxAAAA(JadeStringUtil.substring(ann
                                                .getAnnonce().getChampEnregistrement(), 44, 4)), ci.getIdCi());

                                // on crée ensuite le RCI enfant
                                rci = new RERassemblementCI();
                                rci.setSession(_session);
                                rci.setReferenceUniqueArc(refArc);
                                rci.setIdCI(ci.getIdCi());
                                rci.setCsEtat(IRERassemblementCI.CS_ETAT_RASSEMBLE);
                                rci.setMotif(JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 42, 2));
                                rci.setDateCloture(PRDateFormater.convertDate_MMAA_to_MMxAAAA(JadeStringUtil.substring(
                                        ann.getAnnonce().getChampEnregistrement(), 44, 4)));
                                rci.setDateRassemblement(JACalendar.todayJJsMMsAAAA());
                                rci.setIdTiersAyantDroit(nssAyantDroit.getTiers().getProperty(
                                        PRTiersWrapper.PROPERTY_ID_TIERS));
                                rci.setIdParent(rciParent.getIdRCI());
                                rci.add(_transaction);
                            }

                            /*
                             * On récupère le compte individuel pour obtenir l'id tiers, comme ça on peut associer le
                             * CIAdd directement au tiers
                             */
                            RECompteIndividuel compteIndividuel = new RECompteIndividuel(); // Table RECI
                            compteIndividuel.setSession(_session);
                            compteIndividuel.setIdCi(rci.getIdCI());
                            compteIndividuel.retrieve(_transaction);
                            if (compteIndividuel.isNew()) {
                                JadeThread.logError(this.getClass().getName(),
                                        "Impossible de récupérer le compte individuel avec l'id [" + rci.getIdCI()
                                                + "]");
                            }

                            idTiersCIPourControle = compteIndividuel.getIdTiers();
                            idCIPourControle = compteIndividuel.getIdCi();

                            REInscriptionCI inscription = ajouterCIAdd(ann.getAnnonce(), listeAnnonces, rci.getIdRCI(),
                                    idTiersCIPourControle, idCIPourControle);

                            if (!inscriptionCIAjoutees.containsKey(idTiersCIPourControle)) {
                                inscriptionCIAjoutees.put(idTiersCIPourControle, new ArrayList<REInscriptionCI>());
                            }

                            inscriptionCIAjoutees.get(idTiersCIPourControle).add(inscription);
                        } else {

                            // si pas de tier pour ce NSS, on ne fait rien
                            // on passe au prochain groupe d'annonces

                            if (nssAyantDroit.areTwoNssEquals()) {

                                // Ajouter message dans le mail
                                System.out.println("Le NSS ou no AVS suivant n'a pas été trouvé dans les tiers: "
                                        + nssAyantDroit + ". Le CI-add n'a pas été traité.");

                                String information = FWMessageFormat.format(
                                        (_session).getLabel("INFO_NSS_NAVS_PAS_DANS_TIERS_POUR_CI_ADD"),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                                this.logMessage(information);
                            } else {

                                String information = FWMessageFormat.format(
                                        (_session).getLabel("INFO_NAVS_ET_NSS_CONCORDANCE_PAS_DANS_TIERS_POUR_CI_ADD"),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNssRecuDansAnnonce()),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                                this.logMessage(information);
                            }

                            continue groupesAnnonce;
                        }

                    }

                    // mettre le CI Enfant également a rassemblé TODO

                    // //////////////////////////////////////////////////////////////////////////////////////
                    // b) si c'est un CI normal
                    // //////////////////////////////////////////////////////////////////////////////////////
                } else {

                    // Pour l'annonce 11 (la première normalement)
                    if (isAnnonce11(ann)) {

                        if (isMotif99(ann)) {
                            continue groupesAnnonce;
                        }

                        // on cherche l'assure dans Pyxis
                        // on cherche bien le nss dans l'annonce 11
                        // TODO Attention, la logique d'extraction du nss ici n'est pas identique à celle trouvée dans
                        // createAnnonce38 -> à uniformiser!
                        // String nssAssureAyantDroit = JadeStringUtil.substring(
                        // ann.getAnnonce().getChampEnregistrement(), 36, 11);
                        // String nssAssureAyantDroitRecuDansAnnonce = nssAssureAyantDroit;
                        // if (nssAssureAyantDroit.startsWith("-")) {
                        // nssAssureAyantDroit = JadeStringUtil.change(nssAssureAyantDroit, "-", "756");
                        // } else {
                        // // on cherche dans la table de concordance pour
                        // // trouver le NSS
                        // String nssConcordance = NSUtil.returnNNSS(_session, nssAssureAyantDroit);
                        // if (nssConcordance != null) {
                        // nssAssureAyantDroit = nssConcordance;
                        // }
                        // }

                        NSSAyantDroit nssAyantDroit = new NSSAyantDroit(ann, _session, _log)
                                .forTypeCI(TypeCI.CI_NORMAL);

                        String information = FWMessageFormat.format((_session).getLabel("INFO_CI_POUR"),
                                NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                        this.logMessage(information);

                        // premiere recherche dans les NSS/N AVS courant
                        // PRTiersWrapper assureAyantDroit = REDownloaderInscriptionsCI.getTiersFromNss(_session,
                        // _transaction, nssAyantDroit.nss());

                        // le tiers existe dans Pyxis
                        if (nssAyantDroit.existInPyxs()) {
                            // Récupération du CI de l'assuré
                            RECompteIndividuel ci = rechercherOuCreerCI(nssAyantDroit.getTiers().getProperty(
                                    PRTiersWrapper.PROPERTY_ID_TIERS)); // Table RECI

                            idTiersCIPourControle = ci.getIdTiers();
                            idCIPourControle = ci.getIdCi();

                            // on cherche bien la ref ARC dans l'annonce 11

                            // la reference ARC n'est plus reprise depuis l'annonce elle meme, mais directement dans
                            // HEANNOP.
                            // refArc = JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 30, 6);
                            refArc = JadeStringUtil.stripBlanks(ann.getAnnonce().getRefUnique());

                            // on cherche le dernier RCI avec la ref. ARC
                            RERassemblementCIManager rciMgr = new RERassemblementCIManager();
                            rciMgr.setSession(_session);

                            // on peut comparer directement la refARC, car la refARC contenue dans HEANNOP n'est pas
                            // tronquee
                            // rciMgr.setRefARCEndWith(refArc);
                            rciMgr.setForRefARC(refArc);

                            rciMgr.setForIdTiers(nssAyantDroit.getTiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                            rciMgr.setOrderBy(RERassemblementCI.FIELDNAME_ID_RCI + " DESC");
                            rciMgr.find(_transaction, 1);

                            if (rciMgr.getSize() > 0) {

                                rci = (RERassemblementCI) rciMgr.getFirstEntity();

                                // Si le rassemblement est en état rassemblé, on notifie l'utilisateur
                                // qu'il y a un possible doublon et on ignore l'importation du CI
                                if (IRERassemblementCI.CS_ETAT_RASSEMBLE.equals(rci.getCsEtat())) {
                                    String information2 = FWMessageFormat.format(
                                            (_session).getLabel("INFO_DOUBLON_RASSEMBLEMENT"),
                                            NSUtil.formatAVSUnknown(nssAyantDroit.getNss()), refArc);

                                    this.logMessage(information2);

                                    continue groupesAnnonce;
                                }

                                // Si le rassemblement est révoqué, on en crée un nouveau
                                if (IRERassemblementCI.CS_ETAT_REVOQUE.equals(rci.getCsEtat())) {
                                    // creation d'un autre RCI identique
                                    // avec la ref. ARC, mais dans l'etat
                                    // "en attente"
                                    RERassemblementCI newRci = new RERassemblementCI();
                                    newRci.setSession(_session);
                                    newRci.setCsEtat(IRERassemblementCI.CS_ETAT_ENCOURS);
                                    newRci.setMotif(rci.getMotif());
                                    newRci.setDateCloture(rci.getDateCloture());
                                    newRci.setIdTiersAyantDroit(rci.getIdTiersAyantDroit());
                                    newRci.setIdCI(rci.getIdCI());
                                    newRci.setReferenceUniqueArc(refArc);
                                    newRci.add(_transaction);

                                    rci = newRci;
                                }

                                // on cherche bien la date de cloture dans
                                // l'annonce 11 04
                                // si il y a un 11 04...
                                if (listeAnnonces.hasAnnonce1104()) {
                                    ann1104 = iterator2.next();
                                }

                                // si pas de RCI avec la ref. ARC on cherche
                                // le tier par son NSS
                                // on passe au prochain bloc
                            } else {

                                // si il y a un 11, creation d'un nouvel RCI
                                // pour cette demande de rassemblement
                                rci = new RERassemblementCI();
                                rci.setSession(_session);
                                rci.setIdCI(ci.getIdCi());
                                rci.setCsEtat(IRERassemblementCI.CS_ETAT_RASSEMBLE);
                                // on cherche le motif dans l'annonce 11 01
                                rci.setMotif(JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 99, 2));

                                // on cherche la date de cloture dans l'annonce 11 04
                                // si il y a un 11 04...
                                if (listeAnnonces.hasAnnonce1104()) {
                                    ann1104 = iterator2.next();

                                    if (ann1104.getAnnonce().getChampEnregistrement().length() > 59) {
                                        rci.setDateCloture(PRDateFormater.convertDate_MMAA_to_MMxAAAA(JadeStringUtil
                                                .substring(ann1104.getAnnonce().getChampEnregistrement(), 56, 4)));
                                    }
                                }

                                rci.setReferenceUniqueArc(refArc);
                                rci.setIdTiersAyantDroit(nssAyantDroit.getTiers().getProperty(
                                        PRTiersWrapper.PROPERTY_ID_TIERS));
                                rci.add(_transaction);

                            }

                            // si l'idTers manque, on prend celui du tiers
                            // ayant droit
                            if (JadeStringUtil.isIntegerEmpty(rci.getIdTiersAyantDroit())) {
                                rci.retrieve(_transaction);
                                rci.setIdTiersAyantDroit(nssAyantDroit.getTiers().getProperty(
                                        PRTiersWrapper.PROPERTY_ID_TIERS));
                                rci.update(_transaction);
                            }

                        } else {
                            // si pas de tier pour ce NSS, on ne fait rien
                            // on passe au prochain groupe d'annonces

                            // Ajouter message dans le mail

                            if (nssAyantDroit.areTwoNssEquals()) {

                                System.out.println("Le NSS ou no AVS suivant n'a pas été trouvé dans les tiers: "
                                        + nssAyantDroit + ". Le CI n'a pas été traité.");

                                String information2 = FWMessageFormat.format(
                                        (_session).getLabel("INFO_NSS_NAVS_PAS_DANS_TIERS_POUR_CI"),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                                this.logMessage(information2);
                            } else {

                                String information2 = FWMessageFormat.format(
                                        (_session).getLabel("INFO_NAVS_ET_NSS_CONCORDANCE_PAS_DANS_TIERS_POUR_CI"),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNssRecuDansAnnonce()),
                                        NSUtil.formatAVSUnknown(nssAyantDroit.getNss()));

                                this.logMessage(information2);
                            }

                            continue groupesAnnonce;
                        }

                        // pour les autres annonces (38 et 39)
                    } else {

                        // Ici on traite toutes les annonces 38 et 39 après
                        // l'annonce 11
                        String code = JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 0, 2);

                        if ("39".equals(code)) {
                            traiteAnnonce39(rci, ann);

                        } else if ("38".equals(code)) {
                            traiteAnnonce38(listeAnnonces, rci, ann, idTiersCIPourControle, idCIPourControle);

                        } else {
                            throw new REDownloaderException("Code application annonce incorrect! code application="
                                    + code);
                        }

                    }

                    // A la fin du traitement d'un CI

                    // On met le rassemblement de CI à jour
                    rci.retrieve(_transaction);
                    rci.setCsEtat(IRERassemblementCI.CS_ETAT_RASSEMBLE);
                    rci.setDateRassemblement(JACalendar.todayJJsMMsAAAA());
                    rci.update(_transaction);

                    // On cherche la demande de rente qui correspond
                    REDemandeRenteJointDemandeManager drjdManager = new REDemandeRenteJointDemandeManager();
                    drjdManager.setSession(_session);
                    drjdManager.setForCsEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                    drjdManager.setForCsEtatDemandeIn(null);
                    drjdManager.setForIdTiersRequ(rci.getIdTiersAyantDroit());
                    // pas les API
                    drjdManager.setForCsTypeDemandeIn(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE + ","
                            + IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT + ","
                            + IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE);
                    drjdManager.find(_transaction);

                    Iterator<REDemandeRenteJointDemande> iter = drjdManager.iterator();
                    while (iter.hasNext()) {
                        REDemandeRenteJointDemande drjd = iter.next();

                        REDemandeRente dr = new REDemandeRente();
                        dr.setSession(_session);
                        dr.setIdDemandeRente(drjd.getIdDemandeRente());
                        dr.retrieve(_transaction);

                        dr.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL);
                        dr.update(_transaction);
                    }
                }
            }
        }

        return inscriptionCIAjoutees;
    }

    /**
     * on test si ce nest pas un motif 99
     * on peut recevoir un CI avec un motif 99, mais sans annonce 29 liee.
     * Ces CI ne doivent pas etre enregistre dans la DB
     * 
     * @param ann l'annonce servant à déterminer le motif
     * @return booléen si c'est un motif 99
     */
    private boolean isMotif99(REAnnoncesHermesMap ann) {
        return "99".equals(JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 99, 2));
    }

    private boolean isAnnonce11(REAnnoncesHermesMap ann) {
        return JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 0, 2).equals("11");
    }

    private boolean isAnnonces38Enregistrement01(REAnnoncesHermesMap ann) {
        return "38".equals(JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 0, 2))
                && "1".equals(JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 71, 1));
    }

    /**
     * Détermine si on est dans le cas d'un CI_ADDITIONEL.
     * On prend le premier élément de la liste pour définir si ça
     * sera un CI-ADD ou pas
     * 
     * @param listeAnnonces, la liste des annonces
     * @return état booléen si on est dans le cas d'un CI Additionel
     */
    private boolean determineIfIsCI_ADD(REArrayListPourAnnonce listeAnnonces) {

        Preconditions.checkNotNull(listeAnnonces);

        boolean isCI_ADD = false;

        if (listeAnnonces.size() > 0) {
            REAnnoncesHermesMap annonce = listeAnnonces.get(0);

            isCI_ADD = isAnnonce11(annonce) == false;
        }
        return isCI_ADD;
    }

    private void logErreur(String text) {
        this.logMessage(text, FWMessage.ERREUR);
    }

    private void logMessage(String text) {
        this.logMessage(text, FWMessage.INFORMATION);
    }

    private void logMessage(String text, String level) {
        _log.logMessage(text, level, this.getClass().getName());
    }

    private RECompteIndividuel rechercherOuCreerCI(String idTiers) throws Exception {

        // Rechercher le CI pour un id-tiers donné via un manager
        RECompteIndividuelManager ciManager = new RECompteIndividuelManager();
        ciManager.setSession(_session);
        ciManager.setForIdTiers(idTiers);
        ciManager.find();

        // Le CI a été trouvé -> le retourner!
        if (ciManager.getSize() > 0) {
            return (RECompteIndividuel) ciManager.getFirstEntity();
        }

        // Le CI n'a pas été troué -> le créer et le retourner!
        RECompteIndividuel newCi = new RECompteIndividuel();
        newCi.setSession(_session);
        newCi.setAlternateKey(RECompteIndividuel.ALTERNATE_KEY_ID_TIERS);
        newCi.setIdTiers(idTiers);
        newCi.add(_transaction);

        return newCi;
    }

    private RERassemblementCI rechercherOuCreerRCIParent(String idTiers, String motif, String dateCloture, String idCI)
            throws Exception {
        // on recherche d'abord le RCI parent
        RERassemblementCIManager rciParentMgr = new RERassemblementCIManager();
        rciParentMgr.setSession(_session);
        rciParentMgr.setForIdTiers(idTiers);
        rciParentMgr.setForMotif(motif);
        rciParentMgr.setForDateClotureAAAAMM(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateCloture));
        rciParentMgr.setForRassWithoutParent(Boolean.TRUE);
        rciParentMgr.setOrderBy(RERassemblementCI.FIELDNAME_ID_RCI + " DESC");
        rciParentMgr.find(_transaction, 1);

        if (rciParentMgr.size() > 0) {
            return (RERassemblementCI) rciParentMgr.getFirstEntity();
        }

        // si pas de RCI parent, on en crée un
        RERassemblementCI rciParent = new RERassemblementCI();
        rciParent = new RERassemblementCI();
        rciParent.setSession(_session);
        rciParent.setIdTiersAyantDroit(idTiers);
        rciParent.setMotif(motif);
        rciParent.setDateCloture(dateCloture);
        rciParent.setIdCI(idCI);
        rciParent.add(_transaction);

        return rciParent;
    }

    private void traiteAnnonce38(REArrayListPourAnnonce listeAnnonces, RERassemblementCI rci, REAnnoncesHermesMap ann,
            String idTiersCIPourControle, String idCIPourControle) throws Exception {
        // creation de l'annonce

        String codeAnnonce = JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 71, 1);

        // on ne traite que les Code 1 (partie CI)
        if (codeAnnonce.equals("1")) {

            String idAnnonce = createAnnonce38(ann.getAnnonce(), listeAnnonces, idTiersCIPourControle, idCIPourControle);

            // creation de l'inscription
            REInscriptionCI inscription = new REInscriptionCI();
            inscription.setSession(_session);
            inscription.setIdArc(idAnnonce);
            inscription.setIdRCI(rci.getIdRCI());
            inscription.add(_transaction);
        }
    }

    private void traiteAnnonce39(RERassemblementCI rci, REAnnoncesHermesMap ann) throws Exception {
        // on ne traite pas les annonces 39

        // Bugzilla 4336
        // Si le RCI n'a pas ete cree depuis les rentes, il faut completer la date de cloture
        // Depuis l'annonce 39
        if (JadeStringUtil.isBlankOrZero(rci.getDateCloture())) {
            rci.retrieve(_transaction);

            // Date de clôture: MMAA
            String dateCloture = "";
            try {
                dateCloture = JadeStringUtil.substring(ann.getAnnonce().getChampEnregistrement(), 44, 4);
                dateCloture = PRDateFormater.convertDate_MMAA_to_MMxAAAA(dateCloture);
            } catch (Exception e) {
                // pas de date de cloture dans l'annonce 39
                dateCloture = "";
            }
            rci.setDateCloture(dateCloture);
            rci.update(_transaction);
        }
    }

    private void verifierRCI(RERassemblementCI rci, PRTiersWrapper assureAyantDroit, IHEOutputAnnonce annonce)
            throws Exception {

        if (rci.isNew()) {
            rci.retrieve(_transaction);
        }
        if (rci.isNew()) {
            return;
        }

        boolean needUpdate = false;

        // si l'idTers manque dans le RCI, on prend celui du tiers ayant droit
        if (JadeStringUtil.isIntegerEmpty(rci.getIdTiersAyantDroit())) {
            rci.setIdTiersAyantDroit(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            needUpdate = true;
        }

        // Bugzilla 4336
        // Si le RCI n'a pas ete cree depuis les rentes, il faut completer la date de cloture
        if (JadeStringUtil.isBlankOrZero(rci.getDateCloture())) {
            // Date de clôture: MMAA
            String dateCloture = "";
            try {
                dateCloture = JadeStringUtil.substring(annonce.getChampEnregistrement(), 44, 4);
                dateCloture = PRDateFormater.convertDate_MMAA_to_MMxAAAA(dateCloture);
            } catch (Exception e) {
                // pas de date de cloture dans l'annonce 38
                dateCloture = "";
            }

            rci.setDateCloture(dateCloture);
            needUpdate = true;
        }

        if (needUpdate) {
            rci.update(_transaction);
        }
    }

}
