package price;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalculadoraFinanceira {

	public static Financiamento calcularFinanciamento(OpcoesFinanciamento opcoes) throws ParseException {
		
		Financiamento financiamento = new Financiamento(opcoes);
		
		Juros jurosMensais = converterParaJurosCompostosMensais(opcoes.getJuros());

		Date umMesAntesDoPrimeiroVencimento = subtrairUmMes(opcoes.getDataPrimeiroVencimento()); 
		
		ValorInteiro carenciaEmDias = new ValorInteiro(
				calcularDias360(
						umMesAntesDoPrimeiroVencimento,
						opcoes.getDataFinanciamento())); 
		
		ValorMonetario jurosCarencia = calcularJurosCarencia(opcoes.getValorFinanciado(),
				jurosMensais,
				carenciaEmDias);
		
		ValorMonetario valorJurosCarenciaParcelado = new ValorMonetario(jurosCarencia);
		
		ValorMonetario valorIofAdicional = calcularIofAdicional(
				opcoes.getValorFinanciado(),
				opcoes.getTaxaIofAdicional());
		
		ValorMonetario saldoDevedorInicial = opcoes.getValorFinanciado()
				.soma(jurosCarencia)
				.valorMonetario();
		
		ValorMonetario valorParcela = calcularValorDaParcela(
				saldoDevedorInicial,
				jurosMensais,
				opcoes.getQuantidadeParcelas());

		ValorMonetario saldoDevedorAtual = new ValorMonetario(saldoDevedorInicial);
		
		ValorMonetario valorTotalIof = new ValorMonetario(valorIofAdicional);
		
		for (int numeroParcela = 1; numeroParcela <= opcoes.getQuantidadeParcelas().getValor(); numeroParcela++) {
			
			Date dataVencimento = calcularDataVencimentoParcela(
					opcoes.getDataPrimeiroVencimento(),
					numeroParcela);
			
			ValorInteiro prazoParcelaEmDias = new ValorInteiro(subtrairDatas(
					dataVencimento,
					opcoes.getDataFinanciamento()));
			
			ValorMonetario valorJurosSemCarencia = jurosMensais.getTaxa()
					 .multiplicaPor(saldoDevedorAtual)
					 .dividePor(ValorDecimal.CEM)
					 .valorMonetario();
			
			ValorMonetario valorJurosParcela = calcularJurosParcela(
					valorParcela, 
					saldoDevedorAtual, 
					valorJurosCarenciaParcelado, 
					jurosMensais);
			
			ValorMonetario valorJurosCarenciaAcrescidoNaParcela = valorJurosParcela
					.subtrai(valorJurosSemCarencia)
					.valorMonetario(); 
			
			valorJurosCarenciaParcelado = valorJurosCarenciaParcelado
					.subtrai(valorJurosCarenciaAcrescidoNaParcela)
					.valorMonetario();
			
			ValorMonetario valorPrincipalParcela = valorParcela
					.subtrai(valorJurosParcela)
					.valorMonetario();
			
			ValorMonetario valorIofDiario = calcularIofDiarioParcela(
					valorPrincipalParcela,
					opcoes.getTaxaIofDiario(),
					opcoes.getDataFinanciamento(),
					dataVencimento);

			valorTotalIof = valorTotalIof
					.soma(valorIofDiario)
					.valorMonetario();
			
			saldoDevedorAtual = saldoDevedorAtual
					.soma(valorJurosSemCarencia)
					.subtrai(valorJurosParcela)
					.subtrai(valorPrincipalParcela)
					.valorMonetario();
			
			financiamento.adicionaParcela(
					criarParcela(
						valorParcela,
						saldoDevedorAtual,
						numeroParcela,
						dataVencimento,
						valorJurosParcela,
						valorPrincipalParcela,
						valorIofDiario,
						prazoParcelaEmDias));
		}
		
		financiamento.setValorEmprestimoAjustado(saldoDevedorInicial);
		financiamento.setValorPrestacao(valorParcela);
		financiamento.setValorIofAdicional(valorIofAdicional);
		financiamento.setValorIofTotal(valorTotalIof);
		
		return financiamento;
	}

	private static ValorMonetario calcularJurosParcela(ValorMonetario valorParcela, ValorMonetario saldoDevedorAtual,
			ValorMonetario jurosCarenciaNaParcela, Juros jurosMensais) {
		
		ValorMonetario valorJurosParcela = jurosMensais.getTaxa()
			.multiplicaPor(saldoDevedorAtual)
			.dividePor(ValorDecimal.CEM)
			.valorMonetario();
		
		if (valorJurosParcela.soma(jurosCarenciaNaParcela).getValor() > valorParcela.getValor()) {
			valorJurosParcela = new ValorMonetario(valorParcela);
		} else {
			valorJurosParcela = valorJurosParcela
					.soma(jurosCarenciaNaParcela)
					.valorMonetario();
		}
		
		return valorJurosParcela;
	}

	private static ValorMonetario calcularJurosCarencia(ValorMonetario valorFinanciado, Juros juros,
			ValorInteiro carenciaEmDias) {
		
		PeriodicidadeTaxa periodicidade = PeriodicidadeTaxa
				.criarPeriodicidadeDiasCorridos(carenciaEmDias);
		
		Juros jurosEquivalente = converterParaJurosCompostosDoPeriodo(
				juros,
				periodicidade);
		
		ValorDecimal valorJurosCarencia = valorFinanciado.multiplicaPor(
						jurosEquivalente.getTaxa()
							.dividePor(ValorDecimal.CEM));
		
		return valorJurosCarencia.valorMonetario();
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

	private static Date calcularDataVencimentoParcela(Date dataPrimeiroVencimento, int numeroParcela) throws ParseException {
		
		Date dataVencimento = dataPrimeiroVencimento;
		
		if (numeroParcela > 1) {
			
			int indiceParcela = 1;
			
			while(indiceParcela < numeroParcela) {
				dataVencimento = somaUmMes(dataVencimento);
				indiceParcela++;
			}
		}
		
		return dataVencimento;
	}
	
	private static Parcela criarParcela(ValorMonetario valorDaParcela, ValorMonetario saldoDevedor, int numeroParcela,
			Date dataVencimento, ValorMonetario valorJuros, ValorMonetario valorPrincipal,
			ValorMonetario valorIofDiario, ValorInteiro prazoParcelaEmDias) {
		
		Parcela parcela = new Parcela();
		
		parcela.setNumero(new ValorInteiro(numeroParcela));
		parcela.setPrazoEmDias(prazoParcelaEmDias);
		parcela.setSaldoDevedor(saldoDevedor);
		parcela.setValor(valorDaParcela);
		parcela.setValorPrincipal(valorPrincipal);
		parcela.setValorJuros(valorJuros);
		parcela.setValorIofDiario(valorIofDiario);
		parcela.setDataVencimento(dataVencimento);
		
		return parcela;
	}

	public static ValorInteiro subtrairDatas(Date umaData, Date outraData) {
		Long milisegundosPorDia = 86400000L;
		
		Long milisegundosUmaData = umaData.getTime();
		Long milisegundosOutraData = outraData.getTime();
		
		Long diferenca = milisegundosUmaData - milisegundosOutraData;
		
		return new ValorInteiro((int) (Math.floor(diferenca / milisegundosPorDia)));
	}
	
	public static int calcularUltimoDiaMes(int ano, int mes) {
		
		return Mes.get(mes).doAno(ano).getUltimoDia();
	}
	
	public static Date subtrairUmMes(Date data) throws ParseException {
		
		Calendar dataCalculada = Calendar.getInstance();
		dataCalculada.setTime(data);
		
		int dia = dataCalculada.get(Calendar.DAY_OF_MONTH);
		int diaMesAnterior;
		
		dataCalculada.add(Calendar.MONTH, -1);
		
		int mes = dataCalculada.get(Calendar.MONTH) + 1;
		int ano = dataCalculada.get(Calendar.YEAR);
		int ultimoDiaMesAnterior = Mes.get(mes).doAno(ano).getUltimoDia();
		
		if(dia > ultimoDiaMesAnterior) {
			diaMesAnterior = ultimoDiaMesAnterior;
		} else {
			diaMesAnterior = dia;
		}
		
		return construirData(diaMesAnterior, mes, ano);
	}
	
	public static Date somaUmMes(Date data) throws ParseException {
		
		Calendar dataCalculada = Calendar.getInstance();
		dataCalculada.setTime(data);
		
		int dia = dataCalculada.get(Calendar.DAY_OF_MONTH);
		int diaMesAnterior;
		
		dataCalculada.add(Calendar.MONTH, 1);
		
		int mes = dataCalculada.get(Calendar.MONTH) + 1;
		int ano = dataCalculada.get(Calendar.YEAR);
		int ultimoDiaMesAnterior = Mes.get(mes).doAno(ano).getUltimoDia();
		
		if(dia > ultimoDiaMesAnterior) {
			diaMesAnterior = ultimoDiaMesAnterior;
		} else {
			diaMesAnterior = dia;
		}
		
		return construirData(diaMesAnterior, mes, ano);
	}

	public static Date construirData(int dia, int mes, int ano) {
		
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes - 1);
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);
		
		return data.getTime();
	}
	
	public static int calcularDias360(Date ini, Date fin){   
		Calendar cini = Calendar.getInstance();
		cini.setTime(ini);
		Calendar cfin = Calendar.getInstance();
		cfin.setTime(fin);
		int anos = cfin.get(Calendar.YEAR) - cini.get(Calendar.YEAR);
		int meses = cfin.get(Calendar.MONTH) - cini.get(Calendar.MONTH);
		int dia1 = 0, dia2 = 0;
		if (cini.get(Calendar.DATE) == 31) {
			dia1 = 30;
		} else if (cini.get(Calendar.MONTH) == Calendar.FEBRUARY && cini.get(Calendar.DATE) >= 28) {
			dia1 = 30;
		} else {
			dia1 = cini.get(Calendar.DATE);
		}
		if (cfin.get(Calendar.DATE) == 31) {
			dia2 = 30;
		} else if (cfin.get(Calendar.MONTH) == Calendar.FEBRUARY && cfin.get(Calendar.DATE) >= 28) {
			dia2 = 30;
		} else {
			dia2 = cfin.get(Calendar.DATE);
		}
		int dias = dia2 - dia1;
		int diasLab = anos * 360 + meses * 30 + dias;
		return Math.abs(diasLab);  
	}
}