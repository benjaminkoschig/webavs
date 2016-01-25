package globaz.osiris.helpers.lettrage.rules;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.helpers.lettrage.PropositionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * - Rêgles de couverture des sections débitrices -
 */

/*
 * applique les rêgles pour déterminer l'ordre des propositions Il y a 3 cas de figures :
 * 
 * 1) La règle des 17 : Les section 17 : Les sections céditrice 17 ne sont utilisé que pour compenser des section 17
 * débitrice de même année (4 première pos du décompte). Si il y en a plusieurs, elles sont compensée de la plus récente
 * (date de section) à la plus ancienne.
 * 
 * 2) Les sections 13,14,18 Les section créditrice 13,14,18 doivent compenser en priorité les sections débitrice de même
 * année (4 première pos du décompte) de la plus récente à la plus ancienne (par date de section)
 * 
 * Les autres décomptes sont ensuite pris par année de décompte + cat (6 première pos du décompte), de l'année la plus
 * ancienne à l'année la plus récente. si plusieurs décompte existent pour une même année+ cat, ils sont triés par date
 * de section, du plus ancien au plus récent.
 * 
 * 3) Toutes les autres catégories. La rêgle est similaire à 2), al'exception des décompte de la même année qui sont
 * trié du plus ancien au plus récent.
 * 
 * 
 * Les 20 prennent soit la regle 2 soit la regle 3 suivant que l'affiliation soit radiée ou non
 * 
 * [oca] [refonte total en cours pour gestion "par année"]
 */

public class CARulesCouvertureSectionDebitrices {

    public final static String RULE_17 = "2";
    public final static String RULE_ANTE = "1";
    public final static String RULE_CHRONO = "3";
    public static Map<String, String> ruleDescriptionDE = new HashMap<String, String>();
    public static Map<String, String> ruleDescriptionFR = new HashMap<String, String>();
    public static Map<String, String> ruleDescriptionIT = new HashMap<String, String>();
    static {
        CARulesCouvertureSectionDebitrices.ruleDescriptionFR
                .put(CARulesCouvertureSectionDebitrices.RULE_ANTE,
                        "Sections créditrices de catégorie  13, 14, 18, et 20 pour les affiliés radiés<br><br>"
                                + "<ul>"
                                + "<li>Les sections débitrices échues de même année que la section créditrice et qui ne sont pas de catégorie 17, par ordre <b>antéchronologique</b>"
                                + "<li>Les sections débitrices échues de même année que la section créditrice et qui sont de catégorie 17, par ordre antéchronologique\n"
                                + "<li>Par année (chronologiquement), en sautant l'année déjà traitée : \n"
                                + "  <ul>"
                                + "	<li>Les sections débitrices échues qui ne sont pas de catégorie 17, par ordre chronologique dans l'année"
                                + "   <li>Les sections débitrices échues de catégorie 17, par ordre antéchronologique dans l'année"
                                + "	</ul>" + "</ul>");

        CARulesCouvertureSectionDebitrices.ruleDescriptionFR
                .put(CARulesCouvertureSectionDebitrices.RULE_17,
                        "Sections créditrices de catégorie 17<br><br>"
                                + "<ul><li>Seules les sections débitrices échues de catégorie 17 de même année que la section créditrice sont couvertes de manière antéchronologique</ul>");

        CARulesCouvertureSectionDebitrices.ruleDescriptionFR
                .put(CARulesCouvertureSectionDebitrices.RULE_CHRONO,

                        "Autres sections créditrices (règle par défaut)<br><br>"
                                + "<ul>"
                                + "<li>Les sections débitrices échues de même année que la section créditrice et qui ne sont pas de catégorie 17, par ordre <b>chronologique</b>"
                                + "<li>Les sections débitrices échues de même année que la section créditrice et qui sont de catégorie 17, par ordre antéchronologique\n"
                                + "<li>Par année (chronologiquement), en sautant l'année déjà traitée : \n"
                                + "  <ul>"
                                + "	<li>Les sections débitrices échues qui ne sont pas de catégorie 17, par ordre chronologique dans l'année"
                                + "   <li>Les sections débitrices échues de catégorie 17, par ordre antéchronologique dans l'année"
                                + "	</ul>" + "</ul>");

        CARulesCouvertureSectionDebitrices.ruleDescriptionDE = CARulesCouvertureSectionDebitrices.ruleDescriptionFR; // TODO
        CARulesCouvertureSectionDebitrices.ruleDescriptionIT = CARulesCouvertureSectionDebitrices.ruleDescriptionFR; // TODO
    }

