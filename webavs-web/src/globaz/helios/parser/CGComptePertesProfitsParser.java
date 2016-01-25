package globaz.helios.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGComptePertesProfitsListViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Class permettant de gérer le manager de la section "compte pertes et profits" pour imprimer à les écrans les
 * informations correctement formatées.
 * 
 * @author dda
 * 
 */
public class CGComptePertesProfitsParser {
    private static final String LABEL_PARSER_RESULTAT = "PARSER_RESULTAT";
    private static final String LABEL_PARSER_TOTAUX = "PARSER_TOTAUX";

    /**
     * Ajout d'une ligne contenant les informations du compte, montant charges et produits, libellé.
     * 
     * @param result
     * @param entity
     * @param soldeCharges
     * @param soldeProduites
     * @return
     */
    private static void addEntityLine(TreeMap<String, CGComptePertesProfitsLine> result,
            CGPlanComptableViewBean entity, String soldeCharges, String soldeProduites) {
        CGComptePertesProfitsLine line = new CGComptePertesProfitsLine();
        line.setIdCompte(entity.getIdCompte());
        line.setIdExterne(entity.getIdExterne());

        line.setIdNature(entity.getIdNature());
        line.setIdDomaine(entity.getIdDomaine());

        if (new FWCurrency(soldeCharges).isPositive()) {
            line.setSoldeCharges(JANumberFormatter.fmt(soldeCharges, true, true, true, 2));
        } else {
            line.setSoldeProduits(JANumberFormatter.fmt(soldeProduites, true, true, true, 2));
        }

        line.setLibelle(entity.getLibelle());
        line.setGenreLibelle(entity.getGenreLibelle());
        line.setIdGenre(entity.getIdGenre());

        result.put(entity.getIdExterne(), line);
    }

    /**
     * Ajout d'une ligne contenant les informations du compte ofas, montant charges et produits ainsi que le genre de
     * compte (le genre de compte ne dépendant pas du compte ofas => le genre de compte sera affiché en fonction de la
     * dernière entié du compte ofas utilisé).
     * 
     * @param result
     * @param entity
     * @param soldeCharges
     * @param soldeProduits
     * @param idCompteOfas
     * @return
     */
    private static void addEntityLineOfas(TreeMap<String, CGComptePertesProfitsLine> result,
            CGPlanComptableViewBean entity, String soldeCharges, String soldeProduits, String idCompteOfas) {
        CGCompteOfas compteOfas = new CGCompteOfas();
        compteOfas.setSession(entity.getSession());

        compteOfas.setIdCompteOfas(idCompteOfas);

        try {
            compteOfas.retrieve();
        } catch (Exception e) {
            // do nothing
        }

        CGComptePertesProfitsLine line = new CGComptePertesProfitsLine();
        line.setIdExterne(compteOfas.getIdExterne());

        if (CGCompte.CS_GENRE_CHARGE.equals(entity.getIdGenre())) {
            line.setSoldeCharges(JANumberFormatter.fmt(soldeCharges, true, true, true, 2));
        } else if (CGCompte.CS_GENRE_PRODUIT.equals(entity.getIdGenre())) {
            line.setSoldeProduits(JANumberFormatter.fmt(soldeProduits, true, true, true, 2));
        } else if (CGCompte.CS_GENRE_RESULTAT.equals(entity.getIdGenre()) && new FWCurrency(soldeCharges).isPositive()) {
            line.setSoldeCharges(JANumberFormatter.fmt(soldeCharges, true, true, true, 2));
        } else if (CGCompte.CS_GENRE_RESULTAT.equals(entity.getIdGenre()) && new FWCurrency(soldeProduits).isPositive()) {
            line.setSoldeProduits(JANumberFormatter.fmt(soldeProduits, true, true, true, 2));
        }

        line.setLibelle(compteOfas.getLibelle());
        line.setGenreLibelle(entity.getGenreLibelle());
        line.setIdGenre(compteOfas.getIdGenre());

        line.setIdDomaine(compteOfas.getIdDomaine());
        line.setIdNature(compteOfas.getIdNature());

        if (result.containsKey(compteOfas.getIdExterne())) {
            addValueToResult(result, compteOfas.getIdExterne(), line);
        } else {
            result.put(compteOfas.getIdExterne(), line);
        }
    }

