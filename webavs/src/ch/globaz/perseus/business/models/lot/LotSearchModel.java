package ch.globaz.perseus.business.models.lot;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class LotSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_BY_DATE_COMPTABILISATION = "dateComptabilisation";
    public final static String WITH_DATE_COMPTABILISATION_LE = "withDateComptabilisation";
    public final static String WITH_DATE_VALABLE_LE = "withMoisValable";

    private String forDateDebut = null;
    private String forDateFin = null;
    private String forEtatCs = null;
    private String forMoisComptabilisation = null;
    private String forMoisValable = null;
    private String forNotEtatCs = null;
    private String forTypeLot = null;

    public LotSearchModel() {
        super();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForEtatCs() {
        return forEtatCs;
    }

    /**
     * @return the forMoisComptabilisation
     */
    public String getForMoisComptabilisation() {
        return forMoisComptabilisation;
    }

    /**
     * @return the forDateValable
     */
    public String getForMoisValable() {
        return forMoisValable;
    }

    /**
     * @return the forNotEtatCs
     */
    public String getForNotEtatCs() {
        return forNotEtatCs;
    }

    public String getForTypeLot() {
        return forTypeLot;
    }

    public void setForEtatCs(String forEtatCs) {
        this.forEtatCs = forEtatCs;
    }

    /**
     * @param forMoisComptabilisation
     *            the forMoisComptabilisation to set
     */
    public void setForMoisComptabilisation(String forMoisComptabilisation) {
        this.forMoisComptabilisation = forMoisComptabilisation;
        if (!JadeStringUtil.isEmpty(forMoisComptabilisation)) {
            setWhereKey(LotSearchModel.WITH_DATE_COMPTABILISATION_LE);
            forDateDebut = "01." + forMoisComptabilisation;
            forDateFin = "01." + forMoisComptabilisation;
            forDateFin = JadeDateUtil.addMonths(forDateFin, 1);
            forDateFin = JadeDateUtil.addDays(forDateFin, -1);
        }
    }

    public void setForMoisValable(String forMoisValable) {
        this.forMoisValable = forMoisValable;
        if (!JadeStringUtil.isEmpty(forMoisValable)) {
            setWhereKey(LotSearchModel.WITH_DATE_VALABLE_LE);
            forDateDebut = "01." + forMoisValable;
            forDateFin = "01." + forMoisValable;
            forDateFin = JadeDateUtil.addMonths(forDateFin, 1);
            forDateFin = JadeDateUtil.addDays(forDateFin, -1);
        }
    }

    /**
     * @param forNotEtatCs
     *            the forNotEtatCs to set
     */
    public void setForNotEtatCs(String forNotEtatCs) {
        this.forNotEtatCs = forNotEtatCs;
    }

    public void setForTypeLot(String forTypeLot) {
        this.forTypeLot = forTypeLot;
    }

    @Override
    public Class whichModelClass() {
        return Lot.class;
    }

}
