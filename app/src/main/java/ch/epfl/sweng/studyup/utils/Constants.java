package ch.epfl.sweng.studyup.utils;

import android.util.Pair;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.npc.NPCItems;
import ch.epfl.sweng.studyup.npc.NPCSpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestType;

@SuppressWarnings("HardCodedStringLiteral")
public abstract class Constants {

    // Values associated with Firebase
    public static final String FB_USERS = "users";
    public static final String FB_USERNAME = "username";
    public static final String FB_ITEMS = "items";
    public static final String FB_FIRSTNAME = "firstname";
    public static final String FB_LASTNAME = "lastname";
    public static final String FB_SCIPER = "sciper";
    public static final String FB_ROLE = "role";
    public static final String FB_KNOWN_NPCS = "npcs";
    public static final String FB_UNLOCKED_THEME = "themes";
    public static final String FB_XP = "xp";
    public static final String FB_COURSES_ENROLLED = "attending courses";
    public static final String FB_COURSES_TEACHED = "teaching courses";
    public static final String FB_CURRENCY = "currency";
    public static final String FB_LEVEL = "level";
    public static final String FB_SECTION = "section";
    public static final String FB_YEAR = "year";
    public static final String FB_TOKEN = "token";
    public static final String FB_QUESTION_CLICKEDINSTANT = "clickedInstants questions";
    public static final String FB_QUESTIONS = "questions";
    public static final String FB_QUESTION_AUTHOR = "author";
    public static final String FB_QUESTION_TITLE = "title";
    public static final String FB_QUESTION_ANSWER = "answer";
    public static final String FB_QUESTION_TRUEFALSE = "trueFalse";
    public static final String FB_QUESTION_LANG = "lang";
    public static final String FB_QUESTION_DURATION = "duration";
    public static final String FB_QUESTS = "quests";
    public static final String FB_COURSE = "course";
    public static final String FB_COURSES = "courses";
    public static final String FB_SPECIALQUESTS = "specialQuests";
    public static final String FB_SPECIALQUEST_TYPE = "specialQuestType";
    public static final String FB_SPECIAL_QUEST_COMPLETION_COUNT = "completionCount";
    public static final String FB_TEACHING_STAFF = "teaching staff";
    public static final String FB_EVENTS = "events";
    public static final String FB_EVENTS_ID = "id";
    public static final String FB_EVENTS_NAME = "name";
    public static final String FB_EVENTS_LOCATION = "location";
    public static final String FB_EVENTS_START = "startTimeInMillis";
    public static final String FB_EVENTS_END = "endTimeInMillis";
    public static final String FB_COURSE_REQUESTS = "course requests";
    public static final String FB_REQUESTED_COURSES = "requested courses";
    public static final String FB_ANSWERED_QUESTIONS = "answeredQuestions";
    public static final Set<String> FB_ALL_ENTRIES = Sets.newHashSet(
            FB_USERS, FB_FIRSTNAME, FB_LASTNAME, FB_SCIPER, FB_ROLE, FB_XP, FB_CURRENCY,
            FB_LEVEL, FB_SECTION, FB_YEAR, FB_TOKEN, FB_QUESTIONS, FB_QUESTS, FB_USERNAME, FB_ITEMS, FB_ANSWERED_QUESTIONS);
    public static final Set<String> SUPER_USERS = Sets.newHashSet(
            "262413", "272432", "300137", "245940");


    // Values associated with Firebase storage
    public static final String QUESTION_IMAGES_DIRECTORY_NAME = "question_images";
    public static final String PROFILE_PICTURES_DIRECTORY_NAME = "profile_pictures";

    // Values associated with intent extras
    public static final String INTENT_ROLE_KEY = "INTENT_ROLE_KEY";

    /**
     * Constant of firebase (mostly testing purpose)
     * <p>
     * Reserved sciper:
     * MIN_SCIPER, MAX_SCIPER, 123456: reserved to manipulate in tests
     * MIN_SCIPER + 1: user present in database but with empty document (not valid format)
     */
    public static final int MAX_SCIPER = 999999;
    public static final int MIN_SCIPER = 100000;
    public static final long LOCATION_REQ_INTERVAL = 5 * 1000;
    public static final long LOCATION_REQ_FASTEST_INTERVAL = 5 * 1000;
    public static final String JUSTONCE = "JUST ONCE";

