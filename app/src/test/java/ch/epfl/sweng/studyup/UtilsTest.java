package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class UtilsTest {
    @Test
    public void waitInterrupted() {
        PowerMockito.mockStatic(Log.class);

        PowerMockito.when(Log.w("FAIL","Test was interrupted: ")).thenThrow(InterruptedException.class);

        waitAndTag(500, "FAIL");
    }

    @Test
    public void getOrDefaultTest(){
        Map<String, Object> map = new HashMap<String, Object>(){
            {
                put("Test", 1);
                put("Test2", "test");
            }
        };

        assertEquals(1, Utils.getOrDefault(map, "Test", 2));
        assertEquals("test", Utils.getOrDefault(map, "Test2", 2));
        assertEquals(true, Utils.getOrDefault(map, "sdf", true));

    }
}
