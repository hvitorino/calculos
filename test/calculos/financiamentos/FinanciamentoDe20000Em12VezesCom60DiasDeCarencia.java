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

public class FinanciamentoDe20000Em12VezesCom60DiasDeCarencia {
	
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
	public void possui12Parcelas() {
		
		assertEquals(12, financiamento.getParcelas().size());
	}
	
	@Test
	public void parcelaDe1861ponto10() {
		
		Parcela parcela = financiamento.getParcelas().get(0);
		
		assertEquals(new Double(1861.10), parcela.getValor().getValor());
	}
}
