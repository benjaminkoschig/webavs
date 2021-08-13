<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.naos.translation.CodeSystem" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="CAF0018";
	globaz.naos.db.assurance.AFAssuranceViewBean viewBean = (globaz.naos.db.assurance.AFAssuranceViewBean)session.getAttribute ("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";
	int autoDigiAff = 14;
	String method = request.getParameter("_method"); 
	boolean hasAssuranceReference = ! globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdAssuranceReference());
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	if ("<%=request.getParameter("_valid")%>" != "fail") {
		document.forms[0].elements('typeCalcul').value   = "<%=globaz.naos.translation.CodeSystem.TYPE_CALCUL_STANDARD%>";	
	}
	document.forms[0].elements('userAction').value="naos.assurance.assurance.ajouter"
}

function upd() {
}

function validate() {
	//updateRubriqueId(tag);
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.assurance.assurance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.assurance.assurance.modifier";
	return (true);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.assurance.assurance.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'assurance sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.assurance.assurance.supprimer";
		document.forms[0].submit();
	}
}

function init() {
	if (<%=hasAssuranceReference%> == false) {
		document.all('referenceAssuranceDisplay').style.display='none';	
	} else {
		document.all('referenceAssuranceDisplay').style.display='block';
	}
}

function updateRubriqueId(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('rubriqueIdList').value = tag.select[tag.select.selectedIndex].value;
		document.getElementById('rubriqueId').value     = tag.select[tag.select.selectedIndex].idRubrique;
	}
}

