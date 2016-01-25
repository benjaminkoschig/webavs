
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import ="globaz.helios.db.comptes.*"%>
<%
	CGExerciceComptableListViewBean viewBean = (CGExerciceComptableListViewBean)request.getAttribute ("viewBean");
	size =viewBean.getSize();
	detailLink ="helios?userAction=helios.comptes.exerciceComptable.afficher&selectedId=";
	String chooseLink ="helios?userAction=helios.comptes.exerciceComptable.choisirExercice&selectedId=";
	wantPagination = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
  
<Th width="16">&nbsp;</Th>

<Th width="">Nummer</Th>
<Th width="">Rechnungsjahr</Th>
<Th width="">Mandant</Th>
<Th width="">Abgeschlossen</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <%
	 	actionDetail = "parent.location.href='"+chooseLink+viewBean.getIdExerciceComptable(i)+"'";
	 	String tmp = detailLink + viewBean.getIdExerciceComptable(i);
	 %>
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-exerciceComptable" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
	 	<ct:menuParam key="selectedId" value="<%=viewBean.getIdExerciceComptable(i)%>"/>  
	 </ct:menuPopup>
     </TD>
     
     <TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=(viewBean.getIdExerciceComptable(i).equals(""))?"&nbsp;":viewBean.getIdExerciceComptable(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(viewBean.getExerciceComptable(i).equals(""))?"&nbsp;":viewBean.getExerciceComptable(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(viewBean.getMandatLibelle(i).equals(""))?"&nbsp;":viewBean.getMandatLibelle(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center" ><%if(viewBean.isEstCloture(i).booleanValue()) { %><IMG src="<%=request.getContextPath()%>/images/cadenas.gif" border="0"><% }else {%>&nbsp;<%}%></TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>