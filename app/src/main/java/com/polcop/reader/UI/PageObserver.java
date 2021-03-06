package com.polcop.reader.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.polcop.reader.Constants;
import com.polcop.reader.MainActivity;
import com.polcop.reader.PageInfo;
import com.polcop.reader.R;
import com.polcop.reader.Utils;
import com.polcop.reader.fragments.Feed;

import java.util.Calendar;

/**
 * Created by oleg on 29.09.14.
 */
public class PageObserver {

    private  Context context;


    public PageObserver() {
        context = PageInfo.getInstance().getContext();
    }

   PageSelectionFragment pageSelectionFragment;
   DatePickerFragment datePickerFragment;

    public void switchPage() {
        switch (Utils.getLoaderId()){
            case Constants.ZADOLBALI_LOADER:
                if(PageInfo.getInstance().getCurrentPage().equals(Constants.ZADOLBALI_LINK)){
                    selectPageByDate();
                }else{
                    selectPageByNumber();
                }
                break;
            case Constants.IT_HAPPENS_LOADER:
                selectPageByNumber();
                break;
            case Constants.BASH_LOADER:
                if(PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_ABYSS_BEST)){
                    selectPageByDate();
                }else{
                      selectPageByNumber();
                 }
                 break;
            case Constants.KILL_ME_PLZ_LOADER:
                selectPageByNumber();
                break;
            case Constants.SHORTIKI_LOADER:
                selectPageByNumber();
                break;
        }

    }

    private void selectPageByNumber(){
        if(pageSelectionFragment==null){
            pageSelectionFragment = new PageSelectionFragment();
        }
        pageSelectionFragment.show(((MainActivity)context).getSupportFragmentManager(),null);
    }

    private void selectPageByDate(){
        if(datePickerFragment==null){
            datePickerFragment = new DatePickerFragment();
        }
        datePickerFragment.show(((MainActivity)context).getSupportFragmentManager(),null);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        boolean twiceCalledMark = false;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.getDatePicker().setMinDate(Constants.ZADDOLBALI_FIRST_STORY_DATE);
            }
            setCancelable(false);
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(twiceCalledMark){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    loadNewPage(createDateForLink(year, monthOfYear, dayOfMonth));
                }else{
                    if(isCorrectDate(year, monthOfYear, dayOfMonth)){
                        loadNewPage(createDateForLink(year, monthOfYear, dayOfMonth));
                    }else{
                        Toast.makeText(getActivity(), R.string.page_select_exception,Toast.LENGTH_SHORT).show();
                    }
                }
                twiceCalledMark = false;
            }


        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            twiceCalledMark = true;
            super.onCreate(savedInstanceState);
        }

        private void loadNewPage(String newDate){
            Feed feed = new Feed();
            Bundle arg = new Bundle();
            Utils.clearBackStack();
            PageInfo.getInstance().setStoryInfos(null);
            if(Utils.getLoaderId()==Constants.ZADOLBALI_LOADER){
                arg.putString(Constants.LINK, Constants.ZADOLBALI_LINK + "/" + newDate);
            }
            if(Utils.getLoaderId()==Constants.BASH_LOADER){
                arg.putString(Constants.LINK, Constants.BASH_ABYSS_BEST + "/" + newDate);
            }
            arg.putInt(Constants.LOADER_ID, Utils.getLoaderId());
            feed.setArguments(arg);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, feed, Constants.FEED_TAG)
                    .commit();
        }

        private boolean isCorrectDate (int year, int monthOfYear, int dayOfMonth){
            //todo проверку для баша, что прошло не больше года
            if(2009 > year){
                return false;
            }else
            if(9>monthOfYear+1){
                return false;
            }else
            if(8>dayOfMonth){
                return false;
            }
            String maxDate = PageInfo.getInstance().getMaxPageNumber();
            int yearMax = Integer.parseInt(maxDate.substring(0,4));
            int monthMax = Integer.parseInt(maxDate.substring(4,6));
            int dayMax = Integer.parseInt(maxDate.substring(6));
            if(year > yearMax){
                return false;
            }else
            if(monthOfYear+1>monthMax){
                return false;
            }else
            if(dayOfMonth>dayMax){
                return false;
            }
            return true;
        }

        private String createDateForLink(int year, int monthOfYear, int dayOfMonth){
            String day = String.valueOf(dayOfMonth);
            String month = String.valueOf(monthOfYear+1);
            if(day.length()==1){
                day = "0"+dayOfMonth;
            }
            if(month.length()==1){
                monthOfYear++;
                month = "0"+monthOfYear;
            }
            return year+month+day;
        }
    }

    public static class PageSelectionFragment extends DialogFragment{

        private static int EDITTEXT_ID = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final EditText pageField = new EditText(getActivity());
            pageField.setInputType(InputType.TYPE_CLASS_NUMBER);
            pageField.setId(EDITTEXT_ID);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.page_select_title).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newPage = pageField.getText().toString();
                    try {
                        int page = Integer.parseInt(newPage);
                        if(page>Integer.parseInt(PageInfo.getInstance().getMaxPageNumber())||page<=0){
                            Toast.makeText(getActivity(), R.string.page_select_exception,Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getActivity(), R.string.page_select_exception,Toast.LENGTH_SHORT).show();
                        pageField.setText("");
                        return;
                    }
                    loadNewPage(newPage);
                }
            })
                    .setMessage(getString(R.string.page_select_message)
                            + PageInfo.getInstance().
                            getMaxPageNumber())
                    .setView(pageField);

            setCancelable(false);

            return builder.create();
        }

        private void loadNewPage(String newPage){
            Feed feed = new Feed();
            Bundle arg = new Bundle();
            Utils.clearBackStack();
            PageInfo.getInstance().setStoryInfos(null);
            switch (Utils.getLoaderId()){
                case Constants.ZADOLBALI_LOADER:
                    arg.putString(Constants.LINK, PageInfo.getInstance().getCurrentPage() + "/" + newPage);
                    break;
                case Constants.IT_HAPPENS_LOADER:
                    if (Constants.IT_HAPPENS_LINK.equals(PageInfo.getInstance().getCurrentPage())){
                        arg.putString(Constants.LINK, Constants.IT_HAPPENS_PAGE + newPage);
                    }else{
                        arg.putString(Constants.LINK, PageInfo.getInstance().getCurrentPage() + "/" + newPage);
                    }
                    break;
                case Constants.BASH_LOADER:
                    if(Constants.BASH_LINK.equals(PageInfo.getInstance().getCurrentPage())){
                        arg.putString(Constants.LINK, Constants.BASH_LINK + "/" + newPage);
                    }else{
                        arg.putString(Constants.LINK, Constants.BASH_BY_RATING + "/" + newPage);
                    }
                    break;
                case Constants.KILL_ME_PLZ_LOADER:
                    if(Constants.KILL_ME_PLZ_LINK.equals((PageInfo.getInstance().getCurrentPage()))){
                        arg.putString(Constants.LINK, Constants.KILL_ME_PLZ_PAGE + newPage);
                    }else{
                        arg.putString(Constants.LINK,PageInfo.getInstance().getCurrentPage() + "/" + newPage);
                    }
                    break;
                case Constants.SHORTIKI_LOADER:
                    if(Constants.SHORTIKI_LINK.equals(PageInfo.getInstance().getCurrentPage())){
                        arg.putString(Constants.LINK, Constants.SHORTIKI_PAGER + newPage);
                    }else{
                        arg.putString(Constants.LINK, Constants.SHORTIKI_PAGER_BY_RATE + newPage);
                    }
                    break;
            }
            arg.putInt(Constants.LOADER_ID,Utils.getLoaderId());
            feed.setArguments(arg);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, feed, Constants.FEED_TAG)
                    .commit();
        }

    }

}
