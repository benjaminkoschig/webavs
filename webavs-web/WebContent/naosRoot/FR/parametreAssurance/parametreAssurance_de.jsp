<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="CAF0022";
	globaz.naos.db.parametreAssurance.AFParametreAssuranceViewBean viewBean = (globaz.naos.db.parametreAssurance.AFParametreAssuranceViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.modifier";
		
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le paramètre d'assurance séléctionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.parametreAssurance.parametreAssurance.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Param&egrave;tres d'une assurance - D&eacute;tail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD nowrap width="125">&nbsp;</TD>
						</TR>
						<TR> 
							<TD height="31" width="125" >Assurance libell&eacute;</TD>
							<TD width="30"></TD>
							<TD width="400"> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getParametreAssuranceId()%>'>
								<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
								<INPUT name="assuranceLibelle" size="50" maxlength="50" type="text" value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="Disabled" readOnly><BR>
								<INPUT name="assuranceLibelleCourt" size="20" maxlength="20" type="text" value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" tabindex="-1" class="Disabled" readOnly>
							</TD>
							<TD width="100"> 
						</TR>
						<TR> 
							<TD nowrap width="125">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="8"> 
								<HR size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD width="125" height="31">Genre</TD>
							<TD height="14" width="30"></TD>
							<TD height="31" width="200" > 
							<% if (method != null && method.equals("add")) { %>
								<ct:FWCodeSelectTag 
									name="genre" 
									defaut="<%=viewBean.getGenre()%>" 
									codeType="VEGENREPAR"/> 
							<% } else { %>
								<input type="text" name="genreInv" size="30" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getGenre())%>" tabindex="-1" class="Disabled" readOnly>
							<% } %>
							</TD>
						</TR>
						<TR> 
							<TD width="117" height="31">Sexe</TD>
							<TD width="30"></TD>
							<TD width="200"> 
								<ct:FWCodeSelectTag 
									name="sexe" 
									defaut="<%=viewBean.getSexe()%>" 
									codeType="PYSEXE"
									wantBlank="true"/> 
							</TD>
						</TR>
						<TR> 
							<TD width="117" height="31">Date de d&eacute;but</TD>
							<TD width="30"></TD>
							<TD width="200"> 
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" /> 
							</TD>
						</TR>
						<!--TR> 
							<TD nowrap width="120" height="31" >P&eacute;riode</TD>
							<TD width="30" align="right">de</TD>
							<TD nowrap width="270">
								<ct:FWCalendarTag name="dateDebut" 
									value="<=viewBean.getDateDebut()>" />
								&agrave; 
								<ct:FWCalendarTag name="dateFin" 
									value="<=viewBean.getDateFin()>" /> 
							</TD>
						</TR-->
						<!--TR> 
							<TD width="117" height="31">Date de d&eacute;but</TD>
							<TD width="30"></TD>
							<TD width="200"> 
								<ct:FWCalendarTag name="dateDebut" value="<=viewBean.getDateDebut()>" /> 
							</TD>
						</TR>
						<TR> 
							<TD width="117" height="31">Date de fin</TD>
							<TD width="30"></TD>
							<TD width="200"> 
								<ct:FWCalendarTag name="dateFin" value="<=viewBean.getDateFin()>" /> 
							</TD>
						</TR-->
						<TR> 
							<TD width="117" height="31">Valeur</TD>
							<TD width="30"></TD>
							<TD width="200"> 
								<INPUT type="text" name="valeur" size="60" maxlength="20" value="<%=viewBean.getValeur()%>">						
							</TD>
						</TR>
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
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>