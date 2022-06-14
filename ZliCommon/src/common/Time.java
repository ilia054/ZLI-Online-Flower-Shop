package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
*The class Time has diffrent static methods that are related to time calculations
* @author Biran Fridman
*/


public class Time {
	/**
	 * calculateTimeDiff(String date)
	 * this function calculates the time diffrence in minutes between the input date to current time(LocalTime)
	 * date format must be: yyyy-MM-dd HH:mm
	 * @param date the date
	 * @return time diffrence between date to current time in minutes
	 */
	public static Long calculateTimeDiff(String date)
	{
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   //System.out.println(dtf.format(now));  
		   String my_str = dtf.format(now);
		   String my_new_str = my_str.replace("/", "-");
		   SimpleDateFormat date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		   SimpleDateFormat date2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		   Date odate = null;
		   Date cdate = null;
		   try {
				odate = date1.parse(date);
				cdate = date2.parse(my_new_str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			   
		   Long diffInMillies = odate.getTime()-cdate.getTime();
		   
		return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * getCurrentMonthAndYear()
	 * returns the current month and year in arraylist only if it's after the 30th of the month
	 * @return ArrayList that holds currrent month in cell 1 and current year in cell 0	 
	 */
	public static ArrayList<String> getCurrentMonthAndYearOnlyIfItIsTheEndOfTheMonth(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now(); 
		String my_str = dtf.format(now);
		String[] dateStrings = my_str.split("/");
		if(Integer.valueOf(dateStrings[2]) >= 30) {
			//											month		 , year
			return new ArrayList<String>(Arrays.asList(dateStrings[1], dateStrings[0]));
		}
		return null;
	}
	
	/**
	 * getCurrentMonthAndYear()
	 * returns the current month and year in arraylist
	 * @return ArrayList that holds currrent month in cell 1 and current year in cell 0	 
	 */
	public static ArrayList<String> getCurrentMonthAndYear(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now(); 
		String my_str = dtf.format(now);
		String[] dateStrings = my_str.split("/");
		//											month		 , year
		return new ArrayList<String>(Arrays.asList(dateStrings[1], dateStrings[0]));
	}
	
	
	/**
	 * formatLocalDate()
	 * this function creates a specific format for localtime (yyyy-MM-dd HH:mm)
	 * @return String of localtime in format yyyy-MM-dd HH:mm
	 */
	public static String formatLocalDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
		LocalDateTime now = LocalDateTime.now(); 
		return dtf.format(now);
	}
	
	/**
	 * saleDateReminder()
	 * This method calculates the remaining time of a current sale
	 * @param date which is the endDate of the sale 
	 * @return ArrayList {Remaining Days, Remaining Hours,Remaining Minutes};
	 */
	public static ArrayList<Integer> saleDateReminder(String date)
	{
		ArrayList<Integer> daysHoursMinutes=new ArrayList<>();
		Long timeDiff=calculateTimeDiff(date+":00");
		int days=(int) (timeDiff/1440);
		timeDiff=timeDiff%1440;
		int hours=(int)(timeDiff/60);
		timeDiff=timeDiff%60;
		int minutes=(int)(timeDiff/1);
		daysHoursMinutes.add(days);
		daysHoursMinutes.add(hours);
		daysHoursMinutes.add(minutes);
		return daysHoursMinutes;
	}
}
