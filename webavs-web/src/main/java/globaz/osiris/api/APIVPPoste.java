/*
 * Cr�� le 15 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

/**
 * @author ald Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface APIVPPoste {
    public static final String CS_PROCEDURE_AUTRE_PROCEDURE = "238003";
    public static final String CS_PROCEDURE_PLAINTE_PENALE = "238001";
    public static final String CS_PROCEDURE_REPARATION_DOMMAGE = "238002";

    public void addDetailMontant(APIVPDetailMontant detailMontant);

    public APIVPDetailMontant getDetailMontant(int i);

    public APIVPDetailMontant getFirstDetailMontant();

    public APIRubrique getRubrique();

    public boolean isPosteAVentiler();

    public void setRubrique(APIRubrique rubrique);
}
