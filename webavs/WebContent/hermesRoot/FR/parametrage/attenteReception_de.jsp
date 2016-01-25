<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
boolean isSent = false;
subTableWidth = "";

globaz.hermes.db.parametrage.HEAttenteReceptionViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteReceptionViewBean)session.getAttribute("viewBean");
viewBean.loadChamps();
idEcran="GAZ0010";
%>
<SCRIPT language="JavaScript">
	top.document.title = "ARC - Detail d'un ARC";
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="javascript">
function add() {}
function upd() {

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hermes.parametrage.attenteReception.ajouter";
    else
        document.forms[0].elements('userAction').value="hermes.parametrage.attenteReception.modifier";   
    return state;
} 

function cancel() {

  document.forms[0].elements('userAction').value="hermes.parametrage.attenteReception.afficher";
}

function del() {

	if(document.forms[0].elements('statut').value=='true'){
		alert('Impossible de modifier une annonce déjà envoyée !');
	} else {
	    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
	        document.forms[0].elements('userAction').value="hermes.parametrage.attenteReception.supprimer";
       	 document.forms[0].submit();
	    }
	}
	
}

function init(){

var tmpButton = document.getElementById("btnUpd");
tmpButton.style.visibility = "hidden";

tmpButton = document.getElementById("btnDel");
tmpButton.style.visibility = "hidden";
 
}

function checkKey(input){
	var re = /[^a-zA-Z\-',].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une annonce reçue<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <!--
          <tr> 
            <td width="40%">&nbsp;Motif</td>
            <td>&nbsp;<input type="text" value="<%=request.getParameter("motif")%>" size="3"/></td>
          </tr>-->
          <%
String messageRetour = null;
String typeEnvoi = "";
for (int i = 0; i < viewBean.getChampsSize() ; i++){
	globaz.hermes.db.parametrage.HEAttenteReceptionChampsViewBean line = (globaz.hermes.db.parametrage.HEAttenteReceptionChampsViewBean) viewBean.getChampsReceptionAt(i);
	if(!line.getStatut().equals(IHEAnnoncesViewBean.CS_EN_ATTENTE)){
		isSent = true;
	}
	if(messageRetour==null){
		messageRetour = line.getMessageRetour();
	}
	if(!typeEnvoi.equals(line.getLibelleAnnonce())){%>

		<tr>
			<td colspan="2"><hr></td>			
		</tr>	
		<tr>
			<td><b><%=line.getLibelleAnnonce()%></b></td>
			<td>&nbsp;</td>			
		</tr>
		<tr>
			<td colspan="2"><hr></td>			
		</tr>		
	<%
		typeEnvoi = line.getLibelleAnnonce();
	}
	if(!line.isHidden() && line.getValeur().trim().length()!=0){
		String hermesKey = line.getIdChamp();
		if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isDateField(hermesKey)) {			
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td><input type="text" class="disabled" readonly value="<%=globaz.hermes.utils.StringUtils.unformatDate(line.getValeur())%>" /></td>
          </tr>
          <%
		} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomField(hermesKey)){
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>de 
              <input type="text" class="disabled" readonly name="<%=hermesKey%>" size="4" maxlength="4" value="<%=line.getValeur(hermesKey,0)%>" id="frm1">
              à 
              <input type="text" class="disabled" readonly name="<%=hermesKey%>" size="4" maxlength="4" value="<%=line.getValeur(hermesKey,1)%>" id="frm1">
              , chiffre clef spécial 
              <select name="<%=(String)hermesKey%>">
                <option <%=line.isSelected(hermesKey,"")%> value=""></option>
                <option <%=line.isSelected(hermesKey,"1")%> value="1">1</option>
                <option <%=line.isSelected(hermesKey,"2")%> value="2">2</option>
                <option <%=line.isSelected(hermesKey,"3")%> value="3">3</option>
                <option <%=line.isSelected(hermesKey,"4")%> value="4">4</option>
                <option <%=line.isSelected(hermesKey,"5")%> value="5">5</option>
              </select>
            </td>
          </tr>
          <%
		} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNumeroAVS(hermesKey)) {
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>
              <input type="text" class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(globaz.hermes.utils.HENNSSUtils.afficheNumAVS(hermesKey,line.getValeur()))%>" >
            </td>
            <%
		    } else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomSelectField((String) hermesKey)){
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>
            <input type="text" name="<%=((String)hermesKey)%>" class="disabled" readonly value="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.getCustomSelectFieldValue(line.getValeur(),(String)hermesKey,languePage)%>" />
            </td>
          </tr>
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isReadOnlyField((String) hermesKey)){
%>
          <tr> 
            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td>
              <input type="text" class="disabled" readonly name="<%=(String)hermesKey%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>" value="<%=line.getValeur()%>" >
            </td>
          </tr>
		   
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNameField((String) hermesKey)){
%>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail">
              <input type="text" class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=line.getValeur()%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>" onKeyUp="checkKey(this)" onKeyDown="checkKey(this)">
            </td>
          </tr>
          <%}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomInputField((String) hermesKey)){
%>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail">
             <input type="text" class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=line.getValeur((String)hermesKey)%>" size="<%=line.getLongueur((String)hermesKey)%>" maxlength="<%=line.getLongueur((String)hermesKey)%>">
            </td>
          </tr>		  
          <%			
			}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCurrencyField((String) hermesKey)){
%>
          <tr> 
            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
            <td class="detail">
              <input type="text" class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=viewBean.isRevenuCache()?line.getSession().getLabel("HERMES_10001"):line.getValeur((String)hermesKey)%>" size="<%=((line.getValeur((String)hermesKey)).length())+2%>">
            </td>
          </tr>		  
          <%
          	}else  if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCountryField((String)hermesKey)){
			%>
				<tr>
					<td class="detail">&nbsp;<%=line.getLibelleChamp()%></td>
					<td class="detail">
					<input type="text" class="disabled" readonly value="<%=line.getValeur()+" - "+viewBean.getLibelleOrigine(line.getValeur())%>" name="<%=line.getIdChamp()%>"/>
					</td>
				</tr>		
			<%
			}else if(IHEAnnoncesViewBean.MOTIF_ANNONCE.equals((String)hermesKey)){%>
	          <tr> 
	            <td width="40%">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
	            <td> 
	            	<input tabindex="-1" type="text" name="<%=hermesKey%>" value="<%=line.getValeur()%>" class="disabled" readonly id="frm1" size="<%=line.getLongueur(2)%>"/>
					-
	            	<input tabindex="-1" type="text" value="<%=viewBean.getLibelleMotif()%>" class="disabled" readonly id="frm1" size="70"/>
	            </td>
	          </tr>
	       <%}else{%>
	          <tr> 
	            <td class="detail">&nbsp;<%=line.getLibelleChamp()%>&nbsp;:&nbsp;</td>
	            <td class="detail">
	              <input type="text" class="disabled" readonly name="<%=line.getIdChamp()%>" value="<%=line.getValeur()%>" size="<%=line.getLongueur(2)%>" maxlength="<%=line.getLongueur()%>">
	            </td>
	          </tr>
          <%}
	}
}	
%>
          <tr> 
          <td>
            <input type="hidden" name="statut" value='<%=isSent%>'>
          </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>