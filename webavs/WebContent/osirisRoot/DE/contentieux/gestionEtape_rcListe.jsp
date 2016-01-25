 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<% 
globaz.osiris.db.contentieux.CAParametreEtapeManagerListViewBean viewBean = (globaz.osiris.db.contentieux.CAParametreEtapeManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.contentieux.CAParametreEtape parametreEtape = null; 
size = viewBean.size();
detailLink ="osiris?userAction=osiris.contentieux.gestionEtape.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2">Laufende Nummer</TH>
    <TH nowrap width="710">Etappe</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	parametreEtape = (globaz.osiris.db.contentieux.CAParametreEtape) viewBean.getEntity(i); 
		actionDetail = "parent.location.href='"+detailLink+parametreEtape.getIdParametreEtape()+"'";
    %>
<!--    <TD width="19"><A href="<%=request.getContextPath()%>/osiris?userAction=osiris.contentieux.gestionEtape.afficher&id=<%=parametreEtape.getIdParametreEtape()%>" target="fr_main"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></A></TD>-->
    <TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+parametreEtape.getIdParametreEtape())%>"/>	        
	</TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="276"><%=parametreEtape.getSequence()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap width="710"><%=parametreEtape.getEtape().getDescription()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>