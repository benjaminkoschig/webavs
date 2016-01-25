<%@ page import="globaz.pyxis.extensions.*,globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
TIPersonneAvsAdresseExtension ext = (TIPersonneAvsAdresseExtension) viewBean.getExtension("EXT_TIERS_ZONE3");

%>
<tr >
	<td colspan="3"><hr size="1" noshade style="color:gray"></td>
</tr>
<tr >
	<td colspan="1" >
		
		<ct:FWPanelContainerTag style="height:140">
		  <ct:FWPanelTag value="Adresse" text="Adresse" defaultTab="yes">
			<br>
		<table cellpadding="0" cellspacing="0">
			
			<tr>
				<td>Attention</td>
				<td><input type="text" class="adresse" name="attention" value="<%=ext.getAttention()%>"></td>
			</tr>
			<tr>
				<td>Rue</td>
				<td><input type="text" class="adresse" name="rue" value="<%=ext.getRue()%>"> n° 
				    <input type="text" size="10" name="numero" class="adresse" value="<%=ext.getNumero()%>" ></td>
			</tr>
			<tr>
				<td>Localité</td>
				<td nowrap="nowrap">
					<INPUT tabindex="-1" type="text" class="libelleLongDisabled" readonly name="localite" value='<%=viewBean.getSelectionNpa()+" - "+viewBean.getSelectionLocalite()%>'>
		  			<%
					Object[] localiteMethodsName = new Object[]{
						new String[]{"setSelectionIdLocalite","getIdLocalite"},
						new String[]{"setSelectionLocalite","getLocalite"},
						new String[]{"setSelectionNpa","getNumPostal"}
					};
					
					%>
					<ct:FWSelectorTag 
						name="localiteSelector" 
						
						methods="<%=localiteMethodsName %>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.adressecourrier.localite.chercher"
						
						/>
					
					<INPUT type="hidden" name="selectionIdLocalite" value='<%=viewBean.getSelectionIdLocalite()%>'>
	  		</td>

	 			
				
			</tr>
		</table>


		    
		  </ct:FWPanelTag>
		  <ct:FWPanelTag value="Aperçu" text="Aperçu">
		
		  	<TEXTAREA rows="8" cols="25"  readonly class="libelleLongDisabled" style="font-weight:bold;font-size:8pt" ><%=viewBean.getAdresseAsString(globaz.pyxis.constantes.IConstantes.CS_AVOIR_ADRESSE_DOMICILE,false)%></TEXTAREA>
		  
		  
		  </ct:FWPanelTag>
		</ct:FWPanelContainerTag>


	</td>
	<td colspan ="2" width="*">
	
		<ct:FWPanelContainerTag style="width:100%; height:140">
		<ct:FWPanelTag value="Paiement" text="Paiement" defaultTab="yes">
		<br>
		<table width="100%" cellpadding="0" cellspacing="0">
			
			<tr>
				<td>CCP</td>
				<td><input type="text" class="adresse" name="ccp" value="<%=ext.getCcp()%>"></td>
			</tr>
			<tr>
				<td>Compte</td>
				<td><input type="text" class="adresse" name="compte" value="<%=ext.getCompte()%>"> </td>
			</tr>
			<tr>
				<td>Banque</td>
				<td nowrap="nowrap">
					<INPUT tabindex="-1" type="text" class="libelleLongDisabled" readonly name="banque" value='<%=viewBean.getSelectionClearing()+" - "+viewBean.getSelectionBanque()%>'>

		  			<%
					Object[] banqueMethodsName = new Object[]{
						new String[]{"setSelectionIdBanque","getIdTiers"},
						new String[]{"setSelectionBanque","getDesignation1_tiers"},
						new String[]{"setSelectionClearing","getClearing"}
					};
					
					%>
					<ct:FWSelectorTag 
						name="banqueSelector" 
						
						methods="<%=banqueMethodsName %>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.tiers.banque.chercher"
						
						/>
					<INPUT type="hidden" name="selectionIdBanque" value='<%=("0".equals(viewBean.getSelectionIdBanque())?"":viewBean.getSelectionIdBanque())%>'>
					
					<br>
					<input type="button" value="Vider" onclick="document.getElementsByName('banque')[0].value='';document.getElementsByName('selectionIdBanque')[0].value='';document.getElementsByName('ccp')[0].value='';document.getElementsByName('compte')[0].value=''">
					
	  		</td>
			</tr>
    		 </table>
		  </ct:FWPanelTag>
		  
		   <ct:FWPanelTag value="AperçuBanque" text="Aperçu">
			<table width="100%"><tr><td width="100%">
		  	<TEXTAREA rows="8" cols="25"  readonly class="libelleLongDisabled" style="font-weight:bold;font-size:8pt" ><%=viewBean.getAdressePrincipalePaiement()%></TEXTAREA>
		  	</td></tr></table>
		  
		  </ct:FWPanelTag>
		  
		</ct:FWPanelContainerTag>
		<INPUT type="hidden" name="selectorName" value="">
	</td>
	
</tr>
