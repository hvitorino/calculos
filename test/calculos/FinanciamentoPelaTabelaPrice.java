package calculos;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import price.CalculadoraFinanciamento;
import price.CalculadoraPrice;
import price.Decimal;
import price.PeriodicidadeEnum;
import price.PlanilhaOperacaoTO;
import price.ValorInteiro;
import price.ValorMonetario;
import price.ValorTaxa;

public class FinanciamentoPelaTabelaPrice {

	@Test
	public void calculaValorDaParcela() {
		
		ValorMonetario valorSolicitadoPeloCliente = new ValorMonetario(20000.00);
		ValorTaxa taxaJurosMensais = new ValorTaxa(4.00);
		ValorInteiro quantidadeDeParcelas = new ValorInteiro(8);

		CalculadoraPrice calculadora = new CalculadoraPrice();
				
		Decimal valorDaParcela = calculadora.calcularValorDaParcela(
				valorSolicitadoPeloCliente, 
				taxaJurosMensais, 
				quantidadeDeParcelas);

		assertTrue(valorDaParcela.getValor().equals(2970.56));
	}
	
	@Test
	public void calculaValorDaParcelaCabeca() throws ParseException {
		
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		
		CalculadoraFinanciamento calculadora = new CalculadoraFinanciamento();

		List<PlanilhaOperacaoTO> planilhas = calculadora.simulaOperacaoCredito(
				formatador.parse("2016-02-23"),
				formatador.parse("2016-04-15"),
				new ValorTaxa(18.20),
				new ValorInteiro(12),
				PeriodicidadeEnum.MES,
				new ValorMonetario(20000.00),
				new ValorTaxa(0.0),
				new ValorTaxa(0.0),
				new ValorTaxa(0.0));
		
		PlanilhaOperacaoTO planilhaSelecionada = planilhas.get(11);
		
		assertTrue(planilhaSelecionada.getValorParcela().equals(2970.56));
	}

}
