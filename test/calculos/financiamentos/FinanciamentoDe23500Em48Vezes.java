package calculos.financiamentos;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import price.CalculadoraFinanceira;
import price.Financiamento;
import price.Financiar;
import price.OpcoesFinanciamento;
import price.Parcela;
import price.PeriodicidadeTaxa;

public class FinanciamentoDe23500Em48Vezes {
	
	private Financiamento financiamento;

	@Before
	public void cenario() throws ParseException {

		OpcoesFinanciamento opcoes = Financiar.valor(23500.00)
			.divididoEmParcelas(48)
			.comJuros(1.5, PeriodicidadeTaxa.MENSAL)
			.comIofDiario(0.0)
			.comIofAdicional(0.0)
			.contratadoEm("27/02/2016")
			.vencendoAPrimeiraParcelaEm("15/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void possui12Parcelas() {
		
		assertEquals(48, financiamento.getParcelas().size());
	}
	
	@Test
	public void parcelaDe696ponto51() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(696.51), parcela.getValor().getValor());
	}
	
	@Test
	public void saldoDevedorDaPrimeiraParcela23710ponto87() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(23710.87), parcela.getSaldoDevedor().getValor());
	}
}
