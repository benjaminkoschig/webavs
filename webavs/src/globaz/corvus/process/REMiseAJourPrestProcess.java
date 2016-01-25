package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.anakin.REArcConverter;
import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiersManager;
import globaz.corvus.db.adaptation.RERentesAdaptees;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiersManager;
import globaz.corvus.db.annonces.IREAnnonceAdaptation;
import globaz.corvus.db.annonces.REAnnonce51Adaptation;
import globaz.corvus.db.annonces.REAnnonce53Adaptation;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10EmeManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9EmeManager;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.adaptation.REAnnoncesCentraleKeyRA;
import globaz.corvus.process.adaptation.REKeyAnnoncesRevalorisees;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import ch.admin.ofit.anakin.commum.Session;
import ch.admin.ofit.anakin.donnee.Annonce10eme;
import ch.admin.ofit.anakin.donnee.Annonce9eme;
import ch.admin.ofit.anakin.donnee.AnnonceAbstraite;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;
import ch.admin.ofit.arena.augmentation.controleur.ControleurAugmentationPlafonnement;
import ch.admin.ofit.arena.augmentation.controleur.ControleurAugmentationRente;
import ch.admin.ofit.arena.augmentation.controleur.ControleurAugmentationSurAssurance;

public class REMiseAJourPrestProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    private final Map<String, Object> mapPrestationsNonTrouveesCentrale2 = new TreeMap<String, Object>();
    private String moisAnnee = "";
    private String moisAnneePrecedent = "";
    private String pourcentA = "";
    private String pourcentDe = "";
    private final HashMap<String, Object> prestationsAugmentees = new HashMap<String, Object>();

    public REMiseAJourPrestProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        JACalendar cal = new JACalendarGregorian();

        moisAnneePrecedent = cal.addMonths(getMoisAnnee(), -1);
        moisAnneePrecedent = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(moisAnneePrecedent);

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            // => Si déjà rentes adaptées automatiquement = déjà fait, donc bloqué
            RERentesAdapteesJointRATiersManager radMgr = new RERentesAdapteesJointRATiersManager();
            radMgr.setForAnneeAdaptation(String.valueOf(JACalendar.getYear(getMoisAnnee())));
            radMgr.setForCsTypeAdaptation(IREAdaptationRente.CS_TYPE_AUG_ADAPTATION_AUTO);
            radMgr.setSession(getSession());
            radMgr.find(transaction);

            if (!radMgr.isEmpty()) {
                throw new Exception(getSession().getLabel("PROCESS_MAJ_PREST_ERREUR_DEJA_FAIT"));
            }

            // Vérification si SAVE OK
            FWParametersManager paramMgr = new FWParametersManager();
            paramMgr.setSession(getSession());
            paramMgr.setForApplication(REApplication.DEFAULT_APPLICATION_CORVUS);
            paramMgr.setForIdCle(IREAdaptationRente.CLE_CONTROLE_SAVE_ADAPTATION);
            paramMgr.find(transaction);

            if (paramMgr.isEmpty()) {
                throw new Exception(getSession().getLabel("PROCESS_MAJ_PREST_CTRL_SAVE_ADAPTATION"));
            }

            if (paramMgr.size() == 1) {
                FWParameters param = (FWParameters) paramMgr.getFirstEntity();
                if (!param.getValeurAlpha().equals("OK")) {
                    throw new Exception(getSession().getLabel("PROCESS_MAJ_PREST_CTRL_SAVE_ADAPTATION"));
                }
            }

            // Création des listes d'erreurs
            REListeErreursProcess process = new REListeErreursProcess();
            process.setParentWithCopy(this);
            process.setTransaction(transaction);
            process.setControleTransaction(true);
            process.setSession(getSession());
            process.setEMailAddress(getEMailAddress());
            process.setMoisAnnee(getMoisAnnee());
            process.setWantListePourcent(false);
            process.executeProcess();

            // Mise à jour des prestations
            miseAjourPrestation(transaction, process.getMapDifferencesCentraleCaisse());

            // Décisions prises en décembre
            mapPrestationsNonTrouveesCentrale2.putAll(miseAjourDecisisionDecembres(transaction,
                    process.getMapPrestationsNonTrouveesCentrale()));

            // Insertion dans une table des rentes non augmentées !!
            ajouterPrestationsNonAdaptees(transaction, mapPrestationsNonTrouveesCentrale2);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            getAttachedDocuments().clear();
            getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                    "REMiseAJourPrestProcess");
            transaction.addErrors(e.toString());
            return false;
        } finally {

            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
        try {

            // Liste des prestations augmentées
            listePrestationsAugmentees();

            // Fusion des documents générés
            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            this.mergePDF(info, true, 500, false, null);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REMiseAJourPrestProcess");
            }
            return false;
        }

        return true;

    }

    private void ajouterPrestationsNonAdaptees(final BITransaction transaction,
            final Map<String, Object> mapPrestationsNonTrouveesCentrale2) throws Exception {

        RERentesAdaptees rad = null;

        long progressCounter = 0;
        int maxScale = mapPrestationsNonTrouveesCentrale2.size();
        if (maxScale > 0) {
            setProgressScaleValue(maxScale);
        } else {
            setProgressScaleValue(1);
        }

        for (String keyMapPresNonTCentrale : mapPrestationsNonTrouveesCentrale2.keySet()) {

            setProgressCounter(progressCounter++);

            REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) mapPrestationsNonTrouveesCentrale2
                    .get(keyMapPresNonTCentrale);

            // Insertion dans une table des rentes non augmentées !!
            rad = new RERentesAdaptees();
            rad.setSession(getSession());
            rad.setCsTypeAdaptation(IREAdaptationRente.CS_TYPE_NON_AUGMENTEES);
            rad.setCodePrestation(ra.getCodePrestation());

            // Stockage NSS, Nom, Prénom selon type de rente
            String genreRente = ra.getCodePrestation();
            if (REGenresPrestations.GENRE_10.equals(genreRente) || REGenresPrestations.GENRE_13.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_50.equals(genreRente)
                    || REGenresPrestations.GENRE_70.equals(genreRente)
                    || REGenresPrestations.GENRE_72.equals(genreRente)
                    || REGenresPrestations.GENRE_81.equals(genreRente)
                    || REGenresPrestations.GENRE_82.equals(genreRente)
                    || REGenresPrestations.GENRE_83.equals(genreRente)
                    || REGenresPrestations.GENRE_84.equals(genreRente)
                    || REGenresPrestations.GENRE_85.equals(genreRente)
                    || REGenresPrestations.GENRE_86.equals(genreRente)
                    || REGenresPrestations.GENRE_87.equals(genreRente)
                    || REGenresPrestations.GENRE_88.equals(genreRente)
                    || REGenresPrestations.GENRE_91.equals(genreRente)
                    || REGenresPrestations.GENRE_92.equals(genreRente)
                    || REGenresPrestations.GENRE_93.equals(genreRente)
                    || REGenresPrestations.GENRE_94.equals(genreRente)
                    || REGenresPrestations.GENRE_95.equals(genreRente)
                    || REGenresPrestations.GENRE_96.equals(genreRente)
                    || REGenresPrestations.GENRE_97.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_89.equals(genreRente)) {
                rad.setNssTri(ra.getNss());
                rad.setNomTri(ra.getNom());
                rad.setPrenomTri(ra.getPrenom());
            } else {
                PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersComplementaire1());
                if (tier != null) {
                    rad.setNssTri(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    rad.setNomTri(tier.getProperty(PRTiersWrapper.PROPERTY_NOM));
                    rad.setPrenomTri(tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                } else {
                    rad.setNssTri(ra.getNss());
                    rad.setNomTri(ra.getNom());
                    rad.setPrenomTri(ra.getPrenom());
                }
            }

            rad.setRemarquesAdaptation(ra.getRemarquesAdaptation());

            // Recherche du RAM dans la bc
            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(getSession());
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve();

            rad.setAncienRAM(bc.getRevenuAnnuelMoyen());
            rad.setNouveauRAM("");

            JADate date = new JADate(getMoisAnnee());

            rad.setAncienMontantPrestation(ra.getMontantPrestation());
            rad.setNouveauMontantPrestation("");
            rad.setAncienMntRenteOrdinaireRempl(ra.getMontantRenteOrdiRemplacee());
            rad.setNouveauMntRenteOrdinaireRempl("");
            rad.setAncienSupplementAjournement(ra.getSupplementAjournement());
            rad.setNouveauSupplementAjournement("");
            rad.setAncienMntReductionAnticipation(ra.getMontantReducationAnticipation());
            rad.setNouveauMntReductionAnticipation("");
            rad.setAncienAnneeMontantRAM(ra.getAnneeMontantRAM());
            rad.setNouveauAnneeMontantRAM(String.valueOf(date.getYear()));
            rad.setIdPrestationAccordee(ra.getIdPrestationAccordee());

            rad.setFractionRente(ra.getFractionRente());
            rad.add(transaction);

        }

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_MAJ_PREST_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getPourcentA() {
        return pourcentA;
    }

    public String getPourcentDe() {
        return pourcentDe;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void listePrestationsAugmentees() throws Exception {

        REListePrstAugmenteesProcess process = new REListePrstAugmenteesProcess();
        process.setSession(getSession());
        process.setEMailAddress(getEMailAddress());
        process.setMoisAnnee(getMoisAnnee());
        process.setIsLstPrestAugManuellement(false);
        process.setIsLstPrestProgrammeCentrale(true);
        process.setIsLstPrestTraitementAutomatique(true);
        process.setIsLstPrestNonAdapte(true);
        process.setIsLstRecapAdaptation(true);
        process.setControleTransaction(true);
        process.executeProcess();

    }

    private Map<String, REPrestAccJointInfoComptaJointTiers> miseAjourDecisisionDecembres(
            final BITransaction transaction,
            final Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale) throws Exception {

        /*
         * Décisions prises en décembre
         * 
         * - Contrôle et revalorisation de la prestation - Plafonnement - Application de la surassurance
         * 
         * - Mise à jour des prestations
         * 
         * Contenu de la mapPrestationsNonTrouveesCentrale : -------------------------------------------------
         * 
         * clé = KeyRAAnnoncesAdaptation valeur = REPrestAccJointInfoComptaJointTiers
         * 
         * Retour : -------- Cette méthode doit retournée toutes les éventuelles rentes contenues dans la map passée en
         * paramètre, mais qui n'ont pas été adaptées par cette méthode.
         */

        Map<String, Object> mapAnnonces4144 = new HashMap<String, Object>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale2 = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();

        JACalendar cal = new JACalendarGregorian();

        // 1) Recherche des annonces de code application 44 et 41 pour le mois rapport 12/2010
        REAnnoncesAugmentationModification10EmeManager mgr10 = new REAnnoncesAugmentationModification10EmeManager();
        mgr10.setSession(getSession());
        mgr10.setForMoisRapport(moisAnneePrecedent);
        // on filtre les codes d'annonces afin de ne pas prendre en compte les annonces ponctuelles
        mgr10.setForCodeAnnonce("44");
        mgr10.find(BManager.SIZE_NOLIMIT);

        for (Iterator iterator = mgr10.iterator(); iterator.hasNext();) {
            REAnnoncesAugmentationModification10Eme ann44 = (REAnnoncesAugmentationModification10Eme) iterator.next();

            REAnnonceRenteManager annRenteMgr = new REAnnonceRenteManager();
            annRenteMgr.setForIdAnnonceHeader(ann44.getIdAnnonce());
            annRenteMgr.setSession(getSession());
            annRenteMgr.find();

            mapAnnonces4144.put(((REAnnonceRente) annRenteMgr.getFirstEntity()).getIdRenteAccordee(), ann44);
        }

        REAnnoncesAugmentationModification9EmeManager mgr9 = new REAnnoncesAugmentationModification9EmeManager();
        mgr9.setSession(getSession());
        mgr9.setForMoisRapport(moisAnneePrecedent);
        // on filtre les codes d'annonces afin de ne pas prendre en compte les annonces ponctuelles
        mgr9.setForCodeAnnonce("41");
        mgr9.find(BManager.SIZE_NOLIMIT);

        for (Iterator iterator = mgr9.iterator(); iterator.hasNext();) {
            REAnnoncesAugmentationModification9Eme ann41 = (REAnnoncesAugmentationModification9Eme) iterator.next();
            REAnnonceRenteManager annRenteMgr = new REAnnonceRenteManager();
            annRenteMgr.setForIdAnnonceHeader(ann41.getIdAnnonce());
            annRenteMgr.setSession(getSession());
            annRenteMgr.find();

            mapAnnonces4144.put(((REAnnonceRente) annRenteMgr.getFirstEntity()).getIdRenteAccordee(), ann41);
        }

        Set set = mapPrestationsNonTrouveesCentrale.keySet();
        Iterator iter = set.iterator();

        Map<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA> annoncesRevalorisees = new HashMap<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA>();
        Map<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA> annoncesRevalorisees05 = new HashMap<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA>();
        Map<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA> annoncesRevalorisees02 = new HashMap<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA>();

        // Pour chacune de ce rentes, rechercher l'annonce correspondante
        while (iter.hasNext()) {

            String keyRA = (String) iter.next();
            String idRA = mapPrestationsNonTrouveesCentrale.get(keyRA).getIdPrestationAccordee();

            // si trouvé
            if (mapAnnonces4144.containsKey(idRA)) {

                // 2) Passage de ces annonces dans le module de revalorisation de la centrale

                REArcConverter converter = new REArcConverter();

                AnnonceAbstraite aa = new Annonce10eme();

                String moisRapport = PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                        .convertDate_MMxAAAA_to_AAAAMM(getMoisAnnee()));

                if (mapAnnonces4144.get(idRA) instanceof REAnnoncesAugmentationModification10Eme) {

                    REAnnoncesAugmentationModification10Eme arc4401 = (REAnnoncesAugmentationModification10Eme) mapAnnonces4144
                            .get(idRA);

                    REAnnoncesAugmentationModification10Eme arc4402 = new REAnnoncesAugmentationModification10Eme();
                    arc4402.setSession(getSession());
                    arc4402.setIdAnnonce(arc4401.getIdLienAnnonce());
                    arc4402.retrieve();

                    aa = converter.convertToAnakinArc(getSession(), arc4401, arc4402, moisRapport);

                } else if (mapAnnonces4144.get(idRA) instanceof REAnnoncesAugmentationModification9Eme) {

                    REAnnoncesAugmentationModification9Eme arc4101 = (REAnnoncesAugmentationModification9Eme) mapAnnonces4144
                            .get(idRA);

                    REAnnoncesAugmentationModification9Eme arc4102 = new REAnnoncesAugmentationModification9Eme();
                    arc4102.setSession(getSession());
                    arc4102.setIdAnnonce(arc4101.getIdLienAnnonce());
                    arc4102.retrieve();

                    aa = converter.convertToAnakinArc(getSession(), arc4101, arc4102, moisRapport);

                }

                // Le contrôleur de la central permettant le calcul
                ControleurAugmentationRente revalorisation = new ControleurAugmentationRente(
                        Logger.getLogger(ControleurAugmentationRente.class.getName()));

                Session.getInstance().setParametre(
                        ControleurAugmentationRente.class.getName(),
                        "dateActuelle",
                        PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                                .convertDate_MMxAAAA_to_AAAAMM(moisAnneePrecedent)));

                AnnonceAbstraite aar = revalorisation.controle(aa);

                // 3) Stockage des annonces révalorisées dans une map
                REAnnoncesCentraleKeyRA annRCentrKeyRA = new REAnnoncesCentraleKeyRA();
                annRCentrKeyRA.setAnnAbsRevalorisee(aar);
                annRCentrKeyRA.setCleRAAnnoncesAdaptation(keyRA);
                annRCentrKeyRA.setIdRA(idRA);

                REKeyAnnoncesRevalorisees keyAnnRev = new REKeyAnnoncesRevalorisees(aar.getNumeroAVS(),
                        aar.getGenrePrestation(), keyRA);

                if (aar.getErreur().getListe().size() > 0) {
                    annRCentrKeyRA.setListeErreurs(aar.getErreur().getListe());

                    StringBuffer erreurs = new StringBuffer();

                    for (Iterator iterator = aar.getErreur().getListe().keySet().iterator(); iterator.hasNext();) {

                        String keyError = (String) iterator.next();
                        AnnonceErreur annErreur = ((AnnonceErreur) aar.getErreur().getListe().get(keyError));

                        if (erreurs.length() > 0) {
                            erreurs.append("\n");
                        }

                        erreurs.append(annErreur.getMessage());

                    }

                    mapPrestationsNonTrouveesCentrale.get(keyRA).setRemarquesAdaptation(erreurs.toString());
                    mapPrestationsNonTrouveesCentrale2.put(keyRA, mapPrestationsNonTrouveesCentrale.get(keyRA));

                } else {
                    if (aar.getAncienCasSpecial(1).equals("05") || aar.getAncienCasSpecial(2).equals("05")
                            || aar.getAncienCasSpecial(3).equals("05") || aar.getAncienCasSpecial(4).equals("05")
                            || aar.getAncienCasSpecial(5).equals("05")) {
                        annoncesRevalorisees05.put(keyAnnRev, annRCentrKeyRA);
                    } else if (aar.getAncienCasSpecial(1).equals("02") || aar.getAncienCasSpecial(2).equals("02")
                            || aar.getAncienCasSpecial(3).equals("02") || aar.getAncienCasSpecial(4).equals("02")
                            || aar.getAncienCasSpecial(5).equals("02")) {
                        annoncesRevalorisees02.put(keyAnnRev, annRCentrKeyRA);
                    } else {
                        annoncesRevalorisees.put(keyAnnRev, annRCentrKeyRA);
                    }
                }

                // Pas trouvé, alors dans la liste des non trouvées pour retour
            } else {

                mapPrestationsNonTrouveesCentrale.get(keyRA).setRemarquesAdaptation(
                        getSession().getLabel("PROCESS_MAJ_PREST_LABEL_5") + " " + idRA);

                mapPrestationsNonTrouveesCentrale2.put(keyRA, mapPrestationsNonTrouveesCentrale.get(keyRA));

            }

        }

        // 4) Parcours des annonces revalorisées avec ccs 5
        AnnonceAbstraite[] aap = new Annonce10eme[2];
        AnnonceAbstraite[] aapOK = new Annonce10eme[2];

        while (!annoncesRevalorisees05.isEmpty()) {

            Set set1 = annoncesRevalorisees05.keySet();
            REKeyAnnoncesRevalorisees key = (REKeyAnnoncesRevalorisees) set1.iterator().next();

            REAnnoncesCentraleKeyRA annCentrKeyRA = annoncesRevalorisees05.get(key);
            AnnonceAbstraite aar = annCentrKeyRA.getAnnAbsRevalorisee();

            aap[0] = aar;
            aap[1] = null;

            List<REKeyAnnoncesRevalorisees> lstPrestRemove = new ArrayList<REKeyAnnoncesRevalorisees>();
            lstPrestRemove.add(key);

            REAnnoncesCentraleKeyRA annCentrKeyRA2;
            REKeyAnnoncesRevalorisees key2 = null;

            // - Recherche de l'autre annonce correspondante (conjoint ou enfant)
            for (REKeyAnnoncesRevalorisees keyTemp : annoncesRevalorisees05.keySet()) {

                key2 = keyTemp;
                annCentrKeyRA2 = annoncesRevalorisees05.get(key2);
                AnnonceAbstraite aar2 = annCentrKeyRA2.getAnnAbsRevalorisee();

                // - Recherche sur premierNSSAyantDroit (si conjoint)
                if (aar2.getNumeroAVSComplementaire1().equals(aar.getNumeroAVS())) {
                    aap[1] = aar2;
                    lstPrestRemove.add(key2);

                    // - Recherche sur deuxièmeNSSAyantDroit (si enfant)
                } else if (aar2.getNumeroAVSComplementaire2().equals(aar.getNumeroAVS())) {
                    aap[1] = aar2;
                    lstPrestRemove.add(key2);

                    // - Recherche sur NssPrincipal (rente enfant)
                } else if (aar2.getNumeroAVS().equals(aar.getNumeroAVS()) && !aar2.equals(aar)) {
                    aap[1] = aar2;
                    lstPrestRemove.add(key2);

                }
            }

            if (aap[1] != null) {
                // - Passage des 2 annonces dans le module de plafonnement
                ControleurAugmentationPlafonnement plafonnement = new ControleurAugmentationPlafonnement();

                Session.getInstance().setParametre(
                        ControleurAugmentationPlafonnement.class.getName(),
                        "dateActuelle",
                        PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                                .convertDate_MMxAAAA_to_AAAAMM(moisAnneePrecedent)));

                aapOK = plafonnement.controle(aap);

                // Mise à jour dans la map
                for (int i = 0; i < aapOK.length; i++) {
                    AnnonceAbstraite aa = aapOK[i];

                    for (Iterator iterator = lstPrestRemove.iterator(); iterator.hasNext();) {
                        REKeyAnnoncesRevalorisees reKeyAnnoncesRevalorisees = (REKeyAnnoncesRevalorisees) iterator
                                .next();

                        if (aa.getNumeroAVS().equals(reKeyAnnoncesRevalorisees.nss)
                                && aa.getGenrePrestation().equals(reKeyAnnoncesRevalorisees.codePrestation)) {

                            REAnnoncesCentraleKeyRA value = annoncesRevalorisees05.get(reKeyAnnoncesRevalorisees);
                            value.setAnnAbsRevalorisee(aa);
                            value.setListeErreurs(aa.getErreur().getListe());

                            // Ajout des annonces dans la map des revalorisées
                            if (value.getListeErreurs().size() > 0) {

                                StringBuffer erreurs = new StringBuffer();

                                for (Iterator iterator2 = aa.getErreur().getListe().keySet().iterator(); iterator2
                                        .hasNext();) {

                                    String keyError = (String) iterator2.next();
                                    AnnonceErreur annErreur = ((AnnonceErreur) aa.getErreur().getListe().get(keyError));

                                    if (erreurs.length() > 0) {
                                        erreurs.append("\n");
                                    }

                                    erreurs.append(annErreur.getMessage());

                                }

                                mapPrestationsNonTrouveesCentrale
                                        .get(reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation).setRemarquesAdaptation(
                                                erreurs.toString());

                                mapPrestationsNonTrouveesCentrale2.put(
                                        reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation,
                                        mapPrestationsNonTrouveesCentrale
                                                .get(reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation));

                            } else {
                                if (aar.getAncienCasSpecial(1).equals("02") || aar.getAncienCasSpecial(2).equals("02")
                                        || aar.getAncienCasSpecial(3).equals("02")
                                        || aar.getAncienCasSpecial(4).equals("02")
                                        || aar.getAncienCasSpecial(5).equals("02")) {
                                    annoncesRevalorisees02.put(reKeyAnnoncesRevalorisees,
                                            annoncesRevalorisees05.get(reKeyAnnoncesRevalorisees));
                                } else {
                                    annoncesRevalorisees.put(reKeyAnnoncesRevalorisees,
                                            annoncesRevalorisees05.get(reKeyAnnoncesRevalorisees));

                                    getMemoryLog().logMessage(
                                            getSession().getLabel("PROCESS_MAJ_PREST_LABEL_1") + " "
                                                    + NSUtil.formatAVSUnknown(reKeyAnnoncesRevalorisees.nss) + " - "
                                                    + reKeyAnnoncesRevalorisees.codePrestation,
                                            FWMessage.AVERTISSEMENT, getName());
                                }
                            }

                            annoncesRevalorisees05.remove(reKeyAnnoncesRevalorisees);
                        }
                    }
                }

                // Pas deux annonces trouvées, alors dans la liste des non trouvées pour retour
            } else {
                mapPrestationsNonTrouveesCentrale2.put(annCentrKeyRA.getCleRAAnnoncesAdaptation(),
                        mapPrestationsNonTrouveesCentrale.get(annCentrKeyRA.getCleRAAnnoncesAdaptation()));

                mapPrestationsNonTrouveesCentrale.get(annCentrKeyRA.getCleRAAnnoncesAdaptation())
                        .setRemarquesAdaptation(getSession().getLabel("PROCESS_MAJ_PREST_LABEL_2"));

                annoncesRevalorisees05.remove(key);
            }

        }

        // 6) Parcours des annonces revalorisées avec ccs 02
        AnnonceAbstraite[] aas;
        AnnonceAbstraite[] aasOK;

        while (!annoncesRevalorisees02.isEmpty()) {

            Set set1 = annoncesRevalorisees02.keySet();
            REKeyAnnoncesRevalorisees key = (REKeyAnnoncesRevalorisees) set1.iterator().next();

            REAnnoncesCentraleKeyRA annCentrKeyRA = annoncesRevalorisees02.get(key);
            AnnonceAbstraite aar = annCentrKeyRA.getAnnAbsRevalorisee();

            List<REKeyAnnoncesRevalorisees> lstPrestRemove = new ArrayList<REKeyAnnoncesRevalorisees>();
            lstPrestRemove.add(key);

            REAnnoncesCentraleKeyRA annCentrKeyRA2;
            REKeyAnnoncesRevalorisees key2;

            // - Recherche des autres annonces correspondantes (enfants)
            for (REKeyAnnoncesRevalorisees keyTemp : annoncesRevalorisees02.keySet()) {

                key2 = keyTemp;
                annCentrKeyRA2 = annoncesRevalorisees02.get(key2);
                AnnonceAbstraite aar2 = annCentrKeyRA2.getAnnAbsRevalorisee();

                // - Recherche sur premierNSSAyantDroit
                if (aar2.getNumeroAVSComplementaire1().equals(aar.getNumeroAVSComplementaire1()) && !aar2.equals(aar)) {
                    lstPrestRemove.add(key2);
                }
            }

            aas = new Annonce10eme[20];
            aasOK = new Annonce10eme[20];

            if (lstPrestRemove.size() > 1) {

                int i = 0;
                for (Iterator iterator = lstPrestRemove.iterator(); iterator.hasNext();) {
                    REKeyAnnoncesRevalorisees reKeyAnnoncesRevalorisees = (REKeyAnnoncesRevalorisees) iterator.next();

                    REAnnoncesCentraleKeyRA obj = annoncesRevalorisees02.get(reKeyAnnoncesRevalorisees);

                    if (JadeStringUtil.isBlank(obj.getAnnAbsRevalorisee().getNumeroAVSFamille())) {
                        obj.getAnnAbsRevalorisee().setNumeroAVSFamille("0");
                    }

                    aas[i] = obj.getAnnAbsRevalorisee();
                    i++;
                }

                // - Passage de toutes les annonces dans le module de sur assurance
                ControleurAugmentationSurAssurance surassurance = new ControleurAugmentationSurAssurance();
                Session.getInstance().setParametre(
                        ControleurAugmentationSurAssurance.class.getName(),
                        "dateActuelle",
                        PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                                .convertDate_MMxAAAA_to_AAAAMM(moisAnneePrecedent)));

                boolean isErreurSurAss = false;
                AnnonceErreur annErr = null;

                try {

                    aasOK = surassurance.controle(aas);

                } catch (Exception e) {

                    // Erreurs dans le module de surassurance
                    aasOK = aas;
                    isErreurSurAss = true;
                    annErr = new AnnonceErreur(e);

                }

                // Mise à jour dans la map
                for (int j = 0; j < lstPrestRemove.size(); j++) {
                    AnnonceAbstraite aa = aasOK[j];

                    REKeyAnnoncesRevalorisees cle = lstPrestRemove.get(j);
                    REAnnoncesCentraleKeyRA value = annoncesRevalorisees02.get(cle);
                    value.setAnnAbsRevalorisee(aa);

                    if (isErreurSurAss) {
                        Hashtable<String, AnnonceErreur> errorMan = new Hashtable<String, AnnonceErreur>();
                        errorMan.put("", annErr);
                        aa.getErreur().setListe(errorMan);
                    }

                    value.setListeErreurs(aa.getErreur().getListe());
                }

                // Ajout des annonces dans la map des revalorisées
                for (Iterator iterator = lstPrestRemove.iterator(); iterator.hasNext();) {
                    REKeyAnnoncesRevalorisees reKeyAnnoncesRevalorisees = (REKeyAnnoncesRevalorisees) iterator.next();

                    REAnnoncesCentraleKeyRA value = annoncesRevalorisees02.get(reKeyAnnoncesRevalorisees);
                    if (value.getListeErreurs().size() > 0) {

                        StringBuffer erreurs = new StringBuffer();

                        for (Iterator iterator2 = value.getListeErreurs().keySet().iterator(); iterator2.hasNext();) {

                            String keyError = (String) iterator2.next();
                            AnnonceErreur annErreur = ((AnnonceErreur) value.getListeErreurs().get(keyError));

                            if (erreurs.length() > 0) {
                                erreurs.append("\n");
                            }

                            erreurs.append(annErreur.getMessage());

                        }

                        mapPrestationsNonTrouveesCentrale.get(reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation)
                                .setRemarquesAdaptation(erreurs.toString());

                        mapPrestationsNonTrouveesCentrale2.put(reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation,
                                mapPrestationsNonTrouveesCentrale
                                        .get(reKeyAnnoncesRevalorisees.cleRAAnnoncesAdaptation));

                    } else {

                        annoncesRevalorisees.put(reKeyAnnoncesRevalorisees,
                                annoncesRevalorisees02.get(reKeyAnnoncesRevalorisees));

                        getMemoryLog().logMessage(
                                getSession().getLabel("PROCESS_MAJ_PREST_LABEL_3") + " "
                                        + NSUtil.formatAVSUnknown(reKeyAnnoncesRevalorisees.nss) + " - "
                                        + reKeyAnnoncesRevalorisees.codePrestation, FWMessage.AVERTISSEMENT, getName());
                    }

                    annoncesRevalorisees02.remove(reKeyAnnoncesRevalorisees);

                }

                // - Si aucune correspondance, retourner pour liste non augmentées
            } else {
                mapPrestationsNonTrouveesCentrale2.put(annCentrKeyRA.getCleRAAnnoncesAdaptation(),
                        mapPrestationsNonTrouveesCentrale.get(annCentrKeyRA.getCleRAAnnoncesAdaptation()));

                mapPrestationsNonTrouveesCentrale.get(annCentrKeyRA.getCleRAAnnoncesAdaptation())
                        .setRemarquesAdaptation(getSession().getLabel("PROCESS_MAJ_PREST_LABEL_4"));

                annoncesRevalorisees02.remove(key);
            }

        }

        // 8) Mise à jour des prestations selon map des annonces
        miseAjourPrestProgCentrale(transaction, annoncesRevalorisees);

        // 9) Retour des prestations non augmentées
        mapPrestationsNonTrouveesCentrale.clear();
        mapPrestationsNonTrouveesCentrale.putAll(mapPrestationsNonTrouveesCentrale2);

        return mapPrestationsNonTrouveesCentrale;
    }

    private void miseAjourPrestation(final BITransaction transaction,
            final Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse) throws Exception {

        /*
         * Rechercher toutes les rentes qui sont en cours et qui ne sont pas en erreur => Pour cela, on reprend le
         * contenu de la liste des différences (liste des erreurs) qui contient toutes les rentes à mettre à jour
         * 
         * => La clé de la map est l'id de la rente accordée
         */

        // Stockage des changements dans la table correspondante
        RERentesAdaptees rad = null;

        long progressCounter = 0;
        int maxScale = mapDifferencesCentraleCaisse.size();
        if (maxScale > 0) {
            setProgressScaleValue(maxScale);
        } else {
            setProgressScaleValue(1);
        }

        // Pour chacune de ces rentes :
        for (String idRA : mapDifferencesCentraleCaisse.keySet()) {

            setProgressCounter(progressCounter++);

            rad = new RERentesAdaptees();
            rad.setSession(getSession());
            rad.setCsTypeAdaptation(IREAdaptationRente.CS_TYPE_AUG_ADAPTATION_AUTO);

            ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(idRA);
            REPrestAccJointInfoComptaJointTiers raP = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

            rad.setCodePrestation(raP.getCodePrestation());

            // Stockage NSS, Nom, Prénom selon type de rente
            String genreRente = raP.getCodePrestation();
            if (REGenresPrestations.GENRE_10.equals(genreRente) || REGenresPrestations.GENRE_13.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_50.equals(genreRente)
                    || REGenresPrestations.GENRE_70.equals(genreRente)
                    || REGenresPrestations.GENRE_72.equals(genreRente)
                    || REGenresPrestations.GENRE_81.equals(genreRente)
                    || REGenresPrestations.GENRE_82.equals(genreRente)
                    || REGenresPrestations.GENRE_83.equals(genreRente)
                    || REGenresPrestations.GENRE_84.equals(genreRente)
                    || REGenresPrestations.GENRE_85.equals(genreRente)
                    || REGenresPrestations.GENRE_86.equals(genreRente)
                    || REGenresPrestations.GENRE_87.equals(genreRente)
                    || REGenresPrestations.GENRE_88.equals(genreRente)
                    || REGenresPrestations.GENRE_91.equals(genreRente)
                    || REGenresPrestations.GENRE_92.equals(genreRente)
                    || REGenresPrestations.GENRE_93.equals(genreRente)
                    || REGenresPrestations.GENRE_94.equals(genreRente)
                    || REGenresPrestations.GENRE_95.equals(genreRente)
                    || REGenresPrestations.GENRE_96.equals(genreRente)
                    || REGenresPrestations.GENRE_97.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_89.equals(genreRente)) {
                rad.setNssTri(raP.getNss());
                rad.setNomTri(raP.getNom());
                rad.setPrenomTri(raP.getPrenom());
            } else {

                String nssAnnonceCompl1 = "";

                if (listRaAnn.get(0) instanceof REAnnonce51Adaptation) {
                    REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) listRaAnn.get(0);
                    nssAnnonceCompl1 = ann51.getPremierNSSCompl();

                } else {
                    REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) listRaAnn.get(0);
                    nssAnnonceCompl1 = ann53.getPremierNSSCompl();

                }

                PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(), NSUtil.formatAVSUnknown(nssAnnonceCompl1));
                if (tier != null) {
                    rad.setNssTri(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    rad.setNomTri(tier.getProperty(PRTiersWrapper.PROPERTY_NOM));
                    rad.setPrenomTri(tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                } else {
                    rad.setNssTri(raP.getNss());
                    rad.setNomTri(raP.getNom());
                    rad.setPrenomTri(raP.getPrenom());
                }
            }

            // Mise à jour des bases de calcul
            // - RAM
            // - Ancien RAM sans BTE (pas stocké)
            // - Ancien montant sans BTE (BC 9ème)
            String RAMpourRA = "";
            String ancienMontantBTEPourRA = "";

            JADate date = new JADate(getMoisAnnee());

            IREAnnonceAdaptation annonceAdaptation = (IREAnnonceAdaptation) listRaAnn.get(0);

            if (annonceAdaptation instanceof REAnnonce51Adaptation) {
                REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) annonceAdaptation;

                // Si rentes à zéro, sortir dans le mail pour l'instant
                if (!codeCasSpecialPresent("07", annonceAdaptation) && !codeCasSpecialPresent("08", annonceAdaptation)
                        && JadeStringUtil.isBlankOrZero(ann51.getMontantPrestation())) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("ERREUR_MAJ_PRST_MONTANT_ZERO") + " "
                                    + NSUtil.formatAVSUnknown(ann51.getNss()), FWMessage.AVERTISSEMENT, getName());
                }

                RAMpourRA = new FWCurrency(ann51.getRAM()).toString();
                ancienMontantBTEPourRA = new FWCurrency(ann51.getAncienMontantBTE()).toString();
            } else {
                REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) annonceAdaptation;

                // Si rentes à zéro, sortir dans le mail pour l'instant
                if (!codeCasSpecialPresent("07", annonceAdaptation) && !codeCasSpecialPresent("08", annonceAdaptation)
                        && JadeStringUtil.isBlankOrZero(ann53.getMontantPrestation())) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("ERREUR_MAJ_PRST_MONTANT_ZERO") + " "
                                    + NSUtil.formatAVSUnknown(ann53.getNss()), FWMessage.AVERTISSEMENT, getName());
                }

                RAMpourRA = new FWCurrency(ann53.getRAM()).toString();
            }

            // Recherche de la ra 9ème
            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
            bc9.setSession(getSession());
            bc9.setIdBasesCalcul(raP.getIdBaseCalcul());
            bc9.retrieve();

            if (bc9.isNew()) {
                REBasesCalcul bc10 = new REBasesCalcul();
                bc10.setSession(getSession());
                bc10.setIdBasesCalcul(raP.getIdBaseCalcul());
                bc10.retrieve();

                // 10ème
                REDemandeRente demande = new REDemandeRente();
                demande.setSession(getSession());
                demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demande.setIdRenteCalculee(bc10.getIdRenteCalculee());
                demande.retrieve();

                if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                    bc10.setIsDemandeRenteAPI(true);
                }

                rad.setAncienRAM(bc10.getRevenuAnnuelMoyen());
                rad.setNouveauRAM(RAMpourRA);

                bc10.setRevenuAnnuelMoyen(RAMpourRA);
                bc10.setAnneeTraitement(String.valueOf(date.getYear()));
                bc10.update(transaction);

            } else {

                // 9ème
                REDemandeRente demande = new REDemandeRente();
                demande.setSession(getSession());
                demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demande.setIdRenteCalculee(bc9.getIdRenteCalculee());
                demande.retrieve();

                if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                    bc9.setIsDemandeRenteAPI(true);
                }

                rad.setAncienRAM(bc9.getRevenuAnnuelMoyen());
                rad.setNouveauRAM(RAMpourRA);

                bc9.setRevenuAnnuelMoyen(RAMpourRA);
                bc9.setBonificationTacheEducative(ancienMontantBTEPourRA);
                bc9.setAnneeTraitement(String.valueOf(date.getYear()));
                bc9.update(transaction);

            }

            // Mise à jour des rentes accordées
            // - Montant prestation
            // - Montant rente ordinaire remplacée
            // - Supplément ajournement
            // - Montant réduction pour anticipation
            // - Année montant RAM
            // - Codes cas spéciaux
            String montantPrestPourRA = "";
            String montantRenteOrdRempPourRA = "";
            String montantSuppAjourPourRA = "";
            String dureeAjournementPourRA = "";
            String dateRevocationAjourPourRA = "";
            String montantRedAntiPourRA = "";
            String anneeMontantRAMPourRA = "";
            String ccs1PourRA = "";
            String ccs2PourRA = "";
            String ccs3PourRA = "";
            String ccs4PourRA = "";
            String ccs5PourRA = "";

            if (listRaAnn.get(0) instanceof REAnnonce51Adaptation) {
                REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) listRaAnn.get(0);

                montantPrestPourRA = new FWCurrency(ann51.getMontantPrestation()).toString();
                montantRenteOrdRempPourRA = new FWCurrency(ann51.getMntRenteOrdRemplacee()).toString();
                montantSuppAjourPourRA = new FWCurrency(ann51.getMntSuppAjournement()).toString();
                dureeAjournementPourRA = ann51.getDureeAjournement();
                dateRevocationAjourPourRA = ann51.getDateRevocationAjour();
                anneeMontantRAMPourRA = String.valueOf(date.getYear());
                ccs1PourRA = ann51.getCodeCasSpecial1();
                ccs2PourRA = ann51.getCodeCasSpecial2();
                ccs3PourRA = ann51.getCodeCasSpecial3();
                ccs4PourRA = ann51.getCodeCasSpecial4();
                ccs5PourRA = ann51.getCodeCasSpecial5();

            } else {
                REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) listRaAnn.get(0);

                montantPrestPourRA = new FWCurrency(ann53.getMontantPrestation()).toString();
                montantSuppAjourPourRA = new FWCurrency(ann53.getMntSuppAjournement()).toString();
                dureeAjournementPourRA = ann53.getDureeAjournement();
                dateRevocationAjourPourRA = ann53.getDateRevocationAjour();
                montantRedAntiPourRA = new FWCurrency(ann53.getMntReducAnticipation()).toString();
                anneeMontantRAMPourRA = String.valueOf(date.getYear());
                ccs1PourRA = ann53.getCodeCasSpecial1();
                ccs2PourRA = ann53.getCodeCasSpecial2();
                ccs3PourRA = ann53.getCodeCasSpecial3();
                ccs4PourRA = ann53.getCodeCasSpecial4();
                ccs5PourRA = ann53.getCodeCasSpecial5();

            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(raP.getIdPrestationAccordee());
            ra.retrieve();

            rad.setAncienMontantPrestation(ra.getMontantPrestation());
            rad.setNouveauMontantPrestation(montantPrestPourRA);
            rad.setAncienMntRenteOrdinaireRempl(ra.getMontantRenteOrdiRemplacee());
            rad.setNouveauMntRenteOrdinaireRempl(montantRenteOrdRempPourRA);
            rad.setAncienSupplementAjournement(ra.getSupplementAjournement());
            rad.setNouveauSupplementAjournement(montantSuppAjourPourRA);
            rad.setAncienMntReductionAnticipation(ra.getMontantReducationAnticipation());
            rad.setNouveauMntReductionAnticipation(montantRedAntiPourRA);
            rad.setAncienAnneeMontantRAM(ra.getAnneeMontantRAM());
            rad.setNouveauAnneeMontantRAM(anneeMontantRAMPourRA);
            rad.setIdPrestationAccordee(ra.getIdPrestationAccordee());
            rad.setFractionRente(ra.getFractionRente());

            rad.add(transaction);

            ra.setMontantPrestation(montantPrestPourRA);
            ra.setMontantRenteOrdiRemplacee(montantRenteOrdRempPourRA);

            if (!JadeStringUtil.isBlankOrZero(montantSuppAjourPourRA)) {
                ra.setSupplementAjournement(montantSuppAjourPourRA);
                ra.setDureeAjournement(dureeAjournementPourRA);
                ra.setDateRevocationAjournement(PRDateFormater.convertDate_MMAA_to_MMxAAAA(dateRevocationAjourPourRA));
            }

            ra.setMontantReducationAnticipation(montantRedAntiPourRA);
            ra.setAnneeMontantRAM(anneeMontantRAMPourRA);
            ra.setCodeCasSpeciaux1(ccs1PourRA);
            ra.setCodeCasSpeciaux2(ccs2PourRA);
            ra.setCodeCasSpeciaux3(ccs3PourRA);
            ra.setCodeCasSpeciaux4(ccs4PourRA);
            ra.setCodeCasSpeciaux5(ccs5PourRA);
            ra.update(transaction);

            // Mise à jour des prestations dues/versées
            // - Ajouter date de fin à la période en cours
            REPrestationsDuesManager prstduMgr = new REPrestationsDuesManager();
            prstduMgr.setSession(getSession());
            prstduMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
            prstduMgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
            prstduMgr.setForPeriodePDInMoisAnnee(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(moisAnneePrecedent));
            prstduMgr.find();

            if (prstduMgr.isEmpty() || (prstduMgr.size() > 1)) {
                exceptionErreurMiseAjourPrestation(rad, ra);
            } else {
                REPrestationDue pd = (REPrestationDue) prstduMgr.getFirstEntity();

                // si date de fin, créer seulement la nouvelle période
                if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                    pd.setDateFinPaiement(moisAnneePrecedent);
                    pd.update();
                }

                // - Création d'une nouvelle période avec :
                // - Date début paiement
                // - RAM
                // - Supplément ajournement
                // - Réduction anticipation
                // - Montant
                // - Etat (partiel)
                // - Date de fin (vide)
                // - Taux réduction anticipation (reprendre idem précédent)
                // - Type (paiement mensuel)
                REPrestationDue newPd = new REPrestationDue();
                newPd.setSession(getSession());
                newPd.setDateDebutPaiement(getMoisAnnee());

                newPd.setRam(RAMpourRA);
                newPd.setMontantSupplementAjournement(ra.getSupplementAjournement());
                newPd.setMontantReductionAnticipation(ra.getMontantReducationAnticipation());
                newPd.setMontant(ra.getMontantPrestation());
                newPd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                newPd.setDateFinPaiement("");
                newPd.setTauxReductionAnticipation(pd.getTauxReductionAnticipation());
                newPd.setCsType(pd.getCsType());
                newPd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                newPd.add(transaction);

            }

            // Ajout dans la map
            prestationsAugmentees.put(ra.getIdPrestationAccordee(), ra);

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(
                        "Adaptation en erreur : idTiers = " + ra.getIdTiersBeneficiaire() + " - IDRA = "
                                + rad.getIdPrestationAccordee() + " Erreur : " + transaction.getErrors().toString(),
                        FWMessage.ERREUR, getName());
                /**
                 * ATTENTION, la méthode clearErrorBuffer remet le flag 'rollbackOnly' à false !!!!
                 * Il faut donc en premier effacer le message et ensuite remettre le flag 'rallbackOnly' à TRUE
                 */
                ((BTransaction) transaction).clearErrorBuffer();
                transaction.setRollbackOnly();
            }
        }
    }

    private void exceptionErreurMiseAjourPrestation(RERentesAdaptees rad, RERenteAccordee ra) throws Exception {
        String infoAssure = " id rente: " + ra.getIdPrestationAccordee();
        if (rad.getNssTri() != null && !JadeStringUtil.isBlankOrZero(rad.getNssTri())) {
            infoAssure += " - Nss: " + rad.getNssTri();
        }
        throw new Exception(getSession().getLabel("ERREUR_MAJ_PRST_DU_ZERO_PLUS") + infoAssure);
    }

    private boolean codeCasSpecialPresent(String codeCasSpecial, IREAnnonceAdaptation annonceAdaptation) {
        return codeCasSpecial.equals(annonceAdaptation.getCodeCasSpecial1())
                || codeCasSpecial.equals(annonceAdaptation.getCodeCasSpecial2())
                || codeCasSpecial.equals(annonceAdaptation.getCodeCasSpecial3())
                || codeCasSpecial.equals(annonceAdaptation.getCodeCasSpecial4())
                || codeCasSpecial.equals(annonceAdaptation.getCodeCasSpecial5());
    }

    private void miseAjourPrestProgCentrale(final BITransaction transaction,
            final Map<REKeyAnnoncesRevalorisees, REAnnoncesCentraleKeyRA> annoncesRevalorisees) throws Exception {

        // Stockage des changements dans la table correspondante
        RERentesAdaptees rad = null;

        // Pour chacune de ce rentes :
        for (REKeyAnnoncesRevalorisees key : annoncesRevalorisees.keySet()) {

            rad = new RERentesAdaptees();
            rad.setSession(getSession());
            rad.setCsTypeAdaptation(IREAdaptationRente.CS_TYPE_AUG_DECISIONS_DECEMBRE);

            REAnnoncesCentraleKeyRA annCentraleKeyRa = annoncesRevalorisees.get(key);

            String idRA = annCentraleKeyRa.getIdRA();

            REPrestAccJointInfoComptaJointTiersManager mgr = new REPrestAccJointInfoComptaJointTiersManager();
            mgr.setSession(getSession());
            mgr.setForIdRenteAccordee(idRA);
            mgr.find();

            if (mgr.isEmpty()) {
                throw new Exception("Rente accordée non trouvée pour idRA = " + idRA);
            } else {

            }

            REPrestAccJointInfoComptaJointTiers raP = (REPrestAccJointInfoComptaJointTiers) mgr.getFirstEntity();

            rad.setCodePrestation(raP.getCodePrestation());

            // Stockage NSS, Nom, Prénom selon type de rente
            String genreRente = raP.getCodePrestation();
            if (REGenresPrestations.GENRE_10.equals(genreRente) || REGenresPrestations.GENRE_13.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_50.equals(genreRente)
                    || REGenresPrestations.GENRE_70.equals(genreRente)
                    || REGenresPrestations.GENRE_72.equals(genreRente)
                    || REGenresPrestations.GENRE_81.equals(genreRente)
                    || REGenresPrestations.GENRE_82.equals(genreRente)
                    || REGenresPrestations.GENRE_83.equals(genreRente)
                    || REGenresPrestations.GENRE_84.equals(genreRente)
                    || REGenresPrestations.GENRE_85.equals(genreRente)
                    || REGenresPrestations.GENRE_86.equals(genreRente)
                    || REGenresPrestations.GENRE_87.equals(genreRente)
                    || REGenresPrestations.GENRE_88.equals(genreRente)
                    || REGenresPrestations.GENRE_91.equals(genreRente)
                    || REGenresPrestations.GENRE_92.equals(genreRente)
                    || REGenresPrestations.GENRE_93.equals(genreRente)
                    || REGenresPrestations.GENRE_94.equals(genreRente)
                    || REGenresPrestations.GENRE_95.equals(genreRente)
                    || REGenresPrestations.GENRE_96.equals(genreRente)
                    || REGenresPrestations.GENRE_97.equals(genreRente)
                    || REGenresPrestations.GENRE_20.equals(genreRente)
                    || REGenresPrestations.GENRE_23.equals(genreRente)
                    || REGenresPrestations.GENRE_89.equals(genreRente)) {
                rad.setNssTri(raP.getNss());
                rad.setNomTri(raP.getNom());
                rad.setPrenomTri(raP.getPrenom());
            } else {
                PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), raP.getIdTiersComplementaire1());
                if (tier != null) {
                    rad.setNssTri(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    rad.setNomTri(tier.getProperty(PRTiersWrapper.PROPERTY_NOM));
                    rad.setPrenomTri(tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                } else {
                    rad.setNssTri(raP.getNss());
                    rad.setNomTri(raP.getNom());
                    rad.setPrenomTri(raP.getPrenom());
                }
            }

            // Mise à jour des bases de calcul
            // - RAM
            // - Ancien RAM sans BTE (pas stocké)
            // - Ancien montant sans BTE (BC 9ème)
            String RAMpourRA = "";
            String ancienMontantBTEPourRA = "";

            JADate date = new JADate(getMoisAnnee());

            boolean is9eme = false;

            if (annCentraleKeyRa.getAnnAbsRevalorisee() instanceof Annonce9eme) {
                Annonce9eme ann51 = (Annonce9eme) annCentraleKeyRa.getAnnAbsRevalorisee();

                RAMpourRA = ann51.getRevenuAnnuelMoyen();
                ancienMontantBTEPourRA = ann51.getAncienMontantBonification();

                is9eme = true;
            } else {
                Annonce10eme ann53 = (Annonce10eme) annCentraleKeyRa.getAnnAbsRevalorisee();

                RAMpourRA = ann53.getRevenuAnnuelMoyen();
            }

            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
            bc9.setSession(getSession());
            bc9.setIdBasesCalcul(raP.getIdBaseCalcul());
            bc9.retrieve();

            if (bc9.isNew()) {

                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(raP.getIdBaseCalcul());
                bc.retrieve();

                REDemandeRente demande = new REDemandeRente();
                demande.setSession(getSession());
                demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demande.setIdRenteCalculee(bc.getIdRenteCalculee());
                demande.retrieve();

                if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                    bc.setIsDemandeRenteAPI(true);
                }

                rad.setAncienRAM(bc.getRevenuAnnuelMoyen());
                rad.setNouveauRAM(RAMpourRA);

                bc.setAnneeTraitement(String.valueOf(date.getYear()));
                bc.setRevenuAnnuelMoyen(RAMpourRA);
                bc.update(transaction);

            } else {

                // 9ème
                REDemandeRente demande = new REDemandeRente();
                demande.setSession(getSession());
                demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demande.setIdRenteCalculee(bc9.getIdRenteCalculee());
                demande.retrieve();

                if (demande.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                    bc9.setIsDemandeRenteAPI(true);
                }

                rad.setAncienRAM(bc9.getRevenuAnnuelMoyen());
                rad.setNouveauRAM(RAMpourRA);

                bc9.setRevenuAnnuelMoyen(RAMpourRA);
                bc9.setAnneeTraitement(String.valueOf(date.getYear()));
                bc9.setBonificationTacheEducative(ancienMontantBTEPourRA);
                bc9.update(transaction);

            }

            // Mise à jour des rentes accordées
            // - Montant prestation
            // - Montant rente ordinaire remplacée
            // - Supplément ajournement
            // - Montant réduction pour anticipation
            // - Année montant RAM
            // - Codes cas spéciaux
            String montantPrestPourRA = "";
            String montantRenteOrdRempPourRA = "";
            String montantSuppAjourPourRA = "";
            String dureeAjournementPourRA = "";
            String dateRevocationAjourPourRA = "";
            String montantRedAntiPourRA = "";
            String anneeMontantRAMPourRA = "";
            String ccs1PourRA = "";
            String ccs2PourRA = "";
            String ccs3PourRA = "";
            String ccs4PourRA = "";
            String ccs5PourRA = "";

            if (annCentraleKeyRa.getAnnAbsRevalorisee() instanceof Annonce9eme) {
                Annonce9eme ann51 = (Annonce9eme) annCentraleKeyRa.getAnnAbsRevalorisee();

                montantPrestPourRA = ann51.getMontantPrestation();
                montantRenteOrdRempPourRA = ann51.getMontantRenteOrdinaireRemplacee();
                montantSuppAjourPourRA = ann51.getSupplementAjournement();
                dureeAjournementPourRA = ann51.getDureeAjournement();
                dateRevocationAjourPourRA = ann51.getDateRevocation();
                anneeMontantRAMPourRA = String.valueOf(date.getYear());
                ccs1PourRA = ann51.getCasSpecial(1);
                ccs2PourRA = ann51.getCasSpecial(2);
                ccs3PourRA = ann51.getCasSpecial(3);
                ccs4PourRA = ann51.getCasSpecial(4);
                ccs5PourRA = ann51.getCasSpecial(5);

            } else {
                Annonce10eme ann53 = (Annonce10eme) annCentraleKeyRa.getAnnAbsRevalorisee();

                montantPrestPourRA = ann53.getMontantPrestation();
                montantSuppAjourPourRA = ann53.getSupplementAjournement();
                dureeAjournementPourRA = ann53.getDureeAjournement();
                dateRevocationAjourPourRA = ann53.getDateRevocation();
                montantRedAntiPourRA = ann53.getReductionAnticipation();
                anneeMontantRAMPourRA = String.valueOf(date.getYear());
                ccs1PourRA = ann53.getCasSpecial(1);
                ccs2PourRA = ann53.getCasSpecial(2);
                ccs3PourRA = ann53.getCasSpecial(3);
                ccs4PourRA = ann53.getCasSpecial(4);
                ccs5PourRA = ann53.getCasSpecial(5);

            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(raP.getIdPrestationAccordee());
            ra.retrieve();

            rad.setAncienMontantPrestation(ra.getMontantPrestation());
            rad.setNouveauMontantPrestation(montantPrestPourRA);
            rad.setAncienMntRenteOrdinaireRempl(ra.getMontantRenteOrdiRemplacee());
            rad.setNouveauMntRenteOrdinaireRempl(montantRenteOrdRempPourRA);
            rad.setAncienSupplementAjournement(ra.getSupplementAjournement());
            rad.setNouveauSupplementAjournement(montantSuppAjourPourRA);
            rad.setAncienMntReductionAnticipation(ra.getMontantReducationAnticipation());
            rad.setNouveauMntReductionAnticipation(montantRedAntiPourRA);
            rad.setAncienAnneeMontantRAM(ra.getAnneeMontantRAM());
            rad.setNouveauAnneeMontantRAM(anneeMontantRAMPourRA);
            rad.setIdPrestationAccordee(ra.getIdPrestationAccordee());
            rad.setFractionRente(ra.getFractionRente());

            rad.add(transaction);

            ra.setMontantPrestation(montantPrestPourRA);
            ra.setMontantRenteOrdiRemplacee(montantRenteOrdRempPourRA);

            if (!JadeStringUtil.isBlankOrZero(montantSuppAjourPourRA)) {
                ra.setSupplementAjournement(montantSuppAjourPourRA);
                ra.setDureeAjournement(dureeAjournementPourRA);
                ra.setDateRevocationAjournement(PRDateFormater.convertDate_MMAA_to_MMxAAAA(dateRevocationAjourPourRA));
            }

            ra.setMontantReducationAnticipation(montantRedAntiPourRA);
            ra.setAnneeMontantRAM(anneeMontantRAMPourRA);
            ra.setCodeCasSpeciaux1(ccs1PourRA);
            ra.setCodeCasSpeciaux2(ccs2PourRA);
            ra.setCodeCasSpeciaux3(ccs3PourRA);
            ra.setCodeCasSpeciaux4(ccs4PourRA);
            ra.setCodeCasSpeciaux5(ccs5PourRA);
            ra.update(transaction);

            // Mise à jour des prestations dues/versées
            // - Ajouter date de fin à la période en cours
            REPrestationsDuesManager prstduMgr = new REPrestationsDuesManager();
            prstduMgr.setSession(getSession());
            prstduMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
            prstduMgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
            prstduMgr.setForPeriodePDInMoisAnnee(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(moisAnneePrecedent));
            prstduMgr.find();

            if (prstduMgr.isEmpty() || (prstduMgr.size() > 1)) {
                exceptionErreurMiseAjourPrestation(rad, ra);
            } else {
                REPrestationDue pd = (REPrestationDue) prstduMgr.getFirstEntity();

                // si date de fin, créer seulement la nouvelle période
                if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                    pd.setDateFinPaiement(moisAnneePrecedent);
                    pd.update();
                }

                // - Création d'une nouvelle période avec :
                // - Date début paiement
                // - RAM
                // - Supplément ajournement
                // - Réduction anticipation
                // - Montant
                // - Etat (partiel)
                // - Date de fin (vide)
                // - Taux réduction anticipation (reprendre idem précédent)
                // - Type (paiement mensuel)
                REPrestationDue newPd = new REPrestationDue();
                newPd.setSession(getSession());
                newPd.setDateDebutPaiement(getMoisAnnee());

                newPd.setRam(RAMpourRA);
                newPd.setMontantSupplementAjournement(ra.getSupplementAjournement());
                newPd.setMontantReductionAnticipation(ra.getMontantReducationAnticipation());
                newPd.setMontant(ra.getMontantPrestation());
                newPd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                newPd.setDateFinPaiement("");
                newPd.setTauxReductionAnticipation(pd.getTauxReductionAnticipation());
                newPd.setCsType(pd.getCsType());
                newPd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                newPd.add(transaction);

            }

            // Ajout dans la map
            prestationsAugmentees.put(ra.getIdPrestationAccordee(), ra);

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(
                        "Adaptation en erreur : NSS = " + ra.getIdTiersBeneficiaire() + " - IDRA = "
                                + rad.getIdPrestationAccordee(), FWMessage.ERREUR, getName());
            }
        }
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setPourcentA(final String pourcentA) {
        this.pourcentA = pourcentA;
    }

    public void setPourcentDe(final String pourcentDe) {
        this.pourcentDe = pourcentDe;
    }

}
