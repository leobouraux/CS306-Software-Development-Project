package ch.epfl.sweng.studyup.ItemsTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.items.Items.COIN_SACK;
import static org.junit.Assert.assertEquals;


@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class ShopActivityAndBuyItemActivityTest {
    private ListView list;

    @Rule
    public final ActivityTestRule<ShopActivity> mActivityRule =
            new ActivityTestRule<>(ShopActivity.class, true, false);

    @Before
    public void init() {
        mActivityRule.launchActivity(new Intent().putExtra(Items.class.getName(), new Items[]{COIN_SACK}));
        list = mActivityRule.getActivity().findViewById(R.id.list_view_shop);
        Player.get().resetPlayer();
    }

    @Test
    public void BuyTwoCoinSackAndPlusMinusButton() {
        Player.get().addCurrency(30, mActivityRule.getActivity());
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        for(int i = 0; i < 3; ++i) {
            onView(withId(R.id.plus_button)).perform(scrollTo(), click());
        }
        onView(withId(R.id.minus_button)).perform(scrollTo(), click());
        onView(withId(R.id.buy_button)).perform(scrollTo(), click());
        assertEquals(0, Player.get().getCurrency());
        assertEquals(Arrays.asList(COIN_SACK, COIN_SACK, COIN_SACK), Player.get().getItems());
    }

    @Test
    public void BuyWithNotEnoughMoneyDoesNotWorAndBackButton() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        Utils.waitAndTag(150, "Waiting for click on item");
        onView(withId(R.id.buy_button)).perform(scrollTo(), click());
        onView(withId(R.id.back_button)).perform(click());
        assertEquals(0, Player.get().getItems().size());
    }

    @Test
    public void backButtonTest() {
        onView(withId(R.id.back_button)).perform(click());
    }

    @After
    public void resetPlayer() {
        Player.get().resetPlayer();
    }

}
