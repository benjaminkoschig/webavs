/*
 * Créé le 9 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.comparaison;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICIComparaisonIteratorInput {

    public void close();

    public String getFileName();

    public boolean hasNext() throws Exception;

    public CIEnteteRecord next() throws Exception;

    public void setFileName(String string);

    public int size() throws Exception;
}
