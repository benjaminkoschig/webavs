/*
 * Créé le 18 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api.helper;

import globaz.aquila.api.ICOSequence;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ICOSequenceHelper extends GlobazHelper implements ICOSequence {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static String IMPLEMENTATION_CLASS_NAME = "globaz.aquila.db.access.batch.COSequence";
    private static String IMPLEMENTATION_METHODE_NAME = "load";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String MNAME_CS_SEQUENCE = "libSequence";
    private String MNAME_ID_SEQUENCE = "idSequence";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ICOSequenceHelper.
     */
    public ICOSequenceHelper() {
        super(ICOSequenceHelper.IMPLEMENTATION_CLASS_NAME);
    }

    private ICOSequenceHelper(GlobazValueObject vo) {
        super(vo);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.api.ICOSequence#getCsSequence()
     */
    @Override
    public String getCsSequence() {
        return (String) _getValueObject().getProperty(MNAME_CS_SEQUENCE);
    }

    /**
     * @see globaz.aquila.api.ICOSequence#getIdSequence()
     */
    @Override
    public String getIdSequence() {
        return (String) _getValueObject().getProperty(MNAME_ID_SEQUENCE);
    }

    /**
     * @see globaz.aquila.api.ICOSequence#load(java.util.Map)
     */
    @Override
    public Collection load(Map criteres) throws Exception {
        LinkedList retValue = new LinkedList();
        Object[] vos = _getArray(ICOSequenceHelper.IMPLEMENTATION_METHODE_NAME, new Object[] { criteres });

        if (vos != null) {
            for (int idVO = 0; idVO < vos.length; ++idVO) {
                retValue.add(new ICOSequenceHelper((GlobazValueObject) vos[idVO]));
            }
        }

        return retValue;
    }
}
