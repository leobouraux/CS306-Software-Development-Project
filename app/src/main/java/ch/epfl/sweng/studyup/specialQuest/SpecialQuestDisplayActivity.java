package ch.epfl.sweng.studyup.specialQuest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_KEY;

/*
This activity is used for displaying special quests.
It is called when a player clicks on a special quest in HomeActivity.
It is also called from AvailableSpecialQuests activity.
Thus the SpecialQuest that is passed as an intent extra may or may not be one that
the current player is enrolled in. See checks in code below.
 */
public class SpecialQuestDisplayActivity extends RefreshContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_quest_display);

        renderSpecialQuestInterface();
    }

    @Override
    public void onResume() {

        super.onResume();
        renderSpecialQuestInterface();
    }

    public void renderSpecialQuestInterface() {

        SpecialQuest specialQuest = (SpecialQuest) getIntent().getSerializableExtra(SPECIAL_QUEST_KEY);

        String displayTitle = specialQuest.getSpecialQuestType().getTitle();
        String displayDesc = specialQuest.getSpecialQuestType().getDescription();

        TextView titleView = findViewById(R.id.specialQuestTitle);
        titleView.setText(displayTitle);

        TextView descriptionView = findViewById(R.id.specialQuestDescription);
        descriptionView.setText(displayDesc);

        if (Player.get().getSpecialQuests().contains(specialQuest)) {
            // Player enrolled in special quest, display progress
            renderProgressBar(specialQuest.getProgress());

            renderAlreadyEnrolledButton();
        } else {
            // Player not enrolled, do not display progress, display enrollment button
            renderEnrollmentButton(specialQuest);
        }

        if (specialQuest.getProgress() >= 1.0) {
            ((TextView)findViewById(R.id.specialQuestEnrollButton)).setText(R.string.quest_completed);

            TextView congratText = findViewById(R.id.specialQuestCongrat);
            congratText.setText(R.string.congrat_text_special_quest);
            TextView rewardItemText = findViewById(R.id.specialQuestReward);
            rewardItemText.setText(specialQuest.getReward().getName());
        }
    }

    private void renderProgressBar(Double progress) {
        CircularProgressIndicator progressBarView = findViewById(R.id.specialQuestProgress);
        progressBarView.setVisibility(View.VISIBLE);
        progressBarView.setProgressTextAdapter(new CustomProgressTextAdapter());
        progressBarView.setProgress(progress * 100, 100);
    }

    private void renderAlreadyEnrolledButton(){
        Button enrollmentButton = findViewById(R.id.specialQuestEnrollButton);
        enrollmentButton.setText(R.string.already_enrolled);
        enrollmentButton.setEnabled(false);
    }

    private void renderEnrollmentButton(final SpecialQuest specialQuest) {
        Button enrollmentButton = findViewById(R.id.specialQuestEnrollButton);
        enrollmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.get().addSpecialQuest(specialQuest);
                Utils.restartHomeActivity(SpecialQuestDisplayActivity.this);
            }
        });
    }

    public void onBackButtonSpecialQuest(View v) {
        Utils.restartHomeActivity(SpecialQuestDisplayActivity.this);
    }

    public final class CustomProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {

        @Override
        public String formatText(double currentProgress) {
            return String.valueOf((int) currentProgress) + " %";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.restartHomeActivity(SpecialQuestDisplayActivity.this);
    }
}
