package globaz.helios.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Class permettant de gérer le manager du bilan pour imprimer à les écrans les informations correctement formatées.
 * 
 * @author dda
 */
public class CGBilanParser {
    public static final String LABEL_PARSER_RESULTAT = "PARSER_RESULTAT";
    private static final String LABEL_PARSER_TOTAUX = "PARSER_TOTAUX";

    /**
     * Ajout d'une ligne contenant les informations du compte, montant passif et actif, nature, monnaie étrangère,
     * libellé.
     * 
     * @param result
     * @param entity
     * @param soldeActif
     * @param soldePassif
     * @param soldeActifMonnaie
     * @param soldePassifMonnaie
     * @return
     */
    private static void addEntityLine(TreeMap result, CGPlanComptableViewBean entity, String soldeActif,
            String soldePassif, String soldeActifMonnaie, String soldePassifMonnaie) {
        CGBilanLine bilanLine = new CGBilanLine();
        bilanLine.setIdCompte(entity.getIdCompte());
        bilanLine.setIdExterne(entity.getIdExterne());

        bilanLine.setIdNature(entity.getIdNature());
        bilanLine.setIdDomaine(entity.getIdDomaine());

        if (CGCompte.CS_GENRE_ACTIF.equals(entity.getIdGenre())
                || CGCompte.CS_GENRE_OUVERTURE.equals(entity.getIdGenre())) {
            bilanLine.setSoldeActif(JANumberFormatter.fmt(soldeActif, true, true, true, 2));

            if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                bilanLine.setCodeISOMonnaie(entity.getCodeISOMonnaie());
                bilanLine.setSoldeMonnaie(JANumberFormatter.fmt(soldeActifMonnaie, true, true, true, 2));
            }
        } else if (CGCompte.CS_GENRE_PASSIF.equals(entity.getIdGenre())
                || CGCompte.CS_GENRE_CLOTURE.equals(entity.getIdGenre())) {
            bilanLine.setSoldePassif(JANumberFormatter.fmt(soldePassif, true, true, true, 2));

            if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                bilanLine.setCodeISOMonnaie(entity.getCodeISOMonnaie());
                bilanLine.setSoldeMonnaie(JANumberFormatter.fmt(soldePassifMonnaie, true, true, true, 2));
            }
        }

        bilanLine.setLibelle(entity.getLibelle());
        bilanLine.setGenreLibelle(entity.getGenreLibelle());
        bilanLine.setIdGenre(entity.getIdGenre());

        result.put(entity.getIdExterne(), bilanLine);
    }

    /**
     * Ajout d'une ligne contenant les informations du compte ofas, montant passif et actif ainsi que le genre de compte
     * (le genre de compte ne dépendant pas du compte ofas => le genre de compte sera affiché en fonction de la dernière
     * entié du compte ofas utilisé).
     * 
     * @param result
     * @param entity
     * @param soldeActif
     * @param soldePassif
     * @param soldeActifMonnaie
     * @param soldePassifMonnaie
     * @param idCompteOfas
     * @return
     */
    private static void addEntityLineOfas(TreeMap result, CGPlanComptableViewBean entity, String soldeActif,
            String soldePassif, String soldeActifMonnaie, String soldePassifMonnaie, String idCompteOfas) {
        CGCompteOfas compteOfas = new CGCompteOfas();
        compteOfas.setSession(entity.getSession());

        compteOfas.setIdCompteOfas(idCompteOfas);

        try {
            compteOfas.retrieve();
        } catch (Exception e) {
            // do nothing
        }

        CGBilanLine bilanLine = new CGBilanLine();
        bilanLine.setIdExterne(compteOfas.getIdExterne());

        if (CGCompte.CS_GENRE_ACTIF.equals(entity.getIdGenre())
                || CGCompte.CS_GENRE_OUVERTURE.equals(entity.getIdGenre())) {
            bilanLine.setSoldeActif(JANumberFormatter.fmt(soldeActif, true, true, true, 2));
        } else if (CGCompte.CS_GENRE_PASSIF.equals(entity.getIdGenre())
                || CGCompte.CS_GENRE_CLOTURE.equals(entity.getIdGenre())) {
            bilanLine.setSoldePassif(JANumberFormatter.fmt(soldePassif, true, true, true, 2));
        }

        bilanLine.setLibelle(compteOfas.getLibelle());
        bilanLine.setGenreLibelle(entity.getGenreLibelle());
        bilanLine.setIdGenre(compteOfas.getIdGenre());

        bilanLine.setIdDomaine(compteOfas.getIdDomaine());
        bilanLine.setIdNature(compteOfas.getIdNature());

        if (result.containsKey(compteOfas.getIdExterne())) {
            CGBilanParser.addValueToResult(result, compteOfas.getIdExterne(), bilanLine);
        } else {
            result.put(compteOfas.getIdExterne(), bilanLine);
        }
    }

    /**
     * Enlève la clef dans les résultats, somme les nouveaux montants et ajoute cette nouvelle clef de fin de liste. <br/>
     * Si total actif est égal au total passif la clef ne sera pas réajouté.
     * 
     * @param result
     * @param key
     * @param bilanLine
     * @param alwaysShow
     */
    private static void addFinalValueToResult(TreeMap result, String key, CGBilanLine bilanLine, boolean alwaysShow) {
        CGBilanLine tmp = (CGBilanLine) result.remove(key);

        FWCurrency tmpActif = new FWCurrency(tmp.getSoldeActif());
        FWCurrency tmpPassif = new FWCurrency(tmp.getSoldePassif());
        tmpActif.add(bilanLine.getSoldeActif());
        tmpPassif.add(bilanLine.getSoldePassif());

        if (!tmpActif.isZero()) {
            tmp.setSoldeActif(JANumberFormatter.fmt(tmpActif.toString(), true, true, true, 2));
        }

        if (!tmpPassif.isZero()) {
            tmp.setSoldePassif(JANumberFormatter.fmt(tmpPassif.toString(), true, true, true, 2));
        }

        if (!alwaysShow && !tmpActif.isZero() && !tmpPassif.isZero()) {
            if (tmpActif.compareTo(tmpPassif) == 1) {
                tmpActif.sub(tmpPassif);
                tmpPassif = new FWCurrency();
                tmp.setSoldeActif(JANumberFormatter.fmt(tmpActif.toString(), true, true, true, 2));
                tmp.setSoldePassif("");
            } else {
                tmpPassif.sub(tmpActif);
                tmpActif = new FWCurrency();
                tmp.setSoldeActif("");
                tmp.setSoldePassif(JANumberFormatter.fmt(tmpPassif.toString(), true, true, true, 2));
            }
        }

        if (alwaysShow || (tmpActif.compareTo(tmpPassif) != 0)) {
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
    private static void addTotauxLine(BSession session, TreeMap result, FWCurrency totalCharges,
            FWCurrency totalProduits, String secteurAVS, CGBilanListViewBean listViewBean) {

        CGBilanLine bilanLine = new CGBilanLine();

        bilanLine.setLibelle(session.getLabel(CGBilanParser.LABEL_PARSER_TOTAUX));
        bilanLine.setSoldeActif(JANumberFormatter.fmt(totalCharges.toString(), true, true, false, 2));
        bilanLine.setSoldePassif(JANumberFormatter.fmt(totalProduits.toString(), true, true, false, 2));

        String key;
        if (listViewBean.isGroupIdCompteOfas()) {
            key = secteurAVS.substring(0, 3) + ".9997";
        } else {
            key = secteurAVS + "9999.99997";
        }

        if (listViewBean.isGroupIdCompteOfas() && result.containsKey(key)) {
            CGBilanParser.addFinalValueToResult(result, key, bilanLine, true);
        } else {
            result.put(key, bilanLine);
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

            bilanLine = new CGBilanLine();

            bilanLine.setLibelle(session.getLabel(CGBilanParser.LABEL_PARSER_RESULTAT));

            bilanLine.setSoldeActif(benefice);
            bilanLine.setSoldePassif(perte);

            if (listViewBean.isGroupIdCompteOfas()) {
                key = secteurAVS.substring(0, 3) + ".9998";
            } else {
                key = secteurAVS + "9999.99998";
            }

            if (listViewBean.isGroupIdCompteOfas() && result.containsKey(key)) {
                CGBilanParser.addFinalValueToResult(result, key, bilanLine, false);
            } else {
                result.put(key, bilanLine);
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
     * @param bilanLine
     */
    private static void addValueToResult(TreeMap result, String key, CGBilanLine bilanLine) {
        CGBilanLine tmp = (CGBilanLine) result.get(key);

        FWCurrency tmpActif = new FWCurrency(tmp.getSoldeActif());
        FWCurrency tmpPassif = new FWCurrency(tmp.getSoldePassif());
        tmpActif.add(bilanLine.getSoldeActif());
        tmpPassif.add(bilanLine.getSoldePassif());

        if (!tmpActif.isZero()) {
            tmp.setSoldeActif(JANumberFormatter.fmt(tmpActif.toString(), true, true, true, 2));
        }

        if (!tmpPassif.isZero()) {
            tmp.setSoldePassif(JANumberFormatter.fmt(tmpPassif.toString(), true, true, true, 2));
        }
    }

    /**
     * Convert hashmap to arraylist for parsing.
     * 
     * @param result
     * @return
     */
    private static ArrayList convert(TreeMap result) {
        Iterator iter = result.keySet().iterator();
        ArrayList resultForParsing = new ArrayList();
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
    private static String getIdCurrentIdCompte(CGBilanListViewBean listViewBean, CGPlanComptableViewBean entity) {
        if (listViewBean.isGroupIdCompteOfas()) {
            return entity.getIdCompteOfas();
        } else {
            return entity.getIdCompte();
        }
    }

    /**
     * Return une ArrayList contenant la liste des comptes a vérifier pour le bouclement.
     * 
     * @param listViewBean
     * @param exerciceComptable
     * @return
     */
    public static ArrayList getLinesToBouclement(CGBilanListViewBean listViewBean, CGExerciceComptable exerciceComptable) {
        TreeMap result = new TreeMap();

        String secteurAVS = "";

        // Les variables suivantes sont définies en dehors de la boucle fort car
        // elles sont sommés lors du parcours des entities
        FWCurrency totalCharges = new FWCurrency();
        FWCurrency totalProduits = new FWCurrency();
        FWCurrency soldeActif = new FWCurrency();
        FWCurrency soldePassif = new FWCurrency();
        FWCurrency soldeActifMonnaie = new FWCurrency();
        FWCurrency soldePassifMonnaie = new FWCurrency();

        String oldIdCompte = new String();

        for (int i = 0; i < listViewBean.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) listViewBean.getEntity(i);

            CGPlanComptableViewBean nextEntity = null;
            if ((i + 1) < listViewBean.size()) {
                nextEntity = (CGPlanComptableViewBean) listViewBean.getEntity(i + 1);
            }

            if (entity != null) {

                String idCompte = CGBilanParser.getIdCurrentIdCompte(listViewBean, entity);

                if (!idCompte.equals(oldIdCompte)) {
                    oldIdCompte = idCompte;

                    soldeActif = CGBilanParser.getSoldeActif(listViewBean, entity);
                    soldePassif = CGBilanParser.getSoldePassif(listViewBean, entity);

                    soldeActifMonnaie = CGBilanParser.getSoldeMonnaieActif(listViewBean, entity);
                    soldePassifMonnaie = CGBilanParser.getSoldeMonnaiePassif(listViewBean, entity);
                } else {
                    // Ajout des montants.
                    soldeActif.add(CGBilanParser.getSoldeActif(listViewBean, entity));
                    soldePassif.add(CGBilanParser.getSoldePassif(listViewBean, entity));

                    soldeActifMonnaie.add(CGBilanParser.getSoldeMonnaieActif(listViewBean, entity));
                    soldePassifMonnaie.add(CGBilanParser.getSoldeMonnaiePassif(listViewBean, entity));
                }

                // Impression uniquement des soldes != 0
                if (CGBilanParser.isNextEntityIdCompteNotEquals(listViewBean, entity, nextEntity)
                        && (!JadeStringUtil.isDecimalEmpty(soldeActif.toString()) || !JadeStringUtil
                                .isDecimalEmpty(soldePassif.toString()))) {

                    if (CGCompte.CS_GENRE_ACTIF.equals(entity.getIdGenre())
                            || CGCompte.CS_GENRE_OUVERTURE.equals(entity.getIdGenre())) {
                        totalCharges.add(soldeActif);
                    } else if (CGCompte.CS_GENRE_PASSIF.equals(entity.getIdGenre())
                            || CGCompte.CS_GENRE_CLOTURE.equals(entity.getIdGenre())) {
                        totalProduits.add(soldePassif);
                    }

                    CGBilanParser.addEntityLine(result, entity, soldeActif.toString(), soldePassif.toString(),
                            soldeActifMonnaie.toString(), soldePassifMonnaie.toString());
                }
            }
        }

        return CGBilanParser.convert(result);
    }

    /**
     * Return une ArrayList contenant le bilan à imprimer à l'écran.
     * 
     * @param listViewBean
     * @param exerciceComptable
     * @return
     */
    public static ArrayList getLinesToPrint(CGBilanListViewBean listViewBean,
            CGExerciceComptableViewBean exerciceComptable) {
        return CGBilanParser.getLinesToPrint(listViewBean, exerciceComptable, false);
    }

    /**
     * Return une ArrayList contenant le bilan à imprimer dans l'impression du compte annuel.
     * 
     * @param listViewBean
     * @param exerciceComptable
     * @param isCompteAnnuel
     *            --> supprime les montants négatifs (par ex. un montant actif négatif est changé en montant passif
     *            positif)
     * @return
     */
    public static ArrayList getLinesToPrint(CGBilanListViewBean listViewBean,
            CGExerciceComptableViewBean exerciceComptable, boolean isCompteAnnuel) {
        TreeMap result = new TreeMap();

        String secteurAVS = "";

        // Les variables suivantes sont définies en dehors de la boucle fort car
        // elles sont sommés lors du parcours des entities
        FWCurrency totalCharges = new FWCurrency();
        FWCurrency totalProduits = new FWCurrency();
        FWCurrency soldeActif = new FWCurrency();
        FWCurrency soldePassif = new FWCurrency();
        FWCurrency soldeActifMonnaie = new FWCurrency();
        FWCurrency soldePassifMonnaie = new FWCurrency();

        String oldIdCompte = new String();

        for (int i = 0; i < listViewBean.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) listViewBean.getEntity(i);

            CGPlanComptableViewBean nextEntity = null;
            if ((i + 1) < listViewBean.size()) {
                nextEntity = (CGPlanComptableViewBean) listViewBean.getEntity(i + 1);
            }

            if (entity != null) {

                String idCompte = CGBilanParser.getIdCurrentIdCompte(listViewBean, entity);

                if (!idCompte.equals(oldIdCompte)) {
                    oldIdCompte = idCompte;

                    soldeActif = CGBilanParser.getSoldeActif(listViewBean, entity);
                    soldePassif = CGBilanParser.getSoldePassif(listViewBean, entity);

                    soldeActifMonnaie = CGBilanParser.getSoldeMonnaieActif(listViewBean, entity);
                    soldePassifMonnaie = CGBilanParser.getSoldeMonnaiePassif(listViewBean, entity);
                } else {
                    // Ajout des montants.
                    soldeActif.add(CGBilanParser.getSoldeActif(listViewBean, entity));
                    soldePassif.add(CGBilanParser.getSoldePassif(listViewBean, entity));

                    soldeActifMonnaie.add(CGBilanParser.getSoldeMonnaieActif(listViewBean, entity));
                    soldePassifMonnaie.add(CGBilanParser.getSoldeMonnaiePassif(listViewBean, entity));
                }

                // Impression uniquement des soldes != 0
                if (CGBilanParser.isNextEntityIdCompteNotEquals(listViewBean, entity, nextEntity)
                        && (!JadeStringUtil.isDecimalEmpty(soldeActif.toString()) || !JadeStringUtil
                                .isDecimalEmpty(soldePassif.toString()))) {
                    if (secteurAVS.equals("")) {
                        secteurAVS = entity.getIdSecteurAVS();
                    }

                    if (exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {
                        if (!entity.getIdSecteurAVS().equals(secteurAVS)) {
                            CGBilanParser.addTotauxLine(listViewBean.getSession(), result, totalCharges, totalProduits,
                                    secteurAVS, listViewBean);

                            secteurAVS = entity.getIdSecteurAVS();

                            // Remise à zéro des totaux. Dans une comptabilité
                            // AVS les totaux sont affichés en fonction des
                            // secteurs et non pas une foi en fin de bilan
                            // uniquement
                            totalCharges = new FWCurrency();
                            totalProduits = new FWCurrency();
                        }
                    }

                    if (CGCompte.CS_GENRE_ACTIF.equals(entity.getIdGenre())
                            || CGCompte.CS_GENRE_OUVERTURE.equals(entity.getIdGenre())) {
                        if (isCompteAnnuel) {
                            if (soldeActif.isPositive()) {
                                totalCharges.add(soldeActif);
                            } else {
                                FWCurrency soldeActifAbs = new FWCurrency(soldeActif.toString());
                                soldeActifAbs.abs();
                                totalProduits.add(soldeActifAbs);
                            }
                        } else {
                            totalCharges.add(soldeActif);
                        }
                    } else if (CGCompte.CS_GENRE_PASSIF.equals(entity.getIdGenre())
                            || CGCompte.CS_GENRE_CLOTURE.equals(entity.getIdGenre())) {
                        if (isCompteAnnuel) {
                            if (soldePassif.isPositive()) {
                                totalProduits.add(soldePassif);
                            } else {
                                FWCurrency soldePassifAbs = new FWCurrency(soldePassif.toString());
                                soldePassifAbs.abs();
                                totalCharges.add(soldePassifAbs);
                            }

                        } else {
                            totalProduits.add(soldePassif);
                        }
                    }

                    if (listViewBean.isGroupIdCompteOfas()) {
                        CGBilanParser.addEntityLineOfas(result, entity, soldeActif.toString(), soldePassif.toString(),
                                soldeActifMonnaie.toString(), soldePassifMonnaie.toString(), entity.getIdCompteOfas());
                    } else {
                        CGBilanParser.addEntityLine(result, entity, soldeActif.toString(), soldePassif.toString(),
                                soldeActifMonnaie.toString(), soldePassifMonnaie.toString());
                    }
                }
            }
        }

        // Ajout du total final (pour compta non AVS) ou total attrayant au
        // dernier secteur (compta AVS)
        if (listViewBean.size() != 0) {
            CGBilanParser.addTotauxLine(listViewBean.getSession(), result, totalCharges, totalProduits, secteurAVS,
                    listViewBean);
        }

        return CGBilanParser.convert(result);
    }

    /**
     * Retourne le solde actif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    public static FWCurrency getSoldeActif(CGBilanListViewBean listViewBean, CGPlanComptableViewBean entity) {
        if (CodeSystem.CS_DEFINITIF.equals(listViewBean.getReqComptabilite())) {
            return new FWCurrency(entity.getSolde());
        } else {
            return new FWCurrency(entity.getSoldeProvisoire());
        }
    }

    /**
     * Retourne le solde monnaie actif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou
     * PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    private static FWCurrency getSoldeMonnaieActif(CGBilanListViewBean listViewBean, CGPlanComptableViewBean entity) {
        if (CodeSystem.CS_DEFINITIF.equals(listViewBean.getReqComptabilite())) {
            return new FWCurrency(entity.getSoldeMonnaie());
        } else {
            return new FWCurrency(entity.getSoldeProvisoireMonnaie());
        }
    }

    /**
     * Retourne le solde monnaie passif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou
     * PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    private static FWCurrency getSoldeMonnaiePassif(CGBilanListViewBean listViewBean, CGPlanComptableViewBean entity) {
        if (CodeSystem.CS_DEFINITIF.equals(listViewBean.getReqComptabilite())) {
            FWCurrency result = new FWCurrency(entity.getSoldeMonnaie());
            result.negate();
            return result;
        } else {
            FWCurrency result = new FWCurrency(entity.getSoldeProvisoireMonnaie());
            result.negate();
            return result;
        }
    }

    /**
     * Retourne le solde passif (dépendant du mode d'impression de bilan, respectivement DEFINITIF ou PROVISOIRE).
     * 
     * @param listViewBean
     * @param entity
     * @return
     */
    public static FWCurrency getSoldePassif(CGBilanListViewBean listViewBean, CGPlanComptableViewBean entity) {
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
    private static boolean isNextEntityIdCompteNotEquals(CGBilanListViewBean listViewBean,
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
