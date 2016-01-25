<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0055"; %>
<%
	globaz.osiris.db.avance.CAAvanceViewBean viewBean = (globaz.osiris.db.avance.CAAvanceViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPlanRecouvrement();	
%>
	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

<!--hide this script from non-javascript-enabled browsers
	function add() {  
	  	document.forms[0].elements('userAction').value="osiris.avance.ajouter";
	}

	function upd() {		
	  	document.forms[0].elements('userAction').value="osiris.avance.modifier";	
	}

	function validate() {
	    state = validateFields();
	    
	    if (document.forms[0].elements('_method').value == "add") {
	        document.forms[0].elements('userAction').value="osiris.avance.ajouter";	    
	    } else {
	        document.forms[0].elements('userAction').value="osiris.avance.modifier";
	    }	    

	    return state;	
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="back";
		} else {
			document.forms[0].elements('userAction').value="osiris.avance.afficher";
		}					
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'avance séléctionnée! Voulez-vous continuer?")) {
	        document.forms[0].elements('userAction').value="osiris.avance.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
	
	function updateIdCompteAnnexe(tag){
		if(tag.select && tag.select.selectedIndex != -1){	
			document.getElementById('idCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedIdCompteAnnexe;
			document.getElementById('descCompteAnnexe').value = tag.select[tag.select.selectedIndex].selectedCompteAnnexeDesc;
		}
	}
	
	top.document.title = "Détail d'une avance - " + top.location.href;
// stop hiding -->
</SCRIPT>

<ct:menuChange displayId="options" menuId="CA-Avance" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
</ct:menuChange>

	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une avance<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD class="label">Compte</TD>
		<TD class="control">
		<input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>" >

		<%
			int autoCompleteStart = globaz.osiris.parser.CAAutoComplete.getCompteAnnexeAutoCompleteStart(objSession);
			String jspAffilieSelectLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
		%>
              
        <ct:FWPopupList 
	    	name="idExterneRoleEcran" 
	        value="<%=viewBean.getIdExterneRole()%>" 
	        className="libelle" 
	        jspName="<%=jspAffilieSelectLocation%>" 
	        autoNbrDigit="<%=autoCompleteStart%>"
	        size="25"
	        onChange="updateIdCompteAnnexe(tag);"
	        minNbrDigit="1"
	    />
		</TD>
	
		<TD class="label">Nom de l'affilié</TD>
		<TD class="control"><input type="text" name="descCompteAnnexe" maxlength="29" value="<%=viewBean.getCompteAnnexeNom()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
	</TR>

	<TR>
		<TD class="label">Libellé</TD>
		<TD class="control"><INPUT type="text" name="libelle" value="<%=viewBean.getLibelle()%>"></TD>
	
		<TD class="label">Mode</TD>
		<TD class="control">
			<%@page import="globaz.osiris.db.avance.CAAvanceViewBean"%>
			<ct:FWListSelectTag data="<%=CAAvanceViewBean.modesPourUtilisateurCourant(objSession, CAAvanceViewBean.CODES_EXCLUS_POUR_ECRANS, false)%>" defaut="<%=viewBean.getIdModeRecouvrement()%>" name="idModeRecouvrement"/>			
		</TD>		
	</TR>
	
	<TR>
		<TD class="label">Echéance</TD>
		<TD class="control">
		<SELECT id="idTypeEcheance" name="idTypeEcheance">
			<OPTION <%if (viewBean.getIdTypeEcheance().equals(globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_ANNUELLE)) {%>selected<%}%> value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_ANNUELLE%>">Annuelle</OPTION>
			<OPTION <%if (viewBean.getIdTypeEcheance().equals(globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_MENSUELLE)) {%>selected<%}%> value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_MENSUELLE%>">Mensuelle</OPTION>
			<OPTION <%if (viewBean.getIdTypeEcheance().equals(globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_TRIMESTRIELLE)) {%>selected<%}%> value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_TRIMESTRIELLE%>">Trimestrielle</OPTION>
		</SELECT>
		</TD>
		
		<TD class="label">Etat</TD>
		<TD class="control"><ct:FWCodeSelectTag codeType="OSIPLRETA" defaut="<%=viewBean.getIdEtat()%>" name="idEtat"/></TD>
	</TR>
	<TR>
		<TD class="label">Date d'octroi</TD>
		<TD class="control"><ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/></TD>
		
		<TD class="label">1ère date d'échéance</TD>
		<TD class="control">
		
		<%
			String tmp = viewBean.getDateEcheance();
			
			if (globaz.jade.client.util.JadeStringUtil.isBlank(tmp)) {
				tmp = globaz.globall.util.JACalendar.today().toStr(".");
			}
		%>
		<ct:FWCalendarTag name="dateEcheance" doClientValidation="CALENDAR" value="<%=tmp%>"/>
		</TD>
	</TR>
	<TR>
		<TD class="label">Acompte</TD>
		<TD class="control"><INPUT type="text" name="acompte" value="<%=viewBean.getAcompteFormate()%>" class="montant"></TD>
		<TD class="label">1er Acompte</TD>
		<TD class="control"><INPUT type="text" name="premierAcompte" value="<%=viewBean.getPremierAcompteFormate()%>" class="montant"></TD>
	</TR>
	<TR>
		<TD class="label">Montant maximum à couvrir</TD>
		<TD class="control"><INPUT type="text" name="plafond" value="<%=viewBean.getPlafondFormate()%>" class="montant"></TD>
		<TD class="label">Date de fin</TD>
		<TD class="control"><ct:FWCalendarTag name="dateMax" doClientValidation="CALENDAR" value="<%=viewBean.getDateMax()%>"/></TD>
	</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>