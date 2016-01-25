package globaz.lynx.db.recherche;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.utils.LXConstants;
import java.util.ArrayList;

public class LXRechercheDetailManager extends LXOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtat;

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer ordreBy = new StringBuffer();
        ordreBy.append("PSPY ASC");

        if (!JadeStringUtil.isIntegerEmpty(getForTri())) {
            ordreBy.append(", ").append(getForTri()).append(" DESC");
        }

        return ordreBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        if (!JadeStringUtil.isBlankOrZero(getForEtat())) {

            ArrayList<String> listeEtat = new ArrayList<String>();
            listeEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
            listeEtat.add(LXOperation.CS_ETAT_PREPARE);
            listeEtat.add(LXOperation.CS_ETAT_SOLDE);

            if (LXConstants.ETAT_PROVISOIRE.equals(getForEtat())) {
                listeEtat.add(LXOperation.CS_ETAT_OUVERT);
                listeEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
            }

            setForCsEtatIn(listeEtat);
        }

        return super._getWhere(statement);
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

}
