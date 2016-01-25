
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran="CCP4005";
	subTableHeight = 50;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
<%
globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean viewBean = (globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean)session.getAttribute ("viewBean");							
%>
usrAction = "phenix.principale.lienTypeDecRemarque.lister";
detailLink = servlet+"?userAction=phenix.principale.lienTypeDecRemarque.afficher&_method=add";
top.document.title = "Association remarques / types de décision";
<%
bButtonNew = false;
bButtonFind = false;
%>
bFind=true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				Association remarques types / type de décision
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
        <TR>
        	 <TD >
			<textarea rows="3" cols="100" style="overflow : hidden;background-color : #b3c4db;border: 0" readonly><%=viewBean.getTexteRemarqueType()%></textarea>
		</TD>
        </TR>
        <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>