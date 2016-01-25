<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.tauxAssurance.tauxMoyen.afficher&selectedId=";
	globaz.naos.db.tauxAssurance.AFTauxMoyenListViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxMoyenListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	<TH width="30">&nbsp;</TH>
	<TH align="center">Jahr</TH>
	<TH align="right">Lohnsumme</TH>
	<TH align="right">Fakturierte Monate</TH>
	<TH align="right">Durchschnittlicher Beitragsansatz (%)</TH>
	<TH align="center">Blockierung</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getTauxMoyenId(i)+"'";
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getTauxMoyenId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=viewBean.getAnnee(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMasseAnnuelle(i),true,true,false,2)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getNbrMois(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getTauxTotal(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center"><IMG src="<%=request.getContextPath()%><%=(viewBean.getBlocage(i).booleanValue())?"/images/cadenas.gif" : "/images/cadenas_ouvert.gif"%>"></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>