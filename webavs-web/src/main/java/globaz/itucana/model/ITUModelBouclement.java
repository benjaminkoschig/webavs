package globaz.itucana.model;

import java.util.Iterator;

/**
 * Classe m�re de tous les mod�les bouclement
 * 
 * @author fgo date de cr�ation : 22 juin 06
 * @version : version 1.0
 * 
 */
public interface ITUModelBouclement {
    /**
     * M�thode permettant d'ajouter une ligne de bouclement
     * 
     * @param canton
     *            r�f�rence OFS du canton (2 lettres majuscules ou minuscules)
     * @param rubrique
     *            id code syst�me de la rubrique. Peut �tre repris des constantes de
     *            <code>ITUCSRubriqueListeDesRubriques</code>
     * @param montantNombre
     *            soit un nombre ou un montant d�taillant la rubrique
     */
    public void addLine(String canton, String rubrique, String montantNombre);

    /**
     * R�cup�re une ent�te
     * 
     * @return
     */
    public ITUEntete getEntete();

    /**
     * R�cup�re un it�rator de lignes de bouclement
     * 
     * @return
     */
    public Iterator getLines();

    /**
     * Cr�ation de l'ent�te du bouclement. Le num�ro de passage est utile � l'application faisant l'appelle afin d'avoir
     * un identifiant pour l'ensemble des lignes du bouclement. Ce num�ro de passage sera demand� si une suppression de
     * passage doit �tre effectu�e.<br>
     * 
     * @param anneeComptable
     * @param moisComptable
     * @param numeroPassage
     */
    public void setEntete(String anneeComptable, String moisComptable, String numeroPassage);

}
