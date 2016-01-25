<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0012";
	globaz.pyxis.db.tiers.TICompositionTiersViewBean viewBean = (globaz.pyxis.db.tiers.TICompositionTiersViewBean )session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdComposition(); 
	tableHeight = 180;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put  --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Liens entre tiers - Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.ajouter"
}
function upd() {
}
function validate() {
	var exit = true;
	
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.modifier";
	return (exit);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.chercher";
 } else {
  document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.afficher";
 }

}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer tous les moyens de communication! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.compositionTiers.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Legami tra terzi - dettaglio<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		<tr>
			<TD nowrap width="128"><A href='<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=request.getParameter("idTiers")%>'>Terzi</A></TD>
  		     
			<td><INPUT class="libelleLongDisabled" readonly style="color: black" type="text" name="selection" value="<%=viewBean.getNomTiers(request.getParameter("idTiers"))%>"  > </td>

		</tr>
		<tr>
			<td colspan="2"><hr></td>
		</tr>

	<TR>

<%if (!"".equals(viewBean.getIdTiersEnfant())) {%>
			<TD nowrap width="128"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiersEnfant()%>">Relazione tra terzi</A></TD>

<%} else {%>

			<td>Relazione tra terzi</TD>
<%}%>

		<TD align="left" width="100%">
			<INPUT type="hidden" name="idTiersParent" value="<%=request.getParameter("idTiers")%>"  > 
			<INPUT type="hidden" name="idTiers" value="<%=request.getParameter("idTiers")%>"  > 

			<INPUT type="hidden" name="idTiersEnfant" value="<%=viewBean.getIdTiersEnfant()%>"  > 
			<INPUT class="libelleLongDisabled" readonly style="color: black" type="text" name="selection" value="<%=viewBean.getNomTiersEnfant()%>"  > 
			<%
				Object[] tiersMethodsName= new Object[]{
					new String[]{"setNomTiersEnfant","getNom"},
					new String[]{"setIdTiersEnfant","getIdTiers"},
				};
				
			%>
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
							/>
			<ct:FWSelectorTag 
				name="adminSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.administration.chercher"
							/>
			<ct:FWSelectorTag 
				name="banqueSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.banque.chercher"
							/>


			<script>
				document.getElementById("tiersSelector").value="Terzi ...";
				document.getElementById("adminSelector").value="Amministrazione ...";
				document.getElementById("banqueSelector").value="Banca ...";
			</script>

		</TD>
		
	</TR>
	<tr>
		<TD nowrap >Tipo di relazione</TD>
		<TD align="left" width="100%">
				
			<%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.pyxis.db.tiers.TICompositionTiers.CS_CONJOINT);
				except.add(globaz.pyxis.db.tiers.TICompositionTiers.CS_SUCCURSALE);
			%>

				<ct:FWCodeSelectTag name="typeLien"
					      defaut="<%=viewBean.getTypeLien()%>"
						wantBlank="<%=false%>"
					      codeType="PYTYPELIEN"
						except="<%=except%>"
				/>
		</TD>
	</tr>
	
	
	<TR>
        <TD nowrap >Valido dal</TD>
        <TD nowrap >
        	<ct:FWCalendarTag name="debutRelation" 
				value="<%=viewBean.getDebutRelation()%>" 
			/>
	 &nbsp;al&nbsp;
		 <ct:FWCalendarTag name="finRelation" 
			value="<%=viewBean.getFinRelation()%>"
	 		/>
		</TD>	 	 
     </TR>
	
	

		
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>

<ct:menuChange displayId="options" menuId="tiers-detail" showTab="options">
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiersParent()%>"/>
</ct:menuChange>
		
		
 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>