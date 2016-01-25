package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un poste de recouvrement dans le cadre de la ventilation (contentieux)
 * 
 * @author sch
 * 
 */
public class CARecouvrementPoste implements Serializable {
    private static final long serialVersionUID = -5512738544378713947L;

    private Integer annee;
    private BigDecimal cotisationAvs;
    private BigDecimal cumulCotisationAmortie;
    private BigDecimal cumulRecouvrementCotisationAmortie;
    private BigDecimal cumulCotisationAmortieCorrigee;
    private BigDecimal cumulRecouvrementCotisationAmortieCorrigee;
    private String genreDecision;
    private String idRubrique;
    private String idRubriqueIrrecouvrable;
    private String idRubriqueRecouvrement;
    private boolean isRecouvrementPosteOnError;
    private String libelleRubriqueRecouvrement;
    private List<String> messageErreurList;
    private String numeroRubriqueIrrecouvrable;
    private String numeroRubriqueRecouvrement;
    private String ordrePriorite;
    private BigDecimal recouvrement;
    private BigDecimal revenuCi;
    private BigDecimal valeurInitialeCotAmortie;
    private BigDecimal valeurInitialeCotRecouvrement;
    private CATypeDeRecouvrementPoste type;
    private BigDecimal montantNoteDeCredit;
    private CARecouvrementKeyPosteContainer recouvrementKeyPosteContainerPrincipal;

    /**
     * @return the montantNoteDeCredit
     */
    public BigDecimal getMontantNoteDeCredit() {
        return montantNoteDeCredit;
    }

    /**
     * Constructeur de CARecouvrementPoste
     * 
     * @param annee
     * @param idRubrique
     * @param idRubriqueIrrecouvrable
     * @param numeroRubriqueIrrecouvrable
     * @param idRubriqueRecouvrement
     * @param numeroRubriqueRecouvrement
     * @param libelleRubriqueRecouvrement
     * @param cumulCotisationAmortie
     * @param cumulCotisationAmortieCorrigee
     * @param cumulRecouvrementCotisationAmortie
     * @param cumulRecouvrementCotisationAmortieCorrigee
     * @param recouvrement
     * @param ordrePriorite
     * @param valeurInitialeCotAmortie
     * @param valeurInitialeCotRecouvrement
     * @param montantNoteDeCredit
     * @param type
     */
    public CARecouvrementPoste(Integer annee, String idRubrique, String idRubriqueIrrecouvrable,
            String numeroRubriqueIrrecouvrable, String idRubriqueRecouvrement, String numeroRubriqueRecouvrement,
            String libelleRubriqueRecouvrement, BigDecimal cumulCotisationAmortie,
            BigDecimal cumulCotisationAmortieCorrigee, BigDecimal cumulRecouvrementCotisationAmortie,
            BigDecimal cumulRecouvrementCotisationAmortieCorrigee, BigDecimal recouvrement, String ordrePriorite,
            BigDecimal valeurInitialeCotAmortie, BigDecimal valeurInitialeCotRecouvrement,
            BigDecimal montantNoteDeCredit, CATypeDeRecouvrementPoste type) {
        this.annee = annee;
        this.idRubrique = idRubrique;
        this.idRubriqueIrrecouvrable = idRubriqueIrrecouvrable;
        this.idRubriqueRecouvrement = idRubriqueRecouvrement;
        this.numeroRubriqueIrrecouvrable = numeroRubriqueIrrecouvrable;
        this.numeroRubriqueRecouvrement = numeroRubriqueRecouvrement;
        this.libelleRubriqueRecouvrement = libelleRubriqueRecouvrement;
        this.cumulCotisationAmortie = cumulCotisationAmortie;
        this.cumulCotisationAmortieCorrigee = cumulCotisationAmortieCorrigee;
        this.cumulRecouvrementCotisationAmortie = cumulRecouvrementCotisationAmortie;
        this.cumulRecouvrementCotisationAmortieCorrigee = cumulRecouvrementCotisationAmortieCorrigee;
        this.recouvrement = recouvrement;
        this.ordrePriorite = ordrePriorite;
        this.valeurInitialeCotAmortie = valeurInitialeCotAmortie;
        this.valeurInitialeCotRecouvrement = valeurInitialeCotRecouvrement;
        isRecouvrementPosteOnError = false;
        messageErreurList = new ArrayList<String>();
        revenuCi = new BigDecimal(0);
        cotisationAvs = new BigDecimal(0);
        this.montantNoteDeCredit = montantNoteDeCredit;
        this.type = type;
    }

