package ch.epfl.sweng.studyup.npc;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.Constants.NPC_ACTIVITY_INTENT_NAME;

public class NPCActivity extends RefreshContext {
    private NPC npc;

    private final static int TIME_BETWEEN_MESSAGES = 1500;
    private final static int TIME_BETWEEN_CHARACTERS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npc);
        String npcName = getIntent().getStringExtra(NPC_ACTIVITY_INTENT_NAME);
        npc = Utils.getNPCfromName(npcName);
        ImageView imageView = findViewById(R.id.npc_image);
        imageView.setImageResource(npc.getImage());

        setupMessages();
        displayMessagesOneByOne();
    }

    public void onBackButtonNPC(View v) {
        finish();
    }

    public void displayMessagesOneByOne() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index == npc.getMessages().size()) {
                    Button yesButton = findViewById(R.id.yes_button_npc);
                    Button noButton = findViewById(R.id.no_button_npc);

                    yesButton.setVisibility(View.VISIBLE);
                    noButton.setVisibility(View.VISIBLE);

                    scroll();
                } else {
                    displayCharactersOneByOne(index);
                }

                index++;
                if (index <= npc.getMessages().size()) {
                    handler.postDelayed(this, TIME_BETWEEN_MESSAGES);
                }
            }
        });
    }

    private void displayCharactersOneByOne(final int indexOfMessage) {
        final TextView message = findViewById(indexOfMessage);
        final CharSequence m = message.getText();

        message.setText("");
        message.setVisibility(View.VISIBLE);

        final Handler handlerText = new Handler();
        handlerText.post(new Runnable() {
            private int i = 0;

            @Override
            public void run() {
                message.append(m, i, i+1);

                scroll();

                i++;
                if (i < m.length()) {
                    handlerText.postDelayed(this, TIME_BETWEEN_CHARACTERS);
                }
            }
        });
    }

    private void scroll() {
        final Handler scroll = new Handler();
        scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScrollView scrollNCP = findViewById(R.id.scrollView_npc);
                scrollNCP.fullScroll(View.FOCUS_DOWN);
            }
        }, TIME_BETWEEN_CHARACTERS);
    }

    public void setupMessages() {
        //List<Integer> messages = npc.getMessages();

        for (int i = 0; i < npc.getMessages().size(); ++i) {
            String message = getString(npc.getMessages().get(i));
            addMessage(message, i, npc.getMessages().size() - 1);
        }

        fixYesNoButtons(npc.getMessages().size() - 1);
    }

    private void addMessage(String m, int index, int maxIndex) {
        ConstraintLayout.LayoutParams lparams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 8, 0, 8);
        lparams.horizontalBias = index % 2;
        // If index = max
        lparams.bottomToTop = index == maxIndex ? R.id.no_button_npc : index + 1;
        lparams.endToEnd = R.id.constraintLayout_npc;
        lparams.startToStart = R.id.constraintLayout_npc;
        // If index = 0
        lparams.topToBottom = index == 0 ? R.id.npc_image : index - 1;

        final TextView message = new TextView(this);
        message.setLayoutParams(lparams);
        message.setId(index);
        message.setText(m);
        message.setBackground(getDrawable(index % 2 == 0 ? R.drawable.message_from_npc : R.drawable.message_from_user));
        message.setTextColor(getResources().getColor(R.color.colorGreyBlack));
        message.setTextSize(20);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        message.setMaxWidth((int) (width * 0.7));
        message.setWidth(message.getMaxWidth());

        message.setVisibility(View.GONE);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout_npc);
        constraintLayout.addView(message);
    }

    private ConstraintLayout.LayoutParams getConstraintYesNoButtons(int maxIndex) {
        ConstraintLayout.LayoutParams lparams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(8, 8, 8, 8);
        lparams.bottomToBottom = R.id.constraintLayout_npc;
        lparams.endToEnd = R.id.constraintLayout_npc;
        lparams.startToStart = R.id.constraintLayout_npc;
        lparams.topToBottom = maxIndex;

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (174 * scale + 0.5f);

        lparams.width = pixels;

        return lparams;
    }

    public void onYesButton(View view) {
        npc.onYesButton(this);
    }

    private void fixYesNoButtons(int maxIndex) {
        ConstraintLayout.LayoutParams lparamsYes = getConstraintYesNoButtons(maxIndex);
        lparamsYes.horizontalBias = 1f;

        ConstraintLayout.LayoutParams lparamsNo = getConstraintYesNoButtons(maxIndex);
        lparamsNo.horizontalBias = 0f;

        Button yesButton = findViewById(R.id.yes_button_npc);
        Button noButton = findViewById(R.id.no_button_npc);

        yesButton.setLayoutParams(lparamsYes);
        yesButton.setVisibility(View.GONE);
        noButton.setLayoutParams(lparamsNo);
        noButton.setVisibility(View.GONE);
    }
}
