package globaz.osiris.db.irrecouvrable;

import globaz.globall.util.JANumberFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une ligne contenue dans un poste.
 * 
 * @author bjo
 * 
 */
public class CALigneDePoste implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal cotisationAvs;
    private String genreDecision;
    private String idSection;
    private String libelleSection;
    private List<String> messageErreurList;
    private BigDecimal montantAffecte;
    private BigDecimal montantDu;
    private String numeroSection;
    private BigDecimal revenuCi;
    private CATypeLigneDePoste type;

    /**
     * @param idSection
     * @param numeroSection
     * @param libelleSection
     * @param montantDu
     * @param type
     */
    public CALigneDePoste(String idSection, String numeroSection, String libelleSection, BigDecimal montantDu,
            CATypeLigneDePoste type) {
        this.idSection = idSection;
        this.numeroSection = numeroSection;
        this.libelleSection = libelleSection;
        montantAffecte = new BigDecimal(0);
        this.montantDu = montantDu;
        this.type = type;
        messageErreurList = new ArrayList<String>();
        revenuCi = new BigDecimal(0);
        cotisationAvs = new BigDecimal(0);
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
     * Additionne le montant passé en paramètre au montantAffecte
     * 
     * @param montantToAdd
     */
    public void additionnerToMontantAffecte(BigDecimal montantToAdd) {
        montantAffecte = montantAffecte.add(montantToAdd);
    }

    /**
     * Additionne le montant passé en paramètre au montantDu
     * 
     * @param montantToAdd
     */
    public void additionnerToMontantDu(BigDecimal montantToAdd) {
        montantDu = montantDu.add(montantToAdd);
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
     * Ajoute un mesage d'erreur à la ligne de poste
     * 
     * @param message
     */
    public void addMessageErreur(String message) {
        messageErreurList.add(message);
    }

    /**
     * Calcul le solde de la ligne de poste (solde = montantDu - montantAffecte)
     * 
     * @return
     */
    public BigDecimal calculerSolde() {
        return montantDu.subtract(montantAffecte);
    }

    /**
     * Calcul le solde de la ligne de poste (solde = montantDu - montantAffecte) et arrondi le montant au plus proche
     * 
     * @return
     */
    public BigDecimal calculerSoldeRound() {
        BigDecimal solde = calculerSolde();
        return JANumberFormatter.round(solde, 0.05, 2, JANumberFormatter.NEAR);
    }

    public BigDecimal getCotisationAvs() {
        return cotisationAvs;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getLibelleSection() {
        return libelleSection;
    }

    /**
     * Retourne l'ensemble des messages d'erreur de la ligne de poste
     * 
     * @return
     */
    public List<String> getMessageErreurList() {
        return messageErreurList;
    }

    public BigDecimal getMontantAffecte() {
        return montantAffecte;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public String getNumeroSection() {
        return numeroSection;
    }

    public BigDecimal getRevenuCi() {
        return revenuCi;
    }

    public CATypeLigneDePoste getType() {
        return type;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setMontantAffecte(BigDecimal montantAffecte) {
        this.montantAffecte = montantAffecte;
    }

    @Override
    public String toString() {
        String result = "";
        result += "                  ===>idSection      : " + idSection + "\n";
        result += "                  ===>numeroSection  : " + numeroSection + "\n";
        result += "                  ===>libelle        : " + libelleSection + "\n";
        result += "                  ===>montantAffecte : " + montantAffecte + "\n";
        result += "                  ===>montantDu      : " + montantDu + "\n";
        result += "                  ===>type           : " + type + "\n";
        result += "                  ===>revenuCi       : " + revenuCi + "\n";
        result += "                  ===>genreDecision  : " + genreDecision + "\n";
        result += "                  ===>cotisationAvs  : " + cotisationAvs + "\n";
        for (String messageErreur : messageErreurList) {
            result += "              	  ===>messageErreur  : " + messageErreur + "\n";
        }
        result += "\n";
        return result;
    }

}
