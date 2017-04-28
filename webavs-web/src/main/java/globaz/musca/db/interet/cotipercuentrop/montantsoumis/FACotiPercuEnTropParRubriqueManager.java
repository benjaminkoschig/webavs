/*
 * Globaz SA.
 */
package globaz.musca.db.interet.cotipercuentrop.montantsoumis;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlanManager;

public class FACotiPercuEnTropParRubriqueManager extends FASumMontantSoumisParPlanManager {

    private static final long serialVersionUID = 1L;
    private String fromAnneeCotisation;

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement) + ", b." + FAAfact.FIELD_IDRUBRIQUE + ", a."
                + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", a." + FAEnteteFacture.FIELD_IDROLE + ", a."
                + FAEnteteFacture.FIELD_IDSOUSTYPE;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder where = new StringBuilder();

        where.append(getWhere());

        try {
            FAApplication app = new FAApplication();
            if (!JadeStringUtil.isBlank(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()))) {
                setFromAnneeCotisation(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()));
                where.append(" and b." + FAAfact.FIELD_ANNEECOTISATION + " >= " + getFromAnneeCotisation() + " ");
            }
        } catch (Exception e) {
            JadeLogger.info(e, e.getMessage());
        }

        where.append(getGroupBy());

        where.append(",b." + FAAfact.FIELD_IDRUBRIQUE + ", a." + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", a."
                + FAEnteteFacture.FIELD_IDROLE + ", a." + FAEnteteFacture.FIELD_IDSOUSTYPE + " ");

        where.append(getHaving());
        where.append(getOrder());

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FACotiPercuEnTropParRubrique();
    }

    public String getFromAnneeCotisation() {
        return fromAnneeCotisation;
    }

    public void setFromAnneeCotisation(String fromAnneeCotisation) {
        this.fromAnneeCotisation = fromAnneeCotisation;
    }
}
