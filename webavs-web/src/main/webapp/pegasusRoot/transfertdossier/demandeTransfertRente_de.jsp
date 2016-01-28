<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.pegasus.vb.transfertdossier.PCDemandeTransfertRenteViewBean"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_DEMANDE_TRANSFERT_RENTE"

	idEcran="PPC2007";
	
	PCDemandeTransfertRenteViewBean viewBean = (PCDemandeTransfertRenteViewBean)session.getAttribute("viewBean");
	
	String rootPath = servletContext+(mainServletPath+"Root");
	
	// Desactivation des boutons de détail
	bButtonCancel=false;
	bButtonUpdate=false;
	bButtonValidate=false;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/demande/transfert.css"/>
<SCRIPT language="javascript">
var userAction = "<%=IPCActions.ACTION_DEMANDE_TRANSFERT_RENTE_TRANSFERT %>.executer";

var t_idCaissesRefusant='<%=BSessionUtil.getSessionFromThreadContext().getApplication().getProperty(EPCProperties.LIST_ID_CAISSE_REFUSANT.getProperty()) %>'.split(',');

$(function(){
	
	setPrintBouton();

	setTimeout(function(){
		$('#imgCaisseAcceptant').hide();
		$('#imgCaisseRefusant').hide();
	},10);
	
});

function setPrintBouton(){
	$('<input/>',{
		type: 'button',
		value:'<%=objSession.getLabel("JSP_DEMANDE_TRANSFERT_RENTE_D_BTN_TRANSFERT") %>',
		id:'btnPrint',
		click:function(){
			$("[name=userAction]").val(userAction);
			$("[name=mainForm]").submit();
		}
	}).prependTo('#btnCtrlJade');
}

function init(){}
function add(){}
function upd(){}
function del(){}

function readOnly(flag) {
 	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" align="left">
								
				<div id="ligneRequerant">
					<span class="label"><ct:FWLabel key="JSP_PC_DECSUPP_D_REQ"/></span>
					<span id="valRequerant"><%=viewBean.getRequerantInfos() %></span>
					<a id="aGed" href="#" target="GED_CONSULT"><ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/></a>
				</div>	
				<div id="ligneGestionnaire">
					<span class="label"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_GESTIONNAIRE"/></span>
					<ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=controller.getSession().getUserId()%>" name="idGestionnaire"/>
					<span class="label" id="lblEmail"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_EMAIL"/></span>
					<span id="valEmail"><input type="text" name="mailAddress" value="<%=controller.getSession().getUserEMail()%>" /></span>					
				</div>
				<div id="ligneCaisseAgence">
				
					<span class="label"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_CAISSE"/></span>
				
					<ct:widget id='caisseAgence' name='caisseAgence' notation='data-g-string="mandatory:true"' styleClass="libelleLong selecteurHome">
						<input type="hidden" class="idAdmin1" />
						<ct:widgetService defaultLaunchSize="1" methodName="find" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">								
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_DEMANDE_TRANSFERT_RENTE_W_CODE"/>
						<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_DEMANDE_TRANSFERT_RENTE_W_NOM"/>																						
						<ct:widgetLineFormatter format="#{admin.codeAdministration} #{tiers.designation1} #{tiers.designation2}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									var codeAdmin=$(element).attr('admin.codeAdministration');
									this.value=codeAdmin+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									$('#idCaisseAgence').val($(element).attr('admin.idTiersAdministration'));
									$('#noCaisseAgence').val(codeAdmin);
									var isAcceptant=($.inArray(codeAdmin,t_idCaissesRefusant)==-1);
									$('#imgCaisseAcceptant').toggle(isAcceptant);
									$('#imgCaisseRefusant').toggle(!isAcceptant);
									$('.dateTransfert').toggle(isAcceptant);
								}
							</script>										
						</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
					<input type="hidden" name="noCaisseAgence" id="noCaisseAgence" value=""/>	
					<input type="hidden" name="idCaisseAgence" id="idCaisseAgence" value=""/>
				</div>
				<br/>
				<div id="ligneDateTransfert">
					<span>
						<span class="label"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_DATE_ANNONCE"/></span>
						<input type="text" data-g-calendar="mandatory:true,currentDate:true" value="<%=viewBean.getDateAnnonce() %>" name="dateAnnonce" />
					</span>
					<span class="dateTransfert">
						<span class="labelDateTransfert"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_DATE_TRANSFERT"/></span>
						<input type="text"  data-g-calendar="mandatory:true,type:month,currentDate:true" value="<%=viewBean.getDateTransfert() %>" name="dateTransfert" />
					</span>
				</div>	
				<div id="ligneInfoCaisse">
					<div data-g-boxmessage="type:WARN" id="imgCaisseAcceptant"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_CAISSE_ACCEPTANT"/></div>
					<div data-g-boxmessage="type:WARN" id="imgCaisseRefusant"><ct:FWLabel key="JSP_DEMANDE_TRANSFERT_RENTE_D_CAISSE_REFUSANT"/></div>
				</div>
				
		</TD>
	</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>