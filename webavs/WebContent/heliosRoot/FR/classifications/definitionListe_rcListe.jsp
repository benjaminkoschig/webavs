<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.classifications.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGDefinitionListeListViewBean viewBean = (CGDefinitionListeListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.classifications.definitionListe.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
  
<Th width="16">&nbsp;</Th>

<Th width="">Numéro</Th>
<Th width="">Libellé</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdDefinitionListe(i)+"'";
	String tmp = detailLink+viewBean.getIdDefinitionListe(i);
%>      
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>     
	 </TD>
     
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(viewBean.getIdDefinitionListe(i).equals(""))?"&nbsp;":viewBean.getIdDefinitionListe(i)%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(viewBean.getLibelle(i).equals(""))?"&nbsp;":viewBean.getLibelle(i)%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>