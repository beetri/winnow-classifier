package ia2.util;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class Profiler {
	
	private GregorianCalendar startTime;
	
	public Profiler() {
		this.startTime = new GregorianCalendar();
	}
	
	public synchronized void startOperation() {
		new Printer(startTime).start();
		this.startTime = new GregorianCalendar();
	}
	
} final class Printer extends Thread {
	
	private static final String DOT = ".";
	
	private static final String DOUBLE_DOT = ":";
	
	private static final String TAB = ":";

	private final Calendar now;
	
	private final Calendar startTime;
	
	public Printer(final Calendar startTime) {
		this.now = new GregorianCalendar();
		this.startTime = startTime;
	}
	@Override
	public void run() {
		long appoggio	= now.getTimeInMillis() - startTime.getTimeInMillis();
		int millis = (int) appoggio%1000;
		appoggio/=1000;
		int sec = (int) appoggio%60;
		appoggio/=60;
		int min = (int) appoggio%60;
		appoggio/=60;
		int ore = (int) appoggio%60;
		System.out.print(TAB);
		System.out.print(ore);
		System.out.print(DOUBLE_DOT);
		System.out.print(min);
		System.out.print(DOUBLE_DOT);
		System.out.print(sec);
		System.out.print(DOT);
		System.out.println(millis);
	}
}
