<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%
	bButtonCancel = false;
	idEcran="CLA0003";
	LAInsertionFichierViewBean viewBean = (LAInsertionFichierViewBean) request.getAttribute("viewBean");
	if(viewBean == null){
		viewBean = (LAInsertionFichierViewBean) session.getAttribute("viewBean");
	}
	String jspLocationNSS = servletContext + mainServletPath + "Root/ci_select.jsp";
	%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@page import="db.LAInsertionFichierViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
	top.document.title = "Création d'une affiliation"
	function add() {
		document.forms[0].elements('userAction').value="";
	}
	function upd() {
	}
	function validate() {
		state = validateFields(); 
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="lacerta.fichier.mutation";
		else
			document.forms[0].elements('userAction').value="lacerta.fichier.mutation";
		return (state);
	}
	function cancel() {
		document.forms[0].elements('userAction').value="lacerta.fichier.afficherMutation";
	}
	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
		{
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.supprimer";
			document.forms[0].submit();
		}
	}
	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Mutation d'une affiliation<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<tr>
           <TD nowrap width="80">NSS</TD>
            <TD nowrap >
            <nss:nssPopup
				name="numAvs" 
				jspName="<%=jspLocationNSS%>"
				value="<%=viewBean.getPartialNss()%>"
				newnss="<%=viewBean.getNumeroavsNNSS()%>"
				avsMinNbrDigit="8" avsAutoNbrDigit="11"
				nssMinNbrDigit="7" nssAutoNbrDigit="10"
			/>
			</TD> 			
            <TD>&nbsp;</TD>
          </tr>
          <tr>
            <td>
              Forme juridique
            </td>
            <td>
                <ct:FWCodeSelectTag 
            	name="formeJuridique"
            	libelle="both" 
				defaut="<%=viewBean.getFormeJuridique()%>"
				codeType="VEPERSONNA"
				wantBlank="true"
				/>
            </td>
          </tr>          
          <tr> 
            <td>Titre</td>
            <td>
             <ct:FWCodeSelectTag name="titre"
	          defaut="<%=viewBean.getTitre()%>"
			  codeType="PYTITRE" wantBlank="true"/>
		    </td>
			  <td></td>
	          <td></td>
          </tr>
          <tr>
            <td>Nom ou Raison soc.</td>
            <td><INPUT type="text" name="nom" value="<%=viewBean.getNom()%>" style="width: 300px;"/></td>
          </tr>
          <tr>
            <td>Prénom ou Rais. soc.</td>
            <td><INPUT type="text" name="prenom" value="<%=viewBean.getPrenom()%>" style="width: 300px;"/></td>
          </tr>
          <tr>
            <td>Nom (suite) 1</td>
            <td><INPUT type="text" name="nomSuite"  value="<%=viewBean.getNomSuite()%>" style="width: 300px;" colspan="3"/></td>
          </tr>
          <tr><td><b><i>Adresse de courrier</i></b></td></tr>
          <tr>
							<td >Rue, n° </td>
							<td colspan="3">
                <INPUT type="text" style="width: 300px;" name="adresseCRue" value="<%=viewBean.getAdresseCRue()%>" />
                <INPUT type="text" style="width: 50px;" name="adresseCNumero" value="<%=viewBean.getAdresseCNumero()%>"/>
              </td>
					</tr>
	<tr>
    <td>Localite</td>
	<td>
		  <INPUT name="localiteCCode" value="<%=viewBean.getLocaliteCCode()%>" onkeydown="document.getElementsByName('localiteC')[0].value=''; document.getElementsByName('idCLocalite')[0].value=''" onchange="this.style.color='red';document.getElementsByName('adresseCrue')[0].style.color='red'" type="text" size="30">
  	      <!-- <input type="button" onClick="_pos.value=localiteCode.value;_meth.value=_method.value;_act.value='pyxis.adressecourrier.adresse.afficher';userAction.value='pyxis.adressecourrier.localite.chercher';submit()" value="..."> --> 
	
			<%
			Object[] localiteMethodsName = new Object[]{
				new String[]{"setIdCLocalite","getIdLocalite"},
				new String[]{"setLocaliteC","getLocalite"},
				new String[]{"setLocaliteCCode","getNumPostalEntier"}
			};
			Object[] localiteParams = new Object[]{new String[]{"localiteCCode","_pos"} };
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/fichier/mutation_de.jsp";
			%>
			<ct:FWSelectorTag 
				name="localiteSelector" 
				methods="<%=localiteMethodsName %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams%>"
				redirectUrl="<%=redirectUrl%>"
				/>
	  </td>
	 <td><INPUT type="hidden" name="idCLocalite" value='<%=viewBean.getIdCLocalite()%>'></td>
    </tr>
	<tr>
		<td>&nbsp;</td>
		<TD nowrap width="265"><INPUT name="localiteC" tabindex="-1" type="text" value="<%=viewBean.getLocaliteC()%>" class="libelleLongDisabled" readonly></TD>
	</tr>
	<tr><td><b><i>Adresse de domicile</i></b></td></tr>
          <tr>
							<td >Rue, n° </td>
							<td colspan="3">
                <INPUT type="text" style="width: 300px;" name="adresseDRue" value="<%=viewBean.getAdresseDRue()%>"/>
                <INPUT type="text" style="width: 50px;" name="adresseDNumero" value="<%=viewBean.getAdresseDNumero()%>"/>
              </td>
	</tr>
	<tr>
    <td>Localite</td>
	<td>
		  <INPUT name="localiteDCode" value="<%=viewBean.getLocaliteDCode()%>" onkeydown="document.getElementsByName('localiteD')[0].value=''; document.getElementsByName('idDLocalite')[0].value=''" onchange="this.style.color='red';document.getElementsByName('adresseDrue')[0].style.color='red'" type="text" size="30">
  	      <!-- <input type="button" onClick="_pos.value=localiteCode.value;_meth.value=_method.value;_act.value='pyxis.adressecourrier.adresse.afficher';userAction.value='pyxis.adressecourrier.localite.chercher';submit()" value="..."> --> 
	
			<%
			Object[] localiteMethodsName2 = new Object[]{
				new String[]{"setIdDLocalite","getIdLocalite"},
				new String[]{"setLocaliteD","getLocalite"},
				new String[]{"setLocaliteDCode","getNumPostalEntier"}
			};
			Object[] localiteParams2 = new Object[]{new String[]{"localiteDCode","_pos"} };
			%>
			<ct:FWSelectorTag 
				name="localiteSelector2" 
				methods="<%=localiteMethodsName2 %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams2%>"
				redirectUrl="<%=redirectUrl%>"
				/>
	  </td>
	 <td><INPUT type="hidden" name="idDLocalite" value='<%=viewBean.getIdDLocalite()%>'></td>
    </tr>
    <tr>
		<td>&nbsp;</td>
		<TD nowrap width="265"><INPUT name="localiteD" tabindex="-1" type="text" value="<%=viewBean.getLocaliteD()%>" class="libelleLongDisabled" readonly></TD>
	</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="LA-DetailFichier" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idAffiliation" value="<%=viewBean.getIdAffiliation()%>" checkAdd="no"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>