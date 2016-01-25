package globaz.corvus.annonce.formatter;

import globaz.corvus.annonce.RENSS;

/**
 * Cette classe poss�de des m�thodes de formatage de tous les champs li�s aux annonces des rentes 9�me r�vision.
 * Les formats de chacun de ces champs sont tr�s sp�cifique et doivent �tre respect�s.
 * C'est pourquoi on retrouve une m�thode de formatage par champ.
 * 
 * Cette classe est utilis�e par RECreationAnnonceService qui s'occupe de la g�n�ration des annonces (BEntity) 01 et 02
 * gr�ce � un objet de domain typ�
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
     * Retourne la valeur <code>nouveauNoAssureAyantDroit</code> format�e</br>
     * </br>
     * -> Retourne une cha�ne vide si <code>nouveauNoAssureAyantDroit</code> est null</br>
     * -> Sinon retourne le NSS format� sans les point (13 positions)</br>
     * </br>
     * 
     * @param nouveauNoAssureAyantDroit Le nouveau num�ro NSS de l'ayant droit
     * @return la valeur <code>nouveauNoAssureAyantDroit</code> format�e
     */
    public String formatNouveauNoAssureAyantDroit(RENSS nouveauNoAssureAyantDroit) {
        return formatNSS(nouveauNoAssureAyantDroit);
    }

    // -------------------------------------------------------------------------
    // REAAL3B

    /**
     * REAAL3B.ZCMROR</br>
     * Retourne la valeur <code>montantRenteOrdinaireRemplace</code> format�e</br>
     * 
     * -> Retourne un cha�ne vide si <code>montantRenteOrdinaireRemplace</code> est null</br>
     * -> Sinon indente la valeur de <code>montantRenteOrdinaireRemplace</code> sur 5 positions avec des 0 � gauche</br>
     * 
     * @param montantRenteOrdinaireRemplace Le montant de la rente ordinaire remplac�
     * @return la valeur <code>montantRenteOrdinaireRemplace</code> format�e
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
     * Retourne la valeur <code>revenuPrisEnCompte</code> format�e</br>
     * 
     * -> Retourne un cha�ne vide si <code>revenuPrisEnCompte</code> est null</br>
     * -> Sinon retourne la valeur de <code>revenuPrisEnCompte</code> Les valeurs possible ont [1, 2, 3]</br>
     * 
     * @param revenuPrisEnCompte Le revenu pris en compte
     * @return la valeur <code>revenuPrisEnCompte</code> format�e</br>
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
     * Retourne la valeur <code>isLimiteRevenu</code> format�e</br>
     * 
     * -> Retourne un cha�ne vide si <code>isLimiteRevenu</code> est null</br>
     * -> Retourne "1" si <code>isLimiteRevenu</code> �gal <code>true</code></br>
     * -> Retourne "0" dans tous les autres cas</br>
     * 
     * @param isLimiteRevenu Si une limite de revenu est pr�sente
     * @return la valeur <code>isLimiteRevenu</code> format�e</br>
     */
    public String formatIsLimiteRevenu(Boolean isLimiteRevenu) {
        return formatBoolean(isLimiteRevenu);
    }

    /**
     * REAAL3B.ZCBMIN</br>
     * Retourne la valeur <code>isMinimumGaranti</code> format�e</br>
     * 
     * -> Retourne un cha�ne vide si <code>isMinimumGaranti</code> est null</br>
     * -> Retourne "1" si <code>isMinimumGaranti</code> �gal <code>true</code></br>
     * -> Retourne "0" dans tous les autres cas</br>
     * 
     * @param isMinimumGaranti Si un minimum garanti est existant
     * @return la valeur <code>isMinimumGaranti</code> format�e</br>
     */
    public String formatIsMinimumGaranti(Boolean isMinimumGaranti) {
        return formatBoolean(isMinimumGaranti);
    }

    /**
     * REAAL3B.ZCNRAM</br>
     * </br>
     * Retourne la valeur <code>revenuAnnuelMoyenSansBTE</code> format�e</br>
     * </br>
     * -> Retourne un cha�ne vide si <code>revenuAnnuelMoyenSansBTE</code> est null</br>
     * -> Sinon retourne la valeur de <code>revenuAnnuelMoyenSansBTE</code> sur 6 positions avec des 0 � gauche</br>
     * </br>
     * 
     * @param revenuAnnuelMoyenSansBTE le revenu annuel moyen sans BTE
     * @return la valeur <code>revenuAnnuelMoyenSansBTE</code> format�e</br>
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
     * Retourne la valeur <code>bteMoyenPrisEnCompte</code> format�e</br>
     * 
     * -> Retourne un cha�ne vide si <code>bteMoyenPrisEnCompte</code> est null</br>
     * -> Sinon retourne la valeur de <code>bteMoyenPrisEnCompte</code> sur 5 positions avec des 0 � gauche</br>
     * 
     * @param bteMoyenPrisEnCompte
     * @return la valeur <code>bteMoyenPrisEnCompte</code> format�e</br>
     */
    public String formatBteMoyennePrisEnCompte(Integer bteMoyenPrisEnCompte) {
        String result = "";
        if (bteMoyenPrisEnCompte != null) {
            result = indentLeftWithZero(bteMoyenPrisEnCompte, 5);
        }
        return result;
    }

}
