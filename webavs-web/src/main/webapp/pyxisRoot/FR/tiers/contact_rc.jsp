<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0028";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script>
/*
function setFieldFromRequest() {
	var myHash = {
	<%	/*
		java.util.Enumeration enum = request.getParameterNames();
		while (enum.hasMoreElements()) {
			String  param = (String) enum.nextElement();
				if (request.getParameter(param) != null) {
					pageContext.getOut().print(param+":"+"'"+request.getParameter(param)+"'");	
					if (enum.hasMoreElements()) {
						pageContext.getOut().println(",");
					}
				}
		}*/
	%>
	}

	var elem;
	var tags
	
	// input tag
	tags = document.getElementsByTagName("input");	
	for (var i=0;i<tags.length;i++) {
		elem = tags(i);
		if ((myHash[tags(i).name] != null)&&(myHash[tags(i).name] != 'null')) {
			tags(i).value=myHash[tags(i).name];
		}
	}

	// select tag
	tags = document.getElementsByTagName("select");	
	for (var i=0;i<tags.length;i++) {
    		elem = tags(i);
  		if ((myHash[elem.name] != null)&&(myHash[elem.name] != 'null')) {
			elem.value=myHash[elem.name];
		}
	}
	
}
*/
</script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
bFind = false;
<%
	//globaz.pyxis.db.tiers.TITiersViewBean viewBean = (globaz.pyxis.db.tiers.TITiersViewBean)session.getAttribute("viewBean");
	//actionNew += "&idTiers=" + viewBean.getIdTiers();
%>
usrAction = "pyxis.tiers.contact.lister";


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><span style="font-family:webdings;font-weight:normal"></span>Contacts<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
		<TR>
			<TD nowrap>Veuillez sélectionner un contact (Nom / Prénom)</TD>
		</TR>
		<TR>
			<TD>
				<input maxlength="40" size="40" type="text" name="forNomLike" >
				&nbsp;
				<input maxlength="40" size="40" type="text" name="forPrenomLike" >
			</TD>
		</TR>

	 				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>