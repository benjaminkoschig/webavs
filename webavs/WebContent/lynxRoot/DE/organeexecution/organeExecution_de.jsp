<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0005"; %>
<%@ page import="globaz.lynx.db.organeexecution.*" %>
<%
LXOrganeExecutionViewBean viewBean = (LXOrganeExecutionViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdOrganeExecution();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<%@page import="globaz.lynx.db.organeexecution.LXOrganeExecutionViewBean"%><SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Ausführungsorgan zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.organeexecution.organeExecution.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

function postInit() {
	change(document.getElementById("csGenre"));
}


function updateCompte(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteCredit").value = element.idCompte;
		document.getElementById("libelleCompteCredit").value = element.libelleCompte;
		
	}
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Das Konto existiert nicht.");
	}
}

function change(element) {
	
	var selectOption = element.options[element.selectedIndex];
	var elementTransfert = document.getElementById("csModeTransfert");
	
	if(selectOption.value != '<%= LXOrganeExecution.CS_GENRE_POSTE%>') {
		for(i = 0; i < elementTransfert.length ;i++) {
			var option = elementTransfert.options[i];
			if(option.value == '<%= LXOrganeExecution.CS_MODE_TRANSFERT_MAIL%>') {
				option.selected = 'selected';
				elementTransfert.disabled = true;
			}
		}
	}
}

top.document.title = "Detail des Ausführungsorgans - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Ausführungsorgans<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	     <TR>
            <TD>Nummer</TD>
            <TD colspan="2">
                <input type="text" name="idOrganeExecution" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdOrganeExecution()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
          </TR>
         <TR>
            <TD>Schuldnerfirma </TD>
            <TD colspan="2">
            	<%
            		if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSociete())) {
            			viewBean.setIdSociete(request.getParameter("idSocieteDebitrice"));
            		}
            	%>
            
				 <input type="hidden" name="idSociete" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdSociete()%>" />
				 
				 <input type="text" style="width:14cm" size="10" maxlength="9" value="<%=globaz.lynx.utils.LXSocieteDebitriceUtil.getLibelle(objSession, viewBean.getIdSociete())%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>
		<TR> 
			<TD height="11" colspan="3"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
         <TR>
            <TD>Name</TD>
            <TD colspan="2">
				 <input type="text" name="nom" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getNom()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
         <TR>
            <TD>Art</TD>
            <TD colspan="2">
				 <ct:FWCodeSelectTag name="csGenre" defaut="<%=viewBean.getCsGenre()%>" codeType="LXGENRE" wantBlank="false" tabindex="1"/>
				 <script language="JavaScript">
					element = document.getElementById("csGenre");
				  	element.onchange = function() {change(this);}
				 </script>
            </TD>
        </TR>
         <TR>
            <TD>Mitglied-Nr.</TD>
            <TD colspan="2">
				 <input type="text" name="numeroAdherent" style="width:7cm" size="31" maxlength="30" value="<%=viewBean.getNumeroAdherent()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
         <TR>
            <TD>ESR Mitglied-Nr.</TD>
            <TD colspan="2">
				 <input type="text" name="numeroAdherentBVR" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getNumeroAdherentBVR()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
         <TR>
            <TD>DTA Identifizierung</TD>
            <TD colspan="2">
				 <input type="text" name="identifiantDta" style="width:7cm" size="6" maxlength="5" value="<%=viewBean.getIdentifiantDta()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
         <TR>
            <TD>Bereich der Zahlungsadresse</TD>
            <TD colspan="2">
            	<ct:FWCodeSelectTag name="csDomaine" defaut="<%=viewBean.getCsDomaine()%>" codeType="PYAPPLICAT" wantBlank="false" tabindex="1"/>
            </TD>
        </TR>
         <TR>
            <TD>Übertragungsmodus</TD>
            <TD colspan="2">
				 <ct:FWCodeSelectTag name="csModeTransfert" defaut="<%=viewBean.getCsModeTransfert()%>" codeType="OSIMODTRA" wantBlank="false" tabindex="1"/>
            </TD>
        </TR>
		<TR> 
			<TD height="11" colspan="3"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
         <TR>
            <TD>Kreditkonto</TD>
            <TD>
				<%
					String jspLocation = servletContext + "/lynxRoot/autocomplete/compte_select.jsp";
					String params = "forDate=" + globaz.globall.util.JACalendar.todayJJsMMsAAAA() + "&idMandat=" + viewBean.getIdMandat();
					int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
				%>            
            
            	<input type="hidden" name="idCompteCredit" value="<%=viewBean.getIdCompteCredit()%>"/>
				<ct:FWPopupList name="idExterneCompteCredit" onFailure="onCompteFailure(window.event);" onChange="updateCompte(tag)"  validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteCredit()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
            </TD>
            <TD>
            	<input type="text" name="libelleCompteCredit" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getLibelleCompteCredit()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>
		<TR> 
			<TD height="11" colspan="4"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
        <TR>
            <TD>Zahlungsadresse</TD>
            <TD>&nbsp;</TD>
            <TD align="left" colspan="2">
				<%
					if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) {
				%>			
					<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Partner</A>
				<% } else { %>
				&nbsp;
				<% } %>
            </TD>	
        </TR>
        
        
        <TR>
            <TD>
            <TEXTAREA rows="9" cols="30" class="libelleLongDisabled" class="libelleLongDisabled" readonly="readonly"><%=(!"ADD".equalsIgnoreCase(request.getParameter("_method"))?viewBean.getAdressePaiementAsString():"")%>
            </TEXTAREA>
            </TD>

        </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>