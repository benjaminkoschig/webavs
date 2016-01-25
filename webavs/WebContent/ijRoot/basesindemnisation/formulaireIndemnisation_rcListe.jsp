<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_FIN_L'
--%>
<%
	globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationListViewBean viewBean = (globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationListViewBean) request.getAttribute("viewBean");
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
	menuName = globaz.ij.menu.IAppMenu.MENU_OPTION_FORMULAIRES;
	size = viewBean.size();
	detailLink = servletContext + mainServletPath + "?userAction="+globaz.ij.servlet.IIJActions.ACTION_FORMULAIRE_INDEMNISATION+".afficher&selectedId=";
%>

<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>    
	<TH><ct:FWLabel key="JSP_FIN_L_DATE_ENVOI"/></TH>
	<TH><ct:FWLabel key="JSP_FIN_L_DATE_RECEPTION"/></TH>
	<TH><ct:FWLabel key="JSP_FIN_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_FIN_L_NOM_AGENT"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean line = (globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean) viewBean.get(i);
    actionDetail = targetLocation + "='" + detailLink + line.getIdFormulaireIndemnisation() + "'";
    %>
	<TD class="mtd">
	<ct:menuPopup menu="ij-formulaires" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdFormulaireIndemnisation()%>">
		<ct:menuParam key="idFormulaire" value="<%=line.getIdFormulaireIndemnisation()%>"/>
		<ct:menuParam key="idPrononce" value="<%=line.getIdPrononce()%>"/>
		<ct:menuParam key="csTypeIJ" value="<%=line.getCsTypeIJ()%>"/>
	</ct:menuPopup>

	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateEnvoi()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateReception()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelleEtat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getNomAgent()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>