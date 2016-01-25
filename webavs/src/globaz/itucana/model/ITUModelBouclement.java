package globaz.itucana.model;

import java.util.Iterator;

/**
 * Classe mère de tous les modèles bouclement
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public interface ITUModelBouclement {
    /**
     * Méthode permettant d'ajouter une ligne de bouclement
     * 
     * @param canton
     *            référence OFS du canton (2 lettres majuscules ou minuscules)
     * @param rubrique
     *            id code système de la rubrique. Peut être repris des constantes de
     *            <code>ITUCSRubriqueListeDesRubriques</code>
     * @param montantNombre
     *            soit un nombre ou un montant détaillant la rubrique
     */
    public void addLine(String canton, String rubrique, String montantNombre);

    /**
     * Récupère une entête
     * 
     * @return
     */
    public ITUEntete getEntete();

    /**
     * Récupère un itérator de lignes de bouclement
     * 
     * @return
     */
    public Iterator getLines();

    /**
     * Création de l'entête du bouclement. Le numéro de passage est utile à l'application faisant l'appelle afin d'avoir
     * un identifiant pour l'ensemble des lignes du bouclement. Ce numéro de passage sera demandé si une suppression de
     * passage doit être effectuée.<br>
     * 
     * @param anneeComptable
     * @param moisComptable
     * @param numeroPassage
     */
    public void setEntete(String anneeComptable, String moisComptable, String numeroPassage);

}
