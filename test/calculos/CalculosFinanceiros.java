package calculos;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import price.CalculadoraFinanceira;
import price.Financiamento;
import price.Financiar;
import price.Juros;
import price.OpcoesFinanciamento;
import price.PeriodicidadeTaxa;
import price.ValorInteiro;
import price.ValorMonetario;
import price.ValorTaxa;

public class CalculosFinanceiros {

	@Test
	public void calculaTaxaMensalAPartirDaAnualCorrida() {
		
		ValorTaxa taxaAnual = new ValorTaxa(18.20);
		ValorTaxa taxaMensal = CalculadoraFinanceira.converterParaTaxaMensal(taxaAnual, PeriodicidadeTaxa.ANUAL_CORRIDO);

		assertEquals(new Double(1.4032), taxaMensal.getValor());
	}

	@Test
	public void calculaJurosMensalAPartirDoAnualCorrido() {
		
		Juros jurosAnuais = new Juros(new ValorTaxa(18.20), PeriodicidadeTaxa.ANUAL_CORRIDO);
		Juros jurosMensais = CalculadoraFinanceira.converterParaJurosCompostosMensais(jurosAnuais);

		assertEquals(new Double(1.4032), jurosMensais.getTaxa().getValor());
		assertEquals(PeriodicidadeTaxa.MENSAL, jurosMensais.getPeriodicidade());
	}

	@Test
	public void calculaIofDiario() throws ParseException {
		
		ValorMonetario valorPrincipalParcela = new ValorMonetario(435.98);
		ValorTaxa iofDiario = new ValorTaxa(0.0082);
		Date dataFinanciamento = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2016");
		Date dataPrimeiroVencimento = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2016");

		ValorMonetario valorIofDiario = CalculadoraFinanceira.calcularIofDiarioParcela(
				valorPrincipalParcela, 
				iofDiario, 
				dataFinanciamento, 
				dataPrimeiroVencimento);

		assertEquals(new Double(1.11), valorIofDiario.getValor());
	}

	@Test
	public void calculaIofAdicional() {
		
		ValorMonetario valorSolicitado = new ValorMonetario(1800.00);
		ValorTaxa iofAdicional = new ValorTaxa(0.38);

		ValorMonetario valorIofAdicional = CalculadoraFinanceira.calcularIofAdicional(valorSolicitado, iofAdicional);

		assertEquals(new Double(6.84), valorIofAdicional.getValor());
	}

	@Test
	public void calculaSaldoBase() {

		ValorMonetario valorSolicitado = new ValorMonetario(1800.00);
		ValorInteiro diasDecorridosDaContratacao = new ValorInteiro(22);
		Juros juros = new Juros(new ValorTaxa(14.12), PeriodicidadeTaxa.ANUAL_CORRIDO);

		ValorMonetario valorSaldoBase = CalculadoraFinanceira.calcularSaldoDevedorAposCarencia(valorSolicitado, juros,
				diasDecorridosDaContratacao);

		assertEquals(new Double(1814.59), valorSaldoBase.getValor());
	}

	@Test
	public void calculaValorDaParcela() {

		ValorMonetario saldoBase = new ValorMonetario(12100.0);
		Juros juros = new Juros(new ValorTaxa(10.00), PeriodicidadeTaxa.MENSAL);
		ValorInteiro quantidadeParcelas = new ValorInteiro(5);

		ValorMonetario valorDaParcela = CalculadoraFinanceira.calcularValorDaParcela(saldoBase, juros, quantidadeParcelas);

		assertEquals(new Double(3191.95), valorDaParcela.getValor());
	}
	
	@Test
	public void calculaValorDaParcelaCenario1() {

		ValorMonetario saldoBase = new ValorMonetario(12100.0);
		Juros juros = new Juros(new ValorTaxa(10.00), PeriodicidadeTaxa.MENSAL);
		ValorInteiro quantidadeParcelas = new ValorInteiro(5);

		ValorMonetario valorDaParcela = CalculadoraFinanceira.calcularValorDaParcela(saldoBase, juros, quantidadeParcelas);

		assertEquals(new Double(3191.95), valorDaParcela.getValor());
	}

}
