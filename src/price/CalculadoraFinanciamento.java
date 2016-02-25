package price;

public class CalculadoraFinanciamento {

		public ValorMonetario calcularValorDaParcela(ValorMonetario saldoBase, Juros juros, ValorInteiro numeroParcela) {
		
		Decimal percentualJuros = juros.getTaxa().dividePor(Decimal.CEM);
		
		Decimal fator1 = Decimal.UM.soma(percentualJuros).potenciaDe(numeroParcela);
		Decimal fator2 = fator1.subtrai(Decimal.UM); 
		Decimal fator3 = fator1.dividePor(fator2);
		Decimal fator4 = percentualJuros.multiplicaPor(fator3).multiplicaPor(saldoBase);
		
		return new ValorMonetario(fator4);
	}
	
	public ValorMonetario calcularSaldoBase(ValorMonetario valorTotalOperacao, Juros juros, ValorInteiro diasDecorridosDaContratacao) {
		
		PeriodicidadeTaxa periodicidade = PeriodicidadeTaxa.criarPeriodicidadeDiasCorridos(diasDecorridosDaContratacao);
		Juros jurosEquivalente = converterParaJurosCompostosDoPeriodo(juros, periodicidade);
		Decimal valorJuros = valorTotalOperacao.multiplicaPor(jurosEquivalente.getTaxa().dividePor(Decimal.CEM));
		Decimal valorSaldoBaseCalculado = valorTotalOperacao.soma(valorJuros);
		
		return new ValorMonetario(valorSaldoBaseCalculado);
	}
	
	public ValorMonetario calcularIofDiarioParcela(ValorMonetario valorPrincipalParcela, ValorTaxa iofDiario, ValorInteiro diasCorridosDaDataDaOperacao) {

		ValorInteiro diasCorridosParaCalculo;
		
		if (diasCorridosDaDataDaOperacao.getValor() > 365) {
			diasCorridosParaCalculo = new ValorInteiro(365);
		} else {
			diasCorridosParaCalculo = new ValorInteiro(diasCorridosDaDataDaOperacao);
		}
		
		PeriodicidadeTaxa periodoDecorrido = PeriodicidadeTaxa
				.criarPeriodicidadeDiasCorridos(diasCorridosParaCalculo);
		
		ValorTaxa iofProporcionalAoPeriodo = converterParaTaxaSimplesDoPeriodo(iofDiario, periodoDecorrido); 
		Decimal iofDiarioParcela = iofProporcionalAoPeriodo.multiplicaPor(valorPrincipalParcela).dividePor(Decimal.CEM);
				
		return new ValorMonetario(iofDiarioParcela);
	}
	
	public Juros converterParaJurosCompostosMensais(Juros juros) {

		ValorTaxa taxaJurosMensal = converterParaTaxaMensal(juros.getTaxa(), juros.getPeriodicidade());

		return new Juros(taxaJurosMensal, PeriodicidadeTaxa.MENSAL);
	}

	public Juros converterParaJurosCompostosDoPeriodo(Juros juros, PeriodicidadeTaxa periodicidade) {

		Decimal trinta = new Decimal(30.0);
		Juros jurosMensais = converterParaJurosCompostosMensais(juros);
		
		Decimal fator1 = Decimal.UM.soma(jurosMensais.getTaxa().dividePor(Decimal.CEM));
		Decimal fator2 = periodicidade.getDias().dividePor(trinta);
		Decimal valorCalculado = fator1.potenciaDe(fator2).subtrai(Decimal.UM).multiplicaPor(Decimal.CEM);

		ValorTaxa taxaPeriodo = new ValorTaxa(valorCalculado);
		Juros jurosDoPeriodo = new Juros(taxaPeriodo, null);
		
		return jurosDoPeriodo;
	}
	
	public ValorTaxa converterParaTaxaMensal(ValorTaxa taxa, PeriodicidadeTaxa periodicidade) {

		Decimal trinta = new Decimal(30.0);
		Decimal diasDaPeriodicidade = periodicidade.getDias();

		Decimal fator1 = Decimal.UM.soma(taxa.dividePor(Decimal.CEM));
		Decimal fator2 = trinta.dividePor(diasDaPeriodicidade);
		Decimal valorCalculado = fator1.potenciaDe(fator2).subtrai(Decimal.UM).multiplicaPor(Decimal.CEM);

		return new ValorTaxa(valorCalculado);
	}
	
	public ValorTaxa converterParaTaxaSimplesDoPeriodo(ValorTaxa taxa, PeriodicidadeTaxa periodicidade) {

		Decimal taxaEquivalente = taxa.multiplicaPor(periodicidade.getDias());
		
		return new ValorTaxa(taxaEquivalente);
	}

	public ValorMonetario calcularIofAdicional(ValorMonetario valorSolicitado, ValorTaxa iofAdicional) {
		
		Decimal valorIodAdicionalCalculado = valorSolicitado.multiplicaPor(iofAdicional).dividePor(Decimal.CEM);
		
		return new ValorMonetario(valorIodAdicionalCalculado);
	}
}