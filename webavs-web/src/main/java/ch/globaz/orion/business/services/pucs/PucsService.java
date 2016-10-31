package ch.globaz.orion.business.services.pucs;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

public interface PucsService extends JadeApplicationService {

    public String pucFileLisible(String id, String provenance, String etatSwissDecPucsFile);

    public String pucFileLisibleXls(String id, String provenance, String etatSwissDecPucsFile);

    public String pucFileLisibleXml(String id, String provenance, String etatSwissDecPucsFile);

    public String pucsFileLisibleForEbusiness(String id, DeclarationSalaireProvenance provenance, String format,
            String loginName, String userEmail, String langue);

}
