
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA0068"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CALotsRetoursViewBean viewBean = (CALotsRetoursViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";

bButtonDelete = bButtonDelete && viewBean.isSupprimable();
bButtonUpdate = bButtonUpdate && viewBean.isModifiable();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.osiris.db.retours.CALotsRetoursViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.osiris.db.retours.CALotsRetours"%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.ajouter";
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.modifier";
  
  <%if(CALotsRetours.CS_ETAT_LOT_COMPTABILISE.equals(viewBean.getCsEtatLot())){%>
  	document.getElementById("idExterneRubriqueEcran").readOnly=true;
	document.getElementById("idExterneRubriqueEcran").disabled=true;
	
	document.getElementById("montantTotal").readOnly=true;
	document.getElementById("montantTotal").disabled=true;
	
	document.getElementById("libelleLot").readOnly=true;
	document.getElementById("libelleLot").disabled=true;
  <%}%>
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Rolle zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.retours.lotsRetours.chercher";
}

function init(){
	validateFloatNumber(document.forms[0].elements('montantTotal'));
}

function updateRubrique(el) {
	if(el!=null){
		if (el.value== "" || el.options[el.selectedIndex] == null)
			rubriqueManuelleOn();
		else {
			var elementSelected = el.options[el.selectedIndex];
			document.getElementById('idCompteFinancier').value = elementSelected.idCompte;
			document.getElementById('rubriqueDescription').value = elementSelected.rubriqueDescription;
		}
	}
}

function rubriqueManuelleOn(){
	document.getElementById('idCompteFinancier').value="";
	document.getElementById('rubriqueDescription').value="";
}

top.document.title = "Retours - détail d'un lot- " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Jobs<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD>Nummer</TD>
            <TD> 
              <INPUT type="text" name="idLotAffiche" readonly class="disabled" value="<%=viewBean.getIdLot()%>">
            </TD>
            <TD>Status</TD>
            <TD> 
              <INPUT type="text" name="CsEtatLotLibelle" readonly class="disabled" value="<%=JadeStringUtil.isEmpty(viewBean.getCsEtatLot())?viewBean.getSession().getCodeLibelle(CALotsRetoursViewBean.CS_ETAT_LOT_OUVERT):viewBean.getCsEtatLotLibelle()%>">
              <INPUT type="hidden" name="csEtatLot" value="<%=JadeStringUtil.isEmpty(viewBean.getCsEtatLot())?CALotsRetoursViewBean.CS_ETAT_LOT_OUVERT :viewBean.getCsEtatLot()%>">
            </TD>
          </TR>
          
          <TR><TD colspan="4">&nbsp;</TD></TR>
          
          <TR>
	        <TD>Bezeichnung</TD>
		  	<TD><INPUT type="text" name="libelleLot" value="<%=viewBean.getLibelleLot()%>"  size="40" maxlength="40"> </TD>
		  	<TD>Gesamtbetrag</TD>
		  	<TD><INPUT type="text" name="montantTotal" value="<%= viewBean.getMontantTotal()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
          </TR>
          
          <TR>
			<TD>Buchungsdatum</TD>
			<TD><ct:FWCalendarTag name="dateLot" value="<%=JadeStringUtil.isEmpty(viewBean.getDateLot())?JACalendar.todayJJsMMsAAAA():viewBean.getDateLot()%>"/></TD>
          </TR>

          <TR>
            <TD>Rubrik</TD>

            <TD>
            	<%if(viewBean.getCompte()==null){ %>
            	<ct:FWPopupList name="idExterneRubriqueEcran"
            	onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value=""
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"
				 />
				<%}else{%>
            	<ct:FWPopupList name="idExterneRubriqueEcran"
            	onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value="<%=viewBean.getCompte().getIdExterne()%>"
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"
				 />
				<%}%>
			&nbsp;
            <input type="hidden" name="idCompteFinancier" value="<%=viewBean.getIdCompteFinancier()%>" >
            <input type="text" name="rubriqueDescription" size="30" value="<%if (viewBean.getCompte() != null) {%><%=viewBean.getCompte().getDescription()%><%}%>" class="libelleLongDisabled"  readonly tabindex="-1">
            </TD>
            
            <%if(JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalCA())){%>
				<TD colspan="2">&nbsp;</TD>
			<%}else{%>
				<TD>&nbsp;</TD>
				<TD>
					<A href="/webavs/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournalCA()%>" class="link">Journal</A>
				</TD>
			<%}%>
          </TR>
          
          <TR>
            <TD>Auftragsart</TD>
		  	<TD>
              <ct:FWCodeSelectTag codeType="OSIORDLIV" name="csNatureOrdre" defaut="<%=viewBean.getCsNatureOrdre()%>" wantBlank="true"/>
		  	</TD>
		  	<TD colspan="2"></TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>