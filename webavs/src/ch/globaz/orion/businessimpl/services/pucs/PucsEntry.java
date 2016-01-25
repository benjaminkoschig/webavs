package ch.globaz.orion.businessimpl.services.pucs;

public class PucsEntry {
    /**
     * Année de la déclaration de salaire
     */
    private String anneeDeclarationSalaire = null;
    /**
     * Le status courant du fichier pucs
     */
    private String currentStatus = null;
    /**
     * Date à laquelle le fichier à été soumis
     */
    private String dateDeReception = null;
    /**
     * Utilisateur qui est entrain de traiter le fichier PUCS
     */
    private String handlingUser = null;

    /**
     * Identifiant (clé primaire) de l'entrée fichier PUCS
     */
    private String idPucsEntry = null;

    /**
     * Montant total AVS du fichier PUCS
     */
    private String montantTotalDeControl = null;
    /**
     * Le numéro de l'affilié concerné
     */
    private String nomAffilie = null;

    /**
     * Nombre de salaires traités par le fichier
     */
    private String nombreDeSalairies = null;

    /**
     * Le numéro de l'affilié concerné
     */
    private String numeroAffilie = null;
    /**
     * Le type de fichier pucs
     */
    private String type = null;

    /**
     * Constructeur
     */
    public PucsEntry() {
        super();
    }

    /**
     * @return Année de la déclaration de salaire
     */
    public String getAnneeDeclarationSalaire() {
        return anneeDeclarationSalaire;
    }

    /**
     * @return Le status courant du fichier pucs
     */
    public String getCurrentStatus() {
        return currentStatus;
    }

    /**
     * @return Date à laquelle le fichier à été soumis
     */
    public String getDateDeReception() {
        return dateDeReception;
    }

    /**
     * @return Utilisateur qui est entrain de traiter le fichier PUCS
     */
    public String getHandlingUser() {
        return handlingUser;
    }

    /**
     * @return Identifiant (clé primaire) de l'entrée fichier PUCS
     */
    public String getIdPucsEntry() {
        return idPucsEntry;
    }

    /**
     * @return Montant total AVS du fichier PUCS
     */
    public String getMontantTotalDeControl() {
        return montantTotalDeControl;
    }

    /**
     * @return Le nom de l'affilié
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * @return Nombre de salaires traités par le fichier
     */
    public String getNombreDeSalairies() {
        return nombreDeSalairies;
    }

    /**
     * @return Le numéro de l'affilié concerné
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getType() {
        return type;
    }

    /**
     * @param anneeDeclarationSalaire
     *            Année de la déclaration de salaire
     */
    public void setAnneeDeclarationSalaire(String anneeDeclarationSalaire) {
        this.anneeDeclarationSalaire = anneeDeclarationSalaire;
    }

    /**
     * @param currentStatus
     *            Le status courant du fichier pucs
     */
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * @param dateDeReception
     *            Date à laquelle le fichier à été soumis
     */
    public void setDateDeReception(String dateDeReception) {
        this.dateDeReception = dateDeReception;
    }

    /**
     * @param handlingUser
     *            Utilisateur qui est entrain de traiter le fichier PUCS
     */
    public void setHandlingUser(String handlingUser) {
        this.handlingUser = handlingUser;
    }

    /**
     * @param idPucsEntry
     *            Identifiant (clé primaire) de l'entrée fichier PUCS
     */
    public void setIdPucsEntry(String idPucsEntry) {
        this.idPucsEntry = idPucsEntry;
    }

    /**
     * @param montantTotalDeControl
     *            Montant total AVS du fichier PUCS
     */
    public void setMontantTotalDeControl(String montantTotalDeControl) {
        this.montantTotalDeControl = montantTotalDeControl;
    }

    /**
     * @param nomAffilie
     *            Le nom de l'affilié concerné
     */
    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    /**
     * @param nombreDeSalairies
     *            Nombre de salaires traités par le fichier
     */
    public void setNombreDeSalairies(String nombreDeSalairies) {
        this.nombreDeSalairies = nombreDeSalairies;
    }

    /**
     * @param numeroAffilie
     *            Le numéro de l'affilié concerné
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setType(String type) {
        this.type = type;
    }

}
