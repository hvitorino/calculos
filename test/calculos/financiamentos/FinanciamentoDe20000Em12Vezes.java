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

public class FinanciamentoDe20000Em12Vezes {
	
	private Financiamento financiamento;

	@Before
	public void cenario() throws ParseException {

		OpcoesFinanciamento opcoes = Financiar.valor(20000.00)
			.divididoEmParcelas(12)
			.comJuros(1.5, PeriodicidadeTaxa.MENSAL)
			.comIofDiario(0.0)
			.comIofAdicional(0.0)
			.contratadoEm("27/02/2016")
			.vencendoAPrimeiraParcelaEm("27/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void valorParcela1861ponto10() {
		assertEquals(new Double(1861.10), financiamento.getValorPrestacao().getValor());
	}
	
	@Test
	public void valorEmprestimoAjustado20300() {
		assertEquals(new Double(20300.00), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorTotalJuros2333ponto25() {
		assertEquals(new Double(2333.25), financiamento.getValorTotalJuros().getValor());
	}
	
	@Test
	public void valorTotalEmprestimoAjustado22333ponto25() {
		assertEquals(new Double(22333.25), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorPrincipalTotal20000() {
		
		ValorMonetario principalTotal = new ValorMonetario(0.0);
		
		for(Parcela parcela : financiamento.getParcelas()) {
			principalTotal = principalTotal
					.soma(parcela.getValorPrincipal())
					.valorMonetario();
		}
		
		assertEquals(new Double(500000), principalTotal.getValor());
	}
}