    /**
     * Retourne le type (employeur, salarié ou simple)
     * 
     * @return CATypeDeRecouvrementPoste
     */
    public CATypeDeRecouvrementPoste getType() {
        return type;
    }

    /**
     * Additionne le montant passé en paramètre à la cotisationAvs
     * 
     * @param cotisationAvsToAdd
     */
    public void additionnerToCotisationAvs(BigDecimal cotisationAvsToAdd) {
        cotisationAvs = cotisationAvs.add(cotisationAvsToAdd);
    }

    /**
     * Additionne le montant passé en paramètre au montant de recouvrement
     * 
     * @param cotisationAvsToAdd
     */
    public void additionnerToRecouvrement(BigDecimal recouvrementToAdd) {
        recouvrement = recouvrement.add(recouvrementToAdd);
    }

    /**
     * Additionne le montant passé en paramètre au revenuCi
     * 
     * @param revenuCiToAdd
     */
    public void additionnerToRevenuCi(BigDecimal revenuCiToAdd) {
        revenuCi = revenuCi.add(revenuCiToAdd);
    }

    /**
     * Additionne le montant passé en paramètre au montant de la note de crédit
     * 
     * @param montantNoteDeCreditToAdd
     */
    public void additionnerToMontantNoteDeCredit(BigDecimal montantNoteDeCreditToAdd) {
        montantNoteDeCredit = montantNoteDeCredit.add(montantNoteDeCreditToAdd);
    }

    /**
     * Ajoute un mesage d'erreur dans le recouvrementPoste
     * 
     * @param message
     */
    public void addMessageErreur(String message) {
        messageErreurList.add(message);
    }

    /**
     * Retourne l'année
     * 
     * @return Integer annee
     */
    public Integer getAnnee() {
        return annee;
    }

    /**
     * Retourne le montant de la cotisation AVS
     * 
     * @return BigDecimal
     */
    public BigDecimal getCotisationAvs() {
        return cotisationAvs;
    }

    /**
     * Retourne le cumul des cotisations amorties
     * 
     * @return BigDecimal
     */
    public BigDecimal getCumulCotisationAmortie() {
        return cumulCotisationAmortie;
    }

    /**
     * Retourne le cumul des recouvrements des cotisations amorties
     * 
     * @return BigDecimal
     */
    public BigDecimal getCumulRecouvrementCotisationAmortie() {
        return cumulRecouvrementCotisationAmortie;
    }

    /**
     * Retourne le genre de décision cot. pers.
     * 
     * @return String
     */
    public String getGenreDecision() {
        return genreDecision;
    }

    /**
     * Retourne l'identifiant de la rubrique
     * 
     * @return String
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Retourne l'identifiant de la rubrique irrécouvrable
     * 
     * @return String
     */
    public String getIdRubriqueIrrecouvrable() {
        return idRubriqueIrrecouvrable;
    }

    /**
     * Retourne l'identifiant de la rubrique de recouvrement
     * 
     * @return String
     */
    public String getIdRubriqueRecouvrement() {
        return idRubriqueRecouvrement;
    }

    /**
     * Retourne le libellé de la rubrique de recouvrement
     * 
     * @return String
     */
    public String getLibelleRubriqueRecouvrement() {
        return libelleRubriqueRecouvrement;
    }

    /**
     * Retourne l'ensemble des messages d'erreur du recouvrementPoste
     * 
     * @return List<String>
     */
    public List<String> getMessageErreurList() {
        return messageErreurList;
    }

    /**
     * Retourne le numéro de la rubrique irrécouvrable
     * 
     * @return String
     */
    public String getNumeroRubriqueIrrecouvrable() {
        return numeroRubriqueIrrecouvrable;
    }

    /**
     * Retourne le numéro de la rubrique de recouvrement
     * 
     * @return String
     */
    public String getNumeroRubriqueRecouvrement() {
        return numeroRubriqueRecouvrement;
    }

    /**
     * Retourne l'ordre de priorité
     * 
     * @return String
     */
    public String getOrdrePriorite() {
        return ordrePriorite;
    }

    /**
     * Retourne le montant du recouvrement
     * 
     * @return BigDecimal
     */
    public BigDecimal getRecouvrement() {
        return recouvrement;
    }

    /**
     * Retourne le montant du revenu CI
     * 
     * @return BigDecimal
     */
    public BigDecimal getRevenuCi() {
        return revenuCi;
    }

