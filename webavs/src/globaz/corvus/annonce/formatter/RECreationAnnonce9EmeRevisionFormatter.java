package globaz.corvus.annonce.formatter;

import globaz.corvus.annonce.RENSS;

/**
 * Cette classe possède des méthodes de formatage de tous les champs liés aux annonces des rentes 9ème révision.
 * Les formats de chacun de ces champs sont très spécifique et doivent être respectés.
 * C'est pourquoi on retrouve une méthode de formatage par champ.
 * 
 * Cette classe est utilisée par RECreationAnnonceService qui s'occupe de la génération des annonces (BEntity) 01 et 02
 * grâce à un objet de domain typé
 * 
 * @author lga
 * 
 */
public class RECreationAnnonce9EmeRevisionFormatter extends REAbstractCreationAnnonceFormatter {

    // -----------------------------------------------------------------------
    // REANN44

    /**
     * REANN44.ZENNNA</br>
     * </br>
     * Retourne la valeur <code>nouveauNoAssureAyantDroit</code> formatée</br>
     * </br>
     * -> Retourne une chaîne vide si <code>nouveauNoAssureAyantDroit</code> est null</br>
     * -> Sinon retourne le NSS formaté sans les point (13 positions)</br>
     * </br>
     * 
     * @param nouveauNoAssureAyantDroit Le nouveau numéro NSS de l'ayant droit
     * @return la valeur <code>nouveauNoAssureAyantDroit</code> formatée
     */
    public String formatNouveauNoAssureAyantDroit(RENSS nouveauNoAssureAyantDroit) {
        return formatNSS(nouveauNoAssureAyantDroit);
    }

    // -------------------------------------------------------------------------
    // REAAL3B

    /**
     * REAAL3B.ZCMROR</br>
     * Retourne la valeur <code>montantRenteOrdinaireRemplace</code> formatée</br>
     * 
     * -> Retourne un chaîne vide si <code>montantRenteOrdinaireRemplace</code> est null</br>
     * -> Sinon indente la valeur de <code>montantRenteOrdinaireRemplace</code> sur 5 positions avec des 0 à gauche</br>
     * 
     * @param montantRenteOrdinaireRemplace Le montant de la rente ordinaire remplacé
     * @return la valeur <code>montantRenteOrdinaireRemplace</code> formatée
     */
    public String formatMontantRenteOrdinaireRemplace(Integer montantRenteOrdinaireRemplace) {
        String result = "";
        if (montantRenteOrdinaireRemplace != null) {
            result = indentLeftWithZero(montantRenteOrdinaireRemplace, 5);
        } else {
            result = indentLeftWithZero(0, 5);
        }
        return result;
    }

    /**
     * REAAL3B.ZCMREV</br>
     * Retourne la valeur <code>revenuPrisEnCompte</code> formatée</br>
     * 
     * -> Retourne un chaîne vide si <code>revenuPrisEnCompte</code> est null</br>
     * -> Sinon retourne la valeur de <code>revenuPrisEnCompte</code> Les valeurs possible ont [1, 2, 3]</br>
     * 
     * @param revenuPrisEnCompte Le revenu pris en compte
     * @return la valeur <code>revenuPrisEnCompte</code> formatée</br>
     */
    public String formatRevenuPrisEnCompte(Integer revenuPrisEnCompte) {
        String result = "";
        if (revenuPrisEnCompte != null) {
            result = String.valueOf(revenuPrisEnCompte);
        }
        return result;
    }

    /**
     * REAAL3B.ZCBCIM</br>
     * Retourne la valeur <code>isLimiteRevenu</code> formatée</br>
     * 
     * -> Retourne un chaîne vide si <code>isLimiteRevenu</code> est null</br>
     * -> Retourne "1" si <code>isLimiteRevenu</code> égal <code>true</code></br>
     * -> Retourne "0" dans tous les autres cas</br>
     * 
     * @param isLimiteRevenu Si une limite de revenu est présente
     * @return la valeur <code>isLimiteRevenu</code> formatée</br>
     */
    public String formatIsLimiteRevenu(Boolean isLimiteRevenu) {
        return formatBoolean(isLimiteRevenu);
    }

    /**
     * REAAL3B.ZCBMIN</br>
     * Retourne la valeur <code>isMinimumGaranti</code> formatée</br>
     * 
     * -> Retourne un chaîne vide si <code>isMinimumGaranti</code> est null</br>
     * -> Retourne "1" si <code>isMinimumGaranti</code> égal <code>true</code></br>
     * -> Retourne "0" dans tous les autres cas</br>
     * 
     * @param isMinimumGaranti Si un minimum garanti est existant
     * @return la valeur <code>isMinimumGaranti</code> formatée</br>
     */
    public String formatIsMinimumGaranti(Boolean isMinimumGaranti) {
        return formatBoolean(isMinimumGaranti);
    }

    /**
     * REAAL3B.ZCNRAM</br>
     * </br>
     * Retourne la valeur <code>revenuAnnuelMoyenSansBTE</code> formatée</br>
     * </br>
     * -> Retourne un chaîne vide si <code>revenuAnnuelMoyenSansBTE</code> est null</br>
     * -> Sinon retourne la valeur de <code>revenuAnnuelMoyenSansBTE</code> sur 6 positions avec des 0 à gauche</br>
     * </br>
     * 
     * @param revenuAnnuelMoyenSansBTE le revenu annuel moyen sans BTE
     * @return la valeur <code>revenuAnnuelMoyenSansBTE</code> formatée</br>
     */
    public String formatRevenuAnnuelMoyenSansBTE(Integer revenuAnnuelMoyenSansBTE) {
        String result = "";
        if (revenuAnnuelMoyenSansBTE != null) {
            result = indentLeftWithZero(revenuAnnuelMoyenSansBTE, 6);
        }
        return result;
    }

    /**
     * REAAL3B.ZCMBTE</br>
     * Retourne la valeur <code>bteMoyenPrisEnCompte</code> formatée</br>
     * 
     * -> Retourne un chaîne vide si <code>bteMoyenPrisEnCompte</code> est null</br>
     * -> Sinon retourne la valeur de <code>bteMoyenPrisEnCompte</code> sur 5 positions avec des 0 à gauche</br>
     * 
     * @param bteMoyenPrisEnCompte
     * @return la valeur <code>bteMoyenPrisEnCompte</code> formatée</br>
     */
    public String formatBteMoyennePrisEnCompte(Integer bteMoyenPrisEnCompte) {
        String result = "";
        if (bteMoyenPrisEnCompte != null) {
            result = indentLeftWithZero(bteMoyenPrisEnCompte, 5);
        }
        return result;
    }

}
