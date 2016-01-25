package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.rdp.cashin.RequisitionPoursuiteCashinBuilder;
import globaz.aquila.db.rdp.cashin.exporter.CashinExporter;
import globaz.aquila.db.rdp.cashin.model.RequisitionPoursuite;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.db.comptes.CASection;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;

public class COExecuterRDPCashin extends COTransitionAction {

    private static final String CASHIN_EXPORT_PATH = "cashin.exportpath";

    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        if (contentieux.getPrevisionnel()) {
            return;
        }

        try {
            CASection Section = contentieux.getSection();
            String idTiers = contentieux.getCompteAnnexe().getIdTiers();
            String idExterneRole = contentieux.getCompteAnnexe().getIdExterneRole();
            String user = contentieux.getUser();
            String date = getDateExecution();
            String motif = getMotif();

            RequisitionPoursuiteCashinBuilder builder = new RequisitionPoursuiteCashinBuilder(transaction);
            RequisitionPoursuite requisitionPoursuite = builder.build(Section, idTiers, idExterneRole, user, date,
                    motif, transaction);

            CashinExporter.export(requisitionPoursuite, getCashinExportPath());
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        _validerSolde(contentieux);
        _validerEcheance(contentieux);
        _validerCasPourEnvoyerPoursuite(contentieux);
    }

    private String getCashinExportPath() throws JadeApplicationException {

        String cashinExportPath;
        try {
            cashinExportPath = GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA).getProperty(
                    CASHIN_EXPORT_PATH);
        } catch (Exception e) {
            throw new PropertiesException("Unable to retrieve the property ["
                    + ICOApplication.DEFAULT_APPLICATION_AQUILA + "." + CASHIN_EXPORT_PATH + "]", e);
        }

        if (JadeStringUtil.isBlank(cashinExportPath)) {
            throw new PropertiesException("The property [" + ICOApplication.DEFAULT_APPLICATION_AQUILA + "."
                    + CASHIN_EXPORT_PATH + "] doesn't exist");
        }

        return cashinExportPath.trim();
    }
}
