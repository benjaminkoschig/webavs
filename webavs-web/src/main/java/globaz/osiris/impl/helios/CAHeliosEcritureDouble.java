package globaz.osiris.impl.helios;

import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGEcritureDouble;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.external.IntCompteCG;
import globaz.osiris.external.IntEcritureDouble;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (23.05.2003 09:03:34)
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
     * R�cup�re le code d�bit / cr�dit de l'�criture <BR>
     * DB = D�bit CR = Credit ED = Extourne d�bit EC = Extourne cr�dit Date de cr�ation : (28.10.2002 09:12:34)
     * 
     * @return java.lang.String le code d�bit cr�dit de l'�criture
     */
    @Override
    public String getCodeDebitCredit() {
        return APIEcriture.DEBIT;
    }

    /**
     * R�cup�re le compte de comptabilit� g�n�rale associ� � l'�criture Date de cr�ation : (28.10.2002 09:04:30)
     * 
     * @param BTransaction
     *            la transaction courante
     * @return globaz.osiris.external.IntCompteCG le compte de comptabilit� g�n�rale
     */
    @Override
    public IntCompteCG getCompteCG(BTransaction transaction) {
        // Fin si pas d'�criture
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
     * R�cup�re le compte de contre �criture (une valeur null est possible si la contre �criture n'est pas disponible)
     * Date de cr�ation : (28.10.2002 10:08:33)
     * 
     * @param BTransaction
     *            la transaction courante
     * @return IntCompteCG Le compte de contre �criture
     */
    @Override
    public IntCompteCG getContreEcriture(BTransaction transaction) {
        // Fin si pas d'�criture
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
     * R�cup�re l'identifiant de l'�criture collective Date de cr�ation : (28.10.2002 08:35:41)
     * 
     * @return java.lang.String l'identifiant de l'�criture collective
     */
    @Override
    public String getIdEcritureCollective() {
        return ecriture.getIdEcriture();
    }

    /**
     * R�cup�re l'identifiant du livre de comptabilit� g�n�rale. La valeur null indique que cette possiblit� n'est pas
     * impl�ment�e. Date de cr�ation : (28.10.2002 10:12:54)
     * 
     * @return java.lang.String L'identifiant du livre de comptabilit� g�n�rale
     */
    @Override
    public String getIdLivre() {
        return new String();
    }

    /**
     * R�cup�re le montant de l'�criture Date de cr�ation : (28.10.2002 09:11:23)
     * 
     * @return java.lang.String le montant de l'�criture
     */
    @Override
    public String getMontant() {
        return ecriture.getMontant();
    }

    /**
     * R�cup�re le num�ro de la pi�ce comptable Date de cr�ation : (28.10.2002 09:11:54)
     * 
     * @return java.lang.String le num�ro de la pi�ce comptable
     */
    @Override
    public String getPieceComptable() {
        return ecriture.getPiece();
    }

    /**
     * R�cup�re la r�f�rence externe de l'�criture Date de cr�ation : (28.10.2002 10:06:19)
     * 
     * @return java.lang.String La r�f�rence externe de l'�criture
     */
    @Override
    public String getReferenceExterne() {
        return new String();
    }

    /**
     * Affecter l'identifiant de l'�criture collective Date de cr�ation : (28.10.2002 08:36:33)
     * 
     * @param newIdEcritureCollective
     *            java.lang.String l'identifiant de l'�criture collective
     */
    @Override
    public void setIdEcritureCollective(String newIdEcritureCollective) {
        ecriture.setIdEcriture(newIdEcritureCollective);
    }

}
