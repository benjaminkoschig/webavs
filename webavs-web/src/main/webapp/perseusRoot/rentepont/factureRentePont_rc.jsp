<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeSoin"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%

	idEcran="PPF1232";
	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PF_DOS_R_TRIER_PAR_NOM")});	
	rememberSearchCriterias = false;
	
	String idQdRentePont = "";
	if (!JadeStringUtil.isEmpty(request.getParameter("idQdRentePont"))) {
		idQdRentePont = request.getParameter("idQdRentePont");
	}
	String idDossier = "";
	if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
		idDossier = request.getParameter("idDossier");
	}

	bButtonNew = false;
	
%>
<%-- /tpl:insert --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "perseus.rentepont.factureRentePont.lister";
	
	//TODO: A corriger, appele d'un service dans une JSP !!! Il faut simuler un viewBean
	var sousTypesSoins = <%=PerseusServiceLocator.getTypesSoinsRentePontService().getAllSousTypesInJson(objSession) %>

	listeSelect = {
			$selectSousTypeSoin: null,
			
			init: function () {
				this.$selectMaster = $("#typeSoin");
				this.$selectSousTypeSoin=$("#sousTypeSoin");	
				this.$selectSousTypeSoin.hide();
				this.addEvent();
				
				if($.trim(this.$selectMaster.val()).length){
					this.addOptions(this.$selectMaster.val());
					
				}
			},
			
			createOptions : function (codeSystemParent) {
				var options = "",
				     typeSoins = sousTypesSoins[codeSystemParent];
				for ( key in typeSoins) {
					options += "<option value='"+key+"'>"+typeSoins[key]+"</option>";
				}
				return options;
			},
			
			addOptions: function (codeSystemParent) {
				this.$selectSousTypeSoin.children().remove();
				this.$selectSousTypeSoin.append(this.createOptions(codeSystemParent));
				this.$selectSousTypeSoin.show();
			},
			
			addEvent: function () {
				var that = this;
				this.$selectMaster.change(function () {
					that.addOptions(this.value);
				});
			}		
	}
	
	$(function(){
		listeSelect.init();
		
		<%if(JadeStringUtil.isEmpty(idQdRentePont)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdQdRentePont,[for=forIdQdRentePont]').hide().after('&nbsp;');
		<%}%>
		
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
		<%}%>
		
	})
	
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,#forIdDossier,#searchModel\\.forIdGestionnaire,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forCSTypeQD,#searchModel\\.forAnnee,#searchModel\\.forCsEtatFacture').val('');
		$('#partialsearchModel\\.likeNss').focus();
	}
		
</SCRIPT>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_FACRP_R_TITRE" /><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="5" width="100%">
				<TR>
					<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PF_FACRP_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="searchModel.hasPostitField" id="hasPostitField" value="true"></TD>
					<TD>
						<nss:nssPopup avsMinNbrDigit="99"
								  nssMinNbrDigit="99"
								  newnss=""
								  name="searchModel.likeNss"/>								  
					</TD>
					<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_FACRP_R_NOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likeNom" name="searchModel.likeNom" class="clearable" value=""></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_FACRP_R_PRENOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.likePrenom" class="clearable" value=""></TD>
				</TR>				<TR>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_FACRP_R_TYPE"/></LABEL>&nbsp;</TD>
					<TD colspan="3">
						<select name='searchModel.forCsTypeSoin' id="typeSoin"  notation="data-g-select='mandatory:true'">
						<option value=''></option>
						<%
							//TODO:Pareil qu'en haut, simuler en viewBean
							HashMap<String, String> mapSurTypes = PerseusServiceLocator.getTypesSoinsRentePontService().getMapSurTypes(objSession);
							for(String key1: mapSurTypes.keySet() ){
						%>
							<option value='<%= key1 %>'> <%= mapSurTypes.get(key1) %></option>
						<%} %>
						</select>
						<select name='searchModel.forCsSousTypeSoin' id="sousTypeSoin"></select>
					</TD>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_QD_R_ANNEE"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.forAnnee" class="clearable" value=""></TD>
				</TR>
				<tr>
					<TD><LABEL for="forCsEtatFacture"><ct:FWLabel key="JSP_PF_FACRP_R_ETAT_FACTURE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWCodeSelectTag codeType="<%=ch.globaz.perseus.business.constantes.IPFConstantes.CSGROUP_ETAT_FACTURE%>" name="searchModel.forCsEtatFacture" wantBlank="true" defaut="blank"/></TD>
					<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PF_QD_R_DOSSIER"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></TD>
					<TD><LABEL for="forIdQdRentePont"><ct:FWLabel key="JSP_PF_FACRP_R_QD"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdQdRentePont" id="forIdQdRentePont" value="<%=idQdRentePont%>" class="disabled" readonly tabindex="-1"></TD>
				</tr>
				<tr>
					<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_PF_QD_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=globaz.perseus.utils.PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forIdGestionnaire"/></TD>
					<TD colspan="4">&nbsp;</TD>
				</tr>
				<TR>
					<TD colspan="6">&nbsp;</TD>
				</TR>
				<TR>
					<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
					<TD colspan="3">&nbsp;</TD>
					<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="searchModel.orderBy" /></TD>
				</TR>
				
			</TABLE>
		</TD>
	</TR>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