    /**
     * Enlève la clef dans les résultats, somme les nouveaux montants et ajoute cette nouvelle clef de fin de liste. <br/>
     * Si total actif est égal au total passif la clef ne sera pas réajouté.
     * 
     * @param result
     * @param key
     * @param line
     * @param alwaysShow
     */
    private static void addFinalValueToResult(TreeMap<String, CGComptePertesProfitsLine> result, String key,
            CGComptePertesProfitsLine line, boolean alwaysShow) {
        CGComptePertesProfitsLine tmp = result.remove(key);

        FWCurrency tmpCharges = new FWCurrency(tmp.getSoldeCharges());
        FWCurrency tmpProduits = new FWCurrency(tmp.getSoldeProduits());
        tmpCharges.add(line.getSoldeCharges());
        tmpProduits.add(line.getSoldeProduits());

        if (!tmpCharges.isZero()) {
            tmp.setSoldeCharges(JANumberFormatter.fmt(tmpCharges.toString(), true, true, true, 2));
        }

        if (!tmpProduits.isZero()) {
            tmp.setSoldeProduits(JANumberFormatter.fmt(tmpProduits.toString(), true, true, true, 2));
        }

        if (!alwaysShow && !tmpCharges.isZero() && !tmpProduits.isZero()) {
            if (tmpCharges.compareTo(tmpProduits) == 1) {
                tmpCharges.sub(tmpProduits);
                tmpProduits = new FWCurrency();
                tmp.setSoldeCharges(JANumberFormatter.fmt(tmpCharges.toString(), true, true, true, 2));
                tmp.setSoldeProduits("");
            } else {
                tmpProduits.sub(tmpCharges);
                tmpCharges = new FWCurrency();
                tmp.setSoldeCharges("");
                tmp.setSoldeProduits(JANumberFormatter.fmt(tmpProduits.toString(), true, true, true, 2));
            }
        }

        if (alwaysShow || tmpCharges.compareTo(tmpProduits) != 0) {
            result.put(key, tmp);
        }
    }

    /**
     * Ajout des informations "Totaux" et si nécessaire "Bénéfice" ou "Perte". <br/>
     * De plus un objet null est ajouté à l'ArrayList de résultat pour forcer une ligne vide à l'écran.
     * 
     * @param session
     * @param result
     * @param totalCharges
     * @param totalProduits
     * @param secteurAVS
     * @param listViewBean
     */
    private static void addTotauxLine(BSession session, TreeMap<String, CGComptePertesProfitsLine> result,
            FWCurrency totalCharges, FWCurrency totalProduits, String secteurAVS,
            CGComptePertesProfitsListViewBean listViewBean) {
        CGComptePertesProfitsLine line = new CGComptePertesProfitsLine();

        line.setLibelle(session.getLabel(LABEL_PARSER_TOTAUX));
        line.setSoldeCharges(JANumberFormatter.fmt(totalCharges.toString(), true, true, false, 2));
        line.setSoldeProduits(JANumberFormatter.fmt(totalProduits.toString(), true, true, false, 2));

        String key;
        if (listViewBean.isGroupIdCompteOfas()) {
            key = secteurAVS.substring(0, 3) + ".9997";
        } else {
            key = secteurAVS + "9999.99997";
        }

        if (listViewBean.isGroupIdCompteOfas() && result.containsKey(key)) {
            addFinalValueToResult(result, key, line, true);
        } else {
            result.put(key, line);
        }

        if (!(totalCharges.compareTo(totalProduits) == 0)) {
            FWCurrency temp = new FWCurrency(totalProduits.toString());
            temp.sub(totalCharges);
            String perte = "";
            String benefice = "";
            if (totalCharges.compareTo(totalProduits) == 1) {
                temp.negate();
                perte = JANumberFormatter.fmt(temp.toString(), true, true, false, 2);
            } else {
                benefice = JANumberFormatter.fmt(temp.toString(), true, true, false, 2);
            }

            line = new CGComptePertesProfitsLine();

            line.setLibelle(session.getLabel(LABEL_PARSER_RESULTAT));

            line.setSoldeCharges(perte);
            line.setSoldeProduits(benefice);

            if (listViewBean.isGroupIdCompteOfas()) {
                key = secteurAVS.substring(0, 3) + ".9998";
            } else {
                key = secteurAVS + "9999.99998";
            }

            if (listViewBean.isGroupIdCompteOfas() && result.containsKey(key)) {
                addFinalValueToResult(result, key, line, false);
            } else {
                result.put(key, line);
            }
        }

        if (listViewBean.isGroupIdCompteOfas()) {
            key = secteurAVS.substring(0, 3) + ".9999";
        } else {
            key = secteurAVS + "9999.99999";
        }

        // Needed for space at screen !
        if (listViewBean.isGroupIdCompteOfas() && result.containsKey(key)) {
            result.remove(key);
            result.put(key, null);
        } else {
            result.put(key, null);
        }
    }

