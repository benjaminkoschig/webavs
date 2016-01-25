package globaz.aquila.db.access.traitspec;

import globaz.aquila.api.ICOTraitementSpecifique;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1> DOCUMENT ME!
 * 
 * @author vre
 */
public class COTraitementSpecifiqueManager extends BManager {

    private static final long serialVersionUID = 4015734514646370334L;

    private boolean forAllTraitementSpecifique = true;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + COTraitementSpecifique.TABLE_NAME;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String where = new String();

        if (isForAllTraitementSpecifique()) {
            String in = getTypeTraitementSpecifique(statement);
            if (!JadeStringUtil.isBlank(in)) {
                where += COTraitementSpecifique.FNAME_CS_TYPE + " in (";
                where += in;
                where += ")";
            }
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COTraitementSpecifique();
    }

    private String getTypeTraitementSpecifique(BStatement statement) {
        String types = null;

        try {
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
            manager.setSession(getSession());
            manager.getListeCodesSup(ICOTraitementSpecifique.CS_FAMILLE_TYPE_TSP, getSession().getIdLangue());

            for (int i = 0; i < manager.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) manager.get(i);

                if (!JadeStringUtil.isIntegerEmpty(code.getCurrentCodeUtilisateur().getIdCodeSysteme())) {
                    if (types == null) {
                        types = new String();
                    } else {
                        types += ", ";
                    }
                    types += code.getCurrentCodeUtilisateur().getIdCodeSysteme();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return types;
    }

    public boolean isForAllTraitementSpecifique() {
        return forAllTraitementSpecifique;
    }

    public void setForAllTraitementSpecifique(boolean forAllTraitementSpecifique) {
        this.forAllTraitementSpecifique = forAllTraitementSpecifique;
    }

}
