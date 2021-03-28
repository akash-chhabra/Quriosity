package com.example.quriositylite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.w3c.dom.Text;

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
    private Button back_demo, back_mine, back_display,back_balance,back_pay,back_pending,back_finish,start; // back
    TextView NAME;
    TextView EMAIL;

    //Varialbles:
    public static long balance = 0;
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
    public static ArrayList<String> blockchain = new ArrayList<String>();
    private ArrayList<String> all_hashes = new ArrayList<String>();
    private ArrayList<String> all_nonce = new ArrayList<String>();
    public static String gen_block;
    private ArrayList<String> al = new ArrayList<String>();
    public static int idx=0;
    public static ArrayList<String> chain = new ArrayList<String>();
    public static String Name;
    public static String Email;

    static boolean once;

    static // executing only once
    {
        once = false;
    }

    // widgets
    TextView main_balance_txt;
    EditText et_name;
    EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(once == false)
        {
            get_details();
            once = true;
        }

        main_balance_txt = findViewById(R.id.balance_txt);
        NAME = findViewById(R.id.disp_name);
        EMAIL = findViewById(R.id.disp_email);

        try {
            prev_hash = toHexString(getSHA("genesis block 0"));
//            Toast.makeText(this, prev_hash, Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        main_balance_txt.setText(balance+"");
        NAME.setText(Name);
        EMAIL.setText(Email);

        // mine coins
        LottieAnimationView lottieAnimationView_mine = findViewById(R.id.mine_the_coin);

        lottieAnimationView_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent0 = new Intent(MainActivity.this, coins.class);
                startActivity(intent0);
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
                        intent1.putExtra("b_c", blockchain);
                        intent1.putExtra("genblock",gen_block);
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
                        intent1.putExtra("b_c", blockchain);
                        intent1.putExtra("genblock",gen_block);
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
    public void get_details() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View add_startup = getLayoutInflater().inflate(R.layout.popup_details, null);
        start = add_startup.findViewById(R.id.btn_details);
        et_name = add_startup.findViewById(R.id.et_name);
        et_email = add_startup.findViewById(R.id.et_email);

        dialogBuilder.setView(add_startup);
        dialog = dialogBuilder.create();
        dialog.show();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String na = et_name.getText().toString();
                String em = et_email.getText().toString();
                if(na.isEmpty())
                {
                    et_name.setError("Name Cannot be Empty");
                    et_name.requestFocus();
                    et_name.setText("");
                    return;
                }
                if(em.isEmpty())
                {
                    et_email.setError("Email Cannot be Empty");
                    et_email.requestFocus();
                    et_email.setText("");
                    return;
                }
                else if(em.contains("[0-9]+"))
                {
                    et_name.setError("Name can't contain a Number!");
                    et_name.requestFocus();
                    et_name.setText("");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    et_email.setError("Enter a valid Email!");
                    et_email.requestFocus();
                    et_email.setText("");
                    return;
                }
                else {
                    Name = na;
                    Email = em;
                }
                NAME.setText(na);
                EMAIL.setText(em);
                dialog.dismiss();
                start_demo();
            }
        });

    } // end of start demo

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
        back_mine = mine_startup.findViewById(R.id.pop_back_mine);
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

        back_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                start_demo();
            }
        });

        minecoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balance = 500; // update balance
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
        back_display = display_startup.findViewById(R.id.pop_back_display);
        try {
            nonce =(int) Math.round(1000 + Math.random()*3000);
            curr_hash = toHexString(getSHA(id+"reward"+MainActivity.Name+"100"+nonce));
//            Toast.makeText(this, prev_hash, Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        disp_details.setText("Displaying Mined Block:\n\nPrevious Hash:"+prev_hash.substring(0,20)+"...\nHash:"+curr_hash.substring(0,20)+"...\nFrom:Reward\nTo:"+MainActivity.Name+"\nAmount:500\nNonce:"+nonce+"\n\nWe Have Added some balance! as a welcome gift");
        al.add(prev_hash.substring(0,20)+"... "+curr_hash.substring(0,20)+"... Reward "+MainActivity.Name+" 100 "+nonce);
        all_hashes.add(prev_hash);
        all_nonce.add(nonce+"");
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

        back_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_mine();
            }
        });



    }// end of popup display


    public void popup_balance() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_balance, null);
        next_balance = display_startup.findViewById(R.id.pop_next_balance);
        back_balance = display_startup.findViewById(R.id.pop_back_balance);
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

        back_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_display();
            }
        });

    }

    public void popup_pay() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_pay, null);
        next_pay = display_startup.findViewById(R.id.pop_next_pay);
        back_pay = display_startup.findViewById(R.id.pop_back_pay);

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
//                Toast.makeText(getApplicationContext(),To+" selected",Toast.LENGTH_LONG).show();
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

                if(Amount.getText().toString().isEmpty())
                {
                    Amount.setError("Email Cannot be Empty");
                    Amount.requestFocus();
                    Amount.setText("");
                    return;
                }
                else if(m_reward.getText().toString().isEmpty())
                {
                    m_reward.setError("Name can't contain a Number!");
                    m_reward.requestFocus();
                    m_reward.setText("");
                    return;
                }
                else {
                    start_amount = Long.parseLong(Amount.getText().toString());
                    start_miner_reward = Long.parseLong(m_reward.getText().toString());
                    al_pending_transactions.add(MainActivity.Name+" "+ To + " " + start_amount + " " + start_miner_reward);
                    try {
                        String new_hash = toHexString(getSHA(al_pending_transactions.get(0)));
                        al.add(al.get(0).split(" ")[0] + " " + new_hash.substring(0, 20) + "... "+MainActivity.Name+" "+ To + " " + start_amount + " " + (int) Math.round(1000 + Math.random() * 3000));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    popup_pending();
                }
            }
        });

        back_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_balance();
            }
        });

    }

    public void popup_pending() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View display_startup = getLayoutInflater().inflate(R.layout.popup_pending_mining, null);
        next_pending = display_startup.findViewById(R.id.pop_next_pending);
        back_pending = display_startup.findViewById(R.id.pop_back_pending);

        final Spinner spin = display_startup.findViewById(R.id.spin_pending);
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, al_pending_transactions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getApplicationContext(), Transaction +" selected",Toast.LENGTH_LONG).show();
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
                    System.out.println(str[2]+" "+str[3]);
                    int money = Integer.parseInt(str[str.length-2]);
                    int miner_reward = Integer.parseInt(str[str.length-1]);

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

        back_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                popup_pay();
            }
        });

    }
        public void popup_blockchain () {

            dialogBuilder = new AlertDialog.Builder(this);
            final View display_startup = getLayoutInflater().inflate(R.layout.popup_blockchain, null);
            popup_finish = display_startup.findViewById(R.id.pop_finish);
            TextView tv1 = display_startup.findViewById(R.id.txt_chain1);
            TextView tv2 = display_startup.findViewById(R.id.txt_chain2);
            TextView tv3 = display_startup.findViewById(R.id.txt_chain3);
            back_finish = display_startup.findViewById(R.id.pop_blockchain_back);

            // Adding Transaction Details to Blocks:
//            tv1.setText("Genesis Block:\nPrevious Hash:"+prev_hash+"\nHash:"+curr_hash+"\nNonce:"+(int) Math.round(1000 + Math.random()*3000));
            String h = null;
            try {
                h = toHexString(getSHA("genesis block 0"));
                h = h.substring(0,20)+"...";
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            tv1.setText("Genesis Block:\nHash:"+h+"...\nNonce:"+(int) Math.round(1000 + Math.random()*3000));
            gen_block = "Genesis Block:\nHash:"+h+"...\nNonce:"+(int) Math.round(1000 + Math.random()*3000);

            int n1 = (int) Math.round(1000 + Math.random()*4000);
            int n2 = (int) Math.round(1000 + Math.random()*4000);

            tv2.setText("Block#1:\nPrevious Hash:"+h+"\nHash:"+al.get(0).split(" ")[1]+"\nFrom:"+al.get(0).split(" ")[2]+"\nTo:"+al.get(0).split(" ")[3]+"\nAmount:"+al.get(0).split(" ")[4]+"\nNonce:"+n1);
            blockchain.add("Block#1:\nPrevious Hash:"+h+"\nHash:"+al.get(0).split(" ")[1]+"\nFrom:"+al.get(0).split(" ")[2]+"\nTo:"+al.get(0).split(" ")[3]+"\nAmount:"+al.get(0).split(" ")[4]+"\nNonce:"+n1);
            chain.add(h+" "+al.get(0).split(" ")[1]+" "+al.get(0).split(" ")[2]+" "+al.get(0).split(" ")[3]+" "+al.get(0).split(" ")[4]+" "+n1);


            tv3.setText("Block#2:\nPrevious Hash:"+al.get(0).split(" ")[1]+"\nHash:"+al.get(1).split(" ")[1]+"\nFrom:"+al.get(1).split(" ")[2]+"\nTo:"+al.get(1).split(" ")[3]+"\nAmount:"+al.get(1).split(" ")[4]+"\nNonce:"+n2);
            blockchain.add("Block#2:\nPrevious Hash:"+al.get(0).split(" ")[1]+"\nHash:"+al.get(1).split(" ")[1]+"\nFrom:"+al.get(1).split(" ")[2]+"\nTo:"+al.get(1).split(" ")[3]+"\nAmount:"+al.get(1).split(" ")[4]+"\nNonce:"+n2);
            chain.add(al.get(0).split(" ")[1]+" "+al.get(1).split(" ")[1]+" "+al.get(1).split(" ")[2]+" "+al.get(1).split(" ")[3]+" "+al.get(1).split(" ")[4]+" "+n2);

            idx += 1;
            // finished setting adapter
            dialogBuilder.setView(display_startup);
            dialog = dialogBuilder.create();
            dialog.show();

            popup_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    return;
                }
            });

            back_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    popup_pending();
                }
            });

        }


    } //  end of Main_Activity
