<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.conventions.RFConventionViewBean"%>
<%@page import="globaz.cygnus.vb.conventions.RFFournisseurType"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersWrapper"%>

<%@page import="globaz.cygnus.vb.conventions.RFAssureConvention"%><script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/conventions/jsClasses/FournisseurSoin.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/tabsStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/dataTableStyle.css"/>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_D"
	idEcran="PRF0025";
	RFConventionViewBean viewBean = (RFConventionViewBean) session.getAttribute("viewBean");

	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonNew = false;
	bButtonCancel = false;
	bButtonValidate = false;
	
	String paramAjout = request.getParameter("_ajout");
		
	boolean ajoutFournisseur = "fournisseur".equals(paramAjout);
	boolean ajoutAssure = "assure".equals(paramAjout);
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>
<script language="JavaScript">

	servlet = "<%=(servletContext + mainServletPath)%>";

	//si une cellule est vide il faut mettre un espace pour l'affichage correcte des bordures
	<% String espace= "&nbsp;";%>
	
	function precedant(){		
		if (window.confirm("<ct:FWLabel key='WARNING_RF_CONV_MODIFICATIONS_PERDUES'/>")){
			document.getElementsByName("provenance")[0].value = "precedant";		
			
			document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.chercher";   
			return true;	
		}
	}	 

	//il s'agit de vider le contenu des tableaux assurés et type-fournisseur
	function annuler(){
		if (window.confirm("<ct:FWLabel key='WARNING_RF_CONV_MODIFICATIONS_PERDUES'/>")){
			actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.supprimer";						
			
			var myform=document.forms[0];		
			myform.elements("userAction").value = actionNew;	
			myform.action=servlet;
			myform.submit();			
		}
	}
	
	function validate(){		

		actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.ajouterBDD";

		var myform=document.forms[0];		
		myform.elements("userAction").value = actionNew;	
		myform.action=servlet;
		myform.submit();
	}
	
	function del() {
	}

	function add() {
	}
	
	function init(){
		
		document.getElementById("numeroAVS").disabled=true;
		document.getElementById("numeroAVS").readOnly=true;
		
		//si la case est cochée alors on affiche l'assuré
		if( $('#concerneAssure')[0].checked)
			afficheId('contenu');
		else
			cacheId('contenu');
		
		<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
		errorObj.text="<%=viewBean.getMessage()%>";
		showErrors();
		errorObj.text="";
		<%}%>			
	
		if(<%=ajoutAssure%>){			
			$("#numeroAVS").val("");		
		}				
	}

	function ajouterFournisseurType(){				
		
		actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.ajouterFournisseurType";

		//$('.adressePaiement').text(""); 
		//$('.descFournisseur').text("");
		
		var myform=document.forms[0];		
		myform.elements("userAction").value = actionNew;	
		myform.action=servlet;		
		myform.submit();		
	}
	
	function clearListesTypesDeSoins() {			
		
		document.getElementsByName("codeTypeDeSoin")[0].value="";
		document.getElementsByName("codeSousTypeDeSoin")[0].value="";
		document.getElementsByName("codeTypeDeSoinList")[0].value="";
		document.getElementsByName("codeSousTypeDeSoinList")[0].value="";	
	}


	function ajouterAssureTiers(){
		
		actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.ajouterAssure";
		detailLink = "<%=actionNew%>";

		var myform=document.forms[0];
		myform.elements("userAction").value =actionNew;
		myform.action=servlet;
		myform.submit();	  	  				
	}

	function postInit(){

		clearListesTypesDeSoins();		
		action('add');
		document.forms[0].method = "get";
	}

	// fonctions pour afficher / cacher la partie assuré
	function afficheId(baliseId) 
	  {
	  if (document.getElementById && document.getElementById(baliseId) != null) 
	    {
	    document.getElementById(baliseId).style.visibility='visible';
	    document.getElementById(baliseId).style.display='block';
	    }
	  }

	function cacheId(baliseId) 
	  {
	  if (document.getElementById && document.getElementById(baliseId) != null) 
	    {
	    document.getElementById(baliseId).style.visibility='hidden';
	    document.getElementById(baliseId).style.display='none';
	    }
	  }
	
	function getConcerneAssureValue(){			
		if( $('#concerneAssure')[0].checked){
			afficheId('contenu');			
		}
		else{
			cacheId('contenu');
		} 	
	}

	function changeConcerneAssureValue(){
		getConcerneAssureValue();
	}

	function suppressionFT(idFournisseur, idSousType){
		actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.supprimerCoupleFournisseurSTS";
		detailLink = "<%=actionNew%>";

		var myform=document.forms[0];
		myform.elements("userAction").value = actionNew;
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"idFournisseur",
			"value":idFournisseur
		});		
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"idSousType",
			"value":idSousType
		});		
		
		myform.action=servlet;
		myform.submit();		
	}

	function suppressionAssure(nssKey,dDebutKey,dFinKey){
		actionNew = "<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.supprimerAssure";
		detailLink = "<%=actionNew%>";

		var myform=document.forms[0];
		myform.elements("userAction").value = actionNew;
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"nssKey",
			"value":nssKey
		});	

		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"dDebutKey",
			"value":dDebutKey
		});	

		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"dFinKey",
			"value":dFinKey
		});	
			
		myform.action=servlet;
		myform.submit();		
	}	


	
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_SAISIE_CONVENTION_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_LIBELLE"/></TD>
					<TD colspan="6"><span><b><%=viewBean.getLibelle()%></b></span></TD>
				</TR>              
                <TR><TD colspan="6"><HR></HR></TD></TR>

                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                    <TD><ct:FWLabel key="JSP_RF_SAISIE_CONV_DESC_FOURNISSEUR"/>&nbsp;&nbsp;</TD>
                    <TD width="400px">
                    	<div><label class="descFournisseur" id="descFournisseur" >
                    	<%=viewBean.getDescFournisseur()%></label></div>
                    </TD>
                    <TD width="630px">
                    	<INPUT type="hidden" name="provenance" value="" />
                        <INPUT type="hidden" name="descFournisseur" class="PRlibelleLongDisabled" value="<%=viewBean.getDescFournisseur()%>" readonly />
                        <LABEL><ct:FWLabel key="JSP_TIERS"/></LABEL>
                        <ct:FWSelectorTag
                                    name="selecteurTiersFournisseur"
                                    methods="<%=viewBean.getMethodesSelecteurFournisseur()%>"
                                    providerApplication="pyxis"
                                    providerPrefix="TI"
                                    providerAction="pyxis.tiers.tiers.chercher"
                                    target="fr_main"
                                    redirectUrl="<%=mainServletPath%>"/>


                        <LABEL><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL>
                        <ct:FWSelectorTag
                                name="selecteurAdminFournisseur"
                                methods="<%=viewBean.getMethodesSelecteurFournisseur()%>"
                                providerApplication="pyxis"
                                providerPrefix="TI"
                                providerAction="pyxis.tiers.administration.chercher"
                                target="fr_main"
                                redirectUrl="<%=mainServletPath%>"/>
                    </TD>
                </TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>                
                <TR>				
					<TD>
						<ct:FWLabel key="JSP_RF_SAISIE_CONV_ADRESSE_DE_PAIEMENT"/>&nbsp;&nbsp; 
					</TD>
					<TD width="400px">                                     
						<INPUT type="hidden" name="descAdressePaiement" class="PRlibelleLongDisabled" value="<%=viewBean.getDescAdressePaiement()%>" readonly />
						<P class="adressePaiement" ><%=viewBean.getDescAdressePaiement()%></P>
					</TD>
					<TD width="630px">                  
                        <LABEL><ct:FWLabel key="JSP_TIERS"/></LABEL>
                        <ct:FWSelectorTag
                                    name="selecteurTiersAdressePaiement"
                                    methods="<%=viewBean.getMethodesSelecteurAdressePaiement()%>"
                                    providerApplication="pyxis"
                                    providerPrefix="TI"
                                    providerAction="pyxis.tiers.tiers.chercher"
                                    target="fr_main"
                                    redirectUrl="<%=mainServletPath%>"/>
                    </TD>
                </TR>                
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
				<TR>
               		<TD colspan="6">
	                	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
	                	<INPUT type="hidden" name="isEditSoins" value="false"/>
	                	<INPUT type="hidden" name="isSaisieQd" value="false"/>	                	
               		</TD>
               	</TR> 
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
                	<TD>
						<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_AK_REQ_AJOUTER"/>" onclick="ajouterFournisseurType()" />
					</TD>
				</TR>      
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<!-- Création du tableau couple type de soin / fournisseur -->
				<TR><TD colspan="4">
				<div>
				<TABLE>				
				<TR>
					<TD  colspan="4">
						<%
							if(viewBean.getFournisseurTypeArray().size()>0){
						%>	
						<div class="dataTable">
						<TABLE style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >							
							<TR style="border=1px solid black; " >
								<th><ct:FWLabel key="JSP_RF_CONV_FOURNISSEUR"/></th>
								<th><ct:FWLabel key="JSP_RF_CONV_ADRESSE_PAIEMENT"/></th>
								<th><ct:FWLabel key="JSP_RF_CONV_TYPE_DE_SOIN_TABLE"/></th>
								<th><ct:FWLabel key="JSP_RF_CONV_SOUS_TYPE_DE_SOIN_TABLE"/></th>
								<th><ct:FWLabel key="JSP_RF_CONV_EFFACER_LIGNE_TABLE"/></th>
							</TR>
							<% 								
								int numLigne = 0;
								for(Iterator it = viewBean.getFournisseurTypeArray().iterator();it.hasNext();){
									RFFournisseurType fournisseurType=(RFFournisseurType)it.next();
									if(!fournisseurType.getSupprimer()){																														
							%>
									<TR class="mtd">
										<TD style="border-right=1px solid #C0C0C0;"><%=!JadeStringUtil.isEmpty(fournisseurType.getFournisseur())?fournisseurType.getFournisseur():espace%></TD>
										<TD style="border-right=1px solid #C0C0C0;"><%=!JadeStringUtil.isEmpty(fournisseurType.getIdAdressePaiement())?viewBean.getDescAdressePaiement(fournisseurType.getIdAdressePaiement()):"&nbsp;"%></TD>
										<TD style="border-right=1px solid #C0C0C0;"><%=!JadeStringUtil.isEmpty(fournisseurType.getTypeDeSoin())?fournisseurType.getTypeDeSoin()+". "+viewBean.getSession().getCodeLibelle(RFUtils.getIdTypeDeSoin(fournisseurType.getTypeDeSoin(),viewBean.getSession())):espace%></TD>
										<TD style="border-right=1px solid #C0C0C0;"><%=!JadeStringUtil.isEmpty(fournisseurType.getSousTypeDeSoin())?fournisseurType.getSousTypeDeSoin()+". "+viewBean.getSession().getCodeLibelle(RFUtils.getIdSousTypeDeSoin(fournisseurType.getTypeDeSoin() ,fournisseurType.getSousTypeDeSoin(),viewBean.getSession())):espace%></TD>
										<TD style="border-right=1px solid #C0C0C0;"><a href="javascript:suppressionFT('<%=fournisseurType.getIdFournisseur().toString() %>','<%=fournisseurType.getIdSousTypeDeSoin().toString() %>')">suppr.</a></TD>	
									</TR>
							<%		
									}
									numLigne++;
								}
							%>
						</TABLE> 	
						<script>$('.mtd:odd').css("background-color", "#E8EEF4");</script>
						</div>
						<% } %>
					</TD>
				</TR> 
				</TABLE>
				</div>  
				</TD></TR> 
				<TR><TD colspan="6"><HR></HR></TD></TR>
				<TR>
					<TD><LABEL><ct:FWLabel key="JSP_RF_CONV_CONCERNE_ASSURE"/></LABEL></TD>
					<TD><INPUT type="checkbox" id="concerneAssure" name="concerneAssure" onchange="changeConcerneAssureValue()" onclick="getConcerneAssureValue()" <%=viewBean.getConcerneAssure().booleanValue()?"CHECKED":""%> /></TD>
				</TR>
				<!-- partie visible uniquement si la checkbox concerneAssure est cochée -->
				<TR><TD colspan="4">
				<div>
				<TABLE id="contenu">
					<TR>
						<TD><LABEL><ct:FWLabel key="JSP_RF_CONV_ASSURE"/></LABEL></TD>
						<TD colspan="2">
								<input type="hidden" name="forIdTiers" value="<%=viewBean.getIdAssure()%>">
								<input type="text" id="numeroAVS" value="<%=viewBean.getNssTiers().length()>2?viewBean.getNssTiers():""%>">
		
								<ct:FWSelectorTag
									name="selecteurTiers"
									methods="<%=viewBean.getMethodesSelecteurAssure()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.tiers.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
									&nbsp;
									<b class="descAssure"><%=/*(viewBean.isRetourDepuisPyxis() && viewBean.isFromAssure())?*/viewBean.getDescAssure()/*:espace*/%></b>
						</TD>
					</TR> 	
					<TR>
						<TD><LABEL><ct:FWLabel key="JSP_RF_CONV_ASSURE_PERIODE"/></LABEL></TD>	
						<TD><ct:FWLabel key="JSP_RF_CONV_ASSURE_DU"/><input data-g-calendar=" "  name="forDateDebut" value="<%=viewBean.getForDateDebut()%>"/></TD>
						<TD><ct:FWLabel key="JSP_RF_CONV_ASSURE_AU"/><input data-g-calendar=" "  name="forDateFin" value="<%=viewBean.getForDateFin()%>"/></TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_CONV_MONTANT_ASSURE"/>&nbsp;</TD>
						<TD colspan="2"><input type="text" name="forMontantAssure" style="text-align:right;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" value="<%=new FWCurrency(viewBean.getForMontantAssure()).toStringFormat()%>"  /></TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
	                	<TD>
							<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_AK_REQ_AJOUTER"/>" onclick="ajouterAssureTiers()" />
						</TD>
					</TR>
					<!-- Création du tableau des assurés -->				
					<TR>
						<TD  colspan="4">
						<%
							if(viewBean.getAssureArray().size()>0){
						%>		
						
							<div class="dataTable">															
							<TABLE style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
								<TR style="border=1px solid black; " >
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_NSS"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_NOM"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_PRENOM"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_ADRESSE"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_DATE_DEBUT"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_ASSURE_DATE_FIN"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_MONTANT_ASSURE"/></th>
									<th><ct:FWLabel key="JSP_RF_CONV_EFFACER_LIGNE_TABLE"/></th>
								</TR>
								<% 
									String montant = "";int numLigne = 0;
									for(Iterator it = viewBean.getAssureArray().iterator();it.hasNext();){
										RFAssureConvention assure=(RFAssureConvention)it.next();
										//si il n'est pas encore à supprimer
										if(!assure.getSupprimer()){											
												montant = new FWCurrency(assure.getMontant()).toStringFormat();
								%>
										<TR class="mtd">										
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getNSS())?assure.getNSS():espace%></TD>
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getNom())?assure.getNom():espace%></TD>
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getPrenom())?assure.getPrenom():espace%></TD>
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getAdresse())?assure.getAdresse():espace%></TD>
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getDateDebut())?assure.getDateDebut():espace%></TD>	
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getDateFin())?assure.getDateFin():espace%></TD>	
											<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(assure.getMontant())?montant:"0.00"%></TD>
											<TD style="border-right=1px solid #C0C0C0"><a href="javascript:suppressionAssure('<%=assure.getNSS().toString() %>','<%=assure.getDateDebut().toString() %>','<%=assure.getDateFin().toString() %>')">suppr.</a></TD>																																	
										</TR>										
								<%	
										}
										numLigne++;
									}
								%>
							</TABLE>
							<script>$('.mtd:odd').css("background-color", "#E8EEF4");</script>
							</div>  
						<% } %>							 
						</TD>
					</TR>  					 					
				</TABLE>
				</div>
			</TD>
		</TR>					
<style type="text/css">
	.dataTable { height:expression(this.scrollHeight > 160 ? "160px" : "auto"); }
</style>
          
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<INPUT type="button" value="<ct:FWLabel key="JSP_RF_CONV_ASSURE_VALIDER"/> " onclick="validate();" >
<INPUT type="button" value="<ct:FWLabel key="JSP_RF_CONV_ASSURE_ANNULER"/> " onclick="annuler();" >
<INPUT type="button" value="<ct:FWLabel key="JSP_RF_CONV_ASSURE_PRECEDANT"/> " onclick="if(precedant()) action(COMMIT);" >
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>			