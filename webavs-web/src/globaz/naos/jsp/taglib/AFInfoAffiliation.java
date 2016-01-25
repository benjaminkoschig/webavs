/*
 * Créé le 14 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.jsp.taglib;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Taglib utilisé pour afficher les informations principale de l'affiliation
 * 
 * @author dgi
 */
public class AFInfoAffiliation extends TagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String styleTag = "readOnly tabindex='-1' class='disabled'";
    private AFAffiliation affiliation = null;
    private String colspan = null;
    private String contentWidth = "500";
    private String name = null;
    private String showTitle = "Affilié";
    private String titleWidth = "70";

    @Override
    public int doStartTag() throws JspTagException {
        try {
            pageContext.getOut().println("<TR>");
            if (affiliation != null) {
                // afficher lien
                if (!JadeStringUtil.isEmpty(showTitle)) {
                    pageContext.getOut().println(
                            "<TD nowrap width='" + titleWidth + "'><A href='"
                                    + ((HttpServletRequest) pageContext.getRequest()).getContextPath()
                                    + "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                                    + affiliation.getAffiliationId() + "'>"
                                    + affiliation.getSession().getLabel("AFFILIE") + "</A>:</TD>");
                }
                pageContext.getOut().println(
                        "<TD nowrap width='" + contentWidth + "' "
                                + (colspan != null ? "colspan='" + colspan + "'" : "") + ">");
                if (name != null) {
                    pageContext.getOut().println(
                            "<INPUT type='hidden' name='" + name + "' value='" + affiliation.getAffiliationId() + "'>");
                }
                if (affiliation.getTiers().idTiersExterneFormate().length() != 0) {
                    // afficher id Tiers externe et no d'affilié
                    pageContext.getOut().println(
                            "<INPUT type='text' name='affilieNumero' size='28' maxlength='28' value='"
                                    + affiliation.getAffilieNumero() + "' " + AFInfoAffiliation.styleTag + ">");
                    pageContext.getOut().println(
                            "<INPUT type='text' name='idExterne' size='28' maxlength='28' value='"
                                    + affiliation.getTiers().idTiersExterneFormate() + "' "
                                    + AFInfoAffiliation.styleTag + ">");
                } else {
                    // afficher no affiliation
                    pageContext.getOut().println(
                            "<INPUT type='text' name='affilieNumero' size='60' maxlength='60' value='"
                                    + affiliation.getAffilieNumero() + "' " + AFInfoAffiliation.styleTag + ">");
                }

                // recherche de l'adresse d'exploitation
                // TIAvoirAdresse adresse = AFAffiliationUtil.getAdresseExploitation(this.affiliation);
                TIAdresseDataSource adresse = AFAffiliationUtil.getAdresse(affiliation);
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    String ligneAdresse1 = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1);
                    if (!JadeStringUtil.isEmpty(ligneAdresse1)) {
                        ligneAdresse1 += " " + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2);
                    } else {
                        ligneAdresse1 = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2);
                    }
                    pageContext.getOut().println(
                            "<INPUT type='text' name='nom' size='60' maxlength='60' value=\""
                                    + JadeStringUtil.change((!JadeStringUtil.isEmpty(ligneAdresse1) ? ligneAdresse1
                                            : affiliation.getTiersNom()), "\"", "&quot;") + "\" "
                                    + AFInfoAffiliation.styleTag + ">");

                    // construction de la ligne adresse (rue+no+npa+localité)
                    StringBuffer ligneAdresse = new StringBuffer();
                    ligneAdresse.append((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
                    ligneAdresse.append(" ");
                    ligneAdresse.append((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO));
                    if (ligneAdresse.length() != 0) {
                        ligneAdresse.append(", ");
                    }
                    ligneAdresse.append((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                    ligneAdresse.append(" ");
                    ligneAdresse.append((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                    pageContext.getOut().println(
                            "<INPUT type='text' name='localiteLong' size='60' maxlength='60' value=\""
                                    + JadeStringUtil.change(ligneAdresse.toString(), "\"", "&quot;") + "\" "
                                    + AFInfoAffiliation.styleTag + ">");

                    // canton
                    String canton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_COURT) + " - "
                            + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON);
                    pageContext.getOut().println(
                            "<INPUT type='text' name='canton' size='60' maxlength='60' value='" + canton + "' "
                                    + AFInfoAffiliation.styleTag + ">");
                } else {
                    // pas d'adresse
                    pageContext.getOut().println(
                            "<INPUT type='text' name='nom' size='60' maxlength='60' value=\""
                                    + JadeStringUtil.change(affiliation.getTiersNom(), "\"", "&quot;") + "\" "
                                    + AFInfoAffiliation.styleTag + ">");
                    pageContext.getOut().println(
                            "<INPUT type='text' name='empty3' size='60' maxlength='60' value='' "
                                    + AFInfoAffiliation.styleTag + ">");
                    pageContext.getOut().println(
                            "<INPUT type='text' name='empty4' size='60' maxlength='60' value='' "
                                    + AFInfoAffiliation.styleTag + ">");
                }
            } else {
                // afficher titre
                if (!JadeStringUtil.isEmpty(showTitle)) {
                    pageContext.getOut().println("<TD nowrap width='" + titleWidth + "'>" + showTitle + ":</TD>");
                }
                pageContext.getOut().println(
                        "<TD nowrap width='" + contentWidth + "' "
                                + (colspan != null ? "colspan='" + colspan + "'" : "") + ">");
                // contenu vide
                pageContext.getOut().println(
                        "<INPUT type='text' name='empty1' size='60' maxlength='60' value='' "
                                + AFInfoAffiliation.styleTag + ">");
                pageContext.getOut().println(
                        "<INPUT type='text' name='empty2' size='60' maxlength='60' value='' "
                                + AFInfoAffiliation.styleTag + ">");
                pageContext.getOut().println(
                        "<INPUT type='text' name='empty3' size='60' maxlength='60' value='' "
                                + AFInfoAffiliation.styleTag + ">");
                pageContext.getOut().println(
                        "<INPUT type='text' name='empty4' size='60' maxlength='60' value='' "
                                + AFInfoAffiliation.styleTag + ">");
            }
            pageContext.getOut().println("</TD></TR>");

        } catch (Exception e) {
            throw new JspTagException(e.toString());
        }
        return Tag.SKIP_BODY;
    }

    public void setAffiliation(AFAffiliation aff) {
        affiliation = aff;
    }

    public void setColspan(String nbr) {
        colspan = nbr;
    }

    public void setContentWidth(String width) {
        contentWidth = width;
    }

    public void setIdAffiliation(String idAff) {
    }

    public void setName(String nameAtt) {
        name = nameAtt;
    }

    public void setShowTitle(String show) {
        showTitle = show;
    }

    public void setTitleWidth(String width) {
        titleWidth = width;
    }

}
