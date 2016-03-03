package calculos.financiamentos;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import price.CalculadoraFinanceira;
import price.Financiamento;
import price.Financiar;
import price.OpcoesFinanciamento;
import price.Parcela;
import price.PeriodicidadeTaxa;
import price.ValorMonetario;

public class FinanciamentoDe21600Em55Vezes {

	private static Financiamento financiamento;

	@BeforeClass
	public static void cenario() throws ParseException {

		OpcoesFinanciamento opcoes = Financiar.valor(21600.00).divididoEmParcelas(55)
				.comJuros(1.62, PeriodicidadeTaxa.MENSAL).comIofDiario(0.0).comIofAdicional(0.0)
				.contratadoEm("12/01/2016").vencendoAPrimeiraParcelaEm("19/02/2016").pronto();

		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);

		PlanilhaExcel planilha = new PlanilhaExcel("21600Em55Vezes", financiamento);
		planilha.salvar();
	}

	@Test
	public void valorParcela598ponto54() {
		assertEquals(new Double(598.54), financiamento.getValorPrestacao().getValor());
	}

	@Test
	public void valorEmprestimoAjustado21681ponto15() {
		assertEquals(new Double(21681.15), financiamento.getValorEmprestimoAjustado().getValor());
	}

	@Test
	public void valorTotalJuros11319ponto92() {
		assertEquals(new Double(11319.92), financiamento.getValorTotalJuros().getValor());
	}

	@Test
	public void valorTotalEmprestimoAjustado32919ponto92() {
		assertEquals(new Double(32919.92), financiamento.getValorTotalEmprestimoAjustado().getValor());
	}

	@Test
	public void valorPrincipalTotal21600() {

		ValorMonetario principalTotal = new ValorMonetario(0.0);

		for (Parcela parcela : financiamento.getParcelas()) {
			principalTotal = principalTotal.soma(parcela.getValorPrincipal()).valorMonetario();
		}

		assertEquals(new Double(21600), principalTotal.getValor());
	}

	@Test
	public void valorPrincipalParcela1_166ponto16() {
		Parcela parcela = financiamento.getParcelas().get(0);

		assertEquals(new Double(166.16), parcela.getValorPrincipal().getValor());
	}

	@Test
	public void valorJurosParcela1_432ponto38() {
		Parcela parcela = financiamento.getParcelas().get(0);

		assertEquals(new Double(432.38), parcela.getValorJuros().getValor());
	}

	@Test
	public void valorPrincipalParcela2_251ponto31() {
		Parcela parcela = financiamento.getParcelas().get(1);

		assertEquals(new Double(251.31), parcela.getValorPrincipal().getValor());
	}

	@Test
	public void valorJurosParcela2_347ponto23() {
		Parcela parcela = financiamento.getParcelas().get(1);

		assertEquals(new Double(347.23), parcela.getValorJuros().getValor());
	}

	@Test
	public void valorPrincipalParcela10_285ponto79() {
		Parcela parcela = financiamento.getParcelas().get(9);

		assertEquals(new Double(285.79), parcela.getValorPrincipal().getValor());
	}
	
}
