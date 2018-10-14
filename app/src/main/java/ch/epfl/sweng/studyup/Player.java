package ch.epfl.sweng.studyup;

import ch.epfl.sweng.studyup.Utils.SECTION_SHORT;
import ch.epfl.sweng.studyup.Utils.YEAR;

import static ch.epfl.sweng.studyup.Firebase.userData;
import static ch.epfl.sweng.studyup.Utils.FB_CURRENCY;
import static ch.epfl.sweng.studyup.Utils.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LASTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LEVEL;
import static ch.epfl.sweng.studyup.Utils.FB_SCIPER;
import static ch.epfl.sweng.studyup.Utils.FB_XP;

public class Player {
    private static Player instance = null;
    private int experience;
    private int level;
    private int currency;
    private String firstName;
    private String lastName;
    private int sciper;
    private SECTION_SHORT section;
    private YEAR year;
    private int[] questionsCurr;
    private int[] questsCurr;
    private int[] questionsAcheived;
    private int[] questsAcheived;
    public final static int XP_TO_LEVEL_UP = 100;
    public final static int CURRENCY_PER_LEVEL = 10;
    public static final int XP_STEP = 10;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_CURRENCY = 0;
    public static final int INITIAL_LEVEL = 1;
    public static final int INITIAL_SCIPER = 100000;
    private static final String TAG = Player.class.getSimpleName();

    /**
     * Constructor called before someone is login.
     */
    private Player(){
        experience = INITIAL_XP;
        currency = INITIAL_CURRENCY;
        level = INITIAL_LEVEL;
        sciper = INITIAL_SCIPER;
    }

    public static Player get(){
        if(instance == null){
            instance = new Player();
        }
        return instance;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrency() { return currency; }

    private void updateLevel() {
        int newLevel = experience / XP_TO_LEVEL_UP + 1;

        if(newLevel - level > 0){
            currency += (newLevel - level) * CURRENCY_PER_LEVEL;
        }

        if(level != newLevel) {
            Firebase.get().setUserData(FB_LEVEL, newLevel);
        }

        level = newLevel;
    }

    public void addCurrency(int curr){
        currency += curr;
        Firebase.get().setUserData(FB_CURRENCY, currency);

    }

    public void addExperience(int xp){
        experience += xp;
        Firebase.get().setUserData(FB_XP, experience);

        updateLevel();
    }

    public double getLevelProgress(){
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }

    //Changes the Player to the basic state, right after constructor
    public void reset(){
        instance = new Player();
        instance.setSciper(INITIAL_SCIPER);
        instance.setName("", "");
    }

    /**
     * Method used to save the state contained in the userData attribute of the class Firebase in
     * the class Player
     * (currently only saving experience, currency, names and sciper)
     */
    public void updatePlayerData() {
        /*int newExperience = Ints.checkedCast((Long) userData.get(FB_XP)); Keeping this in case we want to have number attribute and not strings*/

        experience = Integer.parseInt(userData.get(FB_XP).toString());
        currency = Integer.parseInt(userData.get(FB_CURRENCY).toString());
        firstName = userData.get(FB_FIRSTNAME).toString();
        lastName = userData.get(FB_LASTNAME).toString();
        sciper = Integer.parseInt(userData.get(FB_SCIPER).toString());

        updateLevel();
    }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSciper(int sciper) {
        this.sciper = sciper;
    }

    public int getSciper() {
        return sciper;
    }
}