package globaz.musca.db.interet.cotipercuentrop.montantsoumis;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlanManager;

public class FACotiPercuEnTropParRubriqueManager extends FASumMontantSoumisParPlanManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fromAnneeCotisation;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", b." + FAAfact.FIELD_IDRUBRIQUE + ", a."
                + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", a." + FAEnteteFacture.FIELD_IDROLE + ", a."
                + FAEnteteFacture.FIELD_IDSOUSTYPE;
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();

        where.append(getWhere());

        try {
            FAApplication app = new FAApplication();
            if (!JadeStringUtil.isBlank(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()))) {
                setFromAnneeCotisation(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()));
                where.append(" and b." + FAAfact.FIELD_ANNEECOTISATION + " >= " + getFromAnneeCotisation() + " ");
            }
        } catch (Exception e) {
            // do nothing
        }

        where.append(getGroupBy());

        where.append(",b." + FAAfact.FIELD_IDRUBRIQUE + ", a." + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", a."
                + FAEnteteFacture.FIELD_IDROLE + ", a." + FAEnteteFacture.FIELD_IDSOUSTYPE + " and ");

        // K150915_057
        // n'appliquer que dans le cas des Contrôle employeur (FACotiPercuEnTropParRubriqueManager)
        // where.append(getHaving());
        if (isForMontantPositif()) {
            where.append("b." + FAAfact.FIELD_MONTANTFACTURE + " > 0 ");
        } else {
            where.append("b." + FAAfact.FIELD_MONTANTFACTURE + " < 0 ");
        }

        where.append(getOrder());

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FACotiPercuEnTropParRubrique();
    }

    /**
     * @return the fromAnneeCotisation
     */
    public String getFromAnneeCotisation() {
        return fromAnneeCotisation;
    }

    /**
     * @param fromAnneeCotisation
     *            the fromAnneeCotisation to set
     */
    public void setFromAnneeCotisation(String fromAnneeCotisation) {
        this.fromAnneeCotisation = fromAnneeCotisation;
    }
}
