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
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

class IJImportationCalculAcor {
    private IJPrononce prononce;
    private IJBaseIndemnisation baseIndemnisation;
    private final BSession session;
    private PRTiersWrapper tiers;
    private final EntityService entityService;

    public IJImportationCalculAcor() {
        session = BSessionUtil.getSessionFromThreadContext();
        entityService = EntityService.of(this.session);
    }

    void importCalculAcor(String idPrononce, FCalcul fCalcul) {

        try {


            // Chargement des informations du prononc� correspondant � la demande de calcul Acor
            // dans WebAVS
            loadPrononce(idPrononce);

            if (prononce == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver le prononc� du calcul. ");
            }
            loadTiers();
            if (tiers == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver le tiers du calcul. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        // R�cup�re le NSS du FCalcul re�u d'ACOR
        checkNssIntegrite(fCalcul);

        // Mapping des donn�es li�es aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = IJIJCalculeeMapper.baseCalculMapToIJIJCalculee(baseCalcul, tiers, prononce, session);
                IJIJIndemniteJournaliereMapper.baseCalculEtIjMapToIndemniteJournaliere(baseCalcul, ijCalculee, session);
            }
        }
    }

    void importDecompteAcor(String idPrononce, String idBaseIndemnisation, FCalcul fCalcul) {

        try {
            // Chargement des informations du prononc� correspondant � la demande du decompte Acor
            // dans WebAVS
            loadPrononce(idPrononce);
            if (prononce == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver le prononc� du decompte. ");
            }
            loadTiers();
            if (tiers == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver le tiers  du decompte. ");
            }
            loadBaseIndemnisation(idBaseIndemnisation);
            if (baseIndemnisation == null) {
                throw new PRACORException("R�ponse invalide : Impossible de retrouver la base d'indemnisation du decompte. ");
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
        checkNssIntegrite(fCalcul);

        // Mapping des donn�es li�es aux bases de calcul.
        for (FCalcul.Cycle cycle :
                fCalcul.getCycle()) {

            for (FCalcul.Cycle.BasesCalcul baseCalcul :
                    cycle.getBasesCalcul()) {
                IJIJCalculee ijCalculee = IJIJCalculeeMapper.baseCalculMapToIJIJCalculee(baseCalcul, tiers, prononce, session);
                IJIJIndemniteJournaliereMapper.baseCalculEtIjMapToIndemniteJournaliere(baseCalcul, ijCalculee, session);
                // Mapping li�s aux prestations
                IJDecompteMapper.baseCalculDecompteMapToIJPrestation(baseCalcul, ijCalculee.getId(), idBaseIndemnisation, session);
            }
        }
    }

    private void checkNssIntegrite(FCalcul fCalcul) {
        // R�cup�re le NSS du FCalcul re�u d'ACOR
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
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de retrouver le NSS du requ�rant. ");
        }
        if (nss.equals(tiers.getNSS())) {
            throw new PRAcorDomaineException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
        }
    }

    private void loadPrononce(String idPrononce) throws Exception {
        if ((prononce == null) & !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            prononce = IJPrononce.loadPrononce(session, null, idPrononce, null);
        }
    }

    private void loadTiers() throws Exception {
        if (tiers == null) {
            tiers = prononce.loadDemande(null).loadTiers();
        }
    }

    private void loadBaseIndemnisation(String idBaseIndemnisation) {
        if ((baseIndemnisation == null) && !JadeStringUtil.isIntegerEmpty(idBaseIndemnisation)) {
            baseIndemnisation = this.entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
        }
    }
}
