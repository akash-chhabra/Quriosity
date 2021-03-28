package com.example.quriositylite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Display_Ledger extends AppCompatActivity {

    SpaceNavigationView navigationView;
    private ArrayList<String> blockchain;
    private String genBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ledger);

//         calling variables from MainActivity
        MainActivity ma = new MainActivity();
//        blockchain = (ArrayList<String>) getIntent().getSerializableExtra("b_c");
//        genBlock = (String) getIntent().getSerializableExtra("genblock");
        blockchain = MainActivity.blockchain;
        genBlock = MainActivity.gen_block;

        TextView genesis = findViewById(R.id.genesisblock);
        genesis.setText(genBlock);

        try {

            ListView listView = findViewById(R.id.listv);
            myCustomAdapter mca = new myCustomAdapter(Display_Ledger.this, blockchain);
            listView.setAdapter(mca);
        }
        catch (Exception e)
        {
            System.out.println("hi");
        }

        // Bottom navigation
        navigationView = findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.send_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.list_icon));

        // navigating through the app:
        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent intent0 = new Intent(Display_Ledger.this, MainActivity.class);
                startActivity(intent0);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        Intent intent1 = new Intent(Display_Ledger.this, Send_Coins.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        // Do Nothing
                        break;
                    default:
                        Toast.makeText(Display_Ledger.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        Intent intent1 = new Intent(Display_Ledger.this, Send_Coins.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        // Do Nothing
                        break;
                    default:
                        Toast.makeText(Display_Ledger.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

class myCustomAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    ArrayList<String> blk_chain;

    myCustomAdapter(Activity c,ArrayList<String> block_chain)
    {
        super(c, R.layout.recycler_view,block_chain);
        this.context = c;
        blk_chain = block_chain;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View row = layoutInflater.inflate(R.layout.recycler_view,null,true);

        // refrencing from layout
//        TextView tv_cat = row.findViewById(R.id.category_display);
//        TextView tv_amt = row.findViewById(R.id.amount_display);
//
//        home_screen hs = new home_screen();
//        System.out.println(catg+" "+amt);
//        tv_cat.setText(catg.get(position));
//        tv_amt.setText(amt.get(position));

        // Adding elements from arraylist:
            System.out.println(blk_chain);
            TextView tv = row.findViewById(R.id.txtView);
            tv.setText(blk_chain.get(position));


//        return super.getView(position, convertView, parent);
        return row;
    }
}