<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "hercule?userAction=hercule.reviseur.reviseur.afficher&selectedId=";
	globaz.hercule.db.reviseur.CEReviseurListViewBean viewBean = (globaz.hercule.db.reviseur.CEReviseurListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH width="40"><ct:FWLabel key="REVISEUR"/></TH>
	<TH width="150"><ct:FWLabel key="VISA"/></TH>
	<TH><ct:FWLabel key="DESCRIPTION"/></TH>
	<TH><ct:FWLabel key="TYPE_REVISEUR"/></TH>
	<TH width="30"><ct:FWLabel key="ACTIF"/></TH>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getIdReviseur(i)+"'";
	
	    String actif = "";
	    if(viewBean.isReviseurActif(i).booleanValue()) {
	    	actif = "<img title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
	    }else {
	    	actif = "<img title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
	    }
	    
	%>
	<TD class="mtd" width="16" >
		<ct:menuPopup menu="CE-OptionsDefaut" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getIdReviseur(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="40"><%=viewBean.getIdReviseur(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150"><%=viewBean.getVisa(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getNomReviseur(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=viewBean.getTypeReviseur(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=actif%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>