package com.example.quriositylite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class Display_Ledger extends AppCompatActivity {

    SpaceNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ledger);

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
                finish();
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