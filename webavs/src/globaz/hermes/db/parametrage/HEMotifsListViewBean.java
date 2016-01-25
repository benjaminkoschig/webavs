package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (25.10.2002 15:46:59)
 * 
 * @author: Administrator
 */
public class HEMotifsListViewBean extends FWParametersSystemCodeManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        // Ins�rez ici le code de d�marrage de l'application
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HEMotifsListViewBean motifsM = new HEMotifsListViewBean();
            motifsM.setSession(session);
            motifsM.setForCodeUtilisateur("95");
            motifsM.find();
            System.out.println(motifsM.size());
            HEMotifsViewBean motif = (HEMotifsViewBean) motifsM.getEntity(0);
            System.out.println(motif.getIdCode() + "-" + motif.getCodeUti() + "-" + motif.getLibelle());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    public HEMotifsListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.01.2002 11:07:08)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws java.lang.Exception {
        setForIdGroupe(HEMotifsViewBean.GROUPE);
        setForIdTypeCode(HEMotifsViewBean.TYPE_CODE);
    }

    @Override
    protected java.lang.String _getOrder(BStatement statement) {
        return "PCOUID , PCOSLI";
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new HEMotifsViewBean();
    }
}
