package com.polcop.reader;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by oleg on 18.09.14.
 */
public class PageSelectionFragment extends DialogFragment{

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
                }catch (NumberFormatException e){
                    Toast.makeText(getActivity(), R.string.page_select_exception,Toast.LENGTH_SHORT).show();
                    pageField.setText("");
                }
                Utils.clearBackStack(getActivity());
                //иначе добавятся в конец
                PageInfo.getInstance().setStoryInfos(null);
                Feed feed = new Feed();
                Bundle arg = new Bundle();
                arg.putString(Constants.LINK, Constants.IT_HAPPENS_PAGE+newPage);
                arg.putInt(Constants.LOADER_ID,Utils.getLoaderId(getActivity()));
                feed.setArguments(arg);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, feed, Constants.FEED_TAG)
                        .commit();
                }
            }

            ).

            setMessage(getString(R.string.page_select_message)

                            + PageInfo.getInstance().

                            getMaxPageNumber()

            ).

            setView(pageField);

            setCancelable(false);

            return builder.create();
        }

    }
