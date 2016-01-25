<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0034";
	globaz.pyxis.db.adressecourrier.TILocaliteViewBean viewBean = (globaz.pyxis.db.adressecourrier.TILocaliteViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

%>

<SCRIPT language="JavaScript">

</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
top.document.title = "Tiers - Localité Détail"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.localite.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.localite.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.localite.modifier";
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.localite.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.localite.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une localité (étrangère)
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
					<tr >
						<TD width="100">Localité</TD>
						<td><INPUT name="localite" type="text" value="<%=viewBean.getLocalite()%>" class="libelleLong"></td>
					</tr>
					<tr>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
					<tr>
						<TD>Libellé court</TD>
						<td><INPUT name="localiteCourt" type="text" value="<%=viewBean.getLocaliteCourt()%>" size="18" maxlength="18"></td>
					</tr>
					<tr>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
					<tr>
						<TD>Numéro postal</TD>
						<td><INPUT name="numPostal" type="text" value="<%=viewBean.getNumPostal()%>" class="numero"></td>
					</tr>
					<tr>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
					<tr>
						<TD>Canton</TD>
						<td><INPUT name="numPostal" type="text" value='<ct:FWCodeLibelle csCode="<%=viewBean.getIdCanton() %>"/>' class="numero"></td>
					</tr>
					<tr>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
					<tr>
						<TD>Province</TD>
						<TD><INPUT name="province" type="text" value="<%=viewBean.getProvince()%>" class="libelleLong"></TD>
					</tr>
					<tr>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
					<tr>
						<TD>Pays</TD>
						<TD>
							<ct:FWListSelectTag name="idPays" 
							defaut="<%=viewBean.getIdPays()%>"
							data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
							<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
						</TD>
					</tr>
					 <tr>
        	<td>Inactiver cette localité</td>
        	<td>
        		<input  type="checkbox"  name="actf" <%=("S".equals(viewBean.getActf()))?"CHECKED":""%>> 
        	</td>
        	
        </tr>
					

   						 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT>
 <%	} %>
 
 <ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>