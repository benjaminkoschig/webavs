package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.eavs.utils.StringUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.acor2020.mapper.IJDecompteMapper;
import globaz.ij.acor2020.mapper.IJIJCalculeeMapper;
import globaz.ij.acor2020.mapper.IJIJIndemniteJournaliereMapper;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

class IJImportationCalculAcor {
    private IJPrononce prononce;
    private IJBaseIndemnisation baseIndemnisation;
    private IJIJCalculee ijijCalculee;
    private final BSession session;
    private PRTiersWrapper tiers;
    private final EntityService entityService;

    public IJImportationCalculAcor() {
        session = BSessionUtil.getSessionFromThreadContext();
        entityService = EntityService.of(this.session);
    }

    void importCalculAcor(String idPrononce, FCalcul fCalcul) {

        try {
            // Chargement des informations du prononcé correspondant à la demande de calcul Acor
            // dans WebAVS
            prononce = loadPrononce(idPrononce);
            if (prononce == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le prononcé du calcul. ");
            }
            tiers = loadTiers(prononce.getIdDemande());
            if (tiers == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le tiers du calcul. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        // Récupère le NSS du FCalcul reçu d'ACOR
        checkNssIntegrite(fCalcul);

        // Mapping des données liées aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = IJIJCalculeeMapper.baseCalculMapToIJIJCalculee(baseCalcul, tiers, prononce, session);
                IJIJIndemniteJournaliereMapper.baseCalculEtIjMapToIndemniteJournaliere(baseCalcul, ijCalculee, session);
            }
        }
    }

    void importDecompteAcor(String idIJCalculee, String idBaseIndemnisation, FCalcul fCalcul) {

        try {

            ijijCalculee = loadIjCalculee(idIJCalculee);
            if(ijijCalculee == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            prononce = loadPrononce(ijijCalculee.getIdPrononce());
            if(prononce == null){
                throw new PRACORException("Réponse invalide : Impossible de retrouver l'ij calculee du decompte. ");
            }
            tiers = loadTiers(prononce.getIdDemande());
            if (tiers == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le tiers  du decompte. ");
            }
            baseIndemnisation = loadBaseIndemnisation(idBaseIndemnisation);
            if (baseIndemnisation == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver la base d'indemnisation du decompte. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
        checkNssIntegrite(fCalcul);

        // Mapping des données liées aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                // Mapping liés aux prestations
                IJDecompteMapper.baseCalculDecompteMapToIJPrestation(baseCalcul, ijijCalculee.getId(), idBaseIndemnisation, session);
            }
        }
    }

    private void checkNssIntegrite(FCalcul fCalcul) {
        // Récupère le NSS du FCalcul reçu d'ACOR
        String nss = "";
        for (FCalcul.Assure assure :
                fCalcul.getAssure()) {
            if ("req".equals(assure.getFonction())) {
                if ("nss".equals(assure.getId().getType()) || "navs".equals(assure.getId().getType())) {
                    nss = assure.getId().getValue();
                    break;
                }
            }
        }
        if (StringUtils.isBlank(nss)) {
            throw new PRAcorDomaineException("Réponse invalide : Impossible de retrouver le NSS du requérant. ");
        }
        if (nss.equals(tiers.getNSS())) {
            throw new PRAcorDomaineException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
        }
    }

    private IJIJCalculee loadIjCalculee(final String idIJCalculee) {
        return entityService.load(IJIJCalculee.class, idIJCalculee);
    }

    private IJPrononce loadPrononce(String idPrononce) throws Exception {
        return entityService.load(IJPrononce.class, idPrononce);
    }

    private PRTiersWrapper loadTiers(String idDemande) throws Exception {
        PRDemande demande = loadDemande(idDemande);
        if (demande != null) {
            return demande.loadTiers();
        }
        return null;
    }

    private PRDemande loadDemande(String idDemande){
        return entityService.load(PRDemande.class, idDemande);
    }

    private IJBaseIndemnisation loadBaseIndemnisation(String idBaseIndemnisation) {
        return entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
    }
}
