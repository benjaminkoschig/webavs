/*
 * Créé le 8 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.parametrage.LEComplementFormuleManager;
import globaz.leo.db.parametrage.LEComplementFormuleViewBean;
import java.util.Vector;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEtapesSuivantesViewBean extends LEEnvoiViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorie = new String();
    private String etapeSuivante = new String();
    private Vector formule = new Vector();

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        etapeSuivante = statement.dbReadNumeric("JCJOVA");
    }

    /**
     * @return
     */
    public String getCategorie() {
        return categorie;
    }

    public String getEtapeSuivante() {
        return etapeSuivante;
    }

    // public void setStatut(String csStatut) throws Exception{
    // // mets à jour le statut dans complément journal
    // LUComplementJournalListViewBean cpl = new
    // LUComplementJournalListViewBean();
    // cpl.setSession(getSession());
    // cpl.setForIdJournalisation(getIdJournalisation());
    // cpl.setForCsTypeCodeSysteme(ILEConstantes.CS_STATUT_GROUPE);
    // cpl.find();
    // if(cpl.size()>0){
    // LUComplementJournalViewBean vb =
    // (LUComplementJournalViewBean)cpl.getFirstEntity();
    // vb.setValeurCodeSysteme(csStatut);
    // vb.update();
    // }else{
    // throw new
    // Exception("Erreur : aucun statut trouvé pour les paramètres suivants : idJournal="+getIdJournalisation()+" et type code système="+ILEConstantes.CS_STATUT_GROUPE);
    // }
    // }
    /**
     * @return
     */
    public Vector getFormule() {
        if (formule.size() > 0) {
            return formule;
        } else {
            return new Vector();
        }

    }

    public Vector getFormulesList(BSession sessionUt, String categorie) {
        Vector formule = new Vector();
        LEComplementFormuleManager manager = new LEComplementFormuleManager();
        manager.setSession(sessionUt);
        if (!JAUtil.isStringNull(categorie)) {
            manager.setForCsValeur(categorie);
        }
        manager.setWantDebutOrFin(false);
        try {
            manager.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ajoute un espace blanc
        formule.add(new String[] { "", "" });
        // ajout une à une les catégories
        for (int i = 0; i < manager.getSize(); i++) {
            LEComplementFormuleViewBean vb = (LEComplementFormuleViewBean) manager.getEntity(i);
            if ((!vb.getDefinitionDoc().equals(ILEConstantes.CS_DEBUT_SUIVI_LPP))
                    && (!vb.getDefinitionDoc().equals(ILEConstantes.CS_DEBUT_SUIVI_LAA))
                    && (!vb.getDefinitionDoc().equals(ILEConstantes.CS_DEBUT_SUIVI_ATTESTATION_IP))) {
                formule.add(new String[] { vb.getDefinitionDoc(), sessionUt.getCodeLibelle(vb.getDefinitionDoc()) });
            }
        }
        return formule;
    }

    /**
     * @param string
     */
    public void setCategorie(String string) {
        categorie = string;
    }

    public void setEtapeSuivante(String string) {
        etapeSuivante = string;
    }

    /**
     * @param vector
     */
    public void setFormule(Vector vector) {
        formule = vector;
    }

}
