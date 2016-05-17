package globaz.corvus.vb.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

/**
 * Classe permettant de convertir un {@link REDemandeRenteJointPrestationAccordeeViewBean} en code HTML pour les JSP
 * 
 * @author PBA
 */
public class REDemandeRenteJointPrestationAccordeeHtmlConverter {

    private static final String HTML_CARRIAGE_RETURN = "<br/>";
    private static final String HTML_BLANK_SPACE = " ";
    private static final String HTML_TRAIT_UNION_WITH_SPACE = " - ";
    private static final String HTML_TRAIT_UNION = "-";
    private static final String HTML_ITALIC = "<i>";
    private static final String HTML_ITALIC_END = "</i>";
    private static final String HTML_BOLD = "<b>";
    private static final String HTML_BOLD_END = "</b>";

    /**
     * Format les détails d'un requérant pour un {@link REDemandeRenteJointPrestationAccordeeViewBean} <br/>
     * (avec la ligne concernant le décès si elle a lieu d'être)<br/>
     * Utilise les méthodes de {@link globaz.prestation.tools.nnss.PRNSSUtil PRNSSUtil}
     * 
     * @param viewBean
     *            le viewBean à formatter
     * @return le code HTML généré, sous forme de {@link java.lang.String String}
     */
    public static String formatDetailRequerant(REDemandeRenteJointPrestationAccordeeViewBean viewBean) throws Exception {
        if (JadeStringUtil.isEmpty(viewBean.getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListe(viewBean.getNoAVS(),
                    viewBean.getNom() + " " + viewBean.getPrenom(), viewBean.getDateNaissance(),
                    viewBean.getLibelleCourtSexe(), viewBean.getLibellePays());
        } else {
            return PRNSSUtil.formatDetailRequerantListeDecede(viewBean.getNoAVS(),
                    viewBean.getNom() + " " + viewBean.getPrenom(), viewBean.getDateNaissance(),
                    viewBean.getLibelleCourtSexe(), viewBean.getLibellePays(), viewBean.getDateDeces());
        }
    }

    private static StringBuilder premiereLigne(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {

        StringBuilder htmlTypeDemandeBuilder = new StringBuilder();

        if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_BILATERALES)) {
            htmlTypeDemandeBuilder.append(HTML_CARRIAGE_RETURN);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_BILATERALES"));
        } else if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_TRANSITOIRE)) {
            htmlTypeDemandeBuilder.append(HTML_BLANK_SPACE);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_TRANSITOIRE"));
        } else if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL)) {
            htmlTypeDemandeBuilder.append(HTML_CARRIAGE_RETURN);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_PREVISIONNEL"));

        }

        return htmlTypeDemandeBuilder;
    }

    private static StringBuilder deuxiemeLigne(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {

        StringBuilder htmlTypeDemandeBuilder = new StringBuilder();

        if (REDemandeRenteJointPrestationAccordeeHtmlConverter.isInfoComplRenteVeuvePerdure(viewBean)) {
            htmlTypeDemandeBuilder.append(HTML_CARRIAGE_RETURN);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel(
                    "JSP_DRE_R_TYPEDEMANDE_PLUS_RENTE_VEUVE_PERDURE"));
        } else if (REDemandeRenteJointPrestationAccordeeHtmlConverter.isInfoComplRenteRefus(viewBean)) {
            htmlTypeDemandeBuilder.append(HTML_CARRIAGE_RETURN);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_REFUS"));
        } else if (REDemandeRenteJointPrestationAccordeeHtmlConverter.isInfoComplDeces(viewBean)) {
            htmlTypeDemandeBuilder.append(HTML_TRAIT_UNION_WITH_SPACE);
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPE_INFO_COMPL_DECES"));
        }

        return htmlTypeDemandeBuilder;
    }

    protected static Map<CodeRente, String> combineListsIntoOrderedMap(List<CodeRente> keys, List<String> values) {

        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Cannot combine lists with dissimilar sizes");
        }

        Map<CodeRente, String> map = new HashMap<CodeRente, String>();

        for (int i = 0; i < keys.size(); i++) {

            map.put((keys.get(i)), values.get(i));
        }
        return map;
    }

    protected static List<CodeRente> convertToCodesRente(List<String> codesRenteToConvert) {

        List<CodeRente> codesRente = new ArrayList<CodeRente>();

        for (String code : codesRenteToConvert) {
            codesRente.add(new CodeRente(code));
        }

        return codesRente;
    }

    protected static StringBuilder codesPrestations(List<String> codesPrest, List<String> dateFinDroit) {

        // converion de la liste en liste de CodeRente pour utilisation de plusieurs meme valeurs dans une map en tant
        // que clé!
        List<CodeRente> codesPrestations = convertToCodesRente(codesPrest);
        // map des deux listes combinées
        final Map<CodeRente, String> codePrestFinDroit = combineListsIntoOrderedMap(codesPrestations, dateFinDroit);

        List<String> codesPrestationsAsHtml = FluentIterable.from(codesPrestations)

        .filter(new Predicate<CodeRente>() {

            @Override
            public boolean apply(CodeRente input) {
                return input.getCodeRente() != null && !input.getCodeRente().isEmpty();
            }

        }).transform(new Function<CodeRente, String>() {

            @Override
            public String apply(CodeRente input) {
                String output = "";

                String dateFinDroit = codePrestFinDroit.get(input);
                boolean isFinDroit = dateFinDroit != null && !dateFinDroit.isEmpty();

                if (isFinDroit) {
                    output = HTML_ITALIC + input.getCodeRente() + HTML_ITALIC_END;
                } else {
                    output = HTML_BOLD + input.getCodeRente() + HTML_BOLD_END;
                }

                return output;
            }

        }).toList();

        Joiner codesFusiones = Joiner.on(HTML_TRAIT_UNION);

        return new StringBuilder(codesFusiones.join(codesPrestationsAsHtml));

    }

    /**
     * Format le type de demande pour un {@link REDemandeRenteJointPrestationAccordeeViewBean}<br/>
     * (avec les codes de prestations)<br/>
     * 
     * @param viewBean
     *            le viewBean à formatter
     * @return le code HTML généré, sous forme de {@link java.lang.String String}
     * @throws Exception
     *             si impossible de résoudre les codes systèmes du type de demande
     */
    public static String formatTypeDemande(REDemandeRenteJointPrestationAccordeeViewBean viewBean) throws Exception {

        StringBuilder htmlTypeDemandeBuilder = new StringBuilder(viewBean.getSession().getCodeLibelle(
                viewBean.getCsTypeDemande()));

        htmlTypeDemandeBuilder.append(premiereLigne(viewBean));

        htmlTypeDemandeBuilder.append(deuxiemeLigne(viewBean));

        htmlTypeDemandeBuilder.append(HTML_CARRIAGE_RETURN);

        htmlTypeDemandeBuilder.append(codesPrestations(viewBean.getCodesPrestation(), viewBean.getDatesFinDroit()));

        return htmlTypeDemandeBuilder.toString();
    }

    /**
     * BZ 5493, charge l'information complémentaire de la rente afin de vérifier si elle est de type Décès
     * 
     * @param viewBean
     * @return <code>true</code> si l'information complémentaire est de type "Décès", sinon <code>false</code>
     */
    private static boolean isInfoComplDeces(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {
        return IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_DECES.equals(viewBean.getCsTypeInfoComplementaire());
    }

    /**
     * BZ 5198, charge l'information complémentaire de la rente pour savoir si elle est de type "veuve perdure"
     * 
     * @param viewBean
     * @return <code>true</code> si de type "rente de veuve perdure", sinon <code>false</code>
     */
    private static boolean isInfoComplRenteVeuvePerdure(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {

        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE.equals(viewBean
                .getCsTypeInfoComplementaire())) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isInfoComplRenteRefus(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {

        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_REFUS.equals(viewBean.getCsTypeInfoComplementaire())) {
            return true;
        } else {
            return false;
        }
    }

    protected static class CodeRente {

        private String codeRente;

        public CodeRente(String codeRente) {
            this.codeRente = codeRente;
        }

        String getCodeRente() {
            return codeRente;
        }

    }

}
