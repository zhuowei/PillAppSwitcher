package net.zhuoweizhang.pill;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
  private static final String TAG = "Pill";
  private RecyclerView mRecyclerView;
  private ScreensAdapter mAdapter;
  private OkHttpClient mHttpClient;
  private LinearLayoutManager mLayoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mRecyclerView = findViewById(R.id.recycler_view);
    mHttpClient = new OkHttpClient();
    mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new ScreensAdapter();
    mAdapter.mData = new JSONArray();
    mRecyclerView.setAdapter(mAdapter);
  }

  protected void onStart() {
    super.onStart();
    Request request = new Request.Builder().url("http://localhost:11133/").build();
    mHttpClient.newCall(request).enqueue(new Callback() {
                                           @Override
                                           public void onFailure(Call call, IOException e) {
                                             throw new RuntimeException(e);
                                           }
                                           @Override
                                           public void onResponse(Call call, final Response response) throws IOException {
                                             JSONArray data = null;
                                             try {
                                               data = new JSONArray(response.body().string());
                                             } catch (Exception e) {
                                               throw new RuntimeException(e);
                                             }
                                            receivedResponse(data);
                                           }
                                         });
  }

  private void receivedResponse(final JSONArray data) {
    MainActivity.this.runOnUiThread(new Runnable() {
      public void run() {
        try {
          mAdapter.mData = data;
          mAdapter.notifyDataSetChanged();
          System.out.println("Set data: " + data);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private static class ScreenHolder extends RecyclerView.ViewHolder {
    public ScreenHolder(View view) {
      super(view);
    }
    public ImageView mImageView;
    public TextView mPackageNameView;
  }

  private class ScreensAdapter extends RecyclerView.Adapter<ScreenHolder> {
    public JSONArray mData;
    @NonNull
    @Override
    public ScreenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ScreenHolder holder = new ScreenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_screen, parent, false));
      holder.mImageView = holder.itemView.findViewById(R.id.carousel_image);
      holder.mPackageNameView = holder.itemView.findViewById(R.id.carousel_package_name);
      return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ScreenHolder holder, int position) {
      int persistentId = 0;
      try {
        persistentId = mData.getJSONObject(position).getInt("persistentId");
        holder.mPackageNameView.setText(mData.getJSONObject(position).optString("topPackage", "Unknown"));
      } catch (JSONException je) {
        throw new RuntimeException(je);
      }
      Glide.with(holder.itemView).load("http://localhost:11133/thumb/" + persistentId)
      .into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
      return mData.length();
    }
  }


}
