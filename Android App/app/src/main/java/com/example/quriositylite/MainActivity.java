package com.example.quriositylite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // bottom navigation bar
    SpaceNavigationView navigationView;

    // pop up dialog
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button next_demo, next_mine, next_display,next_balance,next_pay,next_pending,popup_finish; // next
    private Button back_demo, back_mine, back_display,back_balance,back_pay,back_pending,back_finish; // back

    //Varialbles:
    private long balance = 0;
    private int id = 0;
    private int nonce;
    private String prev_hash;
    private String curr_hash;
    private ArrayList<String> al_peers = new ArrayList<String>();
    private String To;
    private long start_amount;
    private long start_miner_reward;
    private ArrayList<String> al_pending_transactions = new ArrayList<String>();
    private String Transaction;
    private ArrayList<String> blockchain = new ArrayList<String>();
    private ArrayList<String> all_hashes = new ArrayList<String>();



    static boolean once;

    static // executing only once
    {
        once = false;
    }

    // widgets
    TextView main_balance_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(once == false)
        {
            start_demo();
            once = true;
        }

        main_balance_txt = findViewById(R.id.balance_txt);

        try {
            prev_hash = toHexString(getSHA("genesis0"));
            Toast.makeText(this, prev_hash, Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        Intent intent0 = new Intent(MainActivity.this, Send_Coins.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, Display_Ledger.class);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        Intent intent0 = new Intent(MainActivity.this, Send_Coins.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, Display_Ledger.class);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Wrong index, in default", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // computing hashes
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /*
    Order of Intro sequence:
    1.start demo
    2.mine
    3.display block details
    4.balance
    5.pay
    6.pending mine
    7.block chain display
     */
    // App Intro pop-up Activities
    public void start_demo() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View add_startup = getLayoutInflater().inflate(R.layout.popup_coin_start, null);
        next_demo = add_startup.findViewById(R.id.pop_start_id);
        dialogBuilder.setView(add_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        next_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_mine();
            }
        });

    } // end of start demo

    public void popup_mine() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View mine_startup = getLayoutInflater().inflate(R.layout.popup_mine, null);
        next_mine = mine_startup.findViewById(R.id.pop_next_mine);
        Button minecoins = mine_startup.findViewById(R.id.minecoins);
        dialogBuilder.setView(mine_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        next_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_display();
            }
        });

        minecoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balance = 100; // update balance
                Toast.makeText(MainActivity.this, "Balance Updated", Toast.LENGTH_SHORT).show();
                main_balance_txt.setText(balance+"");
                id++;
            }
        });

    } // end of popup mine

    public void popup_display() {

        //"Displaying Mined Block:\n\nPrevious Hash:\nHash:\nFrom:\nTo:\nAmount:"

        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_display_mined, null);
        next_display = display_startup.findViewById(R.id.pop_next_display);
        TextView disp_details = display_startup.findViewById(R.id.textView);
        try {
            nonce =(int) Math.round(1000 + Math.random()*3000);
            curr_hash = toHexString(getSHA(id+"rewardsanjeev100"+nonce));
            Toast.makeText(this, prev_hash, Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        disp_details.setText("Displaying Mined Block:\n\nPrevious Hash:"+prev_hash.substring(0,20)+"...\nHash:"+curr_hash.substring(0,20)+"...\nFrom:Reward\nTo:Sanjeev\nAmount:100\nNonce:"+nonce);
        dialogBuilder.setView(display_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        next_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_balance();
            }
        });



    }// end of popup display


    public void popup_balance() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_balance, null);
        next_balance = display_startup.findViewById(R.id.pop_next_balance);
        //android:text="Displaying Balance After First Mining\n\nBalance = "
        TextView tv = display_startup.findViewById(R.id.textView);
        tv.setText("Displaying Balance After First Mining\n\nBalance = "+balance);
        dialogBuilder.setView(display_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        next_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_pay();
            }
        });

    }

    public void popup_pay() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_pay, null);
        next_pay = display_startup.findViewById(R.id.pop_next_pay);

        al_peers.add("Abhishek");
        al_peers.add("Akash");
        al_peers.add("Devansh");

        Spinner spin = display_startup.findViewById(R.id.spin);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, al_peers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                To = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),To+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText Amount = display_startup.findViewById(R.id.et_amount);
        final EditText m_reward = display_startup.findViewById(R.id.et_miner_reward);


        dialogBuilder.setView(display_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        next_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                start_amount = Long.parseLong(Amount.getText().toString());
                start_miner_reward = Long.parseLong(m_reward.getText().toString());
                al_pending_transactions.add("Sanjeev "+To+" "+start_amount+" "+start_miner_reward);
                popup_pending();

            }
        });

    }

    public void popup_pending() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_pending_mining, null);
        next_pending = display_startup.findViewById(R.id.pop_next_pending);

        final Spinner spin = display_startup.findViewById(R.id.spin_pending);
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, al_pending_transactions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), Transaction +" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button mining = display_startup.findViewById(R.id.mining);


        dialogBuilder.setView(display_startup);
        dialog = dialogBuilder.create();
        dialog.show();

//        Toast.makeText(getApplicationContext(), al_pending_transactions.get(0), Toast.LENGTH_SHORT).show();

        next_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_blockchain();
            }
        });

        mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Transaction.equals("")) {
                    String str[] = Transaction.split(" ");
                    int money = Integer.parseInt(str[2]);
                    int miner_reward = Integer.parseInt(str[3]);

                    // subtracting from balance:
                    balance = balance - money - miner_reward;

                    // Adding miner-reward to balance
                    balance = balance + miner_reward;

                    // Updating balance on Text View:
                    main_balance_txt.setText(balance + "");

                    // removing the mined transaction from pending:
                    al_pending_transactions.clear();

                    //setting adapter
                    spin.setAdapter(dataAdapter);
                    Transaction = ""; // stopping from mining the transactions
                }
            }
        });

    }
        public void popup_blockchain () {

            dialogBuilder = new AlertDialog.Builder(this);
            final View display_startup = getLayoutInflater().inflate(R.layout.popup_blockchain, null);
            popup_finish = display_startup.findViewById(R.id.pop_finish);
            dialogBuilder.setView(display_startup);
            dialog = dialogBuilder.create();
            dialog.show();

            popup_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }


    }