package globaz.helios.itext.list.plancomptable;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.db.classifications.CGExtendedCompte;
import globaz.helios.db.classifications.CGExtendedCompteManager;
import globaz.helios.itext.list.utils.CGGeneric_Bean;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRCloneableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGPlanComptable_DS extends CGExtendedCompteManager implements JRCloneableDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private HashMap beans = new HashMap();
    private CGExtendedCompte entity = null;
    private BProcess processCtx = null;
    private globaz.globall.db.BStatement statement = null;

    private globaz.globall.db.BTransaction transaction = null;

    /**
     * Constructor for CGPlanComptable_DS.
     */
    public CGPlanComptable_DS() {
        super();
    }

    /**
     * Constructor for CGPlanComptable_DS.
     */
    public CGPlanComptable_DS(BSession session) {
        setSession(session);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.03.2003 14:40:10)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#getContext()
     */
    public BProcess getContext() {
        return processCtx;
    }

    /**
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(JRField)
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {

        // retourne chaque champ

        String entityID = entity.getId();
        CGGeneric_Bean currentBean = (beans.containsKey(entityID)) ? (CGGeneric_Bean) beans.get(entityID)
                : new CGGeneric_Bean();

        // On renseigne la clé de tri
        currentBean.setSortKey(entity.getIdExterne());

        // info classe de compte principale
        if (jrField.getName().equals("COL_1")) {
            currentBean.setCol("", 1, entity.getNoClasseNiv1() + " " + entity.getLibelleClasseNiv1());
            beans.put(entityID, currentBean);
            return entity.getNoClasseNiv1() + " " + entity.getLibelleClasseNiv1();
        }
        // info classe de compte secondaire
        if (jrField.getName().equals("COL_2")) {
            currentBean.setCol("", 2, entity.getNoClasseNiv2() + " " + entity.getLibelleClasseNiv2());
            beans.put(entityID, currentBean);
            return entity.getNoClasseNiv2() + " " + entity.getLibelleClasseNiv2();
        }
        // Infos de compte
        if (jrField.getName().equals("COL_3")) {
            currentBean.setCol("", 3, entity.getIdExterne() + " " + entity.getLibellePlanComptable());
            beans.put(entityID, currentBean);
            return entity.getIdExterne() + " " + entity.getLibellePlanComptable();
        }
        if (jrField.getName().equals("COL_4")) {
            currentBean.setCol("", 4, entity.getDomaineLibelle());
            beans.put(entityID, currentBean);
            return entity.getDomaineLibelle();
        }
        if (jrField.getName().equals("COL_5")) {
            currentBean.setCol("", 5, entity.getGenreLibelle());
            beans.put(entityID, currentBean);
            return entity.getGenreLibelle();
        }
        if (jrField.getName().equals("COL_6")) {
            currentBean.setCol("", 6, entity.getNatureLibelle());
            beans.put(entityID, currentBean);
            return entity.getNatureLibelle();
        }
        if (jrField.getName().equals("COL_7")) {
            currentBean.setCol("", 7, entity.getCodeISOMonnaie());
            beans.put(entityID, currentBean);
            return entity.getCodeISOMonnaie();
        }
        if (jrField.getName().equals("COL_8")) {
            currentBean.setCol("", 8, (entity.isEstVerrouille().booleanValue()) ? "X" : "");
            beans.put(entityID, currentBean);
            return (entity.isEstVerrouille().booleanValue()) ? "X" : "";
        }
        if (jrField.getName().equals("COL_9")) {
            currentBean.setCol("", 9, (entity.isEstConfidentiel().booleanValue()) ? "X" : "");
            beans.put(entityID, currentBean);
            return (entity.isEstConfidentiel().booleanValue()) ? "X" : "";
        }

        return null;

    }

    public ArrayList getListBeans() {
        return new ArrayList(beans.values());
    }

    /**
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        try {

            if ((processCtx != null) && processCtx.isAborted()) {
                // On met entity a null afin que la transaction soir correctement fermée dans le block finally
                entity = null;
                return false;
            }

            // Ouvre le curseur si c'est le statement est null -> donc pas
            // encore ouvert
            if (statement == null) { // Cursor not open
                transaction = new globaz.globall.db.BTransaction(getSession());
                transaction.openTransaction();
                statement = cursorOpen(transaction);
            }
            // lit le nouveau entity
            entity = (CGExtendedCompte) cursorReadNext(statement);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.clearErrorBuffer();
            }
            e.printStackTrace();
        } finally {
            // Si l'entity est null donc on est à la fin du select -> on ferme
            // le curseur
            if (entity == null) { // Close cursor
                try {
                    if (statement != null) {
                        cursorClose(statement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (transaction != null) {
                            transaction.closeTransaction();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#setContext(BProcess)
     */
    public void setContext(BProcess process) {
        processCtx = process;
    }

}
