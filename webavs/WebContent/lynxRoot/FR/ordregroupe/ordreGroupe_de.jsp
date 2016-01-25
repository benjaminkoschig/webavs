<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0018"; %>
<%@ page import="globaz.lynx.db.ordregroupe.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.lynx.utils.LXOrdreGroupeUtil"%>
<%@ page import="globaz.lynx.parser.LXSelectBlockParser"%>

<%
	LXOrdreGroupeViewBean viewBean = (LXOrdreGroupeViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOrdreGroupe();
	bButtonDelete = false;
	if(!LXOrdreGroupeUtil.isOuvert(objSession, viewBean.getIdOrdreGroupe())){
		bButtonUpdate = false;
	}
	
	String selectOrganeExecutionSelect = LXSelectBlockParser.getIdOrganeExecutionSelectBlock(objSession, viewBean.getIdSociete(), viewBean.getIdOrganeExecution());
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script language="javascript">

$(document).ready(function() {
	$('#datePaiement').click(function () {
		$('#fixe').attr('checked', true);
	});
	$('#anchor_datePaiement').click(function () {
		$('#fixe').attr('checked', true);
	});
});

function add() {
    document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.afficher";
}

function del() {
	if (window.confirm("<ct:FWLabel key='GLX0018_CONFIRME_DELETE'/>")) {
        document.forms[0].elements('userAction').value="lynx.ordregroupe.ordreGroupe.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

function postInit() {
	if(document.getElementById("idOrganeExecution").value == '' )
		updateListOrganeExecution(document.getElementById("idSociete").value);
}

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;

		updateListOrganeExecution(element.idSociete);
	}
}

// fonction qui permet la création de la liste des organes d'execution
function updateListOrganeExecution(idSociete) {	
	elSel = document.getElementById('idOrganeExecution');
	for (i = elSel.length - 1; i>=0; i--) {
		elSel.remove(i);
	}

	elOptNew = document.createElement('option');
	elOptNew.text = '';
	elOptNew.value = '';
			
	elSel.add(elOptNew);

	//Debut du manager
	<%=LXSelectBlockParser.getListOrganeExecution(objSession)%>
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert("<ct:FWLabel key='GLX0018_SOCIETE_FAILURE'/>");
	}
}

