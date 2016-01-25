/**
 *
 */
package globaz.osiris.db.ventilation;

import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIVPDetailMontant;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author sel 18.08.2009
 */
public class CAVPListePosteParRubrique {
    /**
     * Marge que l'on peut se permettre de mettre dans la dernière rubrique ventilée
     */
    private static final String MARGE_MONTANT_A_REPARTIR = "2.00";

    private HashMap<String, ArrayList<CAVPPoste>> listePosteParRubrique; // key=idRubrique
    private BigDecimal montantTotalAVentiler;

    /**
     * Constructeur par défaut
     */
    public CAVPListePosteParRubrique() {
        super();

        listePosteParRubrique = new HashMap<String, ArrayList<CAVPPoste>>();
        montantTotalAVentiler = new BigDecimal("0");
    }

    /**
     * Ajoute un Poste pour l'idRubrique
     * 
     * @param idRubrique
     * @param poste
     */
    public void addPoste(String idRubrique, CAVPPoste poste) {
        if (listePosteParRubrique.containsKey(idRubrique)) {
            listePosteParRubrique.get(idRubrique).add(poste);
        } else {
            ArrayList<CAVPPoste> newListe = new ArrayList<CAVPPoste>();
            newListe.add(poste);
            listePosteParRubrique.put(idRubrique, newListe);
        }
    }

    /**
     * Ajoute les postes de la section selon l'idRubrique
     * 
     * @param listePosteParSection
     */
    public void addPostes(CAVPListePosteParSection listePosteParSection) {
        for (String idRubrique : listePosteParSection.getPostesDeLaSectionParRubriqueMap().keySet()) {
            addPoste(idRubrique, listePosteParSection.getPoste(idRubrique));
        }
    }

    /**
     * vérifie si la rubrique est présente.
     * 
     * @param idRubrique
     *            : key
     * @return true si la clé existe.
     */
    public boolean containsKey(String idRubrique) {
        return listePosteParRubrique.containsKey(idRubrique);
    }

    /**
     * Méthode utile uniquement pour le débogage (affiche les infos dans la console). Ne doit pas être utilisée pour
     * code en production
     * 
     */
    public void displayListePosteParRubrique() {
        System.out.println("************************* contenu de listePosteParRubrique *************************");
        for (Map.Entry<String, ArrayList<CAVPPoste>> listePosteParRubriqueEntry : listePosteParRubrique.entrySet()) {
            System.out.println("idRubrique = " + listePosteParRubriqueEntry.getKey());
            for (CAVPPoste vpPoste : listePosteParRubriqueEntry.getValue()) {
                if (vpPoste.isMontantSimple()) {
                    CAVPDetailMontant detailMontantSimple = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
                    System.out.println("simple : base = " + detailMontantSimple.getMontantBase() + " ventilé = "
                            + detailMontantSimple.getMontantVentile());
                    for (Map.Entry<String, BigDecimal> montantParAnneeEntry : detailMontantSimple.getMontantParAnnee()
                            .entrySet()) {
                        String annee = montantParAnneeEntry.getKey();
                        BigDecimal montant = montantParAnneeEntry.getValue();
                        System.out.println("------------------------> montant par année : " + annee + " >> "
                                + montant.toString());
                    }
                } else {
                    CAVPDetailMontant detailMontantEmployeur = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
                    System.out.println("employeur : base = " + detailMontantEmployeur.getMontantBase() + " ventilé = "
                            + detailMontantEmployeur.getMontantVentile());

                    CAVPDetailMontant detailMontantSalarie = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
                    System.out.println("salarié : base = " + detailMontantSalarie.getMontantBase() + " ventilé = "
                            + detailMontantSalarie.getMontantVentile());
                }
            }
        }
    }

    /**
     * @param idRubrique
     * @return ArrayList de CAVPPoste
     */
    public ArrayList<CAVPPoste> getListePostes(String idRubrique) {
        return listePosteParRubrique.get(idRubrique);
    }

