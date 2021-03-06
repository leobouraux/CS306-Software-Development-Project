package ch.epfl.sweng.studyup.player;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.specialQuest.AvailableSpecialQuestsActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.utils.Callback;
import ch.epfl.sweng.studyup.utils.adapters.SpecialQuestListViewAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.player.LeaderboardActivity.studentRankComparator;
import static ch.epfl.sweng.studyup.utils.Constants.MAIN_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_KEY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.LOCATION_PROVIDER_CLIENT;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.StatsUtils.loadUsers;
import static ch.epfl.sweng.studyup.utils.Utils.setupToolbar;

public class HomeActivity extends NavigationStudent {
    private final int MY_PERMISSION_REQUEST_FINE_LOCATION = 202;
    private ImageView image_view;

    // Text that will be displayed in the
    // layout
    public CircularProgressIndicator levelProgress;
    ImageButton pic_button2;
    ImageButton pic_button;

    // Display login success message from intent set by authentication activity
    public void displayLoginSuccessMessage(Intent intent) {
        String successMessage = intent.getStringExtra(getString(R.string.post_login_message_value));
        if (successMessage != null) {
            Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GPS_MAP", "Destroyed main and canceled Background location service");
        unScheduleBackgroundLocation();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar(this);

        displayLoginSuccessMessage(getIntent());

        pic_button = findViewById(R.id.pic_btn);
        pic_button2 = findViewById(R.id.pic_btn2);

        Log.d("GPS_MAP", "Started main");
        // GPS Job scheduler
        ActivityCompat.requestPermissions(
                HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_FINE_LOCATION);

        LOCATION_PROVIDER_CLIENT = new FusedLocationProviderClient(this);

        if (!MOCK_ENABLED) {
            scheduleBackgroundLocation();
        }

        //bottom navigation bar
        navigationSwitcher(HomeActivity.this, HomeActivity.class, MAIN_INDEX);

        ActivityCompat.requestPermissions(
                HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123);

        loadInterface();
        populateSpecialQuestsList();
    }

    private void loadInterface() {
        // User picture
        ImageButton pic_button = findViewById(R.id.pic_btn);
        pic_button2 = findViewById(R.id.pic_btn2);
        image_view = findViewById(R.id.pic_imageview);

        FileStorage.downloadProfilePicture(Player.get().getSciperNum(), image_view);

        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
            }
        });
        pic_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
                pic_button2.setBackground(getResources().getDrawable(R.drawable.ic_mode_edit_clicked_24dp));
            }
        });

        //username
        TextView view_username = findViewById(R.id.usernameText);
        view_username.setText(Player.get().getUserName());
        view_username.setMaxLines(1);
        view_username.setMaxWidth(300);

        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameDisplay();
        updateCurrDisplay();
        updateXpAndLvlDisplay();
        populateSpecialQuestsList();
        updateStatDisplay();
        updateFavoriteCourseDisplay(null);
        updateNPCDiscoveredNumber();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("GPS_MAP", "Permission granted");
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.location_required), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void scheduleBackgroundLocation(){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(BackgroundLocation.BACKGROUND_LOCATION_ID, new ComponentName(this, BackgroundLocation.class)).setPeriodic(15 * 60 * 1000).build();
        scheduler.schedule(jobInfo);
        for(JobInfo job: scheduler.getAllPendingJobs()){
            Log.d("GPS_MAP", "Scheduled: " + job);
        }
        Log.d("GPS_MAP", "schedule");
    }
    public void unScheduleBackgroundLocation(){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BackgroundLocation.BACKGROUND_LOCATION_ID);
    }

    public void updateUsernameDisplay() {
        TextView view_username = findViewById(R.id.usernameText);
        view_username.setText(Player.get().getUserName());
        view_username.setMaxLines(1);
        view_username.setMaxWidth(300);
    }

    public void updateXpAndLvlDisplay() {
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
        ((TextView) findViewById(R.id.levelText)).setText(getString(R.string.text_level) +" "+ Player.get().getLevel());
    }

    public void populateSpecialQuestsList() {
        final List<SpecialQuest> specialQuestsList = Player.get().getSpecialQuests();

        List<Integer> iconList = new ArrayList<>();
        for (SpecialQuest currSpecialQuest : specialQuestsList) {
            if (currSpecialQuest.getProgress() == 1) {
                iconList.add(R.drawable.ic_check_green_24dp);
            }
            else {
                iconList.add(R.drawable.ic_todo_grey_24dp);
            }
        }

        ListView specialQuestsListView = findViewById(R.id.specialQuestsListView);
        SpecialQuestListViewAdapter listAdapter =
            new SpecialQuestListViewAdapter(this, R.layout.special_quest_model, specialQuestsList, iconList);
        specialQuestsListView.setAdapter(listAdapter);

        /*
        Set onClick listeners for all special quests that will open SpecialQuestDisplayActivity,
        passing the Special Quest as a serializable object. It will be de-serialized and used in that activity.
         */
        specialQuestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displaySpecialQuestion = new Intent(HomeActivity.this, SpecialQuestDisplayActivity.class);
                displaySpecialQuestion.putExtra(SPECIAL_QUEST_KEY, Player.get().getSpecialQuests().get(position));

                startActivity(displaySpecialQuestion);
            }});
    }

    public Callback<List> displayRankOfStudent = new Callback<List>() {
        public void call(List userList) {
            List<Pair<String, Integer>> ranking = new ArrayList<>();
            for (UserData studentData : (List<UserData>) userList) {
                ranking.add(new Pair<>(studentData.getSciperNum(), studentData.getXP()));
            }
            Collections.sort(ranking, studentRankComparator);
            int counter = 0;
            for(Pair<String, Integer> studentRank : ranking) {
                ++counter;
                if(studentRank.first.equals(Player.get().getSciperNum())) {
                    setRankTo(counter);
                    return;
                }
            }
        }
    };
    private void setRankTo(int rank) {
     TextView rankText = findViewById(R.id.rankNumberTextview);
     String rankValue = "#"+String.valueOf(rank);
     rankText.setText(rankValue);
    }
    private void updateStatDisplay() {
        loadUsers(displayRankOfStudent);
        computeQuestionsPercentage();
    }

    private void updateNPCDiscoveredNumber() {
        TextView npcNumberView = findViewById(R.id.numberOfNpcDiscoveredTextView);
        npcNumberView.setText(String.valueOf(Player.get().getKnownNPCs().size()));
    }

    private void computeQuestionsPercentage() {
        Map<String, List<String>> answeredQuestions = Player.get().getAnsweredQuestion();
        Set<String> keySet = answeredQuestions.keySet();
        double totalQuestions = keySet.size();
        int goodAnswers = 0;
        int answered = 0;
        for (String questionID: keySet) {
            List<String> questionInfo = answeredQuestions.get(questionID);
            boolean trueFalse = Boolean.parseBoolean(questionInfo.get(0));
            answered++;
            if (trueFalse)
                goodAnswers++;
        }
        double questionRatio = 0.0;
        if (totalQuestions > 0.0) {
            questionRatio = goodAnswers / totalQuestions * 100;
        }

        TextView ratioPercentage = findViewById(R.id.ratioPercentageTextview)   ;
        ratioPercentage.setText(String.format("%.1f %%", questionRatio));

        TextView answeredNumberView = findViewById(R.id.answeredNumberTextview);
        answeredNumberView.setText(String.valueOf(answered));
    }

    /**
     * Get the number of answers per course for the current player and put the course with the
     * maximum number of answer as the favorite course.
     * This count will not take into account the
     * questions that have been deleted as we cannot retrieve the course of the answered questions
     * anymore if it has been deleted.
     *
     * @param questions All the questions available to the player
     */
    private void updateFavoriteCourseDisplay(List<Question> questions) {
        if(questions == null) {
            getQuestions();
        } else {
            String favoriteCourse = getFavoriteCourse(getNbrAnswerPerCourse(questions));

            TextView favoriteCourseView = findViewById(R.id.favoriteCourseTextview);
            favoriteCourseView.setText(favoriteCourse);
        }
    }
    private void getQuestions() {
        Callback<List<Question>> onQuestionLoaded = new Callback<List<Question>>() {
            @Override
            public void call(List<Question> callbackParam) {
                updateFavoriteCourseDisplay(callbackParam);
            }
        };
        if(MOCK_ENABLED) {
            QuestionParser.parseQuestionsLiveData(this).observe(this, new Observer<List<Question>>() {
                @Override
                public void onChanged(@Nullable List<Question> questions) {
                    updateFavoriteCourseDisplay(questions);
                }
            });
        } else {
            Firestore.get().loadQuestions(this, onQuestionLoaded);
        }
    }
    private Map<String, Integer> getNbrAnswerPerCourse(List<Question> questions) {
        Map<String, List<String>> answersInfoById = Player.get().getAnsweredQuestion();
        Map<String, Integer> nbrAnswerPerCourse = new HashMap<>();
        for(Question q : questions) {
            String course = q.getCourseName();
            if(answersInfoById.containsKey(q.getQuestionId())) {
                if(nbrAnswerPerCourse.containsKey(course)) {
                    nbrAnswerPerCourse.put(course, nbrAnswerPerCourse.get(course) + 1);
                } else {
                    nbrAnswerPerCourse.put(course, 1);
                }
            }
        }

        return nbrAnswerPerCourse;
    }
    private String getFavoriteCourse(Map<String, Integer> nbrAnswerPerCourse) {
        int max = -1;
        String favoriteCourse = getString(R.string.no_course_representation);
        for (String c : nbrAnswerPerCourse.keySet()) {
            int nbrAnswerForC = nbrAnswerPerCourse.get(c);
            if (nbrAnswerForC > max) {
                max = nbrAnswerForC;
                favoriteCourse = c;
            }
        }

        return favoriteCourse;
    }

    public void onLeaderboardButtonClick(View view) {
        startActivity(new Intent(HomeActivity.this, LeaderboardActivity.class));
    }

    public void updateCurrDisplay() {
        ((TextView) findViewById(R.id.currText)).setText(getString(R.string.text_money) +" "+ Player.get().getCurrency());
    }
}