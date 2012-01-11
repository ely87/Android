package adapters;

import java.util.ArrayList;
import com.publizar.publizaraldia.R;
import domain.Promotion;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	private ArrayList<Promotion> promotions;

	public CalendarAdapter(Activity a,
			ArrayList<Promotion> promotions) {
		this.promotions = promotions;
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return promotions.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.calendar_items, null);

		}
		final Promotion promotion = promotions.get(position);

		TextView title = (TextView) vi
				.findViewById(R.id.promo_calendar_content);
		TextView days = (TextView) vi.findViewById(R.id.promo_calendar_days);

		title.setText(promotion.getTitle());
		days.setText(promotion.getDay());
		return vi;
	}
}