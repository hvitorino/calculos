package price;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CalendarioUtil extends GregorianCalendar {

	private static final long serialVersionUID = -1646960319399923927L;

	/**
	 * Campo LOCALE_DEFAULT, do tipo Locale.
	 * 
	 * @since JDK1.4.2_06
	 * 
	 * @author Ramon Oliveira, em 24/12/2005 - versão inicial.
	 */
	public static final Locale LOCALE_DEFAULT = new Locale("pt", "BR");
	
	/**
	 * 
	 */
	private static DatatypeFactory df = null;

	/**
	 * Campo DIA_TIME, do tipo long.
	 * 
	 * @since JDK1.4.2_06
	 * 
	 * @author Ramon Oliveira, em 24/12/2005 - versão inicial.
	 */
	public static final long DIA_TIME = 86400000;
	
	/**
	 * 
	 */
	static {
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Error while trying to obtain a new instance of DatatypeFactory",
					e);
		}
	}
	/**
	 *  Converts a java.util.Date into an instance of XMLGregorianCalendar
	 * @param date
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) {
		if (date == null) {
			return null;
		} else {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return df.newXMLGregorianCalendar(gc);
		}
	}

	/**
	 *  Converts an XMLGregorianCalendar to an instance of java.util.Date
	 * @param xmlGC
	 * @return Date
	 */
	public static Date getDate(XMLGregorianCalendar xmlGC) {
		if (xmlGC == null) {
			return null;
		} else {
			return xmlGC.toGregorianCalendar().getTime();
		}
	}

	/**
	 * Método calculaDiasCorridos.
	 * 
	 * @param data1
	 * @param data2
	 * @return
	 * 
	 * @since JDK1.4.2_06
	 * 
	 * @author Ramon Oliveira, em 24/12/2005 - versão inicial.
	 */
	public static int calculaDiasCorridos(long data1, long data2) {
		return (int) Math.floor(Math.abs(data2 - data1) / DIA_TIME);
	}
	
	public static int calculaDiasComerciaisCorridos(java.util.Date data1, java.util.Date data2) {
        return calculaDiasComerciaisCorridos(data1.getTime(), data2.getTime());
    }
	
	public static GregorianCalendar newCalendar(long data) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(data);
        return gc;
    }
	
	public static int calculaDiasComerciaisCorridos(long data1, long data2) {
        if (data2 > data1) {
            GregorianCalendar gCalendar1 = newCalendar(data1);
            GregorianCalendar gCalendar2 = newCalendar(data2);

            return (gCalendar2.get(YEAR) - gCalendar1.get(YEAR)) * 360 + (gCalendar2.get(MONTH) - gCalendar1.get(MONTH)) * 30 + (gCalendar2.get(DAY_OF_MONTH) - gCalendar1.get(DAY_OF_MONTH));

        } else if (data1 > data2) {
            return -calculaDiasComerciaisCorridos(data2, data1);

        } else {
            return 0;
        }
    }

	/**
	 * Método calculaDiasCorridos.
	 * 
	 * @param data1
	 * @param data2
	 * @return
	 * 
	 * @since JDK1.4.2_06
	 * 
	 * @author Ramon Oliveira, em 24/12/2005 - versão inicial.
	 */
	public static int calculaDiasCorridos(java.util.Date data1,
			java.util.Date data2) {
		return calculaDiasCorridos(data1.getTime(), data2.getTime());
	}
}