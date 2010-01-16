package org.cw;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.cw.dataitems.TrackFile;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
		ImageView icon = (ImageView)row.getTag(R.id.icon);
		
		if(_itemViews.get(position) != null && _itemViews.get(position) == ViewTypeEnum.Working)
			icon.setImageResource(R.anim.working);
		else
			icon.setImageDrawable(null);

		label.setText(getItem(position).toString());
		
		return row;		
	}
	
	
}
