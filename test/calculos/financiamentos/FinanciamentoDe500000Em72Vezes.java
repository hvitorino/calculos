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

public class FinanciamentoDe500000Em72Vezes {
	
	private Financiamento financiamento;

	@Before
	public void cenario() throws ParseException {

		OpcoesFinanciamento opcoes = Financiar.valor(500000.00)
			.divididoEmParcelas(72)
			.comJuros(1.5, PeriodicidadeTaxa.MENSAL)
			.comIofDiario(0.0)
			.comIofAdicional(0.0)
			.contratadoEm("27/02/2016")
			.vencendoAPrimeiraParcelaEm("15/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void possui72Parcelas() {
		
		assertEquals(72, financiamento.getParcelas().size());
	}
	
	@Test
	public void parcelaDe11506ponto22() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(11506.22), parcela.getValor().getValor());
	}
	
	@Test
	public void saldoDevedorDaPrimeiraParcela504486ponto59() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(504486.59), parcela.getSaldoDevedor().getValor());
	}
	
	@Test
	public void jurosTotalDe328448ponto19() {
		assertEquals(new Double(328448.19), financiamento.getJurosTotal().getValor());
	}
}
