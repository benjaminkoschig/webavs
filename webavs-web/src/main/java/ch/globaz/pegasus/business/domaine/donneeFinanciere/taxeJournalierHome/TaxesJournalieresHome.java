package ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;

public class TaxesJournalieresHome extends DonneesFinancieresList<TaxeJournaliereHome, TaxesJournalieresHome> {

    public TaxesJournalieresHome() {
        super(TaxesJournalieresHome.class);
    }

    public Set<String> resovleIdsTypeChambre() {
        Set<String> ids = new HashSet<String>();
        List<TaxeJournaliereHome> taxes = getList();
        for (TaxeJournaliereHome taxe : taxes) {
            ids.add(taxe.getIdTypeChambre());
        }
        return ids;
    }

    public TaxeJournaliereHome resolveCurrentTaxejournaliere() {
        if (hasOneElement()) {
            return getList().get(0);
        } else if (hasMoreThanOneElement()) {
            throw new RpcBusinessException("trop de taxe journalières trouvées");
        }
        return null;
    }
}
