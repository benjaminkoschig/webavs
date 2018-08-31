package ch.globaz.vulpecula.external.services;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.affiliation.SuiviCaisse;
import globaz.globall.db.BSession;

/**
 * @since WebBMS 2.6
 */
public interface SuiviCaissesService {
    List<SuiviCaisse> findByIdEmployeurAndDate(BSession session, String idEmployeur, Date dateReference);
}
