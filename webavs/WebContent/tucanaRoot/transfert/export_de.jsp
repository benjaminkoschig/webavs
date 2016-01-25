<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.tucana.vb.transfert.TUExportViewBean viewBean = (globaz.tucana.vb.transfert.TUExportViewBean) session.getAttribute("viewBean");
	String idBouclement = request.getParameter("idBouclement");
	String year = request.getParameter("year");
	String month = request.getParameter("month");
	idEcran = "TU-901";
	String btnExpEnLabel = objSession.getLabel("BTN_EXPORTER");
	boolean myError = false;


%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
function add() {}
function upd() {}
function validate() {}
function cancel() {}
function del() {}
function init(){
	document.getElementById("annee").focus();
}
function onClickExporter(){
    state = validateFields();
    document.forms[0].elements('userAction').value="tucana.transfert.export.generer";
	chargeUrl(state);
}



<% if (viewBean.getMsgType().equals (globaz.framework.bean.FWViewBeanInterface.ERROR)) {
	myError = true;
} else{
	myError = false;
}%>




<%-- méthode qui charge l'url (fait un submit) --%>
function chargeUrl(state){
	if (state){
		action(COMMIT);
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ct:FWLabel key="TIT_EXPORTATION"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="ID_BOUCLEMENT" />
							</TD>
							<TD>
								<ct:inputText defaultValue="<%=idBouclement%>" id="idBouclement" name="idBouclement" styleClass="numeroCourt" style="text-align: right;" readonly="true" tabindex="-1"/>								
							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="EMAIL"/></TD>
							
							<TD width="80%" style="font-weight: bold;">
								<ct:inputText name="eMail" styleClass="libelleLong" tabindex="1"/>
							<script>
								document.getElementsByName("email")[0].value ="<%=objSession.getUserEMail()%>";
							</script>

							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="ANNEE"/></TD>
							
							<TD width="80%">
								<ct:inputText defaultValue="<%=year%>" name="annee" styleClass="numero" style="text-align: right;" tabindex="2" id="annee"/>
							</TD>
						</TR>
						<TR>
							<TD width="20%"><ct:FWLabel key="MOIS"/></TD>
							
							<TD width="80%">
								<ct:inputText defaultValue="<%=month%>" name="mois" styleClass="numeroCourt" style="text-align: right;" tabindex="3" id="mois"/>
							</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- Bouton "Exporter" --%>
				<%if (!myError){%><INPUT type="button" class="btnCtrl" id="btnExpEn" value="<%=btnExpEnLabel%>" onclick="onClickExporter();" tabindex="4"><%} %>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<SCRIPT language="javascript">
<%-- rend innaccessible les boutons "valider", "ajouter", "modifier", "supprimer" --%>
<%if (bButtonValidate) {%>
	document.getElementById('btnVal').style.visibility="hidden";
<%}%>
<%if (bButtonNew) {%>
	document.getElementById('btnNew').style.visibility="hidden";
<%}%>	
<%if (bButtonUpdate) {%>
	document.getElementById('btnUpd').style.visibility="hidden";
<%}%>
<%if (bButtonDelete) {%>
	document.getElementById('btnDel').style.visibility="hidden";
<%}%>

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>