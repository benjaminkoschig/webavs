
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0043"; %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.globall.parameters.*" %>
<%@ page import="globaz.osiris.utils.CAUtil" %>
<%
globaz.osiris.db.ordres.CAOrdreGroupeViewBean viewBean =
  (globaz.osiris.db.ordres.CAOrdreGroupeViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%      //recherche de la date du jour
	globaz.globall.util.JACalendarGregorian cal = new globaz.globall.util.JACalendarGregorian();
      	String result = cal.format(cal.addDays(cal.today(), 1),globaz.globall.util.JACalendarGregorian.FORMAT_DDsMMsYYYY);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
	if (document.forms[0].dateEcheance.value == "") {
		document.forms[0].dateEcheance.value = "<%=result%>";
	}
	document.forms[0].natureOrdresLivres.value = "<%=globaz.osiris.db.ordres.CAOrdreGroupeViewBean.ORDRESTOUS%>";
  	document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.ajouter";

}
function upd() {
	if (document.forms[0].etat.value == document.forms[0].transmis.value) {
		document.forms[0].idOrganeExecution.disabled = true;
		document.forms[0].natureOrdresLivres.disabled = true;
	}

  	document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.modifier";

}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.modifier";

	if (document.getElementById("typeOrdreGroupe").value==207003){
		document.getElementById("dateEcheance").value=document.getElementById("dateRecouvrement").value;
	} else {
		document.getElementById("dateEcheance").value=document.getElementById("dateVersement").value;
	}
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.afficher";
}
function del() {
	if (window.confirm("Vous �tes sur le point de supprimer l'ordre group� s�l�ctionn�! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.ordres.ordresGroupes.supprimer";
        document.forms[0].submit();
    }
}
function init(){
	selectChangeISO20022(document.getElementById("idOrganeExecution"));
}
function changeAffichage(){
	if (document.getElementById("typeOrdreGroupe").value==207002){
		document.getElementById("tdVersement").style.display="block";
		document.getElementById("tdRecouvrement").style.display="none";
	} else {
		document.getElementById("tdRecouvrement").style.display="block";
		document.getElementById("tdVersement").style.display="none";			
	}
}
function selectChangeISO20022(s) {
	changeAffichageISO20022(s[s.selectedIndex].id);
}
function changeAffichageISO20022(cs) {
	if(cs==258003){ // 258003 traitement ISO20022
		$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
	} else {
		$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
	} 
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D�tail d'un ordre group�<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="153">
              <P>Num&eacute;ro</P>
            </TD>
            <TD width="10"></TD>
            <TD width="289">
              <input type="text" name="idOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>" class="libelleDisabled" tabindex="-1" readonly size="15" maxlength="15">
            </TD>
            <TD width="123"></TD>
            <TD width="139"></TD>
          </TR>
          <TR>
            <TD nowrap width="153">Description</TD>
            <TD width="10">&nbsp;</TD>
            <TD width="289">
              <input type="text" name="motif" value="<%=viewBean.getMotif()%>" size="31" maxlength="30">
            </TD>
            <TD width="123">Date d'&eacute;ch&eacute;ance
				<INPUT type="hidden" id="dateEcheance" name="dateEcheance" value="<%=viewBean.getDateEcheance()%>"/>
			</TD>
            <TD id="tdVersement" width="139">
 				<ct:FWCalendarTag name="dateVersement" doClientValidation="CALENDAR" value="<%=viewBean.getDateEcheance()%>"/>
				<script>
					document.getElementsByName("dateVersement")[0].id = "dateVersement";
				</script>
<%--              <INPUT type="text" name="dateEcheance" value="<%=viewBean.getDateEcheance()%>" class="input" tabindex="-1" size="15" maxlength="15"> --%>
            </TD>
            <TD id="tdRecouvrement">
              <%	java.util.List lst = viewBean.getListeDateEcheance();%>
              <select id="dateRecouvrement" name="dateRecouvrement">
				<%if (!JadeNumericUtil.isEmptyOrZero(viewBean.getDateEcheance())){ %>
					<option selected value="<%=viewBean.getDateEcheance()%>"><%=viewBean.getDateEcheance()%></option>}
				<%} %>
                <% java.util.Iterator iter = lst.iterator();
                	while (iter.hasNext()){
						String myDate = (String)iter.next();
						if (!myDate.equalsIgnoreCase(viewBean.getDateEcheance())){%>
							<option value="<%=myDate%>"><%=myDate%></option>}
	                		
						<%}%>
							
	                <%}%>
					
              </select>
            </TD>

          </TR>
          <TR>
            <TD width="153" valign="bottom">Organe d'ex&eacute;cution</TD>
            <TD width="10"></TD>
            <TD width="289">
              <%	viewBean.getCsOrganeExecution();
				CAOrganeExecution _organeExecution = null;
				CAOrganeExecutionManager _CsOrganeExecution = new CAOrganeExecutionManager();
				_CsOrganeExecution.setForIdTypeTraitementOG(true);
				_CsOrganeExecution.setSession(objSession);
				_CsOrganeExecution.find(); %>
              <select name="idOrganeExecution" onchange="selectChangeISO20022(this)">
                <%	for (int i=0; i < _CsOrganeExecution.size(); i++) {
				_organeExecution = (CAOrganeExecution) _CsOrganeExecution.getEntity(i);
				if (_organeExecution .getIdOrganeExecution().equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
	                <option selected value="<%=_organeExecution .getIdOrganeExecution()%>" id="<%=_organeExecution.getIdTypeTraitementOG()%>"><%=_organeExecution.getNom()%></option>
	                
	                <%	} else { %>
	                <option value="<%=_organeExecution .getIdOrganeExecution()%>" id="<%=_organeExecution.getIdTypeTraitementOG()%>"><%=_organeExecution .getNom()%></option>
	                <%	}
				} %>
              </select>
            </TD>
            <TD width="123" height="31"></TD>
            <TD width="139" height="31"></TD>
          </TR>
          <TR>
            <TD nowrap width="153" height="31">Type d'ordre group&eacute;</TD>
            <TD width="10" height="31"></TD>
            <TD width="289" height="31">
              <% viewBean.getCsTypeOrdreGroupe();
   FWParametersCode _csTypeOrdreGroupe = null;%>
              <SELECT id="typeOrdreGroupe" name="typeOrdreGroupe" onchange="changeAffichage();">
                <%	for (int i = 0; i < viewBean.getCsTypesOrdreGroupe().size(); i++) {
		_csTypeOrdreGroupe = (FWParametersCode) viewBean.getCsTypesOrdreGroupe().getEntity(i);
		if (_csTypeOrdreGroupe.getIdCode().equalsIgnoreCase(CAOrdreGroupe.TOUS)){
			continue;
		}
		if (_csTypeOrdreGroupe.getIdCode().equalsIgnoreCase(viewBean.getTypeOrdreGroupe())) {%>
                <OPTION selected value="<%= _csTypeOrdreGroupe.getIdCode()%>"><%= _csTypeOrdreGroupe.getCurrentCodeUtilisateur().getLibelle()%>
                </OPTION>
                <% } else { %>
                <OPTION value="<%= _csTypeOrdreGroupe.getIdCode()%>"><%= _csTypeOrdreGroupe.getCurrentCodeUtilisateur().getLibelle()%>
                </OPTION>
                <% }
	} %>
              </SELECT>
            </TD>
            <TD width="123" height="31"></TD>
            <TD width="139" height="31"></TD>
          </TR>
          <TR>
            <TD nowrap width="153" height="31">Nature des ordres &agrave; livrer</TD>
            <TD width="10" height="31">&nbsp;</TD>
            <TD nowrap width="289">
              <%viewBean.getCsNaturesOrdresLivres();
		FWParametersCode _csNatureOrdresLivres = null;
