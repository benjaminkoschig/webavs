package globaz.corvus.vb.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * Classe permettant de convertir un {@link REDemandeRenteJointPrestationAccordeeViewBean} en code HTML pour les JSP
 * 
 * @author PBA
 */
public class REDemandeRenteJointPrestationAccordeeHtmlConverter {

    /**
     * Format les d�tails d'un requ�rant pour un {@link REDemandeRenteJointPrestationAccordeeViewBean} <br/>
     * (avec la ligne concernant le d�c�s si elle a lieu d'�tre)<br/>
     * Utilise les m�thodes de {@link globaz.prestation.tools.nnss.PRNSSUtil PRNSSUtil}
     * 
     * @param viewBean
     *            le viewBean � formatter
     * @return le code HTML g�n�r�, sous forme de {@link java.lang.String String}
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

    /**
     * Format le type de demande pour un {@link REDemandeRenteJointPrestationAccordeeViewBean}<br/>
     * (avec les codes de prestations)<br/>
     * 
     * @param viewBean
     *            le viewBean � formatter
     * @return le code HTML g�n�r�, sous forme de {@link java.lang.String String}
     * @throws Exception
     *             si impossible de r�soudre les codes syst�mes du type de demande
     */
    public static String formatTypeDemande(REDemandeRenteJointPrestationAccordeeViewBean viewBean) throws Exception {
        StringBuilder htmlTypeDemandeBuilder = new StringBuilder();

        htmlTypeDemandeBuilder.append(viewBean.getSession().getCodeLibelle(viewBean.getCsTypeDemande()));

        // 1�re ligne
        // Type de demande
        if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_BILATERALES)) {
            htmlTypeDemandeBuilder.append("<br/>");
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_BILATERALES"));
        } else if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_TRANSITOIRE)) {
            htmlTypeDemandeBuilder.append(" ");
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_TRANSITOIRE"));
        } else if (viewBean.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL)) {
            htmlTypeDemandeBuilder.append("<br/>");
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_PREVISIONNEL"));
            // BZ 5198, si de type rente de veuve perdure (dans les info compl�mentaires)
            // je l'affiche dans le type de demande
        } else if (REDemandeRenteJointPrestationAccordeeHtmlConverter.isInfoComplRenteVeuvePerdure(viewBean)) {
            htmlTypeDemandeBuilder.append("<br/>");
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel(
                    "JSP_DRE_R_TYPEDEMANDE_PLUS_RENTE_VEUVE_PERDURE"));
        } else if (REDemandeRenteJointPrestationAccordeeHtmlConverter.isInfoComplDeces(viewBean)) {
            htmlTypeDemandeBuilder.append(" - ");
            htmlTypeDemandeBuilder.append(viewBean.getSession().getLabel("JSP_DRE_R_TYPE_INFO_COMPL_DECES"));
        }
        htmlTypeDemandeBuilder.append("<br/>");

        // 2�me ligne
        // les codes de prestation
        boolean isFirst = true;
        for (int i = 0; i < viewBean.getCodesPrestation().size(); i++) {
            String codePrestation = viewBean.getCodesPrestation().get(i);

            if (!JadeStringUtil.isBlank(codePrestation)) {
                boolean isFinDroit = !JadeStringUtil.isEmpty(viewBean.getDatesFinDroit().get(i));

                if (!isFirst) {
                    htmlTypeDemandeBuilder.append("-");
                }
                // si la prestation est termin�e, le code prestation sera en italique
                if (isFinDroit) {
                    htmlTypeDemandeBuilder.append("<i>");
                }
                // sinon en gras
                else {
                    htmlTypeDemandeBuilder.append("<b>");
                }

                htmlTypeDemandeBuilder.append(viewBean.getCodesPrestation().get(i));

                if (isFinDroit) {
                    htmlTypeDemandeBuilder.append("</i>");
                } else {
                    htmlTypeDemandeBuilder.append("</b>");
                }

                if (isFirst) {
                    isFirst = false;
                }
            }
        }

        return htmlTypeDemandeBuilder.toString();
    }

    /**
     * BZ 5493, charge l'information compl�mentaire de la rente afin de v�rifier si elle est de type D�c�s
     * 
     * @param viewBean
     * @return <code>true</code> si l'information compl�mentaire est de type "D�c�s", sinon <code>false</code>
     */
    private static boolean isInfoComplDeces(REDemandeRenteJointPrestationAccordeeViewBean viewBean) {
        return IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_DECES.equals(viewBean.getCsTypeInfoComplementaire());
    }

    /**
     * BZ 5198, charge l'information compl�mentaire de la rente pour savoir si elle est de type "veuve perdure"
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
}
