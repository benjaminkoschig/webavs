package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.eavs.utils.StringUtils;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.acor2020.mapper.IJIJCalculeeMapper;
import globaz.ij.acor2020.mapper.IJIJIndemniteJournaliereMapper;
import globaz.ij.acor2020.mapper.IJPrestationMapper;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

class IJImportationCalculAcor {
    IJPrononce prononce;
    BSession session;
    PRTiersWrapper tiers;

    void importCalculAcor(String idPrononce, FCalcul fCalcul) {

        try {
            loadSession();
            // Chargement des informations du prononcé correspondant à la demande de calcul Acor
            // dans WebAVS
            loadPrononce(idPrononce, fCalcul.getTypeDem());
            if (prononce == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le prononcé du calcul. ");
            }
            loadTiers();
            if (tiers == null) {
                throw new PRACORException("Réponse invalide : Impossible de retrouver le tiers du calcul. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

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

        // Mapping des données liées aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = IJIJCalculeeMapper.baseCalculMapToIJIJCalculee(baseCalcul, tiers, prononce, session);
                IJIJIndemniteJournaliereMapper.baseCalculEtIjMapToIndemniteJournaliere(baseCalcul, ijCalculee, session);
                // Mapping liés aux prestations
                // TODO : Voir comment récupérer l'id de la base d'indemnisation depuis le webservice via le token.
                String idBaseIndemnisation = "";
                IJPrestationMapper.baseCalculDecompteMapToIJPrestation(baseCalcul, ijCalculee.getId(), idBaseIndemnisation, session);
            }
        }
    }

    private void loadPrononce(String idPrononce, String csTypeIJ) throws Exception {
        if ((prononce == null) & !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            prononce = IJPrononce.loadPrononce(session, null, idPrononce, csTypeIJ);
        }
    }

    private void loadSession(){
        if(session == null){
            session = BSessionUtil.getSessionFromThreadContext();
        }
    }

    private void loadTiers() throws Exception {
        if(tiers == null) {
            tiers = prononce.loadDemande(null).loadTiers();
        }
    }
}
