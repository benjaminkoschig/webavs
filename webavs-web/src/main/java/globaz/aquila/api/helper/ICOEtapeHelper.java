/*
 * Créé le 2 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api.helper;

import globaz.aquila.api.ICOEtape;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ICOEtapeHelper extends GlobazHelper implements ICOEtape {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static String ETAPE_CONTENTIEUX_JUSQUA_RP_SQL_FORMAT = ICOEtape.CS_AUCUNE + "," + ICOEtape.CS_DECISION + ","
            + ICOEtape.CS_PREMIER_RAPPEL_ENVOYE + "," + ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE + ","
            + ICOEtape.CS_SOMMATION_ENVOYEE;

    public static String ETAPE_CONTENTIEUX_SQL_NOT_IN_FORMAT = "0, " + ICOEtape.CS_ARD_CREE + ", " + ICOEtape.CS_AUCUNE
            + ", " + ICOEtape.CS_CONTENTIEUX_CREE;
    public static String ETAPE_POURSUITE_SQL_NOT_IN_FORMAT = "0, " + ICOEtape.CS_ARD_CREE + ", " + ICOEtape.CS_AUCUNE
            + ", " + ICOEtape.CS_CONTENTIEUX_CREE + ", " + ICOEtape.CS_DECISION + ", "
            + ICOEtape.CS_PREMIER_RAPPEL_ENVOYE + ", " + ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE + ", "
            + ICOEtape.CS_SOMMATION_ENVOYEE;

    private static String IMPLEMENTATION_CLASS_NAME = "globaz.aquila.db.access.batch.COEtape";
    private static String IMPLEMENTATION_LOAD_METHOD_NAME = "load";
    private static String MNAME_CS_ACTION = "libAction";
    private static String MNAME_CS_ETAPE = "libEtape";

    private static String MNAME_ID_ETAPE = "idEtape";
    private static String MNAME_ID_SEQUENCE_CONTENTIEUX = "idSequence";
    private static final long serialVersionUID = 5105509574101603613L;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param CS_Etape
     * @return true si l'étape reçue est la RP ou après la RP, false si l'étape est avant la RP
     */
    public static boolean isEtapePoursuite(String CS_Etape) {
        if (JadeStringUtil.isBlank(CS_Etape)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_ARD_CREE)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_AUCUNE)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_CONTENTIEUX_CREE)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_DECISION)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_PREMIER_RAPPEL_ENVOYE)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE)) {
            return false;
        } else if (CS_Etape.equals(ICOEtape.CS_SOMMATION_ENVOYEE)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Crée une nouvelle instance de la classe ICOContentieuxHelper.
     */
    public ICOEtapeHelper() {
        super(ICOEtapeHelper.IMPLEMENTATION_CLASS_NAME);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private ICOEtapeHelper(GlobazValueObject vo) {
        super(vo);
    }

    /**
     * @see globaz.aquila.api.ICOEtape#getCsAction()
     */
    @Override
    public String getCsAction() {
        return (String) _getValueObject().getProperty(ICOEtapeHelper.MNAME_CS_ACTION);
    }

    /**
     * @see globaz.aquila.api.ICOEtape#getCsEtape()
     */
    @Override
    public String getCsEtape() {
        return (String) _getValueObject().getProperty(ICOEtapeHelper.MNAME_CS_ETAPE);
    }

    /**
     * @see globaz.aquila.api.ICOEtape#getIdEtape()
     */
    @Override
    public String getIdEtape() {
        return (String) _getValueObject().getProperty(ICOEtapeHelper.MNAME_ID_ETAPE);
    }

    /**
     * @see globaz.aquila.api.ICOEtape#getIdSequence()
     */
    @Override
    public String getIdSequence() {
        return (String) _getValueObject().getProperty(ICOEtapeHelper.MNAME_ID_SEQUENCE_CONTENTIEUX);
    }

    /**
     * @see globaz.aquila.api.ICOEtape#load(java.util.HashMap)
     */
    @Override
    public Collection load(HashMap criteres) throws Exception {
        LinkedList retValue = new LinkedList();
        Object[] vos = _getArray(ICOEtapeHelper.IMPLEMENTATION_LOAD_METHOD_NAME, new Object[] { criteres });

        if (vos != null) {
            for (int idVO = 0; idVO < vos.length; ++idVO) {
                retValue.add(new ICOEtapeHelper((GlobazValueObject) vos[idVO]));
            }
        }

        return retValue;
    }
}
