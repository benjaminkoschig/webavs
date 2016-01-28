
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.comptes.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGLibelleStandardListViewBean viewBean = (CGLibelleStandardListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.comptes.libelleStandard.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
  
<Th width="16">&nbsp;</Th>

<Th width="">Akronym</Th>
<Th width="">Bezeichnung FR</Th>
<Th width="">Bezeichnung DE</Th>
<Th width="">Bezeichnung IT</Th>

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%

	CGLibelleStandardViewBean entity = (CGLibelleStandardViewBean)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdLibelleStandard() + "&idMandat=" + entity.getIdMandat() + "'";
	String tmp = detailLink+entity.getIdLibelleStandard() + "&idMandat=" + entity.getIdMandat();
%>   
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>

	  <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdLibelleStandard()%>&nbsp;</TD>     
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getLibelleFR().equals(""))?"&nbsp;":entity.getLibelleFR()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getLibelleDE().equals(""))?"&nbsp;":entity.getLibelleDE()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=(entity.getLibelleIT().equals(""))?"&nbsp;":entity.getLibelleIT()%>&nbsp;</TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>