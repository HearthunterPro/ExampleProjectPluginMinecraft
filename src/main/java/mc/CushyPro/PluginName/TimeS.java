package mc.CushyPro.PluginName;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeS {

	public static Date getDate(Date date, int cal, int args) {
		if (date == null)
			date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(cal, args);
		return c.getTime();
	}

	public static long get(Date now, Date end) {
		long diff = end.getTime() - now.getTime();
		return TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);
	}
}
