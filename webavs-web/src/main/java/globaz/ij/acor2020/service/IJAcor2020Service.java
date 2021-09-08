package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.ObjectFactory;
import ch.globaz.common.util.Debug;
import ch.globaz.common.xml.JaxbHandler;
import ch.globaz.common.xml.MessageValidation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IJAcor2020Service {

    private static final JaxbHandler<InHostType> IN_HOST_TYPE_VALIDATEUR = createJaxbValidator();

    public InHostType createInHostCalcul(String idPrononce) {
        IJExportationCalculAcor exporter = new IJExportationCalculAcor();
        InHostType inHostType = exporter.createInHost(idPrononce);

        if (IN_HOST_TYPE_VALIDATEUR != null && Debug.isEnvironnementInDebug()) {
            List<MessageValidation> validate = IN_HOST_TYPE_VALIDATEUR.validate(inHostType);
            validate.forEach(validationEvent -> LOG.warn("Acor 4 InHostType object not valid: {}",validationEvent.message()));
        }

        return inHostType;
    }

    public InHostType createInHostDecompte(String idPrononce, String idBaseIndemnisation) {
        IJExportationCalculAcor exporter = new IJExportationCalculAcor();
        InHostType inHostType = exporter.createInHost(idPrononce);

        if (IN_HOST_TYPE_VALIDATEUR != null && Debug.isEnvironnementInDebug()) {
            List<MessageValidation> validate = IN_HOST_TYPE_VALIDATEUR.validate(inHostType);
            validate.forEach(validationEvent -> LOG.warn("Acor 4 InHostType object not valid: {}",validationEvent.message()));
        }

        return inHostType;
    }

    private static JaxbHandler<InHostType> createJaxbValidator() {
        if (Debug.isEnvironnementInDebug()) {
            return JaxbHandler.build(
                    "/xsd/acorRentes/xsd/acor-rentes-in-host.xsd",
                    InHostType.class,
                    inHost -> new ObjectFactory().createInHost(inHost));
        }
        return null;
    }

    public void importCalculAcor(String idPrononce, FCalcul fCalcul) {
        IJImportationCalculAcor importer = new IJImportationCalculAcor();
        importer.importCalculAcor(idPrononce, fCalcul);
    }

    public void importDecompteAcor(String idPrononce, String idBaseIndemnisation, FCalcul fCalcul){
        IJImportationCalculAcor importer = new IJImportationCalculAcor();
        importer.importDecompteAcor(idPrononce, idBaseIndemnisation, fCalcul);
    }
}