%>
              <SELECT name="natureOrdresLivres">
              <option value=""></option>
                <%			for (int i=0; i < viewBean.getCsNaturesOrdresLivres().size(); i++) {
				_csNatureOrdresLivres = (FWParametersCode) viewBean.getCsNaturesOrdresLivres().getEntity(i);
				if (_csNatureOrdresLivres.getIdCode().equalsIgnoreCase(viewBean.getNatureOrdresLivres())){
%>
                <OPTION selected value="<%=_csNatureOrdresLivres .getIdCode()%>"><%=_csNatureOrdresLivres.getCurrentCodeUtilisateur().getCodeUtilisateur()%>-<%=_csNatureOrdresLivres.getCurrentCodeUtilisateur().getLibelle()%></OPTION>
                <%	} else { %>
                <OPTION value="<%=_csNatureOrdresLivres.getIdCode()%>"><%=_csNatureOrdresLivres.getCurrentCodeUtilisateur().getCodeUtilisateur()%>-<%=_csNatureOrdresLivres.getCurrentCodeUtilisateur().getLibelle()%></OPTION>
                <%				}
			}
%>
              </SELECT>
            </TD>
            <TD width="123" height="31">&nbsp;</TD>
            <TD width="139" height="31"></TD>
          </TR>
          <TR class="classNonIso">
            <TD width="153">Num&eacute;ro OG</TD>
            <TD width="10"></TD>
            <TD nowrap width="289">
              <input type="text" name="numeroOG" value="<%=viewBean.getNumeroOG()%>">
            </TD>
            <TD width="123">Nom du support</TD>
            <TD width="139">
              <input type="text" name="nomSupport" value="<%=viewBean.getNomSupport()%>" size="15" maxlength="15">
            </TD>
          </TR>
          <TR class="classIso">
            <TD width="153">Num&eacute;ro de livraison</TD>
            <TD width="10"></TD>
            <TD nowrap width="289">
              <input type="text" name="isoNumLivraison" value="<%=viewBean.getIsoNumLivraison()%>" size="35" maxlength="35" class="inputDisabled" readonly>
            </TD>
            <TD width="123">Gestionnaire</TD>
            <TD width="139">
              <input type="text" name="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" maxlength="25">
            </TD>
          </TR>
          <TR class="classIso">
            <TD width="153">Priorit&eacute; d'ex&eacute;cution</TD>
            <TD width="10"></TD>
            <TD nowrap width="289">
              <select id="isoHighPriority" name="isoHighPriority">
               <%			
				if ("1".equalsIgnoreCase(viewBean.getIsoHighPriority())){
%>
				<OPTION value="0">normale</OPTION>
                <OPTION selected value="1">haute</OPTION>
                <%	} else { %>
                <OPTION selected value="0">normale</OPTION>
                <OPTION value="1">haute</OPTION>
                <%}%>
              </select>
            </TD>
            <TD width="123">Type d'avis</TD>
            <TD width="139">
				<ct:FWCodeSelectTag name="isoCsTypeAvis" defaut="<%=viewBean.getIsoCsTypeAvis()%>" codeType="OSIOGTYA" />
            </TD>
          </TR>
          <TR class="classIso">
            <TD width="153">Statut d'ex&eacute;cution de l'ordre</TD>
            <TD width="10"></TD>
            <TD nowrap width="289">
            	
             <input type="text" name="hrIsoCsOrdreStatutExec" value="<%=viewBean.getHRIsoCsOrdreStatutExec()%>" class="inputDisabled" readonly>
            </TD>
            <TD width="123">Statut d'ex&eacute;cution de la transaction</TD>
            <TD width="139">
             <input type="text" name="htIsoCsTransmissionStatutExec" value="<%=viewBean.getHRIsoCsTransmissionStatutExec()%>" class="inputDisabled" readonly>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="153">Etat</TD>
            <TD width="10"></TD>
            <TD width="289">
              <INPUT type="hidden" name="transmis" value="<%=globaz.osiris.db.ordres.CAOrdreGroupeViewBean.TRANSMIS%>">
              <INPUT type="hidden" name="etat" value="<%=viewBean.getEtat()%>">
              <INPUT type="text" name="ucEtat" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="123"></TD>
            <TD width="139"></TD>
          </TR>
          <TR>
            <TD nowrap width="153" height="26">&nbsp;</TD>
            <TD width="10" height="26"></TD>
            <TD width="289" height="26">&nbsp; </TD>
            <TD width="123" height="26"></TD>
            <TD width="139"></TD>
          </TR>
          <TR>
            <TD nowrap width="153"></TD>
            <TD width="10"></TD>
            <TD nowrap width="289"></TD>
            <TD width="123"></TD>
            <TD width="139" nowrap></TD>
            <TD width="3"></TD>
            <TD nowrap width="1"></TD>
          </TR>
          <TR>
            <TD nowrap width="153">Montant total CHF</TD>
            <TD width="10"></TD>
            <TD width="289">
              <INPUT type="text" name="total" value="<%=viewBean.getTotalToCurrency().toStringFormat()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD width="123">Transactions</TD>
            <TD width="139">
              <input type="text" name="nombreTransactions" value="<%=viewBean.getNombreTransactions()%>" class="inputDisabled" tabindex="-1" readonly size="15" maxlength="15">
            </TD>
          </TR>
          <TR>
            <TD nowrap width="153">Date de cr&eacute;ation</TD>
            <TD width="10"></TD>
            <TD width="289">
              <input type="text" name="dateCr�ation" value="<%=viewBean.getDateCreation()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD width="123">Date de transmission</TD>
            <TD width="139">
              <input type="text" name="dateTransmission" value="<%=viewBean.getDateTransmission()%>" class="inputDisabled" tabindex="-1" readonly size="15" maxlength="15">
            </TD>
          </TR>
          <TR>
            <TD nowrap width="153">
				<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())) { %>
					<A href="osiris?userAction=osiris.comptes.apercuJournal.afficher&id=<%=viewBean.getIdJournal()%>">Journal associ&eacute;</A>
				<% } else { %>
					Journal associ&eacute;
				<% } %>
			</TD>
            <TD width="10"></TD>
            <TD width="289">
              <input type="text" name="idJournal" value="<%=viewBean.getIdJournal()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD width="123"></TD>
            <TD width="139"></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
<SCRIPT>
changeAffichage();
</SCRIPT>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdOrdreGroupe())) { %>
<ct:menuChange displayId="options" menuId="CA-OrdresGroupes" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdOrdreGroupe()%>"/>
	
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
	<ct:menuActivateNode active="yes" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% if (!"0".equals(viewBean.getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>"/>
	<% } %>	
		
	<% if (CAOrdreGroupe.ANNULE.equals(viewBean.getEtat())) { %>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>"/>
		<ct:menuActivateNode active="no" nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>"/>
	<% } %>		
</ct:menuChange>

 <%} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>