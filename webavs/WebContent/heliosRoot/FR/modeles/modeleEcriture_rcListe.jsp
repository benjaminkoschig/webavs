
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import ="globaz.helios.db.modeles.*"%>
<%
	CGModeleEcritureListViewBean viewBean = (CGModeleEcritureListViewBean)request.getAttribute ("viewBean");
	size =viewBean.getSize();
	detailLink ="helios?userAction=helios.modeles.modeleEcriture.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
  
<Th width="10%" colspan="2">Numéro</Th>
<Th width="60%">Libellé</Th>
<Th width="15%">Pièce</Th>

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <%
     	CGModeleEcriture entity = (CGModeleEcriture)viewBean.getEntity(i);
	 	actionDetail = "parent.location.href='"+detailLink+entity.getIdModeleEcriture()+"'";
	 %>
     <TD>
     	<ct:menuPopup menu="CG-modele" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=entity.getIdModeleEcriture()%>"/>  
		</ct:menuPopup>
     </TD>
     
     <TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=entity.getIdModeleEcriture()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" align="right"><%=entity.getPiece()==null?"":entity.getPiece()%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>