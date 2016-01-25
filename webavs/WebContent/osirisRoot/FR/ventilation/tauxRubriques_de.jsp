<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4021"; %>
<%
	bButtonUpdate = false;
	bButtonDelete = false;
	globaz.osiris.db.ventilation.CATauxRubriquesViewBean viewBean = (globaz.osiris.db.ventilation.CATauxRubriquesViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdTauxRubrique();
	userActionValue = "osiris.osiris.ventilation.tauxRubriques.modifier";
	String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";




%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="osiris.ventilation.tauxRubriques.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
	}
	function upd() {
	}
	function add(){
	}

	function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.ventilation.tauxRubriques.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.ventilation.tauxRubriques.modifier";
       return state;

	}
	function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="osiris.ventilation.tauxRubriques.afficher";
	}
	<%
	String jspCaissesProfSelectLocation = servletContext + mainServletPath + "Root/caissesprof_select.jsp";
	%>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un taux<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>Rubrique</td>
							<TD>&nbsp;</TD>
							<td><ct:FWPopupList
							validateOnChange="true" value="<%=viewBean.getNumeroRubrique()%>"
							name="numeroRubrique" size="15" className="visible" jspName="<%=jspLocation%>"
							minNbrDigit="3" autoNbrDigit="11" />

							</td>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</tr>
						<tr>
							<td>Taux employeur</td>
							<TD>&nbsp;</TD>
							<td>
								<input type="text" name="tauxEmployeur" value="<%=viewBean.getTauxEmployeur()%>">
							</td>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</tr>
						<tr>
						<td>Taux salarié</td>
							<TD>&nbsp;</TD>
							<td>
								<input type="text" name="tauxSalarie" value="<%=viewBean.getTauxSalarie()%>">
							</td>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</tr>
						<tr>
							<td>Date</td>
							<TD>&nbsp;</TD>
							<td>
								<input type="text" name="date" size="10" value="<%=viewBean.getDate()%>">
								<input value="..." type=button name="anchor_date" id="anchor_date" onClick="calendar.select(date,'anchor_date','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(date,'CALENDAR')" >
							</td>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
            				<TD nowrap width="129">Caisse professionnelle</TD>
            				<TD width="1">&nbsp;</TD>
            				<TD width="147">
            					<input type="hidden" id="idCaisseProf" name="idCaisseProf" value="<%=viewBean.getIdCaisseProf()%>" >
            					<input type="text" id="numeroCaisseProf" name="numeroCaisseProf" value="<%=viewBean.getCaisseProfessionnelleNumero()%>" >
            				</TD>
            				<TD>&nbsp;</TD>
            				<TD>
              					<input type="text" name="caisseProfDescription" size="30" value="<%=viewBean.getCaisseProfessionnelleLibelle()%>" class="libelleLongDisabled"  readonly tabindex="-1">
            				</TD>
           					<TD >&nbsp;</TD>



						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>