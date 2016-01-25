package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.hermes.utils.StringUtils;

/**
 * Insérez la description du type ici. Date de création : (21.10.2002 14:53:40)
 * 
 * @author: Administrator
 */
public class HEMotifsViewBean extends FWParametersSystemCode implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //
    public final static String CS_AUCUN_MOTIF = "111229"; // Motifs
    public final static String GROUPE = "HEMOTIFS";
    public final static String ORDRE = "1";
    public final static String TYPE_CODE = "11100002";

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
            HEMotifsViewBean motif = new HEMotifsViewBean();
            motif.setSession(session);
            // AJOUT
            // motif.setCodeUti("YYY");
            // motif.setMotifLibelle("MOTIF POUR TEST");
            // motif.add();
            // Retrieve
            motif.setIdCode("111201");
            motif.retrieve();
            // delete
            // motif.delete();
            // MODIFY
            // motif.setCodeUti("YYYY");
            // motif.setMotifLibelle("MOTIF POUR TEST MOD");
            // motif.update();
            // motif.retrieve();
            System.out.println(motif.getIdCode() + "-" + motif.getCodeUti() + "-" + motif.getLibelle());
            if (motif.hasErrors()) {
                throw new Exception(motif.getErrors().toString());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /**
     * Commentaire relatif au constructeur HEMotifs.
     */
    public HEMotifsViewBean() {
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

    protected void _beforeOldDelete(BTransaction transaction) throws java.lang.Exception {
        // on regarde si il reste un lien sur les traitements ou sur les motifs
        // code application
        /*
         * FWParametersUserCode uc = new FWParametersUserCode(); uc.setSession(getSession());
         * uc.setIdCodeSysteme(getCodeApplication()); uc.setIdLangue(getSession().getIdLangue()); uc.retrieve();
         */
        // y'a-t'il des liens avec les Motifs Code application
        /*
         * HEMotifcodeapplicationManager mca = new HEMotifcodeapplicationManager(); mca.setSession(getSession());
         * mca.setForIdMotif(getCodeUtilisateur()); mca.find(); if (mca.size() > 0) { // y'a des liens avec les codes
         * applications !! _addError(transaction, Constantes.FR_E_00003); // on quitte sans supprimer return; } else if
         * (false) { // y'a-t'il des liens avec les Traitements } else { // aucun lien, on peut supprimer uc.delete(); }
         */
    }

    @Override
    public void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // On regarde si on peut modifier
        // est-ce qu'on tente de changer le numéro du motif ??
        /*
         * FWParametersUserCode uc = new FWParametersUserCode(); uc.setSession(getSession());
         * uc.setIdCodeSysteme(getCodeApplication()); uc.setIdLangue(getSession().getIdLangue()); uc.retrieve(); if
         * (!newCode.equals(getCodeUtilisateur())) { HEMotifcodeapplicationManager mca = new
         * HEMotifcodeapplicationManager(); mca.setSession(getSession()); mca.setForIdMotif(getCodeUtilisateur());
         * mca.find(); if (mca.size() > 0) { // y'a des liens avec les codes applications !! _addError(transaction,
         * Constantes.FR_E_00002); // on met qd même à jour le libellé
         * uc.setLibelle(StringUtils.trimString(getLibelleCodeApplication(), 60)); uc.update(); // et on quitte return;
         * } else if (false) { // y'a-t'il des liens avec les Traitements } else { // aucun lien, on peut changer le
         * numéro et le libellé uc.setCodeUtilisateur(newCode);
         * uc.setLibelle(StringUtils.trimString(getLibelleCodeApplication(), 60)); uc.update(); } } else { // le numéro
         * change pas, le libellé oui uc.setLibelle(StringUtils.trimString(getLibelleCodeApplication(), 60));
         * uc.update(); }
         */
    }

    public String getCodeUti() {
        return getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    @Override
    public String getLibelle() {
        return getCurrentCodeUtilisateur().getLibelle();
    }

    public void setMotifLibelle(String libelle) {
        // tronquer à 40
        setLibelle(StringUtils.trimString(libelle, 40));
        // tronquer à 60
        setLibelleUti(StringUtils.trimString(libelle, 60));
    }
}
