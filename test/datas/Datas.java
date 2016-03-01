package datas;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import price.CalculadoraFinanceira;
import price.ValorInteiro;

public class Datas {
	
	@Test
	public void fevereiroDe2016Tem29() {
		
		int ultimoDia = CalculadoraFinanceira.calcularUltimoDiaMes(2016, 2);
		
		assertEquals(29, ultimoDia);
	}
	
	@Test
	public void fevereiroDe2015NAOTem29() {
		
		int ultimoDia = CalculadoraFinanceira.calcularUltimoDiaMes(2015, 2);
		
		assertEquals(28, ultimoDia);
	}
	
	@Test
	public void fevereiroDe2017NAOTem29() {
		
		int ultimoDia = CalculadoraFinanceira.calcularUltimoDiaMes(2017, 2);
		
		assertEquals(28, ultimoDia);
	}
	
	@Test
	public void _15_03_2015_Menos_14_03_2015_Igual_1() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _14_03_2015 = formatter.parse("14/03/2015");
		
		ValorInteiro dias = CalculadoraFinanceira.subtrairDatas(_15_03_2015, _14_03_2015);
		
		assertEquals(new Double(1.0), dias.getValor());
	}
	
	@Test
	public void _15_03_2015_Menos_13_03_2015_Igual_2() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _13_03_2015 = formatter.parse("13/03/2015");
		
		ValorInteiro dias = CalculadoraFinanceira.subtrairDatas(_15_03_2015, _13_03_2015);
		
		assertEquals(new Double(2.0), dias.getValor());
	}
	
	@Test
	public void _15_03_2015_Menos_12_03_2015_Igual_3() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _12_03_2015 = formatter.parse("12/03/2015");
		
		ValorInteiro dias = CalculadoraFinanceira.subtrairDatas(_15_03_2015, _12_03_2015);
		
		assertEquals(new Double(3.0), dias.getValor());
	}
	
	@Test
	public void _15_03_2015_Menos_11_03_2015_Igual_4() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _11_03_2015 = formatter.parse("11/03/2015");
		
		ValorInteiro dias = CalculadoraFinanceira.subtrairDatas(_15_03_2015, _11_03_2015);
		
		assertEquals(new Double(4.0), dias.getValor());
	}
	
	@Test
	public void _15_03_2015_Menos_10_03_2015_Igual_5() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _10_03_2015 = formatter.parse("10/03/2015");
		
		ValorInteiro dias = CalculadoraFinanceira.subtrairDatas(_15_03_2015, _10_03_2015);
		
		assertEquals(new Double(5.0), dias.getValor());
	}
	
	@Test
	public void _15_03_2015_MenosUmMes_Igual_15_02_2015() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _15_03_2015 = formatter.parse("15/03/2015");
		Date _15_02_2015 = formatter.parse("15/02/2015");
		
		Date data = CalculadoraFinanceira.subtrairUmMes(_15_03_2015);
		
		assertEquals(_15_02_2015, data);
	}
	
	@Test
	public void _30_03_2015_MenosUmMes_Igual_28_02_2015() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _30_03_2015 = formatter.parse("30/03/2015");
		Date _28_02_2015 = formatter.parse("28/02/2015");
		
		Date data = CalculadoraFinanceira.subtrairUmMes(_30_03_2015);
		
		assertEquals(_28_02_2015, data);
	}
	
	@Test
	public void _31_05_2015_MenosUmMes_Igual_30_04_2015() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date _31_05_2015 = formatter.parse("31/05/2015");
		Date _30_04_2015 = formatter.parse("30/04/2015");
		
		Date data = CalculadoraFinanceira.subtrairUmMes(_31_05_2015);
		
		assertEquals(_30_04_2015, data);
	}
	
	@Test
	public void _31_03_2016_menosUmMes_Igual_29_02_2016() throws ParseException {
		
		Date _31_03_2016 = new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2016");
		Date _29_02_2016 = new SimpleDateFormat("dd/MM/yyyy").parse("29/02/2016");
		Date resultado = CalculadoraFinanceira.subtrairUmMes(_31_03_2016);
		
		assertEquals(resultado, _29_02_2016);
	}
	
	@Test
	public void _31_05_2016_menosUmMes_Igual_30_04_2016() throws ParseException {
		
		Date _31_05_2016 = new SimpleDateFormat("dd/MM/yyyy").parse("31/05/2016");
		Date _30_04_2016 = new SimpleDateFormat("dd/MM/yyyy").parse("30/04/2016");
		Date resultado = CalculadoraFinanceira.subtrairUmMes(_31_05_2016);
		
		assertEquals(resultado, _30_04_2016);
	}
}
