package price;

import java.util.Calendar;
import java.util.Date;

public class CalculadoraFinanceira {

	public static Financiamento calcularFinanciamento(OpcoesFinanciamento opcoes) {
		
		Financiamento financiamento = new Financiamento(opcoes);
		
		Juros jurosMensais = converterParaJurosCompostosMensais(opcoes.getJuros());

		Date umMesAntesDoPrimeiroVencimento = subtrairUmMes(opcoes.getDataPrimeiroVencimento()); 
		
		ValorInteiro carenciaEmDias = new ValorInteiro(
				subtrairDatas(
						umMesAntesDoPrimeiroVencimento,
						opcoes.getDataFinanciamento())); 
				
		ValorMonetario saldoDevedorInicial = calcularSaldoDevedorAposCarencia(
				opcoes.getValorFinanciado(),
				jurosMensais,
				carenciaEmDias);
		
		ValorMonetario valorDaParcela = calcularValorDaParcela(
				saldoDevedorInicial,
				jurosMensais,
				opcoes.getQuantidadeParcelas());

		ValorMonetario saldoDevedorAtual = new ValorMonetario(saldoDevedorInicial);
		
		ValorMonetario jurosTotal = new ValorMonetario(0.0);
		
		for (int numeroParcela = 1; numeroParcela <= opcoes.getQuantidadeParcelas().getValor(); numeroParcela++) {
			
			Date dataVencimento = calcularDataVencimentoParcela(
					opcoes.getDataPrimeiroVencimento(),
					numeroParcela);
			
			ValorMonetario valorJuros = jurosMensais.getTaxa()
						.multiplicaPor(saldoDevedorAtual)
						.dividePor(ValorDecimal.CEM)
						.valorMonetario();

			ValorMonetario valorPrincipal = valorDaParcela
						.subtrai(valorJuros)
						.valorMonetario();
			
			ValorMonetario valorIofDiario = calcularIofDiarioParcela(
					valorPrincipal,
					opcoes.getTaxaIofDiario(),
					opcoes.getDataFinanciamento(),
					dataVencimento);

			Parcela parcela = criarParcela(
					valorDaParcela,
					saldoDevedorAtual,
					numeroParcela,
					dataVencimento,
					valorJuros,
					valorPrincipal,
					valorIofDiario);
			
			financiamento.adicionaParcela(parcela);
			
			jurosTotal = jurosTotal.soma(valorJuros)
					.valorMonetario();
			
			saldoDevedorAtual = saldoDevedorAtual
					.subtrai(valorPrincipal)
					.valorMonetario();
		}
		
		financiamento.setJurosTotal(jurosTotal);

		return financiamento;
	}

	public static ValorMonetario calcularValorDaParcela(ValorMonetario saldoBase, Juros juros,
			ValorInteiro quantidadeParcelas) {

		Juros jurosMensais = converterParaJurosCompostosMensais(juros);
		ValorDecimal percentualJuros = jurosMensais.getTaxa().dividePor(ValorDecimal.CEM);

		ValorDecimal fator1 = ValorDecimal.UM.soma(percentualJuros).potenciaDe(quantidadeParcelas);
		ValorDecimal fator2 = fator1.subtrai(ValorDecimal.UM);
		ValorDecimal fator3 = fator1.dividePor(fator2);
		ValorDecimal fator4 = percentualJuros.multiplicaPor(fator3).multiplicaPor(saldoBase);

		return fator4.valorMonetario();
	}

	public static ValorMonetario calcularSaldoDevedorAposCarencia(ValorMonetario valorTotalOperacao, Juros juros,
			ValorInteiro diasDecorridosDaContratacao) {

		PeriodicidadeTaxa periodicidade = PeriodicidadeTaxa
				.criarPeriodicidadeDiasCorridos(diasDecorridosDaContratacao);
		
		Juros jurosEquivalente = converterParaJurosCompostosDoPeriodo(
				juros,
				periodicidade);
		
		ValorDecimal valorJuros = valorTotalOperacao.multiplicaPor(
						jurosEquivalente.getTaxa()
							.dividePor(ValorDecimal.CEM));
		
		ValorDecimal valorSaldoBaseCalculado = valorTotalOperacao.soma(valorJuros);

		return valorSaldoBaseCalculado.valorMonetario();
	}

	public static ValorMonetario calcularIofDiarioParcela(ValorMonetario valorPrincipalParcela, ValorTaxa iofDiario,
			Date dataFinanciamento, Date dataVencimento) {

		ValorInteiro diasCorridosDaParcela;
		
		ValorInteiro diasCorridosDaDataDaOperacao = subtrairDatas(
				dataVencimento,
				dataFinanciamento);

		if (diasCorridosDaDataDaOperacao.getValor() > 365) {
			diasCorridosDaParcela = new ValorInteiro(365);
		} else {
			diasCorridosDaParcela = new ValorInteiro(diasCorridosDaDataDaOperacao);
		}

		PeriodicidadeTaxa periodoDecorrido = PeriodicidadeTaxa.criarPeriodicidadeDiasCorridos(diasCorridosDaParcela);

		ValorTaxa iofProporcionalAoPeriodo = converterParaTaxaSimplesDoPeriodo(
				iofDiario,
				periodoDecorrido);
		
		ValorDecimal iofDiarioParcela = iofProporcionalAoPeriodo
				.multiplicaPor(valorPrincipalParcela)
				.dividePor(ValorDecimal.CEM);

		return iofDiarioParcela.valorMonetario();
	}

