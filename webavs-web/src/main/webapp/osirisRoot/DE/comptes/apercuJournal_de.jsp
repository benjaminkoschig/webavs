
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%	idEcran = "GCA0007";%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
	CAJournalViewBean viewBean = (CAJournalViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	selectedIdValue = viewBean.getIdJournal();
	bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());
	
	boolean hasJournalIso = viewBean.getJournalISODetail() != null;
	
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">
	function add() {
	    document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.ajouter"
	}
	
	function upd() {
		document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.modifier";
	}
	
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.ajouter";
	    else
	        document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.modifier";
	    
	    return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.afficher";
	}
	
	function del() {
		if (window.confirm("Sie sind dabei, das ausgewählte Journal zu löschen! Wollen Sie fortfahren?")) {
	        document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init() {}
	
	top.document.title = "Konti - Detail des Journals " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail des Journals<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD width="20%"><p>Journal</p></TD>
            <TD width="28%"><INPUT type="text" name="idJournal" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdJournal()%>" class="libelleDisabled" tabindex="-1" readonly></TD>
<%			if(hasJournalIso) {%>
				<TD width="4%">&nbsp;</TD>
	            <TD width="20%">Message ID</TD>
	            <TD width="28%"><INPUT type="text" class="libelleDisabled" name="messageIdLibelle" style="width:7cm" value="<%=viewBean.getJournalISODetail().getMessageId()%>"  readonly/></TD>
<%			} else {%>
				<TD colspan="3" width="52%">&nbsp;</TD>
<%			}%>
		</TR>
		<TR>
			<TD width="20%">Bezeichnung</TD>
            <TD width="28%"><INPUT type="text" name="libelle" style="width:7cm" size="40" maxlength="40" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>" class="libelleStandard"></TD>
<%			if(hasJournalIso) {%>
				<TD width="4%">&nbsp;</TD>
	            <TD width="20%">Created Datetime</TD>
	            <TD width="28%"><INPUT type="text" class="libelleDisabled" name="createdDateTimeLibelle" style="width:7cm" value="<%=viewBean.getJournalISODetail().getCreatedDateTime()%>"  readonly/></TD>
<%			} else {%>
				<TD colspan="3" width="52%">&nbsp;</TD>
<%			}%>
		</TR>
		<TR>
			<TD width="20%">Buchungsdatum</TD>
            <TD width="28%"><ct:FWCalendarTag name="dateValeurCG" doClientValidation="CALENDAR" value="<%=viewBean.getDateValeurCG()%>"/></TD>
<%			if(hasJournalIso) {%>
				<TD width="4%">&nbsp;</TD>
	            <TD width="20%">Notification ID</TD>
	            <TD width="28%"><INPUT type="text" class="libelleDisabled" name="notificationIdLibelle" style="width:7cm" value="<%=viewBean.getJournalISODetail().getNotificationId()%>" readonly/></TD>
<%			} else {%>
				<TD colspan="3" width="52%">&nbsp;</TD>
<%			}%>
          </TR>
          <TR>
            <TD width="20%">Datum</TD>
            <TD width="28%"><INPUT type="text" name="date" size="16" maxlength="15" value="<%=viewBean.getDate()%>" class="dateDisabled" tabindex="-1" readonly></TD>
<%			if(hasJournalIso) {%>
				<TD width="4%">&nbsp;</TD>
	            <TD width="20%">Filename</TD>
	            <TD width="28%"><INPUT type="text" class="libelleDisabled" name="fileNameLibelle" style="width:7cm" value="<%=viewBean.getJournalISODetail().getFileName()%>" readonly/></TD>
<%			} else {%>
				<TD colspan="3" width="52%">&nbsp;</TD>
<%			}%>
		</TR>
		<TR>
            <TD width="20%"><!-- Visibilit&eacute; des op&eacute;rations en saisie --></TD>
            <TD width="28%"><INPUT type="hidden" name="estVisibleImmediatement" value=<%=(viewBean.getEstVisibleImmediatement().booleanValue())? "on" : ""%>></TD>
            <TD colspan="3" width="52%"></TD>
		</TR>
		<TR>
            <TD width="20%"><!-- Saisie ouverte aux autres utilisateurs --></TD>
            <TD width="28%"><INPUT type="hidden" name="estPublic" value=<%=(viewBean.getEstPublic().booleanValue())? "on" : ""%>></TD>
            <TD colspan="3" width="52%"></TD>
		</TR>
		<TR>
          	<TD width="20%">Benutzer</TD>
            <TD width="28%"><INPUT type="text" name="proprietaire" style="width:7cm" maxlength="30" value="<%=viewBean.getProprietaire()%>" class="libelleDisabled" tabindex="-1" readonly></TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
		<TR>
            <TD width="20%">
<%			if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getNoJournalCG())) {%>
				<ct:ifhasright element="helios.special.journal.afficher" crud="r">
					<A href="<%=request.getContextPath()%>/helios?userAction=helios.special.journal.afficher&idJournal=<%=viewBean.getNoJournalCG()%>" class="external_link">
				</ct:ifhasright>
				Verbundenes Journal
				<ct:ifhasright element="helios.special.journal.afficher" crud="r">
					</A>
				</ct:ifhasright>
<%			} else {%>
				Verbundenes Journal
<%			}%>
            </TD>
            <TD width="28%">
            	<INPUT type="text" name="numeroJournalExerciceComptableCG" style="width:7cm" maxlength="30" value="<%=viewBean.getNumeroJournalComptaGen()%>" class="libelleDisabled" tabindex="-1" readonly>
            	<INPUT type="hidden" name="noJournalCG" value="<%=viewBean.getNoJournalCG()%>"/>
            </TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="20%">Status</TD>
            <TD width="28%"><INPUT type="text" name="ucEtat" style="width:7cm" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="libelleDisabled" tabindex="-1" readonly></TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
<% 		if (!"1".equals(viewBean.getIdJournal())) {%>
		<TR>
			<TD width="20%">Total der Buchungen</TD>
            <TD width="28%"><INPUT type="text" name="totEcritures" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalEcritures()%>" class="montantdisabled" tabindex="-1" readonly></TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="20%">Total der Zahlungsaufträge</TD>
            <TD width="28%"><INPUT type="text" name="totOV" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalOrdreVersement()%>" class="montantdisabled" tabindex="-1" readonly></TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="20%">Total der Inkassoaufträge</TD>
          	<TD width="28%"><INPUT type="text" name="totOR" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalOrdreRecouvrement()%>" class="montantdisabled" tabindex="-1" readonly></TD>
            <TD colspan="3" width="52%">&nbsp;</TD>
		</TR>
<%		}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if ("add".equalsIgnoreCase(request.getParameter("_method")) && request.getParameter("_valid") == null) {
	} else {
		if (!"1".equals(viewBean.getIdJournal())) { %>
			<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options" checkAdd="no">
				<ct:menuSetAllParams key="id" value="<%=viewBean.getIdJournal()%>" checkAdd="no"/>
<%				if (!viewBean.isAnnule()) {%>
					<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
<% 				} else {%>
					<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
<% 				}%>
			</ct:menuChange>
<% 		}
	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>