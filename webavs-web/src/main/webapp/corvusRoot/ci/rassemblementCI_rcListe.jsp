<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_RCI_L"

	globaz.corvus.vb.ci.RERassemblementCIListViewBean viewBean = (globaz.corvus.vb.ci.RERassemblementCIListViewBean) request.getAttribute("viewBean");
	
	size = viewBean.getSize ();
	
	String IdTiers = request.getParameter("idTiers");
	
	detailLink = "corvus?userAction="+IREActions.ACTION_RASSEMBLEMENT_CI+ ".afficher&selectedId=";
%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<SCRIPT language="JavaScript">


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH><ct:FWLabel key="JSP_RCI_L_MOTIF"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_DATE_CLOTURE"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_DATE_ORDRE"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_REVOCATION"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_CI_ADD"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_DATE_TRAITEMENT"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_NO_ARC"/></TH>
    <TH><ct:FWLabel key="JSP_RCI_L_NO"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
				globaz.corvus.vb.ci.RERassemblementCIViewBean courant = (globaz.corvus.vb.ci.RERassemblementCIViewBean) viewBean.get(i);
				String detailUrl = "parent.location.href='" + detailLink + courant.getIdRCI() + "&idTiers=" + IdTiers + "'";
				String myFinalString = detailLink + courant.getIdRCI() + "&idTiers=" + IdTiers;
			%>
			<TD class="mtd" nowrap>
				<ct:menuPopup menu="corvus-optionsrassemblementci" detailLabelId="MENU_OPTION_DETAIL" 
					detailLink="<%=myFinalString%>">
					<ct:menuParam key="forIdRCI" value="<%=courant.getIdRCI()%>"/>
					<ct:menuParam key="selectedId" value="<%=courant.getIdRCI()%>"/>
					<ct:menuParam key="idTiers" value="<%=IdTiers%>"/>
				</ct:menuPopup>	
			</TD>
			<%if(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(courant.getIdParent())){%>
				<%-- CI add. --%>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(courant.getMotif())?"":courant.getMotif()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getCsEtatLibelle()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getDateCloture()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getDateRassemblement()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getDateRevocation()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>" align="center">
					<IMG src="<%=request.getContextPath()+courant.getIsCiAdditionnelImage()%>" alt="">
					<%=courant.getIsCiAdditionnelTraite().booleanValue()?"(" + courant.getDateTraitement()+")":""%>&nbsp;
				</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getDateTraitement()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic; text-align:right;" onClick="<%=detailUrl%>"><%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(courant.getReferenceUniqueArc())?"":courant.getReferenceUniqueArc()%>&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>"><%=courant.getIdRCI()%>&nbsp;</TD>
			<%} else {%>
				<%-- CI --%>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(courant.getMotif())?"":courant.getMotif()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsEtatLibelle()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateCloture()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateRassemblement()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateRevocation()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>">&nbsp;</TD>
				<TD class="mtd" nowrap style="font-style:italic;" onClick="<%=detailUrl%>">&nbsp;</TD>
				<TD class="mtd" nowrap style="text-align:right;"><%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(courant.getReferenceUniqueArc())?"":courant.getReferenceUniqueArc()%>&nbsp;</TD>
				<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdRCI()%>&nbsp;</TD>
			<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>