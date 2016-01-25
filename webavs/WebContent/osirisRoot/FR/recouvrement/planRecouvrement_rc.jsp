<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<%idEcran="GCA60006";
rememberSearchCriterias = true;
actionNew = servletContext + mainServletPath + "?userAction=" + "osiris.recouvrement.compteAnnexe.chercher";
%>
<SCRIPT language="JavaScript">
	var usrAction = "osiris.recouvrement.planRecouvrement.lister";
	top.document.title ="Plans Recouvrement";

	function changeFrom(){
		if(document.getElementById("orderBy").value=='IDPLANRECOUVREMENT'){
			showTexteField("fromIdPlanRecouvrement");
		} else if(document.getElementById("orderBy").value=='LIBELLE'){
			showTexteField("fromLibelle");
		} else if(document.getElementById("orderBy").value=='DATE'){
			showDateField();
		} else if(document.getElementById("orderBy").value=='COLLABORATEUR'){
			showTexteField("fromCollaborateur");
		} else if(document.getElementById("orderBy").value=='IDEXTERNEROLE'){
			showIdExterneField();
		}
	}

	function showDateField() {
		document.getElementById('divFromDate').style.display = "block";
		document.getElementById('divFromTexte').style.display = "none";
		document.getElementById('fieldFromTexte').value = "";
		document.getElementById('divFromIdExterneRole').style.display = "none";
		document.getElementById('fieldFromIdExterneRole').value = "";
	}

	function showTexteField(name) {
		document.getElementById('divFromDate').style.display = "none";
		document.getElementById('fromDate').value = "";
		document.getElementById('divFromTexte').style.display = "block";
		document.getElementById('fieldFromTexte').name = name;
		document.getElementById('divFromIdExterneRole').style.display = "none";
		document.getElementById('fieldFromIdExterneRole').value = "";
	}

	function showIdExterneField() {
		document.getElementById('divFromDate').style.display = "none";
		document.getElementById('fromDate').value = "";
		document.getElementById('divFromTexte').style.display = "none";
		document.getElementById('fieldFromTexte').value = "";
		document.getElementById('divFromIdExterneRole').style.display = "block";
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Sursis au paiement<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD><TABLE width="100%">
		<TR>
			<TD class="label" colspan="3">Numéro du compte annexe </TD>
			<TD class="control" colspan="3">
				<%
					String forIdExterneRoleLike = request.getParameter("forIdExterneRoleLike");
					if (forIdExterneRoleLike == null) {
						forIdExterneRoleLike = "";
					} else {
					%>
						<SCRIPT language="JavaScript">
						bFind = true;
						</SCRIPT>
					<%
					}
				%>

				<INPUT type="text" name="forIdExterneRoleLike" value="<%=forIdExterneRoleLike%>" id="fieldForIdExterneRoleLike">
			</TD>
		</TR>
		<TR>
			<TD class="label">Tri</TD>
			<TD class="control">
				<SELECT id="orderBy" name="orderBy"	onchange="javascript:changeFrom();">
					<OPTION value="IDPLANRECOUVREMENT" selected="selected">N° du plan</OPTION>
					<OPTION value="LIBELLE">Libellé</OPTION>
					<OPTION value="DATE">Date</OPTION>
					<OPTION value="COLLABORATEUR">Collaborateur</OPTION>
					<OPTION value="IDEXTERNEROLE">Compte annexe</OPTION>
				</SELECT>
			</TD>
			<TD class="label">A partir de</TD>
			<TD class="control" colspan="3">
				<DIV style="display:none" id="divFromDate">
					<ct:FWCalendarTag name="fromDate" value=""/>
				</DIV>
				<DIV id="divFromTexte">
					<INPUT type="text" name="fromIdPlanRecouvrement" value="" id="fieldFromTexte">
				</DIV>
				<DIV style="display:none" id="divFromIdExterneRole">
				<%
					int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);
					String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
				%>

		        <ct:FWPopupList id="fieldFromIdExterneRole"
			    	name="fromIdExternalRole"
			        value=""
			        className="libelle"
			        jspName="<%=jspAffilieSelectLocation%>"
			        autoNbrDigit="<%=autoCompleteStart%>"
			        size="25"
			        minNbrDigit="1"
			    />
			    </DIV>
			</TD>
		</TR>
		<TR>
			<TD class="label">Etat</TD>
			<TD class="control"><ct:FWCodeSelectTag wantBlank="true" codeType="OSIPLRETA" defaut="" name="forIdEtat"/></TD>
			<TD>Rôle</TD>
			<TD><SELECT name="forSelectionRole">
					<%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, request.getParameter("forIdRole"))%>
				</SELECT></TD>
		</TR>
		</TABLE></TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>