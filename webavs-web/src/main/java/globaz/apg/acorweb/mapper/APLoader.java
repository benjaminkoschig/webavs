package globaz.apg.acorweb.mapper;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.apg.acorweb.service.APExportationCalculAcor;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationFamilialeMatManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.List;

public class APLoader {
    private static final Logger LOG = LoggerFactory.getLogger(APLoader.class);

    public static List<APSituationProfessionnelle> loadSituationsProfessionnelles(String idDroit, BSession session)  {
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);

        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_SITUATION_PROFESSIONNELLE"), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_SITUATION_PROFESSIONNELLE"), e);
        }
        return mgr.getContainer();
    }

    public static List<APSituationFamilialeMat> loadSituationFamillialeMat(String idDroit, BSession session) {
        APSituationFamilialeMatManager situationFamilialeMat = new APSituationFamilialeMatManager();
        situationFamilialeMat.setSession(session);
        situationFamilialeMat.setForIdDroitMaternite(idDroit);
        try {
            situationFamilialeMat.find(session.getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_SITUATION_FAMILIALE"), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_SITUATION_FAMILIALE"), e);
        }
        return situationFamilialeMat.getContainerAsList();
    }
}
