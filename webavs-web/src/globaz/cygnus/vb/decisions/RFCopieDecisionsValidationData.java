/*
 * Créé le 06 avril 2010
 */
package globaz.cygnus.vb.decisions;

/**
 * author : fha
 * 
 * création d'un objet composé de 3 strings : fournisseur, type de soin et sous type de soin utilisé plus tard pour
 * afficher le tableau Fournisseur-Type qui est une liste d'objets RFFournisseurType
 */
public class RFCopieDecisionsValidationData implements Comparable<String> {

    private String descDestinataire = "";

    private Boolean hasAnnexes = Boolean.FALSE;

    private Boolean hasCopies = Boolean.FALSE;

    private Boolean hasMoyensDroit = Boolean.FALSE;

    private Boolean hasPageDeGarde = Boolean.FALSE;

    private Boolean hasRemarque = Boolean.FALSE;

    private Boolean hasSignature = Boolean.FALSE;

    /* fields - l'idDestinataire doit être unique */
    private String idCopie = "";

    private String idDestinataire = "";

    private Boolean isDecompte = Boolean.FALSE;

    private Boolean isEnDB = Boolean.FALSE;

    private Boolean isVersement = Boolean.FALSE;

    /* constructor */

    public RFCopieDecisionsValidationData() {
        super();
    }

    public RFCopieDecisionsValidationData(String idCopie, String idDestinataire, String descDestinataire,
            Boolean hasPageDeGarde, Boolean isVersement, Boolean isDecompte, Boolean hasRemarque,
            Boolean hasMoyensDroit, Boolean hasSignature, Boolean hasAnnexes, Boolean hasCopies) {
        super();
        this.idCopie = idCopie;
        this.idDestinataire = idDestinataire;
        this.descDestinataire = descDestinataire;
        this.hasPageDeGarde = hasPageDeGarde;
        this.isVersement = isVersement;
        this.isDecompte = isDecompte;
        this.hasRemarque = hasRemarque;
        this.hasMoyensDroit = hasMoyensDroit;
        this.hasSignature = hasSignature;
        this.hasAnnexes = hasAnnexes;
        this.hasCopies = hasCopies;
    }

    public RFCopieDecisionsValidationData(String idCopie, String idDestinataire, String descDestinataire,
            Boolean hasPageDeGarde, Boolean isVersement, Boolean isDecompte, Boolean hasRemarque,
            Boolean hasMoyensDroit, Boolean hasSignature, Boolean hasAnnexes, Boolean hasCopies, Boolean isEnDB) {
        super();
        this.idCopie = idCopie;
        this.idDestinataire = idDestinataire;
        this.descDestinataire = descDestinataire;
        this.hasPageDeGarde = hasPageDeGarde;
        this.isVersement = isVersement;
        this.isDecompte = isDecompte;
        this.hasRemarque = hasRemarque;
        this.hasMoyensDroit = hasMoyensDroit;
        this.hasSignature = hasSignature;
        this.hasAnnexes = hasAnnexes;
        this.hasCopies = hasCopies;
        this.isEnDB = isEnDB;
    }

    @Override
    public int compareTo(String destinataire) {
        // TODO Auto-generated method stub
        return idDestinataire.compareTo(destinataire);
    }

    @Override
    public boolean equals(Object obj) {

        RFCopieDecisionsValidationData other = (RFCopieDecisionsValidationData) obj;

        if (!idCopie.equals(other.idCopie)) {
            return false;
        }
        if (!idDestinataire.equals(other.idDestinataire)) {
            return false;
        }
        if (!descDestinataire.equals(other.descDestinataire)) {
            return false;
        }
        if (!hasPageDeGarde.equals(other.hasPageDeGarde)) {
            return false;
        }
        if (!isVersement.equals(other.isVersement)) {
            return false;
        }
        if (!isDecompte.equals(other.isDecompte)) {
            return false;
        }
        if (!hasRemarque.equals(other.hasRemarque)) {
            return false;
        }
        if (!hasMoyensDroit.equals(other.hasMoyensDroit)) {
            return false;
        }
        if (!hasSignature.equals(other.hasSignature)) {
            return false;
        }
        if (!hasAnnexes.equals(other.hasAnnexes)) {
            return false;
        }
        if (!hasCopies.equals(other.hasCopies)) {
            return false;
        }
        if (!isEnDB.equals(other.hasCopies)) {
            return false;
        }

        return true;
    }

    public String getDescDestinataire() {
        return descDestinataire;
    }

    public Boolean getHasAnnexes() {
        return hasAnnexes;
    }

    public Boolean getHasCopies() {
        return hasCopies;
    }

    public Boolean getHasMoyensDroit() {
        return hasMoyensDroit;
    }

    public Boolean getHasPageDeGarde() {
        return hasPageDeGarde;
    }

    public Boolean getHasRemarque() {
        return hasRemarque;
    }

    public Boolean getHasSignature() {
        return hasSignature;
    }

    public String getIdCopie() {
        return idCopie;
    }

    public String getIdDestinataire() {
        return idDestinataire;
    }

    public Boolean getIsDecompte() {
        return isDecompte;
    }

    public Boolean getIsEnDB() {
        return isEnDB;
    }

    public Boolean getIsVersement() {
        return isVersement;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCopie == null) ? 0 : idCopie.hashCode());
        result = prime * result + ((idDestinataire == null) ? 0 : idDestinataire.hashCode());
        result = prime * result + ((descDestinataire == null) ? 0 : descDestinataire.hashCode());
        result = prime * result + ((hasPageDeGarde == null) ? 0 : hasPageDeGarde.hashCode());
        result = prime * result + ((isVersement == null) ? 0 : isVersement.hashCode());
        result = prime * result + ((isDecompte == null) ? 0 : isDecompte.hashCode());
        result = prime * result + ((hasRemarque == null) ? 0 : hasRemarque.hashCode());
        result = prime * result + ((hasMoyensDroit == null) ? 0 : hasMoyensDroit.hashCode());
        result = prime * result + ((hasSignature == null) ? 0 : hasSignature.hashCode());
        result = prime * result + ((hasAnnexes == null) ? 0 : hasAnnexes.hashCode());
        result = prime * result + ((hasCopies == null) ? 0 : hasCopies.hashCode());
        result = prime * result + ((isEnDB == null) ? 0 : isEnDB.hashCode());
        return result;
    }

    public void setDescDestinataire(String descDestinataire) {
        this.descDestinataire = descDestinataire;
    }

    public void setHasAnnexes(Boolean hasAnnexes) {
        this.hasAnnexes = hasAnnexes;
    }

    public void setHasCopies(Boolean hasCopies) {
        this.hasCopies = hasCopies;
    }

    public void setHasMoyensDroit(Boolean hasMoyensDroit) {
        this.hasMoyensDroit = hasMoyensDroit;
    }

    public void setHasPageDeGarde(Boolean hasPageDeGarde) {
        this.hasPageDeGarde = hasPageDeGarde;
    }

    public void setHasRemarque(Boolean hasRemarque) {
        this.hasRemarque = hasRemarque;
    }

    public void setHasSignature(Boolean hasSignature) {
        this.hasSignature = hasSignature;
    }

    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    public void setIdDestinataire(String idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    public void setIsDecompte(Boolean isDecompte) {
        this.isDecompte = isDecompte;
    }

    public void setIsEnDB(Boolean isEnDB) {
        this.isEnDB = isEnDB;
    }

    public void setIsVersement(Boolean isVersement) {
        this.isVersement = isVersement;
    }

}