    /**
     * Recherche la clef dans les résultats et somme les nouveaux montants.
     * 
     * @param result
     * @param key
     * @param compteOfas
     * @param line
     */
    private static void addValueToResult(TreeMap<String, CGComptePertesProfitsLine> result, String key,
            CGComptePertesProfitsLine line) {
        CGComptePertesProfitsLine tmp = result.get(key);

        FWCurrency tmpCharges = new FWCurrency(tmp.getSoldeCharges());
        FWCurrency tmpProduits = new FWCurrency(tmp.getSoldeProduits());
        tmpCharges.add(line.getSoldeCharges());
        tmpProduits.add(line.getSoldeProduits());

        if (!tmpCharges.isZero()) {
            tmp.setSoldeCharges(JANumberFormatter.fmt(tmpCharges.toString(), true, true, true, 2));
        }

        if (!tmpProduits.isZero()) {
            tmp.setSoldeProduits(JANumberFormatter.fmt(tmpProduits.toString(), true, true, true, 2));
        }
    }

    /**
     * Convert hashmap to arraylist for parsing.
     * 
     * @param result
     * @return
     */
    private static ArrayList<CGComptePertesProfitsLine> convert(TreeMap<String, CGComptePertesProfitsLine> result) {
        Iterator<String> iter = result.keySet().iterator();
        ArrayList<CGComptePertesProfitsLine> resultForParsing = new ArrayList<CGComptePertesProfitsLine>();
        while (iter.hasNext()) {
            resultForParsing.add(result.get(iter.next()));
        }
        return resultForParsing;
    }

