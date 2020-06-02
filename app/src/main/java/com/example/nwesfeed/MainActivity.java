package com.example.nwesfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<NewsItem> news;
    private RecViewAdapter adapter;
    private RecyclerView recyclerView;
    private NewsItem item;
    String title = "";
    String desc = "";
    String link = "";
    String date = "";
    String img="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new RecViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        news = new ArrayList<>();

        GetDataAsynkTask asynkTask = new GetDataAsynkTask();
        asynkTask.execute();
    }

    class GetDataAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setNews(news);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();
            try {
                initXMLPullParser(inputStream);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        pullParser.setInput(inputStream, null);//after this parser will point to start of the page
        pullParser.nextTag();
        pullParser.require(XmlPullParser.START_TAG, null, "rss");
        while (pullParser.next() != XmlPullParser.END_TAG) {
            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            pullParser.require(XmlPullParser.START_TAG, null, "channel");
            while (pullParser.next() != XmlPullParser.END_TAG) {
                if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name=pullParser.getName();
                if (name.equals("item")) {
                    pullParser.require(XmlPullParser.START_TAG, null, "item");
                    while (pullParser.next() != XmlPullParser.END_TAG) {
                        if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String tagName = pullParser.getName();
                        if (tagName.equals("title")) {
                            title = getContent(pullParser, "title");
                        } else if (tagName.equals("description")) {
                            desc = getContent(pullParser, "description");
                        } else if (tagName.equals("link")) {
                            link = getContent(pullParser, "link");
                        } else if (tagName.equals("pubDate")) {
                            date = getContent(pullParser, "pubDate");
                        } else {
                            skipTag(pullParser);
                        }
                    }
                    item = new NewsItem(title, desc, date, link);
                    news.add(item);
                } else {
                    skipTag(pullParser);
                }
            }
        }
    }

    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int number = 1;
        while (number != 0) {
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    number++;
                    break;
                case XmlPullParser.END_TAG:
                    number--;
                    break;
                default:
                    break;
            }
        }
    }

    private String getContent(XmlPullParser pullParser, String tagName) {
        try {
            pullParser.require(XmlPullParser.START_TAG, null, tagName);
            String content = "";
            if (pullParser.next() == XmlPullParser.TEXT) {
                content = pullParser.getText();
                pullParser.next();
            }
            return content;
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream() {
        try {
            URL url = new URL("https://www.autosport.com/rss/feed/f1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
