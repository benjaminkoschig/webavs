<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*" %>
<%
    CGMandatListViewBean viewBean = (CGMandatListViewBean)request.getAttribute ("viewBean");
    session.setAttribute("listViewBean",viewBean);
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.comptes.mandat.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
<%if ((request.getParameter("colonneSelection")!=null)&&(request.getParameter("colonneSelection").equals("yes"))) {%>
    <th  nowrap width="35">&nbsp;</th>
<%}%>
  
<Th width="16">&nbsp;</Th>

<Th width="">Nummer</Th>
<Th width="">Name</Th>
<Th width="">Gesperrt</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 


  <%
    actionDetail = "parent.location.href='"+detailLink+viewBean.getIdMandat(i)+"'";
  %>
     <TD class="mtd" >
     <ct:menuPopup menu="CG-mandat" label="<%=optionsPopupLabel%>" target="top.fr_main">
	 	<ct:menuParam key="selectedId" value="<%=viewBean.getIdMandat(i)%>"/>
	 	<ct:menuParam key="forIdMandat" value="<%=viewBean.getIdMandat(i)%>"/>  
	 </ct:menuPopup>
     </TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=(viewBean.getIdMandat(i).equals(""))?"&nbsp;":viewBean.getIdMandat(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" ><%=(viewBean.getDescription(i).equals(""))?"&nbsp;":viewBean.getDescription(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" align="center" ><%if(viewBean.isEstVerrouille(i).booleanValue()) { %><IMG src="<%=request.getContextPath()%>/images/cadenas.gif" border="0"><% }else {%>&nbsp;<%}%></TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>