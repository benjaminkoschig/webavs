package globaz.aquila.db.rdp.cashin.persistence;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;

public class EcritureRDPManager extends BManager {
    private static final long serialVersionUID = 3869122869894602863L;

    private String forIdSection;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EcritureRDP();
    }

    @Override
    protected String _getSql(BStatement statement) {
        return "SELECT s.IDEXTERNE AS NOSECTION, rubcc.IDEXTERNE AS NORUBRIQUECC, s.IDTYPESECTION AS IDTYPESECTION, SUM(o.MONTANT) AS MONTANT FROM "
                + _getCollection()
                + "CAOPERP o JOIN "
                + _getCollection()
                + "CARUBRP r ON r.IDRUBRIQUE=o.IDCOMPTE JOIN "
                + _getCollection()
                + "CASECTP s ON o.IDSECTION=s.IDSECTION JOIN "
                + _getCollection()
                + "CACPTCP cc ON o.IDCOMPTECOURANT=cc.IDCOMPTECOURANT JOIN "
                + _getCollection()
                + "CARUBRP rubcc ON cc.IDRUBRIQUE=rubcc.IDRUBRIQUE "
                + "WHERE s.IDSECTION="
                + forIdSection
                + " AND o.ETAT="
                + APIOperation.ETAT_COMPTABILISE
                + " GROUP BY s.IDEXTERNE, s.IDTYPESECTION, rubcc.IDEXTERNE";
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }
}
