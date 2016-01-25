<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.corvus.db.annonces.REAnnonceInscriptionCI"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<%
// Les labels de cette page commence par le préfix "JSP_ICI_L"

globaz.corvus.vb.ci.REInscriptionCIListViewBean viewBean =
	(globaz.corvus.vb.ci.REInscriptionCIListViewBean) request.getAttribute("viewBean");

size = viewBean.getSize();

detailLink = "corvus?userAction=" + globaz.corvus.servlet.IREActions.ACTION_INSCRIPTION_CI + ".afficher&selectedId=";
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Iterator"%>
<style>
.mtdCITraite {
    font-family : Verdana,Arial;
    font-style : italic;
    border-right: solid 1px silver;
    padding : 3 10;
    color: #F00;
}
</style>
<SCRIPT language="JavaScript">
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="JSP_ICI_L_CAISSE" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_PERIODE" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_BRANCHE_ECONOMIQUE" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_GENRE" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_REVENU" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_IRREC" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_CI_ADD" /></TH>		
		<TH><ct:FWLabel key="JSP_ICI_L_NO_AFFILIE" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_ATTENTE_CI_ADD" /></TH>		
		<TH><ct:FWLabel key="JSP_ICI_L_PV" /></TH>
		<TH><ct:FWLabel key="JSP_ICI_L_NO" /></TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%globaz.corvus.vb.ci.REInscriptionCIViewBean courant = (globaz.corvus.vb.ci.REInscriptionCIViewBean) viewBean.get(i);

		String detailUrl = "parent.location.href='" + detailLink + courant.getIdInscription()+ "&idTiers=" + courant.getIdTiers() + "'";        
		
		String rowClass = rowClass = "mtd";
		boolean comptabiliserMontant = true;
		String labelAttenteCI = "";
		if(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE.equals(courant.getAttenteCIAdditionnelCS())){
			rowClass = "mtdCITraite";
			labelAttenteCI = viewBean.getLabelTraite();
			comptabiliserMontant = false;
		}
		else if(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_PROVISOIRE.equals(courant.getAttenteCIAdditionnelCS())){
			labelAttenteCI = viewBean.getLabelProvisoire();
		}
        %>
		<TD class="mtd" nowrap>
			<ct:menuPopup menu="corvus-optionsinscriptionci" detailLabelId="MENU_OPTION_DETAIL"
			detailLink="<%=detailLink + courant.getIdInscription()%>">
				<ct:menuParam key="selectedId" value="<%=courant.getIdInscription()%>" />
				<ct:menuParam key="idTiers" value="<%=courant.getIdTiers()%>" />
			</ct:menuPopup>
		</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getNumeroCaisse()+ "."
	    	+ globaz.jade.client.util.JadeStringUtil.fillWithZeroes(courant.getNumeroAgence(),3)%>&nbsp;
	    </TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getMoisDebutCotisations()
			+ "-" + courant.getMoisFinCotisations() + " " + courant.getAnneeCotisations()%>&nbsp;
		</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getBrancheEconomiqueCode()%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>" align="center"><%=courant.getGenre()%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>" align="right"><%=(JadeStringUtil.isIntegerEmpty(courant.getCodeExtourne()))? new FWCurrency(courant.getRevenu()).toStringFormat():"-"+ new FWCurrency(courant.getRevenu()).toStringFormat()%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getCodeADS()%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>" align="center">
			<%if(courant.isCiAdditionnel()){%><IMG src="<%=request.getContextPath()+courant.getIsCiAdditionnelImage()%>" alt=""><%}%>&nbsp;
		</TD>
		<TD class="<%=rowClass%>" align="right" nowrap onClick="<%=detailUrl%>"><%=courant.getNoAffilie()%>&nbsp;</TD>

		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=labelAttenteCI%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getProvenance()%>&nbsp;</TD>
		<TD class="<%=rowClass%>" nowrap onClick="<%=detailUrl%>"><%=courant.getIdInscription()%>&nbsp;</TD>
		<%
		// On additionne le montant seulement si ce n'est pas un CI qui était en attente de CI addtionnel et qu'il l'a reçu
		if(comptabiliserMontant){
			if(JadeStringUtil.isIntegerEmpty(courant.getCodeExtourne())){
				viewBean.addMontantToCaisse(new globaz.framework.util.FWCurrency(courant.getRevenu()), courant.getNumeroCaisse()+"."+courant.getNumeroAgence());
			}
			else{
				viewBean.addMontantToCaisse(new globaz.framework.util.FWCurrency("-"+courant.getRevenu()) , courant.getNumeroCaisse()+"."+courant.getNumeroAgence());
			}
		}
		%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

			<%Iterator iter = viewBean.getCaisseKeyIterator();
			  boolean first = true;
			  while(iter.hasNext()){
				  String caisse =(String)iter.next();
			%>
				<%if(first){%>
					<TR>
						<TD colspan="5" align="left" style="font-weight:bold;font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_ICI_L_SOUS_TOTAL_CAISSE"/><%=" "+caisse%></TD>
						<TD class="mtd" nowrap align="right" style="font-weight:bold;font-style: italic; background-color: #dddddd;border-top-style: solid; border-top-color: black;"><%=viewBean.getMontantTotalCaisse(caisse).toStringFormat()%></TD>
						<TD colspan="5" align="right" style="background-color: #dddddd;"></TD>
					</TR>
				<%	first=false; 
				}else{%>
					<TR>
						<TD colspan="5" align="left" style="font-weight:bold;font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_ICI_L_SOUS_TOTAL_CAISSE"/><%=" "+caisse%></TD>
						<TD class="mtd" nowrap align="right" style="font-weight:bold;font-style: italic; background-color: #dddddd;"><%=viewBean.getMontantTotalCaisse(caisse).toStringFormat()%></TD>
						<TD colspan="5" align="right" style="background-color: #dddddd;"></TD>
					</TR>
				<%}%>
			<%}%>
			<TR>
				<TD colspan="5" align="left" style="font-weight:bold;font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_ICI_L_REVENU_TOTAL"/></TD>
				<TD class="mtd" nowrap align="right" style="font-weight:bold;font-style: italic; background-color: #dddddd; border-top-style: double; border-top-color: black;"><%=viewBean.getTotal().toStringFormat()%></TD>
				<TD colspan="5" align="right" style="background-color: #dddddd; border-style: none;"></TD>
			</TR>
			<SCRIPT language="JavaScript">
				parent.document.forms[0].elements('montantTotal').value="<%=viewBean.getTotal().toStringFormat()%>";
			</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>