package globaz.hermes.zas;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;

/**
 * Insérez la description du type ici. Date de création : (17.07.2003 09:46:39)
 * 
 * @author: ado
 */
public class HEMajPavo {
    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("usage : java HEMajPavo <idLot>");
            System.exit(-1);
        }
        new HEMajPavo().go(args[0]);
        System.out.println("bye");
        System.exit(1);
    }

    private HEApplication currentApplication;

    private BSession session;

    /**
     * Commentaire relatif au constructeur HEMajPavo.
     */
    public HEMajPavo() throws Exception {
        super();
        //
        try {
            session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("globazf", "ssiiadm");
            //
            currentApplication = ((HEApplication) getSession().getApplication());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.07.2003 09:55:34)
     * 
     * @return globaz.globall.db.BSession
     */
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    // PUSH PAVO si enregistrement 01 annonce 21, 22, 23, 29
    public void go(String idLot) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            HEOutputAnnonceListViewBean liste = new HEOutputAnnonceListViewBean(getSession());
            liste.setForIdLot(idLot);
            liste.find(transaction);
            if (liste.size() == 0) {
                System.out.println("Pas d'ARC pour le lot " + idLot);
            }
            for (int i = 0; i < liste.size(); i++) {
                HEOutputAnnonceViewBean line = (HEOutputAnnonceViewBean) liste.getEntity(i);
                if (line.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
                    if (line.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("21")
                            || line.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("22")
                            || line.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("23")
                            || line.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("29")) {
                        // currentApplication.pushAnnonceCi(getSession(), line);
                    }
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                transaction = null;
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.07.2003 09:55:34)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }
}
