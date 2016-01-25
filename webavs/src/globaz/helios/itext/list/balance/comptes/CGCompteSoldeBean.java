/**
 *
 */
package globaz.helios.itext.list.balance.comptes;

import globaz.helios.db.comptes.CGCompte;
import java.math.BigDecimal;

/**
 * @author sel
 * 
 *         Bean contenant toutes les informations nécessaire à la constructions des listes : Balance des comptes ; Bilan
 *         ;
 *         Pertes et profits ; Plan comptable et Analyse budgetaire.
 */
public class CGCompteSoldeBean {
    /**
     * Montant provenant de CGSolde
     */
    private Double budget = null;
    private Double budgetPourcent = null;
    /**
     * Niveau de classification calculé
     */
    private String col1_niveau = "";
    private String col2_noClasseLibelle = "";
    private String col3_noCompteLibelle = "";
    /**
     * Contient le montant doit ou doitProvisoire de CGSolde
     */
    private Double col4_mvtDebit = null;
    /**
     * Total des débits pour un niveau
     */
    private Double col4_mvtDebitTotal = null;
    private Double col5_mvtCredit = null;
    private Double col5_mvtCreditTotal = null;
    private String idCompte = null;
    private String idDomaine = "";
    /**
     * Indique le genre : Actif ou passif. Utilisé dans le Bilan.
     */
    private String idGenre = "";
    private String idNature = "";

    private String monnaieCodeIso = "";
    /**
     * Monnaie étrangère
     */
    private Double mvtCreditMonnaie = null;
    private String mvtCreditMonnaieString = null;
    private Double mvtDebitMonnaie = null;
    private String mvtDebitMonnaieString = null;
    /**
     * Numéro du compte ou de la classification.
     */
    private String noClasse = "";
    /**
     * Utilisé dans la liste de perte et profit.<br>
     * resultat = totalCharges - totalProduits
     */
    private Double resultat = null;

    /**
     * Solde présent dans la table CGSOLDP. Correspond à la colonne SOLDE ou SOLDEPROVISOIRE selon la selection
     */
    private Double solde = null;

    private String soldeCreditMonnaie = null;
    private String soldeDebitMonnaie = null;
    private Double soldeExerciceComparaison = null;

    private Double soldeMonnaie = null;
    private Double totalActif = null;
    private Double totalBudget = null;
    private Double totalCharges = null;
    private Double totalPassif = null;
    private Double totalProduits = null;
    /**
     * total du solde par niveau
     */
    private Double totalSolde = null;
    private Double totalSoldeExerciceComparaison = null;

    /**
     *
     */
    public CGCompteSoldeBean() {
    }

    /**
     * Copie un bean
     * 
     * @param bean
     */
    public CGCompteSoldeBean(CGCompteSoldeBean bean) {
        setNiveau(bean.getNiveau());
        setNoClasseLibelle(bean.getNoClasseLibelle());
        setNoCompteLibelle(bean.getNoCompteLibelle());
        setMvtDebit(bean.getMvtDebit());
        setMvtDebitTotal(bean.getMvtDebitTotal());
        setMvtCredit(bean.getMvtCredit());
        setMvtCreditTotal(bean.getMvtCreditTotal());
        setNoClasse(bean.getNoClasse());
        setIdGenre(bean.getIdGenre());
        setIdDomaine(bean.getIdDomaine());
        setIdNature(bean.getIdNature());
        setMonnaieCodeIso(bean.getMonnaieCodeIso());
        setTotalActif(bean.getTotalActif());
        setTotalPassif(bean.getTotalPassif());
        setTotalCharges(bean.getTotalCharges());
        setTotalProduits(bean.getTotalProduits());
        setBudget(bean.getBudget());
        setBudgetPourcent(bean.getBudgetPourcent());
        setSolde(bean.getSolde());
        setIdCompte(bean.getIdCompte());
        setTotalBudget(bean.getTotalBudget());
        setMvtCreditMonnaie(bean.getMvtCreditMonnaie());
        setMvtDebitMonnaie(bean.getMvtDebitMonnaie());
        setSoldeMonnaie(bean.getSoldeMonnaie());
        setMvtCreditMonnaieString(bean.getMvtCreditMonnaieString());
        setMvtDebitMonnaieString(bean.getMvtDebitMonnaieString());
        setSoldeCreditMonnaie(bean.getSoldeDebitMonnaie());
        setSoldeDebitMonnaie(bean.getSoldeDebitMonnaie());
        setTotalSolde(bean.getTotalSolde());
        setTotalSoldeExerciceComparaison(bean.getTotalSoldeExerciceComparaison());
    }

