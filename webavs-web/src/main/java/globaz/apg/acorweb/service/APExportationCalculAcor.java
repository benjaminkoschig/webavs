package globaz.apg.acorweb.service;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.globaz.common.persistence.EntityService;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.utils.APGUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.ij.acorweb.service.IJAcorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APExportationCalculAcor {

    private static final Logger LOG = LoggerFactory.getLogger(APExportationCalculAcor.class);

    private BSession session;
    private String idDroit;
    private String genreService = "";
    private BTransaction transaction;
    private APDroitLAPG droit;
    private EntityService entityService;

    public APExportationCalculAcor(String idDroit, String genreService) {
        this.session = BSessionUtil.getSessionFromThreadContext();;
        this.idDroit = idDroit;
        this.genreService = genreService;
        this.transaction = session.getCurrentThreadTransaction();
        this.entityService = EntityService.of(session);
    }

    public InHostType createInHost() {
        LOG.info("Création du inHost.");
        InHostType inHost = new InHostType(); // TODO WS ACOR APG IMPLEMENT CREATION INHOSTTYPE (EXPORT TO ACOR)
        // inHost = new IJAcorService().createInHostCalcul("2406"); // TODO WS ACOR APG /!\ UTILE POUR AVOIR UN INHOSTTYPE DE TEST SUR CICICAM /!\
        try {
            droit = loadDroit();
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        return inHost;
    }

    public APDroitLAPG loadDroit() throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(idDroit)) {
            droit = APGUtils.loadDroit(session, idDroit, genreService);
        }

        return droit;
    }
}
