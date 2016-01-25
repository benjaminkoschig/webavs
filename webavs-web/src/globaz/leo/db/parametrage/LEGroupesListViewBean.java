/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENGroupeChampsManager;
import globaz.envoi.db.parametreEnvoi.access.IENGroupeChampsDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEGroupesListViewBean extends ENGroupeChampsManager {
    private final static String DEFINITION_FORMULE_TABLE_NAME = "ENPPDEF";
    private final static String FORMULE_TABLE_NAME = "ENPPFO1";
    private final static String ID_DEFINITION_FORMULE = "PDEFID";
    private static final long serialVersionUID = 1L;
    String forCsFormule;

    /*
     * JOINTURE POUR LE le setForCsFormule (DefinitionFormule->Formule->GroupeChamp) INNER JOIN WEBAVSP.ENPPFO1 FORMULE
     * ON GROUPE.PFO1ID = FORMULE.PFO1ID INNER JOIN WEBAVSP.ENPPDEF DEFFORMULE ON DEFFORMULE.PDEFID=FORMULE.PDEFID
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();
        sqlFrom.append(super._getFrom(statement));
        sqlFrom.append(" GROUPE ");
        sqlFrom.append(" INNER JOIN " + _getCollection() + FORMULE_TABLE_NAME + " FORMULE ON FORMULE."
                + IENGroupeChampsDefTable.ID_FORMULE + " = GROUPE." + IENGroupeChampsDefTable.ID_FORMULE);
        sqlFrom.append(" INNER JOIN " + _getCollection() + DEFINITION_FORMULE_TABLE_NAME + " DEFFORMULE ON DEFFORMULE."
                + ID_DEFINITION_FORMULE + " = FORMULE." + ID_DEFINITION_FORMULE);
        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(super._getWhere(statement));
        if (!JadeStringUtil.isBlank(getForCsFormule())) {
            if (!JadeStringUtil.isBlank(sqlWhere.toString())) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" PDEFDO=" + getForCsFormule());
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEGroupeViewBean();
    }

    /**
     * @return
     */
    public String getForCsFormule() {
        return forCsFormule;
    }

    /**
     * @param string
     */
    public void setForCsFormule(String string) {
        forCsFormule = string;
    }
}
