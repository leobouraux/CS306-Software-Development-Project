package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.util.Log;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ch.epfl.sweng.studyup.Firebase.userData;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;


public class Utils {

    //Firebase entries
    public static final String FB_USERS = "users";
    public static final String FB_FIRSTNAME = "firstname";
    public static final String FB_LASTNAME = "lastname";
    public static final String FB_SCIPER = "sciper";
    public static final String FB_XP = "xp";
    public static final String FB_CURRENCY = "currency";
    public static final String FB_LEVEL = "level";
    public static final String FB_SECTION = "section";
    public static final String FB_YEAR = "year";
    public static final String FB_TOKEN = "token";
    public static final String FB_QUESTIONS = "questions";
    public static final String FB_QUESTS = "quests";
    public static final Set<String> FB_ALL_ENTRIES= Sets.newHashSet(
            FB_USERS, FB_FIRSTNAME, FB_LASTNAME, FB_SCIPER, FB_XP, FB_CURRENCY,
            FB_LEVEL, FB_SECTION, FB_YEAR, FB_TOKEN, FB_QUESTIONS, FB_QUESTS
    );

    /**
     * Constant of firebase (mostly testing purpose)
     *
     * Reserved sciper:
     * MIN_SCIPER, MAX_SCIPER, 123456: reserved to manipulate in tests
     * MIN_SCIPER+1: user present in database but with empty document (not valid format)
     */
    public static final int MAX_SCIPER = 999999;
    public static final int MIN_SCIPER = 100000;
    public static Map<String, Object> dbStaticInfo = null;

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag error message if any
     */
    public static final void waitAndTag(int time, String tag) {
        try{
            Thread.sleep(time);
        } catch(InterruptedException e) {
            Log.w(tag, "Test was interrupted: "+e.getMessage());
            return;
        }
    }

    /**
     * Put the given information in Firebase.userData
     */
    public static void putUserData(String key, Object value) {
        if(userData == null) {
            userData = new HashMap<>();
        }

        userData.put(key, value);
    }
  
  public static LatLng position;
    public static FusedLocationProviderClient locationProviderClient;
    public static Context mainContext;
    public static final long LOCATION_REQ_INTERVAL = 5*1000;
    public static final long LOCATION_REQ_FASTEST_INTERVAL = 5*1000;
    public static final String ALLOW = "ALLOW";
    public static final String DENY = "DENY";
    public static final String CAMERA = "Camera";
    public static final String GALLERY = "Gallery";


    //test purpose
    public static Boolean isMockEnabled = false;
    public static Location mockLoc;

    //Map that links item id to activities
    public static final Map<Integer, Class> idToAct;
    static {
        Map<Integer, Class> tempMap = new HashMap<>();
        tempMap.put(R.id.navigation_home, MainActivity.class);
        tempMap.put(R.id.navigation_quests, QuestsActivity.class);
        tempMap.put(R.id.navigation_rankings, RankingsActivity.class);
        tempMap.put(R.id.navigation_map, MapsActivity.class);
        tempMap.put(R.id.navigation_chat, ChatActivity.class);
        idToAct = Collections.unmodifiableMap(tempMap);
    }

}