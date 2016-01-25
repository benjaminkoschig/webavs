<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.aquila.db.administrateurs.CORechercheCompteAnnexeViewBean viewBean = (globaz.aquila.db.administrateurs.CORechercheCompteAnnexeViewBean)session.getAttribute("viewBean");
Object[] osirisMethods = new Object[]{
	new String[]{"setNomAffilie","getDescription"},
	new String[]{"setLikeIdExterneRole","getIdExterneRole"},
	new String[]{"setIdExterneRoleChoisi", "getIdExterneRole"},
};

bButtonNew = false;

String bFind = (String)request.getAttribute("bFind");

idEcran = "GCO0013";
rememberSearchCriterias = true;
String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<SCRIPT language="javaScript">

bFind = <%="true".equals(bFind)?"true":"false"%>;

var usrAction = "aquila.administrateurs.administrateur.lister";

function goNew(){
	if (document.forms[0].elements('idExterneRoleChoisi').value == "" || document.forms[0].elements('nomAffilie').value == ""){
		alert("Vous devez d'abord choisir un compte annexe");
	} else {
		document.location.href='<%=actionNew%>&forIdExterneLike='+document.forms[0].elements('idExterneRoleChoisi').value+'&nomAffilie='+document.forms[0].elements('nomAffilie').value;
	}
}
function updateNomAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){

		document.getElementById('nomAffilie').value = tag.select[tag.select.selectedIndex].selectedCompteAnnexeDesc;
		document.forms[0].elements('idExterneRoleChoisi').value = document.forms[0].elements('likeIdExterneRole').value;

	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche des aministrateurs<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		   <TR>
			<TD>Société : </TD>
			<TD>
				<ct:FWPopupList
	           name="likeIdExterneRole"
	           value="<%=viewBean.getLikeIdExterneRole()%>"
	           className="libelle"
	           jspName="<%=jspAffilieSelectLocation%>"
	           autoNbrDigit="6"
	           size="25"
	           onChange="updateNomAffilie(tag);"
	           minNbrDigit="1"
	          />
	         </TD>

				<TD><INPUT type="text" name="nomAffilie" value="<%=viewBean.getNomAffilie()%>" class="libelleLongDisabled" readonly></TD>
				<TD><INPUT type="hidden" name="forIdGenreCompte" value="235001"></TD>
				<TD><INPUT type="hidden" name="idExterneRoleChoisi" value="<%=viewBean.getIdExterneRoleChoisi()%>"></TD>
				<TD><INPUT type="hidden" name="forSelectionTri" value="1"></TD>
			</TR>

			<TR>
			<TD>Nom : </TD>
			<TD>
			<INPUT type="text" name="likeNom"  >
			</TD>
			</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>

				<ct:ifhasright element="<%=userActionNew%>" crud="c">
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="goNew()">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>