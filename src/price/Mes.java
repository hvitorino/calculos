package price;

import java.util.Calendar;
import java.util.HashMap;

public class Mes {

	public static final Mes JANEIRO = new Mes(1, 31);
	public static final Mes FEVEREIRO = new Mes(2);
	public static final Mes MARCO = new Mes(3, 31);
	public static final Mes ABRIL = new Mes(4, 30);
	public static final Mes MAIO = new Mes(5, 31);
	public static final Mes JUNHO = new Mes(6, 30);
	public static final Mes JULHO = new Mes(7, 31);
	public static final Mes AGOSTO = new Mes(8, 31);
	public static final Mes SETEMBRO = new Mes(9, 30);
	public static final Mes OUTUBRO = new Mes(10, 31);
	public static final Mes NOVEMBRO = new Mes(11, 30);
	public static final Mes DEZEMBRO = new Mes(12, 31);
	
	private static HashMap<Integer, Mes> meses;
	
	public static final Mes get(int mes) {
		
		if(meses == null) {
			meses = new HashMap<>();
			meses.put(JANEIRO.getMes(), JANEIRO);
			meses.put(FEVEREIRO.getMes(), FEVEREIRO);
			meses.put(MARCO.getMes(), MARCO);
			meses.put(ABRIL.getMes(), ABRIL);
			meses.put(MAIO.getMes(), MAIO);
			meses.put(JUNHO.getMes(), JUNHO);
			meses.put(JULHO.getMes(), JULHO);
			meses.put(AGOSTO.getMes(), AGOSTO);
			meses.put(SETEMBRO.getMes(), SETEMBRO);
			meses.put(OUTUBRO.getMes(), OUTUBRO);
			meses.put(NOVEMBRO.getMes(), NOVEMBRO);
			meses.put(DEZEMBRO.getMes(), DEZEMBRO);
		}
		
		return meses.get(mes);
	}
	
	private int mes;
	private int ultimoDia;
	private int ano;

	private Mes(int mes) {
		this.mes = mes;
		this.ano = Calendar.getInstance().get(Calendar.YEAR);
		this.ultimoDia = this.doAno(this.ano).getUltimoDia();
	}

	private Mes(int mes, int ultimoDia) {
		this(mes);
		this.ultimoDia = ultimoDia;
	}
	
	public Integer getMes() {
		return this.mes;
	}

	public Mes doAno(int ano) {
		
		if(this.equals(FEVEREIRO)) {
			if(isAnoBisexto(ano)) {
				this.ultimoDia = 29;
			} else {
				this.ultimoDia = 28;
			}
		}
			
		return this;
	}
	
	public int getUltimoDia() {
		return this.ultimoDia;
	}
	
	public boolean possuiData(int diaDoMes) {
		return this.getUltimoDia() >= diaDoMes;
	}
	
	private static boolean isAnoBisexto(int ano) {
		return Math.floorMod(ano, 4) == 0;
	}
}