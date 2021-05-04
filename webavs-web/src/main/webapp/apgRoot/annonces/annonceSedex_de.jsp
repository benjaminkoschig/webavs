<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.apg.enums.APConstantes"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.controller.FWAction"%>
<%@page import="globaz.pyxis.db.tiers.TIPersonne"%>
<%@page import="globaz.apg.api.annonces.IAPAnnonce"%>
<%@page import="globaz.apg.vb.annonces.APAnnonceSedexViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.menu.MenuPrestation" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0022";

	APAnnonceSedexViewBean viewBean = (APAnnonceSedexViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdAnnonce();
	bButtonCancel = true;
	//bButtonDelete = bButtonDelete && (!viewBean.getEtat().equals(globaz.apg.api.annonces.IAPAnnonce.CS_ENVOYE));
	if (IAPAnnonce.CS_ERRONE.equals(viewBean.getEtat())) {
		bButtonDelete = true;
	} else {
		bButtonDelete = false;
	}
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
	String readOnly = "styleClass='disabled' readonly='true'";
	
	// Création lien sur la page des prestation
	String typePrestation = "typePrestation=" + viewBean.getTypePrestation();
	String idDroit = "forIdDroit=" + viewBean.getIdDroit();
	String lienSurPrestation = request.getContextPath() + "/apg?userAction=" + IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + "." + FWAction.ACTION_CHERCHER + "&" + typePrestation + "&" + idDroit;

%>
	

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

    <ct:menuChange displayId="menu" menuId="<%=MenuPrestation.of(viewBean.getTypeDemande()).getMenuIdPrincipal()%>" showTab="menu"/>
    <ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.ajouter";
  }

  function upd() {
	document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.modifier";
  }
  
  var ACTION_DROIT = "apg.annonces.annonceSedex";

  
  function validate() {
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.ajouter";
    } else {
        document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.modifier";
    }

    action(COMMIT);
    return false;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="apg.annonces.annonceAPG.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.supprimer";
        document.forms[0].submit();
    }
  }

	function init(){
		<% if (viewBean.isOnlyNss()) { %>
	  		readOnly(true);
			$("#insurant")[0].disabled = false;
		<% } %>
	}
	
	//Renvoi d'un seul message, on enlève le enveloppeMessageId
	function reenvoyerMessage() {
		if (window.confirm("<ct:FWLabel key='APG_REENVOYER_MESSAGE_CONFIRMATION'/>")){
			$("#envelopeMessageId").val("");
	        document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.reenvoyer";
	        document.forms[0].submit();
	    }
	}
	
	function reenvoyerMessages() {
	    if (window.confirm("<ct:FWLabel key='APG_REENVOYER_MESSAGES_CONFIRMATION'/>")){
	        document.forms[0].elements('userAction').value="apg.annonces.annonceSedex.reenvoyer";
	        document.forms[0].submit();
	    }
	}
	
	function postInit(){
		$('#btnCan').removeClass("inactive");
	}
  	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCE_APG_SEDEX"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD>
					<table cellspacing="3" width="100%">						
						<TR>
							<TD width="20%">messageId</TD>
							<TD width="30%"><ct:inputText name="champsAnnonce.messageId" id="messageId" styleClass="disabled" readonly="true"/></TD>
							<TD width="20%">businessProcessId</TD>
							<TD width="30%"><ct:inputText name="champsAnnonce.businessProcessId" id="businessProcessId" styleClass="disabled" readonly="true"/></TD>
						</TR>
						<TR>
							<TD>senderId</TD>
							<TD><ct:inputText name="champsAnnonce.senderId" id="senderId" styleClass="disabled" readonly="true"/></TD>
							<TD>recipientId</TD>
							<TD><ct:inputText name="champsAnnonce.recipientId" id="recipientId" styleClass="disabled" readonly="true"/></TD>
						</TR>
						<TR>
							<TD>messageDate</TD>
							<TD><ct:inputText name="champsAnnonce.messageDate" id="messageDate" styleClass="disabled" readonly="true"/></TD>
							<TD>messageType/subType</TD>
							<TD>
								<ct:inputText name="champsAnnonce.messageType" id="messageType" styleClass="disabled" readonly="true" style="width:50"/> -
								<ct:inputText name="champsAnnonce.subMessageType" id="subMessageType" styleClass="disabled" readonly="true" style="width:70"/>
							</TD>
						</TR>
						<TR>
							<TD>timeStamp</TD>
							<TD><ct:inputText name="champsAnnonce.timeStamp" id="timeStamp" styleClass="disabled" readonly="true"/></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD>accountingMonth</TD>
							<TD><ct:inputText name="champsAnnonce.accountingMonth" id="accountingMonth" styleClass="disabled" readonly="true"/></TD>
							<TD>deliveryOffice</TD>
							<TD>
								<ct:inputText name="champsAnnonce.deliveryOfficeOfficeIdentifier" id="deliveryOfficeOfficeIdentifier" styleClass="disabled" readonly="true" style="width:40"/> /
								<ct:inputText name="champsAnnonce.deliveryOfficeBranch" id="deliveryOfficeBranch" styleClass="disabled" readonly="true" style="width:40"/>
							</TD>
						</TR>
						<TR>
							<TD>serviceType</TD>
							<TD><ct:inputText name="champsAnnonce.serviceType" id="serviceType" style="width:40"/></TD>
							<TD>action</TD>
							<TD><ct:inputText name="champsAnnonce.action" id="action" style="width:40" styleClass="disabled" readonly="true"/></TD>
						</TR>
						<TR>
							<TD>referenceNumber</TD>
							<TD><ct:inputText name="champsAnnonce.referenceNumber" id="referenceNumber"/></TD>
							<TD>controlNumber</TD>
							<TD><ct:inputText name="champsAnnonce.controlNumber" id="controlNumber"/></TD>
						</TR>
						<TR>
							<TD>insurant</TD>
							<TD>
								<ct:inputText name="champsAnnonce.insurant" id="insurant"/>
							</TD>
							<TD>insurantMaritalStatus</TD>
							<TD><ct:inputText name="champsAnnonce.insurantMaritalStatus" id="insurantMaritalStatus" style="width:40"/></TD>
						</TR>
						<TR>
							<TD>insurantDomicileCanton</TD>
							<TD><ct:inputText name="champsAnnonce.insurantDomicileCanton" id="insurantDomicileCanton" style="width:40"/></TD>
							<TD>insurantDomicileCountry</TD>
							<TD><ct:inputText name="champsAnnonce.insurantDomicileCountry" id="insurantDomicileCountry" style="width:40"/></TD>
						</TR>
						<TR>
							<TD>activityBeforeService</TD>
							<TD><ct:inputText name="champsAnnonce.activityBeforeService" id="activityBeforeService" style="width:20"/></TD>
							<TD>paymentMethod</TD>
							<TD><ct:inputText name="champsAnnonce.paymentMethod" id="paymentMethod" style="width:20"/></TD>
						</TR>
						<TR>
							<TD>numberOfChildren</TD>
							<TD><ct:inputText name="champsAnnonce.numberOfChildren" id="numberOfChildren" style="width:40"/></TD>
							<TD>numberOfDays</TD>
							<TD><ct:inputText name="champsAnnonce.numberOfDays" id="numberOfDays" style="width:40" styleClass="disabled" readonly="true"/></TD>
						</TR>
						<TR>
							<TD>startOfPeriod</TD>
							<TD><ct:inputText name="champsAnnonce.startOfPeriod" id="startOfPeriod" notation="data-g-calendar=' '"/></TD>
							<TD>endOfPeriod</TD>
							<TD><ct:inputText name="champsAnnonce.endOfPeriod" id="endOfPeriod" notation="data-g-calendar=' '"/></TD>
						</TR>
						<TR>
							<TD>dailyIndemnityGuaranteeAI</TD>
							<TD><ct:inputText name="champsAnnonce.dailyIndemnityGuaranteeAI" id="dailyIndemnityGuaranteeAI" style="width:80"/></TD>
							<TD>allowanceFarm</TD>
							<TD><ct:inputText name="champsAnnonce.allowanceFarm" id="allowanceFarm" style="width:80"/></TD>
						</TR>
