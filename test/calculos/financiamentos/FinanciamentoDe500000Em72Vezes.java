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
			.comIofDiario(0.0082)
			.comIofAdicional(0.38)
			.contratadoEm("27/02/2016")
			.vencendoAPrimeiraParcelaEm("27/04/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
	}
	
	@Test
	public void valorParcela11574ponto95() {
		assertEquals(new Double(11574.95), financiamento.getValorPrestacao().getValor());
	}
	
	@Test
	public void valorEmprestimoAjustado507500() {
		assertEquals(new Double(507500.00), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorTotalEmprestimoAjustado833396ponto69() {
		assertEquals(new Double(833396.69), financiamento.getValorTotalEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorTotalJuros333396ponto69() {
		assertEquals(new Double(333396.69), financiamento.getValorTotalJuros().getValor());
	}
	
	@Test
	public void valorPrincipalTotal500000() {
		
		ValorMonetario principalTotal = new ValorMonetario(0.0);
		
		for(Parcela parcela : financiamento.getParcelas()) {
			principalTotal = principalTotal
					.soma(parcela.getValorPrincipal())
					.valorMonetario();
		}
		
		assertEquals(new Double(500000), principalTotal.getValor());
	}
	
	@Test
	public void prazoPrimeiraParcela60Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(0);

		assertEquals(new Double(60), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void prazoSegundaParcela90Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(1);

		assertEquals(new Double(90), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void prazoTerceiraParcela121Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(2);

		assertEquals(new Double(121), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void prazoQuartaParcela151Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(3);

		assertEquals(new Double(151), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void prazoVigesimaNonaParcela912Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(28);

		assertEquals(new Double(912), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void prazoUltimaParcela2220Dias() {
		
		Parcela primeiraParcela = financiamento.getParcelas().get(71);

		assertEquals(new Double(2220), primeiraParcela.getPrazoEmDias().getValor());
	}
	
	@Test
	public void valorPrincipalParcela2() {
		
		Parcela segundaParcela = financiamento.getParcelas().get(1);

		assertEquals(new Double(484.34), segundaParcela.getValorPrincipal().getValor());
	}
	
	@Test
	public void valorIofTotal16470ponto88() {
		assertEquals(new Double(16470.88), financiamento.getValorIofTotal().getValor());
	}
	
}
