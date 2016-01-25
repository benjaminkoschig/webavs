package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.hermes.utils.StringUtils;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:00:54)
 * 
 * @author: ado
 */
public class HEChampsViewBean extends FWParametersSystemCode implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String GROUPE = "HECHAMPS";
    public final static String ORDRE = "1";
    public final static String TYPE_CODE = "11100008";

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application
        // Insérez ici le code de démarrage de l'application
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HEChampsViewBean champs = new HEChampsViewBean();
            champs.setSession(session);
            // AJOUT
            // champs.setCodeUti("XXX");
            // champs.setCodeApplicationLibelle("CODE APP POUR TEST");
            // champs.add();
            // Retrieve
            long s = System.currentTimeMillis();
            champs.setId("118002");
            champs.retrieve();
            System.out.println(champs.getIdCode() + "-" + champs.getCodeUti() + "-" + champs.getLibelle());
            System.out.println(System.currentTimeMillis() - s);
            s = System.currentTimeMillis();
            //
            champs.setId("118003");
            champs.retrieve();
            System.out.println(champs.getIdCode() + "-" + champs.getCodeUti() + "-" + champs.getLibelle());
            System.out.println(System.currentTimeMillis() - s);
            // delete
            // champs.delete();
            // MODIFY
            // champs.setCodeApplicationLibelle("CODE APP POUR TEST MOD");
            // champs.update();
            // champs.retrieve();
            if (champs.hasErrors()) {
                throw new Exception(champs.getErrors().toString());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /**
     * Commentaire relatif au constructeur HEChampsViewBean.
     */
    public HEChampsViewBean() {
        super();
        setCurrentCodeUtilisateur(new FWParametersUserCode());
    }

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

    public void setChampsLibelle(String libelle) {
        // tronquer à 40
        setLibelle(StringUtils.trimString(libelle, 40));
        // tronquer à 60
        setLibelleUti(StringUtils.trimString(libelle, 60));
    }
}
