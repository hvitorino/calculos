package datas;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import price.CalculadoraFinanceira;
import price.ValorInteiro;

public class OperacoesEntreDatas {
	
	@Test
	public void trintaMarco2016Menos1MesIgual29Fevereiro2016() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Date trinta03 = formatter.parse("30/03/2015");
		Date vinteNove02 = formatter.parse("28/02/2015");
		
		Date data = CalculadoraFinanceira.subtrairUmMes(trinta03);
		
		assertEquals(vinteNove02, data);
	}
}
