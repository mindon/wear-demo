package mindon.idea.wearable.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private static final String TAG = "MindonWearDemo";
    private TheListAdapter mAdapter;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG , "onListItemClick " + position);
        }

		Intent intent = new Intent(getApplicationContext(), WearService.class);
        intent.setAction(WearService.NOTIFY);

        intent.putExtra(WearService.EXTRA, mAdapter.getItemBundle(position));
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.list_content);

        mAdapter = new TheListAdapter(this);
        setListAdapter(mAdapter);
    }
}
