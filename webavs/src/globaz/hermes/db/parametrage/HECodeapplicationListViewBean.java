package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Insérez la description du type ici. Date de création : (15.10.2002 13:12:23)
 * 
 * @author: Administrator
 */
public class HECodeapplicationListViewBean extends FWParametersSystemCodeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(String[] args) {
        // Insérez ici le code de démarrage de l'application
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangue("D");
            session.connect("ssii", "ssiiadm");
            HECodeapplicationListViewBean codappM = new HECodeapplicationListViewBean();
            codappM.setSession(session);
            // codappM.setForCodeUtilisateur("11");
            codappM.find();
            for (int i = 0; i < codappM.size(); i++) {
                HECodeapplicationViewBean codeApp = (HECodeapplicationViewBean) codappM.getEntity(i);
                System.out.println(codeApp.getCurrentCodeUtilisateur().getCodeUtiLib());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    public HECodeapplicationListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.01.2002 11:07:08)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws java.lang.Exception {
        setForIdGroupe(HECodeapplicationViewBean.GROUPE);
        setForIdTypeCode(HECodeapplicationViewBean.TYPE_CODE);
        setForIdLangue(getSession().getIdLangue());
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new HECodeapplicationViewBean();
    }
}
