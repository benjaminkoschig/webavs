package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Arrays;
import java.util.List;

public class AffiliationTiersSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> inTypeAffiliation;
    private String inTypeAffiliationString;// les types doivent être séparés par le caractère "_"
    private String likeNomUpper;
    private String likeNumeroAffilie = null;
    private String likePrenomUpper;

    public AffiliationTiersSearchComplexModel() {
        super();

        likeNomUpper = null;
        likeNumeroAffilie = null;
        likePrenomUpper = null;
        setInTypeAffiliation(null);
    }

    public List<String> getInTypeAffiliation() {
        return inTypeAffiliation;
    }

    public String getInTypeAffiliationString() {
        return inTypeAffiliationString;
    }

    /**
     * @deprecated use {@link #getLikeNomUpper()}
     */
    @Deprecated
    public String getLikeDesignationUpper() {
        return getLikeNomUpper();
    }

    public String getLikeNomUpper() {
        return likeNomUpper;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public String getLikePrenomUpper() {
        return likePrenomUpper;
    }

    public void setInTypeAffiliation(List<String> inTypeAffiliation) {
        this.inTypeAffiliation = inTypeAffiliation;
    }

    public void setInTypeAffiliationString(String inTypeAffiliationString) {
        this.inTypeAffiliationString = inTypeAffiliationString;
        // hack permettant de transformer les types sous formes de String (séparé par le caractère "_") en une liste de
        // types. Cela est nécessaire car le widget ne gère pas le type List et ne permet pas non plus de transmettre la
        // liste des types séparés par des "," (jason)
        inTypeAffiliation = Arrays.asList(inTypeAffiliationString.split("_"));
    }

    /**
     * @deprecated use {@link #setLikeNomUpper(String)}
     */
    @Deprecated
    public void setLikeDesignationUpper(String likeDesignationUpper) {
        setLikeNomUpper(likeDesignationUpper);
    }

    public void setLikeNomUpper(String likeNomUpper) {
        this.likeNomUpper = likeNomUpper;
    }

    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    public void setLikePrenomUpper(String likePrenomUpper) {
        this.likePrenomUpper = likePrenomUpper;
    }

    @Override
    public Class<AffiliationTiersComplexModel> whichModelClass() {
        return AffiliationTiersComplexModel.class;
    }
}
