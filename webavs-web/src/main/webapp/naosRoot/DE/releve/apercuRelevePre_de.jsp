<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.naos.translation.CodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.naos.util.AFUtil"%>
<%
	idEcran ="CAF0040";
	globaz.naos.db.releve.AFApercuReleveViewBean viewBean = (globaz.naos.db.releve.AFApercuReleveViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	String noAff = request.getParameter("noAff");
	if(noAff==null){
		noAff="";
	}
	String jspLocation = servletContext + mainServletPath + "Root/affilie_selectId.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
	
	bButtonNew = false;
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonValidate = false;
	bButtonCancel = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

function add(){}

function cancel() {
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.chercher";
}

function updateAffilieNumero(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('affilieNumero').value     = tag.select[tag.select.selectedIndex].value;
		document.getElementById('idTiers').value           = tag.select.options[tag.select.selectedIndex].idTiers;
		document.getElementById('descriptionTiers1').value = tag.select.options[tag.select.selectedIndex].designation1;
		document.getElementById('descriptionTiers2').value = tag.select.options[tag.select.selectedIndex].designation2;
		document.getElementById('affiliationId').value = tag.select.options[tag.select.selectedIndex].affiliationId;
		
		setPeriode();
	} 
}

function next() { 
	setPeriode();
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.afficher";
	document.forms[0].submit();
}

function postInit() {
	changeTypeReleve();
}

function init(){
	if (document.forms[0].elements('affilieNumero').value == '' && '<%=noAff%>'!= '') {
		document.forms[0].elements('affilieNumero').value='<%=noAff%>';
		affilieNumeroPopupTag.validate();
	}
	/*else if(document.forms[0].elements('affilieNumero').value == "nul.l"){
		document.forms[0].elements('affilieNumero').value='';
	}*/
}

function changeTypeReleve() {
	
	var typeSalaireDifferes =  <%=CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES%>;
	
	if (document.getElementById("type")!= null)
	{
		if (document.getElementById('type').value == typeSalaireDifferes){
			document.getElementById("tdAnneeTaux").style.display = 'block';
			document.getElementById("tdAnneeTaux2").style.display = 'block';
		} else {
			document.getElementById("tdAnneeTaux").style.display = 'none';
			document.getElementById("tdAnneeTaux2").style.display = 'none';
		}
	}
}

function setPeriode(){
	
	changeTypeReleve();
	
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.setPeriode";
	document.forms[0].submit(); 
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Auszugseingabe<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD nowrap width="110" height="31">Abr.-Nr.</TD>	
						<TD nowrap width="30"> 
							<INPUT type="hidden" name="apercuReleve" value='saisie'>
							<INPUT type="hidden" name="idTiers"    value='<%=viewBean.getIdTiers()%>'>
						</TD>
						<TD nowrap colspan="4"> 
						<% if (method != null && method.equalsIgnoreCase("add")) { 
							String 	designation1 = "";
							String 	designation2 = "";
							String 	affiliationId = "";
							if ( viewBean.getTiers() != null) {
								designation1 = viewBean.getTiers().getDesignation1();
								designation2 = viewBean.getTiers().getDesignation2();
								affiliationId = viewBean.getAffiliationId();
							}
						%>
							<ct:FWPopupList 
								name="affilieNumero" 
								value="<%=viewBean.getAffilieNumero()%>" 
								className="libelle" 
								jspName="<%=jspLocation%>" 
								autoNbrDigit="<%=autoDigiAff%>" 
								size="15"
								minNbrDigit="3"
								onChange="updateAffilieNumero(tag);"
								/>
							<IMG
								src="<%=servletContext%>/images/down.gif"
								alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
								title="presser sur la touche 'flèche bas' pour effectuer une recherche"
								onclick="if (document.forms[0].elements('affilieNumero').value != '') affilieNumeroPopupTag.validate();">
							&nbsp; 
							<input type="text" name="descriptionTiers1" value="<%=designation1%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=designation2%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
							<input type="hidden" name="affiliationId" value="<%=affiliationId%>" readonly="readonly" tabindex="-1">
						<% } else  { %>
							<input name="idExterneRole" value="<%=viewBean.getAffilieNumero()%>" readonly="readonly" tabindex="-1" class="disabled" style="width:100px">&nbsp;
							<input type="text" name="descriptionTiers1" value="<%=viewBean.getTiers().getDesignation1()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=viewBean.getTiers().getDesignation2()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
							<input type="hidden" name="affiliationId" value="<%=viewBean.getAffiliationId()%>" readonly="readonly" tabindex="-1">
						<% } %>
						</TD>
					</TR>
					<% if(viewBean.getAffiliation()!=null) { %>
					<TR>
						<TD width="140" height="31" colspan="2">Erfassungsplan</TD>
						<TD width="340" colspan="2">
							<select name="planAffiliationId">
								<%=globaz.naos.util.AFUtil.getPlanAffiliationInfoRom280FitreInactif(viewBean.getAffiliationId(), null, session, false)%>
							</select>
						</TD>
					</TR>
					<% } %>
					<TR>	
						<TD nowrap width="110" height="31">Abrechnungstyp</TD>	
						<TD nowrap width="30"></TD> 	
						<TD nowrap width="170">
							<ct:FWCodeSelectTag name="type"
								defaut="<%=viewBean.getType()%>" 
								codeType="VETYPERELE"
							/> 
							<script>
								document.getElementById("type").onchange = new Function("","setPeriode()");
							</script>
						</TD>	
					</TR>
					<TR>
					    <TD id="tdAnneeTaux" height="31" width="150">Année de référence pour le taux</TD>
					    <TD nowrap width="30"></TD> 	
						<TD id="tdAnneeTaux2" height="31" width="30"><INPUT name="anneeReference" id="anneeReference" size="4" maxlength="4"  onkeypress="return filterCharForPositivInteger(window.event);" value="<%= viewBean.getAnneeReference() %>" ></TD>    
						<TD colspan="3">&nbsp;</TD>
					</TR>					
					<TR>	
						<TD width="110" height="31">Periode</TD>	
						<TD width="30" align="right">von</TD>
						<TD colspan="4" nowrap>
						<%
						String varDateDeb = viewBean.getDateDebut();
						String varDateFin = viewBean.getDateFin();
						if(varDateDeb.length()==10){
						    varDateDeb = varDateDeb.substring(3);
						}
						if(varDateFin.length()==10){
						    varDateFin = varDateFin.substring(3);
						}
						%>
							<ct:FWCalendarTag name="dateDebut" displayType ="month"  
								value="<%=varDateDeb%>" doClientValidation="CALENDAR"/>
							&nbsp;bis&nbsp; 
							<ct:FWCalendarTag name="dateFin" displayType ="month" 
								value="<%=varDateFin%>" doClientValidation="CALENDAR"/>&nbsp;(Monat.Jahr)	
							&nbsp;&nbsp;<INPUT type="checkbox" name="forcePeriode"> Periode forcieren (Abrechnung von Erfassung)									
						</TD>
					</TR>
					<TR>
					
						<TD nowrap colspan="6" height="14">&nbsp;
						</TD>
					</TR>
								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT class="btnCtrl" id="btnNext" type="button" value=">>" onclick="next();">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>