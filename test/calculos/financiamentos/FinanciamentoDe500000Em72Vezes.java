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
			.vencendoAPrimeiraParcelaEm("27/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void possui72Parcelas() {
		
		assertEquals(72, financiamento.getParcelas().size());
	}
	
	@Test
	public void parcelaDe11574ponto95() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(11574.95), parcela.getValor().getValor());
	}
	
	@Test
	public void valorEmprestimoAjustado507500() {
		
		assertEquals(new Double(507500.00), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void jurosTotalDe333369ponto69() {
		assertEquals(new Double(333369.69), financiamento.getJurosTotal().getValor());
	}
	
	@Test
	public void somaDoPrincipalDasParcelasIgualA500000() {
		
		ValorMonetario principalTotal = new ValorMonetario(0.0);
		
		for(Parcela parcela : financiamento.getParcelas()) {
			principalTotal = principalTotal
					.soma(parcela.getValorPrincipal())
					.valorMonetario();
		}
		
		assertEquals(new Double(500000), principalTotal.getValor());
	}
}
