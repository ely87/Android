package adapters;

import com.publizar.publizaraldia.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListImageAdapter extends ArrayAdapter<Object> {
	Activity context;
	String[] items;
	boolean[] arrows;
	int layoutId;
	int textId;
	int numberId;
	int imageId;

	public ListImageAdapter(Activity context, int layoutId, int textId,
			int imageId, String[] items, boolean[] valor) {
		super(context, layoutId);
		this.context = context;
		this.items = items;
		this.arrows = valor;
		this.layoutId = layoutId;
		this.textId = textId;
		this.imageId = imageId;
	}

	public View getView(int pos, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(layoutId, null);
		TextView label = (TextView) row.findViewById(textId);
		label.setText(items[pos]);

		if (arrows[pos]) {
			ImageView icon = (ImageView) row.findViewById(imageId);
			icon.setImageResource(R.drawable.right_arrow);
		}

		return (row);
	}
}
