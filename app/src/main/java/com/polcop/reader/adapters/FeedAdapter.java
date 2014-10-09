package com.polcop.reader.adapters;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.polcop.reader.Constants;
import com.polcop.reader.PageInfo;
import com.polcop.reader.R;
import com.polcop.reader.StoryInfo;
import com.polcop.reader.UI.MovementCheck;
import com.polcop.reader.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 04.09.14.
 */
public class FeedAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StoryInfo> storyInfos;
    private ArrayList<ClickInfo> clickInfos;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public FeedAdapter(Context context, ArrayList<StoryInfo> storyInfos) {
        this.context = context;
        this.storyInfos = storyInfos;
        clickInfos = new ArrayList<ClickInfo>();
        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<StoryInfo> storyInfos) {
        this.storyInfos = storyInfos;
        updateClickInfos();
    }

    private void updateClickInfos() {
        int size = storyInfos.size() - clickInfos.size();
        for (int i = 0; i < size; i++) {
            clickInfos.add(new ClickInfo());
        }
    }

    static class ViewHolder {
        public TextView tvStoryNumber;
        public TextView tvStoryDate;
        public TextView tvStory;
        public ImageButton ibGood;
        public ImageButton ibBad;
        public TextView tvTags;
        public TextView tvRate;
    }

    static class ClickInfo {
        public boolean isGoodClicked = false;
        public boolean isBadClicked = false;
    }

    @Override
    public int getCount() {
        if (storyInfos == null) {
            return 0;
        }
        return storyInfos.size();
    }

    @Override
    public Object getItem(int position) {
        if (storyInfos == null) {
            return null;
        }
        return storyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MovementCheck movementCheck = new MovementCheck();
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.feed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvStoryNumber = (TextView) view
                    .findViewById(R.id.tvStoryNumber);
            viewHolder.tvStoryDate = (TextView) view
                    .findViewById(R.id.tvStoryDate);
            viewHolder.tvStory = (TextView) view.findViewById(R.id.tvStory);
            viewHolder.ibGood = (ImageButton) view.findViewById(R.id.ibGood);
            viewHolder.ibGood.setFocusable(false);
            viewHolder.ibBad = (ImageButton) view.findViewById(R.id.ibBad);
            viewHolder.ibBad.setFocusable(false);
            viewHolder.tvTags = (TextView) view.findViewById(R.id.tvTags);
            viewHolder.tvRate = (TextView) view.findViewById(R.id.tvRate);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvStoryNumber.setText(storyInfos.get(position).getStoryNumber());
        viewHolder.tvStoryDate.setText(storyInfos.get(position).getPublishDate());
        viewHolder.tvTags.setText(storyInfos.get(position).getSpannedTags());
        viewHolder.tvTags.setMovementMethod(movementCheck);
        viewHolder.tvRate.setText(storyInfos.get(position).getRate());
        viewHolder.tvStory.setText(createSpannableStory(position));
        viewHolder.tvStory.setMovementMethod(movementCheck);
        createClickListener(viewHolder, position);
        setRate(viewHolder, position);
        viewHolder.ibBad.setOnClickListener(listener);
        viewHolder.ibGood.setOnClickListener(listener);
        return view;
    }


    //совмещает низвание истории и историю
    private SpannableStringBuilder createSpannableStory(int position) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Spanned storyTitle = storyInfos.get(position).getStoryTitle();
        if (storyTitle != null)
            builder.append(storyInfos.get(position).getStoryTitle());
        builder.append(storyInfos.get(position).getStory());
        return builder;
    }

    private void setRate(ViewHolder viewHolder, int position) {
        switch (Utils.getLoaderId()) {
            case Constants.IT_HAPPENS_LOADER:
            case Constants.ZADOLBALI_LOADER:
                onlyGoodRateButton(viewHolder,position);
                break;
            case Constants.BASH_LOADER:
                if (PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_ABYSS_TOP)) {
                    //топ 25 всех историй
                    viewHolder.ibGood.setVisibility(View.INVISIBLE);
                    viewHolder.ibBad.setVisibility(View.INVISIBLE);
                    viewHolder.tvRate.setText(PageInfo.getInstance().getStoryInfos().get(position).getStoryNumber());
                }
                if (PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_ABYSS_BEST)) {
                    //в этих вкладках нет рейтинга
                    viewHolder.ibGood.setVisibility(View.INVISIBLE);
                    viewHolder.ibBad.setVisibility(View.INVISIBLE);
                    viewHolder.tvRate.setText("( ･_･)");
                } else {
                    if (clickInfos.get(position).isGoodClicked ||
                            clickInfos.get(position).isBadClicked) {
                        viewHolder.ibBad.setVisibility(View.INVISIBLE);
                        viewHolder.ibGood.setVisibility(View.INVISIBLE);
                        if (storyInfos.get(position).getRate().equals("???") || storyInfos.get(position).getRate().equals("...")) {
                            viewHolder.tvRate.setText("( ಠ_ಠ)");
                        } else {
                            int rateAfterClick = Integer.parseInt(storyInfos.get(position).getRate());
                            if (clickInfos.get(position).isGoodClicked) {
                                viewHolder.tvRate.setText(String.valueOf(rateAfterClick + 1));
                            } else {
                                viewHolder.tvRate.setText(String.valueOf(rateAfterClick - 1));
                            }
                        }
                    } else {
                        viewHolder.ibBad.setVisibility(View.VISIBLE);
                        viewHolder.ibGood.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case Constants.KILL_ME_PLZ_LOADER:
                if (clickInfos.get(position).isGoodClicked ||
                        clickInfos.get(position).isBadClicked) {
                    viewHolder.ibBad.setVisibility(View.INVISIBLE);
                    viewHolder.ibGood.setVisibility(View.INVISIBLE);
                    int rateAfterClick = Integer.parseInt(storyInfos.get(position).getRate());
                    if (clickInfos.get(position).isGoodClicked) {
                        viewHolder.tvRate.setText(String.valueOf(rateAfterClick + 1));
                    } else {
                        viewHolder.tvRate.setText(String.valueOf(rateAfterClick - 1));
                    }
                } else {
                    viewHolder.ibBad.setVisibility(View.VISIBLE);
                    viewHolder.ibGood.setVisibility(View.VISIBLE);
                }
                break;
            case Constants.SHORTIKI_LOADER:
                if(PageInfo.getInstance().getCurrentPage().equals(Constants.SHORTIKI_LINK)){
                    onlyGoodRateButton(viewHolder,position);
                }else{
                    viewHolder.ibBad.setVisibility(View.INVISIBLE);
                    viewHolder.ibGood.setVisibility(View.INVISIBLE);
                }
                break;
        }

    }

    //для шортиков задолбали и итхеппенс одинаковая подготовка рейтинга
    private void onlyGoodRateButton (ViewHolder viewHolder,int position){
        viewHolder.ibBad.setVisibility(View.INVISIBLE);
        viewHolder.ibGood.setVisibility(View.VISIBLE);
        if (storyInfos.get(position).getGoodURL().equals("")) {
            viewHolder.ibGood.setVisibility(View.INVISIBLE);
            return;
        }
        if (clickInfos.get(position).isGoodClicked)
            goodClick(viewHolder, position);
    }

    private void createClickListener(final ViewHolder viewHolder, final int position) {
        this.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Utils.getLoaderId()) {
                    case Constants.IT_HAPPENS_LOADER:
                        switch (v.getId()) {
                            case R.id.ibGood:
                                goodClick(viewHolder, position);
                                if (!(storyInfos.get(position).getGoodURL().equals(""))) {
                                    vote(Constants.IT_HAPPENS_LINK + storyInfos.get(position).getGoodURL());
                                }
                        }
                        Toast.makeText(context, "Ваш голос учтен", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.ZADOLBALI_LOADER:
                        switch (v.getId()) {
                            case R.id.ibGood:
                                goodClick(viewHolder, position);
                                if (!(storyInfos.get(position).getGoodURL().equals(""))) {
                                    vote(Constants.ZADOLBALI_LINK + storyInfos.get(position).getGoodURL());
                                }
                        }
                        Toast.makeText(context, "Ваш голос учтен", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.BASH_LOADER:
                        viewHolder.ibBad.setVisibility(View.INVISIBLE);
                        viewHolder.ibGood.setVisibility(View.INVISIBLE);
                        switch (v.getId()) {
                            case R.id.ibBad:
                                badClick(viewHolder, position);
                                postBashBadRate(storyInfos.get(position));
                                break;
                            case R.id.ibGood:
                                goodClick(viewHolder, position);
                                vote(storyInfos.get(position).getGoodURL());
                                postBashGoodRate(storyInfos.get(position));
                                break;
                        }
                        Toast.makeText(context, "Ваш голос учтен", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.KILL_ME_PLZ_LOADER:
                        viewHolder.ibBad.setVisibility(View.INVISIBLE);
                        viewHolder.ibGood.setVisibility(View.INVISIBLE);
                        switch ((v.getId())) {
                            case R.id.ibBad:
                                badClick(viewHolder, position);
                                vote(storyInfos.get(position).getBadURL());
                                break;
                            case R.id.ibGood:
                                goodClick(viewHolder, position);
                                vote(storyInfos.get(position).getGoodURL());
                                break;
                        }
                        Toast.makeText(context, "Спасибо за неравнодушие", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.SHORTIKI_LOADER:
                        viewHolder.ibBad.setVisibility(View.INVISIBLE);
                        viewHolder.ibGood.setVisibility(View.INVISIBLE);
                        switch (v.getId()) {
                            case R.id.ibBad:
                                badClick(viewHolder, position);
                                postShortikiRate(storyInfos.get(position));
                                break;
                            case R.id.ibGood:
                                goodClick(viewHolder, position);
                                vote(storyInfos.get(position).getGoodURL());
                                postShortikiRate(storyInfos.get(position));
                                break;
                        }
                        Toast.makeText(context, "Ваш голос учтен", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
    }

    private void goodClick(ViewHolder viewHolder, int position) {
        clickInfos.get(position).isGoodClicked = true;
        //проверка для башорга
        if (storyInfos.get(position).getRate().equals("???") || storyInfos.get(position).getRate().equals("..."))
            viewHolder.tvRate.setText("( ಠ_ಠ)");
        else {
            int rate = Integer.parseInt(storyInfos.get(position).getRate());
            rate++;
            viewHolder.tvRate.setText("" + rate);
            viewHolder.ibGood.setVisibility(View.INVISIBLE);
        }
    }

    private void badClick(ViewHolder viewHolder, int position) {
        clickInfos.get(position).isBadClicked = true;
        //проверка для башорга
        if (storyInfos.get(position).getRate().equals("???") || storyInfos.get(position).getRate().equals("..."))
            viewHolder.tvRate.setText("( ಠ_ಠ)");
        else {
            int rate = Integer.parseInt(storyInfos.get(position).getRate());
            rate--;
            viewHolder.tvRate.setText("" + rate);
            viewHolder.ibBad.setVisibility(View.INVISIBLE);
        }
    }

    public void vote(final String link) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Jsoup.connect(link).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void postBashBadRate(StoryInfo storyInfo) {
        postBashRate(storyInfo, true);
    }

    private void postBashGoodRate(StoryInfo storyInfo) {
        postBashRate(storyInfo, false);
    }

    private void postBashRate(final StoryInfo storyInfo, final boolean b){
         Thread thread = new Thread(new Runnable() {
              @Override
              public void run() {
                  String action;
                  if(b){
                      action = "sux";
                  }else {
                      action = "rulez";
                  }
                  String id = storyInfo.getStoryNumber().substring(1, storyInfo.getStoryNumber().length() - 1);
                  AndroidHttpClient httpClient;
                  httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"), context);
                  HttpPost httpPost = new HttpPost(String.format("http://bash.im/quote/%s/%s", id, "rulez"));
                  try {
                      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                      httpPost.addHeader("Referer","http://bash.im");
                      nameValuePairs.add(new BasicNameValuePair("quote", id));
                      nameValuePairs.add(new BasicNameValuePair("act", action));
                      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                      HttpResponse response = httpClient.execute(httpPost);
                      StatusLine s = response.getStatusLine();
                      System.out.print(s);
                  } catch (ClientProtocolException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }finally {
                      httpClient.close();
                  }
              }
          });
          thread.start();
      }

    private void postShortikiRate (final StoryInfo storyInfo){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String id = storyInfo.getStoryNumber().substring(1, storyInfo.getStoryNumber().length());
                AndroidHttpClient httpClient;
                httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"), context);
                HttpPost httpPost = new HttpPost("http://shortiki.com/ajax.php");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    httpPost.addHeader("Referer",storyInfo.getGoodURL());
                    nameValuePairs.add(new BasicNameValuePair("action", "doVote"));
                    nameValuePairs.add(new BasicNameValuePair("id", id));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost);
                    StatusLine s = response.getStatusLine();
                    System.out.print(s);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    httpClient.close();
                }
            }
        });
        thread.start();
    }


}