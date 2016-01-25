 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
targetLocation = "location.href";
globaz.hermes.db.gestion.HEInputAnnonceViewBean viewBean = (globaz.hermes.db.gestion.HEInputAnnonceViewBean)session.getAttribute("viewBean");
size = viewBean.critereSize();

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th width=""> 
      <div align="left">Critère</div>
    </Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
globaz.hermes.db.parametrage.HEMotifcodeapplication line = viewBean.getHEMotifcodeapplication(i);	
	
detailLink = "hermes?userAction=hermes.gestion.inputAnnonce.afficher&_method=add&motif="+request.getParameter("motif")+"&critere="+ line.getIdCritereMotif();

String numeroAvs = request.getParameter("numeroAvs");
String referenceExterne = request.getParameter("referenceExterne");
if(numeroAvs!=null && referenceExterne!=null && numeroAvs.trim().length()!=0 && referenceExterne.trim().length()!=0){
	detailLink += "&numeroAvs=" + numeroAvs + "&referenceExterne=" + referenceExterne;
}


actionDetail = targetLocation + "='" + detailLink  + "'";
%>
    <TR class="<%=rowStyle%>" 
        onMouseOver="this.style.background=finds('.rowHighligthed').style.background;this.style.color=finds('.rowHighligthed').style.color;" 
        onMouseOut=" this.style.background=finds('.<%=rowStyle%>').style.background;this.style.color=finds('.<%=rowStyle%>').style.color;"> 
      <TD class="mtd" onClick="<%=actionDetail%>"><%= (	line.getCritereLibelle().equals(""))?"&nbsp;":line.getCritereLibelle()%>&nbsp;</TD>
    </TR>
    <%
if(size==1){%>
	<script language="JavaScript">
		<% String jumpTo = "this.location.href"+ "='" + detailLink  + "'";%>
		<%=jumpTo%>
	</script>
<%	
}
%>	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>