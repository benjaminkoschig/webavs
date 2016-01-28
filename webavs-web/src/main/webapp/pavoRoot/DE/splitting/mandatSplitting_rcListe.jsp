<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*"%>
<%
String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
    globaz.pavo.db.splitting.CIMandatSplittingListViewBean viewBean = (globaz.pavo.db.splitting.CIMandatSplittingListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pavo?userAction=pavo.splitting.mandatSplitting.afficher&selectedId=";
    if(objSession.hasRight(userActionUpd, "UPDATE")) {
		menuName = "mandatSplitting-detail";
	} else {
		menuName = "mandatSplNoRight-detail";
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
<Th width="16">&nbsp;</Th>

<Th width="">Splittingart</Th>
<Th width="">Beginnjahr</Th>
<Th width="">Endjahr</Th>
<Th width="">Status</Th>    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIMandatSplitting line = (CIMandatSplitting)viewBean.getEntity(i); %>
     <TD class="mtd" width="">
		<ct:menuPopup menu="mandatSplNoRight-detail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="idMandatSplitting" value="<%=line.getIdMandatSplitting()%>"/>
     	</ct:menuPopup></TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdMandatSplitting()+"'"; %>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdGenreSplitting(),session)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getAnneeDebut()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getAnneeFin()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdEtat(),session)%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>