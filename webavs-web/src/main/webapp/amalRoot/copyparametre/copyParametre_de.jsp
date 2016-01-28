	<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.amal.vb.copyparametre.AMCopyParametreViewBean"%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Date" %>

<%
	idEcran = "AM1006";
	//View bean depuis la requete
	AMCopyParametreViewBean viewBean = (AMCopyParametreViewBean) session.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	autoShowErrorPopup = true;

	bButtonDelete = false;

	if (viewBeanIsNew) {
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel = false;
		bButtonUpdate = false;
		bButtonValidate = false;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
var userAction;
var ACTION_PARAM_COPY="<%=IAMActions.ACTION_PARAMETRES_COPY_PARAMETRES%>";

$(document).ready(function() {
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
});


function init() {
	
}

function launch() {
	var text ="Copier les paramètres : '"+$("#s_paramsToCopy option:selected").text()+"' de l'année "+$("#s_anneeSubsideToCopy").val()+" vers l'année "+$("#s_newAnnee").val();
	if (confirm(text)) {
		userAction.value = ACTION_PARAM_COPY + ".copyParams";
		document.forms[0].submit();
	}
}

function postInit(){
	$('.alwaysActive').removeProp('disabled');
}
</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext+(mainServletPath+"Root")%>/css/parametres/amalparametres.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
Copie de paramètres
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<!-- Pour que le bord haut des onglets ne soit pas masqué -->
						<div style="padding-bottom:5px;visible:hidden"></div>	
						
						<!-- *** Menu à onglets *** -->
						<%=AMParametresHelper.getOngletHtml(objSession,viewBean,IAMParametres.ONGLETS_PARAMETRES,request,servletContext + mainServletPath)%>
						<!-- ***  /menu onglets  **** -->
						<div style="border:1px solid black; padding:10px">
								<table border="0">
									<tr>
										<td width="120px">Année à copier</td>
										<td><input type="text" class="alwaysActive" name="s_anneeSubsideToCopy" id="s_anneeSubsideToCopy" value="" size="4" data-g-integer="sizeMax:4,mandatory:true" maxlength="4"/></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td>Paramètres à copier</td>
										<td>
											<select class="alwaysActive" id="s_paramsToCopy" name="s_paramsToCopy">
												<option value="1">Subsides par année</option>												
												<option value="2">Paramètres annuels</option>
												<option value="3">Déduction fiscales enfants</option>												
											</select>
										</td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td width="120px">Nouvelle année</td>
										<td><input type="text" class="alwaysActive" name="s_newAnnee" id="s_newAnnee" value="" size="4" data-g-integer="sizeMax:4,mandatory:true" maxlength="4"/></td>
										<td></td>
										<td></td>
									</tr>
								</table>	
								<div align="right">	
									<ct:ifhasright element="<%=userActionNew%>" crud="u">
										<input type="button" class="bt_search alwaysActive" value="Copier" onclick="launch()"/>
									</ct:ifhasright>
								</div>						
							</div>
					</div>
				</TD>
			</TR>			
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>