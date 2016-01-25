
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0007"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CAJournalViewBean viewBean = (CAJournalViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
selectedIdValue = viewBean.getIdJournal();
bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());

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
	if (window.confirm("Vous êtes sur le point de supprimer le journal sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.apercuJournal.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Comptes - Détail du journal - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D&eacute;tail du journal<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	       <TR>
            <TD nowrap width="200">
              <p>Journal</p>
            </TD>
            <TD width="30">&nbsp;</TD>
            <TD nowrap>
              <INPUT type="text" name="idJournal" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdJournal()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="200">Libell&eacute;</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="libelle" style="width:7cm" size="40" maxlength="40" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelle(), "\"", "&quot;")%>" class="libelleStandard">
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Date comptable</TD>
            <TD width="30"></TD>
            <TD nowrap>
               	<ct:FWCalendarTag name="dateValeurCG" doClientValidation="CALENDAR" value="<%=viewBean.getDateValeurCG()%>"/>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Date</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="date" size="16" maxlength="15" value="<%=viewBean.getDate()%>" class="dateDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">
              <!-- Visibilit&eacute; des op&eacute;rations en
              saisie -->
            </TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="hidden" name="estVisibleImmediatement" value=<%=(viewBean.getEstVisibleImmediatement().booleanValue())? "on" : ""%>>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">
              <!-- Saisie ouverte aux autres utilisateurs -->
            </TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="hidden" name="estPublic" value=<%=(viewBean.getEstPublic().booleanValue())? "on" : ""%>>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Propri&eacute;taire</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="proprietaire" style="width:7cm" maxlength="30" value="<%=viewBean.getProprietaire()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">
            <%
	              if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getNoJournalCG())) {
	              	%>
	              	<ct:ifhasright element="helios.special.journal.afficher" crud="r">
		            	<A href="<%=request.getContextPath()%>/helios?userAction=helios.special.journal.afficher&idJournal=<%=viewBean.getNoJournalCG()%>" class="external_link">
		            </ct:ifhasright>
		            		Journal associé
		            <ct:ifhasright element="helios.special.journal.afficher" crud="r">
		            	</A>
		            </ct:ifhasright>
	              	<%
	              } else {
	              %>
					Journal associé
	              <%
	              }
	          	%>
            </TD>
            <TD width="200"></TD>
            <TD nowrap>
              <INPUT type="text" name="numeroJournalExerciceComptableCG" style="width:7cm" maxlength="30" value="<%=viewBean.getNumeroJournalComptaGen()%>" class="libelleDisabled" tabindex="-1" readonly>
              <INPUT type="hidden" name="noJournalCG" value="<%=viewBean.getNoJournalCG()%>"/>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Etat</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="ucEtat" style="width:7cm" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="libelleDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
    <% if (!"1".equals(viewBean.getIdJournal())) { %>
          <TR>
            <TD nowrap width="200">Total des écritures</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="totEcritures" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalEcritures()%>" class="montantdisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Total des ordres de versement</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="totOV" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalOrdreVersement()%>" class="montantdisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
          <TR>
            <TD nowrap width="200">Total des ordres de recouvrement</TD>
            <TD width="30"></TD>
            <TD nowrap>
              <INPUT type="text" name="totOR" style="width:7cm" maxlength="30" value="<%=viewBean._getTotalOrdreRecouvrement()%>" class="montantdisabled" tabindex="-1" readonly>
            </TD>
            <TD width="10"></TD>
            <TD nowrap></TD>
          </TR>
   <%  } %>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && request.getParameter("_valid") == null) {
	} else {
%>
<% 		if (!"1".equals(viewBean.getIdJournal())) { %>
				<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options" checkAdd="no">
					<ct:menuSetAllParams key="id" value="<%=viewBean.getIdJournal()%>" checkAdd="no"/>
					<% if (!viewBean.isAnnule()) {%>
					<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
					<% } else { %>
					<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
					<% } %>
				</ct:menuChange>
		<% 	} %>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>