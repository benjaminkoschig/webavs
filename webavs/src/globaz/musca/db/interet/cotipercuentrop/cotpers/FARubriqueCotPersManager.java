package globaz.musca.db.interet.cotipercuentrop.cotpers;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

public class FARubriqueCotPersManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPlan;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + AFAssurance.TABLE_NAME + " a, " + _getCollection()
                + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " im";
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        where.append("a." + AFAssurance.FIELD_ID_RUBRIQUE + " = im." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE
                + " and ");
        where.append("a." + AFAssurance.FIELD_GENRE_ASSURANCE + " = " + CodeSystem.GENRE_ASS_PERSONNEL + " and ");
        where.append("a." + AFAssurance.FIELD_TYPE_ASSURANCE + " = " + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + " and ");
        where.append("im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + " ");

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FARubriqueCotPers();
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }
}
