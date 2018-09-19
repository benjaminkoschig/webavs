/*
 * Créé le 15 janv. 07
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.vb.acor;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.api.BITransaction;

/**
 * @author scr
 *
 */
public class RECalculACORDemandeRenteViewBean extends REAbstractCalculACORViewBean {

    private String csTypeDemandeRente;
    private String idDemandeRente;
    private String idTiers;
    private boolean isFileContent = false;

    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    /**
     * XSL permettant de filtrer uniquement le contenu nécessaire à la remontée des données de ACOR. C'est XSL est
     * transforné avec la feuille de calcul ACOR. Actuellement, pas utilisé !!!!
     *
     * @deprecated
     * @return
     */
    @Deprecated
    public String getFullXSLT() {
        StringBuffer s = new StringBuffer();
        s.append("<!-- <xsl:stylesheet xmlns:xsl=\\\"http://www.w3.org/TR/WD-xsl\\\"> -->");
        s.append("<xsl:stylesheet version =\\\"1.0\\\" xmlns:xsl=\\\"http://www.w3.org/1999/XSL/Transform\\\">");
        s.append("<xsl:output method=\\\"xml\\\" omit-xml-declaration=\\\"no\\\"/>");
        s.append("   <xsl:template match=\\\"f_calcul\\\">   ");
        s.append("			<?xml version=\\\"1.0\\\" encoding=\\\"ISO-8859-1\\\"?>");
        s.append("			<f_calcul>											");
        s.append("				<demande>");
        s.append("					<xsl:apply-templates select=\\\"demande\\\"/>");
        s.append("				</demande>");

        s.append("					<xsl:apply-templates select=\\\"assure\\\"/>							");

        s.append("				<comparaison>");
        s.append("					<xsl:apply-templates select=\\\"comparaison\\\"/>");
        s.append("				</comparaison>");

        s.append("				<xsl:apply-templates select=\\\"decision\\\"/>");

        s.append("				<xsl:apply-templates select=\\\"carriere_assurance\\\"/>");

        s.append("				<xsl:apply-templates select=\\\"bases_calcul\\\"/>");

        s.append("				<splitting>");
        s.append("					<xsl:apply-templates select=\\\"splitting\\\"/>");
        s.append("				</splitting>				");
        s.append("			</f_calcul>			");
        s.append("   </xsl:template>");

        s.append("	<xsl:template match=\\\"demande\\\">");
        s.append(
                "		<![CDATA[<date_traitement>]]><xsl:value-of select=\\\"date_traitement\\\"/><![CDATA[</date_traitement>]]>");
        s.append("		<![CDATA[<provisoire>]]><xsl:value-of select=\\\"provisoire\\\"/><![CDATA[</provisoire>]]>");
        s.append(
                "		<![CDATA[<previsionnel>]]><xsl:value-of select=\\\"previsionnel\\\"/><![CDATA[</previsionnel>]]>");
        s.append("	 </xsl:template>");

        s.append("   <xsl:template match=\\\"assure\\\">");
        s.append("		 <![CDATA[<assure>]]>");
        s.append("			<![CDATA[<id type=\\\"]]><xsl:value-of select=\\\"id/@type\\\"/><![CDATA[\\\">]]>");
        s.append("				<xsl:value-of select=\\\"id\\\"/>");
        s.append("			<![CDATA[</id>]]>			");
        s.append(
                "			<![CDATA[<anticipation>]]><xsl:value-of select=\\\"anticipation\\\"/><![CDATA[</anticipation>]]>");
        s.append(
                "			<![CDATA[<etat_civil>]]><xsl:value-of select=\\\"etat_civil\\\"/><![CDATA[</etat_civil>]]>");
        s.append(
                "			<![CDATA[<codeRefugie>]]><xsl:value-of select=\\\"codeRefugie\\\"/><![CDATA[</codeRefugie>]]>");
        s.append("			<![CDATA[<fonction>]]><xsl:value-of select=\\\"fonction\\\"/><![CDATA[</fonction>]]>");
        s.append("		 <![CDATA[</assure>]]>");
        s.append("   </xsl:template>");

        s.append("	<xsl:template match=\\\"comparaison\\\">");
        s.append(
                "		<![CDATA[<base_favorable>]]><xsl:value-of select=\\\"base_favorable\\\"/><![CDATA[</base_favorable>]]>");
        s.append(
                "		<![CDATA[<base_decision>]]><xsl:value-of select=\\\"base_decision\\\"/><![CDATA[</base_decision>]]>		");
        s.append("	 </xsl:template>   ");

        s.append("	 <xsl:template match=\\\"decision\\\">	 ");
        s.append("		 <![CDATA[<decision>]]>");
        s.append("			<![CDATA[<prestation>]]>			");
        s.append(
                "				<![CDATA[<beneficiaire>]]><xsl:value-of select=\\\"prestation/beneficiaire\\\"/><![CDATA[</beneficiaire>]]>				");
        s.append("				<![CDATA[<rente>]]>");
        s.append(
                "					<![CDATA[<bases>]]><xsl:value-of select=\\\"prestation/rente/bases\\\"/><![CDATA[</bases>]]>");
        s.append(
                "					<![CDATA[<code_cas_special>]]><xsl:value-of select=\\\"prestation/rente/code_cas_special\\\"/><![CDATA[</code_cas_special>]]>");
        s.append(
                "					<![CDATA[<btr_mont>]]><xsl:value-of select=\\\"prestation/rente/btr_mont\\\"/><![CDATA[</btr_mont>]]>");
        s.append(
                "					<![CDATA[<echelle>]]><xsl:value-of select=\\\"prestation/rente/echelle\\\"/><![CDATA[</echelle>]]>");
        s.append(
                "					<![CDATA[<genre>]]><xsl:value-of select=\\\"prestation/rente/genre\\\"/><![CDATA[</genre>]]>");
        s.append(
                "					<![CDATA[<debut_droit>]]><xsl:value-of select=\\\"prestation/rente/debut_droit\\\"/><![CDATA[</debut_droit>]]>");
        s.append(
                "					<![CDATA[<fin_droit>]]><xsl:value-of select=\\\"prestation/rente/fin_droit\\\"/><![CDATA[</fin_droit>]]>");
        s.append(
                "					<![CDATA[<code_mutation>]]><xsl:value-of select=\\\"prestation/rente/code_mutation\\\"/><![CDATA[</code_mutation>]]>");
        s.append(
                "					<![CDATA[<fin_prevue>]]><xsl:value-of select=\\\"prestation/rente/fin_prevue\\\"/><![CDATA[</fin_prevue>]]>");
        s.append(
                "					<![CDATA[<ncpl1>]]><xsl:value-of select=\\\"prestation/rente/ncpl1\\\"/><![CDATA[</ncpl1>]]>");
        s.append(
                "					<![CDATA[<ncpl2>]]><xsl:value-of select=\\\"prestation/rente/ncpl2\\\"/><![CDATA[</ncpl2>]]>");
        s.append(
                "					<![CDATA[<faute_grave>]]><xsl:value-of select=\\\"prestation/rente/faute_grave\\\"/><![CDATA[</faute_grave>]]>");
        s.append(
                "					<![CDATA[<art29LAI>]]><xsl:value-of select=\\\"prestation/rente/art29LAI\\\"/><![CDATA[</art29LAI>]]>");
        s.append(
                "					<![CDATA[<art48LAI>]]><xsl:value-of select=\\\"prestation/rente/art48LAI\\\"/><![CDATA[</art48LAI>]]>");
        s.append(
                "					<![CDATA[<art46LAS>]]><xsl:value-of select=\\\"prestation/rente/art46LAS\\\"/><![CDATA[</art46LAS>]]>");
        s.append(
                "					<![CDATA[<supp_carr>]]><xsl:value-of select=\\\"prestation/rente/supp_carr\\\"/><![CDATA[</supp_carr>]]>");

        s.append("					<![CDATA[<versement>]]>");
        s.append(
                "						<![CDATA[<debut>]]><xsl:value-of select=\\\"prestation/rente/versement/debut\\\"/><![CDATA[</debut>]]>");
        s.append(
                "						<![CDATA[<fin>]]><xsl:value-of select=\\\"prestation/rente/versement/fin\\\"/><![CDATA[</fin>]]>");
        s.append(
                "						<![CDATA[<montant>]]><xsl:value-of select=\\\"prestation/rente/versement/montant\\\"/><![CDATA[</montant>]]>");
        s.append("					<![CDATA[</versement>]]>");

        // Tri de tous les etats par date de debut.
        s.append("			<xsl:for-each select=\\\"prestation/rente/etat\\\">");
        s.append("			<xsl:sort select=\\\"debut\\\"/>");
        s.append("					<![CDATA[<etat>]]>");
        s.append("						<![CDATA[<an>]]><xsl:value-of select=\\\"an\\\"/><![CDATA[</an>]]>");
        s.append("						<![CDATA[<debut>]]><xsl:value-of select=\\\"debut\\\"/><![CDATA[</debut>]]>");
        s.append("						<![CDATA[<fin>]]><xsl:value-of select=\\\"fin\\\"/><![CDATA[</fin>]]>");
        s.append(
                "						<![CDATA[<montant>]]><xsl:value-of select=\\\"montant\\\"/><![CDATA[</montant>]]>");
        s.append(
                "						<![CDATA[<red_ant>]]><xsl:value-of select=\\\"red_ant\\\"/><![CDATA[</red_ant>]]>");
        s.append(
                "						<![CDATA[<sup_aj>]]><xsl:value-of select=\\\"sup_aj\\\"/><![CDATA[</sup_aj>]]>");
        s.append("					<![CDATA[</etat>]]>");
        s.append("			</xsl:for-each>");

        s.append("				<![CDATA[</rente>]]>		");
        s.append("			<![CDATA[</prestation>]]>		");
        s.append("		<![CDATA[</decision>]]>");
        s.append("	 </xsl:template>");

        s.append("	 <xsl:template match=\\\"carriere_assurance\\\">");
        s.append("		<![CDATA[<carriere_assurance>]]>");
        s.append("			<![CDATA[<assure>]]><xsl:value-of select=\\\"assure\\\"/><![CDATA[</assure>]]>");
        s.append("			<![CDATA[<rev_j_tot>]]><xsl:value-of select=\\\"rev_j_tot\\\"/><![CDATA[</rev_j_tot>]]>");
        s.append(
                "			<![CDATA[<rev_net_tot>]]><xsl:value-of select=\\\"rev_net_tot\\\"/><![CDATA[</rev_net_tot>]]>");
        s.append("		<![CDATA[</carriere_assurance>]]>			");
        s.append("	 </xsl:template>");

        s.append("   <xsl:template match=\\\"bases_calcul\\\">");
        s.append("	  <xsl:if test=\\\"genre = 'decision'\\\">");
        s.append("		<![CDATA[<bases_calcul>]]>");
        s.append("			<![CDATA[<id>]]><xsl:value-of select=\\\"id\\\"/><![CDATA[</id>]]>");
        s.append(
                "			<![CDATA[<generateur>]]><xsl:value-of select=\\\"generateur\\\"/><![CDATA[</generateur>]]>");
        s.append(
                "			<![CDATA[<codeRefugie>]]><xsl:value-of select=\\\"codeRefugie\\\"/><![CDATA[</codeRefugie>]]>		");

        s.append("			<![CDATA[<invalidite>]]>");
        s.append(
                "				<![CDATA[<invalidite_precoce>]]><xsl:value-of select=\\\"invalidite_precoce\\\"/><![CDATA[</invalidite_precoce>]]>");
        s.append(
                "				<![CDATA[<survenance_ev_ass>]]><xsl:value-of select=\\\"survenance_ev_ass\\\"/><![CDATA[</survenance_ev_ass>]]>");
        s.append("				<![CDATA[<oai>]]><xsl:value-of select=\\\"oai\\\"/><![CDATA[</oai>]]>");

        s.append("				<![CDATA[<fraction>]]><xsl:value-of select=\\\"fraction\\\"/><![CDATA[</fraction>]]>");
        s.append(
                "				<![CDATA[<genre_invalidite>]]><xsl:value-of select=\\\"genre_invalidite\\\"/><![CDATA[</genre_invalidite>]]>");
        s.append(
                "				<![CDATA[<degre_invalidite>]]><xsl:value-of select=\\\"degre_invalidite\\\"/><![CDATA[</degre_invalidite>]]>");
        s.append("			<![CDATA[</invalidite>]]>			");

        s.append("			<![CDATA[<evenement_assure>]]>");
        s.append(
                "				<![CDATA[<genre_evenement>]]><xsl:value-of select=\\\"genre_evenement\\\"/><![CDATA[</genre_evenement>]]>");
        s.append("			<![CDATA[</evenement_assure>]]>			");

        s.append("			<![CDATA[<ajournement>]]>");
        s.append(
                "				<![CDATA[<date_revocation>]]><xsl:value-of select=\\\"date_revocation\\\"/><![CDATA[</date_revocation>]]>");
        s.append(
                "				<![CDATA[<duree_ajournement>]]><xsl:value-of select=\\\"duree_ajournement\\\"/><![CDATA[</duree_ajournement>]]>");
        s.append("			<![CDATA[</ajournement>]]>			");

        s.append("			<![CDATA[<anticipation>]]>");
        s.append("				<![CDATA[<n_annees>]]><xsl:value-of select=\\\"n_annees\\\"/><![CDATA[</n_annees>]]>");
        s.append(
                "				<![CDATA[<date_anticipation>]]><xsl:value-of select=\\\"date_anticipation\\\"/><![CDATA[</date_anticipation>]]>");
        s.append(
                "				<![CDATA[<taux_reduction_anticipation>]]><xsl:value-of select=\\\"taux_reduction_anticipation\\\"/><![CDATA[</taux_reduction_anticipation>]]>");
        s.append("			<![CDATA[</anticipation>]]>			");

        s.append("			<![CDATA[<base_ram>]]>");
        s.append("				<![CDATA[<rev_lucr>]]>");
        s.append(
                "					<![CDATA[<fac_rev>]]><xsl:value-of select=\\\"base_ram/rev_lucr/fac_rev\\\"/><![CDATA[</fac_rev>]]>");
        s.append(
                "					<![CDATA[<code_split>]]><xsl:value-of select=\\\"base_ram/rev_lucr/code_split\\\"/><![CDATA[</code_split>]]>");
        s.append(
                "					<![CDATA[<duree>]]><xsl:value-of select=\\\"base_ram/rev_lucr/duree\\\"/><![CDATA[</duree>]]>");
        s.append(
                "					<![CDATA[<sup_carr>]]><xsl:value-of select=\\\"base_ram/rev_lucr/sup_carr\\\"/><![CDATA[</sup_carr>]]>");
        s.append("				<![CDATA[</rev_lucr>]]>");
        s.append("			<![CDATA[</base_ram>]]>");

        s.append("			<![CDATA[<ram>]]><xsl:value-of select=\\\"ram\\\"/><![CDATA[</ram>]]>		");

        s.append("			<![CDATA[<base_echelle>]]>");
        s.append("				<![CDATA[<an_niveau>]]>");
        s.append("					<xsl:value-of select=\\\"base_echelle/an_niveau\\\"/>");
        s.append("				<![CDATA[</an_niveau>]]>");

        s.append("				<![CDATA[<an_cot_clss>]]>");
        s.append("					<xsl:value-of select=\\\"base_echelle/an_cot_clss\\\"/>");
        s.append("				<![CDATA[</an_cot_clss>]]>");

        s.append("				<xsl:for-each select=\\\"base_echelle/d_cot\\\">");
        s.append("				<![CDATA[<d_cot>]]>			");
        s.append(
                "					<![CDATA[<type>]]><xsl:value-of select=\\\"type\\\"/><![CDATA[</type>]]>				");
        s.append("					<![CDATA[<total>]]>");
        s.append(
                "						<![CDATA[<annees>]]><xsl:value-of select=\\\"total/annees\\\"/><![CDATA[</annees>]]>");
        s.append(
                "						<![CDATA[<mois>]]><xsl:value-of select=\\\"total/mois\\\"/><![CDATA[</mois>]]>				");
        s.append("					<![CDATA[</total>]]>");

        s.append("					<![CDATA[<av_73>]]>");
        s.append(
                "						<![CDATA[<annees>]]><xsl:value-of select=\\\"av_73/annees\\\"/><![CDATA[</annees>]]>");
        s.append(
                "						<![CDATA[<mois>]]><xsl:value-of select=\\\"av_73/mois\\\"/><![CDATA[</mois>]]>				");
        s.append("					<![CDATA[</av_73>]]>			");

        s.append("					<![CDATA[<ap_73>]]>");
        s.append(
                "						<![CDATA[<annees>]]><xsl:value-of select=\\\"ap_73/annees\\\"/><![CDATA[</annees>]]>");
        s.append(
                "						<![CDATA[<mois>]]><xsl:value-of select=\\\"ap_73/mois\\\"/><![CDATA[</mois>]]>				");
        s.append("					<![CDATA[</ap_73>]]>							");
        s.append("				<![CDATA[</d_cot>]]>");
        s.append("	</xsl:for-each>");

        s.append("		<![CDATA[</base_echelle>]]>");
        s.append("	<![CDATA[</bases_calcul>]]>");
        s.append("	  </xsl:if>");
        s.append("   </xsl:template>");
        s.append("   	<xsl:template match=\\\"splitting\\\">");
        s.append("		<![CDATA[<id>]]><xsl:value-of select=\\\"id\\\"/><![CDATA[</id>]]>		");
        s.append("	 </xsl:template>   ");
        s.append("</xsl:stylesheet>");
        return s.toString();
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getXSLT() {
        StringBuffer s = new StringBuffer();
        s.append("<!-- <xsl:stylesheet xmlns:xsl=\\\"http://www.w3.org/TR/WD-xsl\\\"> -->");
        s.append("<xsl:stylesheet version =\\\"1.0\\\" xmlns:xsl=\\\"http://www.w3.org/1999/XSL/Transform\\\">");
        s.append("<xsl:output method=\\\"xml\\\" omit-xml-declaration=\\\"no\\\"/>");
        s.append("   <xsl:template match=\\\"f_calcul\\\">   ");
        s.append("			<?xml version=\\\"1.0\\\" encoding=\\\"ISO-8859-1\\\"?>");
        s.append("			<f_calcul>											");
        // s.append(" <xsl:apply-templates select=\\\"evenement/bases_calcul/decision\\\"/>");
        s.append("				<xsl:apply-templates select=\\\"evenement/bases_calcul\\\"/>");
        s.append("			</f_calcul>			");
        s.append("   </xsl:template>");

        // s.append(" <xsl:template match=\\\"decision\\\"> ");
        // s.append(" <![CDATA[<decision>]]>");
        // s.append(" <![CDATA[<prestation>]]> ");
        // s.append(
        // " <![CDATA[<beneficiaire>]]><xsl:value-of select=\\\"prestation/beneficiaire\\\"/><![CDATA[</beneficiaire>]]>
        // ");
        // s.append(" <![CDATA[<rente>]]>");
        // // s.append(
        // // " <![CDATA[<bases>]]><xsl:value-of select=\\\"prestation/rente/bases\\\"/><![CDATA[</bases>]]>");
        // s.append(
        // " <![CDATA[<debut_droit>]]><xsl:value-of
        // select=\\\"prestation/rente/debut_droit\\\"/><![CDATA[</debut_droit>]]>");
        // s.append(
        // " <![CDATA[<genre>]]><xsl:value-of select=\\\"prestation/rente/genre\\\"/><![CDATA[</genre>]]>");
        // s.append(
        // " <![CDATA[<remarques>]]><xsl:value-of select=\\\"prestation/rente/remarques\\\"/><![CDATA[</remarques>]]>");
        //
        // /*
        // * s.append(" <xsl:for-each select=\\\"prestation/rente/remarques\\\">" );s.append(
        // * " <![CDATA[<remarques>]]><xsl:value-of select=\\\".\\\"/><![CDATA[</remarques>]]>" );
        // * s.append(" </xsl:for-each>");
        // */
        //
        // s.append(" <![CDATA[</rente>]]> ");
        // s.append(" <![CDATA[</prestation>]]> ");
        // s.append(" <![CDATA[</decision>]]>");
        // s.append(" </xsl:template>");

        s.append("   <xsl:template match=\\\"bases_calcul\\\">");
        s.append("	  <xsl:if test=\\\"genre = 'decision'\\\">");
        s.append("		<![CDATA[<bases_calcul>]]>");
        s.append("			<![CDATA[<id>]]><xsl:value-of select=\\\"id\\\"/><![CDATA[</id>]]>");
        s.append(
                "			<![CDATA[<generateur>]]><xsl:value-of select=\\\"generateur\\\"/><![CDATA[</generateur>]]>");

        s.append("			<![CDATA[<anticipation>]]>");
        s.append("          <![CDATA[<tranche>]]>");
        s.append(
                "				<![CDATA[<taux_reduction_anticipation>]]><xsl:value-of select=\\\"anticipation/tranche/taux_reduction_anticipation\\\"/><![CDATA[</taux_reduction_anticipation>]]>");
        s.append("          <![CDATA[</tranche>]]>         ");
        s.append("			<![CDATA[</anticipation>]]>			");

        s.append("			<![CDATA[<base_ram>]]>");
        s.append("				<![CDATA[<bte>]]>");
        s.append(
                "					<![CDATA[<an1>]]><xsl:value-of select=\\\"base_ram/bte/an1\\\"/><![CDATA[</an1>]]>");
        s.append(
                "					<![CDATA[<an2>]]><xsl:value-of select=\\\"base_ram/bte/an2\\\"/><![CDATA[</an2>]]>");
        s.append(
                "					<![CDATA[<an4>]]><xsl:value-of select=\\\"base_ram/bte/an4\\\"/><![CDATA[</an4>]]>");
        s.append("				<![CDATA[</bte>]]>");
        s.append("			<![CDATA[</base_ram>]]>");
        s.append("       <![CDATA[<decision>]]>");
        s.append("          <![CDATA[<prestation>]]>            ");
        s.append(
                "               <![CDATA[<beneficiaire>]]><xsl:value-of select=\\\"decision/prestation/beneficiaire\\\"/><![CDATA[</beneficiaire>]]>             ");
        s.append("              <![CDATA[<rente>]]>");
        // s.append(
        // " <![CDATA[<bases>]]><xsl:value-of select=\\\"prestation/rente/bases\\\"/><![CDATA[</bases>]]>");
        s.append(
                "                   <![CDATA[<debut_droit>]]><xsl:value-of select=\\\"decision/prestation/rente/debut_droit\\\"/><![CDATA[</debut_droit>]]>");
        s.append(
                "                   <![CDATA[<genre>]]><xsl:value-of select=\\\"decision/prestation/rente/genre\\\"/><![CDATA[</genre>]]>");
        s.append(
                "                   <![CDATA[<remarques>]]><xsl:value-of select=\\\"decision/prestation/rente/remarques\\\"/><![CDATA[</remarques>]]>");

        /*
         * s.append("           <xsl:for-each select=\\\"prestation/rente/remarques\\\">" );s.append(
         * "                    <![CDATA[<remarques>]]><xsl:value-of select=\\\".\\\"/><![CDATA[</remarques>]]>" );
         * s.append("           </xsl:for-each>");
         */

        s.append("              <![CDATA[</rente>]]>        ");
        s.append("          <![CDATA[</prestation>]]>       ");
        s.append("      <![CDATA[</decision>]]>");
        s.append("	<![CDATA[</bases_calcul>]]>");
        s.append("	  </xsl:if>");
        s.append("   </xsl:template>");
        s.append("</xsl:stylesheet>");
        return s.toString();
    }

    public boolean isFileContent() {
        return isFileContent;
    }

    /**
     * charge correctement une instance d'une demande de rente et la renvoie
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public REDemandeRente loadDemandeRente(BITransaction transaction) throws Exception {

        return REDemandeRente.loadDemandeRente(getSession(), transaction, idDemandeRente, csTypeDemandeRente);

    }

    public void setCsTypeDemandeRente(String csTypeDemande) {
        csTypeDemandeRente = csTypeDemande;
    }

    public void setIdDemandeRente(String idDemande) {
        idDemandeRente = idDemande;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setIsFileContent(boolean elm) {
        isFileContent = elm;
    }

}
