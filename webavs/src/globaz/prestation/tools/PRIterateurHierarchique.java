/*
 * Cr�� le 10 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Extension de java.util.Iterator permettant, dans le cas d'une it�ration sur des instances de PRHierarchique, de
 * savoir apr�s chaque appel de next() si on est descendu ou remont� d'un niveau dans la hi�rarchie.
 * </p>
 * 
 * @author vre
 */
public class PRIterateurHierarchique implements Iterator {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int dernierePosition = 0;
    private boolean dirty = true;

    private PRAbstractManagerHierarchique.Composite elementCourant = null;

    private boolean hasNext = false;
    private Iterator iterateurCourant = null;

    private Stack pile = new Stack();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe PRIterateurHierarchique.
     * 
     * @param racine
     *            DOCUMENT ME!
     */
    public PRIterateurHierarchique(PRAbstractManagerHierarchique.Racine racine) {
        iterateurCourant = racine.iterator();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne le nombre de noeuds qui s�parent le dernier �l�ment retourn� par next() de la racine de la hi�rarchie.
     * 
     * @return le nombre de noeuds qui s�parent le dernier �l�ment retourn� par next() de la racine de la hi�rarchie (au
     *         minimum 1).
     * 
     * @throws NoSuchElementException
     *             si aucun appel � next() n'a encore �t� effectu�.
     */
    public int getPosition() throws NoSuchElementException {
        if (elementCourant == null) {
            throw new NoSuchElementException("aucun �l�ment retourn� encore");
        }

        return pile.size() + 1;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (dirty) {
            dernierePosition = pile.size();

            if ((elementCourant != null) && elementCourant.isPere()) {
                pile.push(iterateurCourant);
                iterateurCourant = elementCourant.iterator();
                hasNext = true;
            } else {
                while (!iterateurCourant.hasNext()) {
                    if (pile.isEmpty()) {
                        break;
                    } else {
                        iterateurCourant = (Iterator) pile.pop();
                    }
                }

                hasNext = iterateurCourant.hasNext();
            }

            if (hasNext) {
                elementCourant = (PRAbstractManagerHierarchique.Composite) iterateurCourant.next();
            }

            dirty = false;
        }

        return hasNext;
    }

    /**
     * getter pour l'attribut orphelin
     * 
     * @return la valeur courante de l'attribut orphelin
     * 
     * @throws NoSuchElementException
     *             DOCUMENT ME!
     */
    public boolean isOrphelin() throws NoSuchElementException {
        if (elementCourant == null) {
            throw new NoSuchElementException("aucun �l�ment retourn� encore");
        }

        return elementCourant.isOrphelin();
    }

    /**
     * getter pour l'attribut pere
     * 
     * @return vrai si le dernier �l�ment retourn� par next() a des enfants.
     * 
     * @throws NoSuchElementException
     *             si aucun appel � next() n'a encore �t� effectu�.
     */
    public boolean isPere() throws NoSuchElementException {
        if (elementCourant == null) {
            throw new NoSuchElementException("aucun �l�ment retourn� encore");
        }

        return elementCourant.isPere();
    }

    /**
     * getter pour l'attribut position plus grande
     * 
     * @return vrai si le dernier element retourn� par next() est � une position plus grande que le pr�c�dent.
     * 
     * @throws NoSuchElementException
     *             si aucun appel � next() n'a encore �t� effectu�.
     */
    public boolean isPositionPlusGrande() throws NoSuchElementException {
        if (elementCourant == null) {
            throw new NoSuchElementException("aucun �l�ment retourn� encore");
        }

        return pile.size() > dernierePosition;
    }

    /**
     * getter pour l'attribut position plus petite
     * 
     * @return vrai si le dernier �l�ment retourn� par next() est � une position plus petite que le pr�c�dent.
     * 
     * @throws NoSuchElementException
     *             si aucun appel � next() n'a encore �t� effectu�.
     */
    public boolean isPositionPlusPetite() throws NoSuchElementException {
        if (elementCourant == null) {
            throw new NoSuchElementException("aucun �l�ment retourn� encore");
        }

        return pile.size() < dernierePosition;
    }

    /**
     * retourne l'�l�ment suivant (instance de PRHierarchique).
     * 
     * @return l'�l�ment suivant (instance de PRHierarchique).
     * 
     * @throws NoSuchElementException
     *             s'il n'y a plus d'�l�ments � retourner.
     * 
     * @see java.util.Iterator#next()
     */
    @Override
    public Object next() throws NoSuchElementException {
        if (dirty) {
            if (!hasNext()) {
                throw new NoSuchElementException("il n'y a plus d'elements dans cette Iterator");
            }
        }

        dirty = true;

        return elementCourant.getElement();
    }

    /**
     * interdit
     * 
     * @throws UnsupportedOperationException
     *             dans tous les cas.
     * 
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("cet it�rateur ne permet pas l'op�ration remove");
    }
}
