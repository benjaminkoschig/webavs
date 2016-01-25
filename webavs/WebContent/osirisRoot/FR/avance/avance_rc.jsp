<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0054";
rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-Avance" showTab="options"/>

<SCRIPT language="JavaScript">
	var usrAction = "osiris.avance.lister";
	top.document.title ="Aperçu des avances";
	bFind = true;

	function changeFrom(){
		if(document.getElementById("orderBy").value=='IDPLANRECOUVREMENT'){
			document.getElementById("from").name = "fromIdPlanRecouvrement";
		} else if(document.getElementById("orderBy").value=='LIBELLE'){
			document.getElementById("from").name = "fromLibelle";
		} else if(document.getElementById("orderBy").value=='DATE'){
			document.getElementById("from").name = "fromDate";
		} else if(document.getElementById("orderBy").value=='COLLABORATEUR'){
			document.getElementById("from").name = "fromCollaborateur";
		}
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des avances<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD class="label">Numéro</TD>
			<TD class="control">
			<%String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";%>

			<ct:FWPopupList
   				name="forIdExterneRoleLike"
				className="libelle"
				jspName="<%=jspAffilieSelectLocation%>"
				size="25"
				onChange=""
				minNbrDigit="1"
			/>
			</TD>
			<TD width="60%">&nbsp;</TD>
			<TD class="label" align="right">Etat&nbsp;<ct:FWCodeSelectTag wantBlank="true" codeType="OSIPLRETA" defaut="<%=globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_ACTIF%>" name="forIdEtat"/></TD>
		</TR>

		<TR>
			<TD class="label">Echéance</TD>
			<TD class="control">
				<SELECT id="forIdTypeEcheance" name="forIdTypeEcheance">
					<OPTION selected value=""/>
					<OPTION value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_ANNUELLE%>">Annuelle</OPTION>
					<OPTION value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_MENSUELLE%>">Mensuelle</OPTION>
					<OPTION value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_TRIMESTRIELLE%>">Trimestrielle</OPTION>
				</SELECT>
			</TD>
			<TD width="60%">&nbsp;</TD>
			<TD class="label" align="right">Tri&nbsp;
				<SELECT id="orderBy" name="orderBy"	onchange="javascript:changeFrom();">
					<OPTION selected="selected"></OPTION>
					<OPTION value="LIBELLE">Libellé</OPTION>
					<OPTION value="DATE">Date</OPTION>
					<OPTION value="ACOMPTE DESC">Acompte</OPTION>
				</SELECT>
			</TD>
		</TR>

		<TR>
			<TD class="label">Mode</TD>
			<TD class="control">
			<%@page import="globaz.osiris.db.avance.CAAvanceViewBean"%>
			<ct:FWListSelectTag data="<%=CAAvanceViewBean.modesPourUtilisateurCourant(objSession)%>" defaut="" name="forIdModeRecouvrement"/>
			</TD>
			<TD colspan="2">&nbsp;</TD>
		</TR>

		<TR>
			<TD class="label">Rôle</TD>
			<TD class="control">
				<SELECT name="forSelectionRole">
					<%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, null)%>
				</SELECT>
			</TD>
			<TD colspan="2">&nbsp;</TD>
		</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>