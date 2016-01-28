<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.couverture.CECouvertureEcranListViewBean"%>
<%@page import="globaz.hercule.db.couverture.CECouvertureEcran"%>

<%
	detailLink = "hercule?userAction=hercule.couverture.couvertureEcran.afficher&selectedId=";
	CECouvertureEcranListViewBean viewBean = (CECouvertureEcranListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<TH nowrap><ct:FWLabel key="NUMERO_AFFILIE"/></TH>
	<TH nowrap><ct:FWLabel key="AFFILLIE_COUVERTURE"/></TH>
	<TH width=><ct:FWLabel key="ANNEE_COUVERTURE"/></TH>
	<TH width=><ct:FWLabel key="COUVERTURES"/></TH>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%
		CECouvertureEcran line = (CECouvertureEcran) viewBean.getEntity(i);
	 	actionDetail = targetLocation + "='" + detailLink + line.getIdCouverture()+"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE +"=" + line.getIdTiers() + "'";
	 	String actif = "";
	  	if(line.isCouvertureActive().booleanValue()) {
			actif = "<img title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
		}else {
			actif = "<img title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
		}
	%>

	<TD align="center" class="mtd" onClick="<%=actionDetail%>" ><%=line.getNumAffilie()%></TD>
	<TD align="left" class="mtd" onClick="<%=actionDetail%>" ><%=line.getNom()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" ><%=line.getAnnee()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" >&nbsp;<%=actif%>&nbsp;</TD>
	
<%-- /tpl:put --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>