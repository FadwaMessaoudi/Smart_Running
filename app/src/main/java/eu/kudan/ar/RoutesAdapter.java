package eu.kudan.ar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import eu.kudan.ar.model.Route;

public class RoutesAdapter extends ArrayAdapter<Route> {
    public RoutesAdapter(Context context, ArrayList<Route> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Route route = getItem(position);
        final Context c=this.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }
        // Lookup view for data population
        LinearLayout rLayout=(LinearLayout) convertView.findViewById(R.id.rLayout);
        rLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
               // Route route = getItem(position);
                // Do what you want here...
                Intent intent = new Intent(getContext(), RunActivity.class);
                Bundle b = new Bundle();


                route.getMarkers();
                b.putSerializable("id", route.getId());
                b.putSerializable("difficulty", route.getDifficulty());
                b.putSerializable("title", route.getTitle());
                b.putSerializable("mode", route.getMode());
                b.putSerializable("description", route.getDescription());
                b.putSerializable("markers",route.getMarkers());

                intent.putExtras(b);
                c.startActivity(intent);



            }
        });
        ImageView rImage=(ImageView) convertView.findViewById(R.id.rImage);
        TextView rName = (TextView) convertView.findViewById(R.id.rName);
        TextView rDes = (TextView) convertView.findViewById(R.id.rDes);
        TextView rDiff = (TextView) convertView.findViewById(R.id.rDiff);
        TextView rNbBalise = (TextView) convertView.findViewById(R.id.rNbBalise);
        // Populate the data into the template view using the data object
        rImage.setImageBitmap(route.getThumbnail());
        rName.setText("Course: "+route.getTitle());
        rDes.setText("Description: "+route.getDescription());
        rDiff.setText("Difficulté: "+route.getDifficulty()+"/5");
        rNbBalise.setText("Nombre de balises: "+route.getMarkers().size());
        // Return the completed view to render on screen
        return convertView;
    }
}
