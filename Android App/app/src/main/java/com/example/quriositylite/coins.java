package com.example.quriositylite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class coins extends AppCompatActivity {

    // Variables:
    ArrayList<String> pending_transactions = new ArrayList<String>();
    private String p_transaction;
    ArrayList<String> on_spinner = new ArrayList<String>();
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

        // getting list of all pending transactions
//        pending_transactions = MainActivity.blockchain;
//        System.out.println(pending_transactions);

        Button mine_it = findViewById(R.id.btn_mine);
        Button go_back = findViewById(R.id.btn_back);

        System.out.println(pending_transactions);

        try {

            for (String str : Send_Coins.pendingT) {
                pending_transactions.add(str.split(" ")[2] +"->"+ str.split(" ")[3] +"/"+ str.split(" ")[4]+"/"+str.split(" ")[6]);
            }
        }
        catch (Exception e)
        {

        }
        final Spinner spin = findViewById(R.id.spin_1);
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pending_transactions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                p_transaction = Send_Coins.pendingT.get(i);
                index = i;
//                Toast.makeText(getApplicationContext(), p_transaction +" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent0 = new Intent(coins.this, MainActivity .class);
                startActivity(intent0);
            }
        });

        mine_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mine the coin:
                // adding block to the chain
                //String h = null;
                //                    try {
                //                        h = MainActivity.toHexString(MainActivity.getSHA(MainActivity.chain.get(MainActivity.idx).split(" ")[0]+" "+h+" Sanjeev "+peerSelected+" "+Amount+" "+(int) Math.round(1000 + Math.random()*4000)));
                //                        h = h.substring(0,20)+"...";
                //                    } catch (NoSuchAlgorithmException e) {
                //                        e.printStackTrace();
                //                    }

                if(index>0) {
                    System.out.println(index+"->"+Send_Coins.pendingT.get(index));
                    Send_Coins.pendingT.remove(index);
                    spin.setAdapter(dataAdapter);
                }
                else
                {
                    spin.setAdapter(null);
                }
                // set adapter to be updated

                String to = p_transaction.split(" ")[3];
                String amount = (Long.parseLong(p_transaction.split(" ")[4]) + Long.parseLong(p_transaction.split(" ")[6]))+"" ;
                String n = p_transaction.split(" ")[5];
                MainActivity.balance = MainActivity.balance + Long.parseLong(p_transaction.split(" ")[6]);// incrementing miner fee
                String h = null;
                try {
                    h = MainActivity.toHexString(MainActivity.getSHA(MainActivity.chain.get(MainActivity.idx).split(" ")[0]+" "+h+" "+MainActivity.Name+" "+to+" "+amount+" "+(int) Math.round(1000 + Math.random()*4000)));
                    h = h.substring(0,20)+"...";
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                MainActivity.blockchain.add("Block#"+(MainActivity.idx+2)+":\nPrevious Hash:"+MainActivity.chain.get(MainActivity.idx).split(" ")[1]+"\nHash:"+h+"\nFrom:"+MainActivity.Name+"\nTo:"+to+"\nAmount:"+amount+"\nNonce:"+n);
                MainActivity.chain.add(MainActivity.chain.get(MainActivity.idx).split(" ")[1]+" "+h+" "+MainActivity.Name+" "+to+" "+amount+" "+n);
                MainActivity.balance = MainActivity.balance + Long.parseLong(p_transaction.split(" ")[6]);
                MainActivity.idx++;

                Toast.makeText(coins.this, "Mined Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}