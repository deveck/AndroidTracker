package org.cw;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.cw.dataitems.TrackFile;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TrackListDataAdapter extends ArrayAdapter<TrackFile> 
{
	/** Defines the different available ListItem view types	 */
	public enum ViewTypeEnum
	{
		/** Default view, no icon */
		Default,
		
		/** View with rotating circle to the left of the icon */
		Working
	}
	
	/** Saves the views that belong to specific items */
	private Dictionary<Integer, ViewTypeEnum> _itemViews = new Hashtable<Integer, ViewTypeEnum>();

	private Activity _context;
	
	public void setViewType(int position, ViewTypeEnum newViewType)
	{
		_itemViews.put(position, newViewType);
		super.notifyDataSetChanged();
	}
	
	
	public TrackListDataAdapter(Activity context, List<TrackFile> objects) 
	{
		super(context, android.R.layout.simple_list_item_multiple_choice, objects);
		
		_context = context;
	}
	

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(_itemViews.get(position) == null || _itemViews.get(position) == ViewTypeEnum.Default)
			return super.getView(position, convertView, parent);
		
		View row=convertView;
		

		LayoutInflater inflater = _context.getLayoutInflater();
			
		row = inflater.inflate(R.layout.row_with_icon, parent, false);
		row.setTag(R.id.icon, row.findViewById(R.id.icon));
		row.setTag(R.id.label, row.findViewById(R.id.label));


		TextView label = (TextView)row.getTag(R.id.label);
		final ImageView icon = (ImageView)row.getTag(R.id.icon);
		
		if(_itemViews.get(position) != null && _itemViews.get(position) == ViewTypeEnum.Working)
			icon.setImageResource(R.anim.working);
		else
			icon.setImageDrawable(null);

		label.setText(getItem(position).toString());
		
		
		//Hack the planet!!
		//
		//according to developer forum, the animation cannot be started immediately,
		//so we just add a delay and execute the start in the main thread
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress();
				
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Void... values) {
				((AnimationDrawable)icon.getDrawable()).start();
			}
			
		}.execute();
		
		
		return row;		
	}
	
	
	@Override
	public int getItemViewType(int position) {
		
		if(_itemViews.get(position) != null && _itemViews.get(position) == ViewTypeEnum.Working)
			return 1;
		else
			return 0;
	}

	@Override
	public int getViewTypeCount() 
	{
		return 2;
	}


}
