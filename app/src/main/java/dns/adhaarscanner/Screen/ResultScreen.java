package dns.adhaarscanner.Screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import dns.adhaarscanner.R;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.XMLFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nayan on 12/31/16.
 */

public class ResultScreen extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recView;
    private static final String AADHAR_CARD_ROOT_KEY = "PrintLetterBarcodeData";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);
        findViewById(R.id.temp).setOnClickListener(this);
        initViews();
    }

    private void initViews() {
        recView = (RecyclerView) findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(new Adapter(getIntent().getStringExtra("data"), this));
    }

    @Override
    public void onClick(View v) {
        initViews();
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        List<String> content;
        LayoutInflater inflater;

        Adapter(String content, Context context) {
            try {
                JSONObject resJSON = XML.toJSONObject(content);
                JSONObject info = resJSON.getJSONObject(AADHAR_CARD_ROOT_KEY);
                this.content = new ArrayList<>(info.length());
                Iterator<String> iterator = info.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = info.getString(key);
                    this.content.add(key + ":" + value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.rec_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(content.get(position));
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.text_view);
            }
        }
    }
}
