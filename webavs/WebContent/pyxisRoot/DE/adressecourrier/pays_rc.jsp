<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0031";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;

%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressecourrier.pays.lister";
bFind = true;
function initCheckBox() {
	var cb = new Array("inclureInactif");
	for (i=0;i<cb.length;i++) {
		if (document.getElementsByName(cb[i])[0].value=="true") {
			document.getElementsByName("C"+cb[i])[0].checked = true;
		} else {
			document.getElementsByName("C"+cb[i])[0].checked = false;
		}
	}
}
function checkInput(el) {
	if(el.checked) {
	document.getElementsByName(el.name.substr(1))[0].value="true"
	} else {
	document.getElementsByName(el.name.substr(1))[0].value="false"
	}
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche eines Lands
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<TR>
            						<TD nowrap >Bezeichnung&nbsp;</TD>
            						<TD nowrap colspan="2" >
										<INPUT type="text" name="fromLibelle" size="35" maxlength="40" value="" >
	     							</TD>
	     							<td width="100%">&nbsp;</td>
	     				</tr>
	     				<tr>	     							
	     							<td nowrap>ZAS Code&nbsp;</td>
	     							<td><INPUT type="text" name="forCodeCentrale" size="3" maxlength="3" maxlength="40" value="" ></td>
          				</TR>
	     				<tr>
	     							<td nowrap>ISO Code&nbsp;</td>
	     							<td><INPUT type="text" name="forCodeIso" size="2" maxlength="2" value="" ></td>
						
									<td>Inaktive Länder einschliessen</td>
      								<td>
		      							<input type="checkbox" name="CinclureInactif" onclick="checkInput(this)">
										<input type="text" style="display:none" name="inclureInactif" value="" >
      								</td>
						</tr>
	     				
	 				
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>


<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>