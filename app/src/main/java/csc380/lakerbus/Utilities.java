package csc380.lakerbus;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Utilities {
    static Date now;

    public Utilities() {}

    static public LatLng findNearest(ArrayList<LatLng> aLL, LatLng ll) {
        Location loc = new Location("");
        loc.setLatitude(ll.latitude);
        loc.setLongitude(ll.longitude);
        float furthest = Float.MAX_VALUE;
        LatLng holder = new LatLng(0, 0);
        for(LatLng a:aLL) {
            Location tempLoc = new Location("");
            tempLoc.setLatitude(a.latitude);
            tempLoc.setLongitude(a.longitude);
            if(tempLoc.distanceTo(loc) < furthest) {
                furthest = tempLoc.distanceTo(loc);
                holder = new LatLng(tempLoc.getLatitude(), tempLoc.getLongitude());
            }
        }
        return holder;
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