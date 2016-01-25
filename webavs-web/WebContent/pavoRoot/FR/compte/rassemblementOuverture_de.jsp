 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "CI - Détail du rassemblement ou ouverture"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%@ page import="globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran = "CCI0007";
    globaz.pavo.db.compte.CIRassemblementOuvertureViewBean viewBean = (globaz.pavo.db.compte.CIRassemblementOuvertureViewBean)session.getAttribute ("viewBean");
	globaz.pavo.db.compte.CICompteIndividuelViewBean viewBeanFK = (globaz.pavo.db.compte.CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
	selectedIdValue = viewBean.getRassemblementOuvertureId();
	userActionValue = "pavo.compte.rassemblementOuverture.modifier";
	boolean isAdd = false;
	if  ("add".equalsIgnoreCase(request.getParameter("_method")))
		isAdd = true;
		
	//Les champs ne peuvent ni êtres modifié, ni effacé.
	//point ouvert no 75
	//Pour les anciens clients et CS, les raou ne sont pas toujours complets, il faut donc pouvoir les modifier
	bButtonDelete = false;
	bButtonUpdate = false;
	if(globaz.pavo.util.CIUtil.isCIAddionelNonConforme(session) && globaz.pavo.util.CIUtil.isSpecialist(session)){
		bButtonUpdate = true;
		isAdd = true;
	}
	String jspLocation4 = servletContext +mainServletPath +"Root/ti_cc_select.jsp";

		
		
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String formState=new String("");
	String fieldDisable = "class='disabled' readonly tabindex='-1'";
	if(!viewBean.getTypeEnregistrementWA().equals("318002")){
	
		formState= fieldDisable;
	}
	//pour les ancien clients les raou doivent être éditables
	if(globaz.pavo.util.CIUtil.isCIAddionelNonConforme(session) && globaz.pavo.util.CIUtil.isSpecialist(session)){
		
		formState = "";
	}
%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.rassemblementOuverture.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.rassemblementOuverture.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.compte.rassemblementOuverture.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.rassemblementOuverture.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="pavo.compte.rassemblementOuverture.supprimer";
        document.forms[0].submit();
    }
}
function init(){}
function updateCaisseComp(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('nomCaisse').value = tag.select[tag.select.selectedIndex].nom;
 	}	
}
function resetCaisse(){
	document.getElementById('nomCaisse').value="";
}

-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un rassemblement ou d'une ouverture<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<TR>
  <TD>Assuré</TD>
  <TD>
     <INPUT type="text" name="numeroAvs" size="17" class="disabled" readonly value="<%=viewBeanFK.getNssFormate()%>">
	 <INPUT type="text" name="nom" class="disabled" size="70" maxlength="70" readonly value="<%=viewBeanFK.getNomPrenom()%>">
	 
  </TD>
</TR>
	<TR>
			<TD nowrap>Date de naissance &nbsp;&nbsp;</TD>
			<TD nowrap colspan="2"><INPUT type="text" name="naissance" class="disabled"
			size="11" readonly value="<%=viewBeanFK.getDateNaissance()%>">
			Sexe
			&nbsp;
			<INPUT type="text" name="sexe" class="disabled"
			size="11" readonly value="<%=viewBeanFK.getSexeLibelle()%>">
			Pays &nbsp;
			<INPUT type="text" name="pays" class="disabled"
			size="48" readonly value="<%=viewBeanFK.getPaysFormate()%>"></TD>
		</td>
	</TR>
<tr>
<td colspan="2"><hr size='1'>
</td></tr>
<tr>
<td>Motif ARC</td>
<td> <input name='motifArc' <%=formState%> size='3' value='<%=viewBean.getMotifArc()%>'> </td>