    /**
     * @param sectionAventiler
     * @return BigDecimal le montant à ventiler pour la section.
     */
    public BigDecimal getMontantAVentiler(CAVPListePosteParSection sectionAventiler) {
        if (sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER) == null) {
            return new BigDecimal("0");
        }
        BigDecimal montantAVentile = sectionAventiler.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER)
                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase();

        return montantAVentile;
    }

    /**
     * @param idRubrique
     * @param type
     *            valeur possible :
     *            <ul>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR</li>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_SALARIE</li>
     *            <li>APIVPDetailMontant.CS_VP_MONTANT_SIMPLE</li>
     *            </ul>
     * @return le total des postes pour une rubrique et un type
     */
    public BigDecimal getMontantTotal(String idRubrique, String type) {
        BigDecimal total = new BigDecimal("0");
        if (getListePostes(idRubrique) != null) {
            Iterator it = (getListePostes(idRubrique)).iterator();
            while (it.hasNext()) {
                CAVPPoste poste = (CAVPPoste) it.next();
                total = total.add(poste.getDetailMontant(type).getMontantBase());
            }
        }
        return total;
    }

    /**
     * @return the montantTotalAVentiler
     */
    public BigDecimal getMontantTotalAVentiler() {
        return montantTotalAVentiler;
    }

    /**
     * rempli la liste en fonction du tableau final fournit en paramètre
     * 
     * @param tableauFinal
     * @param listMontantBaseVentilationSection
     */
    public void remplirListePosteParRubrique(TreeMap<String, CAVPListePosteParSection> tableauFinal,
            HashMap<String, BigDecimal> listMontantBaseVentilationSection) {
        for (Map.Entry<String, CAVPListePosteParSection> tableauFinalEntry : tableauFinal.entrySet()) {
            String keyDateSectionIdSection = tableauFinalEntry.getKey();
            CAVPListePosteParSection postesDeLaSection = tableauFinalEntry.getValue();

            // Sort s'il s'agit du total
            BigDecimal montantAVentiler = getMontantAVentiler(postesDeLaSection);

            // Update le montant global
            montantTotalAVentiler = montantTotalAVentiler.add(montantAVentiler);

            // Liste Excel : Ajoute le total du montant à ventiler pour cette section.
            listMontantBaseVentilationSection.put(keyDateSectionIdSection, montantAVentiler);

            // ajoutes tous les posts de la section à listePosteParRubrique de la classe CAVPListePosteparRubrique
            addPostes(postesDeLaSection);
        }
    }

    /**
     * @param montantTotalAVentiler
     *            the montantTotalAVentiler to set
     */
    public void setMontantTotalAVentiler(BigDecimal montantTotalAVentiler) {
        this.montantTotalAVentiler = montantTotalAVentiler;
    }

    /**
     * met à jour montantTotalAVentiler et le poste correspondant à idSection et à CAVPListePostes.KEY_POSTE_A_VENTILER
     * 
     * @param idSection
     * @param montantVentile
     */

    private void updateMontantRestantAVentiler(String idSection, BigDecimal montantVentile) {
        montantTotalAVentiler = montantTotalAVentiler.subtract(montantVentile);

        ArrayList<CAVPPoste> posteListe = listePosteParRubrique.get(CAVPVentilateur.KEY_POSTE_A_VENTILER);
        // postList peut être null si aucun poste à ventiler
        if (posteListe != null) {
            for (CAVPPoste poste : posteListe) {
                if (poste.getIdSection().equals(idSection)) {
                    poste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).addMontantVentile(montantVentile);
                }
            }
        }
    }

    /**
     * Ventile les postes selon l'idRubrique et type.
     * 
     * @param ordre
     * @return total du montant ventilé
     */
    public BigDecimal ventilePostes(String idRubrique, String type) {
        BigDecimal totalSoustrait = new BigDecimal("0");
        if (getListePostes(idRubrique) != null) {
            for (CAVPPoste poste : getListePostes(idRubrique)) {
                poste.getDetailMontant(type).addMontantVentile(poste.getDetailMontant(type).getMontantBase());
                updateMontantRestantAVentiler(poste.getIdSection(), poste.getDetailMontant(type).getMontantBase());
                totalSoustrait = totalSoustrait.add(poste.getDetailMontant(type).getMontantBase());
            }
        }
        return totalSoustrait;
    }

    /**
     * Ventile les postes par prorata
     * 
     * @param idRubrique
     * @param typeOrdre
     * @param montantAVentile
     * @param montantDuPourOrdreCourant
     * @return
     */
    public BigDecimal ventilePostesParProrata(String idRubrique, String typeOrdre,
            BigDecimal montantDuPourOrdreCourant, BigDecimal montantTotalAVentiler) {
        BigDecimal totalSoustrait = new BigDecimal("0");
        BigDecimal montantADivide;

        if (getListePostes(idRubrique) != null) {
            for (CAVPPoste poste : getListePostes(idRubrique)) {
                montantADivide = poste.getDetailMontant(typeOrdre).getMontantBase();

                if (montantADivide.compareTo(new BigDecimal("0")) <= 0) {
                    continue;
                }

                BigDecimal taux = montantADivide.divide(montantDuPourOrdreCourant, 10, BigDecimal.ROUND_HALF_UP);
                BigDecimal montantASoustraire = JANumberFormatter.round(montantTotalAVentiler.multiply(taux), 0.05, 2,
                        JANumberFormatter.NEAR);
                poste.getDetailMontant(typeOrdre).addMontantVentile(montantASoustraire);
                updateMontantRestantAVentiler(poste.getIdSection(), montantASoustraire);

                totalSoustrait = totalSoustrait.add(montantASoustraire);
                // Si le montant à répartir est entre -MARGE_MONTANT_A_REPARTIR
                // et +MARGE_MONTANT_A_REPARTIR => on l'affecte à la dernière
                // rubrique touchée de manière à avoir un solde de 0
                BigDecimal montantAComparer = new BigDecimal(CAVPListePosteParRubrique.MARGE_MONTANT_A_REPARTIR);
                if (getMontantTotalAVentiler().compareTo(montantAComparer) <= 0) {
                    poste.getDetailMontant(typeOrdre).addMontantVentile(getMontantTotalAVentiler());
                    updateMontantRestantAVentiler(poste.getIdSection(), getMontantTotalAVentiler());
                    totalSoustrait = totalSoustrait.add(getMontantTotalAVentiler());
                }
            }
        }
        return totalSoustrait;
    }
}
