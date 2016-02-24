package price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalculadoraFinanciamento {
	
	public List<PlanilhaOperacaoTO> simulaOperacaoCredito(Date dataOperacao,
			Date dataPrimeiraPrestacao, ValorTaxa taxaOperacao,
			ValorInteiro quantidadeParcelas, PeriodicidadeEnum periodicidade,
			ValorMonetario valorTotalOperacao, ValorTaxa taxaIofAdicional,
			ValorTaxa taxaIofAliquota, ValorTaxa percentualBonus) {

		List<PlanilhaOperacaoTO> planilhaOperacaoList = new ArrayList<PlanilhaOperacaoTO>();

		if(!periodicidade.equals(PeriodicidadeEnum.MES)) {
			taxaOperacao = calcularTaxaMensal(taxaOperacao, periodicidade);
		}
		
		Date dataOperacaoAnterior = removeMes(dataPrimeiraPrestacao);
		ValorInteiro diferencaDias = new ValorInteiro(CalendarioUtil
				.calculaDiasComerciaisCorridos(dataOperacao,
						dataOperacaoAnterior));
		
		System.out.println("Parcelas:  " + quantidadeParcelas + " | Diferença dias: " + diferencaDias);
		
		ValorTaxa diferencaJuros = getJurosPrimeiraParcela(valorTotalOperacao,
				taxaOperacao, periodicidade, diferencaDias);
		ValorMonetario diferencaSaldo = new ValorMonetario(valorTotalOperacao.subtrai(diferencaJuros).getValor());

		/*
		 * 1 - Inicializa Planilha de Operação: 1.1 Identificador Parcela 1.2
		 * Vencimento Parcela 1.3 Diferença (dias) entre Parcelas 1.4 Diferença
		 * (dias) Acumulada entre Parcelas
		 */
		inicializaPlanilhaOperacao(planilhaOperacaoList, dataOperacao,
				dataPrimeiraPrestacao, quantidadeParcelas);

		/*
		 * 2 - Processo de Cálculo PRICE 2.1 Saldo Base 2.2 Valor da Parcela 2.3
		 * Principal / Juros
		 */
		calculoPrestacaoPrice(planilhaOperacaoList, valorTotalOperacao,
				quantidadeParcelas, taxaOperacao, periodicidade, diferencaDias,
				diferencaSaldo, percentualBonus);

		/*
		 * 3 - Processo de Cálculo IOF 3.1 Iof tradicional 3.2 Iof Adicional
		 */

		calculoIof(planilhaOperacaoList, taxaIofAliquota, taxaIofAdicional,
				valorTotalOperacao);

		imprimirParcelas(planilhaOperacaoList);

		return planilhaOperacaoList;

	}

	private void imprimirParcelas(List<PlanilhaOperacaoTO> planilhaOperacaoLista) {

		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");

		for (PlanilhaOperacaoTO parcela : planilhaOperacaoLista) {
			System.out.println("\t" + parcela.getNumeroParcela() + "\t"
					+ sd.format(parcela.getVencimentoParcela()) + "\t"
					+ parcela.getDiasCorridos() + "\t "
					+ parcela.getDiasDiferencaParcelas() + "\t"
					+ parcela.getSaldoDevedor() + "\t"
					+ parcela.getPrincipalParcela() + "\t"
					+ parcela.getJurosParcela() + "\t"
					+ parcela.getValorParcela() + "\t"
					+ parcela.getIofParcela() + "\t" + parcela.getBonus()
					+ "\t" + parcela.getJurosBonus() + "\t"
					+ parcela.getValorParcelaBonus()

			);
		}

		System.out.println("IOF Total: "
				+ planilhaOperacaoLista.get(0).getIofTotal());

	}

	private void inicializaPlanilhaOperacao(
			List<PlanilhaOperacaoTO> listaPlanilhaOperacao, Date dataOperacao,
			Date dataPrimeiraPrestacao, ValorInteiro quantidadeParcelas) {

		if (listaPlanilhaOperacao == null) {
			listaPlanilhaOperacao = new ArrayList<PlanilhaOperacaoTO>();
		}

		PlanilhaOperacaoTO planilhaOperacao = null;

		Date dataVencimentoTemp = new Date();
		Integer diasCorridosTemp = 0;
		Integer diasDiferencaParcelaTemp = 0;

		for (int i = 0; i < quantidadeParcelas.getValor().intValue(); i++) {

			// Número da Parcela
			int numeroParcela = i;
			planilhaOperacao = null;
			planilhaOperacao = new PlanilhaOperacaoTO();
			numeroParcela++;
			planilhaOperacao.setNumeroParcela(numeroParcela);

			// Vencimento da Parcela
			// Primeira Parcela
			if (i == 0) {
				// Vencimento Parcela
				dataVencimentoTemp = dataPrimeiraPrestacao;
				planilhaOperacao.setVencimentoParcela(dataPrimeiraPrestacao);
				// Dias Corridos
				diasCorridosTemp = CalendarioUtil.calculaDiasCorridos(
						dataPrimeiraPrestacao, dataOperacao);
				planilhaOperacao.setDiasCorridos(diasCorridosTemp);
				// Diferenca Parcelas
				planilhaOperacao.setDiasDiferencaParcelas(diasCorridosTemp);
				// Add list
				listaPlanilhaOperacao.add(planilhaOperacao);

				// Demais Parcelas
			} else {
				// Vencimento Parcela
				Date vencimentoParcelaAnterior = dataVencimentoTemp;
				dataVencimentoTemp = adicionaMes(dataVencimentoTemp);
				planilhaOperacao.setVencimentoParcela(dataVencimentoTemp);
				// Dias Corridos
				diasCorridosTemp = +CalendarioUtil.calculaDiasCorridos(
						dataVencimentoTemp, dataOperacao);
				planilhaOperacao.setDiasCorridos(diasCorridosTemp);
				// Diferenca Parcelas
				diasDiferencaParcelaTemp = CalendarioUtil.calculaDiasCorridos(
						dataVencimentoTemp, vencimentoParcelaAnterior);
				planilhaOperacao
						.setDiasDiferencaParcelas(diasDiferencaParcelaTemp);
				// Add list
				listaPlanilhaOperacao.add(planilhaOperacao);
			}
		}
	}

	private void calculoPrestacaoPrice(
			List<PlanilhaOperacaoTO> planilhaOperacaoList,
			ValorMonetario valorTotalOperacao, ValorInteiro quantidadeParcelas,
			ValorTaxa taxaOperacao, PeriodicidadeEnum periodicidade,
			ValorInteiro diferencaDias, ValorMonetario diferenca, ValorTaxa percentualBonus) {
		// 21681.15
		ValorMonetario saldoBase = getSaldoBase(valorTotalOperacao, taxaOperacao,
				periodicidade, diferencaDias);
		ValorMonetario parcela = getObtemValorParcela(saldoBase, taxaOperacao,
				quantidadeParcelas);

		Integer auxiliar = 1;
		ValorMonetario totalPrincipal = new ValorMonetario(0.0);
		Decimal taxaOperacaoPercentual = taxaOperacao.dividePor(new Decimal(100));
		ValorMonetario principal = new ValorMonetario(0.0);
		Decimal valorTotalOperacaoTemp = parcela.multiplicaPor(quantidadeParcelas);
		Decimal diferencaJurosPrimeiraParcela = valorTotalOperacao.subtrai(diferenca);
		ValorMonetario principalAcumulado = new ValorMonetario(0.0);
		ValorMonetario saldoDevedorAtual = new  ValorMonetario(valorTotalOperacao.getValor());
		ValorMonetario bonusTemp = new ValorMonetario(0.0);
		ValorMonetario bonusJurosTemp = new ValorMonetario(0.0);
		ValorMonetario valorParcelaBonus = new ValorMonetario(0.0);

		// Cálculo Principal, Juros
		while (auxiliar <= quantidadeParcelas.getValor().intValue()) {

			ValorMonetario juros = new ValorMonetario(taxaOperacaoPercentual.multiplicaPor(saldoBase).getValor());
			ValorMonetario jurosCredito = new ValorMonetario(juros.soma(diferenca).getValor());

			if (jurosCredito.getValor() > parcela.getValor()) {
				jurosCredito = parcela;
			}

			principal = new ValorMonetario(parcela.subtrai(jurosCredito).getValor());

			saldoBase = new ValorMonetario(saldoBase.subtrai(jurosCredito).soma(juros).subtrai(principal).getValor());

			diferenca = new ValorMonetario(diferenca.subtrai(jurosCredito).soma(juros).getValor());

			totalPrincipal = new ValorMonetario(totalPrincipal.soma(principal).getValor());

			if (valorTotalOperacao != totalPrincipal) {

				principal = new ValorMonetario(principal.soma(valorTotalOperacao.subtrai(totalPrincipal)).getValor());
				ValorMonetario principalTemp = new ValorMonetario(0.0);
				ValorMonetario jurosTemp = new ValorMonetario(0.0);

				if (auxiliar == 1) {
					principalTemp = new ValorMonetario(parcela.subtrai(juros.soma(diferencaJurosPrimeiraParcela)).getValor());
					
					jurosTemp = new ValorMonetario(juros.soma(diferencaJurosPrimeiraParcela).getValor());
					principalAcumulado = new ValorMonetario(principalAcumulado.soma(principalTemp).getValor());
					saldoDevedorAtual = new ValorMonetario(saldoDevedorAtual.subtrai(principalTemp).getValor());
				} else {
					principalTemp = new ValorMonetario(parcela.subtrai(juros).getValor());
					jurosTemp = new ValorMonetario(juros.getValor());
					saldoDevedorAtual = new ValorMonetario(saldoDevedorAtual.subtrai(principalTemp).getValor());
					principalAcumulado = new ValorMonetario(principalAcumulado.soma(principalTemp).getValor());
				}

				Integer idParcela = auxiliar - 1;
				// Ultima Parcela
				if (auxiliar == (quantidadeParcelas.getValor().intValue())) {
					ValorMonetario diferencaTotal = new ValorMonetario(valorTotalOperacao.subtrai(principalAcumulado).getValor());
					
					principalTemp = new ValorMonetario(principalTemp.soma(diferencaTotal).getValor());
					planilhaOperacaoList.get(idParcela).setPrincipalParcela(
							principalTemp.getValor());
				}

				// Adiciona Saldo Devedor / Principal / Juros
				planilhaOperacaoList.get(idParcela).setSaldoDevedor(saldoDevedorAtual.getValor());
				planilhaOperacaoList.get(idParcela).setPrincipalParcela(principalTemp.getValor());
				planilhaOperacaoList.get(idParcela).setJurosParcela(jurosTemp.getValor());
				planilhaOperacaoList.get(idParcela).setValorTotalOperacao(valorTotalOperacaoTemp.getValor());
				planilhaOperacaoList.get(idParcela).setValorParcela(parcela.getValor());

				// Juros Bonus
				bonusTemp = new ValorMonetario(jurosTemp.multiplicaPor(percentualBonus).getValor());
				bonusJurosTemp = new ValorMonetario(jurosTemp.subtrai(bonusTemp).getValor());
				valorParcelaBonus = new ValorMonetario(principalTemp.soma(bonusJurosTemp).getValor());

				planilhaOperacaoList.get(idParcela).setBonus(bonusTemp.getValor());
				planilhaOperacaoList.get(idParcela).setJurosBonus(bonusJurosTemp.getValor());
				planilhaOperacaoList.get(idParcela).setValorParcelaBonus(valorParcelaBonus.getValor());
			}
			auxiliar++;
		}
	}

	private void calculoIof(List<PlanilhaOperacaoTO> listaPlanilhaOperacao,
			ValorTaxa taxaIofAliquotaDia, ValorTaxa taxaIofAdicional,
			ValorMonetario valorTotalOperacao) {

		ValorMonetario iofTotalTradicional = new ValorMonetario(0.0);
		ValorMonetario iofTotalAdicional = new ValorMonetario(0.0);
		ValorMonetario iofTotal = new ValorMonetario(0.0);

		// Iof Tradicional por principal de cada parcela
		for (PlanilhaOperacaoTO parcela : listaPlanilhaOperacao) {
			if (parcela.getPrincipalParcela() != null) {
				calculaIofTradicional(parcela, taxaIofAliquotaDia);
				
				ValorMonetario iofParcela = new ValorMonetario(parcela.getIofParcela());
				iofTotalTradicional = new ValorMonetario(iofTotalTradicional.soma(iofParcela).getValor());
			}
		}

		// Cálculo do Iof Adicional de todo o Principal da Operação
		iofTotalAdicional = calculaIofAdicional(valorTotalOperacao,
				taxaIofAdicional);
		System.out.println("IOF Adicional" + iofTotalAdicional);
		// Seta Iof Total Calculado
		iofTotal = new ValorMonetario(iofTotalAdicional.soma(iofTotalTradicional).getValor());
		for (PlanilhaOperacaoTO parcela : listaPlanilhaOperacao) {
			parcela.setIofTotal(iofTotal.getValor());
		}

	}

	private ValorTaxa getJurosPrimeiraParcela(ValorMonetario valorTotalOperacao,
			ValorTaxa taxaOperacao, PeriodicidadeEnum periodicidade,
			ValorInteiro diferenca) {

		Decimal um = new Decimal(1);
		Decimal cem = new Decimal(100);
		Decimal dias = new Decimal(new Double(periodicidade.quantidadeDias));
		
		Decimal a = um.soma(taxaOperacao.dividePor(cem));
		Decimal b = diferenca.dividePor(dias);
		Decimal resultado = a.potenciaDe(b).subtrai(um);

		return new ValorTaxa(resultado.getValor());

	}

	private ValorMonetario getSaldoBase(ValorMonetario valorTotalOperacao, ValorTaxa taxaOperacao,
			PeriodicidadeEnum periodicidade, ValorInteiro diferencaDias) {
		
		Decimal um = new Decimal(1);
		Decimal cem = new Decimal(100);
		Decimal dias = new Decimal(new Double(periodicidade.quantidadeDias));
		
		Decimal a = um.soma(taxaOperacao.dividePor(cem));
		Decimal b = diferencaDias.dividePor(dias);
		Decimal resultado = a.potenciaDe(b);
		
		return new ValorMonetario(valorTotalOperacao.multiplicaPor(resultado).getValor()); 
	}

	private Date getDataUltimoVencimento(Integer parcelas, Date dataOperacao) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataOperacao);
		cal.add(Calendar.MONTH, ++parcelas);
		Date dataVencimentoUltimaParcela = cal.getTime();
		return dataVencimentoUltimaParcela;
	}

	private ValorMonetario getObtemValorParcela(ValorMonetario saldoBase, ValorTaxa taxaOperacao,
			ValorInteiro prazoTotalOperacao) {
		
		Decimal um = new Decimal(1);
		Decimal cem = new Decimal(100);
		
		Decimal taxaOperacaoPercentual = taxaOperacao.dividePor(cem);
		Decimal a = um.soma(taxaOperacaoPercentual).potenciaDe(prazoTotalOperacao);
		Decimal b = um.soma(taxaOperacaoPercentual).potenciaDe(prazoTotalOperacao).subtrai(um);
		Decimal c = a.dividePor(b);
		Decimal d = taxaOperacaoPercentual.multiplicaPor(c);
		Decimal e = saldoBase.multiplicaPor(d);
		
		return new ValorMonetario(e.getValor());

	}

	private Date removeMes(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, -1);
		Date dataRetorno = cal.getTime();
		return dataRetorno;
	}

	private Date adicionaMes(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, 1);
		Date dataRetorno = cal.getTime();
		return dataRetorno;
	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}

	private double roundUp(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.UP);
		return bd.doubleValue();
	}

	private double roundHalfDown(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}

	private void calculaIofTradicional(PlanilhaOperacaoTO parcela,
			ValorTaxa taxaAliquotaIofDia) {
		
		Decimal cem = new Decimal(100);
		ValorMonetario iofTradicionalCalculado = new ValorMonetario(0.0);

		// Cálculo até 1 ano (365 Dias)
		if (parcela.getDiasCorridos() <= 365) {
			
			ValorTaxa valorTaxaAliquotaIofDia = new ValorTaxa(taxaAliquotaIofDia.dividePor(cem).getValor());
			ValorMonetario valorMonetarioPrincipalParcela = new ValorMonetario(parcela.getPrincipalParcela());
			ValorInteiro valorInteiroDiasCorridos = new ValorInteiro(parcela.getDiasCorridos());
			
			iofTradicionalCalculado = new ValorMonetario(valorMonetarioPrincipalParcela
					.multiplicaPor(valorInteiroDiasCorridos)
					.multiplicaPor(valorTaxaAliquotaIofDia).getValor());
			
			parcela.setIofParcela(iofTradicionalCalculado.getValor());
			// Cálculo IOF após 365 Dias
		} else {
			
			ValorInteiro diasAno = new ValorInteiro(365);
			
			ValorTaxa valorTaxaAliquotaIofDia = new ValorTaxa(taxaAliquotaIofDia.multiplicaPor(diasAno).dividePor(cem).getValor());
			ValorMonetario valorMonetarioPrincipalParcela = new ValorMonetario(parcela.getPrincipalParcela());
			
			iofTradicionalCalculado = new ValorMonetario(valorMonetarioPrincipalParcela.multiplicaPor(valorTaxaAliquotaIofDia).getValor()); 			
			
			parcela.setIofParcela(iofTradicionalCalculado.getValor());

		}
	}

	private ValorMonetario calculaIofAdicional(ValorMonetario valorOperacao,
			ValorTaxa taxaIofAdicional) {
		
		Decimal cem = new Decimal(100);
		
		return new ValorMonetario(valorOperacao.multiplicaPor(taxaIofAdicional.dividePor(cem)).getValor());
	}

	private ValorTaxa calcularTaxaMensal(ValorTaxa taxaAnual,
			PeriodicidadeEnum periodicidade) {
		
		Decimal a = taxaAnual.dividePor(new Decimal(100.0)).soma(new Decimal(1.0));
		Decimal b = new Decimal(30.0).dividePor(new Decimal(new Double(periodicidade.quantidadeDias)));
		Decimal taxaMensal = a.potenciaDe(b).subtrai(new Decimal(1.0));
		
		return new ValorTaxa(taxaMensal.getValor() * 100.0);
	}
}