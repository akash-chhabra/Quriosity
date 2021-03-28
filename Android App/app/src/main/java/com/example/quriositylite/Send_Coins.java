package com.example.quriositylite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Send_Coins extends AppCompatActivity {

    //Variables
    ArrayList<String> peers = new ArrayList<String>();
    private String peerSelected;
    private long Amount;
    private long MinerFee;
    public static ArrayList<String> pendingT = new ArrayList<String>();

    SpaceNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coins);

        peers.add("Akash");
        peers.add("Abhishek");
        peers.add("Devansh");

        Spinner spin = findViewById(R.id.coin_spin);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, peers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                peerSelected = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getApplicationContext(),peerSelected+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText et_amount = findViewById(R.id.et_amount);
        final EditText et_miner_fee = findViewById(R.id.miner_fee);

        Button pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_amount.getText().toString().isEmpty()) {

                    et_amount.setError("Name can't contain a Number!");
                    et_amount.requestFocus();
                    et_amount.setText("");
                    return;

                } else if (et_miner_fee.getText().toString().isEmpty()) {

                    et_miner_fee.setError("Name can't contain a Number!");
                    et_miner_fee.requestFocus();
                    et_miner_fee.setText("");
                    return;

                } else {
                    Amount = Long.parseLong(et_amount.getText().toString());
                    MinerFee = Long.parseLong(et_miner_fee.getText().toString());

                    if (MainActivity.balance - (Amount + MinerFee) < 0) {
                        Toast.makeText(Send_Coins.this, "Insufficient Balance, Try Again", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        String h = null;
                        try {
                            h = MainActivity.toHexString(MainActivity.getSHA(MainActivity.chain.get(MainActivity.idx).split(" ")[0] + " " + h + " " +MainActivity.Name+ " " + peerSelected + " " + Amount + " " + (int) Math.round(1000 + Math.random() * 4000)));
                            h = h.substring(0, 20) + "...";
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }


                        int n = (int) Math.round(1000 + Math.random() * 4000);
                        //"Block#2:\nPrevious Hash:"+al.get(0).split(" ")[1]+"\nHash:"+al.get(1).split(" ")[1]+"\nFrom:"+al.get(1).split(" ")[2]+"\nTo:"+al.get(1).split(" ")[3]+"\nAmount:"+al.get(1).split(" ")[4]+"\nNonce:"+(int) Math.round(1000 + Math.random()*3000)
                        System.out.println(MainActivity.idx);
//                  MainActivity.blockchain.add("Block#"+(MainActivity.idx+2)+":\nPrevious Hash:"+MainActivity.chain.get(MainActivity.idx).split(" ")[1]+"\nHash:"+h+"\nFrom:"+"Sanjeev\nTo:"+peerSelected+"\nAmount:"+Amount+"\nNonce:"+n);
//                    MainActivity.chain.add(MainActivity.chain.get(MainActivity.idx).split(" ")[1]+" "+h+" Sanjeev "+peerSelected+" "+Amount+" "+n);
//                    MainActivity.idx++;
//                    System.out.println(MainActivity.chain.get(MainActivity.idx).split(" ")[1]+" "+h+" Sanjeev "+peerSelected+" "+Amount+" "+n);
                        try {
                            pendingT.add(MainActivity.chain.get(MainActivity.idx).split(" ")[1] + " " + h + " " +MainActivity.Name+ " " +  peerSelected + " " + Amount + " " + n+" "+MinerFee);
                        } catch (Exception e) {

                        }
                        MainActivity.balance = MainActivity.balance - (Amount + MinerFee);
                        Toast.makeText(Send_Coins.this, "Transaction Added to Pending", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Bottom navigation
        navigationView = findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.send_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.list_icon));

        // navigating through the app:
        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent intent0 = new Intent(Send_Coins.this, MainActivity.class);
                finish();
                startActivity(intent0);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        //Do Nothing
                        break;
                    case 1:
                        Intent intent1 = new Intent(Send_Coins.this, Display_Ledger.class);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(Send_Coins.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        //Do Nothing
                        break;
                    case 1:
                        Intent intent1 = new Intent(Send_Coins.this, Display_Ledger.class);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(Send_Coins.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}