    // URLs and filenames used in app
    public static final String AUTH_SERVER_URL = "https://studyup-authenticate.herokuapp.com/getCode";
    public static final String AUTH_SERVER_TOKEN_URL = "https://studyup-authenticate.herokuapp.com/getToken?code=";
    public static final String TEQUILA_AUTH_URL = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo?access_token=";

    public static final String PERSIST_LOGIN_FILENAME = "persist_login.txt";
    public static final String NPC_INTERACTION_FILENAME = "enableNPCInteraction.txt";

    // Timeout values
    public static final int TIME_TO_WAIT_FOR_LOGIN = 2000; //[ms]
    public static final int TIME_TO_WAIT_FOR_AUTO_LOGIN = 2000; //[ms]

    // Basic Player stats
    public static final int XP_TO_LEVEL_UP = 100;
    public static final int CURRENCY_PER_LEVEL = 10;
    public static final int XP_STEP = 10;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_CURRENCY = 0;
    public static final int INITIAL_LEVEL = 1;
    public static final String INITIAL_SCIPER = String.valueOf(MIN_SCIPER);
    public static final String INITIAL_USERNAME = "Player";
    public static final String INITIAL_FIRSTNAME = "Jean-Louis";
    public static final String INITIAL_LASTNAME = "Réymond";

    // Constants for questions
    public static final int XP_GAINED_WITH_QUESTION = 10;

    // Constants for special quests
    public static final String SPECIAL_QUEST_KEY = "SPECIAL_QUEST_KEY";

    // Navigation items indexes for smooth transitions
    public static final int MAIN_INDEX = 0, QUESTS_INDEX_STUDENT = 1, SCHEDULE_INDEX = 2, MAP_INDEX = 3, INVENTORY_INDEX = 4, DEFAULT_INDEX_STUDENT = MAIN_INDEX;
    public static final int QUESTS_INDEX_TEACHER = 0, COURSE_STAT_INDEX = 1, COURSE_SELECTION_FOR_SCHEDULE_INDEX = 2;

    // Settings constants
    public static final String ENGLISH = "English";
    public static final String FRENCH = "Français";
    public static final String[] LANGUAGES = {ENGLISH, FRENCH};

    public static final String USER_PREFS = "StudyUpPrefs";
    public static final String LANG_SETTINGS_KEYWORD = "lang";
    public static final String COLOR_SETTINGS_KEYWORD = "color";
    public static final String SETTINGS_COLOR_RED = "red";
    public static final String SETTINGS_COLOR_GREEN = "green";
    public static final String SETTINGS_COLOR_ORANGE = "orange";
    public static final String SETTINGS_COLOR_BLUE = "blue";
    public static final String SETTINGS_COLOR_DARK = "dark";

    // Constants for schedule
    public static final int FIRST_DAY_SCHEDULE = 19;
    public static final int LAST_DAY_SCHEDULE = FIRST_DAY_SCHEDULE + 4;
    public static final int WEEK_OF_MONTH_SCHEDULE = 4;
    public static final int MONTH_OF_SCHEDULE = 10;
    public static final int YEAR_OF_SCHEDULE = 2018;

    public static final List<String> durationChoice = Arrays.asList("0", "2mn", "1h", "5h", "24h");
    public static final List<Long> durationCorrespond = Arrays.asList(0l, 120000l, 3600000l, 18000000l, 86400000l);

    // UUID used for testing
    public static final String MOCK_UUID = "fake-UUID";
    public static final Set<String> MOCK_UUIDS = Sets.newHashSet(MOCK_UUID, "Temporary fake uuid", "UUID-for-text-question");
    public static final Set<Pair<String, String>> MOCK_NAMES = Sets.newHashSet(
            new Pair<>(INITIAL_FIRSTNAME, INITIAL_LASTNAME),
            new Pair<>(INITIAL_FIRSTNAME+"1", INITIAL_LASTNAME+"1"),
            new Pair<>("John", "Doe")
    );

