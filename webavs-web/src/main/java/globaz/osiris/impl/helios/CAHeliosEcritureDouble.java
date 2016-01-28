package globaz.osiris.impl.helios;

import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGEcritureDouble;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.external.IntCompteCG;
import globaz.osiris.external.IntEcritureDouble;

/**
 * Insérez la description du type ici. Date de création : (23.05.2003 09:03:34)
 * 
 * @author: Administrator
 */
public class CAHeliosEcritureDouble extends CAHeliosJournal implements IntEcritureDouble {
    private ICGEcritureDouble ecriture = null;

    /**
     * Commentaire relatif au constructeur CAHeliosEcritureCollective.
     */
    public CAHeliosEcritureDouble(ICGEcritureDouble ecr) {
        super();
        ecriture = ecr;
    }

    /**
     * Récupère le code débit / crédit de l'écriture <BR>
     * DB = Débit CR = Credit ED = Extourne débit EC = Extourne crédit Date de création : (28.10.2002 09:12:34)
     * 
     * @return java.lang.String le code débit crédit de l'écriture
     */
    @Override
    public String getCodeDebitCredit() {
        return APIEcriture.DEBIT;
    }

    /**
     * Récupère le compte de comptabilité générale associé à l'écriture Date de création : (28.10.2002 09:04:30)
     * 
     * @param BTransaction
     *            la transaction courante
     * @return globaz.osiris.external.IntCompteCG le compte de comptabilité générale
     */
    @Override
    public IntCompteCG getCompteCG(BTransaction transaction) {
        // Fin si pas d'écriture
        if (ecriture == null) {
            return null;
        }

        // Instancier un nouveau compte
        CAHeliosCompte compte = new CAHeliosCompte(ecriture.getISession());
        // Le charger
        compte.setIdExterneCompte(ecriture.getNumeroCompteDebite());
        compte.setIdExerciceComptable(ecriture.getIdExerciceComptable());
        compte.setAlternateKey(1);
        try {
            compte.retrieve(transaction, true);
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
            compte = null;
        }
        // Le retourner
        return compte;
    }

    /**
     * Récupère le compte de contre écriture (une valeur null est possible si la contre écriture n'est pas disponible)
     * Date de création : (28.10.2002 10:08:33)
     * 
     * @param BTransaction
     *            la transaction courante
     * @return IntCompteCG Le compte de contre écriture
     */
    @Override
    public IntCompteCG getContreEcriture(BTransaction transaction) {
        // Fin si pas d'écriture
        if (ecriture == null) {
            return null;
        }
        // Instancier un nouveau compte
        CAHeliosCompte compte = new CAHeliosCompte(ecriture.getISession());
        // Le charger
        compte.setIdExterneCompte(ecriture.getNumeroCompteCredite());
        compte.setIdExerciceComptable(ecriture.getIdExerciceComptable());
        compte.setAlternateKey(1);
        try {
            compte.retrieve(transaction, true);
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
            compte = null;
        }
        // Le retourner
        return compte;
    }

    /**
     * Récupère l'identifiant de l'écriture collective Date de création : (28.10.2002 08:35:41)
     * 
     * @return java.lang.String l'identifiant de l'écriture collective
     */
    @Override
    public String getIdEcritureCollective() {
        return ecriture.getIdEcriture();
    }

    /**
     * Récupère l'identifiant du livre de comptabilité générale. La valeur null indique que cette possiblité n'est pas
     * implémentée. Date de création : (28.10.2002 10:12:54)
     * 
     * @return java.lang.String L'identifiant du livre de comptabilité générale
     */
    @Override
    public String getIdLivre() {
        return new String();
    }

    /**
     * Récupère le montant de l'écriture Date de création : (28.10.2002 09:11:23)
     * 
     * @return java.lang.String le montant de l'écriture
     */
    @Override
    public String getMontant() {
        return ecriture.getMontant();
    }

    /**
     * Récupère le numéro de la pièce comptable Date de création : (28.10.2002 09:11:54)
     * 
     * @return java.lang.String le numéro de la pièce comptable
     */
    @Override
    public String getPieceComptable() {
        return ecriture.getPiece();
    }

    /**
     * Récupère la référence externe de l'écriture Date de création : (28.10.2002 10:06:19)
     * 
     * @return java.lang.String La référence externe de l'écriture
     */
    @Override
    public String getReferenceExterne() {
        return new String();
    }

    /**
     * Affecter l'identifiant de l'écriture collective Date de création : (28.10.2002 08:36:33)
     * 
     * @param newIdEcritureCollective
     *            java.lang.String l'identifiant de l'écriture collective
     */
    @Override
    public void setIdEcritureCollective(String newIdEcritureCollective) {
        ecriture.setIdEcriture(newIdEcritureCollective);
    }

}
