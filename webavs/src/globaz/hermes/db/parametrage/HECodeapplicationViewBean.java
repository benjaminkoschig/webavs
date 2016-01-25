package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.hermes.utils.StringUtils;

/**
 * Insérez la description du type ici. Date de création : (15.10.2002 12:01:42)
 * 
 * @author: Administrator
 */
public class HECodeapplicationViewBean extends FWParametersSystemCode implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String GROUPE = "HECODAPP";
    public final static String ORDRE = "1";
    public final static String TYPE_CODE = "11100001";

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
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HECodeapplicationViewBean ca = new HECodeapplicationViewBean();
            ca.setSession(session);
            // AJOUT
            // ca.setCodeUti("XXX");
            // ca.setCodeApplicationLibelle("CODE APP POUR TEST");
            // ca.add();
            // Retrieve
            long s = System.currentTimeMillis();
            ca.setId("111002");
            ca.retrieve();
            System.out.println(ca.getIdCode() + "-" + ca.getCodeUti() + "-" + ca.getLibelle());
            System.out.println(System.currentTimeMillis() - s);
            s = System.currentTimeMillis();
            ca.setId("111001");
            ca.retrieve();
            System.out.println(ca.getIdCode() + "-" + ca.getCodeUti() + "-" + ca.getLibelle());
            System.out.println(System.currentTimeMillis() - s);
            // delete
            // ca.delete();
            // MODIFY
            // ca.setCodeApplicationLibelle("CODE APP POUR TEST MOD");
            // ca.update();
            // ca.retrieve();
            // System.out.println(ca.getIdCode() + "-" + ca.getCodeUti() + "-" +
            // ca.getLibelle());
            if (ca.hasErrors()) {
                throw new Exception(ca.getErrors().toString());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /**
     * Commentaire relatif au constructeur HECodeApplicationManager.
     */
    public HECodeapplicationViewBean() {
        super();
        setCurrentCodeUtilisateur(new FWParametersUserCode());
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdCode(_incCounter(transaction, getIdCode(), GROUPE, TYPE_CODE));
        setIdTypeCode(TYPE_CODE);
        setOrdre(ORDRE);
        setGroupe(GROUPE);
    }

    public String getCodeUti() {
        return getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    @Override
    public String getLibelle() {
        return getCurrentCodeUtilisateur().getLibelle();
    }

    public void setCodeApplicationLibelle(String libelle) {
        // tronquer à 40
        setLibelle(StringUtils.trimString(libelle, 40));
        // tronquer à 60
        setLibelleUti(StringUtils.trimString(libelle, 60));
    }
}
