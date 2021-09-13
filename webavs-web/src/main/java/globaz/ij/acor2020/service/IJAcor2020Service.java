package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.ObjectFactory;
import ch.globaz.common.util.Debug;
import ch.globaz.common.xml.JaxbHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IJAcor2020Service {

    private static final JaxbHandler<InHostType> IN_HOST_TYPE_VALIDATEUR = createJaxbValidator();

    public InHostType createInHostCalcul(String idPrononce) {
        IJExportationCalculAcor exporter = new IJExportationCalculAcor();
        InHostType inHostType = exporter.createInHostForCalcul(idPrononce);
        validate(inHostType);
        return inHostType;
    }

    public InHostType createInHostDecompte(String idIJCalculee,final String idBaseIndemnisation) {
        IJExportationCalculAcor exporter = new IJExportationCalculAcor();
        InHostType inHostType = exporter.createInHostForDecompte(idIJCalculee,idBaseIndemnisation);
        validate(inHostType);
        return inHostType;
    }

    private void validate(final InHostType inHostType) {
        if (IN_HOST_TYPE_VALIDATEUR != null && Debug.isEnvironnementInDebug()) {
            IN_HOST_TYPE_VALIDATEUR.validate(inHostType).logMessagesInWarn(LOG);
        }
    }

    private static JaxbHandler<InHostType> createJaxbValidator() {
        if (Debug.isEnvironnementInDebug()) {
            return JaxbHandler.build("/xsd/acorRentes/xsd/acor-rentes-in-host.xsd", InHostType.class,
                                     inHost -> new ObjectFactory().createInHost(inHost));
        }
        return null;
    }

    public void importCalculAcor(String idPrononce, FCalcul fCalcul) {
        IJImportationCalculAcor importer = new IJImportationCalculAcor();
        importer.importCalculAcor(idPrononce, fCalcul);
    }

    public void importDecompteAcor(String idIJCalculee, String idBaseIndemnisation, FCalcul fCalcul) {
        IJImportationCalculAcor importer = new IJImportationCalculAcor();
        importer.importDecompteAcor(idIJCalculee, idBaseIndemnisation, fCalcul);
    }
}
