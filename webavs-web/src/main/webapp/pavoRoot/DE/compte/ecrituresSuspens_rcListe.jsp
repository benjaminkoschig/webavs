 
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
  	<Th width="16">SVN</Th>
  	<Th width="16">Namensangaben</Th>
  	<Th width="">Geburtsdatum</Th>
  	<Th width="">Geschlecht</Th>
  	<Th width="">Heimatstaat</Th>
	<Th width="">Abr.-Nr.</Th>
    <Th width="">SZ</Th>
    <Th width="">Periode</Th>
    <Th width="">Betrag</Th>
    <Th width="">Code</Th>
    <!--Th width="">BTA</Th>
<Th width="">Spécial</Th-->
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