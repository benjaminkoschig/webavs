package globaz.apg.pojo;

/**
 * Le but de cette classe est de stocker les critères de recherche de l'écran des annonces
 * 
 * @author lga
 */
public class APAnnonceDTO {

    public static final String PARAMETER_KEY_BPID = "forBusinessProcessId";
    public static final String PARAMETER_KEY_ETAT = "forEtat";
    public static final String PARAMETER_KEY_FULLNSS = "forNss";
    public static final String PARAMETER_KEY_MOIS_ANNEE_COMPTABLE = "forMoisAnneeComptable";
    public static final String PARAMETER_KEY_NSS = "partialforNss";
    public static final String PARAMETER_KEY_ORDER_BY = "orderBy";
    public static final String PARAMETER_KEY_TYPE = "forType";

    public static String getForNss() {
        return APAnnonceDTO.PARAMETER_KEY_FULLNSS;
    }

    private String forBusinessProcessId;
    private String forEtat;
    private String forMoisAnneeComptable;
    private String forNss;
    private String forType;
    private String orderBy;
    private String partialforNss;

    public final String getForBusinessProcessId() {
        return forBusinessProcessId;
    }

    public final String getForEtat() {
        return forEtat;
    }

    public final String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    public final String getForType() {
        return forType;
    }

    public final String getOrderBy() {
        return orderBy;
    }

    public final String getPartialforNss() {
        return partialforNss;
    }

    public final void setForBusinessProcessId(String forBusinessProcessId) {
        this.forBusinessProcessId = forBusinessProcessId;
    }

    public final void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public final void setForMoisAnneeComptable(String forMoisAnneeComptable) {
        this.forMoisAnneeComptable = forMoisAnneeComptable;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    public final void setForType(String forType) {
        this.forType = forType;
    }

    public final void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public final void setPartialforNss(String partialforNss) {
        this.partialforNss = partialforNss;
    }

}
