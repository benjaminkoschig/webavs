<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    globaz.phenix.db.principale.CPCotisationListViewBean viewBean = (globaz.phenix.db.principale.CPCotisationListViewBean )request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.cotisation.afficher&selectedId=";
    menuName="";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    	<Th nowrap width="16">&nbsp;</Th>
    	<Th width="">Assurance</Th>
   	<Th width="">Période</Th>
	<Th width="">Annuelle</Th>
	<Th width="">Semestrielle</Th>    
	<Th width="">Trimestrielle</Th>    
	<Th width="">Mensuelle</Th>
	<Th width="">Taux</Th>    
	<Th width="">Périodicité</Th>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
		<%
		actionDetail = "parent.location.href='"+detailLink+viewBean.getIdCotisation(i)+"'";
		String tmp = detailLink+viewBean.getIdCotisation(i);
		%>
	    <TD class="mtd" width="">
	     	<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	    </TD>
	    <TD class="mtd" onClick="<%=actionDetail%>" width="20%"><%=viewBean.getLibelleAssurance(i)%></TD>
	    <TD class="mtd" onClick="<%=actionDetail%>" width="20%"><%=viewBean.getDebutCotisation(i)%> - <%=viewBean.getFinCotisation(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getMontantAnnuel(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getMontantSemestriel(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getMontantTrimestriel(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getMontantMensuel(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getTaux(i)%></TD>
	    <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=viewBean.getPeriodicite(i)%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<tr bgcolor="black">
		<td colspan="9" height="0"></td>
</tr>
<TR class="<%=rowStyle%>">
<%
	if ((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes"))) {
%>
   <TD class="mtd" width="15"></TD>
<%	
	}
%>
     <TD class="mtd" width="16" ></TD>
      <TD class="mtd" width="20%" style="font-weight : bold;">Montant total dû</TD>
      <TD class="mtd" width="20%" align="right"></TD>
      <TD class="mtd" width="10%" align="right"><%=viewBean.getTotalAnnuel()%></TD>
      <TD class="mtd" width="10%" align="right"><%=viewBean.getTotalSemestriel()%></TD>
      <TD class="mtd" width="10%" align="right"><%=viewBean.getTotalTrimestriel()%></TD>
      <TD class="mtd" width="10%" align="right"><%=viewBean.getTotalMensuel()%></TD>
      <TD class="mtd" width="10%" align="right"></TD>
      <TD class="mtd" width="10%" align="right"></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>