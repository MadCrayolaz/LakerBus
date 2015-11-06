package csc380.lakerbus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by IAmACrayon on 11/6/2015.
 */
public class TimeSorter {
    static Date now;

    public TimeSorter() {

    }

    static public ArrayList<String> sort(ArrayList<String> times) {
        DateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        ArrayList<Date> toSort = new ArrayList<Date>();
        for(String bbb:times) {
            try {
                Date date = inputFormat.parse(bbb);
                toSort.add(date);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

        }
        now = Calendar.getInstance().getTime();
        toSort.add(now);
        Collections.sort(toSort);
        ArrayList<String> holder = new ArrayList<String>();
        for(Date d:toSort) holder.add(inputFormat.format(d));
        for(Date d:toSort) holder.add(inputFormat.format(d));
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(holder.get(holder.indexOf(now.toString()) + 1).toString());
        toReturn.add(holder.get(holder.indexOf(now.toString()) + 2).toString());
        return toReturn;
    }
}
