package net.zhuoweizhang.pill;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
  private static final String TAG = "Pill";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    try {
      ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
      List tasks = (List) ActivityManager.class.getMethod("getRecentTasksForUser", Integer.TYPE,
        Integer.TYPE, Integer.TYPE).invoke(am, 25, 0, 0);
      Log.i(TAG, tasks.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
