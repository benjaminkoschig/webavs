/*
 * Cr�� le 22 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.globall.util.JAUtil;
import java.util.StringTokenizer;

/**
 * @author jpa
 * 
 *         classe pour faciliter la r�cup�ration de la saisie des formules editables. Les noms et valeurs, sont un peu �
 *         la mani�re des userActions : value.CSTYPE.CSCHAMP
 * 
 *         value : nom unique pour identifier nos champs CSTYPE : type du champ date ou checkbox CSCHAMP: CS du champ
 *         (pas obligatoire)
 */
public class LEEditerFormuleHandler {
    public final static String PART_SEPARATOR = "_";
    public String _champ = null;

    private String _idPart = "";
    // pour savoir si le champ est bien-form�e
    private boolean _isWellFormed = false;
    private String _typePart = "";
    private String _valuePart = "";
    private int nbPart;

    public LEEditerFormuleHandler() {
    }

    public LEEditerFormuleHandler(String champ) {
        _champ = champ;
        _parse();
    }

    private void _parse() {
        if (!JAUtil.isStringEmpty(_champ)) {
            try {
                nbPart = 0;
                for (int i = 0; i < _champ.length(); i++) {
                    if (_champ.charAt(i) == PART_SEPARATOR.charAt(0)) {
                        nbPart++;
                    }
                }

                StringTokenizer champParts = new StringTokenizer(_champ, PART_SEPARATOR);
                // rempli les diff�rentes parties
                _idPart = champParts.nextToken();
                _typePart = champParts.nextToken();
                _valuePart = champParts.nextToken();

                // si une seul des parties est vide ou null, l'action n'est pas
                // bien form�e
                if (JAUtil.isStringEmpty(_idPart) || JAUtil.isStringEmpty(_typePart)) {
                    _isWellFormed = false;
                } else {
                    // tous c'est bien pass�, l'action est bien form�e
                    _isWellFormed = true;
                }
            } catch (Exception e) {
                _isWellFormed = false;
            }
        } else {
            // un champ vide ou null est consid�r�e comme mal form�e
            _isWellFormed = false;
        }
    }

    /**
     * @return le champ complet s�par� par des points
     */
    public String get_champ() {
        return _champ;
    }

    /**
     * @return la partie identifiante : en principe VALUE
     */
    public String get_idPart() {
        return _idPart;
    }

    /**
     * @return le type de champ : en principe date ou choix
     */
    public String get_typePart() {
        return _typePart;
    }

    /**
     * @return la valeur du CS du champ (pas obligatoire)
     */
    public String get_valuePart() {
        return _valuePart;
    }

    /**
     * @return le nombre de parties
     */
    public int getNbPart() {
        return nbPart;
    }

    /**
     * @return Nous renseigne si le champ est bien format� et correct
     */
    public boolean is_isWellFormed() {
        return _isWellFormed;
    }

}
