
<%@page import="globaz.al.vb.allocataire.ALRevenusViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALRevenusViewBean viewBean = (ALRevenusViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	idEcran="AL0009";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript">
function add() {
    document.forms[0].elements('userAction').value="al.allocataire.revenus.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.allocataire.revenus.modifier";
}
function validate() {
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.allocataire.revenus.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.allocataire.revenus.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.allocataire.revenus.afficher";
	}
}
function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.allocataire.revenus.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	
	//document.getElementsByClassName("revenuAlloc").style.display="none";
	//document.getElementsByClassName("revenuConj").style.display="block";
}

function postInit(){
}


function switchOnglet(sens){
	//affichage de la bonne classe css pour l'onglet selon lequel cliqué
	if(sens == "alloc"){
		document.getElementById("tabAlloc").className='selected';
		document.getElementById("tabConj").className='inactive';
	}
	if(sens == "conjoint"){
		document.getElementById("tabConj").className='selected';
		document.getElementById("tabAlloc").className='inactive';
	}
	
	var revenusList = document.getElementById("revenus").getElementsByTagName("tr");
	var newRevenuTR = null;
	var lastDisplayTR = null;
	//test sur chaque classe css de chaque tr voir si on doit afficher ou non en fonction contexte onglet actif
	
	for (i in revenusList)
	{
		if(i!="length"){
		
			if(sens == "alloc"){
	
				document.getElementsByName("revenuModel.revenuConjoint")[0].value="false";
				if(revenusList[i].className.indexOf("revenuAlloc")!=-1){
					revenusList[i].style.display="block";
					lastDisplayTR = revenusList[i];
					
				}
				if(revenusList[i].className.indexOf("revenuConj")!=-1){
					revenusList[i].style.display="none";
				}

			}
			if(sens == "conjoint"){
				
				document.getElementsByName("revenuModel.revenuConjoint")[0].value="true";
			
				if(revenusList[i].className.indexOf("revenuConj")!=-1){
					revenusList[i].style.display="block";
					lastDisplayTR = revenusList[i];	
				}
				if(revenusList[i].className.indexOf("revenuAlloc")!=-1){
					revenusList[i].style.display="none";
				}
			}
			
		}
		newRevenuTR = revenusList[i];
		
	}

	//le style css de la ligne nouveau revenu est odd si le dernier revenu affiché est nonodd et vice-versa
	if(lastDisplayTR){
			
		if(lastDisplayTR.className.indexOf("nonodd")!=-1)
			newRevenuTR.className="odd";
		else
			newRevenuTR.className="nonodd";
	}
	else
		newRevenuTR.className="nonodd";
	
}


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
				
			<%-- tpl:insert attribute="zoneTitle" --%>
			<ct:FWLabel key="AL0009_TITRE"/> 
				<%-- tpl:insert attribute="zoneTitle" --%>
			(&nbsp;<%=objSession.getLabel("AL0006_TITRE")+viewBean.getAllocataireComplexModel().getId()%>&nbsp;
			<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %>&nbsp;<%=viewBean.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %>)
			<%-- /tpl:insert --%>
			
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
				<%-- tpl:insert attribute="zoneMain" --%>
	     		
	     	<table class="zone" id="AL0009revenusZone">
	                <tr>
	                	<td class="subtitle" colspan="4">
	                		<ct:FWLabel key="AL0009_REVENUS_ACTIFS"/>
	                	</td></tr>
	                <tr>
	                  <td class="label"><ct:FWLabel key="AL0009_REVENUS_ACTIFS_ALLOC"/></td>
	                  <td><input type="text" value="<%=viewBean.getDernierRevenuAlloc().getMontant() %>" disabled="disabled" class="readOnly" readonly="readonly"/></td>
	                  <td class="label">&nbsp;</td>
	                  <td>&nbsp;</td>
	                </tr>
	                <tr>
	                  <td class="label"><ct:FWLabel key="AL0009_REVENUS_ACTIFS_CONJOINT"/></td>
	                  <td><input class="readOnly" disabled="disabled"  readonly="readonly" type="text" value="<%=viewBean.getDernierRevenuConj().getMontant() %>"/></td>
	                  <td class="label">&nbsp;</td>
	                  <td>&nbsp;</td>
	                </tr>
            </table>

         	<strong><ct:FWLabel key="AL0009_HISTORIQUE_REVENUS"/></strong>
         	<div style="width:100%;text-align:center;">
	     	<div style="margin-top:10px;text-align:center;width:500px;">	
	            <div id="tabs">
		           <ul>
		              <li id="tabAlloc" class="selected" onmouseout="if(this.className!='selected'){this.className='inactive';}" onmouseover="if(this.className!='selected')this.className='active';" onclick="switchOnglet('alloc');"> <ct:FWLabel key="AL0009_ONGLET_ALLOC"/></li>
		              <li id="tabConj" class="inactive" onmouseout="if(this.className!='selected'){this.className='inactive';}" onmouseover="if(this.className!='selected')this.className='active';" onclick="switchOnglet('conjoint');"> <ct:FWLabel key="AL0009_ONGLET_CONJOINT"/></li>    
		           </ul>
	            </div>


              <table class="al_list" id="revenus">
              	<tr>
                  <th scope="col" style="width:5%;"></th>
                  <th scope="col" style="width:15%;"><ct:FWLabel key="AL0009_REVENUS_ENTETE_DATE"/></th>
                  <th scope="col" style="width:25%;"><ct:FWLabel key="AL0009_REVENUS_ENTETE_MONTANT"/></th>   
                </tr>
         
                <%
           
                String rowStyle = "";
        		
                int nbRevenus = viewBean.getRevenuSearchModel().getSize();
                int cpt = 0;
                int cptConj = 0;
                for (int i=0; i < nbRevenus ; i++) {
        			//on affiche que les revenus alloc et pas conjoint
        			boolean isConjoint = viewBean.getRevenuHistoriqueAt(i).getRevenuConjoint().booleanValue();
        			if(!isConjoint)
        				cpt++;
        			else
        				cptConj++;
                %>		<!-- par défaut, on affiche le revenu de alloc et pas conjoint -->
		               <tr style="display:<%=(isConjoint)?"none;":"block;"%>" class="<%=(isConjoint)?"revenuConj":"revenuAlloc"%> <%=(!isConjoint)?(cpt % 2 == 0)?"odd":"nonodd":""%> <%=(isConjoint)?(cptConj % 2 == 0)?"odd":"nonodd":""%>">
			               <td><a title="Supprimer revenu" class="deleteLink" href="<%=servletContext + mainServletPath + "?userAction=al.allocataire.revenus.supprimerRevenu&id="+viewBean.getRevenuHistoriqueAt(i).getIdRevenu()+"&idDossier="+request.getParameter("idDossier")%>"/></td>
		                   <td class="text"><input type="text" value="<%=viewBean.getRevenuHistoriqueAt(i).getDate() %>" class="date readOnly"  readonly="readonly" disabled="disabled" /></td>
		                   <td><input type="text" value="<%=viewBean.getRevenuHistoriqueAt(i).getMontant() %>" class="readOnly"  readonly="readonly" disabled="disabled"/></td>   
			           </tr>
             	<%
        			
                } 
                %>
                <tr class="<%=((cpt+1) % 2 == 0)?"odd":"nonodd"%>">
		               <td></td>
	                   <td class="text">
	                   		<ct:FWCalendarTag name="date" tabindex="1"
	                				value="<%=viewBean.getRevenuModel().getDate()%>"
									doClientValidation="CALENDAR"/>
							<ct:inputHidden name="revenuModel.date" id="dateValue"/>
							<script language="JavaScript">
								document.getElementsByName('date')[0].onblur=function(){fieldFormat(document.getElementsByName('date')[0],'CALENDAR');document.getElementById('dateValue').value = this.value;};
								
								function theTmpReturnFunctiondate(y,m,d) { 
									if (window.CalendarPopup_targetInput!=null) {
										var d = new Date(y,m-1,d,0,0,0);
										window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
										document.getElementById('dateValue').value = document.getElementsByName('date')[0].value;		
									}else {
										alert('Use setReturnFunction() to define which function will get the clicked results!'); 
									}	
								}
								cal_date.setReturnFunction('theTmpReturnFunctiondate');
							</script>	
	                   
	                   </td>
	                   <td>
	                   		<ct:inputText name="revenuModel.montant" tabindex="2"/>
	                   		<ct:inputHidden name="revenuModel.revenuConjoint"/>
	                   		<ct:inputHidden name="revenuModel.idAllocataire"/>
	                   		<input type="hidden" name="idDossier" value="<%=request.getParameter("idDossier") %>"/>
	                   </td>   
		        </tr>    			
              </table>
   			</div>
   			</div>
			<%-- /tpl:insert --%>
			</td></tr>
									
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<ct:menuChange displayId="options" menuId="revenu-detail" showTab="options" checkAdd="no" >			
		<ct:menuSetAllParams checkAdd="no" key="idDossier" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"idDossier\"))?\"\":request.getParameter(\"idDossier\")%>"  />
		<ct:menuSetAllParams checkAdd="no" key="idAllocataire" value="<%=JadeStringUtil.isEmpty(request.getParameter(\"idAllocataire\"))?\"\":request.getParameter(\"idAllocataire\")%>"  />				
</ct:menuChange>


<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