    /**
     * Return l'id compte courante. <br/>
     * Si l'option "group by ofas" est sélectionée l'id compte ofas sera retournée.
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    private static String getIdCurrentIdCompte(CGComptePertesProfitsListViewBean listViewBean,
            CGPlanComptableViewBean entity) {
        if (listViewBean.isGroupIdCompteOfas()) {
            return entity.getIdCompteOfas();
        } else {
            return entity.getIdCompte();
        }
    }

    /**
     * Return une ArrayList contenant les comptes pertes et profits à imprimer à l'écran.
     * 
     * @param listViewBean
     * @param exerciceComptable
     * @return
     */
    public static ArrayList<?> getLinesToPrint(CGComptePertesProfitsListViewBean listViewBean,
            CGExerciceComptableViewBean exerciceComptable) {
        TreeMap<String, CGComptePertesProfitsLine> result = new TreeMap<String, CGComptePertesProfitsLine>();

        String secteurAVS = "";

        // Les variables suivantes sont définies en dehors de la boucle fort car
        // elles sont sommés lors du parcours des entities
        FWCurrency totalCharges = new FWCurrency();
        FWCurrency totalProduits = new FWCurrency();
        FWCurrency soldeCharges = new FWCurrency();
        FWCurrency soldeProduits = new FWCurrency();

        String oldIdCompte = new String();

        for (int i = 0; i < listViewBean.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) listViewBean.getEntity(i);

            CGPlanComptableViewBean nextEntity = null;
            if ((i + 1) < listViewBean.size()) {
                nextEntity = (CGPlanComptableViewBean) listViewBean.getEntity(i + 1);
            }

            if (entity != null) {
                String idCompte = getIdCurrentIdCompte(listViewBean, entity);

                if (!idCompte.equals(oldIdCompte)) {
                    oldIdCompte = idCompte;

                    soldeCharges = getSoldeActif(listViewBean, entity);
                    soldeProduits = getSoldePassif(listViewBean, entity);
                } else {
                    // Ajout des montants.
                    soldeCharges.add(getSoldeActif(listViewBean, entity));
                    soldeProduits.add(getSoldePassif(listViewBean, entity));
                }

                // Impression uniquement des soldes != 0
                if (isNextEntityIdCompteNotEquals(listViewBean, entity, nextEntity)
                        && (!JadeStringUtil.isDecimalEmpty(soldeCharges.toString()) || !JadeStringUtil
                                .isDecimalEmpty(soldeProduits.toString()))) {
                    if (secteurAVS.equals("")) {
                        secteurAVS = entity.getIdSecteurAVS();
                    }

                    if (exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {
                        if (!entity.getIdSecteurAVS().equals(secteurAVS)) {
                            addTotauxLine(listViewBean.getSession(), result, totalCharges, totalProduits, secteurAVS,
                                    listViewBean);

                            secteurAVS = entity.getIdSecteurAVS();

                            // Remise à zéro des totaux. Dans une comptabilité
                            // AVS les totaux sont affichés en fonction des
                            // secteurs et non pas une foi en fin de bilan
                            // uniquement
                            totalCharges = new FWCurrency();
                            totalProduits = new FWCurrency();
                        }
                    }

                    if (CGCompte.CS_GENRE_CHARGE.equals(entity.getIdGenre())) {
                        if (soldeCharges.isPositive()) {
                            totalCharges.add(soldeCharges);
                        } else {
                            totalProduits.add(soldeProduits);
                        }
                    } else if (CGCompte.CS_GENRE_PRODUIT.equals(entity.getIdGenre())) {
                        totalProduits.add(soldeProduits);
                    } else if (CGCompte.CS_GENRE_RESULTAT.equals(entity.getIdGenre()) && soldeCharges.isPositive()) {
                        totalCharges.add(soldeCharges);
                    } else if (CGCompte.CS_GENRE_RESULTAT.equals(entity.getIdGenre()) && soldeProduits.isPositive()) {
                        totalProduits.add(soldeProduits);
                    }

                    if (listViewBean.isGroupIdCompteOfas()) {
                        addEntityLineOfas(result, entity, soldeCharges.toString(), soldeProduits.toString(),
                                entity.getIdCompteOfas());
                    } else {
                        addEntityLine(result, entity, soldeCharges.toString(), soldeProduits.toString());
                    }
                }
            }
        }

        // Ajout du total final (pour compta non AVS) ou total attrayant au
        // dernier secteur (compta AVS)
        if (listViewBean.size() != 0) {
            addTotauxLine(listViewBean.getSession(), result, totalCharges, totalProduits, secteurAVS, listViewBean);
        }

        return convert(result);
    }

    /**
     * Retourne le solde actif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    public static FWCurrency getSoldeActif(CGComptePertesProfitsListViewBean listViewBean,
            CGPlanComptableViewBean entity) {
        if (CodeSystem.CS_DEFINITIF.equals(listViewBean.getReqComptabilite())) {
            return new FWCurrency(entity.getSolde());
        } else {
            return new FWCurrency(entity.getSoldeProvisoire());
        }
    }

    /**
     * Retourne le solde passif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    public static FWCurrency getSoldePassif(CGComptePertesProfitsListViewBean listViewBean,
            CGPlanComptableViewBean entity) {
        if (CodeSystem.CS_DEFINITIF.equals(listViewBean.getReqComptabilite())) {
            FWCurrency result = new FWCurrency(entity.getSolde());
            result.negate();
            return result;
        } else {
            FWCurrency result = new FWCurrency(entity.getSoldeProvisoire());
            result.negate();
            return result;
        }
    }

    /**
     * Est-ce que la prochaine entité à une id compte (ou id compte ofas si mode activée) différente de l'id compte
     * (respectivement id compte ofas) actuelle.
     * 
     * @param listViewBean
     * @param entity
     * @param nextEntity
     * @return
     */
    private static boolean isNextEntityIdCompteNotEquals(CGComptePertesProfitsListViewBean listViewBean,
            CGPlanComptableViewBean entity, CGPlanComptableViewBean nextEntity) {
        if (nextEntity == null) {
            return true;
        } else if ((listViewBean.isGroupIdCompteOfas())
                && (!entity.getIdCompteOfas().equals(nextEntity.getIdCompteOfas()))) {
            return true;
        } else if ((!listViewBean.isGroupIdCompteOfas()) && (!entity.getIdCompte().equals(nextEntity.getIdCompte()))) {
            return true;
        } else {
            return false;
        }
    }
}