</tr>
<% if(!viewBean.CS_RASSEMBLEMENT.equals(viewBean.getTypeEnregistrement()) && !viewBean.CS_EXTRAIT.equals(viewBean.getTypeEnregistrement())) { %>
<tr>
<td>Date de clôture</td>
<td> 
<%if (isAdd) {%>
	<ct:FWCalendarTag name="dateCloture" value='<%=viewBean.getDateCloture().length()==10?viewBean.getDateCloture().substring(3):viewBean.getDateCloture()%>'/> 
<%} else {%>
	<input name='dateCloture' <%=formState%> size='10' value='<%=viewBean.getDateCloture().length()==10?viewBean.getDateCloture().substring(3):""%>'>
<%}%>
</td>

</tr>
<% } %>
<%if(!isAdd){%>
<tr>
<td>Caisse commettante</td>
<td> <textarea <%=formState%> name='caisseAgenceCommettante'  class='libelleLong' rows='5' width='250' align='left'><%=viewBean.getCaisseAgenceCommettante()%>
<%=viewBean.getNomCaisseCommettante()%>
</textarea> </td>
</tr>
<%}else{%>
<tr>
<td>Caisse commettante</td>
	<td>
		<ct:FWPopupList   size="3" name="caisseAgenceCommettante" onChange="updateCaisseComp(tag);" value="<%=viewBean.getCaisseAgenceCommettante()%>" onFailure="resetCaisse();" jspName="<%=jspLocation4%>"  autoNbrDigit="5" minNbrDigit="1"  />
		<input type = "text" readonly class="libelleLongDisabled" size="40"  name="nomCaisse" value = "<%=!JAUtil.isIntegerEmpty(viewBean.getCaisseAgenceCommettante())?viewBean.getNomCaisseCommettante():""%>">
		<input type = "hidden" name="caisseCommettante" value = "">
		<input type="hidden" value="True" name ="isEcran">
	</td>
</tr>
<%}%>

<tr>
<td>Référence interne</td>
<td> <input name='referenceInterne' <%=formState%> size='30' value='<%=viewBean.getReferenceInterne()%>'> </td>

</tr>

<tr>
<td>Date de l'ordre</td>
<td> 
<%if (isAdd) {%>
	<ct:FWCalendarTag name="dateOrdre" value="<%=viewBean.getDateOrdre()%>"/> 
<%} else {%>
	<input name='dateOrdre' size='10' <%=formState%> value="<%=viewBean.getDateOrdre()%>">
<%}%>
	
</td>

</tr>
<% if(viewBean.CS_CLOTURE.equals(viewBean.getTypeEnregistrement())) { %>

<tr>
<td>Date de la révocation</td>
<td> <ct:FWCalendarTag name="dateRevocation" value='<%=viewBean.getDateRevocation()%>'/> </td>

</tr>
<% } %>
<tr>
<td>Type d'enregistrement</td>
<td> <input type="typeEnregistrementInv" <%=formState%> readonly class="libelleLongDisabled" size="40" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getTypeEnregistrementWA(),session)%>" > </td>

</tr>

<tr>
<td>Date de demande du second RCI</td>
<td> <input name='dateSecondRciInv' <%=formState%> class='disabled' size='11' readonly value='<%=viewBean.getDateSecondRci()%>'> </td>

</tr>
<% String caisseTenant = viewBean.getCaisseTenantCIWA();
if(!JAUtil.isStringEmpty(caisseTenant)) { %>
<tr>
<td>Caisse tenant le CI</td>
<td> <input name='caisseTenantInv' <%=formState%> class='disabled' size='11' readonly value='<%=caisseTenant%>'> </td>

</tr>
<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>


	<% if(viewBean.canAnnonceCI()) { %>
	<% //if(objSession.hasRight(userActionUpd, "UPDATE")){%>
		<%if(viewBean.CS_SPLITTING.equals(viewBean.getTypeEnregistrementWA())){%>
		<ct:menuChange displayId="options" menuId="rassemblement-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
			<ct:menuSetAllParams key="rassemblementOuvertureId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
			<ct:menuSetAllParams key="rassemblementEcritureId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
		</ct:menuChange>
		
			
		<%}else{%>
		<ct:menuChange displayId="options" menuId="rassemblementNoSpli-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
			<ct:menuSetAllParams key="rassemblementOuvertureId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
			<ct:menuSetAllParams key="rassemblementEcritureId" value="<%=viewBean.getRassemblementOuvertureId()%>"/>
		</ct:menuChange>
			
		<%}%>
	<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>