<%--						 Paternité--%>
						<%if (viewBean.getTypeDemande().isPaternite()) {%>
						<TR>
							<TD>newbornDateOfBirth</TD>
							<TD><ct:inputText name="champsAnnonce.newbornDateOfBirth" id="newbornDateOfBirth" notation="data-g-calendar=' '"/></TD>
							<TD>numberOfWorkdays</TD>
							<TD><ct:inputText name="champsAnnonce.numberOfWorkdays" id="numberOfWorkdays" style="width:80"/></TD>
						</TR>
						<TR>
							<TD>parternityLeaveType</TD>
							<TD><ct:inputText name="champsAnnonce.parternityLeaveType" id="parternityLeaveType" style="width:80"/></TD>
							<TD>childDomicile</TD>
							<TD><ct:inputText name="champsAnnonce.childDomicile" id="childDomicile" style="width:80"/></TD>
						</TR>
						<TR>
							<TD>childInsurantVn</TD>
							<TD><ct:inputText name="champsAnnonce.childInsurantVn" id="childInsurantVn"/></TD>
							<TD></TD>
							<TD></TD>
						</TR>
						<%}%>

<%--						Fin Paternité--%>

                        <%--Proche aidant--%>
                        <%if (viewBean.getTypeDemande().isProcheAidant()) {%>
                        <TR>
                            <TD>childInsurantVn</TD>
                            <TD><ct:inputText name="champsAnnonce.childInsurantVn" id="childInsurantVn" /></TD>
                            <TD>careLeaveEventID</TD>
                            <TD><ct:inputText name="champsAnnonce.careLeaveEventID" id="careLeaveEventID" /></TD>
                        </TR>
                        <%}%>

                        <%--Proche aidant--%>
						<TR>
							<TD>averageDailyIncome</TD>
							<TD><ct:inputText name="champsAnnonce.averageDailyIncome" id="averageDailyIncome" notation="data-g-amount=' '"/></TD>
							<TD>basicDailyAmount</TD>
							<TD><ct:inputText name="champsAnnonce.basicDailyAmount" id="basicDailyAmount" notation="data-g-amount=' '" styleClass="disabled" readonly="true"/></TD>
						</TR>
						<TR>
							<TD>allowanceCareExpenses</TD>
							<TD><ct:inputText name="champsAnnonce.allowanceCareExpenses" id="allowanceCareExpenses" notation="data-g-amount=' '"/></TD>
							<TD>totalAPG</TD>
							<TD>
								<ct:inputText name="champsAnnonce.totalAPG" id="totalAPG" notation="data-g-amount=' '" styleClass="disabled" readonly="true"/>
								<a href="<%=lienSurPrestation%>"> 
									<ct:FWLabel key="PRESTATION"/>
								</a>
							</TD>
						</TR>
						<TR>
							<TD>eventDate</TD>
							<TD><ct:inputText name="champsAnnonce.eventDate" id="eventDate" notation="data-g-calendar=' '"/></TD>
							<TD>breakRules</TD>
							<TD>
								<ct:inputText name="champsAnnonce.breakRules" id="breakRules"/>
								<span data-g-bubble="text:¦<ct:FWLabel key="JSP_ANNONCE_COMMENT_INSERER_BREAKRULES"/>¦">
									<strong>&nbsp;</strong>
								</span>	
							</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD align="right"><ct:FWLabel key="APG_ETAT"/> : </TD>
							<TD>
								<b><%=objSession.getCodeLibelle(viewBean.getChampsAnnonce().getCsEtat()) %></b>
								<ct:inputHidden name="champsAnnonce.csEtat" />
							</TD>
							<TD>&nbsp;</TD>
						</TR>
						<tr>
							<td>
								<input type="hidden" id="<%=APConstantes.PARAMETER_DO_LOAD_ANNONCE_DTO.getValue()%>" name="<%=APConstantes.PARAMETER_DO_LOAD_ANNONCE_DTO.getValue()%>" value="true"/>
							</td>
						</tr>
						<% if (IAPAnnonce.CS_ENVOYE.equals(viewBean.getEtat())) { %>
							<TR>
								<ct:inputHidden name="champsAnnonce.envelopeMessageId" id="envelopeMessageId"/>
								<TD align="center" colspan="4">
								<ct:ifhasright element="apg.annonces.annonceSedex.reenvoyer" crud="u">
									<a href="#" onclick="reenvoyerMessage()"><ct:FWLabel key="APG_REENVOYER_MESSAGE"/></a><br/>
									<a href="#" onclick="reenvoyerMessages()"><ct:FWLabel key="APG_REENVOYER_MESSAGES"/></a>
								</ct:ifhasright>
								</TD>
							</TR>
						<% } %>
					</table>
				</TD>
			</TR>
<%@ include file="../droits/plausibilites.jsp" %>		
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
