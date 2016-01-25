
						
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="me<%=i%>" value="<%=showValue?viewBean.getMontantEtranger(i):"0.00"%>" readonly="readonly" disabled="disabled"/></td>
						<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this,5);updateMontantChf(<%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="c<%=i%>" value="<%=showValue?viewBean.getCours(i):"0.00000"%>" readonly="readonly" disabled="disabled"/></td>
					</tr>
					<%} %>
					<tr class="somme">
						<td colspan="3" align="right" class="mtdBold">Total :
						<input type="hidden" name="flv" value="" onclick="equilibrate()"/>
						</td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="se" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="readonly" disabled="readonly"/></td>
						<td>&nbsp;</td>
					</tr>

					<tr class="balance">
						<td colspan="3" class="mtd" align="right">Balance :</td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bd" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
						<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="bc" value="<%=showValue?globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2):""%>" readonly="true" disabled="true"/></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right" height="24">
				<%if (viewBean.isJournalEditable() && objSession.hasRight("lynx.facture.facture.afficher", globaz.framework.secure.FWSecureConstants.UPDATE)) {%>
				<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" ><img src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Ajouter une ligne"/></a>
				<%} else {%>
				&nbsp;
				<%}%>
			</td>
		</tr>
	</TBODY>
</TABLE>
