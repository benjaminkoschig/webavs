<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.pavo.db.compte.CIEcrituresNonRAListViewBean viewBean = (globaz.pavo.db.compte.CIEcrituresNonRAListViewBean)request.getAttribute("viewBean");

	size = viewBean.getSize();
	
	detailLink = "pavo?userAction=pavo.compte.ecrituresNonRA.afficher&selectedId=";
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>NSS</TH>
    <TH>Nom, prénom</TH>
    <TH>Date de naissance</TH>
    <TH>Sexe</TH>
    <TH>Pays</TH>
    <TH>Demander un certificat</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.pavo.db.compte.CIEcriture line = (globaz.pavo.db.compte.CIEcriture)viewBean.getEntity(i);
actionDetail = targetLocation + "='" +detailLink+line.getEcritureId()+"'";
%>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getNssFormate())?"&nbsp;":line.getNssFormate()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getNomPrenom())?"&nbsp;":line.getNomPrenom()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDateDeNaissance())?"&nbsp;":line.getDateDeNaissance()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getSexeCode())?"&nbsp;":line.getSexeCode()%></TD>
<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getPaysCode())?"&nbsp;":line.getPaysCode()%></TD>

<%if(line.isCertificat().booleanValue()){%>
<TD>OUI</TD>
<%}else{%>
<TD>NON</TD>
<%}%>                                                                                        
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>