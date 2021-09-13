package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.eavs.utils.StringUtils;
import globaz.globall.db.BSessionUtil;
import globaz.ij.acor2020.mapper.IJDecompteMapper;
import globaz.ij.acor2020.mapper.IJIJCalculeeMapper;
import globaz.ij.acor2020.mapper.IJIJIndemniteJournaliereMapper;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.module.IJRepartitionPaiementBuilder;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrononceRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.util.LinkedList;
import java.util.List;

class IJImportationCalculAcor {
    private final EntityService entityService;

    public IJImportationCalculAcor() {
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    /**
     * Importation des donn�es par ACOR lors d'un calcul d'indemnit� journali�re
     * @param idPrononce : id du prononce fourni par WebAVS � ACOR
     * @param fCalcul : Feuille de calcul de l'indemnit� journali�re
     */
    void importCalculAcor(String idPrononce, FCalcul fCalcul) {
        IJPrononce prononce;
        try {
            // Chargement des informations du prononc� correspondant � la demande de calcul Acor
            // dans WebAVS
            prononce = entityService.load(IJPrononce.class, idPrononce);
            if (prononce == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver le prononc� du calcul. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        // R�cup�re le NSS du FCalcul re�u d'ACOR
        String nss = checkAndGetNssIntegrite(fCalcul, prononce.getIdDemande());

        List ijCalculees = new LinkedList();
        // Mapping des donn�es li�es aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = IJIJCalculeeMapper.baseCalculMapToIJIJCalculee(baseCalcul, nss, prononce, entityService);
                IJIJIndemniteJournaliereMapper.baseCalculEtIjMapToIndemniteJournaliere(baseCalcul, ijCalculee, entityService);
                ijCalculees.add(ijCalculee);
            }
        }
        // restituer ou annuler le prononc� d'origine si celui-ci est une
        // correction
        RestitutionOuCorrectionPrononceOrigine(prononce, ijCalculees);
        UpdateEtatPrononce(prononce);
    }

    private void RestitutionOuCorrectionPrononceOrigine(IJPrononce prononce, List ijCalculees) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(prononce.getIdCorrection())) {
                IJPrononce prononceOrigine = entityService.load(IJPrononce.class, prononce.getIdCorrection());
                IJPrononceRegles.restitutionEtCorrection(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(), prononceOrigine, prononce, ijCalculees);
                prononceOrigine.update();
            }
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de mettre � jour l'�tat du prononce d'origine", e);
        }
    }

    /**
     * Importation des donn�es par ACOR lors d'un calcul d'indemnit� journali�re
     * @param idIJCalculee : Id de l'IJCalculee fourni par WebAVS � ACOR
     * @param idBaseIndemnisation : Id de la base d'indemnisation fourni par WebAVS � ACOR
     * @param fCalcul :  Feuille de calcul de l'indemnit� journali�re comprenant les d�comptes
     *                (dans ACOR ou prestation dans WebAVS) � importer
     */
    void importDecompteAcor(String idIJCalculee, String idBaseIndemnisation, FCalcul fCalcul) {
        IJIJCalculee ijijCalculee;
        IJPrononce prononce;
        IJBaseIndemnisation ijBaseIndemnisation;
        try {
            ijijCalculee = entityService.load(IJIJCalculee.class, idIJCalculee);
            if(ijijCalculee == null){
                throw new PRACORException("R�ponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            prononce = entityService.load(IJPrononce.class, ijijCalculee.getIdPrononce());
            if(prononce == null){
                throw new PRACORException("R�ponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            ijBaseIndemnisation = entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
            if(ijBaseIndemnisation == null){
                throw new PRACORException("R�ponse invalide : Impossible de retrouver la base d'indemnisation du decompte. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        /** TODO JJO 13.09.2021 : Si pas d'utiliter via retour de test supprimer le code comment�
        // Dans le cas d'une base d'indemnistation ayant 2 prestations, on
        // it�re sur chaque prestations
        // pour importer le r�sultat de acor.
        // Lors du traitement de la 2�me prestation, il ne faut pas annuler
        // ou restituer
        // La base parent, car d�j� fait...
        if (!(IIJBaseIndemnisation.CS_VALIDE.equals(base.getCsEtat()) && (caViewBean.getIdsIJCalculees().size() > 1)
                && !JadeStringUtil.isIntegerEmpty(base.getIdParent()))) {
            // restituer ou annuler la base d'origine si celle-ci est une correction
            IJBaseIndemnisationRegles.correction(entityService.getSession(),
                                                 entityService.getSession().getCurrentThreadTransaction(),
                                                 ijBaseIndemnisation);
        }
        **/

        // reinitialiser la base si des prestations ont deja ete calculees
        if (IIJBaseIndemnisation.CS_VALIDE.equals(ijBaseIndemnisation.getCsEtat())) {
            try {
                IJBaseIndemnisationRegles.reinitialiser(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(), ijBaseIndemnisation, ijijCalculee
                        .getIdIJCalculee());
            }catch (Exception e){
                throw new CommonTechnicalException("Erreur lors de la r�initialisation de la base d'indemnisation.");
            }
        }

        checkAndGetNssIntegrite(fCalcul, prononce.getIdDemande());
        List prestations = new LinkedList();
        // Mapping des donn�es li�es aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {
            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                // Mapping li�s aux prestations
                prestations.addAll(IJDecompteMapper.baseCalculDecompteMapToIJPrestation(baseCalcul, ijijCalculee.getId(), idBaseIndemnisation, entityService));
            }
        }

        IJBaseIndemnisation baseIndemnisation = entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
        creerRepartitionPaiement(idBaseIndemnisation, prononce, prestations, baseIndemnisation);
        UpdateEtatBaseIndemnisation(idBaseIndemnisation, baseIndemnisation);
        UpdateEtatPrononce(prononce);
    }

    private void creerRepartitionPaiement(String idBaseIndemnisation, IJPrononce prononce, List prestations, IJBaseIndemnisation baseIndemnisation) {
        try {
            // repartir les paiements de la prestation
            IJRepartitionPaiementBuilder.getInstance().buildRepartitionPaiements(entityService.getSession(), entityService.getSession().getCurrentThreadTransaction(),
                    prononce, baseIndemnisation, prestations);
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de cr�er les repartition de paiement pour les prestations de la base d'indemnisation d'id: " + idBaseIndemnisation, e);
        }
    }

    private void UpdateEtatBaseIndemnisation(String idBaseIndemnisation, IJBaseIndemnisation baseIndemnisation) {
        try {
            baseIndemnisation.setCsEtat(IIJBaseIndemnisation.CS_VALIDE);
            baseIndemnisation.update();
        }catch(Exception e){
            throw new CommonTechnicalException("Impossible de mettre � jour la base d'indemnisation avec l'id : " + idBaseIndemnisation, e);
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
            throw new CommonTechnicalException("Impossible de mettre � jour le prononce avec l'id  " + prononce.getIdPrononce(), e);
        }
    }

    private String checkAndGetNssIntegrite(FCalcul fCalcul, String idDemande) {

        String nss = "";
        PRTiersWrapper tiers = loadTiers(idDemande);
        if(tiers == null){
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de charger le tiers correspondant au requ�rant. ");
        }
        // R�cup�re le NSS du FCalcul re�u d'ACOR
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
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de retrouver le NSS du requ�rant. ");
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
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de charger le tiers correspondant au requ�rant. ");
        }
        return tiers;
    }
}
