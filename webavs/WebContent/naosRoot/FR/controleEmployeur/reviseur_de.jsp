<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0060";
	globaz.naos.db.controleEmployeur.AFReviseurViewBean viewBean = (globaz.naos.db.controleEmployeur.AFReviseurViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.controleEmployeur.reviseur.ajouter"
}

function upd() {
}

function validate() {
	 state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.controleEmployeur.reviseur.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.controleEmployeur.reviseur.modifier";
    
    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="naos.controleEmployeur.reviseur.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le réviseur sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.controleEmployeur.reviseur.supprimer";
		document.forms[0].submit();
	}
}
function init() {
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					R&eacute;viseur - D&eacute;tail 
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
					<TR>
           				<TD nowrap width="140">Numéro</TD>
           				<TD nowrap width="300"><INPUT name="idReviseur" type="text" value="<%=viewBean.getIdReviseur()%>" class="numeroCourtDisabled" readonly></TD>
           			</TR>
            		<TR>
						<TD nowrap width="161"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
						<td>		
					 		  <%
								Object[] tiersMethodsName= new Object[]{
									new String[]{"setNomReviseur","getNom"},
									new String[]{"setIdTiers","getIdTiers"},
								};
								Object[] tiersParams = new Object[]{
									new String[]{"selection","_pos"},
								};
								String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/controleEmployeur/reviseur_de.jsp";	
								%>
								<ct:FWSelectorTag 
									name="tiersSelector" 
									
									methods="<%=tiersMethodsName%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.tiers.chercher"
									redirectUrl="<%=redirectUrl%>"/>
							<input type="hidden" value="<%=viewBean.getIdTiers()%>" name="idTiers">
						</td>
					</TR> 
           			<TR>
						<TD nowrap width="140">Visa</TD>
           				<TD nowrap width="300"><INPUT name="visa" type="text" value="<%=viewBean.getVisa()%>" class="libelleCours"></TD>
					</TR>
					<TR>
						<TD nowrap width="140">Description</TD>
           				<TD nowrap width="300"><INPUT name="nomReviseur" type="text" value="<%=viewBean.getNomReviseur()%>" class="libelleLong"></TD>
					</TR>
					<TR> 
						<TD nowrap>Type de réviseur</TD>
							<TD nowrap>
								<ct:FWCodeSelectTag 
	                				name="typeReviseur" 
									defaut="<%=viewBean.getTypeReviseur()%>"
									wantBlank="false"
									codeType="VETYPEREVI"/> 
								<SCRIPT>
									document.getElementById("typeReviseur").tabIndex="-1";
								</SCRIPT>									
							</TD>
					</TR>
						<TR><TD>&nbsp;</TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value=""/>
		<ct:menuSetAllParams key="selectedId" value=""/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>