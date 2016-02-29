package price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OperacaoCredito {
	private List<PlanilhaOperacaoTO> simulaOperacaoCredito(Date dataOperacao,
			Date dataPrimeiraPrestacao, Double taxaOperacao,
			Integer quantidadeParcelas, PeriodicidadeEnum periodicidade,
			Double valorTotalOperacao, Double taxaIofAdicional,
			Double taxaIofAliquota, Double percentualBonus) {

		List<PlanilhaOperacaoTO> planilhaOperacaoList = new ArrayList<PlanilhaOperacaoTO>();

		if(!periodicidade.equals(PeriodicidadeEnum.MES)) {
			taxaOperacao = calcularTaxaMensal(taxaOperacao, periodicidade);
		}
		
		taxaOperacao = round(taxaOperacao, 4);
		
		Date dataOperacaoAnterior = removeMes(dataPrimeiraPrestacao);
		Integer diasEntreContratacaoEPrimeiraParcela = new Integer(CalendarioUtil
				.calculaDiasComerciaisCorridos(dataOperacao,
						dataOperacaoAnterior));
		
		System.out.println("Parcelas:  " + quantidadeParcelas + " | Diferença dias: " + diasEntreContratacaoEPrimeiraParcela);
		
		Double diferencaJurosPrimeiraParcela = getJurosPrimeiraParcela(valorTotalOperacao,
				taxaOperacao, periodicidade, diasEntreContratacaoEPrimeiraParcela);
		Double valorTotalOperacaoSemJurosPrimeiraParcela = valorTotalOperacao - diferencaJurosPrimeiraParcela;

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
				quantidadeParcelas, taxaOperacao, periodicidade, diasEntreContratacaoEPrimeiraParcela,
				valorTotalOperacaoSemJurosPrimeiraParcela, percentualBonus);

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
			Date dataPrimeiraPrestacao, Integer quantidadeParcelas) {

		if (listaPlanilhaOperacao == null) {
			listaPlanilhaOperacao = new ArrayList<PlanilhaOperacaoTO>();
		}

		PlanilhaOperacaoTO planilhaOperacao = null;

		Date dataVencimentoTemp = new Date();
		Integer diasCorridosTemp = 0;
		Integer diasDiferencaParcelaTemp = 0;

		for (int i = 0; i < quantidadeParcelas; i++) {

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
			Double valorTotalOperacao, Integer quantidadeParcelas,
			Double taxaOperacao, PeriodicidadeEnum periodicidade,
			Integer diasEntreDataOperacaoInicioPrimeiraParcela, Double valorTotalOperacaoSemJurosPrimeiraParcela, Double percentualBonus) {
		// 21681.15
		Double saldoBase = getSaldoBase(valorTotalOperacao, taxaOperacao,
				periodicidade, diasEntreDataOperacaoInicioPrimeiraParcela);
		
		Double parcela = getObtemValorParcela(saldoBase, taxaOperacao,
				quantidadeParcelas);

		Integer auxiliar = 1;
		Double totalPrincipal = 0.0;
		Double taxaOperacaoPercentual = taxaOperacao / 100;
		Double principal = 0.0;
		Double valorTotalOperacaoTemp = parcela * quantidadeParcelas;
		Double diferencaJurosPrimeiraParcela = valorTotalOperacao - valorTotalOperacaoSemJurosPrimeiraParcela;
		Double principalAcumulado = 0.0;
		Double saldoDevedorAtual = valorTotalOperacao;
		Double bonusTemp = 0.0;
		Double bonusJurosTemp = 0.0;
		Double valorParcelaBonus = 0.0;

		// Cálculo Principal, Juros
		while (auxiliar <= quantidadeParcelas) {

			Double juros = taxaOperacaoPercentual * saldoBase;
			juros = round(juros, 2);
			Double jurosCredito = juros + valorTotalOperacaoSemJurosPrimeiraParcela;

			if (jurosCredito > parcela) {
				jurosCredito = parcela;
			}

			principal = parcela - jurosCredito;

			saldoBase = saldoBase - jurosCredito + juros - principal;

			valorTotalOperacaoSemJurosPrimeiraParcela = valorTotalOperacaoSemJurosPrimeiraParcela - jurosCredito + juros;

			totalPrincipal = totalPrincipal + principal;

			if (valorTotalOperacao != totalPrincipal) {

				principal = principal + (valorTotalOperacao - totalPrincipal);
				Double principalTemp = 0.0;
				Double jurosTemp = 0.0;

				if (auxiliar == 1) {
					principalTemp = parcela
							- round((juros + diferencaJurosPrimeiraParcela), 4);
					jurosTemp = juros + diferencaJurosPrimeiraParcela;
					principalAcumulado = +principalAcumulado + principalTemp;
					saldoDevedorAtual = saldoDevedorAtual - principalTemp;
				} else {
					principalTemp = round((parcela - round(juros, 4)), 3);
					jurosTemp = roundHalfDown(juros, 4);
					saldoDevedorAtual = saldoDevedorAtual - principalTemp;
					principalAcumulado = +principalAcumulado + principalTemp;
				}

				Integer idParcela = auxiliar - 1;
				// Ultima Parcela
				if (auxiliar == (quantidadeParcelas)) {
					Double diferencaTotal = valorTotalOperacao
							- principalAcumulado;
					diferencaTotal = round(diferencaTotal, 2);
					principalTemp = (principalTemp + diferencaTotal);
					principalTemp = round(principalTemp, 2);
					planilhaOperacaoList.get(idParcela).setPrincipalParcela(
							principalTemp);
				}

				// Adiciona Saldo Devedor / Principal / Juros
				planilhaOperacaoList.get(idParcela).setSaldoDevedor(
						round(saldoDevedorAtual, 2));
				planilhaOperacaoList.get(idParcela).setPrincipalParcela(
						round(principalTemp, 2));
				planilhaOperacaoList.get(idParcela).setJurosParcela(
						round(jurosTemp, 2));
				planilhaOperacaoList.get(idParcela).setValorTotalOperacao(
						round(valorTotalOperacaoTemp, 2));
				planilhaOperacaoList.get(idParcela).setValorParcela(
						round(parcela, 2));

				// Juros Bonus
				bonusTemp = round(jurosTemp, 2) * percentualBonus;
				bonusJurosTemp = round(jurosTemp, 2) - bonusTemp;
				valorParcelaBonus = round(principalTemp, 2) + bonusJurosTemp;

				planilhaOperacaoList.get(idParcela).setBonus( 
						round(bonusTemp, 2));
				planilhaOperacaoList.get(idParcela).setJurosBonus(
						round(bonusJurosTemp, 2));
				planilhaOperacaoList.get(idParcela).setValorParcelaBonus(
						round(valorParcelaBonus, 2));
			}
			auxiliar++;
		}
	}

	private void calculoIof(List<PlanilhaOperacaoTO> listaPlanilhaOperacao,
			Double taxaIofAliquotaDia, Double taxaIofAdicional,
			Double valorTotalOperacao) {

		Double iofTotalTradicional = 0.0;
		Double iofTotalAdicional = 0.0;
		Double iofTotal = 0.0;

		// Iof Tradicional por principal de cada parcela
		for (PlanilhaOperacaoTO parcela : listaPlanilhaOperacao) {
			if (parcela.getPrincipalParcela() != null) {
				calculaIofTradicional(parcela, taxaIofAliquotaDia);
				iofTotalTradicional = iofTotalTradicional
						+ parcela.getIofParcela();
			}
		}

		// Cálculo do Iof Adicional de todo o Principal da Operação
		iofTotalAdicional = calculaIofAdicional(valorTotalOperacao,
				taxaIofAdicional);
		System.out.println("IOF Adicional" + iofTotalAdicional);
		// Seta Iof Total Calculado
		iofTotal = round((iofTotalAdicional + iofTotalTradicional), 2);
		for (PlanilhaOperacaoTO parcela : listaPlanilhaOperacao) {
			parcela.setIofTotal(iofTotal);
		}

	}

	private Double getJurosPrimeiraParcela(Double valorTotalOperacao,
			Double taxaOperacao, PeriodicidadeEnum periodicidade,
			Integer diferenca) {

		Double a = 1 + (taxaOperacao / 100);
		Double b = diferenca / new Double(periodicidade.quantidadeDias);
		Double resultado = Math.pow(a, b) - 1;

		return round((valorTotalOperacao * resultado), 8);

	}

	private Double getSaldoBase(Double valorTotalOperacao, Double taxaOperacao,
			PeriodicidadeEnum periodicidade, Integer diferencaDias) {
		Double a = 1 + (taxaOperacao / 100);
		Double b = diferencaDias / new Double(periodicidade.quantidadeDias);
		Double resultado = Math.pow(a, b);

		return round((valorTotalOperacao * resultado), 2);
	}

	private Date getDataUltimoVencimento(Integer parcelas, Date dataOperacao) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataOperacao);
		cal.add(Calendar.MONTH, ++parcelas);
		Date dataVencimentoUltimaParcela = cal.getTime();
		return dataVencimentoUltimaParcela;
	}

	private Double getObtemValorParcela(Double saldoBase, Double taxaOperacao,
			Integer prazoTotalOperacao) {
		Double taxaOperacaoPercentual = taxaOperacao / 100;
		Double a = Math.pow((1 + taxaOperacaoPercentual), prazoTotalOperacao);
		Double b = (Math.pow((1 + taxaOperacaoPercentual), prazoTotalOperacao) - 1);
		Double c = (a / b);
		Double d = taxaOperacaoPercentual * c;
		Double e = saldoBase * d;
		return round(e, 2);

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
			Double taxaAliquotaIofDia) {

		Double iofTradicionalCalculado = 0.0;

		// Cálculo até 1 ano (365 Dias)
		if (parcela.getDiasCorridos() <= 365) {
			iofTradicionalCalculado = parcela.getPrincipalParcela() * parcela.getDiasCorridos() * (taxaAliquotaIofDia / 100);
			parcela.setIofParcela(round(iofTradicionalCalculado, 4));
			// Cálculo IOF após 365 Dias
		} else {
			iofTradicionalCalculado = parcela.getPrincipalParcela() * 365 * ( taxaAliquotaIofDia / 100);
			parcela.setIofParcela(round(iofTradicionalCalculado, 4));

		}
	}

	private Double calculaIofAdicional(Double valorOperacao,
			Double taxaIofAdicional) {
		// System.out.println("IOF adicional - " + valorOperacao *
		// (taxaIofAdicional / 100));
		return round(valorOperacao * (taxaIofAdicional / 100), 4);
	}

	private Double calcularTaxaMensal(Double taxaAnual,
			PeriodicidadeEnum periodicidade) {
		Double a = (taxaAnual / 100) + 1;
		Double b = 30.0 / new Double(periodicidade.quantidadeDias);
		Double taxaMensal = Math.pow(a, b) - 1;
		return round(taxaMensal * 100, 4);
	}
}