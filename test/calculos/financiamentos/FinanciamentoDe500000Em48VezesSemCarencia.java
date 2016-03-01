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

public class FinanciamentoDe500000Em48VezesSemCarencia {
	
	private Financiamento financiamento;

	@Before
	public void cenario() throws ParseException {

		OpcoesFinanciamento opcoes = Financiar.valor(500000.00)
			.divididoEmParcelas(48)
			.comJuros(1.5, PeriodicidadeTaxa.MENSAL)
			.comIofDiario(0.0)
			.comIofAdicional(0.0)
			.contratadoEm("27/03/2016")
			.vencendoAPrimeiraParcelaEm("27/4/2016")
			.pronto();
		
		financiamento = CalculadoraFinanceira.calcularFinanciamento(opcoes);
		
		PlanilhaExcel planilha = new PlanilhaExcel("500000Em48VezesSemCarencia", financiamento);
		planilha.salvar();
	}
	
	@Test
	public void valorParcela14687ponto50() {
		assertEquals(new Double(14687.50), financiamento.getValorPrestacao().getValor());
	}
	
	@Test
	public void valorEmprestimoAjustado500000() {
		assertEquals(new Double(500000.00), financiamento.getValorEmprestimoAjustado().getValor());
	}
	
	@Test
	public void valorTotalJuros204999ponto99() {
		assertEquals(new Double(204999.99), financiamento.getValorTotalJuros().getValor());
	}
	
	@Test
	public void valorTotalEmprestimoAjustado704999ponto99() {
		assertEquals(new Double(704999.99), financiamento.getValorTotalEmprestimoAjustado().getValor());
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
}