    /**
     * @param creditToAdd
     */
    public void addToActifTotal(Double actifToAdd) {
        if (actifToAdd == null) {
            return;
        }
        Double actifTotal = getTotalActif() == null ? 0 : getTotalActif();
        setTotalActif(actifTotal + actifToAdd);
    }

    /**
     * @param budget
     */
    public void addToBudgetTotal(Double budgetToAdd) {
        if (budgetToAdd == null) {
            return;
        }
        Double budgetTotal = getTotalBudget() == null ? 0 : getTotalBudget();
        setTotalBudget(budgetTotal + budgetToAdd);
    }

    /**
     * @param produitToAdd
     */
    public void addToChargeTotal(Double chargeToAdd) {
        if (chargeToAdd == null) {
            return;
        }
        Double chargeTotal = getTotalCharges() == null ? 0 : getTotalCharges();
        setTotalCharges(chargeTotal + chargeToAdd);
    }

    /**
     * @param creditToAdd
     */
    public void addToCreditTotal(Double creditToAdd) {
        if (creditToAdd == null) {
            return;
        }
        Double creditTotal = getMvtCreditTotal() == null ? 0 : getMvtCreditTotal();
        setMvtCreditTotal(creditTotal + creditToAdd);
    }

    /**
     * @param debitToAdd
     */
    public void addToDebitTotal(Double debitToAdd) {
        if (debitToAdd == null) {
            return;
        }
        Double debitTotal = getMvtDebitTotal() == null ? 0 : getMvtDebitTotal();
        setMvtDebitTotal(debitTotal + debitToAdd);
    }

    /**
     * @param creditToAdd
     */
    public void addToPassifTotal(Double passifToAdd) {
        if (passifToAdd == null) {
            return;
        }
        Double passifTotal = getTotalPassif() == null ? 0 : getTotalPassif();
        setTotalPassif(passifTotal + passifToAdd);
    }

    /**
     * @param produitToAdd
     */
    public void addToProduitTotal(Double produitToAdd) {
        if (produitToAdd == null) {
            return;
        }
        Double produitTotal = getTotalProduits() == null ? 0 : getTotalProduits();
        setTotalProduits(produitTotal + produitToAdd);
    }

    /**
     * @param soldeToAdd
     */
    public void addToSoldeExerciceComparaisonTotal(Double soldeToAdd) {
        if (soldeToAdd == null) {
            return;
        }
        Double soldeTotal = getTotalSoldeExerciceComparaison() == null ? 0 : getTotalSoldeExerciceComparaison();
        setTotalSoldeExerciceComparaison(soldeTotal + soldeToAdd);
    }

    /**
     * Retourne la valeur.<br/>
     * Si budget ou solde sont null, retourne null.
     * 
     * @return
     */
    public Double computeBudgetPourcent() {
        if ((getBudget() == null) || (getSolde() == null)) {
            return null;
        }
        BigDecimal budgetBD = new BigDecimal(getBudget());
        if (budgetBD.compareTo(new BigDecimal(0)) != 0) {
            BigDecimal soldeBD = new BigDecimal(getSolde());
            soldeBD = soldeBD.divide(budgetBD, 3, BigDecimal.ROUND_HALF_DOWN);

            return soldeBD.doubleValue();
        }
        return getBudgetPourcent();
    }

    /**
     * @return le solde ou null si 0
     */
    public Double computeSoldeActif() {
        Double solde = null;
        if (CGCompte.CS_GENRE_ACTIF.equals(getIdGenre()) || CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre())) {
            solde = getMvtDebit() - getMvtCredit();
        }

