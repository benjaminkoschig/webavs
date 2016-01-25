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
    private String inTypeAffiliationString;// les types doivent �tre s�par�s par le caract�re "_"
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
        // hack permettant de transformer les types sous formes de String (s�par� par le caract�re "_") en une liste de
        // types. Cela est n�cessaire car le widget ne g�re pas le type List et ne permet pas non plus de transmettre la
        // liste des types s�par�s par des "," (jason)
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
