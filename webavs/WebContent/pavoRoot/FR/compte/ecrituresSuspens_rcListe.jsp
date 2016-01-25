 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*,globaz.pavo.translation.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.ecrituresSuspens.afficher&selectedId=";
    CIEcrituresSuspensListViewBean viewBean = (CIEcrituresSuspensListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
  	<Th width="16">NSS</Th>
  	<Th width="16">Nom</Th>
  	<Th width="">Date de naissance</Th>
  	<Th width="">Sexe</Th>
  	<Th width="">Pays</Th>
	<Th width="">Affili� ou partenaire</Th>
    <Th width="">Genre</Th>
    <Th width="">P�riode</Th>
    <Th width="">Montant</Th>
    <Th width="">Code</Th>
    <!--Th width="">BTA</Th>
<Th width="">Sp�cial</Th-->
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CIEcriture line = (CIEcriture)viewBean.getEntity(i); %>
    <% actionDetail = targetLocation+"='"+detailLink+line.getEcritureId()+"'"; %>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" ><%=line.getNssFormate()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" ><%=line.getNomPrenom()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" ><%=line.getDateNaissance()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="" align = "center" ><%=line.getSexeCode()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="" ><%=line.getPaysCode()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" ><%=line.getNoNomEmployeur()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="left"><%=line.getGreFormat()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap ><%=line.getMoisDebutPad()+"-"+line.getMoisFinPad()+"."+line.getAnnee()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="right" nowrap><%=line.getMontantSigne()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="" align="center"><%=CodeSystem.getCodeUtilisateur(line.getCode(),session)%>&nbsp;</TD>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>