        // Si resultat null on ne l'affiche pas
        // Si compte de clôture et résultat au doit on n'affiche pas
        if (CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre()) && (solde >= 0)) {
            return null;
        }

        if ((solde != null) && (solde != 0)) {
            return solde;
        } else {
            return null;
        }
    }

    /**
     * @return le solde ou null si 0
     */
    public Double computeSoldeCharge() {
        Double solde = null;

        // Résultat si compte actif
        if (CGCompte.CS_GENRE_CHARGE.equals(getIdGenre()) || CGCompte.CS_GENRE_RESULTAT.equals(getIdGenre())) {
            solde = getMvtDebit() - getMvtCredit();
        }

        if ((solde == null) || (solde == 0)) {
            return null;
        }

        // Si resultat null on ne l'affiche pas
        if (CGCompte.CS_GENRE_RESULTAT.equals(getIdGenre()) && (solde <= 0)) {
            solde = null;
        }

        return solde;
    }

    /**
     * @return le solde ou null si 0
     */
    public Double computeSoldePassif() {
        Double solde = null;
        if (CGCompte.CS_GENRE_PASSIF.equals(getIdGenre()) || CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre())) {
            solde = getMvtCredit() - getMvtDebit();
        }

        // Si resultat null on ne l'affiche pas
        // Si compte de clôture et résultat au doit on n'affiche pas
        if (CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre()) && (solde >= 0)) {
            return null;
        }

        if ((solde != null) && (solde != 0)) {
            return solde;
        }

        return null;
    }

    /**
     * @return le solde ou null si 0
     */
    public Double computeSoldeProduit() {
        Double solde = null;

        // Résultat si compte passif
        if (CGCompte.CS_GENRE_PRODUIT.equals(getIdGenre()) || CGCompte.CS_GENRE_RESULTAT.equals(getIdGenre())) {
            solde = getMvtCredit() - getMvtDebit();
        }

        if ((solde == null) || (solde == 0)) {
            return null;
        }

        // Inversion du signe. Si resultat null on ne l'affiche pas
        if (CGCompte.CS_GENRE_RESULTAT.equals(getIdGenre()) && (solde <= 0)) {
            solde = null;
        }

        return solde;
    }

    /**
     * @return the budget
     */
    public Double getBudget() {
        return budget;
    }

    /**
     * @return the budgetPourcent
     */
    public Double getBudgetPourcent() {
        return budgetPourcent;
    }

    /**
     * @return the idCompte
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * @return the idDomaine
     */
    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * @return the idGenre
     */
    public String getIdGenre() {
        return idGenre;
    }

    /**
     * @return the idNature
     */
    public String getIdNature() {
        return idNature;
    }

    /**
     * @return the monnaieCodeIso
     */
    public String getMonnaieCodeIso() {
        return monnaieCodeIso;
    }

    /**
     * @return the col5_mvtCredit
     */
    public Double getMvtCredit() {
        return col5_mvtCredit;
    }

    /**
     * @return the mvtCreditMonnaie
     */
    public Double getMvtCreditMonnaie() {
        return mvtCreditMonnaie;
    }

    /**
     * @return the mvtCreditMonnaieString
     */
    public String getMvtCreditMonnaieString() {
        return mvtCreditMonnaieString;
    }

    /**
     * @return the col5_mvtCreditTotal
     */
    public Double getMvtCreditTotal() {
        return col5_mvtCreditTotal;
    }

    /**
     * @return the col4_mvtDebit
     */
    public Double getMvtDebit() {
        return col4_mvtDebit;
    }

    /**
     * @return the mvtDebitMonnaie
     */
    public Double getMvtDebitMonnaie() {
        return mvtDebitMonnaie;
    }

    /**
     * @return the mvtDebitMonnaieString
     */
    public String getMvtDebitMonnaieString() {
        return mvtDebitMonnaieString;
    }

    /**
     * @return the col4_mvtDebitTotal
     */
    public Double getMvtDebitTotal() {
        return col4_mvtDebitTotal;
    }

    /**
     * @return the col1_niveau
     */
    public String getNiveau() {
        return col1_niveau;
    }

    /**
     * @return the noClasse
     */
    public String getNoClasse() {
        return noClasse;
    }

    /**
     * @return the col2_noClasseLibelle
     */
    public String getNoClasseLibelle() {
        return col2_noClasseLibelle;
    }

    /**
     * @return the col3_noCompteLibelle
     */
    public String getNoCompteLibelle() {
        return col3_noCompteLibelle;
    }

    /**
     * @return the resultat
     */
    public Double getResultat() {
        return resultat;
    }

    /**
     * @return the solde
     */
    public Double getSolde() {
        return solde;
    }

    /**
     * @return the soldeCreditMonnaie
     */
    public String getSoldeCreditMonnaie() {
        return soldeCreditMonnaie;
    }

    /**
     * @return the soldeDebitMonnaie
     */
    public String getSoldeDebitMonnaie() {
        return soldeDebitMonnaie;
    }

    /**
     * @return the soldeExerciceComparaison
     */
    public Double getSoldeExerciceComparaison() {
        return soldeExerciceComparaison;
    }

    /**
     * @return the soldeMonnaie
     */
    public Double getSoldeMonnaie() {
        return soldeMonnaie;
    }

    /**
     * @return the totalActif
     */
    public Double getTotalActif() {
        return totalActif;
    }

    /**
     * @return the totalBudget
     */
    public Double getTotalBudget() {
        return totalBudget;
    }

    /**
     * @return the totalCharges
     */
    public Double getTotalCharges() {
        return totalCharges;
    }

    /**
     * @return the totalPassif
     */
    public Double getTotalPassif() {
        return totalPassif;
    }

    /**
     * @return the totalProduits
     */
    public Double getTotalProduits() {
        return totalProduits;
    }

    /**
     * @return the totalSolde
     */
    public Double getTotalSolde() {
        return totalSolde;
    }

    /**
     * @return the totalSoldeExerciceComparaison
     */
    public Double getTotalSoldeExerciceComparaison() {
        return totalSoldeExerciceComparaison;
    }

    /**
     * @return TRUE si le genre est actif
     */
    public boolean isActif() {
        return CGCompte.CS_GENRE_ACTIF.equals(getIdGenre());
    }

    /**
     * @return TRUE si le genre est cloture
     */
    public boolean isCloture() {
        return CGCompte.CS_GENRE_CLOTURE.equals(getIdGenre());
    }

    /**
     * @return TRUE si le genre est passif
     */
    public boolean isPassif() {
        return CGCompte.CS_GENRE_PASSIF.equals(getIdGenre());
    }

    /**
     * @param totalDebit
     * @param totalCredit
     * @return TRUE si totalCharges ou totalProduits est différent de null
     */
    public boolean isTotal() {
        return (totalCharges != null) || (totalProduits != null) || (totalActif != null) || (totalPassif != null);
    }

    /**
     * Met les mouvements de débit et de crédit à null
     */
    public void resetMvt() {
        setMvtDebit(null);
        setMvtCredit(null);
        setBudget(null);
    }

    /**
     * Met tous les totaux à null
     */
    public void resetTotaux() {
        setMvtDebitTotal(null);
        setMvtCreditTotal(null);
        setTotalActif(null);
        setTotalPassif(null);
        setTotalCharges(null);
        setTotalProduits(null);
        setTotalBudget(null);
        setTotalSoldeExerciceComparaison(null);
    }

    /**
     * @param budget
     *            the budget to set
     */
    public void setBudget(Double budget) {
        this.budget = budget;
    }

    /**
     * @param budgetPourcent
     *            the budgetPourcent to set
     */
    public void setBudgetPourcent(Double budgetPourcent) {
        this.budgetPourcent = budgetPourcent;
    }

    /**
     * @param idCompte
     *            the idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * @param idDomaine
     *            the idDomaine to set
     */
    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    /**
     * @param idGenre
     *            the idGenre to set
     */
    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * @param idNature
     *            the idNature to set
     */
    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

    /**
     * @param monnaieCodeIso
     *            the monnaieCodeIso to set
     */
    public void setMonnaieCodeIso(String monnaieCodeIso) {
        this.monnaieCodeIso = monnaieCodeIso;
    }

    /**
     * @param col5_mvtCredit
     *            the col5_mvtCredit to set
     */
    public void setMvtCredit(Double col5_mvtCredit) {
        this.col5_mvtCredit = col5_mvtCredit;
    }

    /**
     * @param mvtCreditMonnaie
     *            the mvtCreditMonnaie to set
     */
    public void setMvtCreditMonnaie(Double mvtCreditMonnaie) {
        this.mvtCreditMonnaie = mvtCreditMonnaie;
    }

    /**
     * @param mvtCreditMonnaieString
     *            the mvtCreditMonnaieString to set
     */
    public void setMvtCreditMonnaieString(String mvtCreditMonnaieString) {
        this.mvtCreditMonnaieString = mvtCreditMonnaieString;
    }

    /**
     * @param col5_mvtCreditTotal
     *            the col5_mvtCreditTotal to set
     */
    private void setMvtCreditTotal(Double col5_mvtCreditTotal) {
        this.col5_mvtCreditTotal = col5_mvtCreditTotal;
    }

    /**
     * @param col4_mvtDebit
     *            the col4_mvtDebit to set
     */
    public void setMvtDebit(Double col4_mvtDebit) {
        this.col4_mvtDebit = col4_mvtDebit;
    }

    /**
     * @param mvtDebitMonnaie
     *            the mvtDebitMonnaie to set
     */
    public void setMvtDebitMonnaie(Double mvtDebitMonnaie) {
        this.mvtDebitMonnaie = mvtDebitMonnaie;
    }

    /**
     * @param mvtDebitMonnaieString
     *            the mvtDebitMonnaieString to set
     */
    public void setMvtDebitMonnaieString(String mvtDebitMonnaieString) {
        this.mvtDebitMonnaieString = mvtDebitMonnaieString;
    }

    /**
     * @param col4_mvtDebitTotal
     *            the col4_mvtDebitTotal to set
     */
    private void setMvtDebitTotal(Double col4_mvtDebitTotal) {
        this.col4_mvtDebitTotal = col4_mvtDebitTotal;
    }

    /**
     * @param col1_niveau
     *            the col1_niveau to set
     */
    public void setNiveau(String col1_niveau) {
        this.col1_niveau = col1_niveau;
    }

    /**
     * @param noClasse
     *            the noClasse to set
     */
    public void setNoClasse(String noClasse) {
        this.noClasse = noClasse;
    }

    /**
     * @param col2_noClasseLibelle
     *            the col2_noClasseLibelle to set
     */
    public void setNoClasseLibelle(String col2_noClasseLibelle) {
        this.col2_noClasseLibelle = col2_noClasseLibelle;
    }

    /**
     * @param col3_noCompteLibelle
     *            the col3_noCompteLibelle to set
     */
    public void setNoCompteLibelle(String col3_noCompteLibelle) {
        this.col3_noCompteLibelle = col3_noCompteLibelle;
    }

    /**
     * @param resultat
     *            the resultat to set
     */
    public void setResultat(Double resultat) {
        this.resultat = resultat;
    }

    /**
     * @param solde
     *            the solde to set
     */
    public void setSolde(Double solde) {
        this.solde = solde;
    }

    /**
     * @param soldeCreditMonnaie
     *            the soldeCreditMonnaie to set
     */
    public void setSoldeCreditMonnaie(String soldeCreditMonnaie) {
        this.soldeCreditMonnaie = soldeCreditMonnaie;
    }

    /**
     * @param soldeDebitMonnaie
     *            the soldeDebitMonnaie to set
     */
    public void setSoldeDebitMonnaie(String soldeDebitMonnaie) {
        this.soldeDebitMonnaie = soldeDebitMonnaie;
    }

    /**
     * @param soldeExerciceComparaison
     *            the soldeExerciceComparaison to set
     */
    public void setSoldeExerciceComparaison(Double soldeExerciceComparaison) {
        this.soldeExerciceComparaison = soldeExerciceComparaison;
    }

    /**
     * @param soldeMonnaie
     *            the soldeMonnaie to set
     */
    public void setSoldeMonnaie(Double soldeMonnaie) {
        this.soldeMonnaie = soldeMonnaie;
    }

    /**
     * @param totalActif
     *            the totalActif to set
     */
    private void setTotalActif(Double totalActif) {
        this.totalActif = totalActif;
    }

    /**
     * @param totalBudget
     *            the totalBudget to set
     */
    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    /**
     * @param totalCharges
     *            the totalCharges to set
     */
    private void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    /**
     * @param totalPassif
     *            the totalPassif to set
     */
    private void setTotalPassif(Double totalPassif) {
        this.totalPassif = totalPassif;
    }

    /**
     * @param totalProduits
     *            the totalProduits to set
     */
    private void setTotalProduits(Double totalProduits) {
        this.totalProduits = totalProduits;
    }

    /**
     * @param totalSolde
     *            the totalSolde to set
     */
    public void setTotalSolde(Double totalSolde) {
        this.totalSolde = totalSolde;
    }

    /**
     * @param totalSoldeExerciceComparaison
     *            the totalSoldeExerciceComparaison to set
     */
    public void setTotalSoldeExerciceComparaison(Double totalSoldeExerciceComparaison) {
        this.totalSoldeExerciceComparaison = totalSoldeExerciceComparaison;
    }

    @Override
    public String toString() {
        return getNiveau() + " " + getNoClasseLibelle() + " " + getNoCompteLibelle() + " " + getIdGenre();
    }
}
