import java.io.IOException;
import java.util.logging.*;



public class Log {
	private final static Logger logr = Logger.getLogger(Log.class.getName());
	
	public static Logger getLogger()
	{
		return logr;
	}
	
	public static void resetLogger()
	{
		LogManager.getLogManager().reset();
		logr.setLevel(Level.ALL);
		
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.SEVERE);
		logr.addHandler(ch);
		
		try {
			FileHandler fh = new FileHandler("myLogger.log");
			fh.setFormatter(new SimpleFormatter());
			fh.setLevel(Level.FINE);
			logr.addHandler(fh);
			
		} catch(IOException e) {
			logr.log(Level.SEVERE, "logger not working");
		}

	}
