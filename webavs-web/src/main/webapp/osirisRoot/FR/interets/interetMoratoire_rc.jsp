<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0035";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.globall.util.*" %>
<%
CAApercuInteretMoratoireListViewBean viewBean = (CAApercuInteretMoratoireListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
CAApercuInteretMoratoireViewBean element = (CAApercuInteretMoratoireViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
viewBean.setForIdCompteAnnexe(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())?viewBean.getForIdCompteAnnexe():element.getIdCompteAnnexe());
String secondServletPath = viewBean.isDomaineCA()?"/osiris":"/musca";
bButtonNew = false;
if ( viewBean.getForDomaine().equals("CA") )
{
	actionNew += "&idJournalCalcul=" + element.getIdJournalCalcul() + "&forDomaine=CA";
}
else
{
	actionNew = servletContext + mainServletPath + "?userAction=osiris.interets.interetMoratoire.afficher&selectorName=enteteFactureImSelector&_method=add&idPassage=" + element.getIdJournalFacturation();
}

//Active le bouton Nouveau si permissions ET domaine==FA
if (bButtonNew &&	viewBean.getForDomaine().equals("FA"))
{
	bButtonNew = true;
}
else
{
	bButtonNew = false;
}

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%	if (viewBean.isDomaineCA()) { %>
<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=element.getIdJournalCalcul()%>"/>
	<% if ((element.getJournal() != null) && (!element.getJournal().isAnnule())) {%>
	<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
	<% } else { %>
	<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
	<% } %>
</ct:menuChange>
<% } %>

<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.interets.interetMoratoire.lister";
bFind = true;
var vFromDescription="";
var vValeurCompte="1000";
var vFromDate="";
var vForMontantABS="";
var vForIdExterneRole="";

function jsVisualisation(){
	var url = window.top.fr_secondary.location.href;
    alert("Rechargement rcListe avec " + url);
}

function jsFromDate(){
	vFromDate = document.forms[0].fromDate.value;
}

function jsInitForIdCompte(){
	document.forms[0].forIdCompte.value = "";
}

function jsFromDescription(){
	document.forms[0].forIdCompteAnnexe.value = "";
	document.forms[0].forIdExterneRole.value = "";
	vFromDescription = document.forms[0].fromDescription.value;
}

function jsForMontantABS(){
	vForMontantABS = document.forms[0].forMontantABS.value;
}

function jsInitForIdCompteAnnexe(){
	document.forms[0].forIdCompteAnnexe.value = "";
	document.forms[0].fromDescription.value = "";
	vFromDescription = "";
	vForIdExterneRole = document.forms[0].forIdExterneRole.value;
}

function jsValeurForSelectionRole(){
	vValeurCompte = document.forms[0].forSelectionRole.value;
}
top.document.title = "Comptes - Recherche des écritures du journal - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
        <div align="left">Aperçu des décisions d'intérêts moratoires du journal</div>
        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="120">Journal</TD>
            <TD width="300">
              <INPUT type="text" style="width:7cm" class="libelleDisabled" value="<%=element.getIdJournal()%> - <%=element.getJournalLibelle()%>" readonly>
              <input type="hidden" name="vueOperationCpteAnnexe" value="true">
              <input type="hidden" name="forIdJournalCalcul" value="<%=element.getIdJournalCalcul()%>">
              <input type="hidden" name="idJournalCalcul" value="<%=element.getIdJournalCalcul()%>">
              <input type="hidden" name="forIdJournalFacturation" value="<%=element.getIdJournalFacturation()%>">
              <input type="hidden" name="idJournalFacturation" value="<%=element.getIdJournalFacturation()%>">
              <INPUT type="hidden" name="forDomaine" value="<%=viewBean.getForDomaine()%>">
              <INPUT type="hidden" name="domaine" value="<%=element.getDomaine()%>">
			</TD>
			<TD width="179">&nbsp;</TD>
            <TD align="right">Date&nbsp;
              <input type="text" class="dateDisabled" value="<%=element.getJournalDate()%>" readonly name="text3" style="width:5cm">
            </TD>
          </TR>
          <TR>
            <TD colspan="4">
              <HR/>
            </TD>
          </TR>
          <tr>
            <td>N&deg; <%=viewBean.isDomaineCA()?"compte":"décompte"%></td>
            <td>
            	<input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>">
            	<%
            	CACompteAnnexe tempCompteAnnexe = new CACompteAnnexe();
            	globaz.musca.db.facturation.FAEnteteFacture enteteFacture = new globaz.musca.db.facturation.FAEnteteFacture();

            	if (viewBean.isDomaineCA())
            	{
					tempCompteAnnexe.setIdCompteAnnexe(viewBean.getForIdCompteAnnexe());
					tempCompteAnnexe.setSession(objSession);
					tempCompteAnnexe.retrieve();
				}
				else // domaine = musca:facturation
				{
					enteteFacture.setIdEntete(viewBean.getForIdCompteAnnexe());
					enteteFacture.setSession(objSession);
					enteteFacture.retrieve();
				}
				%>
				<input type="text" name="forIdExterneRole" value="<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getForIdCompteAnnexe()) && request.getParameter("forIdExterneRole") != null){%><%=request.getParameter("forIdExterneRole")%><%}else{%><%=viewBean.isDomaineCA()?tempCompteAnnexe.getIdExterneRole():enteteFacture.getIdExterneRole()%><%}%>" onChange="jsInitForIdCompteAnnexe()">
				<select name="forIdRole" class="libelleStandard" onChange="jsValeurForSelectionRole()">
                <% String selectionRole = request.getParameter("forSelectionRole");
					if (viewBean.isDomaineCA() && selectionRole == null && tempCompteAnnexe.getIdRole() != null)
						selectionRole = tempCompteAnnexe.getIdRole();
					else if (!viewBean.isDomaineCA() && selectionRole == null && enteteFacture.getIdRole() != null)
						selectionRole = enteteFacture.getIdRole();%>
				<%=CARoleViewBean.createOptionsTags(objSession, selectionRole)%>
              	</select>
				<%
				Object[] compteAnnexeMethodsName = new Object[]{
					new String[]{"setIdCompteAnnexe","getIdCompteAnnexe"}
					};

				String redirectUrl = "/osirisRoot/FR/interets/interetMoratoire_rc.jsp";
				if (viewBean.isDomaineCA())
				{	%>
					<ct:FWSelectorTag
						name="CompteAnnexeSelector"

						methods="<%=compteAnnexeMethodsName%>"
						providerApplication ="osiris"
						providerPrefix="CA"
						providerAction ="osiris.comptes.apercuComptes.chercher"
						redirectUrl="<%=redirectUrl%>" target="fr_main"
					/>

			 	<% } %>
            </td>
			<TD width="179">&nbsp;</TD>
            <td align="right">Tri&nbsp;
              <select name="forSelectionTri" style="width:5cm;">
              	<option selected="selected" value="nom">nom</option>
                <option value="datenumero">date, numéro</option>
                <option value="datenom">date, nom</option>
                <option value="numero">numéro</option>
              </select>
            </td>
          <tr>
            <td>Genre</td>
            <td>
           		<ct:FWSystemCodeSelectTag
           			name="forIdGenreInteret"
           			defaut=""
           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"
           		/>
            </td>
			<TD width="179">&nbsp;</TD>
            <td align="right">Montant&nbsp;
              <input type="text" class="montant" name="fromTotalMontantInteret" style="width:5cm;">
              <INPUT type="hidden" name="selectorName" value="">
              <script>
           		document.getElementById("forIdGenreInteret").style.width=260;
           		</script>
            </td>
          </TR>
          
          <tr>
          <td>Motif de décision</td>
          <td colspan="3">
			<%
			   	java.util.HashSet except = new java.util.HashSet();
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE);
				except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			   	
			%>

	    	<ct:FWSystemCodeSelectTag name="forIdMotifCalcul"  defaut="" 
				codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsMotifDecisionInteret(objSession)%>"
				except="<%=except%>"/>
          </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>