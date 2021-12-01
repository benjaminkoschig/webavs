package globaz.ij.helpers.acor;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;

import java.util.List;

/**
 * @author ebko
 */

interface IIJCalculStandard {

    List calculPrestationsSansAcor(BSession session, BTransaction transaction, IJPrononce prononce,
                                   IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee) throws Exception;

}
