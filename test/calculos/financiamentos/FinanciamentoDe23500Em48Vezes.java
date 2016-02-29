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
import price.ValorMonetario;

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
			.vencendoAPrimeiraParcelaEm("27/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void valorParcela700ponto67() {
		assertEquals(new Double(700.67), financiamento.getValorPrestacao().getValor());
	}
	
	@Test
	public void valorEmprestimoAjustado23852ponto50() {
		assertEquals(new Double(23852.50), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorTotalJuros10132ponto02() {
		assertEquals(new Double(10132.02), financiamento.getValorTotalJuros().getValor());
	}
	
	@Test
	public void valorTotalEmprestimoAjustado33632ponto02() {
		assertEquals(new Double(33632.02), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorPrincipalTotal23500() {
		
		ValorMonetario principalTotal = new ValorMonetario(0.0);
		
		for(Parcela parcela : financiamento.getParcelas()) {
			principalTotal = principalTotal
					.soma(parcela.getValorPrincipal())
					.valorMonetario();
		}
		
		assertEquals(new Double(500000), principalTotal.getValor());
	}
}
