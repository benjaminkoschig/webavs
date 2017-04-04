package globaz.corvus.annonce.service;

import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;

public interface REAnnonceXmlService {

    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception;
}