    /*
     * Regle 1 Cette rêgle ne couvre que les décomptes 17 débiteurs de la même année que la section créditrice.
     */
    private static Integer[] _buildPriority17(Map<String, String> sectionCreditrice,
            List<Map<String, String>> sectionsDebitrices) {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        final String anneeAcouvrir = (sectionCreditrice.get("IDEXTERNE")).substring(0, 4);
        List<Map<String, String>> section17Debtrices = JadeListUtil.filter(sectionsDebitrices,
                new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {
                        String anneeSectionDebitrice = m.get("IDEXTERNE").substring(0, 4);
                        return (anneeAcouvrir.equals(anneeSectionDebitrice) && (17 == Integer.parseInt(m.get(
                                "IDEXTERNE").substring(4, 6))));
                    }
                });
        Collections.reverse(section17Debtrices);
        res.addAll(section17Debtrices);
        return CARulesCouvertureSectionDebitrices._priorityBuildPriorityArray(res, sectionsDebitrices);
    }

    /*
     * Regle 3
     */
    private static Integer[] _buildPriorityAnte(Map<String, String> sectionCreditrice,
            List<Map<String, String>> sectionsDebitrices) {
        return CARulesCouvertureSectionDebitrices._buildPriorityAnteOrChrono(sectionCreditrice, sectionsDebitrices,
                true);
    }

    /*
     * ============================================================================== qq méthodes pratiques
     * ==============================================================================
     */
    private static Integer[] _buildPriorityAnteOrChrono(Map<String, String> sectionCreditrice,
            List<Map<String, String>> sectionsDebitrices, boolean isAnte) {
        final String SECTION_17 = "17";
        final String SECTION_NON_17 = "NON_17";

        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        final String anneeSectionCreditrice = sectionCreditrice.get("IDEXTERNE").substring(0, 4);

        // split par année, l'année correspondant à l'année de la section céditrice en premier
        List<Map<String, String>>[] sectionsDebitricesByYear = CARulesCouvertureSectionDebitrices
                ._prioritySplitByAnnee(anneeSectionCreditrice,
                        CARulesCouvertureSectionDebitrices._prioritySortByYearAndDateSection(sectionsDebitrices));

        // traite les sections débitrices, par années
        for (int i = 0; i < sectionsDebitricesByYear.length; i++) {
            List<Map<String, String>> yearList = sectionsDebitricesByYear[i];

            /*
             * Separt les section 17 de cette année des autres sections car elles seront traitées séparement
             * sectionsForThisYear['17':List[...], 'NON_17':List[...]]
             */
            Map<String, List<Map<String, String>>> sectionsForThisYear = JadeListUtil.groupBy(yearList,
                    new JadeListUtil.Key<Map<String, String>>() {
                        @Override
                        public String exec(Map<String, String> m) {

                            if (SECTION_17.equals((m.get("IDEXTERNE")).substring(4, 6))) {
                                return SECTION_17;
                            }
                            return SECTION_NON_17;
                        }
                    });

            // ajoute les non 17
            List<Map<String, String>> listNon17 = sectionsForThisYear.get(SECTION_NON_17);
            if (listNon17 != null) {
                if (listNon17.size() > 0) {
                    Map<String, String> m = listNon17.get(0); // toutes les années des section débitrice de cette liste
                                                              // sont identiques, je prend donc la première
                    final String anneeSectionDebitrice = m.get("IDEXTERNE").substring(0, 4);

                    /*
                     * dans le cas ou l'année débitrice est la même que l'année créditirice, et si l'on est dans le cas
                     * de la règle anté chronologique (règle 1), alors : on inverse l'ordre
                     */
                    if ((isAnte) && (anneeSectionDebitrice.equals(anneeSectionCreditrice))) {
                        Collections.reverse(listNon17);
                    }
                    res.addAll(listNon17);

                }
            }

            // Ajout les 17 (le 17 sont tj anté chronologique)
            List<Map<String, String>> list17 = sectionsForThisYear.get(SECTION_17);
            if (list17 != null) {
                Collections.reverse(list17);
                res.addAll(list17);
            }

            // Fin du traitement de cette année
        }
        return CARulesCouvertureSectionDebitrices._priorityBuildPriorityArray(res, sectionsDebitrices);
    }

    /*
     * ============================================================================== Définition des 3 règles possibles
     * ==============================================================================
     */

    /*
     * Regle 2
     */
    private static Integer[] _buildPriorityChrono(Map<String, String> sectionCreditrice,
            List<Map<String, String>> sectionsDebitrices) {
        return CARulesCouvertureSectionDebitrices._buildPriorityAnteOrChrono(sectionCreditrice, sectionsDebitrices,
                false);
    }

    /*
     * Determine l'odre de couverture des sections débitrices en fonction d'une section creditrice
     * 
     * renvoi un tableau indiquant les index de listOfMap, dans l'ordre dans lequel le proposition doivent se faire. la
     * liste peut contenir moins de valeurs que la liste des sections débitrices dans le cas ou il ne faudrait pas faire
     * de prosition de montant (cas de 17 créditrice n'ayant pas de 17 débitrice)
     */
    private static Object[] _ordreDeCouverturePourSectionCreditrice(final Map<String, String> sectionCreditrice,
            final boolean isRadie, final List<Map<String, String>> listOfSectionDebitrice) throws Exception {
        int categorie = Integer.parseInt((sectionCreditrice.get("IDEXTERNE")).substring(4, 6));

        // PO 4706
        // Premier filtrage, il n'y a JAMAIS - quel que soit la règle suivante - de proposition
        // de lettrage faite automatiquement pour une section débitrice qui ne serait pas échue...
        //
        List<Map<String, String>> listOfSectionDebitriceEchue = JadeListUtil.filter(listOfSectionDebitrice,
                new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {
                        String dateEcheanceYYYYMMDD = m.get("DATEECHEANCE");
                        String dateEcheance = dateEcheanceYYYYMMDD.substring(6) + "."
                                + dateEcheanceYYYYMMDD.substring(4, 6) + "." + dateEcheanceYYYYMMDD.substring(0, 4);

                        boolean isEchue = JadeDateUtil.isDateAfter(JACalendar.todayJJsMMsAAAA(), dateEcheance);
                        return isEchue;

                    }
                });

        // selon la catégorie de la section creditrice, l'ordre de couverture des
        // section debitrice sera différent.
        Integer[] pri = null;
        switch (categorie) {
            case 20:
                if (isRadie) {
                    pri = CARulesCouvertureSectionDebitrices._buildPriorityAnte(sectionCreditrice,
                            listOfSectionDebitriceEchue);
                    return new Object[] { CARulesCouvertureSectionDebitrices.RULE_ANTE, pri };
                } else {
                    pri = CARulesCouvertureSectionDebitrices._buildPriorityChrono(sectionCreditrice,
                            listOfSectionDebitriceEchue);
                    return new Object[] { CARulesCouvertureSectionDebitrices.RULE_CHRONO, pri };
                }

            case 17:
                pri = CARulesCouvertureSectionDebitrices._buildPriority17(sectionCreditrice,
                        listOfSectionDebitriceEchue);
                return new Object[] { CARulesCouvertureSectionDebitrices.RULE_17, pri };

            case 13:
            case 14:
            case 18: // case 80 :
                pri = CARulesCouvertureSectionDebitrices._buildPriorityAnte(sectionCreditrice,
                        listOfSectionDebitriceEchue);
                return new Object[] { CARulesCouvertureSectionDebitrices.RULE_ANTE, pri };

            default:
                pri = CARulesCouvertureSectionDebitrices._buildPriorityChrono(sectionCreditrice,
                        listOfSectionDebitriceEchue);
                return new Object[] { CARulesCouvertureSectionDebitrices.RULE_CHRONO, pri };
        }
    }

    /*
     * Construit la liste des priorités
     */
    private static Integer[] _priorityBuildPriorityArray(List<Map<String, String>> all,
            List<Map<String, String>> listOfSectionDebitrice) {
        Integer[] priorities = new Integer[listOfSectionDebitrice.size()];
        for (int i = 0; i < all.size(); i++) {
            Map<String, String> m = all.get(i);
            priorities[i] = new Integer(listOfSectionDebitrice.indexOf(m));
        }
        return priorities;
    }

    /*
     * Tri des décomptes, par année de décompte, puis par date de section [oca]
     */
    private static List<Map<String, String>> _prioritySortByYearAndDateSection(final List<Map<String, String>> listOfMap) {
        // copie pour ne pas altérer l'ordre de la list initial "listOfMap" lors du tri
        List<Map<String, String>> copylistOfSectionDebitrice = new ArrayList<Map<String, String>>();
        for (Map<String, String> val : listOfMap) {
            copylistOfSectionDebitrice.add(val);
        }
        Collections.sort(copylistOfSectionDebitrice, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> mLeft, Map<String, String> mRight) {
                // annee + cat (6 première pos de la section) + date de section YYYYMMDD
                // exemple si section 200805000 et date section = 01.05.2009 => 20080520090501
                // ceci permet un tri d'abbors sur l'année du décompte, puis sur la date de section
                String v1 = (mLeft.get("IDEXTERNE")).substring(0, 6) + mLeft.get("DATESECTION");
                String v2 = (mRight.get("IDEXTERNE")).substring(0, 6) + mRight.get("DATESECTION");
                return v1.compareTo(v2);
            }
        });
        return copylistOfSectionDebitrice;
    }

    private static List<Map<String, String>>[] _prioritySplitByAnnee(String anneeSectionCreditrice,
            List<Map<String, String>> copylistOfSectionDebitrice) {
        List<Map<String, String>> sameSectionYear = new ArrayList<Map<String, String>>();
        List<Map<String, String>> diffrentSectionYear = new ArrayList<Map<String, String>>();
        for (Map<String, String> m : copylistOfSectionDebitrice) {

            if (anneeSectionCreditrice.equals(m.get("IDEXTERNE").substring(0, 4))) {
                sameSectionYear.add(m);
            } else {
                diffrentSectionYear.add(m);
            }
        }
        List<List<Map<String, String>>> res = new ArrayList<List<Map<String, String>>>();
        res.add(sameSectionYear); // section debitrice de même année que section créditrice passé en param ==> index 0
        res.addAll(JadeListUtil.splitList(diffrentSectionYear, new JadeListUtil.Splitter<Map<String, String>>() {
            @Override
            public boolean splitWhen(Map<String, String> m1, Map<String, String> m2) {
                String year1 = m1.get("IDEXTERNE").substring(0, 4);
                String year2 = m2.get("IDEXTERNE").substring(0, 4);
                return !year1.equals(year2);
            }
        }));
        return res.toArray(new List[res.size()]);

    }

    public static PropositionModel buildBlankPropositions(List<Map<String, String>> pos,
            final List<Map<String, String>> neg) {
        PropositionModel model = new PropositionModel();
        model.propositions = new String[pos.size()][neg.size()];
        model.ordre = new String[pos.size()][neg.size()];
        FWCurrency[] montantsXACouvrir = new FWCurrency[pos.size()];
        FWCurrency[] montantsYDispo = new FWCurrency[neg.size()];
        for (int x = 0; x < pos.size(); x++) {
            String solde = pos.get(x).get("SOLDE");
            solde = JadeStringUtil.change(solde, ",", ".");
            montantsXACouvrir[x] = new FWCurrency(solde);
        }
        for (int y = 0; y < neg.size(); y++) {
            String solde = neg.get(y).get("SOLDE");
            solde = JadeStringUtil.change(solde, ",", ".");
            montantsYDispo[y] = new FWCurrency(solde);
            montantsYDispo[y].abs();
        }
        model.soldesNeg = montantsYDispo;
        model.soldesPos = montantsXACouvrir;
        model.rule = new String[neg.size()];

        /*
         * Init avec des valeur vide (pour les cas sans proposition)
         */
        for (int x = 0; x < pos.size(); x++) {
            for (int y = 0; y < neg.size(); y++) {
                model.propositions[x][y] = "";
                model.ordre[x][y] = "";
            }
        }
        return model;
    }

    /*
     * Sépare les sections débitrices par année, celle similaire à l'année passée en paramètre dans la première position
     * de la liste puis par ordre chronologique [oca]
     */

    /*
     * ============================================================================== Fait une proposition de
     * répartition des montants entre section Créditrice (-) et section débitrice (+)
     * ==============================================================================
     */
    public static PropositionModel buildPropositions(boolean isRadie, List<Map<String, String>> pos,
            final List<Map<String, String>> neg) throws Exception {
        PropositionModel model = new PropositionModel();
        model.propositions = new String[pos.size()][neg.size()];
        model.ordre = new String[pos.size()][neg.size()];
        FWCurrency[] montantsXACouvrir = new FWCurrency[pos.size()];
        FWCurrency[] montantsYDispo = new FWCurrency[neg.size()];
        for (int x = 0; x < pos.size(); x++) {
            String solde = pos.get(x).get("SOLDE");
            solde = JadeStringUtil.change(solde, ",", ".");
            montantsXACouvrir[x] = new FWCurrency(solde);
        }
        for (int y = 0; y < neg.size(); y++) {
            String solde = neg.get(y).get("SOLDE");
            solde = JadeStringUtil.change(solde, ",", ".");
            montantsYDispo[y] = new FWCurrency(solde);
            montantsYDispo[y].abs();
        }

        /*
         * Init avec des valeur vide (pour les cas sans proposition)
         */
        for (int x = 0; x < pos.size(); x++) {
            for (int y = 0; y < neg.size(); y++) {
                model.propositions[x][y] = "";
                model.ordre[x][y] = "";
            }
        }

        model.rule = new String[neg.size()];

        // Pour chaque section céditrice (axe y)...
        for (int y = 0; y < neg.size(); y++) {
            Object[] res = CARulesCouvertureSectionDebitrices._ordreDeCouverturePourSectionCreditrice(neg.get(y),
                    isRadie, pos);
            model.rule[y] = (String) res[0];
            Integer[] priorities = (Integer[]) res[1];

            int ordre = 0;
            // Couvre les sections débitrices (axe x) en fonction de l'ordre de priorité
            for (int x = 0; x < priorities.length; x++) {
                if (priorities[x] != null) {
                    int i = priorities[x].intValue();
                    model.ordre[i][y] = "" + (ordre++);
                    switch (montantsYDispo[y].compareTo(montantsXACouvrir[i])) {
                        case 0:
                        case -1:
                            // montant y <= montant en x, on utilise tout y pour compenser tout ou partie de x
                            model.propositions[i][y] = montantsYDispo[y].toString();
                            montantsXACouvrir[i].sub(montantsYDispo[y]);
                            montantsYDispo[y] = new FWCurrency(0); // plus rien de disponible
                            break;
                        case 1:
                            // Le montant y est > que le montant x, on utilise une partie de y pour couvrir x
                            model.propositions[i][y] = montantsXACouvrir[i].toString();
                            montantsYDispo[y].sub(montantsXACouvrir[i]);
                            montantsXACouvrir[i] = new FWCurrency(0); // plus rien à couvrir
                            break;
                    }
                }

            }
        }
        model.soldesNeg = montantsYDispo;
        model.soldesPos = montantsXACouvrir;
        return model;
    }

}