	public static Juros converterParaJurosCompostosMensais(Juros juros) {

		ValorTaxa taxaJurosMensal = converterParaTaxaMensal(juros.getTaxa(), juros.getPeriodicidade());

		return new Juros(taxaJurosMensal, PeriodicidadeTaxa.MENSAL);
	}

	public static Juros converterParaJurosCompostosDoPeriodo(Juros juros, PeriodicidadeTaxa periodicidade) {

		ValorDecimal trinta = new ValorDecimal(30.0);
		
		Juros jurosMensais = converterParaJurosCompostosMensais(juros);

		ValorDecimal fator1 = ValorDecimal.UM
				.soma(jurosMensais.getTaxa()
						.dividePor(ValorDecimal.CEM));
		
		ValorDecimal fator2 = periodicidade.getDias()
				.dividePor(trinta);
		
		ValorTaxa taxaPeriodo = fator1.potenciaDe(fator2)
				.subtrai(ValorDecimal.UM)
				.multiplicaPor(ValorDecimal.CEM)
				.valorTaxa();

		Juros jurosDoPeriodo = new Juros(
				taxaPeriodo,
				periodicidade);

		return jurosDoPeriodo;
	}

	public static ValorTaxa converterParaTaxaMensal(ValorTaxa taxa, PeriodicidadeTaxa periodicidade) {

		ValorDecimal trinta = new ValorDecimal(30.0);
		ValorDecimal diasDaPeriodicidade = periodicidade.getDias();

		ValorDecimal fator1 = ValorDecimal.UM
				.soma(taxa.dividePor(ValorDecimal.CEM));
		
		ValorDecimal fator2 = trinta.dividePor(diasDaPeriodicidade);
		
		ValorDecimal valorCalculado = fator1.potenciaDe(fator2)
				.subtrai(ValorDecimal.UM)
				.multiplicaPor(ValorDecimal.CEM);

		return new ValorTaxa(valorCalculado);
	}

	public static ValorTaxa converterParaTaxaSimplesDoPeriodo(ValorTaxa taxa, PeriodicidadeTaxa periodicidade) {

		ValorDecimal taxaEquivalente = taxa.multiplicaPor(periodicidade.getDias());

		return taxaEquivalente.valorTaxa();
	}

	public static ValorMonetario calcularIofAdicional(ValorMonetario valorSolicitado, ValorTaxa iofAdicional) {

		ValorDecimal valorIodAdicionalCalculado = valorSolicitado.multiplicaPor(iofAdicional)
				.dividePor(ValorDecimal.CEM);

		return valorIodAdicionalCalculado.valorMonetario();
	}

	private static Date calcularDataVencimentoParcela(Date dataPrimeiroVencimento, int numeroParcela) {
		
		Date dataVencimento;
		
		if (numeroParcela == 1) {
			dataVencimento = dataPrimeiroVencimento;
		} else {
			
			Calendar dataCalculada = Calendar.getInstance();
			dataCalculada.setTime(dataPrimeiroVencimento);
			dataCalculada.add(Calendar.MONTH, numeroParcela - 1);

			dataCalculada.set(Calendar.HOUR_OF_DAY, 0);
			dataCalculada.set(Calendar.MINUTE, 0);
			dataCalculada.set(Calendar.SECOND, 0);
			dataCalculada.set(Calendar.MILLISECOND, 0);
			
			dataVencimento = dataCalculada.getTime();
		}
		
		return dataVencimento;
	}
	
	private static Date subtrairUmMes(Date data) {
		
		Calendar dataCalculada = Calendar.getInstance();
		dataCalculada.setTime(data);
		dataCalculada.add(Calendar.DAY_OF_MONTH, -30);
		
		return dataCalculada.getTime();
	}
	
	private static Parcela criarParcela(ValorMonetario valorDaParcela, ValorMonetario saldoDevedor, int numeroParcela,
			Date dataVencimento, ValorMonetario valorJuros, ValorMonetario valorPrincipal,
			ValorMonetario valorIofDiario) {
		
		Parcela parcela = new Parcela();
		
		parcela.setNumero(new ValorInteiro(numeroParcela));
		parcela.setSaldoDevedor(saldoDevedor);
		parcela.setValor(valorDaParcela);
		parcela.setValorPrincipal(valorPrincipal);
		parcela.setValorJuros(valorJuros);
		parcela.setValorIofDiario(valorIofDiario);
		parcela.setDataVencimento(dataVencimento);
		
		return parcela;
	}

	private static ValorInteiro subtrairDatas(Date umaData, Date outraData) {
		Long milisegundosPorDia = 86400000L;
		
		Long milisegundosUmaData = umaData.getTime();
		Long milisegundosOutraData = outraData.getTime();
		
		Long diferenca = milisegundosUmaData - milisegundosOutraData;
		
		return new ValorInteiro((int) (Math.floor(diferenca / milisegundosPorDia)));
	}
	
}