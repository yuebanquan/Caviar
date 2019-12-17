package com.example.terminal_work.app;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import com.example.terminal_work.R;

public class wish_details extends AppCompatActivity {

    /**
     * TextView wish_name,wish_date  显示心愿的名字和登记日期的TextView
     * Button delete_wish  结束心愿按钮
     */

    private TextView wish_name, wish_date;
    Button delete_wish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_details);

        wish_name = findViewById(R.id.show_name);
        wish_date = findViewById(R.id.show_date);
        delete_wish = findViewById(R.id.delete_wish);

        wish_name.bringToFront();
        wish_date.bringToFront();
        delete_wish.bringToFront();

    }

}