top.document.title = "<ct:FWLabel key='GLX0018_TITRE'/> - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='GLX0018_TITRE'/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

            <%  // Initialisation des variables suivant si on est en ajout ou en modif
            	String dateAdd = viewBean.getDateCreation();
            	String etatAdd = viewBean.getUcEtat().getLibelle();
            	String proprietaireAdd = viewBean.getProprietaire();
            	
				if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
					dateAdd = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);
					etatAdd = viewBean.getUcEtat(LXOrdreGroupe.CS_ETAT_OUVERT).getLibelle();
					proprietaireAdd = objSession.getUserName();
				}
	        %>

		<tr>
            <td><ct:FWLabel key="GLX0018_NUMERO"/></td>
            <td colspan="2">
              <input type="text" name="idOrdreGroupe" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdOrdreGroupe()%>" class="libelleDisabled" readonly="readonly"  tabindex="-1"/>
            </td>
        </tr>
		<tr>
        	<td><ct:FWLabel key="GLX0018_DATE_ECHEANCE"/></td>
            <td colspan="2">
            	<ct:FWCalendarTag name="dateEcheance" doClientValidation="CALENDAR" value="<%=viewBean.getDateEcheance()%>" tabindex="1"/>
			</td>
		</tr>   
		<tr>
        	<td><ct:FWLabel key="GLX0018_DATE_PAIEMENT"/></td>
            <td colspan="2">
            	<input type="radio" name="typeDatePmt" id="echeance" value="<%=globaz.lynx.db.ordregroupe.LXOrdreGroupeViewBean.TYPE_DATE_PMT_ECHEANCE %>" tabindex="1" <%=globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDatePaiement())?"checked='checked'":"" %> /><label for="echeance"> <ct:FWLabel key="GLX0018_TYPE_PMT_ECHEANCE"/></label></label><br />
				<input type="radio" name="typeDatePmt" id="fixe" value="<%=globaz.lynx.db.ordregroupe.LXOrdreGroupeViewBean.TYPE_DATE_PMT_FIXE %>" <%=!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDatePaiement())?"checked='checked'":"" %> /><label for="fixe"> <ct:FWLabel key="GLX0018_TYPE_PMT_FIXE"/> <ct:FWCalendarTag name="datePaiement" doClientValidation="CALENDAR" value="<%=viewBean.getDatePaiement()%>" tabindex="1"/></label>
			</td>
		</tr>  
		<tr>
        	<td><ct:FWLabel key="GLX0018_DATE_CREATION"/></td>
            <td colspan="2">
				<INPUT type="text" name="" style="width:2.5cm" size="30" maxlength="30" value="<%=dateAdd%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1">    
 			</td>
		</tr>         
		<tr>
        	<td><ct:FWLabel key="GLX0018_DATE_TRANSMISSION"/></td>
            <td colspan="2">
				<INPUT type="text" name="" style="width:2.5cm" size="30" maxlength="30" value="<%=viewBean.getDateTransmission()%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1">    
 			</td>
		</tr>   
        <tr>
         	<TD><ct:FWLabel key="GLX0018_SOCIETE_DEBITRICE"/></TD>
         	<TD colspan="2">
				<%@ include file="/lynxRoot/include/societe.jsp" %>
				
				<%
					if (!"add".equals(request.getParameter("_method"))) {
				%>
					<script language="JavaScript">document.getElementById("idExterneSociete").className = "libelleLongDisabled";</script>				
				<%
					}
				%>
			</TD>
        </tr>    
         <tr>
            <TD><ct:FWLabel key="GLX0018_LIBELLE"/></TD>
            <TD colspan="2">
				 <input type="text" name="libelle" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getLibelle()%>" class="libelle" tabindex="1" />
            </TD>
        </tr>     
          <tr>
            <TD><ct:FWLabel key="GLX0018_ORGANE_EXECUTION"/></TD>
            <TD colspan="2">
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectOrganeExecutionSelect)) {
					out.print(selectOrganeExecutionSelect);
				}
			%>
            </TD>
        </tr> 
          <tr>
            <td><ct:FWLabel key="GLX0018_NUM_OG"/></td>
            <td colspan="2">
              <input type="text" name="numeroOG" size="3" maxlength="2" value="<%=viewBean.getNumeroOG()%>"  tabindex="1"/>
            </td>
          </tr>
         <tr>
            <TD><ct:FWLabel key="GLX0018_PROPRIETAIRE"/></TD>
            <TD colspan="2">
				 <input type="text" name="proprietaire" style="width:7cm" size="121" maxlength="20" value="<%=proprietaireAdd%>" class="libelleDisabled" readonly="readonly" tabindex="-1" />
            </TD>
        </tr>    
          <TR>
            <TD><ct:FWLabel key="GLX0018_ETAT"/></TD>
            <TD colspan="2">
				<input type="text" name="" style="width:7cm" value="<%=etatAdd%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1"/> 
			</TD>
        </tr>           
		<tr> 
			<TD height="11" colspan="4"> 
				<hr size="3" width="100%">
			</TD>
		</tr>    
          <tr>
            <TD><ct:FWLabel key="GLX0018_TOTAL_PAIEMENT"/></TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalPaiement()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
            </TD>
        </tr>        
          <tr>
            <TD><ct:FWLabel key="GLX0018_TOTAL_ESCOMPTE"/></TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalEscompte()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
            </TD>
        </tr>                   
		<tr> 
			<TD height="11" colspan="4"> 
				<hr size="3" width="100%">
			</TD>
		</tr>  
        <tr>
            <TD>
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalLie())) {
			%>
	              <A href="<%=request.getContextPath()%>/lynx?userAction=lynx.journal.journal.afficher&selectedId=<%=viewBean.getIdJournalLie()%>" class="link"><ct:FWLabel key="GLX0018_JOURNAL_ASSOCIE"/></A>
	              	<%
	              		} else {
	              	%>
					<ct:FWLabel key="GLX0018_JOURNAL_ASSOCIE"/>
	              <%
	              		}
	              	%>
			</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdJournalLie()%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1"/>
            </TD>
        </tr>    

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
	} else {
%>
<ct:menuChange displayId="options" menuId="LX-OrdreGroupe" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value="<%=viewBean.getIdOrdreGroupe()%>" checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value="<%=viewBean.getIdSociete()%>" checkAdd='no'/>
	<ct:menuSetAllParams key='idOrdreGroupe' value="<%=viewBean.getIdOrdreGroupe()%>" checkAdd='no'/>
	<ct:menuSetAllParams key='idOrganeExecution' value="<%=viewBean.getIdOrganeExecution()%>" checkAdd='no'/>
</ct:menuChange>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>