package pl.planta.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.planta.R;

/**
 * Created by panko on 20.06.2016.
 */
public class CustomListTutorial extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final String[] itemDescription;
    private final Integer[] itemImage;
    private View mView;
    private TextView mName;
    private TextView mDesc;
    private ImageView mImage;

    public CustomListTutorial(Activity context, String[] itemName, String[] itemDescription, Integer[] itemImage) {
        super(context, R.layout.tutorial_list, itemName);

        this.context = context;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        mView = inflater.inflate(R.layout.tutorial_list, null, true);

        mName = (TextView) mView.findViewById(R.id.nameBuilding);
        mDesc = (TextView) mView.findViewById(R.id.descBuilding);
        mImage = (ImageView) mView.findViewById(R.id.imgBuilding);

        mName.setText(itemName[position]);
        mDesc.setText(itemDescription[position]);
        mImage.setImageResource(itemImage[position]);

        return mView;
    }
}
