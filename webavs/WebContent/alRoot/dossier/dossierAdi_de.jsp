
<%@page import="globaz.al.vb.dossier.ALDossierAdiViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDossierAdiViewBean viewBean = (ALDossierAdiViewBean) session
			.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");

	idEcran = "AL0023";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPrestation"%><script type="text/javascript">
function add() {
    //document.forms[0].elements('userAction').value="al.dossier.dossierAdi.ajouter";
}
function upd() {
    //document.forms[0].elements('userAction').value="al.allocataire.revenus.modifier";
}
function validate() {
	
    /*state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.allocataire.revenus.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.allocataire.revenus.modifier";
    return state;*/
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.dossier.dossierAdi.afficher";
	}
}
function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(
					objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
	/*if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.allocataire.revenus.supprimer";
        document.forms[0].submit();
    }*/
}

function init(){
	
	//document.getElementsByClassName("revenuAlloc").style.display="none";
	//document.getElementsByClassName("revenuConj").style.display="block";
}

function postInit(){
}

$(function() {
	$('.editLink').click(function() {
		$(this).hide();
	});
	
})

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>	
			<%-- tpl:insert attribute="zoneTitle" --%>
			<ct:FWLabel key="AL0023_TITRE"/> 
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
			<%-- tpl:insert attribute="zoneMain" --%>

            <table class="zone" id="AL0023dossierZone">
                <caption><ct:FWLabel key="AL0023_TITRE_DOSSIER"/></caption>
                <tr>
                    <td><ct:FWLabel key="AL0023_DOSSIER_NUM"/></td>
                    <td><ct:inputText name="dossierComplexModel.dossierModel.idDossier" styleClass="readOnly medium"/></td>
                    <td></td>
                    <td></td> 
                    <td></td>
                    <td></td>      
                </tr>
            </table>
          
            <table class="zone" id="AL0023allocataireZone">
                <caption><ct:FWLabel key="AL0023_TITRE_ALLOC"/></caption>
                <tr>
                    <td><ct:FWLabel key="AL0023_ALLOC_ACTIVITE"/></td>
                    <td><ct:inputText name="dossierComplexModel.dossierModel.activiteAllocataire" styleClass="readOnly medium"/></td>
                    <td></td>
                    <td></td> 
                    <td></td>
                    <td></td>         
                </tr>
                <tr>
                    <td><ct:FWLabel key="AL0023_ALLOC_NOM"/></td>
                    <td><input type="text" 
                    		value="<%=viewBean.getDossierComplexModel()
					.getAllocataireComplexModel()
					.getPersonneEtendueComplexModel().getTiers()
					.getDesignation1()
					+ " "
					+ viewBean.getDossierComplexModel()
							.getAllocataireComplexModel()
							.getPersonneEtendueComplexModel().getTiers()
							.getDesignation2()%>" 
                    		class="readOnly long"/>
                    </td>
                    <td></td>
                    <td></td> 
                    <td></td>
                    <td></td>         
                </tr>
                <tr>
                    <td><ct:FWLabel key="AL0023_ALLOC_NSS"/></td>
                    <td>
                        <ct:inputText name="dossierComplexModel.allocataireComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel" styleClass="readOnly long"/>  
                    </td>
                    <td><input type="text" 
                    		value="<%=viewBean.getDossierComplexModel()
					.getAllocataireComplexModel()
					.getPersonneEtendueComplexModel().getPersonne()
					.getDateNaissance()
					+ " - "
					+ objSession.getCodeLibelle(viewBean
							.getDossierComplexModel()
							.getAllocataireComplexModel()
							.getPersonneEtendueComplexModel().getPersonne()
							.getSexe())
					+ " - "
					+ viewBean.getDossierComplexModel()
							.getAllocataireComplexModel().getPaysModel()
							.getCodeIso()%>" 
                    		class="readOnly long"/></td>
                    <td></td>  
                    <td></td>
                    <td></td>        
                </tr>
                <tr>
                    <td><ct:FWLabel key="AL0023_ALLOC_ETATCI"/></td>
                    <td><input type="text" 
                    		value="<%=objSession.getCodeLibelle(viewBean
					.getDossierComplexModel().getAllocataireComplexModel()
					.getPersonneEtendueComplexModel().getPersonne()
					.getEtatCivil())%>" 
                    		class="readOnly long"/></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>          
                </tr>
                
            </table>

         	<strong><ct:FWLabel key="AL0023_HISTORIQUE_DECOMPTE"/></strong>
         	
              <table class="al_list" id="decomptes">
              	<tr> 
              	
                  	<th></th>
                  	<th></th>
          
                  	<th></th>
                  	<th><ct:FWLabel key="AL0023_LIST_ENTETE_ANNEE"/></th>
					<th><ct:FWLabel key="AL0023_LIST_ENTETE_PERIODE"/></th>
					<th><ct:FWLabel key="AL0023_LIST_ENTETE_ETAT"/></th>
					<th><ct:FWLabel key="AL0023_LIST_ENTETE_DATE"/></th>
					<th><ct:FWLabel key="AL0023_LIST_ENTETE_RECEPTION"/></th>
					<th><ct:FWLabel key="AL0023_LIST_ENTETE_ORGANISME"/></th>   
                </tr>
         
                <%
                         	String rowStyle = "";

                         	int nbDecomptes = viewBean.getDecompteSearchModel().getSize();
                         	int cptDecompteDisplay = 0;
                         	String detailLink = "window.location.href='al?userAction=al.adi.decompteAdi.afficher&selectedId=";

                         	for (int i = 0; i < nbDecomptes; i++) {

                         		if (!(cptDecompteDisplay % 2 == 0))
                         			rowStyle += " odd";
                         		else
                         			rowStyle += " nonodd";

                         		String actionDetail = detailLink.concat(viewBean.getDecompteAt(
                         				i).getId());
                         %>		
		               	               
		          	<tr class="<%=rowStyle%>">
		          			
		          			<%if(ALCSPrestation.ETAT_PR.equals(viewBean.getDecompteAt(i).getEtatDecompte())){ %>
		          			<td>
		          				
		          				<ct:ifhasright element="al.adi.decompteAdi.supprimerDecompte" crud="crud">
		          					<a class="deleteLink" onclick="return confirm('<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION")) %>')" 
		          					href="<%=servletContext
									+ mainServletPath
									+ "?userAction=al.adi.decompteAdi.supprimerDecompte&id="
									+ viewBean.getDecompteAt(i).getId()%>" title='<%=objSession.getLabel("LINK_DEL_DECOMPTE_DESC")%>'/>
		          				</ct:ifhasright>
		          				
		          			</td>
		          			<td>
		          				<a class="editLink" href="<%=servletContext
								+ mainServletPath
								+ "?userAction=al.adi.saisieAdi.afficher&_method=add&idDecompte="
								+ viewBean.getDecompteAt(i).getId()%>" title='<%=objSession.getLabel("LINK_SAISIE_DECOMPTE_DESC")%>'/>
		          			</td>
		          			<%}else{%>
		          			<td colspan="2">&nbsp;</td>
		          			<%} %>
		          			<td>	
		          				<a class="detailLink" href="<%=servletContext + mainServletPath
						+ "?userAction=al.adi.decompteAdi.afficher&selectedId="
						+ viewBean.getDecompteAt(i).getId()%>" title='<%=objSession.getLabel("LINK_DETAIL_DECOMPTE_DESC")%>'/>
		          			</td>
				            <td><%=viewBean.getDecompteAt(i).getAnneeDecompte()%></td>
				            <td><%=viewBean.getDecompteAt(i).getPeriodeDebut() + " - "
						+ viewBean.getDecompteAt(i).getPeriodeFin()%></td>
				            <td><%=objSession.getCode(viewBean.getDecompteAt(i)
						.getEtatDecompte())%></td>
				            <td><%=viewBean.getDecompteAt(i).getDateEtat()%></td>
				            <td><%=viewBean.getDecompteAt(i).getDateReception()%></td>
				            <td><%=viewBean.getDecompteAt(i)
						.getIdTiersOrganismeEtranger()%></td>
				    </tr>
				    
				    
				<%
				    				    					cptDecompteDisplay++;
				    				    					}
				    				    				%>
				
				<tfoot>
                  <tr class='<%="odd".equals(rowStyle) ? "nonodd" : "odd"%>'>
                    <td>
                   		<ct:ifhasright element="al.adi.decompteAdi" crud="u">
	                    	<a class="addLink" href="<%=servletContext
								+ mainServletPath
								+ "?userAction=al.adi.decompteAdi.afficher&_method=add&idDossier="
								+ viewBean.getDossierComplexModel()
										.getDossierModel().getIdDossier()%>" title='<%=objSession.getLabel("LINK_NEW_DECOMPTE_DESC")%>'/>
						</ct:ifhasright>
                    </td>
                    <td colspan="8" style="text-align:center;">   
		                <a href="#" id="paginationPrevious" ><ct:FWLabel key="LINK_PAGINATION_PREVIOUS"/></a>
		                    <div id="paginationNumero"></div>
		                <a href="#" id="paginationNext"><ct:FWLabel key="LINK_PAGINATION_NEXT"/></a>
                    </td>
                  </tr>
                </tfoot>
             </table>
   			
			<%-- /tpl:insert --%>
			</td></tr>
									
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<ct:menuChange displayId="options" menuId="default-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDossier" checkAdd="no"
		value="<%=viewBean.getDossierComplexModel().getId()%>" />
</ct:menuChange>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
