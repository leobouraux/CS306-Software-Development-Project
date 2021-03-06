package ch.epfl.sweng.studyup.utils;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.map.Room;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;

@SuppressWarnings("HardCodedStringLiteral")
public abstract class Rooms {
    private final static double RADIUS_ROOM = 30.0;

    public final static Map<String, Room> ROOMS_LOCATIONS = Collections.unmodifiableMap(new HashMap<String, Room>(){
        {
            put("CE_1_1", new Room(46.5206650, 6.5693740));
            put("CE_1_2", new Room(46.5204350, 6.5693790));
            put("CE_1_3", new Room(46.5206680, 6.5697490));
            put("CE_1_4", new Room(46.5204010, 6.5697540));
            put("CE_1_5", new Room(46.5203640, 6.5705050));
            put("CE_1_6", new Room(46.5204850, 6.5707300));

            put("CM_1_1", new Room(46.5206550, 6.5674980));
            put("CM_1_2", new Room(46.5203840, 6.5675020));
            put("CM_1_3", new Room(46.5203810, 6.5671270));
            put("CM_1_4", new Room(46.5204150, 6.5665410));
            put("CM_1_5", new Room(46.5204140, 6.5664010));

            put("CO_1_1", new Room(46.5199270, 6.5653150));
            put("CO_1_2", new Room(46.5199070, 6.5644510));
            put("CO_1_3", new Room(46.5199070, 6.5644510));
            put("CO_1_4", new Room(46.5201620, 6.5648470));
            put("CO_1_5", new Room(46.5201610, 6.5646430));
            put("CO_1_6", new Room(46.5201480, 6.5643660));

            put("INN_3_26", new Room(46.5187270, 6.5625000));

            put("INR_0_11", new Room(46.5184010, 6.5627920));

            put("BC_0_0", new Room(46.5185229, 6.5619692));

            put("SATELITTE", new Room(46.520614, 6.567683));
            put("ROLEX_CENTER", new Room(46.518274, 6.568702));
            put("METRO_M1", new Room(46.522380, 6.565437));
        }
    });

    // https://stackoverflow.com/questions/8832071/how-can-i-get-the-distance-between-two-point-by-latlng
    public static double distanceBetweenTwoLatLng(LatLng latLng1, LatLng latLng2){
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(latLng2.latitude - latLng1.latitude);
        double lngDiff = Math.toRadians(latLng2.longitude - latLng1.longitude);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(latLng1.latitude)) * Math.cos(Math.toRadians(latLng2.latitude)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }

    public static boolean checkIfUserIsInRoom() {
        if(Player.get().getCoursesEnrolled().isEmpty() || Player.get().getScheduleStudent().isEmpty() ||
                POSITION == null) {
            return false;
        }

        Calendar currTime = Calendar.getInstance();
        currTime.set(Calendar.YEAR, Constants.YEAR_OF_SCHEDULE);
        currTime.set(Calendar.MONTH, Constants.MONTH_OF_SCHEDULE);
        currTime.set(Calendar.WEEK_OF_MONTH, Constants.WEEK_OF_MONTH_SCHEDULE);
        List<String> playersCourses = Collections.unmodifiableList(new ArrayList<>(Constants.Course.getNamesFromCourses(Player.get().getCoursesEnrolled())));

        for(WeekViewEvent event : Player.get().getScheduleStudent()) {
            if(playersCourses.contains(event.getName()) &&
                    ROOMS_LOCATIONS.containsKey(event.getLocation()) &&
                    distanceBetweenTwoLatLng(ROOMS_LOCATIONS.get(event.getLocation()).getLocation(), POSITION) <= RADIUS_ROOM &&
                    currTime.after(event.getStartTime()) &&
                    currTime.before(event.getEndTime())) return true;
        }
        return false;
    }

}
