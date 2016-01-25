package globaz.osiris.db.utils;

import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 13:22:22)
 * 
 * @author: Administrator
 */
public class CAGestionRemarque {
    private IntRemarque _intRemarque = null;
    private CARemarque remarque;

    /**
     * Insérez la description de la méthode ici. Date de création : (29.08.2002 08:35:07)
     * 
     * @param param
     *            globaz.osiris.db.utils.IntRemarque
     */
    public CAGestionRemarque(IntRemarque interfaceRemarque) {
        _intRemarque = interfaceRemarque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.08.2002 08:41:22)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void add(BTransaction transaction) throws Exception {

        // Lecture de la remarque
        retrieve(transaction);

        // Ajout d'une remarque
        if (remarque == null) {
            remarque = new CARemarque();
            if (!JadeStringUtil.isBlank(_intRemarque.getTexteRemarque().trim())) {
                remarque.setTexte(_intRemarque.getTexteRemarque());
                remarque.setISession(_intRemarque.getISession());
                remarque.add(transaction);
                _intRemarque.setIdRemarque(remarque.getIdRemarque());
                if (remarque.isNew() || remarque.hasErrors()) {
                    throw new Exception("L'ajout de la remarque a échoué");
                }
            }
        } else {
            if (!remarque.isNew() && !remarque.hasErrors()) {
                if (JadeStringUtil.isBlank(_intRemarque.getTexteRemarque().trim())) {
                    delete(transaction);
                    if (remarque.hasErrors()) {
                        throw new Exception("La suppression de la remarque échoué");
                    }
                } else {
                    remarque.setTexte(_intRemarque.getTexteRemarque());
                    remarque.setISession(_intRemarque.getISession());
                    remarque.update(transaction);
                    _intRemarque.setIdRemarque(remarque.getIdRemarque());
                    if (remarque.isNew() || remarque.hasErrors()) {
                        throw new Exception("La mise à jour de la remarque a échoué");
                    }
                }
            }
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.08.2002 08:55:36)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void delete(BTransaction transaction) throws java.lang.Exception {
        // Effacement d'une remarque
        if (remarque != null) {
            remarque.setIdRemarque(_intRemarque.getIdRemarque());
            remarque.setISession(_intRemarque.getISession());
            remarque.delete(transaction);
            if (remarque.isNew() && !remarque.hasErrors()) {
                _intRemarque.setIdRemarque("");
            }
            remarque = new CARemarque();
            remarque.setISession(_intRemarque.getISession());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 07:30:56)
     * 
     * @return globaz.osiris.db.utils.CARemarque
     */
    public CARemarque getRemarque() {
        return remarque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.08.2002 08:56:20)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void retrieve(BTransaction transaction) {

        // Test si identifiant existe
        if (!JadeStringUtil.isIntegerEmpty(_intRemarque.getIdRemarque())) {

            if (remarque == null) {
                remarque = new CARemarque();

                // lecture de la remarque
                remarque.setIdRemarque(_intRemarque.getIdRemarque());
                remarque.setISession(_intRemarque.getISession());
                try {
                    remarque.retrieve(transaction);
                    if (remarque.isNew() || remarque.hasErrors()) {
                        remarque = null;
                    }
                } catch (Exception e) {
                    remarque = null;
                }
            }
        } else {
            remarque = null;
        }
    }
}
