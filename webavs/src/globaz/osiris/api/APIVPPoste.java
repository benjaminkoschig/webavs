/*
 * Créé le 15 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
