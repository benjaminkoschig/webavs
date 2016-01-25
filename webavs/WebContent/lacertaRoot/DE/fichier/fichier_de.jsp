<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%
	idEcran="FFC0002";
	LAInsertionFichierViewBean viewBean = (LAInsertionFichierViewBean) session.getAttribute("viewBean");
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
		document.forms[0].elements('userAction').value="lacerta.fichier.inserer";
	}
	function upd() {
	}
	function validate() {
		state = validateFields(); 
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="lacerta.fichier.inserer";
		else
			document.forms[0].elements('userAction').value="lacerta.fichier.modifier";
		return (state);
	}
	function cancel() {
		document.forms[0].elements('userAction').value="lacerta.fichier.chercher";
	}
	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
		{
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.supprimer";
			document.forms[0].submit();
		}
	}
	function init(){
	<%if(!JadeStringUtil.isEmpty(viewBean.getMotif())){%>
		document.forms[0].elements('motif').value="<%=viewBean.getMotif()%>";
	<%}%>
	<%if(!JadeStringUtil.isEmpty(viewBean.getSiege())){%>
		document.forms[0].elements('siege').value="<%=viewBean.getSiege()%>";
	<%}%>
	changeView();
	}
	function showPart() {
	if(document.forms[0].elements('motif').value==11){
       document.all('tLienTiers').style.display='none';	 
    }else{
       document.all('tLienTiers').style.display='block';
    }
    if(document.forms[0].elements('motif').value==14||document.forms[0].elements('motif').value==15){
       document.all('tCaisse').style.display='none';
    }else{
       document.all('tCaisse').style.display='block';
    }
    if(document.forms[0].elements('motif').value==13){
       document.all('tOption13').style.display='block';
    }else{
       document.all('tOption13').style.display='none';
    }
    document.all('tLienSiege').style.display='none';
}
function showPartSiege() {
    if(document.forms[0].elements('siege').value==1){
       document.all('tLienTiers').style.display='block';
       document.all('tLienSiege').style.display='none';
    }else{
       document.all('tLienTiers').style.display='none';
       document.all('tLienSiege').style.display='block';
    }
}
function changeView(){
	if(document.forms[0].elements('motif').value==11)
	{
	document.getElementById('tCaisse').style.display='block';
	document.getElementById('tOption13').style.display='none';
	document.getElementById('tLienTiers').style.display='none';
	document.getElementById('tLienSiege').style.display='none';
	}
	if(document.forms[0].elements('motif').value==12)
	{
	document.getElementById('tCaisse').style.display='block';
	document.getElementById('tOption13').style.display='none';
	document.getElementById('tLienTiers').style.display='block';
	document.getElementById('tLienSiege').style.display='none';
	}
	if(document.forms[0].elements('motif').value==13)
	{
	document.getElementById('tCaisse').style.display='block';
	document.getElementById('tOption13').style.display='block';
	if(document.forms[0].elements('siege').value==1){
		document.getElementById('tLienTiers').style.display='block';
		document.getElementById('tLienSiege').style.display='none';
	}else{
		document.getElementById('tLienTiers').style.display='none';
		document.getElementById('tLienSiege').style.display='block';
	}
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Création Tiers Fichier Central <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr><td>
<table width="75%" cellspacing="3" cellpadding="3" >
          <tr>
            <td style="width: 143px;">Création </td>
            <td>
                <SELECT id='motif' value="<%=viewBean.getMotif()%>" name='motif' onchange="javascript:changeView()">
                  <OPTION value='11'>Personne indivudelle ou entreprise</OPTION>
                  <OPTION value='12'>Associé ou communauté héréditaire</OPTION> 
                  <OPTION value='13'>Succursale</OPTION> 
                  <!--<OPTION value='14'>14 - adresse propriétaire si différent du lieu d'exploitation</OPTION>
                  <OPTION value='15'>15 - siège social si différent du lieu d'exploitation</OPTION>!-->
                </SELECT>
				    </td>
          </tr>
          <tr>
            <td colspan ="6"><hr size="3" width="100%"/></td>
          </tr>
        </table>
        
        <table width="75%" cellspacing="3" cellpadding="3" id="tOption13" style="display:none">
          <tr>
            <td style="width: 143px;">Siège </td>
            <td>
                <SELECT id='siege' value="<%=viewBean.getSiege()%>" name='siege' onchange="javascript:changeView();">
                  <OPTION value='1'>Dans le canton</OPTION>
                  <OPTION value='2'>Hors canton</OPTION> 
                </SELECT>
				    </td>
          </tr>
          <tr>
            <td colspan ="6"><hr size="3" width="100%"/></td>
          </tr>
        </table>
        
        <table id="tLienTiers" style="display:none">
          <tr>
            <td style="width: 150px;"><b><i>Parent</i></b></td>
          <td>
            <textarea style="width: 500px;height=100px;" name="parentLibelle" readonly><%=viewBean.getParentDescription(objSession)%></textarea>
            <INPUT type="hidden" name="idParent" value="<%=viewBean.getIdParent()%>" style="width: 300px;"/>
             
            <%
            String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/fichier/fichier_de.jsp";	                                	
			Object[] paramsMethods = new Object[]{
			new String[]{"setIdParent","getIdAffiliation"}										
			};
										
			Object[] paramsControl= new Object[]{
				new String[]{"",""}																								
			};										
			%>
            <TD nowrap height="31" width="294">              
	        <ct:FWSelectorTag name="parentControl"											
			methods="<%=paramsMethods%>"
			providerApplication ="lacerta"
			providerPrefix="LA"
			providerAction ="lacerta.fichier.chercher"		
			providerActionParams="<%=paramsControl%>"								
			redirectUrl="<%=redirectUrl%>"/>
			</TD>	
          </td>
          </tr>
          <tr>
            <td colspan ="6"><hr size="3" width="100%"/></td>
          </tr>
        </table>
        
        <table id="tLienSiege" style="display:none"> 
          <tr>
          <td style="width: 150px;" rowspan="6"><b><i>Parent</i></b></td>
          </tr>
          <tr>
            <td>Nom</td>
            <td><INPUT type="text" name="nomParent1" value="<%=viewBean.getNomParent1()%>" style="width: 300px;"/></td>
          </tr>
          <tr>
            <td>Nom</td>
            <td><INPUT type="text" name="nomParent2" value="<%=viewBean.getNomParent2()%>" style="width: 300px;"/></td>
          </tr> 
          <tr> 
            <td>Nom</td>
            <td><INPUT type="text" name="nomParent3" value="<%=viewBean.getNomParent3()%>" style="width: 300px;"/></td>
          </tr>
        
    <tr>
    <td>Localite</td>
	<td>
	
		  <INPUT name="localiteCodeParent" value="<%=viewBean.getLocaliteCodeParent()%>" onkeydown="document.getElementsByName('localiteParent')[0].value=''; document.getElementsByName('idLocaliteParent')[0].value=''" onchange="this.style.color='red';" type="text" size="30">
  	      <!-- <input type="button" onClick="_pos.value=localiteCode.value;_meth.value=_method.value;_act.value='pyxis.adressecourrier.adresse.afficher';userAction.value='pyxis.adressecourrier.localite.chercher';submit()" value="..."> --> 
	
			<%
			Object[] localiteMethodsName3 = new Object[]{
				new String[]{"setIdLocaliteParent","getIdLocalite"},
				new String[]{"setLocaliteParent","getLocalite"},
				new String[]{"setLocaliteCodeParent","getNumPostalEntier"}
			};
			Object[] localiteParams3 = new Object[]{new String[]{"localiteCodeParent","_pos"} };
			%>
			<ct:FWSelectorTag 
				name="localiteSelector3" 
				methods="<%=localiteMethodsName3%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams3%>"
				/>
	  </td>
	 <td><INPUT type="hidden" name="idLocaliteParent" value='<%=viewBean.getIdLocaliteParent()%>'></td>
    </tr>
	<tr>
		<td>&nbsp;</td>
		<TD nowrap width="265"><INPUT name="localiteParent" type="text" value="<%=viewBean.getLocaliteParent()%>" tabindex="-1" class="libelleLongDisabled" readonly></TD>
	</tr>
         
		<tr>
            <td colspan ="6"><hr size="3" width="100%"/></td>
          </tr>
        </table>
        
        <table>  
          <tr>
          <td style="width: 151px;">Numéro d'affilié</td>
            <td>
              <INPUT type="text" value="<%=viewBean.getNumAffilie()%>" name="numAffilie"/>
            </td>
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
            <td style="width: 150px;">Titre</td>
            <td>
			        <ct:FWCodeSelectTag name="titre"
			        defaut="<%=viewBean.getTitre()%>"
					codeType="PYTITRE" wantBlank="true"/>
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
          </tr>
          <tr>
            <td>Nom ou Raison soc.</td>
            <td><INPUT type="text" name="nom" value="<%=viewBean.getNom()%>" style="width: 300px;"/></td>
            <td>Alias</td>
            <td><INPUT type="text" name="alias" value="<%=viewBean.getAlias()%>"/></td>
          </tr>
          <tr>
            <td>Prénom ou Rais. soc.</td>
            <td><INPUT type="text" name="prenom" value="<%=viewBean.getPrenom()%>" style="width: 300px;"/></td>
            <TD nowrap width="100">Langue</TD>
            <TD nowrap><ct:FWCodeSelectTag name="langue" defaut="<%=viewBean.getLangue()%>" codeType="PYLANGUE"/></TD>
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
			%>
			<ct:FWSelectorTag 
				name="localiteSelector" 
				methods="<%=localiteMethodsName %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams%>"
				/>
	  </td>
	 <td><INPUT type="hidden" name="idCLocalite" value='<%=viewBean.getIdCLocalite()%>'></td>
    </tr>
	<tr>
		<td>&nbsp;</td>
		<TD nowrap width="265"><INPUT name="localiteC" type="text" value="<%=viewBean.getLocaliteC()%>" class="libelleLongDisabled" readonly tabindex="-1"></TD>
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
				/>
	  </td>
	 <td><INPUT type="hidden" name="idDLocalite" value='<%=viewBean.getIdDLocalite()%>'></td>
    </tr>
    <tr>
		<td>&nbsp;</td>
		<TD nowrap width="265"><INPUT name="localiteD" type="text" value="<%=viewBean.getLocaliteD()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
	</tr>
		</table>
		
		<table id="tCaisse" style="display:block">
          <tr>
            <td colspan ="3"><hr size="3" width="100%"/></td>
          </tr>
          <tr> 
            <td>Caisse AVS : </td>
            <td><INPUT type="text" name="caisseAVSNum" value="<%=viewBean.getCaisseAVSNum()%>"/>
            de 
              <ct:FWCalendarTag name="caisseAVSDateDebut" value="<%=viewBean.getCaisseAVSDateDebut()%>"/>
            à 
              <ct:FWCalendarTag name="caisseAVSDateFin" value="<%=viewBean.getCaisseAVSDateFin()%>"/>
              <INPUT type="text" name="numAffilieAVS" value="<%=viewBean.getNumAffilieAVS()%>"/>
            </td>
            <td></td>
          </tr>
          <tr> 
            <td>Personnel de maison :</td>
            <td><INPUT type="text" value="" name="" value="" disabled="disabled" readonly/>
            de
              <ct:FWCalendarTag name="personnelMaisonDateDebut" value="<%=viewBean.getPersonnelMaisonDateDebut()%>"/>
            à
              <ct:FWCalendarTag name="personnelMaisonDateFin" value="<%=viewBean.getPersonnelMaisonDateFin()%>"/>
            </td>
            <td></td>
          </tr>
           <tr> 
            <td>Caisse AF : </td>
            <td><INPUT type="text" value="<%=viewBean.getCaisseAFNum()%>" name="caisseAFNum"/>
            de
               <ct:FWCalendarTag name="caisseAFDateDebut" value="<%=viewBean.getCaisseAFDateDebut()%>"/>
            à
               <ct:FWCalendarTag name="caisseAFDateFin" value="<%=viewBean.getCaisseAFDateFin()%>"/>
               <INPUT type="text" name="numAffilieAF" value="<%=viewBean.getNumAffilieAF()%>"/>
            </td>
            <td></td>
          </tr>
       </table>
</td></tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="LAMenuVide" showTab="menu" checkAdd="no">
	<ct:menuSetAllParams key="idRetour" value="<%=viewBean.getId()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>