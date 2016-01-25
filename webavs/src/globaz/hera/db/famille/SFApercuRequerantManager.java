/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFApercuRequerantManager extends SFMembreFamilleManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BSession session = new BSession("HERA");
            session.connect("globazf", "ssiiadm");
            /***************************************/
            SFApercuRequerantManager arm = new SFApercuRequerantManager();
            arm.setSession(session);

            arm.find();
            for (int i = 0; i < arm.getSize(); i++) {
                System.out.println(arm.getEntity(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String forIdDomaineApplication = null;

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return SFApercuRequerant.createFromClause(_getCollection());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isEmpty(getForIdDomaineApplication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRequerant.TABLE_NAME + "." + SFRequerant.FIELD_IDDOMAINEAPPLICATION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDomaineApplication());
        }
        return sqlWhere;

    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFApercuRequerant();
    }

    /**
     * @return
     */
    public String getForIdDomaineApplication() {
        return forIdDomaineApplication;
    }

    /**
     * @param string
     */
    public void setForIdDomaineApplication(String string) {
        forIdDomaineApplication = string;
    }

}