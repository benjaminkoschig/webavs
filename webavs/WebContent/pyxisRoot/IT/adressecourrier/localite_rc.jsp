<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0033";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
 bFind = false;

usrAction = "pyxis.adressecourrier.localite.lister";
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
				<%-- tpl:put name="zoneTitle" --%>Ricerca di una località
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<TR>
            				<TD nowrap>Ricerca a partire da&nbsp;</TD>
            				<TD nowrap colspan="2" >
            					<%
            						String rech = (request.getParameter("_pos")==null)?"":request.getParameter("_pos");
            					%>
						        <INPUT type="text" name="fromLocalite"  class="libelleLong" value="<%=globaz.globall.util.JAUtil.replaceString(rech,"\"","&quot;") %>">
								<INPUT type="hidden" name="_meth" value="<%=request.getParameter("_meth")%>">
								<input type="hidden" name="colonneSelection" value="<%= request.getParameter("colonneSelection")%>">		
							</TD>
							<td width="100%">&nbsp;</td>							
          				</TR>
						<TR>
            				<TD nowrap>Cantone</TD>
            				<TD nowrap colspan="2" >
            					<ct:FWCodeSelectTag name="forIdCanton" codeType="PYCANTON" defaut="" wantBlank="true"/>
            				</TD>						
          				</TR>
      					<tr>
							<td nowrap>Codice centrale&nbsp;</td>
					        <td><INPUT type="text" name="forIdPays" size="3" maxlength="3" value=''></td>
      						<td>Inclure les localités inactives</td>
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