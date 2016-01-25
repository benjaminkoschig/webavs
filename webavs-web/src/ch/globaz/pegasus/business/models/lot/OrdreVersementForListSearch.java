package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class OrdreVersementForListSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDRE_BY_IDLOT_CS_TYPE = "csTypeOv";

    public static String getOrdreByIdlotCsType() {
        return OrdreVersementForListSearch.ORDRE_BY_IDLOT_CS_TYPE;
    }

    private Collection<String> forCsInTypeOv = null;
    private String forIdLot = null;
    private String forIdPrestation = null;
    private String forIdVersionDroit = null;
    private Collection<String> forInIdVersionDroit = null;

    public Collection<String> getForCsInTypeOv() {
        return forCsInTypeOv;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Collection<String> getForInIdVersionDroit() {
        return forInIdVersionDroit;
    }

    public void setForCsInTypeOv(Collection<String> forCsInTypeOv) {
        this.forCsInTypeOv = forCsInTypeOv;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForInIdVersionDroit(Collection<String> forInIdVersionDroit) {
        this.forInIdVersionDroit = forInIdVersionDroit;
    }

    @Override
    public Class<OrdreVersementForList> whichModelClass() {
        return OrdreVersementForList.class;
    }
}
