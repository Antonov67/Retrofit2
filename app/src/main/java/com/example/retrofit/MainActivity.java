package com.example.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ViewPager viewPager;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    public static class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public MyAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new BlankFragment();
                case 1:
                    return new BlankFragment2();

                case 2:
                    return new BlankFragment3();

                default:
                    return new BlankFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPosts();
        //getComments();
        //createPost();
        //updatePost();
    }
    private void getPosts(){
        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post:posts){
                    String content="";
                    content+="ID " + post.getId() + "\n";
                    content+="UserID " + post.getUserId()+"\n";
                    content+="Title " + post.getTitle()+"\n";
                    content+="Text " + post.getText()+"\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
    private void getComments(){
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }
                List<Comment> comments = response.body();
                for (Comment post:comments){
                    String content="";
                    content+="ID " + post.getId() + "\n";
                    content+="PostID " + post.getPostId()+"\n";
                    content+="Name " + post.getName()+"\n";
                    content+="text " + post.getText()+"\n";
                    content+="Email " + post.getEmail()+"\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
    public void createPost(){
        Post post = new Post(23, "New Title", "New Text");
        Map<String,String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");
        fields.put("body", "New body");


        Call<Post> call = jsonPlaceHolderApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }
                Post postResponse = response.body();
                String content="";
                content+="Code: " + response.code() + "\n";
                content+="ID " + postResponse.getId() + "\n";
                content+="UserID " + postResponse.getUserId()+"\n";
                content+="Title " + postResponse.getTitle()+"\n";
                content+="Text " + postResponse.getText()+"\n\n";
                textView.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
    private void updatePost(){
        Post post = new Post(6,"Antonov","Antonov");
        Call<Post> call = jsonPlaceHolderApi.putPost(55,post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }
                Post postResponse = response.body();
                String content="";
                content+="Code: " + response.code() + "\n";
                content+="ID " + postResponse.getId() + "\n";
                content+="UserID " + postResponse.getUserId()+"\n";
                content+="Title " + postResponse.getTitle()+"\n";
                content+="Text " + postResponse.getText()+"\n\n";
                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}