    // Enums for Role, Course
    public enum Role {
        student,
        teacher
    }

    public enum Course {
        SWENG("Software Engineering"),
        Algebra("Algebra"),
        Ecology("Ecology"),
        Blacksmithing("Blacksmithing"),
        Analyse("Analyse"),
        Physics("Physics"),
        ToC("Theory of Computation"),
        ProbaStat("Probability and Statistics"),
        FunProg("Functional Programming"),
        FakeCourse("A fake course");

        private String name = "";

        Course(String name) {
            this.name = name;
        }

        /**
         * return the longer description of the course,
         * contrary to name() function that returns only the shorter name
         */
        public String toString() {
            return name;
        }

        public static List<String> getNamesFromCourses(List<Course> courses) {
            ArrayList<java.lang.String> coursesName = new ArrayList<>(courses.size());
            for (int i = 0; i < courses.size(); ++i) {
                coursesName.add(courses.get(i).name());
            }
            return coursesName;
        }
    }


    //NPCS
    public static final String NPC_ACTIVITY_INTENT_NAME = "name";
    public static final int NPC_MARKER_HEIGHT = 140;
    public static final int NPC_MARKER_WIDTH = 80;
    public static final double NPC_RANGE = 30;
    public static List<NPC> allNPCs = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
            new NPCSpecialQuest(new SpecialQuest(SpecialQuestType.FIVE_QUESTIONS),"Charlie", Rooms.ROOMS_LOCATIONS.get("CO_1_4").getLocation(), R.drawable.charlie, 3),
            new NPCItems(new ArrayList<>(Arrays.asList(Items.BLUE_THEME, Items.ORANGE_THEME, Items.DARK_THEME)),"Muerte", Rooms.ROOMS_LOCATIONS.get("CE_1_1").getLocation(), R.drawable.death, 3),
            new NPCItems(new ArrayList<>(Arrays.asList(Items.XP_POTION, Items.UNSTABLE_POTION)), "Jeykill", Rooms.ROOMS_LOCATIONS.get("SATELITTE").getLocation(), R.drawable.jeykill, 3),
            new NPCSpecialQuest(new SpecialQuest(SpecialQuestType.CONSISTENT_USE), "Gilbert", Rooms.ROOMS_LOCATIONS.get("ROLEX_CENTER").getLocation(), R.drawable.gilbert, 5),
            new NPCSpecialQuest(new SpecialQuest(SpecialQuestType.THREE_QUESTIONS), "Benedetto", Rooms.ROOMS_LOCATIONS.get("METRO_M1").getLocation(), R.drawable.benedetto, 3),
            new NPCItems(new ArrayList<>(Arrays.asList(Items.MAP)), "Roberto", Rooms.ROOMS_LOCATIONS.get("CM_1_4").getLocation(), R.drawable.roberto, 3),
            new NPCItems(new ArrayList<>(Arrays.asList(Items.GREEN_THEME)),"Luigi", Rooms.ROOMS_LOCATIONS.get("BC_0_0").getLocation(), R.drawable.devil, 3),
            new NPCSpecialQuest(new SpecialQuest(SpecialQuestType.CREATIVE_USERNAME),"Eleanor", Rooms.ROOMS_LOCATIONS.get("CE_1_6").getLocation(), R.drawable.eleanor, 7),
            new NPCSpecialQuest(new SpecialQuest(SpecialQuestType.LEVEL_UP_BONUS),"Cynthia", Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation(), R.drawable.cynthia, 7)
            )));

    // Flags for triggering special quest listener
    public enum SpecialQuestUpdateFlag {
        ANSWERED_QUESTION,
        LEVEL_UP,
        SET_USERNAME,
        USER_LOGIN
    }

    // Color strings
    public static final String gold = "#d4af37";
    public static final String silver = "#595959";
    public static final String bronze = "#cd7f32";

    // Test list for leaderboard testing
    public static List<Pair<String, Integer>> mockStudentRankings = new ArrayList<>(Arrays.asList(
            new Pair<String, Integer>("Giovanni Boccaccio", 3),
            new Pair<String, Integer>("Dante Alighieri", 7),
            new Pair<String, Integer>("Francesco Petrarca", 5)
    ));
}

