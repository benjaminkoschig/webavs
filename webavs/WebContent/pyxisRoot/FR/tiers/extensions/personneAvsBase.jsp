<%@ page import="globaz.pyxis.db.adressecourrier.*,globaz.pyxis.db.tiers.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
TITiersViewBean viewBean = (TITiersViewBean)session.getAttribute ("viewBean");
%>

<tr>
	<td valign="top">
		<table  >
			<tr>
				<td>Titre</td>
				<td>
			        <ct:FWCodeSelectTag name="titreTiers"
			            	defaut="<%=viewBean.getTitreTiers()%>"
					codeType="PYTITRE" wantBlank="true"/>
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifTitre" value="">
					<input type ="hidden" name="dateModifTitre" value="">
					<input type ="hidden" name="oldTitre" value="<%=viewBean.getOldTitre()%>">
				</td>
			</tr>
			<tr>
				<td>Nom ou Raison soc.</td>
				<td>
					<INPUT  name="designation1" type="text" maxlength="40" value="<%=viewBean.getDesignation1()%>"  onblur="fieldFormat(this,'')" class="libelleLong">
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifDesignation1" value="">
					<input type ="hidden" name="dateModifDesignation1" value="">
					<input type ="hidden" name="oldDesignation1" value="<%=viewBean.getOldDesignation1()%>">
			
				</td>
			</tr>
			<tr>
				<td>Prénom ou Rais. soc.</td>
				<td>
				  <INPUT  name="designation2" type="text" maxlength="40" value="<%=viewBean.getDesignation2()%>" doClientValidation="NOT_EMPTY_IF:typeTiers:500001:500003" onblur="fieldFormat(this,'')" class="libelleLong">
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifDesignation2" value="">
					<input type ="hidden" name="dateModifDesignation2" value="">
					<input type ="hidden" name="oldDesignation2" value="<%=viewBean.getOldDesignation2()%>">
			
				</td>
			</tr>
			<tr>
				<td>Nom (suite) 1</td>
				<td>
				  <INPUT name="designation3" type="text" maxlength="40" value="<%=viewBean.getDesignation3()%>" class="libelleLong">
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifDesignation3" value="">
					<input type ="hidden" name="dateModifDesignation3" value="">
					<input type ="hidden" name="oldDesignation3" value="<%=viewBean.getOldDesignation3()%>">
				</td>
			</tr>
			<tr>
				<td>Nom (suite) 2</td>
				<td>
					<INPUT  name="designation4" type="text" maxlength="40" value="<%=viewBean.getDesignation4()%>" class="libelleLong">
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifDesignation4" value="">
					<input type ="hidden" name="dateModifDesignation4" value="">
					<input type ="hidden" name="oldDesignation4" value="<%=viewBean.getOldDesignation4()%>">
			
			
				</td>
			</tr>
		</table>
	</td>
	<td  style="border-left : solid 1px gray">&nbsp</td>
	
	<td valign="top">
		<table>
			<tr>
				<td>Langue</td>
				<td>
					  <ct:FWCodeSelectTag name="langue"
					    defaut="<%=viewBean.getLangue()%>"
				      codeType="PYLANGUE"/>
				</td>
			</tr>
			<tr>
				<td>Nationalité</td>
				<td nowrap><span>
					<input tabindex="-1" type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getNomPays()%>">
					<%
						Object[] adresseMethodsName = new Object[]{
							new String[]{"setIdPays","getIdPays"},
							new String[]{"setNomPays","getLibelle"}
						};
					%>
					<ct:FWSelectorTag 
						name="paysSelector" 
						
						methods="<%=adresseMethodsName %>"
						providerApplication ="pyxis"
						providerPrefix="TI"
						providerAction ="pyxis.adressecourrier.pays.chercher"
					/>
					
					</span>
					<input type="hidden"  name="idPays" value="<%=viewBean.getIdPays()%>">
					<!-- champs caché pour la creation de l'historique -->
					<input type ="hidden" name="motifModifPays" value="">
					<input type ="hidden" name="dateModifPays" value="">
					<input type ="hidden" name="oldPays" value="<%=viewBean.getOldPays()%>">
				</td>
			</tr>
			<tr>
				<td>Canton</td>
				<td>
					<input tabindex="-1" readonly class="libelleLongDisabled" value="<%=viewBean.getCantonDomicile()%>">
				</td>
			</tr>
			<tr> 
				<td>Type de tiers</td>
				<td>
					<%if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { %>
						
						
						<ct:FWCodeSelectTag name="typeTiers"
							defaut="<%=viewBean.getTypeTiers()%>"
							codeType="PYTYPTIERS"
							except="<%=viewBean.getExceptTypeTiers()%>"/>
						
						<b><%=viewBean.getSession().getCodeLibelle(viewBean.getTypeTiers())%></b>
	
					<%} else {%>
						<ct:FWCodeSelectTag name="typeTiers"
							defaut="<%=viewBean.getTypeTiers()%>"
							codeType="PYTYPTIERS"
							except="<%=viewBean.getExceptTypeTiers()%>"/>
	
					<%}%>
				</td>
					
			</tr>
			<tr>
				<td>Personne</td>
				<td>
					physique<input type="checkbox" name="personnePhysique" <%=(viewBean.getPersonnePhysique().booleanValue())?"CHECKED":""%>>
					&nbsp;&nbsp; morale <input type="checkbox" name="personneMorale" <%=(viewBean.getPersonneMorale().booleanValue())?"CHECKED":""%>>
				</td>
			</tr>
			
			<tr>
				<td>Inactif</td>
				<td>
					<input tabindex ="20" type="checkbox" name="inactif" <%=(viewBean.getInactif().booleanValue())?"CHECKED":""%>>
				</td>
			</tr>
		</table>
	</td>
</tr>

