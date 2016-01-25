<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.codesystem.IRFCatalogueTexte"%>
<%@page import="globaz.cygnus.api.documents.IRFDocuments"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="globaz.cygnus.vb.documents.RFDocumentsViewBean"%>

<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%    
    idEcran="PRF0028";

	RFDocumentsViewBean viewBean = (RFDocumentsViewBean) session.getAttribute("viewBean");
    
    userActionValue=IRFActions.ACTION_DOCUMENTS + ".executer";    
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<script language="JavaScript">
 
    function cancel() {
        if (document.forms[0].elements('_method').value == "add"){
            document.forms[0].elements('userAction').value="back";
        }else{
            document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_DOCUMENTS%>.afficher";
        }
    }  
    
    function del() {
    }

    function add() {
    }
    
    function init(){  
    	
    }
    
    function onReevaluerAllocation(){    	
    	
		if($('input[name=isRegimeNouveauMontantMensuel]').is(':checked')){			
			$("#tBody_isNouveauMontantMensuel").show();			
		}
		else{			
			$("#tBody_isNouveauMontantMensuel").hide();			
		}
    }
    
    function onDecisionSuppression(){    	
    	
		if($('input[name=isRegimeRecenteRevision]').is(':checked')){			
			$("#tBody_isNotRecenteRevision").hide();			
		}
		else{			
			$("#tBody_isNotRecenteRevision").show();			
		}
    }    
    
    function postInit(){  	
    } 
    
    $(function(){
        document.forms[0].elements('_method').value = "add";
        $docListSelect = $('#listeDocs');
        $divMiseEnGed = $('#miseEnGed');
        $cacMiseEnGed = $('#etatMiseEnGed');
        
        
        //cac mise en ged caché
        $divMiseEnGed.hide();
        
    	// on cache tout    	
		$("TBODY[id*='tBody']").hide();
		$("isNouveauMontantMensuel").hide();
		$("isRegimeRecenteRevision").hide();
    	
    	$('#btnUpd').click();
    	    	
    	if(<%=!JadeStringUtil.isEmpty(viewBean.getTypeDocument()) && !JadeStringUtil.isEmpty(viewBean.getIdDocument())%>){
    		$("[name=typeDocumentsList]").val("<%=viewBean.getTypeDocument()%>");
    		onChangeTypeDocumentsList();
    		$("[name=documentsList]").val("<%=viewBean.getIdDocument()%>");
    		onChangeDocumentsList();  
    	}
    	
    	
    });    

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
            <%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_DOCS_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
                <TR><TD>&nbsp;</TD></TR>					
               	<TR>
					<TD width="200px;"><ct:FWLabel key="JSP_RF_DOCS_D_REQUERANT"/></TD>               	
               		<TD colspan="4"><LABEL><%=viewBean.getDetailRequerant()%></LABEL></TD>
               	</TR>                
               	<TR><TD colspan="6">&nbsp;</TD></TR>
               	<INPUT type="hidden" name="detailRequerant" value="<%=viewBean.getDetailRequerant()%>"/>
               	<%@ include file="../utils/documents.jspf"%>
               	<INPUT type="hidden" name="isSaisieDocument" value="false"/>	  
               	<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>	              	                                
                <TR><TD colspan="6">&nbsp;</TD></TR>                	
                <TR>
                	<TD width="200px;"><ct:FWLabel key="JSP_RF_DOCS_D_DATE"/></TD>
                	<%                	
	                	String newdate = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);	                	
                	%>
                	
                	<TD><input data-g-calendar=" "  name="dateDocument" value="<%=JadeStringUtil.isEmpty(viewBean.getDateDocument())?newdate:viewBean.getDateDocument()%>"/></TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_DOCS_D_EMAIL"/></TD>                	
                	<TD><INPUT type="text" name="email" value="<%=JadeStringUtil.isEmpty(viewBean.getEmail())?
																				 viewBean.getSession().getUserEMail():viewBean.getEmail()%>" class="libelleLong"></TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_DOCS_D_GESTIONNAIRE"/></TD>                	          
                	<TD>	
        				<ct:FWListSelectTag name="idGestionnaire" 
        					data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
        					defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
        					viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>        					
					</TD>					
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>   
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_QUESTIONNAIRE%>">			 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_COURRIER_PRECEDENT"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateCourrierPrecedent11" value="<%=viewBean.getRegimeDateCourrierPrecedent11()%>"/></TD>				 	
				 </TR>	
				 <TR><TD colspan="6">&nbsp;</TD></TR>					                                  			 
				</TBODY>             
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DECISION_REFUS%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_COURRIER_PRECEDENT"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateCourrierPrecedent12" value="<%=viewBean.getRegimeDateCourrierPrecedent12()%>"/></TD>				 	
				 </TR>					 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_LIBELLE_REGIME"/></TD>
				 	<TD><INPUT type="text" name="regimeLibelleRegime" value="<%=viewBean.getRegimeLibelleRegime()%>" /></TD>				 	
				 </TR>
				 <TR><TD colspan="6">&nbsp;</TD></TR>
				</TBODY>      		
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_ANNEXE_PRESCRIPTION_DIETETIQUE_FORMULAIRE%>">			 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_COURRIER_PRECEDENT"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateCourrierPrecedent13" value="<%=viewBean.getRegimeDateCourrierPrecedent13()%>"/></TD>				 	
				 </TR>	
				 <TR><TD colspan="6">&nbsp;</TD></TR>					                                  			 
				</TBODY> 						
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DEMANDE_EVALUTION_CMS%>">
				 <TR>
					<TD width="200px;"><ct:FWLabel key="JSP_RF_DOCS_D_CMS"/></TD>               	
					<TD colspan="4">		
						<SELECT name="cmsList" tabindex="" onchange="" style="">
								<OPTION value=""></OPTION> 
						</SELECT>		
					</TD>			 	
				 </TR>
				 <TR><TD colspan="6">&nbsp;</TD></TR>
				</TBODY>      				
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DECISION_OCTROI%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_COURRIER_PRECEDENT"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateCourrierPrecedent15" value="<%=viewBean.getRegimeDateCourrierPrecedent15()%>"/></TD>				 	
				 </TR>	                
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_OCTROI"/></TD>
				 	<TD>
				 		<INPUT type="text" name="regimeMontantOctroi" style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" 
				 	value="<%=new FWCurrency(viewBean.getRegimeMontantOctroi()).toStringFormat() %>"/></TD>			 	
				 </TR>				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_PREMIER_VERSEMENT"/></TD>
				 	<TD><input data-g-calendar="type:month" name="regimeDatePremierVersement" value="<%=viewBean.getRegimeDatePremierVersement()%>"/></TD>				 	
				 </TR>	
				 <TR><TD colspan="6">&nbsp;</TD></TR>								                                  			 
				</TBODY>	
				<TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DECISION_REFUS2%>">
				 <TR>
				 	<TD width="250px"><ct:FWLabel key="JSP_RF_DOCS_D_DATE_DEMANDE_INDEMNISATION"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateDemandeIndemnisationRefus2" value="<%=viewBean.getRegimeDateDemandeIndemnisationRefus2()%>"/></TD>				 	
				 </TR>
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_EVALUATION_OMSV"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateEvaluationOMSV" value="<%=viewBean.getRegimeDateEvaluationOMSV()%>"/></TD>				 	
				 </TR>
				 <TR><TD colspan="6">&nbsp;</TD></TR>
				</TBODY>
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_QUESTIONNAIRE_REVISION%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_ALLOCATION_MENSUELLE"/></TD>
				 	<TD>
				 		<INPUT type="text" name="regimeMontantAllocationMensuelle" style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" 
				 	value="<%=new FWCurrency(viewBean.getRegimeMontantAllocationMensuelle()).toStringFormat()%>"/></TD>			 	
				 </TR>
				 <TR><TD colspan="6">&nbsp;</TD></TR>								                                  			 
				</TBODY>	
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE_REVISION%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_ALLOCATION_MENSUELLE"/></TD>
				 	<TD>
				 		<INPUT type="text" name="regimeMontantAllocationMensuelleRappel" style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" 
				 	value="<%=new FWCurrency(viewBean.getRegimeMontantAllocationMensuelleRappel()).toStringFormat() %>"/></TD>			 	
				 </TR>				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_COURRIER_PRECEDENT"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateCourrierPrecedent" value="<%=viewBean.getRegimeDateCourrierPrecedent()%>"/></TD>				 	
				 </TR>	
				 <TR><TD colspan="6">&nbsp;</TD></TR>					                                  			 
				</TBODY>		
                <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DROIT_MAINTENU_REVISION%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_ALLOCATION_MENSUELLE_REEVALUEE"/></TD>
				 	<TD><INPUT type="checkbox" name="isRegimeNouveauMontantMensuel"
				 	onclick="onReevaluerAllocation()" /></TD>
				 </TR>				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_ALLOCATION_MENSUELLE"/></TD>
				 	<TD>
				 		<INPUT type="text" name="regimeMontantAllocationMensuelleApresRevision" style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" 
				 	value="<%=new FWCurrency(viewBean.getRegimeMontantAllocationMensuelleApresRevision()).toStringFormat() %>"/></TD>			 	
				 </TR>
				 <TBODY id="tBody_isNouveauMontantMensuel">				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_ALLOCATION_APRES_REVISION"/></TD>
				 	<TD><input data-g-calendar="type:month" name="regimeDateAllocationMensuelleApresRevision" value="<%=viewBean.getRegimeDateAllocationMensuelleApresRevision()%>"/></TD>				 	
				 </TR>					 
				 </TBODY>
				 <TR><TD colspan="6">&nbsp;</TD></TR>				 					                                  			 
				</TBODY>	
				
               <TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DECISION_SUPPRESSION%>">
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_RECENTE_REVISION"/></TD>
				 	<TD><INPUT type="checkbox" name="isRegimeRecenteRevision"
				 	onclick="onDecisionSuppression()" CHECKED /></TD>
				 </TR>				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_MONTANT_ALLOCATION_MENSUELLE"/></TD>
				 	<TD>
				 		<INPUT type="text" name="regimeMontantAllocationMensuelleSuppression" style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" 
				 	value="<%=new FWCurrency(viewBean.getRegimeMontantAllocationMensuelleSuppression()).toStringFormat() %>"/></TD>			 	
				 </TR>
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_ALLOCATION_SUPPRESSION"/></TD>
				 	<TD><input data-g-calendar="type:month"  name="regimeDateAllocationMensuelleSuppression" value="<%=viewBean.getRegimeDateAllocationMensuelleSuppression()%>"/></TD>				 	
				 </TR>	
				 </TBODY>				 
				 <TBODY id="tBody_isNotRecenteRevision">				 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_ENVOI_LETTRE_1_7"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateEnvoiLettre1_7" value="<%=viewBean.getRegimeDateEnvoiLettre1_7()%>"/></TD>				 	
				 </TR>
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_ENVOI_LETTRE_1_8"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateEnvoiLettre1_8" value="<%=viewBean.getRegimeDateEnvoiLettre1_8()%>"/></TD>				 	
				 </TR>					 				 					 				 
				 <TR><TD colspan="6">&nbsp;</TD></TR>				 					                                  			 
				</TBODY>								
				
				<TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE%>">	 
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_ENVOI_QUESTIONNAIRE"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateEnvoiQuestionnaire" value="<%=viewBean.getRegimeDateEnvoiQuestionnaire()%>"/></TD>				 	
				 </TR>	
				 <TR><TD colspan="6">&nbsp;</TD></TR>					                                  			 
				</TBODY>
				<TBODY id="tBody_<%=IRFDocuments.CODE_REGIME_LIST%>_<%=IRFCatalogueTexte.CS_DECISION_REFUS3%>">	 
				 <TR>
				 	<TD width="250px"><ct:FWLabel key="JSP_RF_DOCS_D_DATE_DEMANDE_INDEMNISATION"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateDemandeIndemnisation" value="<%=viewBean.getRegimeDateDemandeIndemnisation()%>"/></TD>				 	
				 </TR>	
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_LETTRE_1_3"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateLettre1_3" value="<%=viewBean.getRegimeDateLettre1_3()%>"/></TD>				 	
				 </TR>
				 <TR>
				 	<TD><ct:FWLabel key="JSP_RF_DOCS_D_DATE_LETTRE_1_11"/></TD>
				 	<TD><input data-g-calendar=" "  name="regimeDateLettre1_11" value="<%=viewBean.getRegimeDateLettre1_11()%>"/></TD>				 	
				 </TR>					 				 				 
				 <TR><TD colspan="6">&nbsp;</TD></TR>					                                  			 
				</TBODY>
				
				<TR>
					<TD></TD>
					<TD>
						<DIV id="miseEnGed">
        	       			<input id="etatMiseEnGed" type="checkbox" name="miseEnGed" />
        	       			<ct:FWLabel key="JSP_PROCESS_MISE_EN_GED_VALIDATION"/>
        	       		</DIV>
					</TD>
				</TR>
				
				<TR><TD colspan="6">&nbsp;</TD></TR>																
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
