package mindon.idea.wearable.demo;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import android.os.Bundle;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TheListAdapter implements ListAdapter {
    private String TAG = "MindonWearDemo";

    private class Item {
        String title;
        String name;
        String summary;
        Bitmap image;
		JSONObject detail;
    }

    private List<Item> mItems = new ArrayList<Item>();
    private Context mContext;
    private DataSetObserver mObserver;

	private String _LIST_FILE = "list.json";
	private String _LIST_KEY = "list";
	private String _FIELD_NAME = "name";
	private String _FIELD_TITLE = "title";
	private String _FIELD_IMAGE = "src";
	private String _FIELD_SUMMARY = "summary";
	private String _FIELD_STEP_TITLE = "step-title";
	private String _FIELD_STEPS = "steps";
	private String _FIELD_DELETED = "deleted";
	private String _FIELD_CONTENT = "content";
	private String _FIELD_ONGOING = "ongoing";
	private String _FIELD_PRIORITY = "priority";
	private String _FIELD_ONCE = "once";
	private String _FIELD_WHEN = "when";
	private String _FIELD_NUM = "num";
	private String _FIELD_ACTION_FULL = "action-full";
	private String _FIELD_ACTION_FULL_MESSAGE = "action-full-message";
	private String _FIELD_ACTION_REPLY = "action-reply";
	private String _FIELD_ACTION_REPLY_MESSAGE = "action-reply-message";


    public TheListAdapter(Context context) {
        mContext = context;
        loadTheList();
    }

    private void loadTheList() {
        JSONObject jsonObject = AssetUtils.loadJSONAsset(mContext, _LIST_FILE);
       if (jsonObject != null) {
            List<Item> items = parseJson(jsonObject);
			Log.d(TAG, "items"+ items);
            appendItemsToList(items);
        }
    }

    private List<Item> parseJson(JSONObject json) {
        List<Item> result = new ArrayList<Item>();
        try {
            JSONArray items = json.getJSONArray(_LIST_KEY);
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Item parsed = new Item();
				parsed.detail = item;
                parsed.name = item.getString(_FIELD_NAME);
                parsed.title = item.getString(_FIELD_TITLE);
                if (item.has(_FIELD_IMAGE)) {
                    String imageFile = item.getString(_FIELD_IMAGE);
                    parsed.image = AssetUtils.loadBitmapAsset(mContext, imageFile);
                }
				parsed.summary = item.has(_FIELD_SUMMARY) ? item.getString(_FIELD_SUMMARY) : "";
                result.add(parsed);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse list: " + e);
        }
        return result;
    }

    private void appendItemsToList(List<Item> items) {
        mItems.addAll(items);
        if (mObserver != null) {
            mObserver.onChanged();
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inf = LayoutInflater.from(mContext);
            view = inf.inflate(R.layout.list_item, null);
        }
        Item item = (Item) getItem(position);
        TextView titleView = (TextView) view.findViewById(R.id.textTitle);
        TextView summaryView = (TextView) view.findViewById(R.id.textSummary);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);

        titleView.setText(item.title);
        summaryView.setText(item.summary);
        if (item.image != null) {
            iv.setImageBitmap(item.image);
        } else {
            iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.none));
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mObserver = observer;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObserver = null;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public String getItemName(int position) {
        return mItems.get(position).name;
    }

	public Bundle getItemBundle(int position) {
		Item item = mItems.get(position);
        Bundle bundle = new Bundle();
		try {
			JSONObject detail = (JSONObject) item.detail;
			if( detail.has(_FIELD_TITLE) ) {
				bundle.putString( _FIELD_TITLE, detail.getString(_FIELD_TITLE));
			}
			if( detail.has(_FIELD_IMAGE) ) {
				bundle.putString( _FIELD_IMAGE, detail.getString(_FIELD_IMAGE));
			}
			if( detail.has(_FIELD_SUMMARY) ) {
				bundle.putString( _FIELD_SUMMARY, detail.getString(_FIELD_SUMMARY));
			}
			if( detail.has(_FIELD_DELETED) ) {
				bundle.putString( _FIELD_DELETED, detail.getString(_FIELD_DELETED));
			}
			if( detail.has(_FIELD_CONTENT) ) {
				bundle.putString( _FIELD_CONTENT, detail.getString(_FIELD_CONTENT));
			}
			if( detail.has(_FIELD_ACTION_FULL) ) {
				bundle.putString( _FIELD_ACTION_FULL, detail.getString(_FIELD_ACTION_FULL));
			}
			if( detail.has(_FIELD_ACTION_FULL_MESSAGE) ) {
				bundle.putString( _FIELD_ACTION_FULL_MESSAGE, detail.getString(_FIELD_ACTION_FULL_MESSAGE));
			}
			if( detail.has(_FIELD_ACTION_REPLY) ) {
				bundle.putString( _FIELD_ACTION_REPLY, detail.getString(_FIELD_ACTION_REPLY));
			}
			if( detail.has(_FIELD_ACTION_REPLY_MESSAGE) ) {
				bundle.putString( _FIELD_ACTION_REPLY_MESSAGE, detail.getString(_FIELD_ACTION_REPLY_MESSAGE));
			}
			if( detail.has(_FIELD_STEP_TITLE) ) {
				bundle.putString( _FIELD_STEP_TITLE, detail.getString(_FIELD_STEP_TITLE));
			}
			if( detail.has(_FIELD_ONGOING) ) {
				bundle.putBoolean( _FIELD_ONGOING, true);
			}
			if( detail.has(_FIELD_ONCE) ) {
				bundle.putBoolean( _FIELD_ONGOING, true);
			}
			if( detail.has(_FIELD_PRIORITY) ) {
				bundle.putInt( _FIELD_PRIORITY, detail.getInt(_FIELD_PRIORITY));
			}
			if( detail.has(_FIELD_NUM) ) {
				bundle.putInt( _FIELD_NUM, detail.getInt(_FIELD_NUM));
			}
			if( detail.has(_FIELD_WHEN) ) {
				bundle.putInt( _FIELD_WHEN, detail.getInt(_FIELD_WHEN));
			}
			if (detail.has(_FIELD_STEPS)) {
				JSONArray steps = detail.getJSONArray(_FIELD_STEPS);
				ArrayList<Parcelable> stepBundles = new ArrayList<Parcelable>(steps.length());
				for (int i = 0; i < steps.length(); i++) {
					JSONObject step = steps.getJSONObject(i);
					Bundle stepBundle = new Bundle();
					stepBundle.putString(_FIELD_TITLE, step.getString(_FIELD_TITLE));
					if( step.has(_FIELD_SUMMARY) ) {
						stepBundle.putString(_FIELD_SUMMARY, step.getString(_FIELD_SUMMARY));
					}
					if( step.has(_FIELD_IMAGE) ) {
						stepBundle.putString(_FIELD_IMAGE, step.getString(_FIELD_IMAGE));
					}
					stepBundles.add(stepBundle);
				}
	            bundle.putParcelableArrayList(_FIELD_STEPS, stepBundles);

			}
	    } catch (JSONException e) {
			Log.e(TAG, "Failed to parse JSON " + e);
		}
        return bundle;
	}
}