function onChangeHasAssuranceReference() {
	var valueException = document.forms[0].elements('assuranceReference');
	if (valueException.checked == false) {
		document.getElementById('idAssuranceReference').value = "";
		document.getElementById('libelleAssuranceRef').value = "";
		document.all('referenceAssuranceDisplay').style.display='none';
	} else {
		document.all('referenceAssuranceDisplay').style.display='block';
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Assurance - D&eacute;tail 
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD nowrap colspan="3">&nbsp;</TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31"><ct:FWLabel key="ID_ASSURANCE"/></TD>
							<TD width="30" ></TD>
							<TD nowrap colspan="2"><%=viewBean.getAssuranceId()%></TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31">Libell&eacute; court</TD>
							<TD width="30" ></TD>
							<TD nowrap colspan="2">
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getAssuranceId()%>'>
								<INPUT type="hidden" name="idAssuranceReference" value='<%=viewBean.getIdAssuranceReference()%>'>
								<INPUT name="assuranceLibelleCourtFr" maxlength="20" type="text" 
									value="<%=viewBean.getAssuranceLibelleCourtFr()%>">&nbsp;Fran&ccedil;ais<BR>
								<INPUT name="assuranceLibelleCourtAl" maxlength="20" type="text" 
									value="<%=viewBean.getAssuranceLibelleCourtAl()%>">&nbsp;Allemand<BR>
								<INPUT name="assuranceLibelleCourtIt" maxlength="20" type="text" 
									value="<%=viewBean.getAssuranceLibelleCourtIt()%>">&nbsp;Italien
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" colspan="3">&nbsp;</TD>
						</TR>
						<TR>
							<TD nowrap width="125">Libell&eacute;</TD>
							<TD width="30"></TD>
							<TD nowrap colspan="2" width="400">
								<INPUT name="assuranceLibelleFr" maxlength="50" size="50" type="text" 
									value="<%=viewBean.getAssuranceLibelleFr()%>">&nbsp;Fran&ccedil;ais<BR>
								<INPUT name="assuranceLibelleAl" maxlength="50" size="50" type="text" 
									value="<%=viewBean.getAssuranceLibelleAl()%>">&nbsp;Allemand<BR>
								<INPUT name="assuranceLibelleIt" maxlength="50" size="50" type="text" 
									value="<%=viewBean.getAssuranceLibelleIt()%>">&nbsp;Italien
							</TD>
							<TD nowrap width="100"></TD>
						</TR>
						<TR>
							<TD nowrap width="125">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="5"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31">Rubrique comptable</TD>
							<TD width="30"></TD>
							<TD colspan="2">
								<%
									String valueIdRubrique = "";
									String valueIdExterne = "";
									if (viewBean.getRubriqueComptable() != null) {
										valueIdRubrique = viewBean.getRubriqueComptable().getIdRubrique();
										valueIdExterne  = viewBean.getRubriqueComptable().getIdExterne(); 
									}
								%>
								<INPUT type="hidden" name="rubriqueId" value='<%=valueIdRubrique%>'>
								<ct:FWPopupList 
									name="rubriqueIdList" 
									value="<%=valueIdExterne%>" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="20"
									onChange="updateRubriqueId(tag)"
									minNbrDigit="0"/>
							</TD>           
						</TR>
						<TR>
							<TD nowrap width="125" height="31">Canton</TD>
							<TD width="30"></TD>
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="assuranceCanton" 
									defaut="<%=viewBean.getAssuranceCanton()%>"
									codeType="PYCANTON"
									wantBlank="true"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31" >Genre d'assurance</TD>
							<TD width="30"></TD>
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="assuranceGenre" 
									defaut="<%=viewBean.getAssuranceGenre()%>"
									codeType="VEASSURANC"
									wantBlank="true"/>
							            prise dans décompte 13  
						<INPUT type="checkbox" name="assurance13"
									<%=(viewBean.isAssurance13().booleanValue())? "checked" : ""%>>
						prise dans les relevés			
						<INPUT type="checkbox" name="decompte13Releve"
									<%=(viewBean.getDecompte13Releve().booleanValue())? "checked" : ""%>>			
									</TR>
						<TR> 
							<TD nowrap width="157" height="31" >Type d'assurance</TD>
							<TD width="30"></TD>
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="typeAssurance" 
									defaut="<%=viewBean.getTypeAssurance()%>"
									codeType="VETYPEASSU"/>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="157" height="31" >Type de calcul</TD>
							<TD width="30"></TD>
							<TD nowrap colspan="2">
								<ct:FWCodeSelectTag 
									name="typeCalcul" 
									defaut="<%=viewBean.getTypeCalcul()%>"
									codeType="VETYPCALCL"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="157" height="31" >Taux par caisse métier</TD>
							<TD nowrap width="30"></TD>
							<TD nowrap width="40">
								<INPUT type="checkbox" name="tauxParCaisse" <%=(viewBean.isTauxParCaisse().booleanValue())? "checked" : ""%>>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="157" height="31" >Figure sur document de type acompte</TD>
							<TD nowrap width="30"></TD>
							<TD nowrap width="40">
								<INPUT type="checkbox" name="surDocAcompte" <%=(viewBean.isSurDocAcompte().booleanValue())? "checked" : ""%>>
							</TD>
						</TR>
						<% if ((method != null &&  method.equalsIgnoreCase("add")) || 
						       ! viewBean.isAssuranceDeReference().booleanValue()) { %>
						<TR> 
							<TD nowrap width="157" height="31" >Assurance de r&eacute;f&eacute;rence</TD>
							<TD nowrap width="30"></TD>
							<TD nowrap width="40">
								<INPUT type="checkbox" name="assuranceReference" <%= hasAssuranceReference ? "checked" : ""%> 
									onClick="onChangeHasAssuranceReference();">
								<%
									String libelleAssuranceRef = "";
									if (viewBean.getAssuranceReference() != null) {
										libelleAssuranceRef = viewBean.getAssuranceReference().getAssuranceLibelle();
									}
								%>
							</TD> 
							<TD nowrap width="460">
								<TABLE border="0" cellspacing="0" cellpadding="0" id="referenceAssuranceDisplay">
								<TBODY>
								<TR><TD>
								<INPUT type="text" name="libelleAssuranceRef" size="50" value="<%=libelleAssuranceRef%>" class="Disabled" tabindex="-1" readOnly>
								<%  
									Object[] caisseMethods= new Object[]{
										new String[]{"setIdAssuranceReference","getAssuranceId"}
									};
								%>
								<ct:FWSelectorTag 
									name="assuranceSelector" 
									
									methods="<%=caisseMethods%>"
									providerApplication ="naos"
									providerPrefix="AF"
									providerAction ="naos.assurance.assurance.chercher"/>
								</TD></TR>
								</TBODY> 
								</TABLE>
							</TD>
						</TR>
						<% } else { %>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="referenceAssuranceDisplay">
								</TABLE>
							</TD>
						<% } %>
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
<% if(viewBean.getTypeAssurance().equals(CodeSystem.TYPE_ASS_CRP_BASIC)){%>
<ct:menuChange displayId="options" menuId="AFOptionsAssurancesTauxFixe" showTab="options">
	<ct:menuSetAllParams key="assuranceId" value="<%=viewBean.getAssuranceId()%>"/>
</ct:menuChange>
<%}else{%>
<ct:menuChange displayId="options" menuId="AFOptionsAssurances" showTab="options">
	<ct:menuSetAllParams key="assuranceId" value="<%=viewBean.getAssuranceId()%>"/>
</ct:menuChange>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>