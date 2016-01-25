package globaz.osiris.db.ventilation;

import globaz.osiris.api.APIVPDetailMontant;
import java.util.HashMap;

/**
 * Liste des postes pour une même section. La clé correspond à l'id de la rubrique.
 * 
 * @author jmc Créé le 24 nov. 05
 * 
 */
public class CAVPListePosteParSection {
    public static final String MARGE_MONTANT_A_REPARTIR = "0.05";
    private HashMap<String, CAVPPoste> postesDeLaSectionParRubriqueMap; // key=idRubrique

    /**
     * Constructeur par défaut
     */
    public CAVPListePosteParSection() {
        super();
        postesDeLaSectionParRubriqueMap = new HashMap<String, CAVPPoste>();
    }

    /**
     * Ajoute un(e) poste/écriture dans la liste en fonction du numéro de la rubrique(key). si le poste existe déjà dans
     * la liste on le modifie, sinon on l'ajoute.
     * 
     * @param key
     *            : idRubrique
     * @param poste
     */
    public void addPosteDansListeParSection(String key, CAVPPoste poste) {
        if (postesDeLaSectionParRubriqueMap.containsKey(key)) {
            CAVPPoste posteInter = postesDeLaSectionParRubriqueMap.get(key);
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).getMontantBase());
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).getMontantBase());
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase());

        } else {
            postesDeLaSectionParRubriqueMap.put(key, poste);
        }
    }

    /**
     * @param key
     * @param poste
     * @param annee
     */
    public void addPosteDansListeParSection(String key, CAVPPoste poste, String annee) {
        if (postesDeLaSectionParRubriqueMap.containsKey(key)) {
            CAVPPoste posteInter = postesDeLaSectionParRubriqueMap.get(key);
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).getMontantBase(), annee);
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).getMontantBase(), annee);
            posteInter.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).addMontant(
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase(), annee);

        } else {
            postesDeLaSectionParRubriqueMap.put(key, poste);
        }
    }

    /**
     * vérifie si la rubrique est présente.
     * 
     * @param key
     *            : idRubrique
     * @return true si la clé existe.
     */
    public boolean containsKey(String key) {
        return postesDeLaSectionParRubriqueMap.containsKey(key);
    }

    /**
     * @param idRubrique
     *            : idRubrique
     * @return le poste de la rubrique.
     */
    public CAVPPoste getPoste(String idRubrique) {
        return postesDeLaSectionParRubriqueMap.get(idRubrique);
    }

    /**
     * @return
     */
    public HashMap<String, CAVPPoste> getPostesDeLaSectionParRubriqueMap() {
        return postesDeLaSectionParRubriqueMap;
    }

}
