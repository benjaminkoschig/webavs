<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	detailLink = "naos?userAction=naos.controleEmployeur.controleEmployeur.afficher&selectedId=";
	globaz.naos.db.controleEmployeur.AFControleEmployeurListViewBean viewBean = (globaz.naos.db.controleEmployeur.AFControleEmployeurListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	
	String idAff;
	boolean fromDraco = false;
	idAff = request.getParameter("idAFF");	
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<TH width="30">&nbsp;</TH>
	<TH nowrap width="200">Mitglied</TH>
	<TH width="150">Kontrollart</TH>
	<TH width="100">Effektives Datum</TH>
	<TH width="100">Vorgesehenes Datum</TH>
	<TH width="100">Beginn Kontrollperiode</TH>
	<TH width="100">Ende Kontrollperiode</TH>
	<TH width="70">Kontrolleur</TH>
	<TH width="70">Berichtnummer</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getControleEmployeurId(i)+"'";
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getControleEmployeurId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="200"><%=viewBean.getDescription(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenreControle(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDateEffective(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDatePrevue(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDateDebutControle(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDateFinControle(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="70"><%=viewBean.getControleurVisa(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="70"><%=viewBean.getNouveauNumRapport(i)%></TD>

	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>	
	<%if(viewBean.getProchainControle().length() > 0){ %>
	<table class="find" cellspacing="0">
		<tr class="somme">
			<td>&nbsp;Prochain contrôle
			&nbsp;&nbsp;&nbsp;<%=viewBean.getProchainControle()%>
			</td>
		</tr>
	</table>
	<%}%>	
	<script>	
	<%
	if (!JadeStringUtil.isBlank(idAff)) fromDraco = true;
	if (size==1 && !fromDraco) { %>
		parent.location.href = "<%=(servletContext + mainServletPath)%>?userAction=naos.controleEmployeur.controleEmployeur.afficher&selectedId=<%=viewBean.getControleEmployeurId(0)%>";
	<% } else if (size==0 && !request.getParameter("forAnnee").equals(null)) { %>
		parent.location.href = "<%=(servletContext + mainServletPath)%>?userAction=naos.controleEmployeur.controleEmployeur.afficher&_method=add&dateDebutControle=01.01.<%=request.getParameter("forAnnee")%>&numAffilie=<%=request.getParameter("forNumAffilie")%>";
	<% }%>
	</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>