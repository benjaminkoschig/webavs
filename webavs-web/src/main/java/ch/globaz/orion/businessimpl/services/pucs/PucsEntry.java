package ch.globaz.orion.businessimpl.services.pucs;

public class PucsEntry {
    /**
     * Ann�e de la d�claration de salaire
     */
    private String anneeDeclarationSalaire = null;
    /**
     * Le status courant du fichier pucs
     */
    private String currentStatus = null;
    /**
     * Date � laquelle le fichier � �t� soumis
     */
    private String dateDeReception = null;
    /**
     * Utilisateur qui est entrain de traiter le fichier PUCS
     */
    private String handlingUser = null;

    /**
     * Identifiant (cl� primaire) de l'entr�e fichier PUCS
     */
    private String idPucsEntry = null;

    /**
     * Montant total AVS du fichier PUCS
     */
    private String montantTotalDeControl = null;
    /**
     * Le num�ro de l'affili� concern�
     */
    private String nomAffilie = null;

    /**
     * Nombre de salaires trait�s par le fichier
     */
    private String nombreDeSalairies = null;

    /**
     * Le num�ro de l'affili� concern�
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
     * @return Ann�e de la d�claration de salaire
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
     * @return Date � laquelle le fichier � �t� soumis
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
     * @return Identifiant (cl� primaire) de l'entr�e fichier PUCS
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
     * @return Le nom de l'affili�
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * @return Nombre de salaires trait�s par le fichier
     */
    public String getNombreDeSalairies() {
        return nombreDeSalairies;
    }

    /**
     * @return Le num�ro de l'affili� concern�
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getType() {
        return type;
    }

    /**
     * @param anneeDeclarationSalaire
     *            Ann�e de la d�claration de salaire
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
     *            Date � laquelle le fichier � �t� soumis
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
     *            Identifiant (cl� primaire) de l'entr�e fichier PUCS
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
     *            Le nom de l'affili� concern�
     */
    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    /**
     * @param nombreDeSalairies
     *            Nombre de salaires trait�s par le fichier
     */
    public void setNombreDeSalairies(String nombreDeSalairies) {
        this.nombreDeSalairies = nombreDeSalairies;
    }

    /**
     * @param numeroAffilie
     *            Le num�ro de l'affili� concern�
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setType(String type) {
        this.type = type;
    }

}
