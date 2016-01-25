package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;

public class CAMaxAnneeCotisationEcritureForSectionManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSection = new String();

    @Override
    protected String _getFields(BStatement statement) {
        return "max(" + CAOperation.FIELD_ANNEECOTISATION + ") as anneeCotisationMax";
    }

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {

        String select = "SELECT max(" + CAOperation.FIELD_ANNEECOTISATION + ") anneeCotisationMax";

        select += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " OP ";
        select += " WHERE OP." + CAOperation.FIELD_IDSECTION + " = " + getForIdSection() + " AND  OP."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE;

        return select;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new CAMaxAnneeCotisationEcritureForSection();
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

}
