<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java"  %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/json2.js"/></script>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_DEM_L"
	PCDemandeListViewBean viewBean = (PCDemandeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "pegasus?userAction="+IPCActions.ACTION_DEMANDE_DETAIL+ ".afficher&selectedId=";
	menuName = "pegasus-menuprincipal";
%>

<script type="text/javascript">	

$(function (){	

	var synchroMembreFam ={
			idDemande: null,
			
			loadAndDisplayMembreFamToSynchronise: function () {
				var that = this;
				options = {};
				options.parametres= this.idDemande;
				
				options.serviceMethodName= "resolveEnfantToSynchroniseByIdDemande";
				options.callBack=function (data) {
					var template =  '<div><div id="membreFamilleToSync"><ul style="margin:15px 0; list-style-type:none">'+
							'{{@each toAdd}}' + 
								'<li style="padding-top:5px"><input type="checkbox" value="{{idMembreFamille}}" name="membreFamilleToSyn_{{idMembreFamille}}" style="margin-right:10px" />' + 
								'{{nss}} - {{nom}} {{prenom}} /{{dateNaissance}}/{{csSexe}}/{{csNationalite}}</li>' + 
							'{{/@each toAdd}}' + 
							'</ul></div></div>';
								
					$list = data.toAdd.length?window.parent.globazNotation.template.compile$(data, template):window.parent.$("<div> <ct:FWLabel key='JSP_PC_DEM_AJOUT_ENFANT_AUCUN' /> </div>");
					//$list.appendTo("body")
					var buttons = [];
	                window.parent.$(".ui-dialog").remove();
	                
					if(data.toAdd.length){
						function close(dialogue) {
				            window.parent.$(dialogue).dialog("close");
			                window.parent.$("#membreFamilleToSync").remove();
			                window.parent.$(dialogue).dialog("destroy").remove();
						}
						buttons.push({
				             text: '<ct:FWLabel key="JSP_PC_DEM_AJOUT_ENFANT_ANNULER"/>',
				             click: function() {
				            	 close(this);
				             }
						});
						buttons.push({
							 id:"buttoOkSynEnfant",
				             text: '<ct:FWLabel key="JSP_PC_DEM_AJOUT_ENFANT_OK"/>',
				             click: function() {
				 				var ids = [];
				 				$membreFamilleToSync.find("input:checked").each(function(){
									ids.push(this.value);
								});
					            that.synchroniseMembreSelected(ids);
					            close(this);
				             }
				           });
					} else {
						buttons.push({
				             text: '<ct:FWLabel key="JSP_PC_DEM_AJOUT_ENFANT_OK"/>',
				             click: function() {
				            	 close(this);
				             }
						});
					}
					$list.dialog({
						width: 700,
						minHeight: 220,  
						title: '<ct:FWLabel key="JSP_PC_DEM_AJOUT_ENFANT_TITLE"/>',
					    position: { my: "center", at: "center top-5%" },
					    close: function( event, ui ) {
					    	 close(ui);
					    },
						buttons: buttons
					});
					
					var $buttoOkSynEnfant  = window.parent.$("#buttoOkSynEnfant").button("disable",true);

					
					var $membreFamilleToSync = window.parent.$("#membreFamilleToSync");
					
					$membreFamilleToSync.on('click', 'input', function (){
						$buttoOkSynEnfant.button("disable",true);
						$membreFamilleToSync.find("input:checked").each(function(){
							$buttoOkSynEnfant.button("enable", true);
						});
					})
	 	

				};
				this.executeAjax(options);
			},

			executeAjax: function (options) {
				var ajax;
				o_options = {
					serviceClassName: "ch.globaz.pegasus.business.services.models.droit.DroitService",
					criterias: '',
					cstCriterias: '',
					errorCallBack: null
				};
				o_options = $.extend(o_options, options);
				ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
				ajax.options = o_options;
				ajax.read();
			},
			
			synchroniseMembreSelected: function (ids) {
				options = {};
				options.parametres= JSON.stringify({
						idsMembreFamille: ids,
						idDemande: this.idDemande
				});
				
				options.serviceMethodName= "addMembreFamilleByIdMembreFamille";
				options.callBack = function (data) {};
				this.executeAjax(options);
			}
		}

	overLoadActionMenu("pegasus.droit.droit.synchroniser", /(idDemandePc)=(\d*).*(noVersionDroit)=(\d*)/g, function (values, element, defaultAction){
		if(values.noVersionDroit === "1") {
			defaultAction();
		} else {
			synchroMembreFam.idDemande = values.idDemandePc;
			synchroMembreFam.loadAndDisplayMembreFamToSynchronise();
		}
	});
	
});

//resolveMembresFamilleToSynchronise	
	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.pegasus.vb.demande.PCDemandeListViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="globaz.pegasus.vb.demande.PCDemandeViewBean"%>
<%@page import="globaz.prestation.interfaces.fx.PRGestionnaireHelper"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%><TH>&nbsp;</TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DETAIL_ASSURE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DATE_DEPOT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_DATE_ARRIVEE_CC"/></TH>
   	<TH data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_DEM_L_PERIODE"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_ETAT"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_GEST"/></TH>
   	<TH><ct:FWLabel key="JSP_PC_DEM_L_NO"/></TH> 
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			PCDemandeViewBean line = (PCDemandeViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"&idDossier="+line.getListDemandes().getDossier().getId()+"'";
		

	
		%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="pegasus-optionsdemandes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="idDemandePc" value="<%=line.getListDemandes().getSimpleDemande().getId()%>"/> 
     			<ct:menuParam key="idTiers" value="<%=line.getListDemandes().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()%>"/>
     			<ct:menuParam key="idDossier" value="<%=line.getListDemandes().getDossier().getId()%>"/>
		 		<ct:menuParam key="isFratrie" value="<%=line.getIsFratrie() %>"/>
		 		<ct:menuParam key="idGestionnaire" value="<%=line.getIdGestionnaire() %>"/>
		 		<ct:menuParam key="idDecision" value="<%= line.getIdDecision()%>"/>
		 		<ct:menuParam key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_REFUS_SC %>"/>
		 		<ct:menuParam key="noVersionDroit" value="<%= line.getNumeroVersionDroit()%>"/>
		 		
		 		<!--  Si la demande est deja octroyee ou si deja une decision de refus,il n'est plus possibe de preparer une decision -->
		 		<% if(!line.isValidForPrepDecisionRefus()){%>
		 		<ct:menuExcludeNode nodeId="DECREFUS"/>
		 		<%}%>
		 		<!-- Si pas d'id decision de refus, pas de lien direct sur la decision de refus -->
		 		<% if(!line.hasDecisionDerefus()){%>
		 		<ct:menuExcludeNode nodeId="DECREFUS_DE"/>
		 		<%}%>
		 		<!-- Pas de synchoniser famille pour les fratries -->
		 		<% if(line.getDemande().getSimpleDemande().getIsFratrie()){%>
		 		<ct:menuExcludeNode nodeId="SYNCHRO_FAMILLE"/>
		 		<%}%>
		 		<!-- Si le transfert de dossier n'est pas possible, on cache l'option -->
		 		<% if(!line.isTransferable()){%>
		 		<ct:menuExcludeNode nodeId="DOSSIERS_TRANSFERT"/>
		 		<%}%>
		 	</ct:menuPopup>
     	</TD>		
		<TD class="mtd" nowrap onClick="<%=detailUrl%>">
			<div style="position:relative;">
				<%=PCUserHelper.getDetailAssure(objSession,line.getListDemandes().getDossier().getDemandePrestation().getPersonneEtendue())%>
				<span style="position:absolute; top:0; right:0" 
					 	data-g-note="idExterne:<%=line.getListDemandes().getSimpleDemande().getId()%>, 
					 				tableSource:PCDEMPC, inList: true">
				</span>
			</div>
		</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getListDemandes().getSimpleDemande().getDateDepot()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getListDemandes().getSimpleDemande().getDateArrivee()%></TD>
		<TD class="" nowrap onClick="<%=detailUrl%>"><%=line.getPeriode()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getListDemandes().getSimpleDemande().getCsEtatDemande())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDetailGestionnaire()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>