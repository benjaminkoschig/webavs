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
        StringBuilder where = new StringBuilder();

        where.append(getWhere());

        // K150915_057, d�placement du having sum(...) > | < 0 dans le where pour les cotisations per�ues en trop.
        where.append(" and b." + FAAfact.FIELD_MONTANTFACTURE);
        where.append(isForMontantPositif() ? " > " : " < ").append(" 0 ");

        try {
            FAApplication app = new FAApplication();
            if (!JadeStringUtil.isBlank(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()))) {
                setFromAnneeCotisation(app.getAnneeDonneesComptablesCompletes(statement.getTransaction()));
                where.append(" and b." + FAAfact.FIELD_ANNEECOTISATION + " >= " + getFromAnneeCotisation() + " ");
            }
        } catch (Exception e) {
            JadeLogger.info(e, e.getMessage());
            // do nothing
        }

        where.append(getGroupBy());

        where.append(",b." + FAAfact.FIELD_IDRUBRIQUE + ", a." + FAEnteteFacture.FIELD_IDEXTERNEROLE + ", a."
                + FAEnteteFacture.FIELD_IDROLE + ", a." + FAEnteteFacture.FIELD_IDSOUSTYPE + " ");

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
