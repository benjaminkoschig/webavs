package ch.globaz.perseus.business.models.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class LienLocaliteSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_DATE_VALABLE_LE = "withDateValable";

    private String forDateValable = null;
    private String forIdLocalite = null;

    private String forIdZone = null;

    private String likeNpa = null;

    public LienLocaliteSearchModel() {
        super();
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdLocalite() {
        return forIdLocalite;
    }

    public String getForIdZone() {
        return forIdZone;
    }

    public String getLikeNpa() {
        return likeNpa;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
        if (!JadeStringUtil.isEmpty(forDateValable)) {
            setWhereKey(LienLocaliteSearchModel.WITH_DATE_VALABLE_LE);
        }
    }

    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

    public void setForIdZone(String forIdZone) {
        this.forIdZone = forIdZone;
    }

    public void setLikeNpa(String likeNpa) {
        this.likeNpa = likeNpa;
    }

    @Override
    public Class whichModelClass() {
        return LienLocalite.class;
    }

}
