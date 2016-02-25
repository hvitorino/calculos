package calculos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import price.CalculadoraFinanciamento;
import price.CalculadoraPrice;
import price.Decimal;
import price.Juros;
import price.PeriodicidadeTaxa;
import price.ValorInteiro;
import price.ValorMonetario;
import price.ValorTaxa;

public class FinanciamentoPelaTabelaPrice {

	@Test
	public void calculaTaxaMensalAPartirDaAnualCorrida() {
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		ValorTaxa taxaAnual = new ValorTaxa(18.20);
		ValorTaxa taxaMensal = calculadora.converterParaTaxaMensal(taxaAnual, PeriodicidadeTaxa.ANUAL_CORRIDO);
		
		assertEquals(new Double(1.4032), taxaMensal.getValor());
	}
	
	@Test
	public void calculaJurosMensalAPartirDoAnualCorrido() {
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		Juros jurosAnuais = new Juros(new ValorTaxa(18.20), PeriodicidadeTaxa.ANUAL_CORRIDO);
		Juros jurosMensais = calculadora.converterParaJurosCompostosMensais(jurosAnuais);
		
		assertEquals(new Double(1.4032), jurosMensais.getTaxa().getValor());
		assertEquals(PeriodicidadeTaxa.MENSAL, jurosMensais.getPeriodicidade());
	}
	
	@Test
	public void calculaIofDiario() {
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		ValorMonetario valorPrincipalParcela = new ValorMonetario(435.98);
		ValorTaxa iofDiario = new ValorTaxa(0.0082);
		ValorInteiro diasCorridosDaDataDaOperacao = new ValorInteiro(31);
		
		ValorMonetario valorIofDiario = calculadora.calcularIofDiarioParcela(
				valorPrincipalParcela, iofDiario, diasCorridosDaDataDaOperacao);
		
		assertEquals(new Double(1.11), valorIofDiario.getValor());
	}
	
	@Test
	public void calculaIofAdicional() {
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		ValorMonetario valorSolicitado = new ValorMonetario(1800.00);
		ValorTaxa iofAdicional = new ValorTaxa(0.38);
		
		ValorMonetario valorIofAdicional = calculadora.calcularIofAdicional(valorSolicitado, iofAdicional);
		
		assertEquals(new Double(6.84), valorIofAdicional.getValor());
	}
	
	@Test
	public void calculaSaldoBase() {
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		ValorMonetario valorSolicitado = new ValorMonetario(1800.00);
		ValorInteiro diasDecorridosDaContratacao = new ValorInteiro(31);
		Juros juros = new Juros(new ValorTaxa(18.20), PeriodicidadeTaxa.ANUAL_CORRIDO);
		
		ValorMonetario valorSaldoBase = calculadora.calcularSaldoBase(valorSolicitado, juros, diasDecorridosDaContratacao);
		
		assertEquals(new Double(1826.11), valorSaldoBase.getValor());
	}
	
	@Test
	public void calculaValorDaParcela() {
		
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
		
		ValorMonetario saldoBase = new ValorMonetario(1826.11);
		Juros juros = new Juros(new ValorTaxa(18.20), PeriodicidadeTaxa.ANUAL_CORRIDO);
		ValorInteiro quantidadeParcelas = new ValorInteiro(8);
		
		ValorMonetario valorDaParcela = calculadora.calcularValorDaParcela(saldoBase, juros, quantidadeParcelas);

		assertEquals(new Double(444.18), valorDaParcela.getValor());
	}
	
//	@Test
//	public void calculaValorDaParcelaCabeca() throws ParseException {
//		
//		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
//		
//		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();
//
//		List<PlanilhaOperacaoTO> planilhas = calculadora.simulaOperacaoCredito(
//				formatador.parse("2016-02-23"),
//				formatador.parse("2016-04-15"),
//				new ValorTaxa(18.20),
//				new ValorInteiro(12),
//				PeriodicidadeEnum.MES,
//				new ValorMonetario(20000.00),
//				new ValorTaxa(0.0),
//				new ValorTaxa(0.0),
//				new ValorTaxa(0.0));
//		
//		PlanilhaOperacaoTO planilhaSelecionada = planilhas.get(11);
//		
//		assertTrue(planilhaSelecionada.getValorParcela().equals(2970.56));
//	}

}
