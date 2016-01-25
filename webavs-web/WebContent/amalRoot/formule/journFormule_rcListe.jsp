<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.jade.client.util.JadeUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- <%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %> --%>
<%
	globaz.amal.vb.formule.AMJournFormuleListViewBean viewBean = (globaz.amal.vb.formule.AMJournFormuleListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<TH><ai:AIProperty key="FORMULE_JOURN_DATE_IMPORTATION"/>Date d'importation</th>
    	<TH><ai:AIProperty key="UTILISATEUR"/>Utilisateur</th>
    	<TH><ai:AIProperty key="FORMULE_JOURN_FICHIER"/>Libelle importation</th>
    	 <%--<TH><ai:AIProperty key="LANGUE"/></th>
    	<TH><ai:AIProperty key="STATUT"/></th> --%>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%			globaz.amal.vb.formule.AMJournFormuleViewBean line = (globaz.amal.vb.formule.AMJournFormuleViewBean)viewBean.getEntity(i);

%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		
	 <TD id="<%="simpleTd_"+i%>" class="mtd"><%=line.getHistoriqueImportation().getSpyFormate()%></TD>
	 <TD class="mtd"><%=line.getHistoriqueImportation().getSimpleJournalisation().getUser()%></TD>
	 <TD class="mtd"><%=line.getHistoriqueImportation().getSimpleJournalisation().getLibelle()%></TD>
	 <%--	<TD class="mtd"><%="".equals(line.getLibelle())?"&nbsp;": line.getLibelle()%></TD>
		<TD class="mtd"><%="".equals(line.getCsLangue())?"&nbsp;": line.getSession().getCodeLibelle(line.getCsLangue())%></TD>
		<TD class="mtd"><%="".equals(line.getCsStatut())?"&nbsp;": line.getSession().getCodeLibelle(line.getCsStatut())%></TD>--%>
		<script language="JavaScript">
			document.getElementById('simpleTd_<%=i%>').parentNode.style.cursor='none';
		</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="idFormule" value="viewBean.getIdFormule()" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="csProvenance" value="globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE" menuId="amal-optionsformules"/>
<%}%>
	<%-- /tpl:insert --%>