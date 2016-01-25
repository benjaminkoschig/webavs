<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
	globaz.pavo.db.comparaison.CIAnomalieCIViewBean viewBean = (globaz.pavo.db.comparaison.CIAnomalieCIViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getAnomalieId();
	userActionValue = "pavo.comparaison.anomalieCI.modifier";
	idEcran = "CCI0028";

%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	
	String fieldDisable = "class='disabled' readonly tabindex='-1'";
	//bButtonValidate = false;
	//bButtonUpdate = false;
	//bButtonCancel = false;
	
%>
<script>
	function del() {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="pavo.comparaison.anomalieCI.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
	}
	function upd() {
	}
	
	function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.comparaison.anomalieCI.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.comparaison.anomalieCI.modifier";
    
    return state;

	}
	function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="pavo.comparaison.anomalieCI.afficher";
	}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une anomalie CI<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								No Avs
							</td>
							<td>
								<input type="text" size="17" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAvs())%>" <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>Type d'anomalie</td>
							<td>
								<input type="text" size="60" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getTypeAnomalie(),session)%>" <%=fieldDisable%>>
							</td>
							
						</tr>
						<tr>
							<td>No Avs précedent</td>
							<td> <input type="text" size="17" value ="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAvsPrecedent())%>" <%=fieldDisable%> <%=fieldDisable%>></td>
						</tr>	
						<tr>
							<td>Nom</td>
							<td> <input type="text" size="70" value ="<%=viewBean.getNomPrenom()%>" <%=fieldDisable%> <%=fieldDisable%> ></td>
						</tr>
						<tr>
							<td>Pays</td>
							<td> <input type="text" size="50" value ="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getPays(),session)%>" <%=fieldDisable%> ></td>
						</tr>
						<tr>
						<td>Etat</td>
						<td> <ct:FWCodeSelectTag
							name="etat" defaut="<%=viewBean.getEtat()%>" 
							codeType="CITYPANC" wantBlank="false" /></td>
						</tr>
						<tr>
						<td>Motif d'ouverture</td>
							<td>
								<input type="text" size = "2" value ="<%="0".equals(viewBean.getMotifOuverture())?"":viewBean.getMotifOuverture()%>" <%=fieldDisable%>> 
							</td>
						</tr>
						<tr>
						<td>Année d'ouverture</td>
						<td> 
							<input type="text" size = "4" value = "<%="0".equals(viewBean.getAnneeOuverture())?"":viewBean.getAnneeOuverture()%>" <%=fieldDisable%>>
						</td>
						</tr>
						<tr>
						<td>Motif de clôture</td>
						<td> 
							<input type="text" size = "4" value = "<%="0".equals(viewBean.getDernierMotif())?"":viewBean.getDernierMotif()%>" <%=fieldDisable%>>
						</td>
						</tr>
						<tr>
						<td>Dernière clôture</td>
						<td> 
							<input type="text" size = "17" value = "<%=viewBean.getCaisseAgenceFormatee()%>" <%=fieldDisable%>>
						</td>
						</tr>
						<tr>
												
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>