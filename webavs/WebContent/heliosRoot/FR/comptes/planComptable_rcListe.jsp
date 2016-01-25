
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*"%>
<%
    CGPlanComptableListViewBean viewBean = (CGPlanComptableListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.comptes.planComptable.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
  
<Th width="16">&nbsp;</Th>

<Th width="">Numéro</Th>
<Th width="">Libellé</Th>
<Th width="">Genre</Th>
<Th width="">Domaine</Th>
<Th width="">Solde de l'exercice</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
<%
	CGPlanComptableViewBean entity = (CGPlanComptableViewBean)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdCompte()+"'";
	String tmp = detailLink+entity.getIdCompte();
%>
      
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-planComptable" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
		<ct:menuParam key="selectedId" value="<%=entity.getIdCompte()%>"/>  
	 </ct:menuPopup>
     </TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdExterne()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="">
     <% if (entity.hasCGRemarque()) { %>
	 	<img title="<%=entity.getRemarque()%>" src="<%=request.getContextPath()%>/images/attach.png" style="float:right">
	 <% } %>
     <%=entity.getLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getGenreLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getDomaineLibelle()%>&nbsp;</TD>
     <TD align="right" class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getSoldeExercice(i), true, true, false, 2)%>&nbsp;</TD>

 
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>