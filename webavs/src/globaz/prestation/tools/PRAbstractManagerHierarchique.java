/*
 * Cr�� le 10 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Squelette de base d'un BManager permettant de trier les r�sultats retourn�s en fonction de leur hi�rarchie
 * (PRHierarchique).
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractManagerHierarchique extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Une classe qui repr�sente un �l�ment dans une hierarchie d''�l�ments.
     * 
     * <p>
     * Cette classe est bas�e sur la pattern (GOF) composite. Les elements enfants sont contenus dans un liste interne
     * qui est nulle si l'element n'a aucun enfant connu.
     * </p>
     */
    class Composite {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        protected PRHierarchique element;
        protected LinkedList enfants = null;
        protected boolean orphelin;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe Composite.
         * 
         * @param element
         *            DOCUMENT ME!
         */
        public Composite(PRHierarchique element) {
            this.element = element;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * Ajoute un enfant a ce composite.
         * 
         * @param enfant
         *            DOCUMENT ME!
         */
        public void ajouterEnfant(Object enfant) {
            if (enfants == null) {
                enfants = new LinkedList();
            }

            enfants.add(enfant);
        }

        /**
         * getter pour l'attribut element
         * 
         * @return la valeur courante de l'attribut element
         */
        public PRHierarchique getElement() {
            return element;
        }

        /**
         * retourne vrai si l'element est un enfant mais que son parent ne fait pas partie de la liste des elements
         * connus et donc son classement est impossible.
         * 
         * @return la valeur courante de l'attribut orphelin
         */
        public boolean isOrphelin() {
            return orphelin;
        }

        /**
         * retourne vrai si l'element a des enfants connus (c'est-a-dire qui faisaient partie de la liste des elements
         * de base qui ont servis a la creation de cet iterateur.
         * 
         * @return DOCUMENT ME!
         */
        public boolean isPere() {
            return enfants != null;
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public Iterator iterator() {
            return enfants.iterator();
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Une specialisation de Composite qui permet de creer les collections d'elements.
     * 
     * <p>
     * La racine repr�sente le niveau le plus haut de la hierarchie, ses enfants sont tous les elements qui sont soit
     * des peres, soit des enfants orphelins.
     * </p>
     * 
     * @author vre
     */
    class Racine extends Composite {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private HashMap map;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe Racine.
         */
        public Racine() {
            super(null);
            enfants = new LinkedList();
            map = new HashMap();

            /*
             * Le tri des elements par hierarchie se fait en deux etapes au moyen d'un map accessoire.
             * 
             * 1: on ajoute tous les elements dans la liste des enfants et on les insere egalement dans une map pour les
             * recuperer facilement par index.
             */
            for (int idElement = 0; idElement < size(); ++idElement) {
                PRHierarchique element = (PRHierarchique) get(idElement);
                Composite composite = new Composite(element);

                if (!map.containsKey(element.getIdMajeur())) {
                    enfants.add(composite);
                    map.put(element.getIdMajeur(), composite);
                }

            }

            /*
             * 2: on itere sur tous les elements de la liste et on enleve ceux qui sont des enfants pour les rajouter
             * comme enfant, de leur parent.
             */
            for (Iterator composites = enfants.iterator(); composites.hasNext();) {
                Composite composite = (Composite) composites.next();
                Composite parent = (Composite) map.get(composite.getElement().getIdParent());

                if (parent != null) {
                    // ok on a trouve le parent de l'element courant, on le fait
                    // passer dans sa liste d'enfants
                    parent.ajouterEnfant(composite);
                    composites.remove();
                } else if (!JadeStringUtil.isIntegerEmpty(composite.getElement().getIdParent())) {
                    // on a pas trouve le parent alors qu'il devrait y en avoir,
                    // cet element est orphelin
                    composite.orphelin = true;
                }
            }
        }
    }

    // Par d�faut, on trie hierarchicalement, cad que l'on prend les enfants en
    // compte dans
    // le tri.
    private boolean isHierarchicalOrder = false;

    @Override
    protected String _getOrder(BStatement statement) {
        if (isHierarchicalOrder()) {
            if (JadeStringUtil.isBlank(getOrderBy())) {
                return getHierarchicalOrderBy();
            } else {
                return getOrderBy() + ", " + getHierarchicalOrderBy();
            }
        } else {
            return super._getOrder(statement);
        }
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * A impl�menter pour qu'elle retourne les occurences retourn�es en prenant en compte la hi�rarchie p�re/enfant Sans
     * cette impl�mentation, comme le nbr. d'occurence retourn�es par �crans rcListe est limit�, il est possible d'avoir
     * un parent et que son enfant ne soit pas afficher, car l'enfant se trouve dans le r�sultSet suivant [>>>].
     * 
     * Avec cette impl�mentation, seul le dernier parent (dernier �l�ment de la liste )pourait potentiellement avoir son
     * enfant dans le resultSet suivant (bouton [>>>] dans le rcListe). Example :
     * 
     * Elements : id idParent 1 0 2 0 3 0 4 2
     * 
     * Sans le tri hirarchique, les el�ments seront retourn� comme ci-dessus. Avec le tri hi�rarchique
     * (isHierarchicalOrder=true), les �l�ments seront retourn� dans l'ordre suivant :
     * 
     * Elements : id idParent 1 0 2 0 4 2 3 0
     * 
     * @return Instruction case permettant le tri hi�rarchique
     */
    public abstract String getHierarchicalOrderBy();

    public boolean isHierarchicalOrder() {
        return isHierarchicalOrder;
    }

    /**
     * Create une version augmentee de java.util.Iterator sur les elements de ce manager.
     * 
     * <p>
     * L'iterateur hierarchique inspecte les identifiants des parents des elements de ce manager et presente des flags
     * indiquant des changements de niveau hi�rarchique �ventuels.
     * </p>
     * 
     * @return DOCUMENT ME!
     */
    public PRIterateurHierarchique iterateurHierarchique() {
        return new PRIterateurHierarchique(new Racine());
    }

    public void setHierarchicalOrder(boolean isHierarchicalOrder) {
        this.isHierarchicalOrder = isHierarchicalOrder;
    }
}
