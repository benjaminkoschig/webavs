package globaz.ij.acorweb.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.eavs.utils.StringUtils;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JAVector;
import globaz.ij.acorweb.mapper.IJDecompteMapper;
import globaz.ij.acorweb.mapper.IJIJCalculeeMapper;
import globaz.ij.acorweb.mapper.IJIJIndemniteJournaliereMapper;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.*;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.helpers.acor.IJRevision;
import globaz.ij.helpers.acor.IJRevisions;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrononceRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.util.ArrayList;
import java.util.List;

class IJImportationCalculAcor {
    private final EntityService entityService;

    public IJImportationCalculAcor() {
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    /**
     * Importation des données par ACOR lors d'un calcul d'indemnité journalière
     * @param idPrononce : id du prononce fourni par WebAVS à ACOR
     * @param fCalcul : Feuille de calcul de l'indemnité journalière
     */
    void importCalculAcor(String idPrononce, FCalcul fCalcul) {
        IJPrononce prononce;
        try {
            // Chargement des informations du prononcé correspondant à la demande de calcul Acor
            // dans WebAVS
            prononce = entityService.load(IJPrononce.class, idPrononce);
            if (prononce == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le prononcé du calcul. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        // Récupère le NSS du FCalcul reçu d'ACOR
        String nss = checkAndGetNssIntegrite(fCalcul, prononce.getIdDemande());

        List<IJIJCalculee> ijCalculees = new ArrayList<>();
        IJIJCalculeeMapper mapperIJCalculee = new IJIJCalculeeMapper(nss, prononce, entityService);

        // Mapping des données liées aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = mapperIJCalculee.map(baseCalcul);
                IJIJIndemniteJournaliereMapper mapperIndemniteJournaliere = new IJIJIndemniteJournaliereMapper(ijCalculee,entityService);
                mapperIndemniteJournaliere.map(baseCalcul);
                ijCalculees.add(ijCalculee);
            }
        }
        // restituer ou annuler le prononcé d'origine si celui-ci est une
        // correction
        RestitutionCorrectionPrononceOrigine(prononce, ijCalculees);
        UpdateEtatPrononce(prononce);
    }

    private void RestitutionCorrectionPrononceOrigine(IJPrononce prononce, List<IJIJCalculee> ijCalculees) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(prononce.getIdCorrection())) {
                IJPrononce prononceOrigine = entityService.load(IJPrononce.class, prononce.getIdCorrection());
                IJPrononceRegles.restitutionEtCorrection(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(), prononceOrigine, prononce, ijCalculees);
                prononceOrigine.update();
            }
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de mettre à jour l'état du prononce d'origine", e);
        }
    }

    /**
     * Importation des données par ACOR lors d'un calcul d'indemnité journalière
     * @param idIJCalculee : Id de l'IJCalculee fourni par WebAVS à ACOR
     * @param idBaseIndemnisation : Id de la base d'indemnisation fourni par WebAVS à ACOR
     * @param fCalcul :  Feuille de calcul de l'indemnité journalière comprenant les décomptes
     *                (dans ACOR ou prestation dans WebAVS) à importer
     */
    void importDecompteAcor(String idIJCalculee, String idBaseIndemnisation, FCalcul fCalcul) {
        IJIJCalculee ijijCalculee;
        IJPrononce prononce;
        IJBaseIndemnisation ijBaseIndemnisation;
        try {
            ijijCalculee = entityService.load(IJIJCalculee.class, idIJCalculee);
            if(ijijCalculee == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            prononce = entityService.load(IJPrononce.class, ijijCalculee.getIdPrononce());
            if(prononce == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            ijBaseIndemnisation = entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
            if(ijBaseIndemnisation == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver la base d'indemnisation du decompte. ");
            }

        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        List<String> idIJCalculees = new ArrayList<>();
        try {
            idIJCalculees = getIJCalculees(ijBaseIndemnisation, entityService.getSession());
            // Dans le cas d'une base d'indemnistation ayant 2 prestations, on
            // itère sur chaque prestations
            // pour importer le résultat de acor.
            // Lors du traitement de la 2ème prestation, il ne faut pas annuler
            // ou restituer
            // La base parent, car déjà fait...
            if (!(IIJBaseIndemnisation.CS_VALIDE.equals(ijBaseIndemnisation.getCsEtat()) && (idIJCalculees.size() > 1)
                    && !JadeStringUtil.isIntegerEmpty(ijBaseIndemnisation.getIdParent()))) {
                // restituer ou annuler la base d'origine si celle-ci est une correction
                IJBaseIndemnisationRegles.correction(entityService.getSession(),
                        entityService.getSession().getCurrentThreadTransaction(),
                        ijBaseIndemnisation);
            }
        } catch(Exception e) {
            throw new CommonTechnicalException("Erreur lors de la restitutions de prestations.");
        }

        // reinitialiser la base si des prestations ont deja ete calculees
        if (IIJBaseIndemnisation.CS_VALIDE.equals(ijBaseIndemnisation.getCsEtat())) {
            try {
                IJBaseIndemnisationRegles.reinitialiser(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(), ijBaseIndemnisation, ijijCalculee
                        .getIdIJCalculee());
            }catch (Exception e){
                throw new CommonTechnicalException("Erreur lors de la réinitialisation de la base d'indemnisation.");
            }
        }

        checkAndGetNssIntegrite(fCalcul, prononce.getIdDemande());
        List<IJPrestation> prestations = new ArrayList<>();
        IJDecompteMapper mapper = new IJDecompteMapper(ijijCalculee.getId(), idBaseIndemnisation, entityService);
        // Mapping des données liées aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {
            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                // Mapping liés aux prestations
                prestations.addAll(mapper.map(baseCalcul));
            }
        }

        IJBaseIndemnisation baseIndemnisation = entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
        creerRepartitionPaiement(idBaseIndemnisation, prononce, prestations, baseIndemnisation);
        UpdateEtatBaseIndemnisation(idBaseIndemnisation, baseIndemnisation);
        UpdateEtatPrononce(prononce);
    }

    private List getIJCalculees(IJBaseIndemnisation baseIndemnisation, BSession session) throws Exception {
        // charger les ij calculees pour la periode de la base d'indemnisation.
        IJIJCalculeeManager calculeeManager;

        if (IIJPrononce.CS_GRANDE_IJ.equals(baseIndemnisation.getCsTypeIJ())) {
            calculeeManager = new IJGrandeIJCalculeeManager();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(baseIndemnisation.getCsTypeIJ())) {
            calculeeManager = new IJPetiteIJCalculeeManager();
        } else {
            calculeeManager = new IJFpiCalculeeManager();
        }

        calculeeManager.setForIdPrononce(baseIndemnisation.getIdPrononce());
        calculeeManager.setForPeriode(baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode());
        calculeeManager.setSession(session);
        calculeeManager.find();

        // construire la liste des id des ij calculees pour les stocker dans le
        // viewBean
        List idsIJCalculees = null;

        if ((calculeeManager == null) || (calculeeManager.size() == 0)) {
            throw new Exception(session.getLabel("PERIODE_BI_ERREUR"));
        }

        if (calculeeManager.size() > 1) {
            /*
             * il y a plus d'une ij pour cette base, il y a alors 3 possibilites:
             *
             * 1. Une changement de situation de l'assuré (par exemple la naissance d'un enfant) a provoqué le calcul de
             * plusieurs IJ. Dans ce cas il va falloir que l'utilisateur calcule les prestations pour toutes les ij a la
             * suite. 2. Une ij calculee de 4eme revision est en defaveur de l'assure par rapport la meme ij calculee
             * pour la 3eme revision. Dans ce cas, il faut proposer un seul calcul a l'utilisateur mais avec la
             * possibilite de choisir s'il veut garantir la 3eme rev. 3. Un mélange des deux precedents
             *
             * Il est facile de distinguer les couple d'ij-ijgarantie3 car elles ont les memes dates de debut et de fin
             */
            ArrayList ijs = new ArrayList<>(calculeeManager.getContainer());

            idsIJCalculees = new ArrayList<>(calculeeManager.size());

            List<String> idsIJCalculeInseree = new ArrayList<>();

            for (int idIj = 0; idIj < ijs.size(); ++idIj) {
                IJIJCalculee ref = (IJIJCalculee) ijs.get(idIj);
                boolean trouve = false;

                for (int idComp = idIj + 1; idComp < ijs.size(); ++idComp) {
                    IJIJCalculee comp = (IJIJCalculee) ijs.get(idComp);

                    if (ref.getDateDebutDroit().equals(comp.getDateDebutDroit())
                            && ref.getDateFinDroit().equals(comp.getDateFinDroit())) {

                        IJRevisions revisions = new IJRevisions();
                        IJRevision revision = new IJRevision();
                        revision.setIdIJCalculee(ref.getIdIJCalculee());
                        revision.setNoRevision(ref.getNoRevision());

                        String montantDsc = "";

                        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession(session);
                        mgr.setForIdIJCalculee(ref.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);
                        revisions.addRevision(revision);
                        idsIJCalculeInseree.add(ref.getIdIJCalculee());

                        revision = new IJRevision();
                        revision.setIdIJCalculee(comp.getIdIJCalculee());
                        revision.setNoRevision(comp.getNoRevision());

                        mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession(session);
                        mgr.setForIdIJCalculee(comp.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);
                        revisions.addRevision(revision);
                        idsIJCalculeInseree.add(comp.getIdIJCalculee());
                        idsIJCalculees.add(revisions);
                        trouve = true;
                        break;
                    }
                }

                if (!trouve) {
                    IJRevisions revisions = new IJRevisions();

                    if (!idsIJCalculeInseree.contains(ref.getIdIJCalculee())) {
                        IJRevision revision = new IJRevision();
                        revision.setIdIJCalculee(ref.getIdIJCalculee());
                        revision.setNoRevision(ref.getNoRevision());

                        String montantDsc = "";

                        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
                        mgr.setSession(session);
                        mgr.setForIdIJCalculee(ref.getIdIJCalculee());
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                    .getMontantJournalierIndemnite();
                            montantDsc += " / ";
                        }
                        mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                        mgr.find(BManager.SIZE_NOLIMIT);
                        if (!mgr.isEmpty()) {
                            if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                                montantDsc = "na / "
                                        + ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            } else {
                                montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity())
                                        .getMontantJournalierIndemnite();
                            }
                        } else {
                            montantDsc += "na";
                        }

                        revision.setMontant(montantDsc);

                        revisions.addRevision(revision);
                        idsIJCalculees.add(revisions);
                        idsIJCalculeInseree.add(ref.getIdIJCalculee());
                    }
                }
            }
        } else {
            idsIJCalculees = new ArrayList<>(1);

            IJIJCalculee ref = (IJIJCalculee) calculeeManager.getFirstEntity();

            IJRevisions revisions = new IJRevisions();
            IJRevision revision = new IJRevision();
            revision.setIdIJCalculee(ref.getIdIJCalculee());
            revision.setNoRevision(ref.getNoRevision());

            String montantDsc = "";

            IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
            mgr.setSession(session);
            mgr.setForIdIJCalculee(ref.getIdIJCalculee());
            mgr.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
            mgr.find(BManager.SIZE_NOLIMIT);
            if (!mgr.isEmpty()) {
                montantDsc = ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                montantDsc += " / ";
            }
            mgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
            mgr.find(BManager.SIZE_NOLIMIT);
            if (!mgr.isEmpty()) {
                if (JadeStringUtil.isBlankOrZero(montantDsc)) {
                    montantDsc = "na / "
                            + ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                } else {
                    montantDsc += ((IJIndemniteJournaliere) mgr.getFirstEntity()).getMontantJournalierIndemnite();
                }
            } else {
                montantDsc += "na";
            }

            revision.setMontant(montantDsc);

            revisions.addRevision(revision);
            idsIJCalculees.add(revisions);
        }
        return idsIJCalculees;
    }

    private void creerRepartitionPaiement(String idBaseIndemnisation, IJPrononce prononce, List<IJPrestation> prestations, IJBaseIndemnisation baseIndemnisation) {
        try {
            // repartir les paiements de la prestation
            IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiements(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(),
                    prononce, baseIndemnisation, prestations);
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de créer les repartition de paiement pour les prestations de la base d'indemnisation d'id: " + idBaseIndemnisation, e);
        }
    }

    private void UpdateEtatBaseIndemnisation(String idBaseIndemnisation, IJBaseIndemnisation baseIndemnisation) {
        try {
            baseIndemnisation.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);
            baseIndemnisation.update();
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de mettre à jour la base d'indemnisation avec l'id : " + idBaseIndemnisation, e);
        }
    }

    private void UpdateEtatPrononce(IJPrononce prononce) {
        try {
            if (!IIJPrononce.CS_COMMUNIQUE.equals(prononce.getCsEtat())) {
                if (prononce.getAvecDecision()) {
                    prononce.setCsEtat(IIJPrononce.CS_VALIDE);
                } else {
                    prononce.setCsEtat(IIJPrononce.CS_DECIDE);
                }
                prononce.update();
            }
        }catch(Exception e) {
            throw new CommonTechnicalException("Impossible de mettre à jour le prononce avec l'id  " + prononce.getIdPrononce(), e);
        }
    }

    private String checkAndGetNssIntegrite(FCalcul fCalcul, String idDemande) {

        String nss = "";
        PRTiersWrapper tiers = loadTiers(idDemande);
        if(tiers == null){
            throw new PRAcorDomaineException("Réponse invalide : Impossible de charger le tiers correspondant au requérant. ");
        }
        // Récupère le NSS du FCalcul reçu d'ACOR
        for (FCalcul.Assure assure :
                fCalcul.getAssure()) {
            if ("req".equals(assure.getFonction()) &&
                    ("nss".equals(assure.getId().getType()) ||
                     "navs".equals(assure.getId().getType()))) {
                    nss = assure.getId().getValue();
                    break;
            }
        }
        if (StringUtils.isBlank(nss)) {
            throw new PRAcorDomaineException("Réponse invalide : Impossible de retrouver le NSS du requérant. ");
        }
        if (nss.equals(tiers.getNSS())) {
            throw new PRAcorDomaineException(entityService.getSession().getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
        }
        return nss;
    }

    private PRTiersWrapper loadTiers(String idDemande) {
        PRDemande demande = entityService.load(PRDemande.class, idDemande);
        PRTiersWrapper tiers = null;
        try {
            if (demande != null) {
                tiers = demande.loadTiers();
            }
        }catch(Exception e){
            throw new PRAcorDomaineException("Réponse invalide : Impossible de charger le tiers correspondant au requérant. ");
        }
        return tiers;
    }
}
