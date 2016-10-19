package ch.globaz.orion.business.services.pucs;

import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import ch.globaz.xmlns.eb.pucs.PucsSearchOrderByEnum;

public interface PucsService extends JadeApplicationService {
    List<PucsEntrySummary> listPucsFile(String type, int size, String likeAffilie, String forAnnee,
            String dateSoumission, PucsSearchOrderByEnum orderby, BSession session) throws OrionPucsException;

    public String pucFileLisible(String id, String provenance, String etatSwissDecPucsFile);

    public String pucFileLisibleXls(String id, String provenance, String etatSwissDecPucsFile);

    public String pucFileLisibleXml(String id, String provenance, String etatSwissDecPucsFile);

    public String pucsFileLisibleForEbusiness(String id, DeclarationSalaireProvenance provenance, String format,
            String loginName, String userEmail, String langue);

}