    /**
     * Retourne le solde disponible d'un poste (cumulCotisationAmortie - cumulRecouvrementCotisationAmortie)
     * 
     * @return BigDecimal
     */
    public BigDecimal getSoldeDisponible() {
        BigDecimal soldeDisponible = new BigDecimal(0);
        soldeDisponible = getCumulCotisationAmortie().subtract(getCumulRecouvrementCotisationAmortie());

        return soldeDisponible;
    }

    public BigDecimal getCumulCotisationAmortieCorrigee() {
        return cumulCotisationAmortieCorrigee;
    }

    public BigDecimal getCumulRecouvrementCotisationAmortieCorrigee() {
        return cumulRecouvrementCotisationAmortieCorrigee;
    }

    /**
     * Retourne true si le recouvrementPoste est en erreur
     * 
     * @return true si recouvrementPoste
     */
    public boolean isRecouvrementPosteOnError() {
        return isRecouvrementPosteOnError;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setRecouvrementPosteOnError(boolean isRecouvrementPosteOnError) {
        this.isRecouvrementPosteOnError = isRecouvrementPosteOnError;
    }

    public BigDecimal getValeurInitialeCotAmortie() {
        return valeurInitialeCotAmortie;
    }

    public BigDecimal getValeurInitialeCotRecouvrement() {
        return valeurInitialeCotRecouvrement;
    }

    @Override
    public String toString() {
        String result = "";
        result += "           annee                                      : " + annee + "\n";
        result += "           idRubrique                                 : " + idRubrique + "\n";
        result += "           idRubriqueIrrecouvrable                    : " + idRubriqueIrrecouvrable + "\n";
        result += "           idRubriqueRecouvrement                     : " + idRubriqueRecouvrement + "\n";
        result += "           numeroRubriqueIrrecouvrable                : " + numeroRubriqueIrrecouvrable + "\n";
        result += "           numeroRubriqueRecouvrement                 : " + numeroRubriqueRecouvrement + "\n";
        result += "           libelleRubriqueRecouvrement                : " + libelleRubriqueRecouvrement + "\n";
        result += "           cumulCotisationAmortie                     : " + cumulCotisationAmortie + "\n";
        result += "           cumulCotisationAmortieCorrigee             : " + cumulCotisationAmortieCorrigee + "\n";
        result += "           cumulRecouvrementCotisationAmortie         : " + cumulRecouvrementCotisationAmortie
                + "\n";
        result += "           cumulRecouvrementCotisationAmortieCorrigee : "
                + cumulRecouvrementCotisationAmortieCorrigee + "\n";
        result += "           recouvrement                               : " + recouvrement + "\n";
        result += "           ordrePriorite                              : " + ordrePriorite + "\n";
        result += "           revenuCi                                   : " + revenuCi + "\n";
        result += "           cotisationAvs                              : " + cotisationAvs + "\n";
        result += "           genreDecision                              : " + genreDecision + "\n";
        result += "           valeurInitialeCotAmortie                   : " + valeurInitialeCotAmortie + "\n";
        result += "           valeurInitialeCotRecouvrement              : " + valeurInitialeCotRecouvrement + "\n";
        result += "           isRecouvrementPosteOnError                 : " + isRecouvrementPosteOnError + "\n";
        result += "           montantNoteDeCredit                        : " + montantNoteDeCredit + "\n";
        result += "           type                                       : " + type + "\n";
        result += "           recouvrementKeyPosteContainerPrincipal     : " + recouvrementKeyPosteContainerPrincipal
                + "\n";
        for (String messageErreur : messageErreurList) {
            result += "              	  ===>messageErreur  : " + messageErreur + "\n";
        }

        return result;
    }

    /**
     * @return the recouvrementKeyPosteContainerPrincipal
     */
    public CARecouvrementKeyPosteContainer getRecouvrementKeyPosteContainerPrincipal() {
        return recouvrementKeyPosteContainerPrincipal;
    }

    /**
     * @param recouvrementKeyPosteContainerPrincipal the recouvrementKeyPosteContainerPrincipal to set
     */
    public void setRecouvrementKeyPosteContainerPrincipal(
            CARecouvrementKeyPosteContainer recouvrementKeyPosteContainerPrincipal) {
        this.recouvrementKeyPosteContainerPrincipal = recouvrementKeyPosteContainerPrincipal;
    }
}
